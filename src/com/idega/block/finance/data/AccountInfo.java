package com.idega.block.finance.data;

import com.idega.block.category.data.CategoryEntity;


public interface AccountInfo extends CategoryEntity,FinanceAccount
{
 public Integer getAccountId();
 public java.lang.String getAccountName();
 public java.lang.String getAccountType();
 public float getBalance();
 public int getCashierId();
 public int getCategoryId();
 public java.sql.Timestamp getLastUpdated();
 public java.lang.String getName();
 public int getUserId();
}
