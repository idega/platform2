package se.idega.idegaweb.commune.business;

import java.rmi.RemoteException;
import javax.ejb.*;

import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;

public interface CommuneUserBusiness extends com.idega.business.IBOService, UserBusiness
{
 public com.idega.user.data.User createCitizen(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,com.idega.user.data.Gender p4,com.idega.util.IWTimestamp p5)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.User createCitizen(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.User createCitizenByPersonalIDIfDoesNotExist(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,com.idega.user.data.Gender p4,com.idega.util.IWTimestamp p5)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.User createCitizenByPersonalIDIfDoesNotExist(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.User createCommuneAdministrator(java.lang.String p0,java.lang.String p1,java.lang.String p2)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.User createProviderAdministrator(java.lang.String p0,java.lang.String p1,java.lang.String p2,com.idega.block.school.data.School p3)throws javax.ejb.FinderException,javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.User createSchoolAdministrator(java.lang.String p0,java.lang.String p1,java.lang.String p2,com.idega.block.school.data.School p3)throws javax.ejb.FinderException,javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
  public java.util.Collection getAllCommuneAdministrators()throws javax.ejb.FinderException, java.rmi.RemoteException;
 /**
	 * Method getFirstManagingSchoolForUser.
	 * If there is no school that the user manages then the method throws a FinderException.
	 * @param user a user
	 * @return School that is the first school that the user is a manager for.
	 * @throws javax.ejb.FinderException if there is no school that the user manages.
	 */
 public com.idega.block.school.data.School getFirstManagingSchoolForUser(com.idega.user.data.User p0)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.Group getRootCitizenGroup()throws javax.ejb.CreateException,javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.Group getRootCommuneAdministratorGroup()throws javax.ejb.CreateException,javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.Group getRootProviderAdministratorGroup()throws javax.ejb.CreateException,javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.Group getRootSchoolAdministratorGroup()throws javax.ejb.CreateException,javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean hasCitizenAccount(com.idega.user.data.User user)throws RemoteException;
 public boolean hasCitizenAccount(int userID)throws RemoteException;
}
