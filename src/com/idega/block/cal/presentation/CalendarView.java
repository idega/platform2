/*
 * Created on Jan 16, 2004
 */
package com.idega.block.cal.presentation;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import com.idega.block.cal.business.CalBusiness;
import com.idega.block.cal.data.CalendarEntry;
import com.idega.block.cal.data.CalendarLedger;
import com.idega.block.presentation.CategoryBlock;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.CalendarParameters;
import com.idega.idegaweb.presentation.SmallCalendar;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.HiddenInput;
import com.idega.user.data.User;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;

/**
 * Description: <br>
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:birna@idega.is">Birna Iris Jonsdottir</a>
 */
public class CalendarView extends CategoryBlock{
	
	private IWTimestamp now = null;
	private IWTimestamp timeStamp = null;
	private IWCalendar cal = null;
	private int beginHour = 8;
	private int endHour = 17;
	private String borderWhiteTableStyle = "borderAllWhite";
	private int view = CalendarParameters.MONTH;
	private String styledLink = "styledLink";
	private String styledLinkBox = "styledLinkBox";
	
	public static String ACTION = "action";
	public static String OPEN = "open";
	public static String CLOSE = "close";
	
	private String action = null;
	
	private CalBusiness calBiz;


	public CalendarView() {

	}
	public String getCategoryType() {
		return "";
	}
	public boolean getMultible() {
		return true;
	}
	/**
	 * draws the day view of the CalendarView
	 * By default the day view varies from 8:00 until 17:00
	 * but can be changed via setBeginHour(int hour) and setEndHour(int hour)
	 */
	public Table dayView(IWContext iwc, IWTimestamp stamp) {
		//row is 2 because in the first row info on the day (name etc. are printed)
		int row = 2;
		now = new IWTimestamp();
		Table dayTable = new Table();
		dayTable.setWidth("100%");
		dayTable.setWidth(1,20);
		dayTable.setWidth(2,"80%");
		dayTable.mergeCells(1,1,2,1);
		String dateString = stamp.getDateString("dd MMMMMMMM, yyyy",iwc.getCurrentLocale());
		dayTable.add(dateString,1,1);
		//the outer for-loop goes through the hours and prints out
		//the style for each cell,
		//the entrylist for each hour
		for(int i=beginHour;i<=endHour;i++) {
			now.setTime(i,0,0);
			dayTable.add(now.getTime().toString(),1,row);
			dayTable.setColor(1,row,"#ffffff");
			Timestamp fromStamp = Timestamp.valueOf(stamp.getDateString("yyyy-MM-dd hh:mm:ss.S")); 
			fromStamp.setHours(beginHour);
			fromStamp.setMinutes(0);
			fromStamp.setNanos(0);
			Timestamp toStamp =Timestamp.valueOf(stamp.getDateString("yyyy-MM-dd hh:mm:ss.S"));
			toStamp.setHours(endHour);
			toStamp.setMinutes(0);
			toStamp.setNanos(0);			
			List listOfEntries = (List) getCalBusiness(iwc).getEntriesBetweenTimestamps(fromStamp,toStamp);	
			//the inner for-loop goes through the list of entries and prints them out as a link
			//the link opens the view for the entry
			for(int j=0; j<listOfEntries.size(); j++) {
				CalendarEntry entry = (CalendarEntry) listOfEntries.get(j);
//				System.out.println("listOfEntries.size(): " + listOfEntries.size());
//				System.out.println("entry " + j + entry.getName());
				Timestamp fStamp = entry.getDate();
				Timestamp tStamp = entry.getEndDate();
				//i is the current hour 
				if(i <= tStamp.getHours() && i >= fStamp.getHours()) {
					String headline = entry.getName();
					Link headlineLink = new Link(headline);
					headlineLink.addParameter(ACTION,OPEN);
					headlineLink.addParameter(CalendarParameters.PARAMETER_VIEW,view);
					headlineLink.addParameter(CalendarParameters.PARAMETER_YEAR, stamp.getYear());
					headlineLink.addParameter(CalendarParameters.PARAMETER_MONTH, stamp.getMonth());
					headlineLink.addParameter(CalendarParameters.PARAMETER_DAY, stamp.getDay());
					headlineLink.addParameter("entryID", entry.getPrimaryKey().toString());
					headlineLink.addParameter(CalendarEntryCreator.creatorViewParameterName,"view");
					headlineLink.setStyleClass(styledLink);
					dayTable.add(headlineLink,2,row);
					dayTable.add("<br>",2,row);
				}
			}
			
			dayTable.setStyleClass(2,row,borderWhiteTableStyle);
			row++;
		}
		return dayTable;
	}
	/** 
	 * draws the week view of the CalendarView
	 * By default the week view varies from 8:00 until 17:00
	 * but can be changed via setBeginHour(int hour) and setEndHour(int hour)
	 */
	
