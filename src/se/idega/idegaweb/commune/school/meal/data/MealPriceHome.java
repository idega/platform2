/*
 * $Id: MealPriceHome.java,v 1.1 2005/08/10 23:03:11 laddi Exp $
 * Created on Aug 10, 2005
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
import com.idega.data.IDOException;
import com.idega.data.IDOHome;


/**
 * Last modified: $Date: 2005/08/10 23:03:11 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface MealPriceHome extends IDOHome {

	public MealPrice create() throws javax.ejb.CreateException;

	public MealPrice findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealPriceBMPBean#ejbFindAllBySchool
	 */
	public Collection findAllBySchool(School school) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealPriceBMPBean#ejbFindBySchoolAndDate
	 */
	public MealPrice findBySchoolAndDate(School school, Date date) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealPriceBMPBean#ejbHomeGetCountBySchoolAndDate
	 */
	public int getCountBySchoolAndDate(School school, Date date) throws IDOException;
}
