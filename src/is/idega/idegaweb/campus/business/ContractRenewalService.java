package is.idega.idegaweb.campus.business;


import is.idega.idegaweb.campus.data.ContractRenewalOffer;
import is.idega.idegaweb.campus.block.allocation.business.ContractService;
import java.rmi.RemoteException;
import is.idega.idegaweb.campus.data.ContractRenewalOfferHome;
import com.idega.business.IBOService;
import java.util.Collection;

public interface ContractRenewalService extends IBOService {
	/**
	 * @see is.idega.idegaweb.campus.business.ContractRenewalServiceBean#getContractRenewalOffers
	 */
	public Collection getContractRenewalOffers() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.ContractRenewalServiceBean#getContractRenewalOfferHome
	 */
	public ContractRenewalOfferHome getContractRenewalOfferHome()
			throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.ContractRenewalServiceBean#sendOffer
	 */
	public void sendOffer() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.ContractRenewalServiceBean#getOfferByUUID
	 */
	public ContractRenewalOffer getOfferByUUID(String uuid)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.ContractRenewalServiceBean#getCampusService
	 */
	public CampusService getCampusService() throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.ContractRenewalServiceBean#getContractService
	 */
	public ContractService getContractService() throws RemoteException,
			RemoteException;
}