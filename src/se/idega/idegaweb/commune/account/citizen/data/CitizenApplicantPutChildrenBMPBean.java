package se.idega.idegaweb.commune.account.citizen.data;

import com.idega.data.GenericEntity;

/**
 * Last modified: $Date: 2002/11/15 08:34:29 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.1 $
 */
public class CitizenApplicantPutChildrenBMPBean extends GenericEntity
    implements CitizenApplicantPutChildren {
    private static final String ENTITY_NAME
        = "comm_cit_acc_applicant_put_children";
    private static final String COLUMN_ID = ENTITY_NAME + "_id";
	private static final String COLUMN_APPLICATION_ID = "application_id";
    private final static String COLUMN_CURRENT_KOMMUN = "current_kommun";

	public void initializeAttributes() {
		addAttribute (getIDColumnName());
       	addAttribute (COLUMN_APPLICATION_ID, "Application id", true, true,
                      Integer.class, "many-to-one", CitizenAccount.class);
		addAttribute (COLUMN_CURRENT_KOMMUN, "Current Kommun", true, true,
                      String.class, 20);
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

    public String getCurrentKommun () {
        return getStringColumnValue (COLUMN_CURRENT_KOMMUN);
    }
    
    public void setApplicationId (final int applicationId) {
        setColumn (COLUMN_APPLICATION_ID, applicationId);
    }
    
    public void setCurrentKommun (final String currentKommun) {
        setColumn (COLUMN_CURRENT_KOMMUN, currentKommun);
    }
}
