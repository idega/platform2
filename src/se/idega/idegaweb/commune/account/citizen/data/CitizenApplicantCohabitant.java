package se.idega.idegaweb.commune.account.citizen.data;

import com.idega.data.IDOEntity;

/**
 * Last modified: $Date: 2003/10/06 12:29:53 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.7 $
 */
public interface CitizenApplicantCohabitant extends IDOEntity {
    int getApplicationId ();
    String getFirstName ();
    String getLastName ();
    String getSsn ();
    String getCivilStatus ();
    String getPhoneWork ();

    void setApplicationId (int applicationId);
    void setFirstName (String firstName);
    void setLastName (String lastName);
    void setSsn (String ssn);
    void setCivilStatus (String civilStatus);
    void setPhoneWork (String phoneWork);
}
