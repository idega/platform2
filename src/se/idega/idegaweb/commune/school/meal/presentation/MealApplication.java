/*
 * $Id: MealApplication.java,v 1.8 2005/10/18 09:05:35 laddi Exp $
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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.school.meal.business.MonthValues;
import se.idega.idegaweb.commune.school.meal.util.MealConstants;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolSeason;
import com.idega.business.IBORuntimeException;
import com.idega.core.builder.data.ICPage;
import com.idega.data.IDOCreateException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Table;
import com.idega.presentation.text.Heading1;
import com.idega.presentation.text.ListItem;
import com.idega.presentation.text.Lists;
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.user.data.User;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;


/**
 * Last modified: $Date: 2005/10/18 09:05:35 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.8 $
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
	
	private User user;
	private School school;
	private SchoolSeason season;
	private SchoolClass group;
	private SchoolClassMember placement;
	
	private boolean iUseEmployeeView = false;
	private ICPage iHomePage;

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
			user = getUser(iwc);
			if (!isEmployeeView()) {
				placement = getBusiness().getSchoolPlacing(getUser(iwc), season);
				if (placement == null) {
					add(getErrorText(localize("no_placement_found", "No placement found for user")));
					return;
				}
				else {
					group = placement.getSchoolClass();
					school = group.getSchool();
					user = placement.getStudent();
				}
			}
			else {
				school = getSession().getSchool();
				user = iwc.getCurrentUser();
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
		
		form.add(getPersonInfo(iwc, user, school, group));
		
		Layer layer = new Layer(Layer.DIV);
		layer.setID("phasesDiv");
		form.add(layer);
		
		layer.add(new Heading1(localize("application.months", "Months")));
		
		Paragraph paragraph = new Paragraph();
		paragraph.add(new Text(localize("application.months_information", "Months information")));
		layer.add(paragraph);
		
		IWCalendar calendar = new IWCalendar();
		IWTimestamp seasonStart = new IWTimestamp(season.getSchoolSeasonStart());
		IWTimestamp seasonEnd = new IWTimestamp(season.getSchoolSeasonEnd());
		
		Lists monthList = new Lists();
		monthList.setID("monthList");
		while (seasonStart.isEarlierThan(seasonEnd)) {
			ListItem item = new ListItem();
			CheckBox box = new CheckBox(PARAMETER_MONTH, seasonStart.toString());
			box.setStyleClass("checkbox");
			box.keepStatusOnAction(true);
			if (getBusiness().hasChoiceForDate(user, school, season, seasonStart.getDate())) {
				box.setChecked(true);
				box.setDisabled(true);
			}
			Label label = new Label(TextSoap.capitalize(calendar.getMonthName(seasonStart.getMonth(), iwc.getCurrentLocale(), IWCalendar.FULL)), box);
			
			item.add(box);
			item.add(label);
			monthList.add(item);
			
			seasonStart.addMonths(1);
		}
		layer.add(monthList);
		
		Layer buttonLayer = new Layer(Layer.DIV);
		buttonLayer.setStyleClass("buttonDiv");
		layer.add(buttonLayer);
		
		SubmitButton next = new SubmitButton(localize("next", "Next"));
		next.setStyleClass("button");
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_TWO));
		buttonLayer.add(next);
		
		buttonLayer.add(getHelpButton("meal_application_help"));

		add(form);
	}
	
	private void showPhaseTwo(IWContext iwc) throws RemoteException {
		Form form = createForm(iwc, ACTION_PHASE_TWO);
		
		form.add(getPersonInfo(iwc, user, school, group));

		Layer layer = new Layer(Layer.DIV);
		layer.setID("phasesDiv");
		form.add(layer);
		
		layer.add(new Heading1(localize("application.days", "Days")));

		Paragraph paragraph = new Paragraph();
		paragraph.add(new Text(localize("application.days_information", "Days information")));
		layer.add(paragraph);
		
		String[] months = iwc.getParameterValues(PARAMETER_MONTH);
		if (months == null || months.length == 0) {
			getParentPage().setAlertOnLoad(localize("must_select_month", "You must select at least one month"));
			showPhaseOne(iwc);
			return;
		}
		
		for (int i = 0; i < months.length; i++) {
			IWTimestamp month = new IWTimestamp(months[i]);
			layer.add(getMonth(month, iwc.getCurrentLocale()));
		}
		
		Layer clear = new Layer(Layer.DIV);
		clear.setStyleClass("Clear");
		layer.add(clear);
		
		Layer buttonLayer = new Layer(Layer.DIV);
		buttonLayer.setStyleClass("buttonDiv");
		layer.add(buttonLayer);
		
		SubmitButton previous = new SubmitButton(localize("previous", "Previous"));
		previous.setStyleClass("button");
		previous.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_ONE));
		SubmitButton next = new SubmitButton(localize("next", "Next"));
		next.setStyleClass("button");
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_THREE));
		buttonLayer.add(previous);
		buttonLayer.add(next);
		buttonLayer.add(getHelpButton("meal_application_help"));

		add(form);
	}
	
	private Layer getMonth(IWTimestamp month, Locale locale) {
		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass("dayItem");
		
		IWCalendar calendar = new IWCalendar();
		
		layer.add(new Heading1(TextSoap.capitalize(calendar.getMonthName(month.getMonth(), locale, IWCalendar.FULL))));

		Lists daysList = new Lists();
		daysList.setID("daysList");
		layer.add(daysList);
		
		CheckBox monday = new CheckBox(PARAMETER_DAYS + "_" + month.toString(), MealConstants.DAY_MONDAY);
		monday.setStyleClass("checkbox");
		monday.keepStatusOnAction(true);
		CheckBox tuesday = new CheckBox(PARAMETER_DAYS + "_" + month.toString(), MealConstants.DAY_TUESDAY);
		tuesday.setStyleClass("checkbox");
		tuesday.keepStatusOnAction(true);
		CheckBox wednesday = new CheckBox(PARAMETER_DAYS + "_" + month.toString(), MealConstants.DAY_WEDNESDAY);
		wednesday.setStyleClass("checkbox");
		wednesday.keepStatusOnAction(true);
		CheckBox thursday = new CheckBox(PARAMETER_DAYS + "_" + month.toString(), MealConstants.DAY_THURSDAY);
		thursday.setStyleClass("checkbox");
		thursday.keepStatusOnAction(true);
		CheckBox friday = new CheckBox(PARAMETER_DAYS + "_" + month.toString(), MealConstants.DAY_FRIDAY);
		thursday.setStyleClass("checkbox");
		friday.keepStatusOnAction(true);
		
		CheckBox milk = new CheckBox(PARAMETER_MILK + "_" + month.toString(), Boolean.TRUE.toString());
		milk.setStyleClass("checkbox");
		milk.keepStatusOnAction(true);
		CheckBox fruits = new CheckBox(PARAMETER_FRUITS + "_" + month.toString(), Boolean.TRUE.toString());
		fruits.setStyleClass("checkbox");
		fruits.keepStatusOnAction(true);
		
		ListItem item = new ListItem();
		Label label = new Label(localize("monday", "Monday"), monday);
		item.add(monday);
		item.add(label);
		daysList.add(item);

		item = new ListItem();
		label = new Label(localize("tuesday", "Tuesday"), tuesday);
		item.add(tuesday);
		item.add(label);
		daysList.add(item);
		
		item = new ListItem();
		label = new Label(localize("wednesday", "Wednesday"), wednesday);
		item.add(wednesday);
		item.add(label);
		daysList.add(item);
		
		item = new ListItem();
		label = new Label(localize("thursday", "Thursday"), thursday);
		item.add(thursday);
		item.add(label);
		daysList.add(item);
		
		item = new ListItem();
		label = new Label(localize("friday", "Friday"), friday);
		item.add(friday);
		item.add(label);
		daysList.add(item);
		
		item = new ListItem();
		item.add(Text.getNonBrakingSpace());
		daysList.add(item);
		
		item = new ListItem();
		label = new Label(localize("milk", "Milk"), milk);
		item.add(milk);
		item.add(label);
		daysList.add(item);
		
		item = new ListItem();
		label = new Label(localize("fruits", "Fruits"), fruits);
		item.add(fruits);
		item.add(label);
		daysList.add(item);
		
		return layer;
	}
	/**
	 * 
	 * @param month
	 * @param locale
	 * @param value MonthValues ... this will be populated in the method
	 * @return
	 */
	private float getMonthInfo(IWContext iwc, IWTimestamp month, MonthValues values, Layer topLayer) throws RemoteException {
		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass("dayItem");
		
		IWCalendar calendar = new IWCalendar();
		
		layer.add(new Heading1(TextSoap.capitalize(calendar.getMonthName(month.getMonth(), iwc.getCurrentLocale(), IWCalendar.FULL))));
		
		String[] days = iwc.getParameterValues(PARAMETER_DAYS + "_" + month.toString());
		if (days != null) {
			for (int j = 0; j < days.length; j++) {
				if (days[j].equals(MealConstants.DAY_MONDAY)) {
					values.setMonday(true);
				}
				if (days[j].equals(MealConstants.DAY_TUESDAY)) {
					values.setTuesday(true);
				}
				if (days[j].equals(MealConstants.DAY_WEDNESDAY)) {
					values.setWednesday(true);
				}
				if (days[j].equals(MealConstants.DAY_THURSDAY)) {
					values.setThursday(true);
				}
				if (days[j].equals(MealConstants.DAY_FRIDAY)) {
					values.setFriday(true);
				}
			}
		}
		values.setFruits(iwc.isParameterSet(PARAMETER_FRUITS + "_" + month.toString()));
		values.setMilk(iwc.isParameterSet(PARAMETER_MILK + "_" + month.toString()));
		try {
			values = getBusiness().calculatePrices(month.getDate(), school, values, isEmployeeView());
		}
		catch (FinderException fe) {
			getParentPage().setAlertOnLoad(localize("application.no_price_found_for_month", "No price was found for month: ") + month.getDateString("MMM. yyyy", iwc.getCurrentLocale()));
			return 0;
		}
		
		List listOfDays = new ArrayList();
		if (values.isMonday()) {
			listOfDays.add(new Text(localize("monday", "Monday")));
		}
		if (values.isTuesday()) {
			listOfDays.add(new Text(localize("tuesday", "Tuesday")));
		}
		if (values.isWednesday()) {
			listOfDays.add(new Text(localize("wednesday", "Wednesday")));
		}
		if (values.isThursday()) {
			listOfDays.add(new Text(localize("thursday", "Thursday")));
		}
		if (values.isFriday()) {
			listOfDays.add(new Text(localize("friday", "Friday")));
		}
		
		if (!listOfDays.isEmpty()) {
			Lists list = new Lists();
			list.setID("dayList");
			Iterator iter = listOfDays.iterator();
			while (iter.hasNext()) {
				ListItem item = new ListItem();
				Text text = (Text) iter.next();
				item.add(text);
				list.add(item);
			}
			layer.add(list);
		}
		
		NumberFormat format = NumberFormat.getCurrencyInstance(iwc.getCurrentLocale());
		Lists list = new Lists();
		list.setID("priceList");
		layer.add(list);
		
		ListItem item = new ListItem();
		item.add(new Text(localize("meal", "Meal") + ": " + format.format(values.getMealAmount())));
		
		item = new ListItem();
		item.add(new Text(localize("milk", "Milk") + ": " + format.format(values.getMilkAmount())));
		
		item = new ListItem();
		item.add(new Text(localize("fruits", "Fruits") + ": " + format.format(values.getFruitAmount())));

		layer.add(new HiddenInput(PARAMETER_AMOUNT + "_" + month.toString(), String.valueOf(values.getAmount())));
		topLayer.add(layer);
		
		return values.getAmount();
	}
	
	private void showPhaseThree(IWContext iwc) throws RemoteException {
		Form form = createForm(iwc, ACTION_PHASE_THREE);
		
		form.add(getPersonInfo(iwc, user, school, group));
		
		Layer layer = new Layer(Layer.DIV);
		layer.setID("phasesDiv");
		form.add(layer);
		
		layer.add(new Heading1(localize("application.comments", "Comments")));
		
		Paragraph paragraph = new Paragraph();
		paragraph.add(new Text(localize("application.comments_information", "Comments information")));
		layer.add(paragraph);
		
		TextArea comments = new TextArea(PARAMETER_COMMENTS);
		comments.setWidth(Table.HUNDRED_PERCENT);
		comments.setRows(8);
		comments.keepStatusOnAction();
		layer.add(comments);
		
		Layer buttonLayer = new Layer(Layer.DIV);
		buttonLayer.setStyleClass("buttonDiv");
		layer.add(buttonLayer);
		
		SubmitButton previous = new SubmitButton(localize("previous", "Previous"));
		previous.setStyleClass("button");
		previous.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_TWO));
		SubmitButton next = new SubmitButton(localize("next", "Next"));
		next.setStyleClass("button");
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_OVERVIEW));
		buttonLayer.add(previous);
		buttonLayer.add(next);
		buttonLayer.add(getHelpButton("meal_application_help"));

		add(form);
	}
	
	private void showOverview(IWContext iwc) throws RemoteException {
		Form form = createForm(iwc, ACTION_OVERVIEW);
		
		form.add(isEmployeeView() ? getPersonInfo(iwc, iwc.getCurrentUser(), null, null) : getPersonInfo(iwc, placement));
		
		Layer layer = new Layer(Layer.DIV);
		layer.setID("phasesDiv");
		form.add(layer);
		
		layer.add(new Heading1(localize("application.overview", "Overview")));
		
		Paragraph paragraph = new Paragraph();
		paragraph.add(new Text(localize("application.overview_information", "Overview information")));
		layer.add(paragraph);
		
		if (iwc.isParameterSet(PARAMETER_COMMENTS)) {
			layer.add(new Heading1(localize("application.comments", "Comments")));
			
			paragraph = new Paragraph();
			paragraph.add(new Text(iwc.getParameter(PARAMETER_COMMENTS)));
			layer.add(paragraph);
		}

		boolean canSave = true;
		String[] months = iwc.getParameterValues(PARAMETER_MONTH);
		float totalPrice = 0;
		for (int i = 0; i < months.length; i++) {
			IWTimestamp month = new IWTimestamp(months[i]);
			MonthValues values = new MonthValues();
			float monthPrice = getMonthInfo(iwc, month, values, layer);
			if (monthPrice == 0) {
				canSave = false;
			}
			totalPrice += monthPrice;
		}

		Layer clear = new Layer(Layer.DIV);
		clear.setStyleClass("Clear");
		layer.add(clear);
		
		NumberFormat format = NumberFormat.getCurrencyInstance(iwc.getCurrentLocale());
		Layer priceLayer = new Layer(Layer.DIV);
		priceLayer.setID("priceDiv");
		priceLayer.add(new Text(localize("total_payment", "Total payment") + " : " + format.format(totalPrice)));
		layer.add(priceLayer);
				
		Layer buttonLayer = new Layer(Layer.DIV);
		buttonLayer.setStyleClass("buttonDiv");
		layer.add(buttonLayer);
		
		SubmitButton save = new SubmitButton(localize("save", "Save"));
		save.setStyleClass("button");
		save.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_SAVE));
		save.setDisabled(!canSave);
		SubmitButton previous = new SubmitButton(localize("previous", "Previous"));
		previous.setStyleClass("button");
		previous.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_THREE));
		buttonLayer.add(previous);
		buttonLayer.add(save);
		buttonLayer.add(getHelpButton("meal_application_help"));

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
			getBusiness().storeChoice(null, user, school, season, comments, months, values, iwc.getCurrentUser());
			
			Layer layer = new Layer(Layer.DIV);
			layer.setID("phasesDiv");
			add(layer);
			
			layer.add(new Heading1(localize("application.save_completed", "Application sent")));

			Paragraph paragraph = new Paragraph();
			paragraph.add(new Text(localize("application.save_confirmation", "Your application for a school meal has been sent and processed.")));
			layer.add(paragraph);
			
			if (getHomePage() != null) {
				Layer buttonLayer = new Layer(Layer.DIV);
				buttonLayer.setStyleClass("buttonDiv");
				layer.add(buttonLayer);
				
				GenericButton home = new GenericButton(localize("my_page", "My page"));
				home.setStyleClass("button");
				home.setPageToOpen(getHomePage());
			}
		}
		catch (IDOCreateException ice) {
			ice.printStackTrace();
		}
	}
	
	private Form createForm(IWContext iwc, int actionPhase) {
		Form form = new Form();
		form.setID("mealForm");
		if (actionPhase != ACTION_PHASE_ONE) {
			form.maintainParameter(PARAMETER_MONTH);
		}
		if (actionPhase != ACTION_PHASE_THREE) {
			form.maintainParameter(PARAMETER_COMMENTS);
		}
		form.addParameter(PARAMETER_ACTION, actionPhase);
		
		if (actionPhase != ACTION_PHASE_TWO) {
			String[] months = iwc.getParameterValues(PARAMETER_MONTH);
			if (months != null) {
				for (int i = 0; i < months.length; i++) {
					String month = months[i];
					form.maintainParameter(PARAMETER_DAYS + "_" + month);
					form.maintainParameter(PARAMETER_MILK + "_" + month);
					form.maintainParameter(PARAMETER_FRUITS + "_" + month);
				}
			}
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

	private boolean isEmployeeView() {
		return iUseEmployeeView;
	}
	
	public void setUseEmployeeView(boolean useEmployeeView) {
		iUseEmployeeView = useEmployeeView;
	}
	
	private ICPage getHomePage() {
		return iHomePage;
	}
	
	public void setHomePage(ICPage page) {
		iHomePage = page;
	}
}