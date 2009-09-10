package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import com.idega.block.school.data.SchoolArea;
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
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;

/**
 * Title:
 * Description:
 * Copyright: Copyright (c) 2002
 * Company:
 * 
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */

public class SchoolAreaEditor extends SchoolBlock {

	private static final String PARAMETER_ACTION = "sch_area_prm_action";
	private static final String PARAMETER_SCHOOL_AREA_PK = "prm_school_area_pk";
	private static final String PARAMETER_NAME = "prm_name";
	private static final String PARAMETER_CITY = "prm_city";
	private static final String PARAMETER_INFO = "prm_info";

	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;
	private static final int ACTION_NEW = 3;
	private static final int ACTION_SAVE = 4;
	private static final int ACTION_DELETE = 5;

	protected void init(IWContext iwc) throws Exception {
		switch (parseAction(iwc)) {
			case ACTION_VIEW:
				showList(iwc);
				break;

			case ACTION_EDIT:
				Object schoolPK = iwc.getParameter(PARAMETER_SCHOOL_AREA_PK);
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
				getBusiness().removeSchoolArea(iwc.getParameter(PARAMETER_SCHOOL_AREA_PK));
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
		String id = iwc.getParameter(PARAMETER_SCHOOL_AREA_PK);
		String name = iwc.getParameter(PARAMETER_NAME);
		String city = iwc.getParameter(PARAMETER_CITY);
		String info = iwc.getParameter(PARAMETER_INFO);
		int aid = -1;
		if (id != null) {
			aid = Integer.parseInt(id);
		}
		getBusiness().storeSchoolArea(aid, name, info, city);
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
		column.setSpan(3);
		column = columnGroup.createColumn();
		column.setSpan(2);
		column.setWidth("12");

		Collection schoolAreas = null;
		try {
			schoolAreas = getBusiness().findAllSchoolAreas();
		}
		catch (RemoteException rex) {
			schoolAreas = new ArrayList();
		}
		
		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		TableCell2 cell = row.createHeaderCell();
		cell.setStyleClass("firstColumn");
		cell.add(new Text(localize("name", "Name")));
		row.createHeaderCell().add(new Text(localize("city", "City")));
		row.createHeaderCell().add(new Text(localize("info", "Info")));
		row.createHeaderCell().add(Text.getNonBrakingSpace());
		cell = row.createHeaderCell();
		cell.setStyleClass("lastColumn");
		cell.add(Text.getNonBrakingSpace());

		group = table.createBodyRowGroup();
		int iRow = 1;
		java.util.Iterator iter = schoolAreas.iterator();
		while (iter.hasNext()) {
			SchoolArea area = (SchoolArea) iter.next();
			row = group.createRow();

			try {
				Link edit = new Link(getEditIcon(localize("edit", "Edit")));
				edit.addParameter(PARAMETER_SCHOOL_AREA_PK, area.getPrimaryKey().toString());
				edit.addParameter(PARAMETER_ACTION, ACTION_EDIT);

				Link delete = new Link(getDeleteIcon(localize("delete", "Delete")));
				delete.addParameter(PARAMETER_SCHOOL_AREA_PK, area.getPrimaryKey().toString());
				delete.addParameter(PARAMETER_ACTION, ACTION_DELETE);

				cell = row.createCell();
				cell.setStyleClass("firstColumn");
				cell.add(new Text(area.getSchoolAreaName()));
				row.createCell().add(new Text(area.getSchoolAreaCity()));
				row.createCell().add(new Text(area.getSchoolAreaInfo()));
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
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			iRow++;
		}

		form.add(table);
		form.add(new Break());

		SubmitButton newLink = (SubmitButton) getButton(new SubmitButton(localize("area.new", "New area"), PARAMETER_ACTION, String.valueOf(ACTION_NEW)));
		form.add(newLink);

		add(form);
	}

	public void showEditor(IWContext iwc, Object areaPK) {
		Form form = new Form();
		form.setStyleClass(STYLENAME_SCHOOL_FORM);

		TextInput inputName = new TextInput(PARAMETER_NAME);
		TextInput inputCity = new TextInput(PARAMETER_CITY);
		TextArea inputInfo = new TextArea(PARAMETER_INFO);
		if (areaPK != null) {
			try {
				SchoolArea area = getBusiness().getSchoolArea(areaPK);
				inputName.setContent(area.getSchoolAreaName());
				inputCity.setContent(area.getSchoolAreaCity());
				inputInfo.setContent(area.getSchoolAreaInfo());
				form.add(new HiddenInput(PARAMETER_SCHOOL_AREA_PK, areaPK.toString()));
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

		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("city", "City"), inputCity);
		layer.add(label);
		layer.add(inputCity);
		form.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("info", "Info"), inputInfo);
		layer.add(label);
		layer.add(inputInfo);
		form.add(layer);

		form.add(new Break());

		SubmitButton save = (SubmitButton) getButton(new SubmitButton(localize("save", "Save"), PARAMETER_ACTION, String.valueOf(ACTION_SAVE)));
		SubmitButton cancel = (SubmitButton) getButton(new SubmitButton(localize("cancel", "Cancel"), PARAMETER_ACTION, String.valueOf(ACTION_VIEW)));

		form.add(cancel);
		form.add(save);

		add(form);
	}
}