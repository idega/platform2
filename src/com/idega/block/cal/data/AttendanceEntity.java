package com.idega.block.cal.data;

import java.sql.Timestamp;


public interface AttendanceEntity extends com.idega.data.IDOEntity{

	public int getAttendanceID();
	public int getLedgerID();
	public int getUserID();
	public Timestamp getAttendanceDate();
	public String getAttendanceMark();
	public void setAttendanceID(int id);
	public void setLedgerID(int id);
	public void setUserID(int id);
	public void setAttendanceDate(Timestamp stamp);
	public void setAttendanceMark(String mark);
	
}
