/**
 * 
 */
package com.idega.block.school.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOHome;
import com.idega.user.data.User;


/**
 * <p>
 * TODO Maris_O Describe Type SchoolUserHome
 * </p>
 *  Last modified: $Date: 2004/06/28 09:09:50 $ by $Author: Maris_O $
 * 
 * @author <a href="mailto:Maris_O@idega.com">Maris_O</a>
 * @version $Revision: 1.1 $
 */
public interface SchoolUserHome extends IDOHome
{

    public SchoolUser create() throws javax.ejb.CreateException;

    public SchoolUser findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

    /**
     * @see com.idega.block.school.data.SchoolUserBMPBean#ejbFindBySchoolAndType
     */
    public Collection findBySchoolAndType(School school, int userType) throws FinderException;

    /**
     * @see com.idega.block.school.data.SchoolUserBMPBean#ejbFindByTypes
     */
    public Collection findByTypes(int[] userTypes) throws FinderException;

    /**
     * @see com.idega.block.school.data.SchoolUserBMPBean#ejbFindBySchoolAndTypes
     */
    public Collection findBySchoolAndTypes(School school, int[] userTypes) throws FinderException;

    /**
     * @see com.idega.block.school.data.SchoolUserBMPBean#ejbFindBySchoolAndIsEconomicalResponsible
     */
    public Collection findBySchoolAndIsEconomicalResponsible(School school) throws FinderException;

    /**
     * @see com.idega.block.school.data.SchoolUserBMPBean#ejbFindBySchoolAndTypeAndDepartment
     */
    public Collection findBySchoolAndTypeAndDepartment(School school, int userType, int departmentID)
            throws FinderException;

    /**
     * @see com.idega.block.school.data.SchoolUserBMPBean#ejbFindBySchoolAndDepartment
     */
    public Collection findBySchoolAndDepartment(School school, int departmentID) throws FinderException;

    /**
     * @see com.idega.block.school.data.SchoolUserBMPBean#ejbFindBySchoolAndMainHeadmaster
     */
    public Collection findBySchoolAndMainHeadmaster(School school, int userType, boolean main_headmaster)
            throws FinderException;

    /**
     * @see com.idega.block.school.data.SchoolUserBMPBean#ejbFindByUser
     */
    public Collection findByUser(User user) throws FinderException;

    /**
     * @see com.idega.block.school.data.SchoolUserBMPBean#ejbFindForUser
     */
    public SchoolUser findForUser(User user) throws FinderException;

    /**
     * @see com.idega.block.school.data.SchoolUserBMPBean#ejbFindBySchoolAndUser
     */
    public Collection findBySchoolAndUser(School school, User user) throws FinderException;

    /**
     * @see com.idega.block.school.data.SchoolUserBMPBean#ejbFindBySchool
     */
    public Collection findBySchool(School school) throws FinderException;

    /**
     * @see com.idega.block.school.data.SchoolUserBMPBean#ejbHomeGetSchoolUserId
     */
    public Object getSchoolUserId(School school, User user, int userType) throws FinderException;
}
