/*
 * $Id: CitizenAccountBusinessBean.java,v 1.34 2002/11/27 13:41:40 staffan Exp $
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
import com.idega.core.accesscontrol.business.UserHasLoginException;
import com.idega.core.data.*;
import com.idega.data.*;
import com.idega.user.data.*;
import com.idega.util.IWTimestamp;
import is.idega.idegaweb.member.business.*;
import java.rmi.RemoteException;
import java.util.*;
import javax.ejb.*;
import se.idega.idegaweb.commune.account.business.*;
import se.idega.idegaweb.commune.account.citizen.data.*;
import se.idega.idegaweb.commune.account.data.AccountApplication;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.util.PIDChecker;

/**
 * Last modified: $Date: 2002/11/27 13:41:40 $ by $Author: staffan $
 *
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.34 $
 */
public class CitizenAccountBusinessBean extends AccountApplicationBusinessBean
    implements CitizenAccountBusiness, AccountBusiness {
	private boolean acceptApplicationOnCreation = true;

	/**
	 * Creates an application for CitizenAccount for a user with a personalId that is in the system.
	 * @param user The user that makes the application
	 * @param ssn 	The PersonalId of the User to apply for.
	 * @param email Email of the user
	 * @param phoneHome the Home phone of the user
	 * @param phoneWork the Work phone of the user
	 * @return Integer appliaction id or null if insertion was unsuccessful
	 * @throws UserHasLoginException If A User already has a login in the system.
	 */
	public Integer insertApplication (User user, String ssn, String email,
                                      String phoneHome, String phoneWork)
        throws UserHasLoginException, RemoteException {
        CitizenAccount application = null;
		try {
			application = ((CitizenAccountHome) IDOLookup.getHome
                           (CitizenAccount.class)).create();
			application.setSsn (ssn);
			if (user != null) {
				application.setOwner(user);
				if (user.getName() != null) {
					application.setApplicantName(user.getName());
				}
			}
			application.setPhoneHome(phoneHome);
			if (email != null)
				application.setEmail(email);
			if (phoneWork != null)
				application.setPhoneWork(phoneWork);
			application.setCaseStatus(getCaseStatusOpen());

			application.store();

			int applicationID
                    = ((Integer) application.getPrimaryKey()).intValue();
			if (acceptApplicationOnCreation) {
                acceptApplication(applicationID, user);
			}
		}
		catch (Exception e) {
			if (e instanceof UserHasLoginException) {
				throw (UserHasLoginException) e;	
			}
			e.printStackTrace();

			return null;
		}

		return (Integer) (application == null ? null
                          : application.getPrimaryKey());
	}


    public Integer insertApplication
        (final String name, final String ssn, final String email,
         final String phoneHome, final String phoneWork,
         final String street, final String zipCode, final String city,
         final String civilStatus, final boolean hasCohabitant,
         final int childrenCount, final String applicationReason)
        throws RemoteException {
		CitizenAccount application = null;
        try {
            final CitizenAccountHome citizenAccountHome = (CitizenAccountHome)
                    IDOLookup.getHome(CitizenAccount.class);
			application = citizenAccountHome.create ();
			application.setApplicantName (name != null ? name : "");
			application.setSsn (ssn != null ? ssn : "");
            application.setEmail (email != null ? email : "");
			application.setPhoneHome (phoneHome != null ? phoneHome : "");
            application.setPhoneWork (phoneWork != null ? phoneWork : "");
            application.setStreet (street != null ? street : "");
            application.setZipCode (zipCode != null ? zipCode : "");
            application.setCity (city != null ? city : "");
            application.setCivilStatus (civilStatus != null ? civilStatus : "");
            application.setHasCohabitant (hasCohabitant);
            application.setChildrenCount (childrenCount);
            application.setApplicationReason (applicationReason != null
                                              ? applicationReason : "");
			application.setCaseStatus (getCaseStatusOpen());
			application.store ();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
        
		return (Integer) (application == null ? null
                          : application.getPrimaryKey());
    }

    public Integer insertCohabitant
        (final Integer applicationId, final String firstName,
         final String lastName, final String ssn, final String civilStatus,
         final String phoneWork) throws RemoteException,CreateException {
		CitizenApplicantCohabitant cohabitant = null;
        try {
            final CitizenApplicantCohabitantHome citizenApplicantCohabitantHome
                    = (CitizenApplicantCohabitantHome)
                    IDOLookup.getHome(CitizenApplicantCohabitant.class);
			cohabitant = citizenApplicantCohabitantHome.create ();
            cohabitant.setApplicationId (applicationId.intValue ());
            cohabitant.setFirstName (firstName);
            cohabitant.setLastName (lastName);
            cohabitant.setSsn (ssn);
            cohabitant.setCivilStatus (civilStatus);
            cohabitant.setPhoneWork (phoneWork);
			cohabitant.store ();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
        
		return (Integer) (cohabitant == null ? null
                          : cohabitant.getPrimaryKey());
    }

    public Integer insertChildren
        (final Integer applicationId, final String firstName,
         final String lastName, final String ssn)
        throws RemoteException,CreateException {
		CitizenApplicantChildren children = null;
        try {
            final CitizenApplicantChildrenHome citizenApplicantChildrenHome
                    = (CitizenApplicantChildrenHome)
                    IDOLookup.getHome(CitizenApplicantChildren.class);
			children = citizenApplicantChildrenHome.create ();
            children.setApplicationId (applicationId.intValue ());
            children.setFirstName (firstName);
            children.setLastName (lastName);
            children.setSsn (ssn);
			children.store ();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
        
		return (Integer) (children == null ? null : children.getPrimaryKey());
    }

    public Integer insertMovingTo
        (final Integer applicationId, final String address, final String date,
         final String housingType, final String propertyType)
        throws RemoteException, CreateException {
		CitizenApplicantMovingTo movingTo = null;
        try {
            final CitizenApplicantMovingToHome citizenApplicantMovingToHome
                    = (CitizenApplicantMovingToHome)
                    IDOLookup.getHome(CitizenApplicantMovingTo.class);
			movingTo = citizenApplicantMovingToHome.create ();
            movingTo.setApplicationId (applicationId.intValue ());
            movingTo.setAddress (address);
            movingTo.setMovingInDate (date);
            movingTo.setHousingType (housingType);
            movingTo.setPropertyType (propertyType);
			movingTo.store ();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
        
		return (Integer) (movingTo == null ? null : movingTo.getPrimaryKey());
    }

    public Integer insertPutChildren (final Integer applicationId,
                               final String currentKommun)
        throws RemoteException, CreateException {
		CitizenApplicantPutChildren putChildren = null;
        try {
            final CitizenApplicantPutChildrenHome
                    citizenApplicantPutChildrenHome
                    = (CitizenApplicantPutChildrenHome)
                    IDOLookup.getHome(CitizenApplicantPutChildren.class);
			putChildren = citizenApplicantPutChildrenHome.create ();
            putChildren.setApplicationId (applicationId.intValue ());
            putChildren.setCurrentKommun (currentKommun);
			putChildren.store ();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
        
		return (Integer) (putChildren == null ? null
                          : putChildren.getPrimaryKey());
    }

	public User getUser(String ssn) {
		User user = null;
		try {
			StringBuffer userSsn = new StringBuffer(ssn);
			int i = ssn.indexOf('-');
			if (i != -1) {
				userSsn.deleteCharAt(i);
				ssn = userSsn.toString();
			}
			if (userSsn.length() == 10) {
				userSsn.insert(0, "19");
			}
			user = ((UserHome) IDOLookup.getHome(User.class)).findByPersonalID(userSsn.toString());
		}
		catch (RemoteException e) {
			return null;
		}
		catch (FinderException e) {
			if (ssn.length() == 10) {
				StringBuffer userSsn = new StringBuffer("20");
				userSsn.append(ssn);
				try {
					user = ((UserHome) IDOLookup.getHome(User.class)).findByPersonalID(userSsn.toString());
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
				apps.setPID (account.getSsn());
				apps.setId(((Integer) account.getPrimaryKey()).toString());
				if (user == null) {
                    // There's no user with this ssn in the system
                    apps.setName (account.getApplicantName ());
                    apps.setAddress (account.getStreet () + "; "
                                     + account.getZipCode () + " "
                                     + account.getCity ());
                } else {
                    // User is allready with this ssn in the system
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
        final int spaceIndex = name != null ? name.indexOf(" ") : -1;
		final String firstName
                = spaceIndex != -1 ? name.substring(0, spaceIndex) : "";
		final String lastName = spaceIndex != -1
                ? name.substring(spaceIndex + 1, name.length ())
                : (name != null ? name : "");
        final String ssn = applicant.getSsn ();
        final GenderHome genderHome
                = (GenderHome) IDOLookup.getHome(Gender.class);
        final PIDChecker pidChecker = PIDChecker.getInstance ();
        final Gender gender = pidChecker.isFemale (ssn)
                ? genderHome.getFemaleGender () : genderHome.getMaleGender ();
        final Date birthDate = pidChecker.getDateFromPersonalID (ssn);
        final IWTimestamp timestamp = birthDate != null
                ? new IWTimestamp (birthDate.getTime ()) : null;
        final CommuneUserBusiness userBusiness = getUserBusiness ();
        final User user = userBusiness.createCitizenByPersonalIDIfDoesNotExist
                (firstName, "", lastName, ssn, gender, timestamp);
                 
        try {
            Email email = ((EmailHome) IDOLookup.getHome(Email.class)).create();
            email.setEmailAddress(applicant.getEmail());
            email.store();
            user.addEmail(email);
            Phone homePhone = null;
            
            if (applicant.getPhoneHome() != null) {
                homePhone
                        = ((PhoneHome) IDOLookup.getHome(Phone.class)).create();
                homePhone.setNumber(applicant.getPhoneHome());
                homePhone.setPhoneTypeId(PhoneBMPBean.getHomeNumberID());
                homePhone.store();
                user.addPhone (homePhone);
            }
            
            if (applicant.getPhoneWork() != null) {
                final Phone workPhone
                        = ((PhoneHome) IDOLookup.getHome(Phone.class)).create();
                workPhone.setNumber(applicant.getPhoneWork());
                workPhone.setPhoneTypeId(PhoneBMPBean.getWorkNumberID());
                workPhone.store();
                user.addPhone(workPhone);
            }

            final MemberFamilyLogic familyLogic = (MemberFamilyLogic)
                    getServiceInstance (MemberFamilyLogic.class);
            if (applicant.hasCohabitant ()) {
                final CitizenApplicantCohabitantHome home
                        = (CitizenApplicantCohabitantHome)
                        IDOLookup.getHome(CitizenApplicantCohabitant.class);
                final CitizenApplicantCohabitant cohabitant
                        = home.findByApplicationId (applicationID);
                final String cohabitantSsn = cohabitant.getSsn ();
                final Gender cohabitantGender = pidChecker.isFemale (ssn)
                        ? genderHome.getFemaleGender ()
                        : genderHome.getMaleGender ();
                final Date cohabitantBirth
                        = pidChecker.getDateFromPersonalID (ssn);
                final IWTimestamp cohabitantTimestamp = cohabitantBirth != null
                        ? new IWTimestamp (cohabitantBirth.getTime ()) : null;
                final User cohabitantUser
                        = userBusiness.createCitizenByPersonalIDIfDoesNotExist
                        (cohabitant.getFirstName (), "",
                         cohabitant.getLastName (), cohabitantSsn,
                         cohabitantGender, cohabitantTimestamp);
                familyLogic.setAsSpouseFor (user, cohabitantUser);
                final Phone phone
                        = ((PhoneHome) IDOLookup.getHome(Phone.class)).create();
                phone.setNumber (cohabitant.getPhoneWork());
                phone.setPhoneTypeId (PhoneBMPBean.getWorkNumberID());
                phone.store ();
                cohabitantUser.addPhone (phone);
                if (homePhone != null) {
                    cohabitantUser.addPhone (homePhone);
                }
            }
            if (applicant.getChildrenCount () > 0) {
                final CitizenApplicantChildrenHome home
                        = (CitizenApplicantChildrenHome)
                        IDOLookup.getHome(CitizenApplicantChildren.class);
                final CitizenApplicantChildren [] children
                        = home.findByApplicationId (applicationID);
                for (int i = 0; i < children.length; i++) {
                    final String childrenSsn = children [i].getSsn ();
                    final Gender childrenGender = pidChecker.isFemale (ssn)
                            ? genderHome.getFemaleGender ()
                            : genderHome.getMaleGender ();
                    final Date childrenBirth
                            = pidChecker.getDateFromPersonalID (ssn);
                    final IWTimestamp childrenTimestamp = childrenBirth != null
                            ? new IWTimestamp (childrenBirth.getTime ()) : null;
                    final User childrenUser = userBusiness
                            .createCitizenByPersonalIDIfDoesNotExist
                            (children [i].getFirstName (), "",
                             children [i].getLastName (), childrenSsn,
                             childrenGender, childrenTimestamp);
                    familyLogic.setAsParentFor (user, childrenUser);
                    if (homePhone != null) {
                        childrenUser.addPhone (homePhone);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace ();
            throw new CreateException(e.getMessage());
        }

		applicant.setOwner (user);
        applicant.store ();
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
	protected User createCitizenForApplication (AccountApplication theCase)
        throws CreateException, RemoteException {
        final CitizenAccount applicant = (CitizenAccount) theCase;
        final String name = applicant.getApplicantName();
        final int spaceIndex = name != null ? name.indexOf(" ") : -1;
		final String firstName
                = spaceIndex != -1 ? name.substring(0, spaceIndex) : "";
		final String lastName = spaceIndex != -1
                ? name.substring(spaceIndex + 1, name.length ())
                : (name != null ? name : "");
        final String ssn = applicant.getSsn ();
        final GenderHome genderHome
                = (GenderHome) IDOLookup.getHome(Gender.class);
        final PIDChecker pidChecker = PIDChecker.getInstance ();
        Gender gender = null;
        try {
            gender = pidChecker.isFemale (ssn) ? genderHome.getFemaleGender ()
                    : genderHome.getMaleGender ();
        } catch (final FinderException e) {
            throw new CreateException (e.getMessage ());
        }
        final Date birthDate = pidChecker.getDateFromPersonalID (ssn);
        final IWTimestamp timestamp = birthDate != null
                ? new IWTimestamp (birthDate.getTime ()) : null;
        final CommuneUserBusiness userBusiness = getUserBusiness ();
        final User user = userBusiness.createCitizenByPersonalIDIfDoesNotExist
                (firstName, "", lastName, ssn, gender, timestamp);

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
