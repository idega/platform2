package se.idega.idegaweb.commune.account.citizen.data;

import com.idega.data.GenericEntity;
import com.idega.user.data.Gender;

/**
 * Last modified: $Date: 2002/11/19 13:29:37 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.3 $
 */
public class CitizenApplicantCohabitantBMPBean extends GenericEntity
    implements CitizenApplicantCohabitant {
    private static final String ENTITY_NAME = "comm_cit_cohab";
    private static final String COLUMN_ID = ENTITY_NAME + "_id";
	private static final String COLUMN_APPLICATION_ID = "application_id";
    private final static String COLUMN_FIRST_NAME = "first_name";
    private final static String COLUMN_LAST_NAME = "last_name";
    private final static String COLUMN_SSN = "ssn";
    private final static String COLUMN_CIVIL_STATUS = "civil_status";
    private final static String COLUMN_PHONE_WORK = "phone_work";
    private final static String COLUMN_GENDER_ID = "gender_id";

	public void initializeAttributes() {
		addAttribute (getIDColumnName());
       	addAttribute (COLUMN_APPLICATION_ID, "Application id", true, true,
                      Integer.class, "many-to-one", CitizenAccount.class);
		addAttribute (COLUMN_FIRST_NAME, "First Name", true, true, String.class,
                      40);
		addAttribute (COLUMN_LAST_NAME, "Last Name", true, true, String.class,
                      40);
		addAttribute (COLUMN_SSN, "SSN", true, true, String.class, 40);
		addAttribute (COLUMN_CIVIL_STATUS, "Civil Status", true, true,
                      String.class, 20);
		addAttribute (COLUMN_PHONE_WORK, "Phone Work", true, true, String.class,
                      20);
       	addAttribute (COLUMN_GENDER_ID, "Gender", true, true, Integer.class,
                      "many-to-one", Gender.class);
	}

	public String getEntityName() {
		return ENTITY_NAME;
	}

    public int getApplicationId () {
        Integer applicationId = null;
        try {
            applicationId = getIntegerColumnValue (COLUMN_APPLICATION_ID);
        } catch (Exception e) {
            // nothing
        }

        return applicationId == null ? -1 : applicationId.intValue ();
    }

    public String getFirstName () {
        return getStringColumnValue (COLUMN_FIRST_NAME);
    }
    
    public String getLastName () {
        return getStringColumnValue (COLUMN_LAST_NAME);
    }
    
    public String getSsn () {
        return getStringColumnValue (COLUMN_SSN);
    }
    
    public String getCivilStatus () {
        return getStringColumnValue (COLUMN_CIVIL_STATUS);
    }
    
    public String getPhoneWork () {
        return getStringColumnValue (COLUMN_PHONE_WORK);
    }

    public Integer getGenderId () {
        Integer genderId = null;
        try {
            genderId = getIntegerColumnValue (COLUMN_GENDER_ID);
        } catch (Exception e) {
            // nothing
        }

        return genderId;
    }
    

    public void setApplicationId (final int applicationId) {
        setColumn (COLUMN_APPLICATION_ID, applicationId);
    }

    public void setFirstName (final String firstName) {
        setColumn (COLUMN_FIRST_NAME, firstName);
    }
    
    public void setLastName (final String lastName) {
        setColumn (COLUMN_LAST_NAME, lastName);
    }
    
    public void setSsn (final String ssn) {
        setColumn (COLUMN_SSN, ssn);
    }
    
    public void setCivilStatus (final String civilStatus) {
        setColumn (COLUMN_CIVIL_STATUS, civilStatus);
    }
    
    public void setPhoneWork (final String phoneWork) {
        setColumn (COLUMN_PHONE_WORK, phoneWork);
    }
    
    public void setGenderId (final int genderId) {
        setColumn (COLUMN_GENDER_ID, genderId);
    }
}
