package se.idega.idegaweb.commune.account.citizen.data;

import com.idega.data.IDOHome;
import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.*;

/**
 * Last modified: $Date: 2002/11/22 12:58:04 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.2 $
 */
public interface CitizenApplicantChildrenHome extends IDOHome {
    public CitizenApplicantChildren create() throws CreateException,
                                                       RemoteException;
    public CitizenApplicantChildren [] findByApplicationId (int applicantId)
        throws FinderException, RemoteException;
}
