/*
 * $Id: MeetingReportEditor.java,v 1.1 2004/12/05 20:59:37 anna Exp $
 * Created on 2.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.agura.applications.meeting.fee.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
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
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.RadioButton;
import com.idega.user.data.User;
import com.idega.util.PersonalIDFormatter;


/**
 * Last modified: 2.12.2004 15:14:55 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.1 $
 */
public class MeetingReportEditor extends MeetingFeeBlock {

	private static final String PARAMETER_HOURS = "meet_hours";
	private static final String PARAMETER_MINUTES = "meet_minutes";
	/* (non-Javadoc)
	 * @see se.agura.applications.meeting.fee.presentation.MeetingFeeBlock#present(com.idega.presentation.IWContext)
	 */
	public void present(IWContext iwc) {
		String action = iwc.getParameter(PARAMETER_ACTION);
		
		if(action.equals(ACTION_EDIT)) {
			getEditForm(iwc);
		}
	}
	
	private Form getEditForm(IWContext iwc) {
		Form form = new Form();
		form.addParameter(PARAMETER_ACTION, ACTION_SAVE);
		//form.addParameter(PARAMETER_ACTION, ACTION_CLEAR);
		try {
			form.add(getEditCertifyingTable(iwc));
		}
		catch(RemoteException re) {
			log(re);
		}
		form.maintainParameter(PARAMETER_MEETING_FEE_CONGREGATION);
		form.maintainParameter(PARAMETER_MEETING_FEE_MEETING_LOCATION);
		form.maintainParameter(PARAMETER_MEETING_FEE_DATE);
		form.maintainParameter(PARAMETER_MEETING_FEE_PARTICIPANTS);
		return form;
	}
	
	
	private GenericButton getClearButton() {
		GenericButton clear = getButton(new GenericButton("clear", getResourceBundle().getLocalizedString("meeting.fee.clear", "Clear")));	
		return clear;
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
		
		double firstHourAmount = formula.getFirstHourAmount();
		double proceedingHourAmount = formula.getProceedingTimeAmount();
		double proceedingHours = (hours - 1) + (minutes / 60);
		
		double proceedingTimeAmount = proceedingHours * proceedingHourAmount;
		double sum = firstHourAmount + proceedingTimeAmount;	
		
		return new Text(String.valueOf(sum));
	}
	
	private Table getEditCertifyingTable(IWContext iwc) throws RemoteException{
		Table table = new Table();
		
		MeetingFee fee = null;
		try {
			fee = getBusiness(iwc).getMeetingFee(iwc.getParameter(PARAMETER_MEETING_FEE_ID));
		}
		catch(FinderException fe) {
			log(fe);
		}
		
		DropdownMenu congregMenu = new DropdownMenu(PARAMETER_MEETING_FEE_CONGREGATION);
		
		RadioButton meetingPlaceIn = new RadioButton(PARAMETER_MEETING_FEE_MEETING_LOCATION, Boolean.TRUE.toString());
		RadioButton meetingPlaceOut = new RadioButton(PARAMETER_MEETING_FEE_MEETING_LOCATION, Boolean.FALSE.toString());
		if (fee.getInCommune()) {
			meetingPlaceIn.setSelected(true);
		}
		else {
			meetingPlaceOut.setSelected(true);
		}
		
		DropdownMenu participantsMenu = new DropdownMenu(PARAMETER_MEETING_FEE_PARTICIPANTS);
		participantsMenu.setSelectedElement(fee.getParticipantGroupID()); 
		
		Collection users = getUserBusiness(iwc).getUsersInGroup(fee.getParticipantGroup());
		Iterator iter = users.iterator();
		
		int row = 1;
		
		table.add(getResourceBundle().getLocalizedString("meeting.fee.congregation","Congregation"),1,row++);
		table.add(congregMenu, 2, row);
		table.add(getResourceBundle().getLocalizedString("meeting.fee.speaker","Speaker"),1,row);
		table.add(getResourceBundle().getLocalizedString("meeting.fee.meeting_location","Meeting location"),1,row++);
		table.add(meetingPlaceIn, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getResourceBundle().getLocalizedString("meeting.fee.in_commune","In commune"),1,row++);
		
		table.add(meetingPlaceOut, 1, row++);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getResourceBundle().getLocalizedString("meeting.fee.outside_commune","Outside commune"),1,row++);
		
		table.add(getResourceBundle().getLocalizedString("meeting.fee.date_of_meeting", "Date of meeting"),1,row);
		
		DateInput meetingDateInput = new DateInput(PARAMETER_MEETING_FEE_DATE);
		meetingDateInput.setDate(fee.getMeetingDate());
		meetingDateInput.setAsNotEmpty(getResourceBundle().getLocalizedString("meeting.fee.date_not_empty", "This field may not be empty"));
		table.add(meetingDateInput, 2, row++);
		
		table.add(getResourceBundle().getLocalizedString("meeting.fee.participants","Participants"),1, row++);
		table.add(participantsMenu, 1, row);
		//..og hvernig átti nú a› gera bil (Table.BREAK) e-›
		//table.add(OKtakka, 2, row++);
		
		table.add(getResourceBundle().getLocalizedString("meeting.fee.participants.last_name","Last name"),1, row);
		table.add(getResourceBundle().getLocalizedString("meeting.fee.participants.first_name","First name"),2,row);
		table.add(getResourceBundle().getLocalizedString("meeting.fee.participants.personal_number","Personal number"),3, row);
		table.add(getResourceBundle().getLocalizedString("meeting.fee.participants.hours","Hours"), 4,row);
		table.add(getResourceBundle().getLocalizedString("meeting.fee.participants.minutes","Minutes"), 5, row);
		table.add(getResourceBundle().getLocalizedString("meeting.fee.participants.sum","Sum"), 6, row++);
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
			
			table.add(lastName, 1, row);
			table.add(firstName, 2, row);
			table.add(pId, 3, row);
			table.add(getHoursDropdown(iwc, user, 6, info),4, row);
			table.add(getMinutesDropdown(iwc, user, info), 5, row);
			table.add(getAmount(iwc, user, info), 6, row++);
		}
		table.add(getCalculateButton(), 4, row);
		table.add(getClearButton(), 5, row);
		table.add(getSaveButton(), 6, row++);
		
		
		return table;
	}
	private DropdownMenu getHoursDropdown(IWContext iwc, User user, int maxHours, MeetingFeeInfo info) {
		DropdownMenu menu = new DropdownMenu(PARAMETER_HOURS + "_" + user.getPrimaryKey());
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
		menu.addMenuElementFirst("-1", "");
		return menu;
	}

	private DropdownMenu getMinutesDropdown(IWContext iwc, User user, MeetingFeeInfo info) {
		DropdownMenu menu = new DropdownMenu(PARAMETER_MINUTES + "_" + user.getPrimaryKey());
		menu.addMenuElement(-1, "");
		menu.addMenuElement(0, "0");
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
