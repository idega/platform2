package com.idega.block.cal.data;



public class CalendarEntryHomeImpl extends com.idega.data.IDOFactory implements CalendarEntryHome
{
 protected Class getEntityInterfaceClass(){
  return CalendarEntry.class;
 }


 public CalendarEntry create() throws javax.ejb.CreateException{
  return (CalendarEntry) super.createIDO();
 }


public java.util.Collection findEntryByTimestamp(java.sql.Timestamp p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CalendarEntryBMPBean)entity).ejbFindEntryByTimestamp(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findEntriesBetweenTimestamps(java.sql.Timestamp p0, java.sql.Timestamp p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CalendarEntryBMPBean)entity).ejbFindEntryBetweenTimestamps(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}
public java.util.Collection findEntriesByLedgerID(int p0) throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CalendarEntryBMPBean)entity).ejbFindEntryByLedgerID(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}
public java.util.Collection findEntriesByEntryGroupID(int p0) throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CalendarEntryBMPBean)entity).ejbFindEntryByEntryGroupID(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
	
	
}

 public CalendarEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CalendarEntry) super.findByPrimaryKeyIDO(pk);
 }



}