package com.idega.block.building.data;


import com.idega.data.IDOFactory;
import javax.ejb.CreateException;
import com.idega.data.IDOEntity;
import javax.ejb.FinderException;
import java.util.Collection;

public class BuildingHomeImpl extends IDOFactory implements BuildingHome {
	public Class getEntityInterfaceClass() {
		return Building.class;
	}

	public Building create() throws CreateException {
		return (Building) super.createIDO();
	}

	public Building findByPrimaryKey(Object pk) throws FinderException {
		return (Building) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((BuildingBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllIncludingLocked() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((BuildingBMPBean) entity).ejbFindAllIncludingLocked();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByComplex(Integer complexID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((BuildingBMPBean) entity).ejbFindByComplex(complexID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Building findByComplexAndName(String name, Complex complex)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((BuildingBMPBean) entity).ejbFindByComplexAndName(name,
				complex);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection getImageFilesByComplex(Integer complexID)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection theReturn = ((BuildingBMPBean) entity)
				.ejbHomeGetImageFilesByComplex(complexID);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findByComplex(Complex complex) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((BuildingBMPBean) entity).ejbFindByComplex(complex);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}