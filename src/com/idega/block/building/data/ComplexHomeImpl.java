package com.idega.block.building.data;


import com.idega.data.IDOFactory;
import javax.ejb.CreateException;
import com.idega.data.IDOEntity;
import javax.ejb.FinderException;
import java.util.Collection;

public class ComplexHomeImpl extends IDOFactory implements ComplexHome {
	public Class getEntityInterfaceClass() {
		return Complex.class;
	}

	public Complex create() throws CreateException {
		return (Complex) super.createIDO();
	}

	public Complex findByPrimaryKey(Object pk) throws FinderException {
		return (Complex) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ComplexBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllIncludingLocked() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ComplexBMPBean) entity).ejbFindAllIncludingLocked();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Complex findComplexByName(String name) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((ComplexBMPBean) entity).ejbFindComplexByName(name);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}