package com.idega.block.finance.data;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public interface FinanceAccount {

  public float getBalance();
  public int getUserId();
  public int getAccountId();
  public String getAccountName();
  public String getAccountType();
  public java.sql.Timestamp getLastUpdated();
}