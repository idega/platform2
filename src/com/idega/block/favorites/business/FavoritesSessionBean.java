/*
 * $Id: FavoritesSessionBean.java,v 1.1 2004/11/05 13:26:10 laddi Exp $
 * Created on 4.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.favorites.business;

import java.rmi.RemoteException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOSessionBean;


/**
 * Last modified: 4.11.2004 15:49:17 by laddi
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class FavoritesSessionBean extends IBOSessionBean implements FavoritesSession {

	private String iFavoriteType;
	
	private FavoritesBusiness getBusiness() {
		try {
			return (FavoritesBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), FavoritesBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
	
	/**
	 * @return Returns the favoriteType.
	 */
	public String getFavoriteType() {
		if (iFavoriteType == null) {
			try {
				iFavoriteType = getBusiness().getFavoriteTypeIntranet();
			}
			catch (RemoteException re) {
				log(re);
			}
		}
		return iFavoriteType;
	}
	
	/**
	 * @param favoriteType The favoriteType to set.
	 */
	public void setFavoriteType(String favoriteType) {
		iFavoriteType = favoriteType;
	}
}