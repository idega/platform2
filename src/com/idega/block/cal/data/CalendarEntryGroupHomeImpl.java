package com.idega.block.cal.data;


public class CalendarEntryGroupHomeImpl extends com.idega.data.IDOFactory implements CalendarEntryGroupHome
{
 protected Class getEntityInterfaceClass(){
  return CalendarEntryGroup.class;
 }


 public CalendarEntryGroup create() throws javax.ejb.CreateException{
  return (CalendarEntryGroup) super.createIDO();
 }


 public CalendarEntryGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CalendarEntryGroup) super.findByPrimaryKeyIDO(pk);
 }



}