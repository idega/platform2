/**
 * 
 */
package se.idega.idegaweb.commune.accounting.export.raindance.data;



import com.idega.data.IDOEntity;

/**
 * @author bluebottle
 *
 */
public interface RaindanceCheckRecord extends IDOEntity {
	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raidance.data.RaindanceCheckRecordBMPBean#setHeaderId
	 */
	public void setHeaderId(int id);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raidance.data.RaindanceCheckRecordBMPBean#setHeader
	 */
	public void setHeader(RaindanceCheckHeader header);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raidance.data.RaindanceCheckRecordBMPBean#getHeaderId
	 */
	public int getHeaderId();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raidance.data.RaindanceCheckRecordBMPBean#getHeader
	 */
	public RaindanceCheckHeader getHeader();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raidance.data.RaindanceCheckRecordBMPBean#setErrorAmountMissing
	 */
	public void setErrorAmountMissing();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raidance.data.RaindanceCheckRecordBMPBean#setError
	 */
	public void setError(String key);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raidance.data.RaindanceCheckRecordBMPBean#getErrorAmountMissing
	 */
	public String getErrorAmountMissing();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raidance.data.RaindanceCheckRecordBMPBean#getError
	 */
	public String getError();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raidance.data.RaindanceCheckRecordBMPBean#setErrorConcerns
	 */
	public void setErrorConcerns(String desc);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raidance.data.RaindanceCheckRecordBMPBean#getErrorConcerns
	 */
	public String getErrorConcerns();

}
