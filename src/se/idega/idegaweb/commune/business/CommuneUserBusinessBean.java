package se.idega.idegaweb.commune.business;
import is.idega.block.family.business.FamilyLogic;
import is.idega.block.family.business.NoChildrenFound;
import is.idega.block.family.business.NoCustodianFound;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import se.idega.block.pki.business.NBSLoginBusinessBean;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.user.business.DeceasedUserBusiness;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolUserBusiness;
import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.accesscontrol.data.LoginInfo;
import com.idega.core.accesscontrol.data.LoginInfoHome;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.accesscontrol.data.LoginTableHome;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.business.AddressBusiness;
import com.idega.core.location.business.CommuneBusiness;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.Commune;
import com.idega.core.location.data.Country;
import com.idega.core.location.data.CountryHome;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOCreateException;
import com.idega.data.IDOFinderException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplicationSettings;
import com.idega.presentation.IWContext;
import com.idega.repository.data.ImplementorRepository;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.NoEmailFoundException;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.business.UserBusiness;
import com.idega.user.business.UserBusinessBean;
import com.idega.user.data.Gender;
import com.idega.user.data.GenderHome;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.IWTimestamp;
import com.idega.util.text.Name;
import com.idega.util.text.TextSoap;
/**
 * Title:        se.idega.idegaweb.commune.business.CommuneUserBusinessBean
 * Description:	Use this business class to handle Citizen information
 * Copyright:    Copyright (c) 2001
 * Company:      idega software
 * @author AguraIT and idega
 * @version 1.0
 */
public class CommuneUserBusinessBean extends UserBusinessBean implements CommuneUserBusiness,UserBusiness {

	private final String ROOT_CITIZEN_GROUP_ID_PARAMETER_NAME = "commune_id";
	private final String ROOT_OTHER_COMMUNE_CITIZEN_GROUP_ID_PARAMETER_NAME = "special_citizen_group_id";
	private final String ROOT_PROTECTED_CITIZEN_GROUP_ID_PARAMETER_NAME = "protected_citizen_group_id";
	private final String ROOT_CUSTOMER_CHOICE_GROUP_ID_PARAMETER_NAME = "customer_choice_group_id";
	private final String ROOT_CITIZEN_DECEASED_ID_PARAMETER_NAME = "citizen_deceased_group_id";
	
	private final int SCHOOLCHILDREN_AGE = 12;
	
	private Group rootCitizenGroup;
	private Group rootOtherCommuneCitizenGroup;
	private Group rootProtectedCitizenGroup;
	private Group rootCustomerChoiceGroup;
	private Group rootDeceasedCitizensGroup;
	
	CommuneBusiness cBiz = null;
	
	private SchoolBusiness schoolBusiness = null;

	private CommuneBusiness getCommuneBusiness() throws IBOLookupException {
		if(null==cBiz) {
			cBiz = (CommuneBusiness) getServiceInstance(CommuneBusiness.class);
		}
		return cBiz;
	}
	/**
	 * Creates a new citizen with a firstname,middlename, lastname and personalID where middlename and personalID can be null.<br>
	 * Also adds the citizen to the Commune Root Group.
	 */
	public User createCitizen(String firstname, String middlename, String lastname, String personalID) throws CreateException {
		return this.createCitizen(firstname, middlename, lastname, personalID, getGenderFromPin(personalID), getBirthDateFromPin(personalID));
	}
	/**
	 * Creates a new citizen with a firstname,middlename, lastname, personalID,
	   * gender and date of birth where middlename, personalID,gender,dateofbirth
	   * can be null.<br>
	 * Also adds the citizen to the Commune Root Group.
	 */

