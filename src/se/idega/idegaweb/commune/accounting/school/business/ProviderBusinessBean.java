/*
 * $Id: ProviderBusinessBean.java,v 1.5 2003/09/24 08:39:31 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.school.business;


import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import java.rmi.RemoteException;
import javax.ejb.FinderException;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;

import com.idega.business.IBOLookup;

import com.idega.block.school.data.School;
import com.idega.block.school.business.SchoolBusiness;

import se.idega.idegaweb.commune.accounting.school.data.Provider;
import se.idega.idegaweb.commune.accounting.school.data.ProviderAccountingProperties;
import se.idega.idegaweb.commune.accounting.school.data.ProviderAccountingPropertiesHome;

/** 
 * Business logic for providers with accounting information.
 * <p>
 * Last modified: $Date: 2003/09/24 08:39:31 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.5 $
 */
public class ProviderBusinessBean extends com.idega.business.IBOServiceBean implements ProviderBusiness {

	private final static String KP = "provider_error."; // key prefix 

	public final static String KEY_NAME_MISSING = KP + "name_missing";
	public final static String KEY_ILLEGAL_ORGANIZATION_NUMBER = KP + "illegal_organization_number";
	public final static String KEY_CANNOT_SAVE_PROVIDER = KP + "cannot_save_provider";
	public final static String KEY_CANNOT_DELETE_PROVIDER = KP + "cannot_delete_provider";

	public final static String DEFAULT_NAME_MISSING = "The name of the provider must be entered.";
	public final static String DEFAULT_ILLEGAL_ORGANIZATION_NUMBER = "Illegal organization number.";
	public final static String DEFAULT_CANNOT_SAVE_PROVIDER = "The provider cannot be stored due to technical error.";
	public final static String DEFAULT_CANNOT_DELETE_PROVIDER = "The provider cannot be deleted due to technical error.";

	/**
	 * Finds all study paths.
	 * @return collection of study path objects
	 * @see se.idega.idegaweb.commune.accounting.school.data.StudyPath 
	 */
	/*
	public Collection findAllStudyPaths() {
		try {
			SchoolStudyPathHome home = getSchoolStudyPathHome();
			return home.findAll();				
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null;
		}
	}	
*/
	
	/**
	 * Saves a provider object.
	 * Creates a new persistent school object and provider accounting properties object if nescessary.
	 * @throws ProviderException if invalid parameters
	 */
	public void saveProvider(
			String schoolId,
			String name,
			String info,
			String address,
			String zipCode,
			String zipArea,
			String phone,
			String keyCode,
			String latitude,
			String longitude,
			String schoolAreaId,
			Map schoolTypeMap,
			String organizationNumber,
			String extraProviderId,
			String providerTypeId,
			String schoolManagementTypeId,
			java.sql.Date terminationDate,
			String communeId,
			String countryId,
			String centralizedAdministration,
			String paymentByInvoice,
			String postgiro,
			String bankgiro,
			String statisticsType,
			String ownPosting,
			String doublePosting) throws ProviderException {

		// Provider name
		String s = name.trim();
		if (s.equals("")) {
			throw new ProviderException(KEY_NAME_MISSING, DEFAULT_NAME_MISSING);
		} else {
			name = s;
		}

		// Organization number
		if (organizationNumber.trim().length() != 0) {
			String orgNum = "";
			for (int i = 0; i < organizationNumber.length(); i++) {
				char c = organizationNumber.charAt(i);
				if ((c >= '0') && (c <= '9')) {
					orgNum += c;
				}
			}
			if (orgNum.length() != 10) {
				throw new ProviderException(KEY_ILLEGAL_ORGANIZATION_NUMBER, DEFAULT_ILLEGAL_ORGANIZATION_NUMBER);
			}
			int sum = 0;
			for (int i = 0; i < 10; i++) {
				int x = orgNum.charAt(i) - 48;
				if (i % 2 == 0) {
					x = x * 2;
				}
				sum += x / 10 + x % 10;
			}
			if (sum % 10 != 0) {
				throw new ProviderException(KEY_ILLEGAL_ORGANIZATION_NUMBER, DEFAULT_ILLEGAL_ORGANIZATION_NUMBER);
			}
		}
		
				
		try {
			SchoolBusiness sb = getSchoolBusiness();
			School school = sb.storeSchool(
					getInt(schoolId),
					name,
					info,
					address,
					zipCode,
					zipArea,
					phone,
					keyCode,
					latitude,
					longitude,
					getInt(schoolAreaId),
					getSchoolTypeIds(schoolTypeMap),
					getSchoolYearIds(schoolTypeMap),
					organizationNumber,
					extraProviderId,
					schoolManagementTypeId,
					terminationDate,
					getInt(communeId),
					getInt(countryId),
					getBoolean(centralizedAdministration));
			int id = ((Integer) school.getPrimaryKey()).intValue();
			ProviderAccountingProperties pap = null;
			ProviderAccountingPropertiesHome home = getProviderAccountingPropertiesHome();
			try {
				pap = home.findByPrimaryKey(new Integer(id));
			} catch (FinderException e) {
				pap = home.create();
			}
			pap.setSchoolId(id);
			pap.setPaymentByInvoice(getBoolean(paymentByInvoice).booleanValue());
			pap.setPostgiro(postgiro);
			pap.setBankgiro(bankgiro);
			int ptId = getInt(providerTypeId);
			if (ptId > 0) {
				pap.setProviderTypeId(ptId);
			}
			if (!statisticsType.equals("")) {
				pap.setStatisticsType(statisticsType);
			}
			pap.setOwnPosting(ownPosting);
			pap.setDoublePosting(doublePosting);
			pap.store();
		} catch (RemoteException e) { 
			throw new ProviderException(KEY_CANNOT_SAVE_PROVIDER, DEFAULT_CANNOT_SAVE_PROVIDER);
		} catch (CreateException e) { 
			throw new ProviderException(KEY_CANNOT_SAVE_PROVIDER, DEFAULT_CANNOT_SAVE_PROVIDER);
		}		
	}
	
