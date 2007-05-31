package is.idega.idegaweb.campus.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface CampusServiceHome extends IBOHome {
	public CampusService create() throws CreateException, RemoteException;
}