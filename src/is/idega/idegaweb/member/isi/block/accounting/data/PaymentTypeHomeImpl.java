/**
 * 
 */
package is.idega.idegaweb.member.isi.block.accounting.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;

/**
 * @author bluebottle
 *
 */
public class PaymentTypeHomeImpl extends IDOFactory implements PaymentTypeHome {
	protected Class getEntityInterfaceClass() {
		return PaymentType.class;
	}

	public PaymentType create() throws javax.ejb.CreateException {
		return (PaymentType) super.createIDO();
	}

	public PaymentType findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (PaymentType) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllPaymentTypes() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PaymentTypeBMPBean) entity)
				.ejbFindAllPaymentTypes();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public PaymentType findPaymentTypeCreditcard() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((PaymentTypeBMPBean) entity)
				.ejbFindPaymentTypeCreditcard();
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public PaymentType findPaymentTypeCreditcardSystem() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((PaymentTypeBMPBean) entity)
				.ejbFindPaymentTypeCreditcardSystem();
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public PaymentType findPaymentTypeBankSystem() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((PaymentTypeBMPBean) entity)
				.ejbFindPaymentTypeBankSystem();
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

}
