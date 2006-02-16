package is.idega.idegaweb.member.isi.block.accounting.export.business;

import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContract;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntry;
import is.idega.idegaweb.member.isi.block.accounting.export.data.CompanyBatchInformation;
import is.idega.idegaweb.member.isi.block.accounting.export.data.CompanyBatchInformationHome;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.util.IWTimestamp;

public class MasterCardFileCreation implements CreditCardFileCreation {

	public File createFile(CreditCardContract contract, Collection entries) throws IOException {
		File tempfile = File.createTempFile(contract.getContractNumber(), null);
		FileWriter writer = new FileWriter(tempfile);
		BufferedWriter bWriter = new BufferedWriter(writer);
		
		System.out.println("MasterCardFileCreation: file name = " + tempfile.getPath() + tempfile.getName());
		
		StringBuffer empty = new StringBuffer("");
		for (int i = 0; i < 60; i++) {
			empty.append("          ");
		}		
		
		IWTimestamp now = IWTimestamp.RightNow();
		
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(0);
		format.setMinimumFractionDigits(0);
		format.setMinimumIntegerDigits(7);
		format.setMaximumIntegerDigits(7);
		format.setGroupingUsed(false);
		NumberFormat format2 = NumberFormat.getInstance();
		format2.setMaximumFractionDigits(0);
		format2.setMinimumFractionDigits(0);
		format2.setMinimumIntegerDigits(5);
		format2.setMaximumIntegerDigits(5);
		format2.setGroupingUsed(false);
		NumberFormat format3 = NumberFormat.getInstance();
		format3.setMaximumFractionDigits(0);
		format3.setMinimumFractionDigits(0);
		format3.setMinimumIntegerDigits(11);
		format3.setMaximumIntegerDigits(11);
		format3.setGroupingUsed(false);
		NumberFormat format4 = NumberFormat.getInstance();
		format4.setMaximumFractionDigits(0);
		format4.setMinimumFractionDigits(0);
		format4.setMinimumIntegerDigits(13);
		format4.setMaximumIntegerDigits(13);
		format4.setGroupingUsed(false);

		int entryCounter = 1;
		
		//H1
		bWriter.write("H1");
		bWriter.write("10");
		bWriter.write(format.format(entryCounter));
		bWriter.write(now.getDateString("yyyyMMdd"));
		bWriter.write(now.getDateString("hhmmss"));
		bWriter.write(contract.getContractNumber());
		//@Todo check for multiple entries pr. day pr. contract
		bWriter.write("001");
		bWriter.write("TEST"); //bWriter.write("PROD");
		bWriter.write(empty.substring(0, 1));
		bWriter.write(empty.substring(0, 20));
		bWriter.write(empty.substring(0, 100));
		bWriter.write(empty.substring(0, 100));
		entryCounter++;
		bWriter.newLine();
		
		//B1
		bWriter.write("B1");
		bWriter.write("10");
		bWriter.write(format.format(entryCounter));
		bWriter.write(contract.getContractNumber());
		String batchNumber = getBatchNumber(contract);
		bWriter.write(batchNumber);
		bWriter.write(empty.substring(0, 8));
		bWriter.write(empty.substring(0, 4));
		bWriter.write(empty.substring(0, 4));
		bWriter.write(empty.substring(0, 5));
		bWriter.write("ISK");
		bWriter.write(empty.substring(0, 25));
		entryCounter++;
		bWriter.newLine();

		int countDebit = 0;
		int countKredit = 0;
		double amountDebit = 0.0d;
		double amountKredit = 0.0d;
		
		//F1
		Iterator it = entries.iterator();
		while (it.hasNext()) {
			FinanceEntry entry = (FinanceEntry) it.next();
			bWriter.write("F1");
			bWriter.write("10");
			bWriter.write(format.format(entryCounter));
			bWriter.write(contract.getContractNumber());
			bWriter.write(batchNumber);
			bWriter.write(empty.substring(0, 8));
			String cardNumber = entry.getPaymentContract().getCardNumber();
			if (cardNumber.length() < 19) {
				StringBuffer p = new StringBuffer(cardNumber);
				while (p.length() < 19)
					p.insert(0, '0');
				cardNumber = p.toString();
			} else if (cardNumber.length() > 19) {
				cardNumber = cardNumber.substring(0, 19);
			}
			bWriter.write(cardNumber);
			bWriter.write(new IWTimestamp(entry.getDateOfEntry()).getDateString("yyyyMMdd"));
			bWriter.write(new IWTimestamp(entry.getDateOfEntry()).getDateString("hhmmss"));
			//@Todo what is this
			bWriter.write("0001");
			double amount = entry.getAmount();
			boolean isNegative = false;
			if (amount < 0.0d) {
				isNegative = true;
				amount = Math.abs(amount);
				countKredit++;
				amountKredit += amount;
			} else {
				countDebit++;
				amountDebit += amount;
			}
			bWriter.write(format3.format(amount * 100));
			bWriter.write("ISK");
			bWriter.write("T");
			bWriter.write("@");
			bWriter.write("B");
			if (isNegative) {
				bWriter.write("09");
			} else {
				bWriter.write("01");
			}
			bWriter.write(empty.substring(0, 1));
			bWriter.write(empty.substring(0, 1));
			bWriter.write(empty.substring(0, 6));
			bWriter.write(empty.substring(0, 7));
			entryCounter++;
			bWriter.newLine();			
		}

		//R1
		bWriter.write("R1");
		bWriter.write("10");
		bWriter.write(format.format(entryCounter));
		bWriter.write(contract.getContractNumber());
		bWriter.write(batchNumber);
		bWriter.write(now.getDateString("yyyyMMdd"));
		bWriter.write("ISK");
		bWriter.write(format2.format(countKredit));
		bWriter.write(format2.format(countDebit));
		bWriter.write(format3.format(amountKredit * 100));
		bWriter.write(format3.format(amountDebit * 100));
		bWriter.write(format.format(entryCounter - 1));
		entryCounter++;
		bWriter.newLine();
				
		//T3
		bWriter.write("T3");
		bWriter.write("10");
		bWriter.write(format.format(entryCounter));
		bWriter.write(now.getDateString("yyyyMMdd"));
		bWriter.write(now.getDateString("hhmmss"));
		bWriter.write(contract.getContractNumber());
		//@Todo check for multiple entries pr. day pr. contract
		bWriter.write("001");
		bWriter.write("TEST"); //bWriter.write("PROD");
		bWriter.write(format.format(countKredit));
		bWriter.write(format.format(countDebit));
		bWriter.write(format4.format(amountKredit * 100));
		bWriter.write(format4.format(amountDebit * 100));
		bWriter.write(format2.format(1));
		bWriter.write(format.format(entryCounter));
		bWriter.newLine();
		
		bWriter.close();
		
		return tempfile;
	}
	
	private String getBatchNumber(CreditCardContract contract) {
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(0);
		format.setMinimumFractionDigits(0);
		format.setMinimumIntegerDigits(7);
		format.setMaximumIntegerDigits(7);
		format.setGroupingUsed(false);

		CompanyBatchInformation info = null;
		int batchNr = 1;
		try {
			info = ((CompanyBatchInformationHome) IDOLookup.getHome(CompanyBatchInformation.class)).findByPrimaryKey(contract.getContractNumber());
			batchNr = info.getBatchNumber();
			info.setBatchNumber(batchNr + 1);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			try {
				info = ((CompanyBatchInformationHome) IDOLookup.getHome(CompanyBatchInformation.class)).create();
				info.setCompanyNumber(contract.getContractNumber());
				info.setBatchNumber(2);
				info.store();
			} catch (IDOLookupException e1) {
				e1.printStackTrace();
			} catch (CreateException e1) {
				e1.printStackTrace();
			}			
		}
				
		return format.format(batchNr);
	}
}