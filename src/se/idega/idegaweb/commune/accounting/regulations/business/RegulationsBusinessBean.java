/*
 * $Id: RegulationsBusinessBean.java,v 1.1 2003/08/20 13:03:11 kjell Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.regulations.business;

import java.rmi.RemoteException;
import javax.ejb.FinderException;
import java.util.Collection;

import se.idega.idegaweb.commune.accounting.regulations.data.ActivityTypeHome;
import se.idega.idegaweb.commune.accounting.regulations.data.ActivityType;
import se.idega.idegaweb.commune.accounting.regulations.data.CommuneBelongingTypeHome;
import se.idega.idegaweb.commune.accounting.regulations.data.CommuneBelongingType;
import se.idega.idegaweb.commune.accounting.regulations.data.CompanyTypeHome;
import se.idega.idegaweb.commune.accounting.regulations.data.CompanyType;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecTypeHome;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;

/**
 * @author Kjell Lindman
 * 
 */
public class RegulationsBusinessBean extends com.idega.business.IBOServiceBean implements RegulationsBusiness  {
	
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
			CompanyTypeHome home = getCompanyTypeHome();
			return home.findAllCompanyTypes();				
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
	
}
