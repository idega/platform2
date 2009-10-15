package is.idega.idegaweb.campus.block.allocation.business;


import javax.ejb.CreateException;
import java.rmi.RemoteException;
import com.idega.business.IBOHome;

public interface ContractServiceHome extends IBOHome {
	public ContractService create() throws CreateException, RemoteException;
}