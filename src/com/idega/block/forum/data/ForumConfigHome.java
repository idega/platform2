package com.idega.block.forum.data;


public interface ForumConfigHome extends com.idega.data.IDOHome
{
 public ForumConfig create() throws javax.ejb.CreateException;
 public ForumConfig createLegacy();
 public ForumConfig findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ForumConfig findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ForumConfig findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}