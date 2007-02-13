package is.idega.idegaweb.member.isi.block.accounting.netbokhald.business;


import java.util.Collection;
import java.util.Map;
import java.util.Date;
import com.idega.business.IBOService;
import java.rmi.RemoteException;

public interface NetbokhaldBusiness extends IBOService {
	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.business.NetbokhaldBusinessBean#getFinanceEntries
	 */
	public Collection getFinanceEntries(String companyNumber, Date dateFrom) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.business.NetbokhaldBusinessBean#getFinanceEntries
	 */
	public Collection getFinanceEntries(String companyNumber, String fromSerialNumber) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.business.NetbokhaldBusinessBean#getDiscountEntries
	 */
	public Collection getDiscountEntries(String companyNumber, Date dateFrom) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.business.NetbokhaldBusinessBean#getDiscountEntries
	 */
	public Collection getDiscountEntries(String companyNumber, String fromSerialNumber) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.netbokhald.business.NetbokhaldBusinessBean#getAccountingKeys
	 */
	public Map getAccountingKeys(String companyNumber) throws RemoteException;
}