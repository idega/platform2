/*
 * $Id: VATEditor.java,v 1.6 2003/08/23 21:02:38 anders Exp $
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

import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.ApplicationForm;
import se.idega.idegaweb.commune.accounting.presentation.ListTable;
import se.idega.idegaweb.commune.accounting.presentation.ButtonPanel;
import se.idega.idegaweb.commune.accounting.regulations.data.PaymentFlowType;
import se.idega.idegaweb.commune.accounting.regulations.data.ProviderType;
import se.idega.idegaweb.commune.accounting.regulations.data.VATRegulation;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness;
import se.idega.idegaweb.commune.accounting.regulations.business.VATBusiness;
import se.idega.idegaweb.commune.accounting.regulations.business.VATException;

/** 
 * VATRegulations is an idegaWeb block that handles VAT values and
 * VAT regulations for providers.
 * <p>
 * Last modified: $Date: 2003/08/23 21:02:38 $ by $Author: anders $
 *
 * @author <a href="http://www.ncmedia.com">Anders Lindman</a>
 * @version $Revision: 1.6 $
 */
public class VATEditor extends AccountingBlock {

	private final static int ACTION_DEFAULT = 0;
	private final static int ACTION_SEARCH = 1;
	private final static int ACTION_NEW = 2;
	private final static int ACTION_SAVE = 3;
	
	private final static String PP = "cacc_vat_"; // Parameter prefix 

	private final static String PARAMETER_PERIOD_FROM = PP + "period_from";
	private final static String PARAMETER_PERIOD_TO = PP + "period_to";
	private final static String PARAMETER_DESCRIPTION = PP + "description";
	private final static String PARAMETER_VAT_PERCENT = PP + "vat_percent";
	private final static String PARAMETER_PAYMENT_FLOW_TYPE_ID = PP + "payment_flow_type_id";
	private final static String PARAMETER_PROVIDER_TYPE_ID = PP + "provider_type_id";
	private final static String PARAMETER_SEARCH = PP + "search";
	private final static String PARAMETER_NEW = PP + "new";
	private final static String PARAMETER_SAVE = PP + "save";
	private final static String PARAMETER_CANCEL = PP + "cancel";
	
	private final static String KP = "vat_editor."; // key prefix 
	
	private final static String KEY_TITLE = KP + "title";
	private final static String KEY_TITLE_ADD = KP + "title_add";
	private final static String KEY_SCHOOL = KP + "school";
	private final static String KEY_PERIOD = KP + "period";
	private final static String KEY_DESCRIPTION = KP+ "description";
	private final static String KEY_VAT_PERCENT = KP + "vat_percent";
	private final static String KEY_PAYMENT_FLOW_TYPE = KP + "payment_flow_type";
	private final static String KEY_PROVIDER_TYPE = KP + "provider_type";
	private final static String KEY_MAIN_ACTIVITY = KP + "main_activity";
	private final static String KEY_PAYMENT_FLOW_TYPE_HEADER = KP + "payment_flow_type_header";
	private final static String KEY_PROVIDER_TYPE_HEADER = KP + "provider_type_header";
	private final static String KEY_SEARCH = KP + "search";
	private final static String KEY_NEW = KP + "new";
	private final static String KEY_SAVE = KP + "save";
	private final static String KEY_CANCEL = KP + "cancel";

