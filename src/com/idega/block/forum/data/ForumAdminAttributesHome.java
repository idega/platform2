package com.idega.block.forum.data;


public interface ForumAdminAttributesHome extends com.idega.data.IDOHome
{
 public ForumAdminAttributes create() throws javax.ejb.CreateException;
 public ForumAdminAttributes createLegacy();
 public ForumAdminAttributes findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ForumAdminAttributes findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ForumAdminAttributes findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}