package com.idega.block.building.data;


public interface Room extends com.idega.data.IDOEntity
{
 public int getFloorId();
 public int getImageId();
 public java.lang.String getInfo();
 public java.lang.String getName();
 public boolean getRentable();
 public int getRoomSubTypeId();
 public void setFloorId(int p0);
 public void setFloorId(java.lang.Integer p0);
 public void setImageId(int p0);
 public void setImageId(java.lang.Integer p0);
 public void setInfo(java.lang.String p0);
 public void setName(java.lang.String p0);
 public void setRentable(boolean p0);
 public void setRoomSubTypeId(int p0);
 public void setRoomSubTypeId(java.lang.Integer p0);
}
