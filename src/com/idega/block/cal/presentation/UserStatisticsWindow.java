/*
 * Created on Feb 23, 2004
 */
package com.idega.block.cal.presentation;

import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.idega.block.cal.business.CalBusiness;
import com.idega.block.cal.data.AttendanceMark;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.User;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:birna@idega.is">Birna Iris Jonsdottir</a>
 */
public class UserStatisticsWindow extends StyledIWAdminWindow{
	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.cal";
	
	private String grayBackground = "grayBack";
	
	//parameter names
	private static String coachFieldParameterName = "coach";
	private static String groupFieldParameterName = "group";
	private static String dateFieldParameterName = "date";
	
	//texts
	private Text coachText;
	private Text groupText;
	private Text dateText;
	
	//fields
	private Text coachField;
	private String ledgerField;
	private String dateField;
	
	private Table table = null;
	
	private String ledgerString = null;
	private Integer ledgerID = null;
	
	private String borderRight = "borderRight";
	private String borderAllWhite = "borderAllWhite";
	private String bold = "bold";
	
	public UserStatisticsWindow() {
		setWidth(700);
		setHeight(500);
		setResizable(true);
	}
	protected void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		coachText = new Text(iwrb.getLocalizedString(coachFieldParameterName,"Coach"));
		coachText.setStyleClass(bold);
		groupText = new Text(iwrb.getLocalizedString(groupFieldParameterName,"Group"));
		groupText.setStyleClass(bold);
		dateText = new Text(iwrb.getLocalizedString(dateFieldParameterName,"Date"));
		dateText.setStyleClass(bold);
	}
	protected void initializeFields() {
		IWContext iwc = IWContext.getInstance();
//		IWResourceBundle iwrb = getResourceBundle(iwc);

		String lString = iwc.getParameter(LedgerWindow.LEDGER);
		Integer lID =new Integer(lString);
		
		
		if(iwc.isLoggedOn()) {
			User user =iwc.getCurrentUser();
			coachField = new Text(user.getName());
		}
		ledgerField = getCalendarBusiness(iwc).getLedger(lID.intValue()).getName();
		dateField = getCalendarBusiness(iwc).getLedger(lID.intValue()).getDate().toString();
		
		
	}
	public void lineUp(IWContext iwc, int ledID, Collection users) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.add(getHeaderTable(),1,1);
		Table underTable = new Table();
		underTable.setCellpadding(0);
		underTable.setCellspacing(1);
		underTable.setStyleClass(grayBackground);
		Collection marks = getCalendarBusiness(iwc).getAllMarks();
		Collection practices = getCalendarBusiness(iwc).getEntriesByLedgerID(ledID);
		
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
				attendanceMark = (AttendanceMark) markIter.next();
				String markName = attendanceMark.getMark();
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
	public Table getHeaderTable() {
		Table headerTable = new Table();
		headerTable.setCellpadding(0);
		headerTable.setCellspacing(0);
		headerTable.setWidth("100%");
		headerTable.setHeight("100%");
		headerTable.setStyleClass(borderAllWhite);
		headerTable.add(coachText,1,1);
		headerTable.add(coachField,2,1);
		headerTable.add(groupText,1,2);
		headerTable.add(ledgerField,2,2);
		headerTable.add(dateText,1,3);
		headerTable.add(dateField,2,3);
		return headerTable;
	}
	public void main(IWContext iwc) throws Exception {
		initializeTexts();
		initializeFields();
		
		ledgerString = iwc.getParameter(LedgerWindow.LEDGER);
		ledgerID =new Integer(ledgerString);
//		CalendarLedger ledger = getCalendarBusiness(iwc).getLedger(ledgerID.intValue());
		Collection usersInLedger = getCalendarBusiness(iwc).getUsersInLedger(ledgerID.intValue());
		
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
