package se.idega.idegaweb.commune.accounting.invoice.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

import se.idega.idegaweb.commune.accounting.export.data.ExportDataMapping;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader;
import se.idega.idegaweb.commune.accounting.posting.business.PostingException;
import se.idega.idegaweb.commune.accounting.regulations.business.PaymentFlowConstant;
import se.idega.idegaweb.commune.accounting.regulations.business.RegSpecConstant;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationException;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness;
import se.idega.idegaweb.commune.accounting.regulations.business.RuleTypeConstant;
import se.idega.idegaweb.commune.accounting.regulations.data.ConditionParameter;
import se.idega.idegaweb.commune.accounting.regulations.data.PostingDetail;
import se.idega.idegaweb.commune.accounting.school.data.Provider;

import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolStudyPathHome;
import com.idega.block.school.data.SchoolType;
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
		BatchRunQueue.BatchRunDone();
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
					null, //Contract not used here
					placement); //Sent in to be used for e.g. VAT calculations
			}
			catch(Exception e) {
				e.printStackTrace();
				
				throw new RegulationException("reg_exp_no_results", "No regulation match conditions");
			}
		}
		
		return detail;
	}	
	
	protected String[] getPostingStrings(SchoolCategory category, SchoolType schoolType, int regSpecTypeId, Provider provider, Date calculationDate, int schoolYearId, int studyPathId) throws PostingException, RemoteException {
		if (studyPathId != -1){
			try {
				return getPostingBusiness().getPostingStrings(category, schoolType, regSpecTypeId, provider, calculationDate,schoolYearId, studyPathId, false);
			} catch (PostingException e) {
				return getPostingBusiness().getPostingStrings(category, schoolType, regSpecTypeId, provider, calculationDate,schoolYearId, -1, true);
			} 
		}else {
			return getPostingBusiness().getPostingStrings(category, schoolType, regSpecTypeId, provider, calculationDate, schoolYearId);
		}
	}
	
	/**
	 * Adds to the condition ArrayList according to the schoolClassMemeber
	 * Default is no change. Overridden by PymentThreadHighSchool
	 * 
	 * @param schoolClassMember
	 * @param conditions
	 */
	protected void setStudyPath(SchoolClassMember schoolClassMember, ArrayList conditions){
		int studyPathId = schoolClassMember.getStudyPathId();
		if (studyPathId != -1) {
			conditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_STUDY_PATH, new Integer(studyPathId)));
			try {
				SchoolStudyPath schoolStudyPath = ((SchoolStudyPathHome) IDOLookup.getHome(SchoolStudyPath.class)).findByPrimaryKey(new Integer(schoolClassMember.getStudyPathId()));
				errorRelated.append("Study path code " + schoolStudyPath.getCode());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
		 createNewErrorMessage(errorRelated, "invoice.StudypathMissing");
		 }
	}
}