package is.idega.idegaweb.campus.nortek.business;


import java.util.Date;
import com.idega.business.IBOService;
import java.rmi.RemoteException;

public interface NortekBusiness extends IBOService {
	/**
	 * @see is.idega.idegaweb.campus.nortek.business.NortekBusinessBean#isCardValid
	 */
	public boolean isCardValid(String serialNumber) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.nortek.business.NortekBusinessBean#banCard
	 */
	public boolean banCard(String serialNumber, boolean ban) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.nortek.business.NortekBusinessBean#addAmountToCardUser
	 */
	public boolean addAmountToCardUser(String serialNumber, Date timestamp, double amount, String terminalNumber) throws RemoteException;
}