	public Table weekView(IWContext iwc,IWTimestamp stamp) {
		int row = 1;
		int column = 1;
		int day = 0;
		/*
		 * now holds the current time
		 */
		now = new IWTimestamp(); 
		/*
		 * timeStamp is used to traverse trough the week
		 */
		timeStamp = new IWTimestamp();
		cal = new IWCalendar();
		Text nameOfDay = new Text();
		Table weekTable = new Table();
		weekTable.setWidth("100%");
		weekTable.setWidth(1,20);
		String yearString = timeStamp.getDateString("yyyy");
		//TODO: make "vika ársins" localized!!
		weekTable.add(yearString + "<br>" + timeStamp.getWeekOfYear() + " vika ársins",1,1);
		int weekdays = 8;
		int weekday = cal.getDayOfWeek(now.getYear(),now.getMonth(),now.getDay());
		
		int today = timeStamp.getDay();
		/*
		 * The outer for-loop runs through the columns of the weekTable
		 * the weekdays
		 */
		for(int i=0;i<weekdays; i++) {
			row = 1;
			/*
			 * The inner for-loop runs through the rows of the weekTable
			 * the hours
			 */
			for(int j=beginHour;j<endHour;j++) {
				if(column == 1 && row != 1) {
					timeStamp.setTime(j,0,0);
					weekTable.add(timeStamp.getTime().toString(),column,row);
					weekTable.setColor(column,row,"#ffffff");
				}
				else if(row == 1 && column != 1) {
					if(i==weekday) {
						weekTable.setColor(column,row,"#e9e9e9");
					}
					nameOfDay = new Text(cal.getDayName(i, iwc.getCurrentLocale(), IWCalendar.LONG).toString().toLowerCase());
					weekTable.add(nameOfDay,column,row);
					if(i < weekday) {
						if(today == 1) {
							int lengthOfPreviousMonth = 0;
							
							if(timeStamp.getMonth() == 1) {
								/*
								 * if January the lengthOfPreviousMonth is the length of Desember the year before
								 */
								lengthOfPreviousMonth = cal.getLengthOfMonth(12,timeStamp.getYear()-1);
							}
							else {
								/*
								 * else lengthOfPreviousMonth is the length of the month before in the current year
								 */
								lengthOfPreviousMonth = cal.getLengthOfMonth(timeStamp.getMonth()-1,timeStamp.getYear());
							}
							day = (today + lengthOfPreviousMonth) - (weekday - i);
							
						}
						else {
							day = today - (weekday - i);
						}						
						timeStamp.setDay(day);
					}// end if					
					weekTable.add("-" + timeStamp.getDateString("dd.MM"),column,row);
					timeStamp.addDays(1);					
				}// end else if
				else {
					weekTable.setStyleClass(column,row,borderWhiteTableStyle);
					Timestamp fromStamp = Timestamp.valueOf(timeStamp.getDateString("yyyy-MM-dd hh:mm:ss.S")); 
					fromStamp.setHours(beginHour);
					fromStamp.setMinutes(0);
					fromStamp.setNanos(0);
					Timestamp toStamp =Timestamp.valueOf(timeStamp.getDateString("yyyy-MM-dd hh:mm:ss.S"));
					toStamp.setHours(endHour);
					toStamp.setMinutes(0);
					toStamp.setNanos(0);			
					List listOfEntries = (List) getCalBusiness(iwc).getEntriesBetweenTimestamps(fromStamp,toStamp);				
					for(int h=0; h<listOfEntries.size(); h++) {
						CalendarEntry entry = (CalendarEntry) listOfEntries.get(h);
						Timestamp fStamp = entry.getDate();
						Timestamp tStamp = entry.getEndDate();
						if(j <= tStamp.getHours() && j >= fStamp.getHours()) {
							String headline = entry.getName();
							Link headlineLink = new Link(headline);
							headlineLink.addParameter(ACTION,OPEN);
							headlineLink.addParameter(CalendarParameters.PARAMETER_YEAR, stamp.getYear());
							headlineLink.addParameter(CalendarParameters.PARAMETER_MONTH, stamp.getMonth());
							headlineLink.addParameter(CalendarParameters.PARAMETER_DAY, stamp.getDay());
							headlineLink.addParameter("entryID", entry.getPrimaryKey().toString());
							headlineLink.addParameter(CalendarEntryCreator.creatorViewParameterName,"view");
							headlineLink.setStyleClass(styledLink);
							headlineLink.addParameter(CalendarParameters.PARAMETER_VIEW,view);
							weekTable.add(headlineLink,column,row);
							weekTable.add("<br>",column,row);
						}
					}	//end for				
				}
				row++;
			} //end inner for
			column++;
		}//end outer for
		return weekTable;
	}
	/**
	 * 
	 * @return a table displaying the days of the current month
	 */
	
