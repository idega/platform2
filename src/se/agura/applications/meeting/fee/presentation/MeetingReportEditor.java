/*
 * $Id: MeetingReportEditor.java,v 1.4 2005/02/15 11:12:37 anna Exp $ Created on
 * 2.12.2004
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

import se.agura.applications.meeting.fee.data.MeetingFee;
import se.agura.applications.meeting.fee.data.MeetingFeeFormula;
import se.agura.applications.meeting.fee.data.MeetingFeeInfo;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;

/**
 * Last modified: 2.12.2004 15:14:55 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna </a>
 * @version $Revision: 1.4 $
 */
public class MeetingReportEditor extends MeetingFeeBlock {

	private static final String PARAMETER_HOURS = "meet_hours";

	private static final String PARAMETER_MINUTES = "meet_minutes";

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.agura.applications.meeting.fee.presentation.MeetingFeeBlock#present(com.idega.presentation.IWContext)
	 */
	public void present(IWContext iwc) {
		String action = iwc.getParameter(PARAMETER_ACTION);
		if (action == null) {
			action = "";
		}

		if (action.equals("") || action.equals(ACTION_NEXT)) {
			add(getEditForm(iwc));
		}
		else if (action.equals(ACTION_SAVE)) {
			save(iwc);
			showMessage(getResourceBundle().getLocalizedString("meeting.fee.application_saved", "Application saved."));
		}
	}

