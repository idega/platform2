/*
 * Created on 2005-sep-09
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.idega.block.school.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;

/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class SchoolTypeHomeImpl extends IDOFactory implements SchoolTypeHome {
	protected Class getEntityInterfaceClass() {
		return SchoolType.class;
	}

	public SchoolType create() throws javax.ejb.CreateException {
		return (SchoolType) super.createIDO();
	}

	public SchoolType findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (SchoolType) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllSchoolTypes() throws javax.ejb.FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolTypeBMPBean) entity)
				.ejbFindAllSchoolTypes();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByCategory(String category)
			throws javax.ejb.FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolTypeBMPBean) entity)
				.ejbFindAllByCategory(category);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByCategory(String category,
			boolean showFreetimeTypes) throws javax.ejb.FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolTypeBMPBean) entity)
				.ejbFindAllByCategory(category, showFreetimeTypes);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public SchoolType findByTypeKey(String typeKey)
			throws javax.ejb.FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolTypeBMPBean) entity).ejbFindByTypeKey(typeKey);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolType findByTypeString(String typeString)
			throws javax.ejb.FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolTypeBMPBean) entity)
				.ejbFindByTypeString(typeString);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findAllFreetimeTypes() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolTypeBMPBean) entity)
				.ejbFindAllFreetimeTypes();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllFreetimeTypes(String category)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolTypeBMPBean) entity)
				.ejbFindAllFreetimeTypes(category);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
