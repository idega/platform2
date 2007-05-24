package com.idega.block.building.data;


import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

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
}