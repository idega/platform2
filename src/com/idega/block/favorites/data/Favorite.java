/*
 * $Id: Favorite.java,v 1.3 2004/11/05 13:26:11 laddi Exp $
 * Created on 3.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.favorites.data;

import java.sql.Timestamp;


import com.idega.data.IDOEntity;
import com.idega.user.data.User;


/**
 * Last modified: 3.11.2004 10:29:38 by laddi
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3 $
 */
public interface Favorite extends IDOEntity {

	/**
	 * @see com.idega.block.favorites.data.FavoriteBMPBean#getOwner
	 */
	public User getOwner();

	/**
	 * @see com.idega.block.favorites.data.FavoriteBMPBean#getName
	 */
	public String getName();

	/**
	 * @see com.idega.block.favorites.data.FavoriteBMPBean#getTimestamp
	 */
	public Timestamp getTimestamp();

	/**
	 * @see com.idega.block.favorites.data.FavoriteBMPBean#getURL
	 */
	public String getURL();

	/**
	 * @see com.idega.block.favorites.data.FavoriteBMPBean#isQuickLink
	 */
	public boolean isQuickLink();

	/**
	 * @see com.idega.block.favorites.data.FavoriteBMPBean#isDeleted
	 */
	public boolean isDeleted();

	/**
	 * @see com.idega.block.favorites.data.FavoriteBMPBean#getFavoriteType
	 */
	public String getFavoriteType();

	/**
	 * @see com.idega.block.favorites.data.FavoriteBMPBean#setOwner
	 */
	public void setOwner(User user_id);

	/**
	 * @see com.idega.block.favorites.data.FavoriteBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see com.idega.block.favorites.data.FavoriteBMPBean#setTimestamp
	 */
	public void setTimestamp(Timestamp created_date);

	/**
	 * @see com.idega.block.favorites.data.FavoriteBMPBean#setURL
	 */
	public void setURL(String URL);

	/**
	 * @see com.idega.block.favorites.data.FavoriteBMPBean#setAsQuickLink
	 */
	public void setAsQuickLink(boolean is_quicklink);

	/**
	 * @see com.idega.block.favorites.data.FavoriteBMPBean#setDeleted
	 */
	public void setDeleted(boolean is_deleted);

	/**
	 * @see com.idega.block.favorites.data.FavoriteBMPBean#setFavoriteType
	 */
	public void setFavoriteType(String type);

}
