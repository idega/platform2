/*
 * $Id: RegulationsBusinessBean.java,v 1.37 2003/10/05 12:04:05 kjell Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.regulations.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import se.idega.idegaweb.commune.accounting.regulations.data.ActivityType;
import se.idega.idegaweb.commune.accounting.regulations.data.ActivityTypeHome;
import se.idega.idegaweb.commune.accounting.regulations.data.CommuneBelongingType;
import se.idega.idegaweb.commune.accounting.regulations.data.CommuneBelongingTypeHome;
import se.idega.idegaweb.commune.accounting.regulations.data.Condition;
import se.idega.idegaweb.commune.accounting.regulations.data.ConditionHome;
import se.idega.idegaweb.commune.accounting.regulations.data.ConditionType;
import se.idega.idegaweb.commune.accounting.regulations.data.ConditionTypeHome;
import se.idega.idegaweb.commune.accounting.regulations.data.MainRule;
import se.idega.idegaweb.commune.accounting.regulations.data.MainRuleHome;
import se.idega.idegaweb.commune.accounting.regulations.data.PaymentFlowType;
import se.idega.idegaweb.commune.accounting.regulations.data.PaymentFlowTypeHome;
import se.idega.idegaweb.commune.accounting.regulations.data.PostingDetail;
import se.idega.idegaweb.commune.accounting.regulations.data.ProviderType;
import se.idega.idegaweb.commune.accounting.regulations.data.ProviderTypeHome;
import se.idega.idegaweb.commune.accounting.regulations.data.Regulation;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationHome;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecTypeHome;
import se.idega.idegaweb.commune.accounting.regulations.data.SpecialCalculationType;
import se.idega.idegaweb.commune.accounting.regulations.data.SpecialCalculationTypeHome;
import se.idega.idegaweb.commune.accounting.regulations.data.VATRule;
import se.idega.idegaweb.commune.accounting.regulations.data.VATRuleHome;
import se.idega.idegaweb.commune.accounting.regulations.data.YesNo;
import se.idega.idegaweb.commune.accounting.regulations.data.YesNoHome;
import se.idega.idegaweb.commune.childcare.data.ChildCareContract;

import com.idega.block.school.data.SchoolManagementType;
import com.idega.block.school.data.SchoolManagementTypeHome;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.util.IWTimestamp;


/**
 * @author Kelly Lindman
 * 
 */ 
public class RegulationsBusinessBean extends com.idega.business.IBOServiceBean implements RegulationsBusiness {

	private final static String KP = "regulation_spec_type_error."; // key prefix 
	private final static String LP = "cacc_regulation."; // Localization prefex	public final static String KEY_CANNOT_DELETE_REG_SPEC_TYPE  = KP + "cannot_delete_reg_spec_type_regulation";
	public final static String DEFAULT_CANNOT_DELETE_REG_SPEC_TYPE = "Kunde inte radera regelspecifikationstypen";
	public final static String KEY_CANNOT_SAVE_REG_SPEC_TYPE  = KP + "cannot_save_reg_spec_type_regulation";
	public final static String DEFAULT_CANNOT_SAVE_REG_SPEC_TYPE = "Kunde inte spara regelspecifikationstypen";
	public final static String KEY_ERROR_PARAM_DATE_ORDER = KP + "from_date_lare_than_to_date";
	public final static String KEY_ERROR_REGULATION_CREATE = KP + "cannot_create";
	public final static String KEY_ERROR_PARAM_NAME_EMPTY = KP + "name_empty";
	public final static String KEY_ERROR_PARAM_ORDER_EMPTY = KP + "order_empty";
	public final static String KEY_GENERAL_ERROR  = KP + "general_error";
	public final static String DEFAULT_GENERAL_ERROR = "Systemfel";
	public final static String KEY_ERROR_PARAM_MAIN_OPERATION = "main_op_error";
	public final static String KEY_ERROR_PARAM_REG_SPEC_EMPTY = "reg_spec_empty";
	public final static String KEY_ERROR_PARAM_OVERLAP = "overlap_error";
	
