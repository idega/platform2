package com.idega.block.forum.data;


public interface ForumAttributesHome extends com.idega.data.IDOHome
{
 public ForumAttributes create() throws javax.ejb.CreateException;
 public ForumAttributes createLegacy();
 public ForumAttributes findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ForumAttributes findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ForumAttributes findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}