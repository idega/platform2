package com.idega.block.forum.data;


public interface ForumUserHome extends com.idega.data.IDOHome
{
 public ForumUser create() throws javax.ejb.CreateException;
 public ForumUser createLegacy();
 public ForumUser findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ForumUser findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ForumUser findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}