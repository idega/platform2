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

import com.idega.block.school.data.SchoolClassMember;
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
	PaymentHeader paymentHeader;

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

	protected PostingDetail getCheck(RegulationsBusiness regBus, Collection conditions,SchoolClassMember placement) throws RegulationException {
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
				null, //Contract not used here
				placement); //Sent in to be used for e.g. VAT calculations
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
					null, //Contract not used here
					placement); //Sent in to be used for e.g. VAT calculations
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
					null, null); //Sent in to be used for "Specialutrakning"
			}
			catch(Exception e) {
				e.printStackTrace();
				
				throw new RegulationException("reg_exp_no_results", "No regulation match conditions");
			}
		}
		
		return detail;
	}
	
/*	protected Collection getRegulationForResourceArray(RegulationsBusiness regBus, SchoolClassMember schoolClassMember, ResourceClassMember resource, Provider provider) throws RemoteException {
		Collection resourceConditions = new ArrayList();
		Collection all = new ArrayList();
		errorRelated.append("SchoolType "+schoolClassMember.getSchoolType().getName());
		errorRelated.append("SchoolYear "+schoolClassMember.getSchoolYear().getName());
		errorRelated.append("StudyPath" + schoolClassMember.getStudyPathId());
		Commune defaultCommune = null;
		try {
			defaultCommune = getCommuneHome().findDefaultCommune();
		}
		catch(Exception e) {
			
		}
		errorRelated.append("Commune " + defaultCommune.getCommuneName());
		errorRelated.append("StateSubsidyGrant " + provider.getStateSubsidyGrant());
		
		resourceConditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_OPERATION, schoolClassMember.getSchoolType().getLocalizationKey()));
		resourceConditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_SCHOOL_YEAR, schoolClassMember.getSchoolYear().getName()));
		if (schoolClassMember.getStudyPathId() != -1)
			all.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_STUDY_PATH, new Integer(schoolClassMember.getStudyPathId())));
		if (provider.getSchool() != null)
			if (provider.getSchool().getCommune() != null)
				resourceConditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_COMMUNE, provider.getSchool().getCommune().getPrimaryKey()));
		resourceConditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_STADSBIDRAG, new Boolean(provider.getStateSubsidyGrant())));
		
		Collection regulationForResourceArray = null;
		
		all.addAll(resourceConditions);
		
		regulationForResourceArray = regBus.getAllRegulationsByOperationFlowPeriodConditionTypeRegSpecType(
				category.getCategory(),
				PaymentFlowConstant.OUT,
				calculationDate,
				RuleTypeConstant.DERIVED,
				RegSpecConstant.RESOURCE, 
				all
		);
		
		if (regulationForResourceArray == null || regulationForResourceArray.isEmpty()) {
			regulationForResourceArray = regBus.getAllRegulationsByOperationFlowPeriodConditionTypeRegSpecType(
					category.getCategory(),
					PaymentFlowConstant.OUT,
					calculationDate,
					RuleTypeConstant.DERIVED,
					RegSpecConstant.RESOURCE, 
					resourceConditions
			);
		}
		
		return regulationForResourceArray;
	}	*/
}