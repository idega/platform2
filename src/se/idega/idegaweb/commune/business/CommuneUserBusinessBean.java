package se.idega.idegaweb.commune.business;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.business.*;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.data.*;
import com.idega.idegaweb.*;
import com.idega.user.business.*;
import com.idega.user.data.*;
import com.idega.util.IWTimestamp;
import java.rmi.RemoteException;
import java.util.*;
import javax.ejb.*;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

/**
 * Title:        idegaWeb
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega software
 * @author
 * @version 1.0
 */
public class CommuneUserBusinessBean extends IBOServiceBean implements CommuneUserBusiness
{
	private final String ROOT_CITIZEN_GROUP_ID_PARAMETER_NAME = "commune_id";
	protected UserBusiness getUserBusiness() throws RemoteException
	{
		return (UserBusiness) this.getServiceInstance(UserBusiness.class);
	}
	protected GroupBusiness getGroupBusiness() throws RemoteException
	{
		return (GroupBusiness) this.getServiceInstance(GroupBusiness.class);
	}
	/**
	 * Creates a new citizen with a firstname,middlename, lastname and personalID where middlename and personalID can be null.<br>
	 * Also adds the citizen to the Commune Root Group.
	 */
	public User createCitizen(String firstname, String middlename, String lastname, String personalID)
		throws CreateException, RemoteException
	{
		return this.createCitizen(firstname, middlename, lastname, personalID, null, null);
	}
	/**
	 * Creates a new citizen with a firstname,middlename, lastname, personalID,
     * gender and date of birth where middlename, personalID,gender,dateofbirth
     * can be null.<br>
	 * Also adds the citizen to the Commune Root Group.
	 */
	public User createCitizen
        (final String firstname, final String middlename, final String lastname,
         final String personalID, final Gender gender,
         final IWTimestamp dateOfBirth) 
        throws CreateException, RemoteException	{
        User newUser = null;
		try {
            final Group rootGroup = getRootCitizenGroup();
            final UserBusiness business = getUserBusiness();

            System.out.println ("firstname='" + firstname + "'\n" +
                                "middlename='" + middlename + "'\n" +
                                "lastname='" + lastname + "'\n" +
                                "personalID='" + personalID + "'\n" +
                                "gender='" + gender + "'\n" +
                                "dateOfBirth='" + dateOfBirth + "'\n" +
                                "rootGroup='" + rootGroup + "'\n");

			newUser = business.createUser (firstname, middlename, lastname,
                                           personalID, gender, dateOfBirth,
                                           rootGroup);
		} catch (final Exception e) {
            e.printStackTrace ();
			throw new IDOCreateException (e);
		}
		return newUser;
	}
	/**
	 * Finds and updates or Creates a new citizen with a firstname,middlename, lastname and personalID.<br>
	 * Also adds the citizen to the Commune Root Group.
	 */
	public User createCitizenByPersonalIDIfDoesNotExist(String firstName, String middleName, String lastName, String personalID)
		throws CreateException, RemoteException {
		return createCitizenByPersonalIDIfDoesNotExist
                (firstName, middleName, lastName, personalID, null, null);
	}

	public User createCitizenByPersonalIDIfDoesNotExist
        (final String firstName, final String middleName, final String lastName,
         final String personalID, final Gender gender,
         final IWTimestamp dateOfBirth)
		throws CreateException, RemoteException {

		User user = null;
		try {
            final UserBusiness business = getUserBusiness();
            final UserHome home = business.getUserHome();
			user = home.findByPersonalID (personalID);
        } catch (final FinderException e) {
            // nothing, since the case of "not find" is in finally clause
        } finally {
            if (user == null) {
                user = createCitizen (firstName, middleName, lastName,
                                      personalID, gender, dateOfBirth);
            }
        }
		return user;
	}

	/**
	 * Creates a new Commune Administrator with a firstname,middlename, lastname and personalID where middlename and personalID can be null
	 */
	public User createCommuneAdministrator(String firstname, String middlename, String lastname) throws CreateException, RemoteException
	{
		User newUser;
		/**
		 * @todo: put the user in an administrator group
		 */
		newUser = this.getUserBusiness().createUser(firstname, middlename, lastname);
		return newUser;
	}
	/**
	 * Creates a new Administrator whith a with a firstname,middlename, lastname and school where middlename  can be null
	 */
	public User createProviderAdministrator(String firstname, String middlename, String lastname, School school)
		throws javax.ejb.FinderException, CreateException, RemoteException
	{
		User newUser;
		SchoolBusiness schlBuiz = (SchoolBusiness) getServiceInstance(SchoolBusiness.class);
		Group rootSchoolAdminGroup = getRootProviderAdministratorGroup();
		Group schoolGroup = getGroupBusiness().getGroupHome().findByPrimaryKey(new Integer(school.getHeadmasterGroupId()));
		newUser = this.getUserBusiness().createUser(firstname, middlename, lastname, rootSchoolAdminGroup);
		//rootSchoolAdminGroup.addGroup(newUser);
		schoolGroup.addGroup(newUser);
		return newUser;
	}
	/**
	 * Creates a new Administrator whith a with a firstname,middlename, lastname and school where middlename  can be null
	 */
	public User createSchoolAdministrator(String firstname, String middlename, String lastname, School school)
		throws javax.ejb.FinderException, CreateException, RemoteException
	{
		User newUser;
		SchoolBusiness schlBuiz = (SchoolBusiness) getServiceInstance(SchoolBusiness.class);
		Group rootSchoolAdminGroup = getRootSchoolAdministratorGroup();
		Group schoolGroup = getGroupBusiness().getGroupHome().findByPrimaryKey(new Integer(school.getHeadmasterGroupId()));
		newUser = this.getUserBusiness().createUser(firstname, middlename, lastname, rootSchoolAdminGroup);
		//rootSchoolAdminGroup.addGroup(newUser);
		schoolGroup.addGroup(newUser);
		return newUser;
	}
	/**
	 * Generates a user login for the user with login derived from the users name and a random password
	 */
	public LoginTable generateUserLogin(User user) throws CreateException
	{
		try
		{
			return getUserBusiness().generateUserLogin(user);
		} catch (Exception e)
		{
			/**
			 * @todo: remove printStackTrace
			 */
			e.printStackTrace();
			throw new com.idega.data.IDOCreateException(e);
		}
	}

