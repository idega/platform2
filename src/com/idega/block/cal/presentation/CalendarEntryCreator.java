/*
 * Created on Feb 5, 2004
 */
package com.idega.block.cal.presentation;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.Date;
import java.util.Iterator;
import java.util.List;
import com.idega.block.cal.business.CalBusiness;
import com.idega.block.cal.data.CalendarEntry;
import com.idega.block.cal.data.CalendarEntryType;
import com.idega.block.cal.data.CalendarLedger;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.CalendarParameters;
import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DatePicker;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.ResetButton;
import com.idega.presentation.ui.SelectOption;
import com.idega.presentation.ui.StyledButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.TimeInput;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.user.presentation.GroupChooser;
import com.idega.util.IWTimestamp;

/**
 * Description: <br>
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:birna@idega.is">Birna Iris Jonsdottir</a>
 */
public class CalendarEntryCreator extends Form{
	
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
	public static String createNewEntryParameterName = "calendarEntry.create_new";
	public static String changeEntryParameterName = "calendarEntry.change_entry";
	public static String noLedgerFieldParameterName = "noLedger";
	public static String modifyOneOrManyRadioButtonParameterName = "oneOrManyRadioButton";
	public static String modifyOneRadioButtonParameterName = "oneRadioButton";
	public static String modifyManyRadioButtonParameterName = "manyRadioButton";
	public static String groupOrLedger = "calendarEntry.group_or_ledger";
	public static String oneValue = "one";
	public static String manyValue = "many";
	
//	private static final String HELP_TEXT_KEY = "create_entry";
	
//	public static boolean isEntrySet = false;
	
	private String mainTableStyle = "main";
	private String borderAllWhite = "borderAllWhite";
	private String borderBottomStyle = "borderBottom";
	private String styledLinkBox = "styledLinkBox";
	private String styledLink = "styledLink";
	private String boldText = "bold";
	private String headlineFont = "headline";
	private String menuTableStyle = "menu";
	
	private String oneOrMany = "one";
		
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
	private Text changeEntryText;
	private Text modifyOneText;
	private Text modifyManyText;
	private Text groupOrLedgerText;
	
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
	private StyledButton save;
	private StyledButton reset;
//	private Link save;
	private Link deleteLink;
	private HiddenInput hiddenEntryID;
	private HiddenInput hiddenView;
	private HiddenInput hiddenYear;
	private HiddenInput hiddenMonth;
	private HiddenInput hiddenDay;
	private HiddenInput hiddenOneMany;
	private DropdownMenu ledgerField;
	private TextInput locationField;
	private Link newEntryLink;
	private RadioButton modifyOneRadioButton;
	private RadioButton modifyManyRadioButton;
			
	private CalBusiness calBiz;
	private boolean insertPracticeOnly;
	
	private boolean displayingTimeConflict = false;
	
	private String entryIDString = "";
//	private Form form;
		
