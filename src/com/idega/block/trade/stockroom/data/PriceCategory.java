package com.idega.block.trade.stockroom.data;

import javax.ejb.*;

public interface PriceCategory extends com.idega.data.IDOLegacyEntity
{
 public void setCountAsPerson(boolean p0);
 public int getVisibility();
 public void setExtraInfo(java.lang.String p0);
 public void setName(java.lang.String p0);
 public void initializeAttributes();
 public void isNetbookingCategory(boolean p0);
 public void setDescription(java.lang.String p0);
 public java.lang.String getDescription();
 public void setType(java.lang.String p0);
 public java.lang.String getName();
 public void setVisibility(int p0);
 public int getSupplierId();
 public java.lang.String getExtraInfo();
 public int getParentId();
 public java.lang.String getType();
 public boolean getCountAsPerson();
 public void setSupplierId(int p0);
 public void setParentId(int p0);
 public boolean isNetbookingCategory();
}
