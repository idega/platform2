/*
 * Created on Jan 16, 2004
 */
package com.idega.block.cal.presentation;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import com.idega.block.cal.business.CalBusiness;
import com.idega.block.cal.data.CalendarEntry;
import com.idega.block.cal.data.CalendarLedger;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.CalendarParameters;
import com.idega.idegaweb.presentation.SmallCalendar;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.Page;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:birna@idega.is">Birna Iris Jonsdottir</a>
 */
public class CalendarView extends Block{
	
	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.cal";
	private int view = CalendarParameters.MONTH;
	
	private final static String PARAMETER_ISI_GROUP_ID = "group_id";
	
	
	private IWTimestamp now = null;
	private IWTimestamp timeStamp = null;
	private IWCalendar cal = null;
	private int beginHour = 6;
	private int endHour = 24;
	private String borderWhiteTableStyle = "borderAllWhite";
	private String mainTableStyle = "main";
	private String menuTableStyle = "menu";
	private String borderLeftTopRight = "borderLeftTopRight";
	private String styledLink = "styledLink";
	private String entryLink = "entryLink";
	private String entryLinkActive = "entryLinkActive";
	private String bold = "bold";
	private String headline ="headline";
	private String ledgerListStyle = "ledgerList";
	public static String ACTION = "action";
	public static String OPEN = "open";
	public static String CLOSE = "close";
	
	private String action = null;
	
	private CalBusiness calBiz;
	private int userID = -1;
	private int groupID = -2;
	private boolean isPrintable = false;


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
		
		Link right = getRightLink(iwc);
		Link left = getLeftLink(iwc);
		addNextDayPrm(right,timeStamp);
		addLastDayPrm(left,timeStamp);
		
		Table backTable = new Table();
		backTable.setColor("#cccccc");
		backTable.setCellspacing(1);
		backTable.setCellpadding(0);	
		backTable.setWidth(500);
		backTable.setHeight(400);
		
		
		backTable.mergeCells(1,1,2,1);
		String dateString = stamp.getDateString("dd MMMMMMMM, yyyy",iwc.getCurrentLocale());
		Table headTable = new Table();
		headTable.setCellpadding(0);
		headTable.setCellspacing(0);
		headTable.setWidth("100%");
		headTable.setHeight("100%");
		headTable.setStyleClass(mainTableStyle);
		headTable.setVerticalAlignment(1,1,"top");
		headTable.add(left,1,1);
		headTable.add(Text.NON_BREAKING_SPACE,1,1);
		headTable.add(dateString,1,1);
		headTable.add(Text.NON_BREAKING_SPACE,1,1);
		headTable.add(right,1,1);
		headTable.setAlignment(2,1,"right");
		headTable.add(getIconTable(iwc),2,1);
		backTable.add(headTable,1,1);
		
