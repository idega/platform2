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

import com.idega.core.location.data.Commune;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;

/**
 * Holds most of the logic for the batchjob that creates the information that
 * is base for payment data, that is sent to external finance system.
 * 
 * @author Joakim
 * 
 * @see se.idega.idegaweb.commune.accounting.invoice.business.PaymentThreadElementarySchool
 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceChildCareTread
 * @see se.idega.idegaweb.commune.accounting.invoice.business.BillingThread
 */
public class PaymentThreadHighSchool extends PaymentThreadSchool {
//	private final static String DEFAULT_COMMUNE_NAME = "nacka_commune_name";
//	private final static String STATE_PRICE_COMMUNE_NAME = "state_price_commune_name";
	
	PaymentHeader paymentHeader;

//	protected String commune_name = null;
//	protected String state_price_name = null;
	
	public PaymentThreadHighSchool(Date month, IWContext iwc) {
		super(month, iwc);
	}

	public void run() {
		try {
			category = getSchoolCategoryHome().findHighSchoolCategory();
			categoryPosting = (ExportDataMapping) IDOLookup.getHome(ExportDataMapping.class).findByPrimaryKeyIDO(category.getPrimaryKey());

			if (getPaymentRecordHome().getCountForMonthCategoryAndStatusLH(month, category.getCategory()) == 0) {
				createBatchRunLogger(category);
				//Create all the billing info derrived from the contracts
				contracts();
				//Create all the billing info derrived from the regular payments
				regularPayment();
				//VAT
				//calcVAT();
			}
			else {
				createNewErrorMessage("invoice.severeError", "invoice.Posts_with_status_L_or_H_already_exist");
			}
		}
		catch (NotEmptyException e) {
			createNewErrorMessage("invoice.PaymentSchool", "invoice.Severe_MustFirstEmptyOldData");
			e.printStackTrace();
		}
		catch (Exception e) {
			//This is a spawned off thread, so we cannot report back errors to the
			// browser, just log them
			e.printStackTrace();
			createNewErrorMessage("invoice.severeError", "invoice.DBSetupProblem");
		}
		batchRunLoggerDone();
		BatchRunSemaphore.releaseHighRunSemaphore();
	}

	protected PostingDetail getCheck(RegulationsBusiness regBus, Collection conditions) throws RegulationException {
		PostingDetail detail = null;
		
		try {
			Commune defaultCommune = getCommuneHome().findDefaultCommune();
			ArrayList cond = new ArrayList();
			cond.addAll(conditions);
			cond.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_COMMUNE, defaultCommune.getPrimaryKey()));
			detail = regBus.getPostingDetailByOperationFlowPeriodConditionTypeRegSpecType(category.getCategory(), 
				PaymentFlowConstant.OUT, //The payment flow is out
				calculationDate, //Current date to select the correct date range
				RuleTypeConstant.DERIVED, //The conditiontype
				RegSpecConstant.CHECK, //The ruleSpecType shall be Check
				cond, //The conditions that need to fulfilled
				0, //Sent in to be used for "Specialutrakning"
				null); //Sent in to be used for "Specialutrakning"
		}
		catch (Exception e) {
			detail = null;
		}

		if (detail == null) {
			try {
				Commune homeCommune = currentProvider.getSchool().getCommune();
				ArrayList cond = new ArrayList();
				cond.addAll(conditions);
				cond.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_COMMUNE, homeCommune.getPrimaryKey()));
				detail = regBus.getPostingDetailByOperationFlowPeriodConditionTypeRegSpecType(category.getCategory(), 
					PaymentFlowConstant.OUT, //The payment flow is out
					calculationDate, //Current date to select the correct date range
					RuleTypeConstant.DERIVED, //The conditiontype
					RegSpecConstant.CHECK, //The ruleSpecType shall be Check
					cond, //The conditions that need to fulfilled
					0, //Sent in to be used for "Specialutrakning"
					null); //Sent in to be used for "Specialutrakning"
			}
			catch (Exception e) {
				detail = null;
			}
		}

		if (detail == null) {
			try {
				Commune stateCommune = null;
				stateCommune = getCommuneHome().findByCommuneName("Riksprislistan");
				ArrayList cond = new ArrayList();
				cond.addAll(conditions);
				cond.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_COMMUNE, stateCommune.getPrimaryKey()));
				detail = regBus.getPostingDetailByOperationFlowPeriodConditionTypeRegSpecType(category.getCategory(), 
					PaymentFlowConstant.OUT, //The payment flow is out
					calculationDate, //Current date to select the correct date range
					RuleTypeConstant.DERIVED, //The conditiontype
					RegSpecConstant.CHECK, //The ruleSpecType shall be Check
					cond, //The conditions that need to fulfilled
					0, //Sent in to be used for "Specialutrakning"
					null); //Sent in to be used for "Specialutrakning"
			}
			catch(Exception e) {
				e.printStackTrace();
				
				throw new RegulationException("reg_exp_no_results", "No regulation match conditions");
			}
		}
		
		return detail;
	}
}