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

  public SmallCalendar() {
    super();
  }

  public void make(ModuleInfo modinfo){
    idegaCalendar cal = new idegaCalendar();
    int thismonth =  today.getMonth();
    int stampmonth = stamp.getMonth();
    boolean shadow = (thismonth == stampmonth)?true:false;
    int daycount = cal.getLengthOfMonth(stamp.getMonth(),stamp.getYear());
    int daynr = cal.getDayOfWeek(stamp.getYear(),stamp.getMonth(),1);
    String sMonth = cal.getNameOfMonth(stamp.getMonth()).substring(0,3);
    String sYear = String.valueOf(stamp.getYear());
    Text tMonth = new Text(sMonth+" "+sYear);
    tMonth.setFontColor("#FFFFFD");
    tMonth.setFontSize(2);
    tMonth.setBold();
    Link right = new Link(new Image("/pics/calendar/calright.gif"));
    this.addNextMonthPrm(right,stamp);
    Link left = new Link(new Image("/pics/calendar/calleft.gif"));
    this.addLastMonthPrm(left,stamp);

    Table T2 = new Table(3,3);
    T2.setCellpadding(0);
    T2.setCellspacing(0);
    T2.setColor("#6785b7");
    T2.setWidth("135");
    T2.setColumnAlignment(2,"center");

    Table T = new Table();
    T.setColor("#184693");
    T.setCellpadding(1);
    T.setCellspacing(0);

    T2.add(left,2,1);
    T2.add(tMonth,2,1);
    T2.add(right,2,1);

    int number = 1;
    Text t = new Text();
    t.setFontColor("#FFFFFD");
    t.setFontSize(1);
    String[] s = {"S","M","Þ","M","F","F","L"};
    for(int a = 1; a<8;a++){
      t = new Text(s[a-1]);
      t.setFontColor("#FFFFFD");
      t.setFontSize(1);
      T.setAlignment(a,1,"center");
      T.add(t,a,1);
    }
    int n = 1;
    int xpos = daynr;
    int ypos = 2;
    while(n <= daycount){
      t = new Text(String.valueOf(n));
      t.setFontColor("#FFFFFD");
      t.setFontSize(1);
      T.setAlignment(xpos,ypos,"center");
      if(n == today.getDay() && shadow )
        T.setColor(xpos,ypos,"#000000");
      T.add(t,xpos,ypos);

      xpos = xpos%7+1;
      if(xpos == 1)
        ypos++;
      n++;
    }
    T.setRowColor(1,"#000000");
    T2.add(T,2,2);
    add(T2);
    }

    public void main(ModuleInfo modinfo){
      today = new idegaTimestamp();
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

}
