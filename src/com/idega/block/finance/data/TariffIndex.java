package com.idega.block.finance.data;


public interface TariffIndex extends com.idega.block.category.data.CategoryEntity
{
 public java.sql.Timestamp getDate();
 public float getIndex();
 public java.lang.String getInfo();
 public java.lang.String getName();
 public float getNewValue();
 public float getOldValue();
 public java.lang.String getType();
 public void setDate(java.sql.Timestamp p0);
 public void setIndex(float p0);
 public void setIndex(java.lang.Float p0);
 public void setInfo(java.lang.String p0);
 public void setName(java.lang.String p0);
 public void setNewValue(float p0);
 public void setNewValue(java.lang.Float p0);
 public void setOldValue(float p0);
 public void setOldValue(java.lang.Float p0);
 public void setType(java.lang.String p0);
}
