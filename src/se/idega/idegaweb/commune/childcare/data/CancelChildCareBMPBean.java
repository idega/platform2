/*
 * $Id:$
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.childcare.data;

import com.idega.block.process.data.AbstractCaseBMPBean;
import com.idega.block.process.data.Case;

import se.idega.idegaweb.commune.childcare.check.data.Check;

import java.sql.Date;

/**
 * This class does something very clever.....
 * 
 * @author <a href="palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class CancelChildCareBMPBean extends AbstractCaseBMPBean implements CancelChildCare, Case {
	private final static String ENTITY_NAME = "comm_cancel_care";
	private final static String CASE_CODE_KEY = "MBANKOO";
	private final static String CASE_CODE_KEY_DESC = "Cancel child care contract";

	protected final static String REASON = "reason";
	protected final static String CHECK_ID = "check_id";
	protected final static String CANCELLATION_DATE = "cancellation_date";

	/**
	 * @see com.idega.block.process.data.AbstractCaseBMPBean#getCaseCodeKey()
	 */
	public String getCaseCodeKey() {
		return CASE_CODE_KEY;
	}

	/**
	 * @see com.idega.block.process.data.AbstractCaseBMPBean#getCaseCodeDescription()
	 */
	public String getCaseCodeDescription() {
		return CASE_CODE_KEY_DESC;
	}

	/**
	 * @see com.idega.data.IDOLegacyEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/**
	 * @see com.idega.data.IDOLegacyEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(REASON,"",true,true,java.lang.String.class,1000);
		addAttribute(CANCELLATION_DATE,"",true,true,java.sql.Date.class);
		
		addManyToOneRelationship(CHECK_ID,Check.class);		
	}

	public String getReason() {
		return getStringColumnValue(REASON);	
	}

	public Date getCancellationDate() {
		return (Date)getColumnValue(CANCELLATION_DATE);	
	}

	public int getCheckId() {
		return getIntColumnValue(CHECK_ID);	
	}
	
	public Check getCheck() {
		return (Check)getColumnValue(CHECK_ID);	
	}

	public void setReason(String reason) {
		setColumn(REASON,reason);	
	}

	public void setCancellationDate(Date date) {
		setColumn(CANCELLATION_DATE,date);	
	}

	public void setCheckId(int checkId) {
		setColumn(CHECK_ID,checkId);	
	}
	
	public void setCheck(Check check) {
		setColumn(CHECK_ID,check);	
	}	
}