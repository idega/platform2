package com.idega.block.calendar.presentation;

/**
 * Title: Calendar
 * Description: idegaWeb Calendar (Block)
 * Copyright:    Copyright (c) 2001
 * Company: idega
 * @author Laddi
 * @version 1.0
 */

import java.util.List;
import java.util.Iterator;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.block.calendar.data.*;
import com.idega.block.calendar.business.*;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.util.idegaTimestamp;
import com.idega.util.idegaCalendar;
import com.idega.util.text.TextSoap;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.Table;
import com.idega.presentation.Image;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;

public class Calendar extends Block{

private boolean _isAdmin = false;
private int _iLocaleID;
private int _view = CalendarBusiness.MONTH;

private idegaTimestamp _stamp;
private String _width = null;
private boolean _isSelectedDay = false;
private int _daysAhead = 7;
private int _daysBack = 7;
private String _bodyColor = "#000000";
private String _headlineColor = "#000000";
private String _dateColor = "#000000";

private String _noActionDay = "#999966";
private String _actionDay = "#660000";

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.calendar";
protected IWResourceBundle _iwrb;
protected IWBundle _iwb;

public Calendar(){
}

public Calendar(idegaTimestamp timestamp){
  _stamp = timestamp;
}

	public void main(IWContext iwc) throws Exception{
    _iwrb = getResourceBundle(iwc);
    _iwb = getBundle(iwc);

    _isAdmin = iwc.hasEditPermission(this);
    _iLocaleID = ICLocaleBusiness.getLocaleId(iwc.getCurrentLocale());

    if ( iwc.getParameter(CalendarBusiness.PARAMETER_VIEW) != null ) {
      _view = Integer.parseInt(iwc.getParameter(CalendarBusiness.PARAMETER_VIEW));
    }

    if ( _stamp == null ) {
      _stamp = CalendarBusiness.getTimestamp(iwc);
    }

    _isSelectedDay = CalendarBusiness.getIsSelectedDay(iwc);

    switch (_view) {
      case CalendarBusiness.DAY:
        drawDay(iwc);
        break;
      case CalendarBusiness.MONTH:
        drawMonth(iwc);
        break;
      case CalendarBusiness.YEAR:
        drawYear(iwc);
        break;
    }
	}

  private void drawDay(IWContext iwc) {
    Table entriesTable = new Table();
      if ( _width != null )
      entriesTable.setWidth(_width);

    String[] localeStrings = null;
    Text headlineText = null;
    Text bodyText = null;
    idegaTimestamp stamp = null;
    boolean hasImage = true;
    int imageID;
    int ypos = 1;

    CalendarEntry[] entries = null;
    if ( _isSelectedDay ) {
      entries = CalendarFinder.getEntries(_stamp);
    }
    else {
      entries = CalendarFinder.getWeekEntries(_stamp,_daysAhead,_daysBack);
    }

    if ( entries != null ) {
      for ( int a = 0; a < entries.length; a++ ) {
        Image typeImage = null;
        localeStrings = CalendarFinder.getEntryStrings(entries[a],_iLocaleID);
        imageID = CalendarFinder.getImageID(entries[a].getEntryTypeID());

        if ( imageID != -1 ) {
          try {
            typeImage = new Image(imageID);
            typeImage.setHorizontalSpacing(3);
          }
          catch (Exception e) {
            typeImage = null;
          }
        }
        if ( typeImage == null ) {
          typeImage = _iwb.getImage("shared/day_dot.gif");
        }

        if ( localeStrings != null ) {
          if ( localeStrings[0] != null )
            headlineText = new Text(localeStrings[0]);
          else
            headlineText = null;

          if ( localeStrings[1] != null )
            bodyText = new Text(localeStrings[1]);
          else
            bodyText = null;
        }

        int xpos = 1;

        if ( headlineText != null ) {
          if ( typeImage != null ) {
            typeImage.setName(CalendarFinder.getEntryTypeName(entries[a].getEntryTypeID(),_iLocaleID));
            entriesTable.add(typeImage,xpos,ypos);
            hasImage = true;
            xpos++;
          }

          headlineText.setFontStyle("font-face: Verdana,Arial,Helvetica,sans-serif; font-size: 9pt; font-weight: bold; color: "+_headlineColor+";");
          entriesTable.setWidth(xpos,ypos,"100%");
          entriesTable.add(headlineText,xpos,ypos);

          stamp = new idegaTimestamp(entries[a].getDate());
          String date = TextSoap.addZero(stamp.getDay()) + "." + TextSoap.addZero(stamp.getMonth()) + "." + Integer.toString(stamp.getYear());
          Text dateText = new Text(date);
            dateText.setFontStyle("font-face: Verdana,Arial,Helvetica,sans-serif; font-size: 8pt; font-weight: bold; color: "+_dateColor+";");

          xpos++;
          entriesTable.setAlignment(xpos,ypos,"right");
          entriesTable.add(dateText,xpos,ypos);

          if ( _isAdmin ) {
            xpos++;
            entriesTable.add(getEditButtons(entries[a].getID()),xpos,ypos);
          }

          if ( bodyText != null ) {
            ypos++;
            bodyText.setFontStyle("font-face: Verdana,Arial,Helvetica,sans-serif; font-size: 8pt; color: "+_bodyColor+";");
            if ( hasImage ) {
              entriesTable.mergeCells(2,ypos,entriesTable.getColumns(),ypos);
              entriesTable.add(bodyText,2,ypos);
            }
            else {
              entriesTable.mergeCells(1,ypos,entriesTable.getColumns(),ypos);
              entriesTable.add(bodyText,1,ypos);
            }
          }

          ypos++;
        }
      }
    }

    if ( ypos == 1 ) {
      headlineText = new Text(_iwrb.getLocalizedString("no_entries","No entries in calendar"));
      headlineText.setFontStyle("font-face: Verdana,Arial,Helvetica,sans-serif; font-size: 9pt; font-weight: bold; color: "+_headlineColor+";");
      entriesTable.add(headlineText,1,1);
      ypos++;
    }

    if ( _isAdmin ) {
      entriesTable.mergeCells(1,ypos,entriesTable.getColumns(),ypos);
      entriesTable.add(getAddIcon(),1,ypos);
      entriesTable.add(getPropertiesIcon(),1,ypos);
    }

    add(entriesTable);
  }

