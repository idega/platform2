package is.idega.block.nationalregister.business;

import is.idega.block.nationalregister.data.NationalRegister;
import java.rmi.RemoteException;
import javax.ejb.CreateException;
import com.idega.business.IBOLookupException;
import com.idega.business.IBOService;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * @author Joakim
 *
 */
public interface NationalRegisterBusiness extends IBOService {

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterBusinessBean#getEntryBySSN
	 */
	public NationalRegister getEntryBySSN(String ssn) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterBusinessBean#updateEntry
	 */
	public boolean updateEntry(String symbol, String oldId, String ssn, String familyId, String name, String commune,
			String street, String building, String floor, String sex, String maritialStatus, String empty,
			String prohibitMarking, String nationality, String placeOfBirth, String spouseSSN, String fate,
			String parish, String po, String address, String addressCode, String dateOfModification,
			String placementCode, String dateOfCreation, String lastDomesticAddress, String agentSsn, String sNew,
			String addressName, String dateOfDeletion, String newSsnOrName, String dateOfBirth, Group citizenGroup)
			throws java.rmi.RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterBusinessBean#updateUserPersonalID
	 */
	public void updateUserPersonalID(String oldPersonalID, String newPersonalID) throws IBOLookupException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterBusinessBean#updateUserOldID
	 */
	public void updateUserOldID(String oldID, String personalID) throws IBOLookupException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterBusinessBean#updateUserAddress
	 */
	public void updateUserAddress(User user, UserBusiness userBiz, String address, String po) throws RemoteException,
			CreateException, java.rmi.RemoteException;
}
