/*
 * $Id: Provider.java,v 1.3 2003/09/18 16:20:20 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.school.data;

import java.rmi.RemoteException;
import javax.ejb.FinderException;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolHome;

/**
 * This class is a holder for a school bean and provider accounting information.
 * <p>
 * Last modified: $Date: 2003/09/18 16:20:20 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.3 $
 */
public class Provider {

	private School school = null;
	private ProviderAccountingProperties properties = null;
			
	/**
	 * Constructs a new provider object by retrieving the school
	 * and accounting properties for the provider.
	 * @param schoolId the school id for the provider
	 */
	public Provider(int schoolId) {
		try {
			SchoolHome home = (SchoolHome) com.idega.data.IDOLookup.getHome(School.class);
			school = home.findByPrimaryKey(new Integer(schoolId));
			if (school != null) {
				ProviderAccountingPropertiesHome h = (ProviderAccountingPropertiesHome) com.idega.data.IDOLookup.getHome(ProviderAccountingProperties.class);
				properties = h.findByPrimaryKey(new Integer(schoolId)); 
			}
		} catch (RemoteException e) {
		} catch (FinderException e) {}
	}
	
	/**
	 * Returns the school object for this provider.
	 */
	public School getSchool() {
		return school;
	}
	
	/**
	 * Returns the accounting properties for this provider.
	 */
	public ProviderAccountingProperties getAccountingProperties() {
		return properties;
	}
	
	public String getStatisticsType() {
		if (properties != null) {
			String s = properties.getStatisticsType();
			if (s != null) {
				return s;
			} else {
				return "";
			}
		} else {
			return "";
		}
	}

	public boolean getPaymentByInvoice() {
		if (properties != null) {
			return properties.getPaymentByInvoice();
		} else {
			return false;
		}
	}

	public String getPostgiro() {
		if (properties != null) {
			return properties.getPostgiro();
		} else {
			return "";
		}
	}

	public String getBankgiro() {
		if (properties != null) {
			return properties.getBankgiro();
		} else {
			return "";
		}
	}

	public String getOwnPosting() {
		if (properties != null) {
			return properties.getOwnPosting();
		} else {
			return null;
		}
	}

	public String getDoublePosting() {
		if (properties != null) {
			return properties.getDoublePosting();
		} else {
			return null;
		}
	}
}
