/*
 * $Id: CitizenAccountBMPBean.java,v 1.8 2002/11/04 11:27:47 staffan Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.account.citizen.data;

import com.idega.user.data.Gender;
import com.idega.block.process.data.*;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.*;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.account.data.AccountApplication;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class CitizenAccountBMPBean extends AbstractCaseBMPBean
    implements CitizenAccount, Case, AccountApplication {
	private final static String ENTITY_NAME = "comm_cit_acc";
	private final static String CASE_CODE_KEY = "MBANSKO";
	private final static String CASE_CODE_KEY_DESC
        = "Request for citizen account";

	protected final static String NAME = "name";
	protected final static String EMAIL = "email";
	protected final static String PHONE_HOME = "phone_home";
	protected final static String PHONE_WORK = "phone_work";
	protected final static String PID = "pid";
    protected final static String BIRTH_DATE = "birth_date";
    protected final static String CUSTODIAN1_PID = "custodian1_pid";
    protected final static String CUSTODIAN1_CIVIL_STATUS
        = "custodian1_civil_status";
    protected final static String CUSTODIAN2_PID = "custodian2_pid";
    protected final static String CUSTODIAN2_CIVIL_STATUS
        = "custodian2_civil_status";
    protected final static String STREET = "street";
    protected final static String ZIP_CODE = "zipCode";
    protected final static String CITY = "city";
    protected final static String GENDER_ID = "gender_id";

	public void initializeAttributes() {
		addAttribute (getIDColumnName());
		addAttribute (NAME, "Name", true, true, String.class, 40);
		addAttribute (EMAIL, "E-mail", true, true, String.class, 40);
		addAttribute (PHONE_HOME, "Home phone", true, true, String.class, 20);
		addAttribute (PHONE_WORK, "Work phone", true, true, String.class, 20);
       	addAttribute (GENDER_ID, "Gender", true, true, Integer.class,
                      "many-to-one", Gender.class);
		addAttribute (PID, "PID", true, true, String.class, 40);
        addAttribute (BIRTH_DATE, "Date of birth", Timestamp.class);
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


    // get methods for bean properties

	public String getApplicantName () {
		return getStringColumnValue (NAME);
	}

	public String getEmail () {
		return getStringColumnValue (EMAIL);
	}

	public String getPhoneHome () {
		return getStringColumnValue (PHONE_HOME);
	}

	public String getPhoneWork () {
		return getStringColumnValue (PHONE_WORK);
	}

	public String getPID () {
		return getStringColumnValue (PID);
	}

	public Date getBirthDate () {
        return (Timestamp) getColumnValue (BIRTH_DATE);
	}

    public String getCustodian1Pid () {
        return getStringColumnValue (CUSTODIAN1_PID);
    }

    public String getCustodian1CivilStatus () {
        return getStringColumnValue (CUSTODIAN1_CIVIL_STATUS);
    }

    public String getCustodian2Pid () {
        return getStringColumnValue (CUSTODIAN2_PID);
    }

    public String getCustodian2CivilStatus () {
        return getStringColumnValue (CUSTODIAN2_CIVIL_STATUS);
    }

    public String getStreet () {
        return getStringColumnValue (STREET);
    }

    public String getZipCode () {
        return getStringColumnValue (ZIP_CODE);
    }

    public String getCity () {
        return getStringColumnValue (CITY);
    }

    public int getGenderId () {
        return getIntColumnValue (GENDER_ID);
    }


    // set methods for bean properties

	public void setApplicantName (final String name) {
		setColumn (NAME, name);
	}

	public void setEmail (final String email) {
		setColumn (EMAIL, email);
	}

	public void setPhoneHome (final String phone) {
		setColumn (PHONE_HOME, phone);
	}

	public void setPhoneWork (final String phone) {
		setColumn (PHONE_WORK, phone);
	}

	public void setPID (final String pid) {
		setColumn (PID, pid);
	}

	public void setBirthDate (final Date birthDate) {
        setColumn (BIRTH_DATE, new Timestamp (birthDate.getTime ()));
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
    
    public void setStreet (final String street) throws RemoteException {
        setColumn (STREET, street);
    }
    
    public void setZipCode (final String zipCode) throws RemoteException {
        setColumn (ZIP_CODE, zipCode);
    }
    
    public void setCity (final String city) throws RemoteException {
        setColumn (CITY, city);
    }
    
    public void setGenderId (final int genderId) throws RemoteException {
        setColumn (GENDER_ID, genderId);
    }
    
	/**
	 * Finds all cases for all users with the specified caseStatus and the
     * associated caseCode
	 */
	public Collection ejbFindAllCasesByStatus(CaseStatus caseStatus)
        throws FinderException, RemoteException {
		return super.ejbFindAllCasesByStatus(caseStatus.getStatus());
	}
	
	/**
	 * Finds all cases for all users with the specified caseStatus and the
     * associated caseCode
	 */
	public Collection ejbFindAllCasesByStatus(String caseStatus)
        throws FinderException, RemoteException {
		return super.ejbFindAllCasesByStatus(caseStatus);
	}
}
