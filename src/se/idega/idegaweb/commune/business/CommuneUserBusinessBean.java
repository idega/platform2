package se.idega.idegaweb.commune.business;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolUserBusiness;
import com.idega.block.school.data.School;
import com.idega.business.*;
import com.idega.core.accesscontrol.business.LoginCreateException;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.business.AddressBusiness;
import com.idega.core.data.Address;
import com.idega.core.data.Country;
import com.idega.core.data.CountryHome;
import com.idega.core.data.Email;
import com.idega.core.data.Phone;
import com.idega.core.data.PostalCode;
import com.idega.data.*;
import com.idega.idegaweb.*;
import com.idega.user.business.*;
import com.idega.user.data.*;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

import is.idega.idegaweb.member.business.MemberFamilyLogic;
import is.idega.idegaweb.member.business.NoChildrenFound;
import is.idega.idegaweb.member.business.NoCustodianFound;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.*;

import javax.ejb.*;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import se.idega.idegaweb.commune.presentation.CommuneBlock;
/**
 * Title:        se.idega.idegaweb.commune.business.CommuneUserBusinessBean
 * Description:	Use this business class to handle Citizen information
 * Copyright:    Copyright (c) 2001
 * Company:      idega software
 * @author AguraIT and idega
 * @version 1.0
 */
public class CommuneUserBusinessBean extends UserBusinessBean implements CommuneUserBusiness {

	private final String ROOT_CITIZEN_GROUP_ID_PARAMETER_NAME = "commune_id";
	private final String ROOT_SPECIAL_CITIZEN_GROUP_ID_PARAMETER_NAME = "special_citizen_group_id";
	private final String ROOT_PROTECTED_CITIZEN_GROUP_ID_PARAMETER_NAME = "protected_citizen_group_id";
	private final String ROOT_CUSTOMER_CHOICE_GROUP_ID_PARAMETER_NAME = "customer_choice_group_id";
	private Group rootCitizenGroup;
	private Group rootSpecialCitizenGroup;
	private Group rootProtectedCitizenGroup;
	private Group rootCustomerChoiceGroup;

	/**
	 * Creates a new citizen with a firstname,middlename, lastname and personalID where middlename and personalID can be null.<br>
	 * Also adds the citizen to the Commune Root Group.
	 */
	public User createCitizen(String firstname, String middlename, String lastname, String personalID) throws CreateException, RemoteException {
		return this.createCitizen(firstname, middlename, lastname, personalID, getGenderFromPin(personalID), getBirthDateFromPin(personalID));
	}
	/**
	 * Creates a new citizen with a firstname,middlename, lastname, personalID,
	   * gender and date of birth where middlename, personalID,gender,dateofbirth
	   * can be null.<br>
	 * Also adds the citizen to the Commune Root Group.
	 */

	public User createCitizen(final String firstname, final String middlename, final String lastname, final String personalID, final Gender gender, final IWTimestamp dateOfBirth) throws CreateException, RemoteException {
		User newUser = null;
		try {
			newUser = createUser(firstname, middlename, lastname, personalID, gender, dateOfBirth, getRootCitizenGroup());
		}
		catch (final Exception e) {
			e.printStackTrace();
			throw new IDOCreateException(e);
		}
		return newUser;
	}

	/**
	 * Creates a new citizen with a firstname,middlename, lastname, personalID,
		 * gender and date of birth where middlename, personalID,gender,dateofbirth
		 * can be null.<br>
	 * Also adds the citizen to the Commune Root Group.
	 */
	public User createCitizen(final String firstname, final String middlename, final String lastname, final String personalID, final Gender gender, final IWTimestamp dateOfBirth, final Group rootGroup) throws CreateException, RemoteException {
		User newUser = null;
		try {
			newUser = createUser(firstname, middlename, lastname, personalID, gender, dateOfBirth, rootGroup);
		}
		catch (final Exception e) {
			e.printStackTrace();
			throw new IDOCreateException(e);
		}
		return newUser;
	}

