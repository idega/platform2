/*
 * $Id: AgeEditor.java,v 1.5 2003/09/02 13:58:05 anders Exp $
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
import com.idega.presentation.text.Link;

import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.ApplicationForm;
import se.idega.idegaweb.commune.accounting.presentation.ListTable;
import se.idega.idegaweb.commune.accounting.presentation.ButtonPanel;
import se.idega.idegaweb.commune.accounting.regulations.data.AgeRegulation;
import se.idega.idegaweb.commune.accounting.regulations.business.AgeBusiness;
import se.idega.idegaweb.commune.accounting.regulations.business.AgeException;

/** 
 * AgeEditor is an idegaWeb block that handles age values and
 * age regulations for children in childcare.
 * <p>
 * Last modified: $Date: 2003/09/02 13:58:05 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.5 $
 */
public class AgeEditor extends AccountingBlock {

	private final static int ACTION_DEFAULT = 0;
	private final static int ACTION_CANCEL = 1;
	private final static int ACTION_SEARCH = 2;
	private final static int ACTION_NEW = 3;
	private final static int ACTION_OPEN = 4;
	private final static int ACTION_SAVE = 5;
	private final static int ACTION_DELETE_CONFIRM = 6;
	private final static int ACTION_DELETE = 7;
	
	private final static String PP = "cacc_age_"; // Parameter prefix 

	private final static String PARAMETER_SEARCH_PERIOD_FROM = PP + "period_search_from";
	private final static String PARAMETER_SEARCH_PERIOD_TO = PP + "period_search_to";
	private final static String PARAMETER_PERIOD_FROM = PP + "period_from";
	private final static String PARAMETER_PERIOD_TO = PP + "period_to";
	private final static String PARAMETER_AGE_FROM = PP + "age_from";
	private final static String PARAMETER_AGE_TO = PP + "age_to";
	private final static String PARAMETER_DESCRIPTION = PP + "description";
	private final static String PARAMETER_CUT_DATE = PP + "cut_date";
	private final static String PARAMETER_AGE_REGULATION_ID = PP + "age_regulation_id";
	private final static String PARAMETER_SEARCH = PP + "search";
	private final static String PARAMETER_NEW = PP + "new";
	private final static String PARAMETER_SAVE = PP + "save";
	private final static String PARAMETER_CANCEL = PP + "cancel";
	private final static String PARAMETER_DELETE_CONFIRM = PP + "delete_confirm";
	private final static String PARAMETER_DELETE = PP + "delete";
	private final static String PARAMETER_EDIT = PP + "edit";
	
	private final static String KP = "age_editor."; // key prefix 
	
	private final static String KEY_TITLE = KP + "title";
	private final static String KEY_TITLE_SEARCH = KP + "title_search";
	private final static String KEY_TITLE_ADD = KP + "title_add";
	private final static String KEY_TITLE_EDIT = KP + "title_edit";
	private final static String KEY_TITLE_DELETE_CONFIRM = KP + "title_delete_confirm";
	private final static String KEY_SCHOOL = KP + "school";
	private final static String KEY_PERIOD = KP + "period";
	private final static String KEY_AGE_FROM = KP + "age_from";
	private final static String KEY_AGE_TO = KP + "age_to";
	private final static String KEY_DESCRIPTION = KP+ "description";
	private final static String KEY_CUT_DATE = KP + "cut_date";
	private final static String KEY_MAIN_ACTIVITY = KP + "main_activity";
	private final static String KEY_SEARCH = KP + "search";
	private final static String KEY_NEW = KP + "new";
	private final static String KEY_SAVE = KP + "save";
	private final static String KEY_CANCEL = KP + "cancel";
	private final static String KEY_DELETE = KP + "delete";
	private final static String KEY_DELETE_YES = KP + "delete_yes";
	private final static String KEY_DELETE_CONFIRM_MESSAGE = KP + "delete_confirm_message";
	private final static String KEY_BUTTON_EDIT = KP + "button_edit";
	private final static String KEY_BUTTON_DELETE = KP + "button_delete";	

	/**
	 * @see com.idega.presentation.Block#main()
	 */
	public void main(final IWContext iwc) {
		setResourceBundle(getResourceBundle(iwc));

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
					handleOpenAction(iwc);
					break;
				case ACTION_SAVE:
					handleSaveAction(iwc);
					break;
				case ACTION_DELETE_CONFIRM:
					handleDeleteConfirmAction(iwc);
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
		} else if (iwc.isParameterSet(PARAMETER_DELETE_CONFIRM)) {
			action = ACTION_DELETE_CONFIRM;
		} else if (iwc.isParameterSet(PARAMETER_DELETE)) {
			action = ACTION_DELETE;
		} else if (iwc.isParameterSet(PARAMETER_AGE_REGULATION_ID)) {
			action = ACTION_OPEN;
		}

