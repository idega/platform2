package com.idega.block.category.data;

import java.util.Collection;
import java.util.List;
import javax.ejb.FinderException;
import com.idega.core.component.data.ICObjectInstance;
import com.idega.data.IDOFactory;


/**
 * @author gimmi
 */
public class ICCategoryHomeImpl extends IDOFactory implements ICCategoryHome {

	protected Class getEntityInterfaceClass() {
		return ICCategory.class;
	}

	public ICCategory create() throws javax.ejb.CreateException {
		return (ICCategory) super.createIDO();
	}

	public ICCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (ICCategory) super.findByPrimaryKeyIDO(pk);
	}

	public ICCategory createLegacy() {
		try {
			return create();
		}
		catch (javax.ejb.CreateException ce) {
			throw new RuntimeException("CreateException:" + ce.getMessage());
		}
	}

	public ICCategory findByPrimaryKey(int id) throws javax.ejb.FinderException {
		return (ICCategory) super.findByPrimaryKeyIDO(id);
	}

	public ICCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException {
		try {
			return findByPrimaryKey(id);
		}
		catch (javax.ejb.FinderException fe) {
			throw new java.sql.SQLException("FinderException:" + fe.getMessage());
		}
	}

	public List getListOfCategoryForObjectInstance(ICObjectInstance obj, boolean order) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		List theReturn = ((ICCategoryBMPBean) entity).ejbHomeGetListOfCategoryForObjectInstance(obj, order);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getOrderNumber(Category category, ICObjectInstance instance) throws javax.ejb.FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((ICCategoryBMPBean) entity).ejbHomeGetOrderNumber(category, instance);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getOrderNumber(Category category, String objectInstanceId) throws javax.ejb.FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((ICCategoryBMPBean) entity).ejbHomeGetOrderNumber(category, objectInstanceId);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public boolean setOrderNumber(Category category, ICObjectInstance instance, int orderNumber)
			throws com.idega.data.IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		boolean theReturn = ((ICCategoryBMPBean) entity).ejbHomeSetOrderNumber(category, instance, orderNumber);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findRootsByType(String type) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ICCategoryBMPBean) entity).ejbFindRootsByType(type);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByObjectInstance(int iObjectInstanceID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ICCategoryBMPBean) entity).ejbFindAllByObjectInstance(iObjectInstanceID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByObjectInstance(ICObjectInstance instance) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ICCategoryBMPBean) entity).ejbFindAllByObjectInstance(instance);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection theReturn = ((ICCategoryBMPBean) entity).ejbHomeFindAll();
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}
}
