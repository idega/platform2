package com.idega.block.reports.data;


public interface ReportColumnInfo extends com.idega.data.IDOLegacyEntity
{
 public int getColumnNumber();
 public int getColumnSpan();
 public java.lang.String getEndChar();
 public int getFontFamily();
 public int getFontSize();
 public int getFontStyle();
 public int getReportId();
 public boolean getShowName();
 public void setColumnNumber(int p0);
 public void setColumnSpan(int p0);
 public void setEndChar(java.lang.String p0);
 public void setFontFamily(int p0);
 public void setFontSize(int p0);
 public void setFontStyle(int p0);
 public void setReportId(int p0);
 public void setShowName(boolean p0);
}
