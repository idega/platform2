/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.export.ifs.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

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
public class SendIFSFiles extends AccountingBlock {
	protected final static String KEY_HEADER = "cacc_send_files_header";

	protected final static String KEY_HEADER_OPERATION = "cacc_send_files_operation";
	protected final static String KEY_SEND = "cacc_send_files_save";

	protected final static String PARAM_SEND_FILE = "ifs_file_send";

	protected final static int ACTION_VIEW = 0;
	protected final static int ACTION_SEND = 1;
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
					viewForm();
					break;
				case ACTION_SEND :
					viewForm();
					add ("<p>"); 
					sendFiles (iwc);					
					break;
			}
		}	catch (Exception e) {
			add(new ExceptionWrapper(e, this));
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

		if (iwc.isParameterSet(PARAM_SEND_FILE)) {
			return ACTION_SEND;
		}
		return ACTION_VIEW;
	}

	private void sendFiles(IWContext iwc) throws RemoteException {
		final Map filesMaps = getIFSBusiness(iwc).sendFiles(_currentOperation,iwc.getCurrentUser());
		final Table table = new Table();
		table.setCellpadding (getCellpadding ());
		table.setCellspacing (getCellspacing ());
		table.setWidth (Table.HUNDRED_PERCENT);
		final int columnCount = filesMaps.size () + 1;
		table.setColumns (columnCount);
		final int percentageInt = 100 / (columnCount - 1);
		final String percentageString = percentageInt + "%";
		for (int i = 2; i <= columnCount; i++) {
			table.setColumnWidth (i, percentageString);
		}
		add (table);
		int col = 1;
		int numberedRowsCount = 0;
		table.add (getSmallHeader ("#"), 1, 1);
		for (Iterator i = filesMaps.keySet ().iterator (); i.hasNext ();) {
			col++;
			int row = 1;
			final String mapKey = "" + i.next ();
			final Collection files = (Collection) filesMaps.get (mapKey);
			table.setRowColor(row, getHeaderColor ());
			table.add (getSmallHeader (localize (mapKey, mapKey)), col, row);
			for (Iterator j = files.iterator (); j.hasNext ();) {
				row++;
				table.setRowColor (row, (row % 2 == 0) ? getZebraColor1 ()
													 : getZebraColor2 ());
				final String providerName = "" + j.next ();
				table.add (getSmallText (providerName), col, row);
				if (row > numberedRowsCount) {
					numberedRowsCount = row;
					table.setAlignment (1, numberedRowsCount,
															Table.HORIZONTAL_ALIGN_RIGHT);
					table.add (getSmallText ((numberedRowsCount - 1) + ""), 1,
										 numberedRowsCount);
				}
			}
		}
	}

	private void viewForm() {
		ApplicationForm form = new ApplicationForm(this);

		form.setLocalizedTitle(KEY_HEADER, "Send files");
		form.setSearchPanel(getTopPanel());
		form.setButtonPanel(getButtonPanel());
		add(form);
	}

	private Table getTopPanel() {
		Table table = new Table();
		table.setColumnAlignment(1, Table.HORIZONTAL_ALIGN_LEFT);
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_LEFT);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());

		table.add(getLocalizedLabel(KEY_HEADER_OPERATION, "School category"), 1, 1);
		table.add(new OperationalFieldsMenu(), 2, 1);

		return table;
	}
	
	private ButtonPanel getButtonPanel() {
		ButtonPanel buttonPanel = new ButtonPanel(this);
		buttonPanel.addLocalizedButton(PARAM_SEND_FILE, "true", KEY_SEND, "Save");
		return buttonPanel;
	}
		
	private IFSBusiness getIFSBusiness(IWContext iwc) throws RemoteException {
		return (IFSBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, IFSBusiness.class);
	}	
}
