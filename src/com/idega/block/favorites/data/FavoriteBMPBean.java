/*
 * Created on Oct 19, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.idega.block.favorites.data;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.OR;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.user.data.User;

/**
 * @author Anna
 */
public class FavoriteBMPBean extends GenericEntity implements Favorite {

	public static final String ENTITY_NAME = "fav_favorites";

	public static final String COLUMN_FAVORITE_ID = "favorite_id";
	public static final String COLUMN_USER_ID = "user_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_CREATED_DATE = "created_date";
	public static final String COLUMN_URL = "URL";
	public static final String COLUMN_IS_QUICKLINK = "is_quicklink";
	public static final String COLUMN_IS_DELETED = "is_deleted";
	public static final String COLUMN_FAVORITE_TYPE = "favorite_type";
	
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(COLUMN_FAVORITE_ID);
		setAsPrimaryKey(COLUMN_FAVORITE_ID, true);
		
		addAttribute(COLUMN_NAME, "Name", String.class);
		addAttribute(COLUMN_CREATED_DATE, "Date", Timestamp.class);
		addAttribute(COLUMN_URL, "URL", String.class);
		addAttribute(COLUMN_IS_QUICKLINK, "Is quicklink", Boolean.class);
		addAttribute(COLUMN_IS_DELETED, "Is deleted", Boolean.class);
		addAttribute(COLUMN_FAVORITE_TYPE,"Favorite type", String.class, 1);
		
		addManyToOneRelationship(COLUMN_USER_ID, User.class);
		
	}
	
///////////////////////////////////////////////////
//  getters      
///////////////////////////////////////////////////

	public User getOwner() {
		return (User) getColumnValue(COLUMN_USER_ID);
	}
	
	public String getName(){
		return getStringColumnValue(COLUMN_NAME);
	}

	
	public Timestamp getTimestamp() {
		return (Timestamp) getColumnValue(COLUMN_CREATED_DATE);
	}

	public String getURL() {
		return getStringColumnValue(COLUMN_URL);
	}
	
	public boolean isQuickLink() {
		return getBooleanColumnValue(COLUMN_IS_QUICKLINK, false);
	}

	public boolean isDeleted() {
		return getBooleanColumnValue(COLUMN_IS_DELETED, false);
	}
	
	public String getFavoriteType() {
	    return getStringColumnValue(COLUMN_FAVORITE_TYPE);
	}
	
	
///////////////////////////////////////////////////
//  setters      
///////////////////////////////////////////////////

	
	public void setOwner(User user_id) {
		setColumn(COLUMN_USER_ID, user_id);
	}
	
	public void setName(String name){
		setColumn(COLUMN_NAME, name);
	}

	public void setTimestamp(Timestamp created_date) {
		setColumn(COLUMN_CREATED_DATE, created_date);
	}

	public void setURL(String URL) {
		setColumn(COLUMN_URL, URL);
	}

	public void setAsQuickLink(boolean is_quicklink) {
		setColumn(COLUMN_IS_QUICKLINK, is_quicklink);
	}

	public void setDeleted(boolean is_deleted) {
		setColumn(COLUMN_IS_DELETED, is_deleted);
	}
	
	public void setFavoriteType(String type) {
	  setColumn(COLUMN_FAVORITE_TYPE, type);
	}
	
	
