/*
 * $Id: RegulationsBusinessBean.java,v 1.9 2003/08/29 15:36:24 kjell Exp $
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
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.CreateException;
import java.util.Iterator;

import se.idega.idegaweb.commune.accounting.regulations.data.ActivityTypeHome;
import se.idega.idegaweb.commune.accounting.regulations.data.ActivityType;
import se.idega.idegaweb.commune.accounting.regulations.data.CommuneBelongingTypeHome;
import se.idega.idegaweb.commune.accounting.regulations.data.CommuneBelongingType;
import se.idega.idegaweb.commune.accounting.regulations.data.CompanyTypeHome;
import se.idega.idegaweb.commune.accounting.regulations.data.CompanyType;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecTypeHome;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.regulations.data.PaymentFlowTypeHome;
import se.idega.idegaweb.commune.accounting.regulations.data.PaymentFlowType;
import se.idega.idegaweb.commune.accounting.regulations.data.ProviderTypeHome;
import se.idega.idegaweb.commune.accounting.regulations.data.ProviderType;
import se.idega.idegaweb.commune.accounting.regulations.data.MainRuleHome;
import se.idega.idegaweb.commune.accounting.regulations.data.MainRule;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationException;

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
	 * @return collection of Activity Types
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.ActivityType 
	 * @author Kjell
	 */
	public Collection findAllActivityTypes() {
		try {
			ActivityTypeHome home = getActivityTypeHome();
			return home.findAllActivityTypes();				
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null; 
		} 
	}	

	/**
	 * Gets all Activity types (keys) as a comma separated string
	 * @return String of activity types
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.ActivityType 
	 * @author Kjell
	 */
	public String getActivityTypesAsString() {
		String ret = "";
		try {
			ActivityTypeHome home = getActivityTypeHome();
			Collection col = home.findAllActivityTypes();
			Iterator iter = col.iterator();
			while(iter.hasNext())  {
				ActivityType ah = (ActivityType) iter.next();
				ret += replaceToDot(ah.getTextKey());
//				ret += ah.getTextKey().replaceAll("^.*\\.", "");
//				ret += ah.getTextKey();
				if(iter.hasNext()) {
					ret += ", ";
				}
			}
			return ret;				
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
	 * Gets all Commune Belongings types (keys) as a comma separated string
	 * @return String of activity types
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.ActivityType 
	 * @author Kjell
	 */
	public String getCommuneBelongingsAsString() {
		String ret = "";
		try {
			CommuneBelongingTypeHome home = getCommuneBelongingTypeHome();
			Collection col = home.findAllCommuneBelongingTypes();
			Iterator iter = col.iterator();
			while(iter.hasNext())  {
				CommuneBelongingType cbt = (CommuneBelongingType) iter.next();
				ret += replaceToDot(cbt.getTextKey());
//				ret += cbt.getTextKey().replaceAll("^.*\\.", "");
//				ret += cbt.getTextKey();
				if(iter.hasNext()) {
					ret += ", ";
				}
			}
			return ret;				
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
			CompanyTypeHome home = getCompanyTypeHome();
			return home.findAllCompanyTypes();				
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null;
		}
	}	

	/**
	 * Gets all Company Types (keys) as a comma separated string
	 * @return String of activity types
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.ActivityType 
	 * @author Kjell
	 */
	public String getCompanyTypesAsString() {
		String ret = "";
		try {
			CompanyTypeHome home = getCompanyTypeHome();
			Collection col = home.findAllCompanyTypes();
			Iterator iter = col.iterator();
			while(iter.hasNext())  {
				CompanyType ct = (CompanyType) iter.next();
				ret += replaceToDot(ct.getTextKey());
//				ret += ct.getTextKey().replaceAll("^.*\\.", "");
//				ret += ct.getTextKey();
				if(iter.hasNext()) {
					ret += ", ";
				}
			}
			return ret;				
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
			rst.setTextKey(regSpecTypeKey);
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
	 * Gets all Regulation Spec Types (keys) as a comma separated string
	 * @return String of activity types
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.ActivityType 
	 * @author Kjell
	 */
	public String getRegulationSpecTypesAsString() {
		String ret = "";
		try {
			RegulationSpecTypeHome home = getRegulationSpecTypeHome();
			Collection col = home.findAllRegulationSpecTypes();
			Iterator iter = col.iterator();
			while(iter.hasNext())  {
				RegulationSpecType rst = (RegulationSpecType) iter.next();
				ret += replaceToDot(rst.getTextKey());
//				ret += rst.getTextKey().replaceAll("^.*\\.", "");
//				ret += rst.getTextKey();
				if(iter.hasNext()) {
					ret += ", ";
				}
			}
			return ret;				
		} catch (RemoteException e) {
			return "";
		} catch (FinderException e) {
			return ""; 
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
	 * I Need this before we can use replaceAll with regular expressions in 1.4
	 * 
	 * @author Kelly
	 */
	public String replaceToDot(String s) {
		
		String replace = "";
		int dot = s.indexOf(".");
		if (dot > 0) {
			replace = s.substring(dot+1);
		}
		return replace;
	}
 
	protected ActivityTypeHome getActivityTypeHome() throws RemoteException {
		return (ActivityTypeHome) com.idega.data.IDOLookup.getHome(ActivityType.class);
	}

	protected CommuneBelongingTypeHome getCommuneBelongingTypeHome() throws RemoteException {
		return (CommuneBelongingTypeHome) com.idega.data.IDOLookup.getHome(CommuneBelongingType.class);
	}

	protected CompanyTypeHome getCompanyTypeHome() throws RemoteException {
		return (CompanyTypeHome) com.idega.data.IDOLookup.getHome(CompanyType.class);
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

	protected MainRuleHome getMainRuleHome() throws RemoteException {
		return (MainRuleHome) com.idega.data.IDOLookup.getHome(MainRule.class);
	}	

}
 