/*
 * $Id: CitizenAccountApplication.java,v 1.18 2002/11/12 13:01:00 staffan Exp $
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
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version 1.0
 */
public class CitizenAccountApplication extends CommuneBlock {
	private final static int ACTION_VIEW_FORM = 0;
	private final static int ACTION_SUBMIT_SIMPLE_FORM = 1;
	private final static int ACTION_SUBMIT_UNKNOWN_CITIZEN_FORM_1 = 2;
	private final static int ACTION_SUBMIT_UNKNOWN_CITIZEN_FORM_2 = 3;

 
    private final static String APPLICATION_REASON_DEFAULT = "Orsak till ansökan om medborgarkonto?";
    private final static String APPLICATION_REASON_KEY = "caa_application_reason";
    private final static String CHILDREN_COUNT_DEFAULT = "Antal barn i familjen?";
    private final static String CHILDREN_COUNT_KEY = "caa_children_count";
    private final static String CITY_DEFAULT = "Postort";
    private final static String CITY_KEY = "caa_city";
    private final static String CIVIL_STATUS_DEFAULT = "Civilstånd";
    private final static String CIVIL_STATUS_KEY = "caa_civil_status";
    private final static String CUSTODIAN_DEFAULT = "Vårdnadshavare";
    private final static String CUSTODIAN_KEY = "caa_custodian";
    private final static String EMAIL_DEFAULT = "E-post";
    private final static String EMAIL_KEY = "caa_email";
    private final static String FIRST_NAME_DEFAULT = "Förnamn";
    private final static String FIRST_NAME_KEY = "caa_first_name";
    private final static String GENDER_DEFAULT = "Kön";
    private final static String GENDER_KEY = "caa_gender";
    private final static String HAS_COHABITANT_DEFAULT = "Är du sammanboende?";
    private final static String HAS_COHABITANT_KEY = "caa_has_cohabitant";
    private final static String LAST_NAME_DEFAULT = "Efternamn";
    private final static String LAST_NAME_KEY = "caa_last_name";
    private final static String MOVING_TO_NACKA_DEFAULT = "Jag flyttar till Nacka kommun";
    private final static String MOVING_TO_NACKA_KEY = "caa_moving_to_nacka";
    private final static String NO_DEFAULT = "Nej";
    private final static String NO_KEY = "caa_no";
    private final static String PHONE_HOME_DEFAULT = "Telefon (hem)";
    private final static String PHONE_HOME_KEY = "caa_phone_home";
    private final static String PHONE_WORK_DEFAULT = "Telefon (arbete/mobil)";
    private final static String PHONE_WORK_KEY = "caa_phone_work";
    private final static String PUT_CHILDREN_IN_NACKA_DEFAULT = "Jag vill placera barn i barnomsorg/skola i Nacka kommun";
    private final static String PUT_CHILDREN_IN_NACKA_KEY = "caa_put_children_in_nacka";
    private final static String SSN_DEFAULT = "Personnummer";
    private final static String SSN_KEY = "caa_ssn";
    private final static String STREET_DEFAULT = "Gatuadress";
    private final static String STREET_KEY = "caa_street";
   private final static String UNKNOWN_CITIZEN_DEFAULT
        = "Du finns inte registrerad som medborgare i Nacka. Du har ändå"
        + " möjlighet att registrera dig för ett användarkonto om du har"
        + " planerat att flytta till Nacka eller vill att ditt barn ska gå i"
        + " skolan i kommunen. Följ instruktionerna nedan.";
    private final static String UNKNOWN_CITIZEN_KEY = "caa_unknown_citizen";
    private final static String YES_DEFAULT = "Ja";
    private final static String YES_KEY = "caa_yes";
    private final static String ZIP_CODE_DEFAULT = "Postnummer";
    private final static String ZIP_CODE_KEY = "caa_zip_code";

	private final static String SIMPLE_FORM_SUBMIT_KEY = "caa_simpleSubmit";
	private final static String SIMPLE_FORM_SUBMIT_DEFAULT = "Skicka ansökan";
	private final static String UNKNOWN_CITIZEN_FORM_1_SUBMIT_KEY
        = "caa_unknownCitizenSubmit1";
	private final static String UNKNOWN_CITIZEN_FORM_1_SUBMIT_DEAFULT
        = "Fortsätt";
	private final static String UNKNOWN_CITIZEN_FORM_2_SUBMIT_KEY
        = "caa_unknownCitizenSubmit2";
	private final static String UNKNOWN_CITIZEN_FORM_SUBMIT_2_DEAFULT
        = "Skicka ansökan";

