package is.idega.idegaweb.travel.business;

import com.idega.business.IBOHome;


/**
 * @author gimmi
 */
public interface BookerHome extends IBOHome {

	public Booker create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
