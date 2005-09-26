package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolSeason;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Table2;
import com.idega.presentation.TableColumn;
import com.idega.presentation.TableColumnGroup;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.util.IWTimestamp;

/**
 * Title:
 * Description:
 * Copyright: Copyright (c) 2002
 * Company:
 * 
 * @author <a href="mailto:aron@idega.is">Aron Birkir </a> <br>
 * @version 1.0
 */

public class SchoolSeasonEditor extends SchoolBlock {

	private static final String PARAMETER_ACTION = "sch_season_prm_action";
	private static final String PARAMETER_SCHOOL_SEASON_PK = "prm_school_season_pk";
	private static final String PARAMETER_NAME = "prm_name";
	private static final String PARAMETER_CATEGORY = "prm_category";
	private static final String PARAMETER_SEASON_START = "prm_season_start";
	private static final String PARAMETER_SEASON_END = "prm_season_end";
	private static final String PARAMETER_CHOICE_START_DATE = "prm_choice_start_date";
	private static final String PARAMETER_CHOICE_END_DATE = "prm_choice_end_date";
	
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
				Object schoolPK = iwc.getParameter(PARAMETER_SCHOOL_SEASON_PK);
				showEditor(iwc, schoolPK);
				break;

			case ACTION_NEW:
				showEditor(iwc, null);
				break;

			case ACTION_SAVE:
				saveArea(iwc);
				showList(iwc);
				break;

			case ACTION_DELETE:
				getBusiness().removeSchoolSeason(iwc.getParameter(PARAMETER_SCHOOL_SEASON_PK));
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

	private void saveArea(IWContext iwc) throws java.rmi.RemoteException {
		String id = iwc.getParameter(PARAMETER_SCHOOL_SEASON_PK);
		String name = iwc.getParameter(PARAMETER_NAME);
		String category = null;
		if (iSchoolCategory == null) {
			category = iwc.getParameter(PARAMETER_CATEGORY);
		}
		else {
			category = iSchoolCategory;
		}
		Date startDate = iwc.isParameterSet(PARAMETER_SEASON_START) ? new IWTimestamp(iwc.getParameter(PARAMETER_SEASON_START)).getDate() : null;
		Date endDate = iwc.isParameterSet(PARAMETER_SEASON_END) ? new IWTimestamp(iwc.getParameter(PARAMETER_SEASON_END)).getDate() : null;
		Date dueDate = iwc.isParameterSet(PARAMETER_CHOICE_END_DATE) ? new IWTimestamp(iwc.getParameter(PARAMETER_CHOICE_END_DATE)).getDate() : null;
		Date choiceStartDate = iwc.isParameterSet(PARAMETER_CHOICE_START_DATE) ? new IWTimestamp(iwc.getParameter(PARAMETER_CHOICE_START_DATE)).getDate() : null;

		int aid = -1;
		if (id != null) {
			aid = Integer.parseInt(id);
		}
		getBusiness().storeSchoolSeason(aid, name, startDate, endDate, choiceStartDate, dueDate, category);
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

		Collection seasons = null;
		try {
			if (iSchoolCategory != null) {
				seasons = getBusiness().findAllSchoolSeasons(iSchoolCategory);
			}
			else {
				seasons = getBusiness().findAllSchoolSeasons();
			}
		}
		catch (RemoteException rex) {
			seasons = new ArrayList();
		}

		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		row.createHeaderCell().add(new Text(localize("name", "Name")));
		row.createHeaderCell().add(new Text(localize("category", "Category")));
		row.createHeaderCell().add(new Text(localize("start", "Start")));
		row.createHeaderCell().add(new Text(localize("end", "End")));
		row.createHeaderCell().add(new Text(localize("due_date", "Due date")));
		row.createHeaderCell();
		row.createHeaderCell();

		group = table.createBodyRowGroup();
		int iRow = 1;
		java.util.Iterator iter = seasons.iterator();
		while (iter.hasNext()) {
			SchoolSeason season = (SchoolSeason) iter.next();
			row = group.createRow();

			try {
				IWTimestamp startDate = season.getSchoolSeasonStart() != null ? new IWTimestamp(season.getSchoolSeasonStart()) : null;
				IWTimestamp endDate = season.getSchoolSeasonEnd() != null ? new IWTimestamp(season.getSchoolSeasonEnd()) : null;
				IWTimestamp dueDate = season.getChoiceEndDate() != null ? new IWTimestamp(season.getChoiceEndDate()) : null;
				SchoolCategory category = season.getSchoolCategory();
				
				Link edit = new Link(getEditIcon(localize("edit", "Edit")));
				edit.addParameter(PARAMETER_SCHOOL_SEASON_PK, season.getPrimaryKey().toString());
				edit.addParameter(PARAMETER_ACTION, ACTION_EDIT);

				Link delete = new Link(getDeleteIcon(localize("delete", "Delete")));
				delete.addParameter(PARAMETER_SCHOOL_SEASON_PK, season.getPrimaryKey().toString());
				delete.addParameter(PARAMETER_ACTION, ACTION_DELETE);

				row.createCell().add(new Text(season.getSchoolSeasonName()));
				row.createCell().add(new Text(category != null ? localize(category.getLocalizedKey(), category.getName()) : "-"));
				row.createCell().add(new Text(startDate != null ? startDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT) : "-"));
				row.createCell().add(new Text(endDate != null ? endDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT) : "-"));
				row.createCell().add(new Text(dueDate != null ? dueDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT) : "-"));
				row.createCell().add(edit);
				row.createCell().add(delete);

