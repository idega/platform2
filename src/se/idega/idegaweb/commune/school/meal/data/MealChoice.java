/*
 * $Id: MealChoice.java,v 1.2 2005/10/02 22:12:23 laddi Exp $
 * Created on Oct 2, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.meal.data;

import com.idega.block.finance.data.AccountEntry;
import com.idega.block.process.data.Case;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolSeason;
import com.idega.data.IDOEntity;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/10/02 22:12:23 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public interface MealChoice extends IDOEntity, Case {

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealChoiceBMPBean#getCaseCodeKey
	 */
	public String getCaseCodeKey();

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealChoiceBMPBean#getCaseCodeDescription
	 */
	public String getCaseCodeDescription();

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealChoiceBMPBean#getUser
	 */
	public User getUser();

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealChoiceBMPBean#getUserPK
	 */
	public Object getUserPK();

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealChoiceBMPBean#getSchool
	 */
	public School getSchool();

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealChoiceBMPBean#getSchoolPK
	 */
	public Object getSchoolPK();

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealChoiceBMPBean#getSeason
	 */
	public SchoolSeason getSeason();

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealChoiceBMPBean#getSeasonPK
	 */
	public Object getSeasonPK();

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealChoiceBMPBean#getAccountEntry
	 */
	public AccountEntry getAccountEntry();

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealChoiceBMPBean#getAccountEntryPK
	 */
	public Object getAccountEntryPK();

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealChoiceBMPBean#getComments
	 */
	public String getComments();

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealChoiceBMPBean#isEmployee
	 */
	public boolean isEmployee();

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealChoiceBMPBean#setUser
	 */
	public void setUser(User user);

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealChoiceBMPBean#setUser
	 */
	public void setUser(Object userPK);

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealChoiceBMPBean#setSchool
	 */
	public void setSchool(School school);

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealChoiceBMPBean#setSchool
	 */
	public void setSchool(Object schoolPK);

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealChoiceBMPBean#setSeason
	 */
	public void setSeason(SchoolSeason season);

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealChoiceBMPBean#setAccountEntry
	 */
	public void setAccountEntry(Object accountEntryPK);

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealChoiceBMPBean#setAccountEntry
	 */
	public void setAccountEntry(AccountEntry accountEntry);

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealChoiceBMPBean#setSeason
	 */
	public void setSeason(Object seasonPK);

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealChoiceBMPBean#setComments
	 */
	public void setComments(String comments);

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealChoiceBMPBean#setEmployee
	 */
	public void setEmployee(boolean employee);
}
