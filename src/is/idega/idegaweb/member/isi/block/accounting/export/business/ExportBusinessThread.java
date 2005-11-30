/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */

package is.idega.idegaweb.member.isi.block.accounting.export.business;

import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContract;
import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractHome;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntry;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryHome;
import is.idega.idegaweb.member.isi.block.accounting.data.PaymentType;
import is.idega.idegaweb.member.isi.block.accounting.data.PaymentTypeHome;
import is.idega.idegaweb.member.isi.block.accounting.export.data.Batch;
import is.idega.idegaweb.member.isi.block.accounting.export.data.BatchHome;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.finance.data.BankInfo;
import com.idega.block.finance.data.BankInfoHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.util.IWTimestamp;

/**
 * @author palli
 * 
 */
public class ExportBusinessThread extends Thread {

	private boolean isExport = false;

	private String dateFromString = null;

	private String dateToString = null;

	public ExportBusinessThread(String dateFrom, String dateTo, boolean isExport) {
		dateFromString = dateFrom;
		dateToString = dateTo;
		this.isExport = isExport;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		if (isExport) {
			IWTimestamp dateFrom = null;
			IWTimestamp dateTo = null;

			try {
				if (dateFromString != null) {
					dateFrom = new IWTimestamp(dateFromString);
				}
			} catch (Exception e) {
				dateFrom = null;
			}

			try {
				if (dateToString != null) {
					dateTo = new IWTimestamp(dateToString);
					dateTo.addDays(1);
				}
			} catch (Exception e) {
				dateTo = null;
			}

			try {
				createBatches(dateFrom, dateTo);
				createFiles();
				sendFiles();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				BatchRunning.releaseSendFilesBatch();
			}
		} else {
			BatchRunning.releaseGetFilesBatch();
		}

	}

	private void createBatches(IWTimestamp dateFrom, IWTimestamp dateTo)
			throws IDOLookupException, FinderException, CreateException {
		FinanceEntryHome fHome = (FinanceEntryHome) IDOLookup
				.getHome(FinanceEntry.class);
		PaymentTypeHome pHome = (PaymentTypeHome) IDOLookup
				.getHome(PaymentType.class);
		PaymentType creditCard = pHome.findPaymentTypeCreditcardSystem();
		PaymentType bank = pHome.findPaymentTypeBankSystem();

		Collection creditCardEntries = fHome.findAllByPaymentTypeNotInBatch(
				creditCard, dateFrom, dateTo);
		Collection bankEntries = fHome.findAllByPaymentTypeNotInBatch(bank,
				dateFrom, dateTo);

		if (creditCardEntries != null) {
			Iterator it = creditCardEntries.iterator();
			while (it.hasNext()) {
				FinanceEntry entry = (FinanceEntry) it.next();
				CreditCardContract contract = getCreditCardContractForEntry(entry);
				if (contract != null) {
					Batch batch = getBatchForCreditCardContract(contract);
					entry.setISIBatch(batch);
					entry.store();
				}
			}
		}

		if (bankEntries != null) {
			Iterator it = bankEntries.iterator();
			while (it.hasNext()) {
				FinanceEntry entry = (FinanceEntry) it.next();
				BankInfo info = getBankInfoForEntry(entry);
				if (info != null) {
					Batch batch = getBatchForBankInfo(info);
					entry.setISIBatch(batch);
					entry.store();
				}
			}
		}
	}

	private CreditCardContract getCreditCardContractForEntry(FinanceEntry entry)
			throws IDOLookupException {
		if (entry.getPaymentContract() == null) {
			return null;
		}

		CreditCardContract ret = null;

		CreditCardContractHome cHome = (CreditCardContractHome) IDOLookup
				.getHome(CreditCardContract.class);
		try {
			ret = (CreditCardContract) cHome.findByGroupAndType(entry
					.getGroup(), entry.getPaymentContract().getCardType());
		} catch (FinderException e) {
			try {
				ret = (CreditCardContract) cHome.findByDivisionAndType(entry
						.getDivision(), entry.getPaymentContract()
						.getCardType());
			} catch (FinderException e1) {
				try {
					ret = (CreditCardContract) cHome.findByClubAndType(entry
							.getClub(), entry.getPaymentContract()
							.getCardType());
				} catch (FinderException e2) {
					// Add to some kind of log?
					ret = null;
				}
			}
		}

		return ret;
	}

