package com.idega.block.school.data;

import java.sql.Date;
import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.GenericEntity;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;
import com.idega.util.IWTimestamp;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author <br>
 *         <a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */
public class SchoolSeasonBMPBean extends GenericEntity implements SchoolSeason {

	public final static String SCHOOLSEASON = "sch_school_season";
	public final static String START = "season_start";
	public final static String END = "season_end";
	public final static String DUE_DATE = "due_date";
	public final static String START_DATE = "start_date";
	public final static String SCHOOL_CATEGORY = "school_category";
	public final static String NAME = "name";

	public String getEntityName() {
		return SCHOOLSEASON;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(NAME, "name", true, true, String.class);
		addAttribute(START, "Start", true, true, Date.class);
		addAttribute(END, "End", true, true, Date.class);
		addAttribute(START_DATE, "Choice start date", Date.class);
		addAttribute(DUE_DATE, "Choice end date", Date.class);
		
		addManyToOneRelationship(SCHOOL_CATEGORY, SchoolCategory.class);
	}

	public String getName() {
		return getSchoolSeasonName();
	}

	public String getSchoolSeasonName() {
		return getStringColumnValue(NAME);
	}

	public void setSchoolSeasonName(String name) {
		setColumn(NAME, name);
	}

	public Date getSchoolSeasonEnd() {
		return (Date) getColumnValue(END);
	}

	public void setSchoolSeasonEnd(java.util.Date end) {
		setColumn(END, end);
	}

	public Date getSchoolSeasonStart() {
		return (Date) getColumnValue(START);
	}

	public void setSchoolSeasonStart(java.util.Date start) {
		setColumn(START, start);
	}

	public Date getChoiceEndDate() {
		return (Date) getColumnValue(DUE_DATE);
	}

	public void setChoiceEndDate(java.util.Date due) {
		setColumn(DUE_DATE, due);
	}

	public Date getChoiceStartDate() {
		return (Date) getColumnValue(DUE_DATE);
	}

	public void setChoiceStartDate(java.util.Date due) {
		setColumn(START_DATE, due);
	}

	public SchoolCategory getSchoolCategory() {
		return (SchoolCategory) getColumnValue(SCHOOL_CATEGORY);
	}

	public String getSchoolCategoryPK() {
		return getStringColumnValue(SCHOOL_CATEGORY);
	}

	public void setSchoolCategory(SchoolCategory category) {
		setColumn(SCHOOL_CATEGORY, category);
	}

	public void setSchoolCategory(Object categoryPK) {
		setColumn(SCHOOL_CATEGORY, categoryPK);
	}

	public java.util.Collection ejbFindAllSchoolSeasons() throws FinderException {
		return ejbFindAllSchoolSeasons(null);
	}
	
