package se.idega.idegaweb.commune.account.citizen.data;

import com.idega.data.IDOEntity;
import java.rmi.RemoteException;

/**
 * Last modified: $Date: 2003/10/05 20:03:03 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.2 $
 */
public interface CitizenApplicantPutChildren extends IDOEntity {
    int getApplicationId ();
    String getCurrentKommun ();

    void setApplicationId (int applicationId);
    void setCurrentKommun (String currentKommun);
}