	/**
	 * Finds and updates or Creates a new citizen with a firstname,middlename, lastname and personalID.<br>
	 * Also adds the citizen to the Commune Root Group.
	 */
	public User createCitizenByPersonalIDIfDoesNotExist(String firstName, String middleName, String lastName, String personalID) throws CreateException, RemoteException {
		return createCitizenByPersonalIDIfDoesNotExist(firstName, middleName, lastName, personalID, getGenderFromPin(personalID), getBirthDateFromPin(personalID));
	}
	public User createCitizenByPersonalIDIfDoesNotExist(String firstName, String middleName, String lastName, String personalID, Gender gender, IWTimestamp dateOfBirth) throws CreateException, RemoteException {
		User user = null;
		try {
			final UserHome home = getUserHome();
			user = home.findByPersonalID(personalID);
			//update if found
			firstName = (firstName == null) ? "" : firstName;
			middleName = (middleName == null) ? "" : middleName;
			lastName = (lastName == null) ? "" : lastName;
			
			StringBuffer fullName = new StringBuffer();
			firstName = (firstName == null) ? "" : firstName;
			middleName = (middleName == null) ? "" : middleName;
			lastName = (lastName == null) ? "" : lastName;
			fullName.append(firstName).append(" ").append(middleName).append(" ").append(lastName);
			user.setFullName(fullName.toString());
			
			if (gender != null)
				user.setGender((Integer) gender.getPrimaryKey());
			if (dateOfBirth != null)
				user.setDateOfBirth(dateOfBirth.getDate());
			user.store();
		}
		catch (final FinderException e) {
			//create a new user
			if (user == null) {
				user = createCitizen(firstName, middleName, lastName, personalID, gender, dateOfBirth);
			}
		}
		return user;
	}

	/**
	 * Creates a new special citizen with a firstname,middlename, lastname,
	 * personalID, gender and date of birth where middlename, personalID,gender,
	 * dateofbirth can be null.<br> Also adds the citizen to the Commune Special
	 * Citizen Root Group.
	 */
	public User createSpecialCitizen(final String firstname, final String middlename, final String lastname, final String personalID, final Gender gender, final IWTimestamp dateOfBirth) throws CreateException, RemoteException {
		User newUser = null;
		try {
			newUser = createUser(firstname, middlename, lastname, personalID, gender, dateOfBirth, getRootSpecialCitizenGroup());
		}
		catch (final Exception e) {
			e.printStackTrace();
			throw new IDOCreateException(e);
		}
		return newUser;
	}

	/**
	 * Finds and updates or Creates a new special citizen with a firstname,
	 * middlename, lastname and personalID.<br> Also adds the citizen to the
	 * Commune Special Citizen Root Group (people who don't live in Nacka).
	 */
	public User createSpecialCitizenByPersonalIDIfDoesNotExist(String firstName, String middleName, String lastName, String personalID) throws CreateException, RemoteException {
		return createCitizenByPersonalIDIfDoesNotExist(firstName, middleName, lastName, personalID, getGenderFromPin(personalID), getBirthDateFromPin(personalID));
	}
	public User createSpecialCitizenByPersonalIDIfDoesNotExist(String firstName, String middleName, String lastName, String personalID, Gender gender, IWTimestamp dateOfBirth) throws CreateException, RemoteException {
		User user = null;
		try {
			final UserHome home = getUserHome();
			user = home.findByPersonalID(personalID);
			//update if found
			StringBuffer fullName = new StringBuffer();
			firstName = (firstName == null) ? "" : firstName;
			middleName = (middleName == null) ? "" : middleName;
			lastName = (lastName == null) ? "" : lastName;
			fullName.append(firstName).append(" ").append(middleName).append(" ").append(lastName);
			user.setFullName(fullName.toString());
			if (gender != null)
				user.setGender((Integer) gender.getPrimaryKey());
			if (dateOfBirth != null)
				user.setDateOfBirth(dateOfBirth.getDate());
			user.store();
		}
		catch (final FinderException e) {
			//create a new user
			if (user == null) {
				user = createSpecialCitizen(firstName, middleName, lastName, personalID, gender, dateOfBirth);
			}
		}
		return user;
	}

