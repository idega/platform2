package com.idega.block.cal.data;


public class CalendarLedgerHomeImpl extends com.idega.data.IDOFactory implements CalendarLedgerHome
{
 protected Class getEntityInterfaceClass(){
  return CalendarLedger.class;
 }


 public CalendarLedger create() throws javax.ejb.CreateException{
  return (CalendarLedger) super.createIDO();
 }


 public CalendarLedger findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CalendarLedger) super.findByPrimaryKeyIDO(pk);
 }
 
 public java.util.Collection findLedgers()throws javax.ejb.FinderException{
 	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
 	java.util.Collection ids = ((CalendarLedgerBMPBean)entity).ejbFindLedgers();
 	this.idoCheckInPooledEntity(entity);
 	return this.getEntityCollectionForPrimaryKeys(ids);
 }
 
 public CalendarLedger findLedgerByName(String name) throws javax.ejb.FinderException{
 	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
 	Object id = ((CalendarLedgerBMPBean)entity).ejbFindLedgerByName(name);
 	this.idoCheckInPooledEntity(entity);
 	return (CalendarLedger) super.findByPrimaryKeyIDO(id);//getEntityCollectionForPrimaryKeys(ids);
 	
 }
 


}