package com.idega.block.trade.stockroom.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOHome;
import com.idega.data.IDORelationshipException;
import com.idega.util.IWTimestamp;


/**
 * @author gimmi
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
	public Collection findBySupplyPool(SupplyPool pool) throws IDORelationshipException, FinderException;
}
