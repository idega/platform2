package com.idega.block.trade.data;


public interface CurrencyValues extends com.idega.data.IDOLegacyEntity
{
 public float getBuyValue();
 public java.sql.Timestamp getCurrencyDate();
 public java.lang.String getIDColumnName();
 public float getMiddleValue();
 public float getSellValue();
 public void setBuyValue(float p0);
 public void setCurrencyDate(java.sql.Timestamp p0);
 public void setDefaultValues();
 public void setMiddleValue(float p0);
 public void setSellValue(float p0);
}
