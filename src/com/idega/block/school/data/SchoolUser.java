/**
 * 
 */
package com.idega.block.school.data;

import java.rmi.RemoteException;
import com.idega.data.IDOEntity;
import com.idega.user.data.User;


/**
 * <p>
 * TODO Dainis Describe Type SchoolUser
 * </p>
 *  Last modified: $Date: 2004/06/28 09:09:50 $ by $Author: Dainis $
 * 
 * @author <a href="mailto:Dainis@idega.com">Dainis</a>
 * @version $Revision: 1.1 $
 */
public interface SchoolUser extends IDOEntity {

	/**
	 * @see com.idega.block.school.data.SchoolUserBMPBean#setSchoolId
	 */
	public void setSchoolId(int schoolId);

	/**
	 * @see com.idega.block.school.data.SchoolUserBMPBean#getSchoolId
	 */
	public int getSchoolId();

	/**
	 * @see com.idega.block.school.data.SchoolUserBMPBean#setUserId
	 */
	public void setUserId(int userId);

	/**
	 * @see com.idega.block.school.data.SchoolUserBMPBean#getUserId
	 */
	public int getUserId();

	/**
	 * @see com.idega.block.school.data.SchoolUserBMPBean#getUser
	 */
	public User getUser();

	/**
	 * @see com.idega.block.school.data.SchoolUserBMPBean#setUserType
	 */
	public void setUserType(int userType);

	/**
	 * @see com.idega.block.school.data.SchoolUserBMPBean#getUserType
	 */
	public int getUserType();

	/**
	 * @see com.idega.block.school.data.SchoolUserBMPBean#setMainHeadmaster
	 */
	public void setMainHeadmaster(boolean mainHead);

	/**
	 * @see com.idega.block.school.data.SchoolUserBMPBean#getMainHeadmaster
	 */
	public boolean getMainHeadmaster();

	/**
	 * @see com.idega.block.school.data.SchoolUserBMPBean#setIsEconomicalResponsible
	 */
	public void setIsEconomicalResponsible(boolean b);

	/**
	 * @see com.idega.block.school.data.SchoolUserBMPBean#isEconomicalResponsible
	 */
	public boolean isEconomicalResponsible();

	/**
	 * @see com.idega.block.school.data.SchoolUserBMPBean#setShowInContact
	 */
	public void setShowInContact(boolean showinContacts);

	/**
	 * @see com.idega.block.school.data.SchoolUserBMPBean#getShowInContact
	 */
	public boolean getShowInContact();

	/**
	 * @see com.idega.block.school.data.SchoolUserBMPBean#getHome
	 */
	public SchoolUserHome getHome() throws RemoteException;
}
