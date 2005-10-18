/*
 * $Id: VacationEditor.java,v 1.4 2005/10/18 09:05:35 laddi Exp $
 * Created on Oct 2, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.meal.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import se.idega.idegaweb.commune.school.meal.data.MealVacationDay;
import se.idega.idegaweb.commune.school.meal.util.MealConstants;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOCreateException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableColumn;
import com.idega.presentation.TableColumnGroup;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Heading1;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;


/**
 * Last modified: $Date: 2005/10/18 09:05:35 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.4 $
 */
public class VacationEditor extends MealBlock {

	private static final String PARAMETER_ACTION = "prm_action";
	
	private static final String PARAMETER_VACATION_PK = "prm_vacation_pk";
	private static final String PARAMETER_VALID_FROM = "prm_valid_from";
	private static final String PARAMETER_VALID_TO = "prm_valid_to";
	private static final String PARAMETER_TYPE = "prm_type";
	private static final String PARAMETER_NAME = "prm_name";
	
	private static final int ACTION_VIEW = 1;
	private static final int ACTION_NEW = 2;
	private static final int ACTION_EDIT = 3;
	private static final int ACTION_SAVE = 4;
	private static final int ACTION_DELETE = 5;

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.school.meal.presentation.MealBlock#present(com.idega.presentation.IWContext)
	 */
	public void present(IWContext iwc) {
		try {
			if (getSession().getSchool() != null) {
				switch (parseAction(iwc)) {
					case ACTION_VIEW:
						showVacationList(iwc);
						break;
		
					case ACTION_NEW:
						showEditor(iwc);
						break;
		
					case ACTION_EDIT:
						try {
							MealVacationDay vacationDay= getBusiness().getVacationDay(iwc.getParameter(PARAMETER_VACATION_PK));
							showEditor(vacationDay);
						}
						catch (FinderException fe) {
							add(new Text(localize("no_vacation_found_with_pk", "No vacation found with primary key...")));
							add(new Break());
							showVacationList(iwc);
						}
						break;
						
					case ACTION_SAVE:
						save(iwc);
						break;
						
					case ACTION_DELETE:
						delete(iwc);
						break;
				}
			}
			else {
				add(new Text(localize("no_school_found_for_user", "No school found for user")));
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	private void showVacationList(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.setID(STYLENAME_MEAL_FORM);
		
		Table2 table = new Table2();
		table.setWidth("100%");
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setStyleClass(STYLENAME_LIST_TABLE);
		
		TableColumnGroup columnGroup = table.createColumnGroup();
		TableColumn column = columnGroup.createColumn();
		column.setSpan(4);
		column = columnGroup.createColumn();
		column.setSpan(2);
		column.setWidth("12");

		Collection vacationDays = getBusiness().getVacationDays(getSession().getSchool());

		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		row.createHeaderCell().add(new Text(localize("vacation_editor.valid_from", "Valid from")));
		TableCell2 cell = row.createHeaderCell();
		cell.setStyleClass("firstColumn");
		cell.add(new Text(localize("vacation_editor.valid_to", "Valid to")));

		row.createHeaderCell().add(new Text(localize("vacation_editor.type", "Type")));
		row.createHeaderCell().add(new Text(localize("vacation_editor.name", "Name")));
		row.createHeaderCell().add(Text.getNonBrakingSpace());
		
		cell = row.createHeaderCell();
		cell.setStyleClass("lastColumn");
		cell.add(Text.getNonBrakingSpace());
		
		group = table.createBodyRowGroup();
		int iRow = 1;
		
		Iterator iter = vacationDays.iterator();
		while (iter.hasNext()) {
			row = group.createRow();
			MealVacationDay vacationDay = (MealVacationDay) iter.next();
			
			try {
				IWTimestamp validFrom = new IWTimestamp(vacationDay.getValidFrom());
				IWTimestamp validTo = new IWTimestamp(vacationDay.getValidTo());
				
				Link edit = new Link("[ " + localize("vacation_editor.edit_vacation", "Edit vacation") + " ]");
				edit.addParameter(PARAMETER_VACATION_PK, vacationDay.getPrimaryKey().toString());
				edit.addParameter(PARAMETER_ACTION, ACTION_EDIT);
				
				Link delete = new Link("[ " + localize("vacation_editor.delete_vacation", "Delete vacation") + " ]");
				delete.addParameter(PARAMETER_VACATION_PK, vacationDay.getPrimaryKey().toString());
				delete.addParameter(PARAMETER_ACTION, ACTION_DELETE);

				cell = row.createCell();
				cell.setStyleClass("firstColumn");
				cell.add(new Text(validFrom.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)));
				
				if (!validTo.isEqualTo(validFrom)) {
					row.createCell().add(new Text(validTo.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)));
				}
				else {
					row.createCell().add(new Text("-"));
				}
				row.createCell().add(new Text(localize("vacation_type." + vacationDay.getType(), vacationDay.getType())));
				if (vacationDay.getName() != null) {
					row.createCell().add(new Text(vacationDay.getName()));
				}
				else {
					row.createCell().add(new Text("-"));
				}
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
		
		Layer buttonLayer = new Layer(Layer.DIV);
		buttonLayer.setStyleClass("buttonDiv");
		form.add(buttonLayer);
		
		SubmitButton newLink = (SubmitButton) getButton(new SubmitButton(localize("new_vacation", "New vacation"), PARAMETER_ACTION, String.valueOf(ACTION_NEW)));
		newLink.setStyleClass("button");
		buttonLayer.add(newLink);

		add(form);
	}
	
	private void showEditor(IWContext iwc) throws RemoteException {
		Object vacationPK = iwc.getParameter(PARAMETER_VACATION_PK);
		if (vacationPK != null) {
			try {
				MealVacationDay vacationDay = getBusiness().getVacationDay(vacationPK);
				showEditor(vacationDay);
			}
			catch (FinderException fe) {
				fe.printStackTrace();
			}
		}
		else {
			showEditor((MealVacationDay) null);
		}
	}
	
	private void showEditor(MealVacationDay vacationDay) {
		Form form = new Form();
		form.setID(STYLENAME_MEAL_FORM);
		form.addParameter(PARAMETER_ACTION, String.valueOf(vacationDay != null ? ACTION_EDIT : ACTION_NEW));

		Layer layer = new Layer(Layer.DIV);
		layer.setID("vacationsDiv");
		form.add(layer);
		
		layer.add(new Heading1(localize("vacation_editor.vacation_edit", "Vacation edit")));
		
		DateInput validFrom = new DateInput(PARAMETER_VALID_FROM);
		validFrom.keepStatusOnAction(true);
		DateInput validTo = new DateInput(PARAMETER_VALID_TO);
		validTo.keepStatusOnAction(true);
		DropdownMenu types = new DropdownMenu(PARAMETER_TYPE);
		types.addMenuElement(MealConstants.TYPE_PUBLIC_HOLIDAY, localize("vacation_type." + MealConstants.TYPE_PUBLIC_HOLIDAY, MealConstants.TYPE_PUBLIC_HOLIDAY));
		types.addMenuElement(MealConstants.TYPE_TEACHER_WORK_DAY, localize("vacation_type." + MealConstants.TYPE_TEACHER_WORK_DAY, MealConstants.TYPE_TEACHER_WORK_DAY));
		TextInput name = new TextInput(PARAMETER_NAME);
		name.keepStatusOnAction(true);
		
		if (vacationDay != null) {
			form.add(new HiddenInput(PARAMETER_VACATION_PK, vacationDay.getPrimaryKey().toString()));
			
			validFrom.setDate(vacationDay.getValidFrom());
			if (!vacationDay.getValidTo().equals(vacationDay.getValidFrom())) {
				validTo.setDate(vacationDay.getValidTo());
			}
			types.setSelectedElement(vacationDay.getType());
			if (vacationDay.getName() != null) {
				name.setContent(vacationDay.getName());
			}
		}
		
		Layer formElement = new Layer(Layer.DIV);
		formElement.setStyleClass("formElement");
		Label label = new Label(localize("vacation_editor.valid_from", "Valid from"), validFrom);
		formElement.add(label);
		formElement.add(validFrom);
		layer.add(formElement);

		formElement = new Layer(Layer.DIV);
		formElement.setStyleClass("formElement");
		label = new Label(localize("vacation_editor.valid_to", "Valid to"), validTo);
		formElement.add(label);
		formElement.add(validTo);
		layer.add(formElement);

		formElement = new Layer(Layer.DIV);
		formElement.setStyleClass("formElement");
		label = new Label(localize("vacation_editor.type", "Type"), types);
		formElement.add(label);
		formElement.add(types);
		layer.add(formElement);
		
		formElement = new Layer(Layer.DIV);
		formElement.setStyleClass("formElement");
		label = new Label(localize("vacation_editor.name", "Name"), name);
		formElement.add(label);
		formElement.add(name);
		layer.add(formElement);
		
		Layer buttonLayer = new Layer(Layer.DIV);
		buttonLayer.setStyleClass("buttonDiv");
		layer.add(buttonLayer);
		
		SubmitButton back = (SubmitButton) getButton(new SubmitButton(localize("back", "Back")));
		back.setStyleClass("button");
		back.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_VIEW));
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("save", "Save")));
		next.setStyleClass("button");
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_SAVE));
		buttonLayer.add(back);
		buttonLayer.add(next);

		add(form);
	}
	
	private void save(IWContext iwc) throws RemoteException {
		Object vacationPK = iwc.getParameter(PARAMETER_VACATION_PK);

		boolean validates = true;
		if (!iwc.isParameterSet(PARAMETER_VALID_FROM)) {
			getParentPage().setAlertOnLoad(localize("vacation_editor.must_enter_dates", "You must enter from date."));
			validates = false;
		}
		
		if (!validates) {
			showEditor(iwc);
		}
		
		IWTimestamp validFrom = new IWTimestamp(iwc.getParameter(PARAMETER_VALID_FROM));
		IWTimestamp validTo = iwc.isParameterSet(PARAMETER_VALID_TO) ? new IWTimestamp(iwc.getParameter(PARAMETER_VALID_TO)) : validFrom;
		
		String type = iwc.getParameter(PARAMETER_TYPE);
		String name = iwc.getParameter(PARAMETER_NAME);
		
		try {
			getBusiness().storeVacationDays(vacationPK, getSession().getSchool(), validFrom.getDate(), validTo.getDate(), type, name);
			showVacationList(iwc);
		}
		catch (IDOCreateException ice) {
			ice.printStackTrace();
			getParentPage().setAlertOnLoad(localize("vacation_editor.error_while_saving", "An error occured while saving, please try again."));
			showEditor(iwc);
		}
	}
	
	private void delete(IWContext iwc) throws RemoteException {
		try {
			getBusiness().deleteVacationDay(iwc.getParameter(PARAMETER_VACATION_PK));
			showVacationList(iwc);
		}
		catch (RemoveException re) {
			re.printStackTrace();
		}
	}
	
	private int parseAction(IWContext iwc) {
		int action = ACTION_VIEW;
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		
		return action;
	}
}