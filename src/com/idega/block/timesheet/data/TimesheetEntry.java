package com.idega.block.timesheet.data;


public interface TimesheetEntry extends com.idega.data.IDOLegacyEntity
{
 public java.sql.Timestamp getDate();
 public java.lang.String getDescription();
 public double getHowMany();
 public java.lang.String getIDColumnName();
 public com.idega.block.timesheet.data.TimesheetProject getProject();
 public int getProjectId();
 public com.idega.block.timesheet.data.Resource getResource();
 public int getResourceId();
 public com.idega.core.user.data.User getUser();
 public int getUserId();
 public boolean isBooked();
 public boolean isRegistered();
 public void setBooked(boolean p0);
 public void setDate(java.sql.Timestamp p0);
 public void setDescription(java.lang.String p0);
 public void setHowMany(double p0);
 public void setProjectId(int p0);
 public void setRegistered(boolean p0);
 public void setResourceId(int p0);
 public void setUserId(int p0);
}
