/*
 * $Id: RegulationList.java,v 1.3 2003/09/08 08:10:07 laddi Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.regulations.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.sql.Date;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.builder.data.IBPage;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.block.school.business.SchoolBusiness;


import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.ListTable;
import se.idega.idegaweb.commune.accounting.presentation.ApplicationForm;
import se.idega.idegaweb.commune.accounting.presentation.ButtonPanel;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness;
import se.idega.idegaweb.commune.accounting.regulations.data.Regulation;


/**
 * RegulationList is an idegaWeb block that handles maintenance of the regulations
 * 
 * @see se.idega.idegaweb.commune.accounting.regulations.data.RegulationBMPBean#
 * @see se.idega.idegaweb.commune.accounting.regulations.data.ConditionBMPBean#
 * <p>
 * $Id: RegulationList.java,v 1.3 2003/09/08 08:10:07 laddi Exp $
 *
 * @author <a href="http://www.lindman.se">Kjell Lindman</a>
 * @version $Revision: 1.3 $
 */
public class RegulationList extends AccountingBlock {

	private final static int ACTION_DEFAULT = 0;
	//private final static int ACTION_EDIT = 1;
	private final static int ACTION_DELETE = 1;
	private final static String PP = "cacc_regulation_list"; // Parameter prefix 

	private final static String KEY_NAME = PP + "name";
	private final static String KEY_AMOUNT = PP + "amount";
	private final static String KEY_CONDITION_TYPE = PP + "condition_type";
	private final static String KEY_CONDITION_ORDER = PP + "condition_order";
	private final static String KEY_REG_SPEC_TYPE = PP + "reg_specification_type";
	//private final static String KEY_COPY = PP + "copy";
	//private final static String KEY_EDIT = PP + "edit";
	private final static String KEY_CLICK_REMOVE = PP + "click_to_remove";
	private final static String KEY_HEADER_OPERATION = PP + "operation_header"; 
	private final static String KEY_HEADER_PAYMENT_FLOW_TYPE = PP + "payment_flow_type_header"; 
	private final static String KEY_HEADER_SORT_BY = PP + "sort_by_header"; 
	private final static String KEY_HEADER = PP + "header";
	private final static String KEY_BUTTON_COPY = PP + "button_copy";
	private final static String KEY_BUTTON_EDIT = PP + "button_edit";
	private final static String KEY_MENU_OPERATION_HEADER = PP + "menu_operation_header"; 
	private final static String KEY_MENU_SORTNAME_HEADER = PP + "menu_sortname_header"; 
	private final static String KEY_MENU_SORTPERIODE_HEADER = PP + "menu_sortperiode_header"; 
	private final static String KEY_NEW = PP + "new";
	private final static String KEY_PERIOD_SEARCH = PP + "period_searc"; 
	private final static String KEY_PERIOD = PP + "period"; 
	private final static String KEY_REMOVE_CONFIRM = PP + "remove_confirm";
	private final static String KEY_REMOVE = PP + "remove";
	private final static String KEY_SEARCH = PP + "search";

	private final static String PARAM_DELETE_ID = "param_delete_id";
	private final static String PARAM_EDIT_ID = "param_edit_id";
	private final static String PARAM_NEW = "param_button_new";
	private final static String PARAM_SEARCH = "param_button_search";
	private final static String PARAM_SELECTOR_OPERATION = "param_sel_operation";
	private final static String PARAM_SELECTOR_PAYMENT_FLOW_TYPE = "param_sel_payment_flow_type";
	private final static String PARAM_SELECTOR_SORT_BY = "param_sel_sort_by";
	private final static String PARAM_FROM = "param_from";
	private final static String PARAM_TO = "param_to";
	private final static String PARAM_MODE_COPY = "mode_copy";


	
	private IBPage _editPage;
	private Date _currentFromDate;
	private Date _currentToDate;

	/**
	 * Handles the property editPage
	 */
	public void setEditPage(IBPage page) {
		_editPage = page;
	}

	/**
	 * Handles the property editPage
	 */
	public IBPage getEditPage() {
		return _editPage;
	}


