package com.idega.block.trade.stockroom.data;


public interface PriceCategory extends com.idega.data.IDOLegacyEntity
{
 public boolean getCountAsPerson();
 public java.lang.String getDescription();
 public java.lang.String getExtraInfo();
 public java.lang.String getKey();
 public java.lang.String getName();
 public int getParentId();
 public int getSupplierId();
 public java.lang.String getType();
 public int getVisibility();
 public void initializeAttributes();
 public boolean isNetbookingCategory();
 public void isNetbookingCategory(boolean p0);
 public void setCountAsPerson(boolean p0);
 public void setDescription(java.lang.String p0);
 public void setExtraInfo(java.lang.String p0);
 public void setKey(java.lang.String p0);
 public void setName(java.lang.String p0);
 public void setParentId(int p0);
 public void setSupplierId(int p0);
 public void setType(java.lang.String p0);
 public void setVisibility(int p0);
}
