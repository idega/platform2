package se.idega.idegaweb.commune.accounting.invoice.business;

import java.sql.Date;

import se.idega.idegaweb.commune.accounting.export.data.ExportDataMapping;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader;

import com.idega.block.school.data.School;
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
	
	public PaymentThreadElementarySchool(Date month, IWContext iwc, School school, boolean testRun){
		super(month, iwc, school, testRun);
	}
	
	public PaymentThreadElementarySchool(Date month, IWContext iwc){
		super(month, iwc);
	}	
	
	public void run(){
		try {
			category = getSchoolCategoryHome().findElementarySchoolCategory();
			categoryPosting = (ExportDataMapping) IDOLookup.getHome(ExportDataMapping.class).
					findByPrimaryKeyIDO(category.getPrimaryKey());
			
			if(getPaymentRecordHome().getCountForMonthCategoryAndStatusLH(month,category.getCategory()) == 0){
				createBatchRunLogger(category);
				removePreliminaryInformation(month, category.getCategory());
				//Create all the billing info derrived from the contracts
				contracts();
				//Create all the billing info derrived from the regular payments
				regularPayment();
			}else{
				createNewErrorMessage(getLocalizedString("invoice.severeError","Severe error"),getLocalizedString("invoice.Posts_with_status_L_or_H_already_exist","Posts with status L or H already exist"));
			}
		} catch (NotEmptyException e) {
			createNewErrorMessage(getLocalizedString("invoice.PaymentSchool","Payment school"), getLocalizedString("invoice.Severe_MustFirstEmptyOldData","Severe. Must first empty old data"));
			e.printStackTrace();
		} catch (Exception e) {
			//This is a spawned off thread, so we cannot report back errors to the browser, just log them
			e.printStackTrace();
			createNewErrorMessage(getLocalizedString("invoice.severeError","Severe error"),getLocalizedString("invoice.DBSetupProblem","Database setup problem"));
		}
		batchRunLoggerDone();
		BatchRunSemaphore.releaseElementaryRunSemaphore();
		BatchRunQueue.BatchRunDone();
	}
}
