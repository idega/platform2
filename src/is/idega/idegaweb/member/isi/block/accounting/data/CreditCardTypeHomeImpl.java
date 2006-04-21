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
public class CreditCardTypeHomeImpl extends IDOFactory implements
		CreditCardTypeHome {
	protected Class getEntityInterfaceClass() {
		return CreditCardType.class;
	}

	public CreditCardType create() throws javax.ejb.CreateException {
		return (CreditCardType) super.createIDO();
	}

	public CreditCardType findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (CreditCardType) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((CreditCardTypeBMPBean) entity)
				.ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public CreditCardType findTypeVisa() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((CreditCardTypeBMPBean) entity).ejbFindTypeVisa();
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

}
