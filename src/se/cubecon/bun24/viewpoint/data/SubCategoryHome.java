package se.cubecon.bun24.viewpoint.data;

import com.idega.data.IDOHome;
import java.rmi.RemoteException;
import javax.ejb.*;

/**
 * Last modified: $Date: 2002/12/28 11:52:17 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.3 $
 */
public interface SubCategoryHome extends IDOHome {
    SubCategory create() throws CreateException;
    SubCategory [] findSubCategories (int topCategoryId)
        throws FinderException, RemoteException;
    SubCategory findByPrimaryKey (Integer primaryKey) throws FinderException;
    SubCategory findSubCategoryByName(String name) throws FinderException;
}
