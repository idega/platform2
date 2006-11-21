package is.idega.block.nationalregister.business;


import is.idega.block.nationalregister.data.NationalRegisterDeceased;
import com.idega.business.IBOService;

public interface NationalRegisterDeceasedBusiness extends IBOService {
	
	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterBusinessBean#getEntryBySSN
	 */
	public NationalRegisterDeceased getEntryBySSN(String ssn) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterBusinessBean#updateEntry
	 */
	public boolean updateEntry(String symbol, String ssn, String dateOfDeath, String name,
			String street, String commune, String gender, String maritialStatus, String spouseSSN)
			throws java.rmi.RemoteException;

}