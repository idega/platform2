/*
 * $Id: MealBusiness.java,v 1.5 2005/10/02 22:12:23 laddi Exp $
 * Created on Oct 2, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.meal.business;

import java.sql.Date;
import java.util.Collection;
import java.util.Map;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.school.meal.data.MealChoice;
import se.idega.idegaweb.commune.school.meal.data.MealPrice;
import se.idega.idegaweb.commune.school.meal.data.MealVacationDay;
import com.idega.block.process.business.CaseBusiness;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolSeason;
import com.idega.business.IBOService;
import com.idega.data.IDOCreateException;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/10/02 22:12:23 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.5 $
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
	 * @see se.idega.idegaweb.commune.school.meal.business.MealBusinessBean#getVacationDays
	 */
	public Collection getVacationDays(School school) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.business.MealBusinessBean#getVacationDay
	 */
	public MealVacationDay getVacationDay(Object vacationDayPK) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.business.MealBusinessBean#deleteVacationDay
	 */
	public void deleteVacationDay(Object vacationDayPK) throws RemoveException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.business.MealBusinessBean#hasChoiceForDate
	 */
	public boolean hasChoiceForDate(User user, School school, SchoolSeason season, Date date)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.business.MealBusinessBean#getMealPrice
	 */
	public MealPrice getMealPrice(Object pricePK) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.business.MealBusinessBean#deleteMealPrice
	 */
	public void deleteMealPrice(Object pricePK) throws RemoveException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.business.MealBusinessBean#getMealPrice
	 */
	public MealPrice getMealPrice(School school, Date date) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.business.MealBusinessBean#storePrices
	 */
	public void storePrices(Object pricePK, School school, Date validFrom, Date validTo, float dayPrice,
			float monthPrice, float milkPrice, float fruitPrice) throws IDOCreateException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.business.MealBusinessBean#storeVacationDays
	 */
	public void storeVacationDays(Object vacationDayPK, School school, Date fromDate, Date toDate, String type,
			String name) throws IDOCreateException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.business.MealBusinessBean#getSchoolPrices
	 */
	public Collection getSchoolPrices(School school) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.business.MealBusinessBean#getChoicesByClaimStatus
	 */
	public Collection getChoicesByClaimStatus(School school) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.business.MealBusinessBean#getSchoolDiners
	 */
	public Collection getSchoolDiners(School school, Date date, Boolean showEmployees) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.business.MealBusinessBean#calculatePrices
	 */
	public MonthValues calculatePrices(Date month, School school, MonthValues values, boolean isEmployee)
			throws FinderException, java.rmi.RemoteException;

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
