/*
 * $Id: VATEditor.java,v 1.5 2003/08/21 17:09:35 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.regulations.presentation;

import java.util.Collection;
import java.sql.Date;
import java.rmi.RemoteException;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ExceptionWrapper;

import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.ApplicationForm;
import se.idega.idegaweb.commune.accounting.presentation.ListTable;
import se.idega.idegaweb.commune.accounting.presentation.ButtonPanel;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness;

/** 
 * VATRegulations is an idegaWeb block that handles VAT values and
 * VAT regulations for providers.
 * <p>
 * Last modified: $Date: 2003/08/21 17:09:35 $ by $Author: anders $
 *
 * @author <a href="http://www.ncmedia.com">Anders Lindman</a>
 * @version $Revision: 1.5 $
 */
public class VATEditor extends AccountingBlock {

	private final static int ACTION_DEFAULT = 0;
	private final static int ACTION_SEARCH = 1;
	private final static int ACTION_NEW = 2;
	
	private final static String PARAMETER_PREFIX = "cacc_vat_"; 

	private final static String PARAMETER_PERIOD_FROM = PARAMETER_PREFIX + "period_from";
	private final static String PARAMETER_PERIOD_TO = PARAMETER_PREFIX + "period_to";
	private final static String PARAMETER_SEARCH = PARAMETER_PREFIX + "search";
	private final static String PARAMETER_NEW = PARAMETER_PREFIX + "new";
	
	private final static String KEY_PREFIX = "vat_editor."; 
	
	private final static String KEY_TITLE = KEY_PREFIX + "title";
	private final static String KEY_PERIOD = KEY_PREFIX + "period";
	private final static String KEY_DESCRIPTION = KEY_PREFIX + "description";
	private final static String KEY_VAT_PERCENT = KEY_PREFIX + "vat_percent";
	private final static String KEY_DIRECTION = KEY_PREFIX + "direction";
	private final static String KEY_PROVIDER_TYPE = KEY_PREFIX + "provider_type";
	private final static String KEY_MAIN_ACTIVITY = KEY_PREFIX + "main_activity";
	private final static String KEY_SCHOOL = KEY_PREFIX + "school";
	private final static String KEY_SEARCH = KEY_PREFIX + "search";
	private final static String KEY_NEW = KEY_PREFIX + "new";
	private final static String KEY_ERROR_DATE_FORMAT = KEY_PREFIX + "error_date_format";

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
					viewAddVATForm(iwc);
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
		} else if (iwc.isParameterSet(PARAMETER_SEARCH)) {
			action = ACTION_NEW;
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
		app.setMainPanel(getVATList(null));
		app.setButtonPanel(getButtonPanel());
		add(app);
	}

	/*
	 * Views the search result.
	 */	
	private void viewSearchResult(IWContext iwc) {
		String errorMessage = null;
		String periodFrom = iwc.getParameter(PARAMETER_PERIOD_FROM);
		Date from = parseDate(periodFrom);
		if (from == null) {
			errorMessage = localize(KEY_ERROR_DATE_FORMAT,
					"Datum måste anges på formen ÅÅMM, ÅÅMMDD eller ÅÅÅÅMMDD.");
		} else {
			RegulationsBusiness rb = null;
			try {
				rb = getRegulationsBusiness(iwc);
				Collection c = rb.findVATRegulations(from, from);
			} catch (RemoteException e) {
				add(new ExceptionWrapper(e, this));
				return;
			}
		}
		
		ApplicationForm app = new ApplicationForm();
		app.setTitle(KEY_TITLE, "Momssats");
		app.setSearchPanel(getSearchPanel());
		app.setMainPanel(getVATList(errorMessage));
		app.setButtonPanel(getButtonPanel());
		add(app);
	}

	/*
	 * Views the search result.
	 */	
	private void viewAddVATForm(IWContext iwc) {
		ApplicationForm app = new ApplicationForm();
		app.setTitle(KEY_TITLE, "Momssats");
		
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		app.setMainPanel(table);
		
		ButtonPanel bp = new ButtonPanel();
		app.setButtonPanel(getButtonPanel());
		add(app);
	}
	
	/*
	 * Returns the VATList
	 */
	private Table getVATList(String errorMessage) {		
		ListTable list = new ListTable(5);
		list.setHeader(KEY_PERIOD, "Period", 1);
		list.setHeader(KEY_DESCRIPTION, "Benämning", 2);
		list.setHeader(KEY_VAT_PERCENT, "Procentsats", 3);
		list.setHeader(KEY_DIRECTION, "Ström", 4);
		list.setHeader(KEY_PROVIDER_TYPE, "Anordnartyp", 5);
		
		list.add("0301-");
		list.add("Grundmoms");
		list.add("6");
		list.add("Ut");
		list.add("Privat");
		list.add("0301-");
		list.add("Grundmoms");
		list.add("6");
		list.add("Ut");
		list.add("Privat");
		list.add("0301-");
		list.add("Grundmoms");
		list.add("6");
		list.add("Ut");
		list.add("Privat");

		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
	
		if (errorMessage != null) {
			Table t = new Table();
			t.setCellpadding(getCellpadding());
			t.setCellspacing(getCellspacing());
			t.add(getErrorText(errorMessage), 1, 1);
			table.add(t, 1, 1);
			table.add(list, 1, 2);	
		} else {
			table.add(list, 1, 1);
		}
		return table;
	}
	
	/*
	 * Returns the button panel for this block.
	 */
	private ButtonPanel getButtonPanel() {
		ButtonPanel bp = new ButtonPanel();
		bp.addButton(PARAMETER_NEW, KEY_NEW, "Ny");
		return bp;
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
		table.add(getFormTextInput(PARAMETER_PERIOD_TO, "", 80), 3, 2);
		table.add(getFormButton(PARAMETER_SEARCH, KEY_SEARCH, "Sök"), 5, 2);
		return table;
	}	

	/*
	 * Returns a regulations business object
	 */
	private RegulationsBusiness getRegulationsBusiness(IWContext iwc) throws RemoteException {
		return (RegulationsBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, RegulationsBusiness.class);
	}
}
