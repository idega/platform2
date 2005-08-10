/*
 * $Id: MealPriceBMPBean.java,v 1.1 2005/08/10 23:03:11 laddi Exp $
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
import com.idega.data.GenericEntity;
import com.idega.data.IDOException;
import com.idega.data.query.CountColumn;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;


/**
 * Last modified: $Date: 2005/08/10 23:03:11 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class MealPriceBMPBean extends GenericEntity  implements MealPrice{

	private static final String ENTITY_NAME = "comm_meal_price";
	
	private static final String COLUMN_SCHOOL = "school_id";
	private static final String COLUMN_VALID_FROM = "valid_from";
	private static final String COLUMN_VALID_TO = "valid_to";
	private static final String COLUMN_MEAL_PRICE_DAY = "meal_price_day";
	private static final String COLUMN_MEAL_PRICE_MONTH = "meal_price_month";
	private static final String COLUMN_MILK_PRICE = "milk_price";
	private static final String COLUMN_FRUITS_PRICE = "fruits_price";

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		
		addManyToOneRelationship(COLUMN_SCHOOL, School.class);
		
		addAttribute(COLUMN_VALID_FROM, "Valid from", Date.class);
		addAttribute(COLUMN_VALID_TO, "Valid to", Date.class);
		
		addAttribute(COLUMN_MEAL_PRICE_DAY, "Meal price per day", Float.class);
		addAttribute(COLUMN_MEAL_PRICE_MONTH, "Meal price per month", Float.class);
		addAttribute(COLUMN_MILK_PRICE, "Milk price per month", Float.class);
		addAttribute(COLUMN_FRUITS_PRICE, "Fruit price per month", Float.class);
	}
	
	//Getters
	public School getSchool() {
		return (School) getColumnValue(COLUMN_SCHOOL);
	}
	
	public Object getSchoolPK() {
		return getIntegerColumnValue(COLUMN_SCHOOL);
	}
	
	public Date getValidFrom() {
		return getDateColumnValue(COLUMN_VALID_FROM);
	}
	
	public Date getValidTo() {
		return getDateColumnValue(COLUMN_VALID_TO);
	}
	
	public float getMealPricePerDay() {
		return getFloatColumnValue(COLUMN_MEAL_PRICE_DAY);
	}
	
	public float getMealPricePerMonth() {
		return getFloatColumnValue(COLUMN_MEAL_PRICE_MONTH);
	}
	
	public float getMilkPrice() {
		return getFloatColumnValue(COLUMN_MILK_PRICE);
	}
	
	public float getFruitsPrice() {
		return getFloatColumnValue(COLUMN_FRUITS_PRICE);
	}
	
	//Setters
	public void setSchool(School school) {
		setColumn(COLUMN_SCHOOL, school);
	}
	
	public void setSchool(Object schoolPK) {
		setColumn(COLUMN_SCHOOL, schoolPK);
	}
	
	public void setValidFrom(Date validFrom) {
		setColumn(COLUMN_VALID_FROM, validFrom);
	}
	
	public void setValidTo(Date validTo) {
		setColumn(COLUMN_VALID_TO, validTo);
	}
	
	public void setMealPricePerDay(float price) {
		setColumn(COLUMN_MEAL_PRICE_DAY, price);
	}
	
	public void setMealPricePerMonth(float price) {
		setColumn(COLUMN_MEAL_PRICE_MONTH, price);
	}
	
	public void setMilkPrice(float price) {
		setColumn(COLUMN_MILK_PRICE, price);
	}
	
	public void setFruitsPrice(float price) {
		setColumn(COLUMN_FRUITS_PRICE, price);
	}
	
	//Finders
	public Collection ejbFindAllBySchool(School school) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		query.addCriteria(new MatchCriteria(table, COLUMN_SCHOOL, MatchCriteria.EQUALS, school));
		query.addOrder(table, COLUMN_VALID_FROM, true);
		
		return idoFindPKsByQuery(query);
	}

	public Object ejbFindBySchoolAndDate(School school, Date date) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		query.addCriteria(new MatchCriteria(table, COLUMN_SCHOOL, MatchCriteria.EQUALS, school));
		query.addCriteria(new MatchCriteria(table, COLUMN_VALID_FROM, MatchCriteria.LESSEQUAL, date));
		query.addCriteria(new MatchCriteria(table, COLUMN_VALID_TO, MatchCriteria.GREATEREQUAL, date));
		
		return idoFindOnePKByQuery(query);
	}

	public int ejbHomeGetCountBySchoolAndDate(School school, Date date) throws IDOException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new CountColumn(table, getIDColumnName()));
		query.addCriteria(new MatchCriteria(table, COLUMN_SCHOOL, MatchCriteria.EQUALS, school));
		query.addCriteria(new MatchCriteria(table, COLUMN_VALID_FROM, MatchCriteria.LESSEQUAL, date));
		query.addCriteria(new MatchCriteria(table, COLUMN_VALID_TO, MatchCriteria.GREATEREQUAL, date));
		
		return idoGetNumberOfRecords(query);
	}
}