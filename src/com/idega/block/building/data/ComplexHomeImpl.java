/**
 * 
 */
package com.idega.block.building.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;

/**
 * @author bluebottle
 *
 */
public class ComplexHomeImpl extends IDOFactory implements ComplexHome {
	protected Class getEntityInterfaceClass() {
		return Complex.class;
	}

	public Complex create() throws javax.ejb.CreateException {
		return (Complex) super.createIDO();
	}

	public Complex findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (Complex) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ComplexBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
