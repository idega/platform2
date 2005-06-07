/*
 * $Id: GroupEditor.java,v 1.7 2005/06/07 12:35:58 laddi Exp $
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
import javax.ejb.CreateException;
import se.idega.idegaweb.commune.adulteducation.business.DuplicateValueException;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourse;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.presentation.SchoolUserChooser;
import com.idega.block.school.presentation.SchoolUserWindow;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDORelationshipException;
import com.idega.event.IWPageEventListener;
import com.idega.idegaweb.IWException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/06/07 12:35:58 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.7 $
 */
public class GroupEditor extends AdultEducationBlock implements IWPageEventListener {

	private static final String PARAMETER_ACTION = "ge_action";
	private static final String PARAMETER_STUDY_PATH_GROUP = "ge_study_path_group";
	private static final String PARAMETER_SCHOOL_CLASS = "ge_school_class";
	private static final String PARAMETER_NAME = "ge_name";
	private static final String PARAMETER_TYPE = "ge_type";
	private static final String PARAMETER_CODE = "ge_code";
	private static final String PARAMETER_TEACHER = "ge_teacher";
	private static final String PARAMETER_OLD_CODE = "ge_old_code";
	private static final String PARAMETER_OLD_SEASON = "ge_old_season";
	private static final String PARAMETER_UPDATE = "ge_update";

	private static final int ACTION_VIEW = 1;
	private static final int ACTION_CREATE = 2;
	private static final int ACTION_EDIT = 3;
	private static final int ACTION_STORE = 4;
	private static final int ACTION_REMOVE = 5;
	
	private int action = -1;
	
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.adulteducation.presentation.AdultEducationBlock#present(com.idega.presentation.IWContext)
	 */
	public void present(IWContext iwc) {
		try {
			switch (parseAction(iwc)) {
				case ACTION_VIEW:
					showGroups();
					break;
					
				case ACTION_CREATE:
					showEditor();
					break;
					
				case ACTION_EDIT:
					showEditor();
					break;
					
				case ACTION_STORE:
					storeGroup(iwc);
					showGroups();
					break;
					
				case ACTION_REMOVE:
					removeGroup();
					showGroups();
					break;
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	private void showGroups() throws RemoteException {
		Form form = new Form();
		form.setEventListener(GroupEditor.class);
		form.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_VIEW));
		form.addParameter(PARAMETER_SCHOOL_CLASS, "");
		
		form.add(getNavigationTable());
		form.add(new Break());
		form.add(getGroups());
		
		SubmitButton create = (SubmitButton) getButton(new SubmitButton(localize("new_group", "New group")));
		create.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_CREATE));
		form.add(new Break());
		form.add(create);
		
