/*
 * Created on Mar 18, 2004
 */
package com.idega.block.cal.presentation;

import java.util.Collection;
import java.util.Iterator;

import com.idega.block.cal.business.CalBusiness;
import com.idega.block.cal.data.CalendarLedger;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:birna@idega.is">Birna Iris Jonsdottir</a>
 */
public class CreateUserInLedger extends StyledIWAdminWindow{
	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.cal";
	
	private static final String HELP_TEXT_KEY = "cal_create_user_in_ledger";
	
	public static String NEW_USER_IN_LEDGER = "user_new_in_ledger_";
	
	//parameterNames
	private static String nameFieldParameterName = "cul_name";
	private static String ssnFieldParameterName = "cul_ssn";
	private static String submitButtonParameterName = "submit";
	private static String submitButtonParameterValue ="save";
	
	//texts
	private Text nameText;
	private Text ssnText;

	//fields
	private TextInput nameField;
	private TextInput ssnField;
	private SubmitButton submitButton;
	private CloseButton closeButton;
	
	private Form form;
	
	private String titleFont = "font-family:Verdana,Arial,Helvetica,sans-serif;font-size:9pt;font-weight:bold;color:#FFFFFF;";
	private String borderAllWhite = "borderAllWhite";
	
	public CreateUserInLedger() {
		super();
		setHeight(250);
		setWidth(330);
		setScrollbar(false);
		setResizable(true);
	}
	public void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		nameText = new Text(iwrb.getLocalizedString(nameFieldParameterName,"Name") + ":");
		nameText.setBold();
		ssnText = new Text(iwrb.getLocalizedString(ssnFieldParameterName,"SSN") + ":");
		ssnText.setBold();
	}
	public void initializeFields() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		nameField = new TextInput(nameFieldParameterName);		
		ssnField = new TextInput(ssnFieldParameterName);
		
		submitButton = new SubmitButton(iwrb.getLocalizedString("save","Save"),submitButtonParameterName,submitButtonParameterValue);
		//closes the window
		closeButton = new CloseButton(iwrb.getLocalizedString("close","Close"));
	}
	public void lineUp() {
		Table mainTable = new Table();
		mainTable.setCellspacing(0);
		mainTable.setCellpadding(0);
		mainTable.setWidth(Table.HUNDRED_PERCENT);
		mainTable.setVerticalAlignment(1,1,Table.VERTICAL_ALIGN_TOP);
		mainTable.setVerticalAlignment(1,3,Table.VERTICAL_ALIGN_TOP);
		mainTable.setHeight(2, 5);
		
		Table table = new Table();
		table.setCellspacing(12);
		table.setCellpadding(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setStyleClass(borderAllWhite);
		table.add(nameText,1,1);
		table.add(Text.getBreak(),1,1);
		table.add(nameField,1,1);
		table.add(ssnText,1,2);
		table.add(Text.getBreak(),1,2);
		table.add(ssnField,1,2);
		
		Table bottomTable = new Table();
		bottomTable.setCellspacing(12);
		bottomTable.setCellpadding(0);
		bottomTable.setWidth(Table.HUNDRED_PERCENT);
		bottomTable.setStyleClass(borderAllWhite);
		bottomTable.add(getHelp(HELP_TEXT_KEY), 1, 1);
		bottomTable.setAlignment(2,1,Table.HORIZONTAL_ALIGN_RIGHT);
		bottomTable.add(submitButton,2,1);
		bottomTable.add(Text.NON_BREAKING_SPACE,2,1);
		bottomTable.add(closeButton,2,1);
		
		
		mainTable.add(table,1,1);
		mainTable.add(bottomTable,1,3);
		
		form.add(mainTable);
	}
	public void main(IWContext iwc) throws Exception {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		setTitle(iwrb.getLocalizedString("createUILWindow.create_user","Create user in ledger"));
		addTitle(iwrb.getLocalizedString("createUILWindow.create_user","Create user in ledger"),TITLE_STYLECLASS);
		
		form = new Form();
		initializeTexts();
		initializeFields();
		lineUp();
		

		String ledgerString = iwc.getParameter(LedgerWindow.LEDGER);
		Integer ledgerID = new Integer(ledgerString);
		form.maintainParameter(LedgerWindow.LEDGER);
				
		CalendarLedger ledger = null;
		
		String ssn = iwc.getParameter(ssnFieldParameterName);
		String name = iwc.getParameter(nameFieldParameterName);
		
		Collection groups = null;
		boolean isInGroup = false;
		
		String save = iwc.getParameter("submit");
		if(save != null && !save.equals("")){
			if((ssn == null || ssn.equals("")) && (name == null || name.equals(""))) {
				setAlertOnLoad(iwrb.getLocalizedString("create_user_in_ledger.no_input_warning","Name or SSN must be selected"));
			}
			if(name == null || name.equals("") && ssn != null) {
				name = ssn;
			}
			User user = null;
			
			if(ssn != null && !ssn.equals("")) {
				try {
					user = getUserBusiness(iwc).getUser(ssn);					
				}catch (Exception e){
					user = null;
				} 
			}
			
			
			if(user != null) {
				try {
					groups = getUserBusiness(iwc).getUserGroupsDirectlyRelated(user);					
				}catch (Exception e) {
					
				}
			}
			if(ledgerID != null) {
				ledger = getCalendarBusiness(iwc).getLedger(ledgerID.intValue());
			}
			if(groups != null) {
				Iterator groupIter = groups.iterator();
				//go through the groupIDs to see if the user is in the ledgerGroup
				while(groupIter.hasNext()) {
					Group g = (Group) groupIter.next();
					if(g != null) {
						Integer groupID = (Integer) g.getPrimaryKey();
						if(groupID.intValue() == ledger.getGroupID()) {
							isInGroup = true;
						}
					}
					
				}
			}
			
			Integer groupID = new Integer(ledger.getGroupID());			
			if(user!=null) {
				//user exists in a group but not the ledgerGroup
				if(isInGroup == false || user.getPrimaryGroup() == null) {
					//TODO: make adding user available in calbusiness
					
					user.setMetaData(NEW_USER_IN_LEDGER,groupID.toString());//user.getPrimaryKey().toString());
					user.store();
					ledger.addUser(user);	
				}	
				else {
					ledger.addUser(user);
				}
			}
			else {
				try {
					user = getUserBusiness(iwc).createUserByPersonalIDIfDoesNotExist(name,ssn,null,null);
					user.setMetaData(NEW_USER_IN_LEDGER,groupID.toString());
					user.store();
					if(ssn == null || ssn.equals("")) {
						user.setPersonalID(Integer.toString(((Integer)user.getPrimaryKey()).intValue()));
						user.store();
					}
					if(ledger != null) {
						ledger.addUser(user);
					}
				}catch (Exception e){
					e.printStackTrace();
				}
				
			}	
			setOnLoad("window.opener.parent.location.reload()");			
			close();
		}
		
		add(form,iwc);
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
	public UserBusiness getUserBusiness(IWApplicationContext iwc) {
		UserBusiness userBiz = null;
		if (userBiz == null) {
			try {
				userBiz = (UserBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, UserBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return userBiz;
	}

}
