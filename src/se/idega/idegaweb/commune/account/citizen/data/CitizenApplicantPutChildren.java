package se.idega.idegaweb.commune.account.citizen.data;

import com.idega.data.IDOEntity;

/**
 * Last modified: $Date: 2003/10/06 12:29:53 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.3 $
 */
public interface CitizenApplicantPutChildren extends IDOEntity {
    int getApplicationId ();
    String getCurrentKommun ();

    void setApplicationId (int applicationId);
    void setCurrentKommun (String currentKommun);
}
