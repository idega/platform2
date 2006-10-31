package com.idega.block.building.data;


import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOCreateException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class ComplexTypeViewHomeImpl extends IDOFactory implements ComplexTypeViewHome {
	public Class getEntityInterfaceClass() {
		return ComplexTypeView.class;
	}

	public ComplexTypeView create() throws CreateException {
		return (ComplexTypeView) super.createIDO();
	}

	public ComplexTypeView findByPrimaryKey(Object pk) throws FinderException {
		return (ComplexTypeView) super.findByPrimaryKeyIDO(pk);
	}

	public ComplexTypeView findByPrimaryKey(ComplexTypeViewKey primaryKey) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((ComplexTypeViewBMPBean) entity).ejbFindByPrimaryKey(primaryKey);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public ComplexTypeView create(ComplexTypeViewKey primaryKey) throws CreateException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((ComplexTypeViewBMPBean) entity).ejbCreate(primaryKey);
		((ComplexTypeViewBMPBean) entity).ejbPostCreate();
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
		Collection ids = ((ComplexTypeViewBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByCategory(Integer categoryID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ComplexTypeViewBMPBean) entity).ejbFindByCategory(categoryID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}