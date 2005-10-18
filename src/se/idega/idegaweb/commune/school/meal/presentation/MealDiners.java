/*
 * $Id$
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
import se.idega.idegaweb.commune.school.meal.data.MealChoice;
import se.idega.idegaweb.commune.school.meal.data.MealChoiceMonth;
import com.idega.block.school.data.SchoolSeason;
import com.idega.business.IBORuntimeException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableColumn;
import com.idega.presentation.TableColumnGroup;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Heading1;
import com.idega.presentation.text.ListItem;
import com.idega.presentation.text.Lists;
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.User;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;


public class MealDiners extends MealBlock {

	private static final String PARAMETER_ACTION = "prm_action";
	private static final String PARAMETER_MONTH = "prm_month";
	private static final String PARAMETER_SHOW = "prm_show";
	
	private static final int ACTION_SELECT = 1;
	private static final int ACTION_VIEW = 2;
	
	private static final int SHOW_STUDENTS = 1;
	private static final int SHOW_TEACHERS = 2;
	private static final int SHOW_ALL = 3;
	
	private SchoolSeason season;
	
	public void present(IWContext iwc) {
		try {
			if (getSession().getSchool() != null) {
				try {
					season = getBusiness().getOngoingSeason();
				}
				catch (FinderException fe) {
					try {
						season = getBusiness().getNextSeason();
					}
					catch (FinderException fex) {
						add(getErrorText(localize("no_season_found", "No season found.")));
						return;
					}
				}

				switch (parseAction(iwc)) {
					case ACTION_SELECT:
						showSelector(iwc);
						break;
		
					case ACTION_VIEW:
						showDiners(iwc);
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
	
	private void showSelector(IWContext iwc) {
		Form form = new Form();
		form.setID("mealForm");
		form.addParameter(PARAMETER_ACTION, ACTION_SELECT);
		
		Layer layer = new Layer(Layer.DIV);
		layer.setID("dinersDiv");
		form.add(layer);
		
		layer.add(new Heading1(localize("diners.select", "Select")));
		
		Paragraph paragraph = new Paragraph();
		paragraph.add(new Text(localize("diners.select_information", "Select information")));
		layer.add(paragraph);
		
		IWCalendar calendar = new IWCalendar();
		IWTimestamp seasonStart = new IWTimestamp(season.getSchoolSeasonStart());
		IWTimestamp seasonEnd = new IWTimestamp(season.getSchoolSeasonEnd());
		
		Lists dinersList = new Lists();
		dinersList.setID("dinersList");
		layer.add(dinersList);
		
		ListItem item = new ListItem();
		RadioButton button = new RadioButton(PARAMETER_SHOW, String.valueOf(SHOW_STUDENTS));
		button.setStyleClass("checkbox");
		button.setSelected(true);
		button.keepStatusOnAction(true);
		Label label = new Label(localize("diners.show_students", "Show students"), button);
		item.add(button);
		item.add(label);
		dinersList.add(item);
		
		item = new ListItem();
		button = new RadioButton(PARAMETER_SHOW, String.valueOf(SHOW_TEACHERS));
		button.setStyleClass("checkbox");
		button.keepStatusOnAction(true);
		label = new Label(localize("diners.show_teacher", "Show teachers"), button);
		item.add(button);
		item.add(label);
		dinersList.add(item);
		
		item = new ListItem();
		button = new RadioButton(PARAMETER_SHOW, String.valueOf(SHOW_ALL));
		button.setStyleClass("checkbox");
		button.keepStatusOnAction(true);
		label = new Label(localize("diners.show_all", "Show all"), button);
		item.add(button);
		item.add(label);
		dinersList.add(item);
		
		paragraph = new Paragraph();
		paragraph.add(new Text(localize("diners.month_information", "Month information")));
		layer.add(paragraph);
		
		Lists monthList = new Lists();
		monthList.setID("monthList");
		boolean selected = false;
		while (seasonStart.isEarlierThan(seasonEnd)) {
			item = new ListItem();
			button = new RadioButton(PARAMETER_MONTH, seasonStart.toString());
			button.setStyleClass("checkbox");
			button.keepStatusOnAction(true);
			if (!selected) {
				button.setSelected(true);
				selected = true;
			}
			label = new Label(calendar.getMonthName(seasonStart.getMonth(), iwc.getCurrentLocale(), IWCalendar.FULL), button);
			
			item.add(button);
			item.add(label);
			monthList.add(item);
			
			seasonStart.addMonths(1);
		}
		layer.add(monthList);
		
		Layer buttonLayer = new Layer(Layer.DIV);
		buttonLayer.setStyleClass("buttonDiv");
		layer.add(buttonLayer);
		
		SubmitButton next = new SubmitButton(localize("view", "View"));
		next.setStyleClass("button");
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_VIEW));
		buttonLayer.add(next);

		add(form);
	}
	
	private void showDiners(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.setID(STYLENAME_MEAL_FORM);
		form.maintainParameter(PARAMETER_MONTH);
		form.maintainParameter(PARAMETER_SHOW);
		
		IWTimestamp stamp = new IWTimestamp(iwc.getParameter(PARAMETER_MONTH));
		Boolean showEmployees = null;
		switch (Integer.parseInt(iwc.getParameter(PARAMETER_SHOW))) {
			case SHOW_STUDENTS:
				showEmployees = new Boolean(false);
				break;
				
			case SHOW_TEACHERS:
				showEmployees = new Boolean(true);
				break;
				
			case SHOW_ALL:
				showEmployees = null;
				break;
		}
		
		IWCalendar calendar = new IWCalendar();
		Heading1 heading = new Heading1(calendar.getMonthName(stamp.getMonth(), iwc.getCurrentLocale(), IWCalendar.FULL));
		heading.setStyleClass("heading");
		form.add(heading);

		Table2 table = new Table2();
		table.setWidth("100%");
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setStyleClass(STYLENAME_LIST_TABLE);
		
		TableColumnGroup columnGroup = table.createColumnGroup();
		TableColumn column = columnGroup.createColumn();
		column.setSpan(2);
		column = columnGroup.createColumn();
		column.setSpan(7);
		column.setCellHorizontalAlignment(Table2.HORIZONTAL_ALIGNMENT_CENTER);
		
		Collection diners = getBusiness().getSchoolDiners(getSession().getSchool(), stamp.getDate(), showEmployees);

		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		TableCell2 cell = row.createHeaderCell();
		cell.setStyleClass("firstColumn");
		cell.add(new Text(localize("diners.name", "Name")));

		row.createHeaderCell().add(new Text(localize("diners.personal_id", "Personal ID")));
		row.createHeaderCell().add(new Text(localize("mon", "Mon")));
		row.createHeaderCell().add(new Text(localize("tue", "Tue")));
		row.createHeaderCell().add(new Text(localize("wed", "Wed")));
		row.createHeaderCell().add(new Text(localize("thu", "Thu")));
		row.createHeaderCell().add(new Text(localize("fri", "Fri")));
		row.createHeaderCell().add(new Text(localize("milk", "Milk")));
		
		cell = row.createHeaderCell();
		cell.setStyleClass("lastColumn");
		cell.add(new Text(localize("fruits", "Fruits")));
		
		group = table.createBodyRowGroup();
		int iRow = 1;
		
		int mondays = 0;
		int tuesdays = 0;
		int wednesdays = 0;
		int thursdays = 0;
		int fridays = 0;
		int milk = 0;
		int fruits = 0;
		Iterator iter = diners.iterator();
		while (iter.hasNext()) {
			row = group.createRow();
			MealChoiceMonth dinerMonth = (MealChoiceMonth) iter.next();
			MealChoice choice = dinerMonth.getChoice();
			User user = choice.getUser();
			Name name = new Name(user.getFirstName(), user.getMiddleName(), user.getLastName());
			
			try {
				cell = row.createCell();
				cell.setStyleClass("firstColumn");
				cell.add(new Text(name.getName(iwc.getCurrentLocale())));

				row.createCell().add(new Text(user.getPersonalID() != null ? PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale()) : "-"));
				
				if (dinerMonth.hasMondays()) {
					row.createCell().add(new Text("X"));
					mondays++;
				}
				else {
					row.createCell().add(new Text("-"));
				}
				if (dinerMonth.hasTuesdays()) {
					row.createCell().add(new Text("X"));
					tuesdays++;
				}
				else {
					row.createCell().add(new Text("-"));
				}
				if (dinerMonth.hasWednesdays()) {
					row.createCell().add(new Text("X"));
					wednesdays++;
				}
				else {
					row.createCell().add(new Text("-"));
				}
				if (dinerMonth.hasThursdays()) {
					row.createCell().add(new Text("X"));
					thursdays++;
				}
				else {
					row.createCell().add(new Text("-"));
				}
				if (dinerMonth.hasFridays()) {
					row.createCell().add(new Text("X"));
					fridays++;
				}
				else {
					row.createCell().add(new Text("-"));
				}
				if (dinerMonth.hasMilk()) {
					row.createCell().add(new Text("X"));
					milk++;
				}
				else {
					row.createCell().add(new Text("-"));
				}
				cell = row.createCell();
				cell.setStyleClass("lastColumn");
				if (dinerMonth.hasFruits()) {
					cell.add(new Text("X"));
					fruits++;
				}
				else {
					cell.add(new Text("-"));
				}
				
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
			
			if (iter.hasNext()) {
				iRow++;
			}
		}
		
		group = table.createFooterRowGroup();
		row = group.createRow();
		cell = row.createCell();
		cell.setColumnSpan(2);
		cell.add(new Text(localize("diners.total", "Total")));
		row.createCell().add(new Text(String.valueOf(mondays)));
		row.createCell().add(new Text(String.valueOf(tuesdays)));
		row.createCell().add(new Text(String.valueOf(wednesdays)));
		row.createCell().add(new Text(String.valueOf(thursdays)));
		row.createCell().add(new Text(String.valueOf(fridays)));
		row.createCell().add(new Text(String.valueOf(milk)));
		row.createCell().add(new Text(String.valueOf(fruits)));

		form.add(table);
		
		Layer buttonLayer = new Layer(Layer.DIV);
		buttonLayer.setStyleClass("buttonDiv");
		form.add(buttonLayer);
		
		SubmitButton back = new SubmitButton(localize("back", "Back"), PARAMETER_ACTION, String.valueOf(ACTION_SELECT));
		back.setStyleClass("button");
		buttonLayer.add(back);

		add(form);
	}

	private int parseAction(IWContext iwc) {
		int action = ACTION_SELECT;
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		
		return action;
	}
}