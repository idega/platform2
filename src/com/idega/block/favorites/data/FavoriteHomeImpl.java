/*
 * Created on Oct 20, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.idega.block.favorites.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;
import com.idega.user.data.User;

/**
 * @author Anna
 */
public class FavoriteHomeImpl extends IDOFactory implements FavoriteHome {
	protected Class getEntityInterfaceClass() {
		return Favorite.class;
	}

	public Favorite create() throws javax.ejb.CreateException {
		return (Favorite) super.createIDO();
	}

	public Favorite findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (Favorite) super.findByPrimaryKeyIDO(pk);
	}

	public Favorite findByUserAndURL(User user, String URL)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((FavoriteBMPBean) entity).ejbFindByUserAndURL(user, URL);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Favorite findByUserAndName(User user, String name)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((FavoriteBMPBean) entity).ejbFindByUserAndName(user, name);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findAllByUser(User user) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((FavoriteBMPBean) entity)
				.ejbFindAllByUser(user);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllQuickLinksByUser(User user) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((FavoriteBMPBean) entity)
				.ejbFindAllQuickLinksByUser(user);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