	/**
	 * Creates a new Commune Administrator with a firstname,middlename, lastname and personalID where middlename and personalID can be null
	 */
	public User createCommuneAdministrator(String firstname, String middlename, String lastname) throws CreateException, RemoteException {
		User newUser;
		/**
		 * @todo: put the user in an administrator group
		 */
		newUser = createUser(firstname, middlename, lastname);
		return newUser;
	}
	/**
	 * Creates a new Administrator whith a with a firstname,middlename, lastname and school where middlename  can be null
	 */
	public User createProviderAdministrator(String firstname, String middlename, String lastname, School school) throws javax.ejb.FinderException, CreateException, RemoteException {
		User newUser;
		SchoolBusiness schlBuiz = (SchoolBusiness) getServiceInstance(SchoolBusiness.class);
		Group rootSchoolAdminGroup = getRootProviderAdministratorGroup();
		Group schoolGroup = getGroupBusiness().getGroupHome().findByPrimaryKey(new Integer(school.getHeadmasterGroupId()));
		newUser = createUser(firstname, middlename, lastname, rootSchoolAdminGroup);
		//rootSchoolAdminGroup.addGroup(newUser);
		schoolGroup.addGroup(newUser);
		return newUser;
	}
	/**
	 * Creates a new Administrator whith a with a firstname,middlename, lastname and school where middlename  can be null
	 */
	public User createSchoolAdministrator(String firstname, String middlename, String lastname, School school) throws javax.ejb.FinderException, CreateException, RemoteException {
		User newUser;
		SchoolBusiness schlBuiz = (SchoolBusiness) getServiceInstance(SchoolBusiness.class);
		Group rootSchoolAdminGroup = getRootSchoolAdministratorGroup();
		Group schoolGroup = getGroupBusiness().getGroupHome().findByPrimaryKey(new Integer(school.getHeadmasterGroupId()));
		newUser = createUser(firstname, middlename, lastname, rootSchoolAdminGroup);
		//rootSchoolAdminGroup.addGroup(newUser);
		schoolGroup.addGroup(newUser);
		return newUser;
	}
	/**
	 * Generates a user login for the user with login derived from the users name and a random password
	 * @param User the User to create login for
	 * @throws LoginCreateException If an error occurs creating login for the user.
	 */
	//public LoginTable generateUserLogin(User user) throws LoginCreateException, RemoteException
	//{
	//		return generateUserLogin(user);
	//}
	/**
	 * Creates (if not available) and returns the default usergroup all
	   * citizens, read from imports, are members of.
	 * throws a CreateException if it failed to locate or create the group.
	 */
	public Group getRootCitizenGroup() throws CreateException, FinderException, RemoteException {
		//create the default group

		if (rootCitizenGroup != null)
			return rootCitizenGroup;

		final IWApplicationContext iwc = getIWApplicationContext();
		final IWMainApplicationSettings settings = iwc.getApplicationSettings();
		String groupId = (String) settings.getProperty(ROOT_CITIZEN_GROUP_ID_PARAMETER_NAME);
		if (groupId != null) {
			final GroupHome groupHome = getGroupHome();
			rootCitizenGroup = groupHome.findByPrimaryKey(new Integer(groupId));
		}
		else {
			System.err.println("trying to store Commune Root group");
			/**@todo this seems a wrong way to do things**/
			final GroupTypeHome typeHome = (GroupTypeHome) getIDOHome(GroupType.class);
			final GroupType type = typeHome.create();
			final GroupBusiness groupBusiness = getGroupBusiness();
			rootCitizenGroup = groupBusiness.createGroup("Commune Citizens", "The Commune Root Group.", type.getGeneralGroupTypeString());
			settings.setProperty(ROOT_CITIZEN_GROUP_ID_PARAMETER_NAME, (Integer) rootCitizenGroup.getPrimaryKey());
		}
		return rootCitizenGroup;
	}
	/**
	 * Creates (if not available) and returns the default usergroup for  all
	 * citizens not living in the commune, read from imports. throws a
	 * CreateException if it failed to locate or create the group.
	 */
	public Group getRootSpecialCitizenGroup() throws CreateException, FinderException, RemoteException {
		//create the default group
		if (rootSpecialCitizenGroup != null)
			return rootSpecialCitizenGroup;

		final IWApplicationContext iwc = getIWApplicationContext();
		final IWMainApplicationSettings settings = iwc.getApplicationSettings();
		String groupId = (String) settings.getProperty(ROOT_SPECIAL_CITIZEN_GROUP_ID_PARAMETER_NAME);
		if (groupId != null) {
			final GroupHome groupHome = getGroupHome();
			rootSpecialCitizenGroup = groupHome.findByPrimaryKey(new Integer(groupId));
		}
		else {
			System.err.println("trying to store Commune Special Citizen Root group");
			/**@todo this seems a wrong way to do things**/
			final GroupTypeHome typeHome = (GroupTypeHome) getIDOHome(GroupType.class);
			final GroupType type = typeHome.create();
			final GroupBusiness groupBusiness = getGroupBusiness();
			rootSpecialCitizenGroup = groupBusiness.createGroup("Commune Special Citizens", "The Commune Special Citizen Root Group.", type.getGeneralGroupTypeString());
			settings.setProperty(ROOT_SPECIAL_CITIZEN_GROUP_ID_PARAMETER_NAME, (Integer) rootSpecialCitizenGroup.getPrimaryKey());
		}
		return rootSpecialCitizenGroup;
	}
	
