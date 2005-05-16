/*
 * $Id: ChoiceApplication.java,v 1.9 2005/05/16 19:41:02 laddi Exp $
 * Created on May 10, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.adulteducation.business.CourseCollectionHandler;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoice;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceReason;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourse;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolStudyPathGroup;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOCreateException;
import com.idega.data.IDORelationshipException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.remotescripting.RemoteScriptHandler;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.util.SelectorUtility;


/**
 * Last modified: $Date: 2005/05/16 19:41:02 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.9 $
 */
public class ChoiceApplication extends AdultEducationBlock {

	private static final String PARAMETER_ACTION = "ce_action";

	private static final String PARAMETER_SCHOOL_TYPE = "ce_school_type";
	private static final String PARAMETER_STUDY_PATH_GROUP = "ce_study_path_group";
	
	private static final String PARAMETER_SCHOOL = "ce_school";
	private static final String PARAMETER_COURSE = "ce_course";
	private static final String PARAMETER_COMMENT = "ce_comment";
	private static final String PARAMETER_REASON = "ce_reason";
	private static final String PARAMETER_OTHER_REASON = "ce_other_reason";
	private static final String PARAMETER_OLD_COURSES = "ce_old_courses";

	private static final int ACTION_APPLICATION = 1;
	private static final int ACTION_STORE = 2;
	
	private Object iSchoolTypePK;
	private SchoolType iSchoolType;
	private Object iSchoolSeasonPK;
	private SchoolSeason iSchoolSeason;
	private Object iStudyPathGroupPK;
	private SchoolStudyPathGroup iStudyPathGroup;
	private Object iStudyPathPK;
	private AdultEducationChoice iChoice;
	
	private boolean isUpdate = false;

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.adulteducation.presentation.AdultEducationBlock#present(com.idega.presentation.IWContext)
	 */
	public void present(IWContext iwc) {
		try {
			switch (parseAction(iwc)) {
				case ACTION_APPLICATION:
					showApplication(iwc);
					break;
				case ACTION_STORE:
					store(iwc);
					break;
			}
		}
		catch (RemoteException re) {
			re.printStackTrace();
		}
	}
	
	private void showApplication(IWContext iwc) throws RemoteException {
		Form form = new Form();

		Table table = new Table(1, 5);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(2, 20);
		table.setHeight(4, 20);
		table.setAlignment(1, 5, Table.HORIZONTAL_ALIGN_RIGHT);
		form.add(table);
		
		table.add(getNavigationTable(), 1, 1);
		table.add(getApplicationTable(iwc), 1, 3);
		if (isUpdate) {
			form.maintainParameter(PARAMETER_CHOICE);
		}
		
		SubmitButton submit = (SubmitButton) getButton(new SubmitButton(localize("submit", "Submit"), PARAMETER_ACTION, String.valueOf(ACTION_STORE)));
		table.add(submit, 1, 5);
		submit.setOnSubmitFunction("checkApplication", getSubmitCheckScript());
		form.setToDisableOnSubmit(submit, true);

		add(form);
	}
	
