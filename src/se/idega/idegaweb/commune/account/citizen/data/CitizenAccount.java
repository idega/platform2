package se.idega.idegaweb.commune.account.citizen.data;

import com.idega.block.process.data.Case;
import com.idega.data.IDOEntity;
import java.rmi.RemoteException;
import java.util.Date;
import javax.ejb.*;
import se.idega.idegaweb.commune.account.data.AccountApplication;

public interface CitizenAccount extends IDOEntity, Case, AccountApplication {
    String getCaseCodeDescription () throws RemoteException;
    String getCaseCodeKey () throws RemoteException;

    String getApplicantName () throws RemoteException;
    String getEmail () throws RemoteException;
    String getPID () throws RemoteException;
    Date getBirthDate () throws RemoteException;
    String getPhoneHome () throws RemoteException;
    String getPhoneWork () throws RemoteException;
    String getCustodian1Pid () throws RemoteException;
    String getCustodian1CivilStatus () throws RemoteException;
    String getCustodian2Pid () throws RemoteException;
    String getCustodian2CivilStatus () throws RemoteException;
    String getStreet () throws RemoteException;
    String getZipCode () throws RemoteException;
    String getCity () throws RemoteException;
    Integer getGenderId () throws RemoteException;

    void setApplicantName (String name)throws RemoteException;
    void setEmail (String email) throws RemoteException;
    void setPID (String pid) throws RemoteException;
    void setBirthDate (Date date) throws RemoteException;
    void setPhoneHome (String phoneHome) throws RemoteException;
    void setPhoneWork (String phoneWork) throws RemoteException;
    void setCustodian1Pid (String pid) throws RemoteException;
    void setCustodian1CivilStatus (String civilStatus) throws RemoteException;
    void setCustodian2Pid (String pid) throws RemoteException;
    void setCustodian2CivilStatus (String civilStatus) throws RemoteException;
    void setStreet (String street) throws RemoteException;
    void setZipCode (String zipCode) throws RemoteException;
    void setCity (String city) throws RemoteException;
    void setGenderId (int id) throws RemoteException;
}
