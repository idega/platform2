/**
 * 
 */
package com.idega.block.finance.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;

/**
 * @author bluebottle
 *
 */
public class BankHomeImpl extends IDOFactory implements BankHome {
	protected Class getEntityInterfaceClass() {
		return Bank.class;
	}

	public Bank create() throws javax.ejb.CreateException {
		return (Bank) super.createIDO();
	}

	public Bank findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (Bank) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((BankBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
