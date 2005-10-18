/*
 * $Id: PriceEditor.java,v 1.4 2005/10/18 09:05:35 laddi Exp $
 * Created on Sep 30, 2005
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
import se.idega.idegaweb.commune.school.meal.data.MealPrice;
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
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;


/**
 * Last modified: $Date: 2005/10/18 09:05:35 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.4 $
 */
public class PriceEditor extends MealBlock {
	
	private static final String PARAMETER_ACTION = "prm_action";
	
	private static final String PARAMETER_PRICE_PK = "prm_price_pk";
	private static final String PARAMETER_VALID_FROM = "prm_valid_from";
	private static final String PARAMETER_VALID_TO = "prm_valid_to";
	private static final String PARAMETER_USE_MONTH_PRICE = "prm_use_month_price";
	private static final String PARAMETER_MONTH_PRICE = "prm_month_price";
	private static final String PARAMETER_DAY_PRICE = "prm_day_price";
	private static final String PARAMETER_MILK_PRICE = "prm_milk_price";
	private static final String PARAMETER_FRUIT_PRICE = "prm_fruit_price";
	
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
						showPriceList(iwc);
						break;
		
					case ACTION_NEW:
						showEditor(iwc, null);
						break;
		
					case ACTION_EDIT:
						try {
							MealPrice price = getBusiness().getMealPrice(iwc.getParameter(PARAMETER_PRICE_PK));
							showEditor(iwc, price);
						}
						catch (FinderException fe) {
							add(new Text(localize("no_price_found_with_pk", "No price found with primary key...")));
							add(new Break());
							showPriceList(iwc);
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
	
	private void showPriceList(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.setID(STYLENAME_MEAL_FORM);
		
		Table2 table = new Table2();
		table.setWidth("100%");
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setStyleClass(STYLENAME_LIST_TABLE);
		
		TableColumnGroup columnGroup = table.createColumnGroup();
		TableColumn column = columnGroup.createColumn();
		column.setSpan(6);
		column = columnGroup.createColumn();
		column.setSpan(2);
		column.setWidth("12");

		Collection prices = getBusiness().getSchoolPrices(getSession().getSchool());

		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		TableCell2 cell = row.createHeaderCell();
		cell.setStyleClass("firstColumn");
		cell.add(new Text(localize("valid_from", "Valid from")));

		row.createHeaderCell().add(new Text(localize("valid_to", "Valid to")));
		row.createHeaderCell().add(new Text(localize("month_price", "Month price")));
		row.createHeaderCell().add(new Text(localize("day_price", "Day price")));
		row.createHeaderCell().add(new Text(localize("milk_price", "Milk price")));
		row.createHeaderCell().add(new Text(localize("fruit_price", "Fruit price")));
		
		cell = row.createHeaderCell();
		cell.setStyleClass("lastColumn");
		cell.add(Text.getNonBrakingSpace());
		
		row.createHeaderCell().add(Text.getNonBrakingSpace());
		
		group = table.createBodyRowGroup();
		int iRow = 1;
		
		Iterator iter = prices.iterator();
		while (iter.hasNext()) {
			row = group.createRow();
			MealPrice price = (MealPrice) iter.next();
			
			try {
				IWTimestamp validFrom = new IWTimestamp(price.getValidFrom());
				IWTimestamp validTo = new IWTimestamp(price.getValidTo());
				
				Link edit = new Link("[ " + localize("edit_price", "Edit price") + " ]");
				edit.addParameter(PARAMETER_PRICE_PK, price.getPrimaryKey().toString());
				edit.addParameter(PARAMETER_ACTION, ACTION_EDIT);
				
				Link delete = new Link("[ " + localize("delete_price", "Delete price") + " ]");
				delete.addParameter(PARAMETER_PRICE_PK, price.getPrimaryKey().toString());
				delete.addParameter(PARAMETER_ACTION, ACTION_DELETE);

				cell = row.createCell();
				cell.setStyleClass("firstColumn");
				cell.add(new Text(validFrom.getDateString("MMM. yyyy", iwc.getCurrentLocale())));
				
				row.createCell().add(new Text(validTo.getDateString("MMM. yyyy", iwc.getCurrentLocale())));
				if (price.getMealPricePerMonth() != -1) {
					row.createCell().add(new Text(String.valueOf(price.getMealPricePerMonth())));
				}
				else {
					row.createCell().add(new Text("-"));
				}
				if (price.getMealPricePerDay() != -1) {
					row.createCell().add(new Text(String.valueOf(price.getMealPricePerDay())));
				}
				else {
					row.createCell().add(new Text("-"));
				}
				if (price.getMilkPrice() != -1) {
					row.createCell().add(new Text(String.valueOf(price.getMilkPrice())));
				}
				else {
					row.createCell().add(new Text("-"));
				}
				if (price.getFruitsPrice() != -1) {
					row.createCell().add(new Text(String.valueOf(price.getFruitsPrice())));
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
		
		SubmitButton newLink = new SubmitButton(localize("new_price", "New price"), PARAMETER_ACTION, String.valueOf(ACTION_NEW));
		newLink.setStyleClass("button");
		buttonLayer.add(newLink);

		add(form);
	}
	
	private void showEditor(IWContext iwc) throws RemoteException {
		Object pricePK = iwc.getParameter(PARAMETER_PRICE_PK);
		if (pricePK != null) {
			try {
				MealPrice price = getBusiness().getMealPrice(pricePK);
				showEditor(iwc, price);
			}
			catch (FinderException fe) {
				fe.printStackTrace();
			}
		}
		else {
			showEditor(iwc, null);
		}
	}
	
	private void showEditor(IWContext iwc, MealPrice price) {
		Form form = new Form();
		form.setID(STYLENAME_MEAL_FORM);
		form.addParameter(PARAMETER_ACTION, String.valueOf(price != null ? ACTION_EDIT : ACTION_NEW));

		Layer layer = new Layer(Layer.DIV);
		layer.setID("pricesDiv");
		form.add(layer);
		
		layer.add(new Heading1(localize("price_editor.price_list", "Price list")));
		
		DateInput validFrom = new DateInput(PARAMETER_VALID_FROM);
		validFrom.setToShowDay(false);
		validFrom.keepStatusOnAction(true);
		DateInput validTo = new DateInput(PARAMETER_VALID_TO);
		validTo.setToShowDay(false);
		validTo.keepStatusOnAction(true);
		TextInput monthPrice = new TextInput(PARAMETER_MONTH_PRICE);
		monthPrice.keepStatusOnAction(true);
		TextInput dayPrice = new TextInput(PARAMETER_DAY_PRICE);
		dayPrice.keepStatusOnAction(true);
		TextInput milkPrice = new TextInput(PARAMETER_MILK_PRICE);
		milkPrice.keepStatusOnAction(true);
		TextInput fruitPrice = new TextInput(PARAMETER_FRUIT_PRICE);
		fruitPrice.keepStatusOnAction(true);
		
		RadioButton months = new RadioButton(PARAMETER_USE_MONTH_PRICE, Boolean.TRUE.toString());
		months.setToDisableOnClick(dayPrice, true);
		months.setToDisableOnClick(monthPrice, false);
		months.keepStatusOnAction(true);
		RadioButton days = new RadioButton(PARAMETER_USE_MONTH_PRICE, Boolean.FALSE.toString());
		days.setToDisableOnClick(dayPrice, false);
		days.setToDisableOnClick(monthPrice, true);
		days.keepStatusOnAction(true);
		
		if (price != null) {
			form.add(new HiddenInput(PARAMETER_PRICE_PK, price.getPrimaryKey().toString()));
			
			validFrom.setDate(price.getValidFrom());
			validTo.setDate(price.getValidTo());
			if (price.getMealPricePerMonth() != -1) {
				monthPrice.setContent(String.valueOf(price.getMealPricePerMonth()));
				months.setSelected(true);
				dayPrice.setDisabled(true);
			}
			else if (price.getMealPricePerDay() != -1) {
				dayPrice.setContent(String.valueOf(price.getMealPricePerDay()));
				days.setSelected(true);
				monthPrice.setDisabled(true);
			}
			
			if (price.getMilkPrice() != -1) {
				milkPrice.setContent(String.valueOf(price.getMilkPrice()));
			}
			if (price.getFruitsPrice() != -1) {
				fruitPrice.setContent(String.valueOf(price.getFruitsPrice()));
			}
		}
		else {
			months.setSelected(true);
			dayPrice.setDisabled(true);
		}
		
		if (iwc.isParameterSet(PARAMETER_USE_MONTH_PRICE)) {
			boolean useMonthPrice = new Boolean(iwc.getParameter(PARAMETER_USE_MONTH_PRICE)).booleanValue();
			dayPrice.setDisabled(useMonthPrice);
			monthPrice.setDisabled(!useMonthPrice);
		}
		
		Layer sectionLayer = new Layer(Layer.DIV);
		sectionLayer.setID("sectionDiv");
		layer.add(sectionLayer);
		
		Paragraph paragraph = new Paragraph();
		paragraph.add(new Text(localize("price_editor.days_information", "Days information")));
		sectionLayer.add(paragraph);
		
		Layer formElement = new Layer(Layer.DIV);
		formElement.setStyleClass("formElement");
		Label label = new Label(localize("price_editor.valid_from", "Valid from"), validFrom);
		formElement.add(label);
		formElement.add(validFrom);
		sectionLayer.add(formElement);

		formElement = new Layer(Layer.DIV);
		formElement.setStyleClass("formElement");
		label = new Label(localize("price_editor.valid_to", "Valid to"), validTo);
		formElement.add(label);
		formElement.add(validTo);
		sectionLayer.add(formElement);

		sectionLayer = new Layer(Layer.DIV);
		sectionLayer.setID("sectionDiv");
		layer.add(sectionLayer);
		
		paragraph = new Paragraph();
		paragraph.add(new Text(localize("price_editor.price_information", "Price information")));
		sectionLayer.add(paragraph);
		
		formElement = new Layer(Layer.DIV);
		formElement.setStyleClass("radioElement");
		label = new Label(localize("price_editor.use_month_price", "Use month price"), months);
		formElement.add(months);
		formElement.add(label);
		sectionLayer.add(formElement);
		
		formElement = new Layer(Layer.DIV);
		formElement.setStyleClass("radioElement");
		label = new Label(localize("price_editor.use_day_price", "Use day price"), days);
		formElement.add(days);
		formElement.add(label);
		sectionLayer.add(formElement);
		
		sectionLayer.add(new Break());
		
		formElement = new Layer(Layer.DIV);
		formElement.setStyleClass("formElement");
		label = new Label(localize("price_editor.month_price", "Month price"), monthPrice);
		formElement.add(label);
		formElement.add(monthPrice);
		sectionLayer.add(formElement);
		
		formElement = new Layer(Layer.DIV);
		formElement.setStyleClass("formElement");
		label = new Label(localize("price_editor.day_price", "Day price"), dayPrice);
		formElement.add(label);
		formElement.add(dayPrice);
		sectionLayer.add(formElement);

		sectionLayer = new Layer(Layer.DIV);
		sectionLayer.setID("sectionDiv");
		layer.add(sectionLayer);
		
		paragraph = new Paragraph();
		paragraph.add(new Text(localize("price_editor.milk_and_fruit_information", "Milk/Fruit information")));
		sectionLayer.add(paragraph);
		
		formElement = new Layer(Layer.DIV);
		formElement.setStyleClass("formElement");
		label = new Label(localize("price_editor.milk_price", "Milk price"), milkPrice);
		formElement.add(label);
		formElement.add(milkPrice);
		sectionLayer.add(formElement);

		formElement = new Layer(Layer.DIV);
		formElement.setStyleClass("formElement");
		label = new Label(localize("price_editor.fruit_price", "Fruit price"), fruitPrice);
		formElement.add(label);
		formElement.add(fruitPrice);
		sectionLayer.add(formElement);

		Layer buttonLayer = new Layer(Layer.DIV);
		buttonLayer.setStyleClass("buttonDiv");
		layer.add(buttonLayer);
		
		SubmitButton back = new SubmitButton(localize("back", "Back"));
		back.setStyleClass("button");
		back.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_VIEW));
		SubmitButton next = new SubmitButton(localize("save", "Save"));
		next.setStyleClass("button");
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_SAVE));
		buttonLayer.add(back);
		buttonLayer.add(next);

		add(form);
	}
	
