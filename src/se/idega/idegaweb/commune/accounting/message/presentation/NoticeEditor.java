/*
 * $Id: NoticeEditor.java,v 1.1 2003/08/29 15:02:08 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.message.presentation;

import java.util.StringTokenizer;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.text.Break;
import com.idega.presentation.ui.TextArea;

import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.ApplicationForm;
import se.idega.idegaweb.commune.accounting.presentation.ButtonPanel;

/** 
 * NoticeEditor is an idegaWeb block that handles sending a
 * notice message to all providers. The message is sent as an
 * e-mail and as case.
 * <p>
 * Last modified: $Date: 2003/08/29 15:02:08 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.1 $
 */
public class NoticeEditor extends AccountingBlock {

	private final static int ACTION_DEFAULT = 0;
	private final static int ACTION_PREVIEW = 1;
	private final static int ACTION_BACK = 2;
	private final static int ACTION_SEND = 3;
	
	private final static String PP = "cacc_notice_"; // Parameter prefix 

	private final static String PARAMETER_BODY = PP + "body";
	private final static String PARAMETER_PREVIEW = PP + "preview";
	private final static String PARAMETER_BACK = PP + "back";
	private final static String PARAMETER_SEND = PP + "send";
	
	private final static String KP = "notice_editor."; // key prefix 
	
	private final static String KEY_TITLE = KP + "title";
	private final static String KEY_TITLE_SEND_CONFIRM = KP + "title_send_confirm";
	private final static String KEY_MAIN_ACTIVITY = KP + "main_activity";
	private final static String KEY_SCHOOL = KP + "school";
	private final static String KEY_PREVIEW = KP + "preview";
	private final static String KEY_BACK = KP + "back";
	private final static String KEY_SEND_NOTICE = KP + "send_notice";

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
		
		String body = getParameter(iwc, PARAMETER_BODY);
		StringTokenizer st = new StringTokenizer(body, "\n");
		while (st.hasMoreTokens()) {
			String row = st.nextToken();
			table.add(getText(row), 1, 1);
			table.add(new Break());
		}
		app.setMainPanel(table);

		ButtonPanel bp = new ButtonPanel(this);
		bp.addLocalizedButton(PARAMETER_SEND, KEY_SEND_NOTICE, "Skicka påminnelse");
		bp.addLocalizedButton(PARAMETER_BACK, KEY_BACK, "Tillbaka");
		app.setButtonPanel(bp);

		app.addHiddenInput(PARAMETER_BODY, getParameter(iwc, PARAMETER_BODY));
		
		add(app);
	}

	/*
	 * Handles the send action for this block.
	 */	
	private void handleSendAction(IWContext iwc) {
	}

	/*
	 * Returns a notice business object
	 */
//	private NoticeBusiness getNoticeBusiness(IWContext iwc) throws RemoteException {
//		return (NoticeBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, NoticeBusiness.class);
//	}	
}
