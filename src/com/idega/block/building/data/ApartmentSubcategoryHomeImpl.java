package com.idega.block.building.data;


import com.idega.data.IDOFactory;
import javax.ejb.CreateException;
import com.idega.data.IDOEntity;
import javax.ejb.FinderException;
import java.util.Collection;

public class ApartmentSubcategoryHomeImpl extends IDOFactory implements
		ApartmentSubcategoryHome {
	public Class getEntityInterfaceClass() {
		return ApartmentSubcategory.class;
	}

	public ApartmentSubcategory create() throws CreateException {
		return (ApartmentSubcategory) super.createIDO();
	}

	public ApartmentSubcategory findByPrimaryKey(Object pk)
			throws FinderException {
		return (ApartmentSubcategory) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApartmentSubcategoryBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByCategory(Integer categoryID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApartmentSubcategoryBMPBean) entity)
				.ejbFindByCategory(categoryID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public ApartmentSubcategory findByCategoryAndName(String name,
			ApartmentCategory category) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((ApartmentSubcategoryBMPBean) entity)
				.ejbFindByCategoryAndName(name, category);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findByCategory(Integer[] categoryID)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApartmentSubcategoryBMPBean) entity)
				.ejbFindByCategory(categoryID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}