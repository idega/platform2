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
      CalendarEntry[] cal = (CalendarEntry[]) CalendarEntry.getStaticInstance().findAllByColumnOrdered(CalendarEntry.getColumnNameEntryDate(),stamp.toSQLString(),CalendarEntry.getColumnNameEntryTypeID(),"=");
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
        stampPlus.setMinute(59);
        stampPlus.setHour(23);
        stampPlus.setSecond(59);

      idegaTimestamp stamp = new idegaTimestamp(_stamp.getTimestamp());
        stamp.addDays(-daysBack);
        stampPlus.setMinute(0);
        stampPlus.setHour(0);
        stampPlus.setSecond(0);

      CalendarEntry[] cal = (CalendarEntry[]) CalendarEntry.getStaticInstance().findAllByColumnOrdered(CalendarEntry.getColumnNameEntryDate(),stampPlus.toSQLString(),CalendarEntry.getColumnNameEntryDate(),stamp.toSQLString(),CalendarEntry.getColumnNameEntryDate(),"<",">=");
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
        stampPlus.setMinute(59);
        stampPlus.setHour(23);
        stampPlus.setSecond(59);

      idegaTimestamp stampMinus = new idegaTimestamp(stamp.getTimestamp());
        stampMinus.setDate(1);
        stampPlus.setMinute(0);
        stampPlus.setHour(0);
        stampPlus.setSecond(0);

      return EntityFinder.findAllByColumnOrdered(CalendarEntry.getStaticInstance(),CalendarEntry.getColumnNameEntryDate(),stampPlus.toSQLString(),CalendarEntry.getColumnNameEntryDate(),stampMinus.toSQLString(),CalendarEntry.getColumnNameEntryDate(),"<",">=","distinct",CalendarEntry.getColumnNameEntryDate());
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