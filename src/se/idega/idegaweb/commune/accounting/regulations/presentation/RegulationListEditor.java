/*
 * $Id: RegulationListEditor.java,v 1.26 2005/07/06 15:31:30 palli Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.regulations.presentation;

import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.ApplicationForm;
import se.idega.idegaweb.commune.accounting.presentation.ButtonPanel;
import se.idega.idegaweb.commune.accounting.presentation.OperationalFieldsMenu;
import se.idega.idegaweb.commune.accounting.regulations.business.ConditionHolder;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationException;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness;
import se.idega.idegaweb.commune.accounting.regulations.data.Condition;
import se.idega.idegaweb.commune.accounting.regulations.data.Regulation;

import com.idega.builder.business.BuilderLogic;
import com.idega.core.builder.data.ICPage;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;


/**
 * RegulationListEditor is an idegaWeb block that edits a Regulation 
 * <p>
 * $Id: RegulationListEditor.java,v 1.26 2005/07/06 15:31:30 palli Exp $
 *
 * @author <a href="http://www.lindman.se">Kjell Lindman</a>
 * @version $Revision: 1.26 $
 */
public class RegulationListEditor extends AccountingBlock {

	private final static int ACTION_DEFAULT = 0;
	private final static int ACTION_SAVE = 2;
	private final static int ACTION_CANCEL = 3;
	private final static String COLUMN_WIDTH = "100";

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
	private final static String KEY_HEADER_DISCOUNT = PP + "header_discount";
	
	private final static String KEY_CONDITION_TYPE_HEADER = PP + "condition_type_header";
	private final static String KEY_REG_SPEC_TYPE_HEADER = PP + "reg_spec_type_header";
	private final static String KEY_CONDITION_ORDER_HEADER = PP + "condition_order_header";
	private final static String KEY_CONDITION_HEADER = PP + "condition_header";
//	private final static String KEY_MENU_OPERATION_HEADER = PP + "menu_operation_header";
	private final static String KEY_MENU_REG_SPEC_HEADER = PP + "menu_reg_spec_header";
	private final static String KEY_MENU_SPEC_CALC_HEADER = PP + "menu_spec_calc_header";
	private final static String KEY_MENU_VAT_HEADER = PP + "menu_vat_header";	
	private final static String KEY_MENU_VAT_RULE_HEADER = PP + "menu_vat_rule_header";
	private final static String KEY_MENU_COND_TYPE_HEADER = PP + "menu_cond_type_header";
	private final static String KEY_MENU_PAY_FLOW_HEADER = PP + "menu_pay_flow_header";
	private final static String KEY_MAX_AMOUNT = PP + "menu_max_amount_header";
		
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
	private final static String PARAM_DISCOUNT = "param_discount";
	private final static String PARAM_MAX_AMOUNT_DISCOUNT = "param_max_amount_discount";
	private final static String PARAM_PERIOD_FROM = "param_period_from";
	private final static String PARAM_PERIOD_TO = "param_period_to";

	private final static String PARAM_BUTTON_SAVE = "button_save";
	private final static String PARAM_BUTTON_CANCEL = "button_cancel";
	private final static String PARAM_MODE_COPY = "mode_copy";
	private final static String PARAM_EDIT_ID = "param_edit_id";
	private final static String PARAM_CHANGED_SIGN = "param_signed";

	private final static String PARAM_CONDITION_ORDER= "param_condition_order";	 
	
	private final static String PARAM_SELECTOR_MAIN_OPERATION = "param_main_oper_sel";

	private final static String PARAM_SELECTOR_CONDITION = "param_cond_sel";
	private final static String PARAM_SELECTOR_INTERVAL = "param_int_sel";
	private final static String PARAM_SELECTOR_CONDITION_TYPE = "param_cond_type_sel";
	private final static String PARAM_SELECTOR_REG_SPEC_TYPE = "param_reg_spec_sel";
	private final static String PARAM_SELECTOR_SPECIAL_CALCULATION = "param_spec_calc_sel";
	private final static String PARAM_SELECTOR_VAT_ELIGIBLE = "param_vat_eligible_sel";	 
	private final static String PARAM_SELECTOR_VAT_RULE = "param_vat_rule_sel";	 
	private final static String PARAM_SELECTOR_PAYMENT_FLOW_TYPE = "param_pay_flow_sel";	 


