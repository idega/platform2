package com.idega.block.trade.stockroom.data;

import javax.ejb.*;

public interface PriceCategory extends com.idega.data.IDOLegacyEntity
{
 public void delete();
 public java.lang.String getDescription();
 public java.lang.String getExtraInfo();
 public java.lang.String getName();
 public int getParentId();
 public int getSupplierId();
 public java.lang.String getType();
 public boolean isNetbookingCategory();
 public void isNetbookingCategory(boolean p0);
 public void setDefaultValues();
 public void setDescription(java.lang.String p0);
 public void setExtraInfo(java.lang.String p0);
 public void setName(java.lang.String p0);
 public void setParentId(int p0);
 public void setSupplierId(int p0);
 public void setType(java.lang.String p0);
}
