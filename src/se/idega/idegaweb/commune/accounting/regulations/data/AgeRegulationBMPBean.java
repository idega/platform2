/*
 * $Id: AgeRegulationBMPBean.java,v 1.11 2004/01/05 13:01:24 kjell Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.regulations.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.school.data.SchoolCategory;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.util.CalendarMonth;

/**
 * Entity bean for age regulation entries.
 * <p>
 * Last modified: $Date: 2004/01/05 13:01:24 $ by $Author: kjell $
 *
 * @author <a href="http://www.ncmedia.com">Anders Lindman</a>
 * @version $Revision: 1.11 $
 */
public class AgeRegulationBMPBean extends GenericEntity implements AgeRegulation {

	private static final String ENTITY_NAME = "cacc_age_regulation";

	private static final String COLUMN_AGE_REGULATION_ID = "age_regulation_id";
	private static final String COLUMN_PERIOD_FROM = "period_from";
	private static final String COLUMN_PERIOD_TO = "period_to";
	private static final String COLUMN_AGE_FROM = "age_from";
	private static final String COLUMN_AGE_TO = "age_to";
	private static final String COLUMN_DESCRIPTION = "description";
	private static final String COLUMN_CUT_DATE = "cut_date";
	private static final String COLUMN_CATEGORY = "category";
	
	/**
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}
	
	/**
	 * @see com.idega.data.GenericEntity#getIdColumnName()
	 */
	public String getIDColumnName() {
		return COLUMN_AGE_REGULATION_ID;
	}

