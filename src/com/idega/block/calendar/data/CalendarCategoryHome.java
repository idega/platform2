package com.idega.block.calendar.data;


public interface CalendarCategoryHome extends com.idega.data.IDOHome
{
 public CalendarCategory create() throws javax.ejb.CreateException;
 public CalendarCategory createLegacy();
 public CalendarCategory findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public CalendarCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public CalendarCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}