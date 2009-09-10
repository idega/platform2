package com.idega.block.cal.data;

import java.sql.Timestamp;
import java.util.Collection;


public class AttendanceEntityHomeImpl extends com.idega.data.IDOFactory implements AttendanceEntityHome
{
 protected Class getEntityInterfaceClass(){
  return AttendanceEntity.class;
 }


 public AttendanceEntity create() throws javax.ejb.CreateException{
  return (AttendanceEntity) super.createIDO();
 }


 public AttendanceEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (AttendanceEntity) super.findByPrimaryKeyIDO(pk);
 }
 
 public AttendanceEntity findAttendanceByUserIDandTimestamp(int userID, Timestamp stamp) throws javax.ejb.FinderException{
 	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
 	Object id = ((AttendanceEntityBMPBean)entity).ejbFindAttendanceByUserIDandTimestamp(userID,stamp);
 	this.idoCheckInPooledEntity(entity);
 	return (AttendanceEntity) super.findByPrimaryKeyIDO(id);//getEntityCollectionForPrimaryKeys(ids); 	
 }
 public AttendanceEntity findAttendanceByUserIDandEntryID(int userID, int entryID) throws javax.ejb.FinderException{
 	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
 	Object id = ((AttendanceEntityBMPBean)entity).ejbFindAttendanceByUserIDandEntryID(userID,entryID);
 	this.idoCheckInPooledEntity(entity);
 	return (AttendanceEntity) super.findByPrimaryKeyIDO(id);//getEntityCollectionForPrimaryKeys(ids); 	
 	
 }
 public Collection findAttendancesByLedgerID(int ledgerID) throws javax.ejb.FinderException {
 	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
 	java.util.Collection ids = ((AttendanceEntityBMPBean)entity).ejbFindAttendancesByLedgerID(ledgerID);
 	this.idoCheckInPooledEntity(entity);
 	return this.getEntityCollectionForPrimaryKeys(ids);
 	
 }
 public Collection findAttendanceByMark(int userID, int ledgerID, String mark) throws javax.ejb.FinderException{
 	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
 	java.util.Collection ids = ((AttendanceEntityBMPBean)entity).ejbFindAttendanceByMark(userID,ledgerID,mark);
 	this.idoCheckInPooledEntity(entity);
 	return this.getEntityCollectionForPrimaryKeys(ids);
 }



}