	private Batch getBatchForCreditCardContract(CreditCardContract contract)
			throws IDOLookupException, CreateException {
		BatchHome bHome = (BatchHome) IDOLookup.getHome(Batch.class);
		Batch ret = null;
		try {
			ret = bHome.findUnsentByContractAndCreditCardType(contract,
					contract.getCardType());
		} catch (FinderException e) {
			ret = bHome.create();
			ret.setBatchNumber("");
			ret.setCreated(IWTimestamp.getTimestampRightNow());
			ret.setTypeCreditCard();
			ret.setCreditCardContract(contract);
			ret.setCreditCardType(contract.getCardType());
			ret.store();
		}

		return ret;
	}

	private BankInfo getBankInfoForEntry(FinanceEntry entry)
			throws IDOLookupException {
		BankInfo ret = null;

		BankInfoHome bHome = (BankInfoHome) IDOLookup.getHome(BankInfo.class);
		try {
			ret = (BankInfo) bHome.findByGroup(entry.getGroup());
		} catch (FinderException e) {
			try {
				ret = (BankInfo) bHome.findByDivision(entry.getDivision());
			} catch (FinderException e1) {
				try {
					ret = (BankInfo) bHome.findByClub(entry.getClub());
				} catch (FinderException e2) {
					// Add to some kind of log?
					ret = null;
				}
			}
		}

		return ret;
	}

	private Batch getBatchForBankInfo(BankInfo info) throws IDOLookupException,
			CreateException {
		BatchHome bHome = (BatchHome) IDOLookup.getHome(Batch.class);
		Batch ret = null;
		try {
			ret = bHome.findUnsentByBankInfo(info);
		} catch (FinderException e) {
			ret = bHome.create();
			ret.setBatchNumber("");
			ret.setCreated(IWTimestamp.getTimestampRightNow());
			ret.setTypeBank();
			ret.setBankInfo(info);
			ret.store();
		}

		return ret;
	}

	private void createFiles() {
		BatchHome bHome;
		try {
			bHome = (BatchHome) IDOLookup.getHome(Batch.class);
			Collection batchesWithoutFiles = bHome.findAllWithoutFiles();
			Iterator it = batchesWithoutFiles.iterator();
			while (it.hasNext()) {
				Batch batch = (Batch) it.next();
				if (batch.getIsBankType()) {
					createBankFileForBatch(batch);
				} else {
					createCreditcardFileForBatch(batch);
				}
			}
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	private void createBankFileForBatch(Batch batch) {
		try {
			FinanceEntryHome fHome = (FinanceEntryHome) IDOLookup.getHome(FinanceEntry.class);
			Collection entriesInBatch = fHome.findAllByBatch(batch);

			com.idega.block.finance.data.BatchHome finBatchHome = (com.idega.block.finance.data.BatchHome) IDOLookup.getHome(com.idega.block.finance.data.Batch.class);
			com.idega.block.finance.data.Batch finBatch = finBatchHome.create();
			finBatch.setBatchNumber(batch.getBatchNumber());
			finBatch.setCreated(IWTimestamp.getTimestampRightNow());
			finBatch.store();
			
			Iterator it = entriesInBatch.iterator();
			while (it.hasNext()) {
				FinanceEntry entry = (FinanceEntry) it.next();
				
			}
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	private void createCreditcardFileForBatch(Batch batch) {
		try {
			File tempfile = File.createTempFile("bat",null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sendFiles() {
		BatchHome bHome;
		try {
			bHome = (BatchHome) IDOLookup.getHome(Batch.class);
			Collection unsentBatches = bHome.findAllUnsent();
			Iterator it = unsentBatches.iterator();
			while (it.hasNext()) {
				Batch batch = (Batch) it.next();
				if (batch.getIsBankType()) {
					sendBankBatch(batch);
				} else {
					sendCreditcardBatch(batch);
				}
			}
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	private void sendBankBatch(Batch batch) {
		
	}
	
	private void sendCreditcardBatch(Batch batch) {
		
	}
}