package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.school.data.SchoolSubArea;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.DropdownMenu;

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

public class SchoolSubAreaEditor extends SchoolBlock {

	protected void init(IWContext iwc) throws Exception {
		Form F = new Form();

		if (iwc.isParameterSet("sch_save_subarea")) {
			saveArea(iwc);
			F.add(getListTable(iwc, null));
		}
		else if (iwc.isParameterSet("sch_delete_subarea")) {
			int id = Integer.parseInt(iwc.getParameter("sch_delete_subarea"));
			getBusiness().removeSchoolSubArea(id);
			F.add(getListTable(iwc, null));
		}
		else if (iwc.isParameterSet("sch_school_subarea_id")) {
			int id = Integer.parseInt(iwc.getParameter("sch_school_subarea_id"));
			F.add(getInput(iwc, id));

		}
		else if (iwc.isParameterSet("sch_new_subarea")) {
			F.add(getInput(iwc, -1));
		}
		else
			F.add(getListTable(iwc, null));

		add(F);

	}

	private PresentationObject getInput(IWContext iwc, int id) throws java.rmi.RemoteException {
		return getInputTable(iwc, getBusiness().getSchoolSubArea(new Integer(id)));
	}

	private void saveArea(IWContext iwc) throws java.rmi.RemoteException {
		if (iwc.isParameterSet("sch_save_subarea")) {
			String id = iwc.getParameter("sch_school_subarea_id");
			String name = iwc.getParameter("sch_subarea_name");
			String area = iwc.getParameter("sch_area");
			
			int aid = -1;
			if (id != null) {
				aid = Integer.parseInt(id);
			}
			getBusiness().storeSchoolSubArea(aid, name, Integer.parseInt(area));
		}
	}

	public PresentationObject getListTable(IWContext iwc, SchoolSubArea area) {
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setColumns(3);
		table.setWidth(4, 12);
		int row = 1;

		Collection schoolSubAreas = null;
		try {
			schoolSubAreas = getBusiness().findAllSchoolSubAreas();
		}
		catch (RemoteException rex) {
			schoolSubAreas = new ArrayList();
		}
		
		table.setCellpaddingLeft(1, row, 12);
		table.add(getSmallHeader(localize("name", "Name")), 1, row);
		table.add(getSmallHeader(localize("area", "Area")), 2, row);
		table.setRowStyleClass(row++, getHeaderRowClass());

		java.util.Iterator iter = schoolSubAreas.iterator();
		SchoolSubArea sarea;
		while (iter.hasNext()) {
			sarea = (SchoolSubArea) iter.next();
			try {
				Link L = new Link(getEditIcon(localize("edit", "Edit")));
				L.addParameter("sch_school_subarea_id", ((Integer) sarea.getPrimaryKey()).intValue());

				table.setCellpaddingLeft(1, row, 12);
				table.setCellpaddingRight(table.getColumns(), row, 12);
				table.add(getSmallText(sarea.getSchoolSubAreaName()), 1, row);
				table.add(getSmallText(sarea.getSchoolAreaName()), 2, row);
				table.add(L, 3, row);

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
		GenericButton newLink = getButton(new GenericButton("new", localize("subarea.new", "New sub area")));
		newLink.setPageToOpen(iwc.getCurrentIBPageID());
		newLink.addParameterToPage("sch_new_subarea", "true");
		table.add(newLink, 1, row);

		return table;
	}

	public PresentationObject getInputTable(IWContext iwc, SchoolSubArea subarea) {
		Table T = new Table(3, 6);
		T.mergeCells(1, 1, 3, 1);
		T.add(getHeader(localize("school_subarea", "SchoolSubArea")), 1, 1);
		T.add(getHeader(localize("name", "Name")), 1, 2);
		T.add(getHeader(localize("area", "Area")), 1, 3);

		Collection schoolAreas = null;
		TextInput inputName = (TextInput) getStyledInterface(new TextInput("sch_subarea_name"));
		try{
			schoolAreas = getBusiness().getSchoolAreaHome().findAllSchoolAreas();
		}catch(FinderException ex){
			ex.printStackTrace();
		}catch(RemoteException ex){
			ex.printStackTrace();
		}		
		DropdownMenu dropdownAreas = (DropdownMenu) getStyledInterface(new DropdownMenu(schoolAreas, "sch_area"));
		int subareaId = -1;
		if (subarea != null) {
			try {
				subareaId = ((Integer) subarea.getPrimaryKey()).intValue();
				inputName.setContent(subarea.getSchoolSubAreaName());
				dropdownAreas.setSelectedElement(subarea.getSchoolAreaId());
				T.add(new HiddenInput("sch_school_subarea_id", String.valueOf(subareaId)));
			}
			catch (Exception ex) {
			}
		}

		T.add(inputName, 3, 2);
		T.add(dropdownAreas, 3, 3);

		T.add(getButton(new SubmitButton(localize("save", "Save"), "sch_save_subarea", "true")), 3, 5);
		GenericButton cancel = getButton(new GenericButton("cancel", localize("cancel", "Cancel")));
		cancel.setPageToOpen(iwc.getCurrentIBPageID());
		T.add(cancel, 3, 5);
		if (subareaId > 0) {
			GenericButton delete = getButton(new GenericButton("delete", localize("delete", "Delete")));
			delete.setPageToOpen(iwc.getCurrentIBPageID());
			delete.addParameterToPage("sch_delete_subarea", subareaId);
			T.add(delete, 3, 5);
		}

		return T;
	}
}