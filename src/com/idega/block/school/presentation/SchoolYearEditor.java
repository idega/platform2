package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.ejb.FinderException;
import com.idega.block.school.business.SchoolYearComparator;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.block.school.data.SchoolYear;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableColumn;
import com.idega.presentation.TableColumnGroup;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Label;
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
	
	private static final String PARAMETER_ACTION = "sch_year_prm_action";
	private static final String PARAMETER_SCHOOL_YEAR_PK = "prm_school_year_pk";
	private static final String PARAMETER_NAME = "prm_name";
	private static final String PARAMETER_INFO = "prm_info";
	private static final String PARAMETER_CATEGORY = "prm_category";
	private static final String PARAMETER_LOCALIZED_KEY = "prm_localized_key";
	private static final String PARAMETER_TYPE = "prm_type";
	private static final String PARAMETER_AGE = "prm_age";
	
	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;
	private static final int ACTION_NEW = 3;
	private static final int ACTION_SAVE = 4;
	private static final int ACTION_DELETE = 5;
	
	private String iSchoolCategory;

	protected void init(IWContext iwc) throws Exception {
		switch (parseAction(iwc)) {
			case ACTION_VIEW:
				showList(iwc);
				break;

			case ACTION_EDIT:
				Object schoolPK = iwc.getParameter(PARAMETER_SCHOOL_YEAR_PK);
				showEditor(iwc, schoolPK);
				break;

			case ACTION_NEW:
				showEditor(iwc, null);
				break;

			case ACTION_SAVE:
				saveYear(iwc);
				showList(iwc);
				break;

			case ACTION_DELETE:
				getBusiness().removeSchoolYear(iwc.getParameter(PARAMETER_SCHOOL_YEAR_PK));
				showList(iwc);
				break;
		}
	}

	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		return ACTION_VIEW;
	}

	private void saveYear(IWContext iwc) throws java.rmi.RemoteException {
		String id = iwc.getParameter(PARAMETER_SCHOOL_YEAR_PK);
		String name = iwc.getParameter(PARAMETER_NAME);
		String type = iwc.getParameter(PARAMETER_TYPE);
		String age = iwc.getParameter(PARAMETER_AGE);
		String info = iwc.getParameter(PARAMETER_INFO);
		String localizedKey = iwc.getParameter(PARAMETER_LOCALIZED_KEY);
		String category;
		if (iSchoolCategory != null) {
			category = iSchoolCategory;
		}
		else {
			category = iwc.getParameter(PARAMETER_CATEGORY);
		}
		int iAge = -1, sid = -1, stid = -1;
		if (id != null) sid = Integer.parseInt(id);
		if (age != null && age.length() > 0) iAge = Integer.parseInt(age);
		if (type != null) stid = Integer.parseInt(type);
		getBusiness().storeSchoolYear(sid, name, stid, category, info, localizedKey, iAge);
	}

	public void showList(IWContext iwc) {
		Form form = new Form();
		form.setStyleClass(STYLENAME_SCHOOL_FORM);
		
		Table2 table = new Table2();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth("100%");
		table.setStyleClass(STYLENAME_LIST_TABLE);

		TableColumnGroup columnGroup = table.createColumnGroup();
		TableColumn column = columnGroup.createColumn();
		column.setSpan(5);
		column = columnGroup.createColumn();
		column.setSpan(2);
		column.setWidth("12");

		List years = null;
		try {
			if (iSchoolCategory != null) {
				years = new ArrayList(getBusiness().findSchoolYearsBySchoolCategory(iSchoolCategory));
			}
			else {
				years = new ArrayList(getBusiness().findAllSchoolYears());
			}
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			years = new ArrayList();
		}
		catch (RemoteException rex) {
			years = new ArrayList();
		}
		Collections.sort(years, new SchoolYearComparator());

		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		TableCell2 cell = row.createHeaderCell();
		cell.setStyleClass("firstColumn");
		cell.add(new Text(localize("name", "Name")));
		row.createHeaderCell().add(new Text(localize("category", "Category")));
		row.createHeaderCell().add(new Text(localize("school_type", "Type")));
		row.createHeaderCell().add(new Text(localize("info", "Info")));
		row.createHeaderCell().add(new Text(localize("age", "Age")));
		row.createHeaderCell().add(Text.getNonBrakingSpace());
		cell = row.createHeaderCell();
		cell.setStyleClass("lastColumn");
		cell.add(Text.getNonBrakingSpace());

		group = table.createBodyRowGroup();
		int iRow = 1;
		Iterator iter = years.iterator();
		while (iter.hasNext()) {
			SchoolYear year = (SchoolYear) iter.next();
			SchoolType type = year.getSchoolType();
			SchoolCategory category = year.getSchoolCategory();
			row = group.createRow();

			Link edit = new Link(getEditIcon(localize("edit", "Edit")));
			edit.addParameter(PARAMETER_SCHOOL_YEAR_PK, year.getPrimaryKey().toString());
			edit.addParameter(PARAMETER_ACTION, ACTION_EDIT);
			
			Link delete = new Link(getDeleteIcon(localize("delete", "Delete")));
			delete.addParameter(PARAMETER_SCHOOL_YEAR_PK, year.getPrimaryKey().toString());
			delete.addParameter(PARAMETER_ACTION, ACTION_DELETE);

			cell = row.createCell();
			cell.setStyleClass("firstColumn");
			cell.add(new Text(year.getSchoolYearName()));
			row.createCell().add(new Text(category != null ? localize(category.getLocalizedKey(), category.getName()) : "-"));
			row.createCell().add(new Text(type != null ? localize(type.getLocalizationKey(), type.getName()) : "-"));
			row.createCell().add(new Text(year.getSchoolYearInfo() != null ? year.getSchoolYearInfo() : "-"));
			row.createCell().add(new Text(year.getSchoolYearAge() > 0 ? String.valueOf(year.getSchoolYearAge()) : "-"));
			row.createCell().add(edit);
			cell = row.createCell();
			cell.setStyleClass("lastColumn");
			cell.add(delete);

			if (iRow % 2 == 0) {
				row.setStyleClass(STYLENAME_LIST_TABLE_EVEN_ROW);
			}
			else {
				row.setStyleClass(STYLENAME_LIST_TABLE_ODD_ROW);
			}

			iRow++;
		}
		form.add(table);
		form.add(new Break());

		SubmitButton newLink = (SubmitButton) getButton(new SubmitButton(localize("year.new", "New year"), PARAMETER_ACTION, String.valueOf(ACTION_NEW)));
		form.add(newLink);

		add(form);
	}

	public void showEditor(IWContext iwc, Object yearPK) {
		Form form = new Form();
		form.setStyleClass(STYLENAME_SCHOOL_FORM);
		
		TextInput inputName = new TextInput(PARAMETER_NAME);
		TextInput inputAge = new TextInput(PARAMETER_AGE);
		DropdownMenu inputType = new DropdownMenu(PARAMETER_TYPE);
		TextArea inputInfo = new TextArea(PARAMETER_INFO);
		TextInput localizedKey = new TextInput(PARAMETER_LOCALIZED_KEY);
		DropdownMenu inputCategory = new DropdownMenu(PARAMETER_CATEGORY);

		try {
			SchoolTypeHome stHome = (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
			Collection coll = stHome.findAllSchoolTypes();
			if (coll != null && !coll.isEmpty()) {
				inputType.addMenuElements(coll);
			}
			inputType.addMenuElementFirst("-1", "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			SchoolCategoryHome scHome = (SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class);
			Collection coll = scHome.findAllCategories();
			Iterator iter = coll.iterator();
			while (iter.hasNext()) {
				SchoolCategory category = (SchoolCategory) iter.next();
				inputCategory.addMenuElement(category.getCategory(), localize(category.getLocalizedKey(), category.getName()));
			}
			inputCategory.addMenuElementFirst("", "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		if (yearPK != null) {
			try {
				SchoolYear year = getBusiness().getSchoolYear(yearPK);

				inputName.setContent(year.getSchoolYearName());
				inputInfo.setContent(year.getSchoolYearInfo());
				inputAge.setContent(String.valueOf(year.getSchoolYearAge()));
				inputType.setSelectedElement(year.getSchoolTypeId());
				if (year.getLocalizedKey() != null) {
					localizedKey.setContent(year.getLocalizedKey());
				}
				if (year.getSchoolCategory() != null) {
					inputCategory.setSelectedElement(year.getSchoolCategory().getCategory());
				}
				form.add(new HiddenInput(PARAMETER_SCHOOL_YEAR_PK, yearPK.toString()));
			}
			catch (Exception ex) {
			}
		}

		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		Label label = new Label(localize("name", "Name"), inputName);
		layer.add(label);
		layer.add(inputName);
		form.add(layer);

		if (iSchoolCategory == null) {
			layer = new Layer(Layer.DIV);
			layer.setStyleClass(STYLENAME_FORM_ELEMENT);
			label = new Label(localize("category", "Category"), inputCategory);
			layer.add(label);
			layer.add(inputCategory);
			form.add(layer);
		}
		
		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("school_type", "Type"), inputType);
		layer.add(label);
		layer.add(inputType);
		form.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("info", "Info"), inputInfo);
		layer.add(label);
		layer.add(inputInfo);
		form.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("localized_key", "Localized key"), localizedKey);
		layer.add(label);
		layer.add(localizedKey);
		form.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("age", "Age"), inputAge);
		layer.add(label);
		layer.add(inputAge);
		form.add(layer);
		
		form.add(new Break());

		SubmitButton save = (SubmitButton) getButton(new SubmitButton(localize("save", "Save"), PARAMETER_ACTION, String.valueOf(ACTION_SAVE)));
		SubmitButton cancel = (SubmitButton) getButton(new SubmitButton(localize("cancel", "Cancel"), PARAMETER_ACTION, String.valueOf(ACTION_VIEW)));

		form.add(cancel);
		form.add(save);

		add(form);
	}
	
	public void setSchoolCategory(String category) {
		this.iSchoolCategory = category;
	}
}