package com.idega.block.reports.data;


public interface Report extends com.idega.block.category.data.CategoryEntity
{
 public java.lang.String getColInfo();
 public java.util.List getColumnInfos();
 public java.lang.String getHeader();
 public java.lang.String[] getHeaders();
 public java.lang.String getInfo();
 public java.lang.String getName();
 public java.lang.String getSQL();
 public java.lang.String getType();
 public void setColInfo(java.lang.String p0);
 public void setHeaders(java.lang.String[] p0);
 public void setInfo(java.lang.String p0);
 public void setName(java.lang.String p0);
 public void setSQL(java.lang.String p0);
 public void setType(java.lang.String p0);
 public void storeColumnInfos(java.util.List p0);
}
