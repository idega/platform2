/*
 * $Id: NoticeEditor.java,v 1.5 2003/09/10 08:11:08 anders Exp $
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
 * Last modified: $Date: 2003/09/10 08:11:08 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.5 $
 */
public class NoticeEditor extends AccountingBlock {

	private final static int ACTION_DEFAULT = 0;
	private final static int ACTION_PREVIEW = 1;
	private final static int ACTION_BACK = 2;
	private final static int ACTION_SEND = 3;
	
	private final static String PP = "cacc_notice_"; // Parameter prefix 

	private final static String PARAMETER_SUBJECT = PP + "subject";
	private final static String PARAMETER_BODY = PP + "body";
	private final static String PARAMETER_PREVIEW = PP + "preview";
	private final static String PARAMETER_BACK = PP + "back";
	private final static String PARAMETER_SEND = PP + "send";
	private final static String PARAMETER_DEFAULT = PP + "default";
	
	private final static String KP = "notice_editor."; // key prefix 
	
	private final static String KEY_TITLE = KP + "title";
	private final static String KEY_TITLE_SEND_CONFIRM = KP + "title_send_confirm";
	private final static String KEY_TITLE_SEND_CONFIRM_ERROR = KP + "title_send_confirm_error";
	private final static String KEY_TITLE_NOTICE_SENT = KP + "title_notice_sent";
//	private final static String KEY_MAIN_ACTIVITY = KP + "main_activity";
	private final static String KEY_SCHOOL = KP + "school";
	private final static String KEY_PREVIEW = KP + "preview";
	private final static String KEY_BACK = KP + "back";
	private final static String KEY_SEND_NOTICE = KP + "send_notice";
	private final static String KEY_NOTICE_SENT = KP + "notice_sent";
	private final static String KEY_HEADMASTER = KP + "headmaster";
	private final static String KEY_SUBJECT_LABEL = KP + "subject_label";
	private final static String KEY_SUBJECT = KP + "subject";

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
		app.setLocalizedTitle(KEY_TITLE, "Påminnelsebrev");

		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		TextArea body = (TextArea) getStyledInterface(new TextArea(PARAMETER_BODY));
		body.setColumns(60);
		body.setRows(10);
		body.setValue(getParameter(iwc, PARAMETER_BODY));
		table.add(body, 1, 1);
		app.setMainPanel(table);

		ButtonPanel bp = new ButtonPanel(this);
		bp.addLocalizedButton(PARAMETER_PREVIEW, KEY_PREVIEW, "Förhandsgranska");
		app.setButtonPanel(bp);

		add(app);
	}
	
	/*
	 * Handles the preview action for this block.
	 */	
	private void handlePreviewAction(IWContext iwc) {		
		ApplicationForm app = new ApplicationForm(this);
		app.setLocalizedTitle(KEY_TITLE_SEND_CONFIRM, "Påminnelsebrev - förhandsgranska");
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());

		String subject = localize(KEY_SUBJECT, "Påminnelse från Nacka24");
		subject += " - " + formatDate(new Date(System.currentTimeMillis()), 10);
		table.add(getLocalizedLabel(KEY_SUBJECT_LABEL, "Rubrik"), 1, 1);
		table.add(getSmallHeader(" " + subject), 1, 1);
		
		String body = getParameter(iwc, PARAMETER_BODY);
		StringTokenizer st = new StringTokenizer(body, "\n");
		while (st.hasMoreTokens()) {
			String row = st.nextToken();
			table.add(getText(row), 1, 3);
			table.add(new Break(), 1, 3);
		}
		app.setMainPanel(table);

		ButtonPanel bp = new ButtonPanel(this);
		bp.addLocalizedButton(PARAMETER_SEND, KEY_SEND_NOTICE, "Skicka påminnelse");
		bp.addLocalizedButton(PARAMETER_BACK, KEY_BACK, "Tillbaka");
		app.setButtonPanel(bp);

		app.addHiddenInput(PARAMETER_SUBJECT, subject);
		app.addHiddenInput(PARAMETER_BODY, getParameter(iwc, PARAMETER_BODY));
		
		add(app);
	}

	/*
	 * Handles the send action for this block.
	 */	
	private void handleSendAction(IWContext iwc) {

		Collection schools = null; 		
		try {
			NoticeBusiness nb = getNoticeBusiness(iwc);
			//String subject = getParameter(iwc, PARAMETER_SUBJECT);
			String body = getParameter(iwc, PARAMETER_BODY);
			schools = nb.sendNotice("Rubrik", body);
		} 
		catch (RemoteException e) {
			add(new ExceptionWrapper(e));
			return;
		}
		catch (NoticeException e) {
			ApplicationForm app = new ApplicationForm(this);
			app.setLocalizedTitle(KEY_TITLE_SEND_CONFIRM_ERROR, "Påminnelsebrev kunde inte skickas");
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
			return;
		}

		ApplicationForm app = new ApplicationForm(this);
		app.setLocalizedTitle(KEY_TITLE_NOTICE_SENT, "Påminnelsebrev sänt");
		
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.add(getLocalizedText(KEY_NOTICE_SENT, "Påminnelsen har skickats till följande rektorer:"), 1, 1);
		app.setSearchPanel(table);

		ListTable list = new ListTable(this, 2);
		list.setLocalizedHeader(KEY_SCHOOL, "Skola", 1);
		list.setLocalizedHeader(KEY_HEADMASTER, "Rektor", 2);
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
		app.setMainPanel(getErrorText("OBS! Detta är en testversion. Inga riktiga meddelanden har skickats."));
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
}
