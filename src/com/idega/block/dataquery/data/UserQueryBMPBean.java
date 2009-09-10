package com.idega.block.dataquery.data;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.core.file.data.ICFile;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jan 30, 2004
 */
public class UserQueryBMPBean extends GenericEntity implements UserQuery {
	
	private static final String ENTITY_NAME = "USER_QUERY";
	private static final String COLUMN_NAME_NAME = "NAME";
	private static final String COLUMN_NAME_OWNERSHIP = "OWNERSHIP";
	private static final String COLUMN_NAME_PERMISSION = "PERMISSION";
	private static final String COLUMN_NAME_SOURCE = "SOURCE";
	private static final String COLUMN_NAME_ROOT = "ROOT";
	private static final String COLUMN_NAME_DELETED = "DELETED";
	private static final String COLUMN_NAME_DELETED_BY = "DELETED_BY";
	private static final String COLUMN_NAME_DELETED_WHEN= "DELETED_WHEN";

	

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME_NAME, "Name", true, true, String.class);
		addAttribute(COLUMN_NAME_OWNERSHIP, "Ownership", true, true, Integer.class, "one-to-one", Group.class);
		addAttribute(COLUMN_NAME_PERMISSION, "Permission", true, true, String.class, 10);
		addAttribute(COLUMN_NAME_SOURCE, "Query source", true, true, Integer.class, "one-to-one", ICFile.class);
		addAttribute(COLUMN_NAME_ROOT, "Root", true, true, Integer.class, "one-to-one", QuerySequence.class);
    addAttribute(COLUMN_NAME_DELETED,"Deleted",true,true,Boolean.class);
    addAttribute(COLUMN_NAME_DELETED_BY, "Deleted by", true, true, Integer.class, "many-to-one", User.class);
    addAttribute(COLUMN_NAME_DELETED_WHEN, "Deleted when", true, true, Timestamp.class);

	}

	public void setName(String name) {
		setColumn(COLUMN_NAME_NAME, name);
	}
	
	public String getName() {
		return getStringColumnValue(COLUMN_NAME_NAME);
	}
	
	public void setOwnership(Group ownership) {
		setColumn(COLUMN_NAME_OWNERSHIP, ownership);
	}
	
	public Group getOwnership() {
		return (Group) getColumnValue(COLUMN_NAME_OWNERSHIP);
	}
	
	public void setPermission(String permission) {
		setColumn(COLUMN_NAME_PERMISSION, permission);
	}
	
	public String getPermisson() {
		return getStringColumnValue(COLUMN_NAME_PERMISSION);
	}
	
	public void setSourceID(int sourceID) {
		setColumn(COLUMN_NAME_SOURCE, sourceID);
	}
	
	public int getSourceID() {
		return getIntColumnValue(COLUMN_NAME_SOURCE);
	}
	
	public void setSource(ICFile source) {
		setColumn(COLUMN_NAME_SOURCE, source);
	}
	
	public ICFile getSource() {
		return (ICFile) getColumnValue(COLUMN_NAME_SOURCE);
	}

	public void setRootID(int rootID) {
		setColumn(COLUMN_NAME_ROOT, rootID);
	}
	
	public int getRootID() {
		return getIntColumnValue(COLUMN_NAME_ROOT);
	}
	
	public void setRoot(QuerySequence root) {
		setColumn(COLUMN_NAME_ROOT, root);
	}
	
	public QuerySequence getRoot() {
		return (QuerySequence) getColumnValue(COLUMN_NAME_ROOT);
	}
	
  public void setDeleted(boolean isDeleted) {
    setColumn(COLUMN_NAME_DELETED, isDeleted);
  }
  
  public void setDeletedBy(User user)  {
    setColumn(COLUMN_NAME_DELETED_BY, user);
  }
  
  public void setDeletedWhen(Timestamp timestamp) {
    setColumn(COLUMN_NAME_DELETED_WHEN, timestamp);
  }
	
  public boolean getDeleted() {
    return getBooleanColumnValue(COLUMN_NAME_DELETED);
  }
  
  public User getDeletedBy() {
    return (User) getColumnValue(COLUMN_NAME_DELETED_BY);
  }
  
  public Timestamp getDeletedWhen() {
    return ((Timestamp) getColumnValue(COLUMN_NAME_DELETED_WHEN));
  }

  public Collection ejbFindByGroup(Group owner) throws FinderException {
	IDOQuery sql = idoQuery();
	sql.appendSelectAllFrom(this.getEntityName());
	sql.appendWhereEquals(COLUMN_NAME_OWNERSHIP, owner);
	sql.appendAnd().appendLeftParenthesis();
	sql.appendEqualsQuoted(COLUMN_NAME_DELETED, GenericEntity.COLUMN_VALUE_FALSE);
	sql.appendOrIsNull(COLUMN_NAME_DELETED);
	sql.appendRightParenthesis();
	return this.idoFindPKsByQuery(sql);
  }

  
  public Collection ejbFindByGroupAndPermission(Group owner, String permission) throws FinderException {
	IDOQuery sql = idoQuery();
	sql.appendSelectAllFrom(this.getEntityName());
	sql.appendWhereEquals(COLUMN_NAME_OWNERSHIP, owner);
	sql.appendAndEqualsQuoted(COLUMN_NAME_PERMISSION, permission);
	sql.appendAnd().appendLeftParenthesis();
	sql.appendEqualsQuoted(COLUMN_NAME_DELETED, GenericEntity.COLUMN_VALUE_FALSE);
	sql.appendOrIsNull(COLUMN_NAME_DELETED);
	sql.appendRightParenthesis();
	return this.idoFindPKsByQuery(sql);
  }
	
  /** Get all queries but not the deleted ones */
  public Collection ejbFindAll() throws FinderException {
	IDOQuery sql = idoQuery();
	sql.appendSelectAllFrom(this.getEntityName());
	sql.appendWhereEqualsQuoted(COLUMN_NAME_DELETED, GenericEntity.COLUMN_VALUE_FALSE);
	sql.appendOrIsNull(COLUMN_NAME_DELETED);
	return this.idoFindPKsByQuery(sql);
  }
  
}