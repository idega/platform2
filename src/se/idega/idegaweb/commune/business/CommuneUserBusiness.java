package se.idega.idegaweb.commune.business;

import java.rmi.RemoteException;
import javax.ejb.*;

import com.idega.block.school.data.School;
import com.idega.user.data.User;

public interface CommuneUserBusiness extends com.idega.business.IBOService
{
 public com.idega.user.data.User createCitizen(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.user.data.Group getRootCitizenGroup()throws java.rmi.RemoteException,javax.ejb.FinderException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.core.accesscontrol.data.LoginTable generateUserLogin(com.idega.user.data.User p0)throws javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.user.data.User createCitizenByPersonalIDIfDoesNotExist(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.user.data.Group getRootSchoolAdministratorGroup()throws java.rmi.RemoteException,javax.ejb.FinderException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.user.data.Group getRootProviderAdministratorGroup()throws java.rmi.RemoteException,javax.ejb.FinderException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.user.data.User createCommuneAdministrator(java.lang.String p0,java.lang.String p1,java.lang.String p2)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.user.data.User createSchoolAdministrator(java.lang.String p0,java.lang.String p1,java.lang.String p2,com.idega.block.school.data.School p3)throws java.rmi.RemoteException,javax.ejb.CreateException,javax.ejb.FinderException, java.rmi.RemoteException;
 public com.idega.user.data.User createCitizen(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,com.idega.user.data.Gender p4,com.idega.util.IWTimestamp p5)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.user.data.User createCitizenByPersonalIDIfDoesNotExist(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,com.idega.user.data.Gender p4,com.idega.util.IWTimestamp p5)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;

  /**
   * Creates a new Administrator whith a with a firstname,middlename, lastname and school where middlename  can be null
   */
  public User createProviderAdministrator(String firstname, String middlename, String lastname,School school) throws javax.ejb.FinderException,CreateException,RemoteException;

}
