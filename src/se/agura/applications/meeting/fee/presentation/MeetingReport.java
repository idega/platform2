/*
 * $Id: MeetingReport.java,v 1.1 2004/12/05 20:59:37 anna Exp $
 * Created on 24.11.2004
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
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.User;
import com.idega.util.PersonalIDFormatter;


/**
 * Last modified: 24.11.2004 13:46:01 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.1 $
 */
public class MeetingReport extends MeetingFeeBlock {
	
	private static final String PARAMETER_HOURS = "meet_hours";
	private static final String PARAMETER_MINUTES = "meet_minutes";
	
	public void present(IWContext iwc) {
		String action = iwc.getParameter(PARAMETER_ACTION);
		
		if(action == null) {
			formPageOne();
		}		
		
		if(action.equals(ACTION_NEXT))/*OK á fyrstu sí›u*/ {
			formPageTwo(iwc);
		}
		
		if(action.equals(ACTION_SAVE)) {
			 //save(iwc);
		}
		//if action er CLEAR - vantar
		
		//ﬂa› á jafnvel a› koma ACTION_EDIT svo hægt sé a› breyta sk‡rslunni, kemur seinna ef kirkjan vill ﬂa›.
	}
	
	
	/*private void save(IWContext iwc) {
		
		IWTimestamp meetingDate = new IWTimestamp(iwc.getParameter(PARAMETER_MEETING_FEE_DATE));
		boolean meetingPlace = iwc.getParameter(PARAMETER_MEETING_FEE_MEETING_PLACE);
		
		try {
			getBusiness(iwc).storeApplication(iwc.getCurrentUser(),  congregation, speaker, meetingPlace, meetingDate.getDate(), participants, meetingHours, meetingMinutes, amount);
		}
		catch (CreateException ce) {
			log(ce);
		}
		
	}*/
	
	private Form formPageOne() {
		Form form = new Form();
		form.addParameter(PARAMETER_ACTION, ACTION_EDIT);
		form.addParameter(PARAMETER_ACTION, ACTION_NEW);
		form.addParameter(PARAMETER_ACTION, ACTION_NEXT);
		form.add(pageOneMeetingReport());
		
		return form;
	}
	
