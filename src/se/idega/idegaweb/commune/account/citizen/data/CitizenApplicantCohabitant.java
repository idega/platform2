package se.idega.idegaweb.commune.account.citizen.data;

import java.rmi.RemoteException;

import com.idega.data.IDOEntity;

/**
 * Last modified: $Date: 2003/10/05 20:03:03 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.6 $
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