	public String getBundleIdentifier() {
		return se.idega.idegaweb.commune.accounting.presentation.AccountingBlock.IW_ACCOUNTING_BUNDLE_IDENTIFER;
	}

 
	/**
	 * Save regulation. Saves the regultion. If non existing, creates it.
	 *  
	 * @param regID the regulation id
	 * @param periodFrom from date
	 * @param periodTo to date
	 * @param name name of this regulation
	 * @param amount
	 * @param conditionOrder
	 * @param operation
	 * @param paymentFlowType in/out 1/2
	 * @param vatEligible TAX Eligible Yes/No 1/2
	 * @param regSpecType
	 * @param conditionType
	 * @param specialCalculation
	 * @param vatRule
	 * @param changedBy  
	 * @return int of the PK ID for the created/saved regulation
	 * 
	 * @author kelly
	 */
	public int saveRegulation(
				String regID,
				Date periodeFrom, 
				Date periodeTo,
				String name,
				String amount,
				String conditionOrder,
				String operation,
				String paymentFlowType,
				String vatEligible,
				String regSpecType,
				String conditionType,
				String specialCalculation,
				String vatRule,
				String changedBy,
				String discount
		) throws RegulationException, RemoteException {

		RegulationHome home = null;
		Regulation r = null;
		int amountVal = 0;
		float discountVal = 0;
		Integer conditionOrderID = null; 
		Integer regSpecTypeID = null;  
		Integer conditionTypeID = null;  
		Integer specialCalculationID = null;  
		Integer vatRuleID = null;

		if (operation.compareTo("0") == 0) {
			throw new RegulationException(KEY_ERROR_PARAM_MAIN_OPERATION, "Huvudverksamhet måste väljas");			
		}
		
		if (periodeFrom.after(periodeTo)) {
			throw new RegulationException(KEY_ERROR_PARAM_DATE_ORDER, "Från datum kan ej vara senare än tom datum!");			
		}
		if (name.length() == 0) {
			throw new RegulationException(KEY_ERROR_PARAM_NAME_EMPTY, "Namn saknas!");			
		}
		if (conditionOrder.length() == 0) {
			throw new RegulationException(KEY_ERROR_PARAM_ORDER_EMPTY, "Villkorsordning saknas!");			
		}

		try {
			home = (RegulationHome) IDOLookup.getHome(Regulation.class);

			if (amount == null) amount = "0"; 
			if (discount == null) discount = "0"; 
			
			if (conditionOrder == null) conditionOrder = ""; 
			if (regSpecType == null) regSpecType = ""; 
			if (conditionType == null) conditionType = ""; 
			if (specialCalculation == null) specialCalculation = ""; 
			if (vatRule == null) vatRule = ""; 

			
			if (amount.length() != 0) {
				try {
					amountVal = Integer.parseInt(amount);
				} catch ( NumberFormatException e) {
					amountVal = 0;
				}
			}
			if (discount.length() != 0) {
				try {
					discountVal = Float.parseFloat(discount);
				} catch ( NumberFormatException e) {
					discountVal = 0;
				}
			}
			
			conditionOrderID = conditionOrder.length() != 0 ? new Integer(conditionOrder) : null;  
			regSpecTypeID = regSpecType.length() != 0 ? new Integer(regSpecType) : null;  
			conditionTypeID = conditionType.length() != 0 ? new Integer(conditionType) : null;  
			specialCalculationID = specialCalculation.length() != 0 ? new Integer(specialCalculation) : null;  
			vatRuleID = vatRule.length() != 0 ? new Integer(vatRule) : null;  

			int rID = 0;
			if (regID != null) {
				rID = Integer.parseInt(regID);
			}
			r = null;
			if (rID != 0) {				
				r = home.findRegulation(rID);
			}
		} catch (FinderException e) {
			r = null;
		}

		if (isRegulationOverlap(periodeFrom, periodeTo, r)) {
			throw new RegulationException(KEY_ERROR_PARAM_OVERLAP, "Överlappande perioder");			
		}
		
		try {
			if (r == null) {
				r = home.create();
			}
			r.setPeriodFrom(periodeFrom);
			r.setPeriodTo(periodeTo);
			r.setName(name);
			r.setAmount(amountVal);
			r.setDiscount(discountVal);
			r.setChangedDate(IWTimestamp.getTimestampRightNow());

			if (vatEligible != null) {
				r.setVATEligible(Integer.parseInt(vatEligible));
			}
			if (paymentFlowType != null) {
				r.setPaymentFlowType(Integer.parseInt(paymentFlowType));
			}
			if (operation != null) {
					r.setOperation(operation);
			}
			if (conditionOrderID != null) {
				r.setConditionOrder(conditionOrderID.intValue());
			}
			if (regSpecTypeID != null) {
				r.setRegSpecType(regSpecTypeID.intValue());
			}
			if (conditionTypeID != null) {
				r.setConditionType(conditionTypeID.intValue());
			}
			if(specialCalculationID != null) {
				r.setSpecialCalculation(specialCalculationID.intValue());
			}
			if (vatRuleID != null) {
				r.setVATRegulation(vatRuleID.intValue());
			}							
			if (changedBy != null) {
				r.setChangedSign(changedBy);
			}							
			r.store();
		} catch (CreateException ce) {
			throw new RegulationException(KEY_ERROR_REGULATION_CREATE, "Kan ej skapa regel");			
		}
		int id = 0;
		if (r != null) {
			id = Integer.parseInt(r.getPrimaryKey().toString());
		}
		return id;
	}

