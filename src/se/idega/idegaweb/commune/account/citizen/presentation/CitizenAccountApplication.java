/*
 * $Id: CitizenAccountApplication.java,v 1.11 2002/11/05 12:03:51 staffan Exp $
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
import com.idega.user.data.*;
import java.rmi.RemoteException;
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

	private final static String PARAM_FIRST_NAME = "caa_first_name";
	private final static String PARAM_LAST_NAME = "caa_last_name";
	private final static String PARAM_PID = "caa_pid";
	private final static String PARAM_BIRTH_YEAR = "caa_birth_year";
	private final static String PARAM_BIRTH_MONTH = "caa_birth_month";
	private final static String PARAM_BIRTH_DAY = "caa_birth_day";
	private final static String PARAM_GENDER = "caa_gender";
	private final static String PARAM_EMAIL = "caa_email";
	private final static String PARAM_PHONE_HOME = "caa_phone_home";
	private final static String PARAM_PHONE_WORK = "caa_phone_work";
    private final static String PARAM_STREET = "caa_street";
    private final static String PARAM_ZIP_CODE = "caa_zip_code";
    private final static String PARAM_CITY = "caa_city";

    private final static String SSN_KEY = "caa_ssn";
    private final static String SSN_DEFAULT
        = "Personnummer (år/månad/dag - fyra sist siffror)";

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

	private void viewUnknownCitizenApplicationForm (IWContext iwc) {
		Form accountForm = new Form();

		Table table = createTable ();
        addSimpleInputs (table, iwc);
        int row = 9;
        addHeader (table, row++, false, PARAM_FIRST_NAME, "Förnamn");
        addSingleInput (table, row++, iwc, PARAM_FIRST_NAME, 40);
        addHeader (table, row++, false, PARAM_LAST_NAME, "Efternamn");
        addSingleInput (table, row++, iwc, PARAM_LAST_NAME, 40);
        addHeader (table, row++, false, PARAM_STREET, "Gatuadress");
        addSingleInput (table, row++, iwc, PARAM_STREET, 40);
        addHeader (table, row++, false, PARAM_ZIP_CODE, "Postnummer");
        addSingleInput (table, row++, iwc, PARAM_ZIP_CODE, 40);
        addHeader (table, row++, false, PARAM_CITY, "Postort");
        addSingleInput (table, row++, iwc, PARAM_CITY, 40);
        addGenderDropdownInput (table, row++, iwc);
        row++;
        final Text custodianHeader1
                = getLocalizedHeader (CUSTODIAN1_KEY, "Vårdnadshavare 1");
        table.add (custodianHeader1, 1, row++);
        addSsnInput (table, row++, iwc, "_custodian1");
        row++;
        addHeader (table, row++, false, PARAM_CUSTODIAN1_CIVIL_STATUS, "Civilstånd");
        addSingleInput (table, row++, iwc, PARAM_CUSTODIAN1_CIVIL_STATUS, 40);

        final Text custodianHeader2
                = getLocalizedHeader (CUSTODIAN2_KEY, "Vårdnadshavare 2");
        table.add (custodianHeader2, 1, row++);
        addSsnInput (table, row++, iwc, "_custodian2");
        row++;
        addHeader (table, row++, false, PARAM_CUSTODIAN2_CIVIL_STATUS, "Civilstånd");
        addSingleInput (table, row++, iwc, PARAM_CUSTODIAN2_CIVIL_STATUS, 40);
        addSubmitButton (table, row, iwc, PARAM_UNKNOWN_CITIZEN_FORM_SUBMIT,
                         "Submit application");

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
		accountForm.add(table);

		add(accountForm);
    }

	private void viewSimpleApplicationForm(IWContext iwc) {
		final Table table = createTable ();
        addSimpleInputs (table, iwc);
        addSubmitButton (table, 9, iwc, PARAM_SIMPLE_FORM_SUBMIT,
                         "Skicka ansökan");
		final Form accountForm = new Form();
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
		accountForm.add(table);

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
		final String name = iwc.getParameter (PARAM_FIRST_NAME) + " "
                + iwc.getParameter (PARAM_LAST_NAME);
        final int genderId
                = new Integer (iwc.getParameter (PARAM_GENDER)).intValue ();
		final String pid = iwc.getParameter (PARAM_PID);
		final String phoneHome = iwc.getParameter (PARAM_PHONE_HOME);
		final String email = iwc.getParameter (PARAM_EMAIL);
		final String phoneWork = iwc.getParameter (PARAM_PHONE_WORK);
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
                    (name, genderId, ssn, birthDate.getTime (), email,
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


    private void addSimpleInputs (final Table table, final IWContext iwc) {
        addSsnInput (table, 1, iwc, "");
        addHeader (table, 3, _isEmailError, PARAM_EMAIL, "E-post");
        addSingleInput (table, 4, iwc, PARAM_EMAIL, 40);
        addHeader (table, 5, _isPhoneHomeError, PARAM_PHONE_HOME, "Telefon (hem)");
        addSingleInput (table, 6, iwc, PARAM_PHONE_HOME, 20);
        addHeader (table, 7, false, PARAM_PHONE_WORK,
                   "Telefon (jobb/mobil)");
        addSingleInput (table, 8, iwc, PARAM_PHONE_WORK, 20);
    }

    private Table createTable () {
        final Table table = new Table ();
		table.setCellspacing (2);
		table.setCellpadding (4);
		table.setColor (getBackgroundColor ());
        return table;
    }

    private void addSsnInput (final Table table, final int row,
                              final IWContext iwc, final String paramPostfix) {
        final String subject = localize (SSN_KEY, SSN_DEFAULT);
        table.add (getSmallText (subject), 1, row);
        final Calendar rightNow = Calendar.getInstance();
        final int currentYear = rightNow.get (Calendar.YEAR);
        addDropdownInput (table, row + 1, iwc, PARAM_BIRTH_YEAR + paramPostfix,
                          currentYear - 110, currentYear - 18);
        table.add (new Text (" / "), 1, row + 1);
        addDropdownInput (table, row + 1, iwc, PARAM_BIRTH_MONTH + paramPostfix,
                          12, 1);
        table.add (new Text (" / "), 1, row + 1);
        addDropdownInput (table, row + 1, iwc, PARAM_BIRTH_DAY + paramPostfix,
                          31, 1);
        table.add (new Text (" - "), 1, row + 1);
        addSingleInput (table, row + 1, iwc, PARAM_PID + paramPostfix, 4);
    }

    private void addDropdownInput
        (final Table table, final int row, final IWContext iwc,
         final String paramId, final int startId, final int stopId) {
        final boolean forward = startId < stopId;
        final DropdownMenu dropdown = new DropdownMenu (paramId);
        int i = startId;
        while (forward ? i <= stopId : i >= stopId) {
            final String iAsString = new Integer (i).toString ();
            dropdown.addMenuElementFirst (iAsString, iAsString);
            i += (forward ? 1 : -1);
        }
		if (iwc.isParameterSet (paramId)) {
		   dropdown.setSelectedElement (iwc.getParameter (paramId));
        }
        table.add (dropdown, 1, row);
    }
                                   
    private void addGenderDropdownInput
        (final Table table, final int row, final IWContext iwc) {
        final String subject = localize (PARAM_GENDER, "Kön");
        final Text text = getSmallText (subject);
        table.add (text, 1, row);
        try {
            final CitizenAccountBusiness business
                    = (CitizenAccountBusiness) IBOLookup.getServiceInstance
                    (iwc, CitizenAccountBusiness.class);
            final Gender [] genders = business.getGenders ();
            final DropdownMenu dropdown = new DropdownMenu (PARAM_GENDER);
            for (int i = 0; i < genders.length; i++) {
                final String nameInDb = genders [i].getName ();
                final String name = localize ("caa_" + nameInDb, nameInDb);
                final String id = genders [i].getPrimaryKey ().toString ();
                dropdown.addMenuElementFirst (id, name);
            }
            if (iwc.isParameterSet (PARAM_GENDER)) {
                dropdown.setSelectedElement (iwc.getParameter (PARAM_GENDER));
            }
            table.add (dropdown, 1, row + 1);
        } catch (RemoteException e) {
            e.printStackTrace ();
        }
    }
                                   
    private void addSingleInput
        (final Table table, final int row ,final IWContext iwc,
         final String paramId, final int maxLength) {
        final TextInput textInput = new TextInput (paramId);
		textInput.setMaxlength (maxLength);
		textInput.setStyleClass (getSmallTextFontStyle ());
		final String param = iwc.getParameter (paramId);
		if (param != null) {
			textInput.setContent (param);
        }
        table.add (textInput, 1, row);
    }
    
    private void addHeader
        (final Table table, final int row, final boolean isError,
         final String paramId, final String defaultText) {
        final String header = localize (paramId, defaultText);
        final Text text = isError ? getSmallErrorText (header)
                : getSmallText (header);
        table.add (text, 1, row);
    }


    private void addSubmitButton (final Table table, final int row,
                                  final IWContext iwc, final String submitId,
                                  final String defaultText) {
		table.setAlignment (1, row, "right");
        final String text = localize (submitId, defaultText);
		final SubmitButton submitButton = new SubmitButton
                (getBundle(iwc).getImageButton (text), submitId);
		submitButton.setStyleClass (getLinkFontStyle());
		table.add (submitButton, 1, row);
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
