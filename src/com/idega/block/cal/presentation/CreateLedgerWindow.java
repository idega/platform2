/*
 * Created on Feb 2, 2004
 */
package com.idega.block.cal.presentation;

import java.util.Date;

import com.idega.block.cal.business.CalBusiness;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DatePicker;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.presentation.GroupChooser;

/**
 * Description: <br>
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:birna@idega.is">Birna Iris Jonsdottir</a>
 */
public class CreateLedgerWindow extends StyledIWAdminWindow {
	
	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.cal";
	
	
	//parameter names
	private static String coachFieldParameterName = "coach";
	private static String otherCoachesFieldParameterName = "otherCoaches";
	private static String groupFieldParameterName = "group";
	private static String ledgerFieldParameterName ="ledger";
	private static String dateFieldParameterName = "date";
	private static String saveButtonParameterName = "submit";
	private static String saveButtonParameterValue = "save";
	
	//display texts 
	private Text coachText;
	private Text otherCoachesText;
	private Text groupText;
	private Text dateText;
	
	//fields
	private Text coachNameField;
	private GroupChooser otherCoachesNameField;
  private GroupChooser groupNameField;
  private DatePicker fromDatePickerField;
  
  //buttons
  private SubmitButton saveButton;
  private CloseButton closeButton;
  
  private User coach;
  private String otherCoaches;
  private String group;
  private Date date;
  
  private Form form;
  private Table mainTable;
  private CalendarView calendar;
  
  private CalBusiness calBiz;
  private GroupBusiness groupBiz;
  
  private String groupString;
  
  private String mainTableStyle = "main";
	
	public CreateLedgerWindow() {
		setHeight(400);
		setWidth(400);
		setResizable(true);
	}
	/**
	 * initializes the texts to display in the window
	 *
	 */
	protected void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		coachText = new Text(iwrb.getLocalizedString(coachFieldParameterName,"Coach"));
		otherCoachesText = new Text(iwrb.getLocalizedString(otherCoachesFieldParameterName,"Other coaches"));
		groupText = new Text(iwrb.getLocalizedString(groupFieldParameterName,"Group"));
		dateText = new Text(iwrb.getLocalizedString(dateFieldParameterName,"Start Date"));
	}
	/**
	 * initializes the fields in the form of the window
	 *
	 */
	protected void initializeFields() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		//The user logged in is set as the main coach for the ledger
		if(iwc.isLoggedOn()) {
			User user =iwc.getCurrentUser();
			coachNameField = new Text(user.getName());
		}
		otherCoachesNameField = new GroupChooser(otherCoachesFieldParameterName);
		groupNameField = new GroupChooser(groupFieldParameterName);
		//fromDate is the start date of the ledger
		fromDatePickerField =new DatePicker(dateFieldParameterName);
		//when save button is pushed the new ledger is created
		saveButton = new SubmitButton(iwrb.getLocalizedString("save","Save"),saveButtonParameterName,saveButtonParameterValue);

		//closes the window
		closeButton = new CloseButton(iwrb.getLocalizedString("close","Close"));
		
	}
	/**
	 * lines up text and fields of the window
	 *
	 */
	public void lineUp(IWContext iwc) {
		
		mainTable = new Table(2,7);
		mainTable.setWidth(288);
		mainTable.setCellspacing(0);
		mainTable.setCellpadding(5);
		mainTable.setStyleClass(mainTableStyle);
		mainTable.add(coachText,1,1);
		mainTable.add(coachNameField,2,1);
		mainTable.add(otherCoachesText,1,2);
		mainTable.add(otherCoachesNameField,2,2);
		mainTable.add(groupText,1,3);
		mainTable.add(groupNameField,2,3);
		mainTable.add(dateText,1,4);
		mainTable.add(fromDatePickerField,1,4);
		mainTable.setAlignment(2,5,"right");
		mainTable.add(saveButton,2,5);
		mainTable.add(Text.NON_BREAKING_SPACE,2,5);
		mainTable.add(closeButton,2,5);
				
		form.add(mainTable);
		
		
	}
	/**
	 * Saves the ledger which is created by the name of the group and the groupID 
	 * @param iwc - the context
	 * @throws Exception
	 */
	public void saveLedger(IWContext iwc,int groupID,String coachName) throws Exception{
		CalBusiness calBiz = getCalBusiness(iwc);
		GroupBusiness grBiz =getGroupBusiness(iwc);
		
		Group g = grBiz.getGroupByGroupID(groupID);
		String name = g.getName();
		String date = iwc.getParameter(dateFieldParameterName);
		
		calBiz.createNewLedger(name + "_ledger",groupID,coachName,date);
		
	}
	
	public void main(IWContext iwc) throws Exception {
		form = new Form();
		initializeTexts();
		initializeFields();
		
		Integer groupID =null;
		String groupName = null;
		groupString =iwc.getParameter(groupFieldParameterName);
		if(groupString == null || groupString.equals(""))
			groupString = "";
		else {
			groupString = groupString.substring(groupString.lastIndexOf("_")+1);
			groupID = new Integer(groupString);
			Group group = getGroupBusiness(iwc).getGroupByGroupID(groupID.intValue());
			groupName = group.getName();
		}
			
		String coach = iwc.getParameter(coachFieldParameterName);
		
		lineUp(iwc);
		add(form,iwc);

		String save = iwc.getParameter("submit");
		if(save != null && !save.equals("")) {
			saveLedger(iwc,groupID.intValue(),coach);
			int ledgerID = getCalBusiness(iwc).getLedgerIDByName(groupName + "_ledger");
			Integer i = new Integer(ledgerID);
			//the link is not displayed but contains the window to open
			//and the ledgerID parameter
			Link l = new Link();
			l.setWindowToOpen(LedgerWindow.class);
			l.addParameter(ledgerFieldParameterName, i.toString());
			String script = "window.opener." + l.getWindowToOpenCallingScript(iwc);
			setOnLoad(script);
			close();
			
		}
		
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
	
	public GroupBusiness getGroupBusiness(IWApplicationContext iwc) {
		GroupBusiness groupBiz =null;
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
	

}
