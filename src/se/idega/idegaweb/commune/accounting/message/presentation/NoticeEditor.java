/*
 * $Id: NoticeEditor.java,v 1.12 2004/05/11 09:57:05 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.message.presentation;

import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import java.sql.Date;

import java.rmi.RemoteException;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.text.Break;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.CheckBox;

import com.idega.block.school.data.SchoolCategory;

import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.ApplicationForm;
import se.idega.idegaweb.commune.accounting.presentation.ListTable;
import se.idega.idegaweb.commune.accounting.presentation.ButtonPanel;

import se.idega.idegaweb.commune.accounting.message.business.NoticeBusiness;
import se.idega.idegaweb.commune.accounting.message.business.NoticeException;


/** 
 * NoticeEditor is an idegaWeb block that handles sending a
 * notice message to all providers. The message is sent as an
 * e-mail and as case.
 * <p>
 * Last modified: $Date: 2004/05/11 09:57:05 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.12 $
 */
public class NoticeEditor extends AccountingBlock {

	private final static int ACTION_DEFAULT = 0;
	private final static int ACTION_PREVIEW = 1;
	private final static int ACTION_BACK = 2;
	private final static int ACTION_SEND = 3;
	
	private final static String PP = "cacc_notice_"; // Parameter prefix 

	private final static String PARAMETER_SUBJECT = PP + "subject";
	private final static String PARAMETER_BODY = PP + "body";
	private final static String PARAMETER_OPERATIONAL_FIELD = PP + "operational_field";
	private final static String PARAMETER_PREVIEW = PP + "preview";
	private final static String PARAMETER_BACK = PP + "back";
	private final static String PARAMETER_SEND = PP + "send";
	private final static String PARAMETER_DEFAULT = PP + "default";
	
	private final static String KP = "notice_editor."; // key prefix 
	
	private final static String KEY_TITLE = KP + "title";
	private final static String KEY_TITLE_SEND_CONFIRM = KP + "title_send_confirm";
	private final static String KEY_TITLE_SEND_CONFIRM_ERROR = KP + "title_send_confirm_error";
	private final static String KEY_TITLE_NOTICE_SENT = KP + "title_notice_sent";
	private final static String KEY_SCHOOL = KP + "school";
	private final static String KEY_OPERATIONAL_FIELDS = KP + "operational_fields";	
	private final static String KEY_NO_OPERATIONAL_FIELDS_SELECTED = KP + "no_operational_fields_selected";	
	private final static String KEY_AND = KP + "and";
	private final static String KEY_PREVIEW = KP + "preview";
	private final static String KEY_BACK = KP + "back";
	private final static String KEY_SEND_NOTICE = KP + "send_notice";
	private final static String KEY_NOTICE_SENT = KP + "notice_sent";
	private final static String KEY_RECIPIENT = KP + "recipient";
	private final static String KEY_SUBJECT_LABEL = KP + "subject_label";
	private final static String KEY_SUBJECT = KP + "subject";
	private final static String KEY_MESSAGE_TOO_LONG = KP + "message_too_long";

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
				case ACTION_PREVIEW:
					handlePreviewAction(iwc);
					break;
				case ACTION_BACK:
					handleDefaultAction(iwc);
					break;
				case ACTION_SEND:
					handleSendAction(iwc);
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
		