	/**
	 * Handles all of the blocks presentation.
	 * @param iwc user/session context 
	 */
	public void main(final IWContext iwc) {
		setResourceBundle(getResourceBundle(iwc));
		try {
			int action = parseAction(iwc);
			switch (action) {
				case ACTION_DEFAULT :
					viewForm(iwc);
					break;
				case ACTION_DELETE :
					deletePost(iwc);
					viewForm(iwc);
					break;
			}
		} catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}
	}

	/*
	 * Returns the action constant for the action to perform based 
	 * on the POST parameters in the specified context.
	 */
	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAM_DELETE_ID)) {
			return ACTION_DELETE;
		}
		return ACTION_DEFAULT;
	}

	/*
	 * Delete posts marked with the checkbox 
	 */
	private void deletePost(IWContext iwc) {
		String id = iwc.getParameter(PARAM_DELETE_ID);
		if(id != null) {
			try {
				getRegulationBusiness(iwc).deleteRegulation(Integer.parseInt(id));
			} catch ( Exception e) {
				super.add(new ExceptionWrapper(e, this));
			}
		}
	}

	/*
	 * Adds the default form to the block.
	 */	
	private void viewForm(IWContext iwc) {
		ApplicationForm app = new ApplicationForm(this);
		
		if (iwc.isParameterSet(PARAM_FROM)) {
			_currentFromDate = parseDate(iwc.getParameter(PARAM_FROM));
		} else {
			_currentFromDate = parseDate("2001-01-01");
		}
		
		if (iwc.isParameterSet(PARAM_TO)) {
			_currentToDate = parseDate(iwc.getParameter(PARAM_TO));
		} else {
			_currentToDate = parseDate("2009-01-01");
		}

		app.setLocalizedTitle(KEY_HEADER, "Regelverk");
		app.setSearchPanel(getSearchPanel(iwc));
		app.setMainPanel(getRegulationTable(iwc));
		app.setButtonPanel(getButtonPanel());
		app.addHiddenInput(PARAM_DELETE_ID, "-1");
		add(app);		
	}

	private ButtonPanel getButtonPanel() {
		ButtonPanel buttonPanel = new ButtonPanel(this);
		buttonPanel.addLocalizedButton(PARAM_NEW, KEY_NEW, "Ny", _editPage);
		return buttonPanel;
	}
	
	/*
	 * Returns the RegulationList
	 */
	private ListTable getRegulationTable(IWContext iwc) {

		RegulationsBusiness rBiz;
		
		
		ListTable list = new ListTable(this, 9);

		list.setLocalizedHeader(KEY_PERIOD, "Period", 1);
		list.setLocalizedHeader(KEY_NAME, "Benämning", 2);
		list.setLocalizedHeader(KEY_AMOUNT, "Styck-belopp/mån", 3);
		list.setLocalizedHeader(KEY_CONDITION_TYPE, "Villkorstyp", 4);
		list.setLocalizedHeader(KEY_CONDITION_ORDER, "Villkorsordning", 5);
		list.setLocalizedHeader(KEY_REG_SPEC_TYPE, "Regel-spec. typ", 6);
		list.setHeader("", 7);
		list.setHeader("", 8);
		list.setHeader("", 9);
		
		try {
			rBiz = getRegulationBusiness(iwc);
			Collection items = rBiz.findRegulationsByPeriod(_currentFromDate, _currentToDate);
			if(items != null) {
				Iterator iter = items.iterator();
				while (iter.hasNext()) {
					Regulation r = (Regulation) iter.next();
					Link link = getLink(formatDate(r.getPeriodFrom(), 4) + "-" + formatDate(r.getPeriodTo(), 4),
										 PARAM_EDIT_ID, r.getPrimaryKey().toString());
					
					link.setPage(_editPage);
					list.add(link);

					list.add(r.getName() != null ? r.getName() : "" , r.getLocalizationKey());

					if(r.getAmount() != null) {
						list.add(""+(r.getAmount().intValue()));
					} else {
						list.add(getSmallText(""));
					}

					if (r.getConditionType() != null) { 
						list.add(r.getConditionType().getLocalizationKey(), 
							r.getConditionType().getLocalizationKey());
					} else {
						list.add("");
					}

					list.add(r.getConditionOrder() != null ? 
							""+r.getConditionOrder().intValue(): "");

					if (r.getRegSpecType() != null) { 
						list.add(r.getRegSpecType().getLocalizationKey(), 
							r.getRegSpecType().getLocalizationKey());
					} else {
						list.add("");
					}
					Link edit = new Link(getEditIcon(localize(KEY_BUTTON_EDIT, "Redigera")));
					edit.addParameter(PARAM_EDIT_ID, r.getPrimaryKey().toString());
					edit.setPage(_editPage);
					list.add(edit);

					Link copy = new Link(getCopyIcon(localize(KEY_BUTTON_COPY, "Kopiera")));
					copy.addParameter(PARAM_EDIT_ID, r.getPrimaryKey().toString());
					copy.addParameter(PARAM_MODE_COPY, "1");
					copy.setPage(_editPage);
					list.add(copy);

					SubmitButton delete = new SubmitButton(getDeleteIcon(localize(KEY_REMOVE, "Radera")));
					delete.setDescription(localize(KEY_CLICK_REMOVE, "Klicka här för att radera regel"));
					delete.setValueOnClick(PARAM_DELETE_ID, r.getPrimaryKey().toString());
					delete.setSubmitConfirm(localize(KEY_REMOVE_CONFIRM, "Vill du verkligen radera denna regel?"));
					list.add(delete);
				}
			}			
		} catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}	
		
		return list;
	}
	
	private Table getSearchPanel(IWContext iwc) {

		Table table = new Table();
		table.setColumnAlignment(1, Table.HORIZONTAL_ALIGN_LEFT);
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_LEFT);
		table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_LEFT);
		table.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_LEFT);
	 		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());

		table.add(getLocalizedLabel(KEY_HEADER_OPERATION, "Huvudverksamhet"), 1, 1);
		table.add(getLocalizedLabel(KEY_HEADER_PAYMENT_FLOW_TYPE, "Ström"), 1, 2);
		table.add(getLocalizedLabel(KEY_PERIOD_SEARCH, "Period"), 1, 3);
		
		table.add(mainOperationSelector(iwc, PARAM_SELECTOR_OPERATION, 1), 2, 1);
		table.add(paymentFlowTypeSelector(iwc, PARAM_SELECTOR_PAYMENT_FLOW_TYPE, 1), 2, 2);
		table.add(getFromToDatePanel(PARAM_FROM, _currentFromDate, PARAM_TO, _currentToDate), 2, 3);

		table.add(getLocalizedLabel(KEY_HEADER_SORT_BY, "Sortera på"), 3, 1);
		table.add(getLocalizedButton(PARAM_SEARCH, KEY_SEARCH, "Sök"), 3, 3);

		table.add(sortBySelector(PARAM_SELECTOR_SORT_BY, 1), 4, 1);

		return table;
	}

	private Table getFromToDatePanel(String param_from, Date date_from, String param_to, Date date_to) {
		Table table = new Table();
		table.setCellpadding("0");
		table.setCellspacing("0");
		TextInput fromDate = getTextInput(param_from, formatDate(date_from, 10),  80, 10);
		TextInput toDate = getTextInput(param_to, formatDate(date_to, 10),  80, 10);
		fromDate.setLength(10);
		toDate.setLength(10);
		table.add(fromDate, 1, 1);
		table.add(new String("-"), 2, 1);
		table.add(toDate, 3, 1);
		return table;
	}

	/*
	 * Generates a DropDownSelector for sort by terms 
	 * @param refIndex The initial position to set the selector to 
	 * @return the drop down menu
	 */
	private DropdownMenu sortBySelector(String name, int refIndex) {
		DropdownMenu menu = new DropdownMenu(name);
		menu.addMenuElementFirst("1", localize(KEY_MENU_SORTPERIODE_HEADER, "Period"));
		menu.addMenuElementFirst("2", localize(KEY_MENU_SORTNAME_HEADER, "Benämning"));
		menu.setSelectedElement(refIndex);
		return (DropdownMenu) getStyledInterface(menu);
	}


	/*
	 * Generates a DropDownSelector for Main operation (Huvudverksamhet) 
	 * from the school framework 
	 * @see com.idega.block.school.data.SchoolCategory#
	 * @param iwc Idega Web Context 
	 * @param name HTML Parameter ID for this selector
	 * @param refIndex The initial position to set the selector to 
	 * @return the drop down menu
	 */
	private DropdownMenu mainOperationSelector(IWContext iwc, String name, int refIndex) {
		
		DropdownMenu menu = null;
		try {
			menu = (DropdownMenu) getStyledInterface(
					getDropdownMenuLocalized(name, getSchoolBusiness(iwc).getSchoolManagementTypes(),"getLocalizationKey"));
		} catch (Exception e) {
		}
		menu.addMenuElementFirst("0", localize(KEY_MENU_OPERATION_HEADER, "Välj Huvudverksamhet"));
		menu.setSelectedElement(refIndex);
		return menu;
	}


	/*
	 * Generates a DropDownSelector for Payment flow type
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.PaymentFlowType;
	 * @param iwc Idega Web Context 
	 * @param name HTML Parameter ID for this selector
	 * @param refIndex The initial position to set the selector to 
	 * @return the drop down menu
	 */
	private DropdownMenu paymentFlowTypeSelector(IWContext iwc, String name, int refIndex) {
		
		DropdownMenu menu = null;
		try {
			menu = (DropdownMenu) getStyledInterface(
					getDropdownMenuLocalized(name, getRegulationBusiness(iwc).findAllPaymentFlowTypes(), 
					"getLocalizationKey"));
		} catch (Exception e) {
		}
		menu.setSelectedElement(refIndex);
		return menu;
	}


	private SchoolBusiness getSchoolBusiness(IWContext iwc) throws RemoteException {
		return (SchoolBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
	}

	private RegulationsBusiness getRegulationBusiness(IWContext iwc) throws RemoteException {
		return (RegulationsBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, RegulationsBusiness.class);
	}
}

