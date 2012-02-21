package is.idega.idegaweb.campus.business;

import is.idega.idegaweb.campus.block.allocation.business.ContractService;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean;
import is.idega.idegaweb.campus.data.ContractRenewalOffer;
import is.idega.idegaweb.campus.data.ContractRenewalOfferHome;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.business.IBOServiceBean;
import com.idega.core.idgenerator.business.UUIDGenerator;
import com.idega.util.IWTimestamp;

public class ContractRenewalServiceBean extends IBOServiceBean implements
		ContractRenewalService {

	public Collection getContractRenewalOffers() {
		try {
			return getContractRenewalOfferHome().findAllOpen();
		} catch (RemoteException e) {
		} catch (FinderException e) {
		}
		
		return null;
	}
	
	public ContractRenewalOfferHome getContractRenewalOfferHome() throws RemoteException {
		return (ContractRenewalOfferHome) getIDOHome(ContractRenewalOffer.class);
	}
	
	public void sendOffer() {
		try {
			Collection contracts = getContractService().getContractHome().findByStatus(ContractBMPBean.STATUS_SIGNED);
			if (contracts != null && !contracts.isEmpty()) {
				Iterator it = contracts.iterator();
				while (it.hasNext()) {
					Contract contract = (Contract) it.next();
					ContractRenewalOffer offer = getContractRenewalOfferHome().create();
					offer.setContract(contract);
					offer.setUser(contract.getUser());
					offer.setIsOfferClosed(false);
					offer.setOfferSentDate(IWTimestamp.getTimestampRightNow());
					offer.setUniqueId(UUIDGenerator.getInstance().generateUUID());
					offer.store();
					
					//Send mail...
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		}
	}
	
	public ContractRenewalOffer getOfferByUUID(String uuid) {
		try {
			return getContractRenewalOfferHome().findByUUID(uuid);
		} catch (RemoteException e) {
		} catch (FinderException e) {
		}
		
		return null;
	}
	
	public CampusService getCampusService() throws RemoteException {
		return (CampusService) getServiceInstance(CampusService.class);
	}

	public ContractService getContractService() throws RemoteException {
		return (ContractService) getServiceInstance(ContractService.class);
	}

}
