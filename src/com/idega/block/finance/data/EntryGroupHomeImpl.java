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
public class EntryGroupHomeImpl extends IDOFactory implements EntryGroupHome {
	protected Class getEntityInterfaceClass() {
		return EntryGroup.class;
	}

	public EntryGroup create() throws javax.ejb.CreateException {
		return (EntryGroup) super.createIDO();
	}

	public EntryGroup findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (EntryGroup) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((EntryGroupBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
