/*
 * Created on Feb 25, 2004
 */
package com.idega.block.cal.data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOQuery;
import com.idega.data.IDORelationshipException;
import com.idega.user.data.User;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:birna@idega.is">Birna Iris Jonsdottir</a>
 */
public class CalendarLedgerBMPBean extends GenericEntity implements com.idega.block.cal.data.CalendarLedger{
	
	public void initializeAttributes(){
		addAttribute(getColumnNameLedgerID());
		addAttribute(getColumnNameLedgerName(),"Ledger name",true,true,String.class);
		addAttribute(getColumnNameAttendanceDate(),"Attendance date",true,true,Timestamp.class,"one-to-many",Timestamp.class);
		addAttribute(getColumnNameGroupID(), "Group", true, true, Integer.class);
		addAttribute(getColumnNameUserID(), "Coach", true, true, Integer.class);
//		addAttribute(getColumnNameUserID(), "User", true, true, Integer.class,"many-to-one",User.class);
		addManyToManyRelationShip(User.class);
	}
	
	public static String getEntityTableName() { return "CAL_LEDGER"; }
	public static String getColumnNameLedgerID() { return "CAL_LEDGER_ID"; }
	public static String getColumnNameLedgerName() { return "CAL_LEDGER_NAME"; }
	public static String getColumnNameAttendanceDate() { return "CAL_LEDGER_ATTENDANCE_DATE"; }
	public static String getColumnNameGroupID() { return com.idega.user.data.GroupBMPBean.getColumnNameGroupID();}
	public static String getColumnNameUserID() { return com.idega.user.data.UserBMPBean.getColumnNameUserID(); }
	
	
	//GET
	public String getEntityName() {
		return getEntityTableName();
	}
	public int getLedgerID() {
		return getIntColumnValue(getColumnNameLedgerID());
	}
	public String getName() {
		return getStringColumnValue(getColumnNameLedgerName());
	}
	public Timestamp getDate() {
		return (Timestamp) getColumnValue(getColumnNameAttendanceDate());
	}
	public int getGroupID() {
		return getIntColumnValue(getColumnNameGroupID());
	}
	public Collection getUsers() {
		System.out.println("Getting users using relationship");
		try {
			return idoGetRelatedEntities(User.class);
		} catch(IDORelationshipException e) {
			System.out.println("Couldn't find users for ledger " + toString());
			e.printStackTrace();
			return Collections.EMPTY_LIST;
		}
	}
	public int getCoachID() {
		return getIntColumnValue(getColumnNameUserID());
	}
	//SET
	public void setName(String name) {
		setColumn(getColumnNameLedgerName(),name);
	}
	public void setDate(Timestamp date) {
		setColumn(getColumnNameAttendanceDate(),date);
		
	}
	public void setGroupID(int groupID) {
		setColumn(getColumnNameGroupID(),groupID);
	}
	//add a user to the middle table
	public void addUser(User user) {
		try {
			idoAddTo(user);
		} catch(IDOAddRelationshipException e) {
			System.out.println("Could not add user to ledger");
			e.printStackTrace();
		} 
	}
	public void setCoachID(int coachID) {
		setColumn(getColumnNameUserID(),coachID);
	}
	public Collection ejbFindLedgers() throws FinderException {
		List result = new ArrayList(super.idoFindAllIDsOrderedBySQL("CAL_LEDGER_NAME"));
		return result;
	}
	
	public Object ejbFindLedgerByName(String name) throws FinderException {
		IDOQuery query = idoQueryGetSelect();
		query.appendWhereEqualsQuoted("CAL_LEDGER_NAME", name);		
		return super.idoFindOnePKByQuery(query);
	}
	
}
