package com.idega.block.cal.data;


public interface CalendarEntryGroupHome extends com.idega.data.IDOHome
{
 public CalendarEntryGroup create() throws javax.ejb.CreateException;
 public CalendarEntryGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}