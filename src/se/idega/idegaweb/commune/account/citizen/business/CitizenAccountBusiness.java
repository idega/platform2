package se.idega.idegaweb.commune.account.citizen.business;

import com.idega.business.IBOService;
import com.idega.core.accesscontrol.business.UserHasLoginException;
import com.idega.user.data.*;
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

    Gender [] getGenders () throws RemoteException;

    User getUser (String p0) throws RemoteException;

	/**
	 * Creates an application for CitizenAccount for a user with a personalId that is in the system.
	 * @param user The user that makes the application
	 * @param pid 	The PersonalId of the User to apply for.
	 * @param email Email of the user
	 * @param phoneHome the Home phone of the user
	 * @param phoneWork the Work phone of the user
	 * @return Integer appliaction id or null if insertion was unsuccessful
	 * @throws UserHasLoginException If A User already has a login in the system.
	 */
    Integer insertApplication(User user, String pid, String email,
                              String phoneHome, String phoneWork) 
        throws RemoteException, UserHasLoginException;
    
    Integer insertApplication
        (String name, String ssn, String email, String phoneHome,
         String phoneWork, Date birthDate, String street, String zipCode,
         String city, int genderId, String civilStatus, boolean hasCohabitant,
         int childrenCount, String applicationReason)
        throws RemoteException;

    void rejectApplication (int p0, User p1, String p2 )
        throws RemoteException,CreateException,FinderException, RemoteException;

    Integer insertCohabitant (Integer applicationId, String firstName,
                              String lastName, String ssn, String civilStatus,
                              String phoneWork)
        throws RemoteException,CreateException;
}
