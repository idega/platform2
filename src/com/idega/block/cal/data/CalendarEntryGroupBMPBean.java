/*
 * Created on Apr 21, 2004
 */
package com.idega.block.cal.data;

import java.sql.SQLException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:birna@idega.is">Birna Iris Jonsdottir</a>
 */
public class CalendarEntryGroupBMPBean extends GenericEntity implements CalendarEntryGroup{
	public CalendarEntryGroupBMPBean(){
		super();
	}

	public CalendarEntryGroupBMPBean(int id)throws SQLException{
		super(id);
	}
	
	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute(getColumnNameName(),"CalEntryGroupName", true, true, String.class);
		addManyToManyRelationShip(CalendarEntry.class);
	}

	public static String getEntityTableName() { return "CAL_ENTRY_GROUP"; }
	public static String getColumnNameEntryGroupID() { return "CAL_ENTRY_GROUP_ID"; }
	public static String getColumnNameName() { return "CAL_ENTRY_GROUP_NAME"; }

	public String getEntityName(){
		return getEntityTableName();
	}
	
	public int getEntryGroupID() {
		return getIntColumnValue(getColumnNameEntryGroupID());
	}	
	
	public String getName() {
		return getStringColumnValue(getColumnNameName());
	}
	
	public void setName(String entryGroupName) {
		setColumn(getColumnNameName(),entryGroupName);
	}
	
	public void addEntry(CalendarEntry entry) {
		try {
			idoAddTo(entry);
		} catch(IDOAddRelationshipException e) {
			System.out.println("Could not add entry to entryGroup");
			e.printStackTrace();
		} 
		
	}
	public void removeEntryRelation() {
		try {
			idoRemoveFrom(CalendarEntry.class);
		}catch (Exception e) {
			System.out.println("cannot remove from middle table");
			e.printStackTrace();
		}			
	}
	public void removeOneEntryRelation(CalendarEntry entry) {
		try {
			idoRemoveFrom(entry);
		}catch(Exception e) {
			System.out.println("cannot remove from middle table");
			e.printStackTrace();
		}
	}
	
	
	
}
