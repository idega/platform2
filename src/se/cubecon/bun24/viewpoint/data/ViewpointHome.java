package se.cubecon.bun24.viewpoint.data;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOHome;
import com.idega.user.data.Group;

/**
 * Last modified: $Date: 2003/11/10 18:57:57 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.4 $
 */
public interface ViewpointHome extends IDOHome {
    Viewpoint create() throws CreateException;
    Viewpoint findByPrimaryKey (Integer primaryKey) throws FinderException;
    Viewpoint [] findUnhandledViewpointsInGroups (Group [] groups)
        throws FinderException;
}
