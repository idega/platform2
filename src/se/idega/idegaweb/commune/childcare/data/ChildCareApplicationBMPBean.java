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
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolType;
import com.idega.user.data.User;

import se.idega.idegaweb.commune.childcare.check.data.Check;

import java.sql.Date;

/**
 * This class does something very clever.....
 * 
 * @author palli
 * @version 1.0
 */
public class ChildCareApplicationBMPBean extends AbstractCaseBMPBean implements ChildCareApplication, Case {
	private final static String ENTITY_NAME = "comm_childcare";
	private final static String CASE_CODE_KEY = "MBANBOP";
	private final static String CASE_CODE_KEY_DESC = "Application for child care";
	
	protected final static String CHILDREN_CARE_TYPE = "child_care_type";
	protected final static String PROVIDER_ID = "provider_id";
	protected final static String FROM_DATE = "from_date";
	protected final static String CHILD_ID = "child_id";
	protected final static String PARENTS_AGREE = "parents_agree";
	protected final static String QUEUE_DATE = "queue_date";
	protected final static String METHOD = "method";
	protected final static String CARE_TIME = "care_time";
	protected final static String CHOICE_NUMBER = "choice_number";
	protected final static String CHECK_ID = "check_id";

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
		addAttribute(FROM_DATE,"",true,true,java.sql.Date.class);
		addAttribute(PARENTS_AGREE,"",true,true,java.lang.Boolean.class);
		addAttribute(QUEUE_DATE,"",true,true,java.sql.Date.class);
		addAttribute(METHOD,"",true,true,java.lang.Integer.class);
		addAttribute(CARE_TIME,"",true,true,java.lang.Integer.class);
		addAttribute(CHOICE_NUMBER,"",true,true,java.lang.Integer.class);
		
		addManyToOneRelationship(CHILDREN_CARE_TYPE,SchoolType.class);
		addManyToOneRelationship(PROVIDER_ID,School.class);
		addManyToOneRelationship(CHILD_ID,User.class);
		addManyToOneRelationship(CHECK_ID,Check.class);
	}
	
	public int getChildrenCareTypeId() {
		return getIntColumnValue(CHILDREN_CARE_TYPE);	
	}
	
	public SchoolType getChildrenCareType() {
		return (SchoolType)getColumnValue(CHILDREN_CARE_TYPE);	
	}
	
	public int getProviderId() {
		return getIntColumnValue(PROVIDER_ID);
	}
	
	public School getProvider() {
		return (School)getColumnValue(PROVIDER_ID);			
	}
	
	public Date getFromDate() {
		return (Date)getColumnValue(FROM_DATE);
	}
	
	public int getChildId() {
		return getIntColumnValue(CHILD_ID);	
	}
	
	public User getChild() {
		return (User) getColumnValue(CHILD_ID);	
	}
	
	public boolean getParentsAgree() {
		return getBooleanColumnValue(PARENTS_AGREE);	
	}
	
	public Date getQueueDate() {
		return (Date)getColumnValue(QUEUE_DATE);	
	}
	
	public int getMethod() {
		return getIntColumnValue(METHOD);	
	}
	
	public int getCareTime() {
		return getIntColumnValue(CARE_TIME);
	}
	
	public int getChoiceNumber() {
		return getIntColumnValue(CHOICE_NUMBER);	
	}
	
	public int getCheckId() {
		return getIntColumnValue(CHECK_ID);	
	}
	
	public Check getCheck() {
		return (Check)getColumnValue(CHECK_ID);	
	}
		
	public void setChildrenCareTypeId(int type) {
		setColumn(CHILDREN_CARE_TYPE,type);	
	}

	public void setChildrenCareType(SchoolType type) {
		setColumn(CHILDREN_CARE_TYPE,type);	
	}
	
	public void setProviderId(int id) {
		setColumn(PROVIDER_ID,id);
	}

	public void setProvider(School provider) {
		setColumn(PROVIDER_ID,provider);
	}
	
	public void setFromDate(Date date) {
		setColumn(FROM_DATE,date);
	}
	
	public void setChildId(int id) {
		setColumn(CHILD_ID,id);	
	}

	public void setChild(User child) {
		setColumn(CHILD_ID,child);	
	}
	
	public void setParentsAgree(boolean agree) {
		setColumn(PARENTS_AGREE,agree);	
	}
	
	public void setQueueDate(Date date) {
		setColumn(QUEUE_DATE,date);	
	}
	
	public void setMethod(int method) {
		setColumn(METHOD,method);	
	}
	
	public void setCareTime(int careTime) {
		setColumn(CARE_TIME,careTime);	
	}
	
	public void setChoiceNumber(int number) {
		setColumn(CHOICE_NUMBER,number);	
	}
	
	public void setCheckId(int checkId) {
		setColumn(CHECK_ID,checkId);	
	}
	
	public void setCheck(Check check) {
		setColumn(CHECK_ID,check);	
	}	
}