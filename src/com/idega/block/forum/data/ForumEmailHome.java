package com.idega.block.forum.data;


public interface ForumEmailHome extends com.idega.data.IDOHome
{
 public ForumEmail create() throws javax.ejb.CreateException;
 public ForumEmail createLegacy();
 public ForumEmail findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ForumEmail findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ForumEmail findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}