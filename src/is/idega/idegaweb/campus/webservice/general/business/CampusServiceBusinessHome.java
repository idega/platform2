package is.idega.idegaweb.campus.webservice.general.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface CampusServiceBusinessHome extends IBOHome {
	public CampusServiceBusiness create() throws CreateException, RemoteException;
}