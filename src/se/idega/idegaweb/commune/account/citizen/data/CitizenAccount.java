package se.idega.idegaweb.commune.account.citizen.data;

import com.idega.block.process.data.Case;
import com.idega.data.IDOEntity;
import java.rmi.RemoteException;
import javax.ejb.*;
import se.idega.idegaweb.commune.account.data.AccountApplication;

public interface CitizenAccount extends IDOEntity, Case, AccountApplication {
    String getApplicantName ()throws RemoteException;
    String getCaseCodeDescription () throws RemoteException;
    String getCaseCodeKey () throws RemoteException;
    String getEmail () throws RemoteException;
    String getPID () throws RemoteException;
    String getPhoneHome () throws RemoteException;
    String getPhoneWork () throws RemoteException;

    void setApplicantName (String name)throws RemoteException;
    void setEmail (String email) throws RemoteException;
    void setPID (String pid) throws RemoteException;
    void setPhoneHome (String phoneHome) throws RemoteException;
    void setPhoneWork (String phoneWork) throws RemoteException;
    void setCustodian (String custodian) throws RemoteException;
    void setCivilStatus (String civilStatus) throws RemoteException;
    void setStreet (String street) throws RemoteException;
    void setZipCode (String zipCode) throws RemoteException;
    void setCity (String city) throws RemoteException;
}
