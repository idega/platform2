/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.export.ifs.presentation;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import se.idega.idegaweb.commune.accounting.export.business.MoveFileException;
import se.idega.idegaweb.commune.accounting.export.ifs.business.IFSBusiness;
import se.idega.idegaweb.commune.accounting.invoice.business.CheckAmountBusiness;
import se.idega.idegaweb.commune.accounting.invoice.data.CheckAmountBroadcast;
import se.idega.idegaweb.commune.accounting.invoice.data.CheckAmountBroadcastHome;
import se.idega.idegaweb.commune.accounting.invoice.data.CheckAmountReceivingSchool;
import se.idega.idegaweb.commune.accounting.invoice.data.CheckAmountReceivingSchoolHome;
import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.ApplicationForm;
import se.idega.idegaweb.commune.accounting.presentation.ButtonPanel;
import se.idega.idegaweb.commune.accounting.presentation.OperationalFieldsMenu;

import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.user.data.User;

/**
 * @author palli
 */
public class SendIFSFiles extends AccountingBlock {
	private final static String PREFIX = "cacc_send_files_";
	protected final static String KEY_HEADER = PREFIX + "header";

	protected final static String KEY_CREATED_BY = PREFIX + "created_by";
	protected final static String KEY_EMAILED_PROVIDERS = PREFIX + "emailed_providers";
	protected final static String KEY_ENDED = PREFIX + "ended";
	protected final static String KEY_HANDLED_PROVIDER_COUNT = PREFIX + "handled_provider_count";
	protected final static String KEY_HEADER_OPERATION = PREFIX + "operation";
	protected final static String KEY_IGNORED_PROVIDERS = PREFIX + "ignored_providers";
	protected final static String KEY_LAST_BROADCAST_FOR = PREFIX + "last_broadcast_for";
	protected final static String KEY_NAME = PREFIX + "name";
	protected final static String KEY_NO_BROADCAST_EXECUTED = PREFIX + "no_broadcast_executed";
	protected final static String KEY_OF = PREFIX + "of";
	protected final static String KEY_PAPER_MAILED_PROVIDERS = PREFIX + "paper_mailed_providers";
	protected final static String KEY_SEND = PREFIX + "save";
	protected final static String KEY_STARTED = PREFIX + "started";
	protected final static String KEY_UPDATE_INFORMATION_BELOW = PREFIX + "update_information_below";
	protected final static String KEY_MOVE_FILE_COULDNT_FIND_DIRECTORY = PREFIX + "move_file_couldnt_find_directory";
	
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
					try{
						sendFiles (iwc);
					}catch(MoveFileException e){
						add(getLocalizedText(KEY_MOVE_FILE_COULDNT_FIND_DIRECTORY,KEY_MOVE_FILE_COULDNT_FIND_DIRECTORY));
					}
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
			table.setRowColor (row, getHeaderColor ());
			table.setRowAlignment (row, Table.HORIZONTAL_ALIGN_CENTER) ;
			table.add (getSmallHeader (localize (KEY_LAST_BROADCAST_FOR, KEY_LAST_BROADCAST_FOR) + ' ' + getSchoolCategoryName (_currentOperation)), 1, row++);
 			table.setColumnWidth (1, "33%");
			table.add (getSmallHeader (localize (KEY_CREATED_BY, KEY_CREATED_BY) + ':'), 1, row);
			table.add (getSmallText (getSignature (broadcastInfo.getCreatedBy ())), 2, row++);
			table.add (getSmallHeader (localize (KEY_STARTED, KEY_STARTED) + ':'), 1, row);
			table.add (getSmallText (broadcastInfo.getStartTime () + ""), 2, row++);
			final Timestamp endTime = broadcastInfo.getEndTime ();
			table.add (getSmallHeader (localize (KEY_ENDED, KEY_ENDED) + ':'), 1, row);
			table.add (getSmallText (null != endTime ? endTime + "" : ""), 2, row++);
			table.add (getSmallHeader (localize (KEY_HANDLED_PROVIDER_COUNT, KEY_HANDLED_PROVIDER_COUNT) + ':'), 1, row);
			table.add (getSmallText (handledSchoolsCount + " " + localize (KEY_OF, KEY_OF) + " " +  broadcastInfo.getSchoolCount ()), 2, row++);
			table.mergeCells (1, row, table.getColumns (), row);
			table.add (getProviderTable (emailedProviders, paperMailedProviders, ignoredProviders), 1, row++);
			add (table);
		} catch (FinderException e) {
			add (localize (KEY_NO_BROADCAST_EXECUTED, KEY_NO_BROADCAST_EXECUTED));
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}

	private String getSignature (final User user) {
		if (null == user) return "";
		final String firstName = user.getFirstName ();
		final String lastName = user.getLastName ();
		return (firstName != null ? firstName + " " : "")
				+ (lastName != null ? lastName : "");
	}

	private Table getProviderTable
		(final Collection emailedProviders, final Collection paperMailedProviders,
		 final Collection ignoredProviders) throws RemoteException {
		final Table table = createTable (4);
 		for (int i = 2; i <= 4; i++) {
 			table.setColumnWidth (i, "33%");
 		}
		int col = 1;
		int row = 1;
		table.setRowColor (row, getHeaderColor ());
		table.add (getSmallHeader ("#"), col++, row);
		table.add (getSmallHeader (localize (KEY_EMAILED_PROVIDERS, KEY_EMAILED_PROVIDERS)), col++, row);
		table.add (getSmallHeader (localize (KEY_PAPER_MAILED_PROVIDERS, KEY_PAPER_MAILED_PROVIDERS)), col++, row);
		table.add (getSmallHeader (localize (KEY_IGNORED_PROVIDERS, KEY_IGNORED_PROVIDERS)), col++, row);
		final int maxRow = Math.max (Math.max (emailedProviders.size (), paperMailedProviders.size ()), ignoredProviders.size ()) + row;
		final Iterator emailedProvidersIterator = emailedProviders.iterator ();
		final Iterator paperMailedProvidersIterator = paperMailedProviders.iterator ();
		final Iterator ignoredProvidersIterator = ignoredProviders.iterator ();

		int numberedRowsCount = 0;
		while (row < maxRow) {
			numberedRowsCount++;
			row++;
			col = 1;
			table.setRowColor (row, (row % 2 == 0) ? getZebraColor1 ()
												 : getZebraColor2 ());
			table.setAlignment (col, row, Table.HORIZONTAL_ALIGN_RIGHT);
			table.add(getSmallHeader (numberedRowsCount + ""), col++, row);
			addProviderName(table, col++, row, emailedProvidersIterator);
			addProviderName(table, col++, row, paperMailedProvidersIterator);
			addProviderName(table, col++, row, ignoredProvidersIterator);
		}
		return table;
	}

	private void addProviderName
		(final Table table, final int col, final int row,	final Iterator iterator)
		throws RemoteException {
		if (iterator.hasNext ()) {
			final CheckAmountReceivingSchool schoolInfo
					= (CheckAmountReceivingSchool) iterator.next ();
			table.add (getSmallText (schoolInfo.getSchool ().getSchoolName ()), col,
								 row);
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
	
	private void sendFiles(IWContext context) throws RemoteException, MoveFileException {
		try {
			final CheckAmountBusiness business = getCheckAmountBusiness(context);
			final IFSBusiness ifsBusiness = getIFSBusiness(context);
			business.sendCheckAmountLists (context.getCurrentUser(),
																		 _currentOperation);
			business.deleteOldCheckAmountBroadcastInfo (_currentOperation, 90);
			ifsBusiness.moveFiles(_currentOperation);
		} catch (CreateException e) {
			e.printStackTrace ();
		} catch (FinderException e) {
			e.printStackTrace ();
		} catch (RemoveException e) {
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

	private static IFSBusiness getIFSBusiness
	(final IWContext context) throws RemoteException {
		return (IFSBusiness) IBOLookup.getServiceInstance
		(context, IFSBusiness.class);	
	}
}