	/**
	 * initializes text
	 * @param iwc
	 */
	private void initializeTexts(IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		this.headlineText = new Text(iwrb.getLocalizedString(headlineFieldParameterName,"Name"));
		this.headlineText.setStyleClass(this.boldText);
		this.typeText = new Text(iwrb.getLocalizedString(typeFieldParameterName,"Type"));
		this.typeText.setStyleClass(this.boldText);
		this.repeatText =new Text(iwrb.getLocalizedString(repeatFieldParameterName,"Repeat"));
		this.repeatText.setStyleClass(this.boldText);
		this.noRepeatText = new Text(iwrb.getLocalizedString(noRepeatFieldParameterName,"No Repeat"));
		this.noRepeatText.setStyleClass(this.boldText);
		this.dailyText = new Text(iwrb.getLocalizedString(dailyFieldParameterName,"Daily"));
		this.dailyText.setStyleClass(this.boldText);
		this.weeklyText = new Text(iwrb.getLocalizedString(weeklyFieldParameterName,"Weekly"));
		this.weeklyText.setStyleClass(this.boldText);
		this.monthlyText = new Text(iwrb.getLocalizedString(monthlyFieldParameterName,"Monthly"));
		this.monthlyText.setStyleClass(this.boldText);
		this.yearlyText = new Text(iwrb.getLocalizedString(yearlyFieldParameterName,"Yearly"));
		this.yearlyText.setStyleClass(this.boldText);
		this.dayFromText = new Text(iwrb.getLocalizedString(dayFromFieldParameterName,"From day"));
		this.dayFromText.setStyleClass(this.boldText);
		this.dayToText = new Text(iwrb.getLocalizedString(dayToFieldParameterName,"To Day"));
		this.dayToText.setStyleClass(this.boldText);
		this.timeFromText = new Text(iwrb.getLocalizedString(timeFromFieldParameterName, "From time"));
		this.timeFromText.setStyleClass(this.boldText);
		this.timeToText = new Text(iwrb.getLocalizedString(timeToFieldParameterName, "To time"));
		this.timeToText.setStyleClass(this.boldText);
		this.attendeesText = new Text(iwrb.getLocalizedString(chooseGroupParameterName,"Choose group"));
		this.attendeesText.setStyleClass(this.boldText);
		this.descriptionText = new Text(iwrb.getLocalizedString(descriptionFieldParameterName,"Description"));
		this.descriptionText.setStyleClass(this.boldText);
		this.generalText = new Text(iwrb.getLocalizedString(generalFieldParameterName,"General"));
		this.generalText.setStyleClass(this.boldText);
		this.practiceText = new Text(iwrb.getLocalizedString(practiceFieldParameterName, "Practice"));
		this.practiceText.setStyleClass(this.boldText);
		this.ledgerText = new Text(iwrb.getLocalizedString(ledgerFieldParameterName, "Ledger"));
		this.ledgerText.setStyleClass(this.boldText);
		this.locationText = new Text(iwrb.getLocalizedString(locationFieldParameterName, "Location"));
		this.locationText.setStyleClass(this.boldText);
		this.createNewText = new Text(iwrb.getLocalizedString(createNewEntryParameterName, "Create new entry"));
		this.createNewText.setStyleClass(this.headlineFont);
		this.createNewText.setBold();
		this.changeEntryText = new Text(iwrb.getLocalizedString(changeEntryParameterName, "Change entry"));
		this.changeEntryText.setStyleClass(this.headlineFont);
		this.changeEntryText.setBold();
		this.modifyOneText = new Text(iwrb.getLocalizedString(modifyOneRadioButtonParameterName, "Change a single entry"));
		this.modifyOneText.setStyleClass(this.boldText);
		this.modifyManyText = new Text(iwrb.getLocalizedString(modifyManyRadioButtonParameterName, "Change group of entries"));
		this.modifyManyText.setStyleClass(this.boldText);
		this.groupOrLedgerText = new Text(iwrb.getLocalizedString(groupOrLedger,"Save entry for group or ledger?"));
		this.groupOrLedgerText.setStyleClass(this.boldText);
	}
	/**
	 * initialized fields
	 * @param iwc
	 */
	private void initializeFields(IWContext iwc, CalendarEntry entry) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		CalBusiness calBusiness = getCalBusiness(iwc);
				
		this.headlineField = new TextInput(headlineFieldParameterName);
		
		this.practiceField =new SelectOption(this.practiceText.toString(),generalFieldParameterName);
		this.generalField =new SelectOption(this.generalText.toString(),practiceFieldParameterName);
		
		
		this.typeField = new DropdownMenu(typeFieldParameterName);
		
		
		//if the general and practice types have not been added -> they are added!
		if(calBusiness.getEntryTypeByName(practiceFieldParameterName) == null) {
			calBusiness.createNewEntryType(practiceFieldParameterName);
		}	
		if(calBusiness.getEntryTypeByName(generalFieldParameterName) == null) {
			calBusiness.createNewEntryType(generalFieldParameterName);			
		}
			

