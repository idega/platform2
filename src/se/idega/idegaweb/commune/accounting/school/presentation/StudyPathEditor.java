/*
 * $Id: StudyPathEditor.java,v 1.1 2003/09/08 15:50:35 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.school.presentation;

import java.util.Collection;
import java.util.Iterator;
import java.rmi.RemoteException;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.text.Link;

import com.idega.block.school.data.SchoolStudyPath;

import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.ApplicationForm;
import se.idega.idegaweb.commune.accounting.presentation.ListTable;
import se.idega.idegaweb.commune.accounting.presentation.ButtonPanel;
import se.idega.idegaweb.commune.accounting.school.business.StudyPathBusiness;
import se.idega.idegaweb.commune.accounting.school.business.StudyPathException;

/** 
 * AgeEditor is an idegaWeb block that handles age values and
 * age regulations for children in childcare.
 * <p>
 * Last modified: $Date: 2003/09/08 15:50:35 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.1 $
 */
public class StudyPathEditor extends AccountingBlock {

	private final static int ACTION_DEFAULT = 0;
	private final static int ACTION_CANCEL = 1;
	private final static int ACTION_NEW = 3;
	private final static int ACTION_OPEN = 4;
	private final static int ACTION_SAVE = 5;
	private final static int ACTION_DELETE = 6;
	
	private final static String PP = "cacc_study_path_"; // Parameter prefix 

	private final static String PARAMETER_STUDY_PATH_CODE = PP + "study_path_code";
	private final static String PARAMETER_DESCRIPTION = PP + "description";
	private final static String PARAMETER_DELETE_ID = PP + "delete_id";
	private final static String PARAMETER_SEARCH = PP + "search";
	private final static String PARAMETER_NEW = PP + "new";
	private final static String PARAMETER_SAVE = PP + "save";
	private final static String PARAMETER_CANCEL = PP + "cancel";
	private final static String PARAMETER_EDIT = PP + "edit";
	
	private final static String KP = "study_path_editor."; // key prefix 
	
