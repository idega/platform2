package se.cubecon.bun24.viewpoint.data;

import com.idega.data.IDOHome;
import java.rmi.RemoteException;
import javax.ejb.*;

/**
 * Last modified: $Date: 2002/10/23 10:00:36 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.2 $
 */
public interface SubCategoryHome extends IDOHome {
    SubCategory create() throws CreateException;
    SubCategory [] findSubCategories (int topCategoryId)
        throws FinderException, RemoteException;
    SubCategory findByPrimaryKey (Integer primaryKey) throws FinderException;
}
