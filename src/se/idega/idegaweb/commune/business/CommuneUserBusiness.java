package se.idega.idegaweb.commune.business;

import java.rmi.RemoteException;
import javax.ejb.*;

import com.idega.user.business.UserBusiness;
import com.idega.user.data.Gender;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

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
 public com.idega.block.school.data.School getFirstManagingChildCareForUser(com.idega.user.data.User p0)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.Group getRootCitizenGroup()throws javax.ejb.CreateException,javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.Group getRootProtectedCitizenGroup()throws javax.ejb.CreateException,javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;

 public com.idega.user.data.Group getRootCommuneAdministratorGroup()throws javax.ejb.CreateException,javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.Group getRootProviderAdministratorGroup()throws javax.ejb.CreateException,javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.Group getRootSchoolAdministratorGroup()throws javax.ejb.CreateException,javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
    public com.idega.user.data.Group getRootCustomerChoiceGroup () throws javax.ejb.CreateException, javax.ejb.FinderException, java.rmi.RemoteException;
 public boolean hasCitizenAccount(com.idega.user.data.User user)throws RemoteException;
 public boolean hasCitizenAccount(int userID)throws RemoteException;
 public com.idega.core.data.Phone getChildHomePhone(User child) throws RemoteException;
 public com.idega.core.data.Phone getChildHomePhone(int childId) throws RemoteException;
 public java.util.Collection getParentsForChild(User child) throws RemoteException;
 public java.util.Collection getChildrenForUser(User user) throws RemoteException;
 public is.idega.idegaweb.member.business.MemberFamilyLogic getMemberFamilyLogic() throws RemoteException;
 public boolean getIfUserAddressesMatch(com.idega.core.data.Address userAddress, com.idega.core.data.Address userAddressToCompare) throws RemoteException;
 public Group getRootSpecialCitizenGroup() throws CreateException, FinderException, RemoteException;
 public User createSpecialCitizenByPersonalIDIfDoesNotExist(
	 String firstName,
	 String middleName,
	 String lastName,
	 String personalID)
	 throws CreateException, RemoteException;
public User createSpecialCitizenByPersonalIDIfDoesNotExist(
			String firstName,
			String middleName,
			String lastName,
			String personalID,
			Gender gender,
			IWTimestamp dateOfBirth)
			throws CreateException, RemoteException;
	public User createSpecialCitizen(
			final String firstname,
			final String middlename,
			final String lastname,
			final String personalID,
			final Gender gender,
			final IWTimestamp dateOfBirth)
			throws CreateException, RemoteException;
			
			
	public boolean moveCitizenFromCommune(User user) throws RemoteException;
	
	public boolean moveCitizenToCommune(User user) throws RemoteException;
	public boolean moveCitizenToProtectedCitizenGroup(User user) throws RemoteException;
	
	public int getRootAdministratorGroupID() throws RemoteException;
	public void updateCitizen(int userID, String firstName, String middleName, String lastName, String personalID) throws RemoteException;
	public void updateCitizenAddress(int userID, String address, String postalCode, String postalName) throws RemoteException;
	public boolean haveSameAddress(User user, User compareUser) throws RemoteException;
}
