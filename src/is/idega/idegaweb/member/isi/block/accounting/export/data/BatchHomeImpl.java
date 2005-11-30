/**
 * 
 */
package is.idega.idegaweb.member.isi.block.accounting.export.data;

import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContract;
import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardType;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.finance.data.BankInfo;
import com.idega.data.IDOFactory;

/**
 * @author bluebottle
 *
 */
public class BatchHomeImpl extends IDOFactory implements BatchHome {
	protected Class getEntityInterfaceClass() {
		return Batch.class;
	}

	public Batch create() throws javax.ejb.CreateException {
		return (Batch) super.createIDO();
	}

	public Batch findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (Batch) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((BatchBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Batch findUnsentByContractAndCreditCardType(
			CreditCardContract contract, CreditCardType type)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((BatchBMPBean) entity)
				.ejbFindUnsentByContractAndCreditCardType(contract, type);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findAllUnsent() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((BatchBMPBean) entity).ejbFindAllUnsent();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllWithoutFiles() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((BatchBMPBean) entity)
				.ejbFindAllWithoutFiles();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Batch findUnsentByBankInfo(BankInfo info) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((BatchBMPBean) entity).ejbFindUnsentByBankInfo(info);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findAllNewestFirst() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((BatchBMPBean) entity)
				.ejbFindAllNewestFirst();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
