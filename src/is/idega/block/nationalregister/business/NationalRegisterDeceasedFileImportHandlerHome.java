package is.idega.block.nationalregister.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface NationalRegisterDeceasedFileImportHandlerHome extends IBOHome {

	public NationalRegisterDeceasedFileImportHandler create() throws CreateException, RemoteException;
}