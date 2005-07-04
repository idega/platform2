/*
 * $Id: StudentAdministrator.java,v 1.9 2005/07/04 10:16:09 laddi Exp $
 * Created on Jun 16, 2005
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
import javax.ejb.RemoveException;
import se.idega.idegaweb.commune.adulteducation.business.GroupCollectionHandler;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoice;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourse;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberGrade;
import com.idega.business.IBORuntimeException;
import com.idega.event.IWPageEventListener;
import com.idega.idegaweb.IWException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.remotescripting.RemoteScriptHandler;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;


/**
 * Last modified: $Date: 2005/07/04 10:16:09 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.9 $
 */
public class StudentAdministrator extends AdultEducationBlock implements IWPageEventListener {

	public static final String PARAMETER_ACTION = "sa_action";
	
	private static final String PARAMETER_STUDY_PATH_GROUP = "sa_study_path_group";
	private static final String PARAMETER_COURSE = "sa_course";
	private static final String PARAMETER_SCHOOL_CLASS = "sa_school_class";
	private static final String PARAMETER_GRADE = "sa_grade";
	
	public static final int ACTION_VIEW = 1;
	private static final int ACTION_UPDATE_GRADES = 2;
	private static final int ACTION_STORE_GRADES = 3;
	private static final int ACTION_CREATE_CATALOG = 4;
	private static final int ACTION_REMOVE_PLACEMENT = 5;
	
