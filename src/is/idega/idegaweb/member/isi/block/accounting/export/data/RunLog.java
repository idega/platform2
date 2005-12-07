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
public interface RunLog extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.RunLogBMPBean#getCreatedDate
	 */
	public Timestamp getCreatedDate();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.RunLogBMPBean#getCreatedBy
	 */
	public String getCreatedBy();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.RunLogBMPBean#setCreatedDate
	 */
	public void setCreatedDate(Timestamp created);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.RunLogBMPBean#setCreatedBy
	 */
	public void setCreatedBy(String name);

}