	/**
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		setAsPrimaryKey(getIDColumnName(), true);
		addAttribute(COLUMN_PERIOD_FROM, "From period", true, true, Date.class);
		addAttribute(COLUMN_PERIOD_TO, "To period", true, true, Date.class);
		addAttribute(COLUMN_AGE_FROM, "Person age from", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_AGE_TO, "Person age to", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_DESCRIPTION, "Description of the age regulation", true, true, java.lang.String.class);
		addAttribute(COLUMN_CUT_DATE, "Cut date", true, true, Date.class);
		addAttribute(COLUMN_CATEGORY, "Operational field (school category) (foreign key)", true, true, 
				String.class, "many-to-one", SchoolCategory.class);
	}

	public Date getPeriodFrom() {
		return (Date) getColumnValue(COLUMN_PERIOD_FROM);	
	}

	public Date getPeriodTo() {
		return (Date) getColumnValue(COLUMN_PERIOD_TO);	
	}

	public int getAgeFrom() {
		return getIntColumnValue(COLUMN_AGE_FROM);	
	}

	public int getAgeTo() {
		return getIntColumnValue(COLUMN_AGE_TO);	
	}

	public String getAgeInterval() {
		return "" + getAgeFrom() + " - " + getAgeTo();
	}

	public String getDescription() {
		return getStringColumnValue(COLUMN_DESCRIPTION);	
	}

	public Date getCutDate() {
		return (Date) getColumnValue(COLUMN_CUT_DATE);	
	}
	
	public String getCategory() {
		return getStringColumnValue(COLUMN_CATEGORY);	
	}

	public void setPeriodFrom(Date from) { 
		setColumn(COLUMN_PERIOD_FROM, from); 
	}

	public void setPeriodTo(Date to) { 
		CalendarMonth month = new CalendarMonth(to);
		setColumn(COLUMN_PERIOD_TO, month.getLastDateOfMonth()); 
	}

	public void setAgeFrom(int age) { 
		setColumn(COLUMN_AGE_FROM, age); 
	}

	public void setAgeTo(int age) { 
		setColumn(COLUMN_AGE_TO, age); 
	}

	public void setDescription(String description) { 
		setColumn(COLUMN_DESCRIPTION, description); 
	}

	public void setCutDate(Date date) { 
		setColumn(COLUMN_CUT_DATE, date); 
	}

	public void setCategory(String category) { 
		setColumn(COLUMN_CATEGORY, category); 
	}

	/**
	 * Finds all age regulations.
	 * @return collection of all age regulation objects
	 * @throws FinderException
	 */
	public Collection ejbFindAll() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendOrderBy();
		String[] s = {COLUMN_PERIOD_FROM + " desc", COLUMN_AGE_FROM, COLUMN_PERIOD_TO + " desc"};
		sql.appendCommaDelimited(s);
		return idoFindPKsBySQL(sql.toString());
	}

	/**
	 * Finds all age regulations for the specified category (operational field).
	 * @param category the category id
	 * @return collection of all age regulation objects
	 * @throws FinderException
	 */
	public Collection ejbFindByCategory(String category) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEqualsQuoted(COLUMN_CATEGORY, category);
		sql.appendOrderBy();
		String[] s = {COLUMN_PERIOD_FROM + " desc", COLUMN_AGE_FROM, COLUMN_PERIOD_TO + " desc"};
		sql.appendCommaDelimited(s);
		return idoFindPKsBySQL(sql.toString());
	}
	
	/**
	 * Finds all age regulations for the specified time period and category (operational field).
	 * @param from the start of the period
	 * @param to the end of the period
	 * @param category the category id
	 * @return collection of all age regulation for the specified period
	 * @throws FinderException
	 */
	public Collection ejbFindByPeriodAndCategory(Date from, Date to, String category) throws FinderException {
		to = getEndOfMonth(to);
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEqualsQuoted(COLUMN_CATEGORY, category);
		if (from != null) {
			sql.appendAnd();
			sql.append(COLUMN_PERIOD_FROM);
			sql.appendGreaterThanOrEqualsSign();
			sql.append("'" + from + "'");
			if (to != null) {
				sql.appendAnd();
				sql.append(COLUMN_PERIOD_FROM);
				sql.appendLessThanOrEqualsSign();		
				sql.append("'" + to + "'");
			}
		} else if (to != null) {
			sql.appendAnd();
			sql.append(COLUMN_PERIOD_FROM);
			sql.appendLessThanOrEqualsSign();		
			sql.append("'" + to + "'");
		}
		sql.appendOrderBy();
		String[] s = {COLUMN_PERIOD_FROM + " desc", COLUMN_AGE_FROM, COLUMN_PERIOD_TO + " desc"};
		sql.appendCommaDelimited(s);
		return idoFindPKsByQuery(sql);
	}		  
	
	/**
	 * Finds all age regulations for the specified time period.
	 * @param from the start of the period
	 * @param to the end of the period
	 * @return collection of all age regulation for the specified period
	 * @throws FinderException
	 */
	public Collection ejbFindByPeriod(Date from, Date to) throws FinderException {
		to = getEndOfMonth(to);
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		if (from != null) {
			sql.appendWhere(COLUMN_PERIOD_FROM);
			sql.appendGreaterThanOrEqualsSign();
			sql.append("'" + from + "'");
			if (to != null) {
				sql.appendAnd();
				sql.append(COLUMN_PERIOD_FROM);
				sql.appendLessThanOrEqualsSign();		
				sql.append("'" + to + "'");
			}
		} else if (to != null) {
			sql.appendWhere(COLUMN_PERIOD_FROM);
			sql.appendLessThanOrEqualsSign();		
			sql.append("'" + to + "'");
		}
		sql.appendOrderBy();
		String[] s = {COLUMN_PERIOD_FROM + " desc", COLUMN_AGE_FROM, COLUMN_PERIOD_TO + " desc"};
		sql.appendCommaDelimited(s);
		return idoFindPKsByQuery(sql);
	}		  

	/*
	 * This is a fix to always make sure the last date in the (to) month is covered
	 * See nacp377 
	 */
	private Date getEndOfMonth(Date date) {
		CalendarMonth fixedDate = new CalendarMonth(date);
		return fixedDate.getLastDateOfMonth();
	}	
}
