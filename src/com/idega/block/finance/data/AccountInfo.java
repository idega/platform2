package com.idega.block.finance.data;


public interface AccountInfo extends com.idega.data.IDOLegacyEntity,com.idega.block.finance.data.FinanceAccount
{
 public int getAccountId();
 public java.lang.String getAccountName();
 public java.lang.String getAccountType();
 public float getBalance();
 public int getCashierId();
 public int getCategoryId();
 public java.sql.Timestamp getLastUpdated();
 public java.lang.String getName();
 public int getUserId();
}
