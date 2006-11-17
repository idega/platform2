package is.idega.idegaweb.campus.nortek.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface NortekBusinessHome extends IBOHome {
	public NortekBusiness create() throws CreateException, RemoteException;
}