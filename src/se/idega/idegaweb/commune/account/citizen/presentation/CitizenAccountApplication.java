/*
 * $Id: CitizenAccountApplication.java,v 1.5 2002/10/15 11:38:05 laddi Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.account.citizen.presentation;

import com.idega.block.process.business.CaseBusiness;
import com.idega.business.IBOLookup;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusiness;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import java.util.Vector;
import java.util.Iterator;

/**
 * This is the presentation class for the CitizenAccount application
 * 
 * 
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class CitizenAccountApplication extends CommuneBlock {
	private final static int ACTION_VIEW_FORM = 0;
	private final static int ACTION_SUBMIT_FORM = 1;

	private final static String PARAM_PID = "caa_pid";
	private final static String PARAM_EMAIL = "caa_email";
	private final static String PARAM_PHONE_HOME = "caa_phone_home";
	private final static String PARAM_PHONE_WORK = "caa_phone_work";
	private final static String PARAM_FORM_SUBMIT = "caa_submit";

	private final static String ERROR_PID = "caa_pid_error";
	private final static String ERROR_PHONE_HOME = "caa_error_phone_home";
	private final static String ERROR_NO_INSERT = "caa_no_insert";
	private final static String ERROR_NOT_EMAIL = "caa_err_email";

	private final static String TEXT_APPLICATION_SUBMITTED = "caa_app_submitted";

	private boolean _isPIDError = false;
	private boolean _isPhoneHomeError = false;
	private boolean _isEmailError = false;
	private boolean _isError = false;
	private Vector _errorMsg = null;


	public CitizenAccountApplication() {
	}

	public void main(IWContext iwc) {
		setResourceBundle(getResourceBundle(iwc));

		try {
			int action = parseAction(iwc);
			switch (action) {
				case ACTION_VIEW_FORM :
					viewForm(iwc);
					break;
				case ACTION_SUBMIT_FORM :
					submitForm(iwc);
					break;
			}
		}
		catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}    
	}

	private void viewForm(IWContext iwc) {
		Form accountForm = new Form();

		Table inputTable = new Table(2, 6);
		inputTable.setCellspacing(2);
		inputTable.setCellpadding(4);
		inputTable.setAlignment(2, 6, "right");
		inputTable.setColor(getBackgroundColor());

		String pid = localize(PARAM_PID, "PID");
		String email = localize(PARAM_EMAIL, "E-Mail");
		String phone_home = localize(PARAM_PHONE_HOME, "Home phone");
		String phone_work = localize(PARAM_PHONE_WORK, "Work/mobile phone");

		TextInput inputPid = new TextInput(PARAM_PID);
		inputPid.setMaxlength(40);
		TextInput inputEmail = new TextInput(PARAM_EMAIL);
		inputEmail.setAsEmail(localize(ERROR_NOT_EMAIL, "Not a valid email"));
		inputEmail.setMaxlength(40);
		TextInput inputPhoneHome = new TextInput(PARAM_PHONE_HOME);
		inputPhoneHome.setMaxlength(20);
		TextInput inputPhoneWork = new TextInput(PARAM_PHONE_WORK);
		inputPhoneWork.setMaxlength(20);

		inputPid.setStyleClass(getSmallTextFontStyle());
		inputEmail.setStyleClass(getSmallTextFontStyle());
		inputPhoneHome.setStyleClass(getSmallTextFontStyle());
		inputPhoneWork.setStyleClass(getSmallTextFontStyle());

		String pidString = iwc.getParameter(PARAM_PID);
		String emailString = iwc.getParameter(PARAM_EMAIL);
		String phoneHomeString = iwc.getParameter(PARAM_PHONE_HOME);
		String phoneWorkString = iwc.getParameter(PARAM_PHONE_WORK);

		if (pidString != null)
			inputPid.setContent(pidString);

		if (emailString != null)
			inputEmail.setContent(emailString);

		if (phoneHomeString != null)
			inputPhoneHome.setContent(phoneHomeString);

		if (phoneWorkString != null)
			inputPhoneWork.setContent(phoneWorkString);

		if (!_isPIDError)
			inputTable.add(getSmallText(pid), 1, 1);
		else
			inputTable.add(getSmallErrorText(pid), 1, 1);
		if (!_isEmailError)
			inputTable.add(getSmallText(email), 2, 1);
		else
			inputTable.add(getSmallErrorText(email), 2, 1);
		if (!_isPhoneHomeError)
			inputTable.add(getSmallText(phone_home), 1, 3);
		else
			inputTable.add(getSmallErrorText(phone_home), 1, 3);
		inputTable.add(getSmallText(phone_work), 2, 3);

		inputTable.add(inputPid, 1, 2);
		inputTable.add(inputEmail, 2, 2);
		inputTable.add(inputPhoneHome, 1, 4);
		inputTable.add(inputPhoneWork, 2, 4);

		SubmitButton submitButton = new SubmitButton(getBundle(iwc).getImageButton(localize(PARAM_FORM_SUBMIT, "Submit application")), PARAM_FORM_SUBMIT);
		submitButton.setStyleClass(getLinkFontStyle());

		inputTable.add(submitButton, 2, 6);
		if (_isError) {
			if (_errorMsg != null) {
				Table errorTable = new Table(1, 1);
				errorTable.setCellspacing(2);
				errorTable.setCellpadding(4);
				Iterator it = _errorMsg.iterator();
				while (it.hasNext()) {
					String errorMsg = (String) it.next();
					errorTable.add(getErrorText(errorMsg), 1, 1);
					errorTable.add(Text.getBreak(), 1, 1);
				}
				accountForm.add(errorTable);
			}
		}
		accountForm.add(inputTable);

		add(accountForm);
	}

	private void submitForm(IWContext iwc) {
		String pidString = iwc.getParameter(PARAM_PID);
		String phoneHomeString = iwc.getParameter(PARAM_PHONE_HOME);
		String emailString = iwc.getParameter(PARAM_EMAIL);
		String phoneWorkString = iwc.getParameter(PARAM_PHONE_WORK);

		_errorMsg = null;

		if (pidString == null || pidString.equals("")) {
			_isPIDError = true;
			_isError = true;
			addErrorString(localize(ERROR_PID, "PID invalid"));
		}

		if (emailString == null || emailString.equals("")) {
			_isEmailError = true;
			_isError = true;
			addErrorString(localize(ERROR_NOT_EMAIL, "Email invalid"));
		}

		if (phoneHomeString == null || phoneHomeString.equals("")) {
			_isPhoneHomeError = true;
			_isError = true;
			addErrorString(localize(ERROR_PHONE_HOME, "Home phone invalid"));
		}

		if (_isError == true) {
			viewForm(iwc);
			return;
		}

		boolean insert = false;
		try {
			CitizenAccountBusiness business = (CitizenAccountBusiness) IBOLookup.getServiceInstance(iwc, CitizenAccountBusiness.class);
			insert = business.insertApplication(business.getUser(pidString), pidString, emailString, phoneHomeString, phoneWorkString);
		}
		catch (Exception e) {
			e.printStackTrace();
			insert = false;
		}

		if (!insert) {
			_isError = true;
			addErrorString(localize(ERROR_NO_INSERT, "Unable to insert application"));
			viewForm(iwc);
			return;
		}

		if (getResponsePage() != null)
			iwc.forwardToIBPage(getParentPage(), getResponsePage());
		else
			add(new Text(localize(TEXT_APPLICATION_SUBMITTED, "Application submitted")));
	}

	private void addErrorString(String errorString) {
		if (_errorMsg == null)
			_errorMsg = new Vector();

		_errorMsg.add(errorString);
	}

	private int parseAction(IWContext iwc) {
		int action = ACTION_VIEW_FORM;

		if (iwc.isParameterSet(PARAM_FORM_SUBMIT)) {
			action = ACTION_SUBMIT_FORM;
		}

		return action;
	}
}