package is.idega.idegaweb.campus.block.allocation.business;


import javax.ejb.CreateException;
import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.Application;
import is.idega.idegaweb.campus.block.finance.business.CampusAssessmentBusiness;
import com.idega.block.finance.data.AccountHome;
import com.idega.block.building.business.BuildingService;
import com.idega.block.finance.data.AccountKeyHome;
import com.idega.business.IBOService;
import is.idega.idegaweb.campus.block.mailinglist.business.MailingListService;
import is.idega.idegaweb.campus.block.application.data.WaitingListHome;
import is.idega.idegaweb.campus.business.CampusGroupException;
import com.idega.idegaweb.IWResourceBundle;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.AutomaticCharges;
import java.util.Map;
import com.idega.block.building.data.Apartment;
import is.idega.idegaweb.campus.business.CampusUserService;
import java.util.Date;
import com.idega.user.data.User;
import is.idega.idegaweb.campus.block.application.business.ApplicationService;
import java.rmi.RemoteException;
import is.idega.idegaweb.campus.block.building.data.ApartmentTypePeriods;
import java.sql.Timestamp;
import is.idega.idegaweb.campus.block.allocation.data.ContractHome;
import java.util.Collection;
import com.idega.util.IWTimestamp;
import javax.ejb.FinderException;
import is.idega.idegaweb.campus.block.allocation.data.AutomaticChargesHome;
import is.idega.idegaweb.campus.data.ContractAccountApartmentHome;
import is.idega.idegaweb.campus.block.allocation.data.ContractTextHome;

