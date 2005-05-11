/*
 * $Id: ChoiceApplication.java,v 1.1 2005/05/11 13:14:12 laddi Exp $
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
import se.idega.idegaweb.commune.adulteducation.business.CourseCollectionHandler;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceReason;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolStudyPathGroup;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOCreateException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.remotescripting.RemoteScriptHandler;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.util.SelectorUtility;


/**
 * Last modified: $Date: 2005/05/11 13:14:12 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
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

	private static final int ACTION_APPLICATION = 1;
	private static final int ACTION_STORE = 2;
	
	private Object iSchoolTypePK;
	private SchoolType iSchoolType;
	private Object iSchoolSeasonPK;
	private SchoolSeason iSchoolSeason;
	private Object iStudyPathGroupPK;
	private SchoolStudyPathGroup iStudyPathGroup;
	private Object iStudyPathPK;

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

		form.add(getNavigationTable());
		form.add(new Break(2));
		form.add(getApplicationTable(iwc));

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
		Table table = new Table(3, 6);
		table.setCellpadding(3);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.mergeCells(1, 4, 3, 4);
		table.mergeCells(1, 5, 3, 5);
		table.mergeCells(1, 6, 3, 6);
		
		Collection paths = getBusiness().getStudyPaths(iSchoolType, iStudyPathGroup);
		paths.removeAll(getBusiness().getSelectedStudyPaths(iwc.getCurrentUser(), iSchoolSeason));
		
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
		
		table.add(getSmallHeader(localize("study_path", "Study path")), 1, 1);
		table.add(new Break(), 1, 1);
		table.add(studyPaths, 1, 1);
		
		Collection schools = null;
		if (iStudyPathPK != null && iSchoolSeasonPK != null) {
			schools = getBusiness().getAvailableSchools(iStudyPathPK, iSchoolSeasonPK);
		}

		for (int a = 1; a <= 3; a++) {
			DropdownMenu school = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_SCHOOL + "_" + a));
			school.addMenuElementFirst("", localize("select_school", "Select school"));
			if (schools != null) {
				Iterator iterator = schools.iterator();
				while (iterator.hasNext()) {
					School element = (School) iterator.next();
					school.addMenuElement(element.getPrimaryKey().toString(), element.getSchoolName());
				}
			}
			DropdownMenu course = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_COURSE + "_" + a));
			course.addMenuElementFirst("", localize("select_course", "Select course"));

			if (a == 1) {
				table.add(getSmallHeader(localize("school", "School")), 2, 1);
				table.add(new Break(), 2, 1);
				table.add(getSmallHeader(localize("course_code", "Course code")), 3, 1);
				table.add(new Break(), 3, 1);
			}
			
			table.add(school, 2, a);
			table.add(course, 3, a);
			
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
		reasonTable.add(getSmallHeader(localize("other_reason", "Other reason") + ":"), 1, row);
		reasonTable.add(new Break(), 1, row);
		reasonTable.add(otherReason, 1, row);
		
		table.add(reasonTable, 1, 5);
		table.setCellpadding(1, 5, 10);
		table.setCellBorder(1, 5, 1, "#999999", "solid");
		
		SubmitButton submit = (SubmitButton) getButton(new SubmitButton(localize("submit", "Submit"), PARAMETER_ACTION, String.valueOf(ACTION_STORE)));
		table.add(new Break(), 1, 6);
		table.add(submit, 1, 6);

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
		
		try {
			getBusiness().storeChoices(iwc.getCurrentUser(), coursePKs, comment, reasons, otherReason);
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

	private int parseAction(IWContext iwc) {
		try {
			if (iwc.isParameterSet(PARAMETER_SCHOOL_TYPE)) {
				iSchoolTypePK = iwc.getParameter(PARAMETER_SCHOOL_TYPE);
				iSchoolType = getBusiness().getSchoolBusiness().getSchoolType(iSchoolTypePK);
			}
			if (iwc.isParameterSet(PARAMETER_SCHOOL_SEASON)) {
				iSchoolSeasonPK = iwc.getParameter(PARAMETER_SCHOOL_SEASON);
				iSchoolSeason = getBusiness().getSchoolBusiness().getSchoolSeason(iSchoolSeasonPK);
			}
			if (iwc.isParameterSet(PARAMETER_STUDY_PATH_GROUP)) {
				iStudyPathGroupPK = iwc.getParameter(PARAMETER_STUDY_PATH_GROUP);
				iStudyPathGroup = getBusiness().getStudyPathBusiness().findStudyPathGroup(iStudyPathGroupPK);
			}
			if (iwc.isParameterSet(PARAMETER_STUDY_PATH)) {
				iStudyPathPK = iwc.getParameter(PARAMETER_STUDY_PATH);
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