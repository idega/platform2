/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.export.ifs.presentation;

import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.accounting.invoice.business.CheckAmountBusiness;
import se.idega.idegaweb.commune.accounting.invoice.data.CheckAmountBroadcast;
import se.idega.idegaweb.commune.accounting.invoice.data.CheckAmountBroadcastHome;
import se.idega.idegaweb.commune.accounting.invoice.data.CheckAmountReceivingSchool;
import se.idega.idegaweb.commune.accounting.invoice.data.CheckAmountReceivingSchoolHome;
import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.ApplicationForm;
import se.idega.idegaweb.commune.accounting.presentation.ButtonPanel;
import se.idega.idegaweb.commune.accounting.presentation.OperationalFieldsMenu;

/**
 * @author palli
 */
public class SendIFSFiles extends AccountingBlock {
	private final static String PREFIX = "cacc_send_files_";
	protected final static String KEY_HEADER = PREFIX + "header";

	protected final static String KEY_ENDED = PREFIX + "ended";
	protected final static String KEY_HEADER_OPERATION = PREFIX + "operation";
	protected final static String KEY_LAST_BROADCAST_FOR = PREFIX + "last_broadcast_for";
	protected final static String KEY_HANDLED_PROVIDER_COUNT = PREFIX + "handled_provider_count";
	protected final static String KEY_OF = PREFIX + "of";
	protected final static String KEY_SEND = PREFIX + "save";
	protected final static String KEY_STARTED = PREFIX + "started";
	protected final static String KEY_NO_BROADCAST_EXCUTED = PREFIX + "no_broadcast_excuted";
	protected final static String KEY_UPDATE_INFORMATION_BELOW = PREFIX + "update_information_below";

	protected final static String PARAM_SEND_FILE = "ifs_file_send";
	protected final static String PARAM_UPDATE = "ifs_update";

	protected final static int ACTION_VIEW = 0;
	protected final static int ACTION_SEND = 1;

	protected String _currentOperation = null;


	public void init(IWContext iwc) throws Exception {
		try {
			int action = parseAction(iwc);
			viewForm();
			add ("<p/>"); 
			switch (action) {
				case ACTION_SEND :
					sendFiles (iwc);
					break;
			}
			if (null != _currentOperation && 0 < _currentOperation.length ()) {
				showStatistics ();
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

	private void showStatistics () {
		try {
			final CheckAmountBroadcast broadcastInfo = getCheckAmountBroadcastHome ()
					.findLatestBySchoolCategoryId	(_currentOperation);
			final CheckAmountReceivingSchoolHome receivingSchoolHome
					= getCheckAmountReceivingSchoolHome ();
			final Collection emailedProviders = receivingSchoolHome
					.findEmailedProvidersByCheckAmountBroadcast (broadcastInfo);
			final Collection paperMailedProviders = receivingSchoolHome
					.findPaperMailedProvidersByCheckAmountBroadcast (broadcastInfo);
			final Collection ignoredProviders = receivingSchoolHome
					.findIgnoredProvidersByCheckAmountBroadcast (broadcastInfo);
			final int handledSchoolsCount = emailedProviders.size ()
					+ paperMailedProviders.size () + ignoredProviders.size ();

			final Table table = createTable (2);
			int row = 1;
			table.mergeCells (1, row, table.getColumns (), row);
			table.add (getSmallHeader (localize (KEY_LAST_BROADCAST_FOR, KEY_LAST_BROADCAST_FOR) + ' ' + getSchoolCategoryName (_currentOperation)), 1, row++);
			table.add (getSmallHeader (localize (KEY_STARTED, KEY_STARTED) + ':'), 1, row);
			table.add (getSmallText (broadcastInfo.getStartTime () + ""), 2, row++);
			final java.sql.Timestamp endTime = broadcastInfo.getEndTime ();
			table.add (getSmallHeader (localize (KEY_ENDED, KEY_ENDED) + ':'), 1, row);
			table.add (getSmallText (null != endTime ? endTime + "" : ""), 2, row++);
			table.add (getSmallHeader (localize (KEY_HANDLED_PROVIDER_COUNT, KEY_HANDLED_PROVIDER_COUNT) + ':'), 1, row);
			table.add (getSmallText (handledSchoolsCount + " " + localize (KEY_OF, KEY_OF) + " " +  broadcastInfo.getSchoolCount ()), 2, row++);
			add (table);
		} catch (FinderException e) {
			add (localize (KEY_NO_BROADCAST_EXCUTED, KEY_NO_BROADCAST_EXCUTED));
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}

	private Table createTable (final int columnCount) {
		final Table table = new Table();
		table.setCellpadding (getCellpadding ());
		table.setCellspacing (getCellspacing ());
		table.setWidth (Table.HUNDRED_PERCENT);
		table.setColumns (columnCount);
		return table;
	}
	
	private void sendFiles(IWContext context) throws RemoteException {
		try {
			getCheckAmountBusiness(context).sendCheckAmountLists
					(context.getCurrentUser(), _currentOperation);
		} catch (CreateException e) {
			e.printStackTrace ();
		} catch (FinderException e) {
			e.printStackTrace ();
		}
	}

	private void viewForm() {
		ApplicationForm form = new ApplicationForm(this);

		form.setLocalizedTitle(KEY_HEADER, KEY_HEADER);
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

		table.add(getLocalizedLabel(KEY_HEADER_OPERATION, KEY_HEADER_OPERATION), 1, 1);
		table.add(new OperationalFieldsMenu(), 2, 1);

		return table;
	}
	
	private ButtonPanel getButtonPanel() {
		ButtonPanel buttonPanel = new ButtonPanel(this);
		if (null != _currentOperation && 0 < _currentOperation.length ()) {
			buttonPanel.addLocalizedButton(PARAM_SEND_FILE, "true", KEY_SEND, KEY_SEND);
			buttonPanel.addLocalizedButton(PARAM_UPDATE, "true", KEY_UPDATE_INFORMATION_BELOW, KEY_UPDATE_INFORMATION_BELOW);
		}
		return buttonPanel;
	}
		
	private String getSchoolCategoryName (final String schoolCategoryId) {
		try {
			final SchoolCategoryHome categoryHome = getSchoolCategoryHome ();
			final SchoolCategory category
					= categoryHome.findByPrimaryKey (schoolCategoryId);
			return localize (category.getLocalizedKey (), category.getName ());
		} catch (Exception dummy) {
			return "";
		}
	}
	
	private static CheckAmountBroadcastHome getCheckAmountBroadcastHome ()
		throws IDOLookupException {
		return (CheckAmountBroadcastHome) IDOLookup.getHome
				(CheckAmountBroadcast.class);
	}
	
	private static CheckAmountReceivingSchoolHome
		getCheckAmountReceivingSchoolHome () throws IDOLookupException {
		return (CheckAmountReceivingSchoolHome) IDOLookup.getHome
				(CheckAmountReceivingSchool.class);
	}
	
	private static SchoolCategoryHome
		getSchoolCategoryHome () throws IDOLookupException {
		return (SchoolCategoryHome) IDOLookup.getHome	(SchoolCategory.class);
	}
	
	private static CheckAmountBusiness getCheckAmountBusiness
		(final IWContext context) throws RemoteException {
		return (CheckAmountBusiness) IBOLookup.getServiceInstance
				(context, CheckAmountBusiness.class);	
	}
}
