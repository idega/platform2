/*
 * Created on Oct 26, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
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
 * @author Anna
 */
public interface FavoritesBusiness extends IBOService {
    /**
     * @see com.idega.block.favorites.business.FavoritesBusinessBean#getFavorite
     */
    public Favorite getFavorite(Object primaryKey) throws FinderException,
            java.rmi.RemoteException;
    
    /**
     * @see com.idega.block.favorites.business.FavoritesBusinessBean#getFavorite
     */
    public Favorite getFavorite(User user, String name) throws FinderException, 
    				java.rmi.RemoteException;

    /**
     * @see com.idega.block.favorites.business.FavoritesBusinessBean#getFavorites
     */
    public Collection getFavorites(User user) throws FinderException,
            java.rmi.RemoteException;

    /**
     * @see com.idega.block.favorites.business.FavoritesBusinessBean#getQuickLinkFavorites
     */
    public Collection getQuickLinkFavorites(User user) throws FinderException,
            java.rmi.RemoteException;

    /**
     * @see com.idega.block.favorites.business.FavoritesBusinessBean#storeFavorite
     */
    public void storeFavorite(User user, String URL, String name,
            boolean quickLink) throws CreateException, java.rmi.RemoteException;

    /**
     * @see com.idega.block.favorites.business.FavoritesBusinessBean#storeFavorite
     */
    public void storeFavorite(Object pk, String URL, String name,
            boolean quickLink) throws CreateException, java.rmi.RemoteException;

    /**
     * @see com.idega.block.favorites.business.FavoritesBusinessBean#storeFavorite
     */
    public void storeFavorite(Object favoriteID, User user, String URL,
            String name, boolean quickLink) throws CreateException,
            java.rmi.RemoteException;

    /**
     * @see com.idega.block.favorites.business.FavoritesBusinessBean#deleteFavorite
     */
    public void deleteFavorite(Favorite favorite) throws RemoveException,
            java.rmi.RemoteException;

    /**
     * @see com.idega.block.favorites.business.FavoritesBusinessBean#hasURLAsFavorite
     */
    public boolean hasURLAsFavorite(User user, String URL)
            throws FinderException, java.rmi.RemoteException;

}
