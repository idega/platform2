package com.idega.block.cal.data;


public interface CalendarEntryTypeHome extends com.idega.data.IDOHome
{
 public CalendarEntryType create() throws javax.ejb.CreateException;
 public CalendarEntryType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findTypeById(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findTypeByName(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findTypes()throws javax.ejb.FinderException;

}