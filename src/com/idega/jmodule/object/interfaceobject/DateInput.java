/*
 * $Id: DateInput.java,v 1.3 2001/04/30 16:40:40 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.jmodule.object.interfaceobject;

import java.io.*;
import java.util.*;
import com.idega.jmodule.object.*;
import com.idega.util.*;

/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class DateInput extends InterfaceObjectContainer{

private Script script;

private DropdownMenu theDay;
private DropdownMenu theMonth;
private DropdownMenu theYear;
private Parameter theWholeDate;
private boolean setCheck = false;
private int fromYear;
private int toYear;
private int selectedYear=-1;

public DateInput(){
	this("dateinput");
}

public DateInput(String name){
	super();
	script = new Script();
	theDay = new DropdownMenu(name+"_day");
	theMonth = new DropdownMenu(name+"_month");
	theYear = new DropdownMenu(name+"_year");
	theWholeDate = new Parameter(name,"");

        super.add(theDay);
	super.add(theMonth);
	super.add(theYear);
	super.add(theWholeDate);

        doSomeShit("Dagur", "Mánuður", "Ár");

}

public DateInput(String name, boolean inShort){
	super();
	script = new Script();
	theDay = new DropdownMenu(name+"_day");
	theMonth = new DropdownMenu(name+"_month");
	theYear = new DropdownMenu(name+"_year");
	theWholeDate = new Parameter(name,"");

        super.add(theDay);
	super.add(theMonth);
	super.add(theYear);
	super.add(theWholeDate);

        doSomeShit("Dag", "Mán", "Ár");

}


private void doSomeShit(String strDay, String strMonth, String strYear) {



	theYear.setOnChange("populateDays(this,this.form."+theMonth.getName()+",this.form."+theDay.getName()+");setValueOfHiddenDate(this.form."+theYear.getName()+",this.form."+theMonth.getName()+",this.form."+theDay.getName()+",this.form."+theWholeDate.getName()+");");
	theMonth.setOnChange("populateDays(this.form."+theYear.getName()+",this,this.form."+theDay.getName()+");setValueOfHiddenDate(this.form."+theYear.getName()+",this.form."+theMonth.getName()+",this.form."+theDay.getName()+",this.form."+theWholeDate.getName()+");");
	theDay.setOnChange("setValueOfHiddenDate(this.form."+theYear.getName()+",this.form."+theMonth.getName()+",this.form."+theDay.getName()+",this.form."+theWholeDate.getName()+");");


	theDay.setParentObject(this.getParentObject());
	theMonth.setParentObject(this.getParentObject());
	theYear.setParentObject(this.getParentObject());



	theYear.addMenuElement("",strYear);

        idegaTimestamp stamp = idegaTimestamp.RightNow();
        int currentYear = stamp.getYear();
        this.setYearRange(currentYear,currentYear+5);


	//theYear.addMenuElement("2000","2000");
	//theYear.addMenuElement("2001","2001");
	//theYear.addMenuElement("2002","2002");

	theMonth.addMenuElement("",strMonth);
	theMonth.addMenuElement("01","janúar");
	theMonth.addMenuElement("02","febrúar");
	theMonth.addMenuElement("03","mars");
	theMonth.addMenuElement("04","apríl");
	theMonth.addMenuElement("05","maí");
	theMonth.addMenuElement("06","júní");
	theMonth.addMenuElement("07","júlí");
	theMonth.addMenuElement("08","ágúst");
	theMonth.addMenuElement("09","september");
	theMonth.addMenuElement("10","október");
	theMonth.addMenuElement("11","nóvember");
	theMonth.addMenuElement("12","desember");

	theDay.addMenuElement("",strDay);
	theDay.addMenuElement("01","1");
	theDay.addMenuElement("02","2");
	theDay.addMenuElement("03","3");
	theDay.addMenuElement("04","4");
	theDay.addMenuElement("05","5");
	theDay.addMenuElement("06","6");
	theDay.addMenuElement("07","7");
	theDay.addMenuElement("08","8");
	theDay.addMenuElement("09","9");

	theDay.addMenuElement("10","10");
	theDay.addMenuElement("11","11");
	theDay.addMenuElement("12","12");
	theDay.addMenuElement("13","13");
	theDay.addMenuElement("14","14");
	theDay.addMenuElement("15","15");
	theDay.addMenuElement("16","16");
	theDay.addMenuElement("17","17");
	theDay.addMenuElement("18","18");
	theDay.addMenuElement("19","19");

	theDay.addMenuElement("20","20");
	theDay.addMenuElement("21","21");
	theDay.addMenuElement("22","22");
	theDay.addMenuElement("23","23");
	theDay.addMenuElement("24","24");
	theDay.addMenuElement("25","25");
	theDay.addMenuElement("26","26");
	theDay.addMenuElement("27","27");
	theDay.addMenuElement("28","28");
	theDay.addMenuElement("29","29");

	theDay.addMenuElement("30","30");
	theDay.addMenuElement("31","31");


        getScript().addFunction("populateDays","function populateDays(yearInput,monthInput,dayInput) {\r	timeA = new Date(yearInput.options[yearInput.selectedIndex].text, monthInput.options[monthInput.selectedIndex].value,1);\r	timeDifference = timeA - 86400000;\r	timeB = new Date(timeDifference);\r	\r	var oldSelectedDay = dayInput.selectedIndex;\r	\r	var daysInMonth = timeB.getDate();\r	for (var i = 0; i < dayInput.length; i++) {\r		dayInput.options[0] = null;\r		dayInput.options[0] = new Option('Dagur','');\r	}\r	for (var i = 1; i <= daysInMonth; i++) {\r		if (i<10){\r			dayInput.options[i] = new Option(i,'0'+i);\r		}\r		else{\r			dayInput.options[i] = new Option(i,i);\r		}\r	}\r	\r	dayInput.options[oldSelectedDay].selected = true;\r	\r}");
        getScript().addFunction("setValueOfHiddenDate","function setValueOfHiddenDate(yearInput,monthInput,dayInput,hiddenInput){\r\r	var yearValue='2000';\r	var monthValue='01';\r	var dayValue='01';\r\r	if(yearInput.selectedIndex != 0){\r		yearValue=yearInput.options[yearInput.selectedIndex].value;\r	}\r	if(monthInput.selectedIndex != 0){\r		monthValue=monthInput.options[monthInput.selectedIndex].value;\r	}\r	if(dayInput.selectedIndex != 0){\r		dayValue=dayInput.options[dayInput.selectedIndex].value;\r	}\r\r	if ((yearInput.selectedIndex == 0) || (monthInput.selectedIndex == 0) || (dayInput.selectedIndex == 0)){\r		hiddenInput.value = '';\r	}\r	else{\r		hiddenInput.value = yearValue+'-'+monthValue+'-'+dayValue+'';\r	}\r}");

	//getScript().addFunction("populateDays","function populateDays(yearInput,monthInput,dayInput) {\r	timeA = new Date(yearInput.options[yearInput.selectedIndex].text, monthInput.options[monthInput.selectedIndex].value,1);\r	timeDifference = timeA - 86400000;\r	timeB = new Date(timeDifference);\r	\r	var oldSelectedDay = dayInput.selectedIndex;\r	\r	var daysInMonth = timeB.getDate();\r	for (var i = 0; i < dayInput.length; i++) {\r		dayInput.options[0] = null;\r		dayInput.options[0] = new Option('Dagur','');\r	}\r	for (var i = 1; i <= daysInMonth; i++) {\r		dayInput.options[i] = new Option(i,i);\r	}\r	\r	dayInput.options[oldSelectedDay].selected = true;\r	\r}");
	//getScript().addFunction("setValueOfHiddenDate","function setValueOfHiddenDate(yearInput,monthInput,dayInput,hiddenInput){\r\r	var yearValue='2000';\r	var monthValue='01';\r	var dayValue='01';\r\r	if(yearInput.selectedIndex != 0){\r		yearValue=yearInput.options[yearInput.selectedIndex].value;\r	}\r	if(monthInput.selectedIndex != 0){\r		monthValue=monthInput.options[monthInput.selectedIndex].value;\r	}\r	if(dayInput.selectedIndex != 0){\r		dayValue=dayInput.options[dayInput.selectedIndex].value;\r	}\r\r	if ((yearInput.selectedIndex == 0) || (monthInput.selectedIndex == 0) || (dayInput.selectedIndex == 0)){\r		hiddenInput.value = '';\r	}\r	else{\r		hiddenInput.value = yearValue+'-'+monthValue+'-'+dayValue+'';\r	}\r}");


	super.add(script);
}

/*private void setScript(Script script){
	this.script = script;
	setAssociatedScript(script);
}*/

