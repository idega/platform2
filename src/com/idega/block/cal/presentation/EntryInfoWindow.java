/*
 * Created on Mar 28, 2004
 */
package com.idega.block.cal.presentation;

import com.idega.block.cal.business.CalBusiness;
import com.idega.block.cal.data.CalendarEntry;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.CalendarParameters;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.util.IWTimestamp;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:birna@idega.is">Birna Iris Jonsdottir</a>
 */
public class EntryInfoWindow extends StyledIWAdminWindow{

	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.cal";
	
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
	
	private Table table;
	
	public EntryInfoWindow() {
		setHeight(400);
		setWidth(400);
		setResizable(true);
	}
	public void initializeTexts(IWContext iwc) {

		IWResourceBundle iwrb = getResourceBundle(iwc);
		headlineText = new Text(iwrb.getLocalizedString(headlineFieldParameterName,"Name"));
		headlineText.setStyleClass(boldText);
		typeText = new Text(iwrb.getLocalizedString(typeFieldParameterName,"Type"));
		typeText.setStyleClass(boldText);
		dayFromText = new Text(iwrb.getLocalizedString(dayFromFieldParameterName,"From day"));
		dayFromText.setStyleClass(boldText);
		dayToText = new Text(iwrb.getLocalizedString(dayToFieldParameterName,"To Day"));
		dayToText.setStyleClass(boldText);
		timeFromText = new Text(iwrb.getLocalizedString(timeFromFieldParameterName, "From time"));
		timeFromText.setStyleClass(boldText);
		timeToText = new Text(iwrb.getLocalizedString(timeToFieldParameterName, "To time"));
		timeToText.setStyleClass(boldText);
		descriptionText = new Text(iwrb.getLocalizedString(descriptionFieldParameterName,"Description"));
		descriptionText.setStyleClass(boldText);
		locationText = new Text(iwrb.getLocalizedString(locationFieldParameterName, "Location"));
		locationText.setStyleClass(boldText);		
	}
	public void initializeFields(IWContext iwc) {
		String entryIDString = iwc.getParameter(CalendarEntryCreator.entryIDParameterName);
		
		Integer entryID = new Integer(entryIDString);
		CalendarEntry entry = getCalBusiness(iwc).getEntry(entryID.intValue());
		
		headlineField = entry.getName();
		typeField = entry.getEntryTypeName();
		dayFromField = entry.getDate().toString();
		dayToField = entry.getEndDate().toString();
		
		locationField = entry.getLocation();
		descriptionField = entry.getDescription();
		
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
	}
	public void lineUp() {
		table = new Table();
		table.setCellspacing(0);
		table.setCellpadding(0);
		table.setStyleClass(borderAllWhiteStyle);
		table.add(headlineText,1,1);
		table.add(headlineField,2,1);
		table.add(typeText,1,2);
		table.add(typeField,2,2);
		table.add(dayFromText,1,3);
		table.add(dayFromField,2,3);
		table.add(dayToText,1,4);
		table.add(dayToField,2,4);
		table.add(locationText,1,5);
		table.add(locationField,2,5);
		table.add(descriptionText,1,6);
		table.add(descriptionField,2,6);
	}
	public void main(IWContext iwc) throws Exception {
		initializeTexts(iwc);
		initializeFields(iwc);
		lineUp();
		add(table,iwc);
		
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
	
	

}
