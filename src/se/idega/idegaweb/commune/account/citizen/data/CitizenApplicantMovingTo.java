package se.idega.idegaweb.commune.account.citizen.data;

import com.idega.data.IDOEntity;
import java.rmi.RemoteException;

/**
 * Last modified: $Date: 2002/12/12 13:06:59 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.2 $
 */
public interface CitizenApplicantMovingTo extends IDOEntity {
    int getApplicationId () throws RemoteException;
    String getAddress () throws RemoteException;
    String getMovingInDate () throws RemoteException;
    String getHousingType () throws RemoteException;
    String getPropertyType () throws RemoteException;
    String getLandlord () throws RemoteException;

    void setApplicationId (int applicationId) throws RemoteException;
    void setAddress (String address) throws RemoteException;
    void setMovingInDate (String movingInDate) throws RemoteException;
    void setHousingType (String housingType) throws RemoteException;
    void setPropertyType (String propertyType) throws RemoteException;
    void setLandlord (String name, String phone, String address)
        throws RemoteException;
}
