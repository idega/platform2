/*
 * $Id: CitizenAccountBusiness.java,v 1.27 2004/11/02 21:20:21 aron Exp $
 * Created on 2.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.account.citizen.business;


import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.account.business.AccountBusiness;
import se.idega.idegaweb.commune.account.citizen.data.CitizenAccount;
import se.idega.idegaweb.commune.account.citizen.data.CitizenApplicantChildren;
import se.idega.idegaweb.commune.account.citizen.data.CitizenApplicantCohabitant;
import se.idega.idegaweb.commune.account.citizen.data.CitizenApplicantMovingTo;
import se.idega.idegaweb.commune.account.citizen.data.CitizenApplicantPutChildren;

import com.idega.business.IBOService;
import com.idega.core.accesscontrol.business.UserHasLoginException;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.location.data.Commune;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;

/**
 * 
 *  Last modified: $Date: 2004/11/02 21:20:21 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.27 $
 */
public interface CitizenAccountBusiness extends IBOService, AccountBusiness {
    /**
     * @see se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusinessBean#insertApplication
     */
    public Integer insertApplication(IWContext iwc, User user, String ssn,
            String email, String phoneHome, String phoneWork)
            throws UserHasLoginException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusinessBean#insertApplication
     */
    public Integer insertApplication(IWContext iwc, User user, String ssn,
            String email, String phoneHome, String phoneWork, boolean sendEmail)
            throws UserHasLoginException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusinessBean#insertApplication
     */
    public Integer insertApplication(IWContext iwc, String name, String ssn,
            String email, String phoneHome, String phoneWork, String careOf,
            String street, String zipCode, String city, String civilStatus,
            boolean hasCohabitant, int childrenCount, String applicationReason)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusinessBean#insertCohabitant
     */
    public Integer insertCohabitant(Integer applicationId, String firstName,
            String lastName, String ssn, String civilStatus, String phoneWork)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusinessBean#insertChildren
     */
    public Integer insertChildren(Integer applicationId, String firstName,
            String lastName, String ssn) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusinessBean#insertMovingTo
     */
    public Integer insertMovingTo(Integer applicationId, String address,
            String date, String housingType, String propertyType,
            String landlordName, String landlordPhone, String landlordAddress)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusinessBean#insertPutChildren
     */
    public Integer insertPutChildren(Integer applicationId,
            String currentCommuneID) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusinessBean#findCitizenApplicantPutChildren
     */
    public CitizenApplicantPutChildren findCitizenApplicantPutChildren(
            int applicationId) throws RemoteException, FinderException;

    /**
     * @see se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusinessBean#findCommuneByCommunePK
     */
    public Commune findCommuneByCommunePK(Object communePK)
            throws FinderException, IDOLookupException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusinessBean#findCitizenApplicantChildren
     */
    public CitizenApplicantChildren[] findCitizenApplicantChildren(
            int applicationId) throws RemoteException, FinderException;

    /**
     * @see se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusinessBean#findCitizenApplicantCohabitant
     */
    public CitizenApplicantCohabitant findCitizenApplicantCohabitant(
            int applicationId) throws RemoteException, FinderException;

    /**
     * @see se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusinessBean#findCitizenApplicantMovingTo
     */
    public CitizenApplicantMovingTo findCitizenApplicantMovingTo(
            int applicationId) throws RemoteException, FinderException;

    /**
     * @see se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusinessBean#getUser
     */
    public User getUser(String ssn) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusinessBean#getUser
     */
    public User getUser(String ssn, Locale locale)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusinessBean#getUserIcelandic
     */
    public User getUserIcelandic(String ssn) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusinessBean#getListOfUnapprovedApplications
     */
    public List getListOfUnapprovedApplications()
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusinessBean#getAccount
     */
    public CitizenAccount getAccount(int id) throws FinderException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusinessBean#acceptApplication
     */
    public void acceptApplication(int applicationID, User performer,
            boolean createUserMessage, boolean createPasswordMessage,
            boolean sendEmail) throws CreateException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusinessBean#removeApplication
     */
    public void removeApplication(int applicationId, User performer)
            throws RemoteException, FinderException;

    /**
     * @see se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusinessBean#rejectApplication
     */
    public void rejectApplication(int applicationID, User performer,
            String reasonDescription) throws RemoteException, CreateException,
            FinderException;

    /**
     * @see se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusinessBean#getAllAcceptedApplications
     */
    public Collection getAllAcceptedApplications()
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusinessBean#getAllPendingApplications
     */
    public Collection getAllPendingApplications()
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusinessBean#getAllRejectedApplications
     */
    public Collection getAllRejectedApplications()
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusinessBean#getAcceptMessageSubject
     */
    public String getAcceptMessageSubject() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusinessBean#getRejectMessageSubject
     */
    public String getRejectMessageSubject() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusinessBean#changePasswordAndSendLetterOrEmail
     */
    public void changePasswordAndSendLetterOrEmail(IWUserContext iwuc,
            LoginTable loginTable, User user, String newPassword,
            boolean sendLetter) throws CreateException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusinessBean#getNumberOfApplications
     */
    public int getNumberOfApplications() throws RemoteException;

}