	/**
	 * Save condition. If non existing, creates it.
	 *  
	 * @param regulation_id the regulation id
	 * @param idx the index that this condition has (among the big 5) Can be expanded.
	 * @param operation_id the operation index in the lists
	 * @param interval_id the interval index in the lists
	 * @author kelly
	 */
	public void saveCondition(
				String regulation_id,
				String idx,
				String operation_id,
				String interval_id
		) throws RegulationException, RemoteException {

		ConditionHome home = null;
		Condition c = null;
		
		Integer regulationID = null;  
		Integer index = null; 
		Integer operationID = null;  
		Integer intervalID = null;  

		try {
			regulationID = regulation_id.length() != 0 ? new Integer(regulation_id) : null;  
			index = idx.length() != 0 ? new Integer(idx) : null;  
			operationID = operation_id.length() != 0 ? new Integer(operation_id) : null;  
			intervalID = interval_id.length() != 0 ? new Integer(interval_id) : null;  
			c = null;
			home = (ConditionHome) IDOLookup.getHome(Condition.class);
			c = (Condition) findConditionByRegulationAndIndex(regulationID, index);
		} catch (Exception e) {}

		try {
			if (c == null) {
				c = home.create();
			}
			c.setConditionID(operationID.intValue());
			if (intervalID != null) {
				c.setIntervalID(intervalID.intValue());
			}
			if (index != null) {
				c.setIndex(index.intValue());
			}
			if (regulationID != null) {
				c.setRegulationID(regulationID.intValue());
			}
			c.store();
		} catch (Exception e) {}

 	}


	/**
	 * gets a posting detail
	 *  
	 * @param xxx  
	 * @return yyy
	 * 
	 * @author kelly
	 */
	public PostingDetail getPostingDetailByOperationFlowPeriodConditionTypeRegSpecType(
			String operation, 
			String flow, 
			Date period, 
			Collection condition, 
			String regSpecType,
			int totalSum, 
			ChildCareContract contract) {

		PostingDetail postingDetail = new PostingDetail();
		IWBundle bundle = getIWApplicationContext().getApplication().getBundle(getBundleIdentifier());
		IWResourceBundle iwrb = 
				bundle.getResourceBundle(getIWApplicationContext().getApplication().getSettings().getDefaultLocale());

		Collection items = findRegulationsByPeriod(period, period);
		if (items != null) {
			Iterator iter = items.iterator();
			int match = 0;
			while (iter.hasNext()) {
				Regulation r = (Regulation) iter.next();
				if (flow.compareTo(r.getPaymentFlowType().getLocalizationKey()) == 0) {
					match++;
				}
				if (regSpecType.compareTo(r.getRegSpecType().getLocalizationKey()) == 0) {
					match++;
				}
				match += checkConditions(r, condition);
				if (match == (2 + condition.size())) {
					// match
					postingDetail.setAmount(r.getAmount().intValue());
					postingDetail.setTerm(iwrb.getLocalizedString(r.getLocalizationKey()));
					break;
				}	
				match = 0;
			}
		}
		return postingDetail;
	}


	private int checkConditions(Regulation r, Collection c) {
		// something must be done here
		// I dont know what the collection c contains
		// The conditions are located in the Bean ConditionBMPBean
		// There are always 5 conditions for each regulation.
		// A condition is just a record with a pointer to the PK of Condition and the PK of the Interval
		
		r.toString(); // Added to remove compiler warning
		c.toString(); // Added to remove compiler warning
		return 0;	
	}
		 
