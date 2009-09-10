package com.idega.block.building.data;


public interface RoomTypeHome extends com.idega.data.IDOHome
{
 public RoomType create() throws javax.ejb.CreateException;
 public RoomType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}