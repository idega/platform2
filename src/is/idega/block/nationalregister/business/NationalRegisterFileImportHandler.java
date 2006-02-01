/**
 * 
 */
package is.idega.block.nationalregister.business;

import is.idega.block.family.business.FamilyLogic;

import java.rmi.RemoteException;
import java.util.List;


import com.idega.block.importer.business.ImportFileHandler;
import com.idega.block.importer.data.ImportFile;
import com.idega.business.IBOService;
import com.idega.user.data.Group;

/**
 * @author bluebottle
 *
 */
public interface NationalRegisterFileImportHandler extends IBOService,
		ImportFileHandler {
	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterFileImportHandlerBean#handleRecords
	 */
	public boolean handleRecords() throws RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterFileImportHandlerBean#getTimeString
	 */
	public String getTimeString(long time) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterFileImportHandlerBean#printFailedRecords
	 */
	public void printFailedRecords() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterFileImportHandlerBean#setImportFile
	 */
	public void setImportFile(ImportFile file) throws RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterFileImportHandlerBean#setRootGroup
	 */
	public void setRootGroup(Group rootGroup) throws RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterFileImportHandlerBean#getFailedRecords
	 */
	public List getFailedRecords() throws RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterFileImportHandlerBean#getMemberFamilyLogic
	 */
	public FamilyLogic getMemberFamilyLogic() throws RemoteException;

}