	/**
	 * Gets a Condition by Regulation ID and Index
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParameters# 
	 * @param regulationID Regulation ID
	 * @param index the index of the condition
	 * @return Condition
	 * @author Kelly
	 */
	public Object findConditionByRegulationAndIndex(Integer regulationID, Integer index) throws FinderException {
		try {
			ConditionHome home = getConditionHome();
			return home.findAllConditionsByRegulationAndIndex(
					regulationID.intValue(),
					index.intValue()
			);				
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null;
		}
	}
		 
	/**
	 * Gets all Activity types
	 * @return collection of Activity Types = School types
	 * @see import com.idega.block.school.data.SchoolType#
	 * @author Kelly
	 */
	public Collection findAllActivityTypes() {
		try {
			SchoolTypeHome home = getSchoolTypeHome();
			return home.findAllSchoolTypes();				
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null; 
		} 
	}	

	/**
	 * Gets all Commune belonging types
	 * @return collection of Commune belonging types
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.CommuneBelongingType 
	 * @author Kelly
	 */
	public Collection findAllCommuneBelongingTypes() {
		try {
			CommuneBelongingTypeHome home = getCommuneBelongingTypeHome();
			return home.findAllCommuneBelongingTypes();			
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null;
		}
	}	


	/**
	 * Gets all Company Types
	 * @return collection of Company Types
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.CompanyType 
	 * @author Kelly
	 */
	public Collection findAllCompanyTypes() {
		try {
			SchoolManagementTypeHome home = getSchoolManagementTypeHome();
			return home.findAllManagementTypes();				
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null;
		}
	}	

	/**
	 * Gets all Regulation specification types
	 * @return collection of Regulation specification types
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType 
	 * @author Kelly
	 */
	public Collection findAllRegulationSpecTypes() throws RegulationException {
		try {
			RegulationSpecTypeHome home = getRegulationSpecTypeHome();
			return home.findAllRegulationSpecTypes();				
		} catch (RemoteException e) {
			throw new RegulationException(KEY_GENERAL_ERROR, DEFAULT_GENERAL_ERROR);
		} catch (FinderException e) {
			throw new RegulationException(KEY_GENERAL_ERROR, DEFAULT_GENERAL_ERROR);
		}
	}	


	/**
	 * Saves a Regulation specification type.
	 * @param regSpecTypeId The regulation specification id
	 * @param regSpecTypeKey localized key
	 * @param mainRuleId the MainTule relational id
	 * @throws RegulationException if invalid parameters
	 * @author Kelly
	 */
	public void saveRegulationSpecType(
			int regSpecTypeId,
			String regSpecTypeKey,
			int mainRuleId) throws RegulationException {

		boolean create = false;
		RegulationSpecTypeHome home = null;
		RegulationSpecType rst = null;
		
		if (regSpecTypeKey.length() == 0) {
			throw new RegulationException(KEY_ERROR_PARAM_REG_SPEC_EMPTY, "Regelspecifikcationstyp saknas!");			
		}
	
		try {
			home = getRegulationSpecTypeHome();
			rst = home.findByPrimaryKey(new Integer(regSpecTypeId));
		} catch (FinderException e) {
			create = true;
		} catch (RemoteException e) { 
			throw new RegulationException(KEY_CANNOT_SAVE_REG_SPEC_TYPE, DEFAULT_CANNOT_SAVE_REG_SPEC_TYPE);
		}
		try { 
			if (create) {
				rst = home.create();
			}
			rst.setMainRule(mainRuleId);
			rst.setLocalizationKey(regSpecTypeKey);
			rst.store();
		} catch (CreateException e) { 
			throw new RegulationException(KEY_CANNOT_SAVE_REG_SPEC_TYPE, DEFAULT_CANNOT_SAVE_REG_SPEC_TYPE);
		}		
	
	} 





	/**
	 * Deletes the regulation spec type object with the specified id.
	 * @param id the RegSpecType id
	 * @throws RegulationException if the regulation could not be deleted
	 */ 
	public void deleteRegulationSpecType(int id) throws RegulationException {
		try {
			RegulationSpecTypeHome home = getRegulationSpecTypeHome();
			RegulationSpecType rst = home.findByPrimaryKey(new Integer(id));
			rst.remove();
		} catch (RemoteException e) { 
			throw new RegulationException(KEY_CANNOT_DELETE_REG_SPEC_TYPE, DEFAULT_CANNOT_DELETE_REG_SPEC_TYPE);
		} catch (FinderException e) { 
			throw new RegulationException(KEY_CANNOT_DELETE_REG_SPEC_TYPE, DEFAULT_CANNOT_DELETE_REG_SPEC_TYPE);
		} catch (RemoveException e) { 
			throw new RegulationException(KEY_CANNOT_DELETE_REG_SPEC_TYPE, DEFAULT_CANNOT_DELETE_REG_SPEC_TYPE);
		}		
	}