  private void drawMonth(IWContext iwc) {
    SmallCalendar cal = getCalendar(_stamp);
    if ( _width != null ) {
      try {
        cal.setWidth(Integer.parseInt(_width));
      }
      catch (NumberFormatException e) {
        cal.setWidth(110);
      }
    }

    add(cal);
  }

  private SmallCalendar getCalendar(idegaTimestamp stamp) {
    List list = CalendarFinder.getMonthEntries(stamp);

    SmallCalendar calendar = new SmallCalendar(stamp);
      calendar.setDaysAsLink(true);
      calendar.setDayTextColor(_noActionDay);

    if ( list != null ) {
      Iterator iter = list.iterator();
      while (iter.hasNext()) {
        calendar.setDayFontColor(new idegaTimestamp(((CalendarEntry) iter.next()).getDate()),_actionDay);
      }
    }

    return calendar;
  }

  private void drawYear(IWContext iwc) {
    Table yearTable = new Table();
    idegaTimestamp yearStamp = null;
    SmallCalendar calendar = null;
    int ypos = 1;
    int xpos = 1;

    for ( int a = 1; a <= 12; a++ ) {
      yearStamp = new idegaTimestamp(_stamp.getDay(),a,_stamp.getYear());
      calendar = getCalendar(yearStamp);
      calendar.setOnlySelectedHighlighted(true);
      calendar.useNextAndPreviousLinks(false);

      yearTable.add(calendar,xpos,ypos);
      yearTable.setRowVerticalAlignment(ypos,"top");

      xpos = xpos % 3 + 1;
      if(xpos == 1)
        ypos++;
    }

    add(yearTable);
  }

  private Link getAddIcon() {
    Image image = _iwb.getImage("shared/create.gif");
      image.setHorizontalSpacing(1);
    Link link = new Link(image);
      link.setWindowToOpen(CalendarEditor.class);
      if ( this._isSelectedDay )
        link.addParameter(CalendarBusiness.PARAMETER_ENTRY_DATE,_stamp.toSQLString());
    return link;
  }

  private Link getPropertiesIcon() {
    Image image = _iwb.getImage("shared/edit.gif");
      image.setHorizontalSpacing(1);
    Link link = new Link(image);
      link.setWindowToOpen(CalendarTypeEditor.class);
    return link;
  }

  private Table getEditButtons(int entryID) {
    Table table = new Table(2,1);
      table.setCellpadding(0);
      table.setCellspacing(0);

    Image editImage = _iwb.getImage("shared/edit.gif");
      editImage.setHorizontalSpacing(1);
    Image deleteImage = _iwb.getImage("shared/delete.gif");
      deleteImage.setHorizontalSpacing(1);

    Link editLink = new Link(editImage);
      editLink.setWindowToOpen(CalendarEditor.class);
      editLink.addParameter(CalendarBusiness.PARAMETER_ENTRY_ID,entryID);
      editLink.addParameter(CalendarBusiness.PARAMETER_MODE,CalendarBusiness.PARAMETER_MODE_EDIT);
    table.add(editLink,1,1);
    Link deleteLink = new Link(deleteImage);
      deleteLink.setWindowToOpen(CalendarEditor.class);
      deleteLink.addParameter(CalendarBusiness.PARAMETER_ENTRY_ID,entryID);
      deleteLink.addParameter(CalendarBusiness.PARAMETER_MODE_DELETE,CalendarBusiness.PARAMETER_TRUE);
    table.add(deleteLink,2,1);

    return table;
  }

  public void setView(int view) {
    _view = view;
  }

  public void setWidth(int width) {
    _width = Integer.toString(width);
  }

  public void setWidth(String width) {
    _width = width;
  }

  public void setDaysAhead(int daysAhead) {
    _daysAhead = daysAhead;
  }

  public void setDaysBack(int daysBack) {
    _daysBack = daysBack;
  }

  public void setHeadlineColor(String headlineColor) {
    _headlineColor = headlineColor;
  }

  public void setBodyColor(String bodyColor) {
    _bodyColor = bodyColor;
  }

  public void setDateColor(String dateColor) {
    _dateColor = dateColor;
  }

  public void setInActiveDayColor(String color) {
    _noActionDay = color;
  }

  public void setActiveDayColor(String color) {
    _actionDay = color;
  }

  public void setDate(int year,int month,int day) {
    if ( _stamp == null ) {
      _stamp = new idegaTimestamp(day,month,year);
    }
    else {
      _stamp.setDate(day);
      _stamp.setMonth(month);
      _stamp.setYear(year);
    }
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
}