package com.idega.block.finance.data;


public interface AccountKey extends com.idega.block.category.data.CategoryEntity,com.idega.block.finance.business.Key
{
 public java.lang.String getInfo();
 public java.lang.String getName();
 public int getTariffKeyId();
 public void setInfo(java.lang.String p0);
 public void setName(java.lang.String p0);
 public void setTariffKeyId(int p0);
}