		//the outer for-loop goes through the hours and prints out
		//the style for each cell,
		//the entrylist for each hour
		for(int i=beginHour;i<=endHour;i++) {
			backTable.setHeight(1,row,"40");
			backTable.setHeight(2,row,"40");
			
			Table dayTable = new Table();
			dayTable.setCellspacing(0);
			dayTable.setCellpadding(1);
			dayTable.setWidth("100%");
			dayTable.setHeight("100%");
			Table entryTable = new Table();
			entryTable.setCellspacing(0);
			entryTable.setCellpadding(1);
			entryTable.setWidth("100%");
			entryTable.setHeight("100%");
			entryTable.setColor(1,1,"#ffffff");
			now.setTime(i,0,0);
			dayTable.add(now.getTime().toString(),1,1);
			dayTable.setColor(1,1,"#ffffff");
			backTable.add(dayTable,1,row);
			backTable.setWidth(1,row,"20%");
			Timestamp fromStamp = Timestamp.valueOf(stamp.getDateString("yyyy-MM-dd hh:mm:ss.S")); 
			fromStamp.setHours(beginHour);
			fromStamp.setMinutes(0);
			fromStamp.setNanos(0);
			Timestamp toStamp =Timestamp.valueOf(stamp.getDateString("yyyy-MM-dd hh:mm:ss.S"));
			toStamp.setHours(endHour);
			toStamp.setMinutes(0);
			toStamp.setNanos(0);			
			List listOfEntries = (List) getCalBusiness(iwc).getEntriesBetweenTimestamps(fromStamp,toStamp);
			User user = null;
			Integer userID = null;
			if(iwc.isLoggedOn()) {
				user = iwc.getCurrentUser();
				userID = (Integer) user.getPrimaryKey();
			}
			//the inner for-loop goes through the list of entries and prints them out as a link
			//the link opens the view for the entry
			for(int j=0; j<listOfEntries.size(); j++) {
				CalendarEntry entry = (CalendarEntry) listOfEntries.get(j);
				CalendarLedger ledger = null;
				int groupIDInLedger = -1;
				boolean isInGroup = false;
				//get a collection of groups the current user may view
				
				Collection viewGroups = null;
				if(user != null) {
					try {
						viewGroups = getUserBusiness(iwc).getUserGroups(user);
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
				
				if(entry.getLedgerID() != -1) {
					ledger = getCalBusiness(iwc).getLedger(entry.getLedgerID());
					if(ledger != null) {
						groupIDInLedger = ledger.getGroupID();
					}
										
				}
				else {
					groupIDInLedger = -1;
				}
				if(viewGroups != null) {
					Iterator viewGroupsIter = viewGroups.iterator();
					//goes through the groups the user may view and prints out the entry if 
					//the group connected to the entry is the same as the group the user may view
					while(viewGroupsIter.hasNext()) {
						Group group =(Group) viewGroupsIter.next();
						Integer groupID = (Integer) group.getPrimaryKey();
						if(entry.getGroupID() == groupID.intValue()  
								|| groupIDInLedger == groupID.intValue()) {
							isInGroup = true;
						}
					}
				}
				if(groupIDInLedger == getViewGroupID()) {
					isInGroup = true;
				}

				if(isInGroup || iwc.isSuperAdmin() || 
						getViewGroupID() == entry.getGroupID() ||
						userID.intValue() == entry.getUserID()) {
					Timestamp fStamp = entry.getDate();
					Timestamp tStamp = entry.getEndDate();
					//i is the current hour 
					if(i <= tStamp.getHours() && i >= fStamp.getHours()) {
						entry.getGroupID();
						String headline = getEntryHeadline(entry);
						Link headlineLink = new Link(headline);
						headlineLink.addParameter(ACTION,OPEN);
						headlineLink.addParameter(CalendarParameters.PARAMETER_VIEW,view);
						headlineLink.addParameter(CalendarParameters.PARAMETER_YEAR, stamp.getYear());
						headlineLink.addParameter(CalendarParameters.PARAMETER_MONTH, stamp.getMonth());
						headlineLink.addParameter(CalendarParameters.PARAMETER_DAY, stamp.getDay());
						headlineLink.addParameter("entryID", entry.getPrimaryKey().toString());
						if(!iwc.getAccessController().hasRole("cal_view_entry_creator",iwc) || isPrintable) {
							headlineLink.setWindowToOpen(EntryInfoWindow.class);
						}
						headlineLink.setStyleClass(entryLink);
						entryTable.add(headlineLink,1,1);
						entryTable.add("<br>",1,1);
					}						
				}
							
			}	
			backTable.add(entryTable,2,row);
			row++;
		}
		return backTable;
	}
	/** 
	 * draws the week view of the CalendarView
	 * By default the week view varies from 8:00 until 17:00
	 * but can be changed via setBeginHour(int hour) and setEndHour(int hour)
	 */
	
	public Table weekView(IWContext iwc,IWTimestamp stamp) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		int row = 1;
		int column = 1;
		int day = 0;
		/*
		 * now holds the current time
		 */
		now = new IWTimestamp();
//		Link right = getRightLink(iwc);
//		Link left = getLeftLink(iwc);
//		addNextWeekPrm(right,timeStamp);
//		addLastWeekPrm(left,timeStamp);
		

		Table backTable = new Table();
		backTable.setColor("#cccccc");
		backTable.setCellspacing(1);
		backTable.setCellpadding(0);	
		backTable.setWidth(500);
		backTable.setHeight(400);
		
		/*
		 * timeStamp is used to traverse trough the week
		 */
//		IWTimestamp tStamp = new IWTimestamp();
		cal = new IWCalendar();
		Text nameOfDay = new Text();
		Table headTable = new Table();
		headTable.setWidth("100%");
		headTable.setHeight("100%");
		headTable.setCellpadding(0);
		headTable.setCellspacing(0);
		headTable.setStyleClass(mainTableStyle);
		headTable.setAlignment(2,1,"right");
		String yearString = stamp.getDateString("yyyy");
//		headTable.add(left,1,1);
		headTable.add(iwrb.getLocalizedString("calView.week_of_year","Week of the year") + " " + stamp.getWeekOfYear() + "<br>" + yearString,1,1);
//		headTable.add(right,1,1);
		headTable.add(getIconTable(iwc),2,1);
		backTable.mergeCells(1,1,8,1);
		backTable.add(headTable,1,1);
		int weekdays = 8;
		int weekday = cal.getDayOfWeek(now.getYear(),now.getMonth(),now.getDay());
		
		int today = stamp.getDay();
		/*
		 * The outer for-loop runs through the columns of the weekTable
		 * the weekdays
		 */
		for(int i=0;i<weekdays; i++) {
			
			row = 2;
			/*
			 * The inner for-loop runs through the rows of the weekTable
			 * the hours
			 */
			for(int j=beginHour;j<=endHour;j++) {
				Table weekTable = new Table();
				weekTable.setWidth("100%");
				weekTable.setHeight("100%");
				weekTable.setColor("#ffffff");
				weekTable.setCellpadding(1);
				weekTable.setCellspacing(0);
				
				if(column == 1 && row != 2) {
					stamp.setTime(j,0,0);
					weekTable.add(stamp.getTime().toString(),1,1);
					backTable.add(weekTable,column,row);
				}
				else if(row == 2 && column != 1) {
					if(i==weekday) {
						weekTable.setColor("#e9e9e9");
					}
					nameOfDay = new Text(cal.getDayName(i, iwc.getCurrentLocale(), IWCalendar.LONG).toString());					
					
					if(i < weekday) {
						if(today == 1) {
							int lengthOfPreviousMonth = 0;							
							if(stamp.getMonth() == 1) {
								/*
								 * if January the lengthOfPreviousMonth is the length of Desember the year before
								 */
								lengthOfPreviousMonth = cal.getLengthOfMonth(12,stamp.getYear()-1);
							}
							else {
								/*
								 * else lengthOfPreviousMonth is the length of the month before in the current year
								 */
								lengthOfPreviousMonth = cal.getLengthOfMonth(stamp.getMonth()-1,stamp.getYear());
							}
							day = (today + lengthOfPreviousMonth) - (weekday - i);							
						}
						else {
							day = today - (weekday - i);
						}						
						stamp.setDay(day);
					}// end if
					weekTable.add(nameOfDay,1,1);
					weekTable.add("-" + stamp.getDateString("dd.MM"),1,1);
					backTable.add(weekTable,column,row);
					
					stamp.addDays(1);					
				}// end else if
				else {
//					weekTable.setStyleClass(column,row,borderWhiteTableStyle);
					Timestamp fromStamp = Timestamp.valueOf(stamp.getDateString("yyyy-MM-dd hh:mm:ss.S")); 
					fromStamp.setHours(beginHour);
					fromStamp.setMinutes(0);
					fromStamp.setNanos(0);
					Timestamp toStamp =Timestamp.valueOf(stamp.getDateString("yyyy-MM-dd hh:mm:ss.S"));
					toStamp.setHours(endHour);
					toStamp.setMinutes(0);
					toStamp.setNanos(0);			
					List listOfEntries = (List) getCalBusiness(iwc).getEntriesBetweenTimestamps(fromStamp,toStamp);	
					User user = null;
					Integer userID = null;
					if(iwc.isLoggedOn()) {
						user = iwc.getCurrentUser();
						userID = (Integer) user.getPrimaryKey();
					}
					for(int h=0; h<listOfEntries.size(); h++) {
						
						CalendarEntry entry = (CalendarEntry) listOfEntries.get(h);
						Collection viewGroups = null;
						CalendarLedger ledger = null;
						boolean isInGroup = false;
						int groupIDInLedger = -1;
						if(user != null) {
							try {
								viewGroups = getUserBusiness(iwc).getUserGroups(user);
							}catch(Exception e) {
								e.printStackTrace();
							}
						}
						
						if(entry.getLedgerID() != -1) {
							ledger = getCalBusiness(iwc).getLedger(entry.getLedgerID());
							if(ledger != null) {
								groupIDInLedger = ledger.getGroupID();
							}
												
						}
						else {
							groupIDInLedger = 1;
						}
						if(viewGroups != null) {
							Iterator viewGroupsIter = viewGroups.iterator();
							//goes through the groups the user may view and prints out the entry if 
							//the group connected to the entry is the same as the group the user may view
							while(viewGroupsIter.hasNext()) {
								Group group =(Group) viewGroupsIter.next();
								Integer groupID = (Integer) group.getPrimaryKey();
								if(entry.getGroupID() == groupID.intValue() 
										|| groupIDInLedger == groupID.intValue()) {
									isInGroup = true;
								}
							}
						}
						if(groupIDInLedger == getViewGroupID()) {
							isInGroup = true;
						}
						
						if(isInGroup || iwc.isSuperAdmin() || 
								getViewGroupID() == entry.getGroupID() ||
								userID.intValue() == entry.getUserID()) {
							Timestamp fStamp = entry.getDate();
							Timestamp ttStamp = entry.getEndDate();
							if(j <= ttStamp.getHours() && j >= fStamp.getHours()) {
								String headline = getEntryHeadline(entry);
								Link headlineLink = new Link(headline);
								headlineLink.addParameter(ACTION,OPEN);
								headlineLink.addParameter(CalendarParameters.PARAMETER_YEAR, stamp.getYear());
								headlineLink.addParameter(CalendarParameters.PARAMETER_MONTH, stamp.getMonth());
								headlineLink.addParameter(CalendarParameters.PARAMETER_DAY, stamp.getDay());
								headlineLink.addParameter("entryID", entry.getPrimaryKey().toString());
								if(!iwc.getAccessController().hasRole("cal_view_entry_creator",iwc) || isPrintable) {
									headlineLink.setWindowToOpen(EntryInfoWindow.class);
								}									
								headlineLink.setStyleClass(entryLink);
								headlineLink.addParameter(CalendarParameters.PARAMETER_VIEW,view);
								weekTable.add(headlineLink,1,1);
								weekTable.add("<br>",1,1);
//									backTable.add(weekTable,column,row);
							}
						}							
					}	//end for	
					backTable.setHeight(column,row,"40");
					backTable.add(weekTable,column,row);
				}
				row++;
			} //end inner for			
			column++;
		}//end outer for
		return backTable;
	}
	/**
	 * 
	 * @return a table displaying the days of the current month
	 */
	