	public Table monthView(IWContext iwc, IWTimestamp stamp) {	
//		iwc.setSessionAttribute("calwin_month" + getICObjectInstanceID(), stamp);
		/*
		 * now holds the current time
		 */
		now = IWTimestamp.RightNow();
		cal = new IWCalendar();
		Text nameOfDay = new Text();
		Text t = new Text();
		
		int weekdays = 8;
		int daycount = cal.getLengthOfMonth(stamp.getMonth(),stamp.getYear());
		int firstWeekDayOfMonth = cal.getDayOfWeek(stamp.getYear(),stamp.getMonth(),1);
//		int dayOfMonth = cal.getDay();
		int row = 3;
		int column = firstWeekDayOfMonth;
		int n = 1;
		
		Table monthTable = new Table();
		monthTable.setColor("#cccccc");
		monthTable.setCellspacing(1);
		monthTable.setCellpadding(0);
		monthTable.setWidth("100%");
		
		
		int weekday = 1;
		for(int i=1; i<weekdays; i++) {
			weekday = i % 7;
			if (weekday == 0)
				weekday = 7;
			nameOfDay = new Text(cal.getDayName(weekday, iwc.getCurrentLocale(), IWCalendar.LONG).toString().toLowerCase());
			monthTable.add(nameOfDay,i,2);
			monthTable.setWidth(i,2,"200");
			monthTable.setStyleClass(i,2,"main");
		}
		while (n <= daycount) {
			Table dayCell = new Table();
			dayCell.setCellspacing(0);
			dayCell.setCellpadding(1);
			dayCell.setWidth("100%");
			dayCell.setHeight("100%");
			dayCell.setVerticalAlignment(1,1,"top");
			
			t = new Text(String.valueOf(n));
			int cellRow = 2;

			monthTable.setWidth(column,row,"200");
			monthTable.setHeight(column,row,"105");
//			monthTable.setStyleClass(column,row,borderWhiteTableStyle);
			if(n == now.getDay()) {
				dayCell.setColor("#e9e9e9");
//				monthTable.setColor(column,row,"#e9e9e9");
			}
			else {
				dayCell.setColor("#ffffff");
			}
//			monthTable.add(t, column, row);
			dayCell.add(t,1,1);
			dayCell.setHeight(1,1,"12");
			dayCell.add("<br>",1,1);
			dayCell.setAlignment(1,1,"right");
			Timestamp fromStamp = Timestamp.valueOf(stamp.getDateString("yyyy-MM-dd hh:mm:ss.S"));
			fromStamp.setDate(n);
			fromStamp.setHours(1);
			fromStamp.setMinutes(0);
			fromStamp.setNanos(0);	
			Timestamp toStamp = Timestamp.valueOf(stamp.getDateString("yyyy-MM-dd hh:mm:ss.S"));
			toStamp.setDate(n);
			toStamp.setHours(23);
			toStamp.setMinutes(0);
			toStamp.setNanos(0);
			List listOfEntries = (List) getCalBusiness(iwc).getEntriesBetweenTimestamps(fromStamp,toStamp);				
			for(int h=0; h<listOfEntries.size(); h++) {
				CalendarEntry entry = (CalendarEntry) listOfEntries.get(h);
				String headline = entry.getName();
				Link headlineLink = new Link(headline);
				headlineLink.addParameter(ACTION,OPEN);
				headlineLink.addParameter(CalendarParameters.PARAMETER_VIEW,view);
				headlineLink.addParameter(CalendarParameters.PARAMETER_YEAR, stamp.getYear());
				headlineLink.addParameter(CalendarParameters.PARAMETER_MONTH, stamp.getMonth());
				headlineLink.addParameter(CalendarParameters.PARAMETER_DAY, stamp.getDay());
				headlineLink.addParameter("entryID", entry.getPrimaryKey().toString());
				headlineLink.addParameter(CalendarEntryCreator.creatorViewParameterName,"view");
				headlineLink.setStyleClass(styledLink);
				dayCell.add(headlineLink,1,cellRow);
				dayCell.setVerticalAlignment(1,cellRow,"top");
				dayCell.add("<br>",1,cellRow++);
//				monthTable.add(headlineLink,column,row);
//				monthTable.add("<br>",column,row);
			}
			monthTable.add(dayCell,column,row);
			monthTable.setColor(column,row,"#ffffff");
			monthTable.add(Text.NON_BREAKING_SPACE,column,row);
			monthTable.setVerticalAlignment(column,row,"top");
			column = column % 7 + 1;
			if (column == 1)
				row++;
			n++;
			
		}
		
		String dateString = stamp.getDateString("MMMMMMMM, yyyy",iwc.getCurrentLocale());
		monthTable.mergeCells(1,1,7,1);
		monthTable.setAlignment(1,1,"center");
		monthTable.add(dateString,1,1);
		
		return monthTable;
	}
	
