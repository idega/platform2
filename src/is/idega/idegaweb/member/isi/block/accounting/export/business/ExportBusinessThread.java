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
import is.idega.idegaweb.member.isi.block.accounting.export.data.Configuration;
import is.idega.idegaweb.member.isi.block.accounting.export.data.ConfigurationHome;
import is.idega.idegaweb.member.isi.block.accounting.export.data.RunLog;
import is.idega.idegaweb.member.isi.block.accounting.export.data.RunLogEntry;
import is.idega.idegaweb.member.isi.block.accounting.export.data.RunLogEntryHome;
import is.idega.idegaweb.member.isi.block.accounting.export.data.RunLogHome;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.finance.business.InvoiceDataInsert;
import com.idega.block.finance.data.AccountEntry;
import com.idega.block.finance.data.AccountEntryHome;
import com.idega.block.finance.data.BankInfo;
import com.idega.block.finance.data.BankInfoHome;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
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

	private RunLog log = null;

	private String userName = null;

	public ExportBusinessThread(String dateFrom, String dateTo,
			boolean isExport, String userName) {
		dateFromString = dateFrom;
		dateToString = dateTo;
		this.isExport = isExport;
		this.userName = userName;
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
				createLog();
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

	private void createLog() throws IDOLookupException, CreateException {
		RunLogHome rHome = (RunLogHome) IDOLookup.getHome(RunLog.class);
		log = rHome.create();
		log.setCreatedBy(userName);
		log.setCreatedDate(IWTimestamp.getTimestampRightNow());
		log.store();
	}

	private void createLogEntry(String entryText, boolean isError) {
		try {
			RunLogEntryHome eHome = (RunLogEntryHome) IDOLookup
					.getHome(RunLogEntry.class);
			RunLogEntry entry = eHome.create();
			entry.setDateOfEntry(IWTimestamp.getTimestampRightNow());
			entry.setEntry(entryText);
			entry.setIsError(isError);
			entry.setRunLog(log);
			entry.store();
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
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
		} catch (Exception e) {
			try {
				ret = (CreditCardContract) cHome.findByDivisionAndType(entry
						.getDivision(), entry.getPaymentContract()
						.getCardType());
			} catch (Exception e1) {
				try {
					ret = (CreditCardContract) cHome.findByClubAndType(entry
							.getClub(), entry.getPaymentContract()
							.getCardType());
				} catch (Exception e2) {
					createLogEntry(e.getMessage(), true);
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
		} catch (Exception e) {
			try {
				ret = (BankInfo) bHome.findByDivision(entry.getDivision());
			} catch (Exception e1) {
				try {
					ret = (BankInfo) bHome.findByClub(entry.getClub());
				} catch (Exception e2) {
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
			FinanceEntryHome fHome = (FinanceEntryHome) IDOLookup
					.getHome(FinanceEntry.class);
			Collection entriesInBatch = fHome.findAllByBatch(batch);

			com.idega.block.finance.data.BatchHome finBatchHome = (com.idega.block.finance.data.BatchHome) IDOLookup
					.getHome(com.idega.block.finance.data.Batch.class);
			com.idega.block.finance.data.Batch finBatch = finBatchHome.create();
			finBatch.setBatchNumber(batch.getBatchNumber());
			finBatch.setCreated(IWTimestamp.getTimestampRightNow());
			finBatch.store();

			batch.setFinBatch(finBatch);
			batch.store();

			Iterator it = entriesInBatch.iterator();
			while (it.hasNext()) {
				FinanceEntry entry = (FinanceEntry) it.next();
				AccountEntry accEntry = ((AccountEntryHome) IDOLookup
						.getHome(AccountEntry.class)).create();
				accEntry.setInvoiceNumber((Integer) entry.getPrimaryKey());
				// accEntry.setAccountBook(entry.geta);
				accEntry.setDueDate(entry.getDueDate());
				accEntry.setTotal((float) entry.getAmount());
				IWTimestamp fdd = new IWTimestamp(entry.getFinalDueDate());
				accEntry.setFinalDueDate(fdd.getTimestamp());
				accEntry.setUserId(entry.getPayedByUserID());
				accEntry.setBatchNumber(((Integer) finBatch.getPrimaryKey())
						.intValue());
				accEntry.store();
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
			Configuration conf = ((ConfigurationHome) IDOLookup
					.getHome(Configuration.class)).findByCreditcardType(batch
					.getCreditCardType());
			CreditCardFileCreation create = (CreditCardFileCreation) Class
					.forName(conf.getSendFTPFileCreationPlugin()).newInstance();

			FinanceEntryHome fHome = (FinanceEntryHome) IDOLookup
					.getHome(FinanceEntry.class);
			Collection entriesInBatch = fHome.findAllByBatch(batch);

			File creditCardFile = create.createFile(batch
					.getCreditCardContract(), entriesInBatch);

			ICFile file = ((ICFileHome) IDOLookup.getHome(ICFile.class))
					.create();
			file.setFileValue(new FileInputStream(creditCardFile));
			file.setName(creditCardFile.getName());
			file.store();
			batch.setCreditCardFile(file);
			batch.setCreditCardFileName(file.getName());
			batch.store();
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
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
		try {
			InvoiceDataInsert insert = (InvoiceDataInsert) Class.forName(
					batch.getBankInfo().getClaimantsBankBranch().getBank()
							.getPluginName()).newInstance();
			insert.createClaimsInBank(batch.getFinBatchID(), batch
					.getBankInfo());

			batch.setSent(IWTimestamp.getTimestampRightNow());
			batch.store();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendCreditcardBatch(Batch batch) {
		try {
			Configuration conf = ((ConfigurationHome) IDOLookup
					.getHome(Configuration.class)).findByCreditcardType(batch
					.getCreditCardType());
			String plugin = conf.getSendFTPFilePlugin();

			boolean sent = false;
			
			if (plugin != null && !"".equals(plugin)) {
				CreditCardSendFile send = (CreditCardSendFile) Class
						.forName(plugin)
						.newInstance();

				sent = send.sendFile(conf, batch);
			}

			if (sent) {
				batch.setSent(IWTimestamp.getTimestampRightNow());
				batch.store();
			} else {
				System.out.println("Didn't send file");
			}
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}