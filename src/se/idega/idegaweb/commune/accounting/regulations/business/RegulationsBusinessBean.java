/*
 * $Id: RegulationsBusinessBean.java,v 1.14 2003/09/05 16:09:27 kjell Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.regulations.business;

import java.util.Collection;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.CreateException;


import com.idega.block.school.data.SchoolManagementType;
import com.idega.block.school.data.SchoolManagementTypeHome;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.block.school.data.SchoolType;

import se.idega.idegaweb.commune.accounting.regulations.data.ActivityTypeHome;
import se.idega.idegaweb.commune.accounting.regulations.data.ActivityType;
import se.idega.idegaweb.commune.accounting.regulations.data.CommuneBelongingTypeHome;
import se.idega.idegaweb.commune.accounting.regulations.data.CommuneBelongingType;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecTypeHome;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.regulations.data.PaymentFlowTypeHome;
import se.idega.idegaweb.commune.accounting.regulations.data.PaymentFlowType;
import se.idega.idegaweb.commune.accounting.regulations.data.ProviderTypeHome;
import se.idega.idegaweb.commune.accounting.regulations.data.ProviderType;
import se.idega.idegaweb.commune.accounting.regulations.data.MainRuleHome;
import se.idega.idegaweb.commune.accounting.regulations.data.MainRule;
import se.idega.idegaweb.commune.accounting.regulations.data.Regulation;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationHome;
import se.idega.idegaweb.commune.accounting.regulations.data.Condition;
import se.idega.idegaweb.commune.accounting.regulations.data.ConditionHome;
import se.idega.idegaweb.commune.accounting.regulations.data.ConditionType;
import se.idega.idegaweb.commune.accounting.regulations.data.ConditionTypeHome;
import se.idega.idegaweb.commune.accounting.regulations.data.SpecialCalculationType;
import se.idega.idegaweb.commune.accounting.regulations.data.SpecialCalculationTypeHome;




/**
 * @author Kjell Lindman
 * 
 */ 
public class RegulationsBusinessBean extends com.idega.business.IBOServiceBean implements RegulationsBusiness {

	private final static String KP = "regulation_spec_type_error."; // key prefix 
	public final static String KEY_CANNOT_DELETE_REG_SPEC_TYPE  = KP + "cannot_delete_reg_spec_type_regulation";
	public final static String DEFAULT_CANNOT_DELETE_REG_SPEC_TYPE = "Kunde inte radera regelspecifikationstypen";
	public final static String KEY_CANNOT_SAVE_REG_SPEC_TYPE  = KP + "cannot_save_reg_spec_type_regulation";
	public final static String DEFAULT_CANNOT_SAVE_REG_SPEC_TYPE = "Kunde inte spara regelspecifikationstypen";
	public final static String KEY_GENERAL_ERROR  = KP + "general_error";
	public final static String DEFAULT_GENERAL_ERROR = "Systemfel";
	 
	/**
	 * Gets all Activity types
	 * @return collection of Activity Types = School types
	 * @see import com.idega.block.school.data.SchoolType#
	 * @author Kjell
	 */
	public Collection findAllActivityTypes() {
		try {
//			ActivityTypeHome home = getActivityTypeHome();
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
	 * @author Kjell
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
	 * @author Kjell
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
	 * @author Kjell
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
	 * @author Kjell
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
	 * Gets all Regulations
	 * @return collection of Regulations
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.Regulation
	 * @author Kjell
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


	/**
	 * Gets regulations for a certain periode
	 * @param from periode (Date)
	 * @param to periode (Date)
	 * @return collection of Regulations
	 * @author Kjell
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
	 * Gets a Regulation
	 * @return Regulations
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.Regulation
	 * @author Kjell
	 */
	public Regulation findRegulation(int id) {
		try {
			RegulationHome home = getRegulationHome();
			return (Regulation) home.findRegulation(id);				
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null;
		}
	}	


	/**
	 * Deletes a regulation
	 * @param id Regulation ID
	 * @author Kjell
	 * 
	 */
	public void deleteRegulation(int id) throws java.rmi.RemoteException {
		try {
			Regulation r = (Regulation) findRegulation(id);
			r.remove();
			r.store();	
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	/**
	 * Gets all ConditionTypes
	 * @return collection of condition types
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.ConditionType#
	 * @author Kjell
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


	public Collection findAllOperations() {

			ArrayList arr = new ArrayList();
			String PP = "cacc_regulation_conditions_";
			arr.add(new ConditionHolder(
					"Verksamhet", 
					PP + "verksamhet", 
					"com.idega.block.school.business.SchoolBusiness", 
					"findAllSchoolTypes",
					"getLocalizationKey")
			);
			
			arr.add(new ConditionHolder(
					"Resurs", 
					PP + "resurs", 
					"se.idega.idegaweb.commune.accounting.resource.business.ResourceBusiness", 
					"findAllResources",
					"getResourceName")
			);
	
			arr.add(new ConditionHolder(
					"Momssats", 
					PP + "momssats", 
					"se.idega.idegaweb.commune.accounting.regulations.business.VATBusiness", 
					"findAllVATRegulations",
					"getLocalizationKey")
			);
	
			arr.add(new ConditionHolder(
					"Årskurs", 
					PP + "aarskurs", 
					"com.idega.block.school.business.SchoolBusiness", 
					"findAllSchoolYears",
					"getSchoolYearName")
			);
	
			arr.add(new ConditionHolder(
					"Timmar", 
					PP + "timmar", 
					"se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness", 
					"findAllHourIntervals",
					"")
			);
	
			arr.add(new ConditionHolder(
					"Syskonnr", 
					PP + "syskonnr", 
					"se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness", 
					"findAllSiblingValues",
					"")
			);
	
			arr.add(new ConditionHolder(
					"Ålder", 
					PP + "alder", 
					"se.idega.idegaweb.commune.accounting.regulations.business.AgeBusiness", 
					"findAllAgeRegulations",
					"getAgeInterval")
			);
	
			arr.add(new ConditionHolder(
					"Rabattsats", 
					PP + "rabattsats", 
					"se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness", 
					"findAllDiscountValues",
					"")
			);
			return (Collection) arr;	
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

	protected SpecialCalculationTypeHome getSpecialCalculationTypeHome() throws RemoteException {
		return (SpecialCalculationTypeHome) com.idega.data.IDOLookup.getHome(SpecialCalculationType.class);
	}	

	protected MainRuleHome getMainRuleHome() throws RemoteException {
		return (MainRuleHome) com.idega.data.IDOLookup.getHome(MainRule.class);
	}	

}
 