	private final static String KEY_TITLE = KP + "title";
	private final static String KEY_TITLE_ADD = KP + "title_add";
	private final static String KEY_TITLE_EDIT = KP + "title_edit";
	private final static String KEY_TITLE_DELETE_CONFIRM = KP + "title_delete_confirm";
	private final static String KEY_UPPER_SECONDARY_SCHOOL = KP + "upper_secondary_school";
	private final static String KEY_STUDY_PATH_CODE = KP + "study_path_code";
	private final static String KEY_DESCRIPTION = KP+ "description";
	private final static String KEY_MAIN_ACTIVITY = KP + "main_activity";
	private final static String KEY_NEW = KP + "new";
	private final static String KEY_SAVE = KP + "save";
	private final static String KEY_CANCEL = KP + "cancel";
	private final static String KEY_EDIT = KP + "edit";
	private final static String KEY_DELETE = KP + "delete";
	private final static String KEY_DELETE_CONFIRM = KP + "delete_confirm_message";
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
				case ACTION_NEW:
					handleNewAction(iwc);
					break;
				case ACTION_OPEN:
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
		} else if (iwc.isParameterSet(PARAMETER_DELETE_ID)) {
			action = ACTION_DELETE;
		} else if (iwc.isParameterSet(PARAMETER_STUDY_PATH_CODE)) {
			action = ACTION_OPEN;
		}

		return action;
	}

	/*
	 * Handles the default action for this block.
	 */	
	private void handleDefaultAction(IWContext iwc) {
		ApplicationForm app = new ApplicationForm(this);
		app.setLocalizedTitle(KEY_TITLE, "Studievägskoder");
		app.setSearchPanel(getSearchPanel(iwc));
		app.setMainPanel(getStudyPathList(iwc));
		app.setButtonPanel(getButtonPanel());
		add(app);
	}

	/*
	 * Handles the new action for this block.
	 */	
	private void handleNewAction(IWContext iwc) {
		add(getStudyPathForm(
				getParameter(iwc, PARAMETER_STUDY_PATH_CODE),
				getParameter(iwc, PARAMETER_DESCRIPTION),
				null,
				true)
		);
	}

	/*
	 * Handles the open action (link clicked in the list) for this block.
	 */	
	private void handleOpenAction(IWContext iwc) {
		try {
			StudyPathBusiness spb = getStudyPathBusiness(iwc);
			SchoolStudyPath sp = spb.getStudyPath(getParameter(iwc, PARAMETER_STUDY_PATH_CODE));
			add(getStudyPathForm(
					sp.getPrimaryKey().toString(),
					sp.getDescription(),
					null,
					false)
			);
		} catch (RemoteException e) {
			add(new ExceptionWrapper(e));
		} catch (StudyPathException e) {
			add(localize(e.getTextKey(), e.getDefaultText()));
		}
	}

	/*
	 * Handles the save action for this block.
	 */
	private void handleSaveAction(IWContext iwc) {
		String errorMessage = null;

		try {
			StudyPathBusiness spb = getStudyPathBusiness(iwc);
			spb.saveStudyPath(
					iwc.getParameter(PARAMETER_STUDY_PATH_CODE),
					iwc.getParameter(PARAMETER_DESCRIPTION));
		} catch (RemoteException e) {
			add(new ExceptionWrapper(e));
			return;
		} catch (StudyPathException e) {
			errorMessage = localize(e.getTextKey(), e.getDefaultText());
		}
		
		if (errorMessage != null) {
			add(getStudyPathForm(
					getParameter(iwc, PARAMETER_STUDY_PATH_CODE),
					getParameter(iwc, PARAMETER_DESCRIPTION),
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
			StudyPathBusiness spb = getStudyPathBusiness(iwc);
			spb.deleteStudyPath(getParameter(iwc, PARAMETER_DELETE_ID));
		} catch (RemoteException e) {
			add(new ExceptionWrapper(e));
		} catch (StudyPathException e) {
			errorMessage = localize(e.getTextKey(), e.getDefaultText());
		}

		if (errorMessage != null) {
			ApplicationForm app = new ApplicationForm(this);
			app.setLocalizedTitle(KEY_TITLE_DELETE_CONFIRM, "Ta bort studieväg");
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
		table.add(getLocalizedText(KEY_UPPER_SECONDARY_SCHOOL, "Gymnasieskola"), 2, 1);
		return table;
	}	
	
	/*
	 * Returns the list of study paths.
	 */
	private Table getStudyPathList(IWContext iwc) {
		Collection studyPaths = null;

		try {
			StudyPathBusiness spb = getStudyPathBusiness(iwc);
			studyPaths = spb.findAllStudyPaths();
		} catch (RemoteException e) {
			Table t = new Table();
			t.add(new ExceptionWrapper(e));
			return t;
		}

		ListTable list = new ListTable(this, 4);
		list.setLocalizedHeader(KEY_STUDY_PATH_CODE, "Kod", 1);
		list.setLocalizedHeader(KEY_DESCRIPTION, "Beskrivning", 2);
		list.setLocalizedHeader(KEY_EDIT, "Redigera", 3);
		list.setLocalizedHeader(KEY_DELETE, "Ta bort", 4);

		if (studyPaths != null) {
			Iterator iter = studyPaths.iterator();
			while (iter.hasNext()) {
				SchoolStudyPath sp = (SchoolStudyPath) iter.next();
				list.add(sp.getCode());
				list.add(sp.getDescription());

				Link edit = new Link(getEditIcon(localize(KEY_BUTTON_EDIT, "Redigera denna studieväg")));
				edit.addParameter(PARAMETER_STUDY_PATH_CODE, sp.getPrimaryKey().toString());
				list.add(edit);

				SubmitButton delete = new SubmitButton(getDeleteIcon(localize(KEY_DELETE, "Radera")));
				delete.setDescription(localize(KEY_BUTTON_DELETE, "Klicka h‰r fˆr att ta bort denna studieväg"));
				delete.setValueOnClick(PARAMETER_DELETE_ID, sp.getPrimaryKey().toString());
				delete.setSubmitConfirm(localize(KEY_DELETE_CONFIRM, "Vill du verkligen ta bort denna studieväg?"));
				list.add(delete);
			}
		}

		Table mainPanel = new Table();
		mainPanel.setCellpadding(0);
		mainPanel.setCellspacing(0);
		mainPanel.add(new HiddenInput(PARAMETER_DELETE_ID, "-1"));
	
		mainPanel.add(list, 1, 1);
		
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
	 * Returns the application form for creating or editing a study path.
	 */
	private ApplicationForm getStudyPathForm(
			String studyPathCode,
			String description,
			String errorMessage,
			boolean isNew) {
		ApplicationForm app = new ApplicationForm(this);
		if (isNew) {
			app.setLocalizedTitle(KEY_TITLE_ADD, "Skapa ny studieväg");
		} else {
			app.setLocalizedTitle(KEY_TITLE_EDIT, "Redigera studieväg");
		}
		
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.add(getLocalizedLabel(KEY_STUDY_PATH_CODE, "Kod"), 1, 1);
		table.add(getTextInput(PARAMETER_STUDY_PATH_CODE, studyPathCode, 40), 2, 1);
		table.add(getLocalizedLabel(KEY_DESCRIPTION, "Beskrivning"), 1, 2);
		table.add(getTextInput(PARAMETER_DESCRIPTION, description, 200), 2, 2);

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
		app.addHiddenInput(PARAMETER_STUDY_PATH_CODE, studyPathCode);
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
	 * Returns a study path business object
	 */
	private StudyPathBusiness getStudyPathBusiness(IWContext iwc) throws RemoteException {
		return (StudyPathBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, StudyPathBusiness.class);
	}	
}