	/**
	 * Deletes the provider object with the specified id.
	 * @param id the school id for the provider
	 * @throws ProviderException if the provider could not be deleted
	 */ 
	public void deleteProvider(String id) throws ProviderException {
		try {
			Integer schoolId = new Integer(id);
			SchoolBusiness sb = getSchoolBusiness();
			sb.removeSchool(schoolId.intValue());
			ProviderAccountingPropertiesHome home = getProviderAccountingPropertiesHome();
			ProviderAccountingProperties pap = home.findByPrimaryKey(schoolId);
			pap.remove();
		} catch (RemoteException e) { 
			throw new ProviderException(KEY_CANNOT_DELETE_PROVIDER, DEFAULT_CANNOT_DELETE_PROVIDER);
		} catch (FinderException e) { 
			throw new ProviderException(KEY_CANNOT_DELETE_PROVIDER, DEFAULT_CANNOT_DELETE_PROVIDER);
		} catch (RemoveException e) { 
			throw new ProviderException(KEY_CANNOT_DELETE_PROVIDER, DEFAULT_CANNOT_DELETE_PROVIDER);
		}
	}
		
	/**
	 * Returns the provider with the specified school id.
	 * @param schoolId the school id for the provider
	 */
	public Provider getProvider(int schoolId) throws StudyPathException {
		Provider p = new Provider(schoolId);
		if (p.getSchool() != null) {
			return p;
		} else {
			return null;
		}
	}
	
	/**
	 * Returns school business. 
	 */	
	protected SchoolBusiness getSchoolBusiness() throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolBusiness.class);	
	}	
	
	/**
	 * Returns provider accounting properties home. 
	 */
	protected ProviderAccountingPropertiesHome getProviderAccountingPropertiesHome() throws RemoteException {
		return (ProviderAccountingPropertiesHome) com.idega.data.IDOLookup.getHome(ProviderAccountingProperties.class);
	}

	/*
	 * Extracts the school type ids from the specified map
	 */
	private int[] getSchoolTypeIds(Map map) {
		int[] ids = new int[map.size()];
		Iterator iter = map.keySet().iterator();
		int i = 0;
		while (iter.hasNext()) {
			int id = getInt((String) iter.next());
			ids[i] = id;
			i++;
		}
		return ids;
	}

	/*
	 * Extracts the school year ids from the specified map
	 */
	private int[] getSchoolYearIds(Map map) {
		int yearCount = 0;
		Collection years = new java.util.ArrayList();
		Iterator iter = map.values().iterator();
		while (iter.hasNext()) {
			Map m = (Map) iter.next();
			Iterator iter2 = m.values().iterator();
			while(iter2.hasNext()) {
				years.add(iter2.next());
				yearCount++;
			}
		}
		int[] ids = new int[yearCount];
		iter = years.iterator();
		int i = 0;
		while (iter.hasNext()) {
			int id = getInt((String) iter.next());
			ids[i] = id;
			i++;
		}
		return ids;
	}
	
	/*
	 * Parses an integer, returns -1 if exception
	 */
	int getInt(String s) {
		int n = -1;
		try {
			n = Integer.parseInt(s);
		} catch (Exception e) {}
		return n;
	}	
	
	/*
	 * Parses a boolean
	 */
	Boolean getBoolean(String s) {
		Boolean b = new Boolean(false);
		if (s.length() > 0) {
			b = new Boolean(true);
		}
		return b;
	}	
}
