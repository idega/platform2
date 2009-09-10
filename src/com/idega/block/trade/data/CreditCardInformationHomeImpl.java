package com.idega.block.trade.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;
import com.idega.user.data.Group;


/**
 * @author gimmi
 */
public class CreditCardInformationHomeImpl extends IDOFactory implements CreditCardInformationHome {

	protected Class getEntityInterfaceClass() {
		return CreditCardInformation.class;
	}

	public CreditCardInformation create() throws javax.ejb.CreateException {
		return (CreditCardInformation) super.createIDO();
	}

	public CreditCardInformation findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (CreditCardInformation) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findBySupplierManager(Group supplierManager) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((CreditCardInformationBMPBean) entity).ejbFindBySupplierManager(supplierManager);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
