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
import com.idega.presentation.IWContext;

public interface CalBusiness extends com.idega.business.IBOService {
	public java.util.List getAllLedgers();
	public CalendarLedger getLedger(int ledgerID);
	public int getLedgerIDByName(String name); 
//	public CalendarLedger getLedgerByUserID(int userID);
	public com.idega.block.cal.data.CalendarEntryType getEntryTypeByName(String entryTypeName); 
	public CalendarEntry getEntry(int p0);
	public void deleteEntry(int entryID);
	public void deleteLedger(int ledgerID);
	public void deleteUserFromLedger(int userID, int ledgerID, IWContext iwc);
	public AttendanceEntity getAttendanceByUserIDandEntry(int userID, CalendarEntry entry);
	public void updateAttendance(int attendanceID, int userID, int ledgerID, CalendarEntry entry, String mark);
	public java.util.List getAllEntryTypes();
	public boolean createNewEntryType(String p0);
	public void createNewEntry(String headline, String type, String repeat, String startDate, String startHour, String starMinute, String endDate, String enHour, String endMinute, String attendees,String ledger, String description, String location);		
	public void updateEntry(String entryID, String headline, String type, String repeat, String startDate, String startHour, String startMinute, String endDate, String endHour, String endMinute, String attendees, String ledger, String description, String location);
	public void createNewLedger(String name, int groupID, String coachName, String date,int coachGroupID);
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
	public Collection getPracticesByLedgerID(int ledgerID);
	public Collection getPracticesByLedIDandMonth(int ledgerID, int month, int year);
	public java.util.List getAllMarks();
}
