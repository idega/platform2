package com.idega.block.cal.data;


public interface CalendarEntry extends com.idega.data.IDOEntity
{
 public int getCalendarID();
 public java.sql.Timestamp getDate();
 public int getDay();
 public java.lang.String getDescription();
 public java.lang.String getLocation();
 public java.sql.Timestamp getEndDate();
 public java.lang.String getEntryType();
 public java.lang.String getEntryTypeName();
 public int getEntryTypeID();
 public int getGroupID();
 public int getLedgerID();
 public java.lang.String getName();
 public java.lang.String getRepeat();
 public int getUserID();
 public java.util.Collection getUsers();
// public int getCategoryID();
 public void setDate(java.sql.Timestamp p0);
 public void setDescription(java.lang.String p0);
 public void setLocation(java.lang.String p0);
 public void setEndDate(java.sql.Timestamp p0);
 public void setEntryType(java.lang.String p0);
 public void setEntryTypeID(int p0);
 public void setGroupID(int p0);
 public void setLedgerID(int p0);
 public void setName(java.lang.String p0);
 public void setRepeat(java.lang.String p0);
 public void setUserID(int p0);
// public void setCategoryID(int p0);
}
