/*
 * $Id: RegulationSpecificationEditor.java,v 1.9 2003/09/09 14:09:44 laddi Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.regulations.presentation;

import java.util.Collection;
import java.util.Iterator;
import java.rmi.RemoteException;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.SubmitButton;


import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.ApplicationForm;
import se.idega.idegaweb.commune.accounting.presentation.ListTable;
import se.idega.idegaweb.commune.accounting.presentation.ButtonPanel;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationException;
/** 
 * RegulationSpecificationEditor is an idegaWeb block that handles RegSpec types
 * <p>
 * $Date: 2003/09/09 14:09:44 $
 *
 * @author Kjell Lindman
 * @version $Revision: 1.9 $
 */
public class RegulationSpecificationEditor extends AccountingBlock {

	private final static int ACTION_DEFAULT = 0;
	private final static int ACTION_CANCEL = 1;
	private final static int ACTION_NEW = 2;
	private final static int ACTION_EDIT = 3;
	private final static int ACTION_SAVE = 4;
	private final static int ACTION_DELETE = 5;
	
	private final static String PP = "cacc_reg_spec_"; // Parameter prefix 
	private final static String Z = "z_cacc_reg_spec."; // Auto localize prefix 

	private final static String PARAMETER_NEW = PP + "new";
	private final static String PARAMETER_SAVE = PP + "save";
	private final static String PARAMETER_CANCEL = PP + "cancel";
	private final static String PARAMETER_DELETE_ID = PP + "delete_id";
	private final static String PARAMETER_EDIT = PP + "edit";
	private final static String PARAMETER_REGULATION_SPEC_TYPE_ID = PP + "reg_spec_type_id";
	private final static String PARAMETER_REGULATION_SPEC_TYPE = PP + "reg_spec_type";
	private final static String PARAMETER_MAIN_RULE_ID = PP + "main_rule_id";

	
	private final static String KP = "reg_spec_editor."; // key prefix 
	
	private final static String KEY_TITLE = KP + "title";
	private final static String KEY_REG_SPEC_TYPE = KP + "reg_spec_type";
	private final static String KEY_TITLE_EDIT = KP + "title_edit";
	private final static String KEY_TITLE_ADD = KP + "title_add";
	private final static String KEY_HEADER_REG_SPEC_TYPE = KP + "reg_spec_type_header";
	private final static String KEY_HEADER_MAIN_RULE = KP + "main_rule_header";
	private final static String KEY_NEW = KP + "new";
	private final static String KEY_MAIN_RULE = KP + "main_rule";
	private final static String KEY_SAVE = KP + "save";
	private final static String KEY_CANCEL = KP + "cancel";
	private final static String KEY_CLICK_EDIT = KP + "click_edit";
	private final static String KEY_CLICK_REMOVE = KP + "click_delete";
	private final static String KEY_REMOVE_CONFIRM = KP + "remove_confirm";
	private final static String KEY_MAIN_RULE_DROP_HEADER= KP + "main_rule_drop_header";
	private final static String KEY_SELECT_MAIN_RULE_ERROR = KP + "select_main_rule_error";
	private final static String DEFAULT_SELECT_MAIN_RULE_ERROR = KP + "Var god välj en huvudregel!";
	

	/**
	 * @see com.idega.presentation.Block#main()
	 */
	public void init(final IWContext iwc) {
		try {
			int action = parseAction(iwc);
			switch (action) {
				case ACTION_DEFAULT:
					handleDefaultAction(iwc);
					break;
				case ACTION_CANCEL:
					handleDefaultAction(iwc);
					break;
				case ACTION_NEW:
					handleNewAction(iwc);
					break;
				case ACTION_EDIT:
					handleOpenAction(iwc);
					break;
				case ACTION_SAVE:
					handleSaveAction(iwc);
					break;
				case ACTION_DELETE:
					handleDeleteAction(iwc);
					break;
			}
		}
		catch (Exception e) {
			add(new ExceptionWrapper(e, this));
		}
	}

