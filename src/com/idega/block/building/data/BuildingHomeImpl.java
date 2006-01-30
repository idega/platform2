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
public class BuildingHomeImpl extends IDOFactory implements BuildingHome {
	protected Class getEntityInterfaceClass() {
		return Building.class;
	}

	public Building create() throws javax.ejb.CreateException {
		return (Building) super.createIDO();
	}

	public Building findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (Building) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((BuildingBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByComplex(Integer complexID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((BuildingBMPBean) entity)
				.ejbFindByComplex(complexID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection getImageFilesByComplex(Integer complexID)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection theReturn = ((BuildingBMPBean) entity)
				.ejbHomeGetImageFilesByComplex(complexID);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findByComplex(Complex complex) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((BuildingBMPBean) entity)
				.ejbFindByComplex(complex);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
