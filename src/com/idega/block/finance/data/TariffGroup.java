package com.idega.block.finance.data;


public interface TariffGroup extends com.idega.block.category.data.CategoryEntity
{
 public java.sql.Date getGroupDate();
 public int getHandlerId();
 public java.lang.String getInfo();
 public java.lang.String getName();
 public boolean getUseIndex();
 public void setGroupDate(java.sql.Date p0);
 public void setHandlerId(int p0);
 public void setInfo(java.lang.String p0);
 public void setName(java.lang.String p0);
 public void setUseIndex(boolean p0);
}
