package com.idega.block.finance.data;


public interface Account extends com.idega.block.category.data.CategoryEntity,com.idega.block.finance.data.FinanceAccount
{
 public void addAmount(java.lang.Float p0);
 public void addAmount(float p0);
 public void addDebet(float p0);
 public void addDebet(java.lang.Float p0);
 public void addKredit(float p0);
 public void addKredit(java.lang.Float p0);
 public Integer getAccountId();
 public java.lang.String getAccountName();
 public java.lang.String getAccountType();
 public int getAccountTypeId();
 public float getBalance();
 public int getCashierId();
 public java.sql.Timestamp getCreationDate();
 public java.lang.String getExtraInfo();
 public java.sql.Timestamp getLastUpdated();
 public java.lang.String getName();
 public java.lang.String getType();
 public int getUserId();
 public boolean getValid();
 public void setAccountTypeId(int p0);
 public void setBalance(java.lang.Float p0);
 public void setBalance(float p0);
 public void setCashierId(java.lang.Integer p0);
 public void setCashierId(int p0);
 public void setCreationDate(java.sql.Timestamp p0);
 public void setExtraInfo(java.lang.String p0);
 public void setLastUpdated(java.sql.Timestamp p0);
 public void setName(java.lang.String p0);
 public void setType(java.lang.String p0);
 public void setTypeFinancial();
 public void setTypePhone();
 public void setUserId(java.lang.Integer p0);
 public void setUserId(int p0);
 public void setValid(boolean p0);
}
