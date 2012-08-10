package is.idega.idegaweb.campus.business;


import is.idega.idegaweb.campus.data.ContractRenewalOffer;
import is.idega.idegaweb.campus.block.allocation.business.ContractService;
import java.rmi.RemoteException;
import java.util.Locale;
import is.idega.idegaweb.campus.data.ContractRenewalOfferHome;
import com.idega.business.IBOService;
import com.idega.presentation.IWContext;
import java.util.Collection;

public interface ContractRenewalService extends IBOService {
	/**
	 * @see is.idega.idegaweb.campus.business.ContractRenewalServiceBean#getContractRenewalOffers
	 */
	public Collection getContractRenewalOffers() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.ContractRenewalServiceBean#getContractRenewalOffers
	 */
	public Collection getContractRenewalOffers(Integer status, Integer complex,
			Integer building) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.ContractRenewalServiceBean#setRenewalGranted
	 */
	public void setRenewalGranted(String uuid, String status)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.ContractRenewalServiceBean#getContractRenewalOfferHome
	 */
	public ContractRenewalOfferHome getContractRenewalOfferHome()
			throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.ContractRenewalServiceBean#sendOffer
	 */
	public void sendOffer(Locale locale) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.ContractRenewalServiceBean#sendReminder
	 */
	public void sendReminder(Locale locale) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.ContractRenewalServiceBean#sendContract
	 */
	public void sendContract(IWContext iwc) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.ContractRenewalServiceBean#closeOffer
	 */
	public void closeOffer(Locale locale) throws RemoteException;

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