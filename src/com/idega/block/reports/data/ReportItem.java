package com.idega.block.reports.data;


public interface ReportItem extends com.idega.block.category.data.CategoryEntity
{
 public java.lang.String getConditionData();
 public java.lang.String getConditionOperator();
 public java.lang.String getConditionType();
 public java.lang.String[][] getData();
 public int getDisplayOrder();
 public java.lang.String getEntity();
 public int getEntityId();
 public java.lang.String getField();
 public java.lang.String getInfo();
 public boolean getIsFunction();
 public java.lang.String getJoin();
 public java.lang.String[] getJoinTable();
 public java.lang.String getJoinTables();
 public java.lang.String getMainTable();
 public java.lang.String getName();
 public java.lang.String[] getOps();
 public void setConditionData(java.lang.String p0);
 public void setConditionOperator(java.lang.String p0);
 public void setConditionType(java.lang.String p0);
 public void setData(java.lang.String[][] p0);
 public void setDisplayOrder(int p0);
 public void setEntity(com.idega.data.IDOLegacyEntity p0);
 public void setEntity(java.lang.String p0);
 public void setEntityId(int p0);
 public void setField(java.lang.String p0);
 public void setInfo(java.lang.String p0);
 public void setIsFunction(boolean p0);
 public void setJoin(java.lang.String p0);
 public void setJoinTables(java.lang.String p0);
 public void setMainTable(java.lang.String p0);
 public void setName(java.lang.String p0);
 public void setOps(java.lang.String[] p0);
}
