/*
 * Created on Feb 5, 2004
 */
package com.idega.block.cal.presentation;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;

import com.idega.block.cal.business.CalBusiness;
import com.idega.block.cal.data.CalendarEntry;
import com.idega.block.cal.data.CalendarEntryType;
import com.idega.block.cal.data.CalendarLedger;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.CalendarParameters;
import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DatePicker;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.ResetButton;
import com.idega.presentation.ui.SelectOption;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.TimeInput;
import com.idega.presentation.ui.Window;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.presentation.GroupChooser;
import com.idega.util.IWTimestamp;

/**
 * Description: <br>
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:birna@idega.is">Birna Iris Jonsdottir</a>
 */
public class CalendarEntryCreator extends Window{
	
	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.cal";
	public final static String ENTRY = "entry";
	
	
	//parameter names
	public static String entryIDParameterName ="entryID";
	public static String headlineFieldParameterName = "headline";
	public static String typeFieldParameterName = "type";
	public static String generalFieldParameterName = "general";
	public static String practiceFieldParameterName = "practice";
	public static String repeatFieldParameterName = "repeat";
	public static String noRepeatFieldParameterName = "none";
	public static String dailyFieldParameterName = "daily";
	public static String weeklyFieldParameterName = "weekly";
	public static String monthlyFieldParameterName = "monthly";
	public static String yearlyFieldParameterName = "yearly";
	public static String dayFromFieldParameterName = "dayFrom";
	public static String dayToFieldParameterName = "dayTo";
	public static String timeFromFieldParameterName ="timeFrom";
	public static String timeToFieldParameterName = "timeTo";
	public static String chooseGroupParameterName = "calEntryCreator.choose_group";
	public static String attendeesFieldParameterName = "attendees";
	public static String descriptionFieldParameterName = "description";
	public static String saveButtonParameterName = "save";
	public static String saveButtonParameterValue = "save";
	public static String changeParameterName = "change";
	public static String creatorViewParameterName = "creatorView";
	public static String ledgerFieldParameterName = "ledger";
	public static String locationFieldParameterName = "location";
	public static String createNewEntry = "calendarEntry.create_new";
	public static String noLedgerFieldParameterName = "noLedger";
	
	private String mainTableStyle = "main";
	private String borderAllWhite = "borderAllWhite";
	private String borderBottomStyle = "borderBottom";
	private String styledLinkBox = "styledLinkBox";
	private String boldText = "bold";
	private String headlineFont = "headline";
	
	
	
	//texts
	private Text headlineText;
	private Text typeText;
	private Text repeatText;
	private Text noRepeatText;
	private Text dailyText;
	private Text weeklyText;
	private Text monthlyText;
	private Text yearlyText;
	private Text dayFromText;
	private Text dayToText;
	private Text timeFromText;
	private Text timeToText;
	private Text attendeesText;
	private Text descriptionText;
	private Text generalText;
	private Text practiceText;
	private Text ledgerText;
	private Text locationText;
	private Text createNewText;
	
	//fields
	private TextInput headlineField;
	private DropdownMenu typeField;
	private SelectOption generalField;
	private SelectOption practiceField;
	private DropdownMenu repeatField;
	private SelectOption noRepeatField;
	private SelectOption dailyField;
	private SelectOption weeklyField;
	private SelectOption monthlyField;
	private SelectOption yearlyField;
	private DatePicker dayFromField;
	private DatePicker dayToField;
	private TimeInput timeFromField;
	private TimeInput timeToField;
	private GroupChooser attendeesField;
	private TextArea descriptionField;
	private SubmitButton save;
	private ResetButton reset;
//	private Link save;
	private Link deleteLink;
	private HiddenInput hiddenEntryID;
	private HiddenInput hiddenView;
	private HiddenInput hiddenYear;
	private HiddenInput hiddenMonth;
	private HiddenInput hiddenDay;
	private DropdownMenu ledgerField;
	private TextInput locationField;
	
	//view fields
	private Text viewHeadline;
	private Text viewType;
	private Text viewRepeat;
	private Text viewDayFrom;
	private Text viewDayTo;
	private Text viewTimeFrom;
	private Text viewTimeTo;
	private Text viewAttendees;
	private Text viewDescription;
	private Text viewLedger;
	private Text viewLocation;
	private Link change;
		
	private CalBusiness calBiz;
	private boolean insertPracticeOnly;
	
	private boolean displayingTimeConflict = false;
	
	private String entryIDString = "";
	private boolean entryUpdated = false;
	private Form form;
		
