/*
 * Created on Oct 20, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.idega.block.favorites.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;
import com.idega.user.data.User;

/**
 * @author Anna
 */
public interface FavoriteHome extends IDOHome {
	public Favorite create() throws javax.ejb.CreateException;

	public Favorite findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.favorites.data.FavoriteBMPBean#ejbFindByUserAndURL
	 */
	public Favorite findByUserAndURL(User user, String URL)
			throws FinderException;

	/**
	 * @see com.idega.block.favorites.data.FavoriteBMPBean#ejbFindByUserAndName
	 */
	public Favorite findByUserAndName(User user, String name)
			throws FinderException;

	/**
	 * @see com.idega.block.favorites.data.FavoriteBMPBean#ejbFindAllByUser
	 */
	public Collection findAllByUser(User user) throws FinderException;

	/**
	 * @see com.idega.block.favorites.data.FavoriteBMPBean#ejbFindAllQuickLinksByUser
	 */
	public Collection findAllQuickLinksByUser(User user) throws FinderException;

}
