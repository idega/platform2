package se.cubecon.bun24.viewpoint.data;

import java.rmi.RemoteException;
import javax.ejb.FinderException;
import java.util.Collection;
import com.idega.data.IDOEntity;
import com.idega.user.data.Group;

/**
 * Last modified: $Date: 2002/10/23 10:00:36 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.2 $
 */
public interface SubCategory extends IDOEntity {
    String getName ();
    Group getHandlerGroup ();

    void setName (String name);
    void setTopCategoryId (int id);
    void setHandlerGroupId (int id);

    Collection ejbFindSubCategories (int topCategoryId)
        throws FinderException, RemoteException;
}
