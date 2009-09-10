package com.idega.block.building.data;


import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOCreateException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class ComplexSubcategoryViewHomeImpl extends IDOFactory implements
		ComplexSubcategoryViewHome {
	public Class getEntityInterfaceClass() {
		return ComplexSubcategoryView.class;
	}

	public ComplexSubcategoryView create() throws CreateException {
		return (ComplexSubcategoryView) super.createIDO();
	}

	public ComplexSubcategoryView findByPrimaryKey(Object pk)
			throws FinderException {
		return (ComplexSubcategoryView) super.findByPrimaryKeyIDO(pk);
	}

	public ComplexSubcategoryView findByPrimaryKey(ComplexSubcategoryViewKey primaryKey)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((ComplexSubcategoryViewBMPBean) entity)
				.ejbFindByPrimaryKey(primaryKey);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public ComplexSubcategoryView create(ComplexSubcategoryViewKey primaryKey)
			throws CreateException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((ComplexSubcategoryViewBMPBean) entity)
				.ejbCreate(primaryKey);
		((ComplexSubcategoryViewBMPBean) entity).ejbPostCreate();
		this.idoCheckInPooledEntity(entity);
		try {
			return findByPrimaryKey(pk);
		} catch (FinderException fe) {
			throw new IDOCreateException(fe);
		} catch (Exception e) {
			throw new IDOCreateException(e);
		}
	}

	public Collection findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ComplexSubcategoryViewBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByCategory(Integer categoryID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ComplexSubcategoryViewBMPBean) entity)
				.ejbFindByCategory(categoryID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}