package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.block.school.data.SchoolYear;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author <br>
 *         <a href="mailto:aron@idega.is">Aron Birkir </a> <br>
 * @version 1.0
 */

public class SchoolYearEditor extends SchoolBlock {

	protected void init(IWContext iwc) throws Exception {
		Form F = new Form();

		if (iwc.isParameterSet("sch_save_year")) {
			saveYear(iwc);
			F.add(getListTable(iwc, null));
		}
		else if (iwc.isParameterSet("sch_delete_year")) {
			int id = Integer.parseInt(iwc.getParameter("sch_delete_year"));
			getBusiness().removeSchoolYear(id);
			F.add(getListTable(iwc, null));
		}
		else if (iwc.isParameterSet("sch_school_year_id")) {
			int id = Integer.parseInt(iwc.getParameter("sch_school_year_id"));
			F.add(getInput(iwc, id));

		}
		else if (iwc.isParameterSet("sch_new_area")) {
			F.add(getInput(iwc, -1));
		}
		else
			F.add(getListTable(iwc, null));

		add(F);

	}

	private PresentationObject getInput(IWContext iwc, int id) throws java.rmi.RemoteException {
		return getInputTable(iwc, getBusiness().getSchoolYear(new Integer(id)));
	}

	private void saveYear(IWContext iwc) throws java.rmi.RemoteException {
		if (iwc.isParameterSet("sch_save_year")) {
			String id = iwc.getParameter("sch_school_year_id");
			String name = iwc.getParameter("sch_year_name");
			String type = iwc.getParameter("sch_type");
			String age = iwc.getParameter("sch_year_age");
			String info = iwc.getParameter("sch_year_info");
			String localizedKey = iwc.getParameter("localized_key");
			String category = null;
			if (iwc.isParameterSet("sch_category")) {
				category = iwc.getParameter("sch_category");
			}
			int iAge = -1, sid = -1, stid = -1;
			if (id != null) sid = Integer.parseInt(id);
			if (age != null && age.length() > 0) iAge = Integer.parseInt(age);
			if (type != null) stid = Integer.parseInt(type);
			getBusiness().storeSchoolYear(sid, name, stid, category, info, localizedKey, iAge);
		}
	}

	public PresentationObject getListTable(IWContext iwc, SchoolYear area) {
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setColumns(5);
		table.setWidth(5, 12);
		int row = 1;
		int col = 1;

		Collection years = null;
		try {
			years = getBusiness().findAllSchoolYears();
		}
		catch (RemoteException rex) {
			years = new ArrayList();
		}

		table.setCellpaddingLeft(1, row, 12);
		table.add(getSmallHeader(localize("name", "Name")), col++, row);
		table.add(getSmallHeader(localize("school_type", "Type")), col++, row);
		table.add(getSmallHeader(localize("info", "Info")), col++, row);
		table.add(getSmallHeader(localize("age", "Age")), col++, row);
		table.setRowStyleClass(row++, getHeaderRowClass());

		Iterator iter = years.iterator();
		SchoolYear sYear;
		SchoolType sType;
		while (iter.hasNext()) {
			col = 1;
			sYear = (SchoolYear) iter.next();
			sType = sYear.getSchoolType();

			Link L = new Link(getEditIcon(localize("edit", "Edit")));
			L.addParameter("sch_school_year_id", ((Integer) sYear.getPrimaryKey()).intValue());

			table.setCellpaddingLeft(1, row, 12);
			table.setCellpaddingRight(table.getColumns(), row, 12);
			table.add(getSmallText(sYear.getSchoolYearName()), col++, row);
			if (sType != null) {
				table.add(getSmallText(localize(sType.getLocalizationKey(), sType.getName())), col++, row);
			}
			else {
				table.add(getSmallText("-"), col++, row);
			}
			table.add(getSmallText(sYear.getSchoolYearInfo()), col++, row);
			table.add(getSmallText(String.valueOf(sYear.getSchoolYearAge())), col++, row);
			table.add(L, col++, row);

			if (row % 2 == 0) {
				table.setRowStyleClass(row, getDarkRowClass());
			}
			else {
				table.setRowStyleClass(row, getLightRowClass());
			}
			row++;
		}

		table.setHeight(row++, 12);
		table.setCellpaddingLeft(1, row, 12);
		table.mergeCells(1, row, table.getColumns(), row);
		GenericButton newLink = getButton(new GenericButton("new", localize("year.new", "New year")));
		newLink.setPageToOpen(iwc.getCurrentIBPageID());
		newLink.addParameterToPage("sch_new_area", "true");
		table.add(newLink, 1, row);

		return table;
	}

