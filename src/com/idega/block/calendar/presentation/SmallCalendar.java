package com.idega.block.calendar.presentation;

import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import java.util.*;
import java.text.DateFormatSymbols;
import java.util.Hashtable;
import java.util.Enumeration;
import com.idega.presentation.text.*;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.block.calendar.business.CalendarBusiness;
import com.idega.builder.data.IBPage;
import com.idega.util.text.*;
import com.idega.util.*;
import com.idega.util.IWTimestamp;

public class SmallCalendar extends Block{

private IWTimestamp today;
private IWTimestamp stamp;
private IWCalendar cal = new IWCalendar();
private IBPage _page;

private boolean useNextAndPreviousLinks = true;
private boolean daysAreLinks = false;
private boolean showNameOfDays = true;
private boolean _highlight = false;
private boolean LINE_VIEW = false;

private String textColor = "#000000";
private String highlightedText = "#660000";
private String headerTextColor = "#000000";
private String dayTextColor = headerTextColor;
private String headerColor = null;
private String dayCellColor = null;
private String bodyColor = null;
private String inactiveCellColor = null;
private String backgroundColor = null;
private String todayColor = headerColor;
private String selectedColor = "#CCCCCC";
private String URL;

private String width = "110";
private String height = "60";
private Link _link;
private String _target;

private Vector parameterName  = new Vector();
private Vector parameterValue = new Vector();

private Hashtable dayColors = null;
private Hashtable dayFontColors = null;

private boolean useTarget = false;


public Table T;

public SmallCalendar() {
  initialize();
}

  public SmallCalendar(IWTimestamp timestamp) {
      initialize();
      stamp = timestamp;
  }

  public SmallCalendar(int year,int month) {
    initialize();
    stamp = new IWTimestamp();
    stamp.setMonth(month);
    stamp.setYear(year);
  }

  public void main(IWContext iwc){
    if (stamp == null) {
      String day = iwc.getParameter(CalendarBusiness.PARAMETER_DAY);
      String month = iwc.getParameter(CalendarBusiness.PARAMETER_MONTH);
      String year = iwc.getParameter(CalendarBusiness.PARAMETER_YEAR);
      if(isTarget() || !useTarget ){
	stamp = CalendarBusiness.getTimestamp(day,month,year);
      }
      else if(iwc.getSessionAttribute("smcal"+getICObjectInstanceID())!=null){
	stamp = (IWTimestamp) iwc.getSessionAttribute("smcal"+getICObjectInstanceID());
      }
      else
	stamp = IWTimestamp.RightNow();
    }
    make(iwc);
  }

