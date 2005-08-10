/*
 * $Id: MealVacationDayBMPBean.java,v 1.1 2005/08/10 23:03:11 laddi Exp $
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
 * Last modified: $Date: 2005/08/10 23:03:11 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class MealVacationDayBMPBean extends GenericEntity  implements MealVacationDay{

	private static final String ENTITY_NAME = "comm_meal_vacation_days";
	
	private static final String COLUMN_SCHOOL = "school_id";
	private static final String COLUMN_DATE = "from";
	private static final String COLUMN_TYPE = "type";
	
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
		
		addAttribute(COLUMN_DATE, "Date", Date.class);
		addAttribute(COLUMN_TYPE, "Vacation type", String.class);
	}

	//Getters
	public School getSchool() {
		return (School) getColumnValue(COLUMN_SCHOOL);
	}
	
	public Object getSchoolPK() {
		return getIntegerColumnValue(COLUMN_SCHOOL);
	}
	
	public Date getDate() {
		return getDateColumnValue(COLUMN_DATE);
	}
	
	public String getVacationType() {
		return getStringColumnValue(COLUMN_TYPE);
	}
	
	//Setters
	public void setSchool(School school) {
		setColumn(COLUMN_SCHOOL, school);
	}
	
	public void setSchool(Object schoolPK) {
		setColumn(COLUMN_SCHOOL, schoolPK);
	}
	
	public void setDate(Date date) {
		setColumn(COLUMN_DATE, date);
	}
	
	public void setType(String type) {
		setColumn(COLUMN_TYPE, type);
	}
	
	//Finders
	public Collection ejbFindAllBySchool(School school) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		query.addCriteria(new MatchCriteria(table, COLUMN_SCHOOL, MatchCriteria.EQUALS, school));
		query.addOrder(table, COLUMN_DATE, true);
		
		return idoFindPKsByQuery(query);
	}

	public Collection ejbFindAllBySchoolAndPeriod(School school, Date from, Date to) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		query.addCriteria(new MatchCriteria(table, COLUMN_SCHOOL, MatchCriteria.EQUALS, school));
		query.addCriteria(new MatchCriteria(table, COLUMN_DATE, MatchCriteria.LESSEQUAL, to));
		query.addCriteria(new MatchCriteria(table, COLUMN_DATE, MatchCriteria.GREATEREQUAL, from));
		query.addOrder(table, COLUMN_DATE, true);
		
		return idoFindPKsByQuery(query);
	}
	
	public Object ejbFindBySchoolAndDate(School school, Date date) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		query.addCriteria(new MatchCriteria(table, COLUMN_SCHOOL, MatchCriteria.EQUALS, school));
		query.addCriteria(new MatchCriteria(table, COLUMN_DATE, MatchCriteria.EQUALS, date));
		
		return idoFindOnePKByQuery(query);
	}
}