/*private Script getScript(){
	if (getAssociatedScript() == null){

		setScript(new Script());
	}
	else
	{
		script = getAssociatedScript();
	}
	return script;
}*/


private Script getScript(){
	return this.script;
}


public void keepStatusOnAction(){
	theDay.keepStatusOnAction();
	theMonth.keepStatusOnAction();
	theYear.keepStatusOnAction();
}

/*
public void setToSubmit(){
	theDay.setToSubmit();
	theMonth.setToSubmit();
	theYear.setToSubmit();
}*/


public void setYear(int year){
	setCheck=true;
        if(!(year>fromYear && year<toYear) || !(year<fromYear && year>toYear)){
          if(year<fromYear){
            fromYear=year;
          }
          if(year>toYear){
            toYear=year;
          }
        }
	//theYear.setSelectedElement(Integer.toString(year));
        selectedYear=year;
}

public void setYear(String year){
  setYear(Integer.parseInt(year));
}

public void setMonth(int month){
	setMonth(Integer.toString(month));
}

public void setMonth(String month){
	setCheck=true;
	if (month.length() > 1){
		theMonth.setSelectedElement(month);
	}
	else{
		theMonth.setSelectedElement("0"+month);
	}
}

public void setDay(int day){
	setDay(Integer.toString(day));
}

