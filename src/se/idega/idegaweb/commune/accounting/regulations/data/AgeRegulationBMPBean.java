/*
 * $Id: AgeRegulationBMPBean.java,v 1.2 2003/08/25 20:58:48 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.regulations.data;

import java.util.Collection;
import java.sql.Date;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;

/**
 * Entity bean for age regulation entries.
 * <p>
 * Last modified: $Date: 2003/08/25 20:58:48 $ by $Author: anders $
 *
 * @author <a href="http://www.ncmedia.com">Anders Lindman</a>
 * @version $Revision: 1.2 $
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
		addAttribute(COLUMN_PERIOD_FROM, "From period", true, true, Date.class);
		addAttribute(COLUMN_PERIOD_TO, "To period", true, true, Date.class);
		addAttribute(COLUMN_AGE_FROM, "Person age from", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_AGE_FROM, "Person age to", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_DESCRIPTION, "Description of the age regulation", true, true, java.lang.String.class);
		addAttribute(COLUMN_CUT_DATE, "Cut date", true, true, Date.class);
		setAsPrimaryKey(getIDColumnName(), true);
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

	public String getDescription() {
		return getStringColumnValue(COLUMN_DESCRIPTION);	
	}

	public Date getCutDate() {
		return (Date) getColumnValue(COLUMN_CUT_DATE);	
	}

	public void setPeriodFrom(Date from) { 
		setColumn(COLUMN_PERIOD_FROM, from); 
	}

	public void setPeriodTo(Date to) { 
		setColumn(COLUMN_PERIOD_TO, to); 
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

	/**
	 * Finds all age regulations.
	 * @return collection of all age regulation objects
	 * @throws FinderException
	 */
	public Collection ejbFindAll() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendOrderBy();
		String[] s = {COLUMN_PERIOD_FROM, COLUMN_PERIOD_TO, COLUMN_DESCRIPTION};
		sql.appendCommaDelimited(s);
		return idoFindPKsBySQL(sql.toString());
	}
	
	/**
	 * Finds all age regulations for the specified time period.
	 * @param from the start of the period
	 * @param to the end of the period
	 * @return collection of all age regulation for the specified period
	 * @throws FinderException
	 */
	public Collection ejbFindByPeriod(Date from, Date to) throws FinderException {
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
		String[] s = {COLUMN_PERIOD_FROM, COLUMN_PERIOD_TO, COLUMN_DESCRIPTION};
		sql.appendCommaDelimited(s);
		return idoFindPKsByQuery(sql);
	}		  
}
