/**
 * 
 */
package is.idega.idegaweb.campus.business;

import is.idega.idegaweb.campus.block.allocation.business.ContractService;
import is.idega.idegaweb.campus.block.application.business.ApplicationService;
import is.idega.idegaweb.campus.block.application.data.SchoolHome;

import java.rmi.RemoteException;

import com.idega.block.building.business.BuildingService;
import com.idega.block.finance.business.FinanceService;
import com.idega.business.IBOService;

/**
 * @author bluebottle
 *
 */
public interface CampusService extends IBOService {
	/**
	 * @see is.idega.idegaweb.campus.business.CampusServiceBean#getCampusSettings
	 */
	public CampusSettings getCampusSettings() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.CampusServiceBean#storeSettings
	 */
	public void storeSettings(CampusSettings settings)
			throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.CampusServiceBean#getSchoolHome
	 */
	public SchoolHome getSchoolHome() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.CampusServiceBean#getContractService
	 */
	public ContractService getContractService() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.CampusServiceBean#getApplicationService
	 */
	public ApplicationService getApplicationService() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.CampusServiceBean#getBuildingService
	 */
	public BuildingService getBuildingService() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.CampusServiceBean#getFinanceService
	 */
	public FinanceService getFinanceService() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.CampusServiceBean#getUserService
	 */
	public CampusUserService getUserService() throws RemoteException;

}
