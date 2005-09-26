/**
 * 
 */
package com.idega.block.school.data;

import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.data.IDOException;
import com.idega.data.IDOHome;
import com.idega.data.IDOLookupException;
import com.idega.user.data.Group;

/**
 * @author Dainis
 *
 */
public interface SchoolHome extends IDOHome {
    public School create() throws javax.ejb.CreateException;

    public School findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

    /**
     * @see com.idega.block.school.data.SchoolBMPBean#ejbFindAllBySchoolType
     */
    public Collection findAllBySchoolType(Collection typeIds)
            throws javax.ejb.FinderException;

    /**
     * @see com.idega.block.school.data.SchoolBMPBean#ejbFindAllBySchoolType
     */
    public Collection findAllBySchoolType(int typeId)
            throws javax.ejb.FinderException;

    /**
     * @see com.idega.block.school.data.SchoolBMPBean#ejbFindAllBySchoolType
     */
    public Collection findAllBySchoolType(SchoolType type)
            throws javax.ejb.FinderException;

    /**
     * @see com.idega.block.school.data.SchoolBMPBean#ejbFindAllByAreaTypeManagement
     */
    public Collection findAllByAreaTypeManagement(int areaId, int typeId,
            String managementType) throws javax.ejb.FinderException;

    /**
     * @see com.idega.block.school.data.SchoolBMPBean#ejbFindAllByAreaTypeManagementCommune
     */
    public Collection findAllByAreaTypeManagementCommune(int areaId,
            int typeId, String managementType, int communeId)
            throws javax.ejb.FinderException;

    /**
     * @see com.idega.block.school.data.SchoolBMPBean#ejbFindAllByAreaTypeManagementCommune
     */
    public Collection findAllByAreaTypeManagementCommune(int areaId,
            int typeId, Collection managementTypes, int communeId)
            throws javax.ejb.FinderException;

    /**
     * @see com.idega.block.school.data.SchoolBMPBean#ejbFindAllByAreaTypeManagementCommune
     */
    public Collection findAllByAreaTypeManagementCommune(int areaId,
            Collection typeIds, Collection managementTypes, int communeId)
            throws javax.ejb.FinderException;

    /**
     * @see com.idega.block.school.data.SchoolBMPBean#ejbFindAllByAreaTypeCommune
     */
    public Collection findAllByAreaTypeCommune(int areaId, int typeId,
            int communeId) throws javax.ejb.FinderException;

    /**
     * @see com.idega.block.school.data.SchoolBMPBean#ejbFindAllBySchoolName
     */
    public Collection findAllBySchoolName(String schoolName)
            throws javax.ejb.FinderException;

    /**
     * @see com.idega.block.school.data.SchoolBMPBean#ejbFindBySchoolName
     */
    public School findBySchoolName(String schoolName)
            throws javax.ejb.FinderException;

    /**
     * @see com.idega.block.school.data.SchoolBMPBean#ejbFindAllCentralizedAdministrated
     */
    public Collection findAllCentralizedAdministrated()
            throws javax.ejb.FinderException;

    /**
     * @see com.idega.block.school.data.SchoolBMPBean#ejbFindAllCentralizedAdministratedByType
     */
    public Collection findAllCentralizedAdministratedByType(Collection typeIds)
            throws javax.ejb.FinderException;

    /**
     * @see com.idega.block.school.data.SchoolBMPBean#ejbFindAllBySchoolArea
     */
    public Collection findAllBySchoolArea(int areaId)
            throws javax.ejb.FinderException;

    /**
     * @see com.idega.block.school.data.SchoolBMPBean#ejbFindAllSchools
     */
    public Collection findAllSchools() throws javax.ejb.FinderException;

    /**
     * @see com.idega.block.school.data.SchoolBMPBean#ejbFindAllSchoolsIncludingTerminated
     */
    public Collection findAllSchoolsIncludingTerminated()
            throws javax.ejb.FinderException;

    /**
     * @see com.idega.block.school.data.SchoolBMPBean#ejbFindAllSchoolsByCategoryIncludingTerminated
     */
    public Collection findAllSchoolsByCategoryIncludingTerminated(
            String category) throws javax.ejb.FinderException;

    /**
     * @see com.idega.block.school.data.SchoolBMPBean#ejbFindAllByAreaAndType
     */
    public Collection findAllByAreaAndType(int area, int type)
            throws javax.ejb.FinderException;

    /**
     * @see com.idega.block.school.data.SchoolBMPBean#ejbFindAllByInQuery
     */
    public Collection findAllByInQuery(String inQuery) throws FinderException;

    /**
     * @see com.idega.block.school.data.SchoolBMPBean#ejbFindAllByAreaAndTypeAndYear
     */
    public Collection findAllByAreaAndTypeAndYear(int areaID, int typeID,
            int yearID) throws javax.ejb.FinderException;

    /**
     * @see com.idega.block.school.data.SchoolBMPBean#ejbFindAllByAreaAndTypes
     */
    public Collection findAllByAreaAndTypes(int area, Collection types)
            throws javax.ejb.FinderException;

    /**
     * @see com.idega.block.school.data.SchoolBMPBean#ejbFindAllByAreaAndTypesAndYear
     */
    public Collection findAllByAreaAndTypesAndYear(int area, Collection types,
            int yearID) throws javax.ejb.FinderException;

    /**
     * @see com.idega.block.school.data.SchoolBMPBean#ejbFindAllBySubAreaAndTypes
     */
    public Collection findAllBySubAreaAndTypes(int subarea, Collection types)
            throws javax.ejb.FinderException;

    /**
     * @see com.idega.block.school.data.SchoolBMPBean#ejbFindAllByCategory
     */
    public Collection findAllByCategory(SchoolCategory schoolCategory)
            throws javax.ejb.FinderException;

    /**
     * @see com.idega.block.school.data.SchoolBMPBean#ejbFindAllInHomeCommuneByCategory
     */
    public Collection findAllInHomeCommuneByCategory(
            SchoolCategory schoolCategory) throws IDOLookupException,
            EJBException, FinderException;

    /**
     * @see com.idega.block.school.data.SchoolBMPBean#ejbFindAllBySchoolGroup
     */
    public Collection findAllBySchoolGroup(Group schoolGroup)
            throws javax.ejb.FinderException;

    /**
     * @see com.idega.block.school.data.SchoolBMPBean#ejbHomeGetNumberOfRelations
     */
    public int getNumberOfRelations(School school, SchoolYear year)
            throws IDOException;

    /**
     * @see com.idega.block.school.data.SchoolBMPBean#ejbHomeGetNumberOfFreetimeTypes
     */
    public int getNumberOfFreetimeTypes(int schoolID) throws IDOException;

    /**
     * @see com.idega.block.school.data.SchoolBMPBean#ejbFindAllPrivate
     */
    public Collection findAllPrivate() throws IDOLookupException, EJBException,
            FinderException;

}
