/*
 * Created on Feb 22, 2004
 */
package com.idega.block.cal.presentation;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
import com.idega.presentation.ui.PrintButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
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
	
	
	public static final String SELECTED_USERS_KEY = "selected_users";
	public static final String DELETE_USERS_KEY = "delete_selected_users";
	public static final String NEW_USER_IN_LEDGER = "user_new_in_ledger_";
	public static final String BUNDLE_KEY_LEDGER_VARIATIONS_HANDLER_CLASS = "ledger_variations_class";
	
	public static final String GROUP ="group";
	public static final String LEDGER ="ledger";
	
	//parameter names
	private static String coachFieldParameterName = "coach";
	private static String otherCoachesFieldParameterName = "otherCoaches";
	private static String groupFieldParameterName = "group";
	private static String dateFieldParameterName = "date";
	private static String saveButtonParameterName = "save";
	private static String saveButtonParameterValue = "save";
	private static String clubNameParameterName = "clubName";
		
	//display texts 
	private Text coachText;
	private Text otherCoachesText;
	private Text groupText;
	private Text dateText;
	private Text clubNameText;
	private Text allowedMarksText;
	
	//fields
	private Text coachNameField;
	private String otherCoachesNameField;
	private String groupNameField;
	private String fromDateField;
	private String clubNameField;
	
	//buttons
	private SubmitButton saveButton;
	private CloseButton closeButton;
	private PrintButton printButton;
	private Link deleteLink;
	
	private CalBusiness calBiz = null;
	private GroupBusiness groupBiz = null;

	private Form form;
	private Table mainTable;
	private EntityBrowser entityBrowser;
	
	private String mainTableStyle = "main";
	private String borderAllWhiteStyle = "borderAllWhite";
	private String styledLink = "styledLink";
	private String styledLinkUnderline = "styledLinkUnderline";
	private String bold = "bold";
	
	private String groupString = null;
	private Integer groupID;
	private String ledgerString = null;
	private Integer ledgerID = null;
	
	public LedgerWindow() {
		setHeight(400);
		setWidth(800);
		setResizable(true);
	}
	/**
	 * initalizes the display texts
	 *
	 */
	protected void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		coachText = new Text(iwrb.getLocalizedString(coachFieldParameterName,"Coach"));
		coachText.setStyleClass(bold);
		otherCoachesText = new Text(iwrb.getLocalizedString(otherCoachesFieldParameterName,"Other coaches"));
		otherCoachesText.setStyleClass(bold);
		groupText = new Text(iwrb.getLocalizedString(groupFieldParameterName,"Group"));
		groupText.setStyleClass(bold);
		dateText = new Text(iwrb.getLocalizedString(dateFieldParameterName,"Start Date"));
		dateText.setStyleClass(bold);
		clubNameText = new Text(iwrb.getLocalizedString(clubNameParameterName,"Club name"));
		clubNameText.setStyleClass(bold);
		allowedMarksText = new Text(iwrb.getLocalizedString("ledgerwindow.allowed_marks", "Allowed marks"));
		allowedMarksText.setStyleClass(bold);
	}
	/**
	 * initializes the fields in the form of the window
	 *
	 */
	protected void initializeFields() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		IWBundle iwb = getBundle(iwc);
		
		
		LedgerVariationsHandler ledgerVariationsHandler = getLedgerVariationsHandler(iwc);
		
		String lIDString = iwc.getParameter(LEDGER);
		Integer lID = new Integer(lIDString);
				
		//The user logged in is set as the main coach for the ledger
		if(iwc.isLoggedOn()) {
			User user =iwc.getCurrentUser();
			coachNameField = new Text(user.getName());
		}
		
		int coachGroupID = getCalendarBusiness(iwc).getLedger(lID.intValue()).getCoachGroupID();
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
			clubNameField = ledgerVariationsHandler.getParentGroupName(parentGroups);
		}
		if(clubNameField == null) {
			clubNameField = iwrb.getLocalizedString("ledgerwindow.no_club_text","No club");
		}
		Timestamp fromD = getCalendarBusiness(iwc).getLedger(lID.intValue()).getDate();
		IWTimestamp iwFromD = new IWTimestamp(fromD);
		//fromDate is the start date of the ledger
		fromDateField = iwFromD.getDateString("dd. MMMMMMMM yyyy");
		
		//when save button is pushed the new ledger is created
		saveButton = new SubmitButton(iwrb.getLocalizedString("ledgerwindow.save","Save"),saveButtonParameterName,saveButtonParameterValue);
		//closes the window
		closeButton = new CloseButton(iwrb.getLocalizedString("ledgerwindow.close","Close"));
		
		Image print = iwb.getImage("printer2.gif");
		print.setAlt(iwrb.getLocalizedString("ledger_window.print","Print"));
		printButton = new PrintButton(print);
		
		deleteLink = new Link(iwrb.getLocalizedString("delete","Delete"));
		deleteLink.setWindowToOpen(ConfirmDeleteWindow.class);
		deleteLink.addParameter(ConfirmDeleteWindow.PRM_DELETE_ID, lIDString);
		deleteLink.addParameter(ConfirmDeleteWindow.PRM_DELETE, CalendarParameters.PARAMETER_TRUE);
		deleteLink.addParameter(ConfirmDeleteWindow.PRM_ENTRY_OR_LEDGER,LEDGER);
		Image del = iwb.getImage("delete2.gif");
		del.setAlt(iwrb.getLocalizedString("ledger_window.close_ledger","Close Ledger"));
		deleteLink.setImage(del);
		
	}

	/**
	 * lines up the gui
	 *
	 */
	public void lineUp(IWContext iwc, List marks) {
		
		CalendarView c = new CalendarView();
		
		IWTimestamp timeStamp = null;

		String day = iwc.getParameter(CalendarParameters.PARAMETER_DAY);
		String month = iwc.getParameter(CalendarParameters.PARAMETER_MONTH);
		String year = iwc.getParameter(CalendarParameters.PARAMETER_YEAR);

		if(month != null && !month.equals("") &&
				day != null && !day.equals("") &&
				year != null && !year.equals("")) {
			timeStamp = c.getTimestamp(day,month,year);
		}
		else {
			timeStamp = IWTimestamp.RightNow();
		}
				
		Link right = c.getRightLink(iwc);
		Link left = c.getLeftLink(iwc);
		addNextMonthPrm(right, timeStamp, iwc);
		addLastMonthPrm(left, timeStamp, iwc);
		
		mainTable = new Table();
//		mainTable.setWidth(288);
		mainTable.setCellspacing(0);
		mainTable.setCellpadding(5);
		mainTable.setStyleClass(borderAllWhiteStyle);
		mainTable.add(coachText,1,1);
		mainTable.add(coachNameField,2,1);
		mainTable.add(clubNameText,1,2);
		mainTable.add(clubNameField,2,2);
		mainTable.setVerticalAlignment(1,3,"top");
		mainTable.add(otherCoachesText,1,3);
		mainTable.add(otherCoachesNameField,2,3);
		mainTable.add(groupText,1,4);
		mainTable.add(groupNameField,2,4);
		mainTable.add(dateText,1,5);
		mainTable.add(fromDateField,2,5);
		
		mainTable.add(left,1,6);
		String dateString = timeStamp.getDateString("MMMMMMMM, yyyy",iwc.getCurrentLocale());
		mainTable.add(dateString,1,6);
		mainTable.add(right,2,6);
		
		mainTable.mergeCells(4,1,4,4);
		mainTable.setVerticalAlignment(3,1,"top");
		mainTable.setVerticalAlignment(4,1,"top");
		mainTable.add(allowedMarksText ,3,1);
		Iterator markIter = marks.iterator();
		while(markIter.hasNext()) {
			AttendanceMark mark = (AttendanceMark) markIter.next();
			mainTable.add(mark.getMark()+": "+mark.getMarkDescription(),4,1);
			mainTable.add("<br>",4,1);
		}
		mainTable.setVerticalAlignment(5,4,"bottom");
		mainTable.setAlignment(5,4,"right");
		mainTable.add(printButton,5,4);
		mainTable.add(deleteLink,5,4);
				
		mainTable.mergeCells(1,7,5,7);
		mainTable.add(getUserList(iwc),1,7);
	
		mainTable.setAlignment(5,8,"right");
		mainTable.add(saveButton,5,8);
		mainTable.add(Text.NON_BREAKING_SPACE,3,8);
		mainTable.add(closeButton,5,8);
		
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
		
				
				Link aLink = new Link(text);
				if(isInGroup == false) {
					aLink.setStyleClass("errorMessage");
				}
				else {
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
				
				List timeList = new ArrayList();

				Iterator entryIter = entryList.iterator();
				
				
				while(entryIter.hasNext()) {					
					entry = (CalendarEntry) entryIter.next();
						IWTimestamp date = new IWTimestamp(entry.getDate());	
						timeList.add(date);						
				}				
				Collections.sort(timeList);
				int column = 2;
				Iterator timeIter = timeList.iterator();
				while(timeIter.hasNext()) {
					IWTimestamp date = (IWTimestamp) timeIter.next();
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
								
				int h = 0;
				for(int i = 1; i<=entryList.size(); i++) {

					//the userID + underscore + the number of the column is set as the name
					//of the TextInput - done to make each TextInput name expicit
					String mark = "";
					TextInput input = new TextInput(user.getPrimaryKey().toString() + "_" + i);
					input.setMaxlength(1);

					input.setWidth("20");
					input.setStyleClass("borderAllWhite");	
					Integer userID = (Integer) user.getPrimaryKey();
					AttendanceEntity attendance = null;
					try {
						attendance = getCalendarBusiness(iwc).getAttendanceByUserIDandEntry(userID.intValue(),(CalendarEntry) entryList.get(h++));
						if(attendance != null) {
							mark = attendance.getAttendanceMark();
						}							
					}catch(Exception e) {
						e.printStackTrace();
					}	
					if(mark != null && !mark.equals("")) {
						input.setValue(mark);
					}	
					attendanceMarkTable.setWidth(i,1,40);
					attendanceMarkTable.add(input,i,1);						
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
		EntityToPresentationObjectConverter converterParent = new EntityToPresentationObjectConverter() {
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
		};
		
		
		
		Collection users = null;
		
		try {
			users = getCalendarBusiness(iwc).getUsersInLedger(ledgerID.intValue());
		} catch (Exception e){
			e.printStackTrace();
		}

		IWResourceBundle resourceBundle = getResourceBundle(iwc);
		entityBrowser = new EntityBrowser();

		entityBrowser.setEntities("havanna",users);
		entityBrowser.setDefaultNumberOfRows(Math.min(users.size(), 30));
		entityBrowser.setWidth(Table.HUNDRED_PERCENT);
		
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
					resourceBundle.getLocalizedImageButton("Delete selection", "Delete selection"),
					DELETE_USERS_KEY,
					DELETE_USERS_KEY);
		deleteButton.setSubmitConfirm(confirmDeleting);
		entityBrowser.addPresentationObjectToBottom(deleteButton);
		IWResourceBundle iwrb = getResourceBundle(iwc);
		Link addUserLink = new Link(iwrb.getLocalizedString("ledgerwindow.add_user","Add user"));
		addUserLink.setStyleClass(styledLink);
		addUserLink.setParameter(LEDGER,ledgerString);
		addUserLink.setWindowToOpen(CreateUserInLedger.class);
		Link statisticsLink = new Link(iwrb.getLocalizedString("ledgerwindow.statistics","Statistics"));
		statisticsLink.setStyleClass(styledLink);
		statisticsLink.addParameter(LEDGER,ledgerString);
		statisticsLink.setWindowToOpen(UserStatisticsWindow.class);
		Link addNewMarkLink = new Link(iwrb.getLocalizedString("ledgerwindow.add_mark","Add mark"));
		addNewMarkLink.setStyleClass(styledLink);
		addNewMarkLink.addParameter(LEDGER,ledgerString);
		addNewMarkLink.setWindowToOpen(NewMarkWindow.class);
		
		entityBrowser.addPresentationObjectToBottom(addUserLink);
		entityBrowser.addPresentationObjectToBottom(statisticsLink);
		entityBrowser.addPresentationObjectToBottom(addNewMarkLink);
		
		return entityBrowser;
	}
	
	public void removeUsersFromLedger(Collection userIds, int ledID, IWContext iwc) {
		ArrayList notRemovedUsers = new ArrayList();
		Iterator iterator = userIds.iterator();
		while (iterator.hasNext()) {
			String userIDString = (String) iterator.next();
			int userID = Integer.parseInt(userIDString);
		
			try {
				getCalendarBusiness(iwc).deleteUserFromLedger(userID,ledID,iwc);
			}
			catch (Exception e) {
				System.err.println("[BasicUserOverview] user with id " + userID + " could not be removed" + e.getMessage());
				e.printStackTrace(System.err);
				notRemovedUsers.add(userIDString);
			}		
		}
//		return notRemovedUsers;
	}
	public void main(IWContext iwc) throws Exception {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		form = new Form();
		initializeTexts();
		initializeFields();
		
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
		
		form.maintainParameter(LEDGER);
		form.maintainParameter(CalendarParameters.PARAMETER_YEAR);
		form.maintainParameter(CalendarParameters.PARAMETER_MONTH);
		form.maintainParameter(CalendarParameters.PARAMETER_DAY);
		
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
		
		lineUp(iwc,marks);
		
		String deleteUsers = iwc.getParameter(DELETE_USERS_KEY);
		if(deleteUsers != null) {
			String[] userIds;
			userIds = iwc.getParameterValues(SELECTED_USERS_KEY);
			if(userIds != null) {
				removeUsersFromLedger(Arrays.asList(userIds),ledgerID.intValue(),iwc);
			}
		}

		String save = iwc.getParameter(saveButtonParameterName);
		if(save != null) {
			int userColumns = entries.size();
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
				int h = 0;
				user = (User) userIter.next();
				
				for(int j = 1; j<=userColumns;j++) {
					Integer userID = (Integer) user.getPrimaryKey();
					String mark = iwc.getParameter(userID.toString() + "_" + j);
					if(mark != null) {
						if(mark.equals("")) {
							Text emptyWarning = new Text(iwrb.getLocalizedString("ledgerWindow.emptyCellWarning","There is a empty cell, to you want to go on?"));
							setOnLoad("confirm('"+emptyWarning+"')");
						}
						//data validation
						//if mark is a part of the allowable marks: match is true!
						//b.toString() is a sequence of the allowable marks with a * after each mark!
						boolean match = Pattern.matches(b.toString(), mark);
						if(match) {
							//if the attendance mark exists in the database the marking is updated
							if(biz.getAttendanceByUserIDandEntry(userID.intValue(),(CalendarEntry) entries.get(h)) != null) {
								AttendanceEntity attendance = biz.getAttendanceByUserIDandEntry(userID.intValue(),(CalendarEntry) entries.get(h));
								Integer attendanceID = (Integer) attendance.getPrimaryKey();
								biz.updateAttendance(attendanceID.intValue(),userID.intValue(),ledgerID.intValue(),(CalendarEntry) entries.get(h++),mark);
							}
							//if no attendance mark exists, a new one is created
							else {
								biz.saveAttendance(userID.intValue(),ledgerID.intValue(),(CalendarEntry) entries.get(h++),mark);
							}							
						}
						else {
							setAlertOnLoad(mark + " is not a mark!");
							h++;
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
	
	public LedgerVariationsHandler getLedgerVariationsHandler(IWContext iwc) {
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
		return ledgerVariationsHandler;
		
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
