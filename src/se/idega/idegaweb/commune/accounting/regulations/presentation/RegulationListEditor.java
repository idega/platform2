/*
 * $Id: RegulationListEditor.java,v 1.1 2003/09/06 08:46:02 kjell Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.regulations.presentation;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.lang.reflect.*;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.builder.business.BuilderLogic;
import com.idega.builder.data.IBPage;
import com.idega.user.data.User;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ExceptionWrapper;
import com.idega.util.IWTimestamp;

import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.ListTable;
import se.idega.idegaweb.commune.accounting.presentation.ApplicationForm;
import se.idega.idegaweb.commune.accounting.presentation.ButtonPanel;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness;
import se.idega.idegaweb.commune.accounting.regulations.data.Regulation;
import se.idega.idegaweb.commune.accounting.regulations.data.Condition;
import se.idega.idegaweb.commune.accounting.regulations.business.ConditionHolder;
import se.idega.idegaweb.commune.accounting.regulations.business.VATBusiness;


/**
 * RegulationListEditor is an idegaWeb block that edits a Regulation 
 * <p>
 * $Id: RegulationListEditor.java,v 1.1 2003/09/06 08:46:02 kjell Exp $
 *
 * @author <a href="http://www.lindman.se">Kjell Lindman</a>
 * @version $Revision: 1.1 $
 */
public class RegulationListEditor extends AccountingBlock {

	private final static int ACTION_DEFAULT = 0;
	private final static int ACTION_EDIT = 1;
	private final static int ACTION_SAVE = 2;
	private final static int ACTION_CANCEL = 3;

	private final static String PP = "cacc_regulation_list_editor"; // Parameter prefix 

	private final static String KEY_SAVE = PP + "save";
	private final static String KEY_CANCEL = PP + "cancel";

	private final static String KEY_HEADER = PP + "header";
	private final static String KEY_HEADER_OPERATION = PP + "header_operation";
	private final static String KEY_HEADER_AMOUNT = PP + "header_amount";
	private final static String KEY_HEADER_NAME = PP + "header_name";
	private final static String KEY_HEADER_PAYMENT_FLOW = PP + "header_payment_flow";
	private final static String KEY_HEADER_VAT_ELIGIBLE = PP + "header_vat_eligible";
	private final static String KEY_HEADER_VAT_REGULATION = PP + "header_vat_regulation";
	private final static String KEY_HEADER_SPECIAL_CALCULATION = PP + "header_special_calculation";

	private final static String KEY_CONDITION_HEADER = PP + "condition_header";
	private final static String KEY_INTERVAL_HEADER = PP + "intervall_header";
	private final static String KEY_CONDITION_TYPE_HEADER = PP + "condition_type_header";
	private final static String KEY_REG_SPEC_TYPE_HEADER = PP + "reg_spec_type_header";
	private final static String KEY_CONDITION_ORDER_HEADER = PP + "condition_order_header";

	private final static String KEY_FROM_DATE = PP + "from_date";
	private final static String KEY_TO_DATE = PP + "to_date";
	private final static String KEY_CHANGE_DATE = PP + "change_date";
	private final static String KEY_CHANGE_SIGN = PP + "change_sign";
	private final static String KEY_CHOOSE_CONDITION = PP + "choose_cond";
	private final static String KEY_CHOOSE_INTERVAL = PP + "choose_interval";
	private final static String KEY_VAT_YES = PP + "vat_yes";
	private final static String KEY_VAT_NO = PP + "vat_no";

	private final static String PARAM_NAME = "param_name";
	private final static String PARAM_AMOUNT = "param_amount";
	private final static String PARAM_PERIOD_FROM = "param_period_from";
	private final static String PARAM_PERIOD_TO = "param_period_to";

	private final static String PARAM_BUTTON_SAVE = "button_save";
	private final static String PARAM_BUTTON_CANCEL = "button_cancel";
	private final static String PARAM_MODE_COPY = "mode_copy";
	private final static String PARAM_EDIT_ID = "param_edit_id";
	private final static String PARAM_SIGNED = "param_signed";
	private final static String PARAM_OPERATION_SELECTOR = "param_oper_sel";
	private final static String PARAM_INTERVAL_SELECTOR = "param_int_sel";
	private final static String PARAM_SELECTOR_CONDITION_TYPE = "param_cond_type_sel";
	private final static String PARAM_SELECTOR_REG_SPEC_TYPE = "param_reg_spec_sel";
	private final static String PARAM_SELECTOR_SPECIAL_CALCULATION = "param_spec_calc_sel";
	private final static String PARAM_SELECTOR_VAT_ELIGIBLE = "param_vat_eligible_sel";	 
	private final static String PARAM_SELECTOR_VAT_REGULATION = "param_vat_regulation_sel";	 