	/**
	 * Creates (if not available) and returns the default usergroup all
     * citizens, read from imports, are members of.
	 * throws a CreateException if it failed to locate or create the group.
	 */
	public Group getRootCitizenGroup()
        throws CreateException, FinderException, RemoteException {
		Group rootGroup = null;
		//create the default group
        final IWApplicationContext iwc = getIWApplicationContext ();
        final IWMainApplicationSettings settings = iwc.getApplicationSettings();
		String groupId = (String) settings.getProperty
                (ROOT_CITIZEN_GROUP_ID_PARAMETER_NAME);
        final UserBusiness userBusiness = getUserBusiness();
		if (groupId != null) {
            final GroupHome groupHome = userBusiness.getGroupHome();
			rootGroup = groupHome.findByPrimaryKey (new Integer(groupId));
		} else {
			System.err.println("trying to store Commune Root group");
			/**@todo this seems a wrong way to do things**/
			final GroupTypeHome typeHome
                    = (GroupTypeHome) getIDOHome(GroupType.class);
			final GroupType type = typeHome.create();
            final GroupBusiness groupBusiness = userBusiness.getGroupBusiness();
			rootGroup = groupBusiness.createGroup
                    ("Commune Citizens", "The Commune Root Group.",
                     type.getGeneralGroupTypeString());
			settings.setProperty(ROOT_CITIZEN_GROUP_ID_PARAMETER_NAME,
                                 (Integer) rootGroup.getPrimaryKey());
		}
		return rootGroup;
	}

	/**
	* Returns or creates (if not available) the default usergroup all provider(childcare) administors have as their primary group.
	* @throws CreateException if it failed to create the group.
	* @throws FinderException if it failed to locate the group.
	*/
	public Group getRootProviderAdministratorGroup() throws CreateException, FinderException, RemoteException
	{
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
			/**@todo this seems a wrong way to do things**/
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
	}
	/**
	* Returns or creates (if not available) the default usergroup all school administors have as their primary group.
	* @throws CreateException if it failed to create the group.
	* @throws FinderException if it failed to locate the group.
	*/
	public Group getRootSchoolAdministratorGroup() throws CreateException, FinderException, RemoteException
	{
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
			/**@todo this seems a wrong way to do things**/
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
	}
	/**
	* Returns or creates (if not available) the default usergroup all commune administors have as their primary group.
	* @throws CreateException if it failed to create the group.
	* @throws FinderException if it failed to locate the group.
	*/
	public Group getRootCommuneAdministratorGroup() throws CreateException, FinderException, RemoteException
	{
		Group rootGroup = null;
		//create the default group
		String ROOT_COMMUNE_ADMINISTRATORS_GROUP = "commune_administrators_group_id";
		IWBundle bundle = getCommuneBundle();
		String groupId = bundle.getProperty(ROOT_COMMUNE_ADMINISTRATORS_GROUP);
		if (groupId != null)
		{
			rootGroup = getUserBusiness().getGroupHome().findByPrimaryKey(new Integer(groupId));
		} else
		{
			System.err.println("trying to store Commune administrators Root group");
			/**@todo this seems a wrong way to do things**/
			GroupTypeHome typeHome = (GroupTypeHome) this.getIDOHome(GroupType.class);
			GroupType type = typeHome.create();
			rootGroup =
				getUserBusiness().getGroupBusiness().createGroup(
					"Commune Administrators",
					"The Commune Administrators Root Group.",
					type.getGeneralGroupTypeString());
			bundle.setProperty(ROOT_COMMUNE_ADMINISTRATORS_GROUP, rootGroup.getPrimaryKey().toString());
		}
		return rootGroup;
	}
	/**
	 * Gets all the users that have the CommuneAdministrator group as its primary group.
	* @return Collection of com.idega.user.data.User objects with all the users in the CommuneAdministrators group.
	* @throws FinderException if it failed to locate the group or its users.
	*/
	public Collection getAllCommuneAdministrators() throws FinderException
	{
		try
		{
			return getUserBusiness().getUsersInPrimaryGroup(getRootCommuneAdministratorGroup());
		} catch (Exception e)
		{
			throw new IDOFinderException(e);
		}
	}
	protected IWBundle getCommuneBundle()
	{
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
			// if user is a SchoolAdministrator
			if (user.hasRelationTo(rootGroup)) {
				Collection schools = ((SchoolBusiness)IBOLookup.getServiceInstance(this.getIWApplicationContext(), SchoolBusiness.class)).getSchoolHome().findAllBySchoolGroup(user);
				if (!schools.isEmpty()) {
					Iterator iter = schools.iterator();
					while (iter.hasNext()) {
						School school = (School) iter.next();
						return school;
					}
				}
			}
		}
		catch (CreateException e) {
			e.printStackTrace();
		}

		throw new FinderException("No school found that " + user.getName() + " manages");
	}
	
	public boolean hasCitizenAccount(User user)throws RemoteException{
		return getUserBusiness().hasUserLogin(user);
	}
	
	public boolean hasCitizenAccount(int userID)throws RemoteException{
		return getUserBusiness().hasUserLogin(userID);
	}

}