	public Table yearView(IWContext iwc, IWTimestamp stamp) {
		
		now = new IWTimestamp(); 
		
		Table yearTable = new Table();
		
		IWTimestamp yearStamp = null;
		SmallCalendar smallCalendar = null;
		int row = 1;
		int column = 1;


		for (int a = 1; a <= 12; a++) {
			yearStamp = new IWTimestamp(stamp.getDay(), a, stamp.getYear());
			smallCalendar = new SmallCalendar(yearStamp);
			yearTable.add(smallCalendar,column, row);
			yearTable.setStyleClass(column,row,borderWhiteTableStyle);
			yearTable.setRowVerticalAlignment(row, "top");
			yearTable.setAlignment("center");
			
			column = column % 4 + 1;
			if (column == 1)
				row++;
		
		}		
		return yearTable;
	}
	

	public void main(IWContext iwc) throws Exception{
//		Form form = new Form();
		IWResourceBundle iwrb = getResourceBundle(iwc);	
		if (timeStamp == null) {
			String day = iwc.getParameter(CalendarParameters.PARAMETER_DAY);
			String month = iwc.getParameter(CalendarParameters.PARAMETER_MONTH);
			String year = iwc.getParameter(CalendarParameters.PARAMETER_YEAR);

			if(month == null || month.length() == 0 &&
					day == null &&
					year == null || year.length() == 0) {
				timeStamp = IWTimestamp.RightNow();
			}
			else {
				timeStamp =getTimestamp(day,month,year);
			}
			 

		}	
		User user = iwc.getCurrentUser();
		/*
		 * view is set to the current view
		 */
		if (iwc.getParameter(CalendarParameters.PARAMETER_VIEW) != null) {
			view = Integer.parseInt(iwc.getParameter(CalendarParameters.PARAMETER_VIEW));
		}
		Integer pmView = new Integer(view);
		HiddenInput hi = new HiddenInput(CalendarParameters.PARAMETER_VIEW,pmView.toString());
//		form.add(hi);
		action = iwc.getParameter(ACTION);
	
		CalendarEntryCreator creator = new CalendarEntryCreator();
		String save = iwc.getParameter(creator.saveButtonParameterName);
		if(save != null) {			
			creator.saveEntry(iwc);			
		}
		
		
		Table table = new Table();
		table.setCellspacing(0);
		table.setCellpadding(0);
		table.setWidth("100%");
		table.setHeight("100%");
		table.mergeCells(1,1,1,4);
		table.setAlignment(1,2,"center");
		table.setWidth(2,1,"8");
		table.mergeCells(2,1,2,4);
		table.setVerticalAlignment("top");
		
		table.setVerticalAlignment(3,1,"top");
		table.setStyleClass(3,1,"main");
		table.setVerticalAlignment(3,3,"top");
		table.setVerticalAlignment(3,4,"top");
		
		
//		table.setStyleClass("borderAllWhite");
		
		Table viewTable = new Table();
		
//		Link openCreator = new Link("Open Creator");
//		openCreator.setStyleClass(styledLink);
//		openCreator.addParameter(ACTION,OPEN);
//		openCreator.addParameter(CalendarParameters.PARAMETER_VIEW,view);
//		
//		Link closeCreator = new Link("Close Creator");
//		closeCreator.setStyleClass(styledLink);
//		closeCreator.addParameter(CalendarParameters.PARAMETER_VIEW,view);
////		closeCreator.addParameter(ACTION,CLOSE);
		


		/*
		 * dayView, weekView, monthView and yearView trigger a change of the calendar view
		 */ 
		Link dayView = new Link("day");
		dayView.setStyleClass(styledLinkBox);
		addLinkParameters(dayView,CalendarParameters.DAY,timeStamp);
			
		Link weekView = new Link("week");
		weekView.setStyleClass(styledLinkBox);
		addLinkParameters(weekView,CalendarParameters.WEEK,timeStamp);
		
		Link monthView = new Link("month");
		monthView.setStyleClass(styledLinkBox);
		addLinkParameters(monthView,CalendarParameters.MONTH,timeStamp);
		
		Link yearView = new Link("year");
		yearView.setStyleClass(styledLinkBox);
		addLinkParameters(yearView,CalendarParameters.YEAR,timeStamp);
		
		/*
		 * Links right and left can be used to view the next and the previous day/month/year
		 * TODO:traverse through weeks with left and right. 
		 */
		Link right = new Link(">");
		right.setStyleClass(styledLinkBox);
		right.addParameter(CalendarParameters.PARAMETER_VIEW,view);
		Link left = new Link("<");
		left.setStyleClass(styledLinkBox);
		left.addParameter(CalendarParameters.PARAMETER_VIEW,view);
		
		if(view != CalendarParameters.WEEK) {
			table.add(left,1,5);
			table.add(Text.NON_BREAKING_SPACE,1,5);
		}	
		
		switch (view) {
		case CalendarParameters.DAY :
		viewTable = dayView(iwc, timeStamp);
			addNextDayPrm(right,timeStamp);
			addLastDayPrm(left,timeStamp);
			break;
		case CalendarParameters.WEEK :
			viewTable = weekView(iwc,timeStamp);
			break;
		case CalendarParameters.MONTH :
			viewTable = monthView(iwc,timeStamp);
			addNextMonthPrm(right, timeStamp);
			addLastMonthPrm(left, timeStamp);
			break;
		case CalendarParameters.YEAR :
			viewTable = yearView(iwc,timeStamp);
			addNextYearPrm(right,timeStamp);
			addLastYearPrm(left,timeStamp);
			break;
		}
		table.add(dayView,1,5);
		table.add(Text.NON_BREAKING_SPACE,1,5);
		table.add(weekView,1,5);
		table.add(Text.NON_BREAKING_SPACE,1,5);
		table.add(monthView,1,5);
		table.add(Text.NON_BREAKING_SPACE,1,5);
		table.add(yearView,1,5);
		
		table.add(viewTable,1,1);
		
		Iterator ledgerIter = getCalBusiness(iwc).getAllLedgers().iterator();
		while(ledgerIter.hasNext()) {
			CalendarLedger ledger = (CalendarLedger) ledgerIter.next();
			Link ledgerLink =new Link(ledger.getName());
			ledgerLink.setStyleClass(styledLink);
			ledgerLink.addParameter("ledger",ledger.getPrimaryKey().toString());
			ledgerLink.setWindowToOpen(LedgerWindow.class);
			if(((Integer) user.getPrimaryKey()).intValue() == ledger.getCoachID()) {
				table.add(ledgerLink,3,1);
				table.add("<br>",3,1);
			}			
			
		}
		Text linkText = new Text(iwrb.getLocalizedString("calendarwindow.new_ledger","New Ledger"));
		Link newLedgerLink = new Link(linkText);
		newLedgerLink.setStyleClass(styledLink);
		newLedgerLink.setWindowToOpen(CreateLedgerWindow.class);
		table.add("<br>",3,1);
		table.add(newLedgerLink,3,1);
		
		table.add(creator,3,3);
		
		SmallCalendar smallCalendar = new SmallCalendar();
		table.add(smallCalendar,3,4);
		
		if(action == null) {
//			table.add(openCreator,3,1);
		}
		else {
//			table.add(closeCreator,3,1);
//			table.add(creator,3,1);
		}
	
		if(view != CalendarParameters.WEEK) {
			table.add(Text.NON_BREAKING_SPACE,1,5);
			table.add(right,1,5);					
		}	
//		form.add(table);
		add(table);
	}
	/**
	 * 
	 * @param l
	 * @param viewValue
	 * @param stamp
	 */
	public void addLinkParameters(Link l, int viewValue, IWTimestamp stamp) {
		l.addParameter(CalendarParameters.PARAMETER_VIEW, viewValue);
		l.addParameter(CalendarParameters.PARAMETER_YEAR, stamp.getYear());
		l.addParameter(CalendarParameters.PARAMETER_MONTH, stamp.getMonth());
		l.addParameter(CalendarParameters.PARAMETER_DAY, stamp.getDay());
		l.addParameter(ACTION,action);
		
		
	}
	
