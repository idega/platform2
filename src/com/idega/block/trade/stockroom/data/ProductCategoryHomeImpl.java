package com.idega.block.trade.stockroom.data;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;


/**
 * @author gimmi
 */
public class ProductCategoryHomeImpl extends IDOFactory implements ProductCategoryHome {

	protected Class getEntityInterfaceClass() {
		return ProductCategory.class;
	}

	public ProductCategory create() throws javax.ejb.CreateException {
		return (ProductCategory) super.createIDO();
	}

	public ProductCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (ProductCategory) super.findByPrimaryKeyIDO(pk);
	}

	public ProductCategory createLegacy() {
		try {
			return create();
		}
		catch (javax.ejb.CreateException ce) {
			throw new RuntimeException("CreateException:" + ce.getMessage());
		}
	}

	public ProductCategory findByPrimaryKey(int id) throws javax.ejb.FinderException {
		return (ProductCategory) super.findByPrimaryKeyIDO(id);
	}

	public ProductCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException {
		try {
			return findByPrimaryKey(id);
		}
		catch (javax.ejb.FinderException fe) {
			throw new java.sql.SQLException("FinderException:" + fe.getMessage());
		}
	}

	public ProductCategory getProductCategory(String type) throws FinderException, RemoteException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		ProductCategory theReturn = ((ProductCategoryBMPBean) entity).ejbHomeGetProductCategory(type);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection theReturn = ((ProductCategoryBMPBean) entity).ejbHomeFindAll();
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}
}
