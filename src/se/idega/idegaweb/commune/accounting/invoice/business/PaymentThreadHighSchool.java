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
 * @see se.idega.idegaweb.commune.accounting.invoice.business.PaymentThreadElementarySchool
 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceChildCareTread
 * @see se.idega.idegaweb.commune.accounting.invoice.business.BillingThread
 */
public class PaymentThreadHighSchool extends PaymentThreadSchool{
	PaymentHeader paymentHeader;
	Date currentDate = new Date( System.currentTimeMillis());
	
	public PaymentThreadHighSchool(Date month, IWContext iwc){
		super(month,iwc);
	}
	
	public void run(){
		try {
			category = getSchoolCategoryHome().findHighSchoolCategory();
			categoryPosting = (ExportDataMapping) IDOLookup.getHome(ExportDataMapping.class).
					findByPrimaryKeyIDO(category.getPrimaryKey());
			
			if(getPaymentRecordHome().getCountForMonthCategoryAndStatusLH(month,category.getCategory()) == 0){
				createBatchRunLogger(category);
				//Create all the billing info derrived from the contracts
				contracts();
				//Create all the billing info derrived from the regular payments
				regularPayment();
				//VAT
				calcVAT();
				batchRunLoggerDone();
			}else{
				createNewErrorMessage("invoice.severeError","invoice.Posts_with_status_L_or_H_already_exist");
				batchRunLoggerDone();
			}
		} catch (NotEmptyException e) {
			createNewErrorMessage("invoice.PaymentSchool", "invoice.Severe_MustFirstEmptyOldData");
			batchRunLoggerDone();
			e.printStackTrace();
		} catch (Exception e) {
			//This is a spawned off thread, so we cannot report back errors to the browser, just log them
			e.printStackTrace();
			createNewErrorMessage("invoice.severeError","invoice.DBSetupProblem");
			batchRunLoggerDone();
		}
	}
}
