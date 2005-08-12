/*
 * $Id: MealApplication.java,v 1.2 2005/08/12 08:56:07 gimmi Exp $
 * Created on Aug 10, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.meal.presentation;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.school.meal.business.MonthValues;
import se.idega.idegaweb.commune.school.meal.util.MealConstants;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolSeason;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOCreateException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.user.data.User;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;


/**
 * Last modified: $Date: 2005/08/12 08:56:07 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public class MealApplication extends MealBlock {
	
	private static final String PARAMETER_ACTION = "prm_action";
	
	private static final String PARAMETER_MONTH = "prm_month";
	private static final String PARAMETER_DAYS = "prm_days";
	private static final String PARAMETER_MILK = "prm_milk";
	private static final String PARAMETER_FRUITS = "prm_fruits";
	private static final String PARAMETER_COMMENTS = "prm_comments";
	private static final String PARAMETER_AMOUNT = "prm_amount";
	
	private static final int ACTION_PHASE_ONE = 1;
	private static final int ACTION_PHASE_TWO = 2;
	private static final int ACTION_PHASE_THREE = 3;
	private static final int ACTION_OVERVIEW = 4;
	private static final int ACTION_SAVE = 5;
	
	private SchoolSeason season;
	private SchoolClassMember placement;
	
	private boolean iUseEmployeeView = false;

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.school.meal.presentation.MealBlock#present(com.idega.presentation.IWContext)
	 */
	public void present(IWContext iwc) {
		try {
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
			placement = getBusiness().getSchoolPlacing(getUser(iwc), season);
			if (placement == null && !isEmployeeView()) {
				add(getErrorText(localize("no_placement_found", "No placement found for user")));
				return;
			}
			
			switch (parseAction(iwc)) {
				case ACTION_PHASE_ONE:
					showPhaseOne(iwc);
					break;
					
				case ACTION_PHASE_TWO:
					showPhaseTwo(iwc);
					break;

				case ACTION_PHASE_THREE:
					showPhaseThree(iwc);
					break;

				case ACTION_OVERVIEW:
					showOverview(iwc);
					break;

				case ACTION_SAVE:
					save(iwc);
					break;
}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	private void showPhaseOne(IWContext iwc) throws RemoteException {
		Form form = createForm(iwc, ACTION_PHASE_ONE);
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		if (!isEmployeeView()) {
			table.add(getPersonInfoTable(iwc, placement), 1, row++);
			table.setHeight(row++, 6);
		}
		
		table.add(getHeader(localize("application.months", "Months")), 1, row++);
		table.setHeight(row++, 6);
		
		table.add(getText(localize("application.months_information", "Months information")), 1, row++);
		table.setHeight(row++, 12);
		
		IWCalendar calendar = new IWCalendar();
		IWTimestamp seasonStart = new IWTimestamp(season.getSchoolSeasonStart());
		IWTimestamp seasonEnd = new IWTimestamp(season.getSchoolSeasonEnd());
		
		while (seasonStart.isEarlierThan(seasonEnd)) {
			CheckBox box = getCheckBox(PARAMETER_MONTH, seasonStart.toString());
			box.keepStatusOnAction(true);
			
			table.add(box, 1, row);
			table.add(Text.getNonBrakingSpace(), 1, row);
			table.add(calendar.getMonthName(seasonStart.getMonth(), iwc.getCurrentLocale(), IWCalendar.FULL), 1, row++);
			
			seasonStart.addMonths(1);
		}
		
		table.setHeight(row++, 18);
		
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("next", "Next"), PARAMETER_ACTION, String.valueOf(ACTION_PHASE_TWO)));
		
		table.add(next, 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setCellpaddingRight(1, row, 12);

		add(form);
	}
	
	private void showPhaseTwo(IWContext iwc) throws RemoteException {
		Form form = createForm(iwc, ACTION_PHASE_TWO);
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		if (!isEmployeeView()) {
			table.add(getPersonInfoTable(iwc, placement), 1, row++);
			table.setHeight(row++, 6);
		}
		
		table.add(getHeader(localize("application.days", "Days")), 1, row++);
		table.setHeight(row++, 6);
		
		table.add(getText(localize("application.days_information", "Days information")), 1, row++);
		table.setHeight(row++, 12);
		
		String[] months = iwc.getParameterValues(PARAMETER_MONTH);
		if (months.length == 0) {
			getParentPage().setAlertOnLoad(localize("must_select_month", "You must select at least one month"));
			showPhaseOne(iwc);
			return;
		}
		
		Table dayTable = new Table();
		dayTable.setColumns(3);
		dayTable.setCellspacing(5);
		dayTable.setCellpadding(0);
		dayTable.setWidth(Table.HUNDRED_PERCENT);
		table.add(dayTable, 1, row++);
		int dColumn = 1;
		int dRow = 1;
		
		for (int i = 0; i < months.length; i++) {
			IWTimestamp month = new IWTimestamp(months[i]);
			dayTable.add(getMonth(month, iwc.getCurrentLocale()), dColumn++, dRow);
			
			if ((i + 1) % 3 == 0) {
				dColumn = 1;
				dRow++;
			}
		}
		dayTable.setWidth(1, "33%");
		dayTable.setWidth(2, "33%");
		dayTable.setWidth(3, "33%");
		
		table.setHeight(row++, 18);
		
		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(localize("previous", "Previous"), PARAMETER_ACTION, String.valueOf(ACTION_PHASE_ONE)));
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("next", "Next"), PARAMETER_ACTION, String.valueOf(ACTION_PHASE_THREE)));
		
		table.add(previous, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(next, 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setCellpaddingRight(1, row, 12);

		add(form);
	}
	
	private Table getMonth(IWTimestamp month, Locale locale) {
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		int row = 1;
		
		IWCalendar calendar = new IWCalendar();
		
		table.add(getSmallHeader(calendar.getMonthName(month.getMonth(), locale, IWCalendar.FULL)), 1, row++);
		table.setHeight(row++, 6);
		
		CheckBox monday = getCheckBox(PARAMETER_DAYS + "_" + month.toString(), MealConstants.DAY_MONDAY);
		monday.keepStatusOnAction(true);
		CheckBox tuesday = getCheckBox(PARAMETER_DAYS + "_" + month.toString(), MealConstants.DAY_TUESDAY);
		tuesday.keepStatusOnAction(true);
		CheckBox wednesday = getCheckBox(PARAMETER_DAYS + "_" + month.toString(), MealConstants.DAY_WEDNESDAY);
		wednesday.keepStatusOnAction(true);
		CheckBox thursday = getCheckBox(PARAMETER_DAYS + "_" + month.toString(), MealConstants.DAY_THURSDAY);
		thursday.keepStatusOnAction(true);
		CheckBox friday = getCheckBox(PARAMETER_DAYS + "_" + month.toString(), MealConstants.DAY_FRIDAY);
		friday.keepStatusOnAction(true);
		
		CheckBox milk = getCheckBox(PARAMETER_MILK + "_" + month.toString(), Boolean.TRUE.toString());
		milk.keepStatusOnAction(true);
		CheckBox fruits = getCheckBox(PARAMETER_FRUITS + "_" + month.toString(), Boolean.TRUE.toString());
		fruits.keepStatusOnAction(true);
		
		table.add(monday, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getSmallText(localize("monday", "Monday")), 1, row++);
		
		table.add(tuesday, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getSmallText(localize("tuesday", "Tuesday")), 1, row++);
		
		table.add(wednesday, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getSmallText(localize("wednesday", "Wednesday")), 1, row++);
		
		table.add(thursday, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getSmallText(localize("thursday", "Thursday")), 1, row++);
		
		table.add(friday, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getSmallText(localize("friday", "Friday")), 1, row++);
		
		table.setHeight(row++, 6);
		
		table.add(milk, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getSmallText(localize("milk", "Milk")), 1, row++);
		
		table.add(fruits, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getSmallText(localize("fruits", "Fruits")), 1, row++);
		
		return table;
	}
	/**
	 * 
	 * @param month
	 * @param locale
	 * @param value MonthValues ... this will be populated in the method
	 * @return
	 */
	private Table getMonthInfo(IWTimestamp month, Locale locale, MonthValues values) {
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		int row = 1;
		
		// TODO populate MonthValues and list um the months
		values.getAmount(); // to remove 'unused variable' warning
		
		IWCalendar calendar = new IWCalendar();
		
		table.add(getSmallHeader(calendar.getMonthName(month.getMonth(), locale, IWCalendar.FULL)), 1, row++);
		table.setHeight(row++, 6);
		
		CheckBox monday = getCheckBox(PARAMETER_DAYS + "_" + month.toString(), MealConstants.DAY_MONDAY);
		monday.keepStatusOnAction(true);
		CheckBox tuesday = getCheckBox(PARAMETER_DAYS + "_" + month.toString(), MealConstants.DAY_TUESDAY);
		tuesday.keepStatusOnAction(true);
		CheckBox wednesday = getCheckBox(PARAMETER_DAYS + "_" + month.toString(), MealConstants.DAY_WEDNESDAY);
		wednesday.keepStatusOnAction(true);
		CheckBox thursday = getCheckBox(PARAMETER_DAYS + "_" + month.toString(), MealConstants.DAY_THURSDAY);
		thursday.keepStatusOnAction(true);
		CheckBox friday = getCheckBox(PARAMETER_DAYS + "_" + month.toString(), MealConstants.DAY_FRIDAY);
		friday.keepStatusOnAction(true);
		
		CheckBox milk = getCheckBox(PARAMETER_MILK + "_" + month.toString(), Boolean.TRUE.toString());
		milk.keepStatusOnAction(true);
		CheckBox fruits = getCheckBox(PARAMETER_FRUITS + "_" + month.toString(), Boolean.TRUE.toString());
		fruits.keepStatusOnAction(true);
		
		table.add(monday, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getSmallText(localize("monday", "Monday")), 1, row++);
		
		table.add(tuesday, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getSmallText(localize("tuesday", "Tuesday")), 1, row++);
		
		table.add(wednesday, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getSmallText(localize("wednesday", "Wednesday")), 1, row++);
		
		table.add(thursday, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getSmallText(localize("thursday", "Thursday")), 1, row++);
		
		table.add(friday, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getSmallText(localize("friday", "Friday")), 1, row++);
		
		table.setHeight(row++, 6);
		
		table.add(milk, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getSmallText(localize("milk", "Milk")), 1, row++);
		
		table.add(fruits, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getSmallText(localize("fruits", "Fruits")), 1, row++);
		
		return table;
	}
	
	private void showPhaseThree(IWContext iwc) throws RemoteException {
		Form form = createForm(iwc, ACTION_PHASE_THREE);
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		if (!isEmployeeView()) {
			table.add(getPersonInfoTable(iwc, placement), 1, row++);
			table.setHeight(row++, 6);
		}
		
		table.add(getHeader(localize("application.comments", "Comments")), 1, row++);
		table.setHeight(row++, 6);
		
		table.add(getText(localize("application.comments_information", "Comments information")), 1, row++);
		table.setHeight(row++, 12);
		
		TextArea comments = (TextArea) getStyledInterface(new TextArea(PARAMETER_COMMENTS));
		comments.setWidth(Table.HUNDRED_PERCENT);
		comments.setRows(8);
		table.add(comments, 1, row++);
		
		table.setHeight(row++, 18);
		
		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(localize("previous", "Previous"), PARAMETER_ACTION, String.valueOf(ACTION_OVERVIEW)));
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("next", "Next"), PARAMETER_ACTION, String.valueOf(ACTION_PHASE_TWO)));
		
		table.add(previous, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(next, 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setCellpaddingRight(1, row, 12);

		add(form);
	}
	
	private void showOverview(IWContext iwc) throws RemoteException {
		Form form = createForm(iwc, ACTION_OVERVIEW);
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		if (!isEmployeeView()) {
			table.add(getPersonInfoTable(iwc, placement), 1, row++);
			table.setHeight(row++, 6);
		}
		
		Table dayTable = new Table();
		dayTable.setColumns(3);
		dayTable.setCellspacing(5);
		dayTable.setCellpadding(0);
		dayTable.setWidth(Table.HUNDRED_PERCENT);
		table.add(dayTable, 1, row++);
		int dColumn = 1;
		int dRow = 1;
		
		String[] months = iwc.getParameterValues(PARAMETER_MONTH);
		MonthValues[] values = new MonthValues[months.length];
		for (int i = 0; i < months.length; i++) {
			IWTimestamp month = new IWTimestamp(months[i]);
			dayTable.add(getMonthInfo(month, iwc.getCurrentLocale(), values[i]), dColumn++, dRow);
			
			if ((i + 1) % 3 == 0) {
				dColumn = 1;
				dRow++;
			}
		}
		dayTable.setWidth(1, "33%");
		dayTable.setWidth(2, "33%");
		dayTable.setWidth(3, "33%");
				
		SubmitButton save = (SubmitButton) getButton(new SubmitButton(localize("save", "Save"), PARAMETER_ACTION, String.valueOf(ACTION_SAVE)));
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("next", "Next"), PARAMETER_ACTION, String.valueOf(ACTION_PHASE_THREE)));
		
		table.add(save, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(next, 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setCellpaddingRight(1, row, 12);

		add(form);
	}
	
	private void save(IWContext iwc) throws RemoteException {
		String comments = iwc.getParameter(PARAMETER_COMMENTS);
		String[] monthsStrings = iwc.getParameterValues(PARAMETER_MONTH);
		Date[] months = new Date[monthsStrings.length];
		Map values = new HashMap();
		
		for (int i = 0; i < monthsStrings.length; i++) {
			String month = monthsStrings[i];
			months[i] = new IWTimestamp(month).getDate();
			String[] days = iwc.getParameterValues(PARAMETER_DAYS + "_" + month);
			
			MonthValues value = new MonthValues();
			value.setMilk(iwc.isParameterSet(PARAMETER_MILK + "_" + month));
			value.setFruits(iwc.isParameterSet(PARAMETER_FRUITS + "_" + month));
			value.setAmount(iwc.isParameterSet(PARAMETER_AMOUNT + "_" + month) ? Float.parseFloat(iwc.getParameter(PARAMETER_AMOUNT + "_" + month)) : 0);
			
			for (int j = 0; j < days.length; j++) {
				String day = days[j];
				value.setMonday(day.equals(MealConstants.DAY_MONDAY));
				value.setTuesday(day.equals(MealConstants.DAY_TUESDAY));
				value.setWednesday(day.equals(MealConstants.DAY_WEDNESDAY));
				value.setThursday(day.equals(MealConstants.DAY_THURSDAY));
				value.setFriday(day.equals(MealConstants.DAY_FRIDAY));
			}
			
			values.put(months[i], value);
		}
		
		try {
			//TODO: Need to use the correct school if no placement exist (for employee that is...)
			getBusiness().storeChoice(null, getUser(iwc), placement != null ? placement.getSchoolClass().getSchool() : null, season, comments, months, values, iwc.getCurrentUser());
		}
		catch (IDOCreateException ice) {
			ice.printStackTrace();
		}
	}
	
	private Form createForm(IWContext iwc, int actionPhase) {
		Form form = new Form();
		form.maintainParameter(PARAMETER_MONTH);
		form.maintainParameter(PARAMETER_COMMENTS);
		form.addParameter(PARAMETER_ACTION, actionPhase);
		
		String[] months = iwc.getParameterValues(PARAMETER_MONTH);
		for (int i = 0; i < months.length; i++) {
			String month = months[i];
			form.maintainParameter(PARAMETER_DAYS + "_" + month);
			form.maintainParameter(PARAMETER_MILK + "_" + month);
			form.maintainParameter(PARAMETER_FRUITS + "_" + month);
		}
		
		return form;
	}
	
	private int parseAction(IWContext iwc) {
		int action = ACTION_PHASE_ONE;
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		
		return action;
	}
	
	private User getUser(IWContext iwc) throws RemoteException {
		User user = getSession().getUser();
		if (user == null) {
			user = iwc.getCurrentUser();
		}
		
		return user;
	}

	public boolean isEmployeeView() {
		return iUseEmployeeView;
	}

	
	public void setUseEmployeeView(boolean useEmployeeView) {
		iUseEmployeeView = useEmployeeView;
	}
}