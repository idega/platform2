package com.idega.block.trade.stockroom.data;


public interface Supplies extends com.idega.data.IDOLegacyEntity
{
 public float getCurrentSupplies();
 public int getPeriod();
 public int getProductId();
 public float getRecord();
 public java.sql.Timestamp getRecordTime();
 public void setCurrentSupplies(float p0);
 public void setPeriod(int p0);
 public void setProductId(int p0);
 public void setRecord(float p0);
 public void setRecordTime(java.sql.Timestamp p0);
}