public interface ContractService extends IBOService {
	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#signContract
	 */
	public String signContract(Integer contractID, Integer groupID,
			Integer cashierID, Integer financeCategoryID, String sEmail,
			boolean sendMail, boolean newAccount, boolean newPhoneAccount,
			boolean newLogin, boolean generatePasswd, IWResourceBundle iwrb,
			String login, String passwd) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#createUserLogin
	 */
	public void createUserLogin(User user, Integer groupID, String login,
			String pass, boolean generatePasswd) throws Exception,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#changeApplicationStatus
	 */
	public void changeApplicationStatus(Contract eContract) throws Exception,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#deleteFromWaitingList
	 */
	public void deleteFromWaitingList(Contract eContract)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#deleteFromWaitingList
	 */
	public void deleteFromWaitingList(Application application)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#deleteFromWaitingList
	 */
	public void deleteFromWaitingList(Applicant applicant)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#endContract
	 */
	public void endContract(Integer contractID, IWTimestamp movingDate,
			String info, boolean datesync) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#endContract
	 */
	public void endContract(Contract C, IWTimestamp movingDate, String info,
			boolean datesync) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#endExpiredContracts
	 */
	public void endExpiredContracts() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#garbageEndedContracts
	 */
	public void garbageEndedContracts(java.sql.Date lastChangeDate)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#garbageResignedContracts
	 */
	public void garbageResignedContracts(java.sql.Date lastChangeDate)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#finalizeGarbageContracts
	 */
	public void finalizeGarbageContracts(java.sql.Date lastChangeDate)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#automaticKeyStatusChange
	 */
	public void automaticKeyStatusChange() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#returnKey
	 */
	public void returnKey(Integer contractID, User currentUser)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#deliverKey
	 */
	public void deliverKey(Integer contractID, Timestamp when,
			boolean addKeyCharge, Integer accountKeyId, Integer tariffGroupId,
			Integer financeCategoryId, double amount) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getCampusAssessmentBusiness
	 */
	public CampusAssessmentBusiness getCampusAssessmentBusiness()
			throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#resignContract
	 */
	public void resignContract(Integer contractID, IWTimestamp movingDate,
			String info, boolean datesync) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#createNewContract
	 */
	public Contract createNewContract(Integer userID, Integer applicantID,
			Integer apartmentID, Date from, Date to) throws RemoteException,
			CreateException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#createUserFamily
	 */
	public User createUserFamily(Applicant applicant, String[] emails)
			throws RemoteException, CreateException, CampusGroupException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#createNewUser
	 */
	public User createNewUser(Applicant A, String[] emails)
			throws RemoteException, CreateException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#deleteAllocation
	 */
	public boolean deleteAllocation(Integer contractID, User currentUser)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getContractStampsFromPeriod
	 */
	public IWTimestamp[] getContractStampsFromPeriod(ApartmentTypePeriods ATP,
			int monthOverlap) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getContractStampsForApartment
	 */
	public IWTimestamp[] getContractStampsForApartment(Integer apartmentID)
			throws FinderException, RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getContractStampsForApartment
	 */
	public IWTimestamp[] getContractStampsForApartment(Apartment apartment)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getContractStampsFromPeriod
	 */
	public IWTimestamp[] getContractStampsFromPeriod(ApartmentTypePeriods ATP,
			Integer monthOverlap) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getLocalizedStatus
	 */
	public String getLocalizedStatus(IWResourceBundle iwrb, String status)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#doGarbageContract
	 */
	public boolean doGarbageContract(Integer contractID) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getApartmentTypePeriod
	 */
	public ApartmentTypePeriods getApartmentTypePeriod(Integer typeID)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#allocate
	 */
	public Contract allocate(Integer contractID, Integer apartmentID,
			Integer applicantID, Date validFrom, Date validTo,
			Integer applicationID) throws AllocationException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getValidPeriod
	 */
	public Period getValidPeriod(Contract contract, Apartment apartment,
			Integer dayBuffer, Integer monthOverlap) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getNextAvailableDate
	 */
	public Date getNextAvailableDate(Apartment apartment)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getIsContractResigned
	 */
	public boolean getIsContractResigned(Apartment apartment)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#resetWaitingListRejection
	 */
	public void resetWaitingListRejection(Integer waitingListID)
			throws RemoteException, FinderException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#reactivateWaitingList
	 */
	public void reactivateWaitingList(Integer waitingListID)
			throws RemoteException, FinderException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#removeWaitingList
	 */
	public void removeWaitingList(Integer waitingListID)
			throws RemoteException, FinderException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getAvailableApartmentDates
	 */
	public Map getAvailableApartmentDates(Integer aprtTypeID, Integer cplxID)
			throws FinderException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getApplicantContractsByStatus
	 */
	public Map getApplicantContractsByStatus(String status)
			throws RemoteException, FinderException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getRentableStatuses
	 */
	public String[] getRentableStatuses() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getAllocateableStatuses
	 */
	public String[] getAllocateableStatuses() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getResignStatus
	 */
	public String[] getResignStatus() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getAllowedTemporaryPersonalID
	 */
	public Collection getAllowedTemporaryPersonalID() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getAutomaticChargesByUser
	 */
	public AutomaticCharges getAutomaticChargesByUser(User user)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#addChargeForUnlimitedDownloadToUser
	 */
	public void addChargeForUnlimitedDownloadToUser(String userID)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#removeChargeForUnlimitedDownloadForUser
	 */
	public void removeChargeForUnlimitedDownloadForUser(String userID)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#addChargeForHandlingToUser
	 */
	public void addChargeForHandlingToUser(String userID)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#removeChargeForHandlingForUser
	 */
	public void removeChargeForHandlingForUser(String userID)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#addChargeForTransferToUser
	 */
	public void addChargeForTransferToUser(String userID)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#removeChargeForTransferForUser
	 */
	public void removeChargeForTransferForUser(String userID)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#removeAllAutomaticChargesForUser
	 */
	public void removeAllAutomaticChargesForUser(String userID)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getNewApplicantContracts
	 */
	public Map getNewApplicantContracts() throws RemoteException,
			FinderException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getPrintedContracts
	 */
	public Map getPrintedContracts() throws RemoteException, FinderException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getUserService
	 */
	public CampusUserService getUserService() throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getContractHome
	 */
	public ContractHome getContractHome() throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getAutomaticChargesHome
	 */
	public AutomaticChargesHome getAutomaticChargesHome()
			throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getAccountHome
	 */
	public AccountHome getAccountHome() throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getContractTextHome
	 */
	public ContractTextHome getContractTextHome() throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getWaitingListHome
	 */
	public WaitingListHome getWaitingListHome() throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getContractAccountApartmentHome
	 */
	public ContractAccountApartmentHome getContractAccountApartmentHome()
			throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getAccountKeyHome
	 */
	public AccountKeyHome getAccountKeyHome() throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getMailingListService
	 */
	public MailingListService getMailingListService() throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getApplicationService
	 */
	public ApplicationService getApplicationService() throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.business.ContractServiceBean#getBuildingService
	 */
	public BuildingService getBuildingService() throws RemoteException,
			RemoteException;
}