				if (iRow % 2 == 0) {
					row.setStyleClass(STYLENAME_LIST_TABLE_EVEN_ROW);
				}
				else {
					row.setStyleClass(STYLENAME_LIST_TABLE_ODD_ROW);
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			iRow++;
		}
		form.add(table);
		form.add(new Break());

		SubmitButton newLink = (SubmitButton) getButton(new SubmitButton(localize("season.new", "New season"), PARAMETER_ACTION, String.valueOf(ACTION_NEW)));
		form.add(newLink);

		add(form);
	}

	public void showEditor(IWContext iwc, Object seasonPK) throws RemoteException {
		Form form = new Form();
		form.setStyleClass(STYLENAME_SCHOOL_FORM);
		
		TextInput inputName = new TextInput(PARAMETER_NAME);
		DateInput inputStart = new DateInput(PARAMETER_SEASON_START);
		DateInput inputEnd = new DateInput(PARAMETER_SEASON_END);
		DateInput inputStartDate = new DateInput(PARAMETER_CHOICE_START_DATE);
		DateInput inputDueDate = new DateInput(PARAMETER_CHOICE_END_DATE);
		SelectorUtility util = new SelectorUtility();
		DropdownMenu drpCategory = (DropdownMenu) util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_CATEGORY), getBusiness().getSchoolCategories(), "getLocalizedKey", getResourceBundle());
		
		if (seasonPK != null) {
			try {
				SchoolSeason season = getBusiness().getSchoolSeason(seasonPK);

				inputName.setContent(season.getSchoolSeasonName());
				inputStart.setDate(season.getSchoolSeasonStart());
				inputEnd.setDate(season.getSchoolSeasonEnd());
				if (season.getChoiceStartDate() != null) {
					inputStartDate.setDate(season.getChoiceStartDate());
				}
				inputDueDate.setDate(season.getChoiceEndDate());
				if (season.getSchoolCategoryPK() != null) {
					drpCategory.setSelectedElement(season.getSchoolCategoryPK());
				}
				form.add(new HiddenInput(PARAMETER_SCHOOL_SEASON_PK, seasonPK.toString()));
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
			label = new Label(localize("school_category", "School category"), drpCategory);
			layer.add(label);
			layer.add(drpCategory);
			form.add(layer);
		}

		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("start", "Start"), inputStart);
		layer.add(label);
		layer.add(inputStart);
		form.add(layer);
		
		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("end", "End"), inputEnd);
		layer.add(label);
		layer.add(inputEnd);
		form.add(layer);
		
		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("start_date", "Start date"), inputStartDate);
		layer.add(label);
		layer.add(inputStartDate);
		form.add(layer);
		
		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("due_date", "Duedate"), inputDueDate);
		layer.add(label);
		layer.add(inputDueDate);
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