package com.idega.block.building.data;

import javax.ejb.*;

public interface Apartment extends com.idega.data.IDOLegacyEntity
{
 public int getApartmentTypeId();
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
 public void setFloorId(java.lang.Integer p0);
 public void setFloorId(int p0);
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
