package se.idega.idegaweb.commune.account.citizen.data;

import com.idega.data.IDOEntity;
import java.rmi.RemoteException;

/**
 * Last modified: $Date: 2002/11/20 11:50:59 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.3 $
 */
public interface CitizenApplicantChildren extends IDOEntity {
    int getApplicationId () throws RemoteException;
    String getFirstName () throws RemoteException;
    String getLastName () throws RemoteException;
    String getSsn () throws RemoteException;

    void setApplicationId (int applicationId) throws RemoteException;
    void setFirstName (String firstName) throws RemoteException;
    void setLastName (String lastName) throws RemoteException;
    void setSsn (String ssn) throws RemoteException;
}
