/*
 * $Id: SchoolCoursePackageBMPBean.java,v 1.2 2005/08/08 22:21:37 laddi Exp $
 * Created on Jul 4, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.data;

import java.util.Collection;
import java.util.Iterator;
import javax.ejb.FinderException;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolSeason;
import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.data.query.CountColumn;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;


/**
 * Last modified: $Date: 2005/08/08 22:21:37 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public class SchoolCoursePackageBMPBean extends GenericEntity  implements SchoolCoursePackage{

	private static final String ENTITY_NAME = "vux_school_package";
	
	private static final String COLUMN_PACKAGE = "vux_package_id";
	private static final String COLUMN_SCHOOL = "sch_school_id";
	private static final String COLUMN_SEASON = "sch_school_season_id";
	private static final String COLUMN_FREE_TEXT = "free_text";
	private static final String COLUMN_ACTIVE = "active";
	
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
		
		addManyToOneRelationship(COLUMN_PACKAGE, CoursePackage.class);
		addManyToOneRelationship(COLUMN_SCHOOL, School.class);
		addManyToOneRelationship(COLUMN_SEASON, SchoolSeason.class);
		
		addAttribute(COLUMN_FREE_TEXT, "Free text", String.class);
		addAttribute(COLUMN_ACTIVE, "Active", Boolean.class);
		
		addManyToManyRelationShip(AdultEducationCourse.class);
	}
	
	public void setDefaultValues() {
		setActive(false);
	}
	
	//Getters
	public CoursePackage getPackage() {
		return (CoursePackage) getColumnValue(COLUMN_PACKAGE);
	}
	
	public Object getPackagePK() {
		return getIntegerColumnValue(COLUMN_PACKAGE);
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
	
	public String getFreeText() {
		return getStringColumnValue(COLUMN_FREE_TEXT);
	}
	
	public boolean isActive() {
		return getBooleanColumnValue(COLUMN_ACTIVE, true);
	}
	
	public Collection getCourses() throws IDORelationshipException {
		return idoGetRelatedEntities(AdultEducationCourse.class);
	}
	
	//Setters
	public void setPackage(CoursePackage coursePackage) {
		setColumn(COLUMN_PACKAGE, coursePackage);
	}
	
	public void setSchool(School school) {
		setColumn(COLUMN_SCHOOL, school);
	}
	
	public void setSeason(SchoolSeason season) {
		setColumn(COLUMN_SEASON, season);
	}
	
	public void setFreeText(String text) {
		setColumn(COLUMN_FREE_TEXT, text);
	}
	
	public void setActive(boolean active) {
		setColumn(COLUMN_ACTIVE, active);
	}
	
	public void addCourse(AdultEducationCourse course) throws IDOAddRelationshipException {
		idoAddTo(course);
	}
	
	public void addCourses(Collection courses) throws IDOAddRelationshipException {
		Iterator iter = courses.iterator();
		while (iter.hasNext()) {
			addCourse((AdultEducationCourse) iter.next());
		}
	}
	
	public void removeCourse(AdultEducationCourse course) throws IDORemoveRelationshipException {
		idoRemoveFrom(course);
	}
	
	public void removeCourses() throws IDORemoveRelationshipException {
		idoRemoveFrom(AdultEducationCourse.class);
	}
	
	//Finders
	public Collection ejbFindAllBySchool(School school) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		query.addCriteria(new MatchCriteria(table, COLUMN_SCHOOL, MatchCriteria.EQUALS, school));
		
		return idoFindPKsByQuery(query);
	}

	public Collection ejbFindBySchoolAndSeason(School school, SchoolSeason season) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		query.addCriteria(new MatchCriteria(table, COLUMN_SCHOOL, MatchCriteria.EQUALS, school));
		query.addCriteria(new MatchCriteria(table, COLUMN_SEASON, MatchCriteria.EQUALS, season));
		
		return idoFindPKsByQuery(query);
	}
	
	public Collection ejbFindBySchoolAndSeasonAndPackage(School school, SchoolSeason season, CoursePackage coursePackage) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		query.addCriteria(new MatchCriteria(table, COLUMN_SCHOOL, MatchCriteria.EQUALS, school));
		if (season != null) {
			query.addCriteria(new MatchCriteria(table, COLUMN_SEASON, MatchCriteria.EQUALS, season));
		}
		if (coursePackage != null) {
			query.addCriteria(new MatchCriteria(table, COLUMN_PACKAGE, MatchCriteria.EQUALS, coursePackage));
		}
		
		return idoFindPKsByQuery(query);
	}
	
	public int ejbHomeGetNumberOfSchoolPackages(CoursePackage coursePackage) throws IDOException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new CountColumn(table, getIDColumnName()));
		query.addCriteria(new MatchCriteria(table, COLUMN_PACKAGE, MatchCriteria.EQUALS, coursePackage));
		
		return idoGetNumberOfRecords(query);
	}
}