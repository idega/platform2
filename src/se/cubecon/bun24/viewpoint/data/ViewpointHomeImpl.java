package se.cubecon.bun24.viewpoint.data;

import com.idega.data.*;
import com.idega.user.data.Group;
import java.util.Collection;
import javax.ejb.*;
import java.rmi.RemoteException;

/**
 * Last modified: $Date: 2002/10/23 10:00:36 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.2 $
 */
public class ViewpointHomeImpl extends IDOFactory implements ViewpointHome {
    public Viewpoint create () throws CreateException{
        return (Viewpoint) createIDO ();
    }

    public Viewpoint findByPrimaryKey (final Integer primaryKey)
        throws FinderException {
        return (Viewpoint) findByPrimaryKeyIDO (primaryKey);
    }

    protected Class getEntityInterfaceClass() {
        return Viewpoint.class;
    }

    public Viewpoint [] findUnhandledViewpointsInGroups (final Group [] groups)
        throws FinderException, RemoteException {
        final IDOEntity entity = idoCheckOutPooledEntity();
        final Collection ids
                = ((ViewpointBMPBean)entity).ejbFindUnhandledViewpointsInGroups
                (groups);
        idoCheckInPooledEntity (entity);
        return (Viewpoint []) getEntityCollectionForPrimaryKeys(ids).toArray
                (new Viewpoint [0]);
    }
}
