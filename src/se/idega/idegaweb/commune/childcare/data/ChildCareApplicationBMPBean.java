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

import com.idega.block.contract.data.Contract;
import com.idega.block.process.data.AbstractCaseBMPBean;
import com.idega.block.process.data.Case;
import com.idega.block.process.data.CaseStatus;
import com.idega.block.school.data.School;
import com.idega.core.data.ICFile;
import com.idega.data.IDOException;
import com.idega.data.IDOQuery;
import com.idega.user.data.User;

import se.idega.idegaweb.commune.childcare.check.data.Check;
import se.idega.idegaweb.commune.childcare.check.data.GrantedCheck;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

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
	
	protected final static String PROVIDER_ID = "provider_id";
	protected final static String FROM_DATE = "from_date";
	protected final static String CHILD_ID = "child_id";
	protected final static String QUEUE_DATE = "queue_date";
	protected final static String METHOD = "method";
	protected final static String CARE_TIME = "care_time";
	protected final static String CHOICE_NUMBER = "choice_number";
	protected final static String CHECK_ID = "check_id";
	protected final static String CONTRACT_ID = "contract_id";
	protected final static String CONTRACT_FILE_ID = "contract_file_id";
	protected final static String REJECTION_DATE = "rejection_date";
	protected final static String PROGNOSIS = "prognosis";
	protected final static String PRESENTATION = "presentation";
	protected final static String MESSAGE = "message";
	protected final static String QUEUE_ORDER = "queue_order";
	protected final static String APPLICATION_STATUS = "application_status";
	protected final static String HAS_PRIORITY = "has_priority";
	
	protected final int SORT_DATE_OF_BIRTH = 1;
	protected final int SORT_QUEUE_DATE = 2;
	protected final int SORT_PLACEMENT_DATE = 3;
	
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
		addAttribute(QUEUE_DATE,"",true,true,java.sql.Date.class);
		addAttribute(METHOD,"",true,true,java.lang.Integer.class);
		addAttribute(CARE_TIME,"",true,true,java.lang.Integer.class);
		addAttribute(CHOICE_NUMBER,"",true,true,java.lang.Integer.class);
		addAttribute(REJECTION_DATE,"",true,true,java.sql.Date.class);
		addAttribute(PROGNOSIS,"",true,true,java.lang.String.class,1000);
		addAttribute(PRESENTATION,"",true,true,java.lang.String.class,1000);
		addAttribute(MESSAGE,"",true,true,java.lang.String.class,1000);
		addAttribute(QUEUE_ORDER,"",true,true,java.lang.Integer.class);
		addAttribute(APPLICATION_STATUS,"",true,true,java.lang.String.class,1);
		addAttribute(HAS_PRIORITY,"",true,true,java.lang.Boolean.class);
		
		addManyToOneRelationship(PROVIDER_ID,School.class);
		addManyToOneRelationship(CHILD_ID,User.class);
		addManyToOneRelationship(CHECK_ID,GrantedCheck.class);
		addManyToOneRelationship(CONTRACT_ID,Contract.class);
		addManyToOneRelationship(CONTRACT_FILE_ID,ICFile.class);
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
	
	public GrantedCheck getCheck() {
		return (GrantedCheck)getColumnValue(CHECK_ID);	
	}
		
	public Date getRejectionDate() {
		return (Date)getColumnValue(REJECTION_DATE);	
	}
	
	public int getContractId() {
		return getIntColumnValue(CONTRACT_ID);	
	}

	public Contract getContract() {
		return (Contract)getColumnValue(CONTRACT_ID);	
	}
	
	public int getContractFileId() {
		return getIntColumnValue(CONTRACT_FILE_ID);	
	}
	
	public String getPrognosis() {
		return getStringColumnValue(PROGNOSIS);	
	}

	public String getPresentation() {
		return getStringColumnValue(PRESENTATION);	
	}
	
	public String getMessage() {
		return getStringColumnValue(MESSAGE);	
	}
	
	public int getQueueOrder() {
		return getIntColumnValue(QUEUE_ORDER);	
	}
	
	public char getApplicationStatus() {
		String status = this.getStringColumnValue(APPLICATION_STATUS);
		if (status != null)
			return status.charAt(0);
		else
			return 'A';
	}
	
	public boolean getHasPriority() {
		return getBooleanColumnValue(HAS_PRIORITY, false);
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

	public void setRejectionDate(Date date) {
		setColumn(REJECTION_DATE,date);	
	}

	public void setContractId(int id) {
		setColumn(CONTRACT_ID,id);	
	}

	public void setContractFileId(int id) {
		setColumn(CONTRACT_FILE_ID,id);	
	}

	public void setPrognosis(String prognosis) {
		setColumn(PROGNOSIS,prognosis);	
	}

	public void setPresentation(String presentation) {
		setColumn(PRESENTATION,presentation);	
	}
	
	public void setMessage(String message) {
		setColumn(MESSAGE,message);	
	}
	
	public void setQueueOrder(int order) {
		setColumn(QUEUE_ORDER,order);	
	}
	
	public void setApplicationStatus(char status) {
		setColumn(APPLICATION_STATUS,String.valueOf(status));	
	}
	
	public void setHasPriority(boolean hasPriority) {
		setColumn(HAS_PRIORITY, hasPriority);
	}
  
	public Collection ejbFindAllCasesByProviderAndStatus(int providerId, CaseStatus caseStatus) throws FinderException {
		try {
			return ejbFindAllCasesByProviderStatus(providerId, caseStatus.getStatus());
		}
		catch (RemoteException e) {
			throw new FinderException(e.getMessage());
		}
	}

	public Collection ejbFindAllCasesByProviderAndStatus(School provider, String caseStatus) throws FinderException {
		return ejbFindAllCasesByProviderStatus(((Integer)provider.getPrimaryKey()).intValue(), caseStatus);
	}
	
	public Collection ejbFindAllCasesByProviderAndStatus(School provider, CaseStatus caseStatus) throws FinderException {
		try {
			return ejbFindAllCasesByProviderStatus(((Integer)provider.getPrimaryKey()).intValue(), caseStatus.getStatus());
		}
		catch (RemoteException e) {
			throw new FinderException(e.getMessage());
		}
	}
	
	public Collection ejbFindAllCasesByProviderStatus(int providerId, String caseStatus) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).append("c , proc_case p");
		sql.appendWhereEquals("c."+getIDColumnName(), "p.proc_case_id");
		sql.appendAndEquals("c."+PROVIDER_ID,providerId);
		sql.appendAnd().appendEqualsQuoted("p.case_status",caseStatus);
		sql.appendAnd().appendEqualsQuoted("p.case_code",CASE_CODE_KEY);
		sql.appendOrderBy("c."+QUEUE_ORDER);

		return (Collection)super.idoFindPKsBySQL(sql.toString());
	}	
	
	public Collection ejbFindAllCasesByProviderAndStatus(int providerId, String caseStatus, int numberOfEntries, int startingEntry) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).append(" c, proc_case p");
		sql.appendWhereEquals("c."+getIDColumnName(), "p.proc_case_id");
		sql.appendAndEquals("c."+PROVIDER_ID,providerId);
		sql.appendAnd().appendEqualsQuoted("p.case_status",caseStatus);
		sql.appendAnd().appendEqualsQuoted("p.case_code",CASE_CODE_KEY);
		sql.appendOrderBy("c."+QUEUE_ORDER);

		return (Collection)super.idoFindPKsBySQL(sql.toString(), numberOfEntries, startingEntry);
	}	
	
	public Collection ejbFindAllCasesByProviderAndNotInStatus(int providerId, String[] caseStatus, int numberOfEntries, int startingEntry) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).append(" c, proc_case p");
		sql.appendWhereEquals("c."+getIDColumnName(), "p.proc_case_id");
		sql.appendAndEquals("c."+PROVIDER_ID,providerId);
		if (caseStatus != null)
			sql.appendAnd().append("p.case_status").appendNotInArrayWithSingleQuotes(caseStatus);
		sql.appendAnd().appendEqualsQuoted("p.case_code",CASE_CODE_KEY);
		sql.appendOrderBy("c."+APPLICATION_STATUS+" desc, c."+QUEUE_ORDER);

		return (Collection)super.idoFindPKsBySQL(sql.toString(), numberOfEntries, startingEntry);
	}	
	
	public Collection ejbFindAllCasesByProviderAndNotInStatus(int providerId, int sortBy, Date fromDate, Date toDate, String[] caseStatus, int numberOfEntries, int startingEntry) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).append(" c, proc_case p");
		if (sortBy == SORT_DATE_OF_BIRTH)
			sql.append(", ic_user u");
		sql.appendWhereEquals("c."+getIDColumnName(), "p.proc_case_id");
		sql.appendAndEquals("c."+PROVIDER_ID,providerId);
		if (caseStatus != null)
			sql.appendAnd().append("p.case_status").appendNotInArrayWithSingleQuotes(caseStatus);
		sql.appendAnd().appendEqualsQuoted("p.case_code",CASE_CODE_KEY);
		if (sortBy == SORT_DATE_OF_BIRTH) {
			sql.appendAndEquals("c."+CHILD_ID, "u.ic_user_id");
			sql.appendAnd().append("u.date_of_birth").appendGreaterThanOrEqualsSign().append(fromDate);
			sql.appendAnd().append("u.date_of_birth").appendLessThanOrEqualsSign().append(toDate);
		}
		else if (sortBy == SORT_QUEUE_DATE) {
			sql.appendAnd().append("c."+QUEUE_DATE).appendGreaterThanOrEqualsSign().append(fromDate);
			sql.appendAnd().append("c."+QUEUE_DATE).appendLessThanOrEqualsSign().append(toDate);
		}
		else if (sortBy == SORT_PLACEMENT_DATE) {
			sql.appendAnd().append("c."+FROM_DATE).appendGreaterThanOrEqualsSign().append(fromDate);
			sql.appendAnd().append("c."+FROM_DATE).appendLessThanOrEqualsSign().append(toDate);
		}
		sql.appendOrderBy("c."+APPLICATION_STATUS+" desc, c."+QUEUE_ORDER);

		return (Collection)super.idoFindPKsBySQL(sql.toString(), numberOfEntries, startingEntry);
	}	
	
	public Collection ejbFindAllCasesByProviderStatus(int providerId, String caseStatus[]) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).append(" c, proc_case p");
		sql.appendWhereEquals("c."+getIDColumnName(), "p.proc_case_id");
		sql.appendAndEquals("c."+PROVIDER_ID,providerId);
		sql.appendAnd().appendEqualsQuoted("p.case_code",CASE_CODE_KEY);
		sql.appendAnd().append("p.case_status").appendInArray(caseStatus);
		sql.appendOrderBy("c."+QUEUE_ORDER);

		return (Collection)super.idoFindPKsBySQL(sql.toString());
	}		
	
	public Collection ejbFindAllCasesByProviderStatusNotRejected(int providerId, String caseStatus) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).append(" c, proc_case p");
		sql.appendWhereEquals("c."+getIDColumnName(), "p.proc_case_id");
		sql.appendAndEquals("c."+PROVIDER_ID,providerId);
		sql.appendAnd().appendEqualsQuoted("p.case_status",caseStatus);
		sql.appendAnd().appendEqualsQuoted("p.case_code",CASE_CODE_KEY);
		sql.appendAnd().append(REJECTION_DATE).append(" is null");
		
		return (Collection)super.idoFindPKsBySQL(sql.toString());
	}	
	
	public Collection ejbFindAllCasesByUserAndStatus(User owner, String caseStatus) throws  FinderException {
		return super.ejbFindAllCasesByUserAndStatus(owner,caseStatus);
	}
	
	public Collection ejbFindAllCasesByStatus(String caseStatus) throws FinderException {
		return super.ejbFindAllCasesByStatus(caseStatus);
	}
	
	public Integer ejbFindApplicationByChildAndChoiceNumber(User child, int choiceNumber) throws FinderException {
		return ejbFindApplicationByChildAndChoiceNumber(((Integer)child.getPrimaryKey()).intValue(), choiceNumber);
	}

	public Integer ejbFindApplicationByChildAndChoiceNumber(int childID, int choiceNumber) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(CHOICE_NUMBER, choiceNumber).appendAndEquals(CHILD_ID,childID);
		return (Integer) idoFindOnePKByQuery(sql);
	}
	
	public Collection ejbFindApplicationByChild(int childID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(CHILD_ID,childID);
		sql.appendOrderBy(CHOICE_NUMBER);
		return super.idoFindPKsByQuery(sql);
	}
	
	public Collection ejbFindApplicationByChildAndNotInStatus(int childID, String[] caseStatus) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).append(" c, proc_case p");
		sql.appendWhereEquals("c."+getIDColumnName(), "p.proc_case_id");
		sql.appendAndEquals("c."+CHILD_ID,childID);
		sql.appendAnd().appendEqualsQuoted("p.case_code",CASE_CODE_KEY);
		sql.appendAnd().append("p.case_status").appendNotInArrayWithSingleQuotes(caseStatus);
		sql.appendOrderBy(CHOICE_NUMBER);
		return super.idoFindPKsByQuery(sql);
	}
	
	public Collection ejbFindApplicationsByProviderAndDate(int providerID, Date date) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(PROVIDER_ID,providerID);
		sql.appendAndEquals(QUEUE_DATE, date);
		sql.appendOrderBy(QUEUE_ORDER);
		return super.idoFindPKsByQuery(sql);
	}
	
	public int ejbHomeGetNumberOfApplications(int providerID, String caseStatus) throws IDOException {
		IDOQuery sql = idoQuery();
		sql.append("select count(c."+CHILD_ID+") from ").append(ENTITY_NAME).append(" c , proc_case p");
		sql.appendWhereEquals("c."+getIDColumnName(), "p.proc_case_id");
		sql.appendAndEquals("c."+PROVIDER_ID,providerID);
		sql.appendAnd().appendEqualsQuoted("p.case_status",caseStatus);
		sql.appendAnd().appendEqualsQuoted("p.case_code",CASE_CODE_KEY);

		return idoGetNumberOfRecords(sql);
	}

	public int ejbHomeGetNumberOfApplications(int providerID, String[] caseStatus) throws IDOException {
		IDOQuery sql = idoQuery();
		sql.append("select count(c."+CHILD_ID+") from ").append(ENTITY_NAME).append(" c , proc_case p");
		sql.appendWhereEquals("c."+getIDColumnName(), "p.proc_case_id");
		sql.appendAndEquals("c."+PROVIDER_ID,providerID);
		sql.appendAnd().append("p.case_status").appendNotInArrayWithSingleQuotes(caseStatus);
		sql.appendAnd().appendEqualsQuoted("p.case_code",CASE_CODE_KEY);

		return idoGetNumberOfRecords(sql);
	}
	
	public int ejbHomeGetNumberOfApplications(int providerID, String[] caseStatus, int sortBy, Date fromDate, Date toDate) throws IDOException {
		IDOQuery sql = idoQuery();
		sql.append("select count(c."+CHILD_ID+") from ").append(ENTITY_NAME).append(" c , proc_case p");
		if (sortBy == SORT_DATE_OF_BIRTH)
			sql.append(", ic_user u");
		sql.appendWhereEquals("c."+getIDColumnName(), "p.proc_case_id");
		sql.appendAndEquals("c."+PROVIDER_ID,providerID);
		if (caseStatus != null)
			sql.appendAnd().append("p.case_status").appendNotInArrayWithSingleQuotes(caseStatus);
		sql.appendAnd().appendEqualsQuoted("p.case_code",CASE_CODE_KEY);
		if (sortBy == SORT_DATE_OF_BIRTH) {
			sql.appendAndEquals("c."+CHILD_ID, "u.ic_user_id");
			sql.appendAnd().append("u.date_of_birth").appendGreaterThanOrEqualsSign().append(fromDate);
			sql.appendAnd().append("u.date_of_birth").appendLessThanOrEqualsSign().append(toDate);
		}
		else if (sortBy == SORT_QUEUE_DATE) {
			sql.appendAnd().append("c."+QUEUE_DATE).appendGreaterThanOrEqualsSign().append(fromDate);
			sql.appendAnd().append("c."+QUEUE_DATE).appendLessThanOrEqualsSign().append(toDate);
		}
		else if (sortBy == SORT_PLACEMENT_DATE) {
			sql.appendAnd().append("c."+FROM_DATE).appendGreaterThanOrEqualsSign().append(fromDate);
			sql.appendAnd().append("c."+FROM_DATE).appendLessThanOrEqualsSign().append(toDate);
		}
		sql.appendAnd().appendEqualsQuoted("p.case_code",CASE_CODE_KEY);

		return idoGetNumberOfRecords(sql);
	}
	
	public int ejbHomeGetPositionInQueue(int queueOrder, int providerID, String[] caseStatus) throws IDOException {
		IDOQuery sql = idoQuery();
		sql.append("select count(c."+CHILD_ID+") from ").append(ENTITY_NAME).append(" c , proc_case p");
		sql.appendWhereEquals("c."+getIDColumnName(), "p.proc_case_id");
		sql.appendAndEquals("c."+PROVIDER_ID,providerID);
		sql.appendAnd().append("p.case_status").appendNotInArrayWithSingleQuotes(caseStatus);
		sql.appendAndEqualsQuoted("p.case_code",CASE_CODE_KEY);
		sql.appendAnd().append(QUEUE_ORDER).appendLessThanOrEqualsSign().append(queueOrder);
		return idoGetNumberOfRecords(sql);
	}

	public int ejbHomeGetPositionInQueue(int queueOrder, int providerID, String caseStatus) throws IDOException {
		IDOQuery sql = idoQuery();
		sql.append("select count(c."+CHILD_ID+") from ").append(ENTITY_NAME).append(" c , proc_case p");
		sql.appendWhereEquals("c."+getIDColumnName(), "p.proc_case_id");
		sql.appendAndEquals("c."+PROVIDER_ID,providerID);
		sql.appendAndEqualsQuoted("p.case_status",caseStatus);
		sql.appendAndEqualsQuoted("p.case_code",CASE_CODE_KEY);
		sql.appendAnd().append(QUEUE_ORDER).appendLessThanOrEqualsSign().append(queueOrder);
		return idoGetNumberOfRecords(sql);
	}
}