package com.idega.block.finance.data;


public interface FinanceCategory extends com.idega.data.IDOEntity
{
 public java.lang.String getCategoryName();
 public java.sql.Date getDate();
 public java.lang.String getDescription();
 public java.lang.String getName();
 public boolean getValid();
 public void setCategoryName(java.lang.String p0);
 public void setDate(java.sql.Date p0);
 public void setDescription(java.lang.String p0);
 public void setName(java.lang.String p0);
 public void setValid(boolean p0);
}
