package is.idega.idegaweb.marathon.business;


import com.idega.user.data.Group;
import com.idega.business.IBOService;
import java.util.List;
import java.rmi.RemoteException;
import com.idega.block.importer.data.ImportFile;

public interface MarathonGroupUsersImport extends IBOService {

	/**
	 * @see is.idega.idegaweb.marathon.business.MarathonGroupUsersImportBean#handleRecords
	 */
	public boolean handleRecords() throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.MarathonGroupUsersImportBean#setImportFile
	 */
	public void setImportFile(ImportFile file) throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.MarathonGroupUsersImportBean#setRootGroup
	 */
	public void setRootGroup(Group rootGroup) throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.MarathonGroupUsersImportBean#getFailedRecords
	 */
	public List getFailedRecords() throws RemoteException, RemoteException;
}