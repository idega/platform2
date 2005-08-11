/*
 * $Id: ProductPriceBusiness.java,v 1.1 2005/08/11 14:02:46 gimmi Exp $
 * Created on Aug 11, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.trade.stockroom.business;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.data.ProductPriceHome;
import com.idega.business.IBOService;
import com.idega.util.IWTimestamp;


/**
 * 
 *  Last modified: $Date: 2005/08/11 14:02:46 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.1 $
 */
public interface ProductPriceBusiness extends IBOService {

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductPriceBusinessBean#getProductPrices
	 */
	public Collection getProductPrices(int productId, int timeframeId, int addressId, int[] visibility, IWTimestamp date)
			throws FinderException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductPriceBusinessBean#getProductPrices
	 */
	public Collection getProductPrices(int productId, int timeframeId, int addressId, boolean netbookingOnly,
			IWTimestamp date) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductPriceBusinessBean#getProductPrices
	 */
	public Collection getProductPrices(int productId, int timeframeId, int addressId, boolean netbookingOnly,
			String key, IWTimestamp date) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductPriceBusinessBean#getProductPrices
	 */
	public Collection getProductPrices(int productId, int timeframeId, int addressId, int currencyId,
			boolean netbookingOnly, String key, IWTimestamp date) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductPriceBusinessBean#getProductPrices
	 */
	public Collection getProductPrices(int productId, int timeframeId, int addressId, int currencyId, int[] visibility,
			String key, IWTimestamp date) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductPriceBusinessBean#invalidateCache
	 */
	public boolean invalidateCache(int productId) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductPriceBusinessBean#invalidateCache
	 */
	public boolean invalidateCache() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductPriceBusinessBean#getMiscellaneousPrices
	 */
	public Collection getMiscellaneousPrices(int productId, int timeframeId, int addressId, boolean netBookingOnly)
			throws FinderException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductPriceBusinessBean#getMiscellaneousPrices
	 */
	public Collection getMiscellaneousPrices(int productId, int timeframeId, int addressId, boolean netBookingOnly,
			int currencyId) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ProductPriceBusinessBean#getProductPriceHome
	 */
	public ProductPriceHome getProductPriceHome() throws java.rmi.RemoteException;
}
