package com.idega.block.building.data;


import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class ApartmentCategoryHomeImpl extends IDOFactory implements
		ApartmentCategoryHome {
	public Class getEntityInterfaceClass() {
		return ApartmentCategory.class;
	}

	public ApartmentCategory create() throws CreateException {
		return (ApartmentCategory) super.createIDO();
	}

	public ApartmentCategory findByPrimaryKey(Object pk) throws FinderException {
		return (ApartmentCategory) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApartmentCategoryBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByComplex(Integer complexID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApartmentCategoryBMPBean) entity)
				.ejbFindByComplex(complexID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}