	/*
	 * Returns the action constant for the action to perform based 
	 * on the POST parameters in the specified context.
	 */
	private int parseAction(IWContext iwc) {
		int action = ACTION_DEFAULT;
		
		if (iwc.isParameterSet(PARAMETER_CANCEL)) {
			action = ACTION_CANCEL;
		} else if (iwc.isParameterSet(PARAMETER_NEW)) {
			action = ACTION_NEW;
		} else if (iwc.isParameterSet(PARAMETER_SAVE)) {
			action = ACTION_SAVE;
		} else if (iwc.isParameterSet(PARAMETER_EDIT)) {
			action = ACTION_EDIT;
		} else if (iwc.isParameterSet(PARAMETER_DELETE_ID)) {
			action = ACTION_DELETE;
		}
		return action;
	}

	/*
	 * Handles the default action for this block.
	 */	
	private void handleDefaultAction(IWContext iwc) {
		ApplicationForm app = new ApplicationForm(this);
		app.setLocalizedTitle(KEY_TITLE, "Regelspecificering");
		app.setMainPanel(getRegulationsList(iwc));
		app.setButtonPanel(getButtonPanel());
		app.addHiddenInput(PARAMETER_DELETE_ID, "0");
		add(app);
	}

	/*
	 * Handles the new action for this block.
	 */	
	private void handleNewAction(IWContext iwc) {
		add(getRegulationSpecTypeForm(
				iwc,
				getParameter(iwc, PARAMETER_REGULATION_SPEC_TYPE_ID),
				getParameter(iwc, PARAMETER_REGULATION_SPEC_TYPE),
				getIntParameter(iwc, PARAMETER_MAIN_RULE_ID),
				null,
				true
				)
		);
	}

	/*
	 * Handles the open action (link clicked in the list) for this block.
	 */	
	private void handleOpenAction(IWContext iwc) {
		try {
			RegulationsBusiness rb = getRegulationsBusiness(iwc);
			RegulationSpecType rst = (RegulationSpecType) rb.findRegulationSpecType(
					getIntParameter(iwc, PARAMETER_REGULATION_SPEC_TYPE_ID)
			);
					
			add(getRegulationSpecTypeForm(
					iwc,
					rst.getPrimaryKey().toString(),
					rst.getLocalizationKey(),
					Integer.parseInt(rst.getMainRule().getPrimaryKey().toString()),
					null,
					false)
			);
		} catch (RemoteException e) {
			add(new ExceptionWrapper(e));
		}
	}
	/*
	 * Handles the save action for this block.
	 */
	private void handleSaveAction(IWContext iwc) {
		String errorMessage = null;

		try {
			if(getIntParameter(iwc, PARAMETER_MAIN_RULE_ID) == 0) {
				throw new RegulationException(KEY_SELECT_MAIN_RULE_ERROR, DEFAULT_SELECT_MAIN_RULE_ERROR);
			}
			RegulationsBusiness rb = getRegulationsBusiness(iwc);
			localize(
					getLocalizedKey(iwc, PARAMETER_REGULATION_SPEC_TYPE),
					getParameter(iwc, PARAMETER_REGULATION_SPEC_TYPE)
			);
			rb.saveRegulationSpecType(
					getIntParameter(iwc, PARAMETER_REGULATION_SPEC_TYPE_ID),
					getLocalizedKey(iwc, PARAMETER_REGULATION_SPEC_TYPE),
					getIntParameter(iwc, PARAMETER_MAIN_RULE_ID));	
		} catch (RemoteException e) {
			add(new ExceptionWrapper(e));
			return;
		} catch (RegulationException e) {
			errorMessage = localize(e.getTextKey(), e.getDefaultText());
		}

		if (errorMessage != null) {
			add(getRegulationSpecTypeForm(
					iwc,
					getParameter(iwc, PARAMETER_REGULATION_SPEC_TYPE_ID),
					getParameter(iwc, PARAMETER_REGULATION_SPEC_TYPE),
					getIntParameter(iwc, PARAMETER_MAIN_RULE_ID),
					errorMessage,
					!iwc.isParameterSet(PARAMETER_EDIT))
			);
		} else {
			handleDefaultAction(iwc);
		}
		
	}


