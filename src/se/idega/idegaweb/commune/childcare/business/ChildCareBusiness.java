/*
 * $Id: ChildCareBusiness.java 1.1 5.10.2004 aron Exp $
 * Created on 5.10.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.business;


import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.childcare.data.ChildCareContract;
import se.idega.idegaweb.commune.accounting.childcare.data.ChildCareContractHome;
import se.idega.idegaweb.commune.block.importer.business.AlreadyCreatedException;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.childcare.check.business.CheckBusiness;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.data.ChildCarePrognosis;
import se.idega.idegaweb.commune.childcare.data.ChildCarePrognosisHome;
import se.idega.idegaweb.commune.childcare.data.ChildCareQueue;
import se.idega.idegaweb.commune.childcare.data.ChildCareQueueHome;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;

import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.data.Case;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.business.IBOService;
import com.idega.core.file.data.ICFile;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.exception.IWBundleDoesNotExist;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWUserContext;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * 
 *  Last modified: $Date: 5.10.2004 10:21:52 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public interface ChildCareBusiness extends IBOService, CaseBusiness {
    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getBundleIdentifier
     */
    public String getBundleIdentifier() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getChildCarePrognosisHome
     */
    public ChildCarePrognosisHome getChildCarePrognosisHome()
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getChildCareContractArchiveHome
     */
    public ChildCareContractHome getChildCareContractArchiveHome()
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getChildCareQueueHome
     */
    public ChildCareQueueHome getChildCareQueueHome()
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getPrognosis
     */
    public ChildCarePrognosis getPrognosis(int providerID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getApplication
     */
    public ChildCareApplication getApplication(int childID, int choiceNumber)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNonActiveApplication
     */
    public ChildCareApplication getNonActiveApplication(int childID,
            int choiceNumber) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNewestApplication
     */
    public ChildCareApplication getNewestApplication(int providerID, Date date)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasApplications
     */
    public boolean hasApplications(int childID) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#updatePrognosis
     */
    public void updatePrognosis(int providerID, int threeMonthsPrognosis,
            int oneYearPrognosis, int threeMonthsPriority, int oneYearPriority,
            int providerCapacity) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#setChildCareQueueExported
     */
    public void setChildCareQueueExported(ChildCareQueue queue)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#exportQueue
     */
    public void exportQueue(Collection choices) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getHasUnexportedChoices
     */
    public boolean getHasUnexportedChoices(int childID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#insertApplications
     */
    public boolean insertApplications(User user, int[] provider, String date,
            int checkId, int childId, String subject, String message,
            boolean freetimeApplication) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#insertApplications
     */
    public boolean insertApplications(User user, int[] provider,
            String[] dates, String message, int checkId, int childId,
            String subject, String body, boolean freetimeApplication)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#insertApplications
     */
    public boolean insertApplications(User user, int[] provider,
            String[] dates, String message, int childID, Date[] queueDates,
            boolean[] hasPriority) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#insertApplications
     */
    public boolean insertApplications(User user, int[] provider,
            String[] dates, String message, int checkId, int childId,
            String subject, String body, boolean freetimeApplication,
            boolean sendMessages, Date[] queueDates, boolean[] hasPriority)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#changePlacingDate
     */
    public void changePlacingDate(int applicationID, Date placingDate,
            String preSchool) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#sendMessageToProvider
     */
    public void sendMessageToProvider(ChildCareApplication application,
            String subject, String message, User sender)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#sendMessageToProvider
     */
    public void sendMessageToProvider(ChildCareApplication application,
            String subject, String message) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#sendMessageToParents
     */
    public void sendMessageToParents(ChildCareApplication application,
            String subject, String body) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getQueueChoices
     */
    public Collection getQueueChoices(int childID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getPositionInQueue
     */
    public int getPositionInQueue(ChildCareQueue queue)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getApplicationsByProviderAndApplicationStatus
     */
    public Collection getApplicationsByProviderAndApplicationStatus(
            int providerID, String applicationStatus)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getInactiveApplicationsByProvider
     */
    public Collection getInactiveApplicationsByProvider(int providerID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getUnhandledApplicationsByProvider
     */
    public Collection getUnhandledApplicationsByProvider(int providerId)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberOfUnhandledApplicationsByProvider
     */
    public int getNumberOfUnhandledApplicationsByProvider(int providerID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberOfApplicationsByProvider
     */
    public int getNumberOfApplicationsByProvider(int providerID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberOfApplicationsByProvider
     */
    public int getNumberOfApplicationsByProvider(int providerID, int sortBy,
            Date fromDate, Date toDate) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberOfFirstHandChoicesByProvider
     */
    public int getNumberOfFirstHandChoicesByProvider(int providerID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberOfFirstHandChoicesByProvider
     */
    public int getNumberOfFirstHandChoicesByProvider(int providerID, Date from,
            Date to) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberOfFirstHandNettoChoicesByProvider
     */
    public int getNumberOfFirstHandNettoChoicesByProvider(int providerID,
            Date from, Date to) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberOfFirstHandBruttoChoicesByProvider
     */
    public int getNumberOfFirstHandBruttoChoicesByProvider(int providerID,
            Date from, Date to) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getPendingApplications
     */
    public Collection getPendingApplications(int childID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getPendingApplications
     */
    public Collection getPendingApplications(int childID, String caseCode)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasPendingApplications
     */
    public boolean hasPendingApplications(int childID, String caseCode)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#cleanQueue
     */
    public boolean cleanQueue(int providerID, User performer, IWUserContext iwuc)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#cleanQueueInThread
     */
    public boolean cleanQueueInThread(int providerID, User performer,
            IWUserContext iwuc) throws FinderException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasActiveApplications
     */
    public boolean hasActiveApplications(int childID, String caseCode,
            Date activeDate) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#removePendingFromQueue
     */
    public boolean removePendingFromQueue(User performer)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#renewApplication
     */
    public void renewApplication(int applicationID, User performer)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberInQueue
     */
    public int getNumberInQueue(ChildCareApplication application)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberInQueueByStatus
     */
    public int getNumberInQueueByStatus(ChildCareApplication application)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getUnhandledApplicationsByProvider
     */
    public Collection getUnhandledApplicationsByProvider(int providerId,
            int numberOfEntries, int startingEntry)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getUnhandledApplicationsByProvider
     */
    public Collection getUnhandledApplicationsByProvider(int providerId,
            int numberOfEntries, int startingEntry, int sortBy, Date fromDate,
            Date toDate) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getUnhandledApplicationsByChild
     */
    public Collection getUnhandledApplicationsByChild(int childID,
            String caseCode) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getUnhandledApplicationsByChild
     */
    public Collection getUnhandledApplicationsByChild(int childID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getUnhandledApplicationsByChildAndProvider
     */
    public ChildCareApplication getUnhandledApplicationsByChildAndProvider(
            int childID, int providerID) throws FinderException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getUnhandledApplicationsByProvider
     */
    public Collection getUnhandledApplicationsByProvider(School provider)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getUnhandledApplicationsByProvider
     */
    public Collection getUnhandledApplicationsByProvider(User provider)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getUnsignedApplicationsByProvider
     */
    public Collection getUnsignedApplicationsByProvider(int providerId)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getUnsignedApplicationsByProvider
     */
    public Collection getUnsignedApplicationsByProvider(School provider)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getUnsignedApplicationsByProvider
     */
    public Collection getUnsignedApplicationsByProvider(User provider)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#isAfterSchoolApplication
     */
    public boolean isAfterSchoolApplication(Case application)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#isAfterSchoolApplication
     */
    public boolean isAfterSchoolApplication(int applicationID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#rejectApplication
     */
    public boolean rejectApplication(ChildCareApplication application,
            String subject, String message, User user)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#rejectApplication
     */
    public boolean rejectApplication(int applicationId, String subject,
            String body, User user) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#placeApplication
     */
    public boolean placeApplication(int applicationID, String subject,
            String body, int childCareTime, int groupID, int schoolTypeID,
            int employmentTypeID, User user, Locale locale)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#alterValidFromDate
     */
    public void alterValidFromDate(int applicationID, Date newDate,
            int employmentTypeID, Locale locale, User user)
            throws RemoteException, NoPlacementFoundException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#alterValidFromDate
     */
    public void alterValidFromDate(ChildCareApplication application,
            Date newDate, int employmentTypeID, Locale locale, User user)
            throws RemoteException, NoPlacementFoundException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#alterContract
     */
    public boolean alterContract(int childcareContractID, int careTime,
            Date fromDate, Date endDate, Locale locale, User performer,
            int employmentType, int invoiceReceiver, int schoolType,
            int schoolClass) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#alterContract
     */
    public boolean alterContract(ChildCareContract childcareContract,
            int careTime, Date fromDate, Date endDate, Locale locale,
            User performer, int employmentType, int invoiceReceiver,
            int schoolType, int schoolClass) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#recreateContractFile
     */
    public ICFile recreateContractFile(ChildCareContract archive, Locale locale)
            throws IDORemoveRelationshipException, RemoteException,
            IWBundleDoesNotExist, IDOAddRelationshipException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#moveToGroup
     */
    public void moveToGroup(int childID, int providerID, int schoolClassID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#moveToGroup
     */
    public void moveToGroup(int placementID, int schoolClassID)
            throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#removeFromProvider
     */
    public void removeFromProvider(int placementID, Timestamp date,
            boolean parentalLeave, String message)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#acceptApplication
     */
    public boolean acceptApplication(ChildCareApplication application,
            IWTimestamp validUntil, String subject, String message, User user)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#retractOffer
     */
    public boolean retractOffer(int applicationID, String subject,
            String message, User user) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#retractOffer
     */
    public boolean retractOffer(ChildCareApplication application,
            String subject, String message, User user)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#reactivateApplication
     */
    public boolean reactivateApplication(int applicationID, User user)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#reactivateApplication
     */
    public boolean reactivateApplication(ChildCareApplication application,
            User user) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#changeApplicationStatus
     */
    public boolean changeApplicationStatus(int applicationID, char newStatus,
            User performer) throws IllegalArgumentException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#changeApplicationStatus
     */
    public boolean changeApplicationStatus(ChildCareApplication application,
            char newStatus, User performer) throws IllegalArgumentException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#cancelContract
     */
    public boolean cancelContract(ChildCareApplication application,
            boolean parentalLeave, IWTimestamp date, String message,
            String subject, String body, User user)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#acceptApplication
     */
    public boolean acceptApplication(int applicationId, IWTimestamp validUntil,
            String subject, String message, User user)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#rejectOffer
     */
    public boolean rejectOffer(int applicationId, User user)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#rejectOfferWithNewDate
     */
    public boolean rejectOfferWithNewDate(int applicationId, User user,
            Date date) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#removeFromQueue
     */
    public boolean removeFromQueue(int applicationId, User user)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#removeFromQueue
     */
    public boolean removeFromQueue(ChildCareApplication application, User user)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getAcceptedApplicationsByChild
     */
    public ChildCareApplication getAcceptedApplicationsByChild(int childID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getApplicationsByProvider
     */
    public Collection getApplicationsByProvider(int providerId)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getOpenAndGrantedApplicationsByProvider
     */
    public Collection getOpenAndGrantedApplicationsByProvider(int providerId)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getAcceptedApplicationsByProvider
     */
    public Collection getAcceptedApplicationsByProvider(int providerID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getApplicationsByProvider
     */
    public Collection getApplicationsByProvider(School provider)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getApplicationsByProvider
     */
    public Collection getApplicationsByProvider(User provider)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getRejectedApplicationsByProvider
     */
    public Collection getRejectedApplicationsByProvider(Integer providerID,
            String fromDateOfBirth, String toDateOfBirth, String fromDate,
            String toDate) throws FinderException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#parentsAgree
     */
    public void parentsAgree(int applicationID, User user, String subject,
            String message) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#parentsAgree
     */
    public void parentsAgree(ChildCareApplication application, User user,
            String subject, String message) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#saveComments
     */
    public void saveComments(int applicationID, String comment)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#createNewPlacement
     */
    public SchoolClassMember createNewPlacement(int applicationID,
            int schooltypeID, int schoolclassID, SchoolClassMember oldStudent,
            IWTimestamp validFrom, User user) throws RemoteException,
            EJBException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#createNewPlacement
     */
    public SchoolClassMember createNewPlacement(
            ChildCareApplication application, int schooltypeID,
            int schoolclassID, SchoolClassMember oldStudent,
            IWTimestamp validFrom, User user) throws RemoteException,
            EJBException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#createNewPlacement
     */
    public SchoolClassMember createNewPlacement(Integer childID,
            Integer schooltypeID, Integer schoolclassID,
            SchoolClassMember oldStudent, IWTimestamp validFrom, User user)
            throws RemoteException, EJBException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#assignContractToApplication
     */
    public boolean assignContractToApplication(int applicationID,
            int oldArchiveID, int childCareTime, IWTimestamp validFrom,
            int employmentTypeID, User user, Locale locale, boolean changeStatus)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#assignContractToApplication
     */
    public boolean assignContractToApplication(int applicationID,
            int archiveID, int childCareTime, IWTimestamp validFrom,
            int employmentTypeID, User user, Locale locale,
            boolean changeStatus, boolean createNewStudent, int schoolTypeId,
            int schoolClassId) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#assignContractToApplication
     */
    public boolean assignContractToApplication(String[] ids, User user,
            Locale locale) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#assignApplication
     */
    public boolean assignApplication(int id, User user, String subject,
            String body) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#assignApplication
     */
    public boolean assignApplication(String[] ids, User user, String subject,
            String body) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getGrantedApplicationsByUser
     */
    public Collection getGrantedApplicationsByUser(User owner)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getApplicationsByUser
     */
    public Collection getApplicationsByUser(User owner)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getApplicationsForChild
     */
    public Collection getApplicationsForChild(User child)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getApplicationsForChild
     */
    public Collection getApplicationsForChild(User child, String caseCode)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getApplicationsForChild
     */
    public Collection getApplicationsForChild(int childId)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getApplicationsForChild
     */
    public Collection getApplicationsForChild(int childId, String caseCode)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberOfApplicationsForChildByStatus
     */
    public int getNumberOfApplicationsForChildByStatus(int childID,
            String caseStatus) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberOfApplicationsForChildByStatus
     */
    public int getNumberOfApplicationsForChildByStatus(int childID,
            String caseStatus, String caseCode) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberOfApplicationsForChild
     */
    public int getNumberOfApplicationsForChild(int childID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberOfApplicationsForChildNotInactive
     */
    public int getNumberOfApplicationsForChildNotInactive(int childID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberOfApplicationsForChildNotInactive
     */
    public int getNumberOfApplicationsForChildNotInactive(int childID,
            String caseCode) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasOutstandingOffers
     */
    public boolean hasOutstandingOffers(int childID, String caseCode)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getApplicationForChildAndProvider
     */
    public ChildCareApplication getApplicationForChildAndProvider(int childID,
            int providerID) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#findAllGrantedApplications
     */
    public Collection findAllGrantedApplications()
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#findAllApplicationsWithChecksToRedeem
     */
    public Collection findAllApplicationsWithChecksToRedeem()
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getApplicationByPrimaryKey
     */
    public ChildCareApplication getApplicationByPrimaryKey(String key)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getApplication
     */
    public ChildCareApplication getApplication(int applicationID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#redeemApplication
     */
    public boolean redeemApplication(String applicationId, User performer)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getPreSchoolTypes
     */
    public Collection getPreSchoolTypes() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getFamilyDayCareTypes
     */
    public Collection getFamilyDayCareTypes() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getFamilyAfterSchoolTypes
     */
    public Collection getFamilyAfterSchoolTypes()
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getXMLContractTxtURL
     */
    public String getXMLContractTxtURL(IWBundle iwb, Locale locale)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getXMLContractPdfURL
     */
    public String getXMLContractPdfURL(IWBundle iwb, Locale locale)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#setAsPriorityApplication
     */
    public void setAsPriorityApplication(int applicationID, String message,
            String body) throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#setAsPriorityApplication
     */
    public void setAsPriorityApplication(ChildCareApplication application,
            String message, String body) throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasBeenPlacedWithOtherProvider
     */
    public boolean hasBeenPlacedWithOtherProvider(int childID, int providerID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getContractFile
     */
    public ChildCareContract getContractFile(int contractFileID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getContractsByChild
     */
    public Collection getContractsByChild(int childID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getContractsByChildAndProvider
     */
    public Collection getContractsByChildAndProvider(int childID, int providerID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getContractsByApplication
     */
    public Collection getContractsByApplication(int applicationID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getLocalizedCaseDescription
     */
    public String getLocalizedCaseDescription(Case theCase, Locale locale)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getMessageBusiness
     */
    public MessageBusiness getMessageBusiness() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getUserBusiness
     */
    public CommuneUserBusiness getUserBusiness()
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getSchoolBusiness
     */
    public SchoolBusiness getSchoolBusiness() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getCheckBusiness
     */
    public CheckBusiness getCheckBusiness() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getSchoolChoiceBusiness
     */
    public SchoolChoiceBusiness getSchoolChoiceBusiness()
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getAfterSchoolBusiness
     */
    public AfterSchoolBusiness getAfterSchoolBusiness()
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusAccepted
     */
    public char getStatusAccepted() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusContract
     */
    public char getStatusContract() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusParentsAccept
     */
    public char getStatusParentsAccept() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusPriority
     */
    public char getStatusPriority() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusReady
     */
    public char getStatusReady() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusSentIn
     */
    public char getStatusSentIn() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusTimedOut
     */
    public char getStatusTimedOut() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusCancelled
     */
    public char getStatusCancelled() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusDenied
     */
    public char getStatusDenied() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusDeleted
     */
    public char getStatusDeleted() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusMoved
     */
    public char getStatusMoved() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusRejected
     */
    public char getStatusRejected() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusNotAnswered
     */
    public char getStatusNotAnswered() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusNewChoice
     */
    public char getStatusNewChoice() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getQueueTotalByProvider
     */
    public int getQueueTotalByProvider(int providerID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getQueueByProvider
     */
    public int getQueueByProvider(int providerID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getQueueTotalByProvider
     */
    public int getQueueTotalByProvider(int providerID, Date from, Date to,
            boolean isOnlyFirstHand) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getQueueByProvider
     */
    public int getQueueByProvider(int providerID, Date from, Date to,
            boolean isOnlyFirstHand) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getQueueTotalByProviderWithinMonths
     */
    public int getQueueTotalByProviderWithinMonths(int providerID, int months,
            boolean isOnlyFirstHand) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getBruttoQueueTotalByProvider
     */
    public int getBruttoQueueTotalByProvider(int providerID, Date from,
            Date to, boolean isOnlyFirstHand) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getBruttoQueueByProvider
     */
    public int getBruttoQueueByProvider(int providerID, Date from, Date to,
            boolean isOnlyFirstHand) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getBruttoQueueTotalByProviderWithinMonths
     */
    public int getBruttoQueueTotalByProviderWithinMonths(int providerID,
            int months, boolean isOnlyFirstHand)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNettoQueueTotalByProvider
     */
    public int getNettoQueueTotalByProvider(int providerID, Date from, Date to,
            boolean isOnlyFirstHand) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNettoQueueByProvider
     */
    public int getNettoQueueByProvider(int providerID, Date from, Date to,
            boolean isOnlyFirstHand) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNettoQueueTotalByProviderWithinMonths
     */
    public int getNettoQueueTotalByProviderWithinMonths(int providerID,
            int months, boolean isOnlyFirstHand)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getQueueTotalByArea
     */
    public int getQueueTotalByArea(int areaID) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getQueueByArea
     */
    public int getQueueByArea(int areaID) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getOldQueueTotal
     */
    public int getOldQueueTotal(String[] queueType, boolean exported)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getProviderAreaMap
     */
    public Map getProviderAreaMap(Collection schoolAreas, Locale locale,
            String emptyString, boolean isFreetime)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getProviderAreaMap
     */
    public Map getProviderAreaMap(Collection schoolAreas, School currentSchool,
            Locale locale, String emptyString, boolean isFreetime)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getValidContract
     */
    public ChildCareContract getValidContract(int applicationID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getValidContract
     */
    public ChildCareContract getValidContract(int applicationID, Date validDate)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getActiveApplicationByChild
     */
    public ChildCareApplication getActiveApplicationByChild(int childID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasActiveApplication
     */
    public boolean hasActiveApplication(int childID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasActiveApplication
     */
    public boolean hasActiveApplication(int childID, String caseCode)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getValidContractByChild
     */
    public ChildCareContract getValidContractByChild(int childID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getValidContractByChild
     */
    public ChildCareContract getValidContractByChild(int childID, Date validDate)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#removeFutureContracts
     */
    public void removeFutureContracts(int applicationID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#removeFutureContracts
     */
    public void removeFutureContracts(int applicationID, Date date)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#removeContract
     */
    public boolean removeContract(int childcareContractID, User performer)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#removeContract
     */
    public boolean removeContract(ChildCareContract childcareContract,
            User performer) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#addMissingGrantedChecks
     */
    public void addMissingGrantedChecks() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#convertOldQueue
     */
    public void convertOldQueue() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasActivePlacement
     */
    public boolean hasActivePlacement(int childID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#canCancelContract
     */
    public boolean canCancelContract(int applicationID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberOfContractsForApplication
     */
    public int getNumberOfContractsForApplication(int applicationID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasFutureContracts
     */
    public boolean hasFutureContracts(int applicationID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasActiveContract
     */
    public boolean hasActiveContract(int applicationID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberOfFutureContracts
     */
    public int getNumberOfFutureContracts(int applicationID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasUnansweredOffers
     */
    public boolean hasUnansweredOffers(int childID, String caseCode)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getActivePlacement
     */
    public ChildCareApplication getActivePlacement(int childID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasActivePlacementNotWithProvider
     */
    public boolean hasActivePlacementNotWithProvider(int childID, int providerID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasTerminationInFutureNotWithProvider
     */
    public boolean hasTerminationInFutureNotWithProvider(int childID,
            int providerID) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasTerminationInFuture
     */
    public boolean hasTerminationInFuture(int childID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getEarliestPossiblePlacementDate
     */
    public Date getEarliestPossiblePlacementDate(int childID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getLatestTerminatedContract
     */
    public ChildCareContract getLatestTerminatedContract(int childID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getCurrentProviderByPlacement
     */
    public School getCurrentProviderByPlacement(int childID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getLatestContract
     */
    public ChildCareContract getLatestContract(int childID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getCaseLogNewContracts
     */
    public Collection getCaseLogNewContracts(Timestamp fromDate,
            Timestamp toDate) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getCaseLogAlteredContracts
     */
    public Collection getCaseLogAlteredContracts(Timestamp fromDate,
            Timestamp toDate) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getCaseLogTerminatedContracts
     */
    public Collection getCaseLogTerminatedContracts(Timestamp fromDate,
            Timestamp toDate) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#importChildToProvider
     */
    public boolean importChildToProvider(int applicationID, int childID,
            int providerID, int groupID, int careTime, int employmentTypeID,
            int schoolTypeID, String comment, IWTimestamp fromDate,
            IWTimestamp toDate, Locale locale, User parent, User admin)
            throws AlreadyCreatedException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#importChildToProvider
     */
    public boolean importChildToProvider(int applicationID, int childID,
            int providerID, int groupID, int careTime, int employmentTypeID,
            int schoolTypeID, String comment, IWTimestamp fromDate,
            IWTimestamp toDate, Locale locale, User parent, User admin,
            boolean canCreateMultiple) throws AlreadyCreatedException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#importChildToProvider
     */
    public boolean importChildToProvider(int applicationID, int childID,
            int providerID, int groupID, int careTime, int employmentTypeID,
            int schoolTypeID, String comment, IWTimestamp fromDate,
            IWTimestamp toDate, Locale locale, User parent, User admin,
            boolean canCreateMultiple, IWTimestamp lastReplyDate,
            String preSchool, boolean extraContract,
            String extraContractMessage, boolean extraContractOther,
            String extraContractOtherMessage) throws AlreadyCreatedException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#findAllEmploymentTypes
     */
    public Collection findAllEmploymentTypes() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#updateMissingPlacements
     */
    public void updateMissingPlacements() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#findUnhandledApplicationsNotInCommune
     */
    public Collection findUnhandledApplicationsNotInCommune()
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#findSentInAndRejectedApplicationsByArea
     */
    public Collection findSentInAndRejectedApplicationsByArea(Object area,
            int monthsInQueue, int weeksToPlacementDate, boolean firstHandOnly,
            String caseCode) throws FinderException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#findRejectedApplicationsByChild
     */
    public Collection findRejectedApplicationsByChild(int childID)
            throws FinderException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusString
     */
    public String getStatusString(char status) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusStringAbbr
     */
    public String getStatusStringAbbr(char status)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#wasRejectedByParent
     */
    public boolean wasRejectedByParent(ChildCareApplication application)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#isSchoolClassBelongingToSchooltype
     */
    public boolean isSchoolClassBelongingToSchooltype(int schoolClassId,
            int schoolTypeId) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#isTryingToChangeSchoolTypeButNotSchoolClass
     */
    public boolean isTryingToChangeSchoolTypeButNotSchoolClass(
            int currentArchiveID, int schoolTypeId, int schoolClassId)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getProviderStats
     */
    public Collection getProviderStats(Locale sortLocale)
            throws FinderException, java.rmi.RemoteException;


    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#setUserAsDeceased
     */
 	public boolean setUserAsDeceased(Integer userID, java.util.Date deceasedDate) throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getPlacementDateHelper
     */
    public PlacementHelper getPlacementHelper(
            Integer applicationID) throws java.rmi.RemoteException;


}
