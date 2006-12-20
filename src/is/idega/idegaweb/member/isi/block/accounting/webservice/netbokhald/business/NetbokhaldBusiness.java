package is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.business;


import java.util.Collection;
import java.util.Date;
import com.idega.business.IBOService;
import java.rmi.RemoteException;

public interface NetbokhaldBusiness extends IBOService {
	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.business.NetbokhaldBusinessBean#getFinanceEntries
	 */
	public Collection getFinanceEntries(String companyNumber, Date dateFrom) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.business.NetbokhaldBusinessBean#getFinanceEntries
	 */
	public Collection getFinanceEntries(String companyNumber, String fromSerialNumber) throws RemoteException;
}