package is.idega.idegaweb.travel.business;

import com.idega.business.IBOHome;


/**
 * @author gimmi
 */
public interface TravelStockroomBusinessHome extends IBOHome {

	public TravelStockroomBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
