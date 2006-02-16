/**
 * 
 */
package is.idega.idegaweb.member.isi.block.accounting.export.data;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * @author bluebottle
 *
 */
public interface CompanyBatchInformationHome extends IDOHome {
	public CompanyBatchInformation create() throws javax.ejb.CreateException;

	public CompanyBatchInformation findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.CompanyBatchInformationBMPBean#ejbFindByCompanyNumber
	 */
	public CompanyBatchInformation findByCompanyNumber(String companyNumber)
			throws FinderException;

}
