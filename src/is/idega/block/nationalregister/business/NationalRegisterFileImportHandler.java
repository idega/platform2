package is.idega.block.nationalregister.business;

import is.idega.idegaweb.member.business.MemberFamilyLogic;
import java.rmi.RemoteException;
import java.util.List;
import com.idega.block.importer.data.ImportFile;
import com.idega.user.data.Group;


/**
 * @author Joakim
 *
 */
public interface NationalRegisterFileImportHandler {

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterFileImportHandlerBean#handleRecords
	 */
	public boolean handleRecords() throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterFileImportHandlerBean#printFailedRecords
	 */
	public void printFailedRecords() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterFileImportHandlerBean#setImportFile
	 */
	public void setImportFile(ImportFile file) throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterFileImportHandlerBean#setRootGroup
	 */
	public void setRootGroup(Group rootGroup) throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterFileImportHandlerBean#getFailedRecords
	 */
	public List getFailedRecords() throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterFileImportHandlerBean#getMemberFamilyLogic
	 */
	public MemberFamilyLogic getMemberFamilyLogic() throws RemoteException, java.rmi.RemoteException;
}
