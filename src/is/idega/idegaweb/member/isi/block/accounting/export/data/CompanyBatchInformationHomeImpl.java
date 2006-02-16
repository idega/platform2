/**
 * 
 */
package is.idega.idegaweb.member.isi.block.accounting.export.data;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;

/**
 * @author bluebottle
 *
 */
public class CompanyBatchInformationHomeImpl extends IDOFactory implements
		CompanyBatchInformationHome {
	protected Class getEntityInterfaceClass() {
		return CompanyBatchInformation.class;
	}

	public CompanyBatchInformation create() throws javax.ejb.CreateException {
		return (CompanyBatchInformation) super.createIDO();
	}

	public CompanyBatchInformation findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (CompanyBatchInformation) super.findByPrimaryKeyIDO(pk);
	}

	public CompanyBatchInformation findByCompanyNumber(String companyNumber)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((CompanyBatchInformationBMPBean) entity)
				.ejbFindByCompanyNumber(companyNumber);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

}
