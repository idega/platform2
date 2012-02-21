package is.idega.idegaweb.campus.business;


import javax.ejb.CreateException;
import java.rmi.RemoteException;
import com.idega.business.IBOHome;

public interface ContractRenewalServiceHome extends IBOHome {
	public ContractRenewalService create() throws CreateException,
			RemoteException;
}