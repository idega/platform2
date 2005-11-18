package se.idega.idegaweb.commune.adulteducation.accounting.business;

import java.sql.Date;

import se.idega.idegaweb.commune.accounting.invoice.business.BatchRunQueue;
import se.idega.idegaweb.commune.accounting.invoice.business.BatchRunSemaphore;

import com.idega.block.school.data.School;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;

public class PaymentThreadAdultEducationHighSchool extends
		PaymentThreadAdultEducation {

	final static int SCHOOL_TYPE_ID = 67;
	
	public PaymentThreadAdultEducationHighSchool(Date month, IWContext iwc, School school, boolean testRun) {
		super(month, iwc, school, testRun);
	}

	public PaymentThreadAdultEducationHighSchool(Date month, IWContext iwc) {
		super(month, iwc);
	}
	
	public void run() {
		try {
			category = getSchoolCategoryHome().findAdultEducationCategory();
			categoryPosting = getCategoryPosting();
			parameters = getBatchParameters(SCHOOL_TYPE_ID);
			if (parameters == null || !parameters.getIncludedInBatch()) {
				return;
			}
			schoolType = getSchoolType(SCHOOL_TYPE_ID);
			IWTimestamp calculationDateTimestamp = new IWTimestamp(calculationDate);
			periodFrom = new IWTimestamp(parameters.getBatchFromDate(), calculationDateTimestamp.getMonth(), calculationDateTimestamp.getYear());
			periodFrom.addMonths(-1);
			periodTo = new IWTimestamp(parameters.getBatchToDate(), calculationDateTimestamp.getMonth(), calculationDateTimestamp.getYear());

//			if (getPaymentRecordHome().getCountForMonthCategoryAndStatusLH(month, category.getCategory()) == 0) {
				createBatchRunLogger(category);
				removePreliminaryInformation(month, category.getCategory());
				// Create all the billing info derrived from the contracts
				placements();
				// Create all the billing info derrived from the regular
				// payments
				//regularPayment();
/*			}
			else {
				createNewErrorMessage(getLocalizedString("invoice.severeError", "Severe error"), getLocalizedString(
						"invoice.Posts_with_status_L_or_H_already_exist", "Posts with status L or H already exist"));
			}*/
		}
/*		catch (NotEmptyException e) {
			createNewErrorMessage(getLocalizedString("invoice.PaymentSchool", "Payment school"), getLocalizedString(
					"invoice.Severe_MustFirstEmptyOldData", "Severe. Must first empty old data"));
			e.printStackTrace();
		}*/
		catch (Exception e) {
			// This is a spawned off thread, so we cannot report back errors to
			// the browser, just log them
			e.printStackTrace();
			createNewErrorMessage(getLocalizedString("invoice.severeError", "Severe error"), getLocalizedString(
					"invoice.DBSetupProblem", "Database setup problem"));
		}
		batchRunLoggerDone();
		BatchRunSemaphore.releaseAdultRunSemaphore();
		BatchRunQueue.BatchRunDone();
	}	
}