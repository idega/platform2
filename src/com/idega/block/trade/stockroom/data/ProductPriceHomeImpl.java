/*
 * $Id: ProductPriceHomeImpl.java,v 1.5 2005/06/02 16:15:14 gimmi Exp $
 * Created on 2.6.2005
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
import com.idega.data.IDOException;
import com.idega.data.IDOFactory;
import com.idega.data.IDOLookupException;


/**
 * 
 *  Last modified: $Date: 2005/06/02 16:15:14 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.5 $
 */
public class ProductPriceHomeImpl extends IDOFactory implements ProductPriceHome {

	protected Class getEntityInterfaceClass() {
		return ProductPrice.class;
	}

	public ProductPrice create() throws javax.ejb.CreateException {
		return (ProductPrice) super.createIDO();
	}

	public ProductPrice findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (ProductPrice) super.findByPrimaryKeyIDO(pk);
	}

	public void clearPrices(int productId, int currencyId) throws IDOLookupException, FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		((ProductPriceBMPBean) entity).ejbHomeClearPrices(productId, currencyId);
		this.idoCheckInPooledEntity(entity);
	}

	public void clearPrices(int productId, int currencyId, String key) throws FinderException, IDOLookupException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		((ProductPriceBMPBean) entity).ejbHomeClearPrices(productId, currencyId, key);
		this.idoCheckInPooledEntity(entity);
	}

	public Collection findProductPrices(int productId, boolean netBookingOnly) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ProductPriceBMPBean) entity).ejbFindProductPrices(productId, netBookingOnly);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findProductPrices(int productId, int timeframeId, boolean netBookingOnly) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ProductPriceBMPBean) entity).ejbFindProductPrices(productId, timeframeId,
				netBookingOnly);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findProductPrices(int productId, int timeframeId, int addressId, boolean netBookingOnly,
			String key) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ProductPriceBMPBean) entity).ejbFindProductPrices(productId, timeframeId,
				addressId, netBookingOnly, key);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findProductPrices(int productId, int timeframeId, int addressId, boolean netBookingOnly)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ProductPriceBMPBean) entity).ejbFindProductPrices(productId, timeframeId,
				addressId, netBookingOnly);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findProductPrices(int productId, int timeframeId, int addressId, int[] visibility)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ProductPriceBMPBean) entity).ejbFindProductPrices(productId, timeframeId,
				addressId, visibility);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findProductPrices(int productId, int timeframeId, int addressId, int[] visibility, String key)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ProductPriceBMPBean) entity).ejbFindProductPrices(productId, timeframeId,
				addressId, visibility, key);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findMiscellaneousPrices(int productId, int timeframeId, int addressId, boolean netBookingOnly)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ProductPriceBMPBean) entity).ejbFindMiscellaneousPrices(productId, timeframeId,
				addressId, netBookingOnly);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findMiscellaneousPrices(int productId, int timeframeId, int addressId, boolean netBookingOnly,
			int currencyId) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ProductPriceBMPBean) entity).ejbFindMiscellaneousPrices(productId, timeframeId,
				addressId, netBookingOnly, currencyId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findProductPrices(int productId, int timeframeId, int addressId, boolean netBookingOnly,
			int countAsPersonStatus, int currencyId) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ProductPriceBMPBean) entity).ejbFindProductPrices(productId, timeframeId,
				addressId, netBookingOnly, countAsPersonStatus, currencyId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findProductPrices(int productId, int timeframeId, int addressId, boolean netBookingOnly,
			int countAsPersonStatus, int currencyId, String key) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ProductPriceBMPBean) entity).ejbFindProductPrices(productId, timeframeId,
				addressId, netBookingOnly, countAsPersonStatus, currencyId, key);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findProductPrices(int productId, int timeframeId, int addressId, int countAsPersonStatus,
			int currencyId, int visibility, String key) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ProductPriceBMPBean) entity).ejbFindProductPrices(productId, timeframeId,
				addressId, countAsPersonStatus, currencyId, visibility, key);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findProductPrices(int productId, int timeframeId, int addressId, int countAsPersonStatus,
			int currencyId, int visibility) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ProductPriceBMPBean) entity).ejbFindProductPrices(productId, timeframeId,
				addressId, countAsPersonStatus, currencyId, visibility);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findProductPrices(int productId, int timeframeId, int addressId, int countAsPersonStatus,
			int currencyId, int[] visibility, String key) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ProductPriceBMPBean) entity).ejbFindProductPrices(productId, timeframeId,
				addressId, countAsPersonStatus, currencyId, visibility, key);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findProductPrices(int productId, int timeframeId, int addressId, int currencyId,
			int priceCategoryId, Date exactDate) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ProductPriceBMPBean) entity).ejbFindProductPrices(productId, timeframeId,
				addressId, currencyId, priceCategoryId, exactDate);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public int[] getCurrenciesInUse(int productId) {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int[] theReturn = ((ProductPriceBMPBean) entity).ejbHomeGetCurrenciesInUse(productId);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int[] getCurrenciesInUse(int productId, int visibility) {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int[] theReturn = ((ProductPriceBMPBean) entity).ejbHomeGetCurrenciesInUse(productId, visibility);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int[] getCurrenciesInUse(int productId, int[] visibility) {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int[] theReturn = ((ProductPriceBMPBean) entity).ejbHomeGetCurrenciesInUse(productId, visibility);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public boolean hasProductPrices(int productId, int timeframeId, int addressId, boolean netBookingOnly, String key)
			throws FinderException, IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		boolean theReturn = ((ProductPriceBMPBean) entity).ejbHomeHasProductPrices(productId, timeframeId, addressId,
				netBookingOnly, key);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public ProductPrice findByData(int productId, int timeframeId, int addressId, int currencyId, int priceCategoryId,
			Date date) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((ProductPriceBMPBean) entity).ejbFindByData(productId, timeframeId, addressId, currencyId,
				priceCategoryId, date);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findBySQL(String sql) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ProductPriceBMPBean) entity).ejbFindBySQL(sql);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
