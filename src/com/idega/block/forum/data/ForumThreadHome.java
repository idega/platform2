package com.idega.block.forum.data;


public interface ForumThreadHome extends com.idega.data.IDOHome
{
 public ForumThread create() throws javax.ejb.CreateException;
 public ForumThread createLegacy();
 public ForumThread findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ForumThread findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ForumThread findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}