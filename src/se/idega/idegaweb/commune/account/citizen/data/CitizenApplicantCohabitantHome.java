package se.idega.idegaweb.commune.account.citizen.data;

import com.idega.data.IDOHome;
import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.*;

/**
 * Last modified: $Date: 2002/11/15 08:34:29 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.1 $
 */
public interface CitizenApplicantCohabitantHome extends IDOHome {
    public CitizenApplicantCohabitant create() throws CreateException,
                                                       RemoteException;
    public Collection findByApplicationId (int applicantId)
        throws FinderException, RemoteException;
}
