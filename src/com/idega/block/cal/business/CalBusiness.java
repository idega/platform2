package com.idega.block.cal.business;

/**
 * Title: CalendarBusiness
 * Description: Business class for Calendar Block
 * Copyright:    Copyright (c) 2001
 * Company: idega
 * @author Laddi - modified by Birna March 2004
 * @version 1.0
 */
import java.sql.Timestamp;
import java.util.Collection;

import com.idega.block.cal.data.AttendanceEntity;
import com.idega.block.cal.data.CalendarEntry;
import com.idega.block.cal.data.CalendarLedger;

public interface CalBusiness extends com.idega.business.IBOService {
	public java.util.List getAllLedgers();
	public CalendarLedger getLedger(int p0);
	public int getLedgerIDByName(String name); 
	public com.idega.block.cal.data.CalendarEntryType getEntryTypeByName(String entryTypeName); 
	public CalendarEntry getEntry(int p0);
	public void deleteEntry(int entryID);
	public AttendanceEntity getAttendanceByUserIDandEntry(int userID, CalendarEntry entry);
	public void updateAttendance(int attendanceID, int userID, int ledgerID, CalendarEntry entry, String mark);
	public java.util.List getAllEntryTypes();
	public boolean createNewEntryType(String p0);
	public void createNewEntry(String headline, String type, String repeat, String startDate, String startHour, String starMinute, String endDate, String enHour, String endMinute, String attendees,String ledger, String description);		
	public void updateEntry(String entryID, String headline, String type, String repeat, String startDate, String startHour, String startMinute, String endDate, String endHour, String endMinute, String attendees, String description);
	public void createNewLedger(String name, int groupID, String coachName);
	public void createNewMark(String markName, String description);
	public Collection getUsersInLedger(int ledgerID);
	public java.util.List getAttendancesByLedgerID(int ledgerID);
	public int getNumberOfPractices(int ledgerID); 
	public java.util.List getAttendanceMarks(int userID, int ledgerID, String mark);
	public void saveAttendance(int userID, int ledgerID, CalendarEntry entry, String mark);
	public void deleteEntryType(int typeID);
	public boolean deleteBlock(int iObjectInstanceId);
	public Collection getEntriesByTimestamp(Timestamp stamp);
	public Collection getEntriesBetweenTimestamps(Timestamp fromStamp, Timestamp toStamp);
	public Collection getEntriesByLedgerID(int ledgerID);
	public java.util.List getAllMarks();
}
