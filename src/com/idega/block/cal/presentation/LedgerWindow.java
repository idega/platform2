/*
 * Created on Feb 22, 2004
 */
package com.idega.block.cal.presentation;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import com.idega.block.cal.business.CalBusiness;
import com.idega.block.cal.business.DefaultLedgerVariationsHandler;
import com.idega.block.cal.business.LedgerVariationsHandler;
import com.idega.block.cal.data.AttendanceEntity;
import com.idega.block.cal.data.AttendanceMark;
import com.idega.block.cal.data.CalendarEntry;
import com.idega.block.cal.data.CalendarLedger;
import com.idega.block.entity.business.EntityToPresentationObjectConverter;
import com.idega.block.entity.data.EntityPath;
import com.idega.block.entity.presentation.EntityBrowser;
import com.idega.core.contact.data.Phone;
import com.idega.core.contact.data.PhoneType;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.Country;
import com.idega.core.location.data.PostalCode;
import com.idega.data.EntityRepresentation;
import com.idega.data.GenericEntity;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.CalendarParameters;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.StyledButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.repository.data.RefactorClassRegistry;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.presentation.UserPropertyWindow;
import com.idega.util.IWTimestamp;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:birna@idega.is">Birna Iris Jonsdottir</a>
 */
public class LedgerWindow extends StyledIWAdminWindow{
	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.cal";
	
	private static final String HELP_TEXT_KEY = "cal_ledger";
	
	public static final String SELECTED_USERS_KEY = "selected_users";
	public static final String DELETE_USERS_KEY = "delete_selected_users";
	public static final String NEW_USER_IN_LEDGER = "user_new_in_ledger_";
	public static final String BUNDLE_KEY_LEDGER_VARIATIONS_HANDLER_CLASS = "ledger_variations_class";
	
	public static final String GROUP ="group";
	public static final String LEDGER ="ledger";
	
	public static final String PRINT_LEDGER = "led_pr";
	public static final String PRINT_WITH_DATA = "0";
	public static final String PRINT_EMPTY = "1";
	
	//parameter names
	public static String creatorFieldParameterName = "creator";
	public static String otherCoachesFieldParameterName = "otherCoaches";
	public static String groupFieldParameterName = "group";
	public static String dateFieldParameterName = "date";
	public static String saveButtonParameterName = "save";
	public static String saveButtonParameterValue = "save";
	public static String clubFieldParameterName = "clubName";
	public static String divisionFieldParameterName = "divisionName";
	
	//display texts 
	private Text creatorText;
	private Text otherCoachesText;
	private Text groupText;
	private Text dateText;
	private Text clubNameText;
	private Text divisionNameText;
	private Text allowedMarksText;
	
	//fields
	private String creatorNameField;
	private String otherCoachesNameField;
	private String groupNameField;
	private String fromDateField;
	private String clubNameField;
	private String divisionNameField;
	private Table allowedMarksField;
	
	//buttons
	private SubmitButton saveButton;
	private StyledButton styledSaveButton;
	private CloseButton closeButton;
	private StyledButton styledCloseButton;
	private Link printableLedger;
	private Link printableEmptyLedger;
//	private PrintButton printButton;
	private GenericButton deleteButton;
	private StyledButton styledDeleteButton;
	
	private CalBusiness calBiz = null;
	private GroupBusiness groupBiz = null;

	private Form form;
	private Table mainTable;
	private EntityBrowser entityBrowser;
	
	private String mainTableStyle = "main";
	private String borderAllWhiteStyle = "borderAllWhite";
	private String styledLink = "styledLink";
	private String markLink = "markLink";
	private String styledLinkUnderline = "styledLinkUnderline";
	private String bold = "bold";
	private String titleFont = "font-family:Verdana,Arial,Helvetica,sans-serif;font-size:9pt;font-weight:bold;color:#FFFFFF;";
	
	private String groupString = null;
	private Integer groupID;
	private String ledgerString = null;
	private Integer ledgerID = null;
	
	public LedgerWindow() {
		setHeight(650);
		setWidth(800);
		setResizable(true);
	}
	/**
	 * initalizes the display texts
	 *
	 */
	protected void initializeTexts(IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);

