package se.idega.idegaweb.commune.accounting.invoice.business;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

import se.idega.idegaweb.commune.accounting.export.data.ExportDataMapping;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader;
import se.idega.idegaweb.commune.accounting.regulations.business.PaymentFlowConstant;
import se.idega.idegaweb.commune.accounting.regulations.business.RegSpecConstant;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationException;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness;
import se.idega.idegaweb.commune.accounting.regulations.business.RuleTypeConstant;
import se.idega.idegaweb.commune.accounting.regulations.data.ConditionParameter;
import se.idega.idegaweb.commune.accounting.regulations.data.PostingDetail;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.core.location.data.Commune;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;

/**
 * Holds most of the logic for the batchjob that creates the information that is
 * base for payment data, that is sent to external finance system.
 * 
 * @author Joakim
 * 
 * @see se.idega.idegaweb.commune.accounting.invoice.business.PaymentThreadHighSchool
 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceChildCareTread
 * @see se.idega.idegaweb.commune.accounting.invoice.business.BillingThread
 */
public class PaymentThreadElementarySchool extends PaymentThreadSchool {

	PaymentHeader paymentHeader;

	public PaymentThreadElementarySchool(Date month, IWContext iwc, School school, boolean testRun) {
		super(month, iwc, school, testRun);
	}

	public PaymentThreadElementarySchool(Date month, IWContext iwc) {
		super(month, iwc);
	}

	public void run() {
		try {
			category = getSchoolCategoryHome().findElementarySchoolCategory();
			categoryPosting = (ExportDataMapping) IDOLookup.getHome(ExportDataMapping.class).findByPrimaryKeyIDO(
					category.getPrimaryKey());

			if (getPaymentRecordHome().getCountForMonthCategoryAndStatusLH(month, category.getCategory()) == 0) {
				createBatchRunLogger(category);
				removePreliminaryInformation(month, category.getCategory());
				// Create all the billing info derrived from the contracts
				contracts();
				// Create all the billing info derrived from the regular
				// payments
				regularPayment();
			}
			else {
				createNewErrorMessage(getLocalizedString("invoice.severeError", "Severe error"), getLocalizedString(
						"invoice.Posts_with_status_L_or_H_already_exist", "Posts with status L or H already exist"));
			}
		}
		catch (NotEmptyException e) {
			createNewErrorMessage(getLocalizedString("invoice.PaymentSchool", "Payment school"), getLocalizedString(
					"invoice.Severe_MustFirstEmptyOldData", "Severe. Must first empty old data"));
			e.printStackTrace();
		}
		catch (Exception e) {
			// This is a spawned off thread, so we cannot report back errors to
			// the browser, just log them
			e.printStackTrace();
			createNewErrorMessage(getLocalizedString("invoice.severeError", "Severe error"), getLocalizedString(
					"invoice.DBSetupProblem", "Database setup problem"));
		}
		batchRunLoggerDone();
		BatchRunSemaphore.releaseElementaryRunSemaphore();
		BatchRunQueue.BatchRunDone();
	}

	protected PostingDetail getCheck(RegulationsBusiness regBus, Collection conditions, SchoolClassMember placement)
			throws RegulationException {
		PostingDetail detail = null;

		try {
			Commune homeCommune = currentProvider.getSchool().getCommune();
			ArrayList cond = new ArrayList();
			cond.addAll(conditions);
			cond.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_COMMUNE, homeCommune.getPrimaryKey()));
			detail = regBus.getPostingDetailByOperationFlowPeriodConditionTypeRegSpecType(category.getCategory(),
					PaymentFlowConstant.OUT, // The payment flow is out
					calculationDate, // Current date to select the correct
										// date range
					RuleTypeConstant.DERIVED, // The conditiontype
					RegSpecConstant.CHECK, // The ruleSpecType shall be Check
					cond, // The conditions that need to fulfilled
					0, // Sent in to be used for "Specialutrakning"
					null, // Contract not used here
					placement); // Sent in to be used for e.g. VAT calculations
		}
		catch (Exception e) {
			e.printStackTrace();

			throw new RegulationException("reg_exp_no_results", "No regulation match conditions");
		}

		return detail;
	}
}