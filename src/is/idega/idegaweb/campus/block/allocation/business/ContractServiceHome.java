package is.idega.idegaweb.campus.block.allocation.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface ContractServiceHome extends IBOHome {
	public ContractService create() throws CreateException, RemoteException;
}