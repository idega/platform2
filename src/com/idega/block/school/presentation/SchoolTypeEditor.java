package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

import com.idega.block.school.data.SchoolType;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.util.SelectorUtility;

/**
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */
public class SchoolTypeEditor extends SchoolBlock {

	private static final String PARAMETER_IS_FREETIME_TYPE = "sch_type_freetime";
	private static final String PARAMETER_IS_FAMILY_FREETIME_TYPE = "sch_type_family_freetime";
	private static final String PARAMETER_MAX_AGE = "sch_type_max_age";
	private static final String PARAMETER_ORDER = "sch_type_order";
	
	protected void init(IWContext iwc) throws Exception {
		Form F = new Form();

		if (iwc.isParameterSet("sch_save_type")) {
			saveType(iwc);
			F.add(getListTable(iwc, null));
		}
		else
			if (iwc.isParameterSet("sch_delete_type")) {
				int id = Integer.parseInt(iwc.getParameter("sch_delete_type"));
				getBusiness().removeSchoolType(id);
				F.add(getListTable(iwc, null));
			}
			else
				if (iwc.isParameterSet("sch_school_type_id")) {
					int id = Integer.parseInt(iwc.getParameter("sch_school_type_id"));
					F.add(getInput(iwc, id));

				}
				else
					if (iwc.isParameterSet("sch_new_type")) {
						F.add(getInput(iwc, -1));
					}
					else
						F.add(getListTable(iwc, null));

		add(F);

	}

	private PresentationObject getInput(IWContext iwc, int id) throws java.rmi.RemoteException {
		return getInputTable(iwc, getBusiness().getSchoolType(new Integer(id)));
	}

