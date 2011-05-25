package is.idega.idegaweb.campus.business;


import javax.ejb.CreateException;
import java.rmi.RemoteException;
import com.idega.business.IBOHome;

public interface CampusServiceHome extends IBOHome {
	public CampusService create() throws CreateException, RemoteException;
}