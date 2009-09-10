package com.idega.block.dataquery.data;


public interface QueryLog extends com.idega.data.IDOEntity
{
 public java.lang.String getStatement();
 public java.lang.String getTransactionID();
 public void setStatement(java.lang.String p0);
 public void setTransactionID(java.lang.String p0);
}
