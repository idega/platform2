package com.idega.block.trade.stockroom.data;


public interface Stock extends com.idega.data.IDOLegacyEntity
{
 public int getInStock();
 public int getMinimumInStock();
 public int getNotifyLevel();
 public int getStopSaleLevel();
 public java.lang.String getVariantString();
 public void setInStock(int p0);
 public void setMinimumInStock(int p0);
 public void setNotifyLevel(int p0);
 public void setStopSaleLevel(int p0);
 public void setVariantString(java.lang.String p0);
}