		CalBusiness calBiz = getCalBusiness(iwc);
		Iterator typeIter = calBiz.getAllEntryTypes().iterator();
		while(typeIter.hasNext()) {
			CalendarEntryType type = (CalendarEntryType) typeIter.next();
			if(!this.insertPracticeOnly) {
				this.typeField.addMenuElement(type.getName(),iwrb.getLocalizedString("calendarEntry."+type.getName(),type.getName()));
			}
			else {
				if(type.getName().equals(practiceFieldParameterName)) {
					this.typeField.addMenuElement(type.getName(),type.getName());
				}
			}
			
		}
		this.typeField.setSelectedElement(practiceFieldParameterName);
		
		this.ledgerField = new DropdownMenu(ledgerFieldParameterName);
		this.ledgerField.addMenuElement(-1,iwrb.getLocalizedString(noLedgerFieldParameterName,"No ledger"));
		
		User user = iwc.getCurrentUser();
		Integer userID = (Integer) user.getPrimaryKey();
		int userGroupID = user.getPrimaryGroupID();

		Iterator ledgerIter = calBiz.getAllLedgers().iterator();
		while(ledgerIter.hasNext()) {
			CalendarLedger ledger = (CalendarLedger) ledgerIter.next();
			if(userID.intValue() == ledger.getCoachID() || userGroupID == ledger.getCoachGroupID()) {
				this.ledgerField.addMenuElement(ledger.getLedgerID(),ledger.getName());
			}
			
		}
		this.locationField = new TextInput(locationFieldParameterName);
		
		this.noRepeatField = new SelectOption(this.noRepeatText.toString(),noRepeatFieldParameterName);
		this.dailyField = new SelectOption(this.dailyText.toString(),dailyFieldParameterName);
		this.weeklyField =new SelectOption(this.weeklyText.toString(),weeklyFieldParameterName);
		this.monthlyField = new SelectOption(this.monthlyText.toString(),monthlyFieldParameterName);
		this.yearlyField = new SelectOption(this.yearlyText.toString(),yearlyFieldParameterName);
		
		this.repeatField = new DropdownMenu(repeatFieldParameterName);
		this.repeatField.addOption(this.noRepeatField);
		this.repeatField.addOption(this.dailyField);
		this.repeatField.addOption(this.weeklyField);
		this.repeatField.addOption(this.monthlyField);
		this.repeatField.addOption(this.yearlyField);
		
		IWTimestamp stamp = new IWTimestamp();
		
		String day = iwc.getParameter(CalendarParameters.PARAMETER_DAY);
		String month = iwc.getParameter(CalendarParameters.PARAMETER_MONTH);
		String year = iwc.getParameter(CalendarParameters.PARAMETER_YEAR);
		String view = iwc.getParameter(CalendarParameters.PARAMETER_VIEW);
		int v = 0;
		if(view != null && !view.equals("")) {
			v = Integer.parseInt(view);
		}
		this.dayFromField = new DatePicker(dayFromFieldParameterName);	
		this.dayToField = new DatePicker(dayToFieldParameterName);		
		if(day != null && month != null && year != null && 
				v == CalendarParameters.DAY) {
			//month is month - 1 -> January == 0!!
			Date date = new Date(Integer.parseInt(year),Integer.parseInt(month)-1,Integer.parseInt(day));
			this.dayFromField.setDate(date);
			this.dayToField.setDate(date);
		}
		else {
			this.dayFromField.setDate(stamp.getDate());
			this.dayToField.setDate(stamp.getDate());
		}
		this.timeFromField = new TimeInput(timeFromFieldParameterName);
		//the default from time is set to the current time
		this.timeFromField.setHour(stamp.getHour());
		this.timeFromField.setMinute(0);
		this.timeToField = new TimeInput(timeToFieldParameterName);
		//the default to time is set one hour later than the current time
		if(stamp.getHour() == 23) {
			this.timeToField.setHour(0);
		}
		else {
			this.timeToField.setHour(stamp.getHour() + 1);
		}		
		this.timeToField.setMinute(0);
	
		this.attendeesField = new GroupChooser(attendeesFieldParameterName);

