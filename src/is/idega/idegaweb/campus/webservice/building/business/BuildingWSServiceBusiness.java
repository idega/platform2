package is.idega.idegaweb.campus.webservice.building.business;


import is.idega.idegaweb.campus.webservice.building.server.ApartmentInfo;
import is.idega.idegaweb.campus.webservice.building.server.BuildingInfo;
import is.idega.idegaweb.campus.webservice.building.server.ComplexInfo;
import java.rmi.RemoteException;
import com.idega.business.IBOService;

public interface BuildingWSServiceBusiness extends IBOService {
	/**
	 * @see is.idega.idegaweb.campus.webservice.building.business.BuildingWSServiceBusinessBean#getComplexInfo
	 */
	public ComplexInfo[] getComplexInfo() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.webservice.building.business.BuildingWSServiceBusinessBean#getBuildingInfo
	 */
	public BuildingInfo[] getBuildingInfo(ComplexInfo complex)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.webservice.building.business.BuildingWSServiceBusinessBean#getApartmentInfo
	 */
	public ApartmentInfo[] getApartmentInfo(BuildingInfo building)
			throws RemoteException;
}