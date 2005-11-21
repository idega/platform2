/**
 * 
 */
package is.idega.idegaweb.campus.block.finance.business;

import is.idega.idegaweb.campus.data.ApartmentAccountEntry;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Locale;

import javax.ejb.CreateException;

import com.idega.block.finance.business.AssessmentBusiness;
import com.idega.block.finance.data.AccountEntry;
import com.idega.block.finance.data.EntryGroup;

/**
 * @author bluebottle
 *
 */
public interface CampusAssessmentBusiness extends AssessmentBusiness {
	/**
	 * @see is.idega.idegaweb.campus.block.finance.business.CampusAssessmentBusinessBean#createDKXMLFile
	 */
	public void createDKXMLFile(EntryGroup entryGroup, Locale locale)
			throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.finance.business.CampusAssessmentBusinessBean#createApartmentAccountEntry
	 */
	public ApartmentAccountEntry createApartmentAccountEntry(
			Integer accountEntryID, Integer apartmentID)
			throws RemoteException, CreateException;

	/**
	 * @see is.idega.idegaweb.campus.block.finance.business.CampusAssessmentBusinessBean#createAccountEntry
	 */
	public AccountEntry createAccountEntry(Integer accountID,
			Integer accountKeyID, Integer cashierID, Integer roundID,
			float netto, float VAT, float total, Date paydate, String Name,
			String Info, String status, Integer externalID)
			throws RemoteException, CreateException;

	/**
	 * @see is.idega.idegaweb.campus.block.finance.business.CampusAssessmentBusinessBean#rollBackAssessment
	 */
	public boolean rollBackAssessment(Integer assessmentRoundId)
			throws java.rmi.RemoteException;

}
