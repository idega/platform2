package com.idega.block.building.data;


public interface RoomTypeHome extends com.idega.data.IDOHome
{
 public RoomType create() throws javax.ejb.CreateException;
 public RoomType createLegacy();
 public RoomType findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public RoomType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public RoomType findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}