	private boolean iShowGradeSetter = true;
	private boolean iShowCatalogCreator = true;

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.adulteducation.presentation.AdultEducationBlock#present(com.idega.presentation.IWContext)
	 */
	public void present(IWContext iwc) {
		try {
			switch (parseAction(iwc)) {
				case ACTION_VIEW:
					showStudents(iwc);
					break;
					
				case ACTION_UPDATE_GRADES:
					showUpdateGrades(iwc);
					break;

				case ACTION_STORE_GRADES:
					updateGrades(iwc);
					showStudents(iwc);
					break;

				case ACTION_REMOVE_PLACEMENT:
					removePlacement(iwc);
					showStudents(iwc);
					break;
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	private void showStudents(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.setEventListener(StudentAdministrator.class);
		form.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_VIEW));
		form.addParameter(PARAMETER_STUDENT, "");
		form.addParameter(PARAMETER_CHOICE, "");
		
		form.add(getNavigationTable());
		form.add(new Break());
		
		form.add(getStudents(iwc, false));
		form.add(getSmallErrorText("* "));
		form.add(getSmallText(localize("has_message", "Has message")));
		
		SubmitButton setGrades = (SubmitButton) getButton(new SubmitButton(localize("set_grades", "Set grades")));
		setGrades.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_UPDATE_GRADES));
		SubmitButton createCatalog = (SubmitButton) getButton(new SubmitButton(localize("create_catalog", "Create catalog")));
		createCatalog.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_CREATE_CATALOG));
		
		form.add(new Break(2));
		if (iShowGradeSetter) {
			form.add(setGrades);
		}
		form.add(Text.getNonBrakingSpace());
		if (iShowCatalogCreator) {
			form.add(createCatalog);
		}
		
		add(form);
	}
	
	private void showUpdateGrades(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.setEventListener(StudentAdministrator.class);
		form.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_VIEW));
		
		form.add(getNavigationTable());
		form.add(new Break());
		
		form.add(getStudents(iwc, true));
		form.add(getSmallErrorText("* "));
		form.add(getSmallText(localize("has_message", "Has message")));
		
		SubmitButton back = (SubmitButton) getButton(new SubmitButton(localize("set_grades.back", "Back")));
		SubmitButton storeGrades = (SubmitButton) getButton(new SubmitButton(localize("store_grades", "Store grades")));
		storeGrades.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_STORE_GRADES));
		
		form.add(new Break(2));
		form.add(back);
		form.add(Text.getNonBrakingSpace());
		form.add(storeGrades);
		
		add(form);
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
			Collection availableCourses = getBusiness().getCoursesWithStudents(getSession().getSchoolSeason().getPrimaryKey(), getSession().getSchool().getPrimaryKey(), getSession().getStudyPathGroup().getPrimaryKey());
			Iterator iter = availableCourses.iterator();
			while (iter.hasNext()) {
				AdultEducationCourse course = (AdultEducationCourse) iter.next();
				courses.addMenuElement(course.getPrimaryKey().toString(), course.getCode());
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
	
	private Table getStudents(IWContext iwc, boolean update) throws RemoteException {
		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(8);
		table.setRowColor(1, getHeaderColor());
		int row = 1;
		int column = 2;
		
		table.add(getLocalizedSmallHeader("name","Name"), column++, row);
		table.add(getLocalizedSmallHeader("personal_id","Personal ID"), column++, row);
		table.add(getLocalizedSmallHeader("start_date","Start date"), column++, row);
		table.add(getLocalizedSmallHeader("end_date","End date"), column++, row);
		table.add(getLocalizedSmallHeader("grade","Grade"), column++, row++);
		
		if (getSession().getSchoolClass() != null && getSession().getCourse() != null) {
			int number = 0;
			IWTimestamp stamp = new IWTimestamp();
			SelectorUtility util = new SelectorUtility();
			Collection students = getBusiness().getStudents(getSession().getSchoolClass());
			Collection grades = getBusiness().getGrades(getSession().getSchoolClass().getSchoolType());
			Iterator iter = students.iterator();
			while (iter.hasNext()) {
				SchoolClassMember member = (SchoolClassMember) iter.next();
				User user = member.getStudent();
				Name name = new Name(user.getFirstName(), user.getMiddleName(), user.getLastName());
				AdultEducationChoice choice = null;
				try {
					choice = getBusiness().getChoice(user, getSession().getCourse());
				}
				catch (FinderException fe) {
					fe.printStackTrace();
					continue;
				}
				IWTimestamp startDate = new IWTimestamp(member.getRegisterDate());
				IWTimestamp endDate = member.getRemovedDate() != null ? new IWTimestamp(member.getRemovedDate()) : null;
				SchoolClassMemberGrade grade = getBusiness().getStudentGrade(member);
				column = 1;
				number++;
				
				Link link = getSmallLink(name.getName(iwc.getCurrentLocale(), true));
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
				if (choice.getComment() != null) {
					table.add(getSmallErrorText("* "), column, row);
				}
				table.add(link, column++, row);
				table.add(getSmallText(PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale())), column++, row);
				table.add(getSmallText(startDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
				if (endDate != null) {
					table.add(getSmallText(endDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column, row);
				}
				column++;
				if (update && !grades.isEmpty()) {
					boolean locked = false;
					if (grade != null) {
						locked = grade.isLocked();
					}
					
					DropdownMenu gradeDrop = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_GRADE), grades, "getGrade"));
					gradeDrop.addMenuElementFirst("", "");
					if ((grade == null && endDate != null) || locked) {
						gradeDrop.setDisabled(true);
					}
					else {
						if (grade != null) {
							gradeDrop.setSelectedElement(grade.getGrade().getPrimaryKey().toString());
						}
						table.add(new HiddenInput(PARAMETER_STUDENT, member.getPrimaryKey().toString()), column, row);
					}
					table.add(gradeDrop, column, row);
				}
				else {
					if (grade != null) {
						table.add(getSmallText(grade.getGrade().getGrade()), column, row);
					}
				}
				column++;
				
				Link edit = new Link(getEditIcon(localize("edit_student", "Edit student")));
				edit.addParameter(PARAMETER_CHOICE, choice.getPrimaryKey().toString());
				edit.addParameter(PARAMETER_STUDENT, member.getPrimaryKey().toString());
				edit.addParameter(StudentEditor.PARAMETER_ACTION, StudentEditor.ACTION_CHANGE_GROUP);
				edit.addParameter(StudentEditor.PARAMETER_PAGE, getParentPageID());
				edit.setWindowToOpen(StudentWindow.class);
				edit.setEventListener(StudentEditor.class);
				table.add(edit, column++, row);
				
				if (endDate == null) {
					if (stamp.isLaterThanOrEquals(startDate)) {
						Link delete = new Link(getDeleteIcon(localize("terminate_placement", "Terminate student placement")));
						delete.addParameter(PARAMETER_CHOICE, choice.getPrimaryKey().toString());
						delete.addParameter(PARAMETER_STUDENT, member.getPrimaryKey().toString());
						delete.addParameter(StudentEditor.PARAMETER_ACTION, StudentEditor.ACTION_SHOW_TERMINATE_PLACEMENT);
						delete.addParameter(StudentEditor.PARAMETER_PAGE, getParentPageID());
						delete.setEventListener(StudentEditor.class);
						delete.setWindowToOpen(StudentWindow.class);
						table.add(delete, column++, row);
					}
					else {
						SubmitButton delete = new SubmitButton(getDeleteIcon(localize("remove_placement", "Remove placement")));
						delete.setDescription(localize("remove_placement", "Remove placement"));
						delete.setValueOnClick(PARAMETER_STUDENT, member.getPrimaryKey().toString());
						delete.setValueOnClick(PARAMETER_CHOICE, choice.getPrimaryKey().toString());
						delete.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_REMOVE_PLACEMENT));
						delete.setSubmitConfirm(localize("confirm_placement_remove", "Are you sure you want to remove the placement?"));
						table.add(delete, column++, row);
					}
				}
				row++;
			}
		}
		table.setWidth(1, 12);
		table.setWidth(7, 12);
		table.setWidth(8, 12);
		
		return table;
	}
	
	private void updateGrades(IWContext iwc) throws RemoteException {
		getBusiness().updateGrades(iwc.getParameterValues(PARAMETER_STUDENT), iwc.getParameterValues(PARAMETER_GRADE), getSession().getCourse());
	}
	
	private void removePlacement(IWContext iwc) throws RemoteException {
		try {
			getBusiness().removePlacement(getSession().getSchoolClassMember(), getSession().getChoice(), iwc.getCurrentUser());
		}
		catch (RemoveException re) {
			re.printStackTrace();
			getParentPage().setAlertOnLoad(localize("remove_placement_failed", "An error occured while trying to remove placement."));
		}
	}
	
	private int parseAction(IWContext iwc) {
		int action = ACTION_VIEW;
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		
		return action;
	}
	
	public boolean actionPerformed(IWContext iwc) throws IWException {
		boolean actionPerformed = false;
		
		if (iwc.isParameterSet(PARAMETER_STUDENT)) {
			try {
				getSession(iwc).setSchoolClassMember(iwc.getParameter(PARAMETER_STUDENT));
				actionPerformed = true;
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		
		if (iwc.isParameterSet(PARAMETER_CHOICE)) {
			try {
				getSession(iwc).setChoice(iwc.getParameter(PARAMETER_CHOICE));
				actionPerformed = true;
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		
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

	public void setShowCatalogCreator(boolean showCatalogCreator) {
		iShowCatalogCreator = showCatalogCreator;
	}

	public void setShowGradeSetter(boolean showGradeSetter) {
		iShowGradeSetter = showGradeSetter;
	}
}