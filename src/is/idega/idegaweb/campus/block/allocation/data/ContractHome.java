/*
 * $Id: ContractHome.java,v 1.11 2004/09/18 17:38:08 aron Exp $
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
import com.idega.data.IDOHome;

/**
 * 
 *  Last modified: $Date: 2004/09/18 17:38:08 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.11 $
 */
public interface ContractHome extends IDOHome {
    public Contract create() throws javax.ejb.CreateException;

    public Contract findByPrimaryKey(Object pk)
            throws javax.ejb.FinderException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#ejbFindByApplicantID
     */
    public Collection findByApplicantID(Integer ID) throws FinderException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#ejbFindByUserID
     */
    public Collection findByUserID(Integer ID) throws FinderException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#ejbFindByApartmentAndUser
     */
    public Collection findByApartmentAndUser(Integer AID, Integer UID)
            throws FinderException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#ejbFindByUserAndRented
     */
    public Collection findByUserAndRented(Integer ID, Boolean rented)
            throws FinderException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#ejbFindByApartmentID
     */
    public Collection findByApartmentID(Integer ID) throws FinderException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#ejbFindByApartmentAndStatus
     */
    public Collection findByApartmentAndStatus(Integer ID, String status)
            throws FinderException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#ejbFindByApartmentAndStatus
     */
    public Collection findByApartmentAndStatus(Integer ID, String[] status)
            throws FinderException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#ejbFindByApplicantAndStatus
     */
    public Collection findByApplicantAndStatus(Integer ID, String status)
            throws FinderException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#ejbFindByApplicantAndRented
     */
    public Collection findByApplicantAndRented(Integer ID, Boolean rented)
            throws FinderException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#ejbFindByApartmentAndRented
     */
    public Collection findByApartmentAndRented(Integer ID, Boolean rented)
            throws FinderException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#ejbFindByStatus
     */
    public Collection findByStatus(String status) throws FinderException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#ejbFindAll
     */
    public Collection findAll() throws FinderException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#ejbFindBySQL
     */
    public Collection findBySQL(String sql) throws FinderException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#ejbFindByApplicant
     */
    public java.util.Collection findByApplicant(Integer ID)
            throws FinderException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#ejbFindByApplicantInCreatedStatus
     */
    public Collection findByApplicantInCreatedStatus(Integer applicant)
            throws FinderException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#ejbHomeGetLastValidToForApartment
     */
    public Date getLastValidToForApartment(Integer apartment)
            throws FinderException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#ejbHomeGetLastValidFromForApartment
     */
    public Date getLastValidFromForApartment(Integer apartment)
            throws FinderException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#ejbFindBySearchConditions
     */
    public Collection findBySearchConditions(String status, Integer complexId,
            Integer buildingId, Integer floorId, Integer typeId,
            Integer categoryId, int order, int returnResultSize,
            int startingIndex) throws FinderException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#ejbHomeCountBySearchConditions
     */
    public int countBySearchConditions(String status, Integer complexId,
            Integer buildingId, Integer floorId, Integer typeId,
            Integer categoryId, int order) throws IDOException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#ejbFindByComplexAndBuildingAndApartmentName
     */
    public Collection findByComplexAndBuildingAndApartmentName(
            Integer complexID, Integer buildingID, String apartmentName)
            throws FinderException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#ejbFindByPersonalID
     */
    public Collection findByPersonalID(String ID) throws FinderException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#ejbHomeGetUnsignedApplicants
     */
    public Collection getUnsignedApplicants(String personalID)
            throws FinderException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#ejbFindByStatusAndValidBeforeDate
     */
    public Collection findByStatusAndValidBeforeDate(String status, Date date)
            throws FinderException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#ejbFindByStatusAndChangeDate
     */
    public Collection findByStatusAndChangeDate(String status, Date date)
            throws FinderException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#ejbFindByStatusAndOverLapPeriodMultiples
     */
    public Collection findByStatusAndOverLapPeriodMultiples(String[] status,
            Date from, Date to) throws FinderException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#ejbFindByUserAndStatus
     */
    public Collection findByUserAndStatus(Integer userId, String[] status)
            throws FinderException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean#ejbFindByUserAndStatus
     */
    public Collection findByUserAndStatus(Integer userId, String status)
            throws FinderException;

}
