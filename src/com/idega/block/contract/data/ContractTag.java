package com.idega.block.contract.data;


public interface ContractTag extends com.idega.data.IDOLegacyEntity
{
 public java.lang.Integer getCategoryId();
 public boolean getInList();
 public boolean getInUse();
 public java.lang.String getInfo();
 public java.lang.String getName();
 public void setCategoryId(int p0);
 public void setCategoryId(java.lang.Integer p0);
 public void setInList(boolean p0);
 public void setInUse(boolean p0);
 public void setInfo(java.lang.String p0);
 public void setName(java.lang.String p0);
 public Class getType();
 public void setType(Class type);
}
