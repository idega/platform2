package se.idega.idegaweb.commune.account.citizen.data;

import com.idega.data.IDOEntity;
import java.rmi.RemoteException;

/**
 * Last modified: $Date: 2003/10/05 20:03:03 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.4 $
 */
public interface CitizenApplicantChildren extends IDOEntity {
    int getApplicationId ();
    String getFirstName ();
    String getLastName ();
    String getSsn ();

    void setApplicationId (int applicationId);
    void setFirstName (String firstName);
    void setLastName (String lastName);
    void setSsn (String ssn);
}
