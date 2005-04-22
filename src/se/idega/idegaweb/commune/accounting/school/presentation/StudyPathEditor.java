/*
 * $Id: StudyPathEditor.java,v 1.10 2005/04/22 08:11:00 malin Exp $
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

import javax.ejb.FinderException;

import com.idega.presentation.IWContext;

import com.idega.presentation.Table;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.text.Link;

import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolStudyPathGroup;

import com.idega.block.school.data.SchoolType;

import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;

import se.idega.idegaweb.commune.accounting.presentation.ApplicationForm;
import se.idega.idegaweb.commune.accounting.presentation.ListTable;
import se.idega.idegaweb.commune.accounting.presentation.ButtonPanel;
import se.idega.idegaweb.commune.accounting.school.business.StudyPathBusiness;
import se.idega.idegaweb.commune.accounting.school.business.StudyPathException;

/** 
 * This idegaWeb block that handles study paths for schools.
 * <p>
 * Last modified: $Date: 2005/04/22 08:11:00 $ by $Author: malin $
 *
 * @author Anders Lindman
 * @version $Revision: 1.10 $
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
	private final static String PARAMETER_POINTS = PP + "points";
	private final static String PARAMETER_STUDY_PATH_GROUP = PP + "study_path_group";
	private final static String PARAMETER_OPERATION = PP + "operation";
	private final static String PARAMETER_STUDY_PATH_ID = PP + "study_path_id";
	private final static String PARAMETER_DELETE_ID = PP + "delete_id";
	private final static String PARAMETER_NEW = PP + "new";
	private final static String PARAMETER_SAVE = PP + "save";
	private final static String PARAMETER_CANCEL = PP + "cancel";
	private final static String PARAMETER_EDIT = PP + "edit";
	
	private final static String KP = "study_path_editor."; // key prefix 
	
	private final static String KEY_TITLE = KP + "title";
	private final static String KEY_TITLE_ADD = KP + "title_add";
	private final static String KEY_TITLE_EDIT = KP + "title_edit";
	private final static String KEY_TITLE_DELETE_CONFIRM = KP + "title_delete_confirm";
	private final static String KEY_STUDY_PATH_CODE = KP + "study_path_code";
	private final static String KEY_DESCRIPTION = KP + "description";
	private final static String KEY_OPERATION = KP + "operation";
	private final static String KEY_OPERATION_SELECTOR_HEADER = KP + "operation_selector_header";
	private final static String KEY_STUDY_PATH_GROUP_SELECTOR_HEADER = KP + "study_path_group_selector_header";
	private final static String KEY_STUDY_PATH_GROUP = KP + "study_path_group";
	private final static String KEY_POINTS = KP + "points";
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
		} else if (getIntParameter(iwc, PARAMETER_DELETE_ID) > 0) {
			action = ACTION_DELETE;
		} else if (iwc.isParameterSet(PARAMETER_STUDY_PATH_ID)) {
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
		add(getStudyPathForm(iwc, "-1", getParameter(iwc, PARAMETER_OPERATION), "", "", null, true, "", ""));
	}

	/*
	 * Handles the open action (link clicked in the list) for this block.
	 */	
	private void handleOpenAction(IWContext iwc) {
		try {
			StudyPathBusiness spb = getStudyPathBusiness(iwc);
			SchoolStudyPath sp = spb.getStudyPath(getParameter(iwc, PARAMETER_STUDY_PATH_ID));
			add(getStudyPathForm(
					iwc,
					sp.getPrimaryKey().toString(),
					"" + sp.getSchoolTypeId(),
					sp.getCode(),
					sp.getDescription(),
					null,
					false, new Integer(sp.getPoints()).toString(), new Integer(sp.getStudyPathGroupID()).toString())
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
					iwc.getParameter(PARAMETER_STUDY_PATH_ID),
					iwc.getParameter(PARAMETER_OPERATION),
					iwc.getParameter(PARAMETER_STUDY_PATH_CODE),
					iwc.getParameter(PARAMETER_DESCRIPTION),
					iwc.getParameter(PARAMETER_POINTS),
					iwc.getParameter(PARAMETER_STUDY_PATH_GROUP));
		} catch (RemoteException e) {
			add(new ExceptionWrapper(e));
			return;
		} catch (StudyPathException e) {
			errorMessage = localize(e.getTextKey(), e.getDefaultText());
		}
		
		if (errorMessage != null) {
			add(getStudyPathForm(
					iwc,
					getParameter(iwc, PARAMETER_STUDY_PATH_ID),
					getParameter(iwc, PARAMETER_OPERATION),
					getParameter(iwc, PARAMETER_STUDY_PATH_CODE),
					getParameter(iwc, PARAMETER_DESCRIPTION),
					errorMessage,
					!iwc.isParameterSet(PARAMETER_EDIT),
					getParameter(iwc, PARAMETER_POINTS),
					getParameter(iwc, PARAMETER_STUDY_PATH_GROUP))
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
		table.add(getLocalizedLabel(KEY_OPERATION, "Verksamhet"), 1, 1);
		DropdownMenu od = getOperationDropdownMenu(iwc, PARAMETER_OPERATION, getParameter(iwc, PARAMETER_OPERATION));
		od.setToSubmit(true);
		table.add(od, 2, 1);
		return table;
	}	
	
	/*
	 * Returns the list of study paths.
	 */
	private Table getStudyPathList(IWContext iwc) {
		Collection studyPaths = null;

		try {
			StudyPathBusiness spb = getStudyPathBusiness(iwc);
			studyPaths = spb.findStudyPathsByOperation(getIntParameter(iwc, PARAMETER_OPERATION));
		} catch (RemoteException e) {
			Table t = new Table();
			t.add(new ExceptionWrapper(e), 1, 1);
			return t;
		}

		ListTable list = new ListTable(this, 4);
		list.setLocalizedHeader(KEY_STUDY_PATH_CODE, "Kod", 1);
		list.setLocalizedHeader(KEY_DESCRIPTION, "Beskrivning", 2);
		list.setLocalizedHeader(KEY_EDIT, "Redigera", 3);
		list.setLocalizedHeader(KEY_DELETE, "Ta bort", 4);

		list.setColumnWidth(2, "66%");
		list.setColumnWidth(3, "60");
		list.setColumnWidth(4, "60");

		if (studyPaths != null) {
			Iterator iter = studyPaths.iterator();
			while (iter.hasNext()) {
				SchoolStudyPath sp = (SchoolStudyPath) iter.next();
				int spgID = sp.getStudyPathGroupID();
				SchoolStudyPathGroup spg = null;
				try {
					spg = getStudyPathBusiness(iwc).findStudyPathGroupByID(spgID);	
				}
				catch (RemoteException re){
					log (re);
				}
				
				Link l = getSmallLink(sp.getCode());
				int points = -1;
				points = sp.getPoints();
				String sPoints = "";
				if (points != -1)
					sPoints = ", " + (new Integer (points)).toString();
				String localizedKey = "";
				if (spg != null && localizedKey != null && localizedKey.equals(""))
					localizedKey = ", " + getLocalizedText(spg.getLocalizationKey(), spg.getLocalizationKey());
				l.addParameter(PARAMETER_STUDY_PATH_ID, sp.getPrimaryKey().toString());
				list.add(l);
				list.add(sp.getDescription() + sPoints + localizedKey);

				Link edit = new Link(getEditIcon(localize(KEY_BUTTON_EDIT, "Redigera denna studieväg")));
				edit.addParameter(PARAMETER_STUDY_PATH_ID, sp.getPrimaryKey().toString());
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
		mainPanel.add(new HiddenInput(PARAMETER_DELETE_ID, "-1"), 1, 1);
	
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
			IWContext iwc,
			String studyPathId,
			String operation,
			String studyPathCode,
			String description,
			String errorMessage,
			boolean isNew, String points, String studypathgroup) {
		ApplicationForm app = new ApplicationForm(this);
		if (isNew) {
			app.setLocalizedTitle(KEY_TITLE_ADD, "Skapa ny studieväg");
		} else {
			app.setLocalizedTitle(KEY_TITLE_EDIT, "Redigera studieväg");
		}
		
		if (points.equals("-1"))
			points = "";
		
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		int row = 1;
		table.add(getLocalizedLabel(KEY_OPERATION, "Verksamhet"), 1, row);
		table.add(getOperationDropdownMenu(iwc, PARAMETER_OPERATION, operation), 2, row++);
		table.add(getLocalizedLabel(KEY_DESCRIPTION, "Beskrivning"), 1, row);
		table.add(getTextInput(PARAMETER_DESCRIPTION, description, 200), 2, row++);
		table.add(getLocalizedLabel(KEY_STUDY_PATH_CODE, "Kod"), 1, row);
		table.add(getTextInput(PARAMETER_STUDY_PATH_CODE, studyPathCode, 50), 2, row++);
	
		table.add(getLocalizedLabel(KEY_POINTS, "Points"), 1, row);
		table.add(getTextInput(PARAMETER_POINTS, points, 50), 2, row++);
			
		table.add(getLocalizedLabel(KEY_STUDY_PATH_GROUP, "Study path group"), 1, row);
		table.add(getStudyPathGroupsDropdownMenu(iwc, PARAMETER_STUDY_PATH_GROUP, studypathgroup), 2, row++);
		
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
		app.addHiddenInput(PARAMETER_STUDY_PATH_ID, studyPathId);
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
	 * Returns a DropdownMenu for operational fields. 
	 */
	private DropdownMenu getOperationDropdownMenu(IWContext iwc, String parameter, String operation) {
		DropdownMenu menu = (DropdownMenu) getStyledInterface(new DropdownMenu(parameter));
		menu.addMenuElement("", localize(KEY_OPERATION_SELECTOR_HEADER, "Choose operation"));
		try {
			Collection c = getStudyPathBusiness(iwc).findAllOperations();
			if (c != null) {
				Iterator iter = c.iterator();
				while (iter.hasNext()) {
					SchoolType st = (SchoolType) iter.next();
					String id = st.getPrimaryKey().toString();
					menu.addMenuElement(id, localize(st.getLocalizationKey(), st.getLocalizationKey()));
				}
				if (operation != null) {
					menu.setSelectedElement(operation);
				}
			}		
		} catch (Exception e) {
			add(new ExceptionWrapper(e));
		}
		return menu;	
	}
	
	/*
	 * Returns a DropdownMenu for study path groups. 
	 */
	private DropdownMenu getStudyPathGroupsDropdownMenu(IWContext iwc, String parameter, String studypathgroup) {
		DropdownMenu menu = (DropdownMenu) getStyledInterface(new DropdownMenu(parameter));
		menu.addMenuElement("", localize(KEY_STUDY_PATH_GROUP_SELECTOR_HEADER, "Choose study path group"));
		try {
			Collection c = getStudyPathBusiness(iwc).findAllStudyPathGroups();
			if (c != null) {
				Iterator iter = c.iterator();
				while (iter.hasNext()) {
					SchoolStudyPathGroup stpg = (SchoolStudyPathGroup) iter.next();
					String id = stpg.getPrimaryKey().toString();
					menu.addMenuElement(id, localize(stpg.getLocalizationKey(), stpg.getLocalizationKey()));
				}
				if (studypathgroup != null) {
					menu.setSelectedElement(studypathgroup);
				}
			}		
		} catch (Exception e) {
			add(new ExceptionWrapper(e));
		}
		return menu;	
	}

	/*
	 * Returns a study path business object
	 */
	private StudyPathBusiness getStudyPathBusiness(IWContext iwc) throws RemoteException {
		return (StudyPathBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, StudyPathBusiness.class);
	}	
	
	
}
