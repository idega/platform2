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
	
	public Collection ejbFindAllCasesByProviderAndStatus(int providerId, CaseStatus caseStatus) throws FinderException, RemoteException {
		return ejbFindAllCasesByProviderStatus(providerId, caseStatus.getStatus());
	}

	public Collection ejbFindAllCasesByProviderAndStatus(School provider, String caseStatus) throws FinderException, RemoteException {
		return ejbFindAllCasesByProviderStatus(((Integer)provider.getPrimaryKey()).intValue(), caseStatus);
	}
	
	public Collection ejbFindAllCasesByProviderAndStatus(School provider, CaseStatus caseStatus) throws FinderException, RemoteException {
		return ejbFindAllCasesByProviderStatus(((Integer)provider.getPrimaryKey()).intValue(), caseStatus.getStatus());
	}
	
	public Collection ejbFindAllCasesByProviderStatus(int providerId, String caseStatus) throws FinderException, RemoteException {
		Collection ids = super.ejbFindAllCasesByStatus(caseStatus);
		
		StringBuffer sql = new StringBuffer("select * from ");
		sql.append(getEntityName());
		sql.append(" where ");
		sql.append(PROVIDER_ID);
		sql.append(" = ");
		sql.append(providerId);
		sql.append(" and ");
		sql.append(getIDColumnName());
		sql.append(" in (");
		
		Iterator it = ids.iterator();
		while (it.hasNext()) {
			Integer id = (Integer)it.next();
			sql.append(id);
			if (it.hasNext())
				sql.append(", ");
		}
		sql.append(")");
		
		return (Collection)super.idoFindPKsBySQL(sql.toString());
	}	
	
	public Collection ejbFindAllCasesByProviderStatus(int providerId, String caseStatus[]) throws FinderException, RemoteException {
		Collection ids = super.ejbFindAllCasesByStatusArray(caseStatus);
		
		StringBuffer sql = new StringBuffer("select * from ");
		sql.append(getEntityName());
		sql.append(" where ");
		sql.append(PROVIDER_ID);
		sql.append(" = ");
		sql.append(providerId);
		sql.append(" and ");
		sql.append(getIDColumnName());
		sql.append(" in (");
		
		Iterator it = ids.iterator();
		while (it.hasNext()) {
			Integer id = (Integer)it.next();
			sql.append(id);
			if (it.hasNext())
				sql.append(", ");
		}
		sql.append(")");
		
		return (Collection)super.idoFindPKsBySQL(sql.toString());
	}		
	
	public Collection ejbFindAllCasesByProviderStatusNotRejected(int providerId, String caseStatus) throws FinderException, RemoteException {
		Collection ids = super.ejbFindAllCasesByStatus(caseStatus);
		
		StringBuffer sql = new StringBuffer("select * from ");
		sql.append(getEntityName());
		sql.append(" where ");
		sql.append(PROVIDER_ID);
		sql.append(" = ");
		sql.append(providerId);
		sql.append(" and ");
		sql.append(getIDColumnName());
		sql.append(" in (");
		
		Iterator it = ids.iterator();
		while (it.hasNext()) {
			Integer id = (Integer)it.next();
			sql.append(id);
			if (it.hasNext())
				sql.append(", ");
		}
		sql.append(") and ");
		sql.append(REJECTION_DATE);
		sql.append(" is null");
		
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
}