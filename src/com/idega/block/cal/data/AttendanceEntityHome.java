package com.idega.block.cal.data;

import java.sql.Timestamp;
import java.util.Collection;


public interface AttendanceEntityHome extends com.idega.data.IDOHome {
 public AttendanceEntity create() throws javax.ejb.CreateException;
 public AttendanceEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public AttendanceEntity findAttendanceByUserIDandTimestamp(int userID,Timestamp stamp) throws javax.ejb.FinderException;
 public AttendanceEntity findAttendanceByUserIDandEntryID(int userID, int entryID) throws javax.ejb.FinderException;
 public Collection findAttendancesByLedgerID(int ledgerID) throws javax.ejb.FinderException ;
 public Collection findAttendanceByMark(int userID, int ledgerID, String mark) throws javax.ejb.FinderException;

}