package com.idega.block.calendar.data;


public interface CalendarEntryHome extends com.idega.data.IDOHome
{
 public CalendarEntry create() throws javax.ejb.CreateException;
 public CalendarEntry createLegacy();
 public CalendarEntry findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public CalendarEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public CalendarEntry findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}