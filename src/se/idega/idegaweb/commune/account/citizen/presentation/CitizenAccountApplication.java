/*
 * $Id: CitizenAccountApplication.java,v 1.12 2002/11/06 07:56:12 staffan Exp $
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

	private final static String FIRST_NAME_KEY = "caa_first_name";
    private final static String FIRST_NAME_DEFAULT = "Förnamn";
	private final static String LAST_NAME_KEY = "caa_last_name";
    private final static String LAST_NAME_DEFAULT = "Efternamn";
	private final static String BIRTH_YEAR_KEY = "caa_birth_year";
	private final static String BIRTH_MONTH_KEY = "caa_birth_month";
	private final static String BIRTH_DAY_KEY = "caa_birth_day";
	private final static String GENDER_KEY = "caa_gender";
    private final static String GENDER_DEFAULT = "Kön";
	private final static String EMAIL_KEY = "caa_email";
    private final static String EMAIL_DEFAULT = "E-post";
	private final static String PHONE_HOME_KEY = "caa_phone_home";
    private final static String PHONE_HOME_DEFAULT = "Telefon (hem)";
	private final static String PHONE_WORK_KEY = "caa_phone_work";
    private final static String PHONE_WORK_DEFAULT = "Telefon (arbete/mobil)";
    private final static String STREET_KEY = "caa_street";
    private final static String STREET_DEFAULT = "Gatuadress";
    private final static String ZIP_CODE_KEY = "caa_zip_code";
    private final static String ZIP_CODE_DEFAULT = "Postnummer";
    private final static String CITY_KEY = "caa_city";
    private final static String CITY_DEFAULT = "Postort";
    private final static String CIVIL_STATUS_KEY = "caa_civil_status";
    private final static String CIVIL_STATUS_DEFAULT = "Civilstånd";
    private final static String SSN_KEY = "caa_ssn";
    private final static String SSN_DEFAULT
        = "Personnummer (år/månad/dag - fyra sist siffror)";
    private final static String CUSTODIAN_KEY = "caa_custodian";
    private final static String CUSTODIAN_DEFAULT = "Vårdnadshavare";
    private final static String PID_KEY = "caa_pid";

	private final static String SIMPLE_FORM_SUBMIT_KEY = "caa_simpleSubmit";
	private final static String SIMPLE_FORM_SUBMIT_DEFAULT = "Skicka ansökan";
	private final static String UNKNOWN_CITIZEN_FORM_SUBMIT_KEY
        = "caa_unknownCitizenSubmit";
	private final static String UNKNOWN_CITIZEN_FORM_SUBMIT_DEAFULT
        = "Skicka ansökan";

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
        addHeader (table, row++, false, FIRST_NAME_KEY, FIRST_NAME_DEFAULT);
        addSingleInput (table, row++, iwc, FIRST_NAME_KEY, 40);
        addHeader (table, row++, false, LAST_NAME_KEY, LAST_NAME_DEFAULT);
        addSingleInput (table, row++, iwc, LAST_NAME_KEY, 40);
        addHeader (table, row++, false, STREET_KEY, STREET_DEFAULT);
        addSingleInput (table, row++, iwc, STREET_KEY, 40);
        addHeader (table, row++, false, ZIP_CODE_KEY, ZIP_CODE_DEFAULT);
        addSingleInput (table, row++, iwc, ZIP_CODE_KEY, 40);
        addHeader (table, row++, false, CITY_KEY, CITY_DEFAULT);
        addSingleInput (table, row++, iwc, CITY_KEY, 40);
        addGenderDropdownInput (table, row++, iwc);
        row++;
        addCustodianInput (table, row++, iwc, 1);
        row += 4;
        addCustodianInput (table, row++, iwc, 2);
        row += 4;
        addSubmitButton (table, row, iwc, UNKNOWN_CITIZEN_FORM_SUBMIT_KEY,
                         UNKNOWN_CITIZEN_FORM_SUBMIT_DEAFULT );

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
        addSubmitButton (table, 9, iwc, SIMPLE_FORM_SUBMIT_KEY,
                         SIMPLE_FORM_SUBMIT_DEFAULT);
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
		String pidString = iwc.getParameter (PID_KEY);
		String phoneHomeString = iwc.getParameter (PHONE_HOME_KEY);
		String emailString = iwc.getParameter (EMAIL_KEY);
		String phoneWorkString = iwc.getParameter (PHONE_WORK_KEY);

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
		final String name = iwc.getParameter (FIRST_NAME_KEY) + " "
                + iwc.getParameter (LAST_NAME_KEY);
        final int genderId
                = new Integer (iwc.getParameter (GENDER_KEY)).intValue ();
		final String pid = iwc.getParameter (PID_KEY);
		final String phoneHome = iwc.getParameter (PHONE_HOME_KEY);
		final String email = iwc.getParameter (EMAIL_KEY);
		final String phoneWork = iwc.getParameter (PHONE_WORK_KEY);
        final String custodian1CivilStatus
                = iwc.getParameter (getCustodianKey (CIVIL_STATUS_KEY, 1));
        final String custodian1Pid
                = iwc.getParameter (getCustodianKey (PID_KEY, 1));
        final String custodian2CivilStatus
                = iwc.getParameter (getCustodianKey (CIVIL_STATUS_KEY, 2));
        final String custodian2Pid
                = iwc.getParameter (getCustodianKey (PID_KEY, 2));
        final String street = iwc.getParameter (STREET_KEY);
        final String zipCode = iwc.getParameter (ZIP_CODE_KEY);
        final String city = iwc.getParameter (CITY_KEY);

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
                                          (BIRTH_YEAR_KEY)).intValue ();
            final int month = new Integer (iwc.getParameter
                                          (BIRTH_MONTH_KEY)).intValue ();
            final int day = new Integer (iwc.getParameter
                                          (BIRTH_DAY_KEY)).intValue ();
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

    private void addCustodianInput
        (final Table table, final int row,
         final IWContext iwc, final int custodianId) {
        final String custodianKey = CUSTODIAN_KEY + "_" + custodianId;
        final String custodianDefault = CUSTODIAN_DEFAULT + " " + custodianId;
        final Text custodianHeader = getLocalizedHeader (custodianKey,
                                                         custodianDefault);
        table.add (custodianHeader, 1, row);
        addSsnInput (table, row + 1, iwc, "_" + custodianKey);
        addHeader (table, row + 3, false, CIVIL_STATUS_KEY,
                   CIVIL_STATUS_DEFAULT);
        final String civilStatusKey = getCustodianKey (CIVIL_STATUS_KEY,
                                                       custodianId);
        addSingleInput (table, row + 4, iwc, civilStatusKey, 40);
    }

    private void addSimpleInputs (final Table table, final IWContext iwc) {
        addSsnInput (table, 1, iwc, "");
        addHeader (table, 3, _isEmailError, EMAIL_KEY, EMAIL_DEFAULT);
        addSingleInput (table, 4, iwc, EMAIL_KEY, 40);
        addHeader (table, 5, _isPhoneHomeError, PHONE_HOME_KEY,
                   PHONE_HOME_DEFAULT);
        addSingleInput (table, 6, iwc, PHONE_HOME_KEY, 20);
        addHeader (table, 7, false, PHONE_WORK_KEY, PHONE_WORK_DEFAULT);
        addSingleInput (table, 8, iwc, PHONE_WORK_KEY, 20);
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
        addDropdownInput (table, row + 1, iwc, BIRTH_YEAR_KEY + paramPostfix,
                          currentYear - 110, currentYear - 18);
        table.add (new Text (" / "), 1, row + 1);
        addDropdownInput (table, row + 1, iwc, BIRTH_MONTH_KEY + paramPostfix,
                          12, 1);
        table.add (new Text (" / "), 1, row + 1);
        addDropdownInput (table, row + 1, iwc, BIRTH_DAY_KEY + paramPostfix,
                          31, 1);
        table.add (new Text (" - "), 1, row + 1);
        addSingleInput (table, row + 1, iwc, PID_KEY + paramPostfix, 4);
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
        final String subject = localize (GENDER_KEY, GENDER_DEFAULT);
        final Text text = getSmallText (subject);
        table.add (text, 1, row);
        try {
            final CitizenAccountBusiness business
                    = (CitizenAccountBusiness) IBOLookup.getServiceInstance
                    (iwc, CitizenAccountBusiness.class);
            final Gender [] genders = business.getGenders ();
            final DropdownMenu dropdown = new DropdownMenu (GENDER_KEY);
            for (int i = 0; i < genders.length; i++) {
                final String nameInDb = genders [i].getName ();
                final String name = localize ("caa_" + nameInDb, nameInDb);
                final String id = genders [i].getPrimaryKey ().toString ();
                dropdown.addMenuElementFirst (id, name);
            }
            if (iwc.isParameterSet (GENDER_KEY)) {
                dropdown.setSelectedElement (iwc.getParameter (GENDER_KEY));
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
        final String year = iwc.getParameter (BIRTH_YEAR_KEY);
        final String month = iwc.getParameter (BIRTH_MONTH_KEY);
        final String day = iwc.getParameter (BIRTH_DAY_KEY);
        final String pid = iwc.getParameter (PID_KEY);
        final String ssn = year + (month.length () > 1 ? month : "0" + month)
                + (day.length () > 1 ? day : "0" + day) + pid;
        return ssn;
    }

    private String getCustodianKey (final String key, final int custodianId) {
        return key + "_" + CUSTODIAN_KEY + "_" + custodianId;
    }
    
	private void addErrorString(String errorString) {
		if (_errorMsg == null)
			_errorMsg = new Vector();

		_errorMsg.add(errorString);
	}

	private int parseAction (IWContext iwc) {
		int action = ACTION_VIEW_FORM;

		if (iwc.isParameterSet(SIMPLE_FORM_SUBMIT_KEY)) {
			action = ACTION_SUBMIT_SIMPLE_FORM;
		} else if (iwc.isParameterSet (UNKNOWN_CITIZEN_FORM_SUBMIT_KEY)) {
            action = ACTION_SUBMIT_UNKNOWN_CITIZEN_FORM;
        }

		return action;
	}
}
