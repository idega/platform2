/**
 * 
 */
package com.idega.block.school.data;

import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.data.IDOException;
import com.idega.data.IDOFactory;
import com.idega.data.IDOLookupException;
import com.idega.user.data.Group;

/**
 * @author Dainis
 *
 */
public class SchoolHomeImpl extends IDOFactory implements SchoolHome {
    protected Class getEntityInterfaceClass() {
        return School.class;
    }

    public School create() throws javax.ejb.CreateException {
        return (School) super.createIDO();
    }

    public School findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
        return (School) super.findByPrimaryKeyIDO(pk);
    }

    public Collection findAllBySchoolType(Collection typeIds)
            throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolBMPBean) entity)
                .ejbFindAllBySchoolType(typeIds);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllBySchoolType(int typeId)
            throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolBMPBean) entity)
                .ejbFindAllBySchoolType(typeId);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllBySchoolType(SchoolType type)
            throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolBMPBean) entity)
                .ejbFindAllBySchoolType(type);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllByAreaTypeManagement(int areaId, int typeId,
            String managementType) throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolBMPBean) entity)
                .ejbFindAllByAreaTypeManagement(areaId, typeId, managementType);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllByAreaTypeManagementCommune(int areaId,
            int typeId, String managementType, int communeId)
            throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolBMPBean) entity)
                .ejbFindAllByAreaTypeManagementCommune(areaId, typeId,
                        managementType, communeId);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllByAreaTypeManagementCommune(int areaId,
            int typeId, Collection managementTypes, int communeId)
            throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolBMPBean) entity)
                .ejbFindAllByAreaTypeManagementCommune(areaId, typeId,
                        managementTypes, communeId);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllByAreaTypeManagementCommune(int areaId,
            Collection typeIds, Collection managementTypes, int communeId)
            throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolBMPBean) entity)
                .ejbFindAllByAreaTypeManagementCommune(areaId, typeIds,
                        managementTypes, communeId);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllByAreaTypeCommune(int areaId, int typeId,
            int communeId) throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolBMPBean) entity)
                .ejbFindAllByAreaTypeCommune(areaId, typeId, communeId);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllBySchoolName(String schoolName)
            throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolBMPBean) entity)
                .ejbFindAllBySchoolName(schoolName);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public School findBySchoolName(String schoolName)
            throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((SchoolBMPBean) entity).ejbFindBySchoolName(schoolName);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

    public Collection findAllCentralizedAdministrated()
            throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolBMPBean) entity)
                .ejbFindAllCentralizedAdministrated();
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllCentralizedAdministratedByType(Collection typeIds)
            throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolBMPBean) entity)
                .ejbFindAllCentralizedAdministratedByType(typeIds);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllBySchoolArea(int areaId)
            throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolBMPBean) entity)
                .ejbFindAllBySchoolArea(areaId);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllSchools() throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolBMPBean) entity).ejbFindAllSchools();
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllSchoolsIncludingTerminated()
            throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolBMPBean) entity)
                .ejbFindAllSchoolsIncludingTerminated();
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllSchoolsByCategoryIncludingTerminated(
            String category) throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolBMPBean) entity)
                .ejbFindAllSchoolsByCategoryIncludingTerminated(category);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllByAreaAndType(int area, int type)
            throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolBMPBean) entity)
                .ejbFindAllByAreaAndType(area, type);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllByInQuery(String inQuery) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolBMPBean) entity)
                .ejbFindAllByInQuery(inQuery);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllByAreaAndTypeAndYear(int areaID, int typeID,
            int yearID) throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolBMPBean) entity)
                .ejbFindAllByAreaAndTypeAndYear(areaID, typeID, yearID);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllByAreaAndTypes(int area, Collection types)
            throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolBMPBean) entity)
                .ejbFindAllByAreaAndTypes(area, types);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllByAreaAndTypesAndYear(int area, Collection types,
            int yearID) throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolBMPBean) entity)
                .ejbFindAllByAreaAndTypesAndYear(area, types, yearID);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllBySubAreaAndTypes(int subarea, Collection types)
            throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolBMPBean) entity)
                .ejbFindAllBySubAreaAndTypes(subarea, types);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllByCategory(SchoolCategory schoolCategory)
            throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolBMPBean) entity)
                .ejbFindAllByCategory(schoolCategory);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllInHomeCommuneByCategory(
            SchoolCategory schoolCategory) throws IDOLookupException,
            EJBException, FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolBMPBean) entity)
                .ejbFindAllInHomeCommuneByCategory(schoolCategory);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllBySchoolGroup(Group schoolGroup)
            throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolBMPBean) entity)
                .ejbFindAllBySchoolGroup(schoolGroup);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public int getNumberOfRelations(School school, SchoolYear year)
            throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((SchoolBMPBean) entity).ejbHomeGetNumberOfRelations(
                school, year);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getNumberOfFreetimeTypes(int schoolID) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((SchoolBMPBean) entity)
                .ejbHomeGetNumberOfFreetimeTypes(schoolID);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public Collection findAllPrivate() throws IDOLookupException, EJBException,
            FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolBMPBean) entity).ejbFindAllPrivate();
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

}
