package se.cubecon.bun24.viewpoint.data;

import java.rmi.RemoteException;
import javax.ejb.FinderException;
import java.util.Collection;
import com.idega.data.IDOEntity;
import com.idega.user.data.Group;

/**
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
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
