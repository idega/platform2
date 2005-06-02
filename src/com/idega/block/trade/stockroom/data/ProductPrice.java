/*
 * $Id: ProductPrice.java,v 1.21 2005/06/02 16:15:14 gimmi Exp $
 * Created on 2.6.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.trade.stockroom.data;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOEntity;
import com.idega.data.IDORelationshipException;


/**
 * 
 *  Last modified: $Date: 2005/06/02 16:15:14 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.21 $
 */
public interface ProductPrice extends IDOEntity {

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#invalidate
	 */
	public void invalidate();

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#validate
	 */
	public void validate();

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#getProductId
	 */
	public int getProductId();

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#setProductId
	 */
	public void setProductId(int id);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#getPriceCategory
	 */
	public PriceCategory getPriceCategory();

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#getPriceCategoryID
	 */
	public int getPriceCategoryID();

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#getPriceCategoryIDInteger
	 */
	public Integer getPriceCategoryIDInteger();

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#setPriceCategoryID
	 */
	public void setPriceCategoryID(int id);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#getCurrencyId
	 */
	public int getCurrencyId();

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#setCurrencyId
	 */
	public void setCurrencyId(int id);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#setCurrencyId
	 */
	public void setCurrencyId(Integer id);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#getPrice
	 */
	public float getPrice();

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#getDiscount
	 */
	public int getDiscount();

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#setPrice
	 */
	public void setPrice(float price);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#getPriceDate
	 */
	public Timestamp getPriceDate();

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#setPriceDate
	 */
	public void setPriceDate(Timestamp timestamp);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#getPriceType
	 */
	public int getPriceType();

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#setPriceType
	 */
	public void setPriceType(int type);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#setIsValid
	 */
	public void setIsValid(boolean isValid);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#getIsValid
	 */
	public boolean getIsValid();

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#getMaxUsage
	 */
	public int getMaxUsage();

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#setMaxUsage
	 */
	public void setMaxUsage(int maxUsage);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#getExactDate
	 */
	public Date getExactDate();

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#setExactDate
	 */
	public void setExactDate(Date date);

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#getTravelAddresses
	 */
	public Collection getTravelAddresses() throws IDORelationshipException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#getTimeframes
	 */
	public Collection getTimeframes() throws IDORelationshipException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#addTimeframe
	 */
	public void addTimeframe(Object timeframePK) throws IDOAddRelationshipException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductPriceBMPBean#addTravelAddress
	 */
	public void addTravelAddress(Object travelAddressPK) throws IDOAddRelationshipException;
}
