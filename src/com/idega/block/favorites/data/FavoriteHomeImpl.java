/*
 * $Id: FavoriteHomeImpl.java,v 1.2 2004/11/05 13:26:11 laddi Exp $
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

import com.idega.data.IDOFactory;
import com.idega.user.data.User;


/**
 * Last modified: 3.11.2004 10:29:39 by laddi
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public class FavoriteHomeImpl extends IDOFactory implements FavoriteHome {

	protected Class getEntityInterfaceClass() {
		return Favorite.class;
	}

	public Favorite create() throws javax.ejb.CreateException {
		return (Favorite) super.createIDO();
	}

	public Favorite findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (Favorite) super.findByPrimaryKeyIDO(pk);
	}

	public Favorite findByUserAndURL(User user, String URL) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((FavoriteBMPBean) entity).ejbFindByUserAndURL(user, URL);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Favorite findByUserAndURL(User user, String URL, String favoriteType) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((FavoriteBMPBean) entity).ejbFindByUserAndURL(user, URL, favoriteType);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Favorite findByUserAndName(User user, String name) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((FavoriteBMPBean) entity).ejbFindByUserAndName(user, name);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Favorite findByUserAndName(User user, String name, String favoriteType) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((FavoriteBMPBean) entity).ejbFindByUserAndName(user, name, favoriteType);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findAllByUser(User user) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((FavoriteBMPBean) entity).ejbFindAllByUser(user);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByUser(User user, String favoriteType) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((FavoriteBMPBean) entity).ejbFindAllByUser(user, favoriteType);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllQuickLinksByUser(User user) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((FavoriteBMPBean) entity).ejbFindAllQuickLinksByUser(user);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
