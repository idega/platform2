package is.idega.idegaweb.marathon.business;

import com.idega.business.IBOHome;


/**
 * @author gimmi
 */
public interface MarathonFileImportHandlerHome extends IBOHome {

	public MarathonFileImportHandler create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
