package is.idega.idegaweb.member.isi.block.accounting.netbokhald.business;


import com.idega.user.data.Group;
import java.util.Collection;
import java.util.Map;
import java.util.Date;
import com.idega.business.IBOService;
import is.idega.idegaweb.member.isi.block.accounting.netbokhald.data.NetbokhaldSetup;
import java.rmi.RemoteException;

public interface NetbokhaldBusiness extends IBOService {
	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.business.NetbokhaldBusinessBean#getFinanceEntries
	 */
	public Collection getFinanceEntries(String companyNumber, Date dateFrom)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.business.NetbokhaldBusinessBean#getFinanceEntries
	 */
	public Collection getFinanceEntries(String companyNumber,
			String fromSerialNumber) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.business.NetbokhaldBusinessBean#getDiscountEntries
	 */
	public Collection getDiscountEntries(String companyNumber, Date dateFrom)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.business.NetbokhaldBusinessBean#getDiscountEntries
	 */
	public Collection getDiscountEntries(String companyNumber,
			String fromSerialNumber) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.business.NetbokhaldBusinessBean#getAccountingKeys
	 */
	public Map getAccountingKeys(String companyNumber) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.business.NetbokhaldBusinessBean#getNetbokhaldConnection
	 */
	public Collection getNetbokhaldConnection(Group club)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.business.NetbokhaldBusinessBean#insertNetbokhaldConnection
	 */
	public boolean insertNetbokhaldConnection(String externalID, Group club,
			Group division, Group group) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.business.NetbokhaldBusinessBean#deleteNetbokhaldConnection
	 */
	public boolean deleteNetbokhaldConnection(String[] ids)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.business.NetbokhaldBusinessBean#getNetbokhaldSetup
	 */
	public NetbokhaldSetup getNetbokhaldSetup(String setupID)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.business.NetbokhaldBusinessBean#insertNetbokhaldAccountingKey
	 */
	public boolean insertNetbokhaldAccountingKey(String setupID, String type,
			int key, String debit, String credit) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.business.NetbokhaldBusinessBean#insertNetbokhaldAccountingKey
	 */
	public boolean insertNetbokhaldAccountingKey(NetbokhaldSetup setup,
			String type, int key, String debit, String credit)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.business.NetbokhaldBusinessBean#deleteAccountingKeys
	 */
	public boolean deleteAccountingKeys(String[] ids) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.business.NetbokhaldBusinessBean#getAccountingKeys
	 */
	public Map getAccountingKeys(NetbokhaldSetup setup) throws RemoteException;
}