package is.idega.idegaweb.campus.business;


import com.idega.block.building.business.BuildingService;
import is.idega.idegaweb.campus.block.allocation.business.ContractService;
import is.idega.idegaweb.campus.block.application.data.ApartmentCategoryCombinationHome;
import com.idega.block.finance.business.FinanceService;
import is.idega.idegaweb.campus.block.application.data.SchoolHome;
import com.idega.business.IBOService;
import is.idega.idegaweb.campus.block.application.business.ApplicationService;
import java.rmi.RemoteException;

public interface CampusService extends IBOService {
	/**
	 * @see is.idega.idegaweb.campus.business.CampusServiceBean#getCampusSettings
	 */
	public CampusSettings getCampusSettings() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.CampusServiceBean#storeSettings
	 */
	public void storeSettings(CampusSettings settings) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.CampusServiceBean#getSchoolHome
	 */
	public SchoolHome getSchoolHome() throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.CampusServiceBean#getApartmentCategoryCombinationHome
	 */
	public ApartmentCategoryCombinationHome getApartmentCategoryCombinationHome()
			throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.CampusServiceBean#getContractService
	 */
	public ContractService getContractService() throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.CampusServiceBean#getApplicationService
	 */
	public ApplicationService getApplicationService() throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.CampusServiceBean#getBuildingService
	 */
	public BuildingService getBuildingService() throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.CampusServiceBean#getFinanceService
	 */
	public FinanceService getFinanceService() throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.CampusServiceBean#getUserService
	 */
	public CampusUserService getUserService() throws RemoteException,
			RemoteException;
}