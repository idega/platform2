/**
 * 
 */
package com.idega.block.school.data;

import java.rmi.RemoteException;
import java.util.Collection;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOEntity;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.user.data.User;


/**
 * <p>
 * TODO Maris_O Describe Type SchoolUser
 * </p>
 *  Last modified: $Date: 2004/06/28 09:09:50 $ by $Author: Maris_O $
 * 
 * @author <a href="mailto:Maris_O@idega.com">Maris_O</a>
 * @version $Revision: 1.1 $
 */
public interface SchoolUser extends IDOEntity
{

    /**
     * @see com.idega.block.school.data.SchoolUserBMPBean#getSchools
     */
    public Collection getSchools() throws IDORelationshipException;

    /**
     * @see com.idega.block.school.data.SchoolUserBMPBean#addSchools
     */
    public void addSchools(Collection schools) throws IDOAddRelationshipException;

    /**
     * @see com.idega.block.school.data.SchoolUserBMPBean#removeSchool
     */
    public void removeSchool(School school) throws IDORemoveRelationshipException;

    /**
     * @see com.idega.block.school.data.SchoolUserBMPBean#removeSchools
     */
    public void removeSchools() throws IDORemoveRelationshipException;

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
