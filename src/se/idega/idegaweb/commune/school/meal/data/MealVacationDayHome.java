/*
 * $Id: MealVacationDayHome.java,v 1.2 2005/10/02 13:44:24 laddi Exp $
 * Created on Oct 2, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.meal.data;

import java.sql.Date;
import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.block.school.data.School;
import com.idega.data.IDOHome;


/**
 * Last modified: $Date: 2005/10/02 13:44:24 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public interface MealVacationDayHome extends IDOHome {

	public MealVacationDay create() throws javax.ejb.CreateException;

	public MealVacationDay findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealVacationDayBMPBean#ejbFindAllBySchool
	 */
	public Collection findAllBySchool(School school) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealVacationDayBMPBean#ejbFindAllBySchoolAndPeriod
	 */
	public Collection findAllBySchoolAndPeriod(School school, Date from, Date to) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealVacationDayBMPBean#ejbFindBySchoolAndDate
	 */
	public MealVacationDay findBySchoolAndDate(School school, Date date) throws FinderException;
}
