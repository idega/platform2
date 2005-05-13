/*
 * $Id: ProductPriceHome.java,v 1.4 2005/05/13 04:36:09 gimmi Exp $
 * Created on 13.5.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.trade.stockroom.data;

import java.sql.Date;
import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOHome;
import com.idega.data.IDOLookupException;


/**
 * 
 *  Last modified: $Date: 2005/05/13 04:36:09 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.4 $
 */
public interface ProductPriceHome extends IDOHome {

	public ProductPrice create() throws javax.ejb.CreateException;

	public ProductPrice findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#ejbHomeClearPrices
	 */
	public void clearPrices(int productId, int currencyId) throws IDOLookupException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#ejbHomeClearPrices
	 */
	public void clearPrices(int productId, int currencyId, String key) throws FinderException, IDOLookupException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#ejbFindProductPrices
	 */
	public Collection findProductPrices(int productId, boolean netBookingOnly) throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#ejbFindProductPrices
	 */
	public Collection findProductPrices(int productId, int timeframeId, boolean netBookingOnly) throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#ejbFindProductPrices
	 */
	public Collection findProductPrices(int productId, int timeframeId, int addressId, boolean netBookingOnly,
			String key) throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#ejbFindProductPrices
	 */
	public Collection findProductPrices(int productId, int timeframeId, int addressId, boolean netBookingOnly)
			throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#ejbFindProductPrices
	 */
	public Collection findProductPrices(int productId, int timeframeId, int addressId, int[] visibility)
			throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#ejbFindProductPrices
	 */
	public Collection findProductPrices(int productId, int timeframeId, int addressId, int[] visibility, String key)
			throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#ejbFindMiscellaneousPrices
	 */
	public Collection findMiscellaneousPrices(int productId, int timeframeId, int addressId, boolean netBookingOnly)
			throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#ejbFindMiscellaneousPrices
	 */
	public Collection findMiscellaneousPrices(int productId, int timeframeId, int addressId, boolean netBookingOnly,
			int currencyId) throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#ejbFindProductPrices
	 */
	public Collection findProductPrices(int productId, int timeframeId, int addressId, boolean netBookingOnly,
			int countAsPersonStatus, int currencyId) throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#ejbFindProductPrices
	 */
	public Collection findProductPrices(int productId, int timeframeId, int addressId, boolean netBookingOnly,
			int countAsPersonStatus, int currencyId, String key) throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#ejbFindProductPrices
	 */
	public Collection findProductPrices(int productId, int timeframeId, int addressId, int countAsPersonStatus,
			int currencyId, int visibility, String key) throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#ejbFindProductPrices
	 */
	public Collection findProductPrices(int productId, int timeframeId, int addressId, int countAsPersonStatus,
			int currencyId, int visibility) throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#ejbFindProductPrices
	 */
	public Collection findProductPrices(int productId, int timeframeId, int addressId, int countAsPersonStatus,
			int currencyId, int[] visibility, String key) throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#ejbFindProductPrices
	 */
	public Collection findProductPrices(int productId, int timeframeId, int addressId, int currencyId,
			int priceCategoryId, Date exactDate) throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#ejbHomeGetCurrenciesInUse
	 */
	public int[] getCurrenciesInUse(int productId);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#ejbHomeGetCurrenciesInUse
	 */
	public int[] getCurrenciesInUse(int productId, int visibility);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#ejbHomeGetCurrenciesInUse
	 */
	public int[] getCurrenciesInUse(int productId, int[] visibility);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#ejbFindByData
	 */
	public ProductPrice findByData(int productId, int timeframeId, int addressId, int currencyId, int priceCategoryId,
			Date date) throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#ejbFindBySQL
	 */
	public Collection findBySQL(String sql) throws FinderException;
}
