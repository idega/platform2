/*
 * $Id: MeetingReport.java,v 1.13 2005/02/08 12:17:13 anna Exp $ Created on
 * 24.11.2004
 * 
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package se.agura.applications.meeting.fee.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.agura.applications.meeting.fee.data.MeetingFeeFormula;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.user.app.UserApplication;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;

/**
 * Last modified: 24.11.2004 13:46:01 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna </a>
 * @version $Revision: 1.13 $
 */
public class MeetingReport extends MeetingFeeBlock {

	private static final String PARAMETER_HOURS = "meet_hours";

	private static final String PARAMETER_MINUTES = "meet_minutes";
	
	private static final String ACTION_SHOW_REPORT = "show_report";
	
	public void present(IWContext iwc) {
		String action = iwc.getParameter(PARAMETER_ACTION);
		if (action == null) {
			action = "";
		}

		if (action.equals(ACTION_NEXT)) {
			add(formPageTwo(iwc));
		}
		else if(action.equals(ACTION_SHOW_REPORT)) {
			add(formPageThree(iwc));
		}
		else if (action.equals(ACTION_SAVE)) {
			save(iwc);
			showMessage(getResourceBundle().getLocalizedString("meeting.fee.application_sent", "Your application has been sent."));
		}
		else {
			add(formPageOne(iwc));
		}
	}

	private void save(IWContext iwc) {
		IWTimestamp meetingDate = new IWTimestamp(iwc.getParameter(PARAMETER_MEETING_FEE_DATE));
		boolean meetingPlace = new Boolean(iwc.getParameter(PARAMETER_MEETING_FEE_MEETING_LOCATION)).booleanValue();
		int parishID = Integer.parseInt(iwc.getParameter(PARAMETER_MEETING_FEE_CONGREGATION));
		int participantGroupID = Integer.parseInt(iwc.getParameter(PARAMETER_MEETING_FEE_PARTICIPANTS));
		String comment = iwc.getParameter(PARAMETER_MEETING_FEE_COMMENT);
		
		String[] participants = iwc.getParameterValues(PARAMETER_PARTICIPANT_USER_ID);
		String[] hours = new String[participants.length];
		String[] minutes = new String[participants.length];
		
		for (int a = 0; a < participants.length; a++) {
			hours[a] = iwc.getParameter(PARAMETER_HOURS + "_" + participants[a]);
			minutes[a] = iwc.getParameter(PARAMETER_MINUTES + "_" + participants[a]);
		}
		
		MeetingFeeFormula formula = getMeetingFeeFormula(iwc);

		try {
			getBusiness(iwc).storeApplication(iwc.getCurrentUser(), parishID, comment, participantGroupID, meetingDate.getDate(), meetingPlace, participants, hours, minutes, formula);
		}
		catch (CreateException ce) {
			log(ce);
		}
		catch (RemoteException re) {
			log(re);
		}
	}

	private Form formPageOne(IWContext iwc) {
		Form form = new Form();
		form.add(pageOneMeetingReport(iwc));
		form.add(new Break());
		form.add(getNextButton());

		return form;
	}

	private Form formPageTwo(IWContext iwc) {
		Form form = new Form();
		try {
			form.add(pageTwoMeetingReport(iwc));
		}
		catch (RemoteException re) {
			log(re);
		}
		form.add(new Break());
		form.add(getBackButton());
		form.add(getCalculateButton());
		form.add(getShowReportButton());
		
		form.maintainParameter(PARAMETER_MEETING_FEE_CONGREGATION);
		form.maintainParameter(PARAMETER_MEETING_FEE_MEETING_LOCATION);
		form.maintainParameter(PARAMETER_MEETING_FEE_DATE);
		form.maintainParameter(PARAMETER_MEETING_FEE_COMMENT);
		form.maintainParameter(PARAMETER_MEETING_FEE_PARTICIPANTS);
		return form;
	}
	
	private Form formPageThree(IWContext iwc) {
		Form form = new Form();
		try {
			form.add(pageThreeMeetingReport(iwc));
		}
		catch(RemoteException re) {
			log(re);
		}
		form.add(new Break());
		form.add(getBackButton());
		form.add(getSaveButton());
		
		form.maintainParameter(PARAMETER_MEETING_FEE_CONGREGATION);
		form.maintainParameter(PARAMETER_MEETING_FEE_MEETING_LOCATION);
		form.maintainParameter(PARAMETER_MEETING_FEE_DATE);
		form.maintainParameter(PARAMETER_MEETING_FEE_COMMENT);
		form.maintainParameter(PARAMETER_MEETING_FEE_PARTICIPANTS);
		
		return form;
	}

