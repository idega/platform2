package is.idega.idegaweb.travel.business;

import com.idega.business.IBOHome;


/**
 * @author gimmi
 */
public interface TravelSessionManagerHome extends IBOHome {

	public TravelSessionManager create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
