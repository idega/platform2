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
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.User;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:birna@idega.is">Birna Iris Jonsdottir</a>
 */
public class UserStatisticsWindow extends StyledIWAdminWindow{
	
	private Table table = null;
	
	private String ledgerString = null;
	private Integer ledgerID = null;
	
	public UserStatisticsWindow() {
		setWidth(200);
		setHeight(200);
		setResizable(true);
	}
	protected void initializeTexts() {
		
	}
	protected void initializeFields() {
		
	}
	public void lineUp(IWContext iwc, int ledID, Collection users) {
		table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setStyleClass("borderAllWhite");
		
		Collection marks = getCalendarBusiness(iwc).getAllMarks();
		Collection practices = getCalendarBusiness(iwc).getEntriesByLedgerID(ledID);
		
		table.add("heildarfjöldi æfinga: " +  practices.size(),2,1);
		table.mergeCells(2,1,5,1);
		
		User user = null;
		Iterator userIter = users.iterator();
		int row = 3;
		
		while(userIter.hasNext()) {
			int column = 2;
			user = (User) userIter.next();
			Integer userID = (Integer) user.getPrimaryKey();
			table.add(user.getName(),1,row);
			AttendanceMark attendanceMark = null;
			Iterator markIter = marks.iterator();
			while(markIter.hasNext()) {
				attendanceMark = (AttendanceMark) markIter.next();
				String markName = attendanceMark.getMark();
				if(row == 3) {
					table.add(markName,column,2);
				}				
				String stat = getStatsForUser(iwc,userID.intValue(),ledID,attendanceMark.getMark(),practices.size());
				table.add(stat,column++,row);
			}			
			row++;
		}		
	}
	public String getStatsForUser(IWContext iwc, int userID, int ledID, String mark, float totalPractices) {
		List numberOfMarks = getCalendarBusiness(iwc).getAttendanceMarks(userID,ledID,mark);
		NumberFormat nf = NumberFormat.getPercentInstance();
		float i = numberOfMarks.size();
		float j = i/totalPractices;
		return nf.format(j);
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
