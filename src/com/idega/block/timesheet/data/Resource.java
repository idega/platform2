package com.idega.block.timesheet.data;


public interface Resource extends com.idega.data.IDOLegacyEntity
{
 public int getDivisionId();
 public java.lang.String getIDColumnName();
 public java.lang.String getName();
 public com.idega.block.projectmanager.data.Project getProject();
 public int getProjectId();
 public java.lang.String getResourceName();
 public java.lang.String getShortName();
 public java.lang.String getResourceType();
 public java.lang.String getUnitName();
 public boolean isClosed();
 public void setClosed(boolean p0);
 public void setDivisionId(int p0);
 public void setProjectId(int p0);
 public void setResourceName(java.lang.String p0);
 public void setShortName(java.lang.String p0);
 public void setResourceType(java.lang.String p0);
 public void setUnitName(java.lang.String p0);
}
