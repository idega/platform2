package se.cubecon.bun24.viewpoint.data;

import com.idega.data.*;
import java.util.Collection;
import javax.ejb.*;

/**
 * Last modified: $Date: 2003/05/15 06:58:34 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.1 $
 */
public class RoadResponsibleHomeImpl extends IDOFactory
    implements RoadResponsibleHome {

    public RoadResponsible create () throws CreateException{
        return (RoadResponsible) createIDO ();
    }

    public RoadResponsible findByPrimaryKey (final Integer primaryKey)
        throws FinderException {
        return (RoadResponsible) findByPrimaryKeyIDO (primaryKey);
    }

    public RoadResponsible [] findAll () throws FinderException {
        final IDOEntity entity = idoCheckOutPooledEntity();
        final Collection ids = ((RoadResponsibleBMPBean)entity).ejbFindAll ();
        idoCheckInPooledEntity (entity);
        return (RoadResponsible [])
                getEntityCollectionForPrimaryKeys(ids).toArray
                (new RoadResponsible [0]);
    }
    
    protected Class getEntityInterfaceClass() {
        return RoadResponsible.class;
    }
}
