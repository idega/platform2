/*
 * Created on Oct 19, 2004
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
import com.idega.block.favorites.data.FavoriteHome;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author Anna
 */
public class FavoritesBusinessBean extends IBOServiceBean implements
		FavoritesBusiness {
	
	private FavoriteHome getFavoriteHome() {
		try {
			return (FavoriteHome) IDOLookup.getHome(Favorite.class);
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
	
	public Favorite getFavorite(Object primaryKey) throws FinderException {
	    return getFavoriteHome().findByPrimaryKey(primaryKey);
	}
	
	public Favorite getFavorite(User user, String name) throws FinderException {
	    return getFavoriteHome().findByUserAndName(user, name);
	}

	public Collection getFavorites(User user) throws FinderException {
		return getFavoriteHome().findAllByUser(user);
	}

	public Collection getQuickLinkFavorites(User user) throws FinderException {
		return getFavoriteHome().findAllQuickLinksByUser(user);
	}
	//creates and stores a new favoritelink
	public void storeFavorite(User user, String URL, String name,
			boolean quickLink) throws CreateException {
		storeFavorite(null, user, URL, name, quickLink);
	}
	//finds (edit) and stores an existing favoritelink 
	public void storeFavorite(Object pk, String URL, String name,
			boolean quickLink) throws CreateException{
		storeFavorite(pk, null,URL, name, quickLink);
		
	}
	//does the actual storing of the favoritelink
	public void storeFavorite(Object favoriteID, User user, String URL, String name,
			boolean quickLink) throws CreateException{
		Favorite favorites = null;
		
		if(favoriteID != null) {
			try{
					favorites = getFavoriteHome().findByPrimaryKey(new Integer(favoriteID.toString()));
			}
			catch(FinderException fe){
				fe.printStackTrace();
				return;
			}
		}
		else{
			favorites = getFavoriteHome().create();
		}
		
		IWTimestamp stamp = new IWTimestamp();
		
		favorites.setName(name);
		favorites.setURL(URL);
		favorites.setOwner(user);
		favorites.setTimestamp(stamp.getTimestamp());
		favorites.setAsQuickLink(quickLink);
		favorites.store();
	}
	
	public void deleteFavorite(Favorite favorite) throws RemoveException {
	    favorite.setDeleted(true);
	    favorite.store();
	}
	
	public boolean hasURLAsFavorite(User user, String URL) throws FinderException {
		boolean hasUrl = false;
		Favorite favorite = null;
		
		favorite = getFavoriteHome().findByUserAndURL(user, URL);
		
		if(favorite != null)
			hasUrl = true;
		return hasUrl;
	}
}