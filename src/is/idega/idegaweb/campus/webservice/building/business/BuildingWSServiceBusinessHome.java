package is.idega.idegaweb.campus.webservice.building.business;


import javax.ejb.CreateException;
import java.rmi.RemoteException;
import com.idega.business.IBOHome;

public interface BuildingWSServiceBusinessHome extends IBOHome {
	public BuildingWSServiceBusiness create() throws CreateException,
			RemoteException;
}