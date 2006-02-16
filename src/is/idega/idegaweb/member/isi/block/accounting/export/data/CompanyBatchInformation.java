/**
 * 
 */
package is.idega.idegaweb.member.isi.block.accounting.export.data;



import com.idega.data.IDOEntity;

/**
 * @author bluebottle
 *
 */
public interface CompanyBatchInformation extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.CompanyBatchInformationBMPBean#setCompanyNumber
	 */
	public void setCompanyNumber(String companyNumber);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.CompanyBatchInformationBMPBean#setBatchNumber
	 */
	public void setBatchNumber(int batchNumber);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.CompanyBatchInformationBMPBean#setBatchMonth
	 */
	public void setBatchMonth(String batchMonth);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.CompanyBatchInformationBMPBean#getCompanyNumber
	 */
	public String getCompanyNumber();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.CompanyBatchInformationBMPBean#getBatchNumber
	 */
	public int getBatchNumber();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.CompanyBatchInformationBMPBean#getBatchMonth
	 */
	public String getBatchMonth();

}