		this.descriptionField = new TextArea(descriptionFieldParameterName);
		this.descriptionField.setRows(5);
		
		
		this.save = new StyledButton(new SubmitButton(iwrb.getLocalizedString("save", "Save"),saveButtonParameterName,saveButtonParameterValue));
//		save.setAsImageButton(true);
		this.reset = new StyledButton(new ResetButton(iwrb.getLocalizedString("reset", "Reset")));
//		reset.setAsImageButton(true);
		
		this.hiddenEntryID = new HiddenInput(entryIDParameterName,iwc.getParameter(entryIDParameterName));
		this.hiddenView = new HiddenInput(CalendarParameters.PARAMETER_VIEW,view);
		this.hiddenYear =new HiddenInput(CalendarParameters.PARAMETER_YEAR,new Integer(stamp.getYear()).toString());
		this.hiddenMonth = new HiddenInput(CalendarParameters.PARAMETER_MONTH,new Integer(stamp.getMonth()).toString());
		this.hiddenDay = new HiddenInput(CalendarParameters.PARAMETER_DAY,new Integer(stamp.getDay()).toString());
				
		//if some entry is selected, data is printed in the fields
//		if(entryIDString != null && !entryIDString.equals("")) {
			
		if(entry != null) {
			
//			isEntrySet = true;
			this.modifyOneRadioButton = new RadioButton(modifyOneOrManyRadioButtonParameterName,oneValue);
			this.modifyOneRadioButton.setSelected();
			this.modifyManyRadioButton = new RadioButton(modifyOneOrManyRadioButtonParameterName,manyValue);
			
			this.newEntryLink = new Link(iwrb.getLocalizedString("new_entry","New Entry"));
			this.newEntryLink.addParameter(entryIDParameterName,"");
			this.newEntryLink.addParameter(CalendarParameters.PARAMETER_VIEW,view);
//			newEntryLink.setStyleClass(styledLink);
			this.newEntryLink.setAsImageButton(true,true);
			
			this.deleteLink = new Link(iwrb.getLocalizedString("delete","Delete"));
			this.deleteLink.setWindowToOpen(ConfirmDeleteWindow.class);
			this.deleteLink.addParameter(ConfirmDeleteWindow.PRM_DELETE_ID, this.entryIDString);
			this.deleteLink.addParameter(ConfirmDeleteWindow.PRM_DELETE, CalendarParameters.PARAMETER_TRUE);
			this.deleteLink.addParameter(ConfirmDeleteWindow.PRM_ENTRY_OR_LEDGER,ENTRY);
			this.deleteLink.addParameter(CalendarView.ACTION,"");
			this.deleteLink.setAsImageButton(true,true);
			
			this.headlineField.setContent(entry.getName());
			this.typeField.setSelectedElement(entry.getEntryTypeName());
			this.repeatField.setSelectedElement(entry.getRepeat());
			this.ledgerField.setSelectedElement(entry.getLedgerID());
			
			Integer groupID = new Integer(entry.getGroupID());
			try {
				if(groupID.intValue() != -1) {
					this.attendeesField.setChooserValue(getGroupBusiness(iwc).getGroupByGroupID(groupID.intValue()).getName(),groupID.toString());
					// prior version:  attendeesField.setSelectedGroup(groupID.toString(),getGroupBusiness(iwc).getGroupByGroupID(groupID.intValue()).getName());
				}
			}catch (Exception e){
				e.printStackTrace();
			}
			
			
			IWTimestamp iwFrom = new IWTimestamp(entry.getDate());
			Date dateF = iwFrom.getDate();
			this.dayFromField.setDate(dateF);
			
			IWTimestamp iwTo = new IWTimestamp(entry.getEndDate());
			Date dateT = iwTo.getDate();
			this.dayToField.setDate(dateT);
			
			this.timeFromField.setHour(entry.getDate().getHours());
			this.timeFromField.setMinute(entry.getDate().getMinutes());
			
			this.timeToField.setHour(entry.getEndDate().getHours());
			this.timeToField.setMinute(entry.getEndDate().getMinutes());	
			
			this.locationField.setContent(entry.getLocation());
			
			this.descriptionField.setContent(entry.getDescription());
			
		}
			
			
//		}		
		else if(this.displayingTimeConflict) {
			String entryAttendees = iwc.getParameter(attendeesFieldParameterName);
			if(entryAttendees == null || entryAttendees.equals("")) {
				entryAttendees = "";
			}
			else {
				entryAttendees = entryAttendees.substring(entryAttendees.lastIndexOf("_")+1);
			}
			
			if(entryAttendees != null && !entryAttendees.equals("")) {
				Integer groupID = new Integer(entryAttendees);
				try {
					this.attendeesField.setChooserValue(getGroupBusiness(iwc).getGroupByGroupID(groupID.intValue()).getName(),groupID.toString());
					// prior version: attendeesField.setSelectedGroup(groupID.toString(),getGroupBusiness(iwc).getGroupByGroupID(groupID.intValue()).getName());
				}catch (Exception e){
					e.printStackTrace();
				}
			}
			this.headlineField.setContent(iwc.getParameter(headlineFieldParameterName));
			this.typeField.setSelectedElement(iwc.getParameter(typeFieldParameterName));
			this.repeatField.setSelectedElement(iwc.getParameter(repeatFieldParameterName));
			this.ledgerField.setSelectedElement(iwc.getParameter(ledgerFieldParameterName));
			this.locationField.setContent(iwc.getParameter(locationFieldParameterName));
			this.descriptionField.setContent(iwc.getParameter(descriptionFieldParameterName));
		}
	}
	
	/**
	 * lines up the gui - for editing entries
	 * @return form containing the gui table
	 */
	private Table lineUpEdit(IWContext iwc, CalendarEntry entry) {
		Table table = new Table();
//		table.setStyleClass(borderAllWhite);
		table.setCellspacing(0);
		table.setCellpadding(0);
		table.setWidth(Table.HUNDRED_PERCENT);
//		table.setStyleClass(1,1,menuTableStyle);
		table.setHeight(1,1,20);
				
		if(entry != null) {
			Table radioButtonTable = new Table();
			radioButtonTable.setWidth(Table.HUNDRED_PERCENT);
			radioButtonTable.add(this.modifyOneRadioButton,1,1);
			radioButtonTable.add(Text.NON_BREAKING_SPACE,1,1);
			radioButtonTable.add(this.modifyOneText,1,1);
			radioButtonTable.add(this.modifyManyRadioButton,1,2);
			radioButtonTable.add(Text.NON_BREAKING_SPACE,1,2);
			radioButtonTable.add(this.modifyManyText,1,2);
			
			table.add(radioButtonTable,1,1);
			
			Table buttonTableTop = new Table();
			buttonTableTop.setWidth(Table.HUNDRED_PERCENT);
			buttonTableTop.add(this.newEntryLink,1,1);
			buttonTableTop.add(this.deleteLink,2,1);
			
			table.setVerticalAlignment(2,1,Table.VERTICAL_ALIGN_TOP);

			table.add(buttonTableTop,2,1);
		}
		
		Table headlineTable = new Table();
		headlineTable.setCellpadding(2);
		headlineTable.setCellspacing(0);
		headlineTable.setWidth(Table.HUNDRED_PERCENT);
		headlineTable.add(this.headlineText,1,1);
		headlineTable.add(this.headlineField,1,2);
		
		table.add(headlineTable,1,2);
		
		Table typeTable = new Table();
		typeTable.setCellpadding(2);
		typeTable.setCellspacing(0);
		typeTable.setWidth(Table.HUNDRED_PERCENT);
		typeTable.add(this.typeText,1,1);
		typeTable.add(this.typeField,1,2);
		
		table.add(typeTable,2,2);
		
		Table locationTable = new Table();
		locationTable.setCellpadding(2);
		locationTable.setCellspacing(0);	
		locationTable.setWidth(Table.HUNDRED_PERCENT);
		locationTable.add(this.locationText,1,1);
		locationTable.add(this.locationField,1,2);
		
		table.add(locationTable,1,3);

		
		Table repeatTable = new Table();
		repeatTable.setCellpadding(2);
		repeatTable.setCellspacing(0);
		repeatTable.setWidth(Table.HUNDRED_PERCENT);
		repeatTable.add(this.repeatText,1,1);
		repeatTable.add(this.repeatField,1,2);
		
		table.add(repeatTable,2,3);
		
		Table dayFromTable = new Table();
		dayFromTable.setCellpadding(2);
		dayFromTable.setCellspacing(0);
		dayFromTable.setWidth(Table.HUNDRED_PERCENT);
		dayFromTable.add(this.dayFromText,1,1);
		dayFromTable.add(this.dayFromField,1,2);
		
		table.add(dayFromTable,1,4);
		
		Table dayToTable = new Table();
		dayToTable.setCellpadding(2);
		dayToTable.setCellspacing(0);
		dayToTable.add(this.dayToText,1,1);
		dayToTable.add(this.dayToField,1,2);
		
		table.add(dayToTable,2,4);
		
		Table timeFromTable = new Table();
		timeFromTable.setWidth(Table.HUNDRED_PERCENT);
		timeFromTable.add(this.timeFromText,1,1);
		timeFromTable.add(this.timeFromField,1,2);
		
		table.add(timeFromTable,1,5);
		
		Table timeToTable = new Table();
		timeToTable.setWidth(Table.HUNDRED_PERCENT);
		timeToTable.add(this.timeToText,1,1);
		timeToTable.add(this.timeToField,1,2);
		
		table.add(timeToTable,2,5);
				
		Table ledgerTable = new Table();
		ledgerTable.setWidth(Table.HUNDRED_PERCENT);
		ledgerTable.add(this.groupOrLedgerText,1,1);
		ledgerTable.add(this.ledgerField,1,2);
		
		table.add(ledgerTable,1,6);
		
		Table groupTable = new Table();
		groupTable.setWidth(Table.HUNDRED_PERCENT);
		groupTable.add(this.attendeesText,1,1);
		// AttentantChooser is a PresentationObject
		groupTable.add((PresentationObject) this.attendeesField,1,2);
		
		table.add(groupTable,1,7);
		
		Table descTable = new Table();
		descTable.setCellpadding(2);
		descTable.setCellspacing(0);
		descTable.setWidth(Table.HUNDRED_PERCENT);
		descTable.add(this.descriptionText,1,1);
		descTable.add(this.descriptionField,1,2);
		
		table.setVerticalAlignment(2,6,Table.VERTICAL_ALIGN_TOP);
		table.mergeCells(2,6,2,7);
		table.add(descTable,2,6);

		Table buttonTable = new Table();
		buttonTable.setCellpadding(2);
		buttonTable.setCellspacing(0);
//		buttonTable.add(help);
		buttonTable.add(this.save,1,1);
		buttonTable.add(this.reset,2,1);
		
		table.setAlignment(2,8,Table.HORIZONTAL_ALIGN_RIGHT);
		table.add(buttonTable,2,8);

		return table;
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
		String entryModifyOneOrMany = iwc.getParameter(modifyOneOrManyRadioButtonParameterName);
		String entryID = iwc.getParameter(entryIDParameterName);
			
		User user = iwc.getCurrentUser();
		
		if(entryAttendees == null || entryAttendees.equals("")) {
			entryAttendees = "";
		}
		else {
			entryAttendees = entryAttendees.substring(entryAttendees.lastIndexOf("_")+1);
		}

		IWTimestamp f = new IWTimestamp(entryDate);
		IWTimestamp t = new IWTimestamp(entryEndDate);
		Timestamp from = f.getTimestamp();
		Timestamp to = t.getTimestamp();
		from.setHours(Integer.parseInt(entryTimeHour));
		from.setMinutes(Integer.parseInt(entryTimeMinute));
		to.setHours(Integer.parseInt(entryEndTimeHour));
		to.setMinutes(Integer.parseInt(entryEndTimeMinute));
		
		int ledgerID;
		Timestamp ledStartTime = from;

		if(entryLedger != null && !entryLedger.equals("") && !entryLedger.equals("-1")) {
			ledgerID = Integer.parseInt(entryLedger);
			CalendarLedger ledger = getCalBusiness(iwc).getLedger(ledgerID);
			if(ledger != null) {
				ledStartTime = ledger.getDate();
			}
			
		}

		if(from.getHours() > to.getHours() || (from.getHours() == to.getHours() && from.getMinutes() > to.getMinutes())) {
			parentPage.setAlertOnLoad(iwrb.getLocalizedString("calEntryCreator.to_time_before_from_time_message","The from time is later than the to time!"));
			this.displayingTimeConflict = true;
		}
					
		else if(from.compareTo(to)>0) {
			parentPage.setAlertOnLoad(iwrb.getLocalizedString("calEntryCreator.to_date_before_from_date_message","The from day is later than the to day!"));
			this.displayingTimeConflict = true;
		}
		
		else if(from.getTime()<ledStartTime.getTime()) {
			parentPage.setAlertOnLoad(iwrb.getLocalizedString("calEntryCreator.from_date_before_led_start_date_message","The from day is before the ledgers start day!"));
			this.displayingTimeConflict = true;
		}
		else {
			if(entryID != null && !entryID.equals("")) {
				CalendarEntry entry = null;
				try{
					entry = calBus.getEntry(Integer.parseInt(entryID));
				}catch(Exception fe) {
					entry = null;
				}
				if(entry != null) {
					List entries = null;
					if(entryModifyOneOrMany.equals(manyValue)) {
						entries = new ArrayList(calBus.getEntriesByEntryGroupID(entry.getEntryGroupID()));
						Iterator entryIter = entries.iterator();
						while(entryIter.hasNext()) {
							CalendarEntry currentEntry = (CalendarEntry) entryIter.next();
							int currentEntryID = currentEntry.getEntryID();
							calBus.updateEntry(currentEntryID,entryHeadline, user, entryType, entryRepeat, currentEntry.getDate().toString(),entryTimeHour, entryTimeMinute, currentEntry.getEndDate().toString(), entryEndTimeHour, entryEndTimeMinute, entryAttendees, entryLedger, entryDescription, entryLocation, entryModifyOneOrMany );
							
						}
					}
					else {
						calBus.updateEntry(Integer.parseInt(entryID),entryHeadline, user, entryType, entryRepeat, entryDate,entryTimeHour, entryTimeMinute, entryEndDate, entryEndTimeHour, entryEndTimeMinute, entryAttendees, entryLedger, entryDescription, entryLocation, entryModifyOneOrMany );				
					}
				}	
				else {
					calBus.createNewEntry(entryHeadline, user, entryType, entryRepeat, entryDate,entryTimeHour, entryTimeMinute, entryEndDate, entryEndTimeHour, entryEndTimeMinute, entryAttendees, entryLedger, entryDescription, entryLocation);
				}
			}
			else {
				calBus.createNewEntry(entryHeadline, user, entryType, entryRepeat, entryDate,entryTimeHour, entryTimeMinute, entryEndDate, entryEndTimeHour, entryEndTimeMinute, entryAttendees, entryLedger, entryDescription, entryLocation);			
			}
		}
	}
	
	public void main(IWContext iwc) {
		setName("form");
		CalendarEntry entry = null;

		this.entryIDString = iwc.getParameter(entryIDParameterName);
		if(this.entryIDString == null || this.entryIDString.equals("")) {
			this.entryIDString = "";
		}
		else {
			try {
				entry = getCalBusiness(iwc).getEntry(Integer.parseInt(this.entryIDString));
			}catch(Exception e) {
				entry = null;
			}
			
		}
		String s = iwc.getParameter(saveButtonParameterName);
		if(s != null && !this.displayingTimeConflict) {
			entry = null;
		}
		
		initializeTexts(iwc);
		initializeFields(iwc, entry);
				
		add(this.hiddenView);
		add(this.hiddenYear);
		add(this.hiddenMonth);
		add(this.hiddenDay);
		add(this.hiddenEntryID);

		add(lineUpEdit(iwc,entry));
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
