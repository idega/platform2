/**
 * 
 */
package is.idega.idegaweb.member.isi.block.accounting.export.data;

import java.sql.Timestamp;


import com.idega.data.IDOEntity;

/**
 * @author bluebottle
 *
 */
public interface RunLogEntry extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.RunLogEntryBMPBean#getRunLogID
	 */
	public int getRunLogID();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.RunLogEntryBMPBean#getRunLog
	 */
	public RunLog getRunLog();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.RunLogEntryBMPBean#getDateOfEntry
	 */
	public Timestamp getDateOfEntry();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.RunLogEntryBMPBean#getEntry
	 */
	public String getEntry();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.RunLogEntryBMPBean#getIsError
	 */
	public boolean getIsError();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.RunLogEntryBMPBean#setRunLogID
	 */
	public void setRunLogID(int id);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.RunLogEntryBMPBean#setRunLog
	 */
	public void setRunLog(RunLog log);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.RunLogEntryBMPBean#setDateOfEntry
	 */
	public void setDateOfEntry(Timestamp dateOfEntry);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.RunLogEntryBMPBean#setEntry
	 */
	public void setEntry(String entry);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.RunLogEntryBMPBean#setIsError
	 */
	public void setIsError(boolean isError);

}