  public void make(IWContext iwc){
    int thismonth =  today.getMonth();
    int stampmonth = stamp.getMonth();

    boolean shadow = (thismonth == stampmonth)?true:false;
	if (shadow) shadow = (today.getYear() == stamp.getYear()) ?true:false;
    int daycount = cal.getLengthOfMonth(stamp.getMonth(),stamp.getYear());
    int daynr = cal.getDayOfWeek(stamp.getYear(),stamp.getMonth(),1);
    String sMonth = cal.getMonthName(stamp.getMonth(),iwc.getCurrentLocale(),IWCalendar.SHORT);
    try {
      sMonth = sMonth.substring(0,1).toUpperCase() + sMonth.substring(1);
    }
    catch (Exception e) {
      sMonth = cal.getMonthName(stamp.getMonth(),iwc.getCurrentLocale(),IWCalendar.SHORT);
    }
    String sYear = String.valueOf(stamp.getYear());
    Text tMonth = new Text(sMonth+" "+sYear);
    tMonth.setFontColor(headerTextColor);
    tMonth.setFontSize(2);
    tMonth.setBold();
    tMonth.setFontStyle("font-family: Arial, Helvetica, sans-serif; font-weight: bold; color: "+headerTextColor+"; font-size: 8pt; text-decoration: none;");
    Link right = new Link(">");

      right.setFontColor(headerTextColor);
      right.setFontSize(2);
      right.setBold();
      right.setFontStyle("font-family: Arial, Helvetica, sans-serif; font-weight: bold; color: "+headerTextColor+"; font-size: 8pt; text-decoration: none;");
      for (int i = 0; i < parameterName.size(); i++) {
	right.addParameter((String) parameterName.get(i), (String) parameterValue.get(i));
      }

    this.addNextMonthPrm(right,stamp);
    right.setTargetObjectInstance(this.getTargetObjectInstance());

    Link left = new Link("<");
      left.setFontColor(headerTextColor);
      left.setFontSize(2);
      left.setBold();
      left.setFontStyle("font-family: Arial, Helvetica, sans-serif; font-weight: bold; color: "+headerTextColor+"; font-size: 8pt; text-decoration: none;");
      for (int i = 0; i < parameterName.size(); i++) {
	left.addParameter((String) parameterName.get(i), (String) parameterValue.get(i));
      }

    this.addLastMonthPrm(left,stamp);
    setAsObjectInstanceTarget(left);

    Table T2 = new Table(1,2);
    T2.setCellpadding(1);
    T2.setCellspacing(0);
    T2.setWidth(width);
    T.setWidth(width);
    T2.setHeight(height);
    T.setHeight(height);
    if( backgroundColor != null ) T2.setColor(backgroundColor);
    T2.setColumnAlignment(1,"center");
    T2.setColumnVerticalAlignment(1,"middle");

    //T.setColor(inactiveCellColor);

    if (useNextAndPreviousLinks) {
	T2.add(left,1,1);
	T2.add(Text.getNonBrakingSpace(1),1,1);
    }
    T2.add(tMonth,1,1);
    if (useNextAndPreviousLinks) {
	T2.add(Text.getNonBrakingSpace(1),1,1);
	T2.add(right,1,1);
    }

    int number = 1;
    Text t = new Text();
    t.setFontColor(textColor);
    t.setFontSize(1);
	if (this.showNameOfDays) {
		int weekdays =( LINE_VIEW?daycount+daynr:8) ;
		int weekday = 1;
		for( int a = 1; a < weekdays; a++ ){
			weekday = a%7;
			if(weekday==0)
				weekday = 7;
			t = new Text(cal.getDayName(weekday,iwc.getCurrentLocale(),IWCalendar.LONG).substring(0,1).toUpperCase());
			t.setFontStyle("font-family: Verdana,Arial, Helvetica, sans-serif; font-weight: bold; color: "+dayTextColor+"; font-size: 10px; text-decoration: none;");
			T.setAlignment(a,1,"center");
			T.add(t,a,1);
      	}
      	if( dayCellColor != null) 
      		T.setRowColor(1,dayCellColor);
    }

    int n = 1;
    int xpos = daynr;
    int ypos = 1;
    if ( showNameOfDays )
      ypos++;

    int month = stamp.getMonth();
    int year = stamp.getYear();

    if ( dayColors != null ) {
    	Enumeration enum = dayColors.keys();
      	while (enum.hasMoreElements()) {
			String dayString = (String) enum.nextElement();
			if ( inThisMonth(dayString,year,month) ) {
	  			IWTimestamp newStamp = new IWTimestamp(dayString);
	  			int[] XY = getXYPos(newStamp.getYear(),newStamp.getMonth(),newStamp.getDay());
	  			T.setColor(XY[0],XY[1],getDayColor(dayString));
			}
      	}
    }

    Link theLink;
    String dayColor = null;
    int newYPos = -1;

    while ( n <= daycount ) {
    	t = new Text(String.valueOf(n));
      	dayColor = textColor;
      	if ( getDayFontColor(getDateString(year,month,n)) != null ) {
			dayColor = getDayFontColor(getDateString(year,month,n));
			t.setFontStyle("font-family: Verdana,Arial, Helvetica, sans-serif; color: "+dayColor+"; font-size: 10px; font-weight: bold; text-decoration: none;");
      }
      else {
			if ( today.getYear() == year && today.getMonth() == month && today.getDay() == n ) {
	  			dayColor = dayTextColor;
	  			t.setFontStyle("font-family: Verdana,Arial, Helvetica, sans-serif; color: "+dayColor+"; font-size: 10px; font-weight: bold; text-decoration: none;");
			}
			else {
	  			t.setFontStyle("font-family: Arial, Helvetica, sans-serif; color: "+dayColor+"; font-size: 10px; text-decoration: none;");
			}
      }
      T.setAlignment(xpos,ypos,"center");

      if ( (todayColor!=null) && ( (n == today.getDay()) && shadow)){
		T.setColor(xpos,ypos,todayColor);
      }

      if ( _highlight ) {
			if ( n == stamp.getDay() && month == stamp.getMonth() && year == stamp.getYear() ) {
	  			T.setColor(xpos,ypos,selectedColor);
			}
      }

      if (daysAreLinks) {
		theLink = getLink();
	  	theLink.setPresentationObject(t);
	  	if ( _page != null ) {
	    	theLink.setPage(_page);
	  	}
	  	theLink.addParameter(CalendarBusiness.PARAMETER_DAY,n);
	  	theLink.addParameter(CalendarBusiness.PARAMETER_MONTH,stamp.getMonth());
	  	theLink.addParameter(CalendarBusiness.PARAMETER_YEAR,stamp.getYear());
	  	theLink.setFontColor(textColor);
	  	theLink.setFontSize(1);
	  	for (int i = 0; i < parameterName.size(); i++) {
	    	theLink.addParameter((String) parameterName.get(i), (String) parameterValue.get(i));
	  	}
		T.add(theLink,xpos,ypos);
      }
      else {
		T.add(t,xpos,ypos);
      }

	  if(LINE_VIEW)
	   	xpos++;
	  else
      	xpos = xpos % 7 + 1;
      if(xpos == 1 && !LINE_VIEW)
			ypos++;
      n++;
    }

    if( inactiveCellColor != null ){
      for ( int a = 1; a <= T.getRows(); a++ ) {
		for ( int b = 1; b <= T.getColumns(); b++ ) {
	  		if ( T.getColor(b,a) == null )
	    	T.setColor(b,a,inactiveCellColor);
		}
      }
    }

    T2.add(T,1,2);
    add(T2);
    iwc.setSessionAttribute("smcal"+getICObjectInstanceID(),stamp);
  }

