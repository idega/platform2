package se.idega.idegaweb.commune.business;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.business.IBOServiceBean;

import com.idega.user.business.UserBusiness;
import com.idega.user.data.*;
import com.idega.block.school.data.School;
import com.idega.core.accesscontrol.data.LoginTable;

import java.rmi.RemoteException;
import javax.ejb.*;

/**
 * Title:        idegaWeb
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega software
 * @author
 * @version 1.0
 */

public class CommuneUserBusinessBean extends IBOServiceBean implements CommuneUserBusiness{

  private final String ROOT_CITIZEN_GROUP_ID_PARAMETER_NAME = "commune_id";

  protected UserBusiness getUserBusiness()throws RemoteException{
    return (UserBusiness)this.getServiceInstance(UserBusiness.class);
  }

  /**
   * Creates a new citizen with a firstname,middlename, lastname and personalID where middlename and personalID can be null.<br>
   * Also adds the citizen to the Commune Root Group.
   */
  public User createCitizen(String firstname, String middlename, String lastname,String personalID) throws CreateException,RemoteException{
    Group rootGroup;
    User newUser;

    try {
      rootGroup = this.getRootCitizenGroup();
      newUser = this.getUserBusiness().createUser(firstname,middlename,lastname,personalID);
      rootGroup.addGroup(newUser);
    }
    catch (Exception ex) {
      throw new com.idega.data.IDOCreateException(ex);
    }

    return newUser;
  }

  /**
   * Finds and updates or Creates a new citizen with a firstname,middlename, lastname and personalID.<br>
   * Also adds the citizen to the Commune Root Group.
   */
  public User createCitizenByPersonalIDIfDoesNotExist(String firstName, String middleName, String lastName,String personalID) throws CreateException,RemoteException{
    User user;
    try{
      user = getUserBusiness().getUserHome().findByPersonalID(personalID);
      user.setFirstName(firstName);
      user.setMiddleName(middleName);
      user.setLastName(lastName);
      user.store();
    }
    catch(FinderException ex){
      user = createCitizen(firstName,middleName,lastName,personalID);
    }

    return user;
  }

  /**
   * Creates a new Commune Administrator with a firstname,middlename, lastname and personalID where middlename and personalID can be null
   */
  public User createCommuneAdministrator(String firstname, String middlename, String lastname) throws CreateException,RemoteException{
      User newUser;
      newUser = this.getUserBusiness().createUser(firstname,middlename,lastname);
      return newUser;
  }

  /**
   * Creates a new Administrator whith a with a firstname,middlename, lastname and school where middlename  can be null
   */
  public User createSchoolAdministrator(String firstname, String middlename, String lastname,School school) throws CreateException,RemoteException{
      User newUser;
      newUser = this.getUserBusiness().createUser(firstname,middlename,lastname);
      return newUser;
  }

  /**
   * Generates a user login for the user with login derived from the users name and a random password
   */
  public LoginTable generateUserLogin(User user)throws CreateException{
    try{
      return getUserBusiness().generateUserLogin(user);
    }
    catch(Exception e){
      throw new com.idega.data.IDOCreateException(e);
    }
  }

  /**
   * Creates (if not available) and returns the default usergroup all citizens, read from imports, are members of.
   * throws a CreateException if it failed to locate or create the group.
   */
  public Group getRootCitizenGroup()throws CreateException,FinderException,RemoteException{
    Group rootGroup = null;
    //create the default group
    String groupId = (String) this.getIWApplicationContext().getApplicationSettings().getProperty(ROOT_CITIZEN_GROUP_ID_PARAMETER_NAME);
    if( groupId!=null ){
      rootGroup = getUserBusiness().getGroupHome().findByPrimaryKey(new Integer(groupId));
    }
    else{
      System.err.println("trying to store Commune Root group");
      /**@todo this seems a wrong way to do things**/
      GroupTypeHome typeHome = (GroupTypeHome) this.getIDOHome(GroupType.class);
      GroupType type = typeHome.create();


      rootGroup = getUserBusiness().getGroupBusiness().createGroup("Commune Citizens","The Commune Root Group.",type.getGeneralGroupTypeString());

      this.getIWApplicationContext().getApplicationSettings().setProperty(ROOT_CITIZEN_GROUP_ID_PARAMETER_NAME,(Integer)rootGroup.getPrimaryKey());
    }

    return rootGroup;
  }

}