package com.idega.block.building.data;

import com.idega.block.text.data.TextEntity;


public interface Apartment extends TextEntity
{
 public com.idega.block.building.data.ApartmentType getApartmentType();
 public int getApartmentTypeId();
 public com.idega.block.building.data.Floor getFloor();
 public int getFloorId();
 public int getImageId();
 public java.lang.String getInfo();
 public java.lang.String getName();
 public boolean getRentable();
 public java.lang.String getSerie();
 public java.lang.String getStatus();
 public java.sql.Date getUnavailableUntil();
 public void setApartmentTypeId(int p0);
 public void setApartmentTypeId(java.lang.Integer p0);
 public void setFloorId(int p0);
 public void setFloorId(java.lang.Integer p0);
 public void setImageId(int p0);
 public void setImageId(java.lang.Integer p0);
 public void setInfo(java.lang.String p0);
 public void setName(java.lang.String p0);
 public void setRentable(boolean p0);
 public void setSerie(java.lang.String p0);
 public void setStatus(java.lang.String p0);
 public void setStatusAvailable();
 public void setStatusFrozen();
 public void setStatusRented();
 public void setUnavailableUntil(java.sql.Date p0);
}
