//idega 2001 - Laddi

package com.idega.block.calendar.data;

import java.sql.*;
import java.util.Locale;
import com.idega.data.*;
import com.idega.core.user.data.User;
import com.idega.core.data.GenericGroup;
import com.idega.block.text.data.LocalizedText;
import com.idega.block.text.business.TextFinder;

public class CalendarEntryType extends GenericEntity{

	public CalendarEntryType(){
		super();
	}

	public CalendarEntryType(int id)throws SQLException{
		super(id);
	}

  public void insertStartData()throws Exception{
    String[] entries = { "Afmæli","Fundir","Viðburður","Frídagur","Skiladagur","Tilkynning","Birthday","Meeting","Event","Holiday","Deadline","Announcement" };

    for ( int a = 0; a < 6; a++ ) {
      EntityBulkUpdater bulk = new EntityBulkUpdater();
      CalendarEntryType type = new CalendarEntryType();

      LocalizedText text = new LocalizedText();
        text.setLocaleId(TextFinder.getLocaleId(new Locale("is","IS")));
        text.setHeadline(entries[a]);

      LocalizedText text2 = new LocalizedText();
        text2.setLocaleId(TextFinder.getLocaleId(Locale.ENGLISH));
        text2.setHeadline(entries[a+6]);

      bulk.add(type,EntityBulkUpdater.insert);
      bulk.add(text,EntityBulkUpdater.insert);
      bulk.add(text2,EntityBulkUpdater.insert);
      bulk.execute();

      text.addTo(type);
      text2.addTo(type);
    }
  }

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
    addAttribute(getColumnNameImageID(), "ICFileID", true, true, Integer.class);
    addManyToManyRelationShip(LocalizedText.class,"CA_CALENDAR_TYPE_LOCALIZED_TEXT");
	}

	public static String getEntityTableName() { return "CA_CALENDAR_TYPE"; }

	public static String getColumnNameCalendarTypeID() { return "CA_CALENDAR_TYPE_ID"; }
	public static String getColumnNameImageID() { return "IC_FILE_ID"; }

  public String getIDColumnName(){
		return getColumnNameCalendarTypeID();
	}

	public String getEntityName(){
		return getEntityTableName();
	}

  public int getImageID() {
    return getIntColumnValue(getColumnNameImageID());
  }

  public void setImageID(int imageID) {
    setColumn(getColumnNameImageID(),imageID);
  }


  //DELETE
	public void delete() throws SQLException{
    removeFrom(LocalizedText.getStaticInstance(LocalizedText.class));
		super.delete();
	}
}