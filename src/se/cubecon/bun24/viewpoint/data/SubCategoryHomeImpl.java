package se.cubecon.bun24.viewpoint.data;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

/**
 * Last modified: $Date: 2003/11/10 19:02:07 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.5 $
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
        throws FinderException {
        final IDOEntity entity = idoCheckOutPooledEntity();
        final Collection ids = ((SubCategoryBMPBean)entity).ejbFindSubCategories
                (topCategoryId);
        idoCheckInPooledEntity (entity);
        return (SubCategory []) getEntityCollectionForPrimaryKeys(ids).toArray
                (new SubCategory [0]);
    }
    
    public SubCategory findSubCategoryByName(String name) throws FinderException {
		IDOEntity entity = idoCheckOutPooledEntity();
		Integer id = ((SubCategoryBMPBean)entity).ejbFindSubCategoryByName(name);
		idoCheckInPooledEntity(entity);
		return (SubCategory) findByPrimaryKeyIDO(id);
    }

    public SubCategory [] findAllSubCategories () throws FinderException {
        final IDOEntity entity = idoCheckOutPooledEntity();
        final Collection ids
                = ((SubCategoryBMPBean)entity).ejbFindAllSubCategories ();
        idoCheckInPooledEntity (entity);
        return (SubCategory []) getEntityCollectionForPrimaryKeys(ids).toArray
                (new SubCategory [0]);
    }

    protected Class getEntityInterfaceClass() {
        return SubCategory.class;
    }
}