	/**
	 * 
	 * @param hour - the time of day when the CalenderView - day- or weekView, starts
	 */
	public void setBeginHour(int hour) {
		beginHour = hour;
	}
	/**
	 * 
	 * @param hour - the time of day when the CalendarView - day- or weekView, ends
	 */
	public void setEndHour(int hour) {
		endHour = hour;
	}	
	/**
	 * The same method as in SmallCalendar.java
	 * @param L
	 * @param idts
	 */
	public void addNextMonthPrm(Link L, IWTimestamp idts) {
		if (idts.getMonth() == 12) {
			L.addParameter(CalendarParameters.PARAMETER_MONTH, String.valueOf(1));
			L.addParameter(CalendarParameters.PARAMETER_YEAR, String.valueOf(idts.getYear() + 1));
		}
		else {
			L.addParameter(CalendarParameters.PARAMETER_MONTH, String.valueOf(idts.getMonth() + 1));
			L.addParameter(CalendarParameters.PARAMETER_YEAR, String.valueOf(idts.getYear()));
		}
	}
	/**
	 * The same method as in SmallCalendar.java
	 * @param L
	 * @param idts
	 */

	public void addLastMonthPrm(Link L, IWTimestamp idts) {
		if (idts.getMonth() == 1) {
			L.addParameter(CalendarParameters.PARAMETER_MONTH, String.valueOf(12));
			L.addParameter(CalendarParameters.PARAMETER_YEAR, String.valueOf(idts.getYear() - 1));
		}
		else {
			L.addParameter(CalendarParameters.PARAMETER_MONTH, String.valueOf(idts.getMonth() - 1));
			L.addParameter(CalendarParameters.PARAMETER_YEAR, String.valueOf(idts.getYear()));
		}
	}
	/**
	 * Adds parameters for the next day to the next day link
	 * @param L
	 * @param idts
	 */
	public void addNextDayPrm(Link L, IWTimestamp idts) {
		IWCalendar cal = new IWCalendar();
		int lastDayOfMonth = cal.getLengthOfMonth(idts.getMonth(),idts.getYear());
		
		if(idts.getDay() == lastDayOfMonth) {
			if(idts.getMonth() == 12) {
				L.addParameter(CalendarParameters.PARAMETER_YEAR, String.valueOf(idts.getYear() + 1));
				L.addParameter(CalendarParameters.PARAMETER_MONTH, String.valueOf(1));
			}
			else {
				L.addParameter(CalendarParameters.PARAMETER_YEAR, String.valueOf(idts.getYear()));
				L.addParameter(CalendarParameters.PARAMETER_MONTH, String.valueOf(idts.getMonth() + 1));
			}
			L.addParameter(CalendarParameters.PARAMETER_DAY, String.valueOf(1));
			
		}
		else {
			L.addParameter(CalendarParameters.PARAMETER_DAY, String.valueOf(idts.getDay() + 1));
			L.addParameter(CalendarParameters.PARAMETER_MONTH, String.valueOf(idts.getMonth()));
			L.addParameter(CalendarParameters.PARAMETER_YEAR, String.valueOf(idts.getYear()));
		}
	}
	/**
	 * 
	 * @param L
	 * @param idts
	 */
	public void addLastDayPrm(Link L, IWTimestamp idts) {
		IWCalendar cal = new IWCalendar();
		int lastDayOfPreviousMonthThisYear = cal.getLengthOfMonth(idts.getMonth() - 1,idts.getYear());
		int lastDayOfPreviousMonthLastYear = cal.getLengthOfMonth(idts.getMonth() - 1,idts.getYear() - 1);
		
		if(idts.getDay() == 1) {
			if(idts.getMonth() == 1) {
				L.addParameter(CalendarParameters.PARAMETER_YEAR, String.valueOf(idts.getYear() - 1));
				L.addParameter(CalendarParameters.PARAMETER_DAY, String.valueOf(lastDayOfPreviousMonthLastYear));
				L.addParameter(CalendarParameters.PARAMETER_MONTH, String.valueOf(12));
			}
			else {
				L.addParameter(CalendarParameters.PARAMETER_YEAR, String.valueOf(idts.getYear()));
				L.addParameter(CalendarParameters.PARAMETER_DAY, String.valueOf(lastDayOfPreviousMonthThisYear));
				L.addParameter(CalendarParameters.PARAMETER_MONTH, String.valueOf(idts.getMonth() - 1));
			}
			
		}
		else {
			L.addParameter(CalendarParameters.PARAMETER_DAY, String.valueOf(idts.getDay() - 1));
			L.addParameter(CalendarParameters.PARAMETER_MONTH, String.valueOf(idts.getMonth()));
			L.addParameter(CalendarParameters.PARAMETER_YEAR, String.valueOf(idts.getYear()));
			
		}
	}
	public void addNextYearPrm(Link L, IWTimestamp idts) {
		L.addParameter(CalendarParameters.PARAMETER_MONTH, String.valueOf(idts.getMonth()));
		L.addParameter(CalendarParameters.PARAMETER_YEAR, String.valueOf(idts.getYear() + 1));
	}
	public void addLastYearPrm(Link L, IWTimestamp idts) {
		L.addParameter(CalendarParameters.PARAMETER_MONTH, String.valueOf(idts.getMonth()));
		L.addParameter(CalendarParameters.PARAMETER_YEAR, String.valueOf(idts.getYear() - 1));
	}
	public static IWTimestamp getTimestamp(String day, String month, String year) {
		IWTimestamp stamp = new IWTimestamp();

		if (day != null) {
			stamp.setDay(Integer.parseInt(day));
		}
		// removed dubius behavior A
		/*else {
		 stamp.setDay(1);
		 }
		 */
		if (month != null) {
			stamp.setMonth(Integer.parseInt(month));
		}
		if (year != null) {
			stamp.setYear(Integer.parseInt(year));
		}

		stamp.setHour(0);
		stamp.setMinute(0);
		stamp.setSecond(0);

		return stamp;
	}	
	

	public CalBusiness getCalBusiness(IWApplicationContext iwc) {
		if (calBiz == null) {
			try {
				calBiz = (CalBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, CalBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return calBiz;
	}
	

}
