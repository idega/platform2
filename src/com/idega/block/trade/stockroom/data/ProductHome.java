/*
 * $Id: ProductHome.java,v 1.7 2005/06/16 21:04:36 gimmi Exp $
 * Created on 16.6.2005
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
import com.idega.data.IDOHome;
import com.idega.data.IDORelationshipException;
import com.idega.util.IWTimestamp;


/**
 * 
 *  Last modified: $Date: 2005/06/16 21:04:36 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.7 $
 */
public interface ProductHome extends IDOHome {

	public Product create() throws javax.ejb.CreateException;

	public Product findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	public Product findByPrimaryKey(int id) throws javax.ejb.FinderException;

	public Product findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#ejbFindProductsOrderedByProductCategory
	 */
	public Collection findProductsOrderedByProductCategory(int supplierId) throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#ejbFindProductsOrderedByProductCategory
	 */
	public Collection findProductsOrderedByProductCategory(int supplierId, IWTimestamp stamp) throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#ejbFindProductsOrderedByProductCategory
	 */
	public Collection findProductsOrderedByProductCategory(int supplierId, IWTimestamp from, IWTimestamp to)
			throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#ejbFindProducts
	 */
	public Collection findProducts(int supplierId) throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#ejbFindProducts
	 */
	public Collection findProducts(int supplierId, int firstEntity, int lastEntity) throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#ejbHomeGetProductCount
	 */
	public int getProductCount(int supplierId) throws IDOException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#ejbFindProducts
	 */
	public Collection findProducts(int supplierId, int productCategoryId, IWTimestamp from, IWTimestamp to)
			throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#ejbFindProducts
	 */
	public Collection findProducts(int supplierId, int productCategoryId, IWTimestamp from, IWTimestamp to,
			String orderBy) throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#ejbHomeGetProductFilterNotConnectedToAnyProductCategory
	 */
	public int getProductFilterNotConnectedToAnyProductCategory();

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#ejbFindProducts
	 */
	public Collection findProducts(int supplierId, int productCategoryId, IWTimestamp from, IWTimestamp to,
			String orderBy, int localeId, int filter) throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#ejbFindProducts
	 */
	public Collection findProducts(int supplierId, int productCategoryId, IWTimestamp from, IWTimestamp to,
			String orderBy, int localeId, int filter, boolean useTimeframes) throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductBMPBean#ejbFindBySupplyPool
	 */
	public Collection findBySupplyPool(SupplyPool pool) throws IDORelationshipException, FinderException,
			IDOCompositePrimaryKeyException;
}
