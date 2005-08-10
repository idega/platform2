/*
 * $Id: MealChoiceBMPBean.java,v 1.1 2005/08/10 23:03:11 laddi Exp $
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
import se.idega.idegaweb.commune.school.meal.util.MealConstants;
import com.idega.block.process.data.AbstractCaseBMPBean;
import com.idega.block.process.data.Case;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolSeason;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/08/10 23:03:11 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class MealChoiceBMPBean extends AbstractCaseBMPBean implements Case , MealChoice{
	
	private static final String ENTITY_NAME = "comm_meal_choice";
	
	private static final String COLUMN_USER = "user_id";
	private static final String COLUMN_SCHOOL = "school_id";
	private static final String COLUMN_SEASON = "season_id";
	private static final String COLUMN_COMMENTS = "comments";
	private static final String COLUMN_IS_EMPLOYEE = "is_employee";

	/* (non-Javadoc)
	 * @see com.idega.block.process.data.AbstractCaseBMPBean#getCaseCodeKey()
	 */
	public String getCaseCodeKey() {
		return MealConstants.CASE_CODE_KEY;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.process.data.AbstractCaseBMPBean#getCaseCodeDescription()
	 */
	public String getCaseCodeDescription() {
		return "Case for school meal choices";
	}

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
		addGeneralCaseRelation();
		
		addManyToOneRelationship(COLUMN_USER, "User", User.class);
		addManyToOneRelationship(COLUMN_SCHOOL, "School", School.class);
		addManyToOneRelationship(COLUMN_SEASON, "Season", SchoolSeason.class);
		
		addAttribute(COLUMN_COMMENTS, "Comments", String.class, 4000);
		addAttribute(COLUMN_IS_EMPLOYEE, "Is employee", Boolean.class);
	}
	
	//Getters
	public User getUser() {
		return (User) getColumnValue(COLUMN_USER);
	}
	
	public Object getUserPK() {
		return getIntegerColumnValue(COLUMN_USER);
	}
	
	public School getSchool() {
		return (School) getColumnValue(COLUMN_SCHOOL);
	}
	
	public Object getSchoolPK() {
		return getIntegerColumnValue(COLUMN_SCHOOL);
	}
	
	public SchoolSeason getSeason() {
		return (SchoolSeason) getColumnValue(COLUMN_SEASON);
	}
	
	public Object getSeasonPK() {
		return getIntegerColumnValue(COLUMN_SEASON);
	}
	
	public String getComments() {
		return getStringColumnValue(COLUMN_COMMENTS);
	}
	
	public boolean isEmployee() {
		return getBooleanColumnValue(COLUMN_IS_EMPLOYEE, false);
	}
	
	//Setters
	public void setUser(User user) {
		setColumn(COLUMN_USER, user);
	}
	
	public void setUser(Object userPK) {
		setColumn(COLUMN_USER, userPK);
	}
	
	public void setSchool(School school) {
		setColumn(COLUMN_SCHOOL, school);
	}
	
	public void setSchool(Object schoolPK) {
		setColumn(COLUMN_SCHOOL, schoolPK);
	}
	
	public void setSeason(SchoolSeason season) {
		setColumn(COLUMN_SEASON, season);
	}
	
	public void setSeason(Object seasonPK) {
		setColumn(COLUMN_SEASON, seasonPK);
	}
	
	public void setComments(String comments) {
		setColumn(COLUMN_COMMENTS, comments);
	}
	
	public void setEmployee(boolean employee) {
		setColumn(COLUMN_IS_EMPLOYEE, employee);
	}
	
	//Finders
	public Object ejbFindByUserAndSeason(User user, School school, SchoolSeason season) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		query.addCriteria(new MatchCriteria(table, COLUMN_USER, MatchCriteria.EQUALS, user));
		query.addCriteria(new MatchCriteria(table, COLUMN_SCHOOL, MatchCriteria.EQUALS, school));
		query.addCriteria(new MatchCriteria(table, COLUMN_SEASON, MatchCriteria.EQUALS, season));
		
		return idoFindOnePKByQuery(query);
	}
	
	public Collection ejbFindAllBySchoolAndSeason(School school, SchoolSeason season) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		query.addCriteria(new MatchCriteria(table, COLUMN_SCHOOL, MatchCriteria.EQUALS, school));
		query.addCriteria(new MatchCriteria(table, COLUMN_SEASON, MatchCriteria.EQUALS, season));
		
		return idoFindPKsByQuery(query);
	}
}