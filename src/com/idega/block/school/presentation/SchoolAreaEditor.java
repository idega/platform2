package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

import com.idega.block.school.data.SchoolArea;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
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

public class SchoolAreaEditor extends SchoolBlock {

	protected void init(IWContext iwc) throws Exception {
		Form F = new Form();

		if (iwc.isParameterSet("sch_save_area")) {
			saveArea(iwc);
			F.add(getListTable(iwc, null));
		}
		else if (iwc.isParameterSet("sch_delete_area")) {
			int id = Integer.parseInt(iwc.getParameter("sch_delete_area"));
			getBusiness().removeSchoolArea(id);
			F.add(getListTable(iwc, null));
		}
		else if (iwc.isParameterSet("sch_school_area_id")) {
			int id = Integer.parseInt(iwc.getParameter("sch_school_area_id"));
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
		return getInputTable(iwc, getBusiness().getSchoolArea(new Integer(id)));
	}

	private void saveArea(IWContext iwc) throws java.rmi.RemoteException {
		if (iwc.isParameterSet("sch_save_area")) {
			String id = iwc.getParameter("sch_school_area_id");
			String name = iwc.getParameter("sch_area_name");
			String city = iwc.getParameter("sch_area_city");
			String info = iwc.getParameter("sch_area_info");
			int aid = -1;
			if (id != null) {
				aid = Integer.parseInt(id);
			}
			getBusiness().storeSchoolArea(aid, name, info, city);
		}
	}

	public PresentationObject getListTable(IWContext iwc, SchoolArea area) {
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setColumns(4);
		table.setWidth(4, 12);
		int row = 1;

		Collection schoolAreas = null;
		try {
			schoolAreas = getBusiness().findAllSchoolAreas();
		}
		catch (RemoteException rex) {
			schoolAreas = new ArrayList();
		}
		
		table.setCellpaddingLeft(1, row, 12);
		table.add(getSmallHeader(localize("name", "Name")), 1, row);
		table.add(getSmallHeader(localize("city", "City")), 2, row);
		table.add(getSmallHeader(localize("info", "Info")), 3, row);
		table.setRowStyleClass(row++, getHeaderRowClass());

		java.util.Iterator iter = schoolAreas.iterator();
		SchoolArea sarea;
		while (iter.hasNext()) {
			sarea = (SchoolArea) iter.next();
			try {
				Link L = new Link(getEditIcon(localize("edit", "Edit")));
				L.addParameter("sch_school_area_id", ((Integer) sarea.getPrimaryKey()).intValue());

				table.setCellpaddingLeft(1, row, 12);
				table.setCellpaddingRight(table.getColumns(), row, 12);
				table.add(getSmallText(sarea.getSchoolAreaName()), 1, row);
				table.add(getSmallText(sarea.getSchoolAreaCity()), 2, row);
				table.add(getSmallText(sarea.getSchoolAreaInfo()), 3, row);
				table.add(L, 4, row);

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
		GenericButton newLink = getButton(new GenericButton("new", localize("area.new", "New area")));
		newLink.setPageToOpen(iwc.getCurrentIBPageID());
		newLink.addParameterToPage("sch_new_area", "true");
		table.add(newLink, 1, row);

		return table;
	}

	public PresentationObject getInputTable(IWContext iwc, SchoolArea area) {
		Table T = new Table(3, 6);
		T.mergeCells(1, 1, 3, 1);
		T.add(getHeader(localize("school_area", "SchoolArea")), 1, 1);
		T.add(getHeader(localize("name", "Name")), 1, 2);
		T.add(getHeader(localize("city", "City")), 1, 3);
		T.add(getHeader(localize("info", "Info")), 1, 4);

		TextInput inputName = (TextInput) getStyledInterface(new TextInput("sch_area_name"));
		TextInput inputCity = (TextInput) getStyledInterface(new TextInput("sch_area_city"));
		TextArea inputInfo = (TextArea) getStyledInterface(new TextArea("sch_area_info"));
		int areaId = -1;
		if (area != null) {
			try {
				areaId = ((Integer) area.getPrimaryKey()).intValue();
				inputName.setContent(area.getSchoolAreaName());
				inputCity.setContent(area.getSchoolAreaCity());
				inputInfo.setContent(area.getSchoolAreaInfo());
				T.add(new HiddenInput("sch_school_area_id", String.valueOf(areaId)));
			}
			catch (Exception ex) {
			}
		}

		T.add(inputName, 3, 2);
		T.add(inputCity, 3, 3);
		T.add(inputInfo, 3, 4);
		T.add(getButton(new SubmitButton(localize("save", "Save"), "sch_save_area", "true")), 3, 5);
		GenericButton cancel = getButton(new GenericButton("cancel", localize("cancel", "Cancel")));
		cancel.setPageToOpen(iwc.getCurrentIBPageID());
		T.add(cancel, 3, 5);
		if (areaId > 0) {
			GenericButton delete = getButton(new GenericButton("delete", localize("delete", "Delete")));
			delete.setPageToOpen(iwc.getCurrentIBPageID());
			delete.addParameterToPage("sch_delete_area", areaId);
			T.add(delete, 3, 5);
		}

		return T;
	}
}