	private String getLocalizedKey(IWContext iwc, String param) {
		String ret = getParameter(iwc, param);
		ret = ret.toLowerCase().replace(' ', '_');
		ret = ret.replace(' ', '_');
		ret = ret.replace('å', 'a');
		ret = ret.replace('ä', 'a');
		ret = ret.replace('ö', 'o');
		ret = ret.replace('Å', 'a');
		ret = ret.replace('Ä', 'a');
		ret = ret.replace('Ö', 'o');
		
/*
		ret = ret.toLowerCase().replaceAll(" ", "_").replaceAll("å","aa").replaceAll("ä","ae").replaceAll("ö", "oe");
		ret = ret.replaceAll("Å","aa").replaceAll("Ä","ae").replaceAll("Ö", "oe");
		ret = ret.replaceAll("'","_").replaceAll(".","_").replaceAll("`", "_");
		ret = ret.replaceAll("`","_").replaceAll(" ","_").replaceAll("=", "_");
*/
		return Z + ret;
	}

	/*
	 * Handles the delete action for this block.
	 */	
	private void handleDeleteAction(IWContext iwc) {
		try {
			RegulationsBusiness rb = getRegulationsBusiness(iwc);
			rb.deleteRegulationSpecType(
					getIntParameter(iwc, PARAMETER_DELETE_ID)
			);
		} catch (RemoteException e) {
			add(new ExceptionWrapper(e));
		} catch (RegulationException e) {
			add(getErrorText(localize(e.getTextKey(), e.getDefaultText())));
		}
		handleDefaultAction(iwc);
	}
	 
	
	/*
	 * Returns a list of  Regulation Specification Types
	 */
	private Table getRegulationsList(IWContext iwc) {
		String errorMessage = null;
		Collection regSpecTypes = null;

		try {
			RegulationsBusiness rb = getRegulationsBusiness(iwc);
			try {
					regSpecTypes = rb.findAllRegulationSpecTypes();
			} catch (RegulationException e) {
				errorMessage = localize(e.getTextKey(), e.getDefaultText());
			}
		} catch (RemoteException e) {
			Table t = new Table();
			t.add(new ExceptionWrapper(e));
			return t;
		}

		ListTable list = new ListTable(this, 4);
		list.setLocalizedHeader(KEY_HEADER_REG_SPEC_TYPE, "Regelspecificeringstyp", 1);
		list.setLocalizedHeader(KEY_HEADER_MAIN_RULE, "Huvudregel", 2);
		list.setHeader(" ", 3);
		list.setHeader(" ", 4);

		if (regSpecTypes != null) {
			Iterator iter = regSpecTypes.iterator();
			if(iter.hasNext()) {
					iter.next();
			}
			while (iter.hasNext()) {
				RegulationSpecType rst = (RegulationSpecType) iter.next();
				list.add(rst.getLocalizationKey(), rst.getRegSpecType());
				list.add(rst.getMainRule().getLocalizationKey(),rst.getMainRule().getMainRule());

				Link edit = new Link(getEditIcon(localize(KEY_CLICK_EDIT, "Redigera")));
				edit.addParameter(PARAMETER_REGULATION_SPEC_TYPE_ID, rst.getPrimaryKey().toString());
				edit.addParameter(PARAMETER_EDIT, "1");
				list.add(edit);
	
				SubmitButton delete = new SubmitButton(getDeleteIcon(localize(KEY_CLICK_REMOVE, "Radera")));
				delete.setDescription(localize(KEY_CLICK_REMOVE, "Klicka här för att radera post"));
				delete.setValueOnClick(PARAMETER_DELETE_ID, rst.getPrimaryKey().toString());
				delete.setSubmitConfirm(localize(KEY_REMOVE_CONFIRM, "Vill du verkligen radera denna post?"));
				list.add(delete);
	

			}
		}

		Table mainPanel = new Table();
		mainPanel.setCellpadding(0);
		mainPanel.setCellspacing(0);
	
		if (errorMessage != null) {
			Table t = new Table();
			t.setCellpadding(getCellpadding());
			t.setCellspacing(getCellspacing());
			t.add(getErrorText(errorMessage), 1, 1);
			mainPanel.add(t, 1, 1);
			mainPanel.add(list, 1, 2);	
		} else {
			mainPanel.add(list, 1, 1);
		}
		
		return mainPanel;
	}

