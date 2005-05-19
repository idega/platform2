/*
 * $Id: CourseEditor.java,v 1.3 2005/05/19 12:35:25 laddi Exp $
 * Created on 27.4.2005
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
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import se.idega.idegaweb.commune.adulteducation.business.DuplicateValueException;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourse;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolStudyPathGroup;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBORuntimeException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.util.IWTimestamp;


/**
 * Last modified: $Date: 2005/05/19 12:35:25 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3 $
 */
public class CourseEditor extends AdultEducationBlock {

	private static final String PARAMETER_ACTION = "ce_action";
	private static final String PARAMETER_FROM_ACTION = "ce_from_action";

	private static final String PARAMETER_SCHOOL_TYPE = "ce_school_type";
	private static final String PARAMETER_SCHOOL = "ce_school";
	private static final String PARAMETER_SCHOOL_SEASON = "ce_school_season";
	private static final String PARAMETER_OLD_SCHOOL_SEASON = "ce_old_school_season";
	private static final String PARAMETER_STUDY_PATH_GROUP = "ce_study_path_group";
	
	private static final String PARAMETER_STUDY_PATH = "ce_study_path";
	private static final String PARAMETER_CODE = "ce_code";
	private static final String PARAMETER_OLD_CODE = "ce_old_code";
	private static final String PARAMETER_START_DATE = "ce_start_date";
	private static final String PARAMETER_END_DATE = "ce_end_date";
	private static final String PARAMETER_COMMENT = "ce_comment";
	private static final String PARAMETER_LENGTH = "ce_length";
	private static final String PARAMETER_NOT_ACTIVE = "ce_not_active";
	private static final String PARAMETER_UPDATE = "ce_update";

	private static final String PARAMETER_COURSE_PK = "ce_course_pk";

	private static final int ACTION_DELETE = 1;
	private static final int ACTION_EDIT = 2;
	private static final int ACTION_COPY = 3;
	private static final int ACTION_STORE = 4;
	
	private int iAction = ACTION_EDIT;
	
	private AdultEducationCourse iCourse;
	private Object iSchoolTypePK;
	private SchoolType iSchoolType;
	private Object iSchoolPK;
	//private School iSchool;
	private Object iSchoolSeasonPK;
	private SchoolSeason iSchoolSeason;
	private Object iStudyPathGroupPK;
	private SchoolStudyPathGroup iStudyPathGroup;
	
	public void present(IWContext iwc) {
		try {
			switch (parseAction(iwc)) {
				case ACTION_EDIT:
					break;
				case ACTION_COPY:
					break;
				case ACTION_DELETE:
					remove(iwc);
					break;
				case ACTION_STORE:
					store(iwc);
					break;
			}
			
			add(getEditor());
			add(new Break(2));
			add(getCourses(iwc));
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	private Form getEditor() throws RemoteException {
		Form form = new Form();
		form.addParameter(PARAMETER_FROM_ACTION, String.valueOf(iAction));
		form.maintainParameter(PARAMETER_OLD_SCHOOL_SEASON);
		form.maintainParameter(PARAMETER_OLD_CODE);

		form.add(getNavigationTable());
		form.add(new Break(2));
		form.add(getEditorTable());

		return form;
	}
	
	private Table getNavigationTable() throws RemoteException {
		Table table = new Table(4, 3);
		table.setCellpadding(3);
		table.setCellspacing(0);
		//table.setWidth(Table.HUNDRED_PERCENT);
		
		SelectorUtility util = new SelectorUtility();
		
		DropdownMenu types = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_SCHOOL_TYPE), getBusiness().getSchoolTypes(), "getLocalizationKey", getResourceBundle()));
		types.addMenuElementFirst("", localize("select_type", "Select type"));
		if (iSchoolTypePK != null) {
			types.setSelectedElement(iSchoolTypePK.toString());
		}
		types.setToSubmit();
		