	/**
	 * Creates (if not available) and returns 'Kundvalsgruppen'
     *
     * @return 'Kundvalsgruppen'
     * @throws CreateException if it failed to locate or create the group.
	 */
	public Group getRootCustomerChoiceGroup () throws CreateException, FinderException, RemoteException {

		if (rootCustomerChoiceGroup != null) {
			return rootCustomerChoiceGroup;
        }

		final IWApplicationContext iwc = getIWApplicationContext();
		final IWMainApplicationSettings settings = iwc.getApplicationSettings();
		String groupId = (String) settings.getProperty(ROOT_CUSTOMER_CHOICE_GROUP_ID_PARAMETER_NAME);
		if (groupId != null) {
			final GroupHome groupHome = getGroupHome();
			rootCustomerChoiceGroup = groupHome.findByPrimaryKey(new Integer(groupId));
		} else {
			final GroupHome groupHome = getGroupHome();

			System.err.println("trying to store Customer Choice Root group");
			/**@todo this seems a wrong way to do things**/
			final GroupTypeHome typeHome = (GroupTypeHome) getIDOHome(GroupType.class);
			final GroupType type = typeHome.create();
			final GroupBusiness groupBusiness = getGroupBusiness();
			rootCustomerChoiceGroup = groupBusiness.createGroup("Kundvalsgruppen", "Kundvalsgruppen", type.getGeneralGroupTypeString());
			settings.setProperty(ROOT_CUSTOMER_CHOICE_GROUP_ID_PARAMETER_NAME, (Integer) rootCustomerChoiceGroup.getPrimaryKey());
		}
		return rootCustomerChoiceGroup;
	}
	
	/**
	 * Creates (if not available) and returns the default usergroup for  all
	 * citizens not living in the commune, read from imports. throws a
	 * CreateException if it failed to locate or create the group.
	 */
	public Group getRootProtectedCitizenGroup() throws CreateException, FinderException, RemoteException {
		//create the default group
		if (rootProtectedCitizenGroup != null)
			return rootProtectedCitizenGroup;

		final IWApplicationContext iwc = getIWApplicationContext();
		final IWMainApplicationSettings settings = iwc.getApplicationSettings();
		String groupId = (String) settings.getProperty(ROOT_PROTECTED_CITIZEN_GROUP_ID_PARAMETER_NAME);
		if (groupId != null) {
			final GroupHome groupHome = getGroupHome();
			rootProtectedCitizenGroup = groupHome.findByPrimaryKey(new Integer(groupId));
		}
		else {
			System.err.println("trying to store Commune Protected Citizen Root group");
			/**@todo this seems a wrong way to do things**/
			final GroupTypeHome typeHome = (GroupTypeHome) getIDOHome(GroupType.class);
			final GroupType type = typeHome.create();
			final GroupBusiness groupBusiness = getGroupBusiness();
			rootProtectedCitizenGroup = groupBusiness.createGroup("Commune Protected Citizens", "The Commune Protected Citizen Root Group.", type.getGeneralGroupTypeString());
			settings.setProperty(ROOT_PROTECTED_CITIZEN_GROUP_ID_PARAMETER_NAME, (Integer) rootProtectedCitizenGroup.getPrimaryKey());
		}
		return rootProtectedCitizenGroup;
	}