	private ICPage _responsePage;
	private String _errorText = "";
	private Map _pMap;

	
	public void setResponsePage(ICPage page) {
		_responsePage = page;
	}

	public ICPage getResponsePage() {
		return _responsePage;
	}
	
	/**
	 * Handles all of the blocks presentation.
	 * @param iwc user/session context 
	 */
	public void init(final IWContext iwc) {
		try {
			int action = parseAction(iwc);
			setDefaultParameters();
			switch (action) {
				case ACTION_DEFAULT :
					viewMainForm(iwc, "");
					break;
				case ACTION_SAVE :
					if (!saveData(iwc)) {
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

		int newId = 0;

		_pMap.clear();
		_pMap.put(PARAM_PERIOD_FROM, iwc.getParameter(PARAM_PERIOD_FROM));
		_pMap.put(PARAM_PERIOD_TO, iwc.getParameter(PARAM_PERIOD_TO));
		_pMap.put(PARAM_NAME, iwc.getParameter(PARAM_NAME));
		_pMap.put(PARAM_AMOUNT, iwc.getParameter(PARAM_AMOUNT));
		_pMap.put(PARAM_CONDITION_ORDER, iwc.getParameter(PARAM_CONDITION_ORDER));
		_pMap.put(PARAM_SELECTOR_PAYMENT_FLOW_TYPE, iwc.getParameter(PARAM_SELECTOR_PAYMENT_FLOW_TYPE));
		_pMap.put(PARAM_SELECTOR_VAT_ELIGIBLE, iwc.getParameter(PARAM_SELECTOR_VAT_ELIGIBLE));
		_pMap.put(PARAM_SELECTOR_REG_SPEC_TYPE, iwc.getParameter(PARAM_SELECTOR_REG_SPEC_TYPE));
		_pMap.put(PARAM_SELECTOR_CONDITION_TYPE, iwc.getParameter(PARAM_SELECTOR_CONDITION_TYPE));
		_pMap.put(PARAM_SELECTOR_SPECIAL_CALCULATION, iwc.getParameter(PARAM_SELECTOR_SPECIAL_CALCULATION));
		_pMap.put(PARAM_SELECTOR_VAT_RULE, iwc.getParameter(PARAM_SELECTOR_VAT_RULE));
		_pMap.put(PARAM_CHANGED_SIGN, iwc.getParameter(PARAM_CHANGED_SIGN));
		_pMap.put(PARAM_DISCOUNT, iwc.getParameter(PARAM_DISCOUNT));
		_pMap.put(PARAM_MAX_AMOUNT_DISCOUNT, iwc.getParameter(PARAM_MAX_AMOUNT_DISCOUNT));

		for (int index = 1; index <= 5; index++) {
			_pMap.put(PARAM_SELECTOR_CONDITION +"_" +index, 
					iwc.getParameter(PARAM_SELECTOR_CONDITION +"_" +index));  
			_pMap.put(PARAM_SELECTOR_INTERVAL +"_" +index, 
					iwc.getParameter(PARAM_SELECTOR_INTERVAL +"_" +index));  
		}

		try {
			String id = null;
			
			if (iwc.isParameterSet(PARAM_EDIT_ID)) {
				id = iwc.getParameter(PARAM_EDIT_ID);
			}
			if (iwc.isParameterSet(PARAM_MODE_COPY)) {
				id = null;
			}
			String operationalField = getSession().getOperationalField();
			operationalField = operationalField == null ? "" : operationalField;

			newId = getRegulationBusiness(iwc).saveRegulation(
					id,
					parseDate(iwc.getParameter(PARAM_PERIOD_FROM)),
					parseDate(iwc.getParameter(PARAM_PERIOD_TO)),
					iwc.getParameter(PARAM_NAME),
					iwc.getParameter(PARAM_AMOUNT),
					iwc.getParameter(PARAM_CONDITION_ORDER),
					operationalField,
					iwc.getParameter(PARAM_SELECTOR_PAYMENT_FLOW_TYPE),
					iwc.getParameter(PARAM_SELECTOR_VAT_ELIGIBLE),
					iwc.getParameter(PARAM_SELECTOR_REG_SPEC_TYPE),
					iwc.getParameter(PARAM_SELECTOR_CONDITION_TYPE),
					iwc.getParameter(PARAM_SELECTOR_SPECIAL_CALCULATION),
					iwc.getParameter(PARAM_SELECTOR_VAT_RULE),
					iwc.getParameter(PARAM_CHANGED_SIGN),
					iwc.getParameter(PARAM_DISCOUNT), 
					iwc.getParameter(PARAM_MAX_AMOUNT_DISCOUNT) 
			);
		} catch (RegulationException e) {
			_errorText = localize(e.getTextKey(), e.getDefaultText());
			return false;
		} catch (RemoteException e) {
			super.add(new ExceptionWrapper(e, this));
			return false;
		}

		try {
			for (int index = 1; index <= 5; index++) {
				getRegulationBusiness(iwc).saveCondition(
					""+newId,
					""+index,
					iwc.getParameter(PARAM_SELECTOR_CONDITION +"_" +index), 
					iwc.getParameter(PARAM_SELECTOR_INTERVAL +"_" +index)
				);
			}
		} catch (RegulationException e) {
			_errorText = localize(e.getTextKey(), e.getDefaultText());
			return false;
		} catch (RemoteException e) {
			super.add(new ExceptionWrapper(e, this));
			return false;
		}
		
		closeMe(iwc);

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
		Table topPanel = getTopPanel(iwc, r, error);		
		Table regulationForm = getRegulationForm(iwc, r);
		Table bottomPanel = new Table();
		ButtonPanel buttonPanel = new ButtonPanel(this);

		buttonPanel.addLocalizedButton(PARAM_BUTTON_SAVE, KEY_SAVE, "Spara");
		buttonPanel.addLocalizedButton(PARAM_BUTTON_CANCEL, KEY_CANCEL, "Avbryt");
		bottomPanel.add(getVATPanel(iwc, r), 1, 1);
		bottomPanel.add(buttonPanel, 1, 1);
		
		app.setLocalizedTitle(KEY_HEADER, "Skapa/Ändra regelverk");
		app.setSearchPanel(topPanel);
		app.setMainPanel(regulationForm);
		app.setButtonPanel(bottomPanel);

		if(iwc.isParameterSet(RegulationList.PARAM_RETURN_FROM_DATE)) {
			app.addHiddenInput(RegulationList.PARAM_RETURN_FROM_DATE, iwc.getParameter(RegulationList.PARAM_RETURN_FROM_DATE));
		}
		if(iwc.isParameterSet(RegulationList.PARAM_RETURN_TO_DATE)) {
			app.addHiddenInput(RegulationList.PARAM_RETURN_TO_DATE, iwc.getParameter(RegulationList.PARAM_RETURN_TO_DATE));
		}
		add(app);		

	}


	/*
	 * Returns a top panel
	 * @param iwc user/session context 
	 * @param r Regulation 
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.Regulation#
	 * @return table panel
	 */
	private Table getTopPanel(IWContext iwc, Regulation r, String error) {

		Table table = new Table();
		table.setWidth("80%");
		int row;
		
		String userName = "";
		String mainOpPK = "";
		try {
			mainOpPK = getSession().getOperationalField();
			mainOpPK = mainOpPK == null ? "" : mainOpPK;
		} catch (RemoteException e) {}

		int payStreamPK = Integer.parseInt((String) _pMap.get(PARAM_SELECTOR_PAYMENT_FLOW_TYPE));
		int condTypePK = Integer.parseInt((String) _pMap.get(PARAM_SELECTOR_CONDITION_TYPE));
		int regSpecTypePK = Integer.parseInt((String)_pMap.get(PARAM_SELECTOR_REG_SPEC_TYPE));

		if (r != null && hasNoError()) {
			mainOpPK = r.getOperation() != null ? 
					r.getOperation().getPrimaryKey().toString() : 
					(String)_pMap.get(PARAM_SELECTOR_MAIN_OPERATION);
						
			payStreamPK = Integer.parseInt(r.getPaymentFlowType() != null ? 
					r.getPaymentFlowType().getPrimaryKey().toString() : 
					(String)_pMap.get(PARAM_SELECTOR_PAYMENT_FLOW_TYPE));
					
			condTypePK = Integer.parseInt(r.getConditionType() != null ? 
					r.getConditionType().getPrimaryKey().toString() : 
					(String) _pMap.get(PARAM_SELECTOR_CONDITION_TYPE));
					
			regSpecTypePK = Integer.parseInt(r.getRegSpecType() != null ? 
					r.getRegSpecType().getPrimaryKey().toString() : 
					(String)_pMap.get(PARAM_SELECTOR_REG_SPEC_TYPE));
		}
		if (iwc.isLoggedOn()) {
			User user = iwc.getCurrentUser();
			userName = user.getFirstName();
		}
		
		
		Timestamp rightNow = IWTimestamp.getTimestampRightNow();
		//Date dd = new Date(System.currentTimeMillis());
		int startRow = 1;
		if (error.length() != 0) {
			table.add(getErrorText(error), 1, startRow);
			table.mergeCells(1, startRow, 3, startRow);
			startRow++;
		}
		row = startRow;	
		table.add(getLocalizedLabel(KEY_HEADER_OPERATION, "Huvudverksamhet"), 1, row++);
		table.add(getLocalizedLabel(KEY_HEADER_NAME, "Benämning"), 1, row++);
		table.add(getLocalizedLabel(KEY_HEADER_AMOUNT, "Månadsbelopp"), 1, row++);
		table.add(getLocalizedLabel(KEY_HEADER_DISCOUNT, "Rabattsats"), 1, row++);
		table.add(getLocalizedLabel(KEY_FROM_DATE, "Från datum"),1 ,row++);
		table.add(getLocalizedLabel(KEY_CHANGE_DATE, "Ändringsdatum"),1 ,row++);

		row = startRow;	
		table.add(new OperationalFieldsMenu(), 2, row++);

//		table.add(mainOperationSelector(iwc, PARAM_SELECTOR_MAIN_OPERATION, mainOpPK), 2, row++);
		
		table.add(getTextInput(PARAM_NAME, r != null && hasNoError() ? r.getName() : 
				(String) _pMap.get(PARAM_NAME), 200, 40), 2, row++);
				
		table.add(getTextInput(PARAM_AMOUNT, r != null && hasNoError() ? 
				formatCash(r.getAmount()) : 
				(String) _pMap.get(PARAM_AMOUNT), 60, 10), 2, row++);
				
		table.add(getTextInput(PARAM_DISCOUNT, r != null  && hasNoError() ? ""+r.getDiscount() : 
				(String) _pMap.get(PARAM_DISCOUNT), 40, 5), 2, row);
		table.add(getSmallText("%"), 2, row++);
		
		table.add(getTextInput(PARAM_PERIOD_FROM, (formatDate(r != null && hasNoError() ? 
				r.getPeriodFrom() : 
				parseDate((String) _pMap.get(PARAM_PERIOD_FROM)), 4)), 
				40, 4), 2, row++);
				
		String dts = formatDate(r != null && hasNoError() ? r.getChangedDate(): rightNow, 6);	
		table.add(getText(r != null ? dts : ""), 2, row++);

		row = startRow;	

		table.add(getLocalizedLabel(KEY_HEADER_PAYMENT_FLOW, "Ström"),3 ,row++);
		table.add("", 3, row++);
		table.add("", 3, row++);
		table.add(getLocalizedLabel(KEY_MAX_AMOUNT, "Maxbelopp"),3 ,row++);
		table.add(getLocalizedLabel(KEY_TO_DATE, "Tom datum"),3 ,row++);
		table.add(getLocalizedLabel(KEY_CHANGE_SIGN, "Ändringssignatur"),3 ,row++);

		row = startRow;	

		table.add(paymentFlowTypeSelector(iwc, PARAM_SELECTOR_PAYMENT_FLOW_TYPE, payStreamPK), 4, row++);
		table.add("", 4, row++);
		table.add("", 4, row++);

		table.add(getTextInput(PARAM_MAX_AMOUNT_DISCOUNT, r != null && hasNoError() ? 
				""+r.getMaxAmountDiscount() : 
				(String) _pMap.get(PARAM_MAX_AMOUNT_DISCOUNT), 40, 5), 4, row);
		table.add(getSmallText("%"), 4, row++);

		table.add(getTextInput(PARAM_PERIOD_TO, (formatDate(r != null && hasNoError() ? 
				r.getPeriodTo() : 
				parseDate((String) _pMap.get(PARAM_PERIOD_TO)), 4)),
				40, 4), 4, row++);
		table.add(getText(r != null ? r.getChangedSign() : ""), 4, row++);
		table.add(new HiddenInput(PARAM_CHANGED_SIGN, ""+userName));

		table.add(getLocalizedLabel(KEY_CONDITION_TYPE_HEADER, "Villkorstyp"), 1, row);
		table.add(getLocalizedLabel(KEY_REG_SPEC_TYPE_HEADER, "Regelspec.typ"), 1, row+1);
		table.add(conditionTypeSelector(iwc, PARAM_SELECTOR_CONDITION_TYPE, condTypePK), 2, row);
		table.add(regSpecTypeSelector(iwc, PARAM_SELECTOR_REG_SPEC_TYPE, regSpecTypePK), 2, row+1);
		table.add(getLocalizedLabel(KEY_CONDITION_ORDER_HEADER, "Villkorsordning"), 3, row);
	
		if (r != null && hasNoError()) {
			table.add(getTextInput(PARAM_CONDITION_ORDER, r.getConditionOrder() != null ? 
					""+r.getConditionOrder().intValue() : 
					(String) _pMap.get(PARAM_CONDITION_ORDER), 
					40, 4), 4, row);
		} else {
			table.add(getTextInput(PARAM_CONDITION_ORDER, 
					(String) _pMap.get(PARAM_CONDITION_ORDER), 40, 4), 4, row);
		}

		if (iwc.isParameterSet(PARAM_MODE_COPY)) {
			table.add(new HiddenInput(PARAM_MODE_COPY, ""+iwc.getParameter(PARAM_MODE_COPY)));
		}
		if (iwc.isParameterSet(PARAM_EDIT_ID)) {
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
		Table table = new Table();
		int row = 1;
	
		row += 2;
		Collection conditions = null;
		Iterator iter = null;
		conditions = findAllConditionsByRegulation(iwc, r);
		if (conditions != null) {
			iter = conditions.iterator();
		}
		
		for (int index = 1; index <= 5; index++) {
			Condition field = null;
			if (iter != null) {
				if (iter.hasNext()) { 
					field = (Condition) iter.next();
				}
			}
			table.add(getLocalizedLabel(KEY_CONDITION_HEADER + index, "Villkor " + index), 1, row);
			ExtendedDropdownDouble dDrop = (ExtendedDropdownDouble) 
					getStyledInterface(conditionSelector(
					iwc, 
					PARAM_SELECTOR_CONDITION +"_" +index, 
					PARAM_SELECTOR_INTERVAL +"_" +index)
			);
			if (field != null) {
				dDrop.setSelectedValues(""+field.getConditionID(), ""+field.getIntervalID());
			} else {
				dDrop.setSelectedValues(
						(String)_pMap.get(PARAM_SELECTOR_CONDITION +"_" +index), 
						(String)_pMap.get(PARAM_SELECTOR_INTERVAL +"_" +index));
			}
			table.add(dDrop, 2, row++);
			
		}
		table.setColumnWidth(1, COLUMN_WIDTH);	
		mtable.add(table, 1, 1);
		
		return mtable;
	}

	/*
	 * Returns a VAT panel with selectors VAT 
	 * @param iwc user/session context 
	 * @return table panel
	 */
	private Table getVATPanel(IWContext iwc, Regulation r) {
		
		int vatYesNoID = Integer.parseInt((String) _pMap.get(PARAM_SELECTOR_VAT_ELIGIBLE));
		int vatRulePK = Integer.parseInt((String) _pMap.get(PARAM_SELECTOR_VAT_RULE));
		int specCalcPK = Integer.parseInt((String) _pMap.get(PARAM_SELECTOR_SPECIAL_CALCULATION));

		Table table = new Table();
		table.add(getLocalizedLabel(KEY_HEADER_SPECIAL_CALCULATION, "Specialberäkning"), 1, 1);
		table.add(getLocalizedLabel(KEY_HEADER_VAT_ELIGIBLE, "Momsersättning"), 1, 2);
		table.add(getLocalizedLabel(KEY_HEADER_VAT_REGULATION, "Momsregel"), 1, 3);

		if (r != null && hasNoError()) {
			vatYesNoID = r.getVATEligible() != null ? r.getVATEligible().intValue() :
					 Integer.parseInt((String) _pMap.get(PARAM_SELECTOR_VAT_ELIGIBLE));	
			vatRulePK = Integer.parseInt(r.getVATRuleRegulation() != null ? 
					r.getVATRuleRegulation().getPrimaryKey().toString() : 
					(String) _pMap.get(PARAM_SELECTOR_VAT_RULE));
			specCalcPK = Integer.parseInt(r.getSpecialCalculation() != null ? 
					r.getSpecialCalculation().getPrimaryKey().toString() : 
					(String) _pMap.get(PARAM_SELECTOR_SPECIAL_CALCULATION));
		}
		
		table.add(specialCalculationSelector(iwc, PARAM_SELECTOR_SPECIAL_CALCULATION, specCalcPK), 2, 1);

		table.add(vatYesNoSelector(PARAM_SELECTOR_VAT_ELIGIBLE, vatYesNoID), 2, 2);
		table.add(vatRuleSelector(iwc, PARAM_SELECTOR_VAT_RULE, vatRulePK), 2, 3);
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
		Regulation r = null;
		try {
			int regulationID = 0;
			
			if (iwc.isParameterSet(PARAM_EDIT_ID)) {
				regulationID = Integer.parseInt(iwc.getParameter(PARAM_EDIT_ID));
			}
			r = getRegulationBusiness(iwc).findRegulation(regulationID);
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
	private DropdownMenu regSpecTypeSelector(IWContext iwc, String name, int refIndex) {
		
		DropdownMenu menu = null;
		try {
			menu = (DropdownMenu) getStyledInterface(
				getDropdownMenuLocalized(name, getRegulationBusiness(iwc).findAllRegulationSpecTypes(), 
				"getLocalizationKey"));
			menu.setSelectedElement(refIndex);
			menu.addMenuElementFirst("0", localize(KEY_MENU_REG_SPEC_HEADER, "Välj regelspecificationstyp"));
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
	private DropdownMenu specialCalculationSelector(IWContext iwc, String name, int refIndex) {
		
		DropdownMenu menu = null;
		try {
			menu = (DropdownMenu) getStyledInterface(
				getDropdownMenuLocalized(name, getRegulationBusiness(iwc).findAllSpecialCalculationTypes(), 
				"getLocalizationKey"));
			menu.setSelectedElement(refIndex);
			menu.addMenuElementFirst("0", localize(KEY_MENU_SPEC_CALC_HEADER, "Välj specialberäkning"));
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
	private DropdownMenu vatRuleSelector(IWContext iwc, String name, int refIndex) {
		
		DropdownMenu menu = null;
		try {
				Collection vatRules = getRegulationBusiness(iwc).findAllVATRuleRegulations();
				menu = (DropdownMenu) getStyledInterface(
				getDropdownMenuLocalized(name,vatRules,"getLocalizationKey"));
			menu.addMenuElementFirst("0", localize(KEY_MENU_VAT_RULE_HEADER, "Välj momsregel"));
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
	private DropdownMenu vatYesNoSelector(String name, int refIndex) {
		
		DropdownMenu menu = (DropdownMenu) getStyledInterface(new DropdownMenu(name));
		menu.addMenuElement(1, localize(KEY_VAT_YES, "Ja"));
		menu.addMenuElement(2, localize(KEY_VAT_NO, "Nej"));
		menu.addMenuElementFirst("0", localize(KEY_MENU_VAT_HEADER, "Välj"));
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
	private DropdownMenu conditionTypeSelector(IWContext iwc, String name, int refIndex) {
		
		DropdownMenu menu = null;
		try {
			menu = (DropdownMenu) getStyledInterface(
			getDropdownMenuLocalized(name, getRegulationBusiness(iwc).findAllConditionTypes(), 
			"getLocalizationKey"));
			menu.addMenuElementFirst("0", localize(KEY_MENU_COND_TYPE_HEADER, "Välj villkorstyp"));
			menu.setSelectedElement(refIndex);
		} catch (Exception e) {
		}
		return menu;
	}

	private ExtendedDropdownDouble conditionSelector(IWContext iwc, String primaryName, String secondaryName) {
		
		ExtendedDropdownDouble dropdown = new ExtendedDropdownDouble(this, primaryName, secondaryName);
		String emptyString = localize(KEY_CHOOSE_INTERVAL, "Välj intervall");
		dropdown.addEmptyElement(localize(KEY_CHOOSE_CONDITION, "Välj Villkor"), emptyString);
		try {
			String op = getSession().getOperationalField();
			op = op == null ? "" : op;

			Collection conditions = getRegulationBusiness(iwc).findAllConditionSelections(op);
				
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
					dropdown.addMenuElement(""+cond.getPrimaryKey(), cond.getLocalizationKey(), (Map) mainMap.get(cond), cond.getDataMethodName());
//					dropdown.addMenuElement(""+cond.getPrimaryKey(), cond.getName(), (Map) mainMap.get(cond), cond.getDataMethodName());
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
			Class partypes[] = new Class[1];
			Object[] args = new Object[1];

			if(condition.getDataParameter().length() != 0) {
				args[0] = condition.getDataParameter();
				partypes[0] = String.class;
			} else {
				partypes = null;
				args = null;
			}
			Method myMethod;
			Class cls = condition.getBusinessClass();
			Object business = com.idega.business.IBOLookup.getServiceInstance(iwc, cls);
			myMethod = cls.getMethod(condition.getCollectDataMethod(), partypes);
			Collection data = (Collection) myMethod.invoke(business, args);
			return data;
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			return col;
		}
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
/*
	private DropdownMenu mainOperationSelector(IWContext iwc, String name, String refIndex) {
		
		DropdownMenu menu = null;
		try {
			menu = (DropdownMenu) getStyledInterface(
				getDropdownMenuLocalized(name, getSchoolBusiness(iwc).getSchoolCategories(),"getLocalizationKey"));
		} catch (Exception e) {
		}
		menu.addMenuElementFirst("0", localize(KEY_MENU_OPERATION_HEADER, "Ingen Huvudverksamhet vald"));
		menu.setSelectedElement(refIndex);
		return menu;
	}
*/

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
		menu.addMenuElementFirst("0", localize(KEY_MENU_PAY_FLOW_HEADER, "Välj ström"));
		menu.setSelectedElement(refIndex);
		return menu;
	}


	
	private Collection findAllConditionsByRegulation(IWContext iwc,  Regulation r) {
		Collection c = null;
		try {
			c = getRegulationBusiness(iwc).findAllConditionsByRegulation(r);
		} catch (Exception e) {
		}
		return c;	
	}

	private void closeMe(IWContext iwc) {
		String backUrl = BuilderLogic.getInstance().getIBPageURL(iwc, ((Integer)_responsePage.getPrimaryKey()).intValue());
		backUrl += 	"&"	+ RegulationList.PARAM_SELECTOR_PAYMENT_FLOW_TYPE + "=" + 
						iwc.getParameter(PARAM_SELECTOR_PAYMENT_FLOW_TYPE)+
					"&"	+ RegulationList.PARAM_RETURN_FROM_DATE + "=" + 
						iwc.getParameter(RegulationList.PARAM_RETURN_FROM_DATE)+
					"&"	+ RegulationList.PARAM_RETURN_TO_DATE + "=" + 
						iwc.getParameter(RegulationList.PARAM_RETURN_TO_DATE);
		getParentPage().setToRedirect(backUrl);
	}

	private void setDefaultParameters() {
	//	String ds = formatDate(new Date(System.currentTimeMillis()), 4);
		if(_pMap == null) {
			_pMap = new HashMap();
		}
		if(!_pMap.containsKey(PARAM_PERIOD_FROM)) {
			_pMap.put(PARAM_PERIOD_FROM, "" );
			_pMap.put(PARAM_PERIOD_TO, "");
			_pMap.put(PARAM_NAME, "");
			_pMap.put(PARAM_AMOUNT, "0");
			_pMap.put(PARAM_CONDITION_ORDER, "");
			_pMap.put(PARAM_SELECTOR_PAYMENT_FLOW_TYPE, "0");
			_pMap.put(PARAM_SELECTOR_VAT_ELIGIBLE, "0");
			_pMap.put(PARAM_SELECTOR_REG_SPEC_TYPE, "0");
			_pMap.put(PARAM_SELECTOR_CONDITION_TYPE, "0");
			_pMap.put(PARAM_SELECTOR_SPECIAL_CALCULATION, "0");
			_pMap.put(PARAM_SELECTOR_VAT_RULE, "0");
			_pMap.put(PARAM_CHANGED_SIGN, "");
			_pMap.put(PARAM_DISCOUNT, "");
			_pMap.put(PARAM_MAX_AMOUNT_DISCOUNT, "");
		}
	}

	private String formatCash(Integer cash) {
		return ""+(cash.intValue());
	}

	private boolean hasNoError() {
		return _errorText.length() == 0 ? true : false;
	}

//	private SchoolBusiness getSchoolBusiness(IWContext iwc) throws RemoteException {
//		return (SchoolBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
//	}
	
	private RegulationsBusiness getRegulationBusiness(IWContext iwc) throws RemoteException {
		return (RegulationsBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, RegulationsBusiness.class);
	}
	/*private VATBusiness getVATBusiness(IWContext iwc) throws RemoteException {
		return (VATBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, VATBusiness.class);
	}*/
}


