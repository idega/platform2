package com.idega.block.calendar.data;


public interface CalendarEntryType extends com.idega.data.IDOLegacyEntity
{
 public void delete()throws java.sql.SQLException;
 public java.lang.String getIDColumnName();
 public int getImageID();
 public void setImageID(int p0);
}