	/**
	 * Gets all payment flow types.
	 * @return collection of payment flow types
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.PaymentFlowType 
	 * @author anders
	 */
	public Collection findAllPaymentFlowTypes() {
		try {
			PaymentFlowTypeHome home = getPaymentFlowTypeHome();
			return home.findAll();				
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null;
		}
	}	

	/**
	 * Gets all provider types.
	 * @return collection of provider types
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.ProviderType 
	 * @author anders
	 */
	public Collection findAllProviderTypes() {
		try {
			ProviderTypeHome home = getProviderTypeHome();
			return home.findAll();				
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null;
		}
	}	

	/**
	 * Gets all Main Rules
	 * @return collection of provider types
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.MainRuleBMPBean# 
	 * @author Kelly
	 */
	public Collection findAllMainRules() {
		try {
			MainRuleHome home = getMainRuleHome();
			return home.findAllMainRules();				
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null;
		}
	}	

	/**
	 * Gets a Regulation Specification
	 * @return RegulationSpecType
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType#
	 * @author Kelly
	 */
	public Object findRegulationSpecType(int id) {
		try {
			RegulationSpecTypeHome home = getRegulationSpecTypeHome();
			return home.findRegulationSpecType(id);				
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null;
		}
	}	

	/**
	 * Gets all Conditions on a certain regulation
	 * @return collection of conditions
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType#
	 * @author Kelly
	 */
	public Collection findAllConditionsByRegulation(Regulation r) {
		try {
			ConditionHome home = getConditionHome();
			return home.findAllConditionsByRegulation(r);				
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null;
		}
	}	

	/**
	 * Gets all Conditions on a certain regulation
	 * @return collection of conditions
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType#
	 * @author Kelly
	 */
	public Collection findAllConditionsByRegulationID(int id) {
		try {
			ConditionHome home = getConditionHome();
			return home.findAllConditionsByRegulationID(id);				
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null;
		}
	}	



	/**
	 * Gets all Regulations
	 * @return collection of Regulations
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.Regulation
	 * @author Kelly
	 */
	public Collection findAllRegulations() {
		try {
			RegulationHome home = getRegulationHome();
			return home.findAllRegulations();				
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null;
		}
	}	

	/*
	 * Checks if dates are in overlap of stored Regulations
	 * @return true if there is an overlap
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.Regulation
	 * @author Kelly
	 */
	private boolean isRegulationOverlap(Date from, Date to, Regulation r) {

		try {
			RegulationHome home = getRegulationHome();
			if (home.findRegulationOverlap(from, to, r) == null) {
				return false;				
			} else {
				return true;
			}
		} catch (RemoteException e) {
			return false;
		} catch (FinderException e) {
			return false;
		}
	}	


	/**
	 * Gets all VAT Rules
	 * @return collection of VAT Rules
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.Regulation
	 * @author Kelly
	 */
	public Collection findAllVATRules() {
		try {
			VATRuleHome home = getVATRuleHome();
			Collection c = home.findAllVATRules();
			if(c == null) {
				VATRule vr = home.create();
				vr.store();
			}
			return home.findAllVATRules();				
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null;
		} catch (CreateException e) {
			return null;
		}
	}	


	/**
	 * Gets regulations for a certain periode
	 * @param from periode (Date)
	 * @param to periode (Date)
	 * @return collection of Regulations
	 * @author Kelly
	 * 
	 */
	public Collection findRegulationsByPeriod(Date from, Date to) {
		try {
			RegulationHome home = getRegulationHome();
			return home.findRegulationsByPeriod(from, to);				
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null;
		}
	}	

	/**
	 * Gets regulations for a certain periode, operationID, FLowTypeID and SortByID
	 * @param from periode (Date)
	 * @param to periode (Date)
	 * @param operationID
	 * @param flowTypeID
	 * @param sortByID
	 * @return collection of Regulations
	 * @author Kelly
	 * 
	 */
	public Collection findRegulationsByPeriod(
			Date from, 
			Date to, 	
			String operationID,
			int flowTypeID,
			int sortByID ) {
				
		try {
			RegulationHome home = getRegulationHome();
			return home.findRegulationsByPeriod(from, to, operationID, flowTypeID, sortByID);				
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null;
		}
	}	


