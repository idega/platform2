/*
 * $Id: RegulationBMPBean.java,v 1.1 2003/09/02 23:42:55 kjell Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.regulations.data;

import java.sql.Date;

import com.idega.data.GenericEntity;

/**
 * Entity bean for regulation entries.
 * <p>
 * $Id: RegulationBMPBean.java,v 1.1 2003/09/02 23:42:55 kjell Exp $
 *
 * @author <a href="http://www.lindman.se">Kjell Lindman</a>
 * @version$
 */

public class RegulationBMPBean extends GenericEntity implements Regulation {

	private static final String ENTITY_NAME = "cacc_age_regulation";

	private static final String COLUMN_AGE_REGULATION_ID = "age_regulation_id";
	private static final String COLUMN_PERIOD_FROM = "period_from";
	private static final String COLUMN_PERIOD_TO = "period_to";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_AMOUNT = "amount";
	private static final String COLUMN_MAIN_ACTIVITY_ID = "flow_type_id";
	private static final String COLUMN_PAYMENT_FLOW_TYPE_ID = "flow_type_id";
	private static final String COLUMN_REG_SPEC_TYPE_ID = "reg_spec_type_id";
	private static final String COLUMN_CONDITION_TYPE_ID = "condition_type";
	private static final String COLUMN_CONDITION_ORDER = "condition_order";
	private static final String COLUMN_CONDITION_INTERVAL_ID = "condition_inteval";
	
	
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
		addAttribute(COLUMN_NAME, "Name", true, true, java.lang.String.class);
		addAttribute(COLUMN_AMOUNT, "Amount", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_MAIN_ACTIVITY_ID, "Main activity ID", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_PAYMENT_FLOW_TYPE_ID, "Flow type relation", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_REG_SPEC_TYPE_ID, "Regulation specification type relation", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_CONDITION_TYPE_ID, "Condition type relation", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_CONDITION_ORDER, "Condition order", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_CONDITION_INTERVAL_ID, "Condition interval relation", true, true, java.lang.Integer.class);
		
		setAsPrimaryKey(getIDColumnName(), true);
	}

	public Date getPeriodFrom() {
		return (Date) getColumnValue(COLUMN_PERIOD_FROM);	
	}

	public Date getPeriodTo() {
		return (Date) getColumnValue(COLUMN_PERIOD_TO);	
	}

	
	public void setPeriodFrom(Date from) { 
		setColumn(COLUMN_PERIOD_FROM, from); 
	}

	public void setPeriodTo(Date to) { 
		setColumn(COLUMN_PERIOD_TO, to); 
	}

			  
}
