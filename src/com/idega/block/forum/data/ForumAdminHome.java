package com.idega.block.forum.data;


public interface ForumAdminHome extends com.idega.data.IDOHome
{
 public ForumAdmin create() throws javax.ejb.CreateException;
 public ForumAdmin createLegacy();
 public ForumAdmin findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ForumAdmin findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ForumAdmin findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}