	public User createCitizen(final String firstname, final String middlename, final String lastname, final String personalID, final Gender gender, final IWTimestamp dateOfBirth) throws CreateException {
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
	public User createCitizen(final String firstname, final String middlename, final String lastname, final String personalID, final Gender gender, final IWTimestamp dateOfBirth, final Group rootGroup) throws CreateException {
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
	public User createOrUpdateCitizenByPersonalID(String firstName, String middleName, String lastName, String personalID) throws CreateException {
		return createOrUpdateCitizenByPersonalID(firstName, middleName, lastName, personalID, getGenderFromPin(personalID), getBirthDateFromPin(personalID));
	}
	/**
	 * Finds and updates or Creates a new citizen with a firstname,middlename, lastname and personalID.<br>
	 * Also adds the citizen to the Commune Root Group.
	 */	
	public User createOrUpdateCitizenByPersonalID(String firstName, String middleName, String lastName, String personalID, Gender gender, IWTimestamp dateOfBirth) throws CreateException {
		User user = null;
		try {
			final UserHome home = getUserHome();
			user = home.findByPersonalID(personalID);
			//update if found
			if ((firstName != null && !firstName.trim().equals("")) || 
			    (middleName != null && !middleName.trim().equals("")) || 
			    (lastName != null && !lastName.trim().equals(""))) {
				firstName = (firstName == null) ? "" : firstName;
				middleName = (middleName == null) ? "" : middleName;
				lastName = (lastName == null) ? "" : lastName;
				
				StringBuffer fullName = new StringBuffer();
				firstName = (firstName == null) ? "" : firstName;
				middleName = (middleName == null) ? "" : middleName;
				lastName = (lastName == null) ? "" : lastName;
				fullName.append(firstName).append(" ").append(middleName).append(" ").append(lastName);
				if (fullName.toString().trim().length() > 0) {
					user.setFullName(fullName.toString());
				}
			}
			
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
	public User createSpecialCitizen(final String firstname, final String middlename, final String lastname, final String personalID, final Gender gender, final IWTimestamp dateOfBirth) throws CreateException {
		User newUser = null;
		try {
			newUser = createUser(firstname, middlename, lastname, personalID, gender, dateOfBirth, getRootOtherCommuneCitizensGroup());
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
	public User createSpecialCitizenByPersonalIDIfDoesNotExist(String firstName, String middleName, String lastName, String personalID) throws CreateException {
		return createOrUpdateCitizenByPersonalID(firstName, middleName, lastName, personalID, getGenderFromPin(personalID), getBirthDateFromPin(personalID));
	}
	public User createSpecialCitizenByPersonalIDIfDoesNotExist(String firstName, String middleName, String lastName, String personalID, Gender gender, IWTimestamp dateOfBirth) throws CreateException {
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
		//SchoolBusiness schlBuiz = (SchoolBusiness) getServiceInstance(SchoolBusiness.class);
		Group rootSchoolAdminGroup = getSchoolBusiness().getRootProviderAdministratorGroup();
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
		//SchoolBusiness schlBuiz = (SchoolBusiness) getServiceInstance(SchoolBusiness.class);
		Group rootSchoolAdminGroup = getSchoolBusiness().getRootSchoolAdministratorGroup();
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
		Commune commune = getCommuneBusiness().getDefaultCommune();
		if(null!= commune) {
			Group group = commune.getGroup();
			if(null!=group) {
				return group;
			}
		}

		//create the default group

		if (rootCitizenGroup != null)
			return rootCitizenGroup;

		final IWApplicationContext iwc = getIWApplicationContext();
		final IWMainApplicationSettings settings = iwc.getApplicationSettings();
		String groupId = settings.getProperty(ROOT_CITIZEN_GROUP_ID_PARAMETER_NAME);
		if (groupId != null) {
			final GroupHome groupHome = getGroupHome();
			rootCitizenGroup = groupHome.findByPrimaryKey(new Integer(groupId));
		}
		else {
			System.err.println("trying to store Commune Root group");
			final GroupBusiness groupBusiness = getGroupBusiness();
			rootCitizenGroup = groupBusiness.createGroup("Commune Citizens", "The Root Group for all Citizens of the Commune");
			
			//settings.setProperty(ROOT_CITIZEN_GROUP_ID_PARAMETER_NAME, rootCitizenGroup.getPrimaryKey());
		}
		getCommuneBusiness().getDefaultCommune().setGroup(rootCitizenGroup);
		return rootCitizenGroup;
	}
	/**
	 * Creates (if not available) and returns the default usergroup for  all
	 * citizens not living in the commune, read from imports. throws a
	 * CreateException if it failed to locate or create the group.
	 */
	public Group getRootOtherCommuneCitizensGroup() throws CreateException, FinderException, RemoteException {
		Commune commune = getCommuneBusiness().getOtherCommuneCreateIfNotExist();
		if(null!= commune) {
			Group group = commune.getGroup();
			if(null!=group) {
				return group;
			}
		}
		//create the default group
		if (rootOtherCommuneCitizenGroup != null)
			return rootOtherCommuneCitizenGroup;

		final IWApplicationContext iwc = getIWApplicationContext();
		final IWMainApplicationSettings settings = iwc.getApplicationSettings();
		String groupId = settings.getProperty(ROOT_OTHER_COMMUNE_CITIZEN_GROUP_ID_PARAMETER_NAME);
		if (groupId != null) {
			final GroupHome groupHome = getGroupHome();
			rootOtherCommuneCitizenGroup = groupHome.findByPrimaryKey(new Integer(groupId));
		}
		else {
			System.out.println("Trying to store OtherCommuneCitizens Root group");
			final GroupBusiness groupBusiness = getGroupBusiness();
			rootOtherCommuneCitizenGroup = groupBusiness.createGroup("Non-Commune Citizens", "The Root Group for all Citizens in other Communes.");
			
//			settings.setProperty(ROOT_OTHER_COMMUNE_CITIZEN_GROUP_ID_PARAMETER_NAME, rootOtherCommuneCitizenGroup.getPrimaryKey());
		}
		getCommuneBusiness().getOtherCommuneCreateIfNotExist().setGroup(rootOtherCommuneCitizenGroup);
		return rootOtherCommuneCitizenGroup;
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
		String groupId = settings.getProperty(ROOT_CUSTOMER_CHOICE_GROUP_ID_PARAMETER_NAME);
		if (groupId != null) {
			final GroupHome groupHome = getGroupHome();
			rootCustomerChoiceGroup = groupHome.findByPrimaryKey(new Integer(groupId));
		} else {
			//final GroupHome groupHome = getGroupHome();

			System.err.println("trying to store Customer Choice Root group");
			final GroupBusiness groupBusiness = getGroupBusiness();
			rootCustomerChoiceGroup = groupBusiness.createGroup("Kundvalsgruppen", "Kundvalsgruppen");
			settings.setProperty(ROOT_CUSTOMER_CHOICE_GROUP_ID_PARAMETER_NAME, rootCustomerChoiceGroup.getPrimaryKey());
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
		String groupId = settings.getProperty(ROOT_PROTECTED_CITIZEN_GROUP_ID_PARAMETER_NAME);
		if (groupId != null) {
			final GroupHome groupHome = getGroupHome();
			rootProtectedCitizenGroup = groupHome.findByPrimaryKey(new Integer(groupId));
		}
		else {
			System.err.println("trying to store Commune Protected Citizen Root group");
			final GroupBusiness groupBusiness = getGroupBusiness();
			rootProtectedCitizenGroup = groupBusiness.createGroup("Commune Protected Citizens", "The Commune Protected Citizen Root Group.");
			settings.setProperty(ROOT_PROTECTED_CITIZEN_GROUP_ID_PARAMETER_NAME, rootProtectedCitizenGroup.getPrimaryKey());
		}
		return rootProtectedCitizenGroup;
	}
	
	/**
	 * Gets or creates (if not available) and returns the default usergroup for
	 * which all citizens are moved to when they get deceased.
	 * CreateException if it failed to locate or create the group.
	 */
	public Group getRootDeceasedCitizensGroup() throws CreateException, FinderException, RemoteException {
		//create the default group
		if (rootDeceasedCitizensGroup != null)
			return rootDeceasedCitizensGroup;

		final IWApplicationContext iwc = getIWApplicationContext();
		final IWMainApplicationSettings settings = iwc.getApplicationSettings();
		String groupId = settings.getProperty(ROOT_CITIZEN_DECEASED_ID_PARAMETER_NAME);
		if (groupId != null) {
			final GroupHome groupHome = getGroupHome();
			rootDeceasedCitizensGroup = groupHome.findByPrimaryKey(new Integer(groupId));
		}
		else {
			System.err.println("trying to store Citizen Deceased Root group");
			final GroupBusiness groupBusiness = getGroupBusiness();
			rootDeceasedCitizensGroup = groupBusiness.createGroup("Deceased Citizens", "The Commune Deceased Citizen Root Group.");
			settings.setProperty(ROOT_CITIZEN_DECEASED_ID_PARAMETER_NAME, rootDeceasedCitizensGroup.getPrimaryKey());
		}
		return rootProtectedCitizenGroup;
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
			rootGroup = getGroupBusiness().createGroup("Commune Administrators", "The Commune Administrators Root Group.");
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
			return getUsersInGroup(getRootCommuneAdministratorGroup());
			//return getUsersInPrimaryGroup(getRootCommuneAdministratorGroup());
		}
		catch (Exception e) {
			throw new IDOFinderException(e);
		}
	}
	
	public boolean isRootCommuneAdministrator(User user) throws RemoteException {
		try {
			if (user.hasRelationTo(getRootCommuneAdministratorGroup()))
				return true;
			return false;
		}
		catch (CreateException e) {
			return false;
		}
		catch (FinderException e) {
			return false;
		}
	}
	
	protected IWBundle getCommuneBundle() {
		return this.getIWApplicationContext().getIWMainApplication().getBundle(CommuneBlock.IW_BUNDLE_IDENTIFIER);
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
			Group rootGroup = getSchoolBusiness().getRootSchoolAdministratorGroup();
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

	/**
	 * Method getFirstManagingSchoolForUser.
	 * If there is no school that the user manages then the method throws a FinderException.
	 * @param user a user
	 * @return School that is the first school that the user is a manager for.
	 * @throws javax.ejb.FinderException if ther is no school that the user manages.
	 */
	public School getFirstManagingMusicSchoolForUser(User user) throws FinderException, RemoteException {
		try {
			Group rootGroup = getSchoolBusiness().getRootMusicSchoolAdministratorGroup();
			if (user.getPrimaryGroupID() != -1 && user.getPrimaryGroup().equals(rootGroup)) {
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
			Group rootGroup = getSchoolBusiness().getRootProviderAdministratorGroup();
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
	
	public boolean hasBankLogin(User user) {
		if (user == null){
			return false;
		}
		return NBSLoginBusinessBean.createNBSLoginBusiness().hasBankLogin(user);			
	}

	public boolean moveCitizenFromCommune(User user, Timestamp time, User performer) {
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
			rootSpecialGroup = getRootOtherCommuneCitizensGroup();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}


		try {
			rootGroup.removeUser(user,performer,time);
		}
		catch (RemoveException e) {
			e.printStackTrace();
			return false;
		}
		
		rootSpecialGroup.addGroup(user,time);
		user.setPrimaryGroup(rootSpecialGroup);
		user.store();

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
	
	public boolean moveCitizenToCommune(User user, Timestamp time, User performer) {
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
			rootSpecialGroup = getRootOtherCommuneCitizensGroup();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
    

		try {
			rootSpecialGroup.removeUser(user,performer,time);
		}
		catch (RemoveException e) {
			e.printStackTrace();
			return false;
		}
		rootGroup.addGroup(user,time);
		user.setPrimaryGroup(rootGroup);
		user.store();
		
		
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
	
	public boolean moveCitizenToProtectedCitizenGroup(User user,Timestamp time, User performer) {
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
			rootSpecialGroup = getRootOtherCommuneCitizensGroup();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}

		try {
			rootSpecialGroup.removeUser(user, performer,time);
			rootGroup.removeUser(user, performer,time);
		}
		catch (RemoveException e) {
			e.printStackTrace();
			return false;
		}
		
		rootProtectedGroup.addGroup(user,time);
		user.setPrimaryGroup(rootProtectedGroup);
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
							// empty
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

	public boolean getIfUserAddressesMatch(Address userAddress, Address userAddressToCompare) {
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
	
	public User getCustodianForChild(int childID) throws RemoteException {
		return getCustodianForChild(getUser(childID));
	}
	
	public User getCustodianForChild(User child) throws RemoteException {
		User performer = null;
		Collection parents = getParentsForChild(child);
		if (parents != null) {
			Iterator iter = parents.iterator();
			while (iter.hasNext()) {
				User parent = (User) iter.next();
				if (hasCitizenAccount(parent)) {
					performer = parent;
					break;	
				}
				if (!iter.hasNext()) {
					performer = parent;
				}
			}
		}
		
		return performer;
	}

	public Collection getChildrenForUser(User user) throws RemoteException {
		try {
			return getMemberFamilyLogic().getChildrenInCustodyOf(user);
		}
		catch (NoChildrenFound ncf) {
			return null;
		}
	}

	public FamilyLogic getMemberFamilyLogic() throws RemoteException {
		return (FamilyLogic) IBOLookup.getServiceInstance(getIWApplicationContext(), FamilyLogic.class);
	}

	public int getRootAdministratorGroupID() {
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
			return home.getMaleGender();
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
			return userAddress.isEqualTo(otherUserAddress);
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
	
	public void updateCitizenAddress(int userID, String address, String postalCode, String postalName, Integer communeId) throws RemoteException {
		try {
			AddressBusiness addressBiz = (AddressBusiness) getServiceInstance(AddressBusiness.class);
			Country country = ((CountryHome)getIDOHome(Country.class)).findByIsoAbbreviation("SE");
			PostalCode code = addressBiz.getPostalCodeAndCreateIfDoesNotExist(postalCode,postalName,country);

			updateUsersMainAddressOrCreateIfDoesNotExist(new Integer(userID), address, (Integer) code.getPrimaryKey(), country.getName(), postalName, postalName, null, communeId);
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
	
	/**
	 * Sets the specified user as deceased all system actions taken when someone dies should
	 * be handled here
	 * @param userID
	 * @param deceasedDate
	 * @return true if successfull
	 */
	public boolean setUserAsDeceased(Integer userID,Date deceasedDate){
		try {
//			UserStatusBusiness userStatusService = (UserStatusBusiness)getServiceInstance(UserStatusBusiness.class);
//			userStatusService.setUserAsDeceased(userID,deceasedDate);
			// remove custodian relations
			FamilyLogic familyService = getMemberFamilyLogic();
			User deceasedUser = getUser(userID);
			familyService.registerAsDeceased(deceasedUser, deceasedDate);
//			familyService.removeAllFamilyRelationsForUser(deceasedUser);

			/* Replaced with "familyService.removeAllFamilyRelationsForUser(deceasedUser);"
			try {
				Collection custodyChildren = familyService.getChildrenInCustodyOf(deceasedUser);
				if(custodyChildren!=null && !custodyChildren.isEmpty()){
					for (Iterator iter = custodyChildren.iterator(); iter.hasNext();) {
						User child = (User) iter.next();
						System.out.println("removing deceased custodian "+deceasedUser.getName()+" for "+child.getName());
						familyService.removeAsCustodianFor(deceasedUser,child);
					}
				}
			}
			catch (NoChildrenFound e1) {
			}
			catch (RemoveException e1) {
				e1.printStackTrace();
			}
			*/
			Group deceasedGroup;
			try {
				deceasedGroup = this.getRootDeceasedCitizensGroup();
				deceasedGroup.addGroup(deceasedUser);
				deceasedUser.setPrimaryGroup(deceasedGroup);
			}
			catch (NullPointerException e2) {
				logError("RootDeceasedCitizensGroup not found");
			}
			catch (CreateException e2) {
				e2.printStackTrace();
			}
			catch (FinderException e2) {
				e2.printStackTrace();
			}
			
			//Try to move the citizen from these groups but it is not certain that he is a member
			try{
				//HACK to get the current user:
				User currentPerformingUser = IWContext.getInstance().getCurrentUser();
				this.getRootCitizenGroup().removeUser(deceasedUser,currentPerformingUser);
			}
			catch(Exception e){
				// empty
			}
			try{
				//HACK to get the current user:
				User currentPerformingUser = IWContext.getInstance().getCurrentUser();
				this.getRootOtherCommuneCitizensGroup().removeUser(deceasedUser,currentPerformingUser);
			}
			catch(Exception e){
				// empty
			}
			
			//Try to remove phones and emails from user if he has any
			try {
				deceasedUser.removeAllPhones();
			}
			catch (IDORemoveRelationshipException e3) {
				log("Failed removing phones from deceased user : "+deceasedUser.getName());
			}
			try {
				deceasedUser.removeAllEmails();
			}
			catch (IDORemoveRelationshipException e4) {
				log("Failed removing emails from deceased user : "+deceasedUser.getName());
			}
			
			//Remove the users login:
			LoginTableHome loginHome = (LoginTableHome)getIDOHome(LoginTable.class);
			Collection coll;
			try {
				coll = loginHome.findLoginsForUser(deceasedUser);
				Iterator iter = coll.iterator();
				while(iter.hasNext()){
					LoginTable login = (LoginTable)iter.next();
					//Try to disable account:
					try{
						LoginInfoHome liHome = (LoginInfoHome)getIDOHome(LoginInfo.class);
						LoginInfo loginInfo = liHome.findByPrimaryKey(login.getPrimaryKey());
						loginInfo.setAccountEnabled(false);
						loginInfo.store();
					}
					catch(Exception e){
						// empty
					}
					//Try to remove login:					
					/*try {
						login.remove();
					}
					catch (EJBException e6) {
						e6.printStackTrace();
					}
					catch (RemoveException e6) {					
						log("Failed removing login:"+login.getUserLogin()+" from deceased user : "+deceasedUser.getName());
					}*/
				}
			}
			catch (FinderException e5) {
				e5.printStackTrace();
			}
			
			// perform deceased user business plugins
			ImplementorRepository repository = ImplementorRepository.getInstance();
			List list = repository.newInstances(DeceasedUserBusiness.class, CommuneUserBusiness.class);
			Iterator iterator = list.iterator();
			boolean resultIsOkay = true;
			while (iterator.hasNext() && resultIsOkay) {
				DeceasedUserBusiness deceasedUserBusiness = (DeceasedUserBusiness) iterator.next();
				resultIsOkay = deceasedUserBusiness.setUserAsDeceased(userID, deceasedDate, getIWApplicationContext());
			}
			return resultIsOkay;
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		//TODO add more actions when user registered as deceased 
		return false;
	}
		
	public Collection findSchoolChildrenBySearchCondition(String searchString) {
		try {
			Collection users = new ArrayList();
			StringTokenizer tokenizer = new StringTokenizer(searchString, " ");
			while (tokenizer.hasMoreElements()) {
				String element = (String) tokenizer.nextElement();
				Collection results = this.getUserHome().findUsersBySearchConditionAndAge(trimSearchString(element), true, SCHOOLCHILDREN_AGE);
				if (results != null) {
					if (users.isEmpty())
						users.addAll(results);
					else
						users.retainAll(results);
				}
			}
			return users;
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
		catch (RemoteException re) {
			re.printStackTrace();
			return new ArrayList();
		}
	}
	
	private String trimSearchString(String string) {
		String temp = string;
		temp = TextSoap.findAndCut(temp, "-");
		try {
			Long.parseLong(temp);
			return temp;
		}
		catch (NumberFormatException nfe) {
			if (temp.indexOf("TF") != -1)
				return temp;
			return string;
		}
	}
	
	public String getNameLastFirst(User user, boolean comma){
		Name name = new Name(user.getFirstName(), user.getMiddleName(), user.getLastName());
		return name.getName(this.getIWMainApplication().getSettings().getDefaultLocale(), comma);
	}
	
	/**
	 * Gets mail address for user for receiving printed letters
	 * If no secondary address is found the primary address is returned
     * @param user
     * @return
     * @throws RemoteException
     * @throws NoUserAddressException
     */
    public Address getPostalAddress(User user) throws RemoteException, NoUserAddressException {
        int userID = ((Integer) user.getPrimaryKey()).intValue();
        Address addr = getUserAddressByAddressType(userID, getAddressHome().getAddressType2());
        if (addr == null || "".equals(addr.getStreetAddress())) 
            addr = getUsersMainAddress(user);
        if (addr == null  || "".equals(addr.getStreetAddress())) 
            throw new NoUserAddressException(user);
        return addr;
    }


	private SchoolBusiness getSchoolBusiness() throws RemoteException {
		if (schoolBusiness == null) {
			schoolBusiness = (SchoolBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolBusiness.class);
		}
		return schoolBusiness;
	}
}