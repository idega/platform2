package se.cubecon.bun24.viewpoint.data;

import com.idega.data.IDOHome;
import java.rmi.RemoteException;
import javax.ejb.*;

/**
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 */
public interface SubCategoryHome extends IDOHome {
    SubCategory create() throws CreateException;
    SubCategory [] findSubCategories (int topCategoryId)
        throws FinderException, RemoteException;
    SubCategory findByPrimaryKey (Integer primaryKey) throws FinderException;
}
