package se.idega.idegaweb.commune.account.citizen.data;

import com.idega.data.IDOEntity;

/**
 * Last modified: $Date: 2003/10/06 13:03:10 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.5 $
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
