/*
 * $Id: MeetingReportCertifier.java,v 1.1 2004/12/05 20:59:37 anna Exp $
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
import se.agura.applications.meeting.fee.data.MeetingFee;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.Form;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;


/**
 * Last modified: 25.11.2004 09:13:11 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.1 $
 */
public class MeetingReportCertifier extends MeetingFeeBlock {
	
	
	public void present(IWContext iwc) {
		String action = iwc.getParameter(PARAMETER_ACTION);
		
		if(action == null) {
			getCertifyingForm(iwc);
		}	
		if(action.equals(ACTION_REJECT)) {
			//SENDA TIL BAKA TIL ASSISTANT
		}
		if(action.equals(ACTION_EDIT)) {
			//ﬂá yfir í MeetingReportEditor - hvernig?
		}
		
		if(action.equals(ACTION_SEND)) {
			//save() and set as errand at the economics department
		}
	}
	
	//JavaScript sem vantar
	//ef ‡tt er á Ok ﬂá kemur upp gluggi me› Cancel og OK (Are you sure you want to sign this report?!)
	private Form getCertifyingForm(IWContext iwc) {
		Form form = new Form();
		try {
			form.add(getCertifyingTable(iwc));
		}
		catch(RemoteException re) {
			log(re);
		}
		form.addParameter(PARAMETER_ACTION, ACTION_SEND);
		form.addParameter(PARAMETER_ACTION, ACTION_EDIT);
		form.addParameter(PARAMETER_ACTION, ACTION_REJECT);
		form.maintainParameter(PARAMETER_MEETING_FEE_CONGREGATION);
		form.maintainParameter(PARAMETER_MEETING_FEE_MEETING_LOCATION);
		form.maintainParameter(PARAMETER_MEETING_FEE_DATE);
		form.maintainParameter(PARAMETER_MEETING_FEE_PARTICIPANTS);
		return form;
	}
	
	
	
	private Table getCertifyingTable(IWContext iwc) throws RemoteException{
		Table table = new Table();
		
		Collection users = getUserBusiness(iwc).getUsersInGroup(Integer.parseInt(iwc.getParameter(PARAMETER_MEETING_FEE_PARTICIPANTS)));
		Iterator iter = users.iterator();
		
		MeetingFee meetingFee = getMeetingFee(iwc);
		Group conGroup = meetingFee.getCongregationGroup();
		String conGroupName = conGroup.getName();
		User owner = meetingFee.getOwner();
		String ownerName = owner.getName();
		String location = meetingFee.getInCommune() ? getResourceBundle().getLocalizedString("meeting.fee.in_commune", "In commune") : getResourceBundle().getLocalizedString("meeting.fee.outside_of_commune", "Outside of commune");
		IWTimestamp meetingDate = new IWTimestamp(meetingFee.getMeetingDate());
		
		int row = 1;
		table.add(getResourceBundle().getLocalizedString("meeting.fee.assignment_from","Assignment from"),1,row);
		table.add(conGroupName,2 , row++);
		table.add(getResourceBundle().getLocalizedString("meeting.fee.speaker","Speaker"),1,row);
		table.add(ownerName, 2, row++); 
		table.add(getResourceBundle().getLocalizedString("meeting.fee.meeting_location","Meeting location"), 1, row);
		table.add(location,2, row++);
		table.add(getResourceBundle().getLocalizedString("meeting.fee.meeting_date","Meeting date"), 1, row);
		table.add(meetingDate.getLocaleDate(iwc.getCurrentLocale()), 2, row++); 
		
		table.add(getResourceBundle().getLocalizedString("meeting.fee.participants","Participants"),1,row);
		
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
			
			table.add(lastName, 1, row);
			table.add(firstName, 2, row);
			table.add(pId, 3, row);
			//table.add(getHours(),4, row);
			//table.add(getMinutes(), 5, row);
			//amount vantar inn hér
		}
		table.add(getRejectButton(),4, row);
		table.add(getEditButton(), 5, row);
		table.add(getNextButton(), 6, row++);
		return table;
	}
	
}