	/**
	 * initializes text
	 * @param iwc
	 */
	public void initializeTexts(IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		headlineText = new Text(iwrb.getLocalizedString(headlineFieldParameterName,"Name"));
		headlineText.setStyleClass(boldText);
		typeText = new Text(iwrb.getLocalizedString(typeFieldParameterName,"Type"));
		typeText.setStyleClass(boldText);
		repeatText =new Text(iwrb.getLocalizedString(repeatFieldParameterName,"Repeat"));
		repeatText.setStyleClass(boldText);
		noRepeatText = new Text(iwrb.getLocalizedString(noRepeatFieldParameterName,"No Repeat"));
		noRepeatText.setStyleClass(boldText);
		dailyText = new Text(iwrb.getLocalizedString(dailyFieldParameterName,"Daily"));
		dailyText.setStyleClass(boldText);
		weeklyText = new Text(iwrb.getLocalizedString(weeklyFieldParameterName,"Weekly"));
		weeklyText.setStyleClass(boldText);
		monthlyText = new Text(iwrb.getLocalizedString(monthlyFieldParameterName,"Monthly"));
		monthlyText.setStyleClass(boldText);
		yearlyText = new Text(iwrb.getLocalizedString(yearlyFieldParameterName,"Yearly"));
		yearlyText.setStyleClass(boldText);
		dayFromText = new Text(iwrb.getLocalizedString(dayFromFieldParameterName,"From day"));
		dayFromText.setStyleClass(boldText);
		dayToText = new Text(iwrb.getLocalizedString(dayToFieldParameterName,"To Day"));
		dayToText.setStyleClass(boldText);
		timeFromText = new Text(iwrb.getLocalizedString(timeFromFieldParameterName, "From time"));
		timeFromText.setStyleClass(boldText);
		timeToText = new Text(iwrb.getLocalizedString(timeToFieldParameterName, "To time"));
		timeToText.setStyleClass(boldText);
		attendeesText = new Text(iwrb.getLocalizedString(chooseGroupParameterName,"Choose group"));
		attendeesText.setStyleClass(boldText);
		descriptionText = new Text(iwrb.getLocalizedString(descriptionFieldParameterName,"Description"));
		descriptionText.setStyleClass(boldText);
		generalText = new Text(iwrb.getLocalizedString(generalFieldParameterName,"General"));
		generalText.setStyleClass(boldText);
		practiceText = new Text(iwrb.getLocalizedString(practiceFieldParameterName, "Practice"));
		practiceText.setStyleClass(boldText);
		ledgerText = new Text(iwrb.getLocalizedString(ledgerFieldParameterName, "Ledger"));
		ledgerText.setStyleClass(boldText);
		locationText = new Text(iwrb.getLocalizedString(locationFieldParameterName, "Location"));
		locationText.setStyleClass(boldText);
		createNewText = new Text(iwrb.getLocalizedString(createNewEntry, "Create new entry"));
		createNewText.setStyleClass(headlineFont);
		
	}
	/**
	 * initialized fields
	 * @param iwc
	 */
	public void initializeFields(IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		CalBusiness calBusiness = getCalBusiness(iwc);
		CalendarEntry entry = null;

		entryIDString = iwc.getParameter(entryIDParameterName);
		if(entryIDString == null || entryIDString.equals("")) {
			entryIDString = "";
		}
		else {
			Integer entryID = new Integer(entryIDString);
			entry = getCalBusiness(iwc).getEntry(entryID.intValue());
			
		}
				
		headlineField = new TextInput(headlineFieldParameterName);
		
		generalField =new SelectOption(generalText.toString(),practiceFieldParameterName);
		practiceField =new SelectOption(practiceText.toString(),generalFieldParameterName);
		
		typeField = new DropdownMenu(typeFieldParameterName);
		
		//if the general and practice types have not been added -> they are added!
		if(calBusiness.getEntryTypeByName(generalFieldParameterName) == null) {
			calBusiness.createNewEntryType(generalFieldParameterName);			
		}
		if(calBusiness.getEntryTypeByName(practiceFieldParameterName) == null) {
			calBusiness.createNewEntryType(practiceFieldParameterName);
		}		

		CalBusiness calBiz = getCalBusiness(iwc);
		Iterator typeIter = calBiz.getAllEntryTypes().iterator();
		while(typeIter.hasNext()) {
			CalendarEntryType type = (CalendarEntryType) typeIter.next();
			if(!insertPracticeOnly) {
				typeField.addMenuElement(type.getName(),type.getName());
			}
			else {
				if(type.getName().equals(practiceFieldParameterName)) {
					typeField.addMenuElement(type.getName(),type.getName());
				}
			}
			
		}
		ledgerField = new DropdownMenu(ledgerFieldParameterName);
		ledgerField.addMenuElement(-1,iwrb.getLocalizedString(noLedgerFieldParameterName,"No ledger"));
		
		Iterator ledgerIter = calBiz.getAllLedgers().iterator();
		while(ledgerIter.hasNext()) {
			CalendarLedger ledger = (CalendarLedger) ledgerIter.next();
			ledgerField.addMenuElement(ledger.getLedgerID(),ledger.getName());
		}
		locationField = new TextInput(locationFieldParameterName);
		
		noRepeatField = new SelectOption(noRepeatText.toString(),noRepeatFieldParameterName);
		dailyField = new SelectOption(dailyText.toString(),dailyFieldParameterName);
		weeklyField =new SelectOption(weeklyText.toString(),weeklyFieldParameterName);
		monthlyField = new SelectOption(monthlyText.toString(),monthlyFieldParameterName);
		yearlyField = new SelectOption(yearlyText.toString(),yearlyFieldParameterName);
		
		repeatField = new DropdownMenu(repeatFieldParameterName);
		repeatField.addOption(noRepeatField);
		repeatField.addOption(dailyField);
		repeatField.addOption(weeklyField);
		repeatField.addOption(monthlyField);
		repeatField.addOption(yearlyField);
		
		IWTimestamp stamp = new IWTimestamp();
		
		String day = iwc.getParameter(CalendarParameters.PARAMETER_DAY);
		String month = iwc.getParameter(CalendarParameters.PARAMETER_MONTH);
		String year = iwc.getParameter(CalendarParameters.PARAMETER_YEAR);
		String view = iwc.getParameter(CalendarParameters.PARAMETER_VIEW);
		int v = 0;
		if(view != null && !view.equals("")) {
			v = Integer.parseInt(view);
		}
		dayFromField = new DatePicker(dayFromFieldParameterName);		
		dayToField = new DatePicker(dayToFieldParameterName);		
		if(day != null && month != null && year != null && 
				v == CalendarParameters.DAY) {
			//month is month - 1 -> January == 0!!
			Date date = new Date(Integer.parseInt(year),Integer.parseInt(month)-1,Integer.parseInt(day));
			dayFromField.setDate(date);
			dayToField.setDate(date);
		}
		else {
			dayFromField.setDate(stamp.getDate());
			dayToField.setDate(stamp.getDate());
		}
		timeFromField = new TimeInput(timeFromFieldParameterName);
		//the default from time is set to the current time
		timeFromField.setHour(stamp.getHour());
		timeFromField.setMinute(stamp.getMinute());
		timeToField = new TimeInput(timeToFieldParameterName);
		//the default to time is set one hour later than the current time
		if(stamp.getHour() == 23) {
			timeToField.setHour(0);
		}
		else {
			timeToField.setHour(stamp.getHour() + 1);
		}		
		timeToField.setMinute(stamp.getMinute());
		
		attendeesField = new GroupChooser(attendeesFieldParameterName);

		descriptionField = new TextArea(descriptionFieldParameterName);
		
		save = new SubmitButton(iwrb.getLocalizedString("save", "Save"),saveButtonParameterName,saveButtonParameterValue);
		reset = new ResetButton(iwrb.getLocalizedString("reset", "Reset"));
		
		hiddenEntryID = new HiddenInput(entryIDParameterName,iwc.getParameter(entryIDParameterName));		
		hiddenView = new HiddenInput(CalendarParameters.PARAMETER_VIEW,iwc.getParameter(CalendarParameters.PARAMETER_VIEW));
		hiddenYear =new HiddenInput(CalendarParameters.PARAMETER_YEAR,new Integer(stamp.getYear()).toString());//iwc.getParameter(CalendarParameters.PARAMETER_YEAR));
		hiddenMonth = new HiddenInput(CalendarParameters.PARAMETER_MONTH,new Integer(stamp.getMonth()).toString());//iwc.getParameter(CalendarParameters.PARAMETER_MONTH));
		hiddenDay = new HiddenInput(CalendarParameters.PARAMETER_DAY,new Integer(stamp.getDay()).toString());//iwc.getParameter(CalendarParameters.PARAMETER_DAY));
		
		
		//if some entry is selected, data is printed in the fields
		if(entryIDString != null && !entryIDString.equals("") && !entryUpdated) {
			
			deleteLink = new Link(iwrb.getLocalizedString("delete","Delete"));
			deleteLink.setWindowToOpen(ConfirmDeleteWindow.class);
			deleteLink.addParameter(ConfirmDeleteWindow.PRM_DELETE_ID, entryIDString);
			deleteLink.addParameter(ConfirmDeleteWindow.PRM_DELETE, CalendarParameters.PARAMETER_TRUE);
			deleteLink.addParameter(ConfirmDeleteWindow.PRM_ENTRY_OR_LEDGER,ENTRY);
			deleteLink.addParameter(CalendarView.ACTION,"");
			deleteLink.setAsImageButton(true,true);
						
			
			headlineField.setContent(entry.getName());
			typeField.setSelectedElement(entry.getEntryType());
			repeatField.setSelectedElement(entry.getRepeat());
			ledgerField.setSelectedElement(entry.getLedgerID());
			
			Integer groupID = new Integer(entry.getGroupID());
			try {
				if(groupID.intValue() != -1) {
					attendeesField.setSelectedGroup(groupID.toString(),getGroupBusiness(iwc).getGroupByGroupID(groupID.intValue()).getName());
				}
			}catch (Exception e){
				e.printStackTrace();
			}
									
			
			IWTimestamp iwFrom = new IWTimestamp(entry.getDate());
			Date dateF = iwFrom.getDate();
			dayFromField.setDate(dateF);
			
			IWTimestamp iwTo = new IWTimestamp(entry.getEndDate());
			Date dateT = iwTo.getDate();
			dayToField.setDate(dateT);
			
			timeFromField.setHour(entry.getDate().getHours());
			timeFromField.setMinute(entry.getDate().getMinutes());
			
			timeToField.setHour(entry.getEndDate().getHours());
			timeToField.setMinute(entry.getEndDate().getMinutes());	
			
			locationField.setContent(entry.getLocation());
			
			descriptionField.setContent(entry.getDescription());
			
		}		
		else if(displayingTimeConflict) {
			String entryAttendees = iwc.getParameter(attendeesFieldParameterName);
			if(entryAttendees == null || entryAttendees.equals(""))
				entryAttendees = "";
			else {
				entryAttendees = entryAttendees.substring(entryAttendees.lastIndexOf("_")+1);
			}
			
			if(entryAttendees != null && !entryAttendees.equals("")) {
				Integer groupID = new Integer(entryAttendees);
				try {
					attendeesField.setSelectedGroup(groupID.toString(),getGroupBusiness(iwc).getGroupByGroupID(groupID.intValue()).getName());
				}catch (Exception e){
					e.printStackTrace();
				}
			}
			headlineField.setContent(iwc.getParameter(headlineFieldParameterName));
			typeField.setSelectedElement(iwc.getParameter(typeFieldParameterName));
			repeatField.setSelectedElement(iwc.getParameter(repeatFieldParameterName));
			ledgerField.setSelectedElement(iwc.getParameter(ledgerFieldParameterName));
			locationField.setContent(iwc.getParameter(locationFieldParameterName));
			descriptionField.setContent(iwc.getParameter(descriptionFieldParameterName));
		}
	}
	
