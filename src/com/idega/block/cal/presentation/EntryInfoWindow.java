/*
 * Created on Mar 28, 2004
 */
package com.idega.block.cal.presentation;

import java.util.ArrayList;
import java.util.Collection;

import com.idega.block.cal.business.CalBusiness;
import com.idega.block.cal.business.DefaultLedgerVariationsHandler;
import com.idega.block.cal.business.LedgerVariationsHandler;
import com.idega.block.cal.data.CalendarEntry;
import com.idega.block.cal.data.CalendarLedger;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.CalendarParameters;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.repository.data.RefactorClassRegistry;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:birna@idega.is">Birna Iris Jonsdottir</a>
 */
public class EntryInfoWindow extends StyledIWAdminWindow{

	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.cal";
	public static final String BUNDLE_KEY_LEDGER_VARIATIONS_HANDLER_CLASS = "ledger_variations_class";

	private static final String HELP_TEXT_KEY = "cal_entry_info";
	
	//parameter names
	public static String headlineFieldParameterName = "headline";
	public static String typeFieldParameterName = "type";
	public static String dayFromFieldParameterName = "dayFrom";
	public static String dayToFieldParameterName = "dayTo";
	public static String timeFromFieldParameterName ="timeFrom";
	public static String timeToFieldParameterName = "timeTo";
	public static String descriptionFieldParameterName = "description";
	public static String locationFieldParameterName = "location";
	
	//texts
	private Text headlineText;
	private Text typeText;
	private Text dayFromText;
	private Text dayToText;
	private Text timeFromText;
	private Text timeToText;
	private Text locationText;
	private Text descriptionText;
	
	//fields
	private Text groupNameField;
	private String clubNameField;
	private String headlineField;
	private String typeField;
	private String dayFromField;
	private String dayToField;
	private String timeFromField;
	private String timeToField;
	private String locationField;
	private String descriptionField;
	
	//styles
	private String boldText ="bold";
	private String borderAllWhiteStyle ="borderAllWhite";
	private String titleFont = "font-family:Verdana,Arial,Helvetica,sans-serif;font-size:9pt;font-weight:bold;color:#FFFFFF;";
	
	private Table table;
	
