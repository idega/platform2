package se.idega.idegaweb.commune.account.citizen.data;

import com.idega.data.IDOEntity;
import java.rmi.RemoteException;
import javax.ejb.FinderException;

/**
 * Last modified: $Date: 2002/11/22 12:58:04 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.4 $
 */
public interface CitizenApplicantCohabitant extends IDOEntity {
    int getApplicationId () throws RemoteException;
    String getFirstName () throws RemoteException;
    String getLastName () throws RemoteException;
    String getSsn () throws RemoteException;
    String getCivilStatus () throws RemoteException;
    String getPhoneWork () throws RemoteException;

    void setApplicationId (int applicationId) throws RemoteException;
    void setFirstName (String firstName) throws RemoteException;
    void setLastName (String lastName) throws RemoteException;
    void setSsn (String ssn) throws RemoteException;
    void setCivilStatus (String civilStatus) throws RemoteException;
    void setPhoneWork (String phoneWork) throws RemoteException;
}
