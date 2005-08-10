/*
 * $Id: MealPrice.java,v 1.1 2005/08/10 23:03:11 laddi Exp $
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
public interface MealPrice extends IDOEntity {

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealPriceBMPBean#getSchool
	 */
	public School getSchool();

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealPriceBMPBean#getSchoolPK
	 */
	public Object getSchoolPK();

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealPriceBMPBean#getValidFrom
	 */
	public Date getValidFrom();

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealPriceBMPBean#getValidTo
	 */
	public Date getValidTo();

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealPriceBMPBean#getMealPricePerDay
	 */
	public float getMealPricePerDay();

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealPriceBMPBean#getMealPricePerMonth
	 */
	public float getMealPricePerMonth();

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealPriceBMPBean#getMilkPrice
	 */
	public float getMilkPrice();

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealPriceBMPBean#getFruitsPrice
	 */
	public float getFruitsPrice();

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealPriceBMPBean#setSchool
	 */
	public void setSchool(School school);

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealPriceBMPBean#setSchool
	 */
	public void setSchool(Object schoolPK);

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealPriceBMPBean#setValidFrom
	 */
	public void setValidFrom(Date validFrom);

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealPriceBMPBean#setValidTo
	 */
	public void setValidTo(Date validTo);

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealPriceBMPBean#setMealPricePerDay
	 */
	public void setMealPricePerDay(float price);

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealPriceBMPBean#setMealPricePerMonth
	 */
	public void setMealPricePerMonth(float price);

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealPriceBMPBean#setMilkPrice
	 */
	public void setMilkPrice(float price);

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealPriceBMPBean#setFruitsPrice
	 */
	public void setFruitsPrice(float price);
}
