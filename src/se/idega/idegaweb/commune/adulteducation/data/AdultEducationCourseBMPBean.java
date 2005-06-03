/*
 * $Id: AdultEducationCourseBMPBean.java,v 1.4 2005/06/03 06:51:18 laddi Exp $
 * Created on 27.4.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.data;

import java.sql.Date;
import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.block.process.data.Case;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolStudyPathGroup;
import com.idega.block.school.data.SchoolType;
import com.idega.data.GenericEntity;
import com.idega.data.IDOEntity;
import com.idega.data.IDORelationshipException;
import com.idega.data.query.InCriteria;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;


public class AdultEducationCourseBMPBean extends GenericEntity  implements AdultEducationCourse{

	private static final String ENTITY_NAME = "vux_course";
	
	private static final String STUDY_PATH = "sch_study_path_id";
	private static final String SCHOOL_SEASON = "sch_school_season_id";
	private static final String SCHOOL = "sch_school_id";
	private static final String START_DATE = "course_start_date";
	private static final String END_DATE = "course_end_date";
	private static final String COMMENT = "course_comment";
	private static final String CODE = "course_code";
	private static final String LENGTH = "course_length";
	private static final String NOT_ACTIVE = "not_active";
	
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		
		addManyToOneRelationship(STUDY_PATH, "Study path", SchoolStudyPath.class);
		addManyToOneRelationship(SCHOOL_SEASON, "Season", SchoolSeason.class);
		addManyToOneRelationship(SCHOOL, "School", School.class);
		
		addAttribute(START_DATE, "Start date", Date.class);
		addAttribute(END_DATE, "End date", Date.class);
		addAttribute(COMMENT, "Comment", String.class);
		addAttribute(CODE, "Code", String.class, 20);
		addAttribute(LENGTH, "Length", Integer.class);
		addAttribute(NOT_ACTIVE, "Not active", Boolean.class);
	}
	
	public void setDefaultValues() {
		setNotActive(false);
	}

	//Getters
	public SchoolStudyPath getStudyPath() {
		return (SchoolStudyPath) getColumnValue(STUDY_PATH);
	}

	public Object getStudyPathPK() {
		return getIntegerColumnValue(STUDY_PATH);
	}

	public SchoolSeason getSchoolSeason() {
		return (SchoolSeason) getColumnValue(SCHOOL_SEASON);
	}

	public Object getSchoolSeasonPK() {
		return getIntegerColumnValue(SCHOOL_SEASON);
	}

	public School getSchool() {
		return (School) getColumnValue(SCHOOL);
	}

	public Object getSchoolPK() {
		return getIntegerColumnValue(SCHOOL);
	}

	public Date getStartDate() {
		return getDateColumnValue(START_DATE);
	}

	public Date getEndDate() {
		return getDateColumnValue(END_DATE);
	}

	public String getComment() {
		return getStringColumnValue(COMMENT);
	}

	public String getCode() {
		return getStringColumnValue(CODE);
	}

	public int getLength() {
		return getIntColumnValue(LENGTH);
	}
	
	public boolean isInactive() {
		return getBooleanColumnValue(NOT_ACTIVE, false);
	}
	
	//Setters
	public void setStudyPath(SchoolStudyPath path) {
		setColumn(STUDY_PATH, path);
	}
	
	public void setStudyPath(Object path) {
		setColumn(STUDY_PATH, path);
	}
	
	public void setSchoolSeason(SchoolSeason season) {
		setColumn(SCHOOL_SEASON, season);
	}
	
	public void setSchoolSeason(Object season) {
		setColumn(SCHOOL_SEASON, season);
	}
	
	public void setSchool(School school) {
		setColumn(SCHOOL, school);
	}
	
	public void setSchool(Object school) {
		setColumn(SCHOOL, school);
	}
	
	public void setStartDate(Date date) {
		setColumn(START_DATE, date);
	}
	
	public void setEndDate(Date date) {
		setColumn(END_DATE, date);
	}
	
	public void setComment(String comment) {
		setColumn(COMMENT, comment);
	}
	
	public void setCode(String code) {
		setColumn(CODE, code.toUpperCase());
	}
	
	public void setLength(int length) {
		setColumn(LENGTH, length);
	}
	
	public void setNotActive(boolean active) {
		setColumn(NOT_ACTIVE, active);
	}
	
	//Finders
	public Object ejbFindBySeasonAndCode(SchoolSeason season, String code) throws FinderException {
		return ejbFindBySeasonAndCode((IDOEntity) season, code);
	}
	
	public Object ejbFindBySeasonAndCode(Object season, String code) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		query.addCriteria(new MatchCriteria(table, SCHOOL_SEASON, MatchCriteria.EQUALS, season));
		query.addCriteria(new MatchCriteria(table, CODE, MatchCriteria.EQUALS, code));
		
		return idoFindOnePKByQuery(query);
	}

	public Collection ejbFindAllBySeasonAndSchool(SchoolSeason season, School school) throws FinderException {
		return ejbFindAllBySeasonAndSchool((IDOEntity) season, (IDOEntity) school);
	}
	
	public Collection ejbFindAllBySeasonAndSchool(Object season, Object school) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		query.addCriteria(new MatchCriteria(table, SCHOOL_SEASON, MatchCriteria.EQUALS, season));
		query.addCriteria(new MatchCriteria(table, SCHOOL, MatchCriteria.EQUALS, school));
		
		return idoFindPKsByQuery(query);
	}

	public static String getFindAllBySeasonAndStudyPathSchoolQuery(SchoolSeason season, SchoolStudyPath path) {
		return getFindAllBySeasonAndStudyPathSchoolQuery((IDOEntity) season, (IDOEntity) path);
	}
	
	public static String getFindAllBySeasonAndStudyPathSchoolQuery(Object season, Object studyPath) {
		Table table = new Table(AdultEducationCourse.class);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, SCHOOL, true);
		query.addCriteria(new MatchCriteria(table, SCHOOL_SEASON, MatchCriteria.EQUALS, season));
		query.addCriteria(new MatchCriteria(table, STUDY_PATH, MatchCriteria.EQUALS, studyPath));
		
		return query.toString();
	}

	public Collection ejbFindAllBySeasonAndTypeAndSchoolAndStudyPathGroup(SchoolSeason season, SchoolType type, School school, SchoolStudyPathGroup group) throws FinderException {
		return ejbFindAllBySeasonAndTypeAndSchoolAndStudyPathGroup((IDOEntity) season, (IDOEntity) type, (IDOEntity) school, (IDOEntity) group);
	}
	
	public Collection ejbFindAllBySeasonAndTypeAndSchoolAndStudyPathGroup(Object season, Object type, Object school, Object group) throws FinderException {
		Table table = new Table(this);
		Table studyPath = new Table(SchoolStudyPath.class);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		try {
			query.addJoin(table, studyPath);
		}
		catch (IDORelationshipException ire) {
			throw new FinderException(ire.getMessage());
		}
		query.addCriteria(new MatchCriteria(table, SCHOOL_SEASON, MatchCriteria.EQUALS, season));
		query.addCriteria(new MatchCriteria(table, SCHOOL, MatchCriteria.EQUALS, school));
		query.addCriteria(new MatchCriteria(studyPath, "study_path_group_id", MatchCriteria.EQUALS, group));
		if (type != null) {
			query.addCriteria(new MatchCriteria(studyPath, "sch_school_type_id", MatchCriteria.EQUALS, type));
		}
		
		return idoFindPKsByQuery(query);
	}
	
	public Collection ejbFindAllBySchoolAndSeasonAndStudyPathGroupConnectedToChoices(Object school, Object season, Object group, Object[] statuses) throws FinderException {
		Table table = new Table(this);
		Table studyPath = new Table(SchoolStudyPath.class);
		Table choices = new Table(AdultEducationChoice.class);
		Table cases = new Table(Case.class);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		try {
			query.addJoin(table, studyPath);
			query.addJoin(choices, table);
			query.addJoin(choices, cases);
		}
		catch (IDORelationshipException ire) {
			throw new FinderException(ire.getMessage());
		}
		query.addCriteria(new MatchCriteria(table, SCHOOL_SEASON, MatchCriteria.EQUALS, season));
		query.addCriteria(new MatchCriteria(table, SCHOOL, MatchCriteria.EQUALS, school));
		query.addCriteria(new MatchCriteria(studyPath, "study_path_group_id", MatchCriteria.EQUALS, group));
		query.addCriteria(new InCriteria(cases, "case_status", statuses));
		
		return idoFindPKsByQuery(query);
	}

	public Collection ejbFindAllBySeasonAndSchoolAndStudyPath(SchoolSeason season, School school, SchoolStudyPath studyPath) throws FinderException {
		return ejbFindAllBySeasonAndSchoolAndStudyPath((IDOEntity) season, (IDOEntity) school, (IDOEntity) studyPath);
	}
	
	public Collection ejbFindAllBySeasonAndSchoolAndStudyPath(Object season, Object school, Object studyPath) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		query.addCriteria(new MatchCriteria(table, SCHOOL_SEASON, MatchCriteria.EQUALS, season));
		query.addCriteria(new MatchCriteria(table, SCHOOL, MatchCriteria.EQUALS, school));
		query.addCriteria(new MatchCriteria(table, STUDY_PATH, MatchCriteria.EQUALS, studyPath));
		
		return idoFindPKsByQuery(query);
	}
}