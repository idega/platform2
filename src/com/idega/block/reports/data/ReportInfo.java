package com.idega.block.reports.data;


public interface ReportInfo extends com.idega.block.category.data.CategoryEntity
{
 public int getBorder();
 public int getColumns();
 public java.lang.String getDescription();
 public float getHeight();
 public boolean getLandscape();
 public java.lang.String getName();
 public java.lang.String getPagesize();
 public java.lang.String getType();
 public float getWidth();
 public void setBorder(int p0);
 public void setColumns(int p0);
 public void setHeight(float p0);
 public void setLandscape(boolean p0);
 public void setName(java.lang.String p0);
 public void setPagesize(java.lang.String p0);
 public void setType(java.lang.String p0);
 public void setWidth(float p0);
}