		add(form);
	}
	
	private void showEditor() throws RemoteException {
		Form form = new Form();
		form.setEventListener(GroupEditor.class);
		boolean update = (action == ACTION_EDIT);
		if (update) {
			form.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_EDIT));
			form.addParameter(PARAMETER_OLD_CODE, getSession().getSchoolClass().getCode());
			form.addParameter(PARAMETER_OLD_SEASON, String.valueOf(getSession().getSchoolClass().getSchoolSeasonId()));
			form.addParameter(PARAMETER_UPDATE, Boolean.TRUE.toString());
		}
		else {
			form.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_CREATE));
		}
		
		Table table = new Table();
		table.setCellpadding(3);
		table.setCellspacing(0);
		int row = 1;
		
		table.add(getSmallHeader(localize("group_name", "Group name") + ":"), 1, row);
		TextInput name = (TextInput) getStyledInterface(new TextInput(PARAMETER_NAME));
		if (update) {
			name.setContent(getSession().getSchoolClass().getSchoolClassName());
		}
		name.keepStatusOnAction(true);
		table.add(name, 2, row++);
		
		SelectorUtility util = new SelectorUtility();
		
		table.add(getSmallHeader(localize("school_type", "School type") + ":"), 1, row);
		DropdownMenu types = new DropdownMenu(PARAMETER_TYPE);
		try {
			Collection type = getSession().getSchool().findRelatedSchoolTypes();
			types = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(types, type, "getLocalizationKey", getResourceBundle()));
		}
		catch (IDORelationshipException ire) {
			ire.printStackTrace();
		}
		if (update) {
			types.setSelectedElement(getSession().getSchoolClass().getSchoolTypeId());
		}
		types.keepStatusOnAction(true);
		table.add(types, 2, row++);

		table.add(getSmallHeader(localize("school_season", "Season") + ":"), 1, row);
		DropdownMenu seasons = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_SCHOOL_SEASON), getBusiness().getSeasons(), "getSeasonName"));
		seasons.addMenuElementFirst("", localize("select_season", "Select season"));
		if (getSession().getSchoolSeason() != null) {
			seasons.setSelectedElement(getSession().getSchoolSeason().getPrimaryKey().toString());
		}
		seasons.setToSubmit();
		table.add(seasons, 2, row++);
		
		table.add(getSmallHeader(localize("study_path_group", "Study path group") + ":"), 1, row);
		DropdownMenu studyGroups = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_STUDY_PATH_GROUP), getBusiness().getStudyPathsGroups(), "getLocalizationKey", getResourceBundle()));
		studyGroups.addMenuElementFirst("", localize("select_study_path_group", "Select group"));
		if (getSession().getStudyPathGroup() != null) {
			studyGroups.setSelectedElement(getSession().getStudyPathGroup().getPrimaryKey().toString());
		}
		studyGroups.setToSubmit();
		table.add(studyGroups, 2, row++);
		
		table.add(getSmallHeader(localize("code", "Code") + ":"), 1, row);
		DropdownMenu courses = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_CODE));
		if (getSession().getSchoolSeason() != null && getSession().getStudyPathGroup() != null) {
			Collection availableCourses = getBusiness().getCourses(getSession().getSchoolSeason().getPrimaryKey(), getSession().getSchool().getPrimaryKey(), getSession().getStudyPathGroup().getPrimaryKey());
			Iterator iter = availableCourses.iterator();
			while (iter.hasNext()) {
				AdultEducationCourse course = (AdultEducationCourse) iter.next();
				courses.addMenuElement(course.getCode(), course.getCode());
			}
		}
		courses.keepStatusOnAction(true);
		table.add(courses, 2, row++);

		Link link = new Link(localize("create_school_user", "Create school user"));
		link.setWindowToOpen(SchoolUserWindow.class);
		link.setParameter("sue_act", "sue_pvs");
		link.setParameter("pr_schl_id", getSession().getSchool().getPrimaryKey().toString());
		table.add(link, 2, row++);
		
		table.add(getSmallHeader(localize("teacher", "Teacher") + ":"), 1, row);
		SchoolUserChooser chooser = new SchoolUserChooser(PARAMETER_TEACHER, getSession().getSchool());
		if (update) {
			try {
				Collection teachers = getSession().getSchoolClass().findRelatedUsers();
				Iterator iter = teachers.iterator();
				while (iter.hasNext()) {
					User teacher = (User) iter.next();
					chooser.setSelected(teacher);
					break;
				}
			}
			catch (IDORelationshipException ire) {
				ire.printStackTrace();
			}
		}
		table.add(chooser, 2, row++);
				
		form.add(table);
		form.add(new Break());
		
		SubmitButton save = (SubmitButton) getButton(new SubmitButton(localize("save_group", "Save group")));
		save.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_STORE));
		SubmitButton cancel = (SubmitButton) getButton(new SubmitButton(localize("cancel", "Cancek")));
		save.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_VIEW));
		
		form.add(cancel);
		form.add(Text.getNonBrakingSpace());
		form.add(save);
		
		add(form);
	}

	private Table getNavigationTable() throws RemoteException {
		Table table = new Table(5, 1);
		table.setCellpadding(3);
		table.setCellspacing(0);
		
		SelectorUtility util = new SelectorUtility();
		
		DropdownMenu seasons = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_SCHOOL_SEASON), getBusiness().getSeasons(), "getSeasonName"));
		seasons.addMenuElementFirst("", localize("select_season", "Select season"));
		if (getSession().getSchoolSeason() != null) {
			seasons.setSelectedElement(getSession().getSchoolSeason().getPrimaryKey().toString());
		}
		
		DropdownMenu studyGroups = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_STUDY_PATH_GROUP), getBusiness().getStudyPathsGroups(), "getLocalizationKey", getResourceBundle()));
		studyGroups.addMenuElementFirst("", localize("select_study_path_group", "Select group"));
		if (getSession().getStudyPathGroup() != null) {
			studyGroups.setSelectedElement(getSession().getStudyPathGroup().getPrimaryKey().toString());
		}
		
		SubmitButton button = (SubmitButton) getButton(new SubmitButton(localize("search", "Search")));
		
		table.add(getSmallHeader(localize("season", "Season") + ":"), 1, 1);
		table.add(seasons, 2, 1);
		table.add(getSmallHeader(localize("study_path_group", "Study path group") + ":"), 3, 1);
		table.add(studyGroups, 4, 1);
		table.add(button, 5, 1);
		
		return table;
	}
	
	private Table getGroups() throws RemoteException {
		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(7);
		table.setRowColor(1, getHeaderColor());
		int row = 1;
		int column = 1;
		
		table.add(getLocalizedSmallHeader("name","Name"), column++, row);
		table.add(getLocalizedSmallHeader("school_type","Type"), column++, row);
		table.add(getLocalizedSmallHeader("school_season","Season"), column++, row);
		table.add(getLocalizedSmallHeader("code","Code"), column++, row);
		table.add(getLocalizedSmallHeader("teacher","Teacher"), column++, row++);
		
		Collection choices = getBusiness().getGroups(getSession().getSchool(), getSession().getSchoolSeason(), getSession().getStudyPathGroup());
		Iterator iter = choices.iterator();
		while (iter.hasNext()) {
			SchoolClass group = (SchoolClass) iter.next();
			SchoolType type = group.getSchoolType();
			SchoolSeason season = group.getSchoolSeason();
			column = 1;
			
			if (row % 2 == 0) {
				table.setRowColor(row, getZebraColor1());
			}
			else {
				table.setRowColor(row, getZebraColor2());
			}

			table.add(getSmallText(group.getSchoolClassName()), column++, row);
			table.add(getSmallText(type.getSchoolTypeName()), column++, row);
			table.add(getSmallText(season.getSchoolSeasonName()), column++, row);
			table.add(getSmallText(group.getCode()), column++, row);
			
			StringBuffer teacher = new StringBuffer();
			try {
				Collection teachers = group.findRelatedUsers();
				Iterator iterator = teachers.iterator();
				while (iterator.hasNext()) {
					User element = (User) iterator.next();
					teacher.append(element.getName());
					if (iterator.hasNext()) {
						teacher.append(", ");
					}
				}
			}
			catch (IDORelationshipException ire) {
				ire.printStackTrace();
			}
			table.add(getSmallText(teacher.toString()), column++, row);
			
			Link edit = new Link(getEditIcon(localize("edit_group", "Edit group")));
			edit.addParameter(PARAMETER_ACTION, ACTION_EDIT);
			edit.addParameter(PARAMETER_SCHOOL_CLASS, group.getPrimaryKey().toString());
			edit.setEventListener(GroupEditor.class);
			table.add(edit, column++, row);
			
			SubmitButton remove = new SubmitButton(getDeleteIcon(localize("remove_group", "Remove group")));
			remove.setValueOnClick(PARAMETER_SCHOOL_CLASS, group.getPrimaryKey().toString());
			remove.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_REMOVE));
			table.add(remove, column++, row++);
		}
		table.setWidth(6, 12);
		table.setWidth(7, 12);
		
		return table;
	}
	
	private void storeGroup(IWContext iwc) throws RemoteException {
		try {
			boolean update = iwc.isParameterSet(PARAMETER_UPDATE);
			String name = iwc.getParameter(PARAMETER_NAME);
			String code = iwc.getParameter(PARAMETER_CODE);
			SchoolType type = getBusiness().getSchoolBusiness().getSchoolType(new Integer(iwc.getParameter(PARAMETER_TYPE)));
			User teacher = null;
			if (iwc.isParameterSet(PARAMETER_TEACHER)) {
				teacher = getBusiness().getUserBusiness().getUser(new Integer(iwc.getParameter(PARAMETER_TEACHER)));
			}

			SchoolSeason oldSeason = null;
			String oldCode = null;
			if (update) {
				oldSeason = getBusiness().getSchoolBusiness().getSchoolSeason(new Integer(iwc.getParameter(PARAMETER_OLD_SEASON)));
				oldCode = iwc.getParameter(PARAMETER_OLD_CODE);
			}
			
			getBusiness().storeGroup(name, getSession().getSchool(), getSession().getSchoolSeason(), oldSeason, type, code, oldCode, teacher, update);
		}
		catch (CreateException ce) {
			ce.printStackTrace();
			getParentPage().setAlertOnLoad(localize("error_during_store", "There was an error during store") + ": " + ce.getMessage());
		}
		catch (DuplicateValueException dve) {
			getParentPage().setAlertOnLoad(localize("group_with_code_already_exists", "A group with that code and season already exists"));
		}
	}
	
	private void removeGroup() throws RemoteException {
		getBusiness().removeGroup(getSession().getSchoolClass());
	}
	
	private int parseAction(IWContext iwc) {
		action = ACTION_VIEW;
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		
		return action;
	}
	
	public boolean actionPerformed(IWContext iwc) throws IWException {
		boolean actionPerformed = false;
		
		if (iwc.isParameterSet(PARAMETER_SCHOOL_SEASON)) {
			try {
				getSession(iwc).setSeason(iwc.getParameter(PARAMETER_SCHOOL_SEASON));
				actionPerformed = true;
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		else {
			try {
				getSession(iwc).setSeason(null);
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
		else {
			try {
				getSession(iwc).setStudyPathGroup(null);
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

		return actionPerformed;
	}
}