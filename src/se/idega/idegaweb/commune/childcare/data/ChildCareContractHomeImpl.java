/*
 * $Id: ChildCareContractHomeImpl.java,v 1.16 2004/09/16 14:14:05 aron Exp $
 * Created on 16.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.data.IDOException;
import com.idega.data.IDOFactory;
import com.idega.user.data.User;
import com.idega.util.TimePeriod;

/**
 * 
 *  Last modified: $Date: 2004/09/16 14:14:05 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.16 $
 */
public class ChildCareContractHomeImpl extends IDOFactory implements
        ChildCareContractHome {
    protected Class getEntityInterfaceClass() {
        return ChildCareContract.class;
    }

    public ChildCareContract create() throws javax.ejb.CreateException {
        return (ChildCareContract) super.createIDO();
    }

    public ChildCareContract findByPrimaryKey(Object pk)
            throws javax.ejb.FinderException {
        return (ChildCareContract) super.findByPrimaryKeyIDO(pk);
    }

    public Collection findByChild(int childID) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareContractBMPBean) entity)
                .ejbFindByChild(childID);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByChildAndDateRange(User child, Date startDate,
            Date endDate) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareContractBMPBean) entity)
                .ejbFindByChildAndDateRange(child, startDate, endDate);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByChildAndProvider(int childID, int providerID)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareContractBMPBean) entity)
                .ejbFindByChildAndProvider(childID, providerID);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByApplication(int applicationID)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareContractBMPBean) entity)
                .ejbFindByApplication(applicationID);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public ChildCareContract findValidContractByApplication(int applicationID,
            Date date) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((ChildCareContractBMPBean) entity)
                .ejbFindValidContractByApplication(applicationID, date);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

    public Collection findValidContractByProvider(int providerID, Date date)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareContractBMPBean) entity)
                .ejbFindValidContractByProvider(providerID, date);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public ChildCareContract findApplicationByContract(int contractID)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((ChildCareContractBMPBean) entity)
                .ejbFindApplicationByContract(contractID);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

    public ChildCareContract findValidContractByChild(int childID, Date date)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((ChildCareContractBMPBean) entity)
                .ejbFindValidContractByChild(childID, date);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

    public ChildCareContract findContractByChildAndPeriod(User child,
            TimePeriod period) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((ChildCareContractBMPBean) entity)
                .ejbFindContractByChildAndPeriod(child, period);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

    public ChildCareContract findLatestTerminatedContractByChild(int childID,
            Date date) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((ChildCareContractBMPBean) entity)
                .ejbFindLatestTerminatedContractByChild(childID, date);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

    public ChildCareContract findNextContractByChild(ChildCareContract contract)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((ChildCareContractBMPBean) entity)
                .ejbFindNextContractByChild(contract);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

    public ChildCareContract findNextTerminatedContractByChild(
            ChildCareContract contract) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((ChildCareContractBMPBean) entity)
                .ejbFindNextTerminatedContractByChild(contract);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

    public ChildCareContract findPreviousTerminatedContractByChild(
            ChildCareContract contract) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((ChildCareContractBMPBean) entity)
                .ejbFindPreviousTerminatedContractByChild(contract);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

    public ChildCareContract findLatestContractByChild(int childID)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((ChildCareContractBMPBean) entity)
                .ejbFindLatestContractByChild(childID);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

    public ChildCareContract findLatestContractByApplication(int applicationID)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((ChildCareContractBMPBean) entity)
                .ejbFindLatestContractByApplication(applicationID);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

    public Collection findLatestByApplication(int applicationID,
            int maxNumberOfContracts) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareContractBMPBean) entity)
                .ejbFindLatestByApplication(applicationID, maxNumberOfContracts);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public ChildCareContract findFirstContractByApplication(int applicationID)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((ChildCareContractBMPBean) entity)
                .ejbFindFirstContractByApplication(applicationID);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

    public Collection findFutureContractsByApplication(int applicationID,
            Date date) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareContractBMPBean) entity)
                .ejbFindFutureContractsByApplication(applicationID, date);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public int getNumberOfActiveNotWithProvider(int childID, int providerID)
            throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareContractBMPBean) entity)
                .ejbHomeGetNumberOfActiveNotWithProvider(childID, providerID);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getNumberOfActiveForApplication(int applicationID, Date date)
            throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareContractBMPBean) entity)
                .ejbHomeGetNumberOfActiveForApplication(applicationID, date);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getNumberOfTerminatedLaterNotWithProvider(int childID,
            int providerID, Date date) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareContractBMPBean) entity)
                .ejbHomeGetNumberOfTerminatedLaterNotWithProvider(childID,
                        providerID, date);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getFutureContractsCountByApplication(int applicationID, Date date)
            throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareContractBMPBean) entity)
                .ejbHomeGetFutureContractsCountByApplication(applicationID,
                        date);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getContractsCountByApplication(int applicationID)
            throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareContractBMPBean) entity)
                .ejbHomeGetContractsCountByApplication(applicationID);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getContractsCountByDateRangeAndProvider(Date startDate,
            Date endDate, int providerID) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareContractBMPBean) entity)
                .ejbHomeGetContractsCountByDateRangeAndProvider(startDate,
                        endDate, providerID);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public ChildCareContract findByContractFileID(int contractFileID)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((ChildCareContractBMPBean) entity)
                .ejbFindByContractFileID(contractFileID);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

    public Collection findByDateRange(Date startDate, Date endDate)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareContractBMPBean) entity)
                .ejbFindByDateRange(startDate, endDate);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByDateRangeAndProviderWhereStatusActive(
            Date startDate, Date endDate, School school) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareContractBMPBean) entity)
                .ejbFindByDateRangeAndProviderWhereStatusActive(startDate,
                        endDate, school);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByDateRangeWhereStatusActive(Date startDate,
            Date endDate) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareContractBMPBean) entity)
                .ejbFindByDateRangeWhereStatusActive(startDate, endDate);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public ChildCareContract findBySchoolClassMember(SchoolClassMember placement)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((ChildCareContractBMPBean) entity)
                .ejbFindBySchoolClassMember(placement);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

    public Collection findAll() throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareContractBMPBean) entity)
                .ejbFindAll();
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByInvoiceReceiver(Integer invoiceReceiverID)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareContractBMPBean) entity)
                .ejbFindByInvoiceReceiver(invoiceReceiverID);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByInvoiceReceiverActiveOrFuture(
            Integer invoiceReceiverID, Date fromDate) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareContractBMPBean) entity)
                .ejbFindByInvoiceReceiverActiveOrFuture(invoiceReceiverID,
                        fromDate);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllBySchoolClassMember(SchoolClassMember member)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareContractBMPBean) entity)
                .ejbFindAllBySchoolClassMember(member);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

}
