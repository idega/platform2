package se.idega.idegaweb.commune.account.citizen.data;

import com.idega.data.IDOEntity;
import java.rmi.RemoteException;

/**
 * Last modified: $Date: 2002/11/15 08:34:29 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.1 $
 */
public interface CitizenApplicantPutChildren extends IDOEntity {
    int getApplicationId () throws RemoteException;
    String getCurrentKommun () throws RemoteException;

    void setApplicationId (int applicationId) throws RemoteException;
    void setCurrentKommun (String currentKommun) throws RemoteException;
}