	public EntryInfoWindow() {
		setHeight(300);
		setWidth(400);
		setResizable(true);
		setScrollbar(false);
	}
	public void initializeTexts(IWContext iwc) {

		IWResourceBundle iwrb = getResourceBundle(iwc);
		this.headlineText = new Text(iwrb.getLocalizedString(headlineFieldParameterName,"Name"));
		this.headlineText.setStyleClass(this.boldText);
		this.typeText = new Text(iwrb.getLocalizedString(typeFieldParameterName,"Type"));
		this.typeText.setStyleClass(this.boldText);
		this.dayFromText = new Text(iwrb.getLocalizedString(dayFromFieldParameterName,"From day"));
		this.dayFromText.setStyleClass(this.boldText);
		this.dayToText = new Text(iwrb.getLocalizedString(dayToFieldParameterName,"To Day"));
		this.dayToText.setStyleClass(this.boldText);
		this.timeFromText = new Text(iwrb.getLocalizedString(timeFromFieldParameterName, "From time"));
		this.timeFromText.setStyleClass(this.boldText);
		this.timeToText = new Text(iwrb.getLocalizedString(timeToFieldParameterName, "To time"));
		this.timeToText.setStyleClass(this.boldText);
		this.descriptionText = new Text(iwrb.getLocalizedString(descriptionFieldParameterName,"Description"));
		this.descriptionText.setStyleClass(this.boldText);
		this.locationText = new Text(iwrb.getLocalizedString(locationFieldParameterName, "Location"));
		this.locationText.setStyleClass(this.boldText);		
	}
	public void initializeFields(IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		String entryIDString = iwc.getParameter(CalendarEntryCreator.entryIDParameterName);
		
		LedgerVariationsHandler ledgerVariationsHandler = getLedgerVariationsHandler(iwc);
		
		Integer entryID = new Integer(entryIDString);
		CalendarEntry entry = getCalBusiness(iwc).getEntry(entryID.intValue());
		
		int groupID = entry.getGroupID();
		int ledgerID = entry.getLedgerID();
		Collection parentGroups = null;
		try {
			if(groupID == -1 && ledgerID != -1) {
				groupID = getCalBusiness(iwc).getLedger(ledgerID).getGroupID();
			}
			if(groupID != -1) {
				this.groupNameField = new Text(getGroupBusiness(iwc).getGroupByGroupID(groupID).getName());
				parentGroups = new ArrayList(getGroupBusiness(iwc).getParentGroupsRecursive(getGroupBusiness(iwc).getGroupByGroupID(groupID)));				
			}
			else if(ledgerID != -1) {
				CalendarLedger ledger = getCalBusiness(iwc).getLedger(ledgerID);
				Group g = getGroupBusiness(iwc).getGroupByGroupID(ledger.getGroupID());
				this.groupNameField = new Text(g.getName());
				this.groupNameField.setStyleClass(this.boldText);
			}
			else {
				this.groupNameField = new Text(iwrb.getLocalizedString("entryinfowindow.no_group_text","No group"));
				this.groupNameField.setStyleClass(this.boldText);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(parentGroups != null) {
			this.clubNameField = ledgerVariationsHandler.getParentOfParentGroupName(parentGroups);

		}
		
		this.headlineField = entry.getName();
		this.typeField = iwrb.getLocalizedString("calendarEntry."+entry.getEntryTypeName(),entry.getEntryTypeName());
		this.dayFromField = new IWTimestamp(entry.getDate()).getDateString("dd MMM yyyy - HH.mm");
		this.dayToField = new IWTimestamp(entry.getEndDate()).getDateString("dd MMM yyyy - HH.mm");
		
		this.locationField = entry.getLocation();
		this.descriptionField = entry.getDescription();
		
		//stamp is needed to get the current day/week/month of the CalendarView 
		//the parameters are then set to the change link (see below)
		IWTimestamp stamp = null;
		if (stamp == null) {
			String day = iwc.getParameter(CalendarParameters.PARAMETER_DAY);
			String month = iwc.getParameter(CalendarParameters.PARAMETER_MONTH);
			String year = iwc.getParameter(CalendarParameters.PARAMETER_YEAR);

			if(month != null && !month.equals("") &&
					day != null && !day.equals("") &&
					year != null && !year.equals("")) {
				stamp = CalendarView.getTimestamp(day,month,year);			
			}
			else {
				stamp = IWTimestamp.RightNow();
			}
		}		
	}
	public void lineUp() {
		this.table = new Table();
		this.table.setCellspacing(0);
		this.table.setCellpadding(2);
		this.table.setStyleClass(this.borderAllWhiteStyle);
		this.table.add(this.clubNameField,1,1);
		this.table.add(" - ",1,1);
		this.table.add(this.groupNameField,1,1);
		this.table.add(this.headlineText,1,2);
		this.table.add(this.headlineField,2,2);
		this.table.add(this.typeText,1,3);
		this.table.add(this.typeField,2,3);
		this.table.add(this.dayFromText,1,4);
		this.table.add(this.dayFromField,2,4);
		this.table.add(this.dayToText,1,5);
		this.table.add(this.dayToField,2,5);
		this.table.add(this.locationText,1,6);
		this.table.add(this.locationField,2,6);
		this.table.add(this.descriptionText,1,7);
		this.table.add(this.descriptionField,2,7);
		
		this.table.add(getHelp(HELP_TEXT_KEY), 1, 8);
	}
	public void main(IWContext iwc) throws Exception {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		setTitle(iwrb.getLocalizedString("entryInfoWindow.entry_info","Entry info"));
		addTitle(iwrb.getLocalizedString("entryInfoWindow.entry_info","Entry info"),TITLE_STYLECLASS);
		
		initializeTexts(iwc);
		initializeFields(iwc);
		lineUp();
		add(this.table,iwc);
		
	}
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	public CalBusiness getCalBusiness(IWApplicationContext iwc) {
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

	
	

}
