package se.cubecon.bun24.viewpoint.data;

import com.idega.data.*;
import java.util.*;
import javax.ejb.*;
import java.rmi.RemoteException;

/**
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 */
public class SubCategoryHomeImpl extends IDOFactory implements SubCategoryHome {
    public SubCategory create () throws CreateException{
        return (SubCategory) createIDO ();
    }

    public SubCategory findByPrimaryKey (final Integer primaryKey)
        throws FinderException {
        return (SubCategory) findByPrimaryKeyIDO (primaryKey);
    }

    public SubCategory [] findSubCategories (final int topCategoryId)
        throws FinderException, RemoteException {
        final IDOEntity entity = idoCheckOutPooledEntity();
        final Collection ids = ((SubCategoryBMPBean)entity).ejbFindSubCategories
                (topCategoryId);
        idoCheckInPooledEntity (entity);
        return (SubCategory []) getEntityCollectionForPrimaryKeys(ids).toArray
                (new SubCategory [0]);
    }

    protected Class getEntityInterfaceClass() {
        return SubCategory.class;
    }
}
