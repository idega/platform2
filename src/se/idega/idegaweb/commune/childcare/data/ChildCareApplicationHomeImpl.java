/*
 * $Id: ChildCareApplicationHomeImpl.java 1.1 6.9.2004 aron Exp $
 * Created on 6.9.2004
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


import com.idega.block.process.data.CaseStatus;
import com.idega.block.school.data.School;
import com.idega.data.IDOException;
import com.idega.data.IDOFactory;
import com.idega.user.data.User;

/**
 * 
 *  Last modified: $Date: 6.9.2004 14:26:48 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public class ChildCareApplicationHomeImpl extends IDOFactory implements
        ChildCareApplicationHome {
    protected Class getEntityInterfaceClass() {
        return ChildCareApplication.class;
    }

    public ChildCareApplication create() throws javax.ejb.CreateException {
        return (ChildCareApplication) super.createIDO();
    }

    public ChildCareApplication findByPrimaryKey(Object pk)
            throws javax.ejb.FinderException {
        return (ChildCareApplication) super.findByPrimaryKeyIDO(pk);
    }

    public Collection findAll() throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindAll();
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllCasesByProviderAndStatus(int providerId,
            CaseStatus caseStatus) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindAllCasesByProviderAndStatus(providerId, caseStatus);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllCasesByProviderAndStatus(School provider,
            String caseStatus) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindAllCasesByProviderAndStatus(provider, caseStatus);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllCasesByProviderAndStatus(School provider,
            CaseStatus caseStatus) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindAllCasesByProviderAndStatus(provider, caseStatus);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllCasesByProviderStatus(int providerId,
            String caseStatus) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindAllCasesByProviderStatus(providerId, caseStatus);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllChildCasesByProvider(int providerId)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindAllChildCasesByProvider(providerId);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllCasesByProviderAndStatus(int providerId,
            String caseStatus, int numberOfEntries, int startingEntry)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindAllCasesByProviderAndStatus(providerId, caseStatus,
                        numberOfEntries, startingEntry);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllCasesByProviderAndNotInStatus(int providerId,
            String[] caseStatus, int numberOfEntries, int startingEntry)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindAllCasesByProviderAndNotInStatus(providerId,
                        caseStatus, numberOfEntries, startingEntry);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllCasesByProviderAndNotInStatus(int providerId,
            int sortBy, Date fromDate, Date toDate, String[] caseStatus,
            int numberOfEntries, int startingEntry) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindAllCasesByProviderAndNotInStatus(providerId, sortBy,
                        fromDate, toDate, caseStatus, numberOfEntries,
                        startingEntry);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllCasesByProviderStatus(int providerId,
            String[] caseStatus) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindAllCasesByProviderStatus(providerId, caseStatus);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllByAreaAndApplicationStatus(Object areaID,
            String[] applicationStatus, String caseCode, Date queueDate,
            Date placementDate, boolean firstHandOnly) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindAllByAreaAndApplicationStatus(areaID,
                        applicationStatus, caseCode, queueDate, placementDate,
                        firstHandOnly);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllCasesByProviderStatusNotRejected(int providerId,
            String caseStatus) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindAllCasesByProviderStatusNotRejected(providerId,
                        caseStatus);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllCasesByUserAndStatus(User owner, String caseStatus)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindAllCasesByUserAndStatus(owner, caseStatus);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllCasesByStatus(String caseStatus)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindAllCasesByStatus(caseStatus);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findApplicationsByProviderAndStatus(int providerID,
            String caseStatus) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindApplicationsByProviderAndStatus(providerID, caseStatus);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findApplicationsByProviderAndStatus(int providerID,
            String[] caseStatus) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindApplicationsByProviderAndStatus(providerID, caseStatus);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findApplicationsByProviderAndStatus(int providerID,
            String[] caseStatus, String caseCode) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindApplicationsByProviderAndStatus(providerID, caseStatus,
                        caseCode);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findApplicationsByProviderAndStatus(int providerID,
            String caseStatus, int numberOfEntries, int startingEntry)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindApplicationsByProviderAndStatus(providerID, caseStatus,
                        numberOfEntries, startingEntry);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findApplicationsByProviderAndStatus(int providerID,
            String[] caseStatus, int numberOfEntries, int startingEntry)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindApplicationsByProviderAndStatus(providerID, caseStatus,
                        numberOfEntries, startingEntry);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findApplicationsByProviderAndStatus(int providerID,
            String[] caseStatus, String caseCode, int numberOfEntries,
            int startingEntry) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindApplicationsByProviderAndStatus(providerID, caseStatus,
                        caseCode, numberOfEntries, startingEntry);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findApplicationsByProviderAndStatus(Integer providerID,
            String[] applicationStatus, Date fromDateOfBirth,
            Date toDateOfBirth, Date fromDate, Date toDate)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindApplicationsByProviderAndStatus(providerID,
                        applicationStatus, fromDateOfBirth, toDateOfBirth,
                        fromDate, toDate);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findApplicationsByProviderAndApplicationStatus(
            int providerID, String[] applicationStatuses)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindApplicationsByProviderAndApplicationStatus(providerID,
                        applicationStatuses);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findApplicationsByProviderAndApplicationStatus(
            int providerID, String[] applicationStatuses, String caseCode)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindApplicationsByProviderAndApplicationStatus(providerID,
                        applicationStatuses, caseCode);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findApplicationsByProviderAndApplicationStatus(
            int providerID, String[] applicationStatuses, String caseCode,
            int numberOfEntries, int startingEntry) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindApplicationsByProviderAndApplicationStatus(providerID,
                        applicationStatuses, caseCode, numberOfEntries,
                        startingEntry);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public ChildCareApplication findApplicationByChildAndApplicationStatus(
            int childID, String[] applicationStatuses) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((ChildCareApplicationBMPBean) entity)
                .ejbFindApplicationByChildAndApplicationStatus(childID,
                        applicationStatuses);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

    public Collection findApplicationsByChildAndApplicationStatus(int childID,
            String[] applicationStatuses) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindApplicationsByChildAndApplicationStatus(childID,
                        applicationStatuses);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findApplicationsWithoutPlacing() throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindApplicationsWithoutPlacing();
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public ChildCareApplication findApplicationByChildAndChoiceNumber(
            User child, int choiceNumber) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((ChildCareApplicationBMPBean) entity)
                .ejbFindApplicationByChildAndChoiceNumber(child, choiceNumber);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

    public ChildCareApplication findApplicationByChildAndChoiceNumber(
            int childID, int choiceNumber) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((ChildCareApplicationBMPBean) entity)
                .ejbFindApplicationByChildAndChoiceNumber(childID, choiceNumber);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

    public ChildCareApplication findApplicationByChildAndChoiceNumberWithStatus(
            int childID, int choiceNumber, String caseStatus)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((ChildCareApplicationBMPBean) entity)
                .ejbFindApplicationByChildAndChoiceNumberWithStatus(childID,
                        choiceNumber, caseStatus);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

    public ChildCareApplication findApplicationByChildAndChoiceNumberInStatus(
            int childID, int choiceNumber, String[] caseStatus)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((ChildCareApplicationBMPBean) entity)
                .ejbFindApplicationByChildAndChoiceNumberInStatus(childID,
                        choiceNumber, caseStatus);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

    public ChildCareApplication findApplicationByChildAndChoiceNumberNotInStatus(
            int childID, int choiceNumber, String[] caseStatus)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((ChildCareApplicationBMPBean) entity)
                .ejbFindApplicationByChildAndChoiceNumberNotInStatus(childID,
                        choiceNumber, caseStatus);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

    public Collection findApplicationByChild(int childID)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindApplicationByChild(childID);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public ChildCareApplication findApplicationByChildAndProvider(int childID,
            int providerID) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((ChildCareApplicationBMPBean) entity)
                .ejbFindApplicationByChildAndProvider(childID, providerID);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

    public ChildCareApplication findApplicationByChildAndProviderAndStatus(
            int childID, int providerID, String[] status)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((ChildCareApplicationBMPBean) entity)
                .ejbFindApplicationByChildAndProviderAndStatus(childID,
                        providerID, status);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

    public ChildCareApplication findNewestApplication(int providerID, Date date)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((ChildCareApplicationBMPBean) entity)
                .ejbFindNewestApplication(providerID, date);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

    public ChildCareApplication findOldestApplication(int providerID, Date date)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((ChildCareApplicationBMPBean) entity)
                .ejbFindOldestApplication(providerID, date);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

    public Collection findApplicationByChildAndNotInStatus(int childID,
            String[] caseStatus) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindApplicationByChildAndNotInStatus(childID, caseStatus);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findApplicationByChildAndNotInStatus(int childID,
            String[] caseStatus, String caseCode) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindApplicationByChildAndNotInStatus(childID, caseStatus,
                        caseCode);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findApplicationByChildAndInStatus(int childID,
            String[] caseStatus, String caseCode) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindApplicationByChildAndInStatus(childID, caseStatus,
                        caseCode);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public ChildCareApplication findActiveApplicationByChild(int childID)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((ChildCareApplicationBMPBean) entity)
                .ejbFindActiveApplicationByChild(childID);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

    public ChildCareApplication findActiveApplicationByChildAndStatus(
            int childID, String[] caseStatus) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((ChildCareApplicationBMPBean) entity)
                .ejbFindActiveApplicationByChildAndStatus(childID, caseStatus);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

    public int getNumberOfActiveApplications(int childID) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetNumberOfActiveApplications(childID);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getNumberOfActiveApplications(int childID, String caseCode)
            throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetNumberOfActiveApplications(childID, caseCode);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getNumberOfApplicationsByStatusAndActiveDate(int childID,
            String[] caseStatus, String caseCode, Date activeDate)
            throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetNumberOfApplicationsByStatusAndActiveDate(childID,
                        caseStatus, caseCode, activeDate);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public Collection findApplicationsByProviderAndDate(int providerID,
            Date date) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindApplicationsByProviderAndDate(providerID, date);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findApplicationsBeforeLastReplyDate(Date date,
            String[] caseStatus) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindApplicationsBeforeLastReplyDate(date, caseStatus);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findApplicationsByProviderAndBeforeDate(int providerID,
            Date date, String[] caseStatus) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindApplicationsByProviderAndBeforeDate(providerID, date,
                        caseStatus);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public int getNumberOfApplications(int providerID, String caseStatus)
            throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetNumberOfApplications(providerID, caseStatus);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getNumberOfApplications(int providerID, String[] caseStatus)
            throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetNumberOfApplications(providerID, caseStatus);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getNumberOfApplicationsByStatus(int providerID,
            String[] caseStatus) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetNumberOfApplicationsByStatus(providerID, caseStatus);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getNumberOfApplicationsForChild(int childID) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetNumberOfApplicationsForChild(childID);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getNumberOfApplicationsForChild(int childID, String caseStatus,
            String caseCode) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetNumberOfApplicationsForChild(childID, caseStatus,
                        caseCode);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getNumberOfApplicationsForChildNotInStatus(int childID,
            String[] caseStatus) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetNumberOfApplicationsForChildNotInStatus(childID,
                        caseStatus);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getNumberOfApplicationsForChildNotInStatus(int childID,
            String[] caseStatus, String caseCode) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetNumberOfApplicationsForChildNotInStatus(childID,
                        caseStatus, caseCode);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getNumberOfApplicationsForChildInStatus(int childID,
            String[] caseStatus, String caseCode) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetNumberOfApplicationsForChildInStatus(childID,
                        caseStatus, caseCode);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getNumberOfPlacedApplications(int childID, int providerID,
            String[] caseStatus) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetNumberOfPlacedApplications(childID, providerID,
                        caseStatus);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getNumberOfApplications(int providerID, String[] caseStatus,
            int sortBy, Date fromDate, Date toDate) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetNumberOfApplications(providerID, caseStatus, sortBy,
                        fromDate, toDate);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getPositionInQueue(Date queueDate, int providerID,
            String[] caseStatus) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetPositionInQueue(queueDate, providerID, caseStatus);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getPositionInQueue(Date queueDate, int providerID,
            String applicationStatus) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetPositionInQueue(queueDate, providerID,
                        applicationStatus);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getPositionInQueueByDate(int queueOrder, Date queueDate,
            int providerID, String[] caseStatus) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetPositionInQueueByDate(queueOrder, queueDate,
                        providerID, caseStatus);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getPositionInQueueByDate(int queueOrder, Date queueDate,
            int providerID, String applicationStatus) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetPositionInQueueByDate(queueOrder, queueDate,
                        providerID, applicationStatus);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getQueueSizeNotInStatus(int providerID, String[] caseStatus)
            throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetQueueSizeNotInStatus(providerID, caseStatus);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getQueueSizeInStatus(int providerID, String[] applicationStatus,
            Date from, Date to, boolean isOnlyFirstHand) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetQueueSizeInStatus(providerID, applicationStatus,
                        from, to, isOnlyFirstHand);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getBruttoQueueSizeInStatus(int providerID,
            String[] applicationStatus, Date from, Date to,
            boolean isOnlyFirstHand) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetBruttoQueueSizeInStatus(providerID,
                        applicationStatus, from, to, isOnlyFirstHand);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getNettoQueueSizeInStatus(int providerID,
            String[] applicationStatus, Date from, Date to,
            boolean isOnlyFirstHand) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetNettoQueueSizeInStatus(providerID,
                        applicationStatus, from, to, isOnlyFirstHand);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getQueueSizeInStatus(int providerID, String caseStatus)
            throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetQueueSizeInStatus(providerID, caseStatus);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getQueueSizeInStatus(int providerID, String applicationStatus,
            Date from, Date to, boolean isOnlyFirstHand) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetQueueSizeInStatus(providerID, applicationStatus,
                        from, to, isOnlyFirstHand);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getBruttoQueueSizeInStatus(int providerID,
            String applicationStatus, Date from, Date to,
            boolean isOnlyFirstHand) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetBruttoQueueSizeInStatus(providerID,
                        applicationStatus, from, to, isOnlyFirstHand);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getNettoQueueSizeInStatus(int providerID,
            String applicationStatus, Date from, Date to,
            boolean isOnlyFirstHand) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetNettoQueueSizeInStatus(providerID,
                        applicationStatus, from, to, isOnlyFirstHand);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getQueueSizeByAreaNotInStatus(int areaID, String[] caseStatus)
            throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetQueueSizeByAreaNotInStatus(areaID, caseStatus);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getQueueSizeByAreaInStatus(int areaID, String caseStatus)
            throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetQueueSizeByAreaInStatus(areaID, caseStatus);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getNumberOfApplicationsByProviderAndChoiceNumber(int providerID,
            int choiceNumber) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetNumberOfApplicationsByProviderAndChoiceNumber(
                        providerID, choiceNumber);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getQueueByProviderAndChoiceNumber(int providerID,
            int choiceNumber, String status, Date from, Date to)
            throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetQueueByProviderAndChoiceNumber(providerID,
                        choiceNumber, status, from, to);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getNettoQueueByProviderAndChoiceNumber(int providerID,
            int choiceNumber, String status, Date from, Date to)
            throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetNettoQueueByProviderAndChoiceNumber(providerID,
                        choiceNumber, status, from, to);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getBruttoQueueByProviderAndChoiceNumber(int providerID,
            int choiceNumber, String status, Date from, Date to)
            throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((ChildCareApplicationBMPBean) entity)
                .ejbHomeGetBruttoQueueByProviderAndChoiceNumber(providerID,
                        choiceNumber, status, from, to);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public Collection findApplicationsInSchoolAreaByStatus(int schoolAreaID,
            String[] statuses, int choiceNumber) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ChildCareApplicationBMPBean) entity)
                .ejbFindApplicationsInSchoolAreaByStatus(schoolAreaID,
                        statuses, choiceNumber);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

}
