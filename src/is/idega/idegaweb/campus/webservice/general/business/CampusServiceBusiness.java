package is.idega.idegaweb.campus.webservice.general.business;


import java.util.Collection;
import com.idega.business.IBOService;
import java.rmi.RemoteException;

public interface CampusServiceBusiness extends IBOService {
	/**
	 * @see is.idega.idegaweb.campus.webservice.general.business.CampusServiceBusinessBean#getRentedContractsForComplex
	 */
	public Collection getRentedContractsForComplex(int complexID) throws RemoteException;
}