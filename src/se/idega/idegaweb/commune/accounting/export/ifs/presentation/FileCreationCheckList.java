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
import se.idega.idegaweb.commune.accounting.export.ifs.data.IFSCheckHeader;
import se.idega.idegaweb.commune.accounting.export.ifs.data.IFSCheckRecord;
import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.ApplicationForm;
import se.idega.idegaweb.commune.accounting.presentation.ListTable;
import se.idega.idegaweb.commune.accounting.presentation.OperationalFieldsMenu;

import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;

/**
 * @author palli
 */
public class FileCreationCheckList extends AccountingBlock {
	protected final static String KEY_HEADER = "cacc_check_list_header";

	protected final static String KEY_HEADER_OPERATION = "cacc_check_list_operation";
	protected final static String KEY_HEADER_STATUS = "cacc_check_list_status";
	protected final static String KEY_HEADER_EVENT_DATE = "cacc_check_list_date";
	protected final static String KEY_HEADER_START_TIME = "cacc_check_list_start";
	protected final static String KEY_HEADER_END_TIME = "cacc_check_list_end";
	protected final static String KEY_HEADER_EXCEL = "cacc_check_list_excel";
	protected final static String KEY_NUMBER = "cacc_check_list_number";
	protected final static String KEY_OBJECT = "cacc_check_list_object";
	protected final static String KEY_ERROR = "cacc_check_list_error";
	protected final static String KEY_BOTTON_TOTAL = "cacc_check_list_total";
	
	protected final static String PARAM_EXCEL = "cacc_check_list_excel";

	protected String _currentOperation = null;
	
	protected int _numberOfErrors = 0;
	protected int _headerId = -1;

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

		form.setLocalizedTitle(KEY_HEADER, "File creation check list");
		form.setSearchPanel(getTopPanel(iwc));
		form.setMainPanel(getCheckTable(iwc));
		form.setButtonPanel(getBottomPanel());
		add(form);
	}

	private Table getTopPanel(IWContext iwc) {
		Table table = new Table();
		table.setColumnAlignment(1, Table.HORIZONTAL_ALIGN_LEFT);
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_LEFT);
		table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_LEFT);
		table.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_LEFT);
		table.setColumnAlignment(5, Table.HORIZONTAL_ALIGN_LEFT);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		
		IFSCheckHeader header = null;
		try {
			header = getIFSBusiness(iwc).getIFSCheckHeaderBySchoolCategory(_currentOperation);
			if (header != null)
				_headerId = ((Integer)header.getPrimaryKey()).intValue();
		}
		catch (RemoteException e) {
			e.printStackTrace();
			header = null;
			_headerId = -1;
		}

		table.add(getLocalizedLabel(KEY_HEADER_OPERATION, "School category"), 1, 1);
		table.add(new OperationalFieldsMenu(), 2, 1);
		table.add(getLocalizedButton(PARAM_EXCEL,KEY_HEADER_EXCEL,"Excel"),3,1);

		table.add(getLocalizedLabel(KEY_HEADER_STATUS, "Status"), 1, 2);
		if (header != null)
			table.add(getLocalizedText(header.getStatus(),header.getStatus()), 2, 2);

		table.add(getLocalizedLabel(KEY_HEADER_EVENT_DATE, "Event date"), 1, 3);
		if (header != null)
			table.add(header.getEventDate().toString(), 2, 3);

		table.add(getLocalizedLabel(KEY_HEADER_START_TIME, "Start time"), 1, 4);
		if (header != null)
			if (header.getEventStartTime() != null)
				table.add(header.getEventStartTime().toString(), 2, 4);

		table.add(getLocalizedLabel(KEY_HEADER_END_TIME, "End time"), 1, 5);
		if (header != null)
			if (header.getEventEndTime() != null)
				table.add(header.getEventEndTime().toString(), 2, 5);
		
		return table;
	}
	
	private Table getBottomPanel() {
		Table table = new Table();
		table.setColumnAlignment(1, Table.HORIZONTAL_ALIGN_LEFT);
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_LEFT);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());

		table.add(getLocalizedLabel(KEY_BOTTON_TOTAL, "Total number of suspected errors"), 1, 1);
		table.add(Integer.toString(_numberOfErrors), 2, 1);

		return table;
	}

	/*
	 * Returns the Journal Log
	 */
	private ListTable getCheckTable(IWContext iwc) {
		IFSBusiness biz;
		ListTable list = new ListTable(this, 3);

		list.setLocalizedHeader(KEY_NUMBER, "No.", 1);
		list.setLocalizedHeader(KEY_OBJECT, "Suspected objects", 2);
		list.setLocalizedHeader(KEY_ERROR, "Suspected error", 3);

		try {
			biz = getIFSBusiness(iwc);

			Collection items = biz.getIFSCheckRecordByHeaderId(_headerId);
			if (items != null && !items.isEmpty()) {
				Iterator iter = items.iterator();
				while (iter.hasNext()) {
					IFSCheckRecord rec = (IFSCheckRecord) iter.next();
					list.add(++_numberOfErrors);
					list.add(rec.getErrorConcerns());
					list.add(localize(rec.getError(),rec.getError()));
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