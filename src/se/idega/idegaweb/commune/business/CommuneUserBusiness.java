package se.idega.idegaweb.commune.business;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.*;

import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Gender;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

public interface CommuneUserBusiness extends com.idega.business.IBOService, UserBusiness
{
 public com.idega.user.data.User createCitizen(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,com.idega.user.data.Gender p4,com.idega.util.IWTimestamp p5)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.User createCitizen(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.User createOrUpdateCitizenByPersonalID(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,com.idega.user.data.Gender p4,com.idega.util.IWTimestamp p5)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.User createOrUpdateCitizenByPersonalID(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
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
  public com.idega.block.school.data.School getFirstManagingMusicSchoolForUser(com.idega.user.data.User p0)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.School getFirstManagingSchoolForUser(com.idega.user.data.User p0)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.School getFirstManagingChildCareForUser(com.idega.user.data.User p0)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.Group getRootCitizenGroup()throws javax.ejb.CreateException,javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.Group getRootProtectedCitizenGroup()throws javax.ejb.CreateException,javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.School getProviderForUser(com.idega.user.data.User user) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.user.data.Citizen getCitizen(int userID);
 public String getNameLastFirst(User user, boolean comma);
 
 public com.idega.user.data.Group getRootCommuneAdministratorGroup()throws javax.ejb.CreateException,javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.Group getRootProviderAdministratorGroup()throws javax.ejb.CreateException,javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.Group getRootSchoolAdministratorGroup()throws javax.ejb.CreateException,javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.Group getRootCustomerChoiceGroup () throws javax.ejb.CreateException, javax.ejb.FinderException, java.rmi.RemoteException;
 public boolean hasCitizenAccount(com.idega.user.data.User user)throws RemoteException;
 public boolean hasCitizenAccount(int userID)throws RemoteException;
 public com.idega.core.contact.data.Phone getChildHomePhone(User child) throws RemoteException;
 public com.idega.core.contact.data.Phone getChildHomePhone(int childId) throws RemoteException;
 public java.util.Collection getParentsForChild(User child) throws RemoteException;
 public java.util.Collection getChildrenForUser(User user) throws RemoteException;
 public is.idega.block.family.business.FamilyLogic getMemberFamilyLogic() throws RemoteException;
 public boolean getIfUserAddressesMatch(com.idega.core.location.data.Address userAddress, com.idega.core.location.data.Address userAddressToCompare) throws RemoteException;
 public Group getRootOtherCommuneCitizensGroup() throws CreateException, FinderException, RemoteException;
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
			
			
	public boolean moveCitizenFromCommune(User user, java.sql.Timestamp time, User performer) throws RemoteException;
	public boolean moveCitizenToCommune(User user, java.sql.Timestamp time, User performer) throws RemoteException;
	public boolean moveCitizenToProtectedCitizenGroup(User user, java.sql.Timestamp time, User performer) throws RemoteException;

	public int getRootAdministratorGroupID() throws RemoteException;
	public void updateCitizen(int userID, String firstName, String middleName, String lastName, String personalID) throws RemoteException;
	public void updateCitizenAddress(int userID, String address, String postalCode, String postalName) throws RemoteException;
	public void updateCitizenAddress(int userID, String address, String postalCode, String postalName, Integer communeId) throws RemoteException;
	public boolean haveSameAddress(User user, User compareUser) throws RemoteException;
	public Email getEmail(User user);
	public Phone getHomePhone(User user);
	public boolean isRootCommuneAdministrator(User user) throws RemoteException;
	public boolean hasBankLogin(User user) throws RemoteException;

	/**
	 * Returns a custodian for this child.  If a custodian has an account that custodian is returned.
	 * @param childID
	 * @return
	 * @throws RemoteException
	 */
	public User getCustodianForChild(int childID) throws RemoteException;
	
	/**
	 * Returns a custodian for this child.  If a custodian has an account that custodian is returned.
	 * @param child
	 * @return
	 * @throws RemoteException
	 */
	public User getCustodianForChild(User child) throws RemoteException;
	
	/**
	 * Sets the specified user as deceased and takes all actions needed
	 * be handled here
	 * @param userID for user who is deceased
	 * @param deceasedDate
	 * @return true if successfull
	 */
	public boolean setUserAsDeceased(Integer userID,java.util.Date deceasedDate)throws RemoteException;
	
	/**
	 * Gets or creates (if not available) and returns the default usergroup for
	 * which all citizens are moved to when they get deceased.
	 * CreateException if it failed to locate or create the group.
	 */
	public Group getRootDeceasedCitizensGroup() throws CreateException, FinderException, RemoteException;

	public Collection findSchoolChildrenBySearchCondition(String searchString)throws RemoteException;
}
