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
import com.idega.block.process.data.CaseStatus;
import com.idega.block.school.data.School;
import com.idega.core.data.ICFile;
import com.idega.data.BlobWrapper;
import com.idega.user.data.User;

import se.idega.idegaweb.commune.childcare.check.data.Check;

import java.io.InputStream;
import java.io.OutputStream;
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
	protected final static String REJECTION_DATE = "rejection_date";
	
	private ICFile _file;
	private BlobWrapper _wrapper;
	
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
		
		addManyToOneRelationship(PROVIDER_ID,School.class);
		addManyToOneRelationship(CHILD_ID,User.class);
		addManyToOneRelationship(CHECK_ID,Check.class);
		addManyToOneRelationship(CONTRACT_ID,ICFile.class);
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
	
//	public boolean getParentsAgree() {
//		return getBooleanColumnValue(PARENTS_AGREE);	
//	}
	
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
		
	public Date getRejectionDate() {
		return (Date)getColumnValue(REJECTION_DATE);	
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

	private int getFileID() {
		return getIntColumnValue(REJECTION_DATE);
	}

	public ICFile getFile() {
		int fileID = getFileID();
		if (fileID != -1) {
			_file = (ICFile) getColumnValue(REJECTION_DATE);
		}
		
		return _file;
	}

	public void setFile(ICFile file) {
//		file.setMimeType(com.idega.core.data.ICMimeTypeBMPBean.IC_MIME_TYPE_XML);
		setColumn(REJECTION_DATE, file);
		_file = file;
	}

	public void setPageValue(InputStream stream) {
		ICFile file = getFile();
		if (file == null) {
			file = ((com.idega.core.data.ICFileHome) com.idega.data.IDOLookup.getHomeLegacy(ICFile.class)).createLegacy();
			setFile(file);
		}
		file.setFileValue(stream);
	}

	public InputStream getPageValue() {
		try {
			ICFile file = getFile();
			if (file != null) {
				return (file.getFileValue());
			}
		}
		catch (Exception e) {
		}

		return null;
	}

	public OutputStream getPageValueForWrite() {
		ICFile file = getFile();
		if (file == null) {
			file = ((com.idega.core.data.ICFileHome) com.idega.data.IDOLookup.getHomeLegacy(ICFile.class)).createLegacy();
			setFile(file);
		}
		OutputStream theReturn = file.getFileValueForWrite();
		_wrapper = (BlobWrapper) file.getColumnValue(com.idega.core.data.ICFileBMPBean.getColumnFileValue());

		return theReturn;
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
}