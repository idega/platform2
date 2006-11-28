package is.idega.idegaweb.campus.webservice.general.business;

import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.ContractHome;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;

public class CampusServiceBusinessBean extends IBOServiceBean implements
		CampusServiceBusiness {

	public Collection getRentedContractsForComplex(int complexID) {
		Collection contracts = null;
		
		try {
			contracts = getContractHome().findByComplexAndRented(new Integer(complexID), true);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		
		return contracts;
	}

	private ContractHome getContractHome() throws IDOLookupException {
		return (ContractHome) IDOLookup.getHome(Contract.class);
	}
}