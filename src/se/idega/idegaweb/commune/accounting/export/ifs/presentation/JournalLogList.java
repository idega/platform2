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

import se.idega.idegaweb.commune.accounting.export.ifs.business.IFSBusiness;
import se.idega.idegaweb.commune.accounting.export.ifs.data.JournalLog;
import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.ApplicationForm;
import se.idega.idegaweb.commune.accounting.presentation.ListTable;
import se.idega.idegaweb.commune.accounting.presentation.OperationalFieldsMenu;

import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.user.data.User;
import com.idega.util.text.Name;

/**
 * @author palli
 */
public class JournalLogList extends AccountingBlock {
	protected final static String KEY_HEADER = "cacc_journal_log_list_header";

	protected final static String KEY_HEADER_OPERATION = "cacc_journal_log_list_operation";
	protected final static String KEY_EVENT = "cacc_journal_log_event";
	protected final static String KEY_EVENT_DATE = "cacc_journal_log_event_date";
	protected final static String KEY_USER = "cacc_journal_log_user";

	protected String _currentOperation = null;

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.presentation.AccountingBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		ApplicationForm form = new ApplicationForm(this);

		try {
			_currentOperation = getSession().getOperationalField();
			if (_currentOperation == null)
				_currentOperation = "";
		}
		catch (RemoteException e) {
		}

		form.setLocalizedTitle(KEY_HEADER, "Journal log");
		form.setSearchPanel(getTopPanel());
		form.setMainPanel(getJournalLogTable(iwc));
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

	/*
	 * Returns the Journal Log
	 */
	private ListTable getJournalLogTable(IWContext iwc) {
		IFSBusiness biz;
		ListTable list = new ListTable(this, 3);

		list.setLocalizedHeader(KEY_EVENT, "Event", 1);
		list.setLocalizedHeader(KEY_EVENT_DATE, "Date/time", 2);
		list.setLocalizedHeader(KEY_USER, "User", 3);

		try {
			biz = getIFSBusiness(iwc);

			Collection items = biz.getJournalLogBySchoolCategory(_currentOperation);
			if (items != null && !items.isEmpty()) {
				Iterator iter = items.iterator();
				while (iter.hasNext()) {
					JournalLog log = (JournalLog) iter.next();
					list.add(localize(log.getLocalizedEventKey(),log.getLocalizedEventKey()));
					list.add(log.getEventDate().toString());
					
					User user = log.getUser();
					Name name = new Name(user.getFirstName(), user.getMiddleName(), user.getLastName());
					list.add(name.getName(iwc.getApplicationSettings().getDefaultLocale()));
				}
			}
		}
		catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}

		return list;
	}

	private IFSBusiness getIFSBusiness(IWContext iwc) throws RemoteException {
		return (IFSBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, IFSBusiness.class);
	}
}