	private IBPage _responsePage;
	private String _errorText = "";
	
	public void setResponsePage(IBPage page) {
		_responsePage = page;
	}

	public IBPage getResponsePage() {
		return _responsePage;
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
					viewMainForm(iwc, "");
					break;
				case ACTION_SAVE :
					if(!saveData(iwc)) {
						viewMainForm(iwc, _errorText);
					}
					break;
				case ACTION_CANCEL :
					closeMe(iwc);
					break;
			}
		}
		catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}
	}
		 
	private boolean saveData(IWContext iwc) {
/*
			try {
				String id = null;
				if(iwc.isParameterSet(PARAM_EDIT_ID)) {
					id = iwc.getParameter(PARAM_EDIT_ID);
				}
				if(iwc.isParameterSet(PARAM_MODE_COPY)) {
					id = null;
				}
				getRegulationBusiness(iwc).saveRegulation(
						id,
						(Date) parseDate(iwc.getParameter(PARAM_PERIODE_FROM)),
						(Date) parseDate(iwc.getParameter(PARAM_PERIODE_TO)),
						iwc.getParameter(PARAM_SIGNED),
						iwc.getParameter(PARAM_SELECTOR_ACTIVITY),				
						iwc.getParameter(PARAM_SELECTOR_REGSPEC),					
						iwc.getParameter(PARAM_SELECTOR_COMPANY_TYPE),					
						iwc.getParameter(PARAM_SELECTOR_COM_BELONGING)
				);
			} catch (RegulationException e) {
				_errorText = localize(e.getTextKey(), e.getDefaultText());
				return false;
			} catch (RemoteException e) {
				super.add(new ExceptionWrapper(e, this));
				return false;
			}
			
			closeMe(iwc);
*/
			return true;
	}

	 
	/*
	 * Returns the action constant for the action to perform based 
	 * on the POST parameters in the specified context.
	 */
	private int parseAction(IWContext iwc) {
		int action = ACTION_DEFAULT;
		if (iwc.isParameterSet(PARAM_BUTTON_SAVE)) {
		  action = ACTION_SAVE;
		}
		if (iwc.isParameterSet(PARAM_BUTTON_CANCEL)) {
		  action = ACTION_CANCEL;
		}
		return action;
	}

	/*
	 * Adds the default form to the block.
    */	
	private void viewMainForm(IWContext iwc, String error) {
		ApplicationForm app = new ApplicationForm(this);
		Regulation r = getThisRegulation(iwc);
		
		Table topPanel = getTopPanel(iwc, r);		
		Table regulationForm = getRegulationForm(iwc, r);
		Table bottomPanel = new Table();
		bottomPanel.setCellpadding("0");		
		bottomPanel.setCellspacing("0");		
					
		ButtonPanel buttonPanel = new ButtonPanel(this);
		buttonPanel.addLocalizedButton(PARAM_BUTTON_SAVE, KEY_SAVE, "Spara");
		buttonPanel.addLocalizedButton(PARAM_BUTTON_CANCEL, KEY_CANCEL, "Avbryt");
		
		bottomPanel.add(getVATPanel(iwc), 1, 1);
		bottomPanel.add(buttonPanel, 1, 1);
		
		app.setLocalizedTitle(KEY_HEADER, "Skapa/Ändra regelverk");
		app.setSearchPanel(topPanel);
		app.setMainPanel(regulationForm);
		app.setButtonPanel(bottomPanel);
		add(app);		
		if(error.length() != 0) {
			add(getSmallErrorText(error));
		}
	}


	/*
	 * Returns a top panel
	 * @param iwc user/session context 
	 * @param r Regulation 
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.Regulation#
	 * @return table panel
	 */
	private Table getTopPanel(IWContext iwc, Regulation r) {

		Table table = new Table();
		table.setWidth("75%");
		User user = iwc.getCurrentUser();
		String userName = "";
		if(user != null) {
			userName = user.getFirstName();
		}
		Timestamp rightNow = IWTimestamp.getTimestampRightNow();
		Date dd = new Date(System.currentTimeMillis());

		table.add(getLocalizedLabel(KEY_HEADER_OPERATION, "Huvudverksamhet"), 1, 1);
		table.add(getLocalizedLabel(KEY_HEADER_NAME, "Benämning"), 1, 2);
		table.add(getLocalizedLabel(KEY_HEADER_AMOUNT, "Månadsbelopp"), 1, 3);
		table.add(getLocalizedLabel(KEY_FROM_DATE, "Från datum"),1 ,4);
		table.add(getLocalizedLabel(KEY_CHANGE_DATE, "Ändringsdatum"),1 ,5);
	
		if(r != null) {
			table.add(getLocalizedLabel(r.getOperation().getLocalizationKey() , 
					r.getOperation().getLocalizationKey()), 2, 1);
		}

		table.add(getTextInput(PARAM_NAME, r != null ? r.getName() : "", 200, 40), 2, 2);

		table.add(getTextInput(PARAM_AMOUNT, r != null ? 
				formatCash(r.getAmount()) : "", 60, 10), 2, 3);

		table.add(getTextInput(PARAM_PERIOD_FROM, (formatDate(r != null ? 
				r.getPeriodFrom() : dd, 4)), 40, 4), 2, 4);

		table.add(getText(formatDate(r != null ? r.getChangedDate(): rightNow, 6)), 2, 5);

		table.add(getLocalizedLabel(KEY_HEADER_PAYMENT_FLOW, "Ström"),3 ,1);
		table.add("", 3, 2);
		table.add("", 3, 3);
		table.add(getLocalizedLabel(KEY_TO_DATE, "Tom datum"),3 ,4);
		table.add(getLocalizedLabel(KEY_CHANGE_SIGN, "Ändringssignatur"),3 ,5);

		if(r != null) {
			table.add(getLocalizedLabel(r.getPaymentFlowType().getLocalizationKey(), 
					r.getPaymentFlowType().getLocalizationKey()), 4, 1);
		} else {
			table.add("", 4, 1);
		}
		table.add("", 4, 2);
		table.add("", 4, 3);
		table.add(getTextInput(PARAM_PERIOD_TO, (formatDate(r != null ? r.getPeriodTo() : dd, 4)), 40, 4), 4, 4);

		table.add(""+userName, 4, 5);
		table.add(new HiddenInput(PARAM_SIGNED, ""+userName));

		table.add(getLocalizedLabel(KEY_CONDITION_TYPE_HEADER, "Villkorstyp"), 1, 6);
		table.add(getLocalizedLabel(KEY_REG_SPEC_TYPE_HEADER, "Regelspec.typ"), 1, 7);

		table.add(conditionTypeSelector(iwc, PARAM_SELECTOR_CONDITION_TYPE), 2, 6);
		table.add(regSpecTypeSelector(iwc, PARAM_SELECTOR_REG_SPEC_TYPE), 2, 7);

		table.add(getLocalizedLabel(KEY_CONDITION_ORDER_HEADER, "Villkorsordning"), 3, 6);
		table.add(getSmallText(r != null ? ""+r.getConditionOrder().intValue() : ""), 4, 6);

		if(iwc.isParameterSet(PARAM_MODE_COPY)) {
			table.add(new HiddenInput(PARAM_MODE_COPY, ""+iwc.getParameter(PARAM_MODE_COPY)));
		}
		if(iwc.isParameterSet(PARAM_EDIT_ID)) {
			table.add(new HiddenInput(PARAM_EDIT_ID, ""+iwc.getParameter(PARAM_EDIT_ID)));
		}

		return table;	
	}
	
	
	/*
	 * Generates the main form with selectors for Conditions and Intervalls
	 *    
	 * @param r Regulation 
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.Regulation#
	 * @return main table
	 */
	private Table getRegulationForm(IWContext iwc, Regulation r) {

		Table mtable = new Table();
		mtable.setCellpadding("0");
		mtable.setCellspacing("0");
		Table table = new Table();
		ListTable list = null;
		int row = 1;
		Date defaultDate;
	
		if (r == null) {
			defaultDate = new Date(System.currentTimeMillis());
		} else  {
			defaultDate = r.getPeriodFrom();
		}
	
		row += 2;
		list = new ListTable(this, 2);

		Collection conditions = null;
		Iterator iter = null;
		conditions = findAllConditionsByRegulation(iwc, r);
		if(conditions != null) {
			iter = conditions.iterator();
		}
			
		for (int index = 1; index <= 5; index++) {
			Condition field = null;
			if(iter != null) {
				if(iter.hasNext()) { 
					field = (Condition) iter.next();
				}
			}
			list.add(getLocalizedLabel(KEY_CONDITION_HEADER + index, "Villkor " + index));
			list.add(getStyledInterface(operationSelector(
					iwc, 
					PARAM_OPERATION_SELECTOR +"_" +index, 
					PARAM_INTERVAL_SELECTOR +"_" +index))
			);
		}
			
		mtable.add(table, 1, 1);
		mtable.add(list, 1, 2);
		
		return mtable;
	}

	/*
	 * Returns a VAT panel with selectors VAT 
	 * @param iwc user/session context 
	 * @return table panel
	 */
	private Table getVATPanel(IWContext iwc) {
		Table table = new Table();
		table.add(getLocalizedLabel(KEY_HEADER_SPECIAL_CALCULATION, "Specialberäkning"), 1, 1);
		table.add(getLocalizedLabel(KEY_HEADER_VAT_ELIGIBLE, "Momsersättning"), 1, 2);
		table.add(getLocalizedLabel(KEY_HEADER_VAT_REGULATION, "Momsregel"), 1, 3);
		table.add(specialCalculationSelector(iwc, PARAM_SELECTOR_SPECIAL_CALCULATION), 2, 1);
		table.add(vatYesNoSelector(iwc, PARAM_SELECTOR_VAT_ELIGIBLE), 2, 2);
		table.add(vatRuleSelector(iwc, PARAM_SELECTOR_VAT_REGULATION), 2, 3);
		return table;
	}

	/*
	 * Retrives from business the current regulation data that is pointed out by PARAM_EDIT_ID.
	 * Remember that this app only can edit one record at a time.
	 *    
	 * @param iwc Idega Web Context 
	 * @see se.idega.idegaweb.commune.accounting.regulation.data.Regulation#
	 * @return Regulation loaded with data
	 */
	private Regulation getThisRegulation(IWContext iwc) {
		RegulationsBusiness rBiz;
		Regulation r = null;
		try {
			int regulationID = 0;
			
			if(iwc.isParameterSet(PARAM_EDIT_ID)) {
				regulationID = Integer.parseInt(iwc.getParameter(PARAM_EDIT_ID));
			}
			r = (Regulation) getRegulationBusiness(iwc).findRegulation(regulationID);
		} catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}	
		return r;
	}

	/*
	 * Generates a DropDownSelector for Regulation specification types, 
	 * collected from the RegulationsBusiness 
	 *    
	 * @param iwc Idega Web Context 
	 * @param name HTML Parameter ID for this selector
	 * @param refIndex The initial position to set the selector to 
	 * @return the drop down menu
	 */
	private DropdownMenu regSpecTypeSelector(IWContext iwc, String name) {
		
		int refIndex = iwc.isParameterSet(name) ? Integer.parseInt(iwc.getParameter(name)) : 1;		
		DropdownMenu menu = null;
		try {
			menu = (DropdownMenu) getStyledInterface(
				getDropdownMenuLocalized(name, getRegulationBusiness(iwc).findAllRegulationSpecTypes(), 
				"getLocalizationKey"));
			menu.setSelectedElement(refIndex);
		} catch (Exception e) {
		}
		return menu;
	}

	/*
	 * Generates a DropDownSelector for Special Calculations 
	 * collected from the RegulationsBusiness 
	 *    
	 * @param iwc Idega Web Context 
	 * @param name HTML Parameter ID for this selector
	 * @param refIndex The initial position to set the selector to 
	 * @return the drop down menu
	 */
	private DropdownMenu specialCalculationSelector(IWContext iwc, String name) {
		
		int refIndex = iwc.isParameterSet(name) ? Integer.parseInt(iwc.getParameter(name)) : 1;		
		DropdownMenu menu = null;
		try {
			menu = (DropdownMenu) getStyledInterface(
				getDropdownMenuLocalized(name, getRegulationBusiness(iwc).findAllSpecialCalculationTypes(), 
//			getDropdownMenuLocalized(name, getRegulationBusiness(iwc).findAllRegulationSpecTypes(), 
				"getLocalizationKey"));
			menu.setSelectedElement(refIndex);
		} catch (Exception e) {
			menu = new DropdownMenu();
			menu.addMenuElement(0, e.getMessage());	
		}
		return menu;
	}

	/*
	 * Generates a DropDownSelector for VAT Rules 
	 * collected from the RegulationBusiness 
	 *    
	 * @param iwc Idega Web Context 
	 * @param name HTML Parameter ID for this selector
	 * @param refIndex The initial position to set the selector to 
	 * @return the drop down menu
	 */
	private DropdownMenu vatRuleSelector(IWContext iwc, String name) {
		
		int refIndex = iwc.isParameterSet(name) ? Integer.parseInt(iwc.getParameter(name)) : 1;		
		DropdownMenu menu = null;
		try {
				menu = (DropdownMenu) getStyledInterface(
				getDropdownMenuLocalized(name, getRegulationBusiness(iwc).findAllVATRules(), 
				"getLocalizationKey"));
			menu.setSelectedElement(refIndex);
		} catch (Exception e) {
		}
		return menu;
	}

	/*
	 * Generates a DropDownSelector for VAT Yes / No 
	 *    
	 * @param iwc Idega Web Context 
	 * @param name HTML Parameter ID for this selector
	 * @param refIndex The initial position to set the selector to 
	 * @return the drop down menu
	 */
	private DropdownMenu vatYesNoSelector(IWContext iwc, String name) {
		
		int refIndex = iwc.isParameterSet(name) ? Integer.parseInt(iwc.getParameter(name)) : 1;		
		DropdownMenu menu = (DropdownMenu) getStyledInterface(new DropdownMenu());
		menu.addMenuElement(1, localize(KEY_VAT_YES, "Ja"));
		menu.addMenuElement(2, localize(KEY_VAT_NO, "Nej"));
		menu.setSelectedElement(refIndex);
		return menu;
	}


	/*
	 * Generates a DropDownSelector for Condition types, collected from the RegulationsBusiness 
	 *    
	 * @param iwc Idega Web Context 
	 * @param name HTML Parameter ID for this selector
	 * @param refIndex The initial position to set the selector to 
	 * @return the drop down menu
	 */
	private DropdownMenu conditionTypeSelector(IWContext iwc, String name) {
		
		int refIndex = iwc.isParameterSet(name) ? Integer.parseInt(iwc.getParameter(name)) : 1;		
		DropdownMenu menu = null;
		try {
			menu = (DropdownMenu) getStyledInterface(
			getDropdownMenuLocalized(name, getRegulationBusiness(iwc).findAllConditionTypes(), 
			"getLocalizationKey"));
			menu.setSelectedElement(refIndex);
		} catch (Exception e) {
		}
		return menu;
	}

	private ExtendedDropdownDouble operationSelector(IWContext iwc, String primaryName, String secondaryName) {
		
		ExtendedDropdownDouble dropdown = new ExtendedDropdownDouble(this, primaryName, secondaryName);
		String emptyString = localize(KEY_CHOOSE_INTERVAL, "Välj intervall");
		dropdown.addEmptyElement(localize(KEY_CHOOSE_CONDITION, "Välj Villkor"), emptyString);
		try {
			Collection conditions = getRegulationBusiness(iwc).findAllOperations();
				
			if (conditions != null) {
				Iterator iter = conditions.iterator();
				Map mainMap = new HashMap();

				while (iter.hasNext()) {
					SortedMap intervalMap = new TreeMap(new RegulationComparator());
					ConditionHolder cond = (ConditionHolder) iter.next();
					Collection intervals = getIntervals(iwc, cond);
					if (intervals != null) {
						Iterator iterator = intervals.iterator();
						while (iterator.hasNext()) {
							Object interval = iterator.next();
							intervalMap.put(interval, interval);
						}
					}
					mainMap.put(cond, intervalMap);
					dropdown.addMenuElement(""+cond.getPrimaryKey(), cond.getName(), (Map) mainMap.get(cond), cond.getDataMethodName());
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return dropdown;
	}

	private Collection getIntervals(IWContext iwc, ConditionHolder condition) {

		Collection col = null; 

		try {
			Method myMethod;
			Class cls = Class.forName(condition.getBusinessClassName());
			Object business = com.idega.business.IBOLookup.getServiceInstance(iwc, cls);

			myMethod = cls.getMethod(condition.getCollectDataMethod(), null);
			Collection data = (Collection) myMethod.invoke(business, null);
			return data;
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			return col;
		}
	}
	
	private Collection findAllConditionsByRegulation(IWContext iwc,  Regulation r) {
		Collection c = null;
		try {
			getRegulationBusiness(iwc).findAllConditionsByRegulation(r);
		} catch (Exception e) {
		}
		return c;	
	}

	private void closeMe(IWContext iwc) {
		getParentPage().setToRedirect(BuilderLogic.getInstance().getIBPageURL(iwc, _responsePage.getID()));
	}

	private String formatCash(Integer cash) {
		return ""+(cash.intValue()/100);
	}
	
	private RegulationsBusiness getRegulationBusiness(IWContext iwc) throws RemoteException {
		return (RegulationsBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, RegulationsBusiness.class);
	}
	private VATBusiness getVATBusiness(IWContext iwc) throws RemoteException {
		return (VATBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, VATBusiness.class);
	}
}