	/**
	 * lines up the gui - for editing entries
	 * @return form containing the gui table
	 */
	public Form lineUpEdit(IWContext iwc) {
		form = new Form();
		Table table = new Table();
		table.setStyleClass(mainTableStyle);
		table.setCellspacing(0);
		table.setCellpadding(2);
		table.mergeCells(1,1,4,1);
//		table.setAlignment(1,1,"center");
		table.setStyleClass(1,1,borderBottomStyle);
		table.add(createNewText,1,1);
		table.add(headlineText,1,2);
		table.add(headlineField,1,3);
		table.add(typeText,1,4);
		table.add(typeField,1,5);
		table.add(repeatText,1,6);
		table.add(repeatField,1,7);
		table.add(attendeesText,1,8);
		table.add(attendeesField,1,9);
		table.add(ledgerText,1,10);
		table.add(ledgerField,1,11);
		table.add(dayFromText,1,12);
		table.add(dayFromField,1,13);
		table.add(timeFromText,2,12);
		table.add(timeFromField,2,13);
		table.add(dayToText,1,14);
		table.add(dayToField,1,15);
		table.add(timeToText,2,14);
		table.add(timeToField,2,15);
		table.add(locationText,1,16);
		table.add(locationField,1,17);
		table.add(descriptionText,1,18);
		table.add(descriptionField,1,19);
		table.setAlignment(4,20,"right");		
		table.add(save,4,20);
		table.add(Text.NON_BREAKING_SPACE,4,20);
		table.add(reset,4,20);
		String entryIDString = iwc.getParameter(entryIDParameterName);
		if(entryIDString != null && !entryIDString.equals("")) {
			table.add(Text.NON_BREAKING_SPACE,4,20);
			table.add(deleteLink,4,20);
			form.add(hiddenEntryID);
		}
		//the hidden inputs are added to maintain parameters
		 
		form.add(hiddenView);
		form.add(hiddenYear);
		form.add(hiddenMonth);
		form.add(hiddenDay);
		form.add(table);
		return form;
	}
	/**
	 * Saves an entry to the calendar. Either updates it or creates a new one. 
	 * @param iwc
	 */
	public void saveEntry(IWContext iwc,Page parentPage) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		CalBusiness calBus = getCalBusiness(iwc);