	private Table pageOneMeetingReport(IWContext iwc) {
		Table table = new Table();
		table.setWidth(iWidth);
		table.setCellpadding(iCellpadding);
		table.setCellspacing(0);
		int row = 1;

		DropdownMenu congregMenu = getCongregationMenu(iwc);
		RadioButton meetingPlaceIn = (RadioButton) getRadioButton(new RadioButton(PARAMETER_MEETING_FEE_MEETING_LOCATION, Boolean.TRUE.toString()));
		meetingPlaceIn.setMustBeSelected(getResourceBundle().getLocalizedString("meeting.fee.place_not_empty", "Meeting place can not be empty"));
		RadioButton meetingPlaceOut = (RadioButton) getRadioButton(new RadioButton(PARAMETER_MEETING_FEE_MEETING_LOCATION, Boolean.FALSE.toString()));
		
		IWTimestamp stamp = new IWTimestamp();
		stamp.addDays(1);
		DateInput meetingDateInput = (DateInput) getInput(new DateInput(PARAMETER_MEETING_FEE_DATE));
		meetingDateInput.setYearRange(stamp.getYear(), stamp.getYear() - 2);
		meetingDateInput.setLatestPossibleDate(stamp.getDate(), getResourceBundle().getLocalizedString("meeting.fee.no_future_dates", "You can't select dates ahead in time"));
		meetingDateInput.setAsNotEmpty(getResourceBundle().getLocalizedString("meeting.fee.must_select_date", "You must select a meeting date"));
		
		DropdownMenu participantsMenu = getParticipantsMenu(iwc, iwc.getCurrentUser());
		TextArea comment = new TextArea(PARAMETER_MEETING_FEE_COMMENT);
		comment.setWidth(Table.HUNDRED_PERCENT);
		comment.setRows(4);

		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.congregation", "Congregation")), 1, row);
		table.add(congregMenu, 2, row++);
		// table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.chairman","Chairman")),1,row);
		table.setHeight(row++, 12);

		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.meeting_location", "Meeting location")), 1, row++);

		table.add(meetingPlaceIn, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getText(getResourceBundle().getLocalizedString("meeting.fee.in_commune", "In commune")), 1, row++);

		table.add(meetingPlaceOut, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getText(getResourceBundle().getLocalizedString("meeting.fee.outside_commune", "Outside commune")), 1, row++);
		table.setHeight(row++, 12);

		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.date_of_meeting", "Date of meeting")), 1, row);
		meetingDateInput.setAsNotEmpty(getResourceBundle().getLocalizedString("meeting.fee.date_not_empty", "This field may not be empty"));
		meetingDateInput.setLatestPossibleDate(stamp.getDate(), getResourceBundle().getLocalizedString("meeting.fee.meeting_date_forward_in_time", "Meeting date can not be forward in time."));
		table.add(meetingDateInput, 2, row++);
		table.setHeight(row++, 12);
		
		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.comment", "Comment")), 1, row);
		table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
		table.add(comment, 2, row++);
		table.setHeight(row++, 12);

		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.participants", "Participants")), 1, row++);
		table.mergeCells(1, row, table.getColumns(), row);
		table.setNoWrap(1, row);
		table.add(participantsMenu, 1, row);
		
		try {
			if (getUserBusiness(iwc).hasTopNodes(iwc.getCurrentUser(), iwc)) {
				table.add(Text.getNonBrakingSpace(), 1, row);
				table.add(getNewGroupButton(), 1, row);
			}
		}
		catch (RemoteException re) {
			log(re);
		}
		
		table.setWidth(1, iHeaderColumnWidth);
		//ßessi l’na hŽr fyrir neÝan lagar vonandi ßaÝ aÝ dropdown og takkinn liggja ‡ rŽttum stšÝum
		table.setWidth(1, row, Table.HUNDRED_PERCENT);
		table.setCellpaddingLeft(1, 0);

		return table;
	}

	private Text getAmount(IWContext iwc, User user) {
		int hours = iwc.isParameterSet(PARAMETER_HOURS + "_" + user.getPrimaryKey()) ? Integer.parseInt(iwc.getParameter(PARAMETER_HOURS + "_" + user.getPrimaryKey())) : 0;
		int minutes = iwc.isParameterSet(PARAMETER_MINUTES + "_" + user.getPrimaryKey()) ? Integer.parseInt(iwc.getParameter(PARAMETER_MINUTES + "_" + user.getPrimaryKey())) : 0;

		MeetingFeeFormula formula = getMeetingFeeFormula(iwc);

		try {
			return getText(String.valueOf(getBusiness(iwc).calculateMeetingFee(hours, minutes, formula)));
		}
		catch (RemoteException re) {
			log(re);
			return getText("0");
		}
	}