	/**
	 * Gets a Regulation
	 * @return Regulations
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.Regulation
	 * @author Kelly
	 */
	public Regulation findRegulation(int id) {
		try {
			RegulationHome home = getRegulationHome();
			return home.findRegulation(id);				
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null;
		}
	}	

	/**
	 * Finds all sibling values
	 * These are not put in an entity bean since Lotta Ringborg 
	 * tells me they shall be fixed and never changed.
	 * @return Collection of sibling numbers
	 * @author Kelly
	 */
	public Collection findAllSiblingValues() {
		ArrayList arr = new ArrayList();
		for (int i = 0; i <= 9; i++) {
			arr.add(new Object[]{new Integer(i), ""+i});
		}
		return arr; 
	}	

	/**
	 * Finds all hour values
	 * These are not put in an entity bean since Lotta Ringborg 
	 * tells me they shall be fixed and never changed.
	 * @return Collection of hour intervals
	 * @author Kelly
	 */
	public Collection findAllHourIntervals() {
		ArrayList arr = new ArrayList();

		arr.add(new Object[]{new Integer(1), "<=10"});
		arr.add(new Object[]{new Integer(2), "11-25"});
		arr.add(new Object[]{new Integer(3), "0-25"});
		arr.add(new Object[]{new Integer(4), ">25"});

		return arr; 
	}	

	/**
	 * Gets a yes/no to use in the regulation framework.
	 * @return Yes no collection
	 * @see se.idega.idegaweb.commune.accounting.regulations.presentation.RegulationListEditor#
	 * @see getAllOperations
	 * @author Kelly
	 */
	public Collection getYesNo() {
		try {
			YesNoHome home = getYesNoHome();
			return home.findAllYesNoValues();				
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null;
		}
	}	


	/**
	 * Finds all Max Amounts
	 * These are not put in an entity bean since Lotta Ringborg 
	 * tells me they shall be fixed and never changed.
	 * @return Collection of Max Amounts
	 * @author Kelly
	 */
	public Collection findAllMaxAmounts() {
		ArrayList arr = new ArrayList();
		for (int i = 1; i <= 100; i++) {
			String s = i+"%";
			arr.add(new Object[]{new Integer(i), s});
	}
		return arr; 
	}	


	/**
	 * Finds all discount values
	 * These are not put in an entity bean since Lotta Ringborg 
	 * tells me they shall be fixed and never changed.
	 * @return Collection of discount values
	 * @author Kelly
	 */
	public Collection findAllDiscountValues() {
		ArrayList arr = new ArrayList();
		for (int i = 0; i <= 100; i++) {
			String s = i+"%";
			arr.add(new Object[]{new Integer(i), "-" + s});
		}
		return arr; 
	}	
	
