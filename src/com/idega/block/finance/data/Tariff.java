package com.idega.block.finance.data;

import javax.ejb.*;

public interface Tariff extends com.idega.data.IDOLegacyEntity
{
 public int getAccountKeyId();
 public boolean getInUse();
 public java.lang.String getIndexType();
 public java.sql.Timestamp getIndexUpdated();
 public java.lang.String getInfo();
 public java.lang.String getName();
 public float getPrice();
 public java.lang.String getTariffAttribute();
 public int getTariffGroupId();
 public java.sql.Timestamp getUseFromDate();
 public boolean getUseIndex();
 public java.sql.Timestamp getUseToDate();
 public void setAccountKeyId(int p0);
 public void setAccountKeyId(java.lang.Integer p0);
 public void setInUse(boolean p0);
 public void setIndexType(java.lang.String p0);
 public void setIndexUpdated(java.sql.Timestamp p0);
 public void setInfo(java.lang.String p0);
 public void setName(java.lang.String p0);
 public void setPrice(float p0);
 public void setPrice(java.lang.Float p0);
 public void setTariffAttribute(java.lang.String p0);
 public void setTariffGroupId(int p0);
 public void setTariffGroupId(java.lang.Integer p0);
 public void setUseFromDate(java.sql.Timestamp p0);
 public void setUseIndex(boolean p0);
 public void setUseToDate(java.sql.Timestamp p0);
}
