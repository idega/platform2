/**
 * 
 */
package se.idega.idegaweb.commune.care.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;

/**
 * @author bluebottle
 *
 */
public class ProviderAccountingPropertiesHomeImpl extends IDOFactory implements
		ProviderAccountingPropertiesHome {
	protected Class getEntityInterfaceClass() {
		return ProviderAccountingProperties.class;
	}

	public ProviderAccountingProperties create()
			throws javax.ejb.CreateException {
		return (ProviderAccountingProperties) super.createIDO();
	}

	public ProviderAccountingProperties findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (ProviderAccountingProperties) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllByPaymentByInvoice(boolean hasPaymentByInvoice)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ProviderAccountingPropertiesBMPBean) entity)
				.ejbFindAllByPaymentByInvoice(hasPaymentByInvoice);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
