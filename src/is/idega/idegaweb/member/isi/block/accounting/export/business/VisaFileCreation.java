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

public class VisaFileCreation implements CreditCardFileCreation {

	public File createFile(CreditCardContract contract, Collection entries) throws IOException {
		File tempfile = File.createTempFile("bat", null);
		FileWriter writer = new FileWriter(tempfile);
		BufferedWriter bWriter = new BufferedWriter(writer);
		
		StringBuffer empty = new StringBuffer("");
		for (int i = 0; i < 60; i++) {
			empty.append("          ");
		}		
		
		IWTimestamp now = IWTimestamp.RightNow();
		
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(0);
		format.setMinimumFractionDigits(0);
		format.setMinimumIntegerDigits(11);
		format.setMaximumIntegerDigits(11);
		format.setGroupingUsed(false);
		NumberFormat format2 = NumberFormat.getInstance();
		format2.setMaximumFractionDigits(0);
		format2.setMinimumFractionDigits(0);
		format2.setMinimumIntegerDigits(9);
		format2.setMaximumIntegerDigits(9);
		format2.setGroupingUsed(false);

		//Styrifaersla
		bWriter.write("1401");
		bWriter.write("VI");
		//bWriter.write(now.getDateString("yyMMdd"));
		bWriter.write(empty.substring(0, 6));
		bWriter.write("//");
		bWriter.write("VISA ISLAND");
		bWriter.write(empty.substring(0, 55));
		bWriter.newLine();
		
		double total = 0.0d;
		
		Iterator it = entries.iterator();
		while (it.hasNext()) {
			FinanceEntry entry = (FinanceEntry) it.next();
			total += entry.getAmount();
		}

		bWriter.write("1401");
		bWriter.write("87");
		bWriter.write(contract.getContractNumber());
		bWriter.write(contract.getPersonalId());
		bWriter.write(empty.substring(0, 1));
		bWriter.write("08");
		bWriter.write(format.format(total * 100));
		bWriter.write(getBatchNumber(contract, now));
		bWriter.write(now.getDateString("ddMM"));
		bWriter.write(now.getDateString("ddMM"));
		bWriter.write(empty.substring(0, 30));
		bWriter.newLine();
		
		it = entries.iterator();
		int counter = 0;
		while (it.hasNext()) {
			counter++;
			FinanceEntry entry = (FinanceEntry) it.next();
			bWriter.write("1401");
			bWriter.write("87");
			String cardNumber = entry.getPaymentContract().getCardNumber();
			if (cardNumber.length() < 16) {
				StringBuffer p = new StringBuffer(cardNumber);
				while (p.length() < 16)
					p.insert(0, '0');
				cardNumber = p.toString();
			} else if (cardNumber.length() > 16) {
				cardNumber = cardNumber.substring(0, 16);
			}
			bWriter.write(cardNumber);
			bWriter.write("05");
			bWriter.write(empty.substring(0, 2));
			bWriter.write(format2.format(entry.getAmount() * 100));
			bWriter.write(getEntryNumber(contract, now, counter));
			bWriter.write(new IWTimestamp(entry.getDateOfEntry()).getDateString("ddMM"));
			bWriter.write(empty.substring(0, 5));
			bWriter.write(empty.substring(0, 28));
			bWriter.newLine();
		}
		
		bWriter.close();
		
		return tempfile;
	}
	
	private String getEntryNumber(CreditCardContract contract, IWTimestamp now, int counter) {
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(0);
		format.setMinimumFractionDigits(0);
		format.setMinimumIntegerDigits(4);
		format.setMaximumIntegerDigits(4);
		format.setGroupingUsed(false);

		StringBuffer number = new StringBuffer();
		number.append(contract.getCompanyNumber());
		
		int month = now.getMonth();
		if (month == 10) {
			month = 0;
		} else if (month == 11) {
			month = 5;
		} else if (month == 12) {
			month = 6;
		}
		
		number.append(month);
		number.append(format.format(counter));
		
		return number.toString();
	}
	
	private String getBatchNumber(CreditCardContract contract, IWTimestamp now) {
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(0);
		format.setMinimumFractionDigits(0);
		format.setMinimumIntegerDigits(3);
		format.setMaximumIntegerDigits(3);
		format.setGroupingUsed(false);

		StringBuffer number = new StringBuffer();
		number.append(contract.getCompanyNumber());
		number.append(now.getDateString("MM"));
	
		CompanyBatchInformation info = null;
		int seq = 0;
		try {
			info = ((CompanyBatchInformationHome) IDOLookup.getHome(CompanyBatchInformation.class)).findByPrimaryKey(contract.getCompanyNumber());
			String month = info.getBatchMonth();
			if (month.equals(now.getDateString("MM"))) {
				seq = info.getBatchNumber();
				seq++;
				info.setBatchNumber(seq);
				info.store();
			} else {
				seq = 1;
				info.setBatchMonth(now.getDateString("MM"));
				info.setBatchNumber(seq);
				info.store();
			}
			
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			try {
				info = ((CompanyBatchInformationHome) IDOLookup.getHome(CompanyBatchInformation.class)).create();
				info.setCompanyNumber(contract.getCompanyNumber());
				info.setBatchMonth(now.getDateString("MM"));
				info.setBatchNumber(1);
				info.store();
				seq = 1;
			} catch (IDOLookupException e1) {
				e1.printStackTrace();
			} catch (CreateException e1) {
				e1.printStackTrace();
			}			
		}
		
		number.append(format.format(seq));
		
		return number.toString();
	}
}