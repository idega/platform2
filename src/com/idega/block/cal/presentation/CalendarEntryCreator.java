/*
 * Created on Feb 5, 2004
 */
package com.idega.block.cal.presentation;

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
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DatePicker;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SelectOption;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.TimeInput;
import com.idega.presentation.ui.Window;
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
	public static String attendeesFieldParameterName = "attendees";
	public static String descriptionFieldParameterName = "description";
	public static String saveButtonParameterName = "save";
	public static String saveButtonParameterValue = "save";
	public static String changeParameterName = "change";
	public static String creatorViewParameterName = "creatorView";
	public static String ledgerFieldParameterName = "ledger";
	
	private String mainTableStyle = "main";
	private String styledLinkBox = "styledLinkBox";
	
	
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
	private Link deleteLink;
	private HiddenInput hiddenEntryID;
	private HiddenInput hiddenView;
	private HiddenInput hiddenYear;
	private HiddenInput hiddenMonth;
	private HiddenInput hiddenDay;
	private DropdownMenu ledgerField;
	
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
	private Link change;
		
	private CalBusiness calBiz;
	private boolean insertPracticeOnly;
		
	/**
	 * initializes text
	 * @param iwc
	 */
	public void initializeTexts(IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		headlineText = new Text(iwrb.getLocalizedString(headlineFieldParameterName,"Name"));
		typeText = new Text(iwrb.getLocalizedString(typeFieldParameterName,"Type"));
		repeatText =new Text(iwrb.getLocalizedString(repeatFieldParameterName,"Repeat"));
		noRepeatText = new Text(iwrb.getLocalizedString(noRepeatFieldParameterName,"No Repeat"));
		dailyText = new Text(iwrb.getLocalizedString(dailyFieldParameterName,"Daily"));
		weeklyText = new Text(iwrb.getLocalizedString(weeklyFieldParameterName,"Weekly"));
		monthlyText = new Text(iwrb.getLocalizedString(monthlyFieldParameterName,"Monthly"));
		yearlyText = new Text(iwrb.getLocalizedString(yearlyFieldParameterName,"Yearly"));
		dayFromText = new Text(iwrb.getLocalizedString(dayFromFieldParameterName,"From day"));
		dayToText = new Text(iwrb.getLocalizedString(dayToFieldParameterName,"To Day"));
		timeFromText = new Text(iwrb.getLocalizedString(timeFromFieldParameterName, "From time"));
		timeToText = new Text(iwrb.getLocalizedString(timeToFieldParameterName, "To time"));
		attendeesText = new Text(iwrb.getLocalizedString(attendeesFieldParameterName,"Attendees"));
		descriptionText = new Text(iwrb.getLocalizedString(descriptionFieldParameterName,"Description"));
		generalText = new Text(iwrb.getLocalizedString(generalFieldParameterName,"General"));
		practiceText = new Text(iwrb.getLocalizedString(practiceFieldParameterName, "Practice"));		
	}
	/**
	 * initialized fields
	 * @param iwc
	 */
	public void initializeFields(IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		CalBusiness calBusiness = getCalBusiness(iwc);
		CalendarEntry entry = null;

		String entryIDString = iwc.getParameter(entryIDParameterName);
		if(entryIDString == null || entryIDString.equals("")) {
			entryIDString = "";
		}
		else {
			Integer entryID = new Integer(entryIDString);
			entry = getCalBusiness(iwc).getEntry(entryID.intValue());
			
		}
				
		headlineField = new TextInput(headlineFieldParameterName);
		
		generalField =new SelectOption(generalText.toString(),generalFieldParameterName);
		practiceField =new SelectOption(practiceText.toString(),practiceFieldParameterName);
		
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
		
		Iterator ledgerIter = calBiz.getAllLedgers().iterator();
		while(ledgerIter.hasNext()) {
			CalendarLedger ledger = (CalendarLedger) ledgerIter.next();
			ledgerField.addMenuElement(ledger.getLedgerID(),ledger.getName());
		}
		
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
		
		dayFromField = new DatePicker(dayFromFieldParameterName);
		dayToField = new DatePicker(dayToFieldParameterName);
		
		IWTimestamp stamp = new IWTimestamp();
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
		//TODO: add groups to menu and add a ledgerChooser
			
		descriptionField = new TextArea(descriptionFieldParameterName);
		
		save = new SubmitButton(iwrb.getLocalizedString("save", "Save"),saveButtonParameterName,saveButtonParameterValue);
		
		hiddenEntryID = new HiddenInput(entryIDParameterName,iwc.getParameter(entryIDParameterName));		
		hiddenView = new HiddenInput(CalendarParameters.PARAMETER_VIEW,iwc.getParameter(CalendarParameters.PARAMETER_VIEW));
		hiddenYear =new HiddenInput(CalendarParameters.PARAMETER_YEAR,new Integer(stamp.getYear()).toString());//iwc.getParameter(CalendarParameters.PARAMETER_YEAR));
		hiddenMonth = new HiddenInput(CalendarParameters.PARAMETER_MONTH,new Integer(stamp.getMonth()).toString());//iwc.getParameter(CalendarParameters.PARAMETER_MONTH));
		hiddenDay = new HiddenInput(CalendarParameters.PARAMETER_DAY,new Integer(stamp.getDay()).toString());//iwc.getParameter(CalendarParameters.PARAMETER_DAY));
		
		
		//if some entry is selected, data is printed in the fields
		if(entryIDString != null && !entryIDString.equals("")) {
			
			deleteLink = new Link("Delete");
			deleteLink.setWindowToOpen(ConfirmDeleteWindow.class);
			deleteLink.addParameter(ConfirmDeleteWindow.PRM_DELETE_ID, entryIDString);
			deleteLink.addParameter(ConfirmDeleteWindow.PRM_DELETE, CalendarParameters.PARAMETER_TRUE);
			deleteLink.addParameter(CalendarView.ACTION,"");
			deleteLink.setAsImageTab(true,true);
						
			
			headlineField.setContent(entry.getName());
			typeField.setSelectedElement(entry.getEntryType());
			repeatField.setSelectedElement(entry.getRepeat());
			
			Date dateF = new Date();
			dateF.setDate(entry.getDate().getDay());
			dateF.setMonth(entry.getDate().getMonth());
			dateF.setYear(entry.getDate().getYear());
			dayFromField.setDate(dateF);
			
			Date dateT = new Date();
			dateT.setDate(entry.getEndDate().getDay());
			dateT.setMonth(entry.getEndDate().getMonth());
			dateT.setYear(entry.getEndDate().getYear());
			dayToField.setDate(dateT);
			
			timeFromField.setHour(entry.getDate().getHours());
			timeFromField.setMinute(entry.getDate().getMinutes());
			
			timeToField.setHour(entry.getEndDate().getHours());
			timeToField.setMinute(entry.getEndDate().getMinutes());	
			
			descriptionField.setContent(entry.getDescription());				
		}		
	}
	
	public void initializeViewFields(IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		int view = CalendarParameters.MONTH;
		if (iwc.getParameter(CalendarParameters.PARAMETER_VIEW) != null) {
			view = Integer.parseInt(iwc.getParameter(CalendarParameters.PARAMETER_VIEW));
		}
		String entryIDString = iwc.getParameter(entryIDParameterName);
		
		Integer entryID = new Integer(entryIDString);
		CalendarEntry entry = getCalBusiness(iwc).getEntry(entryID.intValue());
		
		viewHeadline = new Text(entry.getName());
		viewType = new Text(entry.getEntryTypeName());
		viewRepeat = new Text(entry.getRepeat());
		viewDayFrom = new Text(entry.getDate().toString());
		viewDayTo = new Text(entry.getEndDate().toString());
		viewTimeFrom = new Text();
		viewTimeTo = new Text();
		viewAttendees = new Text();
		viewDescription = new Text(entry.getDescription());
		
		
		//stamp is needed to get the current day/week/month of the CalendarView 
		//the parameters are then set to the change link (see below)
		IWTimestamp stamp = null;
		if (stamp == null) {
			String day = iwc.getParameter(CalendarParameters.PARAMETER_DAY);
			String month = iwc.getParameter(CalendarParameters.PARAMETER_MONTH);
			String year = iwc.getParameter(CalendarParameters.PARAMETER_YEAR);

			if(month == null || month.length() == 0 &&
					day == null &&
					year == null || year.length() == 0) {
				stamp = IWTimestamp.RightNow();
			}
			else {
				stamp = CalendarView.getTimestamp(day,month,year);
			}
		}
		//change is the link from the view mode of the CalendarEntryCreator to the modifying view
		change = new Link(iwrb.getLocalizedString(changeParameterName, "Change"));
		change.addParameter(creatorViewParameterName,"");
		change.addParameter(entryIDParameterName, entryIDString);
		change.addParameter(CalendarView.ACTION,CalendarView.OPEN);
		change.addParameter(CalendarParameters.PARAMETER_VIEW,view);
		change.addParameter(CalendarParameters.PARAMETER_YEAR, stamp.getYear());
		change.addParameter(CalendarParameters.PARAMETER_MONTH, stamp.getMonth());
		change.addParameter(CalendarParameters.PARAMETER_DAY, stamp.getDay());
		change.setAsImageTab(true,true);
//		change.setStyleClass(styledLinkBox);
		
		
	}
	/**
	 * lines up the gui - for editing entries
	 * @return form containing the gui table
	 */
	public Form lineUpEdit(IWContext iwc) {
		Form form = new Form();
		Table table = new Table();
		table.setStyleClass(mainTableStyle);
		table.setCellspacing(0);
		table.setCellpadding(0);
		table.add(headlineText,1,1);
		table.add(headlineField,2,1);
		table.add(typeText,1,2);
		table.add(typeField,2,2);
		table.add(repeatText,1,3);
		table.add(repeatField,2,3);
		table.add(attendeesText,1,4);
		table.add(attendeesField,2,4);
		table.add("<br>",2,4);
		table.add(ledgerField,2,4);
		table.add(dayFromText,1,5);
		table.add(dayFromField,2,5);
		table.add(timeFromText,3,5);
		table.add(timeFromField,4,5);
		table.add(dayToText,1,6);
		table.add(dayToField,2,6);
		table.add(timeToText,3,6);
		table.add(timeToField,4,6);
		table.add(descriptionText,1,7);
		table.add(descriptionField,2,7);
		table.setAlignment(4,8,"right");		
		table.add(save,4,8);
		String entryIDString = iwc.getParameter(entryIDParameterName);
		if(entryIDString != null && !entryIDString.equals("")) {
			table.add(Text.NON_BREAKING_SPACE,4,8);
			table.add(deleteLink,4,8);
		}
		//the hidden inputs are added to maintain parameters
		form.add(hiddenEntryID); 
		form.add(hiddenView);
		form.add(hiddenYear);
		form.add(hiddenMonth);
		form.add(hiddenDay);
		form.add(table);
		return form;
	}
	public Table lineUpView() {
		
		Table table = new Table();
		table.setStyleClass(mainTableStyle);
		table.setCellspacing(0);
		table.setCellpadding(0);
		table.add(headlineText,1,1);
		table.add(viewHeadline,2,1);
		table.add(typeText,1,2);
		table.add(viewType,2,2);
		table.add(repeatText,1,3);
		table.add(viewRepeat,2,3);
		table.add(dayFromText,1,4);
		table.add(viewDayFrom,2,4);
		table.add(dayToText,1,5);
		table.add(viewDayTo,2,5);
		table.add(descriptionText,1,6);
		table.add(viewDescription,2,6);
		table.setAlignment(2,7,"right");
		table.add(change,2,7);
		return table;
	}
	/**
	 * Saves an entry to the calendar. Either updates it or creates a new one. 
	 * @param iwc
	 */
	public void saveEntry(IWContext iwc) {
		
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
		System.out.println("entryAttendees: " + entryAttendees);
		String entryLedger = iwc.getParameter(ledgerFieldParameterName);
		String entryDescription = iwc.getParameter(descriptionFieldParameterName);
	
		String entryID = iwc.getParameter(entryIDParameterName);
		if(entryID != null && !entryID.equals("")) {
			calBus.updateEntry(entryID,entryHeadline, entryType, entryRepeat, entryDate,entryTimeHour, entryTimeMinute, entryEndDate, entryEndTimeHour, entryEndTimeMinute, entryAttendees, entryDescription);
		}
		else {
			calBus.createNewEntry(entryHeadline, entryType, entryRepeat, entryDate,entryTimeHour, entryTimeMinute, entryEndDate, entryEndTimeHour, entryEndTimeMinute, entryAttendees, entryLedger, entryDescription);			
		}		
	}
	public void main(IWContext iwc) {
//		CalendarBusiness calBiz = getCalendarBusiness(iwc);
//		Iterator typeIter = calBiz.getAllEntryTypes().iterator();
//		while(typeIter.hasNext()) {
//			CalendarEntryType type = (CalendarEntryType) typeIter.next();
//			Integer i = (Integer) type.getPrimaryKey();
//			int ii = i.intValue();
//			calBiz.deleteEntryType(ii);
//		}
		initializeTexts(iwc);
		String creatorView = iwc.getParameter(creatorViewParameterName);

//		String delete = iwc.getParameter(ConfirmDeleteWindow.PRM_DELETE);
//		String entryIDString = iwc.getParameter(entryIDParameterName);
//		if(entryIDString != null && !entryIDString.equals("")) {
//			Integer entryID = new Integer(entryIDString);
//			if(delete == CalendarParameters.PARAMETER_TRUE) {
//				getCalBusiness(iwc).deleteEntry(entryID.intValue());
//			}
			
//		}
		if(creatorView == null || creatorView.equals("")) {
			initializeFields(iwc);
			add(lineUpEdit(iwc));
		}
		else {
			initializeViewFields(iwc);
			add(lineUpView());
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

}