///////////////////////////////////////////////////
//  finders      
///////////////////////////////////////////////////

	public Integer ejbFindByUserAndURL(User user, String URL) throws FinderException {
		return ejbFindByUserAndURL(user, URL, null);
	}
	
	public Integer ejbFindByUserAndURL(User user, String URL, String favoriteType) throws FinderException {
		Table favorites = new Table(this);
		
		SelectQuery query = new SelectQuery(favorites);
		query.addColumn(favorites, COLUMN_FAVORITE_ID);
		query.addCriteria(new MatchCriteria(favorites, COLUMN_USER_ID, MatchCriteria.EQUALS, user));
		query.addCriteria(new MatchCriteria(favorites, COLUMN_URL, MatchCriteria.EQUALS, URL));
		if (favoriteType != null) {
			query.addCriteria(new MatchCriteria(favorites, COLUMN_FAVORITE_TYPE, MatchCriteria.EQUALS, favoriteType));
		}
		MatchCriteria isNotDeleted = new MatchCriteria(favorites, COLUMN_IS_DELETED, MatchCriteria.EQUALS, false);
		MatchCriteria isNull = new MatchCriteria(new Column(favorites, COLUMN_IS_DELETED), false);
		query.addCriteria(new OR(isNotDeleted, isNull));
		
		return (Integer) idoFindOnePKBySQL(query.toString());
	}
	
	public Integer ejbFindByUserAndName(User user, String name) throws FinderException {
		return ejbFindByUserAndName(user, name, null);
	}
	
	public Integer ejbFindByUserAndName(User user, String name, String favoriteType) throws FinderException {
		Table favorites = new Table(this);
		
		SelectQuery query = new SelectQuery(favorites);
		query.addColumn(favorites, COLUMN_FAVORITE_ID);
		query.addCriteria(new MatchCriteria(favorites, COLUMN_USER_ID, MatchCriteria.EQUALS, user));
		query.addCriteria(new MatchCriteria(favorites, COLUMN_NAME, MatchCriteria.EQUALS, name));
		if (favoriteType != null) {
			query.addCriteria(new MatchCriteria(favorites, COLUMN_FAVORITE_TYPE, MatchCriteria.EQUALS, favoriteType));
		}
		MatchCriteria isNotDeleted = new MatchCriteria(favorites, COLUMN_IS_DELETED, MatchCriteria.EQUALS, false);
		MatchCriteria isNull = new MatchCriteria(new Column(favorites, COLUMN_IS_DELETED), false);
		query.addCriteria(new OR(isNotDeleted, isNull));
		
		return (Integer) idoFindOnePKBySQL(query.toString());
	}
	
	public Collection ejbFindAllByUser(User user) throws FinderException {
		return ejbFindAllByUser(user, null);
	}
	
	public Collection ejbFindAllByUser(User user, String favoriteType) throws FinderException {
		Table favorites = new Table(this);
		
		SelectQuery query = new SelectQuery(favorites);
		query.addColumn(favorites, COLUMN_FAVORITE_ID);
		query.addCriteria(new MatchCriteria(favorites, COLUMN_USER_ID, MatchCriteria.EQUALS, user));
		if (favoriteType != null) {
			query.addCriteria(new MatchCriteria(favorites, COLUMN_FAVORITE_TYPE, MatchCriteria.EQUALS, favoriteType));
		}
		MatchCriteria isNotDeleted = new MatchCriteria(favorites, COLUMN_IS_DELETED, MatchCriteria.EQUALS, false);
		MatchCriteria isNull = new MatchCriteria(new Column(favorites, COLUMN_IS_DELETED), false);
		query.addCriteria(new OR(isNotDeleted, isNull));
		query.addOrder(favorites, COLUMN_CREATED_DATE, true);

		return idoFindPKsBySQL(query.toString());
	}
	
	public Collection ejbFindAllQuickLinksByUser(User user) throws FinderException {
		Table favorites = new Table(this);
		
		SelectQuery query = new SelectQuery(favorites);
		query.addColumn(favorites, COLUMN_FAVORITE_ID);
		query.addCriteria(new MatchCriteria(favorites, COLUMN_USER_ID, MatchCriteria.EQUALS, user));
		query.addCriteria(new MatchCriteria(favorites, COLUMN_IS_QUICKLINK, MatchCriteria.EQUALS, true));
		MatchCriteria isNotDeleted = new MatchCriteria(favorites, COLUMN_IS_DELETED, MatchCriteria.EQUALS, false);
		MatchCriteria isNull = new MatchCriteria(new Column(favorites, COLUMN_IS_DELETED), false);
		query.addCriteria(new OR(isNotDeleted, isNull));
		query.addOrder(favorites, COLUMN_CREATED_DATE, true);
		
		return idoFindPKsBySQL(query.toString());
	}
}