		creatorText = new Text(iwrb.getLocalizedString(creatorFieldParameterName,"Coach"));
		creatorText.setStyleClass(bold);
		otherCoachesText = new Text(iwrb.getLocalizedString(otherCoachesFieldParameterName,"Other coaches"));
		otherCoachesText.setStyleClass(bold);
		groupText = new Text(iwrb.getLocalizedString(groupFieldParameterName,"Group"));
		groupText.setStyleClass(bold);
		dateText = new Text(iwrb.getLocalizedString(dateFieldParameterName,"Start Date"));
		dateText.setStyleClass(bold);
		clubNameText = new Text(iwrb.getLocalizedString(clubFieldParameterName,"Club name"));
		clubNameText.setStyleClass(bold);
		divisionNameText = new Text(iwrb.getLocalizedString(divisionFieldParameterName, "Division name"));
		divisionNameText.setStyleClass(bold);
		allowedMarksText = new Text(iwrb.getLocalizedString("ledgerwindow.allowed_marks", "Allowed marks"));
		allowedMarksText.setStyleClass(bold);
	}
	/**
	 * initializes the fields in the form of the window
	 *
	 */
	protected void initializeFields(IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		//IWBundle iwb = getBundle(iwc);
		
		LedgerVariationsHandler ledgerVariationsHandler = getLedgerVariationsHandler(iwc);
		
		String lIDString = iwc.getParameter(LEDGER);
		Integer lID = new Integer(lIDString);
		CalendarLedger ledger = getCalendarBusiness(iwc).getLedger(lID.intValue());
		
		User creator = null;
		try{
			creator = getUserBusiness(iwc).getUser(ledger.getCoachID());
		}catch(Exception e) {
			e.printStackTrace();
		}		
		if(creator != null) {
			creatorNameField = creator.getName();
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
			groupNameField = getGroupBusiness(iwc).getGroupByGroupID(groupID).getName();
			Collection c = getGroupBusiness(iwc).getParentGroupsRecursive(getGroupBusiness(iwc).getGroupByGroupID(groupID));
			if(c != null) {		
				parentGroups = new ArrayList(c);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(parentGroups != null) {
			divisionNameField = ledgerVariationsHandler.getParentGroupName(parentGroups);
			clubNameField = ledgerVariationsHandler.getParentOfParentGroupName(parentGroups);
		}
		if(divisionNameField ==  null) {
			divisionNameField = iwrb.getLocalizedString("ledgerwindow.no_division_text","No division");
		}
		if(clubNameField == null) {
			clubNameField = iwrb.getLocalizedString("ledgerwindow.no_club_text","No club");
		}
		
		Timestamp fromD = getCalendarBusiness(iwc).getLedger(lID.intValue()).getDate();
		IWTimestamp iwFromD = new IWTimestamp(fromD);
		//fromDate is the start date of the ledger
		fromDateField = iwFromD.getDateString("dd. MMMMMMMM yyyy");
		
		allowedMarksField = new Table();
		List marks = getCalendarBusiness(iwc).getAllMarks();
		if(marks!= null) {
			Iterator markIter = marks.iterator();	
			while(markIter.hasNext()) {
				AttendanceMark mark = (AttendanceMark) markIter.next();
				String markName = mark.getMark();
				Link m = new Link(markName);
				m.setStyleClass(markLink);
				m.addParameter("markID",mark.getPrimaryKey().toString());
				m.addParameter(LEDGER,lIDString);
				m.setWindowToOpen(NewMarkWindow.class);
				allowedMarksField.add(m,4,1);
				allowedMarksField.add(": "+mark.getMarkDescription(),4,1);
				allowedMarksField.add("<br>",4,1);
			}
		}
		
		
		//when save button is pushed the new ledger is created
		saveButton = new SubmitButton(iwrb.getLocalizedString("ledgerwindow.save","Save"),saveButtonParameterName,saveButtonParameterValue);
		styledSaveButton = new StyledButton(saveButton);
		//closes the window
		closeButton = new CloseButton(iwrb.getLocalizedString("ledgerwindow.close","Close"));
		styledCloseButton = new StyledButton(closeButton); 
		printableLedger = new Link(iwrb.getLocalizedString("ledger_window.printable_ledger","Printable Ledger"));
		printableLedger.setWindowToOpen(PrintableLedgerWindow.class);
		printableLedger.setStyleClass(styledLink);
		printableLedger.addParameter(LEDGER,lIDString);
		printableLedger.addParameter(CalendarParameters.PARAMETER_YEAR,iwc.getParameter(CalendarParameters.PARAMETER_YEAR));
		printableLedger.addParameter(CalendarParameters.PARAMETER_MONTH,iwc.getParameter(CalendarParameters.PARAMETER_MONTH));
		printableLedger.addParameter(PRINT_LEDGER,PRINT_WITH_DATA);
		
		printableEmptyLedger = new Link(iwrb.getLocalizedString("ledger_window.printable_empty_ledger","Printable Empty Ledger"));
		printableEmptyLedger.setWindowToOpen(PrintableLedgerWindow.class);
		printableEmptyLedger.setStyleClass(styledLink);
		printableEmptyLedger.addParameter(LEDGER,lIDString);
		printableEmptyLedger.addParameter(CalendarParameters.PARAMETER_YEAR,iwc.getParameter(CalendarParameters.PARAMETER_YEAR));
		printableEmptyLedger.addParameter(CalendarParameters.PARAMETER_MONTH,iwc.getParameter(CalendarParameters.PARAMETER_MONTH));
		printableEmptyLedger.addParameter(PRINT_LEDGER,PRINT_EMPTY);
		
//		printButton = new PrintButton(print);
		
		deleteButton = new GenericButton(iwrb.getLocalizedString("delete","Delete"));
		deleteButton.setWindowToOpen(ConfirmDeleteWindow.class);
		deleteButton.addParameter(ConfirmDeleteWindow.PRM_DELETE_ID, lIDString);
		deleteButton.addParameter(ConfirmDeleteWindow.PRM_DELETE, CalendarParameters.PARAMETER_TRUE);
		deleteButton.addParameter(ConfirmDeleteWindow.PRM_ENTRY_OR_LEDGER,LEDGER);
		styledDeleteButton = new StyledButton(deleteButton);;
		
	}

	/**
	 * lines up the gui
	 *
	 */
	public void lineUp(IWContext iwc) {
				
		IWTimestamp timeStamp = null;

		String day = iwc.getParameter(CalendarParameters.PARAMETER_DAY);
		String month = iwc.getParameter(CalendarParameters.PARAMETER_MONTH);
		String year = iwc.getParameter(CalendarParameters.PARAMETER_YEAR);

		if(month != null && !month.equals("") &&
				day != null && !day.equals("") &&
				year != null && !year.equals("")) {
			timeStamp = getTimestamp(day,month,year);
		}
		else {
			timeStamp = IWTimestamp.RightNow();
		}
		
		Link right = getRightLink(iwc);
		Link left = getLeftLink(iwc);
		addNextMonthPrm(right, timeStamp, iwc);
		addLastMonthPrm(left, timeStamp, iwc);
		
		mainTable = new Table();
		mainTable.setCellspacing(0);
		mainTable.setCellpadding(0);
		
		Table headerTable = new Table();
		headerTable.setCellspacing(12);
		headerTable.setCellpadding(0);
		headerTable.setStyleClass(borderAllWhiteStyle);
		headerTable.setWidth(Table.HUNDRED_PERCENT);
		
		headerTable.add(creatorText,1,1);
		headerTable.add(creatorNameField,2,1);
		headerTable.add(clubNameText,1,2);
		headerTable.add(clubNameField,2,2);
		headerTable.add(divisionNameText,1,3);
		headerTable.add(divisionNameField,2,3);
		headerTable.setVerticalAlignment(1,4,Table.VERTICAL_ALIGN_TOP);
		headerTable.add(otherCoachesText,1,4);
		headerTable.setVerticalAlignment(2,4,Table.VERTICAL_ALIGN_TOP);
		headerTable.add(otherCoachesNameField,2,4);
		headerTable.add(groupText,1,5);
		headerTable.add(groupNameField,2,5);
		headerTable.add(dateText,1,6);
		headerTable.add(fromDateField,2,6);
		
		Table navTable = new Table();
		navTable.setCellpadding(0);
		navTable.setCellspacing(0);
		navTable.add(left,1,1);
		String dateString = timeStamp.getDateString("MMMMMMMM, yyyy",iwc.getCurrentLocale());
		navTable.add(dateString,2,1);
		navTable.add(right,3,1);
		
		headerTable.mergeCells(1,7,2,7);
		headerTable.add(navTable,1,7);
		
		headerTable.mergeCells(4,1,4,4);
		headerTable.setVerticalAlignment(3,1,Table.VERTICAL_ALIGN_TOP);
		headerTable.setVerticalAlignment(4,1,Table.VERTICAL_ALIGN_TOP);
		headerTable.add(allowedMarksText ,3,1);
		headerTable.add(allowedMarksField,4,1);

		
		mainTable.add(headerTable,1,1);
		mainTable.setHeight(2,5);
		
		Table entityTable = new Table();
		entityTable.setCellpadding(0);
		entityTable.setCellspacing(0);
		entityTable.setStyleClass(borderAllWhiteStyle);
		entityTable.add(getUserList(iwc),1,1);
		entityTable.add(printableLedger,1,2);
		entityTable.add(Text.NON_BREAKING_SPACE,1,2);
		entityTable.add(printableEmptyLedger,1,2);


		mainTable.add(entityTable,1,3);
		
		Table buttonTable = new Table();
		buttonTable.setCellspacing(0);
		buttonTable.setCellpadding(0);
		buttonTable.add(styledSaveButton,1,1);
		buttonTable.setWidth(2, "5");
		buttonTable.add(styledCloseButton,3,1);
		buttonTable.setWidth(4, "5");
		buttonTable.add(styledDeleteButton,5,1);
		
		Table helpTable = new Table();
		helpTable.setCellpadding(0);
		helpTable.setCellspacing(0);
		helpTable.add(getHelp(HELP_TEXT_KEY),1,1);
		
		Table bottomTable = new Table();
		bottomTable.setWidth(Table.HUNDRED_PERCENT);
		bottomTable.setCellpadding(0);
		bottomTable.setCellspacing(5);
		bottomTable.setStyleClass(borderAllWhiteStyle);
		bottomTable.add(helpTable,1,1);
		bottomTable.setAlignment(2,1,Table.HORIZONTAL_ALIGN_RIGHT);
		bottomTable.add(buttonTable,2,1);
		
		mainTable.setHeight(4,5);
		mainTable.add(bottomTable,1,5);
				
		form.add(mainTable);		
	}
	public EntityBrowser getUserList(IWContext iwc) {
		//converter for the delete checkbox 
		EntityToPresentationObjectConverter converterToDeleteButton = new EntityToPresentationObjectConverter() {

			public PresentationObject getHeaderPresentationObject(EntityPath entityPath, EntityBrowser browser, IWContext iwc) {
				CheckBox checkAllCheckBox = new CheckBox("checkAll");
				checkAllCheckBox.setToCheckOnClick(SELECTED_USERS_KEY, "this.checked");
				return checkAllCheckBox;
			}

			public PresentationObject getPresentationObject(Object entity, EntityPath path, EntityBrowser browser, IWContext iwc) {
				User user = (User) entity;
				
				CheckBox checkBox = new CheckBox(SELECTED_USERS_KEY, Integer.toString(user.getID()));
				return checkBox;			
			}
		};
		//the converter for the user - the name is displayed 
		EntityToPresentationObjectConverter converterLink = new EntityToPresentationObjectConverter() {
			public PresentationObject getHeaderPresentationObject(EntityPath entityPath, EntityBrowser browser, IWContext iwc) {
				return browser.getDefaultConverter().getHeaderPresentationObject(entityPath, browser, iwc);
			}

			public PresentationObject getPresentationObject(Object entity, EntityPath path, EntityBrowser browser, IWContext iwc) {
				User user = (User) entity;		
				CalendarLedger ledger = null;
				Collection groups = null;
				boolean isInGroup = false;

				PresentationObject text = browser.getDefaultConverter().getPresentationObject(entity, path, browser, iwc);
				try {
					ledger = getCalendarBusiness(iwc).getLedger(ledgerID.intValue());
					groups = getUserBusiness(iwc).getUserGroupsDirectlyRelated(user);
				}catch(Exception e) {
					e.printStackTrace();
				}
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
				
				
				Link aLink = null;
				if(isInGroup == false) {
					String theText = user.getName();
					String notInGroup = IWMainApplication.getDefaultIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc).getLocalizedString("ledgerwindow.user_not_in_ledger_group","(Not in ledger group)");
					
					aLink = new Link(theText+" "+notInGroup);
					aLink.setStyleClass("errorMessage");
				}
				else {
					aLink = new Link(text);
//					if(user.getMetaData(NEW_USER_IN_LEDGER) != null) {
//						user.removeMetaData(NEW_USER_IN_LEDGER);
//						user.setMetaData(NEW_USER_IN_LEDGER,"");
//						try {
//							user.updateMetaData();
//						}catch(Exception e) {
//							e.printStackTrace();
//						}
//												
////						user.store();
//					}				
					aLink.setStyleClass(styledLinkUnderline);
				}
				

				aLink.setWindowToOpen(UserPropertyWindow.class);
				aLink.addParameter(UserPropertyWindow.PARAMETERSTRING_USER_ID, user.getPrimaryKey().toString());


				return aLink;
			}
		};
		//the converter for the attendance marking 
		//in the top row the dates of practices are displayed
		//and after each user name there is an text input for the attendance marking
		EntityToPresentationObjectConverter converterAttendance = new EntityToPresentationObjectConverter() {
			
			
			public PresentationObject getHeaderPresentationObject(EntityPath path, EntityBrowser browser, IWContext iwc) {
				Table attendanceDateTable = new Table();
				
				CalendarEntry entry = null;
				List entryList = null;
				
				int m = 0;
				int y = 0;

				String year = iwc.getParameter(CalendarParameters.PARAMETER_YEAR);
				String month = iwc.getParameter(CalendarParameters.PARAMETER_MONTH);
				if(year != null && !year.equals("") &&
						month != null && !month.equals("")) {
					y = Integer.parseInt(year);
					m = Integer.parseInt(month);
				}
				

				try {
					entryList = (List) getCalendarBusiness(iwc).getPracticesByLedIDandMonth(ledgerID.intValue(),m,y);
				}
				catch (Exception ex) {
					ex.printStackTrace(System.err);
				}
				Collections.sort(entryList,new Comparator() {
					public int compare(Object arg0, Object arg1) {
						return ((CalendarEntry) arg0).getDate().compareTo(((CalendarEntry) arg1).getDate());
					}				
				});
				
				int column = 2;
				Iterator entryIter = entryList.iterator();
				while(entryIter.hasNext()) {
					entry = (CalendarEntry) entryIter.next();
					IWTimestamp date = new IWTimestamp(entry.getDate());
					String dateString = date.getDateString("dd/MM");
					attendanceDateTable.setWidth(column,1,40);
					attendanceDateTable.add(dateString,column,1);					
					column++;					
				}
				return attendanceDateTable;
			}

			public PresentationObject getPresentationObject(Object entity, EntityPath path, EntityBrowser browser, IWContext iwc) {
				Table attendanceMarkTable = new Table();
				User user = (User) entity;
				List entryList = null;
				
				int m = 0;
				int y = 0;

				String year = iwc.getParameter(CalendarParameters.PARAMETER_YEAR);
				String month = iwc.getParameter(CalendarParameters.PARAMETER_MONTH);
				if(year != null && !year.equals("") &&
						month != null && !month.equals("")) {
					y = Integer.parseInt(year);
					m = Integer.parseInt(month);
				}
				
				try {
					entryList = (List) getCalendarBusiness(iwc).getPracticesByLedIDandMonth(ledgerID.intValue(),m,y);
				}catch(Exception ex) {
					ex.printStackTrace(System.err);
				}
				Collections.sort(entryList,new Comparator() {
					public int compare(Object arg0, Object arg1) {
						return ((CalendarEntry) arg0).getDate().compareTo(((CalendarEntry) arg1).getDate());
					}				
				});
				
				Iterator entryIter = entryList.iterator();
				
				int column = 1;
				while(entryIter.hasNext()) {
					
					CalendarEntry entry = (CalendarEntry) entryIter.next();

					//the userID + underscore + the number of the column is set as the name
					//of the TextInput - done to make each TextInput name expicit
					String mark = "";
					TextInput input = new TextInput(user.getPrimaryKey().toString() + "_" + entry.getPrimaryKey().toString()); //getDate().toString());
					input.setMaxlength(1);

					input.setWidth("20");
					input.setStyleClass("borderAllWhite");	
					Integer userID = (Integer) user.getPrimaryKey();
					AttendanceEntity attendance = null;
					try {
						attendance = getCalendarBusiness(iwc).getAttendanceByUserIDandEntry(userID.intValue(),entry);
						if(attendance != null) {
							mark = attendance.getAttendanceMark();
						}							
					}catch(Exception e) {
						e.printStackTrace();
					}	
					if(mark != null && !mark.equals("")) {
						input.setValue(mark);
					}	
					attendanceMarkTable.setWidth(column,1,40);
					attendanceMarkTable.setAlignment(column,1,"center");
					attendanceMarkTable.add(input,column++,1);						
				}
				return attendanceMarkTable;


			}
		};
		EntityToPresentationObjectConverter converterCompleteAddress = new EntityToPresentationObjectConverter() {
			private List values;
			
			public PresentationObject getHeaderPresentationObject(EntityPath entityPath, EntityBrowser browser, IWContext iwc) {
				return browser.getDefaultConverter().getHeaderPresentationObject(entityPath, browser, iwc);
			}
			
			public PresentationObject getPresentationObject(Object genericEntity, EntityPath path, EntityBrowser browser, IWContext iwc) {
				// entity is a user, try to get the corresponding address
				User user = (User) genericEntity;
				Address address = null;
				try {
					address = getUserBusiness(iwc).getUsersCoAddress(user);
				}
				catch (RemoteException ex) {
					System.err.println("[LedgerWindow]: Address could not be retrieved.Message was :" + ex.getMessage());
					ex.printStackTrace(System.err);
				}
				StringBuffer displayValues = new StringBuffer();
				values = path.getValues((EntityRepresentation) address);
				// com.idega.core.data.Address.STREET_NUMBER plus
				// com.idega.core.data.Address.STREET_NUMBER
				displayValues.append(getValue(0)).append(' ').append(getValue(1));
				// com.idega.core.data.Address.P_O_BOX
				String displayValue = getValue(2);
				if (displayValue.length() != 0)
					displayValues.append(", P.O. Box ").append(displayValue).append(", ");
				// com.idega.core.data.PostalCode.POSTAL_CODE_ID|POSTAL_CODE
				// plus com.idega.core.data.Address.CITY
				displayValue = getValue(3);
				if (displayValue.length() != 0)
					displayValues.append(", ").append(getValue(3)).append(' ').append(getValue(4));
				// com.idega.core.data.Country.IC_COUNTRY_ID|COUNTRY_NAME
				displayValue = getValue(5);
				if (displayValue.length() != 0)
					displayValues.append(", ").append(displayValue);
				return new Text(displayValues.toString());
			}
			private String getValue(int i) {
				Object object = values.get(i);
				return ((object == null) ? "" : object.toString());
			}
		};
		
		EntityToPresentationObjectConverter converterPhone = new EntityToPresentationObjectConverter() {
			public PresentationObject getHeaderPresentationObject(EntityPath entityPath, EntityBrowser browser, IWContext iwc) {
				return browser.getDefaultConverter().getHeaderPresentationObject(entityPath, browser, iwc);
			}
			
			public PresentationObject getPresentationObject(Object entity, EntityPath path, EntityBrowser browser, IWContext iwc) {
				// entity is a user, try to get the corresponding address
				User user = (User) entity;
				Phone[] phone = null;
				try {
					phone = getUserBusiness(iwc).getUserPhones(user);
				}
				catch (RemoteException ex) {
					System.err.println("[LedgerWindow]: Phone could not be retrieved.Message was :" + ex.getMessage());
					ex.printStackTrace(System.err);
				}
				// now the corresponding address was found, now just use the
				// default converter
				int i;
				Table table = new Table();
				for (i = 0; i < phone.length; i++) {
					if(phone[i].getPhoneTypeId() == 3) {
						table.add(browser.getDefaultConverter().getPresentationObject((GenericEntity) phone[i], path, browser, iwc));
					}
				}
				return table;
			}
		};
		/*EntityToPresentationObjectConverter converterParent = new EntityToPresentationObjectConverter() {
		 public PresentationObject getHeaderPresentationObject(EntityPath entityPath, EntityBrowser browser, IWContext iwc) {
		 return browser.getDefaultConverter().getHeaderPresentationObject(entityPath, browser, iwc);
		 }
		 
		 public PresentationObject getPresentationObject(Object entity, EntityPath path, EntityBrowser browser, IWContext iwc) {
		 User user = (User) entity;
		 Collection parents = null;
		 try {
		 parents = user.getReverseRelatedBy("FAM_PARENT"); //getLedgerVariationsHandler(iwc).getParentGroupRelation(iwc, user);
		 }
		 catch (Exception ex) {
		 System.err.println("[LedgerWindow]: Parents could not be retrieved.Message was :" + ex.getMessage());
		 ex.printStackTrace(System.err);
		 }
//				int i = 1;
		 Table table = new Table();
		 table.add(parents.toString(),1,1);
		 return table;
		 }
		 };*/
		
		
		
		List users = null;
		
		try {
			users = (List) getCalendarBusiness(iwc).getUsersInLedger(ledgerID.intValue());
		} catch (Exception e){
			e.printStackTrace();
		}
		final Collator collator = Collator.getInstance(iwc.getLocale());
		if(users != null) {
			Collections.sort(users,new Comparator() {
				public int compare(Object arg0, Object arg1) {
					return collator.compare(((User) arg0).getName(), ((User) arg1).getName());
				}				
			});
		}

		IWResourceBundle resourceBundle = getResourceBundle(iwc);
		entityBrowser = EntityBrowser.getInstanceUsingEventSystem();

		entityBrowser.setEntities("havanna",users);
		entityBrowser.setName("LedgerWindow");
		entityBrowser.setDefaultNumberOfRows(Math.min(users.size(), 30));
		entityBrowser.setWidth(Table.HUNDRED_PERCENT);
		entityBrowser.setUseExternalForm(true);
		
		String nameKey = User.class.getName() + ".FIRST_NAME:" + User.class.getName() + ".MIDDLE_NAME:" + User.class.getName() + ".LAST_NAME";
		String entryKey = CalendarEntry.class.getName() + ".CAL_ENTRY_DATE";
		String ssnKey = User.class.getName() + ".PERSONAL_ID";
		
		String completeAddressKey = 
			Address.class.getName()
			+ ".STREET_NAME:"
			+ Address.class.getName()
			+ ".STREET_NUMBER:"
			+ Address.class.getName()
			+ ".P_O_BOX:"
			+ PostalCode.class.getName()
			+ ".POSTAL_CODE_ID|POSTAL_CODE:"
			+ Address.class.getName()
			+ ".CITY:"
			+ Country.class.getName()
			+ ".IC_COUNTRY_ID|COUNTRY_NAME";
		String phoneKey = PhoneType.class.getName() + ".IC_PHONE_TYPE_ID|TYPE_DISPLAY_NAME:" + Phone.class.getName() + ".PHONE_NUMBER";
		
		entityBrowser.setMandatoryColumn(1, "Delete");
		
		entityBrowser.setDefaultColumn(1, nameKey);
		entityBrowser.setDefaultColumn(2,entryKey);
		
		entityBrowser.setOptionColumn(0,ssnKey);
		entityBrowser.setOptionColumn(1,completeAddressKey);
		entityBrowser.setOptionColumn(2,phoneKey);
//		entityBrowser.setOptionColumn(3,User.class.getName());
		
		entityBrowser.setEntityToPresentationConverter("Delete", converterToDeleteButton);
		entityBrowser.setEntityToPresentationConverter(nameKey, converterLink);
		entityBrowser.setEntityToPresentationConverter(entryKey,converterAttendance);
		
		entityBrowser.setEntityToPresentationConverter(completeAddressKey, converterCompleteAddress);
		entityBrowser.setEntityToPresentationConverter(Phone.class.getName(), converterPhone);
		
		entityBrowser.addEntity(CalendarEntry.class.getName());
		entityBrowser.addEntity(Address.class.getName());
		entityBrowser.addEntity(Phone.class.getName());
		
		String confirmDeleting = resourceBundle.getLocalizedString("delete_selected_users", "Delete selected users");
		confirmDeleting += " ?";
		SubmitButton deleteButton =
			new SubmitButton(
					resourceBundle.getLocalizedString("Delete selection", "Delete selection"),
					DELETE_USERS_KEY,
					DELETE_USERS_KEY);
		deleteButton.setSubmitConfirm(confirmDeleting);
		StyledButton styledDeleteButton = new StyledButton(deleteButton);
		entityBrowser.addPresentationObjectToBottom(styledDeleteButton);
		IWResourceBundle iwrb = getResourceBundle(iwc);
		GenericButton addUserButton = new GenericButton(iwrb.getLocalizedString("ledgerwindow.add_user","Add user"));
		
//		addUserLink.setStyleClass(styledLink);
		addUserButton.addParameter(LEDGER,ledgerString);
		addUserButton.setWindowToOpen(CreateUserInLedger.class);
		StyledButton styledAddUserButton = new StyledButton(addUserButton);
		GenericButton statisticsButton = new GenericButton(iwrb.getLocalizedString("ledgerwindow.statistics","Statistics"));
//		statisticsLink.setStyleClass(styledLink);
		statisticsButton.addParameter(LEDGER,ledgerString);
		statisticsButton.setWindowToOpen(UserStatisticsWindow.class);
		StyledButton styledStatisticsButton = new StyledButton(statisticsButton);
		GenericButton addNewMarkButton = new GenericButton(iwrb.getLocalizedString("ledgerwindow.add_mark","Add mark"));
//		addNewMarkLink.setStyleClass(styledLink);
		addNewMarkButton.addParameter(LEDGER,ledgerString);
		addNewMarkButton.setWindowToOpen(NewMarkWindow.class);
		StyledButton styledAddNewMarkButton = new StyledButton(addNewMarkButton);
		
		entityBrowser.addPresentationObjectToBottom(styledAddUserButton);
		entityBrowser.addPresentationObjectToBottom(styledStatisticsButton);
		entityBrowser.addPresentationObjectToBottom(styledAddNewMarkButton);

		return entityBrowser;
	}
	
	public List removeUsersFromLedger(Collection userIds, int ledID, IWContext iwc) {
		ArrayList notRemovedUsers = new ArrayList();
		Iterator iterator = userIds.iterator();
		while (iterator.hasNext()) {
			String userIDString = (String) iterator.next();
			int userID = Integer.parseInt(userIDString);
			User user = null;
			try{
				user = getUserBusiness(iwc).getUser(userID);
			}catch(RemoteException rme) {
				
			}
			try {
				getCalendarBusiness(iwc).deleteUserFromLedger(userID,ledID,iwc);
				if(user.getMetaData(NEW_USER_IN_LEDGER) != null && 
						!user.getMetaData(NEW_USER_IN_LEDGER).equals("") &&
						!user.getMetaData(NEW_USER_IN_LEDGER).equals("-1")) {
					user.setMetaData(NEW_USER_IN_LEDGER,"-1");
					user.store();
				}
			}
			catch (Exception e) {
				System.err.println("[BasicUserOverview] user with id " + userID + " could not be removed" + e.getMessage());
				e.printStackTrace(System.err);
				notRemovedUsers.add(userIDString);
			}
		}
		return notRemovedUsers;
	}
	public void main(IWContext iwc) throws Exception {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		setTitle(iwrb.getLocalizedString("ledgerWindow.ledger","Ledger"));
		addTitle(iwrb.getLocalizedString("ledgerWindow.ledger","Ledger"),TITLE_STYLECLASS);
		
		form = new Form();
		form.maintainAllParameters();
		form.add(new HiddenInput(LEDGER, iwc.getParameter(LEDGER)));
		//form.maintainParameter(LEDGER);
		form.maintainParameter(CalendarParameters.PARAMETER_YEAR);
		form.maintainParameter(CalendarParameters.PARAMETER_MONTH);
		form.maintainParameter(CalendarParameters.PARAMETER_DAY);
		initializeTexts(iwc);
		initializeFields(iwc);
		
		
		ledgerString = iwc.getParameter(LEDGER);		
		ledgerID =new Integer(ledgerString);

		CalendarLedger ledger = getCalendarBusiness(iwc).getLedger(ledgerID.intValue());
		
		int i = ledger.getGroupID();
		groupID = new Integer(i);
		
		Collection users = null;
		List entries = null;
		List marks = null;
		
		int mon = 0;
		int ye = 0;

		String year = iwc.getParameter(CalendarParameters.PARAMETER_YEAR);
		String month = iwc.getParameter(CalendarParameters.PARAMETER_MONTH);
		if(year != null && !year.equals("") &&
				month != null && !month.equals("")) {
			ye = Integer.parseInt(year);
			mon = Integer.parseInt(month);
		}
		
		
		
		try {
			users = getCalendarBusiness(iwc).getUsersInLedger(ledgerID.intValue());
			entries = (List) getCalendarBusiness(iwc).getPracticesByLedIDandMonth(ledgerID.intValue(),mon,ye);
			marks = getCalendarBusiness(iwc).getAllMarks();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		String close = iwc.getParameter(ConfirmDeleteWindow.PRM_DELETED);
		if(close != null) {
			close();
		}
		
		String deleteUsers = iwc.getParameter(DELETE_USERS_KEY);
		if(deleteUsers != null) {
			String[] userIds;
			userIds = iwc.getParameterValues(SELECTED_USERS_KEY);
			if(userIds != null) {
				removeUsersFromLedger(Arrays.asList(userIds),ledgerID.intValue(),iwc);
			}
		}
		
		lineUp(iwc);

		String save = iwc.getParameter(saveButtonParameterName);
		if(save != null) {
			CalBusiness biz = getCalendarBusiness(iwc);
			User user = null;
			Iterator userIter = users.iterator();
			
			//the allowable attendance marks are set to a StringBuffer witha a * after each mark
			//this is done to do the data validation
			StringBuffer b = new StringBuffer();
			Iterator markIter = marks.iterator();
			while(markIter.hasNext()) {
				AttendanceMark m = (AttendanceMark) markIter.next();
				b.append(m.getMark());
				b.append("*");
			}

			while(userIter.hasNext()) {
				user = (User) userIter.next();
				
				Iterator entryIter = entries.iterator();
				while(entryIter.hasNext()) {
					CalendarEntry entry = (CalendarEntry) entryIter.next();
					Integer userID = (Integer) user.getPrimaryKey();
					String mark = iwc.getParameter(userID.toString() + "_" + entry.getPrimaryKey().toString()); //getDate().toString());
					if(mark != null) {
						if(mark.equals("")) {
							Text emptyWarning = new Text(iwrb.getLocalizedString("ledgerWindow.emptyCellWarning","There is a empty cell, to you want to go on?"));
							setOnLoad("confirm('"+emptyWarning+"')");
							biz.saveAttendance(userID.intValue(),ledgerID.intValue(),entry,"");
						}
						//data validation
						//if mark is a part of the allowable marks: match is true!
						//b.toString() is a sequence of the allowable marks with a * after each mark!
						boolean match = Pattern.matches(b.toString(), mark);
						if(match) {
							//if the attendance mark exists in the database the marking is updated
							if(biz.getAttendanceByUserIDandEntry(userID.intValue(),entry) != null) {
								AttendanceEntity attendance = biz.getAttendanceByUserIDandEntry(userID.intValue(),entry);
								Integer attendanceID = (Integer) attendance.getPrimaryKey();
								biz.updateAttendance(attendanceID.intValue(),userID.intValue(),ledgerID.intValue(),entry,mark);
							}
							//if no attendance mark exists, a new one is created
							else {
								biz.saveAttendance(userID.intValue(),ledgerID.intValue(),entry,mark);
							}							
						}
						else {
							setAlertOnLoad(mark + iwrb.getLocalizedString("ledger_window.is_not_mark_warning"," is not a mark!"));
							//h++
						}
					}																		
				}//end for							
			}//end while				
		}//end if(save != null)			
		add(form,iwc);
		
	}
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	public void addNextMonthPrm(Link L, IWTimestamp idts, IWContext iwc) {
		L.addParameter(LEDGER,iwc.getParameter(LEDGER));
		if (idts.getMonth() == 12) {
			L.addParameter(CalendarParameters.PARAMETER_DAY, idts.getDay());
			L.addParameter(CalendarParameters.PARAMETER_MONTH, String.valueOf(1));
			L.addParameter(CalendarParameters.PARAMETER_YEAR, String.valueOf(idts.getYear() + 1));
		}
		else {
			L.addParameter(CalendarParameters.PARAMETER_DAY, idts.getDay());
			L.addParameter(CalendarParameters.PARAMETER_MONTH, String.valueOf(idts.getMonth() + 1));
			L.addParameter(CalendarParameters.PARAMETER_YEAR, String.valueOf(idts.getYear()));
		}
	}

	public void addLastMonthPrm(Link L, IWTimestamp idts, IWContext iwc) {
		L.addParameter(LEDGER,iwc.getParameter(LEDGER));
		if (idts.getMonth() == 1) {
			L.addParameter(CalendarParameters.PARAMETER_DAY,idts.getDay());
			L.addParameter(CalendarParameters.PARAMETER_MONTH, String.valueOf(12));
			L.addParameter(CalendarParameters.PARAMETER_YEAR, String.valueOf(idts.getYear() - 1));
		}
		else {
			L.addParameter(CalendarParameters.PARAMETER_DAY,idts.getDay());
			L.addParameter(CalendarParameters.PARAMETER_MONTH, String.valueOf(idts.getMonth() - 1));
			L.addParameter(CalendarParameters.PARAMETER_YEAR, String.valueOf(idts.getYear()));
		}
	}
	
	public static LedgerVariationsHandler getLedgerVariationsHandler(IWContext iwc) {
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
		return ledgerVariationsHandler;
		
	}
	
	private Link getRightLink(IWContext iwc) {
		IWBundle iwb = getBundle(iwc);
		Image rightArrow = iwb.getImage("right_arrow.gif");
		Link right = new Link();
		right.setImage(rightArrow);
		return right;		
	}
	private Link getLeftLink(IWContext iwc) {
		IWBundle iwb = getBundle(iwc);
		Image leftArrow = iwb.getImage("left_arrow.gif");
		Link left = new Link();
		left.setImage(leftArrow);
		return left;
	}
	public static IWTimestamp getTimestamp(String day, String month, String year) {
		IWTimestamp stamp = new IWTimestamp();

		if (day != null) {
			stamp.setDay(Integer.parseInt(day));
		}
		// removed dubius behavior A
		/*else {
		 stamp.setDay(1);
		 }
		 */
		if (month != null) {
			stamp.setMonth(Integer.parseInt(month));
		}
		if (year != null) {
			stamp.setYear(Integer.parseInt(year));
		}

		stamp.setHour(0);
		stamp.setMinute(0);
		stamp.setSecond(0);

		return stamp;
	}


	

	/**
	 * 
	 * @param iwc
	 * @return calBiz - the calendar business 
	 */
	public CalBusiness getCalendarBusiness(IWApplicationContext iwc) {
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
