package se.idega.idegaweb.commune.accounting.invoice.business;

import java.sql.Date;

import se.idega.idegaweb.commune.accounting.export.data.ExportDataMapping;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;

/**
 * @author Joakim
 *
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
			//Create all the billing info derrived from the contracts
			contracts();
			//Create all the billing info derrived from the regular payments
			regularPayment();
			//VAT
			calcVAT();
		} catch (Exception e) {
			//This is a spawned off thread, so we cannot report back errors to the browser, just log them
			e.printStackTrace();
			createNewErrorMessage("invoice.severeError","invoice.DBSetupProblem");
		}
	}
}
