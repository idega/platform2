package se.cubecon.bun24.viewpoint.business;

import com.idega.business.IBOHome;
import java.rmi.RemoteException;
import javax.ejb.CreateException;

/**
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 */
public interface ViewpointBusinessHome extends IBOHome {
    ViewpointBusiness create() throws CreateException, RemoteException;
}
