//idega 2001 - Laddi

package com.idega.block.calendar.data;

import java.sql.SQLException;
import java.sql.Timestamp;

import com.idega.block.calendar.business.CalendarBusiness;
import com.idega.block.text.data.LocalizedText;

public class CalendarEntryBMPBean extends com.idega.block.category.data.CategoryEntityBMPBean implements com.idega.block.calendar.data.CalendarEntry {

	public CalendarEntryBMPBean(){
		super();
	}

	public CalendarEntryBMPBean(int id)throws SQLException{
		super(id);
	}

  public void insertStartData()throws Exception{
    CalendarBusiness.initializeCalendarEntry();
  }

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
    addAttribute(getColumnNameEntryTypeID(),"Type",true,true,Integer.class,"many-to-one",CalendarEntryType.class);
		addAttribute(getColumnNameEntryDate(),"Date",true,true,Timestamp.class);
		addAttribute(getColumnNameEntryEndDate(),"End date",true,true,Timestamp.class);
    addAttribute(getColumnNameUserID(), "User", true, true, Integer.class);
    addAttribute(getColumnNameGroupID(), "Group", true, true, Integer.class);
    addManyToManyRelationShip(LocalizedText.class);
    setNullable(getColumnNameEntryTypeID(),false);
    setNullable(getColumnNameEntryDate(),false);
	}

	public static String getEntityTableName() { return "CA_CALENDAR"; }

	public static String getColumnNameCalendarID() { return "CA_CALENDAR_ID"; }
	public static String getColumnNameEntryTypeID() { return com.idega.block.calendar.data.CalendarEntryTypeBMPBean.getColumnNameCalendarTypeID(); }
	public static String getColumnNameEntryDate() { return "ENTRY_DATE"; }
	public static String getColumnNameEntryEndDate() { return "ENTRY_END_DATE"; }
  public static String getColumnNameUserID(){ return com.idega.core.user.data.UserBMPBean.getColumnNameUserID();}
	public static String getColumnNameGroupID() { return com.idega.core.data.GenericGroupBMPBean.getColumnNameGroupID(); }

  public String getIDColumnName(){
		return getColumnNameCalendarID();
	}

	public String getEntityName(){
		return getEntityTableName();
	}

	//GET
  public int getEntryTypeID() {
    return getIntColumnValue(getColumnNameEntryTypeID());
  }

  public Timestamp getDate(){
		return (Timestamp) getColumnValue(getColumnNameEntryDate());
	}

	public Timestamp getEndDate(){
		return (Timestamp) getColumnValue(getColumnNameEntryEndDate());
	}

  public int getUserID() {
    return getIntColumnValue(getColumnNameUserID());
  }

  public int getGroupID() {
    return getIntColumnValue(getColumnNameGroupID());
  }


  //SET
  public void setEntryTypeID(int entryTypeID) {
      setColumn(getColumnNameEntryTypeID(),entryTypeID);
  }

	public void setDate(Timestamp date){
			setColumn(getColumnNameEntryDate(), date);
	}

	public void setEndDate(Timestamp date){
			setColumn(getColumnNameEntryEndDate(), date);
	}

  public void setUserID(int userID) {
      setColumn(getColumnNameUserID(),userID);
  }

  public void setGroupID(int groupID) {
      setColumn(getColumnNameGroupID(),groupID);
  }


  //DELETE
	public void delete() throws SQLException{
    removeFrom(com.idega.block.text.data.LocalizedTextBMPBean.getStaticInstance(LocalizedText.class));
		super.delete();
	}

  public static CalendarEntry getStaticInstance() {
    return (CalendarEntry) com.idega.block.calendar.data.CalendarEntryBMPBean.getStaticInstance(CalendarEntry.class);
  }
}
