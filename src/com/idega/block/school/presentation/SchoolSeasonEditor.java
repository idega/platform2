package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

import com.idega.block.school.data.SchoolSeason;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;

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

public class SchoolSeasonEditor extends SchoolBlock {

	protected void init(IWContext iwc) throws Exception {
		Form F = new Form();

		if (iwc.isParameterSet("sch_save_season")) {
			saveArea(iwc);
			F.add(getListTable(iwc, null));
		}
		else if (iwc.isParameterSet("sch_delete_season")) {
			int id = Integer.parseInt(iwc.getParameter("sch_delete_season"));
			getBusiness().removeSchoolSeason(id);
			F.add(getListTable(iwc, null));
		}
		else if (iwc.isParameterSet("sch_school_season_id")) {
			int id = Integer.parseInt(iwc.getParameter("sch_school_season_id"));
			F.add(getInput(iwc, id));

		}
		else if (iwc.isParameterSet("sch_new_season")) {
			F.add(getInput(iwc, -1));
		}
		else
			F.add(getListTable(iwc, null));

		add(F);

	}

	private PresentationObject getInput(IWContext iwc, int id) throws java.rmi.RemoteException {
		return getInputTable(iwc, getBusiness().getSchoolSeason(new Integer(id)));
	}

	private void saveArea(IWContext iwc) throws java.rmi.RemoteException {
		if (iwc.isParameterSet("sch_save_season")) {
			String id = iwc.getParameter("sch_school_season_id");
			String name = iwc.getParameter("sch_season_name");
			String start = iwc.getParameter("sch_season_start");
			String end = iwc.getParameter("sch_season_end");
			String duedate = iwc.getParameter("sch_season_due_date");
			int aid = -1;
			Date startDate = null;
			Date endDate = null;
			Date dueDate = null;
			if (id != null) {
				aid = Integer.parseInt(id);
			}
			if (start != null) {
				startDate = new IWTimestamp(start).getSQLDate();
			}
			if (end != null) {
				endDate = new IWTimestamp(end).getSQLDate();
			}
			if (duedate != null) {
				dueDate = new IWTimestamp(duedate).getSQLDate();
			}
			getBusiness().storeSchoolSeason(aid, name, startDate, endDate, dueDate);
		}
	}

	public PresentationObject getListTable(IWContext iwc, SchoolSeason season) {
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setColumns(5);
		table.setWidth(5, 12);
		int row = 1;
		int col = 1;

		Collection seasons = null;
		try {
			seasons = getBusiness().findAllSchoolSeasons();
		}
		catch (RemoteException rex) {
			seasons = new ArrayList();
		}

		table.setCellpaddingLeft(1, row, 12);
		table.add(getSmallHeader(localize("name", "Name")), col++, row);
		table.add(getSmallHeader(localize("start", "Start")), col++, row);
		table.add(getSmallHeader(localize("end", "End")), col++, row);
		table.add(getSmallHeader(localize("due_date", "Due date")), col++, row);
		table.setRowStyleClass(row++, getHeaderRowClass());

		java.util.Iterator iter = seasons.iterator();
		SchoolSeason sarea;
		IWTimestamp startDate;
		IWTimestamp endDate;
		IWTimestamp dueDate;
		while (iter.hasNext()) {
			col = 1;
			sarea = (SchoolSeason) iter.next();
			try {
				startDate = sarea.getSchoolSeasonStart() != null ? new IWTimestamp(sarea.getSchoolSeasonStart()) : null;
				endDate = sarea.getSchoolSeasonEnd() != null ? new IWTimestamp(sarea.getSchoolSeasonEnd()) : null;
				dueDate = sarea.getSchoolSeasonDueDate() != null ? new IWTimestamp(sarea.getSchoolSeasonDueDate()) : null;
				Link L = new Link(getEditIcon(localize("edit", "Edit")));
				L.addParameter("sch_school_season_id", ((Integer) sarea.getPrimaryKey()).intValue());
				
				table.setCellpaddingLeft(1, row, 12);
				table.setCellpaddingRight(table.getColumns(), row, 12);
				table.add(getSmallText(sarea.getSchoolSeasonName()), col++, row);
				if (startDate != null) {
					table.add(getSmallText(startDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), col++, row);
				}
				else {
					table.add(getSmallText("-"), col++, row);
				}
				if (endDate != null) {
					table.add(getSmallText(endDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), col++, row);
				}
				else {
					table.add(getSmallText("-"), col++, row);
				}
				if (dueDate != null) {
					table.add(getSmallText(dueDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), col++, row);
				}
				else {
					table.add(getSmallText("-"), col++, row);
				}
				table.add(L, col, row);

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
		GenericButton newLink = getButton(new GenericButton("new", localize("season.new", "New season")));
		newLink.setPageToOpen(iwc.getCurrentIBPageID());
		newLink.addParameterToPage("sch_new_season", "true");
		table.add(newLink, 1, row);

		return table;
	}

	public PresentationObject getInputTable(IWContext iwc, SchoolSeason bean) {
		Table T = new Table(3, 6);
		T.mergeCells(1, 1, 3, 1);
		T.add(getHeader(localize("school_season", "SchoolSeason")), 1, 1);
		T.add(getHeader(localize("name", "Name")), 1, 2);
		T.add(getHeader(localize("start", "Start")), 1, 3);
		T.add(getHeader(localize("end", "End")), 1, 4);
		T.add(getHeader(localize("due_date", "Duedate")), 1, 5);

		TextInput inputName = (TextInput) getStyledInterface(new TextInput("sch_season_name"));
		DateInput inputStart = (DateInput) getStyledInterface(new DateInput("sch_season_start"));
		DateInput inputEnd = (DateInput) getStyledInterface(new DateInput("sch_season_end"));
		DateInput inputDueDate = (DateInput) getStyledInterface(new DateInput("sch_season_due_date"));
		int beanId = -1;
		if (bean != null) {
			try {
				beanId = ((Integer) bean.getPrimaryKey()).intValue();
				inputName.setContent(bean.getSchoolSeasonName());
				inputStart.setDate(bean.getSchoolSeasonStart());
				inputEnd.setDate(bean.getSchoolSeasonEnd());
				inputDueDate.setDate(bean.getSchoolSeasonDueDate());
				T.add(new HiddenInput("sch_school_season_id", String.valueOf(beanId)));
			}
			catch (Exception ex) {
			}
		}

		T.add(inputName, 3, 2);
		T.add(inputStart, 3, 3);
		T.add(inputEnd, 3, 4);
		T.add(inputDueDate, 3, 5);
		T.add(getButton(new SubmitButton(localize("save", "Save"), "sch_save_season", "true")), 3, 6);
		GenericButton cancel = getButton(new GenericButton("cancel", localize("cancel", "Cancel")));
		cancel.setPageToOpen(iwc.getCurrentIBPageID());
		T.add(cancel, 3, 6);
		if (beanId > 0) {
			GenericButton delete = getButton(new GenericButton("delete", localize("delete", "Delete")));
			delete.setPageToOpen(iwc.getCurrentIBPageID());
			delete.addParameterToPage("sch_delete_season", beanId);
			T.add(delete, 3, 6);
		}

		return T;
	}
}