/*
 * $Id: MealBusiness.java,v 1.1 2005/08/10 23:03:11 laddi Exp $
 * Created on Aug 10, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.meal.business;

import java.sql.Date;
import java.util.Map;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.school.meal.data.MealChoice;
import com.idega.block.process.business.CaseBusiness;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolSeason;
import com.idega.business.IBOService;
import com.idega.data.IDOCreateException;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/08/10 23:03:11 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface MealBusiness extends IBOService, CaseBusiness {

	/**
	 * @see se.idega.idegaweb.commune.school.meal.business.MealBusinessBean#getSchoolBusiness
	 */
	public SchoolBusiness getSchoolBusiness() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.business.MealBusinessBean#getUserBusiness
	 */
	public CommuneUserBusiness getUserBusiness() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.business.MealBusinessBean#getOngoingSeason
	 */
	public SchoolSeason getOngoingSeason() throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.business.MealBusinessBean#getNextSeason
	 */
	public SchoolSeason getNextSeason() throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.business.MealBusinessBean#getSchoolPlacing
	 */
	public SchoolClassMember getSchoolPlacing(User user, SchoolSeason season) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.business.MealBusinessBean#storePrices
	 */
	public void storePrices(School school, Date validFrom, Date validTo, float dayPrice, float monthPrice,
			float milkPrice, float fruitPrice) throws IDOCreateException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.business.MealBusinessBean#storeVacationDays
	 */
	public void storeVacationDays(School school, Date fromDate, String type, int numberOfDays) throws IDOCreateException,
			java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.business.MealBusinessBean#getPriceForMonth
	 */
	public float getPriceForMonth(Date month, School school, MonthValues values) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.business.MealBusinessBean#storeChoice
	 */
	public MealChoice storeChoice(MealChoice choice, User user, School school, SchoolSeason season, String comments,
			Date[] months, Map monthValues, User performer) throws IDOCreateException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.business.MealBusinessBean#sendMessageToParents
	 */
	public void sendMessageToParents(MealChoice application, String subject, String body) throws java.rmi.RemoteException;
}
