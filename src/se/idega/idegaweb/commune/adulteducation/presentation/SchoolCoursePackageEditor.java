/*
 * $Id: SchoolCoursePackageEditor.java,v 1.9 2005/08/08 23:27:30 laddi Exp $
 * Created on Jul 6, 2005
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
import javax.ejb.RemoveException;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourse;
import se.idega.idegaweb.commune.adulteducation.data.CoursePackage;
import se.idega.idegaweb.commune.adulteducation.data.SchoolCoursePackage;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
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


/**
 * Last modified: $Date: 2005/08/08 23:27:30 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.9 $
 */
public class SchoolCoursePackageEditor extends AdultEducationBlock implements IWPageEventListener {

	private static final String PARAMETER_ACTION = "prm_action";
	private static final String PARAMETER_SEASON = "prm_season";
	private static final String PARAMETER_SCHOOL = "prm_school";
	private static final String PARAMETER_STUDY_PATH_GROUP = "prm_study_path_group";
	private static final String PARAMETER_COURSE_SEASON = "prm_course_season";
	private static final String PARAMETER_FREE_TEXT = "prm_free_text";
	private static final String PARAMETER_SCHOOL_COURSE_PACKAGE = "prm_school_course_package";
	private static final String PARAMETER_COURSE = "prm_course";
	