	private Table pageTwoMeetingReport(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setWidth(iWidth);
		table.setCellpadding(iCellpadding);
		table.setCellspacing(0);
		int row = 1;

		int groupID = Integer.parseInt(iwc.getParameter(PARAMETER_MEETING_FEE_PARTICIPANTS));
		Collection users = getUserBusiness(iwc).getUsersInGroup(groupID);
		Iterator iter = users.iterator();

		int maxHours = 0;
		if (iwc.getParameter(PARAMETER_MEETING_FEE_MEETING_LOCATION).equals(Boolean.TRUE.toString())) {
			maxHours = 8;
		}
		else {
			maxHours = 6;
		}

		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.participants.last_name", "Last name")), 1, row);
		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.participants.first_name", "First name")), 2, row);
		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.participants.personal_number", "Personal number")), 3, row);
		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.participants.hours", "Hours")), 4, row);
		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.participants.minutes", "Minutes")), 5, row);
		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.participants.sum", "Sum")), 6, row++);
		while (iter.hasNext()) {
			User user = (User) iter.next();
			String lastName = user.getLastName();
			String firstName = user.getFirstName();
			String pId = PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale());

			table.add(new HiddenInput(PARAMETER_PARTICIPANT_USER_ID, user.getPrimaryKey().toString()), 1, row);
			table.add(getText(lastName), 1, row);
			table.add(getText(firstName), 2, row);
			table.add(getText(pId), 3, row);
			table.add(getHoursDropdown(iwc, user, maxHours), 4, row);
			table.add(getMinutesDropdown(iwc, user), 5, row);

			table.add(getAmount(iwc, user), 6, row++);
		}

		table.setCellpaddingLeft(1, 0);

