package com.idega.block.timesheet.data;


public class TimesheetEntryHomeImpl extends com.idega.data.IDOFactory implements TimesheetEntryHome
{
 protected Class getEntityInterfaceClass(){
  return TimesheetEntry.class;
 }


 public TimesheetEntry create() throws javax.ejb.CreateException{
  return (TimesheetEntry) super.createIDO();
 }


public java.util.Collection findBookedByUserWithinPeriod(java.lang.Integer p0,java.sql.Date p1,java.sql.Date p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TimesheetEntryBMPBean)entity).ejbFindBookedByUserWithinPeriod(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByDateAndUser(java.sql.Date p0,java.lang.Integer p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TimesheetEntryBMPBean)entity).ejbFindByDateAndUser(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByProjectWithinPeriod(java.lang.Integer p0,java.sql.Date p1,java.sql.Date p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TimesheetEntryBMPBean)entity).ejbFindByProjectWithinPeriod(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByUserAndProjectWithinPeriod(java.lang.Integer p0,java.lang.Integer p1,java.sql.Date p2,java.sql.Date p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TimesheetEntryBMPBean)entity).ejbFindByUserAndProjectWithinPeriod(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByUserWithinPeriod(java.lang.Integer p0,java.sql.Date p1,java.sql.Date p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TimesheetEntryBMPBean)entity).ejbFindByUserWithinPeriod(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findUnbookedByUserBeforeDate(java.lang.Integer p0,java.sql.Date p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TimesheetEntryBMPBean)entity).ejbFindUnbookedByUserBeforeDate(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findUnbookedByUserWithinPeriod(java.lang.Integer p0,java.sql.Date p1,java.sql.Date p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TimesheetEntryBMPBean)entity).ejbFindUnbookedByUserWithinPeriod(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public TimesheetEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TimesheetEntry) super.findByPrimaryKeyIDO(pk);
 }


public int countByUserBeforeDate(java.lang.Integer p0,java.sql.Date p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((TimesheetEntryBMPBean)entity).ejbHomeCountByUserBeforeDate(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}