	private Table getNavigationTable() throws RemoteException {
		Table table = new Table(3, 1);
		table.setCellpadding(3);
		table.setCellspacing(0);
		
		SelectorUtility util = new SelectorUtility();
		
		DropdownMenu types = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_SCHOOL_TYPE), getBusiness().getSchoolTypes(), "getLocalizationKey", getResourceBundle()));
		types.addMenuElementFirst("", localize("select_type", "Select type"));
		if (iSchoolTypePK != null) {
			types.setSelectedElement(iSchoolTypePK.toString());
		}
		types.setToSubmit();
		if (isUpdate) {
			types.setDisabled(true);
			table.add(new HiddenInput(PARAMETER_SCHOOL_TYPE, iSchoolTypePK.toString()));
		}
		
		DropdownMenu groups = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_STUDY_PATH_GROUP), getBusiness().getStudyPathsGroups(), "getLocalizationKey", getResourceBundle()));
		groups.addMenuElementFirst("", localize("select_study_path_group", "Select group"));
		if (iStudyPathGroupPK != null) {
			groups.setSelectedElement(iStudyPathGroupPK.toString());
		}
		groups.setToSubmit();
		if (isUpdate) {
			groups.setDisabled(true);
			table.add(new HiddenInput(PARAMETER_STUDY_PATH_GROUP, iStudyPathGroupPK.toString()));
		}
		
		DropdownMenu seasons = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_SCHOOL_SEASON), getBusiness().getSeasons(), "getSeasonName"));
		seasons.addMenuElementFirst("", localize("select_season", "Select season"));
		if (iSchoolSeasonPK != null) {
			seasons.setSelectedElement(iSchoolSeasonPK.toString());
		}
		seasons.setToSubmit();
		if (isUpdate) {
			seasons.setDisabled(true);
			table.add(new HiddenInput(PARAMETER_SCHOOL_SEASON, iSchoolSeasonPK.toString()));
		}
		
		table.add(getSmallHeader(localize("type", "Type")), 1, 1);
		table.add(new Break(), 1, 1);
		table.add(types, 1, 1);
		table.add(getSmallHeader(localize("study_path_group", "Study path group") + ":"), 2, 1);
		table.add(new Break(), 2, 1);
		table.add(groups, 2, 1);
		table.add(getSmallHeader(localize("season", "Season") + ":"), 3, 1);
		table.add(new Break(), 3, 1);
		table.add(seasons, 3, 1);
		
		return table;
	}
	
	private Table getApplicationTable(IWContext iwc) throws RemoteException {
		Table table = new Table(4, 5);
		table.setCellpadding(3);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.mergeCells(1, 4, 4, 4);
		table.mergeCells(1, 5, 4, 5);
		table.setWidth(2, 12);
		
		Collection paths = getBusiness().getStudyPaths(iSchoolType, iStudyPathGroup);
		if (!isUpdate) {
			paths.removeAll(getBusiness().getSelectedStudyPaths(iwc.getCurrentUser(), iSchoolSeason));
		}
		
		DropdownMenu studyPaths = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_STUDY_PATH));
		studyPaths.addMenuElementFirst("", localize("select_study_path", "Select study path"));
		Iterator iter = paths.iterator();
		while (iter.hasNext()) {
			SchoolStudyPath path = (SchoolStudyPath) iter.next();
			studyPaths.addMenuElement(path.getPrimaryKey().toString(), path.getDescription() + ", " + path.getPoints());
		}
		studyPaths.setToSubmit();
		if (iStudyPathPK != null) {
			studyPaths.setSelectedElement(iStudyPathPK.toString());
		}
		if (isUpdate) {
			studyPaths.setDisabled(true);
			table.add(new HiddenInput(PARAMETER_STUDY_PATH, iStudyPathPK.toString()));
		}
		
		table.add(getSmallHeader(localize("study_path", "Study path")), 1, 1);
		table.add(new Break(), 1, 1);
		table.add(studyPaths, 1, 1);
		
		Collection schools = null;
		if (iStudyPathPK != null && iSchoolSeasonPK != null) {
			schools = getBusiness().getAvailableSchools(iStudyPathPK, iSchoolSeasonPK);
		}

		for (int a = 1; a <= 3; a++) {
			School chosenSchool = null;
			AdultEducationCourse chosenCourse = null;
			if (isUpdate) {
				try {
					AdultEducationChoice choice = getBusiness().getChoice(iwc.getCurrentUser(), iStudyPathPK, a);
					AdultEducationCourse course = choice.getCourse();
					
					chosenSchool = course.getSchool();
					chosenCourse = choice.getCourse();
				}
				catch (FinderException fe) {
					//fe.printStackTrace();
				}
			}
			
			DropdownMenu school = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_SCHOOL + "_" + a));
			school.addMenuElementFirst("", localize("select_school", "Select school"));
			if (schools != null) {
				Iterator iterator = schools.iterator();
				while (iterator.hasNext()) {
					School element = (School) iterator.next();
					school.addMenuElement(element.getPrimaryKey().toString(), element.getSchoolName());
				}
			}
			if (chosenSchool != null) {
				school.setSelectedElement(chosenSchool.getPrimaryKey().toString());
			}
			
			DropdownMenu course = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_COURSE + "_" + a));
			course.addMenuElementFirst("", localize("select_course", "Select course"));
			if (chosenCourse != null) {
				Collection courses = getBusiness().getAvailableCourses(iSchoolSeasonPK, chosenSchool.getPrimaryKey(), iStudyPathPK);
				Iterator iterator = courses.iterator();
				while (iterator.hasNext()) {
					AdultEducationCourse element = (AdultEducationCourse) iterator.next();
					if (!element.isInactive() || (isUpdate && chosenCourse.equals(element))) {
						course.addMenuElement(element.getPrimaryKey().toString(), element.getCode());
					}
				}
				course.setSelectedElement(chosenCourse.getPrimaryKey().toString());
				table.add(new HiddenInput(PARAMETER_OLD_COURSES, chosenCourse.getPrimaryKey().toString()));
			}
			
			if (a == 1) {
				table.add(getSmallHeader(localize("school", "School")), 3, 1);
				table.add(new Break(), 3, 1);
				table.add(getSmallHeader(localize("course_code", "Course code")), 4, 1);
				table.add(new Break(), 4, 1);
			}
			
			table.add(getSmallHeader(String.valueOf(a)), 2, a);
			table.setVerticalAlignment(2, a, Table.VERTICAL_ALIGN_BOTTOM);
			table.add(school, 3, a);
			table.add(course, 4, a);
			
			try {
				/*RemoteScriptHandler rsh = new RemoteScriptHandler(studyPaths, school);
				rsh.setRemoteScriptCollectionClass(SchoolCollectionHandler.class);
				rsh.setToClear(course, localize("select_course", "Select course"));
				add(rsh);*/

				RemoteScriptHandler rsh = new RemoteScriptHandler(school, course);
				rsh.setRemoteScriptCollectionClass(CourseCollectionHandler.class);
				add(rsh);
			}
			catch (IllegalAccessException iae) {
				throw new IBORuntimeException(iae);
			}
			catch (InstantiationException ie) {
				throw new IBORuntimeException(ie);
			}
		}
		
		TextArea area = (TextArea) getStyledInterface(new TextArea(PARAMETER_COMMENT));
		area.setWidth(Table.HUNDRED_PERCENT);
		area.setRows(5);
		if (isUpdate && iChoice.getComment() != null) {
			area.setContent(iChoice.getComment());
		}
		table.add(new Break(), 1, 4);
		table.add(getSmallHeader(localize("comment", "Comment") + ":"), 1, 4);
		table.add(new Break(), 1, 4);
		table.add(area, 1, 4);
		table.add(new Break(2), 1, 4);
		
		Table reasonTable = new Table();
		reasonTable.setColumns(2);
		reasonTable.setWidth(Table.HUNDRED_PERCENT);
		
		reasonTable.add(getSmallHeader(localize("select_reasons", "Select reasons")), 1, 1);
		reasonTable.setHeight(2, 12);
		
		Collection reasons = getBusiness().getActiveReasons();
		Collection selectedReasons = new ArrayList();
		if (isUpdate) {
			try {
				selectedReasons = iChoice.getReasons();
			}
			catch (IDORelationshipException ire) {
				ire.printStackTrace();
				selectedReasons = new ArrayList();
			}
		}
		int reasonCount = reasons.size();
		int divider = reasonCount / 2;
		int column = 1;
		int row = 3;
		Iterator iterator = reasons.iterator();
		while (iterator.hasNext()) {
			if (divider + 2 < row && column != 2) {
				column = 2;
				row = 3;
			}
			
			AdultEducationChoiceReason reason = (AdultEducationChoiceReason) iterator.next();
			
			CheckBox reasonBox = getCheckBox(PARAMETER_REASON, reason.getPrimaryKey().toString());
			reasonBox.setChecked(selectedReasons.contains(reason));
			
			reasonTable.add(reasonBox, column, row);
			reasonTable.add(Text.getNonBrakingSpace(), column, row);
			reasonTable.add(getSmallText(localize(reason.getLocalizedKey(), reason.getName())), column, row++);
		}
		
		row = reasonTable.getRows() + 2;
		reasonTable.setHeight(row - 1, 12);
		reasonTable.mergeCells(1, row, 2, row);
		
		TextArea otherReason = (TextArea) getStyledInterface(new TextArea(PARAMETER_OTHER_REASON));
		otherReason.setWidth(Table.HUNDRED_PERCENT);
		otherReason.setRows(5);
		if (isUpdate && iChoice.getOtherReason() != null) {
			otherReason.setContent(iChoice.getOtherReason());
		}
		reasonTable.add(getSmallHeader(localize("other_reason", "Other reason") + ":"), 1, row);
		reasonTable.add(new Break(), 1, row);
		reasonTable.add(otherReason, 1, row);
		
		table.add(reasonTable, 1, 5);
		table.setCellpadding(1, 5, 10);
		table.setCellBorder(1, 5, 1, "#999999", "solid");
		
		return table;
	}
	
	private void store(IWContext iwc) throws RemoteException {
		Collection coursePKs = new ArrayList();
		for (int i = 0; i < 3; i++) {
			if (iwc.isParameterSet(PARAMETER_COURSE + "_" + (i+1))) {
				String coursePK = iwc.getParameter(PARAMETER_COURSE + "_" + (i+1));
				if (!coursePKs.equals("-1")) {
					coursePKs.add(coursePK);
				}
			}
		}
		String[] reasons = iwc.getParameterValues(PARAMETER_REASON);
		String comment = iwc.isParameterSet(PARAMETER_COMMENT) ? iwc.getParameter(PARAMETER_COMMENT) : null;
		String otherReason = iwc.isParameterSet(PARAMETER_OTHER_REASON) ? iwc.getParameter(PARAMETER_OTHER_REASON) : null;
		String[] oldCourses = iwc.getParameterValues(PARAMETER_OLD_COURSES);
		
		try {
			getBusiness().storeChoices(iwc.getCurrentUser(), coursePKs, oldCourses, comment, reasons, otherReason);
			if (getResponsePage() != null) {
				iwc.forwardToIBPage(getParentPage(), getResponsePage());
			}
			else {
				add(getSmallHeader(localize("choice_stored", "Choice stored.")));
			}
		}
		catch (IDOCreateException ice) {
			ice.printStackTrace();
			add(getSmallErrorText(localize("choice_store_failed", "Choice store failed.")));
			showApplication(iwc);
		}
	}

	private String getSubmitCheckScript() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\nfunction checkApplication(){\n\t");
		buffer.append("\n\t var dropOne = ").append("findObj('").append(PARAMETER_COURSE + "_1").append("');");
		buffer.append("\n\t var dropTwo = ").append("findObj('").append(PARAMETER_COURSE + "_2").append("');");
		buffer.append("\n\t var dropThree = ").append("findObj('").append(PARAMETER_COURSE + "_3").append("');");
		buffer.append("\n\t var dropSchoolOne = ").append("findObj('").append(PARAMETER_SCHOOL + "_1").append("');");
		buffer.append("\n\t var dropSchoolTwo = ").append("findObj('").append(PARAMETER_SCHOOL + "_2").append("');");
		buffer.append("\n\t var dropSchoolThree = ").append("findObj('").append(PARAMETER_SCHOOL + "_3").append("');");
		buffer.append("\n\t var reasons = ").append("findObj('").append(PARAMETER_REASON).append("');");
		buffer.append("\n\t var otherReason = ").append("findObj('").append(PARAMETER_OTHER_REASON).append("');");
		buffer.append("\n\t findObj('").append(PARAMETER_ACTION).append("').value = " + ACTION_APPLICATION + ";");

		buffer.append("\n\t var one = 0;");
		buffer.append("\n\t var two = 0;");
		buffer.append("\n\t var three = 0;");
		buffer.append("\n\t var schoolOne = 0;");
		buffer.append("\n\t var schoolTwo = 0;");
		buffer.append("\n\t var schoolThree = 0;");
		buffer.append("\n\t var length = 0;");

		buffer.append("\n\n\t if (dropOne.selectedIndex > 0) {\n\t\t one = dropOne.options[dropOne.selectedIndex].value;\n\t\t length++;\n\t }");
		buffer.append("\n\t if (dropTwo.selectedIndex > 0) {\n\t\t two = dropTwo.options[dropTwo.selectedIndex].value;\n\t\t length++;\n\t }");
		buffer.append("\n\t if (dropThree.selectedIndex > 0) {\n\t\t three = dropThree.options[dropThree.selectedIndex].value;\n\t\t length++;\n\t }");
		buffer.append("\n\t if (dropSchoolOne.selectedIndex > 0) {\n\t\t schoolOne = dropSchoolOne.options[dropSchoolOne.selectedIndex].value;\n\t }");
		buffer.append("\n\t if (dropSchoolTwo.selectedIndex > 0) {\n\t\t schoolTwo = dropSchoolTwo.options[dropSchoolTwo.selectedIndex].value;\n\t }");
		buffer.append("\n\t if (dropSchoolThree.selectedIndex > 0) {\n\t\t schoolThree = dropSchoolThree.options[dropSchoolThree.selectedIndex].value;\n\t }");
		
		buffer.append("\n\t if(one > 0 && (one == two || one == three)){");
		String message = localize("must_not_be_the_same", "Please do not choose the same course more than once.");
		buffer.append("\n\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t return false;");
		buffer.append("\n\t }");
		buffer.append("\n\t else if(one <= 0 && schoolOne > 0){");
		message = localize("school_chosen_but_no_course", "Please choose a course together with a provider.");
		buffer.append("\n\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t return false;");
		buffer.append("\n\t }");
		buffer.append("\n\t if(two > 0 && (two == one || two == three)){");
		message = localize("child_care.must_not_be_the_same", "Please do not choose the same course more than once.");
		buffer.append("\n\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t return false;");
		buffer.append("\n\t }");
		buffer.append("\n\t else if(two <= 0 && schoolTwo > 0){");
		message = localize("school_chosen_but_no_course", "Please choose a course together with a provider.");
		buffer.append("\n\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t return false;");
		buffer.append("\n\t }");
		buffer.append("\n\t if(three > 0 && (three == one || three == two)){");
		message = localize("child_care.must_not_be_the_same", "Please do not choose the same course more than once.");
		buffer.append("\n\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t return false;");
		buffer.append("\n\t }");
		buffer.append("\n\t else if(three <= 0 && schoolThree > 0){");
		message = localize("school_chosen_but_no_course", "Please choose a course together with a provider.");
		buffer.append("\n\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t return false;");
		buffer.append("\n\t }");
		buffer.append("\n\t if (length == 0) {");
		message = localize("child_care.must_fill_out_one", "Please fill out the first choice.");
		buffer.append("\n\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t return false;");
		buffer.append("\n\t }");

		buffer.append("\n\t var reasonChecked = false;");
		buffer.append("\n\t for (var a = 0; a < reasons.length; a++) {");
		buffer.append("\n\t\t if (reasons[a].checked == true) reasonChecked = true;");
		buffer.append("\n\t }");
		buffer.append("\n\t if (!reasonChecked && otherReason.value.length == 0) {");
		message = localize("must_choose_reason", "You have to provide a reason.");
		buffer.append("\n\t\t alert('").append(message).append("');");
		buffer.append("\n\t\t return false;");
		buffer.append("\n\t }");

		buffer.append("\n\t var submit = true;");
		message = localize("less_than_three_chosen", "You have chosen less than three course.  A placement can not be guaranteed.");
		buffer.append("\n\t if(length < 3)\n\t\t submit = confirm('").append(message).append("');");
		buffer.append("\n\t if (submit) findObj('").append(PARAMETER_ACTION).append("').value = " + ACTION_STORE + ";");
		buffer.append("\n\t return submit;");
		buffer.append("\n}\n");
		return buffer.toString();
	}

	private int parseAction(IWContext iwc) {
		try {
			if (iwc.isParameterSet(PARAMETER_STUDY_PATH)) {
				iStudyPathPK = iwc.getParameter(PARAMETER_STUDY_PATH);
			}
			if (iwc.isParameterSet(PARAMETER_CHOICE)) {
				isUpdate = true;
				
				try {
					iChoice = getBusiness().getChoice(iwc.getParameter(PARAMETER_CHOICE));
					AdultEducationCourse course = iChoice.getCourse();
					SchoolStudyPath path = course.getStudyPath();
					
					iSchoolTypePK = new Integer(path.getSchoolTypeId());
					iSchoolSeason = course.getSchoolSeason();
					iSchoolSeasonPK = iSchoolSeason.getPrimaryKey();
					iStudyPathGroupPK = new Integer(path.getStudyPathGroupID());
				}
				catch (FinderException fe) {
					fe.printStackTrace();
				}
			}
			if (iwc.isParameterSet(PARAMETER_SCHOOL_TYPE)) {
				iSchoolTypePK = iwc.getParameter(PARAMETER_SCHOOL_TYPE);
			}
			if (iwc.isParameterSet(PARAMETER_SCHOOL_SEASON)) {
				iSchoolSeasonPK = iwc.getParameter(PARAMETER_SCHOOL_SEASON);
			}
			if (iwc.isParameterSet(PARAMETER_STUDY_PATH_GROUP)) {
				iStudyPathGroupPK = iwc.getParameter(PARAMETER_STUDY_PATH_GROUP);
			}
			
			if (iSchoolTypePK != null) {
				iSchoolType = getBusiness().getSchoolBusiness().getSchoolType(iSchoolTypePK);
			}
			if (iSchoolSeasonPK != null) {
				iSchoolSeason = getBusiness().getSchoolBusiness().getSchoolSeason(iSchoolSeasonPK);
			}
			if (iStudyPathGroupPK != null) {
				iStudyPathGroup = getBusiness().getStudyPathBusiness().findStudyPathGroup(iStudyPathGroupPK);
			}
		}
		catch (RemoteException re) {
			re.printStackTrace();
		}

		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		return ACTION_APPLICATION;
	}
}