	private Form formPageTwo(IWContext iwc) {
		Form form = new Form();
		form.addParameter(PARAMETER_ACTION, ACTION_SAVE);
		//form.addParameter(PARAMETER_ACTION, ACTION_CLEAR); 
		try {
			form.add(pageTwoMeetingReport(iwc));
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
	
	
	
	
	private Table pageOneMeetingReport() {
		Table table = new Table();
		DropdownMenu congregMenu = new DropdownMenu(PARAMETER_MEETING_FEE_CONGREGATION);
		RadioButton meetingPlaceIn = new RadioButton(PARAMETER_MEETING_FEE_MEETING_LOCATION, Boolean.TRUE.toString());
		RadioButton meetingPlaceOut = new RadioButton(PARAMETER_MEETING_FEE_MEETING_LOCATION, Boolean.FALSE.toString());
		DropdownMenu participantsMenu = new DropdownMenu(PARAMETER_MEETING_FEE_PARTICIPANTS);
		
		//"Laddi, á ég a› búa til fall fyrir dropdownMenuin, eins og ﬂú ger›ir me› hour- og minuteMenu??????"
		//congregMenu.addMenuElement(int i, String a); ﬂarf ekki a› bæta gildum inn í bæ›i dropdown-in?
		//participantsMenu.addMenuElement(int bla, String bla)   
	
		
		int row = 1;
		table.add(getResourceBundle().getLocalizedString("meeting.fee.congregation","Congregation"),1,row++);
		table.add(congregMenu, 2, row);
		table.add(getResourceBundle().getLocalizedString("meeting.fee.speaker","Speaker"),1,row);//kannski Squealer, veidiggi?
		
		table.add(getResourceBundle().getLocalizedString("meeting.fee.meeting_location","Meeting location"),1,row++);
		
		table.add(meetingPlaceIn, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getResourceBundle().getLocalizedString("meeting.fee.in_commune","In commune"),1,row++);
		
		table.add(meetingPlaceOut, 1, row++);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getResourceBundle().getLocalizedString("meeting.fee.outside_commune","Outside commune"),1,row++);

		table.add(getResourceBundle().getLocalizedString("meeting.fee.date_of_meeting", "Date of meeting"),1,row);
		DateInput meetingDateInput = new DateInput(PARAMETER_MEETING_FEE_DATE);
		meetingDateInput.setAsNotEmpty(getResourceBundle().getLocalizedString("meeting.fee.date_not_empty", "This field may not be empty"));
		table.add(meetingDateInput, 2, row++);
		
		table.add(getResourceBundle().getLocalizedString("meeting.fee.participants","Participants"),1, row++);
		table.add(participantsMenu, 1, row);
		table.add(getNewGroupButton(), 2, row);
		table.add(getEditButton(), 3, row++);
		
		table.add(getNextButton(), 6 , row++);
		
		return table;
	}
	

	private Text getAmount(IWContext iwc, User user, MeetingFeeInfo info) {
		int hours = iwc.isParameterSet(PARAMETER_HOURS + "_" + user.getPrimaryKey()) ? Integer.parseInt(iwc.getParameter(PARAMETER_HOURS + "_" + user.getPrimaryKey())) : 0;
		int minutes = iwc.isParameterSet(PARAMETER_MINUTES + "_" + user.getPrimaryKey()) ? Integer.parseInt(iwc.getParameter(PARAMETER_MINUTES + "_" + user.getPrimaryKey())) : 0;
		
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
	
	private Table pageTwoMeetingReport(IWContext iwc) throws RemoteException {
		Table table = new Table();
		int groupID = Integer.parseInt(iwc.getParameter(PARAMETER_MEETING_FEE_PARTICIPANTS));
		Collection users = getUserBusiness(iwc).getUsersInGroup(groupID);
		Iterator iter = users.iterator();
		
		MeetingFee fee = null;
		try {
			fee = getBusiness(iwc).getMeetingFee(iwc.getParameter(PARAMETER_MEETING_FEE_ID));
		}
		catch(FinderException fe) {
			log(fe);
		}
		
		int maxHours = 0;
		if(iwc.getParameter(PARAMETER_MEETING_FEE_MEETING_LOCATION).equals("In commune"))		{
				maxHours = 8;
		}
		else
				if(iwc.getParameter(PARAMETER_MEETING_FEE_MEETING_LOCATION).equals("Outside commune")) {
					maxHours = 6;
				}
				
				
		int row = 1;
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
			table.add(getHoursDropdown(iwc, user, maxHours),4, row);
			table.add(getMinutesDropdown(iwc, user), 5, row);
			
				table.add(getAmount(iwc, user, info), 6, row++);
		}
		table.add(getCalculateButton(), 4, row);	
		//á s.s kannski barasta a› nota getBackButton() e›a getCancelButton() hér????
		//table.add(getClearButton(), 5, row); ﬂessi hreinsar tíma og mínútur, hvernig á a› forrita ﬂetta?
		table.add(getSaveButton(), 6, row++); //- jamm, ﬂarf hjálp vi› save() falli›!
		
		return table;
	}
	
	
	
	private SubmitButton getNewGroupButton() {
		SubmitButton newGroupButton = (SubmitButton) getButton(new SubmitButton(getResourceBundle().getLocalizedString("meeting.fee.new_group", "New group")));
		return newGroupButton;
	}
	
	//Laddi - ﬂa› vantar takka sem gerir Clear á tíma- og mínútudropdown-in! Hvernig geri ég hann?
	
	private DropdownMenu getHoursDropdown(IWContext iwc, User user, int maxHours) {
		DropdownMenu menu = new DropdownMenu(PARAMETER_HOURS + "_" + user.getPrimaryKey());
		for (int a = 1; a <= maxHours; a++) {
			menu.addMenuElement(a, String.valueOf(a));
		}
		if (iwc.isParameterSet(PARAMETER_HOURS + "_" + user.getPrimaryKey())) {
			menu.setSelectedElement(iwc.getParameter(PARAMETER_HOURS + "_" + user.getPrimaryKey()));
		}
		menu.addMenuElementFirst("0", "");
		return menu;
	}

	private DropdownMenu getMinutesDropdown(IWContext iwc, User user) {
		DropdownMenu menu = new DropdownMenu(PARAMETER_MINUTES + "_" + user.getPrimaryKey());
		menu.addMenuElement(0, "");
		menu.addMenuElement(0, "0");
		menu.addMenuElement(30, "30");
		if (iwc.isParameterSet(PARAMETER_MINUTES + "_" + user.getPrimaryKey())) {
			menu.setSelectedElement(iwc.getParameter(PARAMETER_MINUTES + "_" + user.getPrimaryKey()));
		}
		return menu;
	}
}