package is.idega.idegaweb.marathon.business;

import java.rmi.RemoteException;
import java.util.List;
import com.idega.block.importer.business.ImportFileHandler;
import com.idega.block.importer.data.ImportFile;
import com.idega.business.IBOService;
import com.idega.user.data.Group;


/**
 * @author gimmi
 */
public interface MarathonFileImportHandler extends IBOService, ImportFileHandler{

	/**
	 * @see is.idega.idegaweb.marathon.business.MarathonFileImportHandlerBean#handleRecords
	 */
	public boolean handleRecords() throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.MarathonFileImportHandlerBean#setImportFile
	 */
	public void setImportFile(ImportFile file) throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.MarathonFileImportHandlerBean#setRootGroup
	 */
	public void setRootGroup(Group rootGroup) throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.MarathonFileImportHandlerBean#getFailedRecords
	 */
	public List getFailedRecords() throws RemoteException, java.rmi.RemoteException;
}
