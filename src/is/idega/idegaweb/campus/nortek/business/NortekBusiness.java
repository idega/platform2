package is.idega.idegaweb.campus.nortek.business;


import javax.ejb.CreateException;
import is.idega.idegaweb.campus.nortek.data.Card;
import java.rmi.RemoteException;
import com.idega.user.data.User;
import is.idega.idegaweb.campus.block.finance.business.CampusAssessmentBusiness;
import javax.ejb.FinderException;
import com.idega.business.IBOService;
import java.util.Date;
import java.util.Collection;

public interface NortekBusiness extends IBOService {
	/**
	 * @see is.idega.idegaweb.campus.nortek.business.NortekBusinessBean#isCardValid
	 */
	public boolean isCardValid(String serialNumber) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.nortek.business.NortekBusinessBean#banCard
	 */
	public boolean banCard(String serialNumber, boolean ban)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.nortek.business.NortekBusinessBean#addAmountToCardUser
	 */
	public boolean addAmountToCardUser(String serialNumber, Date timestamp,
			double amount, String terminalNumber) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.nortek.business.NortekBusinessBean#getAllCards
	 */
	public Collection getAllCards() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.nortek.business.NortekBusinessBean#getCardsByValdi
	 */
	public Collection getCardsByValdi(boolean valid) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.nortek.business.NortekBusinessBean#getCard
	 */
	public Card getCard(String serialNumber) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.nortek.business.NortekBusinessBean#getCard
	 */
	public Card getCard(User user) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.nortek.business.NortekBusinessBean#saveCard
	 */
	public void saveCard(String decodedSerial, String ssn, String valid)
			throws CreateException, FinderException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.nortek.business.NortekBusinessBean#deleteCards
	 */
	public void deleteCards(String[] cardIDs) throws CreateException,
			FinderException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.nortek.business.NortekBusinessBean#decodeSerialNumber
	 */
	public String decodeSerialNumber(String serialNumber)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.nortek.business.NortekBusinessBean#encodeDecodedSerialNumber
	 */
	public String encodeDecodedSerialNumber(String encodedSerialNumber)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.nortek.business.NortekBusinessBean#getCampusAssessmentBusiness
	 */
	public CampusAssessmentBusiness getCampusAssessmentBusiness()
			throws RemoteException, RemoteException;
}