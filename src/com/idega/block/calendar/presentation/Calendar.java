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
import com.idega.block.IWBlock;
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
import com.idega.builder.data.IBPage;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.block.presentation.CategoryBlock;


public class Calendar extends CategoryBlock implements IWBlock {

private boolean hasEdit = false,hasAdd = false,hasPref = false;
private int _iLocaleID;
private int _view = CalendarBusiness.MONTH;

private idegaTimestamp _stamp;
private String _width = null;
private boolean _isSelectedDay = false;
private int _daysAhead = 7;
private int _daysBack = 7;
private int _numberOfShown = 4;

private String _bodyColor = "#000000";
private String _headlineColor = "#000000";
private String _dateColor = "#000000";

private String _noActionDay = "#999966";
private String _actionDay = "#660000";
private IBPage _page;

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.calendar";
protected IWResourceBundle _iwrb;
protected IWBundle _iwb;
protected IWBundle _iwbCalendar;

private String AddPermission = "add";
private String PrePermission = "pref";
private int iUserId = -1;

public Calendar(){
}

public Calendar(idegaTimestamp timestamp){
  _stamp = timestamp;
}

  public void registerPermissionKeys(){
    registerPermissionKey(AddPermission);
    registerPermissionKey(PrePermission);
  }

  public String getCategoryType(){
    return ((com.idega.block.calendar.data.CalendarCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(CalendarCategory.class)).createLegacy().getCategoryType();
  }

  public boolean getMultible(){
    return true;
  }

  public void main(IWContext iwc) throws Exception{
    _iwrb = getResourceBundle(iwc);
    _iwb = iwc.getApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
    _iwbCalendar = getBundle(iwc);

    iUserId = iwc.getUserId();

    hasEdit = iwc.hasEditPermission(this);
    hasAdd = iwc.hasPermission(AddPermission,this);
    hasPref = iwc.hasPermission(PrePermission,this);

    _iLocaleID = ICLocaleBusiness.getLocaleId(iwc.getCurrentLocale());

    if ( iwc.getParameter(CalendarBusiness.PARAMETER_VIEW) != null ) {
      _view = Integer.parseInt(iwc.getParameter(CalendarBusiness.PARAMETER_VIEW));
    }

    if ( _stamp == null ) {
      String day = iwc.getParameter(CalendarBusiness.PARAMETER_DAY);
      String month = iwc.getParameter(CalendarBusiness.PARAMETER_MONTH);
      String year = iwc.getParameter(CalendarBusiness.PARAMETER_YEAR);
      _stamp = CalendarBusiness.getTimestamp(day,month,year);
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

    ///// Permisson Area ////////////
    boolean buttonsAdded = false;
    if(hasAdd || hasEdit){
        entriesTable.add(getAddIcon(),1,ypos);
        buttonsAdded = true;
    }
    if(hasPref || hasEdit){
      entriesTable.add(getPropertiesIcon(),1,ypos);
      buttonsAdded = true;
    }
    if(hasEdit){
      entriesTable.add(getCategoryIcon(),1,ypos);
      buttonsAdded = true;
    }
    if(buttonsAdded)
      ypos++;
    /////////////////////////////////

    int numberOfShown = 0;

    List entries = null;
    if ( _isSelectedDay ) {

      entries = CalendarFinder.getInstance().listOfEntries(_stamp,getCategoryIds());
      if ( entries != null )
	      numberOfShown = entries.size();
    }
    else {
      entries = CalendarFinder.getInstance().listOfWeekEntries(_stamp,_daysAhead,_daysBack,getCategoryIds());
      if ( entries != null) {
	      if ( entries.size() > _numberOfShown )
	        numberOfShown = _numberOfShown;
	      else
	        numberOfShown = entries.size();
      }
    }

    if ( entries != null ) {
      CalendarEntry entry;
      for ( int a = 0; a < numberOfShown; a++ ) {
        Image typeImage = null;
        entry = (CalendarEntry) entries.get(a);
        localeStrings = CalendarFinder.getInstance().getEntryStrings(entry,_iLocaleID);
        imageID = CalendarFinder.getInstance().getImageID(entry.getEntryTypeID());

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
          typeImage = _iwbCalendar.getImage("shared/day_dot.gif");
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
            typeImage.setName(CalendarFinder.getInstance().getEntryTypeName(entry.getEntryTypeID(),_iLocaleID));
            entriesTable.add(typeImage,xpos,ypos);
            hasImage = true;
            xpos++;
          }

        headlineText.setFontStyle("font-family: Verdana,Arial,Helvetica,sans-serif; font-size: 9pt; font-weight: bold; color: "+_headlineColor+";");
        entriesTable.setWidth(xpos,ypos,"100%");
        entriesTable.add(headlineText,xpos,ypos);

        stamp = new idegaTimestamp(entry.getDate());
        String date = TextSoap.addZero(stamp.getDay()) + "." + TextSoap.addZero(stamp.getMonth()) + "." + Integer.toString(stamp.getYear());
        Text dateText = new Text(date);
          dateText.setFontStyle("font-family: Verdana,Arial,Helvetica,sans-serif; font-size: 8pt; font-weight: bold; color: "+_dateColor+";");

        xpos++;
        entriesTable.setAlignment(xpos,ypos,"right");
        entriesTable.add(dateText,xpos,ypos);

        // Checking permissions
        if ( hasEdit || hasPref || this.iUserId == entry.getUserID()) {
          xpos++;
          entriesTable.add(getEditButtons(entry.getID()),xpos,ypos);
        }

        if ( bodyText != null ) {
          ypos++;
          bodyText.setFontStyle("font-family: Verdana,Arial,Helvetica,sans-serif; font-size: 8pt; color: "+_bodyColor+";");
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

    if ( ypos == 2 ) {
      headlineText = new Text(_iwrb.getLocalizedString("no_entries","No entries in calendar"));
      headlineText.setFontStyle("font-family: Verdana,Arial,Helvetica,sans-serif; font-size: 9pt; font-weight: bold; color: "+_headlineColor+";");
      entriesTable.add(headlineText,1,2);
      ypos++;
    }
    if ( buttonsAdded ) {
      entriesTable.mergeCells(1,1,entriesTable.getColumns(),1);
    }

    add(entriesTable);
  }

  private void drawMonth(IWContext iwc) {
    SmallCalendar cal = getCalendar(_stamp);
    cal.setICObjectInstanceID(this.getICObjectInstanceID());

    if ( _width != null ) {
      try {
	      cal.setWidth(Integer.parseInt(_width));
      }
      catch (NumberFormatException e) {
	      cal.setWidth(110);
      }
    }

      Table monthTable = new Table();
      monthTable.setCellpadding(0);
      monthTable.setCellspacing(0);
      int ypos = 1;

    ///// Permisson Area ////////////
    boolean buttonsAdded = false;
    if(hasAdd || hasEdit){
        monthTable.add(getAddIcon(),1,ypos);
        buttonsAdded = true;
    }
    if(hasPref || hasEdit){
      monthTable.add(getPropertiesIcon(),1,ypos);
      buttonsAdded = true;
    }
    if(hasEdit){
      monthTable.add(getCategoryIcon(),1,ypos);
      buttonsAdded = true;
    }
    if(buttonsAdded)
      ypos++;
    /////////////////////////////////

    monthTable.add(cal,1,ypos);
    add(monthTable);
  }

  private SmallCalendar getCalendar(idegaTimestamp stamp) {
    List list = CalendarFinder.getInstance().getMonthEntries(stamp,getCategoryIds());

    SmallCalendar calendar = new SmallCalendar(stamp);
      calendar.setDaysAsLink(true);
      calendar.setDayTextColor(_noActionDay);
      if ( _page != null ) {
	      calendar.setPage(_page);
      }

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

    ///// Permisson Area ////////////
    boolean buttonsAdded = false;
    if(hasEdit || hasAdd){
        yearTable.add(getAddIcon(),1,ypos);
        buttonsAdded = true;
    }
    if(hasEdit || hasPref){
      yearTable.add(getPropertiesIcon(),1,ypos);
      buttonsAdded = true;
    }
    if(hasEdit){
      yearTable.add(getCategoryIcon(),1,ypos);
      buttonsAdded = true;
    }
    if(buttonsAdded)
      ypos++;
    /////////////////////////////////


    for ( int a = 1; a <= 12; a++ ) {
      yearStamp = new idegaTimestamp(_stamp.getDay(),a,_stamp.getYear());
      calendar = getCalendar(yearStamp);
      calendar.setICObjectInstanceID(this.getICObjectInstanceID());
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
    Link link = new Link(image);
    link.setWindowToOpen(CalendarEditor.class);
    link.addParameter(CalendarBusiness.PARAMETER_IC_CAT,getCategoryId());
    if ( this._isSelectedDay )
	    link.addParameter(CalendarBusiness.PARAMETER_ENTRY_DATE,_stamp.toSQLString());
    return link;
  }

  private Link getPropertiesIcon() {
    Image image = _iwb.getImage("shared/edit.gif","Types");
    Link link = new Link(image);
      link.setWindowToOpen(CalendarTypeEditor.class);
    return link;
  }

  private Link getCategoryIcon() {
    Image image = _iwb.getImage("shared/edit.gif","Categories");
    Link link = getCategoryLink(((com.idega.block.calendar.data.CalendarCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(CalendarCategory.class)).createLegacy().getCategoryType());
    link.setImage(image);
     // link.setWindowToOpen(CalendarTypeEditor.class);
    return link;
  }

  private Table getEditButtons(int entryID) {
    Table table = new Table(2,1);
      table.setCellpadding(0);
      table.setCellspacing(0);

    Image editImage = _iwb.getImage("shared/edit.gif");
    Image deleteImage = _iwb.getImage("shared/delete.gif");

    Link editLink = new Link(editImage);
      editLink.setWindowToOpen(CalendarEditor.class);
      editLink.addParameter(CalendarBusiness.PARAMETER_ENTRY_ID,entryID);
      editLink.addParameter(CalendarBusiness.PARAMETER_MODE,CalendarBusiness.PARAMETER_MODE_EDIT);
      editLink.addParameter(CalendarBusiness.PARAMETER_IC_CAT,getCategoryId());
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

  public void setNumberOfShown(int numberOfShown) {
    _numberOfShown = numberOfShown;
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

  public void setPage(IBPage page) {
    _page = page;
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public boolean deleteBlock(int ICObjectInstanceID) {
    return CalendarBusiness.deleteBlock(getICObjectInstanceID());
  }
}
