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

  private static CalendarFinder calendarFinder;

  public static CalendarFinder getInstance(){
    if(calendarFinder == null)
      calendarFinder = new CalendarFinder();
    return calendarFinder;
  }

   public CalendarEntry[] getEntries(idegaTimestamp stamp) {
    try {
      CalendarEntry[] cal = (CalendarEntry[]) CalendarEntry.getStaticInstance().findAllByColumnOrdered(CalendarEntry.getColumnNameEntryDate(),stamp.toString(),CalendarEntry.getColumnNameEntryTypeID(),"=");
      if ( cal.length > 0 )
        return cal;
      return null;
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
      return null;
    }
  }

  public List listOfEntries(idegaTimestamp stamp,int[] iCategoryIds) {
    try {
      java.io.StringWriter writer;
      StringBuffer sql = new StringBuffer("select * from ").append(CalendarEntry.getEntityTableName());
      sql.append(" where ").append(CalendarEntry.getColumnNameEntryDate()).append("= '").append(stamp.toString()).append("'");
      sql.append(" and ").append(CalendarEntry.getColumnCategoryId()).append(" in (  ");
      for (int i = 0; i < iCategoryIds.length; i++) {
        if(i > 0)
          sql.append(",");
        sql.append(iCategoryIds[i]);
      }
      sql.append(" ) ");
      sql.append(" order by ").append(CalendarEntry.getColumnNameEntryTypeID());
      //System.err.println(sql.toString());
      return EntityFinder.findAll(CalendarEntry.getStaticInstance(),sql.toString());
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);

    }
    return null;
  }

  public CalendarEntry getEntry(int entryID) {
    return (CalendarEntry) CalendarEntry.getEntityInstance(CalendarEntry.class,entryID);
  }

  public CalendarEntry[] getWeekEntries(idegaTimestamp _stamp, int daysAhead, int daysBack) {
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

      CalendarEntry[] cal = (CalendarEntry[]) CalendarEntry.getStaticInstance().findAllByColumnOrdered(CalendarEntry.getColumnNameEntryDate(),stampPlus.toString(),CalendarEntry.getColumnNameEntryDate(),stamp.toString(),CalendarEntry.getColumnNameEntryDate(),"<",">=");
      if ( cal.length > 0 )
        return cal;
      return null;
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
      return null;
    }
  }

  public List listOfWeekEntries(idegaTimestamp _stamp, int daysAhead, int daysBack,int[] iCategoryIds) {
    try {
      idegaTimestamp stampPlus = new idegaTimestamp(_stamp.getTimestamp());
        stampPlus.addDays(daysAhead);
        stampPlus.setMinute(59);
        stampPlus.setHour(23);
        stampPlus.setSecond(59);

      idegaTimestamp stamp = new idegaTimestamp(_stamp.getTimestamp());
        stamp.addDays(-daysBack);
        stamp.setMinute(0);
        stamp.setHour(0);
        stamp.setSecond(0);


      StringBuffer sql = new StringBuffer("select * from ").append(CalendarEntry.getEntityTableName());
      sql.append(" where ").append(CalendarEntry.getColumnNameEntryDate()).append(" < '").append(stampPlus.toString()).append("'");
      sql.append(" and ").append(CalendarEntry.getColumnNameEntryDate()).append(" >= '").append(stamp.toString()).append("'");
      sql.append(" and ").append(CalendarEntry.getColumnCategoryId()).append(" in ( ");
      for (int i = 0; i < iCategoryIds.length; i++) {
        if(i > 0)
          sql.append(",");
        sql.append(iCategoryIds[i]);
      }
      sql.append(" ) ");
      sql.append("order by ").append(CalendarEntry.getColumnNameEntryDate());
      //System.err.println(sql.toString());
      return EntityFinder.findAll(CalendarEntry.getStaticInstance(),sql.toString());
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);

    }
    return null;
  }

  public List getMonthEntries(idegaTimestamp stamp,int[] iCategoryIds) {
    try {
      idegaTimestamp stampPlus = new idegaTimestamp(stamp.getTimestamp());
        stampPlus.addMonths(1);
        stampPlus.setDate(1);
        stampPlus.setMinute(59);
        stampPlus.setHour(23);
        stampPlus.setSecond(59);

      idegaTimestamp stampMinus = new idegaTimestamp(stamp.getTimestamp());
        stampMinus.setDate(1);
        stampMinus.setMinute(0);
        stampMinus.setHour(0);
        stampMinus.setSecond(0);

      StringBuffer sql = new StringBuffer("select distinct * from ").append(CalendarEntry.getEntityTableName());
      sql.append(" where ").append(CalendarEntry.getColumnNameEntryDate()).append(" < '").append(stampPlus.toString()).append("'");
      sql.append(" and ").append(CalendarEntry.getColumnNameEntryDate()).append(" >= '").append(stampMinus.toString()).append("'");
      sql.append(" and ").append(CalendarEntry.getColumnCategoryId()).append(" in ( ");
      for (int i = 0; i < iCategoryIds.length; i++) {
        if(i > 0)
          sql.append(",");
        sql.append(iCategoryIds[i]);
      }
      sql.append(" ) ");
      sql.append(" order by ").append(CalendarEntry.getColumnNameEntryDate());
      //System.err.println(sql.toString());
      return EntityFinder.findAll(CalendarEntry.getStaticInstance(),sql.toString());

    }
    catch (SQLException e) {
      e.printStackTrace(System.err);

    }
    return null;
  }

  public List getMonthEntries(idegaTimestamp stamp) {
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
      return EntityFinder.findAllByColumnOrdered(CalendarEntry.getStaticInstance(),CalendarEntry.getColumnNameEntryDate(),stampPlus.toString(),CalendarEntry.getColumnNameEntryDate(),stampMinus.toString(),CalendarEntry.getColumnNameEntryDate(),"<",">=","distinct",CalendarEntry.getColumnNameEntryDate());
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
      return null;
    }
  }

  public CalendarEntryType getEntryType(int typeID) {
    try {
      return new CalendarEntryType(typeID);
    }
    catch (SQLException e) {
      return null;
    }
  }

  public String getEntryTypeName(int typeID,int localeID) {
    return getEntryTypeName(getEntryType(typeID),localeID);
  }

  public String getEntryTypeName(CalendarEntryType type,int localeID) {
    if ( type != null ) {
      LocalizedText loc = TextFinder.getLocalizedText(type,localeID);
      if ( loc != null ) {
        return loc.getHeadline();
      }
      return "";
    }
    return "";
  }

  public String[] getEntryStrings(CalendarEntry entry,int localeID) {
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

  public int getImageID(int typeID) {
    try {
      return new CalendarEntryType(typeID).getImageID();
    }
    catch (SQLException e) {
      return -1;
    }
  }

}