package com.idega.block.building.data;


public interface RoomHome extends com.idega.data.IDOHome
{
 public Room create() throws javax.ejb.CreateException;
 public Room createLegacy();
 public Room findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Room findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Room findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}