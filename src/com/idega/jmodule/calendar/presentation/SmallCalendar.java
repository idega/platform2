package com.idega.jmodule.calendar.presentation;

import com.idega.jmodule.object.JModuleObject;
import com.idega.jmodule.object.ModuleInfo;
import java.sql.SQLException;
import java.util.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.Image;
import com.idega.jmodule.object.Table;
import com.idega.util.text.*;
import com.idega.util.*;
import com.idega.util.idegaTimestamp;

public class SmallCalendar extends JModuleObject{

  private idegaTimestamp today;
  private idegaTimestamp stamp;
  private idegaCalendar cal = new idegaCalendar();

  private boolean useNextAndPreviousLinks = true;
  private boolean daysAreLinks = false;

  private String textColor = "#FFFFFD";
  private String headerTextColor = "#FFFFFD";
  private String headerColor = "#000000";
  private String bodyColor = "#184693";
  private String inactiveCellColor = bodyColor;
  private String backgroundColor = "#6785b7";

  private int width = 135;


  public Table T = new Table();

  public SmallCalendar() {
    initialize();
  }

  public SmallCalendar(idegaTimestamp timestamp) {
      initialize();
      stamp = timestamp;
  }

  public void make(ModuleInfo modinfo){
    int thismonth =  today.getMonth();
    int stampmonth = stamp.getMonth();
    boolean shadow = (thismonth == stampmonth)?true:false;
        if (shadow) shadow = (today.getYear() == stamp.getYear()) ?true:false;
    int daycount = cal.getLengthOfMonth(stamp.getMonth(),stamp.getYear());
    int daynr = cal.getDayOfWeek(stamp.getYear(),stamp.getMonth(),1);
    String sMonth = cal.getNameOfMonth(stamp.getMonth(),modinfo).substring(0,3);
    String sYear = String.valueOf(stamp.getYear());
    Text tMonth = new Text(sMonth+" "+sYear);
    tMonth.setFontColor(headerTextColor);
    tMonth.setFontSize(2);
    tMonth.setBold();
    Link right = new Link(new Image("/pics/calendar/calright.gif"));
    this.addNextMonthPrm(right,stamp);
    Link left = new Link(new Image("/pics/calendar/calleft.gif"));
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
    }
    T2.add(tMonth,2,1);
    if (useNextAndPreviousLinks) {
        T2.add(right,2,1);
    }

    int number = 1;
    Text t = new Text();
    t.setFontColor(textColor);
    t.setFontSize(1);
//    String[] s = {"S","M","Þ","M","F","F","L"};
    for(int a = 1; a<8;a++){
      t = new Text(cal.getNameOfDay(a,modinfo).substring(0,1));
      t.setFontColor(textColor);
      t.setFontSize(1);
      T.setAlignment(a,1,"center");
      T.add(t,a,1);
    }
    int n = 1;
    int xpos = daynr;
    int ypos = 2;

    int month = stamp.getMonth();
    int year = stamp.getYear();

    Link theLink;

    while(n <= daycount){
      t = new Text(String.valueOf(n));
      t.setFontColor(textColor);
      t.setFontSize(1);
      T.setAlignment(xpos,ypos,"center");
      if(n == today.getDay() && shadow )
        T.setColor(xpos,ypos,headerColor);

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
    T.setRowColor(1,headerColor);
    T2.add(T,2,2);
    add(T2);
    }


    public void initialize() {
        today = new idegaTimestamp();
        T.setCellpadding(1);
        T.setCellspacing(0);
    }


    public void main(ModuleInfo modinfo){

      if (stamp == null) {
          String month = modinfo.getParameter("month");
          String year = modinfo.getParameter("year");
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

      make(modinfo);

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

            int startingX = 1;
            int startingY = 2;

            int daynr = cal.getDayOfWeek(year,month,1);

            int x = ((daynr-1) + day ) % 7;
            int y = (((daynr-1) + day ) / 7) +1;
                if (x == 0) {
                    x=7;
                    --y;
                }

            x += (startingX -1);
            y += (startingY -1);


            T.setColor(x,y,color);

        }
    }

    public void setDayColor(idegaTimestamp timestamp, String color) {
        this.setDayColor(stamp.getYear() ,stamp.getMonth(),stamp.getDate() ,color);
    }




}
