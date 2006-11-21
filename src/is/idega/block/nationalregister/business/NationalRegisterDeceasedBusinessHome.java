package is.idega.block.nationalregister.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface NationalRegisterDeceasedBusinessHome extends IBOHome {

	public NationalRegisterDeceasedBusiness create() throws CreateException, RemoteException;
}