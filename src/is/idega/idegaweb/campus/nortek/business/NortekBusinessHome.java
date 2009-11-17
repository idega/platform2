package is.idega.idegaweb.campus.nortek.business;


import javax.ejb.CreateException;
import java.rmi.RemoteException;
import com.idega.business.IBOHome;

public interface NortekBusinessHome extends IBOHome {
	public NortekBusiness create() throws CreateException, RemoteException;
}