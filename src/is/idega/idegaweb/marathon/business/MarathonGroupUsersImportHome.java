package is.idega.idegaweb.marathon.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface MarathonGroupUsersImportHome extends IBOHome {

	public MarathonGroupUsersImport create() throws CreateException, RemoteException;
}