	private void saveType(IWContext iwc) throws java.rmi.RemoteException {
		if (iwc.isParameterSet("sch_save_type")) {
			String id = iwc.getParameter("sch_school_type_id");
			String name = iwc.getParameter("sch_type_name");
			String info = iwc.getParameter("sch_type_info");
			String cat = iwc.getParameter("sch_type_cat");
			String locKey = iwc.getParameter("sch_type_lockey");
			int maxAge = -1;
			if (iwc.isParameterSet(PARAMETER_MAX_AGE)) {
				try {
					maxAge = Integer.parseInt(iwc.getParameter(PARAMETER_MAX_AGE));
				}
				catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
			int aid = -1;
			if (id != null)
				aid = Integer.parseInt(id);
			boolean isFreetimeType = false;
			if (iwc.isParameterSet(PARAMETER_IS_FREETIME_TYPE))
				isFreetimeType = true;
			boolean isFamilyFreetimeType = false;
			if (iwc.isParameterSet(PARAMETER_IS_FAMILY_FREETIME_TYPE))
				isFamilyFreetimeType = true;

			int order = -1;
			if (iwc.isParameterSet(PARAMETER_ORDER)) {
				try {
					order = Integer.parseInt(iwc.getParameter(PARAMETER_ORDER));
				}
				catch (NumberFormatException e) {
					order = -1;
				}
			}
			
			getBusiness().storeSchoolType(aid, name, info, cat, locKey, maxAge, isFreetimeType, isFamilyFreetimeType, order);
		}
	}

	public PresentationObject getListTable(IWContext iwc, SchoolType area) {
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setColumns(3);
		table.setWidth(3, 12);
		int row = 1;
		int col = 1;

		Collection schoolTypes = null;
		try {
			schoolTypes = getBusiness().findAllSchoolTypes();
		}
		catch (RemoteException rex) {
			schoolTypes = new ArrayList();
		}
		
		table.setCellpaddingLeft(1, row, 12);
		table.add(getSmallHeader(localize("name", "Name")), col++, row);
		table.add(getSmallHeader(localize("info", "Info")), col++, row);
		table.setRowStyleClass(row++, getHeaderRowClass());

		java.util.Iterator iter = schoolTypes.iterator();
		SchoolType sType;
		while (iter.hasNext()) {
			col = 1;
			sType = (SchoolType) iter.next();
			try {
				Link L = new Link(getEditIcon(localize("edit", "Edit")));
				L.addParameter("sch_school_type_id", ((Integer) sType.getPrimaryKey()).intValue());

				table.setCellpaddingLeft(1, row, 12);
				table.setCellpaddingRight(table.getColumns(), row, 12);
				table.add(getSmallText(sType.getSchoolTypeName()), col++, row);
				table.add(getSmallText(sType.getSchoolTypeInfo()), col++, row);
				table.add(L, col++, row);

				if (row % 2 == 0) {
					table.setRowStyleClass(row, getDarkRowClass());
				}
				else {
					table.setRowStyleClass(row, getLightRowClass());
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			row++;
		}

		table.setHeight(row++, 12);
		table.setCellpaddingLeft(1, row, 12);
		table.mergeCells(1, row, table.getColumns(), row);
		GenericButton newLink = getButton(new GenericButton("new", localize("type.new", "New type")));
		newLink.setPageToOpen(iwc.getCurrentIBPageID());
		newLink.addParameterToPage("sch_new_type", "true");
		table.add(newLink, 1, row);

		return table;
	}

	public PresentationObject getInputTable(IWContext iwc, SchoolType type) throws RemoteException {
		Table T = new Table();
		T.setColumns(3);
		T.mergeCells(1, 1, 3, 1);
		
		T.add(getHeader(localize("school_type", "Schooltype")), 1, 1);
		T.add(getHeader(localize("category", "Category")), 1, 2);
		T.add(getHeader(localize("name", "Name")), 1, 3);
		T.add(getHeader(localize("info", "Info")), 1, 4);
		T.add(getHeader(localize("maxage", "Max school age")), 1, 5);
		T.add(getHeader(localize("localization_key", "Key")), 1, 6);
		T.add(getHeader(localize("is_freetime_type", "Is freetime type")), 1, 7);
		T.add(getHeader(localize("is_family_freetime_type", "Is family freetime type")), 1, 8);
		T.add(getHeader(localize("order", "order")), 1, 9);
		
		SelectorUtility util = new SelectorUtility();
		DropdownMenu drpCategory = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu("sch_type_cat"), getBusiness().getSchoolCategories(), "getLocalizedKey", getResourceBundle()));

		TextInput inputName = (TextInput) getStyledInterface(new TextInput("sch_type_name"));
		TextInput inputKey = (TextInput) getStyledInterface(new TextInput("sch_type_lockey"));
		TextInput inputAge = (TextInput) getStyledInterface(new TextInput(PARAMETER_MAX_AGE));
		inputAge.setLength(4);
		TextArea inputInfo = (TextArea) getStyledInterface(new TextArea("sch_type_info"));
		CheckBox isFreetime = getCheckBox(PARAMETER_IS_FREETIME_TYPE, "true");
		CheckBox isFamilyFreetime = getCheckBox(PARAMETER_IS_FAMILY_FREETIME_TYPE, "true");
		TextInput inputOrder = (TextInput) getStyledInterface(new TextInput(PARAMETER_ORDER));
		inputOrder.setLength(4);
		
		int typeId = -1;
		if (type != null) {
			try {
				String name = type.getSchoolTypeName();
				if (name != null)
					inputName.setContent(name);

				String info = type.getSchoolTypeInfo();
				if (info != null)
					inputInfo.setContent(info);
				if (type.getMaxSchoolAge() != -1)
					inputAge.setContent(String.valueOf(type.getMaxSchoolAge()));
				typeId = ((Integer) type.getPrimaryKey()).intValue();
				T.add(new HiddenInput("sch_school_type_id", String.valueOf(typeId)));
				String category = type.getSchoolCategory();
				drpCategory.setSelectedElement((category));
				String key = type.getLocalizationKey();
				if (key != null)
					inputKey.setContent(key);
				isFreetime.setChecked(type.getIsFreetimeType());
				isFamilyFreetime.setChecked(type.getIsFamilyFreetimeType());
				if (type.getOrder() != -1)
					inputOrder.setContent(String.valueOf(type.getOrder()));
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		T.add(drpCategory, 3, 2);
		T.add(inputName, 3, 3);
		T.add(inputInfo, 3, 4);
		T.add(inputAge, 3, 5);
		T.add(inputKey, 3, 6);
		T.add(isFreetime, 3, 7);
		T.add(isFamilyFreetime, 3, 8);
		T.add(inputOrder, 3, 9);
		
		T.setHeight(10, 6);
		
		T.mergeCells(1, 11, 3, 11);
		T.add(getButton(new SubmitButton(localize("save", "Save"), "sch_save_type", "true")), 1, 11);
		GenericButton cancel = getButton(new GenericButton("cancel", localize("cancel", "Cancel")));
		cancel.setPageToOpen(iwc.getCurrentIBPageID());
		T.add(cancel, 1, 11);
		if (typeId > 0) {
			GenericButton delete = getButton(new GenericButton("delete", localize("delete", "Delete")));
			delete.setPageToOpen(iwc.getCurrentIBPageID());
			delete.addParameterToPage("sch_delete_type", typeId);
			T.add(delete, 1, 11);
		}

		return T;
	}
}