package com.idega.block.finance.data;


public interface AssessmentRound extends com.idega.data.IDOLegacyEntity
{
 public int getAccountCount();
 public int getCategoryId();
 public java.lang.String getName();
 public java.sql.Timestamp getRoundStamp();
 public java.lang.String getStatus();
 public int getTariffGroupId();
 public float getTotals();
 public java.lang.String getType();
 public void setAccountCount(int p0);
 public void setAsNew(java.lang.String p0);
 public void setAsReceived(java.lang.String p0);
 public void setAsSent(java.lang.String p0);
 public void setCategoryId(int p0);
 public void setName(java.lang.String p0);
 public void setRoundStamp(java.sql.Timestamp p0);
 public void setStatus(java.lang.String p0)throws java.lang.IllegalStateException;
 public void setStatusAssessed();
 public void setStatusReceived();
 public void setStatusSent();
 public void setTariffGroupId(int p0);
 public void setTotals(float p0);
 public void setTotals(java.lang.Float p0);
 public void setType(java.lang.String p0);
}
