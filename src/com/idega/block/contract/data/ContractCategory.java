package com.idega.block.contract.data;


public interface ContractCategory extends com.idega.data.IDOLegacyEntity
{
 public java.sql.Date getCreationDate();
 public java.lang.String getDescription();
 public java.lang.String getName();
 public java.lang.String getNewsCategoryName();
 public boolean getValid();
 public void setCategoryName(java.lang.String p0);
 public void setCreationDate(java.sql.Date p0);
 public void setDescription(java.lang.String p0);
 public void setName(java.lang.String p0);
 public void setValid(boolean p0);
}
