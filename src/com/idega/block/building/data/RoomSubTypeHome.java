package com.idega.block.building.data;


public interface RoomSubTypeHome extends com.idega.data.IDOHome
{
 public RoomSubType create() throws javax.ejb.CreateException;
 public RoomSubType createLegacy();
 public RoomSubType findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public RoomSubType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public RoomSubType findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}