		return action;
	}

	/*
	 * Handles the default action for this block.
	 */	
	private void handleDefaultAction(IWContext iwc) {
		ApplicationForm app = new ApplicationForm(this);
		app.setLocalizedTitle(KEY_TITLE, "Regelverk beräkna ålder");
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
		app.setLocalizedTitle(KEY_TITLE_SEARCH, "Regelverk beräkna ålder - sökresultat");
		app.setSearchPanel(getSearchPanel(iwc));
		app.setMainPanel(getSearchList(iwc, true));
		app.setButtonPanel(getButtonPanel());
		add(app);
	}

	/*
	 * Handles the new action for this block.
	 */	
	private void handleNewAction(IWContext iwc) {
		add(getAgeRegulationForm(
				iwc,
				getParameter(iwc, PARAMETER_AGE_REGULATION_ID),
				getParameter(iwc, PARAMETER_PERIOD_FROM),
				getParameter(iwc, PARAMETER_PERIOD_TO),
				getParameter(iwc, PARAMETER_AGE_FROM),
				getParameter(iwc, PARAMETER_AGE_TO),
				getParameter(iwc, PARAMETER_DESCRIPTION),
				getParameter(iwc, PARAMETER_CUT_DATE),
				null,
				true)
		);
	}

	/*
	 * Handles the open action (link clicked in the list) for this block.
	 */	
	private void handleOpenAction(IWContext iwc) {
		try {
			AgeBusiness ab = getAgeBusiness(iwc);
			AgeRegulation ar = ab.getAgeRegulation(getIntParameter(iwc, PARAMETER_AGE_REGULATION_ID));
			add(getAgeRegulationForm(
					iwc,
					ar.getPrimaryKey().toString(),
					formatDate(ar.getPeriodFrom(), 4),
					formatDate(ar.getPeriodTo(), 4),
					"" + ar.getAgeFrom(),
					"" + ar.getAgeTo(),
					ar.getDescription(),
					formatDate(ar.getCutDate(), 4),
					null,
					false)
			);
		} catch (RemoteException e) {
			add(new ExceptionWrapper(e));
		} catch (AgeException e) {
			add(localize(e.getTextKey(), e.getDefaultText()));
		}
	}

	/*
	 * Handles the save action for this block.
	 */
	private void handleSaveAction(IWContext iwc) {
		String errorMessage = null;

		try {
			AgeBusiness ab = getAgeBusiness(iwc);
			ab.saveAgeRegulation(
					getIntParameter(iwc, PARAMETER_AGE_REGULATION_ID),
					parseDate(iwc.getParameter(PARAMETER_PERIOD_FROM)),
					parseDate(iwc.getParameter(PARAMETER_PERIOD_TO)),
					iwc.getParameter(PARAMETER_PERIOD_FROM),
					iwc.getParameter(PARAMETER_PERIOD_TO),
					iwc.getParameter(PARAMETER_AGE_FROM),
					iwc.getParameter(PARAMETER_AGE_TO),
					iwc.getParameter(PARAMETER_DESCRIPTION),
					parseDate(iwc.getParameter(PARAMETER_CUT_DATE)),
					iwc.getParameter(PARAMETER_CUT_DATE));
		} catch (RemoteException e) {
			add(new ExceptionWrapper(e));
			return;
		} catch (AgeException e) {
			errorMessage = localize(e.getTextKey(), e.getDefaultText());
		}
		
		if (errorMessage != null) {
			add(getAgeRegulationForm(
					iwc,
					getParameter(iwc, PARAMETER_AGE_REGULATION_ID),
					getParameter(iwc, PARAMETER_PERIOD_FROM),
					getParameter(iwc, PARAMETER_PERIOD_TO),
					getParameter(iwc, PARAMETER_AGE_FROM),
					getParameter(iwc, PARAMETER_AGE_TO),
					getParameter(iwc, PARAMETER_DESCRIPTION),
					getParameter(iwc, PARAMETER_CUT_DATE),
					errorMessage,
					!iwc.isParameterSet(PARAMETER_EDIT))
			);
		} else {
			handleDefaultAction(iwc);
		}
		
	}

	/*
	 * Handles the delete confirm action for this block.
	 */	
	private void handleDeleteConfirmAction(IWContext iwc) {		
		ApplicationForm app = new ApplicationForm(this);
		app.setLocalizedTitle(KEY_TITLE_DELETE_CONFIRM, "Ta bort åldersregel");
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		try {
			AgeBusiness ab = getAgeBusiness(iwc);
			AgeRegulation ar = ab.getAgeRegulation(getIntParameter(iwc, PARAMETER_AGE_REGULATION_ID));
			table.add(getLocalizedLabel(KEY_PERIOD, "Period"), 1, 1);
			table.add(getText(formatDate(ar.getPeriodFrom(), 4) + " - " + formatDate(ar.getPeriodTo(), 4)), 2, 1);
			table.add(getLocalizedLabel(KEY_AGE_FROM, "Ålder från"), 1, 2);
			table.add(getText("" + ar.getAgeFrom()), 2, 2);
			table.add(getLocalizedLabel(KEY_AGE_TO, "Ålder till"), 1, 3);
			table.add(getText("" + ar.getAgeTo()), 2, 3);
			table.add(getLocalizedLabel(KEY_DESCRIPTION, "Benämning"), 1, 4);
			table.add(getText(ar.getDescription()), 2, 4);
			table.add(getLocalizedLabel(KEY_CUT_DATE, "Brytdatum"), 1, 5);
			table.add(getText(formatDate(ar.getCutDate(), 4)), 2, 5);
			table.setColumnWidth(1, "90");
			table.setColumnWidth(2, "160");
			Table t = new Table();
			t.setCellpadding(0);
			t.setCellspacing(0);
			t.add(table, 1, 1);
			Table t2 = new Table();
			t2.setCellpadding(getCellpadding());
			t2.setCellspacing(getCellspacing());
			t2.add(getErrorText(localize(KEY_DELETE_CONFIRM_MESSAGE, "Vill du verkligen ta bort denna åldersregel?")), 1, 2);
			t.add(t2, 1, 2);
			app.setMainPanel(t);
		} catch (Exception e) {
			add(new ExceptionWrapper(e));
			return;
		}
		ButtonPanel bp = new ButtonPanel(this);
		bp.addLocalizedButton(PARAMETER_DELETE, KEY_DELETE_YES, "Ja");
		bp.addLocalizedButton(PARAMETER_CANCEL, KEY_CANCEL, "Avbryt");
		app.setButtonPanel(bp);
		
		app.addHiddenInput(PARAMETER_AGE_REGULATION_ID, getParameter(iwc, PARAMETER_AGE_REGULATION_ID));
		add(app);
	}

	/*
	 * Handles the delete action for this block.
	 */	
	private void handleDeleteAction(IWContext iwc) {
		String errorMessage = null;
		try {
			AgeBusiness ab = getAgeBusiness(iwc);
			ab.deleteAgeRegulation(getIntParameter(iwc, PARAMETER_AGE_REGULATION_ID));
		} catch (RemoteException e) {
			add(new ExceptionWrapper(e));
		} catch (AgeException e) {
			errorMessage = localize(e.getTextKey(), e.getDefaultText());
		}

		if (errorMessage != null) {
			ApplicationForm app = new ApplicationForm(this);
			app.setLocalizedTitle(KEY_TITLE_DELETE_CONFIRM, "Ta bort åldersregel");
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
		table.add(getLocalizedLabel(KEY_MAIN_ACTIVITY, "Huvudverksamhet"), 1, 1);
		table.add(getLocalizedText(KEY_SCHOOL, "Skola"), 2, 1);
		table.add(getLocalizedLabel(KEY_PERIOD, "Period"), 1, 2);
		table.add(getTextInput(PARAMETER_SEARCH_PERIOD_FROM, getParameter(iwc, PARAMETER_SEARCH_PERIOD_FROM), 60), 2, 2);
		table.add(getText(" - "), 2, 2);
		table.add(getTextInput(PARAMETER_SEARCH_PERIOD_TO,  getParameter(iwc, PARAMETER_SEARCH_PERIOD_TO), 60), 2, 2);
		table.add(getLocalizedButton(PARAMETER_SEARCH, KEY_SEARCH, "Sök"), 5, 2);
		return table;
	}	
	
	/*
	 * Returns a list of age regulations.
	 */
	private Table getSearchList(IWContext iwc, boolean search) {
		String errorMessage = null;
		Collection ageRegulations = null;

		try {
			AgeBusiness ab = getAgeBusiness(iwc);
			String periodFromString = iwc.getParameter(PARAMETER_SEARCH_PERIOD_FROM);
			String periodToString = iwc.getParameter(PARAMETER_SEARCH_PERIOD_TO);
			Date periodFrom = parseDate(periodFromString);
			Date periodTo = parseDate(periodToString);
			try {
				if (search == true) {
					ageRegulations = ab.findAgeRegulations(periodFrom, periodTo, periodFromString, periodToString);
				} else {
					ageRegulations = ab.findAllAgeRegulations();
				}
			} catch (AgeException e) {
				errorMessage = localize(e.getTextKey(), e.getDefaultText());
			}
		} catch (RemoteException e) {
			Table t = new Table();
			t.add(new ExceptionWrapper(e));
			return t;
		}

		ListTable list = new ListTable(this, 7);
		list.setLocalizedHeader(KEY_PERIOD, "Period", 1);
		list.setLocalizedHeader(KEY_AGE_FROM, "Ålder från", 2);
		list.setLocalizedHeader(KEY_AGE_TO, "Ålder till", 3);
		list.setLocalizedHeader(KEY_DESCRIPTION, "Regel", 4);
		list.setLocalizedHeader(KEY_CUT_DATE, "Brytdatum", 5);

		if (ageRegulations != null) {
			Iterator iter = ageRegulations.iterator();
			while (iter.hasNext()) {
				AgeRegulation ar = (AgeRegulation) iter.next();
				list.add(formatDate(ar.getPeriodFrom(), 4) + " - " + formatDate(ar.getPeriodTo(), 4));
				list.add(ar.getAgeFrom());
				list.add(ar.getAgeTo());
				list.add(ar.getDescription());
//				list.add(getLink(ar.getDescription(), PARAMETER_AGE_REGULATION_ID, ar.getPrimaryKey().toString()));
				list.add(formatDate(ar.getCutDate(), 4));

				Link edit = new Link(getEditIcon(localize(KEY_BUTTON_EDIT, "Redigera")));
				edit.addParameter(PARAMETER_AGE_REGULATION_ID, ar.getPrimaryKey().toString());
				list.add(edit);

				Link delete = new Link(getDeleteIcon(localize(KEY_BUTTON_DELETE, "Ta bort")));
				delete.addParameter(PARAMETER_DELETE_CONFIRM, "true");
				delete.addParameter(PARAMETER_AGE_REGULATION_ID, ar.getPrimaryKey().toString());
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
	 * Returns the application form for creating or editing an age regulation.
	 */
	private ApplicationForm getAgeRegulationForm(
			IWContext iwc,
			String id,
			String periodFrom,
			String periodTo,
			String ageFrom,
			String ageTo,
			String description,
			String cutDate,
			String errorMessage,
			boolean isNew) {
		ApplicationForm app = new ApplicationForm(this);
		if (isNew) {
			app.setLocalizedTitle(KEY_TITLE_ADD, "Skapa ny åldersregel");
		} else {
			app.setLocalizedTitle(KEY_TITLE_EDIT, "Redigera åldersregel");
		}
		
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.add(getLocalizedLabel(KEY_PERIOD, "Period"));
		table.add(getTextInput(PARAMETER_PERIOD_FROM, periodFrom, 60), 2, 1);
		table.add(getText(" - "), 2, 1);
		table.add(getTextInput(PARAMETER_PERIOD_TO, periodTo, 60), 2, 1);
		table.add(getLocalizedLabel(KEY_AGE_FROM, "Ålder från"), 1, 2);
		table.add(getTextInput(PARAMETER_AGE_FROM, ageFrom, 30), 2, 2);
		table.add(getLocalizedLabel(KEY_AGE_TO, "Ålder till"), 1, 3);
		table.add(getTextInput(PARAMETER_AGE_TO, ageTo, 30), 2, 3);
		table.add(getLocalizedLabel(KEY_DESCRIPTION, "Regel"), 1, 4);
		table.add(getTextInput(PARAMETER_DESCRIPTION, description, 200), 2, 4);
		table.add(getLocalizedLabel(KEY_CUT_DATE, "Brytdatum"), 1, 5);
		table.add(getTextInput(PARAMETER_CUT_DATE, cutDate, 60), 2, 5);

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
		app.addHiddenInput(PARAMETER_AGE_REGULATION_ID, id);
		if (!isNew) {
			app.addHiddenInput(PARAMETER_EDIT, "true");
		}
		app.setMainPanel(mainPanel);	
		
		ButtonPanel bp = new ButtonPanel(this);
		bp.addLocalizedButton(PARAMETER_SAVE, KEY_SAVE, "Spara");
//		if (!isNew) {
//			bp.addLocalizedButton(PARAMETER_DELETE_CONFIRM, KEY_DELETE, "Ta bort");
//		}
		bp.addLocalizedButton(PARAMETER_CANCEL, KEY_CANCEL, "Avbryt");
		app.setButtonPanel(bp);
		
		return app;		
	}

	/*
	 * Returns an age business object
	 */
	private AgeBusiness getAgeBusiness(IWContext iwc) throws RemoteException {
		return (AgeBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, AgeBusiness.class);
	}	
}
