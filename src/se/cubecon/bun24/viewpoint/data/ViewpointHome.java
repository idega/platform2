package se.cubecon.bun24.viewpoint.data;

import com.idega.data.IDOHome;
import java.rmi.RemoteException;
import javax.ejb.*;
import com.idega.user.data.Group;
import java.rmi.RemoteException;

/**
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 */
public interface ViewpointHome extends IDOHome {
    Viewpoint create() throws CreateException, RemoteException;
    Viewpoint findByPrimaryKey (Integer primaryKey) throws FinderException;
    Viewpoint [] findUnhandledViewpointsInGroups (Group [] groups)
        throws FinderException, RemoteException;
}
