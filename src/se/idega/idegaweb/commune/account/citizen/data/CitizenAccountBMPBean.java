/*
 * $Id: CitizenAccountBMPBean.java,v 1.2 2002/07/22 10:36:30 palli Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.account.citizen.data;

import com.idega.user.data.User;
import com.idega.block.process.data.AbstractCaseBMPBean;
import com.idega.block.process.data.Case;
import com.idega.block.process.data.CaseStatus;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.FinderException;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class CitizenAccountBMPBean extends AbstractCaseBMPBean implements CitizenAccount, Case {
	private final static String ENTITY_NAME = "comm_cit_acc";
	private final static String CASE_CODE_KEY = "MBANSKO";
	private final static String CASE_CODE_KEY_DESC = "Request for citizen account";

	protected final static String EMAIL = "email";
	protected final static String PHONE_HOME = "phone_home";
	protected final static String PHONE_WORK = "phone_work";
	protected final static String PID = "pid";

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(EMAIL, "E-mail", true, true, java.lang.String.class, 40);
		addAttribute(PHONE_HOME, "Home phone", true, true, java.lang.String.class, 20);
		addAttribute(PHONE_WORK, "Work phone", true, true, java.lang.String.class, 20);
		addAttribute(PID, "PID", true, true, java.lang.String.class, 40);
	}

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public String getCaseCodeKey() {
		return CASE_CODE_KEY;
	}

	public String getCaseCodeDescription() {
		return CASE_CODE_KEY_DESC;
	}

	public void setEmail(String email) {
		setColumn(EMAIL, email);
	}

	public void setPhoneHome(String phone) {
		setColumn(PHONE_HOME, phone);
	}

	public void setPhoneWork(String phone) {
		setColumn(PHONE_WORK, phone);
	}

	public void setPID(String pid) {
		setColumn(PID, pid);
	}

	public String getEmail() {
		return getStringColumnValue(EMAIL);
	}

	public String getPhoneHome() {
		return getStringColumnValue(PHONE_HOME);
	}

	public String getPhoneWork() {
		return getStringColumnValue(PHONE_WORK);
	}

	public String getPID() {
		return getStringColumnValue(PID);
	}

	/**
	 * Finds all cases for all users with the specified caseStatus and the associated caseCode
	 */
	public Collection ejbFindAllCasesByStatus(CaseStatus caseStatus) throws FinderException, RemoteException {
		return super.ejbFindAllCasesByStatus(caseStatus.getStatus());
	}
	
	/**
	 * Finds all cases for all users with the specified caseStatus and the associated caseCode
	 */
	public Collection ejbFindAllCasesByStatus(String caseStatus) throws FinderException, RemoteException {
		return super.ejbFindAllCasesByStatus(caseStatus);
	}
}