/*
 * $Id: ChildCareApplicationHome.java,v 1.1 2004/10/14 11:42:34 thomas Exp $
 * Created on 6.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.care.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;


import com.idega.block.process.data.CaseStatus;
import com.idega.block.school.data.School;
import com.idega.data.IDOException;
import com.idega.data.IDOHome;
import com.idega.user.data.User;

/**
 * 
 *  Last modified: $Date: 2004/10/14 11:42:34 $ by $Author: thomas $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public interface ChildCareApplicationHome extends IDOHome {
    public ChildCareApplication create() throws javax.ejb.CreateException;

    public ChildCareApplication findByPrimaryKey(Object pk)
            throws javax.ejb.FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindAll
     */
    public Collection findAll() throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindAllCasesByProviderAndStatus
     */
    public Collection findAllCasesByProviderAndStatus(int providerId,
            CaseStatus caseStatus) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindAllCasesByProviderAndStatus
     */
    public Collection findAllCasesByProviderAndStatus(School provider,
            String caseStatus) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindAllCasesByProviderAndStatus
     */
    public Collection findAllCasesByProviderAndStatus(School provider,
            CaseStatus caseStatus) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindAllCasesByProviderStatus
     */
    public Collection findAllCasesByProviderStatus(int providerId,
            String caseStatus) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindAllChildCasesByProvider
     */
    public Collection findAllChildCasesByProvider(int providerId)
            throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindAllCasesByProviderAndStatus
     */
    public Collection findAllCasesByProviderAndStatus(int providerId,
            String caseStatus, int numberOfEntries, int startingEntry)
            throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindAllCasesByProviderAndNotInStatus
     */
    public Collection findAllCasesByProviderAndNotInStatus(int providerId,
            String[] caseStatus, int numberOfEntries, int startingEntry)
            throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindAllCasesByProviderAndNotInStatus
     */
    public Collection findAllCasesByProviderAndNotInStatus(int providerId,
            int sortBy, Date fromDate, Date toDate, String[] caseStatus,
            int numberOfEntries, int startingEntry) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindAllCasesByProviderStatus
     */
    public Collection findAllCasesByProviderStatus(int providerId,
            String[] caseStatus) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindAllByAreaAndApplicationStatus
     */
    public Collection findAllByAreaAndApplicationStatus(Object areaID,
            String[] applicationStatus, String caseCode, Date queueDate,
            Date placementDate, boolean firstHandOnly) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindAllCasesByProviderStatusNotRejected
     */
    public Collection findAllCasesByProviderStatusNotRejected(int providerId,
            String caseStatus) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindAllCasesByUserAndStatus
     */
    public Collection findAllCasesByUserAndStatus(User owner, String caseStatus)
            throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindAllCasesByStatus
     */
    public Collection findAllCasesByStatus(String caseStatus)
            throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindApplicationsByProviderAndStatus
     */
    public Collection findApplicationsByProviderAndStatus(int providerID,
            String caseStatus) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindApplicationsByProviderAndStatus
     */
    public Collection findApplicationsByProviderAndStatus(int providerID,
            String[] caseStatus) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindApplicationsByProviderAndStatus
     */
    public Collection findApplicationsByProviderAndStatus(int providerID,
            String[] caseStatus, String caseCode) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindApplicationsByProviderAndStatus
     */
    public Collection findApplicationsByProviderAndStatus(int providerID,
            String caseStatus, int numberOfEntries, int startingEntry)
            throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindApplicationsByProviderAndStatus
     */
    public Collection findApplicationsByProviderAndStatus(int providerID,
            String[] caseStatus, int numberOfEntries, int startingEntry)
            throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindApplicationsByProviderAndStatus
     */
    public Collection findApplicationsByProviderAndStatus(int providerID,
            String[] caseStatus, String caseCode, int numberOfEntries,
            int startingEntry) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindApplicationsByProviderAndStatus
     */
    public Collection findApplicationsByProviderAndStatus(Integer providerID,
            String[] applicationStatus, Date fromDateOfBirth,
            Date toDateOfBirth, Date fromDate, Date toDate)
            throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindApplicationsByProviderAndApplicationStatus
     */
    public Collection findApplicationsByProviderAndApplicationStatus(
            int providerID, String[] applicationStatuses)
            throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindApplicationsByProviderAndApplicationStatus
     */
    public Collection findApplicationsByProviderAndApplicationStatus(
            int providerID, String[] applicationStatuses, String caseCode)
            throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindApplicationsByProviderAndApplicationStatus
     */
    public Collection findApplicationsByProviderAndApplicationStatus(
            int providerID, String[] applicationStatuses, String caseCode,
            int numberOfEntries, int startingEntry) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindApplicationByChildAndApplicationStatus
     */
    public ChildCareApplication findApplicationByChildAndApplicationStatus(
            int childID, String[] applicationStatuses) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindApplicationsByChildAndApplicationStatus
     */
    public Collection findApplicationsByChildAndApplicationStatus(int childID,
            String[] applicationStatuses) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindApplicationsWithoutPlacing
     */
    public Collection findApplicationsWithoutPlacing() throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindApplicationByChildAndChoiceNumber
     */
    public ChildCareApplication findApplicationByChildAndChoiceNumber(
            User child, int choiceNumber) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindApplicationByChildAndChoiceNumber
     */
    public ChildCareApplication findApplicationByChildAndChoiceNumber(
            int childID, int choiceNumber) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindApplicationByChildAndChoiceNumberWithStatus
     */
    public ChildCareApplication findApplicationByChildAndChoiceNumberWithStatus(
            int childID, int choiceNumber, String caseStatus)
            throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindApplicationByChildAndChoiceNumberInStatus
     */
    public ChildCareApplication findApplicationByChildAndChoiceNumberInStatus(
            int childID, int choiceNumber, String[] caseStatus)
            throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindApplicationByChildAndChoiceNumberNotInStatus
     */
    public ChildCareApplication findApplicationByChildAndChoiceNumberNotInStatus(
            int childID, int choiceNumber, String[] caseStatus)
            throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindApplicationByChild
     */
    public Collection findApplicationByChild(int childID)
            throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindApplicationByChildAndProvider
     */
    public ChildCareApplication findApplicationByChildAndProvider(int childID,
            int providerID) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindApplicationByChildAndProviderAndStatus
     */
    public ChildCareApplication findApplicationByChildAndProviderAndStatus(
            int childID, int providerID, String[] status)
            throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindNewestApplication
     */
    public ChildCareApplication findNewestApplication(int providerID, Date date)
            throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindOldestApplication
     */
    public ChildCareApplication findOldestApplication(int providerID, Date date)
            throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindApplicationByChildAndNotInStatus
     */
    public Collection findApplicationByChildAndNotInStatus(int childID,
            String[] caseStatus) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindApplicationByChildAndNotInStatus
     */
    public Collection findApplicationByChildAndNotInStatus(int childID,
            String[] caseStatus, String caseCode) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindApplicationByChildAndInStatus
     */
    public Collection findApplicationByChildAndInStatus(int childID,
            String[] caseStatus, String caseCode) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindActiveApplicationByChild
     */
    public ChildCareApplication findActiveApplicationByChild(int childID)
            throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindActiveApplicationByChildAndStatus
     */
    public ChildCareApplication findActiveApplicationByChildAndStatus(
            int childID, String[] caseStatus) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetNumberOfActiveApplications
     */
    public int getNumberOfActiveApplications(int childID) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetNumberOfActiveApplications
     */
    public int getNumberOfActiveApplications(int childID, String caseCode)
            throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetNumberOfApplicationsByStatusAndActiveDate
     */
    public int getNumberOfApplicationsByStatusAndActiveDate(int childID,
            String[] caseStatus, String caseCode, Date activeDate)
            throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindApplicationsByProviderAndDate
     */
    public Collection findApplicationsByProviderAndDate(int providerID,
            Date date) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindApplicationsBeforeLastReplyDate
     */
    public Collection findApplicationsBeforeLastReplyDate(Date date,
            String[] caseStatus) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindApplicationsByProviderAndBeforeDate
     */
    public Collection findApplicationsByProviderAndBeforeDate(int providerID,
            Date date, String[] caseStatus) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetNumberOfApplications
     */
    public int getNumberOfApplications(int providerID, String caseStatus)
            throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetNumberOfApplications
     */
    public int getNumberOfApplications(int providerID, String[] caseStatus)
            throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetNumberOfApplicationsByStatus
     */
    public int getNumberOfApplicationsByStatus(int providerID,
            String[] caseStatus) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetNumberOfApplicationsForChild
     */
    public int getNumberOfApplicationsForChild(int childID) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetNumberOfApplicationsForChild
     */
    public int getNumberOfApplicationsForChild(int childID, String caseStatus,
            String caseCode) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetNumberOfApplicationsForChildNotInStatus
     */
    public int getNumberOfApplicationsForChildNotInStatus(int childID,
            String[] caseStatus) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetNumberOfApplicationsForChildNotInStatus
     */
    public int getNumberOfApplicationsForChildNotInStatus(int childID,
            String[] caseStatus, String caseCode) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetNumberOfApplicationsForChildInStatus
     */
    public int getNumberOfApplicationsForChildInStatus(int childID,
            String[] caseStatus, String caseCode) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetNumberOfPlacedApplications
     */
    public int getNumberOfPlacedApplications(int childID, int providerID,
            String[] caseStatus) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetNumberOfApplications
     */
    public int getNumberOfApplications(int providerID, String[] caseStatus,
            int sortBy, Date fromDate, Date toDate) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetPositionInQueue
     */
    public int getPositionInQueue(Date queueDate, int providerID,
            String[] caseStatus) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetPositionInQueue
     */
    public int getPositionInQueue(Date queueDate, int providerID,
            String applicationStatus) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetPositionInQueueByDate
     */
    public int getPositionInQueueByDate(int queueOrder, Date queueDate,
            int providerID, String[] caseStatus) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetPositionInQueueByDate
     */
    public int getPositionInQueueByDate(int queueOrder, Date queueDate,
            int providerID, String applicationStatus) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetQueueSizeNotInStatus
     */
    public int getQueueSizeNotInStatus(int providerID, String[] caseStatus)
            throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetQueueSizeInStatus
     */
    public int getQueueSizeInStatus(int providerID, String[] applicationStatus,
            Date from, Date to, boolean isOnlyFirstHand) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetBruttoQueueSizeInStatus
     */
    public int getBruttoQueueSizeInStatus(int providerID,
            String[] applicationStatus, Date from, Date to,
            boolean isOnlyFirstHand) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetNettoQueueSizeInStatus
     */
    public int getNettoQueueSizeInStatus(int providerID,
            String[] applicationStatus, Date from, Date to,
            boolean isOnlyFirstHand) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetQueueSizeInStatus
     */
    public int getQueueSizeInStatus(int providerID, String caseStatus)
            throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetQueueSizeInStatus
     */
    public int getQueueSizeInStatus(int providerID, String applicationStatus,
            Date from, Date to, boolean isOnlyFirstHand) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetBruttoQueueSizeInStatus
     */
    public int getBruttoQueueSizeInStatus(int providerID,
            String applicationStatus, Date from, Date to,
            boolean isOnlyFirstHand) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetNettoQueueSizeInStatus
     */
    public int getNettoQueueSizeInStatus(int providerID,
            String applicationStatus, Date from, Date to,
            boolean isOnlyFirstHand) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetQueueSizeByAreaNotInStatus
     */
    public int getQueueSizeByAreaNotInStatus(int areaID, String[] caseStatus)
            throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetQueueSizeByAreaInStatus
     */
    public int getQueueSizeByAreaInStatus(int areaID, String caseStatus)
            throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetNumberOfApplicationsByProviderAndChoiceNumber
     */
    public int getNumberOfApplicationsByProviderAndChoiceNumber(int providerID,
            int choiceNumber) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetQueueByProviderAndChoiceNumber
     */
    public int getQueueByProviderAndChoiceNumber(int providerID,
            int choiceNumber, String status, Date from, Date to)
            throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetNettoQueueByProviderAndChoiceNumber
     */
    public int getNettoQueueByProviderAndChoiceNumber(int providerID,
            int choiceNumber, String status, Date from, Date to)
            throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbHomeGetBruttoQueueByProviderAndChoiceNumber
     */
    public int getBruttoQueueByProviderAndChoiceNumber(int providerID,
            int choiceNumber, String status, Date from, Date to)
            throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean#ejbFindApplicationsInSchoolAreaByStatus
     */
    public Collection findApplicationsInSchoolAreaByStatus(int schoolAreaID,
            String[] statuses, int choiceNumber) throws FinderException;

}