		String entryHeadline = iwc.getParameter(headlineFieldParameterName);
		String entryType = iwc.getParameter(typeFieldParameterName);
		String entryRepeat = iwc.getParameter(repeatFieldParameterName);
		String entryDate = iwc.getParameter(dayFromFieldParameterName);
		String entryTimeHour = iwc.getParameter(timeFromFieldParameterName + "_hour");
		String entryTimeMinute = iwc.getParameter(timeFromFieldParameterName + "_minute");
		String entryEndDate = iwc.getParameter(dayToFieldParameterName);
		String entryEndTimeHour = iwc.getParameter(timeToFieldParameterName + "_hour");
		String entryEndTimeMinute = iwc.getParameter(timeToFieldParameterName + "_minute");
		String entryAttendees = iwc.getParameter(attendeesFieldParameterName);
		String entryLedger = iwc.getParameter(ledgerFieldParameterName);
		String entryDescription = iwc.getParameter(descriptionFieldParameterName);
		String entryLocation = iwc.getParameter(locationFieldParameterName);
		String entryID = iwc.getParameter(entryIDParameterName);
		
		if(entryAttendees == null || entryAttendees.equals(""))
			entryAttendees = "";
		else {
			entryAttendees = entryAttendees.substring(entryAttendees.lastIndexOf("_")+1);
		}