  public void initialize() {
    today = new IWTimestamp();
    T = new Table();
    T.setCellpadding(2);
    T.setCellspacing(0);
    T.setWidth(width);
  }

  public void addNextMonthPrm(Link L,IWTimestamp idts){
    if(idts.getMonth() == 12){
      L.addParameter(CalendarBusiness.PARAMETER_MONTH,String.valueOf(1));
      L.addParameter(CalendarBusiness.PARAMETER_YEAR,String.valueOf(idts.getYear()+1));
    }
    else{
      L.addParameter(CalendarBusiness.PARAMETER_MONTH,String.valueOf(idts.getMonth()+1));
      L.addParameter(CalendarBusiness.PARAMETER_YEAR,String.valueOf(idts.getYear()));
    }
    //L.addParameter(CalendarBusiness.PARAMETER_DAY,String.valueOf(idts.getDay()));
  }

  public void addLastMonthPrm(Link L,IWTimestamp idts){
    if(idts.getMonth() == 1){
      L.addParameter(CalendarBusiness.PARAMETER_MONTH,String.valueOf(12));
      L.addParameter(CalendarBusiness.PARAMETER_YEAR,String.valueOf(idts.getYear()-1));
    }
    else{
      L.addParameter(CalendarBusiness.PARAMETER_MONTH,String.valueOf(idts.getMonth()-1));
      L.addParameter(CalendarBusiness.PARAMETER_YEAR,String.valueOf(idts.getYear()));
    }
    //L.addParameter(CalendarBusiness.PARAMETER_DAY,String.valueOf(idts.getDay()));
  }

  public IWTimestamp nextMonth(IWTimestamp idts){
    if(idts.getMonth() == 12)
      return new IWTimestamp(1,1,idts.getYear()+1);
    else
      return new IWTimestamp(1,idts.getMonth()+1,idts.getYear() );
  }

