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
		
		userText = new Text(iwrb.getLocalizedString(userFieldParameterName,"User"));
		userText.setStyleClass(bold);
		otherCoachesText = new Text(iwrb.getLocalizedString(otherCoachesFieldParameterName,"Other coaches"));
		otherCoachesText.setStyleClass(bold);
		clubText = new Text(iwrb.getLocalizedString(clubFieldParameterName,"Club"));
		clubText.setStyleClass(bold);
		divisionText = new Text(iwrb.getLocalizedString(divisionFieldParameterName,"Division"));
		divisionText.setStyleClass(bold);
		groupText = new Text(iwrb.getLocalizedString(groupFieldParameterName,"Group"));
		groupText.setStyleClass(bold);
		dateText = new Text(iwrb.getLocalizedString(dateFieldParameterName,"Date"));
		dateText.setStyleClass(bold);
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
				classDef = Class.forName(bClass);
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
			userField = new Text(user.getName());
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
			otherCoachesNameField = buff.toString();
			
		}
		else {
			otherCoachesNameField = "";
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
			clubField = ledgerVariationsHandler.getParentOfParentGroupName(parentGroups);
			divisionField = ledgerVariationsHandler.getParentGroupName(parentGroups);
		}
		else {
			clubField = iwrb.getLocalizedString("user_stats_window.no_club_text","No club");
			divisionField = iwrb.getLocalizedString("usr_stats_window.no_division_text","No division");
		}
		
		 
		
		groupField = getCalendarBusiness(iwc).getLedger(lID.intValue()).getName();
		
		dateField = new IWTimestamp(getCalendarBusiness(iwc).getLedger(lID.intValue()).getDate()).getDateString("dd. MMMMMMMM yyyy");
		
		printButton = new PrintButton(iwb.getImage("print.gif"));
		
		closeButton = new CloseButton(iwrb.getLocalizedString("user_stats_window.close", "Close"));
	}
	public void lineUp(IWContext iwc, int ledID, Collection users) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		
		table.add(getHeaderTable(iwc),1,1);
		Table underTable = new Table();
		underTable.setCellpadding(0);
		underTable.setCellspacing(2);
		underTable.setWidth("100%");
		underTable.setStyleClass(grayBackground);
		Collection marks = getCalendarBusiness(iwc).getAllMarks();
		Collection practices = getCalendarBusiness(iwc).getPracticesByLedgerID(ledID);
		
		Table tpTable = new Table();
		tpTable.setCellpadding(1);
		tpTable.setCellspacing(0);
		tpTable.setWidth("100%");
		tpTable.setHeight("100%");
		tpTable.setColor("#ffffff");
		tpTable.add(iwrb.getLocalizedString("userStat.total_practices","Total practices")+ " "  +  practices.size(),1,1);
		underTable.add(tpTable,1,1);

		User user = null;
		Iterator userIter = users.iterator();
		int row = 2;
		
		while(userIter.hasNext()) {
			
			Table nameTable = new Table();
			nameTable.setCellpadding(1);
			nameTable.setCellspacing(0);
			nameTable.setWidth("100%");
			nameTable.setHeight("100%");
			nameTable.setColor("#ffffff");
			int column = 2;
			user = (User) userIter.next();
			Integer userID = (Integer) user.getPrimaryKey();
			nameTable.add(user.getName(),1,1);
			underTable.add(nameTable,1,row);
			underTable.setHeight(1,row,"25");
			underTable.setWidth(1,row,150);
			AttendanceMark attendanceMark = null;
			Iterator markIter = marks.iterator();
			while(markIter.hasNext()) {
				attendanceMark = (AttendanceMark) markIter.next();
				String markName = attendanceMark.getMark();
				Table markTable = new Table();
				markTable.setCellpadding(1);
				markTable.setCellspacing(0);
				markTable.setColor("#ffffff");
				markTable.setWidth("100%");
				markTable.setHeight("100%");
				markTable.setAlignment(1,1,"center");
				Table statsTable =new Table();
				statsTable.setCellpadding(1);
				statsTable.setCellspacing(0);
				statsTable.setColor("#ffffff");
				statsTable.setWidth("100%");
				statsTable.setHeight("100%");
				
				if(row == 2) {
					markTable.add(markName,1,1);
					underTable.add(markTable,column,1);
				}		
				Table stat = getStatsForUser(iwc,userID.intValue(),ledID,attendanceMark.getMark(),practices.size());
				statsTable.add(stat,1,1);
				underTable.setWidth(column,row,50);
				underTable.setHeight(column,row,"25");
				underTable.add(statsTable,column++,row);
				
			}			
			row++;
		}	
		table.add(underTable,1,2);
		table.setAlignment(1,3,"right");
		table.add(closeButton,1,3);
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
		t.setWidth("100%");
		t.setHeight("100%");
		List numberOfMarks = getCalendarBusiness(iwc).getAttendanceMarks(userID,ledID,mark);
		NumberFormat nfp = NumberFormat.getPercentInstance();
		NumberFormat nfi = NumberFormat.getIntegerInstance();
		float i = numberOfMarks.size();
		float j = i/totalPractices;
		t.add(nfp.format(j),1,1);
		t.setStyleClass(1,1,borderRight);
		t.setHeight(1,1,"100%");
		t.add(nfi.format(i),2,1);
		
		
		return t;
	}
	
	public Table getHeaderTable(IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		
		Table headerTable = new Table();
		headerTable.setCellpadding(0);
		headerTable.setCellspacing(0);
		headerTable.setWidth("100%");
		headerTable.setHeight("100%");
		headerTable.setStyleClass(borderAllWhite);
		headerTable.add(userText,1,1);
		headerTable.add(userField,2,1);
		headerTable.add(clubText,1,2);
		headerTable.add(clubField,2,2);
		headerTable.add(otherCoachesText,1,3);
		headerTable.add(otherCoachesNameField,2,3);
		headerTable.add(divisionText,1,4);
		headerTable.add(divisionField,2,4);
		headerTable.add(groupText,1,5);
		headerTable.add(groupField,2,5);
		headerTable.add(dateText,1,6);
		headerTable.add(dateField,2,6);
		
		headerTable.setVerticalAlignment(3,1,"top");
		headerTable.setVerticalAlignment(4,1,"top");
		Text t = new Text(iwrb.getLocalizedString("ledgerwindow.allowed_marks", "Allowed marks"));
		t.setStyleClass(bold);
		headerTable.add(t ,3,1);
		headerTable.mergeCells(4,1,4,4);
		Collection marks = getCalendarBusiness(iwc).getAllMarks(); 
		Iterator markIter = marks.iterator();
		while(markIter.hasNext()) {
			AttendanceMark attendanceMark = (AttendanceMark) markIter.next();
			String markName = attendanceMark.getMark();
			headerTable.add(markName+": "+attendanceMark.getMarkDescription(),4,1);
			headerTable.add("<br>",4,1);
		}
		headerTable.setVerticalAlignment(5,4,"bottom");
		headerTable.setAlignment(5,4,"right");
		headerTable.add(printButton,5,4);
		return headerTable;
	}
	
	public void main(IWContext iwc) throws Exception {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		addTitle(iwrb.getLocalizedString("userStatWindow.user_stat","User statistics"),titleFont);
		
		initializeTexts();
		initializeFields();
		
		ledgerString = iwc.getParameter(LedgerWindow.LEDGER);
		ledgerID =new Integer(ledgerString);
//		CalendarLedger ledger = getCalendarBusiness(iwc).getLedger(ledgerID.intValue());
		List usersInLedger = (List) getCalendarBusiness(iwc).getUsersInLedger(ledgerID.intValue());
		
		final Collator collator = Collator.getInstance(iwc.getLocale());
		if(usersInLedger != null) {
			Collections.sort(usersInLedger,new Comparator() {
				public int compare(Object arg0, Object arg1) {
					return collator.compare(((User) arg0).getName(), ((User) arg1).getName());
				}				
			});
		}
		
		lineUp(iwc,ledgerID.intValue(),usersInLedger);
		add(table,iwc);
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
