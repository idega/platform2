/*
 * $Id: RegulationsBusinessBean.java,v 1.5 2003/08/28 18:35:31 kjell Exp $
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

/**
 * @author Kjell Lindman
 * 
 */ 
public class RegulationsBusinessBean extends com.idega.business.IBOServiceBean implements RegulationsBusiness {
	 
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
				ret += ah.getTextKey();
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
				ret += cbt.getTextKey();
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
				ret += ct.getTextKey();
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
	public Collection findAllRegulationSpecTypes() {
		try {
			RegulationSpecTypeHome home = getRegulationSpecTypeHome();
			return home.findAllRegulationSpecTypes();				
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null;
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
				ret += rst.getTextKey();
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
}
 