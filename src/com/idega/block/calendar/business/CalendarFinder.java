package com.idega.block.calendar.business;

/**
 * Title: CalendarFinder
 * Description: Finder class for Calendar Block
 * Copyright:    Copyright (c) 2001
 * Company: idega
 * @author Laddi
 * @version 1.0
 */

import java.sql.SQLException;
import java.util.List;
import com.idega.block.calendar.data.*;
import com.idega.data.EntityFinder;
import com.idega.util.idegaTimestamp;
import com.idega.block.text.business.TextFinder;
import com.idega.block.text.data.LocalizedText;

public class CalendarFinder {

  public static CalendarEntry[] getEntries(idegaTimestamp stamp) {
    try {
      CalendarEntry[] cal = (CalendarEntry[]) CalendarEntry.getStaticInstance().findAllByColumnOrdered(CalendarEntry.getColumnNameEntryDate(),stamp.toSQLDateString()+" "+idegaTimestamp.FIRST_SECOND_OF_DAY,CalendarEntry.getColumnNameEntryTypeID(),"=");
      if ( cal.length > 0 )
        return cal;
      return null;
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
      return null;
    }
  }

  public static CalendarEntry getEntry(int entryID) {
    try {
      return new CalendarEntry(entryID);
    }
    catch (SQLException e) {
      return null;
    }
  }

  public static CalendarEntry[] getWeekEntries(idegaTimestamp _stamp, int daysAhead, int daysBack) {
    try {
      idegaTimestamp stampPlus = new idegaTimestamp(_stamp.getTimestamp());
        stampPlus.addDays(daysAhead);

      idegaTimestamp stamp = new idegaTimestamp(_stamp.getTimestamp());
        stamp.addDays(-daysBack);

      CalendarEntry[] cal = (CalendarEntry[]) CalendarEntry.getStaticInstance().findAllByColumnOrdered(CalendarEntry.getColumnNameEntryDate(),stampPlus.toSQLDateString()+" "+idegaTimestamp.LAST_SECOND_OF_DAY,CalendarEntry.getColumnNameEntryDate(),stamp.toSQLDateString()+" "+idegaTimestamp.FIRST_SECOND_OF_DAY,CalendarEntry.getColumnNameEntryDate(),"<",">=");
      if ( cal.length > 0 )
        return cal;
      return null;
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
      return null;
    }
  }

  public static List getMonthEntries(idegaTimestamp stamp) {
    try {
      idegaTimestamp stampPlus = new idegaTimestamp(stamp.getTimestamp());
        stampPlus.addMonths(1);
        stampPlus.setDate(1);

      idegaTimestamp stampMinus = new idegaTimestamp(stamp.getTimestamp());
        stampMinus.setDate(1);

      return EntityFinder.findAllByColumnOrdered(CalendarEntry.getStaticInstance(),CalendarEntry.getColumnNameEntryDate(),stampPlus.toSQLDateString()+" "+idegaTimestamp.LAST_SECOND_OF_DAY,CalendarEntry.getColumnNameEntryDate(),stampMinus.toSQLDateString()+" "+idegaTimestamp.FIRST_SECOND_OF_DAY,CalendarEntry.getColumnNameEntryDate(),"<",">=","distinct",CalendarEntry.getColumnNameEntryDate());
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
      return null;
    }
  }

  public static CalendarEntryType getEntryType(int typeID) {
    try {
      return new CalendarEntryType(typeID);
    }
    catch (SQLException e) {
      return null;
    }
  }

  public static String getEntryTypeName(int typeID,int localeID) {
    return getEntryTypeName(getEntryType(typeID),localeID);
  }

  public static String getEntryTypeName(CalendarEntryType type,int localeID) {
    if ( type != null ) {
      LocalizedText loc = TextFinder.getLocalizedText(type,localeID);
      if ( loc != null ) {
        return loc.getHeadline();
      }
      return "";
    }
    return "";
  }

  public static String[] getEntryStrings(CalendarEntry entry,int localeID) {
    String[] returnString = {null,null};
    if ( entry != null ) {
      LocalizedText loc = TextFinder.getLocalizedText(entry,localeID);
      if ( loc != null ) {
        returnString[0] = loc.getHeadline();
        returnString[1] = loc.getBody();
      }
    }
    return returnString;
  }

  public static int getImageID(int typeID) {
    try {
      return new CalendarEntryType(typeID).getImageID();
    }
    catch (SQLException e) {
      return -1;
    }
  }

}