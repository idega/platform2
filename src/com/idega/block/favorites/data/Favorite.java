/*
 * Created on Oct 20, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.idega.block.favorites.data;

import java.sql.Timestamp;


import com.idega.data.IDOEntity;
import com.idega.user.data.User;

/**
 * @author Anna
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
