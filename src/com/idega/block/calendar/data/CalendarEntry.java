package com.idega.block.calendar.data;


public interface CalendarEntry extends com.idega.block.category.data.CategoryEntity
{
 public void delete()throws java.sql.SQLException;
 public java.sql.Timestamp getDate();
 public java.sql.Timestamp getEndDate();
 public int getEntryTypeID();
 public int getGroupID();
 public java.lang.String getIDColumnName();
 public int getUserID();
 public void setDate(java.sql.Timestamp p0);
 public void setEndDate(java.sql.Timestamp p0);
 public void setEntryTypeID(int p0);
 public void setGroupID(int p0);
 public void setUserID(int p0);
}
