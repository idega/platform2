/*
 * $Id: AfterSchoolBusiness.java,v 1.12 2005/08/09 16:35:19 laddi Exp $
 * Created on Aug 9, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.care.data.AfterSchoolChoice;
import se.idega.idegaweb.commune.care.data.ChildCareApplication;
import com.idega.block.process.data.Case;
import com.idega.block.process.data.CaseStatus;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolSeason;
import com.idega.business.IBOService;
import com.idega.data.IDOCreateException;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;


/**
 * Last modified: $Date: 2005/08/09 16:35:19 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.12 $
 */
public interface AfterSchoolBusiness extends IBOService, ChildCareBusiness {

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#getAfterSchoolChoice
	 */
	public AfterSchoolChoice getAfterSchoolChoice(Object afterSchoolChoiceID) throws FinderException,
			java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#findChoicesByProvider
	 */
	public Collection findChoicesByProvider(int providerID) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#findChoicesByProvider
	 */
	public Collection findChoicesByProvider(int providerID, String sorting) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#findChoicesByChildAndChoiceNumberAndSeason
	 */
	public AfterSchoolChoice findChoicesByChildAndChoiceNumberAndSeason(Integer childID, int choiceNumber,
			Integer seasonID) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#acceptAfterSchoolChoice
	 */
	public boolean acceptAfterSchoolChoice(Object afterSchoolChoiceID, User performer) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#denyAfterSchoolChoice
	 */
	public boolean denyAfterSchoolChoice(Object afterSchoolChoiceID, User performer) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#createAfterSchoolChoice
	 */
	public AfterSchoolChoice createAfterSchoolChoice(IWTimestamp stamp, User user, Integer childID, Integer providerID,
			Integer choiceNumber, String message, CaseStatus caseStatus, Case parentCase, Date placementDate,
			SchoolSeason season, String subject, String body) throws CreateException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#createAfterSchoolChoices
	 */
	public List createAfterSchoolChoices(User user, Integer childId, Integer[] providerIDs, String message,
			String[] placementDates, SchoolSeason season, String subject, String body) throws IDOCreateException,
			java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#createContractsForChildrenWithSchoolPlacement
	 */
	public Collection createContractsForChildrenWithSchoolPlacement(int providerId, User user, Locale locale)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#storeDays
	 */
	public void storeDays(ChildCareApplication application, int[] dayOfWeek, String[] timeOfDeparture, boolean[] pickedUp)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#getDefaultGroup
	 */
	public SchoolClass getDefaultGroup(Object schoolPK, Object seasonPK) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#getDefaultGroup
	 */
	public SchoolClass getDefaultGroup(School school, SchoolSeason season) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#storeAfterSchoolCare
	 */
	public void storeAfterSchoolCare(IWTimestamp stamp, User user, User child, School provider, String message,
			SchoolSeason season, int[] days, String[] timeOfDeparture, boolean[] pickedUp, String payerName,
			String payerPersonalID, String cardType, String cardNumber, int validMonth, int validYear)
			throws java.rmi.RemoteException;
}
