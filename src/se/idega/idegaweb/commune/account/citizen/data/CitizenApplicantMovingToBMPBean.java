package se.idega.idegaweb.commune.account.citizen.data;

import com.idega.data.GenericEntity;

/**
 * Last modified: $Date: 2002/11/15 14:05:44 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.3 $
 */
public class CitizenApplicantMovingToBMPBean extends GenericEntity
    implements CitizenApplicantMovingTo {
    private static final String ENTITY_NAME = "comm_cit_moving_to";
    private static final String COLUMN_ID = ENTITY_NAME + "_id";
	private static final String COLUMN_APPLICATION_ID = "application_id";
    private final static String COLUMN_ADDRESS = "address";
    private final static String COLUMN_MOVING_IN_DATE = "moving_in_date";
    private final static String COLUMN_HOUSING_TYPE = "housing_type";
    private final static String COLUMN_PROPERTY_TYPE = "property_type";

	public void initializeAttributes() {
		addAttribute (getIDColumnName());
       	addAttribute (COLUMN_APPLICATION_ID, "Application id", true, true,
                      Integer.class, "many-to-one", CitizenAccount.class);
		addAttribute (COLUMN_ADDRESS, "Address", true, true, String.class, 50);
		addAttribute (COLUMN_MOVING_IN_DATE, "Moving in date", true, true,
                      String.class, 10);
		addAttribute (COLUMN_HOUSING_TYPE, "Housing type", true, true,
                      String.class, 30);
		addAttribute (COLUMN_PROPERTY_TYPE, "Property type", true, true,
                      String.class, 30);
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

    public String getAddress () {
        return getStringColumnValue (COLUMN_ADDRESS);
    }
    
    public String getMovingInDate () {
        return getStringColumnValue (COLUMN_MOVING_IN_DATE);
    }
    
    public String getHousingType () {
        return getStringColumnValue (COLUMN_HOUSING_TYPE);
    }
    
    public String getPropertyType () {
        return getStringColumnValue (COLUMN_PROPERTY_TYPE);
    }
    
    public void setApplicationId (final int applicationId) {
        setColumn (COLUMN_APPLICATION_ID, applicationId);
    }
    
    public void setAddress (final String address) {
        setColumn (COLUMN_ADDRESS, address);
    }
    
    public void setMovingInDate (final String movingInDate) {
        setColumn (COLUMN_MOVING_IN_DATE, movingInDate);
    }
    
    public void setHousingType (final String housingType) {
        setColumn (COLUMN_HOUSING_TYPE, housingType);
    }
    
    public void setPropertyType (final String propertyType) {
        setColumn (COLUMN_PROPERTY_TYPE, propertyType);
    }
}
