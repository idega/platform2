package se.idega.idegaweb.commune.account.citizen.data;

import com.idega.data.IDOHome;
import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.*;

/**
 * Last modified: $Date: 2003/01/14 14:19:37 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.2 $
 */
public interface CitizenApplicantMovingToHome extends IDOHome {
    public CitizenApplicantMovingTo create() throws CreateException,
                                                       RemoteException;
    public CitizenApplicantMovingTo findByApplicationId (int applicantId)
        throws FinderException, RemoteException;
}
