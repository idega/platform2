package se.idega.idegaweb.commune.account.citizen.business;

import com.idega.business.IBOService;
import com.idega.user.data.User;
import java.rmi.RemoteException;
import javax.ejb.*;
import se.idega.idegaweb.commune.account.business.AccountBusiness;
import se.idega.idegaweb.commune.account.citizen.data.CitizenAccount;
import java.util.*;

public interface CitizenAccountBusiness extends IBOService, AccountBusiness {
    void acceptApplication (int p0, User p1)
        throws RemoteException, CreateException, FinderException;
    CitizenAccount getAccount (int p0)
        throws RemoteException,FinderException, RemoteException;
    List getListOfUnapprovedApplications () throws RemoteException;
    User getUser (String p0) throws RemoteException;
    boolean insertApplication
        (User user, String pid, String email, String phoneHome,
         String phoneWork) throws RemoteException;
    boolean insertApplication
        (String name, String pid, Date birthDate, String email,
         String phoneHome, String phoneWork,
         String custodian1Pid, String custodian1CivilStatus,
         String custodian2Pid, String custodian2CivilStatus,
         String street, String zipCode, String city) throws RemoteException;
    void rejectApplication (int p0, User p1, String p2 )
        throws RemoteException,CreateException,FinderException, RemoteException;
}
