package com.idega.block.forum.data;


public interface ForumHome extends com.idega.data.IDOHome
{
 public Forum create() throws javax.ejb.CreateException;
 public Forum createLegacy();
 public Forum findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Forum findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Forum findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}