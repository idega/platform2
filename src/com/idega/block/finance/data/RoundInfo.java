package com.idega.block.finance.data;


public interface RoundInfo extends com.idega.data.IDOEntity
{
 public int getAccounts();
 public int getCategoryId();
 public int getGroupId();
 public java.lang.String getName();
 public float getNetto();
 public int getRoundId();
 public java.sql.Timestamp getRoundStamp();
 public java.lang.String getStatus();
 public float getTotals();
}
