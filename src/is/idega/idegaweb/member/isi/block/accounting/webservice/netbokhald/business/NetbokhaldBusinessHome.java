package is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface NetbokhaldBusinessHome extends IBOHome {
	public NetbokhaldBusiness create() throws CreateException, RemoteException;
}