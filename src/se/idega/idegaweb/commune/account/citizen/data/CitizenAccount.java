package se.idega.idegaweb.commune.account.citizen.data;

import com.idega.block.process.data.Case;
import com.idega.data.IDOEntity;
import java.rmi.RemoteException;
import java.util.Date;
import javax.ejb.*;
import se.idega.idegaweb.commune.account.data.AccountApplication;

public interface CitizenAccount extends IDOEntity, Case, AccountApplication {
	final static String PUT_CHILDREN_IN_NACKA_KEY = "caa_put_children_in_nacka";
	final static String MOVING_TO_NACKA_KEY = "caa_moving_to_nacka";

    String getCaseCodeDescription () throws RemoteException;
    String getCaseCodeKey () throws RemoteException;

    String getApplicantName () throws RemoteException;
    String getSsn () throws RemoteException;
    String getEmail () throws RemoteException;
    String getPhoneHome () throws RemoteException;
    String getPhoneWork () throws RemoteException;
    String getStreet () throws RemoteException;
    String getZipCode () throws RemoteException;
    String getCity () throws RemoteException;
    String getCivilStatus () throws RemoteException;
    boolean hasCohabitant () throws RemoteException;
    int getChildrenCount () throws RemoteException;
    String getApplicationReason () throws RemoteException;

    void setApplicantName (String name)throws RemoteException;
    void setSsn (String ssn) throws RemoteException;
    void setEmail (String email) throws RemoteException;
    void setPhoneHome (String phoneHome) throws RemoteException;
    void setPhoneWork (String phoneWork) throws RemoteException;
    void setStreet (String street) throws RemoteException;
    void setZipCode (String zipCode) throws RemoteException;
    void setCity (String city) throws RemoteException;
    void setCivilStatus (String civilStatus) throws RemoteException;
    void setHasCohabitant (boolean hasCohabitant) throws RemoteException;
    void setChildrenCount (int childrenCount) throws RemoteException;
    void setApplicationReason (String applicationReason) throws RemoteException;
}
