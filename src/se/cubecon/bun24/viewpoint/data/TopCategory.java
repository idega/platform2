package se.cubecon.bun24.viewpoint.data;

import java.rmi.RemoteException;
import javax.ejb.FinderException;
import java.util.Collection;
import com.idega.data.IDOEntity;

/**
 * Last modified: $Date: 2002/10/23 10:00:36 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.2 $
 */
public interface TopCategory extends IDOEntity {
    String getName ();
    void setName (String name);
    Collection ejbFindAll () throws FinderException, RemoteException;
}
