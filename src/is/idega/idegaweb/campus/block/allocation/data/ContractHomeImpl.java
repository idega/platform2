/*
 * $Id: ContractHomeImpl.java,v 1.11 2004/09/18 17:38:08 aron Exp $
 * Created on 3.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.campus.block.allocation.data;


import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOException;
import com.idega.data.IDOFactory;

/**
 * 
 *  Last modified: $Date: 2004/09/18 17:38:08 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.11 $
 */
public class ContractHomeImpl extends IDOFactory implements ContractHome {
    protected Class getEntityInterfaceClass() {
        return Contract.class;
    }

    public Contract create() throws javax.ejb.CreateException {
        return (Contract) super.createIDO();
    }

    public Contract findByPrimaryKey(Object pk)
            throws javax.ejb.FinderException {
        return (Contract) super.findByPrimaryKeyIDO(pk);
    }

    public Collection findByApplicantID(Integer ID) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ContractBMPBean) entity)
                .ejbFindByApplicantID(ID);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByUserID(Integer ID) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ContractBMPBean) entity)
                .ejbFindByUserID(ID);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByApartmentAndUser(Integer AID, Integer UID)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ContractBMPBean) entity)
                .ejbFindByApartmentAndUser(AID, UID);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByUserAndRented(Integer ID, Boolean rented)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ContractBMPBean) entity)
                .ejbFindByUserAndRented(ID, rented);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByApartmentID(Integer ID) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ContractBMPBean) entity)
                .ejbFindByApartmentID(ID);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByApartmentAndStatus(Integer ID, String status)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ContractBMPBean) entity)
                .ejbFindByApartmentAndStatus(ID, status);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByApartmentAndStatus(Integer ID, String[] status)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ContractBMPBean) entity)
                .ejbFindByApartmentAndStatus(ID, status);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByApplicantAndStatus(Integer ID, String status)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ContractBMPBean) entity)
                .ejbFindByApplicantAndStatus(ID, status);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByApplicantAndRented(Integer ID, Boolean rented)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ContractBMPBean) entity)
                .ejbFindByApplicantAndRented(ID, rented);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByApartmentAndRented(Integer ID, Boolean rented)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ContractBMPBean) entity)
                .ejbFindByApartmentAndRented(ID, rented);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByStatus(String status) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ContractBMPBean) entity)
                .ejbFindByStatus(status);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAll() throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ContractBMPBean) entity).ejbFindAll();
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findBySQL(String sql) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ContractBMPBean) entity).ejbFindBySQL(sql);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public java.util.Collection findByApplicant(Integer ID)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ContractBMPBean) entity)
                .ejbFindByApplicant(ID);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByApplicantInCreatedStatus(Integer applicant)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ContractBMPBean) entity)
                .ejbFindByApplicantInCreatedStatus(applicant);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Date getLastValidToForApartment(Integer apartment)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Date theReturn = ((ContractBMPBean) entity)
                .ejbHomeGetLastValidToForApartment(apartment);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public Date getLastValidFromForApartment(Integer apartment)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Date theReturn = ((ContractBMPBean) entity)
                .ejbHomeGetLastValidFromForApartment(apartment);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public Collection findBySearchConditions(String status, Integer complexId,
            Integer buildingId, Integer floorId, Integer typeId,
            Integer categoryId, int order, int returnResultSize,
            int startingIndex) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ContractBMPBean) entity)
                .ejbFindBySearchConditions(status, complexId, buildingId,
                        floorId, typeId, categoryId, order, returnResultSize,
                        startingIndex);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public int countBySearchConditions(String status, Integer complexId,
            Integer buildingId, Integer floorId, Integer typeId,
            Integer categoryId, int order) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ContractBMPBean) entity)
                .ejbHomeCountBySearchConditions(status, complexId, buildingId,
                        floorId, typeId, categoryId, order);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public Collection findByComplexAndBuildingAndApartmentName(
            Integer complexID, Integer buildingID, String apartmentName)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ContractBMPBean) entity)
                .ejbFindByComplexAndBuildingAndApartmentName(complexID,
                        buildingID, apartmentName);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByPersonalID(String ID) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ContractBMPBean) entity)
                .ejbFindByPersonalID(ID);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection getUnsignedApplicants(String personalID)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Collection theReturn = ((ContractBMPBean) entity)
                .ejbHomeGetUnsignedApplicants(personalID);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public Collection findByStatusAndValidBeforeDate(String status, Date date)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ContractBMPBean) entity)
                .ejbFindByStatusAndValidBeforeDate(status, date);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByStatusAndChangeDate(String status, Date date)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ContractBMPBean) entity)
                .ejbFindByStatusAndChangeDate(status, date);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByStatusAndOverLapPeriodMultiples(String[] status,
            Date from, Date to) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ContractBMPBean) entity)
                .ejbFindByStatusAndOverLapPeriodMultiples(status, from, to);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByUserAndStatus(Integer userId, String[] status)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ContractBMPBean) entity)
                .ejbFindByUserAndStatus(userId, status);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByUserAndStatus(Integer userId, String status)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ContractBMPBean) entity)
                .ejbFindByUserAndStatus(userId, status);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

}
