/*
 * Created on May 21, 2004
 */
package com.idega.block.cal.presentation;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.PrintButton;
import com.idega.presentation.ui.Window;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:birna@idega.is">Birna Iris Jonsdottir</a>
 */
public class PrintableCalendarView extends Window{
	
	public PrintableCalendarView() {
		setResizable(true);
		setScrollbar(true);
		setHeight(700);
		setWidth(550);
	}
	public void main(IWContext iwc) throws Exception{
		String groupString = iwc.getParameter("group_id");
		String userString = iwc.getParameter("user_id");
		int groupID = 0;
		int userID = 0;
		if(groupString != null) {
			groupID = Integer.parseInt(groupString);
		}
		if(userString != null) {
			userID = Integer.parseInt(userString);
		}
		CalendarView cal = new CalendarView();
		cal.setPrintableVersion(true);
		if(groupID != 0) {
			cal.setViewInGroupID(groupID);
		}
		if(userID != 0) {
			cal.setViewInUserID(userID);
		}
		Table t = new Table();
		PrintButton print = new PrintButton();
		t.add(cal,1,1);
		t.add(print,1,2);
		add(t);
	}
}
