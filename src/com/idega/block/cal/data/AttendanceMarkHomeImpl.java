package com.idega.block.cal.data;

import java.util.Collection;


public class AttendanceMarkHomeImpl extends com.idega.data.IDOFactory implements AttendanceMarkHome
{
 protected Class getEntityInterfaceClass(){
  return AttendanceMark.class;
 }


 public AttendanceMark create() throws javax.ejb.CreateException{
  return (AttendanceMark) super.createIDO();
 }


 public AttendanceMark findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (AttendanceMark) super.findByPrimaryKeyIDO(pk);
 }
 
 public Collection findMarks()throws javax.ejb.FinderException{
 	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
 	java.util.Collection ids = ((AttendanceMarkBMPBean)entity).ejbFindMarks();
 	this.idoCheckInPooledEntity(entity);
 	return this.getEntityCollectionForPrimaryKeys(ids);
 	
 }



}