/*
 * $Id: CommuneUserBusiness.java,v 1.44 2004/10/14 07:33:59 laddi Exp $
 * Created on 18.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.business;

import is.idega.block.family.business.FamilyLogic;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.school.data.School;
import com.idega.business.IBOService;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Gender;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * 
 *  Last modified: $Date: 2004/10/14 07:33:59 $ by $Author: laddi $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.44 $
 */
public interface CommuneUserBusiness extends IBOService, UserBusiness {
    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#createCitizen
     */
    public User createCitizen(String firstname, String middlename,
            String lastname, String personalID) throws CreateException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#createCitizen
     */
    public User createCitizen(String firstname, String middlename,
            String lastname, String personalID, Gender gender,
            IWTimestamp dateOfBirth) throws CreateException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#createCitizen
     */
    public User createCitizen(String firstname, String middlename,
            String lastname, String personalID, Gender gender,
            IWTimestamp dateOfBirth, Group rootGroup) throws CreateException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#createOrUpdateCitizenByPersonalID
     */
    public User createOrUpdateCitizenByPersonalID(String firstName,
            String middleName, String lastName, String personalID)
            throws CreateException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#createOrUpdateCitizenByPersonalID
     */
    public User createOrUpdateCitizenByPersonalID(String firstName,
            String middleName, String lastName, String personalID,
            Gender gender, IWTimestamp dateOfBirth) throws CreateException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#createSpecialCitizen
     */
    public User createSpecialCitizen(String firstname, String middlename,
            String lastname, String personalID, Gender gender,
            IWTimestamp dateOfBirth) throws CreateException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#createSpecialCitizenByPersonalIDIfDoesNotExist
     */
    public User createSpecialCitizenByPersonalIDIfDoesNotExist(
            String firstName, String middleName, String lastName,
            String personalID) throws CreateException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#createSpecialCitizenByPersonalIDIfDoesNotExist
     */
    public User createSpecialCitizenByPersonalIDIfDoesNotExist(
            String firstName, String middleName, String lastName,
            String personalID, Gender gender, IWTimestamp dateOfBirth)
            throws CreateException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#createCommuneAdministrator
     */
    public User createCommuneAdministrator(String firstname, String middlename,
            String lastname) throws CreateException, RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#createProviderAdministrator
     */
    public User createProviderAdministrator(String firstname,
            String middlename, String lastname, School school)
            throws javax.ejb.FinderException, CreateException, RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#createSchoolAdministrator
     */
    public User createSchoolAdministrator(String firstname, String middlename,
            String lastname, School school) throws javax.ejb.FinderException,
            CreateException, RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#getRootCitizenGroup
     */
    public Group getRootCitizenGroup() throws CreateException, FinderException,
            RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#getRootOtherCommuneCitizensGroup
     */
    public Group getRootOtherCommuneCitizensGroup() throws CreateException,
            FinderException, RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#getRootCustomerChoiceGroup
     */
    public Group getRootCustomerChoiceGroup() throws CreateException,
            FinderException, RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#getRootProtectedCitizenGroup
     */
    public Group getRootProtectedCitizenGroup() throws CreateException,
            FinderException, RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#getRootDeceasedCitizensGroup
     */
    public Group getRootDeceasedCitizensGroup() throws CreateException,
            FinderException, RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#getRootCommuneAdministratorGroup
     */
    public Group getRootCommuneAdministratorGroup() throws CreateException,
            FinderException, RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#getAllCommuneAdministrators
     */
    public Collection getAllCommuneAdministrators() throws FinderException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#isRootCommuneAdministrator
     */
    public boolean isRootCommuneAdministrator(User user) throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#getFirstManagingSchoolForUser
     */
    public School getFirstManagingSchoolForUser(User user)
            throws FinderException, RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#getFirstManagingMusicSchoolForUser
     */
    public School getFirstManagingMusicSchoolForUser(User user)
            throws FinderException, RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#getFirstManagingChildCareForUser
     */
    public School getFirstManagingChildCareForUser(User user)
            throws FinderException, RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#hasCitizenAccount
     */
    public boolean hasCitizenAccount(User user) throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#hasCitizenAccount
     */
    public boolean hasCitizenAccount(int userID) throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#hasBankLogin
     */
    public boolean hasBankLogin(User user) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#moveCitizenFromCommune
     */
    public boolean moveCitizenFromCommune(User user, Timestamp time,
            User performer) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#moveCitizenToCommune
     */
    public boolean moveCitizenToCommune(User user, Timestamp time,
            User performer) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#moveCitizenToProtectedCitizenGroup
     */
    public boolean moveCitizenToProtectedCitizenGroup(User user,
            Timestamp time, User performer) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#getChildHomePhone
     */
    public Phone getChildHomePhone(User child) throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#getChildHomePhone
     */
    public Phone getChildHomePhone(int childId) throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#getIfUserAddressesMatch
     */
    public boolean getIfUserAddressesMatch(Address userAddress,
            Address userAddressToCompare) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#getParentsForChild
     */
    public Collection getParentsForChild(User child) throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#getCustodianForChild
     */
    public User getCustodianForChild(int childID) throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#getCustodianForChild
     */
    public User getCustodianForChild(User child) throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#getChildrenForUser
     */
    public Collection getChildrenForUser(User user) throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#getMemberFamilyLogic
     */
    public FamilyLogic getMemberFamilyLogic() throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#getRootAdministratorGroupID
     */
    public int getRootAdministratorGroupID() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#haveSameAddress
     */
    public boolean haveSameAddress(User user, User compareUser)
            throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#updateCitizen
     */
    public void updateCitizen(int userID, String firstName, String middleName,
            String lastName, String personalID) throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#updateCitizenAddress
     */
    public void updateCitizenAddress(int userID, String address,
            String postalCode, String postalName) throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#updateCitizenAddress
     */
    public void updateCitizenAddress(int userID, String address,
            String postalCode, String postalName, Integer communeId)
            throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#getEmail
     */
    public Email getEmail(User user) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#getHomePhone
     */
    public Phone getHomePhone(User user) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#setUserAsDeceased
     */
    public boolean setUserAsDeceased(Integer userID, Date deceasedDate)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#findSchoolChildrenBySearchCondition
     */
    public Collection findSchoolChildrenBySearchCondition(String searchString)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#getNameLastFirst
     */
    public String getNameLastFirst(User user, boolean comma)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.business.CommuneUserBusinessBean#getSnailMailAddress
     */
    public Address getSnailMailAddress(User user) throws RemoteException,
            NoUserAddressException;

}
