/*
 * $Id: VATEditor.java,v 1.29 2004/10/15 10:36:38 thomas Exp $
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
import java.sql.Date;
import java.rmi.RemoteException;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.text.Link;

import com.idega.block.school.data.SchoolCategory;

import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.OperationalFieldsMenu;
import se.idega.idegaweb.commune.accounting.presentation.ApplicationForm;
import se.idega.idegaweb.commune.accounting.presentation.ListTable;
import se.idega.idegaweb.commune.accounting.presentation.ButtonPanel;
import se.idega.idegaweb.commune.accounting.regulations.data.PaymentFlowType;
import se.idega.idegaweb.commune.accounting.regulations.data.VATRegulation;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness;
import se.idega.idegaweb.commune.accounting.regulations.business.VATBusiness;
import se.idega.idegaweb.commune.accounting.regulations.business.VATException;
import se.idega.idegaweb.commune.care.data.ProviderType;

/** 
 * VATEditor is an idegaWeb block that handles VAT values and
 * VAT regulations for providers.
 * <p>
 * Last modified: $Date: 2004/10/15 10:36:38 $ by $Author: thomas $
 *
 * @author Anders Lindman
 * @version $Revision: 1.29 $
 */
public class VATEditor extends AccountingBlock {

	private final static int ACTION_DEFAULT = 0;
	private final static int ACTION_CANCEL = 1;
	private final static int ACTION_SEARCH = 2;
	private final static int ACTION_NEW = 3;
	private final static int ACTION_OPEN = 4;
	private final static int ACTION_SAVE = 5;
	private final static int ACTION_DELETE = 6;
	private final static int ACTION_COPY = 7;
	
	private final static String PP = "cacc_vat_"; // Parameter prefix 

	private final static String PARAMETER_SEARCH_PERIOD_FROM = PP + "period_search_from";
	private final static String PARAMETER_SEARCH_PERIOD_TO = PP + "period_search_to";
	private final static String PARAMETER_OPERATIONAL_FIELD = PP + "operational_field";
	private final static String PARAMETER_PERIOD_FROM = PP + "period_from";
	private final static String PARAMETER_PERIOD_TO = PP + "period_to";
	private final static String PARAMETER_DESCRIPTION = PP + "description";
	private final static String PARAMETER_VAT_PERCENT = PP + "vat_percent";
	private final static String PARAMETER_PAYMENT_FLOW_TYPE_ID = PP + "payment_flow_type_id";
	private final static String PARAMETER_PROVIDER_TYPE_ID = PP + "provider_type_id";
	private final static String PARAMETER_VAT_REGULATION_ID = PP + "vat_regulation_id";
	private final static String PARAMETER_DELETE_ID = PP + "delete_id";
	private final static String PARAMETER_COPY_ID = PP + "copy_id";
	private final static String PARAMETER_SEARCH = PP + "search";
	private final static String PARAMETER_NEW = PP + "new";
	private final static String PARAMETER_SAVE = PP + "save";
	private final static String PARAMETER_CANCEL = PP + "cancel";
	private final static String PARAMETER_EDIT = PP + "edit";
	
	private final static String KP = "vat_editor."; // key prefix 
	
