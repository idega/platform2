package is.idega.idegaweb.campus.block.application.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface ApplicationSessionHome extends IBOHome {
	public ApplicationSession create() throws CreateException, RemoteException;
}