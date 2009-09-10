/*
 * Created on Feb 23, 2004
 */
package com.idega.block.cal.presentation;

import java.text.Collator;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.idega.block.cal.business.CalBusiness;
import com.idega.block.cal.business.DefaultLedgerVariationsHandler;
import com.idega.block.cal.business.LedgerVariationsHandler;
import com.idega.block.cal.data.AttendanceMark;
import com.idega.block.cal.data.CalendarLedger;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.PrintButton;
import com.idega.repository.data.RefactorClassRegistry;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:birna@idega.is">Birna Iris Jonsdottir</a>
 */
public class UserStatisticsWindow extends StyledIWAdminWindow{
	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.cal";
	public static final String BUNDLE_KEY_LEDGER_VARIATIONS_HANDLER_CLASS = "ledger_variations_class";
	
	private static final String HELP_TEXT_KEY = "cal_user_statistics";
	
	
	//parameter names
	private static String userFieldParameterName = "user_stat_window.user";
	private static String otherCoachesFieldParameterName = "user_stat_window.otherCoaches";
	private static String clubFieldParameterName = "user_stat_window.club";
	private static String divisionFieldParameterName= "user_stat_window.division";
	private static String groupFieldParameterName = "user_stat_window.group";
	private static String dateFieldParameterName = "user_stat_window.date";
	
	//texts
	private Text userText;
	private Text otherCoachesText;
	private Text clubText;
	private Text divisionText;
	private Text groupText;
	private Text dateText;
	
	//fields
	private Text userField;
	private String otherCoachesNameField;
	private String clubField;
	private String divisionField;
	private String groupField;
	private String dateField;
	
	private PrintButton printButton;
	private CloseButton closeButton;
	
	private Table table = null;
	
	private String ledgerString = null;
	private Integer ledgerID = null;
	
