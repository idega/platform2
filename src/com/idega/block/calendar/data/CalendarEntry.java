//idega 2001 - Laddi

package com.idega.block.calendar.data;

import java.sql.*;
import java.util.Locale;
import com.idega.data.*;
import com.idega.core.user.data.User;
import com.idega.core.data.GenericGroup;
import com.idega.block.text.data.LocalizedText;
import com.idega.block.text.business.TextFinder;

public class CalendarEntry extends GenericEntity{

	public CalendarEntry(){
		super();
	}

	public CalendarEntry(int id)throws SQLException{
		super(id);
	}

  public void insertStartData()throws Exception{
    EntityBulkUpdater bulk = new EntityBulkUpdater();
    CalendarEntry entry = new CalendarEntry();
      entry.setDate(new com.idega.util.idegaTimestamp(1,1,2000).getTimestamp());
      entry.setEntryTypeID(3);

    LocalizedText text = new LocalizedText();
      text.setLocaleId(TextFinder.getLocaleId(new Locale("is","IS")));
      text.setHeadline("idega hf. stofnað");

    LocalizedText text2 = new LocalizedText();
      text2.setLocaleId(TextFinder.getLocaleId(Locale.ENGLISH));
      text2.setHeadline("idega co. founded");

    bulk.add(entry,EntityBulkUpdater.insert);
    bulk.add(text,EntityBulkUpdater.insert);
    bulk.add(text2,EntityBulkUpdater.insert);
    bulk.execute();

    text.addTo(entry);
    text2.addTo(entry);
  }

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
    addAttribute(getColumnNameEntryTypeID(),"Type",true,true,Integer.class,"many-to-one",CalendarEntryType.class);
		addAttribute(getColumnNameEntryDate(),"Date",true,true,Timestamp.class);
    addAttribute(getColumnNameUserID(), "User", true, true, Integer.class);
    addAttribute(getColumnNameGroupID(), "Group", true, true, Integer.class);
    addManyToManyRelationShip(LocalizedText.class,"CA_CALENDAR_LOCALIZED_TEXT");

    setNullable(getColumnNameEntryTypeID(),false);
    setNullable(getColumnNameEntryDate(),false);
	}

	public static String getEntityTableName() { return "CA_CALENDAR"; }

	public static String getColumnNameCalendarID() { return "CA_CALENDAR_ID"; }
	public static String getColumnNameEntryTypeID() { return CalendarEntryType.getColumnNameCalendarTypeID(); }
	public static String getColumnNameEntryDate() { return "ENTRY_DATE"; }
  public static String getColumnNameUserID(){ return User.getColumnNameUserID();}
	public static String getColumnNameGroupID() { return GenericGroup.getColumnNameGroupID(); }

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

  public void setUserID(int userID) {
      setColumn(getColumnNameUserID(),userID);
  }

  public void setGroupID(int groupID) {
      setColumn(getColumnNameGroupID(),groupID);
  }


  //DELETE
	public void delete() throws SQLException{
    removeFrom(LocalizedText.getStaticInstance(LocalizedText.class));
		super.delete();
	}

  public static CalendarEntry getStaticInstance() {
    return (CalendarEntry) CalendarEntry.getStaticInstance(CalendarEntry.class);
  }
}