  public IWTimestamp lastMonth(IWTimestamp idts){
    if(idts.getMonth() == 1)
      return new IWTimestamp(1,12,idts.getYear()-1);
    else
      return new IWTimestamp(1,idts.getMonth()-1,idts.getYear() );
  }

  public String getDateString(int year,int month,int day) {
    return Integer.toString(year)+"-"+TextSoap.addZero(month)+"-"+TextSoap.addZero(day);
  }

  public String getDayColor(String dateString) {
    if ( dayColors != null ) {
      if ( dayColors.get(dateString) != null ) {
	return (String) dayColors.get(dateString);
      }
      else {
	return null;
      }
    }
    return null;
  }

  public String getDayFontColor(String dateString) {
    if ( dayFontColors != null ) {
      if ( dayFontColors.get(dateString) != null ) {
	return (String) dayFontColors.get(dateString);
      }
      else {
	return null;
      }
    }
    return null;
  }

  private boolean inThisMonth(String dayString, int year, int month) {
    if ( dayString != null ) {
      if ( dayString.substring(0,7).equalsIgnoreCase(getDateString(year,month,1).substring(0,7)) ) {
	return true;
      }
      return false;
    }
    return false;
  }

  public void setTextColor(String color) {
      this.textColor = color;
  }

  public void setOnlySelectedHighlighted(boolean highlight) {
      this._highlight = highlight;
  }

  public void setSelectedHighlighted(boolean highlight) {
      this._highlight = highlight;
  }

  public void setSelectedHighlightColor(String color) {
      this.selectedColor = color;
  }

  public void setHighlightedTextColor(String color) {
      this.highlightedText = color;
  }

  public void setBackgroundColor(String color) {
      this.backgroundColor = color;
  }

  public void setDayCellColor(String color) {
      this.dayCellColor = color;
  }

  public void setHeaderColor(String color) {
      this.headerColor = color;
  }

  public void setHeaderTextColor(String color) {
      this.headerTextColor = color;
  }

  public void setDayTextColor(String color) {
      this.dayTextColor = color;
  }

  public void setBodyColor(String color) {
      this.bodyColor = color;
  }

  public void setInActiveCellColor(String color) {
      this.inactiveCellColor = color;
  }

  public void useNextAndPreviousLinks(boolean use) {
      this.useNextAndPreviousLinks = use;
  }

  public void setDaysAsLink(boolean use) {
      this.daysAreLinks = use;
  }

  public void setURL(String url) {
      this.URL = url;
      setDaysAsLink(true);
  }

  public void setWidth(String width) {
      this.width = width;
  }

  public void setWidth(int width) {
      setWidth(Integer.toString(width));
  }

  public void setHeight(String height) {
      this.height = height;
  }

  public void setHeight(int height) {
      setHeight(Integer.toString(height));
  }

  public void showNameOfDays(boolean show) {
      this.showNameOfDays = show;
  }

  public void setColorToday(String color) {
    this.todayColor = color;
  }

  public void useColorToday(boolean useColorToday) {
    if (!useColorToday) {
	this.todayColor = "";
    }
  }

  public void setDayFontColor(int year, int month, int day) {
    if (dayFontColors ==  null) {
      dayFontColors = new Hashtable();
    }
    dayFontColors.put(getDateString(year,month,day),highlightedText);
  }

  public void setDayFontColor(int year, int month, int day, String color) {
    if (dayFontColors ==  null) {
      dayFontColors = new Hashtable();
    }
    dayFontColors.put(getDateString(year,month,day),color);
  }

  public void setDayFontColor(IWTimestamp timestamp, String color) {
    setDayFontColor(timestamp.getYear() ,timestamp.getMonth(),timestamp.getDay() ,color);
  }

  public void setTodayFontColor(String color) {
    IWTimestamp timestamp = new IWTimestamp();
    setDayFontColor(timestamp.getYear() ,timestamp.getMonth(),timestamp.getDay() ,color);
  }

