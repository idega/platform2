/*
 * $Id: CitizenAccountBMPBean.java,v 1.15 2003/01/02 18:56:55 eiki Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.account.citizen.data;

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

	private final static String NAME = "name";
	private final static String SSN = "ssn";
	private final static String EMAIL = "email";
	private final static String PHONE_HOME = "phone_home";
	private final static String PHONE_WORK = "phone_work";
    private final static String STREET = "street";
    private final static String ZIP_CODE = "zip_code";
    private final static String CITY = "city";
    private final static String CIVIL_STATUS = "civil_status";
    private final static String HAS_COHABITANT = "has_cohabitant";
    private final static String CHILDREN_COUNT = "children_count";
    private final static String APPLICATION_REASON = "application_reason";

	public void initializeAttributes() {
		//addAttribute (getIDColumnName());
		super.addGeneralCaseRelation();
		addAttribute (NAME, "Name", true, true, String.class, 100);
		addAttribute (SSN, "SSN", true, true, String.class, 40);
		addAttribute (EMAIL, "E-mail", true, true, String.class, 40);
		addAttribute (PHONE_HOME, "Home phone", true, true, String.class, 20);
		addAttribute (PHONE_WORK, "Work phone", true, true, String.class, 20);
        addAttribute (STREET, "street", true, true, String.class, 40);
        addAttribute (ZIP_CODE, "zipCode", true, true, String.class, 40);
        addAttribute (CITY, "city", true, true, String.class, 40);
        addAttribute (CIVIL_STATUS, "Civil Status", true, true, String.class,
                      40);
        addAttribute (HAS_COHABITANT, "Has Cohabitant", true, true,
                      Boolean.class);
        addAttribute (CHILDREN_COUNT, "Children Count", true, true,
                      Integer.class);
        addAttribute (APPLICATION_REASON, "Application Reason", true, true,
                      String.class, 40);
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

	public String getSsn () {
		return getStringColumnValue (SSN);
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

    public String getStreet () {
        return getStringColumnValue (STREET);
    }

    public String getZipCode () {
        return getStringColumnValue (ZIP_CODE);
    }

    public String getCity () {
        return getStringColumnValue (CITY);
    }

    public String getCivilStatus () {
        return getStringColumnValue (CIVIL_STATUS);
    }

    public boolean hasCohabitant () {
        boolean result = false;
        try {
            result = getBooleanColumnValue (HAS_COHABITANT);
        } catch (Exception e) {
            // nothing
        }

        return result;
    }

    public int getChildrenCount () {
        Integer result = null;
        try {
            result = getIntegerColumnValue (CHILDREN_COUNT);
        } catch (Exception e) {
            // nothing
        }
        
        return result != null ? result.intValue () : 0;
    }
    
    public String getApplicationReason () {
        return getStringColumnValue (APPLICATION_REASON);
    }

    // set methods for bean properties

	public void setApplicantName (final String name) {
		setColumn (NAME, name);
	}

	public void setSsn (final String ssn) {
		setColumn (SSN, ssn);
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

    public void setStreet (final String street) throws RemoteException {
        setColumn (STREET, street);
    }
    
    public void setZipCode (final String zipCode) throws RemoteException {
        setColumn (ZIP_CODE, zipCode);
    }
    
    public void setCity (final String city) throws RemoteException {
        setColumn (CITY, city);
    }
    
    public void setCivilStatus (final String civilStatus) {
        setColumn (CIVIL_STATUS, civilStatus);
    }

    public void setHasCohabitant (final boolean hasCohabitant) {
        setColumn (HAS_COHABITANT, new Boolean (hasCohabitant));
    }

    public void setChildrenCount (final int childrenCount) {
        setColumn (CHILDREN_COUNT, childrenCount);
    }

    public void setApplicationReason (final String applicationReason) {
        setColumn (APPLICATION_REASON, applicationReason);
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
        throws FinderException {
		return super.ejbFindAllCasesByStatus(caseStatus);
	}
}
