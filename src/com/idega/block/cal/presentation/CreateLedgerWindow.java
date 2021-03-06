/*
 * Created on Feb 2, 2004
 */
package com.idega.block.cal.presentation;

import java.util.Date;

import com.idega.block.cal.business.CalBusiness;
import com.idega.block.cal.business.DefaultLedgerVariationsHandler;
import com.idega.block.cal.business.LedgerVariationsHandler;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DatePicker;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.repository.data.RefactorClassRegistry;
import com.idega.user.business.GroupBusiness;
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
	public static final String BUNDLE_KEY_LEDGER_VARIATIONS_HANDLER_CLASS = "ledger_variations_class";
	
	private static final String HELP_TEXT_KEY = "cal_create_ledger";
	
	
	//parameter names
//	private static String coachFieldParameterName = "coach";
	private static String creatorFieldParameterName = "creator";
	private static String otherCoachesFieldParameterName = "otherCoaches";
	private static String groupFieldParameterName = "group";
	private static String ledgerFieldParameterName ="ledger";
	private static String dateFieldParameterName = "date";
	private static String saveButtonParameterName = "submit";
	private static String saveButtonParameterValue = "save";
	private static String nameFieldParameterName = "createLedgerWindow.name";
	
	//display texts 
	private Text coachText;
	private Text otherCoachesText;
	private Text groupText;
	private Text dateText;
	private Text nameText;
	
	//fields
	private Text coachNameField;
	private GroupChooser otherCoachesNameField;
  private GroupChooser groupNameField;
  private DatePicker fromDatePickerField;
  private TextInput nameField;
  
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
  private String titleFont = "font-family:Verdana,Arial,Helvetica,sans-serif;font-size:9pt;font-weight:bold;color:#FFFFFF;";
	
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

		this.coachText = new Text(iwrb.getLocalizedString(creatorFieldParameterName,"Coach"));
		this.otherCoachesText = new Text(iwrb.getLocalizedString(otherCoachesFieldParameterName,"Other coaches"));
		this.groupText = new Text(iwrb.getLocalizedString(groupFieldParameterName,"Group"));
		this.dateText = new Text(iwrb.getLocalizedString(dateFieldParameterName,"Start Date"));
		this.nameText = new Text(iwrb.getLocalizedString(nameFieldParameterName,"Name"));
	}
	/**
	 * initializes the fields in the form of the window
	 *
	 */
	protected void initializeFields() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		this.nameField = new TextInput(nameFieldParameterName);
		
		//The user logged in is set as the main coach for the ledger
		if(iwc.isLoggedOn()) {
			User user =iwc.getCurrentUser();
			this.coachNameField = new Text(user.getName());
		}

		this.otherCoachesNameField = new GroupChooser(otherCoachesFieldParameterName);
		this.groupNameField = new GroupChooser(groupFieldParameterName);

		//fromDate is the start date of the ledger
		this.fromDatePickerField =new DatePicker(dateFieldParameterName);
		//when save button is pushed the new ledger is created
		this.saveButton = new SubmitButton(iwrb.getLocalizedString("save","Save"),saveButtonParameterName,saveButtonParameterValue);

		//closes the window
		this.closeButton = new CloseButton(iwrb.getLocalizedString("close","Close"));
		
	}
	/**
	 * lines up text and fields of the window
	 *
	 */
	public void lineUp(IWContext iwc) {
		
		this.mainTable = new Table();
		this.mainTable.setWidth(Table.HUNDRED_PERCENT);
		this.mainTable.setCellspacing(0);
		this.mainTable.setCellpadding(5);
		this.mainTable.setStyleClass(this.mainTableStyle);
		this.mainTable.add(this.nameText,1,1);
		this.mainTable.add(this.nameField,2,1);
		this.mainTable.add(this.coachText,1,2);
		this.mainTable.add(this.coachNameField,2,2);
		this.mainTable.add(this.otherCoachesText,1,3);
		// AttendantChooser is a PresentationObject
		this.mainTable.add(this.otherCoachesNameField,2,3);
		this.mainTable.add(this.groupText,1,4);
		// AttendantChooser is a PresentationObject
		this.mainTable.add(this.groupNameField,2,4);
		this.mainTable.add(this.dateText,1,5);
		this.mainTable.add(this.fromDatePickerField,1,5);
		
		Table buttonTable = new Table();
		buttonTable.setWidth(Table.HUNDRED_PERCENT);
		buttonTable.setCellspacing(0);
		buttonTable.setCellpadding(5);
		buttonTable.setStyleClass(this.mainTableStyle);
		buttonTable.add(getHelp(HELP_TEXT_KEY),1,1);
		buttonTable.setAlignment(2,1,Table.HORIZONTAL_ALIGN_RIGHT);
		buttonTable.add(this.saveButton,2,1);
		buttonTable.add(Text.NON_BREAKING_SPACE,2,1);
		buttonTable.add(this.closeButton,2,1);
		
		Table table = new Table();
		table.setCellspacing(0);
		table.setCellpadding(0);
		table.setWidth(350);
		table.setVerticalAlignment(1,1,Table.VERTICAL_ALIGN_TOP);
		table.setVerticalAlignment(1,3,Table.VERTICAL_ALIGN_TOP);
		table.setHeight(2, 5);
		table.add(this.mainTable,1,1);
		table.add(buttonTable,1,3);
		
		this.form.add(table);
	}
	/**
	 * Saves the ledger which is created by the name of the group and the groupID 
	 * @param iwc - the context
	 * @throws Exception
	 */
	public void saveLedger(IWContext iwc,Page parentPage,String name,int groupID,String coachName,int coachGroupID) throws Exception{
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
		

		String date = iwc.getParameter(dateFieldParameterName);
		
		ledgerVariationsHandler.saveLedger(iwc,parentPage,name,groupID,coachName,coachGroupID,date);		
	}
	
	public void main(IWContext iwc) throws Exception {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		setTitle(iwrb.getLocalizedString("createLedgerWindow.create_ledger","Create Ledger"));
		addTitle(iwrb.getLocalizedString("createLedgerWindow.create_ledger","Create Ledger"),TITLE_STYLECLASS);
		
		this.form = new Form();
		initializeTexts();
		initializeFields();
		
		Integer groupID =null;
		this.groupString =iwc.getParameter(groupFieldParameterName);
		if(this.groupString == null || this.groupString.equals("")) {
			this.groupString = "";
		}
		else {
			this.groupString = this.groupString.substring(this.groupString.lastIndexOf("_")+1);
			groupID = new Integer(this.groupString);
		}
		String coachGroupIDString = iwc.getParameter(otherCoachesFieldParameterName);
		Integer coachGroupID =null;
		if(coachGroupIDString == null || coachGroupIDString.equals("")) {
			coachGroupIDString = "-1";
		}
		else {
			coachGroupIDString = coachGroupIDString.substring(coachGroupIDString.lastIndexOf("_")+1);
			
		}
		coachGroupID = new Integer(coachGroupIDString);	
		String coach = iwc.getParameter(creatorFieldParameterName);
		
		String name = iwc.getParameter(nameFieldParameterName);
		
		lineUp(iwc);
		add(this.form,iwc);
		Page parentPage = getParentPage();
		String save = iwc.getParameter("submit");
		if(save != null && !save.equals("")) {
			saveLedger(iwc,parentPage,name,groupID.intValue(),coach,coachGroupID.intValue());	
//			close();
//			setOnLoad("window.opener.parent.location.reload()");
			
		}		
	}
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	
	public CalBusiness getCalBusiness(IWApplicationContext iwc) {
		if (this.calBiz == null) {
			try {
				this.calBiz = (CalBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, CalBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return this.calBiz;
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
