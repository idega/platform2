package com.idega.block.finance.data;


public interface AccountPhoneEntry extends com.idega.data.IDOEntity,com.idega.block.finance.data.Entry
{
 public int getAccountEntryId();
 public int getAccountId();
 public int getCashierId();
 public int getDayDuration();
 public int getDuration();
 public java.lang.String getFieldNameAccountId();
 public java.lang.String getFieldNameLastUpdated();
 public java.lang.String getFieldNameStatus();
 public java.sql.Timestamp getLastUpdated();
 public java.lang.String getMainNumber();
 public int getNightDuration();
 public java.lang.String getPhonedNumber();
 public java.sql.Timestamp getPhonedStamp();
 public float getPrice();
 public int getRoundId();
 public java.lang.String getStatus();
 public java.lang.String getSubNumber();
 public java.lang.String getTableName();
 public java.lang.String getType();
 public void setAccountEntryId(java.lang.Integer p0);
 public void setAccountEntryId(int p0);
 public void setAccountId(java.lang.Integer p0);
 public void setAccountId(int p0);
 public void setCashierId(java.lang.Integer p0);
 public void setCashierId(int p0);
 public void setDayDuration(int p0);
 public void setDayDuration(java.lang.Integer p0);
 public void setDuration(int p0);
 public void setDuration(java.lang.Integer p0);
 public void setLastUpdated(java.sql.Timestamp p0);
 public void setMainNumber(java.lang.String p0);
 public void setNightDuration(int p0);
 public void setNightDuration(java.lang.Integer p0);
 public void setPhoneNumber(java.lang.String p0);
 public void setPhonedStamp(java.sql.Timestamp p0);
 public void setPrice(float p0);
 public void setPrice(java.lang.Float p0);
 public void setRoundId(java.lang.Integer p0);
 public void setRoundId(int p0);
 public void setStatus(java.lang.String p0)throws java.lang.IllegalStateException;
 public void setSubNumber(java.lang.String p0);
}