	private final static String KEY_TITLE = KP + "title";
	private final static String KEY_TITLE_SEARCH = KP + "title_search";
	private final static String KEY_TITLE_ADD = KP + "title_add";
	private final static String KEY_TITLE_EDIT = KP + "title_edit";
	private final static String KEY_TITLE_DELETE_CONFIRM = KP + "title_delete_confirm";
	private final static String KEY_PERIOD = KP + "period";
	private final static String KEY_DESCRIPTION = KP+ "description";
	private final static String KEY_VAT_PERCENT = KP + "vat_percent";
	private final static String KEY_PAYMENT_FLOW_TYPE = KP + "payment_flow_type";
	private final static String KEY_PROVIDER_TYPE = KP + "provider_type";
	private final static String KEY_OPERATIONAL_FIELD = KP + "operational_field";
	private final static String KEY_OPERATIONAL_FIELD_SELECTOR_HEADER = KP+ "operational_field_selector_header";
	private final static String KEY_PAYMENT_FLOW_TYPE_HEADER = KP + "payment_flow_type_header";
	private final static String KEY_PROVIDER_TYPE_HEADER = KP + "provider_type_header";
	private final static String KEY_SEARCH = KP + "search";
	private final static String KEY_NEW = KP + "new";
	private final static String KEY_SAVE = KP + "save";
	private final static String KEY_EDIT = KP + "edit";
	private final static String KEY_COPY = KP + "copy";
	private final static String KEY_CANCEL = KP + "cancel";
	private final static String KEY_DELETE = KP + "delete";
	private final static String KEY_DELETE_CONFIRM = KP + "delete_confirm";
	private final static String KEY_BUTTON_EDIT = KP + "button_edit";
	private final static String KEY_BUTTON_COPY = KP + "button_copy";
	private final static String KEY_BUTTON_DELETE = KP + "button_delete";	

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
				case ACTION_SEARCH:
					handleSearchAction(iwc);
					break;
				case ACTION_NEW:
					handleNewAction(iwc);
					break;
				case ACTION_OPEN:
					handleOpenAction(iwc, false);
					break;
				case ACTION_COPY:
					handleOpenAction(iwc, true);
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
		
		if (iwc.isParameterSet(PARAMETER_SEARCH)) {
			action = ACTION_SEARCH;
		} else if (iwc.isParameterSet(PARAMETER_CANCEL)) {
			action = ACTION_CANCEL;
		} else if (iwc.isParameterSet(PARAMETER_NEW)) {
			action = ACTION_NEW;
		} else if (iwc.isParameterSet(PARAMETER_SAVE)) {
			action = ACTION_SAVE;
		} else if (getIntParameter(iwc, PARAMETER_DELETE_ID) > 0) {
			action = ACTION_DELETE;
		} else if (iwc.isParameterSet(PARAMETER_COPY_ID)) {
			action = ACTION_COPY;
		} else if (iwc.isParameterSet(PARAMETER_VAT_REGULATION_ID)) {
			action = ACTION_OPEN;
		}

