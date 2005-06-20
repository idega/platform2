/*
 * $Id: StudentPlacer.java,v 1.11 2005/06/20 17:59:27 laddi Exp $
 * Created on Jun 1, 2005
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
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.adulteducation.business.GroupCollectionHandler;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoice;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourse;
import se.idega.idegaweb.commune.school.business.SchoolClassWriter;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.business.IBORuntimeException;
import com.idega.event.IWPageEventListener;
import com.idega.idegaweb.IWException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.remotescripting.RemoteScriptHandler;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;


/**
 * Last modified: $Date: 2005/06/20 17:59:27 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.11 $
 */
public class StudentPlacer extends AdultEducationBlock implements IWPageEventListener {

	public static final String PARAMETER_ACTION = "sp_action";
	
	private static final String PARAMETER_STUDY_PATH_GROUP = "sp_study_path_group";
	private static final String PARAMETER_COURSE = "sp_course";
	private static final String PARAMETER_SCHOOL_CLASS = "sp_school_class";
	private static final String PARAMETER_DATE = "sp_date";
	
	public static final int ACTION_VIEW_GROUP = 1;
	public static final int ACTION_VIEW_CHOICES = 2;
	private static final int ACTION_PLACE_STUDENTS = 4;
	private static final int ACTION_REJECT_STUDENTS = 5;
	private static final int ACTION_REMOVE_STUDENT = 6;
	private static final int ACTION_SET_PLACEMENT_DATE = 8;
	private static final int ACTION_REMOVE_CHOICE = 9;

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.adulteducation.presentation.AdultEducationBlock#present(com.idega.presentation.IWContext)
	 */
	public void present(IWContext iwc) {
		try {
			switch (parseAction(iwc)) {
				case ACTION_VIEW_CHOICES:
					showChoices(iwc);
					break;
					
				case ACTION_VIEW_GROUP:
					showStudents(iwc);
					break;
					
				case ACTION_PLACE_STUDENTS:
					placeStudents(iwc);
					showChoices(iwc);
					break;
					
				case ACTION_REJECT_STUDENTS:
					rejectStudents(iwc);
					showChoices(iwc);
					break;
					
				case ACTION_REMOVE_STUDENT:
					removeStudent(iwc);
					showStudents(iwc);
					break;

				case ACTION_SET_PLACEMENT_DATE:
					showDateSetter();
					break;

				case ACTION_REMOVE_CHOICE:
					removeChoice(iwc);
					showChoices(iwc);
					break;
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	private void showChoices(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.setEventListener(StudentPlacer.class);
		form.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_VIEW_CHOICES));
		form.addParameter(PARAMETER_STUDY_PATH, "");
		form.addParameter(PARAMETER_STUDENT, "");
		
		form.add(getNavigationTable());
		form.add(new Break());

		if (getSession().getSchoolClass() != null) {
			form.add(getGroupInfoTable());
			form.add(new Break());
		}
		
		StringBuffer heading = new StringBuffer();
		heading.append(localize("not_placed_students", "Not placed students"));
		if (getSession().getCourse() != null) {
			SchoolStudyPath path = getSession().getCourse().getStudyPath();
			heading.append(" - ").append(path.getDescription());
		}
		
		form.add(getHeader(heading.toString()));
		form.add(new Break());
		form.add(getChoices(iwc));
		form.add(getSmallErrorText("* "));
		form.add(getSmallText(localize("has_message", "Has message")));
		
		if (getSession().getSchoolClass() != null) {
			IWTimestamp stamp = new IWTimestamp();
			IWTimestamp courseStart = new IWTimestamp(getSession().getCourse().getStartDate());
			
			SubmitButton placeStudents = (SubmitButton) getButton(new SubmitButton(localize("place_students", "Place students")));
			if (courseStart.isEarlierThan(stamp)) {
				placeStudents.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_SET_PLACEMENT_DATE));
			}
			else {
				placeStudents.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PLACE_STUDENTS));
				form.addParameter(PARAMETER_DATE, courseStart.toString());
			}
			SubmitButton rejectStudents = (SubmitButton) getButton(new SubmitButton(localize("reject_students", "Reject students")));
			rejectStudents.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_REJECT_STUDENTS));
			
			form.add(new Break(2));
			form.add(placeStudents);
			form.add(Text.getNonBrakingSpace());
			form.add(rejectStudents);
		}
		
		add(form);
	}
	
	private void showDateSetter() throws RemoteException {
		Form form = new Form();
		form.setEventListener(StudentPlacer.class);
		form.maintainParameter(PARAMETER_CHOICE);
		form.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_PLACE_STUDENTS));
		
		DateInput date = (DateInput) getStyledInterface(new DateInput(PARAMETER_DATE));
		date.setDate(getSession().getCourse().getStartDate());
		
		form.add(getText(localize("placement_date_setter_message", "You are placing a student in a course that has already started. Please choose a start date for this placement.")));
		form.add(new Break(2));
		form.add(getHeader(localize("placement_date", "Placement date") + ":"));
		form.add(Text.getNonBrakingSpace());
		form.add(date);
		
		SubmitButton placeStudents = (SubmitButton) getButton(new SubmitButton(localize("place_students", "Place students")));
		form.add(new Break(2));
		form.add(placeStudents);
		
		add(form);
	}
	
	private Table getGroupInfoTable() throws RemoteException {
		Table table = new Table();
		
		int numberOfStudents = getBusiness().getSchoolBusiness().getNumberOfStudentsInClass(((Integer) getSession().getSchoolClass().getPrimaryKey()).intValue());
		table.add(getSmallHeader(localize("number_of_students_in_group", "Number of placed students in chosen group") + ":"), 1, 1);
		table.add(getSmallText(String.valueOf(numberOfStudents)), 2, 1);
		
		SubmitButton showGroup = (SubmitButton) getButton(new SubmitButton(localize("show_group", "Show group")));
		showGroup.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_VIEW_GROUP));
		table.setCellpaddingLeft(3, 1, 12);
		table.add(showGroup, 3, 1);
		
		return table;
	}
	
	private void showStudents(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.setEventListener(StudentPlacer.class);
		form.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_VIEW_GROUP));
		form.addParameter(PARAMETER_CHOICE, "");
		form.addParameter(PARAMETER_STUDENT, "");
		
		form.add(getNavigationTable());
		form.add(new Break());
		
		form.add(getChoiceInfoTable());
		form.add(new Break());
		
		Link pdfLink = getPDFLink(SchoolClassWriter.class, getBundle().getImage("shared/pdf.gif"));
		Link excelLink = getXLSLink(SchoolClassWriter.class, getBundle().getImage("shared/xls.gif"));

		Table headingTable = new Table(2, 1);
		headingTable.setWidth(Table.HUNDRED_PERCENT);
		headingTable.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_RIGHT);
		
		StringBuffer heading = new StringBuffer();
		heading.append(localize("placed_students", "Placed students"));
		if (getSession().getCourse() != null) {
			SchoolStudyPath path = getSession().getCourse().getStudyPath();
			heading.append(" - ").append(path.getDescription());
		}
		headingTable.add(getHeader(heading.toString()), 1, 1);
		headingTable.add(pdfLink, 2, 1);
		headingTable.add(Text.getNonBrakingSpace(), 2, 1);
		headingTable.add(excelLink, 2, 1);
		
		form.add(headingTable);
		form.add(getStudents(iwc));
		form.add(getSmallErrorText("* "));
		form.add(getSmallText(localize("has_message", "Has message")));
		
		SubmitButton back = (SubmitButton) getButton(new SubmitButton(localize("back", "Back")));
		back.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_VIEW_CHOICES));
		GenericButton sendPlacementMessage = getButton(new GenericButton(localize("send_placement_message", "Send placement message")));
		sendPlacementMessage.setWindowToOpen(StudentWindow.class);
		sendPlacementMessage.addParameter(StudentEditor.PARAMETER_ACTION, StudentEditor.ACTION_SHOW_MESSAGE_SENDING);
		sendPlacementMessage.addParameter(StudentEditor.PARAMETER_PAGE, getParentPageID());
		
		form.add(new Break(2));
		form.add(back);
		form.add(Text.getNonBrakingSpace());
		form.add(sendPlacementMessage);
		
		add(form);
	}
	
	private Table getChoiceInfoTable() throws RemoteException {
		Table table = new Table();
		
		int numberOfChoices = getBusiness().getNumberOfActiveChoices(getSession().getSchoolSeason(), getSession().getCourse());
		table.add(getSmallHeader(localize("number_of_choices_for_code", "Number of active choices for code") + ":"), 1, 1);
		table.add(getSmallText(String.valueOf(numberOfChoices)), 2, 1);
		
		return table;
	}

	private Table getNavigationTable() throws RemoteException {
		Table table = new Table(6, 3);
		table.setCellpadding(3);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		
		SelectorUtility util = new SelectorUtility();
		
		DropdownMenu seasons = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_SCHOOL_SEASON), getBusiness().getSeasons(), "getSeasonName"));
		seasons.addMenuElementFirst("", localize("select_season", "Select season"));
		if (getSession().getSchoolSeason() != null) {
			seasons.setSelectedElement(getSession().getSchoolSeason().getPrimaryKey().toString());
		}
		seasons.setToSubmit();
		
		DropdownMenu studyGroups = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_STUDY_PATH_GROUP), getBusiness().getStudyPathsGroups(), "getLocalizationKey", getResourceBundle()));
		studyGroups.addMenuElementFirst("", localize("select_study_path_group", "Select group"));
		if (getSession().getStudyPathGroup() != null) {
			studyGroups.setSelectedElement(getSession().getStudyPathGroup().getPrimaryKey().toString());
		}
		studyGroups.setToSubmit();
		
		DropdownMenu courses = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_COURSE));
		courses.addMenuElementFirst("", localize("select_course", "Select course code"));
		if (getSession().getSchoolSeason() != null && getSession().getStudyPathGroup() != null) {
			Collection availableCourses = getBusiness().getCourses(getSession().getSchoolSeason().getPrimaryKey(), getSession().getSchool().getPrimaryKey(), getSession().getStudyPathGroup().getPrimaryKey());
			Iterator iter = availableCourses.iterator();
			while (iter.hasNext()) {
				AdultEducationCourse course = (AdultEducationCourse) iter.next();
				boolean hasChoices = getBusiness().hasActiveChoices(getSession().getSchoolSeason(), course);
				courses.addMenuElement(course.getPrimaryKey().toString(), (hasChoices ? "* " : "") + course.getCode());
			}
		}
		if (getSession().getCourse() != null) {
			courses.setSelectedElement(getSession().getCourse().getPrimaryKey().toString());
		}
		
		DropdownMenu groups = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_SCHOOL_CLASS));
		groups.addMenuElementFirst("", localize("select_group", "Select group"));
		if (getSession().getSchoolSeason() != null && getSession().getCourse() != null) {
			Collection availableGroups = getBusiness().getGroups(getSession().getSchool(), getSession().getSchoolSeason(), getSession().getCourse().getCode());
			if (availableGroups.isEmpty()) {
				SchoolClass group = getBusiness().createDefaultGroup(getSession().getSchoolSeason(), getSession().getCourse());
				if (group != null) {
					availableGroups.add(group);
				}
			}
			Iterator iter = availableGroups.iterator();
			while (iter.hasNext()) {
				SchoolClass group = (SchoolClass) iter.next();
				groups.addMenuElement(group.getPrimaryKey().toString(), group.getName());
			}
		}
		if (getSession().getSchoolClass() != null) {
			groups.setSelectedElement(getSession().getSchoolClass().getPrimaryKey().toString());
		}
		groups.setToSubmit();

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

		table.add(getSmallHeader(localize("season", "Season") + ":"), 1, 1);
		table.add(seasons, 2, 1);
		table.add(getSmallHeader(localize("study_path_group", "Study path group") + ":"), 3, 1);
		table.add(studyGroups, 4, 1);
		table.add(getSmallHeader(localize("course", "Course") + ":"), 5, 1);
		table.add(courses, 6, 1);
		table.add(getSmallHeader(localize("group", "Group")), 1, 3);
		table.mergeCells(2, 3, 7, 3);
		table.add(groups, 2, 3);
		
		return table;
	}
	
	private Table getChoices(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(7);
		table.setRowColor(1, getHeaderColor());
		int row = 1;
		int column = 2;
		
		table.add(getLocalizedSmallHeader("name","Name"), column++, row);
		table.add(getLocalizedSmallHeader("personal_id","Personal ID"), column++, row);
		table.add(getLocalizedSmallHeader("priority","Priority"), column++, row);
		table.add(getLocalizedSmallHeader("date","Date"), column++, row);
		
		CheckBox markAll = getCheckBox("check_all", Boolean.TRUE.toString());
		markAll.setToCheckWhenCheckedAndUncheckWhenUnchecked(PARAMETER_CHOICE);
		table.add(markAll, column++, row++);

		if (getSession().getSchoolClass() != null && getSession().getCourse() != null) {
			int number = 0;
			Collection choices = getBusiness().getChoices(getSession().getSchoolSeason(), getSession().getCourse());
			Iterator iter = choices.iterator();
			while (iter.hasNext()) {
				AdultEducationChoice choice = (AdultEducationChoice) iter.next();
				AdultEducationCourse course = choice.getCourse();
				IWTimestamp date = new IWTimestamp(choice.getChoiceDate());
				User user = choice.getUser();
				column = 1;
				number++;
				
				Link link = getSmallLink(user.getName());
				link.addParameter(PARAMETER_CHOICE, choice.getPrimaryKey().toString());
				link.addParameter(StudentEditor.PARAMETER_PAGE, getParentPageID());
				link.addParameter(StudentEditor.PARAMETER_ACTION, StudentEditor.ACTION_SHOW_CHOICE);
				link.setWindowToOpen(StudentWindow.class);
				link.setEventListener(StudentEditor.class);
				
				if (row % 2 == 0) {
					table.setRowColor(row, getZebraColor1());
				}
				else {
					table.setRowColor(row, getZebraColor2());
				}
	
				table.add(getSmallText(String.valueOf(number)), column++, row);
				if (choice.getComment() != null) {
					table.add(getSmallErrorText("* "), column, row);
				}
				table.add(link, column++, row);
				table.add(getSmallText(PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale())), column++, row);
				table.add(getSmallText(String.valueOf(choice.getPriority())), column++, row);
				table.add(getSmallText(date.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
				
				CheckBox box = getCheckBox(PARAMETER_CHOICE, choice.getPrimaryKey().toString());
				box.setDisabled(getSession().getSchoolClass() == null);
				table.add(box, column++, row);

				SubmitButton delete = new SubmitButton(getDeleteIcon(localize("delete_choice", "Delete choice")));
				delete.setDescription(localize("delete_choices", "Delete choices"));
				delete.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_REMOVE_CHOICE));
				delete.setValueOnClick(PARAMETER_STUDY_PATH, course.getStudyPathPK().toString());
				delete.setValueOnClick(PARAMETER_STUDENT, user.getPrimaryKey().toString());
				delete.setSubmitConfirm(localize("confirm_choice_delete", "Are you sure you want to remove the choice?"));
				table.add(delete, column++, row++);
			}
		}
		table.setWidth(6, 12);
		
		return table;
	}
	
	private Table getStudents(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(7);
		table.setRowColor(1, getHeaderColor());
		int row = 1;
		int column = 2;
		
		table.add(getLocalizedSmallHeader("name","Name"), column++, row);
		table.add(getLocalizedSmallHeader("personal_id","Personal ID"), column++, row);
		table.add(getLocalizedSmallHeader("start_date","Start date"), column++, row);
		table.add(getLocalizedSmallHeader("E","E"), 7, row++);

		if (getSession().getSchoolClass() != null) {
			int number = 0;
			Collection students = getBusiness().getSchoolBusiness().findStudentsInClass(((Integer) getSession().getSchoolClass().getPrimaryKey()).intValue());
			Iterator iter = students.iterator();
			while (iter.hasNext()) {
				SchoolClassMember member = (SchoolClassMember) iter.next();
				User user = member.getStudent();
				AdultEducationChoice choice = null;
				try {
					choice = getBusiness().getChoice(user, getSession().getCourse());
				}
				catch (FinderException fe) {
					fe.printStackTrace();
					continue;
				}
				IWTimestamp date = new IWTimestamp(member.getRegisterDate());
				column = 1;
				number++;
				
				Link link = getSmallLink(user.getName());
				link.addParameter(PARAMETER_CHOICE, choice.getPrimaryKey().toString());
				link.addParameter(StudentEditor.PARAMETER_ACTION, StudentEditor.ACTION_SHOW_STUDENT);
				link.addParameter(StudentEditor.PARAMETER_PAGE, getParentPageID());
				link.setWindowToOpen(StudentWindow.class);
				link.setEventListener(StudentEditor.class);
								
				if (row % 2 == 0) {
					table.setRowColor(row, getZebraColor1());
				}
				else {
					table.setRowColor(row, getZebraColor2());
				}
	
				table.add(getSmallText(String.valueOf(number)), column++, row);
				table.add(link, column++, row);
				table.add(getSmallText(PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale())), column++, row);
				table.add(getSmallText(date.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
				
				Link edit = new Link(getEditIcon(localize("edit_student", "Edit student")));
				edit.addParameter(PARAMETER_CHOICE, choice.getPrimaryKey().toString());
				edit.addParameter(PARAMETER_STUDENT, member.getPrimaryKey().toString());
				edit.addParameter(StudentEditor.PARAMETER_ACTION, StudentEditor.ACTION_CHANGE_GROUP);
				edit.addParameter(StudentEditor.PARAMETER_PAGE, getParentPageID());
				edit.setWindowToOpen(StudentWindow.class);
				edit.setEventListener(StudentEditor.class);
				table.add(edit, column++, row);
				
				SubmitButton delete = new SubmitButton(getDeleteIcon(localize("remove_student", "Remove student from group")));
				delete.setValueOnClick(PARAMETER_CHOICE, choice.getPrimaryKey().toString());
				delete.setValueOnClick(PARAMETER_STUDENT, member.getPrimaryKey().toString());
				delete.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_REMOVE_STUDENT));
				delete.setSubmitConfirm(localize("confirm_student_remove", "Are you sure you want to remove the student from the group?"));
				if (!choice.isPlacementMessageSent()) {
					table.add(delete, column, row);
				}
				column++;
				
				table.add(choice.isPlacementMessageSent() ? localize("yes", "Yes") : localize("no", "No"), column++, row++);
			}
		}
		table.setWidth(5, 12);
		table.setWidth(6, 12);
		table.setWidth(7, 12);
		
		return table;
	}
	
	private void placeStudents(IWContext iwc) throws RemoteException {
		getBusiness().placeChoices(iwc.getParameterValues(PARAMETER_CHOICE), getSession().getSchoolClass(), getSession().getCourse(), new IWTimestamp(iwc.getParameter(PARAMETER_DATE)).getDate(), iwc.getCurrentUser());
	}
	
	private void rejectStudents(IWContext iwc) throws RemoteException {
		getBusiness().rejectChoices(iwc.getParameterValues(PARAMETER_CHOICE), iwc.getCurrentUser());
	}
	
	private void removeStudent(IWContext iwc) throws RemoteException {
		getBusiness().removeStudent(iwc.getParameter(PARAMETER_STUDENT), iwc.getParameter(PARAMETER_CHOICE), iwc.getCurrentUser());
	}
	
	private void removeChoice(IWContext iwc) throws RemoteException {
		getBusiness().removeChoices(iwc.getParameter(PARAMETER_STUDY_PATH), getSession().getSchoolSeason().getPrimaryKey(), iwc.getParameter(PARAMETER_STUDENT), iwc.getCurrentUser());
	}
	
	private int parseAction(IWContext iwc) {
		int action = ACTION_VIEW_CHOICES;
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		
		return action;
	}
	
	public boolean actionPerformed(IWContext iwc) throws IWException {
		boolean actionPerformed = false;
		
		if (iwc.isParameterSet(PARAMETER_COURSE)) {
			try {
				getSession(iwc).setCourse(iwc.getParameter(PARAMETER_COURSE));
				actionPerformed = true;
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		
		if (iwc.isParameterSet(PARAMETER_SCHOOL_CLASS)) {
			try {
				getSession(iwc).setSchoolClass(iwc.getParameter(PARAMETER_SCHOOL_CLASS));
				actionPerformed = true;
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}

		if (iwc.isParameterSet(PARAMETER_SCHOOL_SEASON)) {
			try {
				getSession(iwc).setSeason(iwc.getParameter(PARAMETER_SCHOOL_SEASON));
				actionPerformed = true;
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		
		if (iwc.isParameterSet(PARAMETER_STUDY_PATH_GROUP)) {
			try {
				getSession(iwc).setStudyPathGroup(iwc.getParameter(PARAMETER_STUDY_PATH_GROUP));
				actionPerformed = true;
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}

		return actionPerformed;
	}
}