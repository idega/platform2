package com.idega.block.building.data;


public interface Building extends com.idega.block.text.data.TextEntity
{
 public com.idega.block.building.data.Complex getComplex();
 public int getComplexId();
 public java.util.Collection getFloors();
 public int getImageId();
 public java.lang.String getInfo();
 public java.lang.String getName();
 public java.lang.String getSerie();
 public java.lang.String getStreet();
 public java.lang.String getStreetNumber();
 public void setComplexId(int p0);
 public void setImageId(int p0);
 public void setImageId(java.lang.Integer p0);
 public void setInfo(java.lang.String p0);
 public void setName(java.lang.String p0);
 public void setSerie(java.lang.String p0);
 public void setStreet(java.lang.String p0);
 public void setStreetNumber(java.lang.String p0);
}
