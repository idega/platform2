/*
 * $Id: MealVacationDay.java,v 1.1 2005/08/10 23:03:11 laddi Exp $
 * Created on Aug 10, 2005
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
 * Last modified: $Date: 2005/08/10 23:03:11 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
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
	 * @see se.idega.idegaweb.commune.school.meal.data.MealVacationDayBMPBean#getDate
	 */
	public Date getDate();

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealVacationDayBMPBean#getVacationType
	 */
	public String getVacationType();

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealVacationDayBMPBean#setSchool
	 */
	public void setSchool(School school);

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealVacationDayBMPBean#setSchool
	 */
	public void setSchool(Object schoolPK);

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealVacationDayBMPBean#setDate
	 */
	public void setDate(Date date);

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealVacationDayBMPBean#setType
	 */
	public void setType(String type);
}
