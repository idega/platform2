/*
 * $Id: CitizenAccountBMPBean.java,v 1.5 2002/10/31 15:13:02 staffan Exp $
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

import se.idega.idegaweb.commune.account.data.AccountApplication;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class CitizenAccountBMPBean extends AbstractCaseBMPBean implements CitizenAccount, Case, AccountApplication {
	private final static String ENTITY_NAME = "comm_cit_acc";
	private final static String CASE_CODE_KEY = "MBANSKO";
	private final static String CASE_CODE_KEY_DESC = "Request for citizen account";

	protected final static String EMAIL = "email";
	protected final static String PHONE_HOME = "phone_home";
	protected final static String PHONE_WORK = "phone_work";
	protected final static String PID = "pid";
    protected final static String CUSTODIAN1_PID = "custodian1_pid";
    protected final static String CUSTODIAN1_CIVIL_STATUS
        = "custodian1_civil_status";
    protected final static String CUSTODIAN2_PID = "custodian2_pid";
    protected final static String CUSTODIAN2_CIVIL_STATUS
        = "custodian2_civil_status";
    protected final static String STREET = "street";
    protected final static String ZIP_CODE = "zipCode";
    protected final static String CITY = "city";

	public void initializeAttributes() {
		addAttribute (getIDColumnName());
		addAttribute (EMAIL, "E-mail", true, true, String.class, 40);
		addAttribute (PHONE_HOME, "Home phone", true, true, String.class, 20);
		addAttribute (PHONE_WORK, "Work phone", true, true, String.class, 20);
		addAttribute (PID, "PID", true, true, String.class, 40);
        addAttribute (CUSTODIAN1_PID, "custodian1_pid", true, true,
                      String.class, 40);
        addAttribute (CUSTODIAN1_CIVIL_STATUS, "custodian1_civilStatus", true,
                      true, String.class, 40);
        addAttribute (CUSTODIAN2_PID, "custodian2_pid", true, true,
                      String.class, 40);
        addAttribute (CUSTODIAN2_CIVIL_STATUS, "custodian2_civilStatus", true,
                      true, String.class, 40);
        addAttribute (STREET, "street", true, true, String.class, 40);
        addAttribute (ZIP_CODE, "zipCode", true, true, String.class, 40);
        addAttribute (CITY, "city", true, true, String.class, 40);
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

    public void setCustodian1Pid (final String pid) throws RemoteException {
        setColumn (CUSTODIAN1_PID, pid);
    }
    
    public void setCustodian1CivilStatus (final String civilStatus)
        throws RemoteException {
        setColumn (CUSTODIAN1_CIVIL_STATUS, civilStatus);
    }
    
    public void setCustodian2Pid (final String pid) throws RemoteException {
        setColumn (CUSTODIAN2_PID, pid);
    }
    
    public void setCustodian2CivilStatus (final String civilStatus)
        throws RemoteException {
        setColumn (CUSTODIAN2_CIVIL_STATUS, civilStatus);
    }
    
    public void setStreet (String street) throws RemoteException {
        setColumn (STREET, street);
    }
    
    public void setZipCode (final String zipCode) throws RemoteException {
        setColumn (ZIP_CODE, zipCode);
    }
    
    public void setCity (final String city) throws RemoteException {
        setColumn (CITY, city);
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
	
	/**
	 * @see se.idega.idegaweb.commune.account.data.AccountApplication#getApplicantName()
	 */
	public String getApplicantName() throws RemoteException {
		User owner = this.getOwner();
		if (owner != null)
			return owner.getName();
		else
			return null;
	}

	/**
	 * @see se.idega.idegaweb.commune.account.data.AccountApplication#setApplicantName(String)
	 */
	public void setApplicantName(String name) throws RemoteException {
		User owner = this.getOwner();
		if (owner != null) {
			owner.setName(name);
			owner.store();
		}		
	}
}