	private void save(IWContext iwc) throws RemoteException {
		Object pricePK = iwc.getParameter(PARAMETER_PRICE_PK);

		boolean validates = true;
		if (!iwc.isParameterSet(PARAMETER_USE_MONTH_PRICE)) {
			getParentPage().setAlertOnLoad(localize("price_editor.must_select_month_or_day_prices", "You must select to use month or day prices"));
			validates = false;
		}
		if (!iwc.isParameterSet(PARAMETER_MONTH_PRICE) && !iwc.isParameterSet(PARAMETER_DAY_PRICE)) {
			getParentPage().setAlertOnLoad(localize("price_editor.must_enter_prices", "You must enter month/day prices."));
			validates = false;
		}
		
		if (!validates) {
			showEditor(iwc);
		}
		
		IWCalendar calendar = new IWCalendar();
		IWTimestamp validFrom = new IWTimestamp(iwc.getParameter(PARAMETER_VALID_FROM));
		validFrom.setDay(1);
		IWTimestamp validTo = new IWTimestamp(iwc.getParameter(PARAMETER_VALID_TO));
		validTo.setDay(calendar.getLengthOfMonth(validTo.getMonth(), validTo.getYear()));
		
		float monthPrice = -1;
		float dayPrice = -1;
		boolean useMonthPrice = new Boolean(iwc.getParameter(PARAMETER_USE_MONTH_PRICE)).booleanValue();
		if (useMonthPrice) {
			monthPrice = Float.parseFloat(iwc.getParameter(PARAMETER_MONTH_PRICE));
		}
		else {
			dayPrice = Float.parseFloat(iwc.getParameter(PARAMETER_DAY_PRICE));
		}
		
		float milkPrice = 0;
		if (iwc.isParameterSet(PARAMETER_MILK_PRICE)) {
			milkPrice = Float.parseFloat(iwc.getParameter(PARAMETER_MILK_PRICE));
		}
		
		float fruitPrice = 0;
		if (iwc.isParameterSet(PARAMETER_FRUIT_PRICE)) {
			fruitPrice = Float.parseFloat(iwc.getParameter(PARAMETER_FRUIT_PRICE));
		}
		
		try {
			getBusiness().storePrices(pricePK, getSession().getSchool(), validFrom.getDate(), validTo.getDate(), dayPrice, monthPrice, milkPrice, fruitPrice);
			showPriceList(iwc);
		}
		catch (IDOCreateException ice) {
			getParentPage().setAlertOnLoad(localize("price_editor.price_entered_overlaps_another_entry", "You are trying to store a price for a period that overlaps an already created period.  Please adjust the months settings and try again."));
			showEditor(iwc);
		}
	}
	
	private void delete(IWContext iwc) throws RemoteException {
		try {
			getBusiness().deleteMealPrice(iwc.getParameter(PARAMETER_PRICE_PK));
			showPriceList(iwc);
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