package com.idega.block.trade.stockroom.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;
import com.idega.data.IDORelationshipException;
import com.idega.util.IWTimestamp;


/**
 * @author gimmi
 */
public class ProductHomeImpl extends IDOFactory implements ProductHome {

	protected Class getEntityInterfaceClass() {
		return Product.class;
	}

	public Product create() throws javax.ejb.CreateException {
		return (Product) super.createIDO();
	}

	public Product findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (Product) super.findByPrimaryKeyIDO(pk);
	}

	public Product createLegacy() {
		try {
			return create();
		}
		catch (javax.ejb.CreateException ce) {
			throw new RuntimeException("CreateException:" + ce.getMessage());
		}
	}

	public Product findByPrimaryKey(int id) throws javax.ejb.FinderException {
		return (Product) super.findByPrimaryKeyIDO(id);
	}

	public Product findByPrimaryKeyLegacy(int id) throws java.sql.SQLException {
		try {
			return findByPrimaryKey(id);
		}
		catch (javax.ejb.FinderException fe) {
			throw new java.sql.SQLException("FinderException:" + fe.getMessage());
		}
	}

	public Collection findProductsOrderedByProductCategory(int supplierId) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ProductBMPBean) entity).ejbFindProductsOrderedByProductCategory(supplierId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findProductsOrderedByProductCategory(int supplierId, IWTimestamp stamp) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ProductBMPBean) entity).ejbFindProductsOrderedByProductCategory(supplierId, stamp);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findProductsOrderedByProductCategory(int supplierId, IWTimestamp from, IWTimestamp to)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ProductBMPBean) entity).ejbFindProductsOrderedByProductCategory(supplierId, from,
				to);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findProducts(int supplierId) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ProductBMPBean) entity).ejbFindProducts(supplierId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findProducts(int supplierId, int productCategoryId, IWTimestamp from, IWTimestamp to)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ProductBMPBean) entity).ejbFindProducts(supplierId, productCategoryId, from, to);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findProducts(int supplierId, int productCategoryId, IWTimestamp from, IWTimestamp to,
			String orderBy) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ProductBMPBean) entity).ejbFindProducts(supplierId, productCategoryId, from, to,
				orderBy);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public int getProductFilterNotConnectedToAnyProductCategory() {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((ProductBMPBean) entity).ejbHomeGetProductFilterNotConnectedToAnyProductCategory();
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findProducts(int supplierId, int productCategoryId, IWTimestamp from, IWTimestamp to,
			String orderBy, int localeId, int filter) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ProductBMPBean) entity).ejbFindProducts(supplierId, productCategoryId, from, to,
				orderBy, localeId, filter);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findProducts(int supplierId, int productCategoryId, IWTimestamp from, IWTimestamp to,
			String orderBy, int localeId, int filter, boolean useTimeframes) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ProductBMPBean) entity).ejbFindProducts(supplierId, productCategoryId, from, to,
				orderBy, localeId, filter, useTimeframes);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySupplyPool(SupplyPool pool) throws IDORelationshipException, FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ProductBMPBean) entity).ejbFindBySupplyPool(pool);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
