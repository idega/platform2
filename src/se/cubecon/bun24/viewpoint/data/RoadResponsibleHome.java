package se.cubecon.bun24.viewpoint.data;

import com.idega.data.IDOHome;
import java.rmi.RemoteException;
import javax.ejb.*;

/**
 * Last modified: $Date: 2003/05/15 06:58:34 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.1 $
 */
public interface RoadResponsibleHome extends IDOHome {
    RoadResponsible create() throws CreateException;
    RoadResponsible [] findAll () throws FinderException;
    RoadResponsible findByPrimaryKey (Integer primaryKey)
        throws FinderException;
}
