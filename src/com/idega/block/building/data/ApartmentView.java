package com.idega.block.building.data;


public interface ApartmentView extends com.idega.data.IDOEntity
{
 public com.idega.block.building.data.Apartment getApartment();
 public java.lang.Integer getApartmentID();
 public java.lang.String getApartmentName();
 public java.lang.String getApartmentString(java.lang.String p0);
 public com.idega.block.building.data.Building getBuilding();
 public java.lang.Integer getBuildingID();
 public java.lang.String getBuildingName();
 public com.idega.block.building.data.ApartmentCategory getCategory();
 public java.lang.Integer getCategoryID();
 public java.lang.String getCategoryName();
 public com.idega.block.building.data.Complex getComplex();
 public java.lang.Integer getComplexID();
 public java.lang.String getComplexName();
 public java.lang.String getCreationSQL();
 public com.idega.block.building.data.Floor getFloor();
 public java.lang.Integer getFloorID();
 public java.lang.String getFloorName();
 public com.idega.block.building.data.ApartmentType getType();
 public java.lang.Integer getTypeID();
 public java.lang.String getTypeName();
}
