/**
 * 
 */
package se.idega.idegaweb.commune.accounting.export.economa.data;



import com.idega.data.IDOEntity;

/**
 * @author bluebottle
 *
 */
public interface EconomaCheckRecord extends IDOEntity {
	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckRecordBMPBean#setHeaderId
	 */
	public void setHeaderId(int id);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckRecordBMPBean#setHeader
	 */
	public void setHeader(EconomaCheckHeader header);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckRecordBMPBean#getHeaderId
	 */
	public int getHeaderId();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckRecordBMPBean#getHeader
	 */
	public EconomaCheckHeader getHeader();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckRecordBMPBean#setErrorAmountMissing
	 */
	public void setErrorAmountMissing();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckRecordBMPBean#setError
	 */
	public void setError(String key);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckRecordBMPBean#getErrorAmountMissing
	 */
	public String getErrorAmountMissing();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckRecordBMPBean#getError
	 */
	public String getError();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckRecordBMPBean#setErrorConcerns
	 */
	public void setErrorConcerns(String desc);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckRecordBMPBean#getErrorConcerns
	 */
	public String getErrorConcerns();

}
