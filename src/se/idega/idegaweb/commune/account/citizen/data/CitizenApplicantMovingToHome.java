package se.idega.idegaweb.commune.account.citizen.data;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * Last modified: $Date: 2003/04/02 16:12:21 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.3 $
 */
public interface CitizenApplicantMovingToHome extends IDOHome {
    public CitizenApplicantMovingTo create() throws CreateException,
                                                       RemoteException;
    public CitizenApplicantMovingTo findByApplicationId (int applicantId)
        throws FinderException, RemoteException;
}
