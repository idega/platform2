package se.cubecon.bun24.viewpoint.data;

import com.idega.data.IDOHome;
import javax.ejb.*;

/**
 * Last modified: $Date: 2003/05/15 11:44:11 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.2 $
 */
public interface RoadResponsibleHome extends IDOHome {
    RoadResponsible create() throws CreateException;
    RoadResponsible [] findAll () throws FinderException;
    RoadResponsible findByPrimaryKey (Integer primaryKey)
        throws FinderException;
}
