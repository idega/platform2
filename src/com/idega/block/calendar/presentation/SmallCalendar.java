package com.idega.block.calendar.presentation;

import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import java.sql.SQLException;
import java.util.*;
import java.text.DateFormatSymbols;
import com.idega.presentation.text.*;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.util.text.*;
import com.idega.util.*;
import com.idega.util.idegaTimestamp;

public class SmallCalendar extends Block{

private idegaTimestamp today;
private idegaTimestamp stamp;
private idegaCalendar cal = new idegaCalendar();

private boolean useNextAndPreviousLinks = true;
private boolean daysAreLinks = false;
private boolean showNameOfDays = true;

private String textColor = "#FFFFFD";
private String headerTextColor = "#FFFFFD";
private String headerColor = "#000000";
private String bodyColor = "#184693";
private String inactiveCellColor = bodyColor;
private String backgroundColor = "#6785b7";
private String todayColor = headerColor;

private int width = 135;


public Table T = new Table();

public SmallCalendar() {
  initialize();
}

public SmallCalendar(idegaTimestamp timestamp) {
    initialize();
    stamp = timestamp;
}

  public void main(IWContext iwc){
    if (stamp == null) {
        String month = iwc.getParameter("month");
        String year = iwc.getParameter("year");
        if(month != null && year != null){
          try {
            int iMonth = Integer.parseInt(month);
            int iYear = Integer.parseInt(year);
            stamp = new idegaTimestamp( 1,iMonth,iYear);
          }
          catch (Exception ex) {
            stamp = new idegaTimestamp();
          }
        }
        else
          stamp = new idegaTimestamp();
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
    String sMonth = cal.getShortNameOfMonth(stamp.getMonth(),iwc);
    try {
      sMonth = sMonth.substring(0,1).toUpperCase() + sMonth.substring(1);
    }
    catch (Exception e) {
      sMonth = cal.getShortNameOfMonth(stamp.getMonth(),iwc);
    }
    String sYear = String.valueOf(stamp.getYear());
    Text tMonth = new Text(sMonth+" "+sYear);
    tMonth.setFontColor(headerTextColor);
    tMonth.setFontSize(2);
    tMonth.setBold();
    //Link right = new Link(new Image("/pics/calendar/calright.gif"));
    Link right = new Link(">");
      right.setFontStyle("font-face: Verdana, Arial, sans-serif; font-weight: bold; color: #FFFFFF; font-size: 11pt; text-decoration: none;");
    this.addNextMonthPrm(right,stamp);
    //Link left = new Link(new Image("/pics/calendar/calleft.gif"));
    Link left = new Link("<");
      left.setFontStyle("font-face: Verdana, Arial, sans-serif; font-weight: bold; color: #FFFFFF; font-size: 11pt; text-decoration: none;");
    this.addLastMonthPrm(left,stamp);

    Table T2 = new Table(3,3);
    T2.setCellpadding(0);
    T2.setCellspacing(0);
    T2.setColor(backgroundColor);
    T2.setWidth(width);
    T2.setColumnAlignment(2,"center");

    T.setColor(inactiveCellColor);

    if (useNextAndPreviousLinks) {
        T2.add(left,2,1);
        T2.add(Text.getNonBrakingSpace(),2,1);
    }
    T2.add(tMonth,2,1);
    if (useNextAndPreviousLinks) {
        T2.add(Text.getNonBrakingSpace(),2,1);
        T2.add(right,2,1);
    }

    int number = 1;
    Text t = new Text();
    t.setFontColor(textColor);
    t.setFontSize(1);
    if (this.showNameOfDays) {
      for( int a = 1; a < 8; a++ ){
        t = new Text(cal.getShortNameOfDay(a,iwc).substring(0,1).toUpperCase());
        t.setFontColor(textColor);
        t.setFontSize(1);
        T.setAlignment(a,1,"center");
        T.add(t,a,1);
        T.setRowColor(1,headerColor);
      }
    }
    int n = 1;
    int xpos = daynr;
    int ypos = 1;
    if (showNameOfDays) {
        ++ypos;
    }

    int month = stamp.getMonth();
    int year = stamp.getYear();

    for (int i = 1; i < daynr; i++) {
      T.setColor(i,ypos,inactiveCellColor);
    }

    Link theLink;

    while(n <= daycount){
      t = new Text(String.valueOf(n));
      t.setFontColor(textColor);
      t.setFontSize(1);
      T.setAlignment(xpos,ypos,"center");
      if ((n == today.getDay() && shadow) && (!todayColor.equals("")))
        T.setColor(xpos,ypos,todayColor);

      if (this.daysAreLinks) {
        theLink = new Link(t);
          theLink.addParameter("day",n);
          theLink.addParameter("month",stamp.getMonth());
          theLink.addParameter("year",stamp.getYear());
          theLink.setFontColor(textColor);
          theLink.setFontSize(1);
        T.add(theLink,xpos,ypos);
      }
      else {
          T.add(t,xpos,ypos);
      }

      if (T.getColor(xpos,ypos) == null) {
          setDayColor(year,month,n,bodyColor);
      }

      xpos = xpos%7+1;
      if(xpos == 1)
        ypos++;
      n++;
    }
    T2.add(T,2,2);
    add(T2);
  }

  public void initialize() {
      today = new idegaTimestamp();
      T.setCellpadding(1);
      T.setCellspacing(0);
  }

  public void addNextMonthPrm(Link L,idegaTimestamp idts){
    if(idts.getMonth() == 12){
      L.addParameter("month",String.valueOf(1));
      L.addParameter("year",String.valueOf(idts.getYear()+1));
    }
    else{
      L.addParameter("month",String.valueOf(idts.getMonth()+1));
      L.addParameter("year",String.valueOf(idts.getYear()));
    }
  }

  public void addLastMonthPrm(Link L,idegaTimestamp idts){
    if(idts.getMonth() == 1){
      L.addParameter("month",String.valueOf(12));
      L.addParameter("year",String.valueOf(idts.getYear()-1));
    }
    else{
      L.addParameter("month",String.valueOf(idts.getMonth()-1));
      L.addParameter("year",String.valueOf(idts.getYear()));
    }
  }

  public idegaTimestamp nextMonth(idegaTimestamp idts){
    if(idts.getMonth() == 12)
      return new idegaTimestamp(1,1,idts.getYear()+1);
    else
      return new idegaTimestamp(1,idts.getMonth()+1,idts.getYear() );
  }

  public idegaTimestamp lastMonth(idegaTimestamp idts){
    if(idts.getMonth() == 1)
      return new idegaTimestamp(1,12,idts.getYear()-1);
    else
      return new idegaTimestamp(1,idts.getMonth()-1,idts.getYear() );
  }

  public void setTextColor(String color) {
      this.textColor = color;
  }

  public void setBackgroundColor(String color) {
      this.backgroundColor = color;
  }

  public void setHeaderColor(String color) {
      this.headerColor = color;
  }

  public void setHeaderTextColor(String color) {
      this.headerTextColor = color;
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

  public void setWidth(int width) {
      this.width = width;
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

  public void setDayColor(int year, int month, int day, String color) {
      boolean perform = false;
      if (stamp != null) {
          if ((stamp.getMonth() == month) && (stamp.getYear() == year)) {
              perform = true;
          }
      }else {
          perform = true;
      }

      if (perform) {
          int[] xy = this.getXYPos(year, month, day);

          T.setColor(xy[0],xy[1],color);

      }
  }

  public void setDayColor(idegaTimestamp timestamp, String color) {
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

  public synchronized Object clone() {
    SmallCalendar obj = null;
    try {
      obj = (SmallCalendar)super.clone();
      if (this.today != null) {
        obj.today = new idegaTimestamp(today);
      }
      if (this.stamp != null) {
        obj.stamp = new idegaTimestamp(stamp);
      }
      if (this.T != null) {
        obj.T = (Table)T.clone();
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