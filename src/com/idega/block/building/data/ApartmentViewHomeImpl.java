package com.idega.block.building.data;


import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class ApartmentViewHomeImpl extends IDOFactory implements
		ApartmentViewHome {
	public Class getEntityInterfaceClass() {
		return ApartmentView.class;
	}

	public ApartmentView create() throws CreateException {
		return (ApartmentView) super.createIDO();
	}

	public ApartmentView findByPrimaryKey(Object pk) throws FinderException {
		return (ApartmentView) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findByComplex(Integer complexID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApartmentViewBMPBean) entity)
				.ejbFindByComplex(complexID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByBuilding(Integer buildingID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApartmentViewBMPBean) entity)
				.ejbFindByBuilding(buildingID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByFloor(Integer floorID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApartmentViewBMPBean) entity)
				.ejbFindByFloor(floorID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByCategory(Integer categoryID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApartmentViewBMPBean) entity)
				.ejbFindByCategory(categoryID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByType(Integer typeID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApartmentViewBMPBean) entity).ejbFindByType(typeID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySubcategory(Integer subcategoryID)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApartmentViewBMPBean) entity)
				.ejbFindBySubcategory(subcategoryID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByApartmentName(String name) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApartmentViewBMPBean) entity)
				.ejbFindByApartmentName(name);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApartmentViewBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}