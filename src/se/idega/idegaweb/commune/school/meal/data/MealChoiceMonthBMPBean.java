/*
 * $Id: MealChoiceMonthBMPBean.java,v 1.1 2005/08/10 23:03:11 laddi Exp $
 * Created on Aug 10, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.meal.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.GenericEntity;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;


/**
 * Last modified: $Date: 2005/08/10 23:03:11 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class MealChoiceMonthBMPBean extends GenericEntity  implements MealChoiceMonth{

	private static final String ENTITY_NAME = "comm_meal_choice_month";
	
	private static final String COLUMN_MEAL_CHOICE = "meal_choice_id";
	private static final String COLUMN_MONTH = "month";
	private static final String COLUMN_YEAR = "year";
	private static final String COLUMN_MONDAYS = "mondays";
	private static final String COLUMN_TUESDAYS = "tuesdays";
	private static final String COLUMN_WEDNESDAYS = "wednesdays";
	private static final String COLUMN_THURSDAYS = "thursdays";
	private static final String COLUMN_FRIDAYS = "fridays";
	private static final String COLUMN_MILK = "milk";
	private static final String COLUMN_FRUITS = "fruits";
	private static final String COLUMN_AMOUNT = "amount";

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
		
		addManyToOneRelationship(COLUMN_MEAL_CHOICE, "Meal choice", MealChoice.class);
		
		addAttribute(COLUMN_MONTH, "Month", Integer.class);
		addAttribute(COLUMN_YEAR, "Year", Integer.class);

		addAttribute(COLUMN_MONDAYS, "Mondays", Boolean.class);
		addAttribute(COLUMN_TUESDAYS, "Tuesdays", Boolean.class);
		addAttribute(COLUMN_WEDNESDAYS, "Wednesdays", Boolean.class);
		addAttribute(COLUMN_THURSDAYS, "Thursdays", Boolean.class);
		addAttribute(COLUMN_FRIDAYS, "Fridays", Boolean.class);

		addAttribute(COLUMN_MILK, "Milk", Boolean.class);
		addAttribute(COLUMN_FRUITS, "Fruites", Boolean.class);

		addAttribute(COLUMN_AMOUNT, "Amount", Float.class);
	}
	
	//Getters
	public MealChoice getChoice() {
		return (MealChoice) getColumnValue(COLUMN_MEAL_CHOICE);
	}
	
	public Object getChoicePK() {
		return getIntegerColumnValue(COLUMN_MEAL_CHOICE);
	}
	
	public boolean hasMondays() {
		return getBooleanColumnValue(COLUMN_MONDAYS, false);
	}

	public boolean hasTuesdays() {
		return getBooleanColumnValue(COLUMN_TUESDAYS, false);
	}

	public boolean hasWednesdays() {
		return getBooleanColumnValue(COLUMN_WEDNESDAYS, false);
	}

	public boolean hasThursdays() {
		return getBooleanColumnValue(COLUMN_THURSDAYS, false);
	}

	public boolean hasFridays() {
		return getBooleanColumnValue(COLUMN_FRIDAYS, false);
	}

	public boolean hasMilk() {
		return getBooleanColumnValue(COLUMN_MILK, false);
	}

	public boolean hasFruits() {
		return getBooleanColumnValue(COLUMN_FRUITS, false);
	}
	
	public float getAmount() {
		return getFloatColumnValue(COLUMN_AMOUNT);
	}
	
	//Setters
	public void setChoice(MealChoice choice) {
		setColumn(COLUMN_MEAL_CHOICE, choice);
	}
	
	public void setChoice(Object choicePK) {
		setColumn(COLUMN_MEAL_CHOICE, choicePK);
	}
	
	public void setMondays(boolean hasDay) {
		setColumn(COLUMN_MONDAYS, hasDay);
	}
	
	public void setTuesdays(boolean hasDay) {
		setColumn(COLUMN_TUESDAYS, hasDay);
	}
	
	public void setWednesdays(boolean hasDay) {
		setColumn(COLUMN_WEDNESDAYS, hasDay);
	}
	
	public void setThursdays(boolean hasDay) {
		setColumn(COLUMN_THURSDAYS, hasDay);
	}
	
	public void setFridays(boolean hasDay) {
		setColumn(COLUMN_FRIDAYS, hasDay);
	}
	
	public void setMilk(boolean hasMilk) {
		setColumn(COLUMN_MILK, hasMilk);
	}
	
	public void setFruits(boolean hasFruits) {
		setColumn(COLUMN_FRUITS, hasFruits);
	}
	
	public void setAmount(float amount) {
		setColumn(COLUMN_AMOUNT, amount);
	}
	
	//Finders
	public Collection ejbFindAllByChoice(MealChoice choice) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		query.addCriteria(new MatchCriteria(table, COLUMN_MEAL_CHOICE, MatchCriteria.EQUALS, choice));
		query.addOrder(table, COLUMN_YEAR, true);
		query.addOrder(table, COLUMN_MONTH, true);
		
		return idoFindPKsByQuery(query);
	}
	
	public Object ejbFindByChoice(MealChoice choice, int month, int year) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		query.addCriteria(new MatchCriteria(table, COLUMN_MEAL_CHOICE, MatchCriteria.EQUALS, choice));
		query.addCriteria(new MatchCriteria(table, COLUMN_MONTH, MatchCriteria.EQUALS, month));
		query.addCriteria(new MatchCriteria(table, COLUMN_YEAR, MatchCriteria.EQUALS, year));
		
		return idoFindOnePKByQuery(query);
	}
}