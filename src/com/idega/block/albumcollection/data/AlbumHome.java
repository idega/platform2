package com.idega.block.albumcollection.data;


public interface AlbumHome extends com.idega.data.IDOHome
{
 public Album create() throws javax.ejb.CreateException;
 public Album createLegacy();
 public Album findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Album findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Album findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}