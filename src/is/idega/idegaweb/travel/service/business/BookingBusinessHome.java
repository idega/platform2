package is.idega.idegaweb.travel.service.business;

import com.idega.business.IBOHome;


/**
 * @author gimmi
 */
public interface BookingBusinessHome extends IBOHome {

	public BookingBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