	private String borderRight = "borderRight";
	private String borderAllWhite = "borderAllWhite";
	private String bold = "bold";
	private String titleFont = "font-family:Verdana,Arial,Helvetica,sans-serif;font-size:9pt;font-weight:bold;color:#FFFFFF;";
	private String grayBackground = "grayBack";
	
	
	public UserStatisticsWindow() {
		setWidth(700);
		setHeight(500);
		setResizable(true);
	}
	protected void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		this.userText = new Text(iwrb.getLocalizedString(userFieldParameterName,"User"));
		this.userText.setStyleClass(this.bold);
		this.otherCoachesText = new Text(iwrb.getLocalizedString(otherCoachesFieldParameterName,"Other coaches"));
		this.otherCoachesText.setStyleClass(this.bold);
		this.clubText = new Text(iwrb.getLocalizedString(clubFieldParameterName,"Club"));
		this.clubText.setStyleClass(this.bold);
		this.divisionText = new Text(iwrb.getLocalizedString(divisionFieldParameterName,"Division"));
		this.divisionText.setStyleClass(this.bold);
		this.groupText = new Text(iwrb.getLocalizedString(groupFieldParameterName,"Group"));
		this.groupText.setStyleClass(this.bold);
		this.dateText = new Text(iwrb.getLocalizedString(dateFieldParameterName,"Date"));
		this.dateText.setStyleClass(this.bold);
	}
	protected void initializeFields() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		IWBundle iwb = getBundle(iwc);
		
		// the class used to handle ledgerVariations is an applicationProperty... 
		String bClass = null;
		try {
			bClass = iwc.getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).getProperty(BUNDLE_KEY_LEDGER_VARIATIONS_HANDLER_CLASS);
		} catch(Exception e) {
			// just user default LedgerVariationHandler class
		}
		LedgerVariationsHandler ledgerVariationsHandler;
		if(bClass!=null && bClass.trim().length()>0) {
			Class classDef;
			try {
				classDef = RefactorClassRegistry.forName(bClass);
				ledgerVariationsHandler = (LedgerVariationsHandler) classDef.newInstance();
			} catch (Exception e) {
				System.out.println("Couldn't instantiate class for ledgerVariationsHandler, using default: " + bClass);
				e.printStackTrace();
				ledgerVariationsHandler = new DefaultLedgerVariationsHandler();
			}
		} else {
			ledgerVariationsHandler = new DefaultLedgerVariationsHandler();
		}
		
		
		String lString = iwc.getParameter(LedgerWindow.LEDGER);
		Integer lID =new Integer(lString);
		CalendarLedger ledger = getCalendarBusiness(iwc).getLedger(lID.intValue());
				
		if(iwc.isLoggedOn()) {
			User user =iwc.getCurrentUser();
			this.userField = new Text(user.getName());
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
			Collection c = getGroupBusiness(iwc).getParentGroupsRecursive(getGroupBusiness(iwc).getGroupByGroupID(groupID));
			if(c != null) {
				parentGroups = new ArrayList(getGroupBusiness(iwc).getParentGroupsRecursive(getGroupBusiness(iwc).getGroupByGroupID(groupID)));
			}			
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(parentGroups != null) {
			this.clubField = ledgerVariationsHandler.getParentOfParentGroupName(parentGroups);
			this.divisionField = ledgerVariationsHandler.getParentGroupName(parentGroups);
		}
		else {
			this.clubField = iwrb.getLocalizedString("user_stats_window.no_club_text","No club");
			this.divisionField = iwrb.getLocalizedString("usr_stats_window.no_division_text","No division");
		}
		
		 
		
		this.groupField = getCalendarBusiness(iwc).getLedger(lID.intValue()).getName();
		
		this.dateField = new IWTimestamp(getCalendarBusiness(iwc).getLedger(lID.intValue()).getDate()).getDateString("dd. MMMMMMMM yyyy");
		
		this.printButton = new PrintButton(iwb.getImage("print.gif"));
		
		this.closeButton = new CloseButton(iwrb.getLocalizedString("user_stats_window.close", "Close"));
	}
	public void lineUp(IWContext iwc, int ledID, Collection users) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		CalBusiness calBiz = getCalendarBusiness(iwc);
		
		this.table = new Table();
		this.table.setCellpadding(0);
		this.table.setCellspacing(0);
		
		this.table.add(getHeaderTable(iwc),1,1);
		Table underTable = new Table();
		underTable.setCellpadding(0);
		underTable.setCellspacing(1);
		underTable.setWidth(Table.HUNDRED_PERCENT);
		underTable.setStyleClass(this.grayBackground);
		Collection marks = calBiz.getAllMarks();
		Collection practices = calBiz.getPracticesByLedgerID(ledID);
		
		Table tpTable = new Table();
		tpTable.setCellpadding(5);
		tpTable.setCellspacing(0);
		tpTable.setWidth(Table.HUNDRED_PERCENT);
		tpTable.setHeight(Table.HUNDRED_PERCENT);
		tpTable.setColor("#ffffff");
		Text totalPrac = new Text(iwrb.getLocalizedString("userStat.total_practices","Total practices")+ ":");
		totalPrac.setBold();
		tpTable.add(totalPrac,1,1);
		tpTable.add(" " + practices.size(),1,1);
		underTable.add(tpTable,1,1);

		User user = null;
		Iterator userIter = users.iterator();
		int row = 2;
		
		while(userIter.hasNext()) {
			
			Table nameTable = new Table();
			nameTable.setCellpadding(5);
			nameTable.setCellspacing(0);
			nameTable.setWidth(Table.HUNDRED_PERCENT);
			nameTable.setHeight(Table.HUNDRED_PERCENT);
			nameTable.setColor("#ffffff");
			int column = 2;
			user = (User) userIter.next();
			Integer userID = (Integer) user.getPrimaryKey();
			nameTable.add(user.getName(),1,1);
			underTable.add(nameTable,1,row);
			underTable.setHeight(1,row,25);
			underTable.setWidth(1,row,150);
			AttendanceMark attendanceMark = null;
			Iterator markIter = marks.iterator();
			while(markIter.hasNext()) {
				attendanceMark = (AttendanceMark) markIter.next();
				String markName = attendanceMark.getMark();
				Table markTable = new Table();
				markTable.setCellpadding(5);
				markTable.setCellspacing(0);
				markTable.setColor("#ffffff");
				markTable.setWidth(Table.HUNDRED_PERCENT);
				markTable.setHeight(Table.HUNDRED_PERCENT);
				markTable.setAlignment(1,1,Table.HORIZONTAL_ALIGN_CENTER);
				Table statsTable =new Table();
				statsTable.setCellpadding(1);
				statsTable.setCellspacing(0);
				statsTable.setColor("#ffffff");
				statsTable.setWidth(Table.HUNDRED_PERCENT);
				statsTable.setHeight(Table.HUNDRED_PERCENT);
				
				if(row == 2) {
					markTable.add(markName,1,1);
					underTable.add(markTable,column,1);
				}		
				Collection markedEntries = calBiz.getMarkedEntriesByUserIDandLedgerID(userID.intValue(),ledID);
				Table stat = getStatsForUser(iwc,userID.intValue(),ledID,attendanceMark.getMark(),markedEntries.size());
				statsTable.add(stat,1,1);
				underTable.setWidth(column,row,50);
				underTable.setHeight(column,row,25);
				underTable.add(statsTable,column++,row);
				
			}			
			row++;
		}
		
		Table buttonTable = new Table();
		buttonTable.setWidth(Table.HUNDRED_PERCENT);
		buttonTable.setCellpadding(0);
		buttonTable.setCellspacing(12);
		buttonTable.setStyleClass(this.borderAllWhite);
		buttonTable.add(getHelp(UserStatisticsWindow.HELP_TEXT_KEY),1,1);
		buttonTable.setAlignment(2,1,Table.HORIZONTAL_ALIGN_RIGHT);
		buttonTable.add(this.closeButton,2,1);
		
		this.table.setHeight(2,5);
		this.table.add(underTable,1,3);
		this.table.setHeight(4,5);
		this.table.add(buttonTable,1,5);
		
	}
	/**
	 * 
	 * @param iwc
	 * @param userID
	 * @param ledID
	 * @param mark
	 * @param totalPractices
	 * @return
	 */
	public Table getStatsForUser(IWContext iwc, int userID, int ledID, String mark, float totalPractices) {
		Table t = new Table();
		t.setWidth(Table.HUNDRED_PERCENT);
		t.setHeight(Table.HUNDRED_PERCENT);
		List numberOfMarks = getCalendarBusiness(iwc).getAttendanceMarks(userID,ledID,mark);
		NumberFormat nfp = NumberFormat.getPercentInstance();
		NumberFormat nfi = NumberFormat.getIntegerInstance();
		float i = numberOfMarks.size();
		
		if(i != 0) {
			float j = i/totalPractices;
			t.add(nfp.format(j),1,1);
		}
		else {
			t.add("0",1,1);
		}
		
		t.setStyleClass(1,1,this.borderRight);
		t.setHeight(1,1,Table.HUNDRED_PERCENT);
		t.add(nfi.format(i),2,1);
				
		return t;
	}
	
	public Table getHeaderTable(IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		
		Table headerTable = new Table();
		headerTable.setCellpadding(0);
		headerTable.setCellspacing(12);
		headerTable.setWidth(Table.HUNDRED_PERCENT);
		headerTable.setHeight(Table.HUNDRED_PERCENT);
		headerTable.setStyleClass(this.borderAllWhite);
		headerTable.add(this.userText,1,1);
		headerTable.add(this.userField,2,1);
		headerTable.add(this.clubText,1,2);
		headerTable.add(this.clubField,2,2);
		headerTable.add(this.otherCoachesText,1,3);
		headerTable.add(this.otherCoachesNameField,2,3);
		headerTable.add(this.divisionText,1,4);
		headerTable.add(this.divisionField,2,4);
		headerTable.add(this.groupText,1,5);
		headerTable.add(this.groupField,2,5);
		headerTable.add(this.dateText,1,6);
		headerTable.add(this.dateField,2,6);
		
		headerTable.setVerticalAlignment(3,1,"top");
		headerTable.setVerticalAlignment(4,1,"top");
		Text t = new Text(iwrb.getLocalizedString("ledgerwindow.allowed_marks", "Allowed marks"));
		t.setStyleClass(this.bold);
		headerTable.add(t ,3,1);
		headerTable.mergeCells(4,1,4,4);
		Collection marks = getCalendarBusiness(iwc).getAllMarks(); 
		Iterator markIter = marks.iterator();
		while(markIter.hasNext()) {
			AttendanceMark attendanceMark = (AttendanceMark) markIter.next();
			String markName = attendanceMark.getMark();
			headerTable.add(markName+": "+attendanceMark.getMarkDescription(),4,1);
			headerTable.add(Text.BREAK,4,1);
		}
		headerTable.setVerticalAlignment(5,6,Table.VERTICAL_ALIGN_BOTTOM);
		headerTable.setAlignment(5,6,Table.HORIZONTAL_ALIGN_RIGHT);
		headerTable.add(this.printButton,5,6);
		return headerTable;
	}
	
	public void main(IWContext iwc) throws Exception {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		setTitle(iwrb.getLocalizedString("userStatWindow.user_stat","User statistics"));
		addTitle(iwrb.getLocalizedString("userStatWindow.user_stat","User statistics"),TITLE_STYLECLASS);
		
		initializeTexts();
		initializeFields();
		
		this.ledgerString = iwc.getParameter(LedgerWindow.LEDGER);
		this.ledgerID =new Integer(this.ledgerString);
//		CalendarLedger ledger = getCalendarBusiness(iwc).getLedger(ledgerID.intValue());
		List usersInLedger = (List) getCalendarBusiness(iwc).getUsersInLedger(this.ledgerID.intValue());
		
		final Collator collator = Collator.getInstance(iwc.getLocale());
		if(usersInLedger != null) {
			Collections.sort(usersInLedger,new Comparator() {
				public int compare(Object arg0, Object arg1) {
					return collator.compare(((User) arg0).getName(), ((User) arg1).getName());
				}				
			});
		}
		
		lineUp(iwc,this.ledgerID.intValue(),usersInLedger);
		add(this.table,iwc);
	}
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
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
	

}