	private static final int ACTION_SEARCH = 0;
	private static final int ACTION_VIEW = 1;
	private static final int ACTION_STORE = 2;
	private static final int ACTION_REMOVE_COURSE = 3;
	private static final int ACTION_REMOVE = 4;
	private static final int ACTION_ACTIVATE = 5;
	private static final int ACTION_NEW = 6;
	
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.adulteducation.presentation.AdultEducationBlock#present(com.idega.presentation.IWContext)
	 */
	public void present(IWContext iwc) {
		try {
			switch (parseAction(iwc)) {
				case ACTION_SEARCH:
					showSearch();
					break;
					
				case ACTION_VIEW:
					showForm();
					break;
					
				case ACTION_STORE:
					try {
						if (!storePackage(iwc)) {
							getParentPage().setAlertOnLoad(localize("must_fill_in_standard_information", "You have to select a school, a season and a course package"));
						}
					}
					catch (CreateException ce) {
						ce.printStackTrace();
						getParentPage().setAlertOnLoad(localize("school_package_store_failed", "There was an error storing the school package..."));
					}
					showForm();
					break;
					
				case ACTION_REMOVE_COURSE:
					removeCourse(iwc);
					showForm();
					break;
					
				case ACTION_REMOVE:
					removePackage();
					showSearch();
					break;
					
				case ACTION_ACTIVATE:
					activatePackage();
					showForm();
					break;
					
				case ACTION_NEW:
					getSession().setSchoolCoursePackage(null);
					showForm();
					break;
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	private void showSearch() throws RemoteException {
		Form form = new Form();
		form.setEventListener(SchoolCoursePackageEditor.class);
		form.addParameter(PARAMETER_ACTION, ACTION_SEARCH);

		form.add(getNavigation(null, true));
		form.add(new Break());
		
		if (getSession().getChosenSchool() != null) {
			Collection packages = getBusiness().getSchoolCoursePackages(getSession().getChosenSchool(), getSession().getSchoolSeason(), getSession().getCoursePackage());
	
			Table table = new Table();
			table.setCellpadding(getCellpadding());
			table.setCellspacing(getCellspacing());
			table.setColumns(3);
			table.setWidth(Table.HUNDRED_PERCENT);
			table.setRowColor(1, getHeaderColor());
			int column = 1;
			int row = 1;
			
			table.add(getLocalizedSmallHeader("school_course_package", "School course package"), column++, row);
			table.add(getLocalizedSmallHeader("period", "Period"), column++, row);
			table.add(getLocalizedSmallHeader("activated", "Activated"), column++, row++);
			
			Iterator iter = packages.iterator();
			while (iter.hasNext()) {
				column = 1;
				SchoolCoursePackage schoolPackage = (SchoolCoursePackage) iter.next();
				CoursePackage coursePackage = schoolPackage.getPackage();
				SchoolSeason season = schoolPackage.getSeason();
				
				Link link = getSmallLink(coursePackage.getName() + schoolPackage.getFreeText() != null ? " - " + schoolPackage.getFreeText() : "");
				link.addParameter(PARAMETER_SCHOOL_COURSE_PACKAGE, schoolPackage.getPrimaryKey().toString());
				link.addParameter(PARAMETER_ACTION, ACTION_VIEW);
				
				table.add(link, column++, row);
				table.add(getSmallText(season.getSchoolSeasonName()), column++, row);
				table.add(getSmallText(schoolPackage.isActive() ? localize("yes", "Yes") : localize("no", "No")), column++, row++);
			}
			
			form.add(table);
			form.add(new Break());
			
		}

		SubmitButton newPackage = (SubmitButton) getButton(new SubmitButton(localize("create_new_package", "Create new package")));
		newPackage.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_NEW));
		form.add(newPackage);
		
		add(form);
	}
	
	private void showForm() throws RemoteException {
		Form form = new Form();
		form.setEventListener(SchoolCoursePackageEditor.class);
		form.addParameter(PARAMETER_ACTION, ACTION_VIEW);
		
		SchoolCoursePackage schoolPackage = getSession().getSchoolCoursePackage();
		
		form.add(getNavigation(schoolPackage, false));
		form.add(new Break());
		
		if (schoolPackage != null) {
			form.add(getText(localize("courses_connected_to_package", "Courses connected to package") + ": "));
			form.add(getHeader(schoolPackage.getPackage().getName()));
			if (schoolPackage.getFreeText() != null) {
				form.add(getHeader(" - " + schoolPackage.getFreeText()));
			}
			form.add(new Break());
			form.add(getConnectedCourses(schoolPackage));
			form.add(new Break());
			
			if (!schoolPackage.isActive()) {
				SubmitButton removePackage = (SubmitButton) getButton(new SubmitButton(localize("remove_package", "Remove package")));
				SubmitButton activatePackage = (SubmitButton) getButton(new SubmitButton(localize("activate_package", "Activate package")));
				form.add(removePackage);
				form.add(Text.getNonBrakingSpace());
				form.add(activatePackage);
				form.add(new Break(2));

				removePackage.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_REMOVE));
				removePackage.setSubmitConfirm(localize("remove_package_confirm", "Are you sure you want to remove this package?"));
				activatePackage.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_ACTIVATE));
				activatePackage.setSubmitConfirm(localize("activate_package_confirm", "Are you sure you want to activate this package?"));
			}
		}
		
		form.add(getHeader(localize("select_courses_to_connect_to_package", "Select courses to connect to package")));
		form.add(new Break());
		form.add(getCourseNavigation());
		form.add(new Break());
		form.add(getCourses(schoolPackage));
		form.add(new Break());
		
		SubmitButton cancel = (SubmitButton) getButton(new SubmitButton(localize("cancel_ready", "Cancel/Ready")));
		cancel.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_SEARCH));
		form.add(cancel);
		
		if ((getSession().getChosenSchool() != null && getSession().getSchoolSeason() != null && getSession().getCoursePackage() != null) || schoolPackage != null) {
			SubmitButton save = (SubmitButton) getButton(new SubmitButton(localize("store_package", "Store package")));
			save.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_STORE));
			form.add(save);
		}
		
		add(form);
	}
	
	private Table getNavigation(SchoolCoursePackage schoolPackage, boolean showSearch) throws RemoteException {
		Table table = new Table(4, 3);
		table.setCellpadding(3);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		
		SelectorUtility util = new SelectorUtility();
		
		DropdownMenu seasons = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_SEASON), getBusiness().getSeasons(), "getSeasonName"));
		if (getSession().getSchoolSeason() != null) {
			seasons.setSelectedElement(getSession().getSchoolSeason().getPrimaryKey().toString());
		}
		else if (schoolPackage != null) {
			seasons.setSelectedElement(schoolPackage.getSeasonPK().toString());
		}
		if (schoolPackage != null) {
			seasons.setDisabled(true);
		}

		DropdownMenu schools = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_SCHOOL), getBusiness().getSchools(), "getSchoolName"));
		schools.addMenuElementFirst("", localize("select_school", "Select school"));
		if (getSession().getChosenSchool() != null) {
			schools.setSelectedElement(getSession().getChosenSchool().getPrimaryKey().toString());
		}
		else if (schoolPackage != null) {
			schools.setSelectedElement(schoolPackage.getSchoolPK().toString());
		}
		if (schoolPackage != null) {
			schools.setDisabled(true);
		}

		DropdownMenu coursePackages = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_COURSE_PACKAGE), getBusiness().getCoursePackages(), "getName"));
		coursePackages.addMenuElementFirst("", localize("select_course_package", "Select course package"));
		if (getSession().getCoursePackage() != null) {
			coursePackages.setSelectedElement(getSession().getCoursePackage().getPrimaryKey().toString());
		}
		else if (schoolPackage != null) {
			coursePackages.setSelectedElement(schoolPackage.getPackagePK().toString());
		}
		if (schoolPackage != null) {
			coursePackages.setDisabled(true);
		}
		
		TextInput freeText = (TextInput) getStyledInterface(new TextInput(PARAMETER_FREE_TEXT));
		if (schoolPackage != null && schoolPackage.getFreeText() != null) {
			freeText.setContent(schoolPackage.getFreeText());
		}
		
		SubmitButton search = (SubmitButton) getButton(new SubmitButton(localize("search", "Search")));
		
		table.add(getHeader(localize("school", "School") + ":"), 1, 1);
		table.add(schools, 2, 1);
		table.add(getHeader(localize("course_package", "Course package") + ":"), 3, 1);
		table.add(coursePackages, 4, 1);
		table.add(getHeader(localize("school_season", "Season") + ":"), 1, 3);
		table.add(seasons, 2, 3);
		if (showSearch) {
			table.add(search, 3, 3);
		}
		else {
			table.add(getHeader(localize("package_name", "Package name") + ":"), 3, 3);
			table.add(freeText, 4, 3);
		}
		
		return table;
	}
	
	private Table getConnectedCourses(SchoolCoursePackage schoolPackage) {
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(5);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setRowColor(1, getHeaderColor());
		int column = 1;
		int row = 1;
		
		table.add(getLocalizedSmallHeader("nr", "Nr"), column++, row);
		table.add(getLocalizedSmallHeader("study_path", "Study path"), column++, row);
		table.add(getLocalizedSmallHeader("code", "Code"), column++, row);
		table.add(getLocalizedSmallHeader("period", "Period"), column++, row++);
		
		if (schoolPackage != null) {
			try {
				Collection courses = schoolPackage.getCourses();
				Iterator iter = courses.iterator();
				while (iter.hasNext()) {
					column = 1;
					AdultEducationCourse course = (AdultEducationCourse) iter.next();
					SchoolStudyPath path = course.getStudyPath();
					SchoolSeason season = course.getSchoolSeason();
					
					table.add(getSmallText(String.valueOf(row - 1)), column++, row);
					table.add(getSmallText(path.getDescription()), column++, row);
					table.add(getSmallText(course.getCode()), column++, row);
					table.add(getSmallText(season.getSchoolSeasonName()), column++, row);
					
					if (!schoolPackage.isActive()) {
						Link remove = new Link(getDeleteIcon(localize("remove_course_from_package", "Remove course from package")));
						remove.addParameter(PARAMETER_ACTION, ACTION_REMOVE_COURSE);
						remove.addParameter(PARAMETER_COURSE, course.getPrimaryKey().toString());
						table.add(remove, column++, row);
					}
					row++;
				}
			}
			catch (IDORelationshipException ire) {
				ire.printStackTrace();
			}
		}
		table.setWidth(1, 12);
		table.setWidth(5, 12);

		return table;
	}
	
	private Table getCourses(SchoolCoursePackage schoolPackage) throws RemoteException {
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(3);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setRowColor(1, getHeaderColor());
		int column = 1;
		int row = 1;
		
		table.add(getLocalizedSmallHeader("study_path", "Study path"), column++, row);
		table.add(getLocalizedSmallHeader("code", "Code"), column++, row++);
		
		if (getSession().getChosenSchool() != null && getSession().getCourseSeason() != null && getSession().getStudyPathGroup() != null) {
			Collection courses = getBusiness().getCourses(getSession().getCourseSeason().getPrimaryKey(), null, getSession().getChosenSchool().getPrimaryKey(), getSession().getStudyPathGroup().getPrimaryKey());
			if (schoolPackage != null) {
				try {
					Collection connectedCourses = schoolPackage.getCourses();
					courses.removeAll(connectedCourses);
				}
				catch (IDORelationshipException ire) {
					ire.printStackTrace();
				}
			}
			
			Iterator iter = courses.iterator();
			while (iter.hasNext()) {
				column = 1;
				AdultEducationCourse course = (AdultEducationCourse) iter.next();
				SchoolStudyPath path = course.getStudyPath();
				
				table.add(getSmallText(path.getDescription()), column++, row);
				table.add(getSmallText(course.getCode()), column++, row);
				table.add(getCheckBox(PARAMETER_COURSE, course.getPrimaryKey().toString()), column++, row++);
			}
		}
		table.setWidth(3, 12);

		return table;
	}
	
	private Table getCourseNavigation() throws RemoteException {
		Table table = new Table(4, 1);
		table.setCellpadding(3);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		
		SelectorUtility util = new SelectorUtility();
		
		DropdownMenu groups = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_STUDY_PATH_GROUP), getBusiness().getStudyPathsGroups(), "getLocalizationKey", getResourceBundle()));
		groups.addMenuElementFirst("", localize("select_study_path_group", "Select group"));
		if (getSession().getStudyPathGroup() != null) {
			groups.setSelectedElement(getSession().getStudyPathGroup().getPrimaryKey().toString());
		}
		groups.setToSubmit();
		
		DropdownMenu seasons = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_COURSE_SEASON));
		seasons.addMenuElementFirst("", localize("select_school_season", "Select season"));
		if (getSession().getSchoolSeason() != null) {
			Collection nextSeasons = getBusiness().getNextSeasons(getSession().getSchoolSeason());
			Iterator iter = nextSeasons.iterator();
			while (iter.hasNext()) {
				SchoolSeason season = (SchoolSeason) iter.next();
				seasons.addMenuElement(season.getPrimaryKey().toString(), season.getSchoolSeasonName());
			}
		}
		if (getSession().getCourseSeason() != null) {
			seasons.setSelectedElement(getSession().getCourseSeason().getPrimaryKey().toString());
		}
		seasons.setToSubmit();
		
		table.add(getHeader(localize("study_path_group", "Study path group") + ":"), 1, 1);
		table.add(groups, 2, 1);
		table.add(getHeader(localize("school_season", "Season") + ":"), 3, 1);
		table.add(seasons, 4, 1);

		return table;
	}
	
	private boolean storePackage(IWContext iwc) throws CreateException, RemoteException {
		if (getSession().getCoursePackage() != null && getSession().getChosenSchool() != null && getSession().getSchoolSeason() != null) {
			SchoolCoursePackage schoolPackage = getBusiness().storeSchoolPackage(getSession().getSchoolCoursePackage(), getSession().getCoursePackage(), getSession().getChosenSchool(), getSession().getSchoolSeason(), iwc.getParameter(PARAMETER_FREE_TEXT), iwc.getParameterValues(PARAMETER_COURSE));
			getSession().setSchoolCoursePackage(schoolPackage.getPrimaryKey());
			return true;
		}
		return false;
	}
	
	private void activatePackage() throws RemoteException {
		getBusiness().activatePackage(getSession().getSchoolCoursePackage());
	}
	
	private void removePackage() throws RemoteException {
		try {
			getBusiness().removeSchoolPackage(getSession().getSchoolCoursePackage());
			getSession().setSchoolCoursePackage(null);
		}
		catch (RemoveException re) {
			re.printStackTrace();
		}
	}
	
	private void removeCourse(IWContext iwc) throws RemoteException {
		getBusiness().removeCourseFromPackage(getSession().getSchoolCoursePackage(), iwc.getParameter(PARAMETER_COURSE));
	}

	private int parseAction(IWContext iwc) {
		int action = ACTION_SEARCH;
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		return action;
	}

	public boolean actionPerformed(IWContext iwc) throws IWException {
		boolean actionPerformed = false;
		if (iwc.isParameterSet(PARAMETER_SCHOOL)) {
			try {
				getSession(iwc).setChosenSchool(iwc.getParameter(PARAMETER_SCHOOL));
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
		if (iwc.isParameterSet(PARAMETER_COURSE_PACKAGE)) {
			try {
				getSession(iwc).setCoursePackage(iwc.getParameter(PARAMETER_COURSE_PACKAGE));
				actionPerformed = true;
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		if (iwc.isParameterSet(PARAMETER_SCHOOL_COURSE_PACKAGE)) {
			try {
				getSession(iwc).setSchoolCoursePackage(iwc.getParameter(PARAMETER_SCHOOL_COURSE_PACKAGE));
				actionPerformed = true;
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		if (iwc.isParameterSet(PARAMETER_COURSE_SEASON)) {
			try {
				getSession(iwc).setCourseSeason(iwc.getParameter(PARAMETER_COURSE_SEASON));
				actionPerformed = true;
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		if (iwc.isParameterSet(PARAMETER_SEASON)) {
			try {
				getSession(iwc).setSeason(iwc.getParameter(PARAMETER_SEASON));
				actionPerformed = true;
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		return actionPerformed;
	}
}