package se.cubecon.bun24.viewpoint.data;

import com.idega.data.IDOHome;
import java.rmi.RemoteException;
import javax.ejb.*;
import com.idega.user.data.Group;
import java.rmi.RemoteException;

/**
 * Last modified: $Date: 2002/10/23 10:00:36 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.2 $
 */
public interface ViewpointHome extends IDOHome {
    Viewpoint create() throws CreateException, RemoteException;
    Viewpoint findByPrimaryKey (Integer primaryKey) throws FinderException;
    Viewpoint [] findUnhandledViewpointsInGroups (Group [] groups)
        throws FinderException, RemoteException;
}
