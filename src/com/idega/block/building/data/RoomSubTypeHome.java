package com.idega.block.building.data;


public interface RoomSubTypeHome extends com.idega.data.IDOHome
{
 public RoomSubType create() throws javax.ejb.CreateException;
 public RoomSubType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}