	private void save(IWContext iwc) {
		MeetingFee fee = getMeetingFee(iwc);

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
			getBusiness(iwc).storeApplication(fee.getPrimaryKey(), iwc.getCurrentUser(), parishID, comment, participantGroupID, meetingDate.getDate(), meetingPlace, participants, hours, minutes, formula);
		}
		catch (CreateException ce) {
			log(ce);
		}
		catch (RemoteException re) {
			log(re);
		}
	}

	private Form getEditForm(IWContext iwc) {
		Form form = new Form();
		try {
			form.add(getEditCertifyingTable(iwc));
		}
		catch (RemoteException re) {
			log(re);
		}
		form.add(getCalculateButton());
		form.add(getSaveButton());
		
		form.maintainParameter(PARAMETER_MEETING_FEE_ID);
		
		form.maintainParameter(PARAMETER_MEETING_FEE_DATE);
		form.maintainParameter(PARAMETER_MEETING_FEE_MEETING_LOCATION);
		form.maintainParameter(PARAMETER_MEETING_FEE_PARTICIPANTS);
		form.maintainParameter(PARAMETER_MEETING_FEE_CONGREGATION);
		form.maintainParameter(PARAMETER_MEETING_FEE_COMMENT);

		return form;
	}

	private Text getAmount(IWContext iwc, User user, MeetingFeeInfo info) {
		int hours = iwc.isParameterSet(PARAMETER_HOURS + "_" + user.getPrimaryKey()) ? Integer.parseInt(iwc.getParameter(PARAMETER_HOURS + "_" + user.getPrimaryKey())) : 0;
		int minutes = iwc.isParameterSet(PARAMETER_MINUTES + "_" + user.getPrimaryKey()) ? Integer.parseInt(iwc.getParameter(PARAMETER_MINUTES + "_" + user.getPrimaryKey())) : 0;

		if (hours == 0 && info != null) {
			hours = info.getMeetingDuration() / 60;
			minutes = info.getMeetingDuration() % 60;
		}

		MeetingFeeFormula formula = null;
		if (info != null) {
			formula = info.getMeetingFeeFormula();
		}
		else {
			formula = getMeetingFeeFormula(iwc);
		}

		try {
			return getText(String.valueOf(getBusiness(iwc).calculateMeetingFee(hours, minutes, formula)));
		}
		catch (RemoteException re) {
			return getText("0");
		}
	}

	private Table getEditCertifyingTable(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setWidth(iWidth);
		table.setCellpadding(iCellpadding);
		table.setCellspacing(0);
		int row = 1;

		MeetingFee fee = getMeetingFee(iwc);

		DropdownMenu congregMenu = getCongregationMenu(iwc);
		congregMenu.keepStatusOnAction(true);
		congregMenu.setSelectedElement(fee.getCongregationGroupID());

		int maxHours = 0;
		RadioButton meetingPlaceIn = new RadioButton(PARAMETER_MEETING_FEE_MEETING_LOCATION, Boolean.TRUE.toString());
		meetingPlaceIn.keepStatusOnAction(true);
		RadioButton meetingPlaceOut = new RadioButton(PARAMETER_MEETING_FEE_MEETING_LOCATION, Boolean.FALSE.toString());
		meetingPlaceOut.keepStatusOnAction(true);
		if (fee.getInCommune()) {
			meetingPlaceIn.setSelected(true);
			maxHours = 8;
		}
		else {
			meetingPlaceOut.setSelected(true);
			maxHours = 6;
		}

		String comment = fee.getComment();
		
		Group participantsGroup = null;
		if (iwc.isParameterSet(PARAMETER_MEETING_FEE_PARTICIPANTS)) {
			try {
				participantsGroup = getUserBusiness(iwc).getGroupBusiness().getGroupByGroupID(Integer.parseInt(iwc.getParameter(PARAMETER_MEETING_FEE_PARTICIPANTS)));
			}
			catch (FinderException fe) {
				log(fe);
				participantsGroup = fee.getParticipantGroup();
			}
		}
		else {
			participantsGroup = fee.getParticipantGroup();
		}

		DropdownMenu participantsMenu = getParticipantsMenu(iwc, iwc.getCurrentUser());
		participantsMenu.keepStatusOnAction(true);
		participantsMenu.setSelectedElement(fee.getParticipantGroupID());
		participantsMenu.setToSubmit();

		Collection users = getUserBusiness(iwc).getUsersInGroup(participantsGroup);
		Iterator iter = users.iterator();

		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.congregation", "Congregation")), 1, row);
		table.add(congregMenu, 2, row++);
		table.setHeight(row++, 12);

		// table.add(getResourceBundle().getLocalizedString("meeting.fee.speaker","Speaker"),1,row++);
		// table.setHeight(row++, 12);

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
		meetingDateInput.setDate(fee.getMeetingDate());
		meetingDateInput.setAsNotEmpty(getResourceBundle().getLocalizedString("meeting.fee.date_not_empty", "This field may not be empty"));
		table.add(meetingDateInput, 2, row++);
		table.setHeight(row++, 12);
		
		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.comment","Comment")), 1, row);
		table.add(comment, 2, row++);
		table.setHeight(row++, 12);

		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.participants", "Participants")), 1, row);
		table.add(participantsMenu, 2, row++);
		table.setHeight(row++, 12);

		Table participantsTable = new Table();
		participantsTable.setWidth(iWidth);
		participantsTable.setCellpadding(iCellpadding);
		participantsTable.setCellspacing(0);
		table.mergeCells(1, row, table.getColumns(), row);
		table.add(participantsTable, 1, row);
		int participantsRow = 1;

		participantsTable.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.participants.last_name", "Last name")), 1, participantsRow);
		participantsTable.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.participants.first_name", "First name")), 2, participantsRow);
		participantsTable.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.participants.personal_number", "Personal number")), 3, participantsRow);
		participantsTable.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.participants.hours", "Hours")), 4, participantsRow);
		participantsTable.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.participants.minutes", "Minutes")), 5, participantsRow);
		participantsTable.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.participants.sum", "Sum")), 6, participantsRow++);
		while (iter.hasNext()) {
			User user = (User) iter.next();
			String lastName = user.getLastName();
			String firstName = user.getFirstName();
			String pId = PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale());
			MeetingFeeInfo info = null;
			try {
				info = getBusiness(iwc).getMeetingFeeInfo(fee, user);
			}
			catch (FinderException fe) {
				log(fe);
			}

			participantsTable.add(new HiddenInput(PARAMETER_PARTICIPANT_USER_ID, user.getPrimaryKey().toString()), 1, participantsRow);
			participantsTable.add(getText(lastName), 1, participantsRow);
			participantsTable.add(getText(firstName), 2, participantsRow);
			participantsTable.add(getText(pId), 3, participantsRow);
			participantsTable.add(getHoursDropdown(iwc, user, maxHours, info), 4, participantsRow);
			participantsTable.add(getMinutesDropdown(iwc, user, info), 5, participantsRow);
			participantsTable.add(getAmount(iwc, user, info), 6, participantsRow++);
		}

		table.setWidth(1, iHeaderColumnWidth);
		table.setCellpaddingLeft(1, 0);
		participantsTable.setCellpaddingLeft(1, 0);

		return table;
	}

	private DropdownMenu getHoursDropdown(IWContext iwc, User user, int maxHours, MeetingFeeInfo info) {
		DropdownMenu menu = (DropdownMenu) getInput(new DropdownMenu(PARAMETER_HOURS + "_" + user.getPrimaryKey()));
		menu.addMenuElement(0, "0");
		for (int a = 1; a <= maxHours; a++) {
			menu.addMenuElement(a, String.valueOf(a));
		}
		if (info != null) {
			int hours = info.getMeetingDuration() / 60;
			menu.setSelectedElement(hours);
		}
		if (iwc.isParameterSet(PARAMETER_HOURS + "_" + user.getPrimaryKey())) {
			menu.setSelectedElement(iwc.getParameter(PARAMETER_HOURS + "_" + user.getPrimaryKey()));
		}
		return menu;
	}

	private DropdownMenu getMinutesDropdown(IWContext iwc, User user, MeetingFeeInfo info) {
		DropdownMenu menu = (DropdownMenu) getInput(new DropdownMenu(PARAMETER_MINUTES + "_" + user.getPrimaryKey()));
		menu.addMenuElement(0, "00");
		menu.addMenuElement(30, "30");
		if (info != null) {
			int minutes = info.getMeetingDuration() % 60;
			menu.setSelectedElement(minutes);
		}
		if (iwc.isParameterSet(PARAMETER_MINUTES + "_" + user.getPrimaryKey())) {
			menu.setSelectedElement(iwc.getParameter(PARAMETER_MINUTES + "_" + user.getPrimaryKey()));
		}
		return menu;
	}
}