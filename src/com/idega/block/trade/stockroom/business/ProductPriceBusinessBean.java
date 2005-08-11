/*
 * $Id: ProductPriceBusinessBean.java,v 1.1 2005/08/11 14:02:46 gimmi Exp $
 * Created on Aug 10, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.trade.stockroom.business;

import java.sql.Date;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.data.PriceCategoryBMPBean;
import com.idega.block.trade.stockroom.data.ProductPrice;
import com.idega.block.trade.stockroom.data.ProductPriceHome;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORuntimeException;
import com.idega.util.IWTimestamp;


public class ProductPriceBusinessBean extends IBOServiceBean  implements ProductPriceBusiness{

	private HashMap mapForProductPriceMap = new HashMap();
	
	public Collection getProductPrices(int productId, int timeframeId, int addressId, int[] visibility, IWTimestamp date) throws FinderException {
		return getProductPrices(productId, timeframeId, addressId, -1, visibility, null, date);
	}

	public Collection getProductPrices(int productId, int timeframeId, int addressId, boolean netbookingOnly, IWTimestamp date) throws FinderException {
		return getProductPrices(productId, timeframeId, addressId, -1, netbookingOnly, null, date);
	}

	public Collection getProductPrices(int productId, int timeframeId, int addressId, boolean netbookingOnly, String key, IWTimestamp date) throws FinderException {
		return getProductPrices(productId, timeframeId, addressId, -1, netbookingOnly, key, date);
	}
	
	public Collection getProductPrices(int productId, int timeframeId, int addressId, int currencyId, boolean netbookingOnly, String key, IWTimestamp date) throws FinderException {
		int[] vis;
	  	if (netbookingOnly) {
	  		vis = new int[] {PriceCategoryBMPBean.PRICE_VISIBILITY_BOTH_PRIVATE_AND_PUBLIC, PriceCategoryBMPBean.PRICE_VISIBILITY_PUBLIC};	
	  	}else {
	  		vis = new int[] {PriceCategoryBMPBean.PRICE_VISIBILITY_BOTH_PRIVATE_AND_PUBLIC, PriceCategoryBMPBean.PRICE_VISIBILITY_PRIVATE};//, PriceCategoryBMPBean.PRICE_VISIBILITY_PUBLIC};	
	  	}
	  	return getProductPrices(productId, timeframeId, addressId, currencyId, vis, key, date);
	}
		
	public Collection getProductPrices(int productId, int timeframeId, int addressId, int currencyId, int[] visibility, String key, IWTimestamp date) throws FinderException {

		String visString = "";
		if (visibility != null) {
			for (int i = 0; i < visibility.length; i++) {
				visString += visibility[i];
			}
		}
		boolean lookForDate = false;
		String mapKey = productId+"_"+timeframeId+"_"+addressId+"_"+currencyId+"_"+visString+"_"+key;
		String mapDateKey = mapKey;
		if (date != null) {
			mapDateKey += "_"+date.toSQLDateString();
			lookForDate = true;
		}
		
		HashMap priceMap = getPriceMapForProduct(new Integer(productId));
		
//		System.out.println("[ProductPriceBusinessBean] mapKey = "+mapKey);
		
//		Timer t =  new Timer();
//		t.start();
		
		Collection prices = null;

		// Checking for stored price for this day
		if (date != null) {
			if (priceMap.containsKey(mapDateKey)) {
				prices = (Collection) priceMap.get(mapDateKey);
				lookForDate = false;
			}
		}
		
		// Checking for stored price in general
		if (prices == null) {
			prices = (Collection) priceMap.get(mapKey);
		}
		
		if (prices == null || lookForDate) {
			Collection tmp = null;
			if (prices != null) {
				tmp = prices;
			} else {
				tmp = getProductPriceHome().findProductPrices(productId, timeframeId, addressId, visibility, key);
			}
			
			if (date != null) {
				prices = new Vector();
				Date exactDate = date.getDate();
				
				Iterator iter = tmp.iterator();
				ProductPrice price;
				while (iter.hasNext()) {
					price = (ProductPrice) iter.next();
					
					Collection coll = getProductPriceHome().findProductPrices(productId, timeframeId, addressId, currencyId, price.getPriceCategoryID(), exactDate);

					if (coll != null && !coll.isEmpty()) {
						Iterator tmpIter = coll.iterator();
						while (tmpIter.hasNext()) {
							prices.add( (ProductPrice) tmpIter.next() );
						}
					} else {
						prices.add(price);
					}
					
				}
				// Adding the new "improved" prices to the map
				priceMap.put(mapDateKey, prices);
			} else {
				// Adding the orginal collection to the map
				priceMap.put(mapKey, tmp);
				prices = tmp;
			}
			
		}
		
//		t.stop();
//		System.out.println("[ProductPriceBusinessBean] time to get prices = "+t.getTimeString());
		
		return prices;
	}
	
	private HashMap getPriceMapForProduct(Object productID) {
		HashMap t = (HashMap) mapForProductPriceMap.get(productID);
		if (t == null) {
			t = new HashMap();
			mapForProductPriceMap.put(productID, t);
		}
		
		return t;
	}
	
	public boolean invalidateCache(int productId) {
		mapForProductPriceMap.put(new Integer(productId), null);
		return true;
	}
	
	public boolean invalidateCache() {
		mapForProductPriceMap.clear();
		return true;
	}
	
	
	
	public Collection getMiscellaneousPrices(int productId, int timeframeId, int addressId, boolean netBookingOnly) throws FinderException {
		return getProductPriceHome().findMiscellaneousPrices(productId, timeframeId, addressId, netBookingOnly, -1);
	}
	
	public Collection getMiscellaneousPrices(int productId, int timeframeId, int addressId, boolean netBookingOnly, int currencyId) throws FinderException {
		return getProductPriceHome().findProductPrices(productId, timeframeId, addressId, netBookingOnly, 1, currencyId, null);
	}

	
	public ProductPriceHome getProductPriceHome() {
		try {
			return (ProductPriceHome) IDOLookup.getHome(ProductPrice.class);
		}
		catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}

}
