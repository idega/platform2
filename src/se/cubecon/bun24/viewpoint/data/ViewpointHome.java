package se.cubecon.bun24.viewpoint.data;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOHome;
import com.idega.user.data.Group;

/**
 * Last modified: $Date: 2003/04/02 16:12:22 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.3 $
 */
public interface ViewpointHome extends IDOHome {
    Viewpoint create() throws CreateException, RemoteException;
    Viewpoint findByPrimaryKey (Integer primaryKey) throws FinderException;
    Viewpoint [] findUnhandledViewpointsInGroups (Group [] groups)
        throws FinderException, RemoteException;
}
