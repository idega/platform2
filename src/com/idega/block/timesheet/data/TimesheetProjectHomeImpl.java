package com.idega.block.timesheet.data;

import com.idega.data.IDOFactory;


public class TimesheetProjectHomeImpl extends IDOFactory implements TimesheetProjectHome
{
 protected Class getEntityInterfaceClass(){
  return TimesheetProject.class;
 }


 public TimesheetProject create() throws javax.ejb.CreateException{
  return (TimesheetProject) super.createIDO();
 }


public java.util.Collection findAllOrderByNumber()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TimesheetProjectBMPBean)entity).ejbFindAllOrderByNumber();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findEntryRelatedByUserWithinPeriod(java.lang.Integer p0,java.sql.Date p1,java.sql.Date p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TimesheetProjectBMPBean)entity).ejbFindEntryRelatedByUserWithinPeriod(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findUserRelated(com.idega.user.data.User p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TimesheetProjectBMPBean)entity).ejbFindUserRelated(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public TimesheetProject findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TimesheetProject) super.findByPrimaryKeyIDO(pk);
 }



}