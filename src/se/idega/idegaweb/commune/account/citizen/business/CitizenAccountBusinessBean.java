/*
 * $Id: CitizenAccountBusinessBean.java,v 1.2 2002/07/22 10:36:12 palli Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.account.citizen.business;

import com.idega.block.process.business.CaseBusinessBean;
import com.idega.core.data.Address;
import com.idega.data.IDOLookup;
import com.idega.user.data.UserHome;
import com.idega.user.data.User;

import se.idega.idegaweb.commune.account.citizen.data.AdminListOfApplications;
import se.idega.idegaweb.commune.account.citizen.data.CitizenAccount;
import se.idega.idegaweb.commune.account.citizen.data.CitizenAccountHome;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class CitizenAccountBusinessBean extends CaseBusinessBean implements CitizenAccountBusiness {
	public boolean insertApplication(User user, String pid, String email, String phoneHome, String phoneWork) {
		try {
			CitizenAccount application = ((CitizenAccountHome) IDOLookup.getHome(CitizenAccount.class)).create();
			application.setPID(pid);
			if (user != null)
				application.setOwner(user);
			application.setPhoneHome(phoneHome);
			if (email != null)
				application.setEmail(email);
			if (phoneWork != null)
				application.setPhoneWork(phoneWork);
			application.setCaseStatus(getCaseStatusOpen());

			application.store();
		}
		catch (Exception e) {
			e.printStackTrace();

			return false;
		}

		return true;
	}

	public User getUser(String pid) {
		User user = null;
		try {
			user = ((UserHome) IDOLookup.getHome(User.class)).findByPersonalID(pid);
		}
		catch (Exception e) {
			return null;
		}

		return user;
	}

	/**
	 * Method getListOfUnapprovedApplications.
	 * 
	 * @return A {@link java.util.List List} of {@link se.idega.idegaweb.commune.citizen.data.AdminListOfApplications AdminListOfApplications} containing information about the unhandled applications in the system.
	 */
	public List getListOfUnapprovedApplications() {
		Vector li = new Vector();
		try {
			CitizenAccountHome home = (CitizenAccountHome) IDOLookup.getHome(CitizenAccount.class);
			Collection cases = home.findAllCasesByStatus(getCaseStatusOpen());
			Iterator it = cases.iterator();
			while (it.hasNext()) {
				CitizenAccount account = (CitizenAccount) it.next();
				User user = account.getOwner();
				AdminListOfApplications apps = new AdminListOfApplications();
				apps.setPID(account.getPID());
				apps.setId(((Integer) account.getPrimaryKey()).toString());
				if (user != null) {
					apps.setName(user.getName());
					Collection addresses = user.getAddresses();
					Iterator it2 = addresses.iterator();
					Address address = null;
					if (it2.hasNext())
						address = (Address) it2.next();
					if (address != null) {
						StringBuffer fullAddress = new StringBuffer(address.getStreetName());
						fullAddress.append(" ");
						fullAddress.append(address.getStreetNumber());
						apps.setAddress(fullAddress.toString());
					}
				}

				li.addElement(apps);
			}
		}
		catch (FinderException e) {
			return null;
		}
		catch (RemoteException e) {
			return null;
		}

		return li;
	}
}