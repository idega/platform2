package com.idega.block.cal.data;


public class CalendarEntryTypeHomeImpl extends com.idega.data.IDOFactory implements CalendarEntryTypeHome
{
 protected Class getEntityInterfaceClass(){
  return CalendarEntryType.class;
 }


 public CalendarEntryType create() throws javax.ejb.CreateException{
  return (CalendarEntryType) super.createIDO();
 }


public java.util.Collection findTypeById(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CalendarEntryTypeBMPBean)entity).ejbFindTypeById(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findTypeByName(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CalendarEntryTypeBMPBean)entity).ejbFindTypeByName(p0);
	this.idoCheckInPooledEntity(entity);
	return getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findTypes()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CalendarEntryTypeBMPBean)entity).ejbFindTypes();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public CalendarEntryType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CalendarEntryType) super.findByPrimaryKeyIDO(pk);
 }



}