  public void setDayColor(int year, int month, int day, String color) {
    if (dayColors ==  null) {
      dayColors = new Hashtable();
    }
    dayColors.put(getDateString(year,month,day),color);
  }

  public void setDayColor(IWTimestamp timestamp, String color) {
      this.setDayColor(timestamp.getYear() ,timestamp.getMonth(),timestamp.getDay() ,color);
  }

  public void setDayOfWeekColor(int dayOfWeek, String color) {

      int startingY = 1;
      if (showNameOfDays) {
	  ++startingY;
      }
      int[] lastDay = getMaxPos();
      int maxX = lastDay[0];
      int maxY = lastDay[1];

      if (maxX < dayOfWeek) --maxY;


      for (int i = startingY; i <= maxY; i++) {
	T.setColor(dayOfWeek,i,color);
      }

  }

  /**
   * returns the x and y pos of the last day of the month
   */
  private int[] getMaxPos() {
      int day = cal.getLengthOfMonth(stamp.getMonth(), stamp.getYear());

      return this.getXYPos(stamp.getYear(), stamp.getMonth() ,day);
  }

  private int[] getXYPos(int year, int month, int day) {
    int startingX = 1;
    int startingY = 1;
    if (showNameOfDays) {
	++startingY;
    }

    int daynr = cal.getDayOfWeek(year,month,1);

    int x = ((daynr-1) + day ) % 7;
    int y = (((daynr-1) + day ) / 7) +1;
	if (x == 0) {
	    x=7;
	    --y;
	}

    x += (startingX -1);
    y += (startingY -1);

    int[] returner = {x,y};
    return returner;
  }

  private String getTarget(){
    return _target;
  }

  public void setTarget(String target){
    _target = target;
  }
  
  public void setAsLineView(boolean line){
  	this.LINE_VIEW = line;
  }

  private Link getLink(){
    if(_link==null){
      _link=new Link();
      if(getTarget()!=null){
	_link.setTarget(getTarget());
      }
      setAsObjectInstanceTarget(_link);
    }

    return (Link) _link.clone();
  }

  public void setLink(Link link){
    _link = link;
  }

  public void setPage(IBPage page) {
    _page = page;
  }

  public void setTimestamp(IWTimestamp stamp) {
    this.stamp = stamp;
  }

  public void addParameterToLink(String name, int value) {
    addParameterToLink(name, Integer.toString(value));
  }
  public void addParameterToLink(String name, String value) {
    parameterName.add(name);
    parameterValue.add(value);
  }

  public synchronized Object clone() {
    SmallCalendar obj = null;
    try {
      obj = (SmallCalendar)super.clone();
      if (this.today != null) {
	obj.today = new IWTimestamp(today);
      }
      if (this.stamp != null) {
	obj.stamp = new IWTimestamp(stamp);
      }
      if (this.T != null) {
	obj.T = (Table)T.clone();
      }
      if (this.dayColors != null) {
	obj.dayColors = (Hashtable)dayColors.clone();
      }
      if (this.dayFontColors != null) {
	obj.dayFontColors = (Hashtable)dayFontColors.clone();
      }
      if (this.parameterName != null) {
	obj.parameterName = (Vector) parameterName.clone();
      }
      if (this.parameterValue != null) {
	obj.parameterValue = (Vector) parameterValue.clone();
      }

      obj.cal = this.cal;

      obj.useNextAndPreviousLinks = this.useNextAndPreviousLinks;
      obj.daysAreLinks = this.daysAreLinks;
      obj.showNameOfDays = this.showNameOfDays;

      obj.textColor = this.textColor;
      obj.headerTextColor = this.headerTextColor;
      obj.headerColor = this.headerColor;
      obj.bodyColor = this.bodyColor;
      obj.inactiveCellColor = this.inactiveCellColor;
      obj.backgroundColor = this.backgroundColor;
      obj.todayColor = this.todayColor;
    }
    catch(Exception ex) {
      ex.printStackTrace(System.err);
    }
    return obj;
  }
}