		if (iwc.isParameterSet(PARAMETER_PREVIEW)) {
			action = ACTION_PREVIEW;
		} else if (iwc.isParameterSet(PARAMETER_BACK)) {
			action = ACTION_BACK;
		} else if (iwc.isParameterSet(PARAMETER_SEND)) {
			action = ACTION_SEND;
		} else if (iwc.isParameterSet(PARAMETER_DEFAULT)) {
			action = ACTION_DEFAULT;
		}
		return action;
	}

	/*
	 * Handles the default action for this block.
	 */	
	private void handleDefaultAction(IWContext iwc) {
		ApplicationForm app = new ApplicationForm(this);
		app.setLocalizedTitle(KEY_TITLE, "PŒminnelsebrev");


		Table mainTable = new Table();
		mainTable.setCellpadding(0);
		mainTable.setCellspacing(0);
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		TextArea body = (TextArea) getStyledInterface(new TextArea(PARAMETER_BODY));
		body.setColumns(80);
		body.setRows(16);
		body.setValue(getParameter(iwc, PARAMETER_BODY));
		table.add(body, 1, 1);
		mainTable.add(table, 1, 1);
		table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		int row = 1;
		Collection c = null;
		try {
			c = getBusiness().getExportBusiness().getAllOperationalFields();
			Iterator iter = c.iterator();
			while (iter.hasNext()) {
				SchoolCategory sc = (SchoolCategory) iter.next();
				CheckBox cb = new CheckBox(PARAMETER_OPERATIONAL_FIELD, sc.getPrimaryKey().toString());
				String[] selectedOperationalFields = iwc.getParameterValues(PARAMETER_OPERATIONAL_FIELD);
				if (selectedOperationalFields != null) {
					for (int i = 0; i < selectedOperationalFields.length; i++) {						
						if (sc.getPrimaryKey().toString().equals(selectedOperationalFields[i])) {
							cb.setChecked(true);
						}
					}
				}
				table.add(cb, 1, row);
				table.add(localize(sc.getLocalizedKey(), sc.getLocalizedKey()), 2, row++);
			}
			table.setColumnWidth(2, "100%");
		} catch (RemoteException e) {}
		mainTable.add(table, 1, 2);
		app.setMainPanel(mainTable);

		ButtonPanel bp = new ButtonPanel(this);
		bp.addLocalizedButton(PARAMETER_PREVIEW, KEY_PREVIEW, "Fšrhandsgranska");
		app.setButtonPanel(bp);

		add(app);
	}
	
	/*
	 * Handles the preview action for this block.
	 */	
	private void handlePreviewAction(IWContext iwc) {		
		ApplicationForm app = new ApplicationForm(this);
		app.setLocalizedTitle(KEY_TITLE_SEND_CONFIRM, "PŒminnelsebrev - fšrhandsgranska");
		Table mainTable = new Table();
		mainTable.setCellpadding(0);
		mainTable.setCellspacing(0);

		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());

		int row = 1;
		String subject = localize(KEY_SUBJECT, "PŒminnelse frŒn Nacka24");
		subject += " - " + formatDate(new Date(System.currentTimeMillis()), 10);
		table.add(getLocalizedLabel(KEY_SUBJECT_LABEL, "Rubrik"), 1, row);
		table.add(getSmallHeader(" " + subject), 1, row);
		row += 2;
		
		String body = getParameter(iwc, PARAMETER_BODY);
		if (body.length() > 4000) {
			body = body.substring(0, 4000);
			table.add(getErrorText(localize(KEY_MESSAGE_TOO_LONG, "Message too long (max 4000 characters)")), 1, row++);
		}
		StringTokenizer st = new StringTokenizer(body, "\n");
		while (st.hasMoreTokens()) {
			String line = st.nextToken();
			table.add(getText(line), 1, row);
			table.add(new Break(), 1, row);
		}
		mainTable.add(table, 1, 1);
		
		table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		
		table.add(getLocalizedLabel(KEY_OPERATIONAL_FIELDS, "Huvudverksamheter"), 1, row);
		String[] selectedOperationalFields = iwc.getParameterValues(PARAMETER_OPERATIONAL_FIELD);
		if (selectedOperationalFields == null || selectedOperationalFields.length == 0) {
			table.add(getErrorText(localize(KEY_NO_OPERATIONAL_FIELDS_SELECTED, "Inga huvudverksamheter valda")), 2, row++);
		} else {
			try {
				Collection c = getBusiness().getExportBusiness().getAllOperationalFields();
				String fields = "";
				for (int i = 0; i < selectedOperationalFields.length; i++) {
					String operationalField = selectedOperationalFields[i];
					app.addHiddenInput(PARAMETER_OPERATIONAL_FIELD, operationalField);
					Iterator iter = c.iterator();
					while (iter.hasNext()) {
						SchoolCategory sc = (SchoolCategory) iter.next();
						String id = sc.getPrimaryKey().toString();
						if (operationalField.equals(id)) {
							fields += localize(sc.getLocalizedKey(), sc.getLocalizedKey());
							if (i < (selectedOperationalFields.length - 1)) {
								if (i != (selectedOperationalFields.length - 2)) {
									fields += ", ";
								} else {
									fields += " " + localize(KEY_AND, "och") + " ";
								}
							}
						}
					}
				}
				table.add(getSmallHeader(fields), 2, row++);
			} catch (RemoteException e) {}
		}
		mainTable.add(table, 1, 2);

		app.setMainPanel(mainTable);

		ButtonPanel bp = new ButtonPanel(this);
		bp.addLocalizedButton(PARAMETER_SEND, KEY_SEND_NOTICE, "Skicka pŒminnelse");
		bp.addLocalizedButton(PARAMETER_BACK, KEY_BACK, "Tillbaka");
		app.setButtonPanel(bp);

		app.addHiddenInput(PARAMETER_SUBJECT, subject);
		app.addHiddenInput(PARAMETER_BODY, replaceCitation(getParameter(iwc, PARAMETER_BODY)));
		
		add(app);
	}

	/*
	 * Handles the send action for this block.
	 */	
	private void handleSendAction(IWContext iwc) {

		Collection schools = null; 		
		try {
			NoticeBusiness nb = getNoticeBusiness(iwc);
			String subject = getParameter(iwc, PARAMETER_SUBJECT);
			String body = restoreCitation(getParameter(iwc, PARAMETER_BODY));
			String[] operationalFields = iwc.getParameterValues(PARAMETER_OPERATIONAL_FIELD);
			schools = nb.sendNotice(subject, body, operationalFields);
		} 
		catch (RemoteException e) {
			add(new ExceptionWrapper(e));
			return;
		}
		catch (NoticeException e) {
			ApplicationForm app = new ApplicationForm(this);
			app.setLocalizedTitle(KEY_TITLE_SEND_CONFIRM_ERROR, "PŒminnelsebrev kunde inte skickas");
			add(app);
			String errorMessage = localize(e.getTextKey(), e.getDefaultText());
			Table table = new Table();
			table.setCellpadding(getCellpadding());
			table.setCellspacing(getCellspacing());
			table.add(getErrorText(errorMessage), 1, 1);
			app.setMainPanel(table);
			ButtonPanel bp = new ButtonPanel(this);
			bp.addLocalizedButton(PARAMETER_DEFAULT, KEY_BACK, "Tillbaka");
			app.setButtonPanel(bp);
			
			app.addHiddenInput(PARAMETER_BODY, getParameter(iwc, PARAMETER_BODY));
			String[] selectedOperationalFields = iwc.getParameterValues(PARAMETER_OPERATIONAL_FIELD);
			if (selectedOperationalFields != null) {
				for (int i = 0; i < selectedOperationalFields.length; i++) {
					app.addHiddenInput(PARAMETER_OPERATIONAL_FIELD, selectedOperationalFields[i]);
				}
			}
			return;
		}

		ApplicationForm app = new ApplicationForm(this);
		app.setLocalizedTitle(KEY_TITLE_NOTICE_SENT, "PŒminnelsebrev sŠnt");
		
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.add(getLocalizedText(KEY_NOTICE_SENT, "PŒminnelsen har skickats till fšljande rektorer:"), 1, 1);
		app.setSearchPanel(table);

		ListTable list = new ListTable(this, 2);
		list.setLocalizedHeader(KEY_SCHOOL, "Skola", 1);
		list.setLocalizedHeader(KEY_RECIPIENT, "Mottagare", 2);
		if (schools != null) {
			Iterator iter = schools.iterator();
			while (iter.hasNext()) {
				String[] s = (String[]) iter.next();
				list.add(s[0]);
				list.add(s[1]);
			}
		}		
		app.setMainPanel(list);
		app.setMainPanel(new Break()); // Test
//		app.setMainPanel(getErrorText("OBS! Detta Šr en testversion. Inga riktiga meddelanden har skickats."));
		ButtonPanel bp = new ButtonPanel(this);
		bp.addLocalizedButton(PARAMETER_DEFAULT, KEY_BACK, "Tillbaka");
		app.setButtonPanel(bp);

		add(app);
	}

	/*
	 * Returns a notice business object
	 */
	private NoticeBusiness getNoticeBusiness(IWContext iwc) throws RemoteException {
		return (NoticeBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, NoticeBusiness.class);
	}
	
	/*
	 * Returns the specified string with citation replaced by ~ 
	 */
	private String replaceCitation(String s) {
		return s.replace('\"', '~');
	}
	
	/*
	 * Returns the specified string with ~ replaced by citation 
	 */
	private String restoreCitation(String s) {
		return s.replace('~', '\"');
	}
}
