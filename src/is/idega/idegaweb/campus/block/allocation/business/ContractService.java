/*
 * Created on 1.9.2004
 *
 * Copyright (C) 2004 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 */
package is.idega.idegaweb.campus.block.allocation.business;

import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.ContractHome;
import is.idega.idegaweb.campus.block.allocation.data.ContractTextHome;
import is.idega.idegaweb.campus.block.application.business.ApplicationService;
import is.idega.idegaweb.campus.block.application.data.WaitingListHome;
import is.idega.idegaweb.campus.block.building.data.ApartmentTypePeriods;
import is.idega.idegaweb.campus.block.mailinglist.business.MailingListService;
import is.idega.idegaweb.campus.business.CampusGroupException;
import is.idega.idegaweb.campus.business.CampusUserService;
import is.idega.idegaweb.campus.data.ContractAccountApartmentHome;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.application.data.Applicant;
import com.idega.block.building.business.BuildingService;
import com.idega.block.building.data.Apartment;
import com.idega.business.IBOService;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author aron
 *
 * ContractService TODO Describe this type
 */
public interface ContractService extends IBOService {
    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#signContract
     */
    public String signContract(Integer contractID, Integer groupID,
            Integer cashierID, Integer financeCategoryID, String sEmail,
            boolean sendMail, boolean newAccount, boolean newPhoneAccount,
            boolean newLogin, boolean generatePasswd, IWResourceBundle iwrb,
            String login, String passwd) throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#createUserLogin
     */
    public void createUserLogin(User user, Integer groupID, String login,
            String pass, boolean generatePasswd) throws Exception,
            java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#addUserToTenantGroup
     */
    public void addUserToTenantGroup(Integer groupID, User user)
            throws FinderException, RemoteException, java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#changeApplicationStatus
     */
    public void changeApplicationStatus(Contract eContract) throws Exception,
            java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#deleteFromWaitingList
     */
    public void deleteFromWaitingList(Contract eContract)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#deleteFromWaitingList
     */
    public void deleteFromWaitingList(Applicant applicant)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#endContract
     */
    public void endContract(Integer contractID, IWTimestamp movingDate,
            String info, boolean datesync) throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#endContract
     */
    public void endContract(Contract C, IWTimestamp movingDate, String info,
            boolean datesync) throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#endExpiredContracts
     */
    public void endExpiredContracts() throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#garbageEndedContracts
     */
    public void garbageEndedContracts(java.sql.Date lastChangeDate)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#garbageResignedContracts
     */
    public void garbageResignedContracts(java.sql.Date lastChangeDate)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#finalizeGarbageContracts
     */
    public void finalizeGarbageContracts(java.sql.Date lastChangeDate)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#returnKey
     */
    public void returnKey(Integer contractID, User currentUser)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#deliverKey
     */
    public void deliverKey(Integer contractID, Timestamp when)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#deliverKey
     */
    public void deliverKey(Integer contractID) throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#resignContract
     */
    public void resignContract(Integer contractID, IWTimestamp movingDate,
            String info, boolean datesync) throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#createNewContract
     */
    public Contract createNewContract(Integer userID, Integer applicantID,
            Integer apartmentID, Date from, Date to) throws RemoteException,
            CreateException, java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#createUserFamily
     */
    public User createUserFamily(Applicant applicant, String[] emails)
            throws RemoteException, CreateException, CampusGroupException,
            java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#createNewUser
     */
    public User createNewUser(Applicant A, String[] emails)
            throws RemoteException, CreateException, java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#deleteAllocation
     */
    public boolean deleteAllocation(Integer contractID, User currentUser)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getContractStampsFromPeriod
     */
    public IWTimestamp[] getContractStampsFromPeriod(ApartmentTypePeriods ATP,
            int monthOverlap) throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getContractStampsForApartment
     */
    public IWTimestamp[] getContractStampsForApartment(Integer apartmentID)
            throws FinderException, RemoteException, java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getContractStampsForApartment
     */
    public IWTimestamp[] getContractStampsForApartment(Apartment apartment)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getContractStampsFromPeriod
     */
    public IWTimestamp[] getContractStampsFromPeriod(ApartmentTypePeriods ATP,
            Integer monthOverlap) throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getLocalizedStatus
     */
    public String getLocalizedStatus(com.idega.idegaweb.IWResourceBundle iwrb,
            String status) throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#doGarbageContract
     */
    public boolean doGarbageContract(Integer contractID)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getApartmentTypePeriod
     */
    public ApartmentTypePeriods getApartmentTypePeriod(Integer typeID)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#allocate
     */
    public Contract allocate(Integer contractID, Integer apartmentID,
            Integer applicantID, Date validFrom, Date validTo)
            throws AllocationException, java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getValidPeriod
     */
    public Period getValidPeriod(Contract contract, Apartment apartment,
            Integer dayBuffer, Integer monthOverlap)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getNextAvailableDate
     */
    public Date getNextAvailableDate(Apartment apartment)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#resetWaitingListRejection
     */
    public void resetWaitingListRejection(Integer waitingListID)
            throws RemoteException, FinderException, java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#reactivateWaitingList
     */
    public void reactivateWaitingList(Integer waitingListID)
            throws RemoteException, FinderException, java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#removeWaitingList
     */
    public void removeWaitingList(Integer waitingListID)
            throws RemoteException, FinderException, java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getAvailableApartmentDates
     */
    public Map getAvailableApartmentDates(Integer aprtTypeID, Integer cplxID)
            throws FinderException, java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getApplicantContractsByStatus
     */
    public Map getApplicantContractsByStatus(String status)
            throws RemoteException, FinderException, java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getRentableStatuses
     */
    public String[] getRentableStatuses() throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getAllocateableStatuses
     */
    public String[] getAllocateableStatuses() throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getAllowedTemporaryPersonalID
     */
    public Collection getAllowedTemporaryPersonalID()
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getNewApplicantContracts
     */
    public Map getNewApplicantContracts() throws RemoteException,
            FinderException, java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getUserService
     */
    public CampusUserService getUserService() throws RemoteException,
            java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getContractHome
     */
    public ContractHome getContractHome() throws RemoteException,
            java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getContractTextHome
     */
    public ContractTextHome getContractTextHome() throws RemoteException,
            java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getWaitingListHome
     */
    public WaitingListHome getWaitingListHome() throws RemoteException,
            java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getContractAccountApartmentHome
     */
    public ContractAccountApartmentHome getContractAccountApartmentHome()
            throws RemoteException, java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getMailingListService
     */
    public MailingListService getMailingListService() throws RemoteException,
            java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getApplicationService
     */
    public ApplicationService getApplicationService() throws RemoteException,
            java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getBuildingService
     */
    public BuildingService getBuildingService() throws RemoteException,
            java.rmi.RemoteException;

}