		return table;
	}
	
	private Table pageThreeMeetingReport(IWContext iwc) throws RemoteException{
		Table table = new Table();
		table.setWidth(iWidth);
		table.setCellpadding(iCellpadding);
		table.setCellspacing(0);
		table.setColumns(6);
		int row = 1;
	  
		//viÝb¾tt
		Group parish = null;
		Collection supervisors = null;
		//viÝb¾tt
		
		String congregation = null;
		
		try {
			//viÝb¾tt
			parish = getUserBusiness(iwc).getGroupBusiness().getGroupByGroupID(Integer.parseInt(iwc.getParameter(PARAMETER_MEETING_FEE_CONGREGATION)));
			supervisors = parish.getChildGroups(new String[]{"supervisor"}, true);
			//viÝb¾tt
			congregation = parish.getName();
		}
		catch (RemoteException re) {
			log(re);
		}
		catch (FinderException fe) {
			log(fe);
		}
		catch (NumberFormatException nfe) {
			log(nfe);
		}
		//String userName = iwc.getCurrentUser().getName();
		
		boolean inCommune = new Boolean(iwc.getParameter(PARAMETER_MEETING_FEE_MEETING_LOCATION)).booleanValue();
		String location = null;
		if (inCommune) {
			location = getResourceBundle().getLocalizedString("meeting.fee.in_commune", "In commune");
		}
		else {
			location = getResourceBundle().getLocalizedString("meeting.fee.outside_commune", "Outside commune");
		}
		
		//viÝb¾tt	
		if (supervisors != null) {
			
			String supervisorName = null; 
			Iterator iterator = supervisors.iterator();
			if (iterator.hasNext()) {
			User superv = (User) iterator.next();
			supervisorName = superv.getName();
			}	
		//viÝb¾tt
			
		
		IWTimestamp meetingDate = new IWTimestamp(iwc.getParameter(PARAMETER_MEETING_FEE_DATE));
		String comment = iwc.getParameter(PARAMETER_MEETING_FEE_COMMENT);
		
		table.mergeCells(2, row, table.getColumns(), row);
		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.assignment_from","Assignment from")),1,row);
		table.add(getText(congregation), 2, row++);
		table.setHeight(row++, 12);
		
		table.mergeCells(2, row, table.getColumns(), row);
		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.speaker","Speaker")),1,row);
		//NB! UserName should show the name of the supervisor of the same group as this user belongs to!!!
		table.add(getText(supervisorName), 2, row++);
		//table.add(getText(userName), 2, row++); 
		table.setHeight(row++, 12);
		
		table.mergeCells(2, row, table.getColumns(), row);
		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.meeting_location","Meeting location")), 1, row);
		table.add(getText(location), 2, row++);
		table.setHeight(row++, 12);
		
		table.mergeCells(2, row, table.getColumns(), row);
		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.meeting_date","Meeting date")), 1, row);
		table.add(getText(meetingDate.getLocaleDate(iwc.getCurrentLocale())), 2, row++); 
		table.setHeight(row++, 12);
		
		table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
		table.mergeCells(2, row, table.getColumns(), row);
		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.comment","Comment")), 1, row);
		table.add(getText(comment), 2, row++);
		table.setHeight(row++, 18);
		
		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.participants","Participants")),1,row);
		
		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.participants.full_name","Name")),2, row);
		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.participants.personal_number","Personal number")),3, row);
		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.participants.hours","Hours")), 4,row);
		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.participants.minutes","Minutes")), 5, row);
		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.participants.sum","Sum")), 6, row++);
		
		int groupID = Integer.parseInt(iwc.getParameter(PARAMETER_MEETING_FEE_PARTICIPANTS));
		Collection users = getUserBusiness(iwc).getUsersInGroup(groupID);
		Iterator iter = users.iterator();		
		
		while (iter.hasNext()) {
			User user = (User) iter.next();
			String name = user.getName();
			String pId = PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale());
			String hour = iwc.getParameter(PARAMETER_HOURS + "_" + user.getPrimaryKey());
			String minute = iwc.getParameter(PARAMETER_MINUTES + "_" + user.getPrimaryKey());
			
			table.add(new HiddenInput(PARAMETER_PARTICIPANT_USER_ID, user.getPrimaryKey().toString()), 1, row);
			table.add(new HiddenInput(PARAMETER_HOURS + "_" + user.getPrimaryKey(), hour), 1, row);
			table.add(new HiddenInput(PARAMETER_MINUTES + "_" + user.getPrimaryKey(), minute), 1, row);
			table.add(getText(name), 2, row);
			table.add(getText(pId), 3, row);
			table.add(hour, 4, row);
			table.add(minute, 5, row);

			table.add(getAmount(iwc, user), 6, row++);
		}
		}
		table.setCellpaddingLeft(1, 0);

		return table;
	}
	
	private SubmitButton getShowReportButton() {
		SubmitButton showReportButton = (SubmitButton) getButton(new SubmitButton(getResourceBundle().getLocalizedString("meeting.fee.show_report", "Next"), PARAMETER_ACTION, ACTION_SHOW_REPORT));
		showReportButton.setToolTip(getResourceBundle().getLocalizedString("meeting.fee.show_report.tooltip","shows your report"));
		return showReportButton;
	}

	private GenericButton getNewGroupButton() {
		GenericButton newGroupButton = getButton(new GenericButton("new_group", getResourceBundle().getLocalizedString("meeting.fee.edit_groups", "Edit groups")));
		newGroupButton.setWindowToOpen(UserApplication.class);
		return newGroupButton;
	}

	private DropdownMenu getHoursDropdown(IWContext iwc, User user, int maxHours) {
		DropdownMenu menu = (DropdownMenu) getInput(new DropdownMenu(PARAMETER_HOURS + "_" + user.getPrimaryKey()));
		menu.addMenuElement(0, "0");
		for (int a = 1; a <= maxHours; a++) {
			menu.addMenuElement(a, String.valueOf(a));
		}
		if (iwc.isParameterSet(PARAMETER_HOURS + "_" + user.getPrimaryKey())) {
			menu.setSelectedElement(iwc.getParameter(PARAMETER_HOURS + "_" + user.getPrimaryKey()));
		}
		return menu;
	}

	private DropdownMenu getMinutesDropdown(IWContext iwc, User user) {
		DropdownMenu menu = (DropdownMenu) getInput(new DropdownMenu(PARAMETER_MINUTES + "_" + user.getPrimaryKey()));
		menu.addMenuElement(0, "00");
		menu.addMenuElement(30, "30");
		if (iwc.isParameterSet(PARAMETER_MINUTES + "_" + user.getPrimaryKey())) {
			menu.setSelectedElement(iwc.getParameter(PARAMETER_MINUTES + "_" + user.getPrimaryKey()));
		}
		return menu;
	}
}