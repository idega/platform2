/*
 * $Id: ChildCareQueueBMPBean.java,v 1.11 2003/05/22 20:07:11 palli Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.childcare.data;

import java.sql.Date;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.ejb.FinderException;

import com.idega.block.process.data.AbstractCaseBMPBean;
import com.idega.block.process.data.Case;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolArea;
import com.idega.data.IDOException;
import com.idega.data.IDOQuery;
import com.idega.user.data.User;

/**
 * Data bean holding the information about the children waiting in line to get a childcare position
 * This data is just an import of the data used in the old system where there was no limit on 
 * how many child care option one can sign up for. The system now only allows maximum five options 
 * per child.
 * 
 * @author Joakim
 * @version 1.0
 */
public class ChildCareQueueBMPBean extends AbstractCaseBMPBean 
	implements ChildCareQueue,Case {
	private final static String ENTITY_NAME = "comm_childcare_queue";
	private final static String CASE_CODE_KEY = "MBANBOP";
	private final static String CASE_CODE_KEY_DESC = "Queue for child care";
	
	protected final static String CONTRACT_ID = "contract_id";
	protected final static String CHILD_ID = "child_id";
	protected final static String PROVIDER_NAME = "provider_name";
	protected final static String PROVIDER_ID = "provider_id";
	protected final static String PRIORITY = "priority";
	protected final static String CHOICE_NUMBER = "choice_number";
	protected final static String SCHOOL_AREA_NAME = "school_area_name";
	protected final static String SCHOOL_AREA_ID = "school_area_id";
	protected final static String QUEUE_DATE = "queue_date";
	protected final static String START_DATE = "start_date";
	protected final static String IMPORT_DATE = "import_date";
	protected final static String QUEUE_TYPE = "queue_type";
	protected final static String EXPORTED = "exported";

	protected final int SORT_DATE_OF_BIRTH = 1;
	protected final int SORT_QUEUE_DATE = 2;
	
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
		addAttribute(CONTRACT_ID,"",true,true,java.lang.Integer.class);
		addAttribute(CHILD_ID,"",true,true,java.lang.Integer.class,MANY_TO_ONE,User.class);
		addAttribute(PROVIDER_NAME,"",true,true,java.lang.String.class,1000);
		addAttribute(PROVIDER_ID,"",true,true,java.lang.Integer.class,MANY_TO_ONE,School.class);
		addAttribute(PRIORITY,"",true,true,java.lang.String.class,1000);
		addAttribute(CHOICE_NUMBER,"",true,true,java.lang.Integer.class);
		addAttribute(SCHOOL_AREA_NAME,"",true,true,java.lang.String.class,1000);
		addAttribute(SCHOOL_AREA_ID,"",true,true,java.lang.Integer.class,MANY_TO_ONE,SchoolArea.class);
		addAttribute(QUEUE_DATE,"",true,true,java.sql.Date.class);
		addAttribute(START_DATE,"",true,true,java.sql.Date.class);
		addAttribute(IMPORT_DATE,"",true,true,java.sql.Date.class);
		addAttribute(QUEUE_TYPE,"",true,true,java.lang.Integer.class);
		addAttribute(EXPORTED,"",true,true,java.lang.Boolean.class);

		addManyToOneRelationship(CHILD_ID,User.class);
	}
	
	public int getContractId() {
		return getIntColumnValue(CONTRACT_ID);	
	}

	public User getChild() {
		return (User)getColumnValue(CHILD_ID);	
	}
	
	public int getChildId() {
		return getIntColumnValue(CHILD_ID);	
	}

	public String getProviderName() {
		return getStringColumnValue(PROVIDER_NAME);			
	}

	public int getProviderId() {
		return getIntColumnValue(PROVIDER_ID);			
	}

	public School getProvider() {
		return (School) getColumnValue(PROVIDER_ID);			
	}

	public String getPriority() {
		return getStringColumnValue(PRIORITY);			
	}

	public int getChoiceNumber() {
		return getIntColumnValue(CHOICE_NUMBER);
	}
	
	public String getSchoolAreaName() {
		return getStringColumnValue(SCHOOL_AREA_NAME);
	}
	
	public String getSchoolAreaId() {
		return getStringColumnValue(SCHOOL_AREA_ID);
	}
	
	public Date getQueueDate() {
		return (Date)getColumnValue(QUEUE_DATE);	
	}
	
	public Date getStartDate() {
		return (Date)getColumnValue(START_DATE);	
	}
	
	public Date getImportDate() {
		return (Date)getColumnValue(IMPORT_DATE);	
	}
	
	public int getQueueType() {
		return getIntColumnValue(QUEUE_TYPE);
	}
	
	public boolean isExported() {
		return getBooleanColumnValue(EXPORTED, false);
	}
	

	public void setContractId(int id) {
		setColumn(CONTRACT_ID,id);	
	}

	public void setChildId(int id) {
		setColumn(CHILD_ID,id);	
	}

	public void setProviderName(String name) {
		setColumn(PROVIDER_NAME,name);
	}
	
	public void setProviderId(int id) {
		setColumn(PROVIDER_ID,id);
	}
	
	public void setPriority(String priority) {
		setColumn(PRIORITY,priority);
	}
	
	public void setChoiceNumber(int number) {
		setColumn(CHOICE_NUMBER,number);	
	}
	
	public void setSchoolAreaName(String area) {
		setColumn(SCHOOL_AREA_NAME,area);	
	}
	
	public void setSchoolAreaId(int id) {
		setColumn(SCHOOL_AREA_ID,id);	
	}
	
	public void setQueueDate(Date date) {
		setColumn(QUEUE_DATE,date);	
	}

	public void setStartDate(Date sDate) {
		setColumn(START_DATE,sDate);	
	}

	public void setImportedDate(Date iDate) {
		setColumn(IMPORT_DATE,iDate);	
	}

	public void setQueueType(int type) {
		setColumn(QUEUE_TYPE,type);	
	}
	
	public void setExported(boolean exp) {
		setColumn(EXPORTED,exp);	
	}
	
	public Integer ejbFindQueueByChildAndChoiceNumber(User child, int choiceNumber) throws FinderException {
		return ejbFindQueueByChildAndChoiceNumber(((Integer)child.getPrimaryKey()).intValue(), choiceNumber);
	}

	public Integer ejbFindQueueByChildAndChoiceNumber(int childID, int choiceNumber) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(CHOICE_NUMBER, choiceNumber).appendAndEquals(CHILD_ID,childID);
		return (Integer) idoFindOnePKByQuery(sql);
	}
	
	public Integer ejbFindQueueByChildChoiceNumberAndQueueType(int childID, int choiceNumber, int queueType) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(CHOICE_NUMBER, choiceNumber).
		appendAndEquals(CHILD_ID,childID).appendAndEquals(QUEUE_TYPE,queueType);
		return (Integer) idoFindOnePKByQuery(sql);
	}
	
	public Collection ejbFindQueueByChild(int childID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(CHILD_ID,childID);
		sql.appendOrderBy(CHOICE_NUMBER);
		return super.idoFindPKsByQuery(sql);
	}
	
	public Collection ejbFindQueueByProviderAndDate(int providerID, Date queueDate) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(PROVIDER_ID, providerID);
		sql.appendAndEquals(QUEUE_DATE,queueDate);
		return super.idoFindPKsByQuery(sql);
	}
	
	public int ejbHomeGetNumberInQueue(int providerID, Date queueDate) throws IDOException {
		IDOQuery sql = idoQuery();
		sql.appendSelectCountFrom(this).appendWhereEquals(PROVIDER_ID, providerID);
		sql.appendAnd().append(QUEUE_DATE).appendLessThanSign().append(queueDate);
		return super.idoGetNumberOfRecords(sql);
	}
	
	public int ejbHomeGetNumberOfNotExported(int childID) throws IDOException {
		IDOQuery sql = idoQuery();
		sql.appendSelectCountFrom(this).appendWhereEquals(CHILD_ID, childID);
		sql.appendAndEqualsQuoted(EXPORTED, "N");
		return super.idoGetNumberOfRecords(sql);
	}
	
	public int ejbHomeGetTotalCount(String[] queueType, boolean exported) throws IDOException {
		IDOQuery sql = idoQuery();
		sql.appendSelect().append("count(distinct ").append(CHILD_ID).append(") ").appendFrom().append(getEntityName());
		boolean whereAdded = false;
		if (queueType != null) {
			sql.appendWhere().append(QUEUE_TYPE).appendInArrayWithSingleQuotes(queueType);
			whereAdded = true;
		}
		if (exported) {
			if (!whereAdded)
				sql.appendWhereEquals(EXPORTED, "'Y'");
			else
				sql.appendAndEquals(EXPORTED, "'Y'");
		}
		return super.idoGetNumberOfRecords(sql);
	}
	
	public Integer[] ejbHomeGetDistinctNotExportedChildIds() throws IDOException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendAndEqualsQuoted(EXPORTED, "N");
		
		Collection col = null;
		
		try {
			 col = super.idoFindPKsByQuery(sql);
		}
		catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
		
		
		HashMap childIds = new HashMap();
		if (col != null) {
			for (Iterator iter = col.iterator();iter.hasNext();) {
				ChildCareQueue q = (ChildCareQueue)iter.next();
				childIds.put(new Integer(q.getChildId()),null);
			}
		}
		
		Set keys = childIds.keySet();
		if (keys != null)
			return (Integer[])childIds.keySet().toArray();
		else
			return null;
	}
}