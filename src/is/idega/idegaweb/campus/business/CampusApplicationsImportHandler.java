package is.idega.idegaweb.campus.business;


import com.idega.user.data.Group;
import java.util.List;
import com.idega.business.IBOSession;
import com.idega.block.importer.business.ImportFileHandler;
import java.rmi.RemoteException;
import com.idega.block.importer.data.ImportFile;

public interface CampusApplicationsImportHandler extends IBOSession, ImportFileHandler {
	/**
	 * @see is.idega.idegaweb.campus.business.CampusApplicationsImportHandlerBean#handleRecords
	 */
	public boolean handleRecords() throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.CampusApplicationsImportHandlerBean#setImportFile
	 */
	public void setImportFile(ImportFile file) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.CampusApplicationsImportHandlerBean#setRootGroup
	 */
	public void setRootGroup(Group group) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.CampusApplicationsImportHandlerBean#getFailedRecords
	 */
	public List getFailedRecords() throws RemoteException;
}