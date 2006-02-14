/**
 * 
 */
package is.idega.idegaweb.member.isi.block.accounting.export.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * @author bluebottle
 *
 */
public interface RunLogEntryHome extends IDOHome {
	public RunLogEntry create() throws javax.ejb.CreateException;

	public RunLogEntry findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.RunLogEntryBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.RunLogEntryBMPBean#ejbFindByRunLog
	 */
	public Collection findByRunLog(RunLog log) throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.RunLogEntryBMPBean#ejbFindByRunLogID
	 */
	public Collection findByRunLogID(int logID) throws FinderException;

}
