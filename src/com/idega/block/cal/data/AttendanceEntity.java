package com.idega.block.cal.data;

import java.sql.Timestamp;


public interface AttendanceEntity extends com.idega.data.IDOEntity{

	public int getAttendanceID();
	public int getEntryID();
	public int getLedgerID();
	public int getUserID();
	public Timestamp getAttendanceDate();
	public String getAttendanceMark();
	public void setAttendanceID(int id);
	public void setEntryID(int entryID);
	public void setLedgerID(int id);
	public void setUserID(int id);
	public void setAttendanceDate(Timestamp stamp);
	public void setAttendanceMark(String mark);
	
}
