/*
 * Created on May 5, 2004
 */
package com.idega.block.cal.presentation;

import java.sql.Timestamp;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.idega.block.cal.business.CalBusiness;
import com.idega.block.cal.business.LedgerVariationsHandler;
import com.idega.block.cal.data.AttendanceEntity;
import com.idega.block.cal.data.AttendanceMark;
import com.idega.block.cal.data.CalendarEntry;
import com.idega.block.cal.data.CalendarLedger;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.CalendarParameters;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Page;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.PrintButton;
import com.idega.presentation.ui.Window;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * Description: A printable version of the LedgerWindow - displays the same information <br>
 * 							except without any buttons and unnecessary elements
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:birna@idega.is">Birna Iris Jonsdottir</a>
 */
public class PrintableLedgerWindow extends Window{
	
	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.cal";
	
	//Texts
	private Text creatorText;
	private Text otherCoachesText;
	private Text clubText;
	private Text divisionText;
	private Text groupText;
	private Text dateText;
	
	//Fields
	private String creatorNameField;
	private String otherCoachesNameField;
	private String groupNameField;
	private String fromDateField;
	private String clubNameField;
	private String divisionNameField;
	private Table allowedMarksField;
	
	private Table table; 
	private PrintButton printButton;
	
	private static String bold = "bold";
	private String printTable = "printTable";
	private String grayBackground = "grayBack";
	private String borderAllWhite = "borderAllWhite";
	
	public PrintableLedgerWindow() {
		setHeight(400);
		setWidth(800);
		setResizable(true);
	}
	
