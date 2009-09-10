package com.idega.block.cal.data;

import java.sql.Timestamp;
import java.util.Collection;


public interface CalendarEntry extends com.idega.data.IDOEntity
{
 public int getEntryID();
 public Timestamp getDate();
 public int getDay();
 public String getDescription();
 public String getLocation();
 public Timestamp getEndDate();
 public String getEntryType();
 public String getEntryTypeName();
 public int getEntryTypeID();
 public int getGroupID();
 public int getLedgerID();
 public String getName();
 public String getRepeat();
 public int getUserID();
 public Collection getUsers();
 public int getEntryGroupID();
 public void setDate(Timestamp p0);
 public void setDescription(String p0);
 public void setLocation(String p0);
 public void setEndDate(Timestamp p0);
 public void setEntryType(String p0);
 public void setEntryTypeID(int p0);
 public void setGroupID(int p0);
 public void setLedgerID(int p0);
 public void setName(String p0);
 public void setRepeat(String p0);
 public void setUserID(int p0);
 public void setEntryGroupID(int p0);
}
