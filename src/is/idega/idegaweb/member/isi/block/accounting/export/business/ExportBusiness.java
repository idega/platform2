/**
 * 
 */
package is.idega.idegaweb.member.isi.block.accounting.export.business;

import is.idega.idegaweb.member.isi.block.accounting.export.data.Configuration;

import java.util.Collection;


import com.idega.business.IBOService;
import com.idega.util.IWTimestamp;

/**
 * @author bluebottle
 *
 */
public interface ExportBusiness extends IBOService {
	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.business.ExportBusinessBean#createFileFromContracts
	 */
	public boolean createFileFromContracts(String dateFrom, String dateTo)
			throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.business.ExportBusinessBean#getConfiguration
	 */
	public Configuration getConfiguration(String typeID)
			throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.business.ExportBusinessBean#saveConfiguration
	 */
	public boolean saveConfiguration(String configurationID, String sendServer,
			String sendUser, String sendPasswd, String sendPath,
			String sendBackupPath, String createPlugin, String createPath,
			String lastBatch, IWTimestamp batchDate, String createEncPlugin,
			String getServer, String getUser, String getPasswd, String getPath,
			String getBackupPath, String readPlugin, String readEncPlugin)
			throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.business.ExportBusinessBean#findAllBatches
	 */
	public Collection findAllBatches() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.business.ExportBusinessBean#findAllEntriesNotInBatch
	 */
	public Collection findAllEntriesNotInBatch()
			throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.business.ExportBusinessBean#findAllEntriesInBatch
	 */
	public Collection findAllEntriesInBatch(int batchID)
			throws java.rmi.RemoteException;

}
