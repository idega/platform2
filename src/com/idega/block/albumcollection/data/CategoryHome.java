package com.idega.block.albumcollection.data;


public interface CategoryHome extends com.idega.data.IDOHome
{
 public Category create() throws javax.ejb.CreateException;
 public Category createLegacy();
 public Category findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Category findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Category findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}