	private final static String ERROR_NOT_EMAIL_DEFAULT
        = "Felaktigt inmatad e-postadress";
	private final static String ERROR_NOT_EMAIL_KEY = "caa_err_email";
	private final static String ERROR_PHONE_HOME_DEFAULT
        = "Felaktigt inmatat hemtelefonnummer";
	private final static String ERROR_PHONE_HOME_KEY = "caa_error_phone_home";
	private final static String ERROR_SSN_DEFAULT
        = "Felaktigt inmatat personnummer (ååååmmddxxxx)";
	private final static String ERROR_SSN_KEY = "caa_ssn_error";
    private final static String ERROR_NO_INSERT_DEFAULT
        = "Kunde inte lagra ansökan."; 
    private final static String ERROR_NO_INSERT_KEY = "caa_unable_to_insert";
	private final static String TEXT_APPLICATION_SUBMITTED_DEFAULT
        = "Ansökan är mottagen.";
	private final static String TEXT_APPLICATION_SUBMITTED_KEY
        = "caa_app_submitted";

	private boolean _isSsnError = false;
	private boolean _isPhoneHomeError = false;
	private boolean _isEmailError = false;
	private boolean _isError = false;
	private Vector _errorMsg = null;

	public void main (final IWContext iwc) {
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
				case ACTION_SUBMIT_UNKNOWN_CITIZEN_FORM_1:
					submitUnknownCitizenForm1(iwc);
					break;
				case ACTION_SUBMIT_UNKNOWN_CITIZEN_FORM_2:
					submitUnknownCitizenForm2(iwc);
					break;
			}
		}
		catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}    
	}

	private void viewSimpleApplicationForm (final IWContext iwc) {
		final Table table = createTable (this);
        addSimpleInputs (this, table, iwc, _isSsnError, _isEmailError,
                         _isPhoneHomeError);
        addSubmitButton (this, table, 9, iwc, SIMPLE_FORM_SUBMIT_KEY,
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
		String ssn = getSsn (iwc);
		String phoneHome = iwc.getParameter (PHONE_HOME_KEY);
		String email = iwc.getParameter (EMAIL_KEY);
		String phoneWork = iwc.getParameter (PHONE_WORK_KEY);

		_errorMsg = null;

		if (ssn == null || ssn.equals("")) {
			_isSsnError = true;
			_isError = true;
			addErrorString(localize(ERROR_SSN_KEY, ERROR_SSN_DEFAULT));
		}

		if (email == null || email.equals("")) {
			_isEmailError = true;
			_isError = true;
			addErrorString(localize(ERROR_NOT_EMAIL_KEY,
                                    ERROR_NOT_EMAIL_DEFAULT));
		}

		if (phoneHome == null || phoneHome.equals("")) {
			_isPhoneHomeError = true;
			_isError = true;
			addErrorString(localize(ERROR_PHONE_HOME_KEY,
                                    ERROR_PHONE_HOME_DEFAULT));
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
            final User user = business.getUser (ssn);
            if (user == null) {
                viewUnknownCitizenApplicationForm1 (iwc);
                return;
            }
			isInserted = business.insertApplication
                    (user, ssn, email, phoneHome, phoneWork);
		}
		catch (Exception e) {
			e.printStackTrace();
			isInserted = false;
		}

		if (!isInserted) {
			_isError = true;
			addErrorString(localize(ERROR_NO_INSERT_KEY,
                                    ERROR_NO_INSERT_DEFAULT));
			viewSimpleApplicationForm(iwc);
			return;
		}

		if (getResponsePage() != null)
			iwc.forwardToIBPage(getParentPage(), getResponsePage());
		else
			add(new Text(localize(TEXT_APPLICATION_SUBMITTED_KEY,
                                  "Application submitted")));
	}

	private void viewUnknownCitizenApplicationForm1 (final IWContext iwc) {
		Table table = createTable (this);
        addSimpleInputs (this, table, iwc, _isSsnError, _isEmailError,
                         _isPhoneHomeError);
        int row = 5;
        addHeader (this, table, 1, row, false,
                   FIRST_NAME_KEY, FIRST_NAME_DEFAULT);
        addHeader (this, table, 2, row++, false,
                   LAST_NAME_KEY, LAST_NAME_DEFAULT);
        addSingleInput (this, table, 1, row, iwc, FIRST_NAME_KEY, 40);
        addSingleInput (this, table, 2, row++, iwc, LAST_NAME_KEY, 40);
        addHeader (this, table, 1, row++, false, STREET_KEY, STREET_DEFAULT);
        addSingleInput (this, table, 1, row++, iwc, STREET_KEY, 40);
        addHeader (this, table, 1, row, false, ZIP_CODE_KEY, ZIP_CODE_DEFAULT);
        addHeader (this, table, 2, row++, false, CITY_KEY, CITY_DEFAULT);
        addSingleInput (this, table, 1, row, iwc, ZIP_CODE_KEY, 40);
        addSingleInput (this, table, 2, row++, iwc, CITY_KEY, 40);
        addGenderDropdownInput (this, table, row++, iwc);
        row++;
        table.mergeCells (1, row, 2, row);
        addHeader (this, table, 1, row++, false, HAS_COHABITANT_KEY,
                   HAS_COHABITANT_DEFAULT);
        final RadioGroup hasCohabitant = new RadioGroup (HAS_COHABITANT_KEY);
        addRadioInput (this, iwc, hasCohabitant, YES_KEY, YES_DEFAULT);
        addRadioInput (this, iwc, hasCohabitant, NO_KEY, NO_DEFAULT);
        hasCohabitant.setSelected (YES_KEY);
        table.add (hasCohabitant, 1, row++);
        table.mergeCells (1, row, 2, row);
        addHeader (this, table, 1, row++, false, CHILDREN_COUNT_KEY,
                   CHILDREN_COUNT_DEFAULT);
        addSingleInput (this, table, 1, row++, iwc, CHILDREN_COUNT_KEY,
                        2);
        table.mergeCells (1, row, 2, row);
        addHeader (this, table, 1, row++, false, APPLICATION_REASON_KEY,
                   APPLICATION_REASON_DEFAULT);
        final RadioGroup applicationReason
                = new RadioGroup (APPLICATION_REASON_KEY);
        table.mergeCells (1, row, 2, row);
        addRadioInput (this, iwc, applicationReason,
                       MOVING_TO_NACKA_KEY, MOVING_TO_NACKA_DEFAULT);
        table.mergeCells (1, row, 2, row);
        addRadioInput (this, iwc, applicationReason, PUT_CHILDREN_IN_NACKA_KEY,
                       PUT_CHILDREN_IN_NACKA_DEFAULT);
        applicationReason.setSelected (MOVING_TO_NACKA_KEY);
        table.add (applicationReason, 1, row++);
        addSubmitButton (this, table, row, iwc,
                         UNKNOWN_CITIZEN_FORM_1_SUBMIT_KEY,
                         UNKNOWN_CITIZEN_FORM_1_SUBMIT_DEAFULT );
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
        final Text text = new Text (localize (UNKNOWN_CITIZEN_KEY,
                                              UNKNOWN_CITIZEN_DEFAULT));
        add (text);
		accountForm.add(table);
		add(accountForm);
    }

    private void submitUnknownCitizenForm1 (final IWContext iwc) {
		final String name = iwc.getParameter (FIRST_NAME_KEY) + " "
                + iwc.getParameter (LAST_NAME_KEY);
        final int genderId
                = new Integer (iwc.getParameter (GENDER_KEY)).intValue ();
		final String ssn = getSsn (iwc);
		final String phoneHome = iwc.getParameter (PHONE_HOME_KEY);
		final String email = iwc.getParameter (EMAIL_KEY);
		final String phoneWork = iwc.getParameter (PHONE_WORK_KEY);
        final String custodian1Ssn
                = getSsn (iwc, "_" + CUSTODIAN_KEY + "_" + 1);
        final String custodian1CivilStatus
                = iwc.getParameter (getCustodianKey (CIVIL_STATUS_KEY, 1));
        final String custodian2Ssn
                = getSsn (iwc, "_" + CUSTODIAN_KEY + "_" + 2);
        final String custodian2CivilStatus
                = iwc.getParameter (getCustodianKey (CIVIL_STATUS_KEY, 2));
        final String street = iwc.getParameter (STREET_KEY);
        final String zipCode = iwc.getParameter (ZIP_CODE_KEY);
        final String city = iwc.getParameter (CITY_KEY);

		_errorMsg = null;

		if (ssn == null) {
			_isSsnError = true;
			_isError = true;
			addErrorString (localize (ERROR_SSN_KEY, ERROR_SSN_DEFAULT));
		}

		if (email == null || email.equals("")) {
			_isEmailError = true;
			_isError = true;
			addErrorString (localize (ERROR_NOT_EMAIL_KEY,
                                      ERROR_NOT_EMAIL_DEFAULT));
		}

		if (phoneHome == null || phoneHome.equals("")) {
			_isPhoneHomeError = true;
			_isError = true;
			addErrorString (localize (ERROR_PHONE_HOME_KEY,
                                      ERROR_PHONE_HOME_DEFAULT));
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
            final User user = business.getUser (ssn);
            if (user != null) {
                viewSimpleApplicationForm (iwc);
                return;
            }
            final Calendar birth = Calendar.getInstance ();
            birth.set (new Integer (ssn.substring (0, 4)).intValue (),
                       new Integer (ssn.substring (4, 6)).intValue () - 1,
                       new Integer (ssn.substring (6, 8)).intValue ());
			isInserted = business.insertApplication
                    (name, genderId, ssn, birth.getTime (), email,
                     phoneHome, phoneWork,
                     custodian1Ssn, custodian1CivilStatus, custodian2Ssn,
                     custodian2CivilStatus, street, zipCode, city);
		}
		catch (Exception e) {
			e.printStackTrace();
			isInserted = false;
		}

		if (!isInserted) {
			_isError = true;
			addErrorString(localize(ERROR_NO_INSERT_KEY,
                                    ERROR_NO_INSERT_DEFAULT));
			viewUnknownCitizenApplicationForm1 (iwc);
			return;
		}
        
		if (getResponsePage() != null)
			iwc.forwardToIBPage(getParentPage(), getResponsePage());
		else
			add(new Text(localize(TEXT_APPLICATION_SUBMITTED_KEY,
                                  TEXT_APPLICATION_SUBMITTED_DEFAULT)));
    }

    private void submitUnknownCitizenForm2 (final IWContext iwc) {
        throw new UnsupportedOperationException ();
    }

    private static Table createTable (final CommuneBlock communeblock) {
        final Table table = new Table ();
		table.setCellspacing (2);
		table.setCellpadding (4);
		table.setColor (communeblock.getBackgroundColor ());
        return table;
    }
                                   
    private static void addSimpleInputs (final CommuneBlock communeBlock,
                                         final Table table,
                                         final IWContext iwc,
                                         boolean isSsnError,
                                         boolean isEmailError,
                                         boolean isPhoneHomeError) {
        addHeader (communeBlock, table, 1, 1, isSsnError, SSN_KEY, SSN_DEFAULT);
        addSingleInput (communeBlock, table, 1, 2, iwc, SSN_KEY, 12);
        addHeader (communeBlock, table, 2, 1, isEmailError,
                   EMAIL_KEY, EMAIL_DEFAULT);
        addSingleInput (communeBlock, table, 2, 2, iwc, EMAIL_KEY, 40);
        addHeader (communeBlock, table, 1, 3, isPhoneHomeError, PHONE_HOME_KEY,
                   PHONE_HOME_DEFAULT);
        addSingleInput (communeBlock, table, 1, 4, iwc, PHONE_HOME_KEY, 20);
        addHeader (communeBlock, table, 2, 3, false,
                   PHONE_WORK_KEY, PHONE_WORK_DEFAULT);
        addSingleInput (communeBlock, table, 2, 4, iwc, PHONE_WORK_KEY, 20);
    }

    private static void addGenderDropdownInput
        (final CommuneBlock communeBlock, final Table table, final int row,
         final IWContext iwc) {
        final String subject = communeBlock.localize (GENDER_KEY,
                                                      GENDER_DEFAULT);
        final Text text = communeBlock.getSmallText (subject);
        table.add (text, 1, row);
        try {
            final CitizenAccountBusiness business
                    = (CitizenAccountBusiness) IBOLookup.getServiceInstance
                    (iwc, CitizenAccountBusiness.class);
            final Gender [] genders = business.getGenders ();
            final DropdownMenu dropdown = new DropdownMenu (GENDER_KEY);
            for (int i = 0; i < genders.length; i++) {
                final String nameInDb = genders [i].getName ();
                final String name = communeBlock.localize ("caa_" + nameInDb,
                                                           nameInDb);
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
                                   
    private static void addRadioInput
        (final CommuneBlock communeBlock, final IWContext iwc,
         final RadioGroup group, final String paramId,
         final String defaultText) {
        final String name = communeBlock.localize (paramId, defaultText);
        final RadioButton button = new RadioButton (name, paramId);
        group.addRadioButton (button, communeBlock.getSmallText (name));
    }

    private static void addSingleInput
        (final CommuneBlock communeBlock, final Table table,
         final int col, final int row ,final IWContext iwc,
         final String paramId, final int maxLength) {
        final TextInput textInput = new TextInput (paramId);
		textInput.setMaxlength (maxLength);
		textInput.setStyleClass (communeBlock.getSmallTextFontStyle ());
		final String param = iwc.getParameter (paramId);
		if (param != null) {
			textInput.setContent (param);
        }
        table.add (textInput, col, row);
    }
    
    private static void addHeader
        (final CommuneBlock communeBlock, final Table table,
         final int col, final int row, final boolean isError,
         final String paramId, final String defaultText) {
        final String header = communeBlock.localize (paramId, defaultText);
        final Text text = isError ? communeBlock.getSmallErrorText (header)
                : communeBlock.getSmallText (header);
        table.add (text, col, row);
    }

    private static void addSubmitButton
        (final CommuneBlock communeBlock, final Table table, final int row,
         final IWContext iwc, final String submitId, final String defaultText) {
		table.setAlignment (2, row, "right");
        final String text = communeBlock.localize (submitId, defaultText);
		final SubmitButton submitButton = new SubmitButton
                (communeBlock.getBundle(iwc).getImageButton (text), submitId);
		submitButton.setStyleClass (communeBlock.getLinkFontStyle());
		table.add (submitButton, 2, row);
    }

    private static String getSsn (final IWContext iwc) {
        return getSsn (iwc, "");
    }

    private static String getSsn (final IWContext iwc,
                                  final String paramPostfix) {
        final String rawInput = iwc.getParameter (SSN_KEY + paramPostfix);
        final StringBuffer digitOnlyInput = new StringBuffer ();
        for (int i = 0; i < rawInput.length (); i++) {
            if (Character.isDigit (rawInput.charAt (i))) {
                digitOnlyInput.append (rawInput.charAt (i));
            }
        }
        final Calendar rightNow = Calendar.getInstance();
        final int currentYear = rightNow.get (Calendar.YEAR);
        if (digitOnlyInput.length () == 10) {
            final int inputYear = new Integer
                    (digitOnlyInput.substring (0, 2)).intValue ();
            final int century = inputYear + 2000 > currentYear ? 19 : 20;
            digitOnlyInput.insert (0, century);
        }
        if (digitOnlyInput.length () != 12) {
            return null;
        }
        final int year = new Integer
                (digitOnlyInput.substring (0, 4)).intValue ();
        final int month = new Integer
                (digitOnlyInput.substring (4, 6)).intValue ();
        final int day = new Integer
                (digitOnlyInput.substring (6, 8)).intValue ();
        if (year < 1880 || year > currentYear || month < 1 || month > 12
            || day < 1 || day > 31) {
            return null;
        }
        return digitOnlyInput.toString ();
    }

    private static String getCustodianKey (final String key,
                                           final int custodianId) {
        return key + "_" + CUSTODIAN_KEY + "_" + custodianId;
    }
    
	private void addErrorString(String errorString) {
		if (_errorMsg == null)
			_errorMsg = new Vector();

		_errorMsg.add(errorString);
	}

	private static int parseAction (final IWContext iwc) {
		int action = ACTION_VIEW_FORM;

		if (iwc.isParameterSet(SIMPLE_FORM_SUBMIT_KEY)) {
			action = ACTION_SUBMIT_SIMPLE_FORM;
		} else if (iwc.isParameterSet (UNKNOWN_CITIZEN_FORM_1_SUBMIT_KEY)) {
            action = ACTION_SUBMIT_UNKNOWN_CITIZEN_FORM_1;
        } else if (iwc.isParameterSet (UNKNOWN_CITIZEN_FORM_2_SUBMIT_KEY)) {
            action = ACTION_SUBMIT_UNKNOWN_CITIZEN_FORM_2;
        }

		return action;
	}

    /*
    private static void addCustodianInput (CommuneBlock communeBlock,
                                           final Table table, final int row,
                                           final IWContext iwc,
                                           final int custodianId) {
        final String custodianKey = CUSTODIAN_KEY + "_" + custodianId;
        final Text custodianHeader = communeBlock.getLocalizedHeader
                (custodianKey, CUSTODIAN_DEFAULT + " " + custodianId);
        table.add (custodianHeader, 1, row);

        addHeader (communeBlock, table, 1, row + 1, false, SSN_KEY,
                   SSN_DEFAULT);
        final String ssnKey = getCustodianKey (SSN_KEY, custodianId);
        addSingleInput (communeBlock, table, 1, row + 2, iwc, ssnKey, 12);
        addHeader (communeBlock, table, 2, row + 1, false, CIVIL_STATUS_KEY,
                   CIVIL_STATUS_DEFAULT);
        final String civilStatusKey
                = getCustodianKey (CIVIL_STATUS_KEY, custodianId);
        addSingleInput (communeBlock, table, 2, row + 2, iwc, civilStatusKey,
                        40);
    }
    */
}
