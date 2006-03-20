/*
 * $Id: StudentEditor.java,v 1.9.2.2 2006/03/20 02:10:50 sigtryggur Exp $
 * Created on Jun 2, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.RemoveException;
import se.idega.idegaweb.commune.adulteducation.business.GroupCollectionHandler;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoice;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourse;
import com.idega.block.process.data.CaseStatus;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.builder.business.BuilderLogic;
import com.idega.business.IBORuntimeException;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.event.IWPageEventListener;
import com.idega.idegaweb.IWException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.remotescripting.RemoteScriptHandler;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.user.business.NoEmailFoundException;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.URLUtil;
import com.idega.util.text.TextSoap;


/**
 * Last modified: $Date: 2006/03/20 02:10:50 $ by $Author: sigtryggur $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.9.2.2 $
 */
public class StudentEditor extends AdultEducationBlock implements IWPageEventListener {
	
	public static final String PARAMETER_ACTION = "se_action";
	public static final String PARAMETER_PAGE = "se_page";
	private static final String PARAMETER_COURSE = "se_course";
	private static final String PARAMETER_SCHOOL_CLASS = "se_school_class";
	private static final String PARAMETER_MESSAGE = "se_message";
	private static final String PARAMETER_TERMINATED_DATE = "se_terminated_date";
	
	public static final int ACTION_SHOW_CHOICE = 1;
	public static final int ACTION_SHOW_STUDENT = 2;
	private static final int ACTION_REJECT_STUDENT = 3;
	private static final int ACTION_CHANGE_COURSE = 4;
	private static final int ACTION_STORE_COURSE = 5;
	public static final int ACTION_CHANGE_GROUP = 6;
	private static final int ACTION_STORE_GROUP = 7;
	public static final int ACTION_SHOW_MESSAGE_SENDING = 8;
	private static final int ACTION_SEND_MESSAGE = 9;
	public static final int ACTION_SHOW_TERMINATE_PLACEMENT = 10;
	private static final int ACTION_TERMINATE_PLACEMENT = 11;
	
	private int iPageID;
	
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.adulteducation.presentation.AdultEducationBlock#present(com.idega.presentation.IWContext)
	 */
	public void present(IWContext iwc) {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PAGE);
		form.setStyleAttribute("height:100%");

		Table table = new Table(3, 5);
		table.setRowColor(1, "#000000");
		table.setRowColor(3, "#000000");
		table.setRowColor(5, "#000000");
		table.setColumnColor(1, "#000000");
		table.setColumnColor(3, "#000000");
		table.setColor(2, 2, "#CCCCCC");
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setWidth(2, Table.HUNDRED_PERCENT);
		table.setHeight(Table.HUNDRED_PERCENT);
		table.setHeight(4, Table.HUNDRED_PERCENT);
		table.setCellpadding(0);
		table.setCellspacing(0);
		form.add(table);

		Table headerTable = new Table(1, 1);
		headerTable.setCellpadding(6);
		table.add(headerTable, 2, 2);

		Table contentTable = new Table(1, 1);
		contentTable.setCellpadding(10);
		contentTable.setWidth(Table.HUNDRED_PERCENT);
		contentTable.setHeight(Table.HUNDRED_PERCENT);
		table.add(contentTable, 2, 4);

