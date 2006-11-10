package is.idega.idegaweb.member.isi.block.accounting.webservice.general.business;


import is.idega.idegaweb.member.isi.block.accounting.webservice.general.server.UserInfo;
import com.idega.business.IBOService;
import java.rmi.RemoteException;

public interface GeneralAccountingServiceBusiness extends IBOService {
	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.webservice.general.business.GeneralAccountingServiceBusinessBean#getUserInfo
	 */
	public UserInfo getUserInfo(String personalID) throws RemoteException;
}