package com.idega.block.albumcollection.data;


public interface AlbumTypeHome extends com.idega.data.IDOHome
{
 public AlbumType create() throws javax.ejb.CreateException;
 public AlbumType createLegacy();
 public AlbumType findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public AlbumType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public AlbumType findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}