	private void initializeTexts(IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		this.creatorText = new Text(iwrb.getLocalizedString(LedgerWindow.creatorFieldParameterName,"User"));
		this.creatorText.setStyleClass(bold);
		this.otherCoachesText = new Text(iwrb.getLocalizedString(LedgerWindow.otherCoachesFieldParameterName,"Other coaches"));
		this.otherCoachesText.setStyleClass(bold);
		this.clubText = new Text(iwrb.getLocalizedString(LedgerWindow.clubFieldParameterName,"Club"));
		this.clubText.setStyleClass(bold);
		this.divisionText = new Text(iwrb.getLocalizedString(LedgerWindow.divisionFieldParameterName,"Division"));
		this.divisionText.setStyleClass(bold);
		this.groupText = new Text(iwrb.getLocalizedString(LedgerWindow.groupFieldParameterName,"Group"));
		this.groupText.setStyleClass(bold);
		this.dateText = new Text(iwrb.getLocalizedString(LedgerWindow.dateFieldParameterName,"Date"));
		this.dateText.setStyleClass(bold);		
	}
	private void initializeFields(IWContext iwc) {

		IWResourceBundle iwrb = getResourceBundle(iwc);
		IWBundle iwb = getBundle(iwc);
		
		
		LedgerVariationsHandler ledgerVariationsHandler = LedgerWindow.getLedgerVariationsHandler(iwc);
		
		String lIDString = iwc.getParameter(LedgerWindow.LEDGER);
		Integer lID = new Integer(lIDString);
		CalendarLedger ledger = getCalendarBusiness(iwc).getLedger(lID.intValue());
		
		User creator = null;
		try{
			creator = getUserBusiness(iwc).getUser(ledger.getCoachID());
		}catch(Exception e) {
			e.printStackTrace();
		}		
		if(creator != null) {
			this.creatorNameField = creator.getName();
		}
		
		int coachGroupID = ledger.getCoachGroupID();
		Group coaches = null;
		List trainers = null;
		try {
			if(coachGroupID != -1) {
				coaches = getGroupBusiness(iwc).getGroupByGroupID(coachGroupID);
				trainers = (List) getGroupBusiness(iwc).getUsers(coaches);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		if(trainers != null && trainers.size() != 0) {
			StringBuffer buff = new StringBuffer();
			Iterator trainersIter = trainers.iterator();		
			while(trainersIter.hasNext()) {
				User trainer = (User) trainersIter.next();
				buff.append(trainer.getName());
				buff.append("<br>");
			}
			this.otherCoachesNameField = buff.toString();
			
		}
		else {
			this.otherCoachesNameField = "";
		}
		Collection parentGroups = null;
		int groupID = getCalendarBusiness(iwc).getLedger(lID.intValue()).getGroupID();
		try {
			this.groupNameField = getGroupBusiness(iwc).getGroupByGroupID(groupID).getName();
			Collection c = getGroupBusiness(iwc).getParentGroupsRecursive(getGroupBusiness(iwc).getGroupByGroupID(groupID));
			if(c != null) {		
				parentGroups = new ArrayList(c);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(parentGroups != null) {
			this.divisionNameField = ledgerVariationsHandler.getParentGroupName(parentGroups);
			this.clubNameField = ledgerVariationsHandler.getParentOfParentGroupName(parentGroups);
		}
		if(this.divisionNameField ==  null) {
			this.divisionNameField = iwrb.getLocalizedString("ledgerwindow.no_division_text","No division");
		}
		if(this.clubNameField == null) {
			this.clubNameField = iwrb.getLocalizedString("ledgerwindow.no_club_text","No club");
		}
		
		Timestamp fromD = getCalendarBusiness(iwc).getLedger(lID.intValue()).getDate();
		IWTimestamp iwFromD = new IWTimestamp(fromD);
		//fromDate is the start date of the ledger
		this.fromDateField = iwFromD.getDateString("dd. MMMMMMMM yyyy", iwc.getCurrentLocale());
		
		this.allowedMarksField = new Table();
		List marks = getCalendarBusiness(iwc).getAllMarks();
		if(marks!= null) {
			Iterator markIter = marks.iterator();	
			while(markIter.hasNext()) {
				AttendanceMark mark = (AttendanceMark) markIter.next();
				this.allowedMarksField.add(mark.getMark(),4,1);
				this.allowedMarksField.add(": "+mark.getMarkDescription(),4,1);
				this.allowedMarksField.add("<br>",4,1);
			}
		}
		Image print = iwb.getImage("printer2.gif");
		print.setAlt(iwrb.getLocalizedString("ledger_window.print","Print"));
		this.printButton = new PrintButton(print);
		
	}
	private void lineUp(IWContext iwc) {
		this.table = new Table();
		
		Table headerTable = new Table();
		headerTable.setCellspacing(0);
		headerTable.setCellpadding(0);
		headerTable.setWidth(300);
		
		headerTable.add(this.creatorText,1,1);
		headerTable.add(this.creatorNameField,2,1);
		headerTable.add(this.otherCoachesText,1,2);
		headerTable.add(this.otherCoachesNameField,2,2);
		headerTable.add(this.clubText,1,3);
		headerTable.add(this.clubNameField,2,3);
		headerTable.add(this.divisionText,1,4);
		headerTable.add(this.divisionNameField,2,4);
		headerTable.add(this.groupText,1,5);
		headerTable.add(this.groupNameField,2,5);
		headerTable.add(this.dateText,1,6);
		headerTable.add(this.fromDateField,2,6);
		
		this.table.add(headerTable,1,1);

		this.table.add(getAttendanceList(iwc),1,2);
		
		this.table.add(this.printButton,1,3);
		
	}
	public Table getAttendanceList(IWContext iwc) {
		
		
		List entryList = null;
		List userList = null;
		
		CalBusiness calBiz = getCalendarBusiness(iwc);
		
		int m = 0;
		int y = 0;
		int ledID = -1;
		
		String ledgerData = iwc.getParameter(LedgerWindow.PRINT_LEDGER);
		
		String ledgerID = iwc.getParameter(LedgerWindow.LEDGER);
		String year = iwc.getParameter(CalendarParameters.PARAMETER_YEAR);
		String month = iwc.getParameter(CalendarParameters.PARAMETER_MONTH);
		if(year != null && !year.equals("") &&
				month != null && !month.equals("")) {
			y = Integer.parseInt(year);
			m = Integer.parseInt(month);
		}
		if(ledgerID != null && !ledgerID.equals("")) {
			ledID = Integer.parseInt(ledgerID);
		}
		
		if(ledID != -1) {
			try {
				userList = (List) calBiz.getUsersInLedger(ledID);
				entryList = (List) calBiz.getPracticesByLedIDandMonth(ledID,m,y);
			}
			catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
		}		
		final Collator collator = Collator.getInstance(iwc.getLocale());
		if(userList != null) {
			Collections.sort(userList,new Comparator() {
				public int compare(Object arg0, Object arg1) {
					return collator.compare(((User) arg0).getName(), ((User) arg1).getName());
				}				
			});
		}
		Collections.sort(entryList,new Comparator() {
			public int compare(Object arg0, Object arg1) {
				return ((CalendarEntry) arg0).getDate().compareTo(((CalendarEntry) arg1).getDate());
			}				
		});
		
		Table underTable = new Table();
		underTable.setCellpadding(0);
		underTable.setCellspacing(1);
		underTable.setWidth(800);
		underTable.setStyleClass(this.grayBackground);
		underTable.setColor(1,1,"#ffffff");
		
		int c = 2;
		Iterator eIter = entryList.iterator();
		while(eIter.hasNext()) {
			Table topTable = new Table();
			topTable.setColor("#ffffff");
			topTable.setCellpadding(1);
			topTable.setCellspacing(0);
			topTable.setStyleClass(this.borderAllWhite);
			topTable.setWidth(Table.HUNDRED_PERCENT);
			topTable.setHeight(Table.HUNDRED_PERCENT);
			CalendarEntry entry = (CalendarEntry) eIter.next();
			IWTimestamp date = new IWTimestamp(entry.getDate());
			String dateString = date.getDateString("dd/MM");
			topTable.add(dateString,1,1);
			underTable.add(topTable,c++,1);
		}
		int row = 2;
		Iterator userIter = userList.iterator();
		while(userIter.hasNext()) {
			Table nameTable = new Table();
			nameTable.setColor("#ffffff");
			nameTable.setCellpadding(1);
			nameTable.setCellspacing(0);
			nameTable.setStyleClass(this.borderAllWhite);
//			nameTable.setWidth(Table.HUNDRED_PERCENT);
			nameTable.setHeight(Table.HUNDRED_PERCENT);
			int column = 2;
			User user = (User) userIter.next();
			nameTable.add(user.getName(),1,1);
			nameTable.setWidth(180);
			underTable.setWidth(1,row,180);
			underTable.add(nameTable,1,row);
			Integer userID = (Integer) user.getPrimaryKey();
			Iterator entryIter = entryList.iterator();
			while(entryIter.hasNext()) {
				Table markTable = new Table();
				markTable.setColor("#ffffff");
				markTable.setCellpadding(1);
				markTable.setCellspacing(0);
				markTable.setStyleClass(this.borderAllWhite);
				markTable.setWidth(Table.HUNDRED_PERCENT);
				markTable.setHeight(Table.HUNDRED_PERCENT);
				markTable.setAlignment(1,1,Table.HORIZONTAL_ALIGN_CENTER);
				CalendarEntry entry = (CalendarEntry) entryIter.next();
				AttendanceEntity attendance = calBiz.getAttendanceByUserIDandEntry(userID.intValue(),entry);
				if(attendance != null && ledgerData.equals(LedgerWindow.PRINT_WITH_DATA)) {
					if(attendance.getAttendanceMark() == null) {
						markTable.add(Text.NON_BREAKING_SPACE,1,1);
					}
					else if(attendance.getAttendanceMark().equals("")) {
						markTable.add(Text.NON_BREAKING_SPACE,1,1);
					}
					else {
						markTable.add(attendance.getAttendanceMark(),1,1);
					}
				}
				else {
					markTable.add(Text.NON_BREAKING_SPACE,1,1);
				}
				underTable.add(markTable,column,row);
				column++;			
			}
			row++;
		}
		
		return underTable;
	}

	public void main(IWContext iwc) throws Exception{
		Page parentPage = this.getParentPage();
		String styleSrc = iwc.getIWMainApplication().getBundle(getBundleIdentifier()).getResourcesURL();
		String styleSheetName = "CalStyle.css"; 
		styleSrc = styleSrc + "/" + styleSheetName;
		parentPage.addStyleSheetURL(styleSrc);
		
		initializeTexts(iwc);
		initializeFields(iwc);
		lineUp(iwc);
		add(this.table);
		
	}
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	public CalBusiness getCalendarBusiness(IWApplicationContext iwc) {
		CalBusiness calBiz = null;
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
	public GroupBusiness getGroupBusiness(IWApplicationContext iwc) {
		GroupBusiness groupBiz = null;
		if (groupBiz == null) {
			try {
				groupBiz = (GroupBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, GroupBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return groupBiz;
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
