package se.idega.idegaweb.commune.accounting.invoice.business;

import java.sql.Date;

import se.idega.idegaweb.commune.accounting.export.data.ExportDataMapping;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;

/**
 * Holds most of the logic for the batchjob that creates the information that is base for 
 * payment data, that is sent to external finance system.
 * 
 * @author Joakim
 * 
 * @see se.idega.idegaweb.commune.accounting.invoice.business.PaymentThreadHighSchool
 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceChildCareTread
 * @see se.idega.idegaweb.commune.accounting.invoice.business.BillingThread
 */
public class PaymentThreadElementarySchool extends PaymentThreadSchool{
	PaymentHeader paymentHeader;
	Date currentDate = new Date( System.currentTimeMillis());
	
	public PaymentThreadElementarySchool(Date month, IWContext iwc){
		super(month,iwc);
	}
	
	public void run(){
		try {
			category = getSchoolCategoryHome().findElementarySchoolCategory();
			categoryPosting = (ExportDataMapping) IDOLookup.getHome(ExportDataMapping.class).
					findByPrimaryKeyIDO(category.getPrimaryKey());
			
			createBatchRunLogger(category);
			//Create all the billing info derrived from the contracts
			contracts();
			System.out.println("Done with Contracts loop");
			//Create all the billing info derrived from the regular payments
			regularPayment();
			System.out.println("Done with Payment loop");
			//VAT
			calcVAT();
			System.out.println("Done with VAT loop");
			batchRunLoggerDone();
		} catch (Exception e) {
			//This is a spawned off thread, so we cannot report back errors to the browser, just log them
			e.printStackTrace();
			createNewErrorMessage("invoice.severeError","invoice.DBSetupProblem");
		}
	}
}
