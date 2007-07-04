package is.idega.idegaweb.campus.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface CampusApplicationsImportHandlerHome extends IBOHome {
	public CampusApplicationsImportHandler create() throws CreateException, RemoteException;
}