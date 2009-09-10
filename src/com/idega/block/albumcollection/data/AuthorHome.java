package com.idega.block.albumcollection.data;


public interface AuthorHome extends com.idega.data.IDOHome
{
 public Author create() throws javax.ejb.CreateException;
 public Author createLegacy();
 public Author findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Author findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Author findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}