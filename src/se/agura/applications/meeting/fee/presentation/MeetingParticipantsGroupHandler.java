/*
 * $Id: MeetingParticipantsGroupHandler.java,v 1.1 2004/12/05 20:59:37 anna Exp $
 * Created on 3.12.2004
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
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SelectionDoubleBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import com.idega.util.PersonalIDFormatter;


/**
 * Last modified: 3.12.2004 15:12:55 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.1 $
 */
public class MeetingParticipantsGroupHandler extends MeetingFeeBlock {
	
	protected static final String ACTION_SAVE_AS = "save_as";

	/* (non-Javadoc)
	 * @see se.agura.applications.meeting.fee.presentation.MeetingFeeBlock#present(com.idega.presentation.IWContext)
	 */
	public void present(IWContext iwc) {
		String action = iwc.getParameter(PARAMETER_ACTION);
		
		if(action.equals(ACTION_NEXT)) {
			confirmNewGroupForm(iwc);
		}
		
		if(action.equals(ACTION_SAVE /*e›a merki› hvar er ﬂa› á Makkanum? ACTION_SAVE_AS*/)){
			confirmNewGroupForm(iwc);
		}
		
		if(action.equals(ACTION_EDIT)){
			formEditGroup(iwc);
		}
		
		if(action.equals(ACTION_NEW)) {
		  formNewGroup(iwc);
		}
		if(action.equals(ACTION_SAVE_FINALLY)) {
			//save(iwc);
		}
		if(action.equals(ACTION_BACK)) {
			//veit ekki, kannski ﬂarf iPage dæmi hér??????
		}
	}
	
	private Form formNewGroup(IWContext iwc) {
		Form form = new Form();
		form.addParameter(PARAMETER_ACTION, ACTION_NEXT);
		try {
		form.add(makeNewGroupTable(iwc));
		}
		catch(RemoteException re) {
			log(re);
		}
		form.maintainParameter(PARAMETER_MEETING_FEE_PARTICIPANTS);
		form.maintainParameter(PARAMETER_MEETING_FEE_CONGREGATION);
		return form;
	}
	
	private Form formEditGroup(IWContext iwc) {
		Form form = new Form();
		form.addParameter(PARAMETER_ACTION, ACTION_SAVE);
		form.addParameter(PARAMETER_ACTION, ACTION_SAVE_AS);
		try {
			form.add(editGroupTable(iwc));
		}
		catch(RemoteException re) {
			log(re);
		}
		form.maintainParameter(PARAMETER_MEETING_FEE_PARTICIPANTS);
		form.maintainParameter(PARAMETER_MEETING_FEE_CONGREGATION);
		return form;
	}
	
	//ﬂetta er kannski óﬂarfi?
	private Form confirmNewGroupForm(IWContext iwc) {
		Form form = new Form();
		form.addParameter(PARAMETER_ACTION, ACTION_SAVE_FINALLY);
		form.addParameter(PARAMETER_ACTION, ACTION_BACK);
		try {
			form.add(confirmNewGroupTable(iwc));
		}
		catch(RemoteException re) {
			log(re);
		}
		form.maintainParameter(PARAMETER_MEETING_FEE_CONGREGATION);
		return form;
	}
	
	private Table makeNewGroupTable(IWContext iwc) throws RemoteException {
		Table table = new Table();
		TextInput participantsGroup = new TextInput();
		DropdownMenu congregMenu = new DropdownMenu();
		int row = 1;
		table.add(getResourceBundle().getLocalizedString("meeting.fee.participants_group_name","Participants group name"),1,row++);
		table.add(participantsGroup, 1, row++);
		table.add(getResourceBundle().getLocalizedString("meeting.fee.show_participants_from","Show participants from"),1, row++);
		table.add(congregMenu, 1, row++);
		
		//Laddi! ﬁarf ekki hér a› vera búi› a› setja PARTICIPANTS parameterinn ﬂa› sama og sett var inn í textInput fyrir ofan??????
		SelectionDoubleBox doubleBox = new SelectionDoubleBox(PARAMETER_MEETING_FEE_PARTICIPANTS);
		int groupID = Integer.parseInt(iwc.getParameter(PARAMETER_MEETING_FEE_CONGREGATION));
		Collection users = getUserBusiness(iwc).getUsersInGroup(groupID);
		Iterator iter = users.iterator();
		String pk = null;
		
		while (iter.hasNext()) {
			User user = (User) iter.next();
			String lastName = user.getLastName();
			String firstName = user.getFirstName();
			String pId = PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale());
			pk = (String) user.getPrimaryKey();
			doubleBox.addToLeftBox(pk, lastName + ", " + firstName + ", " + pId);
		}
		table.add(doubleBox, 1, row++);
		table.add(getNextButton(),Integer.parseInt(Table.HORIZONTAL_ALIGN_RIGHT),row++);
		
