/*
 * Created on Feb 22, 2004
 */
package com.idega.block.cal.presentation;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import com.idega.block.cal.business.CalBusiness;
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
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
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
	
	public static final String GROUP ="group";
	public static final String LEDGER ="ledger";
	
	//parameter names
	private static String coachFieldParameterName = "coach";
	private static String otherCoachesFieldParameterName = "otherCoaches";
	private static String groupFieldParameterName = "group";
	private static String dateFieldParameterName = "date";
	private static String saveButtonParameterName = "save";
	private static String saveButtonParameterValue = "save";
		
	//display texts 
	private Text coachText;
	private Text otherCoachesText;
	private Text groupText;
	private Text dateText;
	
	//fields
	private Text coachNameField;
	private String otherCoachesNameField;
	private String groupNameField;
	private String fromDateField;
	
	//buttons
	private SubmitButton saveButton;
	private CloseButton closeButton;
	private PrintButton printButton;
	
	private CalBusiness calBiz = null;
	private GroupBusiness groupBiz = null;

	private Form form;
	private Table mainTable;
	
	private String mainTableStyle = "main";
	private String styledLink = "styledLink";
	private String styledLinkUnderline = "styledLinkUnderline";
	
	private String groupString = null;
	private Integer groupID;
	private String ledgerString = null;
	private Integer ledgerID = null;
	
	public LedgerWindow() {
		setHeight(400);
		setWidth(400);
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
			coaches = getGroupBusiness(iwc).getGroupByGroupID(coachGroupID);
			trainers = (List) getGroupBusiness(iwc).getUsers(coaches);
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
		
		//

		
		int groupID = getCalendarBusiness(iwc).getLedger(lID.intValue()).getGroupID();
		try {
			groupNameField = getGroupBusiness(iwc).getGroupByGroupID(groupID).getName();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		Timestamp fromD = getCalendarBusiness(iwc).getLedger(lID.intValue()).getDate();
		IWTimestamp iwFromD = new IWTimestamp(fromD);
		//fromDate is the start date of the ledger
		fromDateField = iwFromD.getDateString("dd. MMMMMMM yyyy");

		
		//when save button is pushed the new ledger is created
		saveButton = new SubmitButton(iwrb.getLocalizedString("ledgerwindow.save","Save"),saveButtonParameterName,saveButtonParameterValue);
		//closes the window
		closeButton = new CloseButton(iwrb.getLocalizedString("ledgerwindow.close","Close"));
		
		printButton = new PrintButton(iwrb.getLocalizedString("ledgerwindow.print","Print"));
		
	}

	/**
	 * lines up the gui
	 *
	 */
	public void lineUp(IWContext iwc, List marks) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		mainTable = new Table();
		mainTable.setWidth(288);
		mainTable.setCellspacing(0);
		mainTable.setCellpadding(5);
		mainTable.setStyleClass(mainTableStyle);
		mainTable.add(coachText,1,1);
		mainTable.add(coachNameField,2,1);
		mainTable.setVerticalAlignment(1,2,"top");
		mainTable.add(otherCoachesText,1,2);
		mainTable.add(otherCoachesNameField,2,2);
		mainTable.add(groupText,1,3);
		mainTable.add(groupNameField,2,3);
		mainTable.add(dateText,1,4);
		mainTable.add(fromDateField,2,4);
		
		mainTable.mergeCells(3,1,3,4);
		mainTable.add(iwrb.getLocalizedString("ledgerwindow.allowed_marks", "Allowed marks") ,3,1);
		mainTable.add("<br><br>",3,1);
		Iterator markIter = marks.iterator();
		while(markIter.hasNext()) {
			AttendanceMark mark = (AttendanceMark) markIter.next();
			mainTable.add(mark.getMark()+": "+mark.getMarkDescription(),3,1);
			mainTable.add("<br>",3,1);
		}
				
		mainTable.mergeCells(1,7,3,7);
		mainTable.add(getUserList(iwc),1,7);
	
		mainTable.setAlignment(3,8,"right");
		mainTable.add(saveButton,3,8);
		mainTable.add(Text.NON_BREAKING_SPACE,3,8);
		mainTable.add(closeButton,3,8);
		mainTable.add(Text.NON_BREAKING_SPACE,3,8);
		mainTable.add(printButton,3,8);
		
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
					Integer groupID = (Integer) g.getPrimaryKey();
					if(groupID.intValue() == ledger.getGroupID()) {
						isInGroup = true;
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

				try {
					entryList = (List) getCalendarBusiness(iwc).getPracticesByLedgerID(ledgerID.intValue());
				}
				catch (Exception ex) {
					ex.printStackTrace(System.err);
				}
				
				Iterator entryIter = entryList.iterator();
				int column = 2;
				while(entryIter.hasNext()) {					
					entry = (CalendarEntry) entryIter.next();
						Timestamp date = entry.getDate();
						int month = date.getMonth()+1;//date.getMonth() = 0 represents January!
						String dateString = date.getDate() + "/" + month;
						attendanceDateTable.setWidth(column,1,30);
						attendanceDateTable.add(dateString,column,1);					
						column++;
				}
				return attendanceDateTable;
			}

			public PresentationObject getPresentationObject(Object entity, EntityPath path, EntityBrowser browser, IWContext iwc) {
				Table attendanceMarkTable = new Table();
				User user = (User) entity;
				List entryList = null;
				
				try {
					entryList = (List) getCalendarBusiness(iwc).getPracticesByLedgerID(ledgerID.intValue());
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
					try {
						AttendanceEntity attendance = getCalendarBusiness(iwc).getAttendanceByUserIDandEntry(userID.intValue(),(CalendarEntry) entryList.get(h++));
						mark = attendance.getAttendanceMark();//iwc.getParameter(user.getPrimaryKey().toString() + "_" + i);
						
					}catch(Exception e) {
						e.printStackTrace();
					}						
					
					
					if(mark != null && !mark.equals("")) {
						input.setValue(mark);
					}	
					attendanceMarkTable.setWidth(i,1,30);
					attendanceMarkTable.add(input,i,1);						
				}
				return attendanceMarkTable;


			}
		};
		Collection users = null;
		
		try {
			users = getCalendarBusiness(iwc).getUsersInLedger(ledgerID.intValue());
		} catch (Exception e){
			e.printStackTrace();
		}

		IWResourceBundle resourceBundle = getResourceBundle(iwc);
		EntityBrowser entityBrowser = new EntityBrowser();

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
		
		entityBrowser.setEntityToPresentationConverter("Delete", converterToDeleteButton);
		entityBrowser.setEntityToPresentationConverter(nameKey, converterLink);
		entityBrowser.setEntityToPresentationConverter(entryKey,converterAttendance);
		
		entityBrowser.addEntity(CalendarEntry.class.getName());
		
		
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
		Link statisticsLink = new Link("Statistics");
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
				
		HiddenInput hi = new HiddenInput(LEDGER,ledgerString);
		form.add(hi);

		Collection users = null;
		Collection entries = null;
		List marks = null;
		
		try {
			users = getCalendarBusiness(iwc).getUsersInLedger(ledgerID.intValue());
			entries = getCalendarBusiness(iwc).getEntriesByLedgerID(ledgerID.intValue());
			marks = getCalendarBusiness(iwc).getAllMarks();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		lineUp(iwc,marks);
		
		//put the entries for the current ledger to a list of CalendarEntries
		//to make them accessible through the List get(int i) method

		List entryList = new ArrayList(entries);
		
		System.out.println("users: " + users.toString());
		
		
		String save = iwc.getParameter(saveButtonParameterName);
		if(save != null) {
			int userColumns = entryList.size();
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
							if(biz.getAttendanceByUserIDandEntry(userID.intValue(),(CalendarEntry) entryList.get(h)) != null) {
								AttendanceEntity attendance = biz.getAttendanceByUserIDandEntry(userID.intValue(),(CalendarEntry) entryList.get(h));
								Integer attendanceID = (Integer) attendance.getPrimaryKey();
								getCalendarBusiness(iwc).updateAttendance(attendanceID.intValue(),userID.intValue(),ledgerID.intValue(),(CalendarEntry) entryList.get(h++),mark);
							}
							//if no attendance mark exists, a new one is created
							else {
								getCalendarBusiness(iwc).saveAttendance(userID.intValue(),ledgerID.intValue(),(CalendarEntry) entryList.get(h++),mark);
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
