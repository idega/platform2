package is.idega.idegaweb.campus.block.finance.business;


import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.data.ApartmentAccountEntry;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Locale;

import javax.ejb.CreateException;

import com.idega.block.finance.business.AssessmentBusiness;
import com.idega.block.finance.business.FinanceService;
import com.idega.block.finance.data.AccountEntry;
import com.idega.block.finance.data.EntryGroup;
import com.idega.user.data.User;

public interface CampusAssessmentBusiness extends AssessmentBusiness {
	/**
	 * @see is.idega.idegaweb.campus.block.finance.business.CampusAssessmentBusinessBean#createDKXMLFile
	 */
	public void createDKXMLFile(EntryGroup entryGroup, Locale locale) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.finance.business.CampusAssessmentBusinessBean#findContractForUser
	 */
	public Contract findContractForUser(User user) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.finance.business.CampusAssessmentBusinessBean#createApartmentAccountEntry
	 */
	public ApartmentAccountEntry createApartmentAccountEntry(Integer accountEntryID, Integer apartmentID) throws RemoteException, CreateException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.finance.business.CampusAssessmentBusinessBean#createAccountEntry
	 */
	public AccountEntry createAccountEntry(Integer accountID, Integer accountKeyID, Integer cashierID, Integer roundID, float netto, float VAT, float total, Date paydate, String Name, String Info, String status, Integer externalID) throws RemoteException, CreateException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.finance.business.CampusAssessmentBusinessBean#rollBackAssessment
	 */
	public boolean rollBackAssessment(Integer assessmentRoundId) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.finance.business.CampusAssessmentBusinessBean#getFinanceService
	 */
	public FinanceService getFinanceService() throws RemoteException, RemoteException;
}