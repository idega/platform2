package is.idega.idegaweb.campus.business;


import java.rmi.RemoteException;
import java.util.List;
import com.idega.block.importer.data.ImportFile;
import com.idega.block.importer.business.ImportFileHandler;
import com.idega.business.IBOService;
import com.idega.user.data.Group;

public interface CampusApartmentsImport extends IBOService, ImportFileHandler {
	/**
	 * @see is.idega.idegaweb.campus.business.CampusApartmentsImportBean#getFailedRecords
	 */
	public List getFailedRecords() throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.CampusApartmentsImportBean#handleRecords
	 */
	public boolean handleRecords() throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.CampusApartmentsImportBean#setImportFile
	 */
	public void setImportFile(ImportFile file) throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.business.CampusApartmentsImportBean#setRootGroup
	 */
	public void setRootGroup(Group rootGroup) throws RemoteException,
			RemoteException;
}