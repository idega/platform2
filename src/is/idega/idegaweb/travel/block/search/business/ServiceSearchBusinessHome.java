package is.idega.idegaweb.travel.block.search.business;

import com.idega.business.IBOHome;


/**
 * @author gimmi
 */
public interface ServiceSearchBusinessHome extends IBOHome {

	public ServiceSearchBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