	public java.util.Collection ejbFindAllSchoolSeasonsWithoutCategory() throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn());
		query.addCriteria(new MatchCriteria(table.getColumn(SCHOOL_CATEGORY), false));
		query.addOrder(table, START, true);
		
		return idoFindPKsByQuery(query);
	}
	
	public java.util.Collection ejbFindAllSchoolSeasons(SchoolCategory category) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn());
		if (category != null) {
			query.addCriteria(new MatchCriteria(table, SCHOOL_CATEGORY, MatchCriteria.EQUALS, category));
		}
		query.addOrder(table, START, true);
		
		return idoFindPKsByQuery(query);
	}

	/**
	 * Gets the previous season to this one.
	 * 
	 * @throws FinderException
	 *           if none is found
	 */
	public SchoolSeason getPreviousSeason() throws FinderException {
		return getSchoolSeasonHome().findPreviousSchoolSeason(this);
	}

	protected SchoolSeasonHome getSchoolSeasonHome() {
		return (SchoolSeasonHome) getEJBLocalHome();
	}

	/**
	 * Find all schoolseasons that start before SchoolSeason schoolSeason and
	 * order by start date
	 * 
	 * @param schoolSeason
	 * @return
	 * @throws FinderException
	 *           If an error occured during the search
	 */
	public java.util.Collection ejbFindAllPreviousSchoolSeasons(SchoolSeason schoolSeason) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn());
		query.addCriteria(new MatchCriteria(table, SCHOOL_CATEGORY, MatchCriteria.EQUALS, schoolSeason.getSchoolCategoryPK()));
		query.addCriteria(new MatchCriteria(table, START, MatchCriteria.LESS, schoolSeason.getSchoolSeasonStart()));
		query.addOrder(table, START, true);
		
		return idoFindPKsByQuery(query);
	}

	public Integer ejbFindPreviousSchoolSeason(SchoolSeason schoolSeason) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn());
		query.addCriteria(new MatchCriteria(table, SCHOOL_CATEGORY, MatchCriteria.EQUALS, schoolSeason.getSchoolCategoryPK()));
		query.addCriteria(new MatchCriteria(table, START, MatchCriteria.LESS, schoolSeason.getSchoolSeasonStart()));
		query.addOrder(table, START, false);
		
		return (Integer) idoFindOnePKByQuery(query);
	}

	public Integer ejbFindSeasonByDate(SchoolCategory category, Date date) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn());
		query.addCriteria(new MatchCriteria(table, SCHOOL_CATEGORY, MatchCriteria.EQUALS, category));
		query.addCriteria(new MatchCriteria(table, START, MatchCriteria.LESSEQUAL, date));
		query.addCriteria(new MatchCriteria(table, END, MatchCriteria.GREATEREQUAL, date));
		query.addOrder(table, START, true);
		
		return (Integer) idoFindOnePKByQuery(query);
	}

	public Collection ejbFindPendingSeasonsByDate(SchoolCategory category, Date date) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn());
		query.addCriteria(new MatchCriteria(table, SCHOOL_CATEGORY, MatchCriteria.EQUALS, category));
		query.addCriteria(new MatchCriteria(table, START, MatchCriteria.GREATER, date));
		query.addOrder(table, START, true);
		
		return (Collection) idoFindPKsByQuery(query);
	}

	public Integer ejbFindCurrentSeason(SchoolCategory category) throws FinderException {
		return ejbFindSeasonByDate(category, new IWTimestamp().getDate());
	}

	public Collection ejbFindSchoolSeasonsActiveInTimePeriod(SchoolCategory category, Date firstDateInPeriod, Date lastDateInPeriod)
			throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn());
		query.addCriteria(new MatchCriteria(table, SCHOOL_CATEGORY, MatchCriteria.EQUALS, category));
		query.addCriteria(new MatchCriteria(table, START, MatchCriteria.LESSEQUAL, firstDateInPeriod));
		query.addCriteria(new MatchCriteria(table, END, MatchCriteria.GREATEREQUAL, firstDateInPeriod));
		query.addOrder(table, START, true);
		
		return idoFindPKsByQuery(query);
	}

	public Integer ejbFindNextSeason(SchoolSeason currentSeason) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn());
		query.addCriteria(new MatchCriteria(table, SCHOOL_CATEGORY, MatchCriteria.EQUALS, currentSeason.getSchoolCategoryPK()));
		query.addCriteria(new MatchCriteria(table, START, MatchCriteria.GREATEREQUAL, currentSeason.getSchoolSeasonEnd()));
		query.addOrder(table, START, true);
		
		return (Integer) idoFindOnePKByQuery(query);
	}

	public Integer ejbFindNextSeason(SchoolCategory category, Date date) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn());
		query.addCriteria(new MatchCriteria(table, SCHOOL_CATEGORY, MatchCriteria.EQUALS, category));
		query.addCriteria(new MatchCriteria(table, START, MatchCriteria.GREATEREQUAL, date));
		query.addOrder(table, START, true);
		
		return (Integer) idoFindOnePKByQuery(query);
	}
}