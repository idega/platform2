/*
 * $Id: FavoritesBusiness.java,v 1.3 2004/11/05 13:26:10 laddi Exp $
 * Created on 3.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.favorites.business;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.favorites.data.Favorite;
import com.idega.business.IBOService;
import com.idega.user.data.User;


/**
 * Last modified: 3.11.2004 10:34:53 by laddi
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3 $
 */
public interface FavoritesBusiness extends IBOService {

	/**
	 * @see com.idega.block.favorites.business.FavoritesBusinessBean#getFavorite
	 */
	public Favorite getFavorite(Object primaryKey) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.favorites.business.FavoritesBusinessBean#getFavoriteByName
	 */
	public Favorite getFavoriteByName(User user, String name) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.favorites.business.FavoritesBusinessBean#getFavoriteByName
	 */
	public Favorite getFavoriteByName(User user, String name, String favoriteType) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.favorites.business.FavoritesBusinessBean#getFavoriteByURL
	 */
	public Favorite getFavoriteByURL(User user, String URL) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.favorites.business.FavoritesBusinessBean#getFavoriteByURL
	 */
	public Favorite getFavoriteByURL(User user, String URL, String favoriteType) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.favorites.business.FavoritesBusinessBean#getFavorites
	 */
	public Collection getFavorites(User user) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.favorites.business.FavoritesBusinessBean#getFavorites
	 */
	public Collection getFavorites(User user, String favoriteType) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.favorites.business.FavoritesBusinessBean#getQuickLinkFavorites
	 */
	public Collection getQuickLinkFavorites(User user) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.favorites.business.FavoritesBusinessBean#storeFavorite
	 */
	public void storeFavorite(User user, String URL, String name, String favoriteType, boolean quickLink) throws CreateException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.favorites.business.FavoritesBusinessBean#storeFavorite
	 */
	public void storeFavorite(Object pk, String URL, String name, String favoriteType, boolean quickLink) throws CreateException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.favorites.business.FavoritesBusinessBean#storeFavorite
	 */
	public void storeFavorite(Object favoriteID, User user, String URL, String name, String favoriteType, boolean quickLink) throws CreateException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.favorites.business.FavoritesBusinessBean#deleteFavorite
	 */
	public void deleteFavorite(Favorite favorite) throws RemoveException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.favorites.business.FavoritesBusinessBean#hasURLAsFavorite
	 */
	public boolean hasURLAsFavorite(User user, String URL) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.favorites.business.FavoritesBusinessBean#getFavoriteTypeIntranet
	 */
	public String getFavoriteTypeIntranet() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.favorites.business.FavoritesBusinessBean#getFavoriteTypeInternet
	 */
	public String getFavoriteTypeInternet() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.favorites.business.FavoritesBusinessBean#getFavoriteTypeDocument
	 */
	public String getFavoriteTypeDocument() throws java.rmi.RemoteException;

}
