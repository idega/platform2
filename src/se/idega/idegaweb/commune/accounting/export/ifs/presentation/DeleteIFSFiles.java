/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.export.ifs.presentation;

import java.rmi.RemoteException;

import se.idega.idegaweb.commune.accounting.export.ifs.business.IFSBusiness;
import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.ApplicationForm;
import se.idega.idegaweb.commune.accounting.presentation.ButtonPanel;
import se.idega.idegaweb.commune.accounting.presentation.OperationalFieldsMenu;

import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;

/**
 * @author palli
 */
public class DeleteIFSFiles extends AccountingBlock {
	protected final static String KEY_HEADER = "cacc_delete_files_header";

	protected final static String KEY_HEADER_OPERATION = "cacc_delete_files_operation";
	protected final static String KEY_DELETE = "cacc_delete_files_save";

	protected final static String PARAM_DELETE_FILE = "ifs_file_delete";

	protected final static int ACTION_VIEW = 0;
	protected final static int ACTION_DELETE = 1;
	protected final static int ACTION_CANCEL = 2; //and does what??

	protected String _currentOperation = null;

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.presentation.AccountingBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		try {
			int action = parseAction(iwc);
			switch (action) {
				case ACTION_VIEW :
					viewForm(iwc, false);
					break;
				case ACTION_DELETE :
					deleteFiles(iwc);
					viewForm(iwc, true);
					break;
			}
		}
		catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}
	}

	private int parseAction(IWContext iwc) {
		try {
			_currentOperation = getSession().getOperationalField();
			if (_currentOperation == null)
				_currentOperation = "";
		}
		catch (RemoteException e) {
		}
		
		if (iwc.isParameterSet(PARAM_DELETE_FILE)) {
			return ACTION_DELETE;
		}
		return ACTION_VIEW;
	}

	private void deleteFiles(IWContext iwc) {
		try {
			getIFSBusiness(iwc).deleteFiles(_currentOperation,iwc.getCurrentUser());
		}
		catch (RemoteException e1) {
			e1.printStackTrace();
			
			//Throw new Exception?
		}
	}

	private void viewForm(IWContext iwc, boolean deleted) {
		ApplicationForm form = new ApplicationForm(this);

		form.setLocalizedTitle(KEY_HEADER, "Delete files");
		form.setSearchPanel(getTopPanel(iwc, deleted));
		form.setButtonPanel(getButtonPanel());
		add(form);
	}

	private Table getTopPanel(IWContext iwc, boolean deleted) {
		Table table = new Table();
		table.setColumnAlignment(1, Table.HORIZONTAL_ALIGN_LEFT);
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_LEFT);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		
		int row = 1;
		if (deleted){
			table.add(getLocalizedString("cacc_delete_files_batch started", "Deleted", iwc), 1, row++);
		}		

		table.add(getLocalizedLabel(KEY_HEADER_OPERATION, "School category"), 1, row);
		table.add(new OperationalFieldsMenu(), 2, row);

		return table;
	}
	
	private ButtonPanel getButtonPanel() {
		ButtonPanel buttonPanel = new ButtonPanel(this);
		buttonPanel.addLocalizedButton(PARAM_DELETE_FILE, "true ", KEY_DELETE, "Save");
		return buttonPanel;
	}
		
	private IFSBusiness getIFSBusiness(IWContext iwc) throws RemoteException {
		return (IFSBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, IFSBusiness.class);
	}	
}
