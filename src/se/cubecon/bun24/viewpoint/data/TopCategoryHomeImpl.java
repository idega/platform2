package se.cubecon.bun24.viewpoint.data;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

/**
 * Last modified: $Date: 2003/11/10 18:59:32 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.3 $
 */
public class TopCategoryHomeImpl extends IDOFactory implements TopCategoryHome {
    public TopCategory create () throws CreateException{
        return (TopCategory) createIDO ();
    }

    public TopCategory [] findAll () throws FinderException {
        final IDOEntity entity = idoCheckOutPooledEntity();
        final Collection ids = ((TopCategoryBMPBean)entity).ejbFindAll ();
        idoCheckInPooledEntity (entity);
        return (TopCategory []) getEntityCollectionForPrimaryKeys(ids).toArray
                (new TopCategory [0]);
    }

    protected Class getEntityInterfaceClass() {
        return TopCategory.class;
    }
}