		return table;
	}
	
	private Table editGroupTable(IWContext iwc) throws RemoteException {
		Table table = new Table();
		
		String participantsGroup = iwc.getParameter(PARAMETER_MEETING_FEE_PARTICIPANTS);
		String congregation = iwc.getParameter(PARAMETER_MEETING_FEE_CONGREGATION);
		DropdownMenu congregMenu = new DropdownMenu();
		congregMenu.setSelectedElement(congregation);
		int row = 1;
		table.add(getResourceBundle().getLocalizedString("meeting.fee.participants_group_name","Participants group name"),1,row++);
		table.add(participantsGroup, 1, row++);
		table.add(getResourceBundle().getLocalizedString("meeting.fee.show_participants_from","Show participants from"),1, row++);
		table.add(congregMenu, 1, row++);
		
		SelectionDoubleBox doubleBox = new SelectionDoubleBox(PARAMETER_MEETING_FEE_PARTICIPANTS);
		int groupID = Integer.parseInt(iwc.getParameter(PARAMETER_MEETING_FEE_CONGREGATION));
		Collection users = getUserBusiness(iwc).getUsersInGroup(groupID);
		Iterator iter = users.iterator();
		String pk = null;
		
		while (iter.hasNext()) {
			User user = (User) iter.next();
			String lastName = user.getLastName();
			String firstName = user.getFirstName();
			String pId = PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale());
			pk = (String) user.getPrimaryKey();
			doubleBox.addToLeftBox(pk, lastName + ", " + firstName + ", " + pId);
			doubleBox.addToRightBox(pk, lastName + ", " + firstName + ", " + pId);
		}
		table.add(doubleBox, 1, row++);
		table.add(getSaveButton(), 2, row++);
		table.add(getSaveAsButton(), 3, row++);
		return table;
	}
	
	private Table confirmNewGroupTable(IWContext iwc) throws RemoteException {
		Table table = new Table();
		String participantsGroup = iwc.getParameter(PARAMETER_MEETING_FEE_PARTICIPANTS);
		int groupID = Integer.parseInt(participantsGroup);
		Collection users = getUserBusiness(iwc).getUsersInGroup(groupID);
		Iterator iter = users.iterator();
		int row = 1;
		
		table.add(getResourceBundle().getLocalizedString("meeting.fee.participants_group_name","Participants group name"),1,row++);
		table.add(participantsGroup, 1, row++);
		
		table.add(getResourceBundle().getLocalizedString("meeting.fee.participants","Participants"),1,row++);
		
		table.add(getResourceBundle().getLocalizedString("meeting.fee.participants.last_name","Last name"),1, row);
		table.add(getResourceBundle().getLocalizedString("meeting.fee.participants.first_name","First name"),2,row);
		table.add(getResourceBundle().getLocalizedString("meeting.fee.participants.personal_number","Personal number"),3, row);
		table.add(getResourceBundle().getLocalizedString("meeting.fee.congregation","Congregation"), 4, row++);
		while (iter.hasNext()) {
			User user = (User) iter.next();
			table.add(user.getLastName(),1, row);
			table.add(user.getFirstName(),2, row);
			String pId = PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale());
			table.add(pId, 3, row);
			table.add(participantsGroup,4, row++);
		}
		table.add(getBackButton(), 3, row);
		table.add(getSaveButton(), 4, row++);
		return table;
	}
	
	private SubmitButton getSaveAsButton() {
		SubmitButton saveAsButton = (SubmitButton) getButton(new SubmitButton(getResourceBundle().getLocalizedString("meeting.fee.save_as", "Save as")));
		return saveAsButton;
	}
}