public void setDay(String day){
	setCheck=true;
	if (day.length() > 1 ){
		theDay.setSelectedElement(day);
	}
	else{
		theDay.setSelectedElement("0"+day);
	}
}

public void setDate(java.sql.Date date){
  GregorianCalendar greg = new GregorianCalendar();
  greg.setTime(date);
//	setYear(date.getYear()+1900);
//	setMonth(date.getMonth()+1);
//	setDay(date.getDate());
	setYear(greg.get(Calendar.YEAR));
	setMonth(greg.get(Calendar.MONTH)+1);
	setDay(greg.get(Calendar.DAY_OF_MONTH));
	//System.out.println("Date gefur year: "+date.getYear()+" fyrir "+this.getName());
	//System.out.println("Date gefur month: "+date.getMonth()+" fyrir "+this.getName());
	//System.out.println("Date gefur day: "+date.getDay()+" fyrir "+this.getName());
}
/**
**Does nothing - overrides function in superclass
**/
public void add(ModuleObject mo){
	//does nothing
}


public void setYearRange(int fromYear,int toYear){
  this.fromYear=fromYear;
  this.toYear=toYear;
}

//public void main(ModuleInfo modinfo)throws Exception{



  //	theYear.addMenuElement("2000","2000");
  //	theYear.addMenuElement("2001","2001");
  //	theYear.addMenuElement("2002","2002");

//}


public void print(ModuleInfo modinfo)throws IOException{

  if(fromYear < toYear){
    for (int i=fromYear;i<=toYear;i++){
      theYear.addMenuElement(Integer.toString(i));
    }
  }else{
    for (int i=fromYear;i>=toYear;i--){
      theYear.addMenuElement(Integer.toString(i));
    }
  }

  if (selectedYear!=-1){
    theYear.setSelectedElement(Integer.toString(selectedYear));
  }

	if (setCheck == true){
		theWholeDate.setValue(theYear.getSelectedElementValue()+"-"+theMonth.getSelectedElementValue()+"-"+theDay.getSelectedElementValue());
	}
	super.print(modinfo);
}

}
