package is.idega.block.nationalregister.business;

import com.idega.business.IBOHome;


/**
 * @author Joakim
 *
 */
public interface NationalRegisterFileImportHandlerHome extends IBOHome {

	public NationalRegisterFileImportHandler create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
