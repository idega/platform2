package se.idega.idegaweb.commune.account.citizen.data;

import javax.ejb.FinderException;

import com.idega.core.location.data.Commune;
import com.idega.core.location.data.CommuneHome;
import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;

/**
 * Last modified: $Date: 2003/10/22 10:00:57 $ by $Author: gimmi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.6 $
 */
public class CitizenApplicantPutChildrenBMPBean extends GenericEntity implements CitizenApplicantPutChildren {
  private static final String ENTITY_NAME = "comm_cit_put_children";
  //private static final String COLUMN_ID = ENTITY_NAME + "_id";
	private static final String COLUMN_APPLICATION_ID = "application_id";
  private final static String COLUMN_CURRENT_KOMMUN = "current_kommun";
  private static String COLUMN_CURRENT_COMMUNE_ID = "current_commune_id";

	public void initializeAttributes() {
		addAttribute (getIDColumnName());
    addAttribute (COLUMN_APPLICATION_ID, "Application id", true, true, Integer.class, "many-to-one", CitizenAccount.class);
		addAttribute (COLUMN_CURRENT_KOMMUN, "Current Kommun", true, true, String.class, 20);
		this.addManyToOneRelationship(COLUMN_CURRENT_COMMUNE_ID, Commune.class);
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

    public int getCurrentCommuneId () {
    	int communeId = getIntColumnValue (COLUMN_CURRENT_COMMUNE_ID);
			if (communeId < 1) {
				System.out.print("[CitizenApplicantPutChildrenBMPBean] CommuneID not found, trying backwards compatability ...");
				String communeName = getStringColumnValue(COLUMN_CURRENT_KOMMUN);
				if (communeName != null && !communeName.equals("")) {
					try {
						CommuneHome cHome = (CommuneHome) IDOLookup.getHome(Commune.class);
						Commune commune = cHome.findByCommuneName(communeName);
						if (commune != null) {
							int newCommuneID = ((Integer) commune.getPrimaryKey()).intValue();
							this.setCurrentCommuneID(newCommuneID);
							this.store();
							System.out.println("Success !!!");
							return newCommuneID;
						}
					} catch (Exception e) {
						System.out.println("Failed, when trying to find \n\tCommune = "+communeName+"\n\tApplicationID = "+getApplicationId()+" \n\tERROR = ("+e.getMessage()+")");
					}
				}
			}
			return communeId;
    }
    
    public void setApplicationId (final int applicationId) {
        setColumn (COLUMN_APPLICATION_ID, applicationId);
    }
    
    public void setCurrentCommuneID (final int currentCommuneID) {
        setColumn (COLUMN_CURRENT_COMMUNE_ID, currentCommuneID);
    }

    public Object ejbFindByApplicationId (final int applicationId)
        throws FinderException {
        final StringBuffer sql = new StringBuffer ();
        sql.append ("select * from " + ENTITY_NAME);
        sql.append (" where " + COLUMN_APPLICATION_ID + " = " + applicationId);
        return idoFindOnePKBySQL (sql.toString ());
    }
}
