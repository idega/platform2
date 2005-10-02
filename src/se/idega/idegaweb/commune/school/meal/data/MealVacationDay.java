/*
 * $Id: MealVacationDay.java,v 1.2 2005/10/02 13:44:24 laddi Exp $
 * Created on Oct 2, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.meal.data;

import java.sql.Date;
import com.idega.block.school.data.School;
import com.idega.data.IDOEntity;


/**
 * Last modified: $Date: 2005/10/02 13:44:24 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public interface MealVacationDay extends IDOEntity {

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealVacationDayBMPBean#getSchool
	 */
	public School getSchool();

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealVacationDayBMPBean#getSchoolPK
	 */
	public Object getSchoolPK();

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealVacationDayBMPBean#getValidFrom
	 */
	public Date getValidFrom();

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealVacationDayBMPBean#getValidTo
	 */
	public Date getValidTo();

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealVacationDayBMPBean#getType
	 */
	public String getType();

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealVacationDayBMPBean#getName
	 */
	public String getName();

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealVacationDayBMPBean#setSchool
	 */
	public void setSchool(School school);

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealVacationDayBMPBean#setSchool
	 */
	public void setSchool(Object schoolPK);

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealVacationDayBMPBean#setValidFrom
	 */
	public void setValidFrom(Date date);

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealVacationDayBMPBean#setValidTo
	 */
	public void setValidTo(Date date);

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealVacationDayBMPBean#setType
	 */
	public void setType(String type);

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealVacationDayBMPBean#setName
	 */
	public void setName(String name);
}
