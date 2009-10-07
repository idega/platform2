package com.idega.block.building.data;


import com.idega.data.IDOFactory;
import javax.ejb.CreateException;
import com.idega.data.IDOEntity;
import javax.ejb.FinderException;
import java.util.Collection;

public class ApartmentTypeHomeImpl extends IDOFactory implements
		ApartmentTypeHome {
	public Class getEntityInterfaceClass() {
		return ApartmentType.class;
	}

	public ApartmentType create() throws CreateException {
		return (ApartmentType) super.createIDO();
	}

	public ApartmentType findByPrimaryKey(Object pk) throws FinderException {
		return (ApartmentType) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApartmentTypeBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllIncludingLocked() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApartmentTypeBMPBean) entity)
				.ejbFindAllIncludingLocked();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByBuilding(Integer buildingID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApartmentTypeBMPBean) entity)
				.ejbFindByBuilding(buildingID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySubcategory(Integer categoryID)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApartmentTypeBMPBean) entity)
				.ejbFindBySubcategory(categoryID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public ApartmentType findBySubcategoryAndAbbrevation(String abbrevation,
			ApartmentSubcategory subCategory) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((ApartmentTypeBMPBean) entity)
				.ejbFindBySubcategoryAndAbbrevation(abbrevation, subCategory);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findByComplex(Integer complexID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApartmentTypeBMPBean) entity)
				.ejbFindByComplex(complexID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}