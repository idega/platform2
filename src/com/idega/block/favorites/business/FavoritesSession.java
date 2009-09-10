/*
 * $Id: FavoritesSession.java,v 1.1 2004/11/05 13:26:10 laddi Exp $
 * Created on 4.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.favorites.business;


import com.idega.business.IBOSession;


/**
 * Last modified: 4.11.2004 15:55:30 by laddi
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface FavoritesSession extends IBOSession {

	/**
	 * @see com.idega.block.favorites.business.FavoritesSessionBean#getFavoriteType
	 */
	public String getFavoriteType() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.favorites.business.FavoritesSessionBean#setFavoriteType
	 */
	public void setFavoriteType(String favoriteType) throws java.rmi.RemoteException;

}
