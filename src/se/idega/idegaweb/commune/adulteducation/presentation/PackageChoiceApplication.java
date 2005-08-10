/*
 * $Id: PackageChoiceApplication.java,v 1.3 2005/08/10 00:19:27 laddi Exp $
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
import se.idega.idegaweb.commune.adulteducation.business.PackageCollectionHandler;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceReason;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolSeason;
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
 * Last modified: $Date: 2005/08/10 00:19:27 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3 $
 */
public class PackageChoiceApplication extends AdultEducationBlock {

	private static final String PARAMETER_ACTION = "ce_action";

	private static final String PARAMETER_SCHOOL = "ce_school";
	private static final String PARAMETER_COMMENT = "ce_comment";
	private static final String PARAMETER_REASON = "ce_reason";
	private static final String PARAMETER_OTHER_REASON = "ce_other_reason";

	private static final int ACTION_APPLICATION = 1;
	private static final int ACTION_STORE = 2;
	
	private Object iSchoolSeasonPK;
	private SchoolSeason iSchoolSeason;

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.adulteducation.presentation.AdultEducationBlock#present(com.idega.presentation.IWContext)
	 */
	public void present(IWContext iwc) {
		try {
			switch (parseAction(iwc)) {
				case ACTION_APPLICATION:
					showApplication();
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
	
	private void showApplication() throws RemoteException {
		Form form = new Form();

		Table table = new Table(1, 5);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(2, 20);
		table.setHeight(4, 20);
		table.setAlignment(1, 5, Table.HORIZONTAL_ALIGN_RIGHT);
		form.add(table);
		
		SelectorUtility util = new SelectorUtility();
		
		DropdownMenu seasons = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_SCHOOL_SEASON), getBusiness().getSeasons(), "getSeasonName"));
		seasons.addMenuElementFirst("", localize("select_season", "Select season"));
		if (iSchoolSeasonPK != null) {
			seasons.setSelectedElement(iSchoolSeasonPK.toString());
		}
		seasons.setToSubmit();

		table.add(getSmallHeader(localize("season", "Season") + ":"), 1, 1);
		table.add(new Break(), 1, 1);
		table.add(seasons, 1, 1);
		
		table.add(getApplicationTable(), 1, 3);
		
		SubmitButton submit = (SubmitButton) getButton(new SubmitButton(localize("submit", "Submit"), PARAMETER_ACTION, String.valueOf(ACTION_STORE)));
		table.add(submit, 1, 5);
		form.setToDisableOnSubmit(submit, true);

		add(form);
	}
	
	private Table getApplicationTable() throws RemoteException {
		Table table = new Table(1, 5);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(2, 20);
		table.setHeight(4, 20);
		
		Table schoolTable = new Table(3, 3);
		schoolTable.setCellpadding(3);
		schoolTable.setCellspacing(0);
		table.add(schoolTable, 1, 1);
		
		Collection schools = getBusiness().getSchools();

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
			
			DropdownMenu coursePackages = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_COURSE_PACKAGE + "_" + a));
			coursePackages.addMenuElementFirst("", localize("select_package", "Select package"));
			
			RemoteScriptHandler rsh = new RemoteScriptHandler(school, coursePackages);
			try {
				rsh.setRemoteScriptCollectionClass(PackageCollectionHandler.class);
			}
			catch (InstantiationException e) {
				e.printStackTrace();
			}
			catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			add(rsh);

			if (a == 1) {
				schoolTable.add(getSmallHeader(localize("school", "School")), 2, 1);
				schoolTable.add(new Break(), 2, 1);
				schoolTable.add(getSmallHeader(localize("course_code", "Course code")), 3, 1);
				schoolTable.add(new Break(), 3, 1);
			}
			
			schoolTable.add(getSmallHeader(String.valueOf(a)), 1, a);
			schoolTable.setVerticalAlignment(1, a, Table.VERTICAL_ALIGN_BOTTOM);
			schoolTable.add(school, 2, a);
			schoolTable.add(coursePackages, 3, a);
		}
		
		TextArea area = (TextArea) getStyledInterface(new TextArea(PARAMETER_COMMENT));
		area.setWidth(Table.HUNDRED_PERCENT);
		area.setRows(5);
		table.add(getSmallHeader(localize("comment", "Comment") + ":"), 1, 3);
		table.add(new Break(), 1, 3);
		table.add(area, 1, 3);
		
		Table reasonTable = new Table();
		reasonTable.setColumns(2);
		reasonTable.setWidth(Table.HUNDRED_PERCENT);
		
		reasonTable.add(getSmallHeader(localize("select_reasons", "Select reasons")), 1, 1);
		reasonTable.setHeight(2, 12);
		
		Collection reasons = getBusiness().getActiveReasons();
		Collection selectedReasons = new ArrayList();

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
		reasonTable.add(getSmallHeader(localize("other_reason", "Other reason") + ":"), 1, row);
		reasonTable.add(new Break(), 1, row);
		reasonTable.add(otherReason, 1, row);
		
		table.add(reasonTable, 1, 5);
		table.setCellpadding(1, 5, 10);
		table.setCellBorder(1, 5, 1, "#999999", "solid");
		
		return table;
	}
	
	private void store(IWContext iwc) throws RemoteException {
		Collection packagePKs = new ArrayList();
		for (int i = 0; i < 3; i++) {
			if (iwc.isParameterSet(PARAMETER_COURSE_PACKAGE + "_" + (i+1))) {
				String packagePK = iwc.getParameter(PARAMETER_COURSE_PACKAGE + "_" + (i+1));
				if (!packagePK.equals("-1")) {
					packagePKs.add(packagePK);
				}
			}
		}
		String[] reasons = iwc.getParameterValues(PARAMETER_REASON);
		String comment = iwc.isParameterSet(PARAMETER_COMMENT) ? iwc.getParameter(PARAMETER_COMMENT) : null;
		String otherReason = iwc.isParameterSet(PARAMETER_OTHER_REASON) ? iwc.getParameter(PARAMETER_OTHER_REASON) : null;
		
		try {
			getBusiness().storeChoices(iwc.getCurrentUser(), this.iSchoolSeason, packagePKs, comment, reasons, otherReason);
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
			showApplication();
		}
	}

	private int parseAction(IWContext iwc) {
		try {
			if (iwc.isParameterSet(PARAMETER_SCHOOL_SEASON)) {
				iSchoolSeasonPK = iwc.getParameter(PARAMETER_SCHOOL_SEASON);
			}

			if (iSchoolSeasonPK != null) {
				iSchoolSeason = getBusiness().getSchoolBusiness().getSchoolSeason(iSchoolSeasonPK);
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