	/*
	 * Returns the default button panel for this block.
	 */
	private ButtonPanel getButtonPanel() {
		ButtonPanel bp = new ButtonPanel(this);
		bp.addLocalizedButton(PARAMETER_NEW, KEY_NEW, "Ny");
		return bp;
	}
	
	/*
	 * Returns the application form for creating or editing a Regulation spec type
	 */
	private ApplicationForm getRegulationSpecTypeForm(
			IWContext iwc,
			String id,
			String regSpecType,
			int mainRuleId,
			String errorMessage,
			boolean isNew) {
		ApplicationForm app = new ApplicationForm(this);
		if (isNew) {
			app.setLocalizedTitle(KEY_TITLE_ADD, "Skapa ny regelspecificering");
		} else {
			app.setLocalizedTitle(KEY_TITLE_EDIT, "Redigera regelspecificering");
		}
		
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());

		table.add(getLocalizedLabel(KEY_REG_SPEC_TYPE, "Regespecificeringstyp"), 1, 1);
		if(isNew) {
			table.add(getTextInput(PARAMETER_REGULATION_SPEC_TYPE, regSpecType, 200), 2, 1);
		} else {
			table.add(getTextInput(PARAMETER_REGULATION_SPEC_TYPE, localize(regSpecType, regSpecType), 200), 2, 1);
		}

		table.add(getLocalizedLabel(KEY_MAIN_RULE, "Huvudregel"), 1, 2);
		table.add(getMainRuleDropdownMenu(iwc, PARAMETER_MAIN_RULE_ID, mainRuleId), 2, 2);


		Table mainPanel = new Table();
		mainPanel.setCellpadding(0);
		mainPanel.setCellspacing(0);
		
		if (errorMessage != null) {
			Table t = new Table();
			t.setCellpadding(getCellpadding());
			t.setCellspacing(getCellspacing());
			t.add(getErrorText(errorMessage), 1, 1);
			mainPanel.add(t, 1, 1);
			mainPanel.add(table, 1, 2);
		} else {
			mainPanel.add(table, 1, 1);
		}
		app.addHiddenInput(PARAMETER_REGULATION_SPEC_TYPE_ID, id);
		app.setMainPanel(mainPanel);	
		
		ButtonPanel bp = new ButtonPanel(this);
		bp.addLocalizedButton(PARAMETER_SAVE, KEY_SAVE, "Spara");
		bp.addLocalizedButton(PARAMETER_CANCEL, KEY_CANCEL, "Avbryt");
		app.setButtonPanel(bp);
		
		return app;		
	}
	
	/*
	 * Generates a DropDownSelector for Main Rules that is collected 
	 * from the regulation framework. 
	 *    
	 * @param iwc Idega Web Context 
	 * @param name HTML Parameter ID for this selector
	 * @param refIndex The initial position to set the selector to 
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.MainRuleBMPBean
	 * @return the drop down menu
	 */
	private DropdownMenu getMainRuleDropdownMenu(IWContext iwc, String name, int refIndex) {
		
		DropdownMenu menu = null;
		try {
			menu = (DropdownMenu) getStyledInterface(
					getDropdownMenuLocalized(name, getRegulationsBusiness(iwc).findAllMainRules(), 
					"getLocalizationKey"));
			menu.addMenuElementFirst("0", localize(KEY_MAIN_RULE_DROP_HEADER, "Välj Huvudregel"));
			menu.setSelectedElement(refIndex);
		} catch (RemoteException e) {}
		return menu;
	}

	
	/*
	 * Returns a regulations business object
	 */
	private RegulationsBusiness getRegulationsBusiness(IWContext iwc) throws RemoteException {
		return (RegulationsBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, RegulationsBusiness.class);
	}

}
