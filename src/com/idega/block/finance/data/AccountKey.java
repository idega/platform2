package com.idega.block.finance.data;

import com.idega.data.CategoryEntity;


public interface AccountKey extends CategoryEntity,com.idega.block.finance.business.Key
{
 public java.lang.String getInfo();
 public java.lang.String getName();
 public int getTariffKeyId();
 public void setInfo(java.lang.String p0);
 public void setName(java.lang.String p0);
 public void setTariffKeyId(int p0);
}