		Timestamp from = Timestamp.valueOf(entryDate);
		Timestamp to = Timestamp.valueOf(entryEndDate);
		from.setHours(Integer.parseInt(entryTimeHour));
		from.setMinutes(Integer.parseInt(entryTimeMinute));
		to.setHours(Integer.parseInt(entryEndTimeHour));
		to.setMinutes(Integer.parseInt(entryEndTimeMinute));
		
		int ledgerID;
		Timestamp ledStartTime = from;

		if(entryLedger != null && !entryLedger.equals("") && !entryLedger.equals("-1")) {
			ledgerID = Integer.parseInt(entryLedger);
			CalendarLedger ledger = getCalBusiness(iwc).getLedger(ledgerID);
			ledStartTime = ledger.getDate();
		}
		
		
		if(from.compareTo(to)>0) {
			parentPage.setAlertOnLoad(iwrb.getLocalizedString("calEntryCreator.to_date_before_from_date_message","The from day is later than the to day!"));
			displayingTimeConflict = true;
		}
		
		else if(from.compareTo(ledStartTime)<0) {
			parentPage.setAlertOnLoad(iwrb.getLocalizedString("calEntryCreator.from_date_before_led_start_date_message","The from day is before the ledgers start day!"));
			displayingTimeConflict = true;
		}
			
								
		else {
			if(entryID != null && !entryID.equals("")) {
				calBus.updateEntry(entryID,entryHeadline, entryType, entryRepeat, entryDate,entryTimeHour, entryTimeMinute, entryEndDate, entryEndTimeHour, entryEndTimeMinute, entryAttendees, entryLedger, entryDescription, entryLocation);
				entryUpdated = true;
				
			}
			else {
				calBus.createNewEntry(entryHeadline, entryType, entryRepeat, entryDate,entryTimeHour, entryTimeMinute, entryEndDate, entryEndTimeHour, entryEndTimeMinute, entryAttendees, entryLedger, entryDescription, entryLocation);			
			}
		}
			
	}
	public void main(IWContext iwc) {
		initializeTexts(iwc);
		initializeFields(iwc);		

		add(lineUpEdit(iwc));
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
	
	

}
