/*
 * Created on Mar 9, 2004
 */
package com.idega.block.cal.data;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:birna@idega.is">Birna Iris Jonsdottir</a>
 */
public class AttendanceEntityBMPBean extends GenericEntity implements com.idega.block.cal.data.AttendanceEntity{

	public void initializeAttributes(){
		addAttribute(getColumnNameAttendanceID());
		addAttribute(getColumnNameLedgerID(),"LedgerID",true,true,Integer.class);
		addAttribute(getColumnNameUserID(), "UserID",true,true,Integer.class);
		addAttribute(getColumnNameAttendanceDate(),"Attendance date",true,true,Timestamp.class);
		addAttribute(getColumnNameAttendanceMark(),"Attendance mark",true,true,String.class);
		addAttribute(getColumnNameEntryID(),"EntryID",true,true,Integer.class);
	}
	
	public static String getEntityTableName() { return "CAL_ATTENDANCE"; }
	public static String getColumnNameAttendanceID() { return "CAL_ATTENDANCE_ID"; }
	public static String getColumnNameLedgerID() { return com.idega.block.cal.data.CalendarLedgerBMPBean.getColumnNameLedgerID(); }
	public static String getColumnNameUserID() { return com.idega.user.data.UserBMPBean.getColumnNameUserID(); }
	public static String getColumnNameAttendanceDate() { return "CAL_ATTENDANCE_DATE"; }
	public static String getColumnNameAttendanceMark() { return "CAL_ATTENDANCE_MARK"; }
	public static String getColumnNameEntryID() { return com.idega.block.cal.data.CalendarEntryBMPBean.getColumnNameEntryID(); }
	
	public String getEntityName() {
		return getEntityTableName();
	}
	
	//GET
	public int getAttendanceID() {
		return getIntColumnValue(getColumnNameAttendanceID());
	}
	public int getEntryID() {
		return getIntColumnValue(getColumnNameEntryID());
	}
	public int getLedgerID() {
		return getIntColumnValue(getColumnNameLedgerID());
	}
	public int getUserID() {
		return getIntColumnValue(getColumnNameUserID());
	}
	public Timestamp getAttendanceDate() {
		return (Timestamp) getColumnValue(getColumnNameAttendanceDate());
	}
	public String getAttendanceMark() {
		return getStringColumnValue(getColumnNameAttendanceMark());
	}
	//SET
	public void setAttendanceID(int id) {
		setColumn(getColumnNameAttendanceID(),id);
	}
	public void setEntryID(int entryID) {
		setColumn(getColumnNameEntryID(),entryID);
	}
	public void setLedgerID(int id) {
		setColumn(getColumnNameLedgerID(),id);
	}
	public void setUserID(int id) {
		setColumn(getColumnNameUserID(),id);
	}
	public void setAttendanceDate(Timestamp stamp) {
		setColumn(getColumnNameAttendanceDate(),stamp);
	}
	public void setAttendanceMark(String mark) {
		setColumn(getColumnNameAttendanceMark(),mark);
	}
	public Object ejbFindAttendanceByUserIDandTimestamp(int userID,Timestamp stamp) throws FinderException{
		Integer id = new Integer(userID);
		IDOQuery query = idoQueryGetSelect();
		query.appendWhereEqualsQuoted(getColumnNameUserID(), id.toString());
		query.appendAnd();
		query.append(getColumnNameAttendanceDate());
		query.appendEqualSign();
		query.append(stamp);
		return super.idoFindOnePKByQuery(query);		
	}
	public Object ejbFindAttendanceByUserIDandEntryID(int userID,int entryID) throws FinderException{
		Integer uid = new Integer(userID);
		Integer eid = new Integer(entryID);
		IDOQuery query = idoQueryGetSelect();
		query.appendWhereEqualsQuoted(getColumnNameUserID(),uid.toString());
		query.appendAnd();
		query.append(getColumnNameEntryID());
		query.appendEqualSign();
		query.append(eid.toString());
		return super.idoFindOnePKByQuery(query);
	}
	public Collection ejbFindAttendancesByLedgerID(int ledgerID) throws FinderException {
		IDOQuery query = idoQueryGetSelect();
		query.appendWhereEquals( getColumnNameLedgerID(),ledgerID);
		return super.idoFindPKsByQuery(query);
	}
	public Collection ejbFindAttendanceByMark(int userID, int ledgerID, String mark) throws FinderException{
		IDOQuery query = idoQueryGetSelect();
		query.appendWhereEquals( getColumnNameLedgerID(),ledgerID);
		query.appendAndEquals(getColumnNameUserID(),userID);
		query.appendAndEqualsQuoted(getColumnNameAttendanceMark(),mark);
		return super.idoFindPKsByQuery(query);
	}

}