	public Table monthView(IWContext iwc, IWTimestamp stamp) {	
		/*
		 * now holds the current time
		 */
		now = IWTimestamp.RightNow();
		cal = new IWCalendar();
		Text nameOfDay = new Text();
		Text t = new Text();

		Link right = getRightLink(iwc);
		Link left = getLeftLink(iwc);
		addNextMonthPrm(right, timeStamp);
		addLastMonthPrm(left, timeStamp);
		
		
		int weekdays = 8;
		int daycount = cal.getLengthOfMonth(stamp.getMonth(),stamp.getYear());
		int firstWeekDayOfMonth = cal.getDayOfWeek(stamp.getYear(),stamp.getMonth(),1);
//		int dayOfMonth = cal.getDay();
		int row = 3;
		int column = firstWeekDayOfMonth;
		int n = 1;
		
		Table backTable = new Table();
		backTable.setColor("#cccccc");
		backTable.setCellspacing(1);
		backTable.setCellpadding(0);
		backTable.setWidth(500);
		backTable.setHeight(400);
		
		String dateString = stamp.getDateString("MMMMMMMM, yyyy",iwc.getCurrentLocale());
		backTable.mergeCells(1,1,7,1);
		Table headTable = new Table();
		headTable.setCellspacing(0);
		headTable.setCellpadding(1);
		headTable.setWidth("100%");
		headTable.setHeight("100%");
		headTable.setStyleClass(mainTableStyle);
		headTable.setVerticalAlignment(1,1,"top");
		headTable.add(left,1,1);
		headTable.add(Text.NON_BREAKING_SPACE,1,1);
		headTable.add(dateString,1,1);
		headTable.add(Text.NON_BREAKING_SPACE,1,1);
		headTable.add(right,1,1);
		headTable.setAlignment(2,1,"right");
		headTable.add(getIconTable(iwc),2,1);
		backTable.add(headTable,1,1);
		
		
		int weekday = 1;
		for(int i=1; i<weekdays; i++) {
			weekday = i % 7;
			if (weekday == 0)
				weekday = 7;
			nameOfDay = new Text(cal.getDayName(weekday, iwc.getCurrentLocale(), IWCalendar.LONG).toString().toLowerCase());
			Table nameOfDayTable = new Table();
			nameOfDayTable.setCellspacing(0);
			nameOfDayTable.setCellpadding(1);
			nameOfDayTable.setWidth("100%");
			nameOfDayTable.setHeight("100%");
			nameOfDayTable.setColor("#ffffff");
			nameOfDayTable.add(nameOfDay,1,1);
			backTable.add(nameOfDayTable,i,2);
			backTable.setWidth(i,2,"200");
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

			backTable.setWidth(column,row,"200");
			backTable.setHeight(column,row,"105");
			if(n == now.getDay()) {
				dayCell.setColor("#e9e9e9");
			}
			else {
				dayCell.setColor("#ffffff");
			}
			Link dayLink = new Link(t);
			dayLink.setStyleClass(styledLink);
			dayLink.addParameter(CalendarParameters.PARAMETER_VIEW,CalendarParameters.DAY);
			dayLink.addParameter(CalendarParameters.PARAMETER_YEAR, stamp.getYear());
			dayLink.addParameter(CalendarParameters.PARAMETER_MONTH, stamp.getMonth());
			dayLink.addParameter(CalendarParameters.PARAMETER_DAY, n);
			if(groupID != -2) {
				dayLink.addParameter(PARAMETER_ISI_GROUP_ID,groupID);
			}
			dayCell.add(dayLink,1,1);
			dayCell.setHeight(1,1,"12");
			dayCell.add("<br>",1,1);
			dayCell.setAlignment(1,1,"right");
			Timestamp fromStamp = Timestamp.valueOf(stamp.getDateString("yyyy-MM-dd hh:mm:ss.S"));
			fromStamp.setDate(n);
			fromStamp.setHours(0);
			fromStamp.setMinutes(0);
			fromStamp.setNanos(0);	
			Timestamp toStamp = Timestamp.valueOf(stamp.getDateString("yyyy-MM-dd hh:mm:ss.S"));
			toStamp.setDate(n);
			toStamp.setHours(23);
			toStamp.setMinutes(59);
			toStamp.setNanos(0);
			List listOfEntries = (List) getCalBusiness(iwc).getEntriesBetweenTimestamps(fromStamp,toStamp);
			Collections.sort(listOfEntries,new Comparator() {
				public int compare(Object arg0, Object arg1) {
					return ((CalendarEntry) arg0).getDate().compareTo(((CalendarEntry) arg1).getDate());
				}				
			});
			User user = null;
			Integer userID = null;
			if(iwc.isLoggedOn()) {
				user = iwc.getCurrentUser();
				userID = (Integer) user.getPrimaryKey();
			}
			else {
				userID = new Integer(-2);
			}
			for(int h=0; h<listOfEntries.size(); h++) {
				CalendarEntry entry = (CalendarEntry) listOfEntries.get(h);
				CalendarLedger ledger = null;
				int groupIDInLedger = 0;
				int coachGroupIDInLedger = 0;
				boolean isInGroup = false;
				Collection viewGroups = null;
				
				if(user != null) {
					try {
						viewGroups = getUserBusiness(iwc).getUserGroups(user);
						
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
				
				
				if(entry.getLedgerID() != -1) {
					ledger = getCalBusiness(iwc).getLedger(entry.getLedgerID());
					if(ledger != null) {
						groupIDInLedger = ledger.getGroupID();
						coachGroupIDInLedger = ledger.getCoachGroupID();
					}

				}
				else {
					groupIDInLedger = -1;
					coachGroupIDInLedger = -1;
				}
				if(viewGroups != null) {
					Iterator viewGroupsIter = viewGroups.iterator();
					//goes through the groups the user may view and prints out the entry if 
					//the group connected to the entry is the same as the group the user may view
					while(viewGroupsIter.hasNext()) {
						Group group =(Group) viewGroupsIter.next();
						Integer groupID = (Integer) group.getPrimaryKey();
						
						if(entry.getGroupID() == groupID.intValue() 
								|| groupIDInLedger == groupID.intValue()){
							isInGroup = true;
						}
					}
				}
				if(groupIDInLedger == getViewGroupID()) {
					isInGroup = true;
				}
				if(coachGroupIDInLedger == getViewGroupID()) {
					isInGroup = true;
				}
				if(isInGroup || iwc.isSuperAdmin() || 
						getViewGroupID() == entry.getGroupID() ||
						userID.intValue() == entry.getUserID()) {
					String headline = getEntryHeadline(entry);
					Link headlineLink = new Link(headline);
					headlineLink.addParameter(ACTION,OPEN);
					headlineLink.addParameter(CalendarParameters.PARAMETER_VIEW,view);
					headlineLink.addParameter(CalendarParameters.PARAMETER_YEAR, stamp.getYear());
					headlineLink.addParameter(CalendarParameters.PARAMETER_MONTH, stamp.getMonth());
					headlineLink.addParameter(CalendarParameters.PARAMETER_DAY, stamp.getDay());
					headlineLink.addParameter(CalendarEntryCreator.entryIDParameterName, entry.getPrimaryKey().toString());
					if(!iwc.getAccessController().hasRole("cal_view_entry_creator",iwc) || isPrintable) {
						headlineLink.setWindowToOpen(EntryInfoWindow.class);
					}
					headlineLink.setStyleClass(entryLink);
					
					//TODO: something - the headlineLink is entryLinkActive even though calentrycreator is empty
					String e = iwc.getParameter(CalendarEntryCreator.entryIDParameterName);
					CalendarEntry ent = null;
					if(e == null || e.equals("")) {
						e = "";
					}
					else {
						try {
							ent = getCalBusiness(iwc).getEntry(Integer.parseInt(e));
						}catch(Exception ex) {
							ent = null;
						}
						
					}
					if(ent != null) {
						if(ent.getPrimaryKey().equals(entry.getPrimaryKey())) {
							headlineLink.setStyleClass(entryLinkActive);
						}
					}
					dayCell.add(headlineLink,1,cellRow);
					dayCell.setVerticalAlignment(1,cellRow,"top");
					dayCell.add("<br>",1,cellRow++);						
				}
				
			}
			backTable.add(dayCell,column,row);
			backTable.setColor(column,row,"#ffffff");
			backTable.add(Text.NON_BREAKING_SPACE,column,row);
			backTable.setVerticalAlignment(column,row,"top");
			column = column % 7 + 1;
			if (column == 1)
				row++;
			n++;
			
		}	
		return backTable;
	}
	
	/**
	 * @param entry
	 * @return
	 */
	private String getEntryHeadline(CalendarEntry entry) {
		Timestamp startDate = entry.getDate();
		int startHours = startDate.getHours();
		int startMinutes = startDate.getMinutes();
		Timestamp endDate = entry.getEndDate();
		int endHours = endDate.getHours();
		int endMinutes = endDate.getMinutes();
		
		String headline = getTimeString(startHours,startMinutes) + 
		"-" + getTimeString(endHours,endMinutes) + 
		" " + entry.getName();
		return headline;
	}
	/**
	 * 
	 * @param hours
	 * @param minutes
	 * @return
	 */
	private String getTimeString(int hours, int minutes) {
		String timeString;
		String mi;
		if(minutes<10) {
			mi = "0"+minutes;
		}
		else {
			mi = ""+minutes;
		}
		timeString = hours + ":" + mi;
		return timeString;
		
	}
	public Table yearView(IWContext iwc, IWTimestamp stamp) {
		now = new IWTimestamp(); 
		
		Link right = getRightLink(iwc);
		Link left = getLeftLink(iwc);
		addNextYearPrm(right,timeStamp);
		addLastYearPrm(left,timeStamp);
		
		
		Table yearTable = new Table();
		Table headTable = new Table();
		headTable.setWidth("100%");
		headTable.setHeight("100%");
		headTable.setStyleClass(mainTableStyle);
		headTable.setCellpadding(0);
		headTable.setCellspacing(0);
		headTable.setVerticalAlignment(1,1,"top");
		headTable.add(left,1,1);
		headTable.add(Text.NON_BREAKING_SPACE,1,1);
		headTable.add("" + stamp.getYear(),1,1);
		headTable.add(Text.NON_BREAKING_SPACE,1,1);
		headTable.add(right,1,1);
		headTable.setAlignment(2,1,"right");
		headTable.add(getIconTable(iwc),2,1);
		yearTable.mergeCells(1,1,4,1);
		yearTable.add(headTable,1,1);
		IWTimestamp yearStamp = null;
		SmallCalendar smallCalendar = null;
		int row = 2;
		int column = 1;

		for (int a = 1; a <= 12; a++) {
			yearStamp = new IWTimestamp(stamp.getDay(), a, stamp.getYear());
			smallCalendar = new SmallCalendar(yearStamp);
			smallCalendar.setDaysAsLink(true);
			smallCalendar.addParameterToLink(CalendarParameters.PARAMETER_VIEW,CalendarParameters.DAY);
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
	
	public Table getIconTable(IWContext iwc) {
		Table iconTable = new Table();
		/*
		 * dayView, weekView, monthView and yearView trigger a change of the calendar view
		 */ 
		IWBundle iwb = getBundle(iwc);		
		Image day_on = iwb.getImage("day_on_blue.gif");
		Link dayView = new Link();
		addLinkParameters(dayView,CalendarParameters.DAY,timeStamp);
		dayView.setImage(day_on);
		
/*		Image week_on = iwb.getImage("week_on_blue.gif");
		Link weekView = new Link();
		addLinkParameters(weekView,CalendarParameters.WEEK,timeStamp);
		weekView.setImage(week_on);
*/		
		Image month_on = iwb.getImage("month_on_blue.gif");
		Link monthView = new Link();
		addLinkParameters(monthView,CalendarParameters.MONTH,timeStamp);
		monthView.setImage(month_on);
		
		Image year_on = iwb.getImage("year_on_blue.gif"); 
		Link yearView = new Link();
		addLinkParameters(yearView,CalendarParameters.YEAR,timeStamp);
		yearView.setImage(year_on);
		
		iconTable.add(dayView,1,1);
//		iconTable.add(weekView,2,1);
		iconTable.add(monthView,3,1);
		iconTable.add(yearView,4,1);
		
		return iconTable;
	}
	public Link getRightLink(IWContext iwc) {
		IWBundle iwb = getBundle(iwc);
		Image rightArrow = iwb.getImage("right_arrow.gif");
		Link right = new Link();
		right.setImage(rightArrow);
		right.addParameter(CalendarParameters.PARAMETER_VIEW,view);
		return right;		
	}
	public Link getLeftLink(IWContext iwc) {
		IWBundle iwb = getBundle(iwc);
		Image leftArrow = iwb.getImage("left_arrow.gif");
		Link left = new Link();
		left.setImage(leftArrow);
		left.addParameter(CalendarParameters.PARAMETER_VIEW,view);
		return left;
	}
	

	public void main(IWContext iwc) throws Exception{
		IWResourceBundle iwrb = getResourceBundle(iwc);
	
		Page parentPage = this.getParentPage();
		String styleSrc = iwc.getIWMainApplication().getBundle(getBundleIdentifier()).getResourcesURL();
		String styleSheetName = "CalStyle.css"; 
		styleSrc = styleSrc + "/" + styleSheetName;
		parentPage.addStyleSheetURL(styleSrc);
		
		if (timeStamp == null) {
			String day = iwc.getParameter(CalendarParameters.PARAMETER_DAY);
			String month = iwc.getParameter(CalendarParameters.PARAMETER_MONTH);
			String year = iwc.getParameter(CalendarParameters.PARAMETER_YEAR);

			if(month != null && !month.equals("") &&
					day != null && !day.equals("") &&
					year != null && !year.equals("")) {
				timeStamp = getTimestamp(day,month,year);
			}
			else {
				timeStamp = IWTimestamp.RightNow();
			}
		}	
		
		CalendarEntryCreator creator = new CalendarEntryCreator();
		
		String save = iwc.getParameter(creator.saveButtonParameterName);
		if(save != null) {
			creator.saveEntry(iwc,parentPage);
		}
		
		/*
		 * view is set to the current view
		 */
		String viewString = iwc.getParameter(CalendarParameters.PARAMETER_VIEW);
		if (viewString != null && !viewString.equals("")) {
			view = Integer.parseInt(viewString);
		}
		action = iwc.getParameter(ACTION);
		Table table = new Table();
		table.setCellspacing(0);
		table.setCellpadding(0);
		table.setHeight("100%");
		table.mergeCells(1,1,1,2);
		table.setAlignment(1,2,"center");
		table.setWidth(2,1,"8");
		table.mergeCells(2,1,2,2);
		table.setVerticalAlignment("top");
		
		table.setVerticalAlignment(3,1,"top");
		table.setVerticalAlignment(3,3,"top");
		table.setVerticalAlignment(3,5,"top");
		
		User user = null;
		if(iwc.isLoggedOn()) {
			user = iwc.getCurrentUser();
		}

		

		
		Table viewTable = new Table();

		switch (view) {
		case CalendarParameters.DAY :
			viewTable = dayView(iwc, timeStamp);
			break;
		case CalendarParameters.WEEK :
			viewTable = weekView(iwc,timeStamp);
			break;
		case CalendarParameters.MONTH :
			viewTable = monthView(iwc,timeStamp);
			break;
		case CalendarParameters.YEAR :
			viewTable = yearView(iwc,timeStamp);
			break;
		}
		table.setVerticalAlignment(1,1,Table.VERTICAL_ALIGN_TOP);		
		table.add(viewTable,1,1);
//		table.setBorder(1);
		
		if(iwc.getAccessController().hasRole("cal_view_entry_creator",iwc) && !isPrintable) { 
			table.setWidth(800);
			Table headlineTable = new Table();
			headlineTable.setCellpadding(0);
			headlineTable.setCellspacing(0);
			headlineTable.setStyleClass(borderLeftTopRight);
			headlineTable.setStyleClass(1,1,menuTableStyle);
			headlineTable.setWidth(400);			
			Text ledgerText = new Text(iwrb.getLocalizedString("calendarView.ledgers", "Ledgers"));
			ledgerText.setStyleClass(headline);
			ledgerText.setBold();
			headlineTable.setAlignment(1,1,Table.HORIZONTAL_ALIGN_CENTER);
			headlineTable.add(ledgerText,1,1);
			table.setHeight(3,1,200);
			table.add(headlineTable,3,1);
			
			int row = 2;
			Table ledgerTable = new Table();
			ledgerTable.setCellpadding(2);
			ledgerTable.setCellspacing(0);
			
			Text linkText = new Text(iwrb.getLocalizedString("calendarwindow.new_ledger","New Ledger"));
			Link newLedgerLink = new Link(linkText);
			newLedgerLink.setWindowToOpen(CreateLedgerWindow.class);
			newLedgerLink.setAsImageButton(true,true);
		
			ledgerTable.setAlignment(1,1,Table.HORIZONTAL_ALIGN_RIGHT);
			ledgerTable.add(newLedgerLink,1,1);
			
			Iterator ledgerIter = getCalBusiness(iwc).getAllLedgers().iterator();
			while(ledgerIter.hasNext()) {
				CalendarLedger ledger = (CalendarLedger) ledgerIter.next();
				Link ledgerLink =new Link(ledger.getName());
				ledgerLink.setStyleClass(styledLink);
				ledgerLink.addParameter(LedgerWindow.LEDGER,ledger.getPrimaryKey().toString());
				ledgerLink.addParameter(CalendarParameters.PARAMETER_DAY,timeStamp.getDay());
				ledgerLink.addParameter(CalendarParameters.PARAMETER_MONTH,timeStamp.getMonth());
				ledgerLink.addParameter(CalendarParameters.PARAMETER_YEAR,timeStamp.getYear());
				ledgerLink.setWindowToOpen(LedgerWindow.class);
				if(user != null) {
					if(((Integer) user.getPrimaryKey()).intValue() == ledger.getCoachID() || user.getPrimaryGroupID() == ledger.getCoachGroupID()) {						
						ledgerTable.add(" - ",1,row);
						ledgerTable.add(ledgerLink,1,row++);
					}			
				}
				
			}
			
						
			Layer layer = new Layer(Layer.DIV);
			layer.setStyleClass(ledgerListStyle);
			layer.add(ledgerTable);
			
			table.add(layer,3,1);
			
			table.setVerticalAlignment(3,2,Table.VERTICAL_ALIGN_TOP);
			table.add(Text.BREAK,3,2);
			table.add(creator,3,2);	
		}	
		else {
			table.setWidth(500);
		}
		if(!isPrintable) {
			Link printLink = new Link(iwrb.getLocalizedString("calendarwindow.printable_cal","Printerfriendly Calendar"));
			printLink.setWindowToOpen(PrintableCalendarView.class);
			printLink.setStyleClass(styledLink);
			table.add(printLink,1,3);
		}
		add(table);
	}
	/**
	 * 
	 * @param l
	 * @param viewValue
	 * @param stamp
	 */
	public void addLinkParameters(Link l, int viewValue, IWTimestamp stamp) {
		if(groupID != -2) {
			l.addParameter("group_id",groupID);
		}		
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
	public void setViewInGroupID(int id) {
		groupID = id;
	}
	public void setViewInUserID(int id) {
		userID = id;
	}
	public int getViewGroupID() {
		return groupID;
	}
	public int getViewUserID() {
		return userID;
	}
	public void setPrintableVersion(boolean isPrintable) {
		this.isPrintable = isPrintable;
	}
	/**
	 * Adds parameters for the next day to the next day link
	 * @param L
	 * @param idts
	 */
	public void addNextDayPrm(Link L, IWTimestamp idts) {
		if(groupID != -2) {
			L.addParameter(PARAMETER_ISI_GROUP_ID,groupID);
		}
		
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
		if(groupID != -2) {
			L.addParameter(PARAMETER_ISI_GROUP_ID,groupID);
		}
		
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
	public void addNextWeekPrm(Link L, IWTimestamp idts) {
		GregorianCalendar calendar = new GregorianCalendar(idts.getYear(),idts.getMonth(),idts.getDay());
		Timestamp ts = idts.getTimestamp();
		calendar.add(calendar.DAY_OF_MONTH,6);
		if(calendar.get(calendar.DAY_OF_MONTH) < idts.getDay()) {
			calendar.add(calendar.MONTH,1);
			if(calendar.get(calendar.MONTH) < idts.getMonth()) {
				calendar.add(calendar.YEAR,1);
			}
		}
		Date sd = calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.S");
		String f = format.format(sd);		
		ts = Timestamp.valueOf(f);
//		int year = sd.getYear() + 1900;
//		ts.setYear(year);	
		idts = new IWTimestamp(ts);
		L.addParameter(CalendarParameters.PARAMETER_DAY,String.valueOf(idts.getDay()));
		L.addParameter(CalendarParameters.PARAMETER_MONTH,String.valueOf(idts.getMonth()));
		L.addParameter(CalendarParameters.PARAMETER_YEAR,String.valueOf(idts.getYear()));
	}
	public void addLastWeekPrm(Link L, IWTimestamp idts) {
		
	}
	
	/**
	 * The same method as in SmallCalendar.java
	 * @param L
	 * @param idts
	 */
	public void addNextMonthPrm(Link L, IWTimestamp idts) {
		if(groupID != -2) {
			L.addParameter(PARAMETER_ISI_GROUP_ID,groupID);
		}
		if (idts.getMonth() == 12) {
			L.addParameter(CalendarParameters.PARAMETER_DAY, idts.getDay());
			L.addParameter(CalendarParameters.PARAMETER_MONTH, String.valueOf(1));
			L.addParameter(CalendarParameters.PARAMETER_YEAR, String.valueOf(idts.getYear() + 1));
		}
		else {
			L.addParameter(CalendarParameters.PARAMETER_DAY, idts.getDay());
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
		if(groupID != -2) {
			L.addParameter(PARAMETER_ISI_GROUP_ID,groupID);
		}
		
		if (idts.getMonth() == 1) {
			L.addParameter(CalendarParameters.PARAMETER_DAY,idts.getDay());
			L.addParameter(CalendarParameters.PARAMETER_MONTH, String.valueOf(12));
			L.addParameter(CalendarParameters.PARAMETER_YEAR, String.valueOf(idts.getYear() - 1));
		}
		else {
			L.addParameter(CalendarParameters.PARAMETER_DAY,idts.getDay());
			L.addParameter(CalendarParameters.PARAMETER_MONTH, String.valueOf(idts.getMonth() - 1));
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

	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
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
	protected UserBusiness getUserBusiness(IWApplicationContext iwc) {
		UserBusiness userBusiness = null;
		if (userBusiness == null) {
			try {
				userBusiness = (UserBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, UserBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return userBusiness;
	}
	
	

}
