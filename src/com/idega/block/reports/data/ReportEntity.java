package com.idega.block.reports.data;


public interface ReportEntity extends com.idega.data.IDOLegacyEntity
{
 public java.lang.String getEntity();
 public java.lang.String getJoin();
 public java.lang.String getJoinTables();
 public java.lang.String getMainTable();
 public void setCategory(java.lang.String p0);
 public void setJoin(java.lang.String p0);
 public void setJoinTables(java.lang.String p0);
 public void setMainTable(java.lang.String p0);
}
