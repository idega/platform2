/*
 * $Id: MeetingReportCertifier.java,v 1.3 2004/12/13 14:35:10 anna Exp $
 * Created on 25.11.2004
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
import se.agura.applications.meeting.fee.data.MeetingFeeInfo;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.ui.Form;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;


/**
 * Last modified: 25.11.2004 09:13:11 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.3 $
 */
public class MeetingReportCertifier extends MeetingFeeBlock {
	
	
	public void present(IWContext iwc) {
		try {
			String action = iwc.getParameter(PARAMETER_ACTION);
		
			if(action == null) {
				add(getCertifyingForm(iwc));
			}	
			else if(action.equals(ACTION_REJECT)) {
				getBusiness(iwc).rejectApplication(getMeetingFee(iwc), iwc.getCurrentUser());
				showMessage(getResourceBundle().getLocalizedString("meeting.fee.application_rejected", "Application has been rejected."));
			}
			else if(action.equals(ACTION_SEND)) {
				getBusiness(iwc).acceptApplication(getMeetingFee(iwc), iwc.getCurrentUser());
				showMessage(getResourceBundle().getLocalizedString("meeting.fee.application_accepted", "Application has been accepted."));
			}
		}
		catch (RemoteException re) {
			log(re);
		}
	}
	
	private Form getCertifyingForm(IWContext iwc) {
		MeetingFee fee = getMeetingFee(iwc);
		
		Form form = new Form();
		try {
			form.add(getCertifyingTable(iwc, fee));
		}
		catch(RemoteException re) {
			log(re);
		}
		form.add(new Break());
		form.add(getRejectButton());
		form.add(getEditButton(PARAMETER_MEETING_FEE_ID, fee.getPrimaryKey().toString()));
		form.add(getNextButton());
		
		return form;
	}
	
	
	
	private Table getCertifyingTable(IWContext iwc, MeetingFee meetingFee) throws RemoteException{
		Table table = new Table();
		table.setWidth(iWidth);
		table.setCellpadding(iCellpadding);
		table.setCellspacing(0);
		int row = 1;
		
		Group conGroup = meetingFee.getCongregationGroup();
		String conGroupName = conGroup.getName();
		User owner = meetingFee.getOwner();
		String ownerName = owner.getName();
		String location = meetingFee.getInCommune() ? getResourceBundle().getLocalizedString("meeting.fee.in_commune", "In commune") : getResourceBundle().getLocalizedString("meeting.fee.outside_of_commune", "Outside of commune");
		IWTimestamp meetingDate = new IWTimestamp(meetingFee.getMeetingDate());
		String comment = meetingFee.getComment();
		
		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.assignment_from","Assignment from")),1,row);
		table.add(getText(conGroupName), 2, row++);
		table.setHeight(row++, 12);

		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.speaker","Speaker")),1,row);
		table.add(getText(ownerName), 2, row++); 
		table.setHeight(row++, 12);
		
		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.meeting_location","Meeting location")), 1, row);
		table.add(getText(location), 2, row++);
		table.setHeight(row++, 12);
		
		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.meeting_date","Meeting date")), 1, row);
		table.add(getText(meetingDate.getLocaleDate(iwc.getCurrentLocale())), 2, row++); 
		table.setHeight(row++, 12);
		
		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.comment","Comment")), 1, row);
		table.add(comment, 2, row++);
		table.setHeight(row++, 12);
		
		table.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.participants","Participants")),1,row++);
		table.setHeight(row++, 3);
		
		Table participantTable = new Table();
		participantTable.setWidth(iWidth);
		participantTable.setCellpadding(iCellpadding);
		participantTable.setCellspacing(0);
		table.mergeCells(1, row, table.getColumns(), row);
		table.add(participantTable, 1, row);
		int participantRow = 1;
		
		participantTable.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.participants.last_name","Last name")),1, participantRow);
		participantTable.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.participants.first_name","First name")),2,participantRow);
		participantTable.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.participants.personal_number","Personal number")),3, participantRow);
		participantTable.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.participants.hours","Hours")), 4,participantRow);
		participantTable.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.participants.minutes","Minutes")), 5, participantRow);
		participantTable.add(getHeader(getResourceBundle().getLocalizedString("meeting.fee.participants.sum","Sum")), 6, participantRow++);
		
		try {
			Collection participants = getBusiness(iwc).getMeetingFeeInfo(meetingFee);
			Iterator iter = participants.iterator();
			while (iter.hasNext()) {
				MeetingFeeInfo info = (MeetingFeeInfo) iter.next();
				User user = info.getUser();
				String lastName = user.getLastName();
				String firstName = user.getFirstName();
				String pId = PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale());
				
				int hours = info.getMeetingDuration() / 60;
				int minutes = info.getMeetingDuration() % 60;
				
				participantTable.add(getText(lastName), 1, participantRow);
				participantTable.add(getText(firstName), 2, participantRow);
				participantTable.add(getText(pId), 3, participantRow);
				participantTable.add(getText(String.valueOf(hours)),4, participantRow);
				participantTable.add(getText(String.valueOf(minutes)), 5, participantRow);
				participantTable.add(getText(String.valueOf(info.getAmount())), 6, participantRow++);
			}
		}
		catch (FinderException fe) {
			log(fe);
		}

		table.setWidth(1, iHeaderColumnWidth);
		table.setCellpaddingLeft(1, 0);
		participantTable.setCellpaddingLeft(1, 0);
		
		return table;
	}
	
}
