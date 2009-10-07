package is.idega.idegaweb.campus.business;


import javax.ejb.CreateException;
import java.rmi.RemoteException;
import com.idega.business.IBOHome;

public interface CampusApartmentsImportHome extends IBOHome {
	public CampusApartmentsImport create() throws CreateException,
			RemoteException;
}