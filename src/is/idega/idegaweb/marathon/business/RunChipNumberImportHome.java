/*
 * Created on 19.8.2004
 */
package is.idega.idegaweb.marathon.business;

import com.idega.business.IBOHome;


/**
 * @author laddi
 */
public interface RunChipNumberImportHome extends IBOHome {

	public RunChipNumberImport create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}