	/**
	 * Deletes a regulation
	 * @param id Regulation ID
	 * @author Kelly
	 * 
	 */
	public void deleteRegulation(int id) throws java.rmi.RemoteException {
		try {
			Regulation r = findRegulation(id);
			r.remove();
			r.store();	
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	/**
	 * Gets all Special Calculation types
	 * @return collection of Special calculation types
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.SpecialCalculationType#
	 * @author Kelly
	 */
	public Collection findAllSpecialCalculationTypes() {
		try {
			SpecialCalculationTypeHome home = getSpecialCalculationTypeHome();
			Collection c = home.findAllSpecialCalculationTypes();
			if (c == null) {
				SpecialCalculationType sct = home.create();
				sct.store();
			}
			return home.findAllSpecialCalculationTypes();				
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null;
		} catch (CreateException e) {
			return null;
		}
	}	



	/**
	 * Gets all ConditionTypes
	 * @return collection of condition types
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.ConditionType#
	 * @author Kelly
	 */
	public Collection findAllConditionTypes() {
		try {
			ConditionTypeHome home = getConditionTypeHome();
			return home.findAllConditionTypes();				
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null;
		}
	}	

	/**
	 * Gets all Operations. This is used to get certain data from different 
	 * parts of the system. This could be placed in a bean later...
	 * As of now I just set the values here.
	 * 
	 * Values are:
	 * 
	 * Operation ID
	 * Real term (initial localized value)
	 * Localization key
	 * Class where the collection can be retrieved
	 * Method to get the collection
	 * Method to get the data in the bean (If blank it means the data is just a collection of objects []
	 * 
	 * @return collection of ConditionHolders
	 * @see se.idega.idegaweb.commune.accounting.regulations.business.ConditionHolder#
	 * @see se.idega.idegaweb.commune.accounting.regulations.presentation.RegulationListEditor#
	 * @author Kelly
	 */
	public Collection findAllOperations() {
			// LP = Localization path
			ArrayList arr = new ArrayList();
			arr.add(new ConditionHolder(
					RuleTypeConstant.CONDITION_ID_OPERATION, 
					"Verksamhet", 
					LP + "verksamhet", 
					"com.idega.block.school.business.SchoolBusiness", 
					"findAllSchoolTypes",
					"getLocalizationKey")
			);
			
			arr.add(new ConditionHolder(
					RuleTypeConstant.CONDITION_ID_RESOURCE, 
					"Resurs", 
					LP + "resurs", 
					"se.idega.idegaweb.commune.accounting.resource.business.ResourceBusiness", 
					"findAllResources",
					"getResourceName")
			);
	
			arr.add(new ConditionHolder(
					RuleTypeConstant.CONDITION_ID_VAT, 
					"Momssats", 
					LP + "momssats", 
					"se.idega.idegaweb.commune.accounting.regulations.business.VATBusiness", 
					"findAllVATRegulations",
					"getDescription")
			);
	
			arr.add(new ConditionHolder(
					RuleTypeConstant.CONDITION_ID_SCHOOL_YEAR, 
					"Årskurs", 
					LP + "aarskurs", 
					"com.idega.block.school.business.SchoolBusiness", 
					"findAllSchoolYears",
					"getSchoolYearName")
			);
	
			arr.add(new ConditionHolder(
					RuleTypeConstant.CONDITION_ID_HOURS, 
					"Timmar", 
					LP + "timmar", 
					"se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness", 
					"findAllHourIntervals",
					"")
			);
	
			arr.add(new ConditionHolder(
					RuleTypeConstant.CONDITION_ID_SIBLINGS, 
					"Syskonnr", 
					LP + "syskonnr", 
					"se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness", 
					"findAllSiblingValues",
					"")
			);
	
			arr.add(new ConditionHolder(
					RuleTypeConstant.CONDITION_ID_AGE_INTERVAL, 
					"Ålder", 
					LP + "alder", 
					"se.idega.idegaweb.commune.accounting.regulations.business.AgeBusiness", 
					"findAllAgeRegulations",
					"getAgeInterval")
			);

			arr.add(new ConditionHolder(
					RuleTypeConstant.CONDITION_ID_STADSBIDRAG, 
					"Stadsbidragsberättigad", 
					LP + "stadsbidragsberattigad", 
					"se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness", 
					"getYesNo",
					"getLocalizationKey")
			);

			
	/*
			arr.add(new ConditionHolder(
					"8", 
					"Rabattsats", 
					LP + "rabattsats", 
					"se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness", 
					"findAllDiscountValues",
					"")
			);
			
			arr.add(new ConditionHolder(
					"9", 
					"Maxbelopp", 
					LP + "maxbelopp", 
					"se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness", 
					"findAllMaxAmounts",
					"")
			);
*/
			return (Collection) arr;	

	}

	/**
	 * Function that will return term (the descriptive text for a charge), the amount, VAT
	 * and the VAT Type using the return object postingDetail. The regulation is selected
	 * where all the inputparameters are fulfilled.
	 * 
	 * @param operation (Huvudverksamhet)
	 * @param flow (Ström)
	 * @param period (date when the reule is valid)
	 * @param conditionType (Villkorstyp)
	 * @param condition (Collection of conditions)
	 * @param regSpecType (RegelSpec.Typ)
	 * @param totalSum total sum calculated so far. Sometimes needed for calculation to return
	 * @param contract The contract archive
	 * @return postingDetail
	 */	
	public PostingDetail getPostingDetailByOperationFlowPeriodConditionTypeRegSpecType(
			String operation, String flow, Date period, String conditionType, 
			String regSpecType, Collection condition, float totalSum, ChildCareContract contract){
	
		PostingDetail postingDetail = new PostingDetail();
		
		//Silly line to prevent the function from generation unused variable warning. Remove when logic created.
		System.out.println(operation+flow+period+conditionType+condition+regSpecType+totalSum+contract);

		//Insert code here to create postingDetail
	
		return postingDetail;
	}

	/**
	 * Function to return all the regulations that fit the description/selection 
	 * according to the input parameters and sorted by the condition order.
	 * 
	 * @param operation
	 * @param flow
	 * @param period
	 * @param conditionType
	 * @param condition
	 * @return ArrayList containing the regulations 
	 */
	public Collection getAllRegulationsByOperationFlowPeriodConditionTypeRegSpecType(
			String operation, String flow, Date period, String conditionType, Collection condition){
	
		ArrayList regulations = new ArrayList();
		
		//Silly line to prevent the function from generation unused variable warning. Remove when logic created.
		System.out.println(operation+flow+period+conditionType+condition);

		//Insert code here to create regulations

		return regulations;
	}
	
	/**
	 * Returns PostingDetail (the text, sum, vat and vat type) calculated for the specific regulation
	 * and contract
	 * 
	 * @param totalSum
	 * @param contract
	 * @return PostingDetail
	 */
	public PostingDetail getPostingDetailForContract(float totalSum, ChildCareContract contract){
	
		PostingDetail postingDetail = new PostingDetail();
		
		//Silly line to prevent the function from generation unused variable warning. Remove when logic created.
		System.out.println(totalSum+" "+contract);

		//Insert code here to create postingDetail
	
		return postingDetail;
	}



	/**
	 * I Need this before we can use replaceAll with regular expressions in 1.4
	 * 
	 * @author Kelly
	 */
	public String replaceToDot(String s) {
		
		String replace = s;
		int dot = s.indexOf(".");
		if (dot > 0) {
			replace = s.substring(dot+1);
		}
		return replace;
	}
 
	protected ActivityTypeHome getActivityTypeHome() throws RemoteException {
		return (ActivityTypeHome) com.idega.data.IDOLookup.getHome(ActivityType.class);
	}

	protected SchoolTypeHome getSchoolTypeHome() throws RemoteException {
		return (SchoolTypeHome) com.idega.data.IDOLookup.getHome(SchoolType.class);
	}

	protected SchoolManagementTypeHome getSchoolManagementTypeHome() throws RemoteException {
		return (SchoolManagementTypeHome) com.idega.data.IDOLookup.getHome(SchoolManagementType.class);
	}

	protected CommuneBelongingTypeHome getCommuneBelongingTypeHome() throws RemoteException {
		return (CommuneBelongingTypeHome) com.idega.data.IDOLookup.getHome(CommuneBelongingType.class);
	}

	protected RegulationSpecTypeHome getRegulationSpecTypeHome() throws RemoteException {
		return (RegulationSpecTypeHome) com.idega.data.IDOLookup.getHome(RegulationSpecType.class);
	}
		
	protected PaymentFlowTypeHome getPaymentFlowTypeHome() throws RemoteException {
		return (PaymentFlowTypeHome) com.idega.data.IDOLookup.getHome(PaymentFlowType.class);
	}	
		
	protected ProviderTypeHome getProviderTypeHome() throws RemoteException {
		return (ProviderTypeHome) com.idega.data.IDOLookup.getHome(ProviderType.class);
	}	

	protected RegulationHome getRegulationHome() throws RemoteException {
		return (RegulationHome) com.idega.data.IDOLookup.getHome(Regulation.class);
	}	

	protected ConditionHome getConditionHome() throws RemoteException {
		return (ConditionHome) com.idega.data.IDOLookup.getHome(Condition.class);
	}	

	protected ConditionTypeHome getConditionTypeHome() throws RemoteException {
		return (ConditionTypeHome) com.idega.data.IDOLookup.getHome(ConditionType.class);
	}	

	protected VATRuleHome getVATRuleHome() throws RemoteException {
		return (VATRuleHome) com.idega.data.IDOLookup.getHome(VATRule.class);
	}	

	protected YesNoHome getYesNoHome() throws RemoteException {
		return (YesNoHome) com.idega.data.IDOLookup.getHome(YesNo.class);
	}	

	protected SpecialCalculationTypeHome getSpecialCalculationTypeHome() throws RemoteException {
		return (SpecialCalculationTypeHome) com.idega.data.IDOLookup.getHome(SpecialCalculationType.class);
	}	

	protected MainRuleHome getMainRuleHome() throws RemoteException {
		return (MainRuleHome) com.idega.data.IDOLookup.getHome(MainRule.class);
	}	

}
 