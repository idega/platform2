/*
 * $Id: ProductHomeImpl.java,v 1.8 2005/07/08 14:10:50 gimmi Exp $
 * Created on Jul 8, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.trade.stockroom.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDOException;
import com.idega.data.IDOFactory;
import com.idega.data.IDORelationshipException;
import com.idega.util.IWTimestamp;


/**
 * 
 *  Last modified: $Date: 2005/07/08 14:10:50 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.8 $
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

	public Collection findProducts(int supplierId, int firstEntity, int lastEntity) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ProductBMPBean) entity).ejbFindProducts(supplierId, firstEntity, lastEntity);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public int getProductCount(int supplierId) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((ProductBMPBean) entity).ejbHomeGetProductCount(supplierId);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
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

	public Collection findBySupplyPool(SupplyPool pool) throws IDORelationshipException, FinderException,
			IDOCompositePrimaryKeyException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ProductBMPBean) entity).ejbFindBySupplyPool(pool);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
