/*
 * Created on Mar 16, 2004
 */
package com.idega.block.cal.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:birna@idega.is">Birna Iris Jonsdottir</a>
 */
public class AttendanceMarkBMPBean extends GenericEntity implements com.idega.block.cal.data.AttendanceMark{
	
	public void insertStartData() throws Exception{
		AttendanceMarkHome markHome = (AttendanceMarkHome) getIDOHome(AttendanceMark.class);
		
		final String [] data = { "x", "s", "v", "f", " " };
		final String [] description = { "Mætt(ur)", "Sein(n)", "Veik(ur)", "Fjarverandi", " "};
		for (int i = 0; i < data.length; i++) {
			AttendanceMark mark = markHome.create();
			mark.setMark(data[i]);
			mark.setMarkDescription(description[i]);
			mark.store();
		}
		
	}
	public void initializeAttributes(){
		addAttribute(getColumnNameMarkID());
		addAttribute(getColumnNameMark(),"Mark",true,true,String.class);
		addAttribute(getColumnNameMarkDescription(),"Mark Description",true,true,String.class);
	}
	
	public static String getEntityTableName() { return "CAL_ATTENDANCE_MARK"; }
	public static String getColumnNameMarkID() { return "CAL_MARK_ID"; }
	public static String getColumnNameMark() { return "CAL_MARK"; }
	public static String getColumnNameMarkDescription() { return "CAL_MARK_DESCRIPTION"; }
	
	public String getEntityName() {
		return getEntityTableName();
	}
	//GET
	public String getMark() {
		return getStringColumnValue(getColumnNameMark());
	}
	public String getMarkDescription() {
		return getStringColumnValue(getColumnNameMarkDescription());
	}
	//SET
	public void setMark(String mark) {
		setColumn(getColumnNameMark(),mark);
	}
	public void setMarkDescription(String markDescription) {
		setColumn(getColumnNameMarkDescription(),markDescription);
	}
	public Collection ejbFindMarks() throws FinderException{
		List result = new ArrayList(super.idoFindAllIDsOrderedBySQL(getColumnNameMark()));
		return result;
		
	}

}
