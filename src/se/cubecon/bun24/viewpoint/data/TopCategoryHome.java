package se.cubecon.bun24.viewpoint.data;

import com.idega.data.IDOHome;
import java.rmi.RemoteException;
import javax.ejb.*;

/**
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 */
public interface TopCategoryHome extends IDOHome {
    TopCategory create() throws CreateException;
    TopCategory [] findAll () throws FinderException, RemoteException;
}