	/**
	 * @see com.idega.presentation.Block#main()
	 */
	public void main(final IWContext iwc) {
		setResourceBundle(getResourceBundle(iwc));

		try {
			int action = parseAction(iwc);
			switch (action) {
				case ACTION_DEFAULT :
					viewDefaultForm(iwc);
					break;
				case ACTION_SEARCH :
					viewSearchResult(iwc);
					break;
				case ACTION_NEW :
					viewAddForm(iwc);
					break;
				case ACTION_SAVE :
					save(iwc);
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
		} else if (iwc.isParameterSet(PARAMETER_NEW)) {
			action = ACTION_NEW;
		} else if (iwc.isParameterSet(PARAMETER_SAVE)) {
			action = ACTION_SAVE;
		}

		return action;
	}

	/*
	 * Views the default application form.
	 */	
	private void viewDefaultForm(IWContext iwc) {
		ApplicationForm app = new ApplicationForm();
		app.setTitle(KEY_TITLE, "Momssats");
		app.setSearchPanel(getSearchPanel());
		app.setMainPanel(getSearchList(iwc, false));
		app.setButtonPanel(getButtonPanel());
		add(app);
	}

	/*
	 * Views the search result.
	 */	
	private void viewSearchResult(IWContext iwc) {		
		ApplicationForm app = new ApplicationForm();
		app.setTitle(KEY_TITLE, "Momssats");
		app.setSearchPanel(getSearchPanel());
		app.setMainPanel(getSearchList(iwc, true));
		app.setButtonPanel(getButtonPanel());
		add(app);
	}

	/*
	 * Views the search result.
	 */	
	private void viewAddForm(IWContext iwc) {
		add(getAddForm(iwc, null));
	}

	/*
	 * Save a new VAT regulation.
	 */
	private void save(IWContext iwc) {
		String errorMessage = null;

		try {
			VATBusiness vb = getVATBusiness(iwc);
			vb.createVATRegulation(
					parseDate(iwc.getParameter(PARAMETER_PERIOD_FROM)),
					parseDate(iwc.getParameter(PARAMETER_PERIOD_TO)),
					iwc.getParameter(PARAMETER_PERIOD_FROM),
					iwc.getParameter(PARAMETER_PERIOD_TO),
					iwc.getParameter(PARAMETER_DESCRIPTION),
					iwc.getParameter(PARAMETER_VAT_PERCENT),
					iwc.getParameter(PARAMETER_PAYMENT_FLOW_TYPE_ID),
					iwc.getParameter(PARAMETER_PROVIDER_TYPE_ID));		
		} catch (RemoteException e) {
			add(new ExceptionWrapper(e));
			return;
		} catch (VATException e) {
			errorMessage = localize(e.getTextKey(), e.getDefaultText());
		}
		
		if (errorMessage != null) {
			add(getAddForm(iwc, errorMessage));
		} else {
			viewDefaultForm(iwc);
		}
		
	}
	 
	/*
	 * Returns the search panel for this block.
	 */
	private Table getSearchPanel() {
		Table table = new Table();
		table.add(getFormLabel(KEY_MAIN_ACTIVITY, "Huvudverksamhet"), 1, 1);
		table.add(getFormText(KEY_SCHOOL, "Skola"), 2, 1);
		table.add(getFormLabel(KEY_PERIOD, "Period"), 1, 2);
		table.add(getFormTextInput(PARAMETER_PERIOD_FROM, "", 60), 2, 2);
		table.add(getSmallText(" - "), 2, 2);
		table.add(getFormTextInput(PARAMETER_PERIOD_TO, "", 60), 2, 2);
		table.add(getFormButton(PARAMETER_SEARCH, KEY_SEARCH, "Sök"), 5, 2);
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
			String periodFromString = iwc.getParameter(PARAMETER_PERIOD_FROM);
			String periodToString = iwc.getParameter(PARAMETER_PERIOD_TO);
			Date periodFrom = parseDate(periodFromString);
			Date periodTo = parseDate(periodToString);
			try {
				if (search == true) {
					vatRegulations = vb.findVATRegulations(periodFrom, periodTo, periodFromString, periodToString);
				} else {
					vatRegulations = vb.findAllVATRegulations();
				}
			} catch (VATException e) {
				errorMessage = localize(e.getTextKey(), e.getDefaultText());
			}
		} catch (RemoteException e) {
			Table t = new Table();
			t.add(new ExceptionWrapper(e));
			return t;
		}

		ListTable list = new ListTable(5);
		list.setHeader(KEY_PERIOD, "Period", 1);
		list.setHeader(KEY_DESCRIPTION, "Benämning", 2);
		list.setHeader(KEY_VAT_PERCENT, "Procentsats", 3);
		list.setHeader(KEY_PAYMENT_FLOW_TYPE, "Ström", 4);
		list.setHeader(KEY_PROVIDER_TYPE, "Anordnartyp", 5);

		if (vatRegulations != null) {
			Iterator iter = vatRegulations.iterator();
			while (iter.hasNext()) {
				VATRegulation vr = (VATRegulation) iter.next();
				list.add(formatDate(vr.getPeriodFrom(), 4) + " - " + formatDate(vr.getPeriodTo(), 4));
				list.add(vr.getDescription());
				list.add(vr.getVATPercent());
				String textKey = vr.getPaymentFlowType().getTextKey();
				list.add(textKey, textKey);
				textKey = vr.getProviderType().getTextKey();
				list.add(textKey, textKey);
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
		ButtonPanel bp = new ButtonPanel();
		bp.addButton(PARAMETER_NEW, KEY_NEW, "Ny");
		return bp;
	}
	
	/*
	 * Returns the application form for creating a new VAT regulation.
	 */
	private ApplicationForm getAddForm(IWContext iwc, String errorMessage) {
		ApplicationForm app = new ApplicationForm();
		app.setTitle(KEY_TITLE_ADD, "Skapa ny momssats");
		
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.add(getFormLabel(KEY_PERIOD, "Period"));
		table.add(getFormTextInput(PARAMETER_PERIOD_FROM, getParameter(iwc, PARAMETER_PERIOD_FROM), 60), 2, 1);
		table.add(getSmallText(" - "), 2, 1);
		table.add(getFormTextInput(PARAMETER_PERIOD_TO, getParameter(iwc, PARAMETER_PERIOD_TO), 60), 2, 1);
		table.add(getFormLabel(KEY_DESCRIPTION, "Benämning"), 1, 2);
		table.add(getFormTextInput(PARAMETER_DESCRIPTION, getParameter(iwc, PARAMETER_DESCRIPTION), 120), 2, 2);
		table.add(getFormLabel(KEY_VAT_PERCENT, "Procentsats"), 1, 3);
		table.add(getFormTextInput(PARAMETER_VAT_PERCENT, getParameter(iwc, PARAMETER_VAT_PERCENT), 40), 2, 3);
		table.add(getFormLabel(KEY_PAYMENT_FLOW_TYPE, "Ström"), 1, 4);
		table.add(getPaymentFlowTypeDropdownMenu(iwc, PARAMETER_PAYMENT_FLOW_TYPE_ID,
				getIntParameter(iwc, PARAMETER_PAYMENT_FLOW_TYPE_ID)), 2, 4);
		table.add(getFormLabel(KEY_PROVIDER_TYPE, "Anordnartyp"), 1, 5);
		table.add(getProviderTypeDropdownMenu(iwc, PARAMETER_PROVIDER_TYPE_ID, 
				getIntParameter(iwc, PARAMETER_PROVIDER_TYPE_ID)), 2, 5); 

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
		
		app.setMainPanel(mainPanel);	
		
		ButtonPanel bp = new ButtonPanel();
		bp.addButton(PARAMETER_SAVE, KEY_SAVE, "Spara");
		bp.addButton(PARAMETER_CANCEL, KEY_CANCEL, "Avbryt");
		app.setButtonPanel(bp);
		
		return app;		
	}
	
	/*
	 * Returns a DropdownMenu for payment direction types. 
	 */
	private DropdownMenu getPaymentFlowTypeDropdownMenu(IWContext iwc, String name, int selectedIndex) {
		DropdownMenu menu = (DropdownMenu) getStyledInterface(new DropdownMenu(name));
		menu.addMenuElement(0, localize(KEY_PAYMENT_FLOW_TYPE_HEADER, "Välj ström"));
		try {
			Collection c = getRegulationsBusiness(iwc).findAllPaymentFlowTypes();
			if (c != null) {
				Iterator iter = c.iterator();
				while (iter.hasNext()) {
					PaymentFlowType pft = (PaymentFlowType) iter.next();
					menu.addMenuElement("" + (((Integer) pft.getPrimaryKey()).intValue()), 
							localize(pft.getTextKey(), pft.getTextKey()));
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
		menu.addMenuElement(0, localize(KEY_PROVIDER_TYPE_HEADER, "Välj anordnartyp"));
		try {
			Collection c = getRegulationsBusiness(iwc).findAllProviderTypes();
			if (c != null) {
				Iterator iter = c.iterator();
				while (iter.hasNext()) {
					ProviderType pt = (ProviderType) iter.next();
					menu.addMenuElement("" + (((Integer) pt.getPrimaryKey()).intValue()), 
							localize(pt.getTextKey(), pt.getTextKey()));
				}
				if (selectedIndex != -1) {
					menu.setSelectedElement(selectedIndex);
				}
			}
		} catch (RemoteException e) {}

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