		DropdownMenu schools = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_SCHOOL), getBusiness().getSchools(iSchoolType), "getSchoolName"));
		schools.addMenuElementFirst("", localize("select_school", "Select school"));
		if (iSchoolPK != null) {
			schools.setSelectedElement(iSchoolPK.toString());
		}
		schools.setToSubmit();
		
		DropdownMenu groups = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_STUDY_PATH_GROUP), getBusiness().getStudyPathsGroups(), "getLocalizationKey", getResourceBundle()));
		groups.addMenuElementFirst("", localize("select_study_path_group", "Select group"));
		if (iStudyPathGroupPK != null) {
			groups.setSelectedElement(iStudyPathGroupPK.toString());
		}
		groups.setToSubmit();
		
		DropdownMenu seasons = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_SCHOOL_SEASON), getBusiness().getSeasons(), "getSeasonName"));
		seasons.addMenuElementFirst("", localize("select_season", "Select season"));
		if (iSchoolSeasonPK != null) {
			seasons.setSelectedElement(iSchoolSeasonPK.toString());
		}
		seasons.setToSubmit();
		
		table.add(getSmallHeader(localize("type", "Type") + ":"), 1, 1);
		table.add(types, 2, 1);
		table.add(getSmallHeader(localize("school", "School") + ":"), 1, 2);
		table.add(schools, 2, 2);
		table.add(getSmallHeader(localize("study_path_group", "Study path group") + ":"), 3, 2);
		table.add(groups, 4, 2);
		table.add(getSmallHeader(localize("season", "Season") + ":"), 1, 3);
		table.add(seasons, 2, 3);
		
		return table;
	}
	
	private Table getEditorTable() throws RemoteException {
		Table table = new Table(4, 3);
		table.setCellpadding(3);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		
		SelectorUtility util = new SelectorUtility();

		DropdownMenu studyPaths = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_STUDY_PATH), getBusiness().getStudyPaths(iSchoolType, iStudyPathGroup), "getDescription"));
		studyPaths.addMenuElementFirst("", localize("select_study_path", "Select study path"));
		if (iCourse != null) {
			studyPaths.setSelectedElement(iCourse.getStudyPathPK().toString());
		}
		if (!(iAction == ACTION_DELETE || iAction == ACTION_STORE)) {
			studyPaths.keepStatusOnAction(true);
		}
		
		TextInput code = (TextInput) getStyledInterface(new TextInput(PARAMETER_CODE));
		if (iCourse != null) {
			code.setContent(iCourse.getCode());
		}
		if (iAction == ACTION_EDIT) {
			table.add(new HiddenInput(PARAMETER_UPDATE, Boolean.TRUE.toString()));
		}
		if (!(iAction == ACTION_DELETE || iAction == ACTION_STORE)) {
			code.keepStatusOnAction(true);
		}
		code.setMaxlength(20);
		
		DateInput startDate = (DateInput) getStyledInterface(new DateInput(PARAMETER_START_DATE));
		if (iSchoolSeason != null) {
			startDate.setDate(iSchoolSeason.getSchoolSeasonStart());
		}
		if (iCourse != null) {
			startDate.setDate(iCourse.getStartDate());
		}
		if (!(iAction == ACTION_DELETE || iAction == ACTION_STORE)) {
			startDate.keepStatusOnAction(true);
		}
		
		DateInput endDate = (DateInput) getStyledInterface(new DateInput(PARAMETER_END_DATE));
		if (iSchoolSeason != null) {
			endDate.setDate(iSchoolSeason.getSchoolSeasonEnd());
		}
		if (iCourse != null) {
			endDate.setDate(iCourse.getEndDate());
		}
		if (!(iAction == ACTION_DELETE || iAction == ACTION_STORE)) {
			endDate.keepStatusOnAction(true);
		}
		
		TextInput comment = (TextInput) getStyledInterface(new TextInput(PARAMETER_COMMENT));
		if (iCourse != null) {
			comment.setContent(iCourse.getComment());
		}
		if (!(iAction == ACTION_DELETE || iAction == ACTION_STORE)) {
			comment.keepStatusOnAction(true);
		}
		comment.setWidth(Table.HUNDRED_PERCENT);
		
		TextInput length = (TextInput) getStyledInterface(new TextInput(PARAMETER_LENGTH));
		if (iCourse != null && iCourse.getLength() != -1) {
			length.setContent(String.valueOf(iCourse.getLength()));
		}
		if (!(iAction == ACTION_DELETE || iAction == ACTION_STORE)) {
			length.keepStatusOnAction(true);
		}
		length.setLength(3);
		length.setMaxlength(3);
		
		CheckBox box = getCheckBox(PARAMETER_NOT_ACTIVE, Boolean.TRUE.toString());
		if (iCourse != null) {
			box.setChecked(iCourse.isInactive());
		}
		if (!(iAction == ACTION_DELETE || iAction == ACTION_STORE)) {
			box.keepStatusOnAction(true);
		}

		SubmitButton store = (SubmitButton) getButton(new SubmitButton(localize("store", "Store"), PARAMETER_ACTION, String.valueOf(ACTION_STORE)));
		
		table.add(getSmallHeader(localize("study_path", "Study path")), 1, 1);
		table.add(new Break(), 1, 1);
		table.add(studyPaths, 1, 1);

		table.add(getSmallHeader(localize("code", "Code")), 2, 1);
		table.add(new Break(), 2, 1);
		table.add(code, 2, 1);

		table.add(getSmallHeader(localize("start_date", "Start date")), 3, 1);
		table.add(new Break(), 3, 1);
		table.add(startDate, 3, 1);

		table.add(getSmallHeader(localize("comment", "Comment")), 1, 2);
		table.add(new Break(), 1, 2);
		table.mergeCells(1, 2, 2, 2);
		table.add(comment, 1, 2);

		table.add(getSmallHeader(localize("end_date", "End date")), 3, 2);
		table.add(new Break(), 3, 2);
		table.add(endDate, 3, 2);

		table.add(getSmallHeader(localize("length", "Length")), 4, 2);
		table.add(new Break(), 4, 2);
		table.add(length, 4, 2);

		table.mergeCells(1, 3, 2, 3);
		table.add(box, 1, 3);
		table.add(Text.getNonBrakingSpace(), 1, 3);
		table.add(getSmallText(localize("course_not_active", "Course is not active")), 1, 3);
		
		table.mergeCells(3, 3, 4, 3);
		table.setAlignment(3, 3, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add(store, 3, 3);
		
		return table;
	}
	
	private Table getCourses(IWContext iwc) {
		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(7);
		table.setRowColor(1, getHeaderColor());
		int row = 1;
		int column = 1;
		
		table.add(getLocalizedSmallHeader("study_path","Study path"), column++, row);
		table.add(getLocalizedSmallHeader("code","Code"), column++, row);
		table.add(getLocalizedSmallHeader("period","Period"), column++, row);
		table.add(getLocalizedSmallHeader("length","Length"), column++, row++);

		if (iSchoolSeasonPK != null && iSchoolTypePK != null && iSchoolPK != null && iStudyPathGroupPK != null) {
			try {
				Collection courses = getBusiness().getCourses(iSchoolSeasonPK, iSchoolTypePK, iSchoolPK, iStudyPathGroupPK);
				Iterator iter = courses.iterator();
				while (iter.hasNext()) {
					AdultEducationCourse course = (AdultEducationCourse) iter.next();
					SchoolStudyPath path = course.getStudyPath();
					IWTimestamp start = new IWTimestamp(course.getStartDate());
					IWTimestamp end = new IWTimestamp(course.getEndDate());
					
					Link edit = new Link(getEditIcon(localize("edit", "Edit")));
					edit.addParameter(PARAMETER_COURSE_PK, course.getPrimaryKey().toString());
					edit.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_EDIT));
					edit.addParameter(PARAMETER_OLD_SCHOOL_SEASON, iSchoolSeasonPK.toString());
					edit.addParameter(PARAMETER_OLD_CODE, course.getCode());
					edit.maintainParameter(PARAMETER_SCHOOL_TYPE, iwc);
					edit.maintainParameter(PARAMETER_STUDY_PATH_GROUP, iwc);
					edit.maintainParameter(PARAMETER_SCHOOL, iwc);
					edit.maintainParameter(PARAMETER_SCHOOL_SEASON, iwc);
					
					Link copy = new Link(getCopyIcon(localize("copy", "Copy")));
					copy.addParameter(PARAMETER_COURSE_PK, course.getPrimaryKey().toString());
					copy.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_COPY));
					copy.maintainParameter(PARAMETER_SCHOOL_TYPE, iwc);
					copy.maintainParameter(PARAMETER_STUDY_PATH_GROUP, iwc);
					copy.maintainParameter(PARAMETER_SCHOOL, iwc);
					copy.maintainParameter(PARAMETER_SCHOOL_SEASON, iwc);
					
					Link delete = new Link(getDeleteIcon(localize("delete", "Delete")));
					delete.addParameter(PARAMETER_COURSE_PK, course.getPrimaryKey().toString());
					delete.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_DELETE));
					delete.maintainParameter(PARAMETER_SCHOOL_TYPE, iwc);
					delete.maintainParameter(PARAMETER_STUDY_PATH_GROUP, iwc);
					delete.maintainParameter(PARAMETER_SCHOOL, iwc);
					delete.maintainParameter(PARAMETER_SCHOOL_SEASON, iwc);
					
					if (row % 2 == 0) {
						table.setRowColor(row, getZebraColor1());
					}
					else {
						table.setRowColor(row, getZebraColor2());
					}
					if (course.isInactive()) {
						table.setRowColor(row, RED_COLOR);
					}
					
					table.add(getSmallText(path.getDescription()), 1, row);
					table.add(getSmallText(course.getCode()), 2, row);
					table.add(getSmallText(start.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT) + " - " + end.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), 3, row);
					table.add(getSmallText(String.valueOf(course.getLength())), 4, row);
					table.add(edit, 5, row);
					table.add(copy, 6, row++);
					table.add(delete, 7, row++);
				}
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		
		return table;
	}
	
	private void store(IWContext iwc) {
		Object oldSeason = iwc.isParameterSet(PARAMETER_OLD_SCHOOL_SEASON) ? iwc.getParameter(PARAMETER_OLD_SCHOOL_SEASON) : null;
		String studyPath = iwc.isParameterSet(PARAMETER_STUDY_PATH) ? iwc.getParameter(PARAMETER_STUDY_PATH) : null;
		String code = iwc.isParameterSet(PARAMETER_CODE) ? iwc.getParameter(PARAMETER_CODE) : null;
		String oldCode = iwc.isParameterSet(PARAMETER_OLD_CODE) ? iwc.getParameter(PARAMETER_OLD_CODE) : null;
		String startDate = iwc.isParameterSet(PARAMETER_START_DATE) ? iwc.getParameter(PARAMETER_START_DATE) : null;
		String endDate = iwc.isParameterSet(PARAMETER_END_DATE) ? iwc.getParameter(PARAMETER_END_DATE) : null;
		String comment = iwc.getParameter(PARAMETER_COMMENT);
		String lengthString = iwc.isParameterSet(PARAMETER_LENGTH) ? iwc.getParameter(PARAMETER_LENGTH) : null;
		boolean notActive = iwc.isParameterSet(PARAMETER_NOT_ACTIVE);
		boolean update = iwc.isParameterSet(PARAMETER_UPDATE);
		int length = 0;
		
		boolean validated = true;
		StringBuffer alert = new StringBuffer();
		alert.append(localize("validation_alert", "The following inputs must be filled: "));
		if (iSchoolPK == null) {
			alert.append(localize("school_input", "School"));
			validated = false;
		}
		if (iSchoolSeasonPK == null) {
			if (!validated) {
				alert.append(", ");
			}
			alert.append(localize("school_season_input", "Season"));
			validated = false;
		}
		if (studyPath == null) {
			if (!validated) {
				alert.append(", ");
			}
			alert.append(localize("study_path_input", "Study path"));
			validated = false;
		}
		if (code == null) {
			if (!validated) {
				alert.append(", ");
			}
			alert.append(localize("code_input", "Code"));
			validated = false;
		}
		if (startDate == null) {
			if (!validated) {
				alert.append(", ");
			}
			alert.append(localize("start_date_input", "Start date"));
			validated = false;
		}
		if (endDate == null) {
			if (!validated) {
				alert.append(", ");
			}
			alert.append(localize("end_date_input", "End date"));
			validated = false;
		}
		if (lengthString == null) {
			if (!validated) {
				alert.append(", ");
			}
			alert.append(localize("length_input", "Length"));
			validated = false;
		}
		if (lengthString != null) {
			try {
				length = Integer.parseInt(lengthString);
			}
			catch (NumberFormatException nfe) {
				getParentPage().setAlertOnLoad(localize("length_must_be_integer", "Length must be a number"));
				return;
			}
		}
		
		if (validated) {
			try {
				getBusiness().storeCourse(iSchoolSeasonPK, oldSeason, code, oldCode, iSchoolPK, studyPath, new IWTimestamp(startDate).getDate(), new IWTimestamp(endDate).getDate(), comment, length, notActive, update);
				if (update) {
					getParentPage().setAlertOnLoad(localize("course_updated", "Course updated"));
				}
				else {
					getParentPage().setAlertOnLoad(localize("course_stored", "Course stored"));
				}
			}
			catch (CreateException ce) {
				ce.printStackTrace();
			}
			catch (DuplicateValueException dpe) {
				getParentPage().setAlertOnLoad(localize("duplicate_values_detected", "Trying to store duplicate values, ignoring..."));
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		else {
			iAction = Integer.parseInt(iwc.getParameter(PARAMETER_FROM_ACTION));
			getParentPage().setAlertOnLoad(alert.toString());
		}
	}
	
	private void remove(IWContext iwc) {
		try {
			getBusiness().removeCourse(iwc.getParameter(PARAMETER_COURSE_PK));
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
		catch (RemoveException re) {
			getParentPage().setAlertOnLoad(localize("course_could_not_be_deleted", "Course could not be deleted.  It probably has some choices attached to it"));
		}
	}
	
	private int parseAction(IWContext iwc) {
		try {
			iAction = ACTION_EDIT;
			if (iwc.isParameterSet(PARAMETER_ACTION)) {
				iAction = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
			}

			if (iwc.isParameterSet(PARAMETER_SCHOOL_TYPE)) {
				iSchoolTypePK = iwc.getParameter(PARAMETER_SCHOOL_TYPE);
				iSchoolType = getBusiness().getSchoolBusiness().getSchoolType(iSchoolTypePK);
			}
			if (iwc.isParameterSet(PARAMETER_SCHOOL)) {
				iSchoolPK = iwc.getParameter(PARAMETER_SCHOOL);
				//iSchool = getBusiness().getSchoolBusiness().getSchool(iSchoolPK);
			}
			if (iwc.isParameterSet(PARAMETER_SCHOOL_SEASON)) {
				iSchoolSeasonPK = iwc.getParameter(PARAMETER_SCHOOL_SEASON);
				iSchoolSeason = getBusiness().getSchoolBusiness().getSchoolSeason(iSchoolSeasonPK);
			}
			if (iwc.isParameterSet(PARAMETER_STUDY_PATH_GROUP)) {
				iStudyPathGroupPK = iwc.getParameter(PARAMETER_STUDY_PATH_GROUP);
				iStudyPathGroup = getBusiness().getStudyPathBusiness().findStudyPathGroup(iStudyPathGroupPK);
			}
			if (iwc.isParameterSet(PARAMETER_COURSE_PK) && iAction == ACTION_EDIT) {
				try {
					iCourse = getBusiness().getCourse(iwc.getParameter(PARAMETER_COURSE_PK));
				}
				catch (FinderException fe) {
					fe.printStackTrace();
				}
			}
			
			return iAction;
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
}