package se.idega.idegaweb.commune.account.citizen.data;

import com.idega.data.GenericEntity;
import java.util.Collection;
import javax.ejb.FinderException;

/**
 * Last modified: $Date: 2003/04/02 17:55:51 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.6 $
 */
public class CitizenApplicantMovingToBMPBean extends GenericEntity
    implements CitizenApplicantMovingTo {
    private static final String ENTITY_NAME = "comm_cit_moving_to";
    //private static final String COLUMN_ID = ENTITY_NAME + "_id";
	private static final String COLUMN_APPLICATION_ID = "application_id";
    private final static String COLUMN_ADDRESS = "address";
    private final static String COLUMN_MOVING_IN_DATE = "moving_in_date";
    private final static String COLUMN_HOUSING_TYPE = "housing_type";
    private final static String COLUMN_PROPERTY_TYPE = "property_type";
    private final static String COLUMN_LANDLORD = "landlord";

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
		addAttribute (COLUMN_LANDLORD, "Landlord", true, true, String.class, 60);
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
    
    public String getLandlord () {
        final String landlord = getStringColumnValue (COLUMN_LANDLORD);
        return landlord == null ? "" : landlord;
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
    
    public void setLandlord (final String name, final String phone,
                             final String address) {
        setColumn (COLUMN_LANDLORD, name + ", " + phone + ", " + address);
    }

    public Collection ejbFindByApplicationId (final int applicationId)
        throws FinderException {
        final String sql = "select * from " + ENTITY_NAME
                + " where " + COLUMN_APPLICATION_ID + " = " + applicationId;
        return idoFindPKsBySQL (sql);
    }
}
