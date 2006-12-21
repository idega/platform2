package com.idega.block.cal.business;

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
import com.idega.block.cal.data.*;
import com.idega.block.category.data.CategoryEntityBMPBean;
import com.idega.data.EntityFinder;
import com.idega.data.GenericEntity;
import com.idega.util.IWTimestamp;
import com.idega.block.text.business.TextFinder;
import com.idega.block.text.data.LocalizedText;

public class CalendarFinder {

  private static CalendarFinder calendarFinder;

  public static CalendarFinder getInstance(){
    if(calendarFinder == null) {
		calendarFinder = new CalendarFinder();
	}
    return calendarFinder;
  }

   public CalendarEntry[] getEntries(IWTimestamp stamp) {
    try {
      CalendarEntry[] cal = (CalendarEntry[]) com.idega.block.calendar.data.CalendarEntryBMPBean.getStaticInstance().findAllByColumnOrdered(com.idega.block.calendar.data.CalendarEntryBMPBean.getColumnNameEntryDate(),stamp.toString(),com.idega.block.calendar.data.CalendarEntryBMPBean.getColumnNameEntryTypeID(),"=");
      if ( cal.length > 0 ) {
		return cal;
	}
      return null;
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
      return null;
    }
  }

  public List listOfEntries(IWTimestamp _stamp,int[] iCategoryIds) {
    try {
      IWTimestamp stampPlus = new IWTimestamp(_stamp.getTimestamp());
	stampPlus.addDays(1);
	stampPlus.setMinute(0);
	stampPlus.setHour(0);
	stampPlus.setSecond(0);

      IWTimestamp stamp = new IWTimestamp(_stamp.getTimestamp());
	stamp.setMinute(0);
	stamp.setHour(0);
	stamp.setSecond(0);

      StringBuffer sql = new StringBuffer("select * from ").append(com.idega.block.calendar.data.CalendarEntryBMPBean.getEntityTableName());
      sql.append(" where ").append(com.idega.block.calendar.data.CalendarEntryBMPBean.getColumnNameEntryDate()).append(" < '").append(stampPlus.toString()).append("'");
      sql.append(" and ").append(com.idega.block.calendar.data.CalendarEntryBMPBean.getColumnNameEntryDate()).append(" >= '").append(stamp.toString()).append("'");
      sql.append(" and ").append(CategoryEntityBMPBean.getColumnCategoryId()).append(" in (  ");
      for (int i = 0; i < iCategoryIds.length; i++) {
	if(i > 0) {
		sql.append(",");
	}
	sql.append(iCategoryIds[i]);
      }
      sql.append(" ) ");
      sql.append(" order by ").append(com.idega.block.calendar.data.CalendarEntryBMPBean.getColumnNameEntryTypeID());
      return EntityFinder.findAll(com.idega.block.calendar.data.CalendarEntryBMPBean.getStaticInstance(),sql.toString());
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);

    }
    return null;
  }

  public CalendarEntry getEntry(int entryID) {
    return (CalendarEntry) GenericEntity.getEntityInstance(CalendarEntry.class,entryID);
  }

  public CalendarEntry[] getWeekEntries(IWTimestamp _stamp, int daysAhead, int daysBack) {
    try {
      IWTimestamp stampPlus = new IWTimestamp(_stamp.getTimestamp());
	stampPlus.addDays(daysAhead);
	stampPlus.setMinute(59);
	stampPlus.setHour(23);
	stampPlus.setSecond(59);

      IWTimestamp stamp = new IWTimestamp(_stamp.getTimestamp());
	stamp.addDays(-daysBack);
	stamp.setMinute(0);
	stamp.setHour(0);
	stamp.setSecond(0);

      CalendarEntry[] cal = (CalendarEntry[]) com.idega.block.calendar.data.CalendarEntryBMPBean.getStaticInstance().findAllByColumnOrdered(com.idega.block.calendar.data.CalendarEntryBMPBean.getColumnNameEntryDate(),stampPlus.toString(),com.idega.block.calendar.data.CalendarEntryBMPBean.getColumnNameEntryDate(),stamp.toString(),com.idega.block.calendar.data.CalendarEntryBMPBean.getColumnNameEntryDate(),"<",">=");
      if ( cal.length > 0 ) {
		return cal;
	}
      return null;
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
      return null;
    }
  }

  public List listOfWeekEntries(IWTimestamp _stamp, int daysAhead, int daysBack,int[] iCategoryIds) {
    try {
      IWTimestamp stampPlus = new IWTimestamp(_stamp.getTimestamp());
	stampPlus.addDays(daysAhead);
	stampPlus.setMinute(59);
	stampPlus.setHour(23);
	stampPlus.setSecond(59);

      IWTimestamp stamp = new IWTimestamp(_stamp.getTimestamp());
	stamp.addDays(-daysBack);
	stamp.setMinute(0);
	stamp.setHour(0);
	stamp.setSecond(0);


      StringBuffer sql = new StringBuffer("select * from ").append(com.idega.block.calendar.data.CalendarEntryBMPBean.getEntityTableName());
      sql.append(" where ").append(com.idega.block.calendar.data.CalendarEntryBMPBean.getColumnNameEntryDate()).append(" < '").append(stampPlus.toString()).append("'");
      sql.append(" and ").append(com.idega.block.calendar.data.CalendarEntryBMPBean.getColumnNameEntryDate()).append(" >= '").append(stamp.toString()).append("'");
      sql.append(" and ").append(CategoryEntityBMPBean.getColumnCategoryId()).append(" in ( ");
      for (int i = 0; i < iCategoryIds.length; i++) {
	if(i > 0) {
		sql.append(",");
	}
	sql.append(iCategoryIds[i]);
      }
      sql.append(" ) ");
      sql.append("order by ").append(com.idega.block.calendar.data.CalendarEntryBMPBean.getColumnNameEntryDate());
      //System.err.println(sql.toString());
      return EntityFinder.findAll(com.idega.block.calendar.data.CalendarEntryBMPBean.getStaticInstance(),sql.toString());
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);

    }
    return null;
  }

  public List listOfNextEntries(int[] iCategoryIds) {
    try {
      IWTimestamp stamp = new IWTimestamp();

      StringBuffer sql = new StringBuffer("select * from ").append(com.idega.block.calendar.data.CalendarEntryBMPBean.getEntityTableName());
      sql.append(" where ").append(com.idega.block.calendar.data.CalendarEntryBMPBean.getColumnNameEntryDate()).append(" >= '").append(stamp.toString()).append("'");
      sql.append(" and ").append(CategoryEntityBMPBean.getColumnCategoryId()).append(" in ( ");
      for (int i = 0; i < iCategoryIds.length; i++) {
	if(i > 0) {
		sql.append(",");
	}
	sql.append(iCategoryIds[i]);
      }
      sql.append(" ) ");
      sql.append("order by ").append(com.idega.block.calendar.data.CalendarEntryBMPBean.getColumnNameEntryDate());
      return EntityFinder.findAll(com.idega.block.calendar.data.CalendarEntryBMPBean.getStaticInstance(),sql.toString());
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);

    }
    return null;
  }

  public List getMonthEntries(IWTimestamp stamp,int[] iCategoryIds) {
    try {
      IWTimestamp stampPlus = new IWTimestamp(stamp.getTimestamp());
	stampPlus.addMonths(1);
	stampPlus.setDay(1);
	stampPlus.setMinute(59);
	stampPlus.setHour(23);
	stampPlus.setSecond(59);

      IWTimestamp stampMinus = new IWTimestamp(stamp.getTimestamp());
	stampMinus.setDay(1);
	stampMinus.setMinute(0);
	stampMinus.setHour(0);
	stampMinus.setSecond(0);

      StringBuffer sql = new StringBuffer("select distinct * from ").append(com.idega.block.calendar.data.CalendarEntryBMPBean.getEntityTableName());
      sql.append(" where ").append(com.idega.block.calendar.data.CalendarEntryBMPBean.getColumnNameEntryDate()).append(" < '").append(stampPlus.toString()).append("'");
      sql.append(" and ").append(com.idega.block.calendar.data.CalendarEntryBMPBean.getColumnNameEntryDate()).append(" >= '").append(stampMinus.toString()).append("'");
      sql.append(" and ").append(CategoryEntityBMPBean.getColumnCategoryId()).append(" in ( ");
      for (int i = 0; i < iCategoryIds.length; i++) {
	if(i > 0) {
		sql.append(",");
	}
	sql.append(iCategoryIds[i]);
      }
      sql.append(" ) ");
      sql.append(" order by ").append(com.idega.block.calendar.data.CalendarEntryBMPBean.getColumnNameEntryDate());
      //System.err.println(sql.toString());
      return EntityFinder.findAll(com.idega.block.calendar.data.CalendarEntryBMPBean.getStaticInstance(),sql.toString());

    }
    catch (SQLException e) {
      e.printStackTrace(System.err);

    }
    return null;
  }

  public List getMonthEntries(IWTimestamp stamp) {
    try {
      IWTimestamp stampPlus = new IWTimestamp(stamp.getTimestamp());
	stampPlus.addMonths(1);
	stampPlus.setDay(1);
	stampPlus.setMinute(59);
	stampPlus.setHour(23);
	stampPlus.setSecond(59);

      IWTimestamp stampMinus = new IWTimestamp(stamp.getTimestamp());
	stampMinus.setDay(1);
	stampPlus.setMinute(0);
	stampPlus.setHour(0);
	stampPlus.setSecond(0);
      return EntityFinder.findAllByColumnOrdered(com.idega.block.calendar.data.CalendarEntryBMPBean.getStaticInstance(),com.idega.block.calendar.data.CalendarEntryBMPBean.getColumnNameEntryDate(),stampPlus.toString(),com.idega.block.calendar.data.CalendarEntryBMPBean.getColumnNameEntryDate(),stampMinus.toString(),com.idega.block.calendar.data.CalendarEntryBMPBean.getColumnNameEntryDate(),"<",">=","distinct",com.idega.block.calendar.data.CalendarEntryBMPBean.getColumnNameEntryDate());
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
      return null;
    }
  }

  public CalendarEntryType getEntryType(int typeID) {
    try {
      return null;//((com.idega.block.cal.data.CalendarEntryTypeHome)com.idega.data.IDOLookup.getHome(CalendarEntryType.class)).findByPrimaryKey(typeID);
    }
    catch (Exception e) {
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
      return ((com.idega.block.calendar.data.CalendarEntryTypeHome)com.idega.data.IDOLookup.getHomeLegacy(CalendarEntryType.class)).findByPrimaryKeyLegacy(typeID).getImageID();
    }
    catch (SQLException e) {
      return -1;
    }
  }

}