	/**
	* Returns or creates (if not available) the default usergroup all provider(childcare) administors have as their primary group.
	* @throws CreateException if it failed to create the group.
	* @throws FinderException if it failed to locate the group.
	*/
	public Group getRootProviderAdministratorGroup() throws CreateException, FinderException, RemoteException {
		return getSchoolBusiness().getRootProviderAdministratorGroup();
		/*
		Group rootGroup = null;
		//create the default group
		String ROOT_SCHOOL_ADMINISTRATORS_GROUP = "provider_administrators_group_id";
		IWBundle bundle = getCommuneBundle();
		String groupId = bundle.getProperty(ROOT_SCHOOL_ADMINISTRATORS_GROUP);
		if (groupId != null)
		{
			rootGroup = getUserBusiness().getGroupHome().findByPrimaryKey(new Integer(groupId));
		} else
		{
			System.err.println("trying to store Commune Root school administrators group");
			//@todo this seems a wrong way to do things
			GroupTypeHome typeHome = (GroupTypeHome) this.getIDOHome(GroupType.class);
			GroupType type = typeHome.create();
			rootGroup =
				getUserBusiness().getGroupBusiness().createGroup(
					"Provider Administrators",
					"The Commune Root Provider Administrators Group.",
					type.getGeneralGroupTypeString());
			bundle.setProperty(ROOT_SCHOOL_ADMINISTRATORS_GROUP, rootGroup.getPrimaryKey().toString());
		}
		return rootGroup;
		*/
	}
	/**
	* Returns or creates (if not available) the default usergroup all school administors have as their primary group.
	* @throws CreateException if it failed to create the group.
	* @throws FinderException if it failed to locate the group.
	*/
	public Group getRootSchoolAdministratorGroup() throws CreateException, FinderException, RemoteException {
		return getSchoolBusiness().getRootSchoolAdministratorGroup();
		/*		
				Group rootGroup = null;
				//create the default group
				String ROOT_SCHOOL_ADMINISTRATORS_GROUP = "school_administrators_group_id";
				IWBundle bundle = getCommuneBundle();
				String groupId = bundle.getProperty(ROOT_SCHOOL_ADMINISTRATORS_GROUP);
				if (groupId != null)
				{
					rootGroup = getUserBusiness().getGroupHome().findByPrimaryKey(new Integer(groupId));
				} else
				{
					System.err.println("trying to store Commune Root school administrators group");
					//@todo this seems a wrong way to do things
					GroupTypeHome typeHome = (GroupTypeHome) this.getIDOHome(GroupType.class);
					GroupType type = typeHome.create();
					rootGroup =
						getUserBusiness().getGroupBusiness().createGroup(
							"School Administrators",
							"The Commune Root School Administrators Group.",
							type.getGeneralGroupTypeString());
					bundle.setProperty(ROOT_SCHOOL_ADMINISTRATORS_GROUP, rootGroup.getPrimaryKey().toString());
				}
				return rootGroup;
		*/
	}
	/**
	* Returns or creates (if not available) the default usergroup all commune administors have as their primary group.
	* @throws CreateException if it failed to create the group.
	* @throws FinderException if it failed to locate the group.
	*/
	public Group getRootCommuneAdministratorGroup() throws CreateException, FinderException, RemoteException {
		Group rootGroup = null;
		//create the default group
		String ROOT_COMMUNE_ADMINISTRATORS_GROUP = "commune_administrators_group_id";
		IWBundle bundle = getCommuneBundle();
		String groupId = bundle.getProperty(ROOT_COMMUNE_ADMINISTRATORS_GROUP);
		if (groupId != null) {
			rootGroup = getGroupHome().findByPrimaryKey(new Integer(groupId));
		}
		else {
			System.err.println("trying to store Commune administrators Root group");
			/**@todo this seems a wrong way to do things**/
			GroupTypeHome typeHome = (GroupTypeHome) this.getIDOHome(GroupType.class);
			GroupType type = typeHome.create();
			rootGroup = getGroupBusiness().createGroup("Commune Administrators", "The Commune Administrators Root Group.", type.getGeneralGroupTypeString());
			bundle.setProperty(ROOT_COMMUNE_ADMINISTRATORS_GROUP, rootGroup.getPrimaryKey().toString());
		}
		return rootGroup;
	}
	/**
	 * Gets all the users that have the CommuneAdministrator group as its primary group.
	* @return Collection of com.idega.user.data.User objects with all the users in the CommuneAdministrators group.
	* @throws FinderException if it failed to locate the group or its users.
	*/
	public Collection getAllCommuneAdministrators() throws FinderException {
		try {
			return getUsersInPrimaryGroup(getRootCommuneAdministratorGroup());
		}
		catch (Exception e) {
			throw new IDOFinderException(e);
		}
	}
	protected IWBundle getCommuneBundle() {
		return this.getIWApplicationContext().getApplication().getBundle(CommuneBlock.IW_BUNDLE_IDENTIFIER);
	}
	/**
	 * Method getFirstManagingSchoolForUser.
	 * If there is no school that the user manages then the method throws a FinderException.
	 * @param user a user
	 * @return School that is the first school that the user is a manager for.
	 * @throws javax.ejb.FinderException if ther is no school that the user manages.
	 */
	public School getFirstManagingSchoolForUser(User user) throws FinderException, RemoteException {
		try {
			Group rootGroup = getRootSchoolAdministratorGroup();
			if (user.getPrimaryGroup().equals(rootGroup)) {
				SchoolUserBusiness sub = (SchoolUserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolUserBusiness.class);
				Collection schoolIds = sub.getSchools(user);
				if (!schoolIds.isEmpty()) {
					Iterator iter = schoolIds.iterator();
					while (iter.hasNext()) {
						School school = sub.getSchoolHome().findByPrimaryKey(iter.next());
						return school;
					}
				}
			}
		}
		catch (CreateException ce) {
			ce.printStackTrace();
		}
		catch (FinderException e) {
			Collection schools = ((SchoolBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), SchoolBusiness.class)).getSchoolHome().findAllBySchoolGroup(user);
			if (!schools.isEmpty()) {
				Iterator iter = schools.iterator();
				while (iter.hasNext()) {
					return (School) iter.next();
				}
			}
		}
		throw new FinderException("No school found that " + user.getName() + " manages");
	}

	public School getFirstManagingChildCareForUser(User user) throws FinderException, RemoteException {
		try {
			Group rootGroup = getRootProviderAdministratorGroup();
			if (user.getPrimaryGroup().equals(rootGroup)) {
				SchoolUserBusiness sub = (SchoolUserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolUserBusiness.class);
				Collection schoolIds = sub.getSchools(user);
				if (!schoolIds.isEmpty()) {
					Iterator iter = schoolIds.iterator();
					while (iter.hasNext()) {
						School school = sub.getSchoolHome().findByPrimaryKey(iter.next());
						return school;
					}
				}
			}
		}
		catch (CreateException ce) {
			ce.printStackTrace();
		}
		catch (FinderException e) {
			Collection schools = ((SchoolBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), SchoolBusiness.class)).getSchoolHome().findAllBySchoolGroup(user);
			if (!schools.isEmpty()) {
				Iterator iter = schools.iterator();
				while (iter.hasNext()) {
					return (School) iter.next();
				}
			}
		}
		throw new FinderException("No childcare found that " + user.getName() + " manages");
	}
	public boolean hasCitizenAccount(User user) throws RemoteException {
		return hasUserLogin(user);
	}
	public boolean hasCitizenAccount(int userID) throws RemoteException {
		return hasUserLogin(userID);
	}
	public SchoolBusiness getSchoolBusiness() throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolBusiness.class);
	}

	public boolean moveCitizenFromCommune(User user) throws RemoteException {

		/*UserTransaction transaction =  getSessionContext().getUserTransaction();
		
		try{
			transaction.begin();*/
		Group rootGroup = null;
		try {
			rootGroup = getRootCitizenGroup();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		Group rootSpecialGroup = null;
		try {
			rootSpecialGroup = getRootSpecialCitizenGroup();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}

		rootGroup.removeUser(user);
		
		rootSpecialGroup.addGroup(user);

		/*	transaction.commit();
		}
		catch (Exception ex) {
		 ex.printStackTrace();
		 System.err.println("Could not move the user to the root special citizen group");
		
		 try {
			transaction.rollback();
		 }
		 catch (SystemException e) {
			 e.printStackTrace();
		 }
		
		 return false;
		}*/

		return true;
	}

	public boolean moveCitizenToCommune(User user) throws RemoteException {
		/*
			UserTransaction transaction =  getSessionContext().getUserTransaction();
			
			try{
				transaction.begin();*/
		Group rootGroup = null;
		try {
			rootGroup = getRootCitizenGroup();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		Group rootSpecialGroup = null;
		try {
			rootSpecialGroup = getRootSpecialCitizenGroup();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}

		rootSpecialGroup.removeUser(user);
		
		rootGroup.addGroup(user);
		
		
		
		/*
		transaction.commit();
		}
		catch (Exception ex) {
		ex.printStackTrace();
		System.err.println("Could not move the user to the root citizen group");
		
		try {
		transaction.rollback();
		}
		catch (SystemException e) {
		 e.printStackTrace();
		}
		
		return false;
		}*/

		return true;
	}
	
	
	public boolean moveCitizenToProtectedCitizenGroup(User user) throws RemoteException {
		/*
			UserTransaction transaction =  getSessionContext().getUserTransaction();
			
			try{
				transaction.begin();*/
		Group rootProtectedGroup = null;
		try {
			rootProtectedGroup = getRootProtectedCitizenGroup();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		
		Group rootGroup = null;
		try {
			rootGroup = getRootCitizenGroup();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		Group rootSpecialGroup = null;
		try {
			rootSpecialGroup = getRootSpecialCitizenGroup();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}

		rootSpecialGroup.removeUser(user);
		rootGroup.removeUser(user);
		
		rootProtectedGroup.addGroup(user);
		
		return true;
		
	}
	

	public Phone getChildHomePhone(User child) throws RemoteException {
		Address childAddress = getUsersMainAddress(child);
		Collection parents = getParentsForChild(child);
		if (parents != null) {
			Address parentAddress;
			Iterator iter = parents.iterator();
			while (iter.hasNext()) {
				User parent = (User) iter.next();
				parentAddress = getUsersMainAddress(parent);
				if (childAddress != null && parentAddress != null) {
					if (getIfUserAddressesMatch(childAddress, parentAddress)) {
						try {
							return this.getUsersHomePhone(parent);
						}
						catch (NoPhoneFoundException npfe) {
						}
					}
				}
			}
		}

		return null;
	}

	public Phone getChildHomePhone(int childId) throws RemoteException {
		return getChildHomePhone(getUser(childId));
	}

	public boolean getIfUserAddressesMatch(Address userAddress, Address userAddressToCompare) throws RemoteException {
		if (((Integer) userAddress.getPrimaryKey()).intValue() == ((Integer) userAddressToCompare.getPrimaryKey()).intValue())
			return true;

		String address1 = userAddress.getStreetAddress().toUpperCase();
		String address2 = userAddressToCompare.getStreetAddress().toUpperCase();

		if (TextSoap.findAndCut(address1, " ").equalsIgnoreCase(TextSoap.findAndCut(address2, " "))) {
			return true;
		}

		return false;
	}

	public Collection getParentsForChild(User child) throws RemoteException {
		try {
			return getMemberFamilyLogic().getCustodiansFor(child);
		}
		catch (NoCustodianFound ncf) {
			return null;
		}
	}

	public Collection getChildrenForUser(User user) throws RemoteException {
		try {
			return getMemberFamilyLogic().getChildrenInCustodyOf(user);
		}
		catch (NoChildrenFound ncf) {
			return null;
		}
	}

	public MemberFamilyLogic getMemberFamilyLogic() throws RemoteException {
		return (MemberFamilyLogic) IBOLookup.getServiceInstance(getIWApplicationContext(), MemberFamilyLogic.class);
	}

	public int getRootAdministratorGroupID() throws RemoteException {
		String ROOT_COMMUNE_ADMINISTRATORS_GROUP = "commune_administrators_group_id";
		IWBundle bundle = getCommuneBundle();
		String groupId = bundle.getProperty(ROOT_COMMUNE_ADMINISTRATORS_GROUP);
		if (groupId != null)
			return Integer.parseInt(groupId);
		return -1;
	}

	private Gender getGenderFromPin(String pin){
		try {
			GenderHome home = (GenderHome) this.getIDOHome(Gender.class);
			if(Integer.parseInt(pin.substring(10,11)) % 2 == 0 ){
				return home.getFemaleGender();
			}
			else{
				return home.getMaleGender();
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public boolean haveSameAddress(User user, User compareUser) throws RemoteException {
		if (((Integer)user.getPrimaryKey()).intValue() == ((Integer)compareUser.getPrimaryKey()).intValue())
			return true;
		
		Address userAddress = getUsersMainAddress(user);
		Address otherUserAddress = getUsersMainAddress(compareUser);
		if (userAddress != null && otherUserAddress != null) {
			if(userAddress.getStreetAddress().equalsIgnoreCase(otherUserAddress.getStreetAddress() )) {
				PostalCode userPostal = null;
				PostalCode otherUserPostal = null;

				try {
					userPostal = userAddress.getPostalCode();
				}
				catch (SQLException e) {
					userPostal = null;					
				}
				
				try {
					otherUserPostal = otherUserAddress.getPostalCode();
				}
				catch (SQLException e) {
					otherUserPostal = null;					
				}

				if (userPostal != null && otherUserPostal != null) {
					if (userPostal.getPostalCode().equalsIgnoreCase(otherUserPostal.getPostalCode())) {
						return true;
					}
				}
				else if (userPostal == null && otherUserPostal == null) {
					return true;
				}
					
				return false;
			}
		}
		
		return false;
	}

	private IWTimestamp getBirthDateFromPin(String pin){
		int dd = Integer.parseInt(pin.substring(6,8));
		int mm = Integer.parseInt(pin.substring(4,6));
		int yyyy = Integer.parseInt(pin.substring(0,4));
		IWTimestamp dob = new IWTimestamp(dd,mm,yyyy);
		return dob;
	}
	
	public void updateCitizen(int userID, String firstName, String middleName, String lastName, String personalID) throws RemoteException {
		try {
			updateUser(userID, firstName, middleName, lastName, null, null, (Integer) (getGenderFromPin(personalID).getPrimaryKey()), personalID, getBirthDateFromPin(personalID), (Integer) getRootCitizenGroup().getPrimaryKey());
		}
		catch (FinderException fe) {
			fe.printStackTrace(System.err);
		}
		catch (CreateException ce) {
			ce.printStackTrace(System.err);
		}
	}
	
	public void updateCitizenAddress(int userID, String address, String postalCode, String postalName) throws RemoteException {
		try {
			AddressBusiness addressBiz = (AddressBusiness) getServiceInstance(AddressBusiness.class);
			Country country = ((CountryHome)getIDOHome(Country.class)).findByIsoAbbreviation("SE");
			PostalCode code = addressBiz.getPostalCodeAndCreateIfDoesNotExist(postalCode,postalName,country);

			updateUsersMainAddressOrCreateIfDoesNotExist(new Integer(userID), address, (Integer) code.getPrimaryKey(), country.getName(), postalName, postalName, null);
		}
		catch (FinderException fe) {
			fe.printStackTrace(System.err);
		}
		catch (CreateException ce) {
			ce.printStackTrace(System.err);
		}
	}
	
	public Email getEmail(User user) {
		try {
			return getUsersMainEmail(user);
		}
		catch (NoEmailFoundException e) {
			return null;
		}
	}

	public Phone getHomePhone(User user) {
		try {
			return this.getUsersHomePhone(user);
		}
		catch (NoPhoneFoundException e) {
			return null;
		}
	}
 }