	public PresentationObject getInputTable(IWContext iwc, SchoolYear entity) {
		Table T = new Table(3, 8);
		T.mergeCells(1, 1, 3, 1);
		T.add(getHeader(localize("school_year", "SchoolYear")), 1, 1);
		T.add(getHeader(localize("name", "Name")), 1, 2);
		T.add(getHeader(localize("school_type", "Type")), 1, 3);
		T.add(getHeader(localize("info", "Info")), 1, 4);
		T.add(getHeader(localize("age", "Age")), 1, 5);
		T.add(getHeader(localize("localized_key", "Localized key")), 1, 6);
		T.add(getHeader(localize("category", "Category")), 1, 7);

		TextInput inputName = (TextInput) getStyledInterface(new TextInput("sch_year_name"));
		TextInput inputAge = (TextInput) getStyledInterface(new TextInput("sch_year_age"));
		DropdownMenu inputType = (DropdownMenu) getStyledInterface(new DropdownMenu("sch_type"));
		TextArea inputInfo = (TextArea) getStyledInterface(new TextArea("sch_year_info"));
		TextInput localizedKey = (TextInput) getStyledInterface(new TextInput("localized_key"));
		DropdownMenu inputCategory = (DropdownMenu) getStyledInterface(new DropdownMenu("sch_category"));

		try {
			SchoolTypeHome stHome = (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
			Collection coll = stHome.findAllSchoolTypes();
			if (coll != null && !coll.isEmpty()) {
				inputType.addMenuElements(coll);
			}
			inputType.addMenuElementFirst("-1", "");
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			SchoolCategoryHome scHome = (SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class);
			Collection coll = scHome.findAllCategories();
			Iterator iter = coll.iterator();
			while (iter.hasNext()) {
				SchoolCategory category = (SchoolCategory) iter.next();
				inputCategory.addMenuElement(category.getCategory(), localize(category.getLocalizedKey(), category.getCategory()));
			}
			inputCategory.addMenuElementFirst("", "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		int beanId = -1;
		if (entity != null) {
			try {
				beanId = ((Integer) entity.getPrimaryKey()).intValue();
				inputName.setContent(entity.getSchoolYearName());
				inputInfo.setContent(entity.getSchoolYearInfo());
				inputAge.setContent(String.valueOf(entity.getSchoolYearAge()));
				inputType.setSelectedElement(entity.getSchoolTypeId());
				if (entity.getLocalizedKey() != null) {
					localizedKey.setContent(entity.getLocalizedKey());
				}
				if (entity.getSchoolCategory() != null) {
					inputCategory.setSelectedElement(entity.getSchoolCategoryPK().toString());
				}
				T.add(new HiddenInput("sch_school_year_id", String.valueOf(beanId)));
			}
			catch (Exception ex) {
			}
		}

		T.add(inputName, 3, 2);
		T.add(inputType, 3, 3);
		T.add(inputInfo, 3, 4);
		T.add(inputAge, 3, 5);
		T.add(localizedKey, 3, 6);
		T.add(inputCategory, 3, 7);
		T.add(getButton(new SubmitButton(localize("save", "Save"), "sch_save_year", "true")), 3, 8);
		GenericButton cancel = getButton(new GenericButton("cancel", localize("cancel", "Cancel")));
		cancel.setPageToOpen(iwc.getCurrentIBPageID());
		T.add(cancel, 3, 8);
		if (beanId > 0) {
			GenericButton delete = getButton(new GenericButton("delete", localize("delete", "Delete")));
			delete.setPageToOpen(iwc.getCurrentIBPageID());
			delete.addParameterToPage("sch_delete_year", beanId);
			T.add(delete, 3, 8);
		}

		return T;
	}
}