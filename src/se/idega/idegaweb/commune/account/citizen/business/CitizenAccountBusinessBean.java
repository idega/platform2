/*
 * $Id: CitizenAccountBusinessBean.java,v 1.17 2002/11/04 11:27:47 staffan Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.account.citizen.business;

import com.idega.block.process.business.CaseBusinessBean;
import com.idega.block.process.data.*;
import com.idega.core.data.Address;
import com.idega.data.IDOLookup;
import com.idega.user.data.*;
import java.rmi.RemoteException;
import java.util.*;
import javax.ejb.*;
import se.idega.idegaweb.commune.account.business.*;
import se.idega.idegaweb.commune.account.citizen.data.*;
import se.idega.idegaweb.commune.account.data.AccountApplication;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class CitizenAccountBusinessBean extends AccountApplicationBusinessBean implements CitizenAccountBusiness, AccountBusiness {
	private boolean acceptApplicationOnCreation = true;

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

			int applicationID = ((Integer) application.getPrimaryKey()).intValue();
			if (acceptApplicationOnCreation) {
				acceptApplication(applicationID, user);
			}
		}
		catch (Exception e) {
			e.printStackTrace();

			return false;
		}

		return true;
	}

    public boolean insertApplication
        (final String name, final int genderId, final String pid,
         final Date date,
         final String email, final String phoneHome, final String phoneWork,
         final String custodian1Pid, final String custodian1CivilStatus,
         final String custodian2Pid, final String custodian2CivilStatus,
         final String street, final String zipCode, final String city)
        throws RemoteException {
		try {
            final CitizenAccountHome citizenAccountHome = (CitizenAccountHome)
                    IDOLookup.getHome(CitizenAccount.class);
			final CitizenAccount application = citizenAccountHome.create ();
			application.setApplicantName (name != null ? name : "");
			application.setGenderId (genderId);
			application.setPID (pid != null ? pid : "");
            application.setBirthDate (date);
			application.setPhoneHome (phoneHome != null ? phoneHome : "");
            application.setEmail (email != null ? email : "");
            application.setPhoneWork (phoneWork != null ? phoneWork : "");
            application.setCustodian1Pid (custodian1Pid != null ? custodian1Pid
                                          : "");
            application.setCustodian1CivilStatus (custodian1CivilStatus != null
                                                  ? custodian1CivilStatus : "");
            application.setCustodian2Pid (custodian2Pid != null ? custodian2Pid
                                          : "");
            application.setCustodian2CivilStatus (custodian2CivilStatus != null
                                                  ? custodian2CivilStatus : "");
            application.setStreet (street != null ? street : "");
            application.setZipCode (zipCode != null ? zipCode : "");
            application.setCity (city != null ? city : "");
			application.setCaseStatus (getCaseStatusOpen());
			application.store ();
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
    }

    public Gender [] getGenders () throws RemoteException {
        final GenderHome home = (GenderHome) IDOLookup.getHome (Gender.class);
        try {
            final Collection genderCollection = home.findAllGenders ();
            final Gender [] genders
                    = (Gender []) genderCollection.toArray (new Gender [0]);
            return genders;
        } catch (Exception e) {
            e.printStackTrace ();
            throw new RemoteException (e.getMessage ());
        }
    }

	public User getUser(String pid) {
		User user = null;
		try {
			StringBuffer userPid = new StringBuffer(pid);
			int i = pid.indexOf('-');
			if (i != -1) {
				userPid.deleteCharAt(i);
				pid = userPid.toString();
			}
			if (userPid.length() == 10) {
				userPid.insert(0, "19");
			}
			user = ((UserHome) IDOLookup.getHome(User.class)).findByPersonalID(userPid.toString());
		}
		catch (RemoteException e) {
			return null;
		}
		catch (FinderException e) {
			if (pid.length() == 10) {
				StringBuffer userPid = new StringBuffer("20");
				userPid.append(pid);
				try {
					user = ((UserHome) IDOLookup.getHome(User.class)).findByPersonalID(userPid.toString());
				}
				catch (Exception ex) {
					return null;
				}
			}
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
				if (user == null) {
                    // There's no user with this pid in the system
                    apps.setName (account.getApplicantName ());
                    apps.setAddress (account.getStreet () + "; "
                                     + account.getZipCode () + " "
                                     + account.getCity ());
                } else {
                    // User is allready with this pid in the system
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

	protected AccountApplication getApplication(int applicationID) throws RemoteException, FinderException {
		return getAccount(applicationID);
	}

	public CitizenAccount getAccount(int id) throws RemoteException, FinderException {
		CitizenAccountHome home = (CitizenAccountHome) IDOLookup.getHome(CitizenAccount.class);

		return (CitizenAccount) home.findByPrimaryKeyIDO(new Integer(id));
	}

	protected Class getCaseEntityClass() {
		return CitizenAccount.class;
	}

	public void acceptApplication
        (final int applicationID, final User performer)
        throws RemoteException, CreateException, FinderException {
        final CitizenAccount applicant = getAccount (applicationID);
        final String name = applicant.getApplicantName();
        final int spaceIndex = name.indexOf(" ");
		final String firstName
                = spaceIndex != -1 ? name.substring(0, spaceIndex) : "";
		final String lastName = spaceIndex != -1
                ? name.substring(spaceIndex + 1, name.length ()) : name;
        final CommuneUserBusiness business = getUserBusiness ();
        //final User user = business.createCitizen (firstName, null, lastName, applicant.getPID ());
        final User user = business.createCitizenByPersonalIDIfDoesNotExist
        (firstName, "", lastName, applicant.getPID ());
		applicant.setOwner (performer);
		super.acceptApplication (applicationID, performer);
	}

	public void rejectApplication(int applicationID, User performer, String reasonDescription) throws RemoteException, CreateException, FinderException {
		super.rejectApplication(applicationID, performer, reasonDescription);
	}
	/**
	 * @see se.idega.idegaweb.commune.account.business.AccountBusiness#getAllAcceptedApplications()
	 */
	public Collection getAllAcceptedApplications() throws FinderException, RemoteException {
		/**
		 * @todo Implement
		 */
		return null;
	}

	/**
	 * @see se.idega.idegaweb.commune.account.business.AccountBusiness#getAllPendingApplications()
	 */
	public Collection getAllPendingApplications() throws FinderException, RemoteException {
		/**
		 * @todo Implement
		 */
		return null;
	}

	/**
	 * @see se.idega.idegaweb.commune.account.business.AccountBusiness#getAllRejectedApplications()
	 */
	public Collection getAllRejectedApplications() throws FinderException, RemoteException {
		/**
		 * @todo Implement
		 */
		return null;
	}
	/**
	 * Overrided from superclass
	 */
	protected User createUserForApplication(AccountApplication theCase) throws CreateException, RemoteException {
		return createCitizenForApplication(theCase);
	}

	/**
	 * Creates a citizen in the Commune system
	 */
	protected User createCitizenForApplication(AccountApplication theCase) throws CreateException, RemoteException {
		String firstName = theCase.getApplicantName().substring(0, theCase.getApplicantName().indexOf(" "));
		String lastName = theCase.getApplicantName().substring(theCase.getApplicantName().lastIndexOf(" ") + 1, theCase.getApplicantName().length());
		User user = null;
		user = getUserBusiness().createCitizen(firstName, null, lastName, null);
		theCase.setOwner(user);
		return user;
	}
	
	public String getAcceptMessageSubject()
	{
		return this.getLocalizedString("acc.app.citizen.appr.subj", "Your citizen account application has been approved");
	}

	public String getRejectMessageSubject()
	{
		return this.getLocalizedString("acc.app.citizen.rej.subj", "Your citizen account application has been rejected");
	}
}
