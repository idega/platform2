/*
 * $Id: MealVacationDayBMPBean.java,v 1.2 2005/10/02 13:44:24 laddi Exp $
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
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;


/**
 * Last modified: $Date: 2005/10/02 13:44:24 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public class MealVacationDayBMPBean extends GenericEntity  implements MealVacationDay{

	private static final String ENTITY_NAME = "comm_meal_vacation_days";
	
	private static final String COLUMN_SCHOOL = "school_id";
	private static final String COLUMN_VALID_FROM = "valid_from";
	private static final String COLUMN_VALID_TO = "valid_to";
	private static final String COLUMN_TYPE = "type";
	private static final String COLUMN_NAME = "name";
	
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
		
		addAttribute(COLUMN_VALID_FROM, "From", Date.class);
		addAttribute(COLUMN_VALID_TO, "To", Date.class);
		addAttribute(COLUMN_TYPE, "Vacation type", String.class);
		addAttribute(COLUMN_NAME, "Name", String.class);
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
	
	public String getType() {
		return getStringColumnValue(COLUMN_TYPE);
	}
	
	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}
	
	//Setters
	public void setSchool(School school) {
		setColumn(COLUMN_SCHOOL, school);
	}
	
	public void setSchool(Object schoolPK) {
		setColumn(COLUMN_SCHOOL, schoolPK);
	}
	
	public void setValidFrom(Date date) {
		setColumn(COLUMN_VALID_FROM, date);
	}
	
	public void setValidTo(Date date) {
		setColumn(COLUMN_VALID_TO, date);
	}
	
	public void setType(String type) {
		setColumn(COLUMN_TYPE, type);
	}
	
	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
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

	public Collection ejbFindAllBySchoolAndPeriod(School school, Date from, Date to) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		query.addCriteria(new MatchCriteria(table, COLUMN_SCHOOL, MatchCriteria.EQUALS, school));
		query.addCriteria(new MatchCriteria(table, COLUMN_VALID_TO, MatchCriteria.LESSEQUAL, to));
		query.addCriteria(new MatchCriteria(table, COLUMN_VALID_FROM, MatchCriteria.GREATEREQUAL, from));
		query.addOrder(table, COLUMN_VALID_FROM, true);
		
		return idoFindPKsByQuery(query);
	}
	
	public Object ejbFindBySchoolAndDate(School school, Date date) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		query.addCriteria(new MatchCriteria(table, COLUMN_SCHOOL, MatchCriteria.EQUALS, school));
		query.addCriteria(new MatchCriteria(table, COLUMN_VALID_FROM, MatchCriteria.EQUALS, date));
		
		return idoFindOnePKByQuery(query);
	}
}