		try {
			switch (parseAction(iwc)) {
				case ACTION_SHOW_CHOICE:
					form.addParameter(PARAMETER_ACTION, "");
					headerTable.add(getHeader(localize("show_choice", "Choice overview")), 1, 1);
					contentTable.add(showOverview(iwc, true), 1, 1);
					add(form);
					break;

				case ACTION_SHOW_STUDENT:
					headerTable.add(getHeader(localize("show_student", "Student overview")), 1, 1);
					contentTable.add(showOverview(iwc, false), 1, 1);
					add(form);
					break;

				case ACTION_REJECT_STUDENT:
					rejectStudent(iwc);
					break;

				case ACTION_CHANGE_COURSE:
					form.addParameter(PARAMETER_ACTION, "");
					headerTable.add(getHeader(localize("show_change_course", "Change course")), 1, 1);
					contentTable.add(showChangeCourse(), 1, 1);
					showChangeCourse();
					add(form);
					break;

				case ACTION_STORE_COURSE:
					changeCourse(iwc);
					break;

				case ACTION_CHANGE_GROUP:
					form.addParameter(PARAMETER_ACTION, ACTION_CHANGE_GROUP);
					headerTable.add(getHeader(localize("show_change_group", "Change group")), 1, 1);
					contentTable.add(showChangeGroup(), 1, 1);
					add(form);
					break;

				case ACTION_STORE_GROUP:
					changeGroup(iwc);
					break;

				case ACTION_SHOW_MESSAGE_SENDING:
					form.addParameter(PARAMETER_ACTION, ACTION_SEND_MESSAGE);
					headerTable.add(getHeader(localize("show_message_sending", "Send placement message")), 1, 1);
					contentTable.add(showMessageSending(), 1, 1);
					add(form);
					break;

				case ACTION_SEND_MESSAGE:
					sendMessage(iwc);
					break;

				case ACTION_SHOW_TERMINATE_PLACEMENT:
					form.addParameter(PARAMETER_ACTION, ACTION_TERMINATE_PLACEMENT);
					headerTable.add(getHeader(localize("show_terminate_placement", "Terminate placement")), 1, 1);
					contentTable.add(showTerminatePlacement(), 1, 1);
					add(form);
					break;

				case ACTION_TERMINATE_PLACEMENT:
					terminatePlacement(iwc);
					break;
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	private Table showOverview(IWContext iwc, boolean showButtons) throws RemoteException {
		Table table = new Table();
		table.setCellpadding(5);
		table.setColumns(2);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(Table.HUNDRED_PERCENT);
		int row = 1;
		
		AdultEducationChoice choice = getSession().getChoice();
		AdultEducationCourse course = choice.getCourse();
		SchoolStudyPath path = course.getStudyPath();
		SchoolSeason season = course.getSchoolSeason();
		User user = choice.getUser();
		Address address = getBusiness().getUserBusiness().getUsersMainAddress(user);
		PostalCode code = address != null ? address.getPostalCode() : null;
		Phone homePhone = null;
		try {
			homePhone = getBusiness().getUserBusiness().getUsersHomePhone(user);
		}
		catch (NoPhoneFoundException npfe) {
			//Nothing found;
		}
		Phone mobilePhone = null;
		try {
			mobilePhone = getBusiness().getUserBusiness().getUsersMobilePhone(user);
		}
		catch (NoPhoneFoundException npfe) {
			//Nothing found;
		}
		Email mail = null;
		try {
			mail = getBusiness().getUserBusiness().getUsersMainEmail(user);
		}
		catch (NoEmailFoundException nefe) {
			//Nothing found;
		}
		Collection choices = getBusiness().getChoices(user, season, path);
		
		table.add(getSmallHeader(localize("name", "Name")), 1, row);
		table.add(getSmallText(user.getName()), 2, row++);
		
		table.add(getSmallHeader(localize("personal_id", "Personal ID")), 1, row);
		table.add(getSmallText(PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale())), 2, row++);
		
		table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
		table.add(getSmallHeader(localize("address_phone", "Address/Phone")), 1, row);
		if (address != null && code != null) {
			table.add(getSmallText(address.getStreetAddress() + ", " + code.getPostalAddress()), 2, row);
			table.add(new Break(), 2, row);
		}
		if (homePhone != null) {
			table.add(getSmallText(localize("phone", "Phone") + ": " + homePhone.getNumber()), 2, row);
			table.add(new Break(), 2, row);
		}
		if (mobilePhone != null) {
			table.add(getSmallText(localize("mobile", "Mobile") + ": " + mobilePhone.getNumber()), 2, row);
		}
		row++;
		
		table.add(getSmallHeader(localize("email", "E-mail")), 1, row);
		if (mail != null && mail.getEmailAddress() != null) {
			Link mailLink = getSmallLink(mail.getEmailAddress());
			mailLink.setURL("mailto:" + mail.getEmailAddress());
			table.add(mailLink, 2, row);
		}
		row++;
		
		table.add(getSmallHeader(localize("study_path", "Study path")), 1, row);
		table.add(getSmallText(path.getDescription()), 2, row++);
		
		table.add(getSmallHeader(localize("priority", "Priority")), 1, row);
		table.add(getSmallText(String.valueOf(choice.getPriority())), 2, row++);
		
		table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
		table.add(getSmallHeader(localize("choices", "Choices")), 1, row);
		Iterator iter = choices.iterator();
		int choiceNumber = 1;
		while (iter.hasNext()) {
			AdultEducationChoice element = (AdultEducationChoice) iter.next();
			CaseStatus status = element.getCaseStatus();
			
			StringBuffer string = new StringBuffer();
			string.append(choiceNumber).append(". ").append(element.getCourse().getSchool().getSchoolName()).append(" (").append(localize("case.status" + status.getStatus(), status.getStatus())).append(")");
			
			if (element.equals(choice)) {
				table.add(getSmallHeader(string.toString()), 2, row);
			}
			else {
				table.add(getSmallText(string.toString()), 2, row);
			}
			
			if (iter.hasNext()) {
				table.add(new Break(), 2, row);
			}
			choiceNumber++;
		}
		row++;

		table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
		table.add(getSmallHeader(localize("message", "Message")), 1, row);
		if (choice.getComment() != null) {
			table.add(getSmallText(choice.getComment()), 2, row);
		}
		row++;
		
		table.add(getSmallHeader(localize("choice_date", "Choice date")), 1, row);
		table.add(getSmallText(new IWTimestamp(choice.getChoiceDate()).getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), 2, row++);
		
		CloseButton close = (CloseButton) getButton(new CloseButton(localize("close", "Close")));
		SubmitButton reject = (SubmitButton) getButton(new SubmitButton(localize("reject_student", "Reject student")));
		reject.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_REJECT_STUDENT));
		reject.setSubmitConfirm(localize("confirm_student_reject", "Are you sure you want to reject the student?"));
		SubmitButton changeCourse = (SubmitButton) getButton(new SubmitButton(localize("change_course", "Change course")));
		changeCourse.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_CHANGE_COURSE));
		
		table.setHeight(row, Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);
		table.mergeCells(1, row, 2, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add(close, 1, row);
		if (showButtons) {
			table.add(Text.getNonBrakingSpace(), 1, row);
			table.add(reject, 1, row);
			table.add(Text.getNonBrakingSpace(), 1, row);
			table.add(changeCourse, 1, row);
		}
		
		return table;
	}
	
	private Table showChangeCourse() throws RemoteException {
		Table table = new Table();
		table.setCellpadding(5);
		table.setColumns(2);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(Table.HUNDRED_PERCENT);
		int row = 1;
		
		AdultEducationChoice choice = getSession().getChoice();
		AdultEducationCourse course = choice.getCourse();
		
		SubmitButton back = (SubmitButton) getButton(new SubmitButton(localize("back", "Back")));
		back.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_SHOW_CHOICE));
		SubmitButton changeCourse = (SubmitButton) getButton(new SubmitButton(localize("change_course", "Change course")));
		changeCourse.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_STORE_COURSE));
		
		DropdownMenu courses = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_COURSE));
		Collection availableCourses = getBusiness().getAvailableCourses(getSession().getSchoolSeason().getPrimaryKey(), getSession().getSchool().getPrimaryKey(), course.getStudyPath());
		Iterator iter = availableCourses.iterator();
		while (iter.hasNext()) {
			AdultEducationCourse element = (AdultEducationCourse) iter.next();
			if (!element.equals(course)) {
				courses.addMenuElement(element.getPrimaryKey().toString(), element.getCode());
			}
		}

		table.add(getSmallHeader(localize("current_course", "Current course")), 1, row);
		table.add(getSmallText(course.getCode()), 2, row++);
		
		table.add(getSmallHeader(localize("new_course", "New course")), 1, row);
		table.add(courses, 2, row++);
		
		table.setHeight(row, Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);
		table.mergeCells(1, row, 2, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add(back, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(changeCourse, 1, row);
		
		return table;
	}
	
	private Table showChangeGroup() throws RemoteException {
		Table table = new Table();
		table.setCellpadding(5);
		table.setColumns(2);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(Table.HUNDRED_PERCENT);
		int row = 1;
		
		AdultEducationChoice choice = getSession().getChoice();
		AdultEducationCourse course = choice.getCourse();
		SchoolClassMember member = getSession().getSchoolClassMember();
		SchoolClass group = member.getSchoolClass();
		
		CloseButton close = (CloseButton) getButton(new CloseButton(localize("close", "Close")));
		SubmitButton changeGroup = (SubmitButton) getButton(new SubmitButton(localize("change_group", "Change group")));
		changeGroup.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_STORE_GROUP));
		
		DropdownMenu courses = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_COURSE));
		Collection availableCourses = getBusiness().getAvailableCourses(getSession().getSchoolSeason().getPrimaryKey(), getSession().getSchool().getPrimaryKey(), course.getStudyPath());
		Iterator iter = availableCourses.iterator();
		while (iter.hasNext()) {
			AdultEducationCourse element = (AdultEducationCourse) iter.next();
			courses.addMenuElement(element.getPrimaryKey().toString(), element.getCode());
		}
		courses.setSelectedElement(course.getPrimaryKey().toString());

		DropdownMenu groups = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_SCHOOL_CLASS));
		if (getSession().getSchoolSeason() != null && getSession().getCourse() != null) {
			Collection availableGroups = getBusiness().getGroups(getSession().getSchool(), getSession().getSchoolSeason(), getSession().getCourse().getCode());
			iter = availableGroups.iterator();
			while (iter.hasNext()) {
				SchoolClass element = (SchoolClass) iter.next();
				groups.addMenuElement(element.getPrimaryKey().toString(), element.getName());
			}
		}
		if (group != null) {
			groups.setSelectedElement(group.getPrimaryKey().toString());
		}

		RemoteScriptHandler rsh = new RemoteScriptHandler(courses, groups);
		try {
			rsh.setRemoteScriptCollectionClass(GroupCollectionHandler.class);
		}
		catch (InstantiationException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		add(rsh);

		table.add(getSmallHeader(localize("course", "Course")), 1, row);
		table.add(courses, 2, row++);
		
		table.add(getSmallHeader(localize("group", "Group")), 1, row);
		table.add(groups, 2, row++);
		
		table.setHeight(row, Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);
		table.mergeCells(1, row, 2, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add(close, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(changeGroup, 1, row);
		
		return table;
	}
	
	private Table showMessageSending() {
		Table table = new Table();
		table.setCellpadding(5);
		table.setColumns(1);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(Table.HUNDRED_PERCENT);
		int row = 1;
		
		String body = localize("choice_placed_body", "You have been placed in course {0} with course code {1} at {2}. The course start date is {3}.");

		CloseButton close = (CloseButton) getButton(new CloseButton(localize("close", "Close")));
		SubmitButton sendMessage = (SubmitButton) getButton(new SubmitButton(localize("send_message", "Send message")));
		sendMessage.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_SEND_MESSAGE));
		
		table.add(getHeader(TextSoap.formatText(localize("message_sending_info", "Enter a message that you want to send to the students.  The {x} have different values attached to them that will be filled automatically.\r\n\r\nThey are:\r\n{0} = Course\r\n{1} = Course code\r\n{2} = School\r\n{3} = Start date."))), 1, row++);
		
		TextArea message = (TextArea) getStyledInterface(new TextArea(PARAMETER_MESSAGE));
		message.setWidth(Table.HUNDRED_PERCENT);
		message.setRows(6);
		message.setContent(body);
		
		table.add(getSmallHeader(localize("message", "Message")), 1, row);
		table.add(new Break(), 1, row);
		table.add(message, 1, row++);
		
		table.setHeight(row, Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);
		table.mergeCells(1, row, 2, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add(close, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(sendMessage, 1, row);
		
		return table;
	}

	private Table showTerminatePlacement() throws RemoteException {
		Table table = new Table();
		table.setCellpadding(5);
		table.setColumns(1);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(Table.HUNDRED_PERCENT);
		int row = 1;
		
		SchoolClassMember member = getSession().getSchoolClassMember();
		IWTimestamp startDate = new IWTimestamp(member.getRegisterDate());
		startDate.addDays(1);
		IWTimestamp stamp = new IWTimestamp();
		
		CloseButton back = (CloseButton) getButton(new CloseButton(localize("close", "Close")));
		SubmitButton terminate = (SubmitButton) getButton(new SubmitButton(localize("terminate", "Terminate")));
		
		DateInput input = (DateInput) getStyledInterface(new DateInput(PARAMETER_TERMINATED_DATE));
		input.setEarliestPossibleDate(startDate.getDate(), localize("end_date_earlier_than_start_date", "You must select a date later than the start date."));
		input.setDate(stamp.getDate());
		input.setYearRange(stamp.getYear() - 1, stamp.getYear() + 5);
		
		table.add(getSmallHeader(localize("terminated_date", "Terminated date")), 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(input, 1, row++);
		
		table.setHeight(row, Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add(back, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(terminate, 1, row);
		
		return table;
	}
	
	private void rejectStudent(IWContext iwc) throws RemoteException {
		Object[] choices = { getSession().getChoice().getPrimaryKey().toString() };
		getBusiness().rejectChoices(choices, iwc.getCurrentUser());
		close(iwc, StudentPlacer.ACTION_VIEW_CHOICES);
	}
	
	private void changeCourse(IWContext iwc) throws RemoteException {
		getBusiness().changeCourse(getSession().getChoice(), iwc.getParameter(PARAMETER_COURSE));
		close(iwc, StudentPlacer.ACTION_VIEW_CHOICES);
	}
	
	private void changeGroup(IWContext iwc) throws RemoteException {
		if (iwc.isParameterSet(PARAMETER_SCHOOL_CLASS)) {
			getBusiness().changeCourse(getSession().getChoice(), iwc.getParameter(PARAMETER_COURSE));
			getBusiness().changeGroup(getSession().getSchoolClassMember(), iwc.getParameter(PARAMETER_SCHOOL_CLASS));
		}
		close(iwc, StudentPlacer.ACTION_VIEW_GROUP);
	}
	
	private void sendMessage(IWContext iwc) throws RemoteException {
		getBusiness().sendPlacementMessage(getSession().getSchoolClass(), getSession().getCourse(), iwc.getParameter(PARAMETER_MESSAGE));
		close(iwc, StudentPlacer.ACTION_VIEW_GROUP);
	}
	
	private void terminatePlacement(IWContext iwc) throws RemoteException {
		IWTimestamp stamp = new IWTimestamp(iwc.getParameter(PARAMETER_TERMINATED_DATE));
		try {
			getBusiness().terminatePlacement(getSession().getSchoolClassMember(), stamp.getTimestamp());
		}
		catch (RemoveException re) {
			re.printStackTrace();
		}
		close(iwc, StudentAdministrator.ACTION_VIEW);
	}
	
	private void close(IWContext iwc, int action) {
		URLUtil URL = new URLUtil(BuilderLogic.getInstance().getIBPageURL(iwc, iPageID));
		URL.addParameter(StudentPlacer.PARAMETER_ACTION, action);
		getParentPage().setParentToRedirect(URL.toString());
		getParentPage().close();
	}

	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_PAGE)) {
			iPageID = Integer.parseInt(iwc.getParameter(PARAMETER_PAGE));
		}
		
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		return ACTION_SHOW_CHOICE;
	}

	public boolean actionPerformed(IWContext iwc) throws IWException {
		boolean actionPerformed = false;
		
		if (iwc.isParameterSet(PARAMETER_CHOICE)) {
			try {
				getSession(iwc).setChoice(iwc.getParameter(PARAMETER_CHOICE));
				actionPerformed = true;
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		
		if (iwc.isParameterSet(PARAMETER_STUDENT)) {
			try {
				getSession(iwc).setSchoolClassMember(iwc.getParameter(PARAMETER_STUDENT));
				actionPerformed = true;
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		
		return actionPerformed;
	}
}