/*
 * $Id: CitizenAccountApplication.java,v 1.9 2002/11/04 09:33:34 staffan Exp $
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
import com.idega.presentation.*;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.*;
import com.idega.user.data.User;
import java.util.*;
import se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusiness;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

/**
 * This is the presentation class for the CitizenAccount application
 * 
 * 
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class CitizenAccountApplication extends CommuneBlock {
	private final static int ACTION_VIEW_FORM = 0;
	private final static int ACTION_SUBMIT_SIMPLE_FORM = 1;
	private final static int ACTION_SUBMIT_UNKNOWN_CITIZEN_FORM = 2;

	private final static String PARAM_NAME = "caa_name";
	private final static String PARAM_PID = "caa_pid";
	private final static String PARAM_BIRTH_YEAR = "caa_birth_year";
	private final static String PARAM_BIRTH_MONTH = "caa_birth_month";
	private final static String PARAM_BIRTH_DAY = "caa_birth_day";
	private final static String PARAM_GENDER = "caa_pid";
	private final static String PARAM_EMAIL = "caa_email";
	private final static String PARAM_PHONE_HOME = "caa_phone_home";
	private final static String PARAM_PHONE_WORK = "caa_phone_work";
    private final static String PARAM_STREET = "caa_street";
    private final static String PARAM_ZIP_CODE = "caa_zip_code";
    private final static String PARAM_CITY = "caa_city";

    private final static String PARAM_CUSTODIAN1_PID = "caa_custodian1_pid";
    private final static String PARAM_CUSTODIAN1_CIVIL_STATUS
        = "caa_custodian1_civil_status";
    private final static String PARAM_CUSTODIAN2_PID = "caa_custodian2_pid";
    private final static String PARAM_CUSTODIAN2_CIVIL_STATUS
        = "caa_custodian2_civil_status";

    private final static String CUSTODIAN1_KEY = "caa_custodian1";
    private final static String CUSTODIAN2_KEY = "caa_custodian2";

	private final static String PARAM_SIMPLE_FORM_SUBMIT = "caa_simpleSubmit";
	private final static String PARAM_UNKNOWN_CITIZEN_FORM_SUBMIT
        = "caa_unknownCitizenSubmit";

	private final static String ERROR_PID = "caa_pid_error";
	private final static String ERROR_PHONE_HOME = "caa_error_phone_home";
	private final static String ERROR_NO_INSERT = "caa_no_insert";
	private final static String ERROR_NOT_EMAIL = "caa_err_email";

	private final static String TEXT_APPLICATION_SUBMITTED
        = "caa_app_submitted";

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
				case ACTION_VIEW_FORM:
					viewSimpleApplicationForm(iwc);
					break;
				case ACTION_SUBMIT_SIMPLE_FORM:
					submitSimpleForm(iwc);
					break;
				case ACTION_SUBMIT_UNKNOWN_CITIZEN_FORM:
					submitUnknownCitizenForm(iwc);
					break;
			}
		}
		catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}    
	}

    private void addSimpleInputs (final IWContext iwc, final Table inputTable) {
		final DropdownMenu yearInput = new DropdownMenu (PARAM_BIRTH_YEAR);
        final Calendar rightNow = Calendar.getInstance();
        final int currentYear = rightNow.get (Calendar.YEAR);
        for (int year = currentYear - 110; year <= currentYear; year++) {
            final String yearAsString = new Integer (year).toString ();
            yearInput.addMenuElementFirst (yearAsString, yearAsString);
        }
        inputTable.add (yearInput, 1, 2);
        final DropdownMenu monthInput = new DropdownMenu (PARAM_BIRTH_MONTH);
        for (int month = 12; month >= 1; month--) {
            final String monthAsString = new Integer (month).toString ();
            monthInput.addMenuElementFirst (monthAsString, monthAsString);
        }
        inputTable.add (monthInput, 1, 2);
        final DropdownMenu dayInput = new DropdownMenu (PARAM_BIRTH_DAY);
        for (int day = 31; day >= 1; day--) {
            final String dayAsString = new Integer (day).toString ();
            dayInput.addMenuElementFirst (dayAsString, dayAsString);
        }
        inputTable.add (dayInput, 1, 2);
        addSingleInput (iwc, PARAM_PID, "Personnummer", inputTable, 4,
                        _isPIDError, 1, 1);
        addSingleInput (iwc, PARAM_EMAIL, "E-post", inputTable, 40,
                        _isEmailError, 2, 1);
        addSingleInput (iwc, PARAM_PHONE_HOME, "Telefon (hem)", inputTable, 20,
                        _isPhoneHomeError, 1, 3);
        addSingleInput (iwc, PARAM_PHONE_WORK, "Telefon (jobb/mobil)",
                        inputTable, 20, false, 2, 3);
    }

    private void addSingleInput
        (final IWContext iwc, final String paramId, final String defaultText,
         final Table inputTable, final int maxLength, final boolean isError,
         final int col, final int row) {

        final TextInput textInput = new TextInput (paramId);
		textInput.setMaxlength (maxLength);
		textInput.setStyleClass (getSmallTextFontStyle ());
		final String param = iwc.getParameter (paramId);
		if (param != null) {
			textInput.setContent (param);
        }
        final String subject = localize (paramId, defaultText);
        final Text text = isError ? getSmallErrorText (subject)
                : getSmallText (subject);
        inputTable.add (text, col, row);
        inputTable.add (textInput, col, row + 1);
    }

    private void addSubmitButton (final IWContext iwc, final String submitId,
                                  final String defaultText,
                                  final Table inputTable, final int row) {
		inputTable.setAlignment(2, row, "right");
        final String text = localize(submitId, defaultText);
		final SubmitButton submitButton = new SubmitButton
                (getBundle(iwc).getImageButton (text), submitId);
		submitButton.setStyleClass(getLinkFontStyle());
		inputTable.add(submitButton, 2, row);
    }

	private void viewUnknownCitizenApplicationForm(IWContext iwc) {
		Form accountForm = new Form();

		Table inputTable = new Table(2, 15);
		inputTable.setCellspacing(2);
		inputTable.setCellpadding(4);
		inputTable.setColor(getBackgroundColor());

        addSimpleInputs (iwc, inputTable);

        addSingleInput (iwc, PARAM_NAME, "Namn", inputTable, 40, false, 1, 5);
        addSingleInput (iwc, PARAM_STREET, "Gatuadress", inputTable, 40, false,
                        2, 5);
        addSingleInput (iwc, PARAM_ZIP_CODE, "Postnummer", inputTable, 40,
                        false, 1, 7);
        addSingleInput (iwc, PARAM_CITY, "Postadress", inputTable, 40, false, 2,
                        7);

        final Text custodianHeader1
                = getLocalizedHeader (CUSTODIAN1_KEY, "Vårdnadshavare 1");

        inputTable.add (custodianHeader1, 1, 9);
        addSingleInput (iwc, PARAM_CUSTODIAN1_PID, "Personnummer", inputTable,
                        40, false, 1, 10);
        addSingleInput (iwc, PARAM_CUSTODIAN1_CIVIL_STATUS, "Civilstånd",
                        inputTable, 40, false, 2, 10);

        final Text custodianHeader2
                = getLocalizedHeader (CUSTODIAN2_KEY, "Vårdnadshavare 2");
        inputTable.add (custodianHeader2, 1, 12);
        addSingleInput (iwc, PARAM_CUSTODIAN2_PID, "Personnummer", inputTable,
                        40, false, 1, 13);
        addSingleInput (iwc, PARAM_CUSTODIAN2_CIVIL_STATUS, "Civilstånd",
                        inputTable, 40, false, 2, 13);

        addSubmitButton (iwc, PARAM_UNKNOWN_CITIZEN_FORM_SUBMIT,
                         "Submit application", inputTable, 15);

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

	private void viewSimpleApplicationForm(IWContext iwc) {
		Form accountForm = new Form();

		Table inputTable = new Table(2, 6);
		inputTable.setCellspacing(2);
		inputTable.setCellpadding(4);
		inputTable.setAlignment(2, 6, "right");
		inputTable.setColor(getBackgroundColor());

        addSimpleInputs (iwc, inputTable);

		SubmitButton submitButton
                = new SubmitButton(getBundle(iwc).getImageButton
                                   (localize(PARAM_SIMPLE_FORM_SUBMIT,
                                             "Submit application")),
                                   PARAM_SIMPLE_FORM_SUBMIT);
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

	private void submitSimpleForm (final IWContext iwc) {
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

		if (_isError) {
			viewSimpleApplicationForm(iwc);
			return;
		}

		boolean isInserted = false;
		try {
			final CitizenAccountBusiness business
                    = (CitizenAccountBusiness) IBOLookup.getServiceInstance
                    (iwc, CitizenAccountBusiness.class);
            final User user = business.getUser(pidString);
            if (user == null) {
                viewUnknownCitizenApplicationForm (iwc);
                return;
            }
            final String ssn = getSsn (iwc);
			isInserted = business.insertApplication
                    (user, ssn, emailString, phoneHomeString,
                     phoneWorkString);
		}
		catch (Exception e) {
			e.printStackTrace();
			isInserted = false;
		}

		if (!isInserted) {
			_isError = true;
			addErrorString(localize(ERROR_NO_INSERT,
                                    "Unable to insert application"));
			viewSimpleApplicationForm(iwc);
			return;
		}

		if (getResponsePage() != null)
			iwc.forwardToIBPage(getParentPage(), getResponsePage());
		else
			add(new Text(localize(TEXT_APPLICATION_SUBMITTED,
                                  "Application submitted")));
	}

    private void submitUnknownCitizenForm (final IWContext iwc) {
		final String name = iwc.getParameter(PARAM_NAME);
		final String pid = iwc.getParameter(PARAM_PID);
		final String phoneHome = iwc.getParameter(PARAM_PHONE_HOME);
		final String email = iwc.getParameter(PARAM_EMAIL);
		final String phoneWork = iwc.getParameter(PARAM_PHONE_WORK);
        final String custodian1Pid = iwc.getParameter (PARAM_CUSTODIAN1_PID);
        final String custodian1CivilStatus
                = iwc.getParameter (PARAM_CUSTODIAN1_CIVIL_STATUS);
        final String custodian2Pid = iwc.getParameter (PARAM_CUSTODIAN2_PID);
        final String custodian2CivilStatus
                = iwc.getParameter (PARAM_CUSTODIAN2_CIVIL_STATUS);
        final String street = iwc.getParameter (PARAM_STREET);
        final String zipCode = iwc.getParameter (PARAM_ZIP_CODE);
        final String city = iwc.getParameter (PARAM_CITY);

		_errorMsg = null;

		if (pid == null || pid.equals("")) {
			_isPIDError = true;
			_isError = true;
			addErrorString(localize(ERROR_PID, "PID invalid"));
		}

		if (email == null || email.equals("")) {
			_isEmailError = true;
			_isError = true;
			addErrorString(localize(ERROR_NOT_EMAIL, "Email invalid"));
		}

		if (phoneHome == null || phoneHome.equals("")) {
			_isPhoneHomeError = true;
			_isError = true;
			addErrorString(localize(ERROR_PHONE_HOME, "Home phone invalid"));
		}

		if (_isError) {
			viewSimpleApplicationForm(iwc);
			return;
		}

		boolean isInserted = false;
		try {
			final CitizenAccountBusiness business
                    = (CitizenAccountBusiness) IBOLookup.getServiceInstance
                    (iwc, CitizenAccountBusiness.class);
            final User user = business.getUser (pid);
            if (user != null) {
                viewSimpleApplicationForm (iwc);
                return;
            }
            final String ssn = getSsn (iwc);
            final int year = new Integer (iwc.getParameter
                                          (PARAM_BIRTH_YEAR)).intValue ();
            final int month = new Integer (iwc.getParameter
                                          (PARAM_BIRTH_MONTH)).intValue ();
            final int day = new Integer (iwc.getParameter
                                          (PARAM_BIRTH_DAY)).intValue ();
            final Calendar birthDate = Calendar.getInstance ();
            birthDate.set (year, month - 1, day);
			isInserted = business.insertApplication
                    (name, ssn, birthDate.getTime (), email,
                     phoneHome, phoneWork,
                     custodian1Pid, custodian1CivilStatus, custodian2Pid,
                     custodian2CivilStatus, street, zipCode, city);
		}
		catch (Exception e) {
			e.printStackTrace();
			isInserted = false;
		}

		if (!isInserted) {
			_isError = true;
			addErrorString(localize(ERROR_NO_INSERT,
                                    "Unable to insert application"));
			viewUnknownCitizenApplicationForm (iwc);
			return;
		}
        
		if (getResponsePage() != null)
			iwc.forwardToIBPage(getParentPage(), getResponsePage());
		else
			add(new Text(localize(TEXT_APPLICATION_SUBMITTED,
                                  "Application submitted")));
    }

    private static String getSsn (final IWContext iwc) {
        final String year = iwc.getParameter (PARAM_BIRTH_YEAR);
        final String month = iwc.getParameter (PARAM_BIRTH_MONTH);
        final String day = iwc.getParameter (PARAM_BIRTH_DAY);
        final String pid = iwc.getParameter (PARAM_PID);
        final String ssn = year + (month.length () > 1 ? month : "0" + month)
                + (day.length () > 1 ? day : "0" + day) + pid;
        return ssn;
    }
    
	private void addErrorString(String errorString) {
		if (_errorMsg == null)
			_errorMsg = new Vector();

		_errorMsg.add(errorString);
	}

	private int parseAction (IWContext iwc) {
		int action = ACTION_VIEW_FORM;

		if (iwc.isParameterSet(PARAM_SIMPLE_FORM_SUBMIT)) {
			action = ACTION_SUBMIT_SIMPLE_FORM;
		} else if (iwc.isParameterSet (PARAM_UNKNOWN_CITIZEN_FORM_SUBMIT)) {
            action = ACTION_SUBMIT_UNKNOWN_CITIZEN_FORM;
        }

		return action;
	}
}