		return action;
	}

	/*
	 * Handles the default action for this block.
	 */	
	private void handleDefaultAction(IWContext iwc) {
		ApplicationForm app = new ApplicationForm(this);
		app.setLocalizedTitle(KEY_TITLE, "Momssats");
		app.setSearchPanel(getSearchPanel(iwc));
		app.setMainPanel(getSearchList(iwc, false));
		app.setButtonPanel(getButtonPanel());
		add(app);
	}

	/*
	 * Handles the search action for this block.
	 */	
	private void handleSearchAction(IWContext iwc) {		
		ApplicationForm app = new ApplicationForm(this);
		app.setLocalizedTitle(KEY_TITLE_SEARCH, "Momssats - sškresultat");
		app.setSearchPanel(getSearchPanel(iwc));
		app.setMainPanel(getSearchList(iwc, true));
		app.setButtonPanel(getButtonPanel());
		add(app);
	}

	/*
	 * Handles the new action for this block.
	 */	
	private void handleNewAction(IWContext iwc) {
		add(getVATRegulationForm(
				iwc,
				getParameter(iwc, PARAMETER_VAT_REGULATION_ID),
				getParameter(iwc, PARAMETER_PERIOD_FROM),
				getParameter(iwc, PARAMETER_PERIOD_TO),
				getParameter(iwc, PARAMETER_DESCRIPTION),
				getParameter(iwc, PARAMETER_VAT_PERCENT),
				getIntParameter(iwc, PARAMETER_PAYMENT_FLOW_TYPE_ID),
				getIntParameter(iwc, PARAMETER_PROVIDER_TYPE_ID),
				null,
				true)
		);
	}

	/*
	 * Handles the open action (link clicked in the list) for this block.
	 */	
	private void handleOpenAction(IWContext iwc, boolean isCopy) {
		try {
			int id = getIntParameter(iwc, PARAMETER_VAT_REGULATION_ID);
			boolean isNew = false;
			if (isCopy) {
				id = getIntParameter(iwc, PARAMETER_COPY_ID);
				isNew = true;
			}
			VATBusiness vb = getVATBusiness(iwc);
			VATRegulation vr = vb.getVATRegulation(id);
			id = isCopy ? -1 : id;
			add(getVATRegulationForm(
					iwc,
					"" + id,
					formatDate(vr.getPeriodFrom(), 4),
					formatDate(vr.getPeriodTo(), 4),
					vr.getDescription(),
					"" + vr.getVATPercent(),
					vr.getPaymentFlowTypeId(),
					vr.getProviderTypeId(),
					null,
					isNew)
			);
		} catch (RemoteException e) {
			add(new ExceptionWrapper(e));
		} catch (VATException e) {
			add(localize(e.getTextKey(), e.getDefaultText()));
		}
	}

	/*
	 * Handles the save action for this block.
	 */
	private void handleSaveAction(IWContext iwc) {
		String errorMessage = null;

		try {
			VATBusiness vb = getVATBusiness(iwc);
			vb.saveVATRegulation(
					getIntParameter(iwc, PARAMETER_VAT_REGULATION_ID),
					parseDate(iwc.getParameter(PARAMETER_PERIOD_FROM)),
					parseDate(iwc.getParameter(PARAMETER_PERIOD_TO)),
					getParameter(iwc, PARAMETER_PERIOD_FROM),
					getParameter(iwc, PARAMETER_PERIOD_TO),
					getParameter(iwc, PARAMETER_DESCRIPTION),
					getParameter(iwc, PARAMETER_VAT_PERCENT),
					getParameter(iwc, PARAMETER_PAYMENT_FLOW_TYPE_ID),
					getParameter(iwc, PARAMETER_PROVIDER_TYPE_ID),
					getParameter(iwc, PARAMETER_OPERATIONAL_FIELD));
		} catch (RemoteException e) {
			add(new ExceptionWrapper(e));
			return;
		} catch (VATException e) {
			errorMessage = localize(e.getTextKey(), e.getDefaultText());
		}

		String operationalField = getParameter(iwc, PARAMETER_OPERATIONAL_FIELD);
		if (operationalField.length() > 0) {
			try {
				getSession().setOperationalField(operationalField);
			} catch (RemoteException e) {}
		}
		
		if (errorMessage != null) {
			add(getVATRegulationForm(
					iwc,
					getParameter(iwc, PARAMETER_VAT_REGULATION_ID),
					getParameter(iwc, PARAMETER_PERIOD_FROM),
					getParameter(iwc, PARAMETER_PERIOD_TO),
					getParameter(iwc, PARAMETER_DESCRIPTION),
					getParameter(iwc, PARAMETER_VAT_PERCENT),
					getIntParameter(iwc, PARAMETER_PAYMENT_FLOW_TYPE_ID),
					getIntParameter(iwc, PARAMETER_PROVIDER_TYPE_ID),
					errorMessage,
					!iwc.isParameterSet(PARAMETER_EDIT))
			);
		} else {
			handleDefaultAction(iwc);
		}
		
	}

	/*
	 * Handles the delete action for this block.
	 */	
	private void handleDeleteAction(IWContext iwc) {
		String errorMessage = null;
		try {
			VATBusiness vb = getVATBusiness(iwc);
			vb.deleteVATRegulation(getIntParameter(iwc, PARAMETER_DELETE_ID));
		} catch (RemoteException e) {
			add(new ExceptionWrapper(e));
		} catch (VATException e) {
			errorMessage = localize(e.getTextKey(), e.getDefaultText());
		}

		if (errorMessage != null) {
			ApplicationForm app = new ApplicationForm(this);
			app.setLocalizedTitle(KEY_TITLE_DELETE_CONFIRM, "Ta bort momssats");
			Table table = new Table();
			table.setCellpadding(getCellpadding());
			table.setCellspacing(getCellspacing());
			table.add(getErrorText(errorMessage), 1, 1);
			app.setMainPanel(table);
			ButtonPanel bp = new ButtonPanel(this);
			bp.addLocalizedButton(PARAMETER_CANCEL, KEY_CANCEL, "Avbryt");
			app.setButtonPanel(bp);
			add(app);		
		} else {
			handleDefaultAction(iwc);
		}
	}
	 
	/*
	 * Returns the search panel for this block.
	 */
	private Table getSearchPanel(IWContext iwc) {
		Table table = new Table();
		table.add(getLocalizedLabel(KEY_OPERATIONAL_FIELD, "Huvudverksamhet"), 1, 1);
		table.add(new OperationalFieldsMenu(), 2, 1);
		table.add(getLocalizedLabel(KEY_PERIOD, "Period"), 1, 2);
		table.add(getTextInput(PARAMETER_SEARCH_PERIOD_FROM, getParameter(iwc, PARAMETER_SEARCH_PERIOD_FROM), 60), 2, 2);
		table.add(getText(" - "), 2, 2);
		table.add(getTextInput(PARAMETER_SEARCH_PERIOD_TO,  getParameter(iwc, PARAMETER_SEARCH_PERIOD_TO), 60), 2, 2);
		table.add(getLocalizedButton(PARAMETER_SEARCH, KEY_SEARCH, "Sšk"), 5, 2);
		return table;
	}	
	
	/*
	 * Returns a list of VAT regulations.
	 */
	private Table getSearchList(IWContext iwc, boolean search) {
		String errorMessage = null;
		Collection vatRegulations = null;

		try {
			VATBusiness vb = getVATBusiness(iwc);
			String periodFromString = iwc.getParameter(PARAMETER_SEARCH_PERIOD_FROM);
			String periodToString = iwc.getParameter(PARAMETER_SEARCH_PERIOD_TO);
			Date periodFrom = parseDate(periodFromString);
			Date periodTo = parseDate(periodToString);
			String operationalField = getSession().getOperationalField();
			operationalField = operationalField == null ? "" : operationalField;
			try {
				if (search) {
					vatRegulations = vb.findVATRegulations(periodFrom, periodTo, periodFromString, periodToString, operationalField);
				} else {
					vatRegulations = vb.findAllVATRegulations(operationalField);
				}
			} catch (VATException e) {
				errorMessage = localize(e.getTextKey(), e.getDefaultText());
			}
		} catch (RemoteException e) {
			Table t = new Table();
			t.add(new ExceptionWrapper(e), 1, 1);
			return t;
		}

		ListTable list = new ListTable(this, 8);
		list.setLocalizedHeader(KEY_PERIOD, "Period", 1);
		list.setLocalizedHeader(KEY_DESCRIPTION, "BenŠmning", 2);
		list.setLocalizedHeader(KEY_VAT_PERCENT, "Procentsats", 3);
		list.setLocalizedHeader(KEY_PAYMENT_FLOW_TYPE, "Stršm", 4);
		list.setLocalizedHeader(KEY_PROVIDER_TYPE, "Anordnartyp", 5);
		list.setLocalizedHeader(KEY_EDIT, "Redigera", 6);
		list.setLocalizedHeader(KEY_COPY, "Kopiera", 7);
		list.setLocalizedHeader(KEY_DELETE, "Ta bort", 8);

		list.setColumnWidth(2, "27%");
		list.setColumnWidth(6, "60");
		list.setColumnWidth(7, "60");
		
		if (vatRegulations != null) {
			Iterator iter = vatRegulations.iterator();
			while (iter.hasNext()) {
				VATRegulation vr = (VATRegulation) iter.next();
				list.add(formatDate(vr.getPeriodFrom(), 4) + " - " + formatDate(vr.getPeriodTo(), 4));
				list.add(getLink(vr.getDescription(), PARAMETER_VAT_REGULATION_ID, vr.getPrimaryKey().toString()));
				list.add("" + vr.getVATPercent());
				String localizationKey = vr.getPaymentFlowType().getLocalizationKey();
				list.add(localizationKey, localizationKey);
				localizationKey = vr.getProviderType().getLocalizationKey();
				list.add(localizationKey, localizationKey);

				Link edit = new Link(getEditIcon(localize(KEY_BUTTON_EDIT, "Redigera denna momssats")));
				edit.addParameter(PARAMETER_VAT_REGULATION_ID, vr.getPrimaryKey().toString());
				list.add(edit);

				Link copy = new Link(getCopyIcon(localize(KEY_BUTTON_COPY, "Kopiera denna Œldersregel")));
				copy.addParameter(PARAMETER_COPY_ID, vr.getPrimaryKey().toString());
				list.add(copy);

				SubmitButton delete = new SubmitButton(getDeleteIcon(localize(KEY_DELETE, "Radera")));
				delete.setDescription(localize(KEY_BUTTON_DELETE, "Klicka här för att ta bort denna momssats"));
				delete.setValueOnClick(PARAMETER_DELETE_ID, vr.getPrimaryKey().toString());
				delete.setSubmitConfirm(localize(KEY_DELETE_CONFIRM, "Vill du verkligen ta bort denna momssats?"));
				list.add(delete);
			}
		}

		Table mainPanel = new Table();
		mainPanel.setCellpadding(0);
		mainPanel.setCellspacing(0);
		mainPanel.add(new HiddenInput(PARAMETER_DELETE_ID, "-1"), 1, 1);
	
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
	 * Returns the application form for creating or editing a VAT regulation.
	 */
	private ApplicationForm getVATRegulationForm(
			IWContext iwc,
			String id,
			String periodFrom,
			String periodTo,
			String description,
			String vatPercent,
			int paymentFlowTypeId,
			int providerTypeId, 
			String errorMessage,
			boolean isNew) {
		ApplicationForm app = new ApplicationForm(this);
		if (isNew) {
			app.setLocalizedTitle(KEY_TITLE_ADD, "Skapa ny momssats");
		} else {
			app.setLocalizedTitle(KEY_TITLE_EDIT, "Redigera momssats");
		}
		
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		int row = 1;
		table.add(getLocalizedLabel(KEY_OPERATIONAL_FIELD, "Huvudverksamhet"), 1, row);
		String operationalField = null;
		try {
			operationalField = getSession().getOperationalField();
		} catch (RemoteException e) {}
		table.add(getOperationalFieldDropdownMenu(PARAMETER_OPERATIONAL_FIELD, operationalField), 2, row++); 
		table.add(getLocalizedLabel(KEY_PERIOD, "Period"), 1, row);
		table.add(getTextInput(PARAMETER_PERIOD_FROM, periodFrom, 60), 2, row);
		table.add(getText(" - "), 2, row);
		table.add(getTextInput(PARAMETER_PERIOD_TO, periodTo, 60), 2, row++);
		table.add(getLocalizedLabel(KEY_DESCRIPTION, "BenŠmning"), 1, row);
		table.add(getTextInput(PARAMETER_DESCRIPTION, description, 120), 2, row++);
		table.add(getLocalizedLabel(KEY_VAT_PERCENT, "Procentsats"), 1, row);
		table.add(getTextInput(PARAMETER_VAT_PERCENT, vatPercent, 30), 2, row);
		table.add(getText(" %"), 2, row++);
		table.add(getLocalizedLabel(KEY_PAYMENT_FLOW_TYPE, "Stršm"), 1, row);
		table.add(getPaymentFlowTypeDropdownMenu(iwc, PARAMETER_PAYMENT_FLOW_TYPE_ID, paymentFlowTypeId), 2, row++);
		table.add(getLocalizedLabel(KEY_PROVIDER_TYPE, "Anordnartyp"), 1, row);
		table.add(getProviderTypeDropdownMenu(iwc, PARAMETER_PROVIDER_TYPE_ID, providerTypeId), 2, row++); 

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
		app.addHiddenInput(PARAMETER_VAT_REGULATION_ID, id);
		if (!isNew) {
			app.addHiddenInput(PARAMETER_EDIT, "true");
		}
		app.setMainPanel(mainPanel);	
		
		ButtonPanel bp = new ButtonPanel(this);
		bp.addLocalizedButton(PARAMETER_SAVE, KEY_SAVE, "Spara");
		bp.addLocalizedButton(PARAMETER_CANCEL, KEY_CANCEL, "Avbryt");
		app.setButtonPanel(bp);
		
		return app;		
	}
	
	/*
	 * Returns a DropdownMenu for payment direction types. 
	 */
	private DropdownMenu getPaymentFlowTypeDropdownMenu(IWContext iwc, String name, int selectedIndex) {
		DropdownMenu menu = (DropdownMenu) getStyledInterface(new DropdownMenu(name));
		menu.addMenuElement(0, localize(KEY_PAYMENT_FLOW_TYPE_HEADER, "VŠlj stršm"));
		try {
			Collection c = getRegulationsBusiness(iwc).findAllPaymentFlowTypes();
			if (c != null) {
				Iterator iter = c.iterator();
				while (iter.hasNext()) {
					PaymentFlowType pft = (PaymentFlowType) iter.next();
					menu.addMenuElement("" + (((Integer) pft.getPrimaryKey()).intValue()), 
							localize(pft.getLocalizationKey(), pft.getLocalizationKey()));
				}
				if (selectedIndex != -1) {
					menu.setSelectedElement(selectedIndex);
				}
			}
		} catch (RemoteException e) {}
		
		return menu;	
	}
	
	/*
	 * Returns a DropdownMenu for provider types. 
	 */
	private DropdownMenu getProviderTypeDropdownMenu(IWContext iwc, String name, int selectedIndex) {
		DropdownMenu menu = (DropdownMenu) getStyledInterface(new DropdownMenu(name));
		menu.addMenuElement(0, localize(KEY_PROVIDER_TYPE_HEADER, "VŠlj anordnartyp"));
		try {
			Collection c = getRegulationsBusiness(iwc).findAllProviderTypes();
			if (c != null) {
				Iterator iter = c.iterator();
				while (iter.hasNext()) {
					ProviderType pt = (ProviderType) iter.next();
					menu.addMenuElement("" + (((Integer) pt.getPrimaryKey()).intValue()), 
							localize(pt.getLocalizationKey(), pt.getLocalizationKey()));
				}
				if (selectedIndex != -1) {
					menu.setSelectedElement(selectedIndex);
				}
			}
		} catch (RemoteException e) {}

		return menu;	
	}

	/*
	 * Returns a DropdownMenu for operational fields. 
	 */
	private DropdownMenu getOperationalFieldDropdownMenu(String parameter, String operationalField) {
		DropdownMenu menu = (DropdownMenu) getStyledInterface(new DropdownMenu(parameter));
		menu.addMenuElement("", localize(KEY_OPERATIONAL_FIELD_SELECTOR_HEADER, "Choose operational field"));
		try {
			Collection c = getBusiness().getExportBusiness().getAllOperationalFields();
			if (c != null) {
				Iterator iter = c.iterator();
				while (iter.hasNext()) {
					SchoolCategory sc = (SchoolCategory) iter.next();
					String id = sc.getPrimaryKey().toString();
					menu.addMenuElement(id, localize(sc.getLocalizedKey(), sc.getLocalizedKey()));
				}
				if (operationalField != null) {
					menu.setSelectedElement(operationalField);
				}
			}		
		} catch (Exception e) {
			add(new ExceptionWrapper(e));
		}
		return menu;	
	}
	
	/*
	 * Returns a regulations business object
	 */
	private RegulationsBusiness getRegulationsBusiness(IWContext iwc) throws RemoteException {
		return (RegulationsBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, RegulationsBusiness.class);
	}

	/*
	 * Returns a VAT business object
	 */
	private VATBusiness getVATBusiness(IWContext iwc) throws RemoteException {
		return (VATBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, VATBusiness.class);
	}
}
