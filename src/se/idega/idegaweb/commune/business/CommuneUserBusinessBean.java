package se.idega.idegaweb.commune.business;

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

public class CommuneUserBusinessBean extends IBOServiceBean {

  protected UserBusiness getUserBusiness()throws RemoteException{
    return (UserBusiness)this.getServiceInstance(UserBusiness.class);
  }

  /**
   * Creates a new citizen with a firstname,middlename, lastname and personalID where middlename and personalID can be null
   */
  public User createCitizen(String firstname, String middlename, String lastname,String personalID) throws CreateException,RemoteException{
      User newUser;
      newUser = this.getUserBusiness().createUser(firstname,middlename,lastname,personalID);
      return newUser;
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
}