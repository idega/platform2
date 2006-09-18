package is.idega.idegaweb.member.isi.block.accounting.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface AccountingBusinessHome extends IBOHome {
	public AccountingBusiness create() throws CreateException, RemoteException;
}