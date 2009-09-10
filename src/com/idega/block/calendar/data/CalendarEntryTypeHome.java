package com.idega.block.calendar.data;


public interface CalendarEntryTypeHome extends com.idega.data.IDOHome
{
 public CalendarEntryType create() throws javax.ejb.CreateException;
 public CalendarEntryType createLegacy();
 public CalendarEntryType findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public CalendarEntryType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public CalendarEntryType findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}