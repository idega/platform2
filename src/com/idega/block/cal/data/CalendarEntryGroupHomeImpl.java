package com.idega.block.cal.data;

import java.util.Collection;



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
 
 public Collection findEntryGroupsByLedgerID(int ledgerID) throws javax.ejb.FinderException{
 	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
 	java.util.Collection ids = ((CalendarEntryGroupBMPBean)entity).ejbFindEntryGroupByLedgerID(ledgerID);
 	this.idoCheckInPooledEntity(entity);
 	return this.getEntityCollectionForPrimaryKeys(ids);
 	
 	
 }



}