/*
 * $Id: FavoriteHome.java,v 1.3 2004/11/05 13:26:11 laddi Exp $
 * Created on 3.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.favorites.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;
import com.idega.user.data.User;


/**
 * Last modified: 3.11.2004 10:29:39 by laddi
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3 $
 */
public interface FavoriteHome extends IDOHome {

	public Favorite create() throws javax.ejb.CreateException;

	public Favorite findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.favorites.data.FavoriteBMPBean#ejbFindByUserAndURL
	 */
	public Favorite findByUserAndURL(User user, String URL) throws FinderException;

	/**
	 * @see com.idega.block.favorites.data.FavoriteBMPBean#ejbFindByUserAndURL
	 */
	public Favorite findByUserAndURL(User user, String URL, String favoriteType) throws FinderException;

	/**
	 * @see com.idega.block.favorites.data.FavoriteBMPBean#ejbFindByUserAndName
	 */
	public Favorite findByUserAndName(User user, String name) throws FinderException;

	/**
	 * @see com.idega.block.favorites.data.FavoriteBMPBean#ejbFindByUserAndName
	 */
	public Favorite findByUserAndName(User user, String name, String favoriteType) throws FinderException;

	/**
	 * @see com.idega.block.favorites.data.FavoriteBMPBean#ejbFindAllByUser
	 */
	public Collection findAllByUser(User user) throws FinderException;

	/**
	 * @see com.idega.block.favorites.data.FavoriteBMPBean#ejbFindAllByUser
	 */
	public Collection findAllByUser(User user, String favoriteType) throws FinderException;

	/**
	 * @see com.idega.block.favorites.data.FavoriteBMPBean#ejbFindAllQuickLinksByUser
	 */
	public Collection findAllQuickLinksByUser(User user) throws FinderException;

}
