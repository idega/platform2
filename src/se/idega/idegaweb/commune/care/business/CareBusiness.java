/*
 * $Id: CareBusiness.java,v 1.10 2005/10/18 20:14:24 laddi Exp $
 * Created on Oct 18, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.care.business;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.care.data.CurrentSchoolSeasonHome;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolSeasonHome;
import com.idega.business.IBOService;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/10/18 20:14:24 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.10 $
 */
public interface CareBusiness extends IBOService {

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#getProviderForUser
	 */
	public School getProviderForUser(User user) throws FinderException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#hasGrantedCheck
	 */
	public boolean hasGrantedCheck(User child) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#getCurrentSeason
	 */
	public SchoolSeason getCurrentSeason() throws java.rmi.RemoteException, javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#getCurrentSchoolSeasonHome
	 */
	public CurrentSchoolSeasonHome getCurrentSchoolSeasonHome() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#getSchoolSeasonHome
	 */
	public SchoolSeasonHome getSchoolSeasonHome() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#getStudentList
	 */
	public Map getStudentList(Collection students) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#storeChildInformation
	 */
	public void storeChildInformation(User child, Boolean growthDeviation, String growthDeviationDetails,
			Boolean allergies, String allergiesDetails, String lastCareProvider, boolean canContactLastProvider,
			String otherInformation) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#hasGrowthDeviation
	 */
	public Boolean hasGrowthDeviation(User child) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#getGrowthDeviationDetails
	 */
	public String getGrowthDeviationDetails(User child) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#hasAllergies
	 */
	public Boolean hasAllergies(User child) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#getAllergiesDetails
	 */
	public String getAllergiesDetails(User child) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#getLastCareProvider
	 */
	public String getLastCareProvider(User child) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#canContactLastCareProvider
	 */
	public Boolean canContactLastCareProvider(User child) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#getOtherInformation
	 */
	public String getOtherInformation(User child) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#storeExtraCustodian
	 */
	public void storeExtraCustodian(User child, User custodian, String relation, String homePhone, String workPhone,
			String mobilePhone, String email) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#getExtraCustodian
	 */
	public User getExtraCustodian(User child) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#getRelatives
	 */
	public List getRelatives(User child) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#storeRelative
	 */
	public void storeRelative(User child, String name, String relation, int number, String homePhone, String workPhone,
			String mobilePhone, String email) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#storeUserRelation
	 */
	public void storeUserRelation(User child, User relatedUser, String relation) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#getUserRelation
	 */
	public String getUserRelation(User child, User relatedUser) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#updateUserInfo
	 */
	public void updateUserInfo(User user, String homePhone, String workPhone, String mobilePhone, String email)
			throws java.rmi.RemoteException;
}
