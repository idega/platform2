package se.cubecon.bun24.viewpoint.business;

import com.idega.business.IBOHome;
import java.rmi.RemoteException;
import javax.ejb.CreateException;

/**
 * Last modified: $Date: 2002/10/23 10:00:36 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.2 $
 */
public interface ViewpointBusinessHome extends IBOHome {
    ViewpointBusiness create() throws CreateException, RemoteException;
}
