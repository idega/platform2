/*
 * $Id: MeetingReport.java,v 1.5 2004/12/09 14:12:15 laddi Exp $ Created on
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
import com.idega.user.app.UserApplication;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;

/**
 * Last modified: 24.11.2004 13:46:01 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna </a>
 * @version $Revision: 1.5 $
 */
public class MeetingReport extends MeetingFeeBlock {

	private static final String PARAMETER_HOURS = "meet_hours";

	private static final String PARAMETER_MINUTES = "meet_minutes";
	
	public void present(IWContext iwc) {
		String action = iwc.getParameter(PARAMETER_ACTION);
		if (action == null) {
			action = "";
		}

		if (action.equals(ACTION_NEXT)) {
			add(formPageTwo(iwc));
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
		
		String[] participants = iwc.getParameterValues(PARAMETER_PARTICIPANT_USER_ID);
		String[] hours = new String[participants.length];
		String[] minutes = new String[participants.length];
		
		for (int a = 0; a < participants.length; a++) {
			hours[a] = iwc.getParameter(PARAMETER_HOURS + "_" + participants[a]);
			minutes[a] = iwc.getParameter(PARAMETER_MINUTES + "_" + participants[a]);
		}
		
		MeetingFeeFormula formula = getMeetingFeeFormula(iwc);

		try {
			getBusiness(iwc).storeApplication(iwc.getCurrentUser(), parishID, participantGroupID, meetingDate.getDate(), meetingPlace, participants, hours, minutes, formula);
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
		form.add(getCalculateButton());
		form.add(getSaveButton());
		
		form.maintainParameter(PARAMETER_MEETING_FEE_CONGREGATION);
		form.maintainParameter(PARAMETER_MEETING_FEE_MEETING_LOCATION);
		form.maintainParameter(PARAMETER_MEETING_FEE_DATE);
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
		RadioButton meetingPlaceOut = (RadioButton) getRadioButton(new RadioButton(PARAMETER_MEETING_FEE_MEETING_LOCATION, Boolean.FALSE.toString()));
		DropdownMenu participantsMenu = getParticipantsMenu(iwc, iwc.getCurrentUser());

		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.congregation", "Congregation")), 1, row);
		table.add(congregMenu, 2, row++);
		// table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.speaker","Speaker")),1,row);
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
		DateInput meetingDateInput = (DateInput) getInput(new DateInput(PARAMETER_MEETING_FEE_DATE));
		meetingDateInput.setAsNotEmpty(getResourceBundle().getLocalizedString("meeting.fee.date_not_empty", "This field may not be empty"));
		table.add(meetingDateInput, 2, row++);
		table.setHeight(row++, 12);

		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.participants", "Participants")), 1, row++);
		table.mergeCells(1, row, table.getColumns(), row);
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

	private GenericButton getNewGroupButton() {
		GenericButton newGroupButton = getButton(new GenericButton("new_group", getResourceBundle().getLocalizedString("meeting.fee.edit_groups", "Edit groups")));
		newGroupButton.setWindowToOpen(UserApplication.class);
		return newGroupButton;
	}

	// Laddi - ßaÝ vantar takka sem gerir Clear ‡ t’ma- og m’nœtudropdown-in!
	// Hvernig geri Žg hann?

	private DropdownMenu getHoursDropdown(IWContext iwc, User user, int maxHours) {
		DropdownMenu menu = new DropdownMenu(PARAMETER_HOURS + "_" + user.getPrimaryKey());
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
		DropdownMenu menu = new DropdownMenu(PARAMETER_MINUTES + "_" + user.getPrimaryKey());
		menu.addMenuElement(0, "00");
		menu.addMenuElement(30, "30");
		if (iwc.isParameterSet(PARAMETER_MINUTES + "_" + user.getPrimaryKey())) {
			menu.setSelectedElement(iwc.getParameter(PARAMETER_MINUTES + "_" + user.getPrimaryKey()));
		}
		return menu;
	}
}