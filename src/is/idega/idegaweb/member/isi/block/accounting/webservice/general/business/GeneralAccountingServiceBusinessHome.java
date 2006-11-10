package is.idega.idegaweb.member.isi.block.accounting.webservice.general.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface GeneralAccountingServiceBusinessHome extends IBOHome {
	public GeneralAccountingServiceBusiness create() throws CreateException, RemoteException;
}