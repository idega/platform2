/*
 * Created on Jan 15, 2004
 */
package com.idega.block.cal.presentation;

import com.idega.block.cal.business.CalBusiness;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;

/**
 * Description: <br>
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:birna@idega.is">Birna Iris Jonsdottir</a>
 */
public class CalendarWindow extends StyledIWAdminWindow{
	
	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.cal";
	
	private static final String HELP_TEXT_KEY = "cal_calendar";
	
	private Table table = null;
	private String borderWhiteTableStyle = "borderAllWhite";
	private String styledLink = "styledLink";
	private String titleFont = "font-family:Verdana,Arial,Helvetica,sans-serif;font-size:9pt;font-weight:bold;color:#FFFFFF;";
	
	private CalendarView calendar;
	private CalBusiness calBiz;
	
	
	public CalendarWindow() {
		setHeight(750);
		setWidth(900);
		setResizable(true);
		setScrollbar(true);
	}
	
	public void initializeWindow(IWContext iwc) {
		calendar = new CalendarView();
		table = new Table();
		table.setWidth("100%");
		table.setHeight(1,1,"100%");
		table.setAlignment("center");
		table.setCellspacing(5);
		table.add(calendar,1,1);
		table.add(getHelp(this.HELP_TEXT_KEY), 1, 3);
		add(table,iwc);
	}
	
	public void main(IWContext iwc) throws Exception {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		setTitle(iwrb.getLocalizedString("calendarWindow.calendar","Calendar"));
		addTitle(iwrb.getLocalizedString("calendarWindow.calendar","Calendar"),TITLE_STYLECLASS);
		initializeWindow(iwc);
				
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

