package se.idega.idegaweb.commune.account.citizen.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.data.Email;
import com.idega.core.data.EmailHome;
import com.idega.core.data.Address;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookup;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.PasswordInput;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import com.idega.user.business.UserBusiness;
import com.idega.util.PersonalIDFormatter;
import com.idega.business.IBOLookup;

import se.idega.idegaweb.commune.presentation.CommuneBlock;

/*
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.core.data.Address;
import com.idega.core.data.Email;
import com.idega.user.data.*;
import com.idega.business.IBOLookup;
import com.idega.user.business.UserBusiness;

import se.idega.idegaweb.commune.presentation.CommuneBlock;
*/

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Anders Lindman
 * @version 1.0
 */
public class CitizenAccountPreferences extends CommuneBlock {
	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";

	private final static int ACTION_VIEW_FORM = 1;
	private final static int ACTION_FORM_SUBMIT = 2;
	private final static int ACTION_CANCEL = 3;

	private final static String PARAMETER_FORM_SUBMIT = "cap_sbmt";
	private final static String PARAMETER_CANCEL = "cap_cncl";
	private final static String PARAMETER_LOGIN = "cap_lgn";
	private final static String PARAMETER_OLD_LOGIN = "cap_old_lgn";
	private final static String PARAMETER_CURRENT_PASSWORD = "cap_c_pw";
	private final static String PARAMETER_NEW_PASSWORD = "cap_n_pw";
	private final static String PARAMETER_NEW_PASSWORD_REPEATED = "cap_n_pw_r";
	private final static String PARAMETER_EMAIL = "cap_email";
	private final static String PARAMETER_EMAIL_ID = "cap_email_id";
	private final static String PARAMETER_PHONE_HOME = "cap_phn_h";
	private final static String PARAMETER_PHONE_WORK = "cap_phn_h";
	private final static String PARAMETER_CO_STREET_ADDRESS = "cap_co_sa";
	private final static String PARAMETER_CO_POSTAL_CODE = "cap_co_pc";
	private final static String PARAMETER_CO_CITY = "cap_co_ct";

	private final int MIN_PASSWORD_LENGTH = 6;
	
	private final static String KEY_PREFIX = "citizen.";
	private final static String KEY_EMAIL = KEY_PREFIX + "email";
	private final static String KEY_LOGIN = KEY_PREFIX + "login";
	private final static String KEY_OLD_LOGIN = KEY_PREFIX + "old_login";
	private final static String KEY_CURRENT_PASSWORD = KEY_PREFIX + "current_password";
	private final static String KEY_NEW_PASSWORD = KEY_PREFIX + "new_password";
	private final static String KEY_NEW_PASSWORD_REPEATED = KEY_PREFIX + "new_password_repeated";
	private final static String KEY_UPDATE = KEY_PREFIX + "update";
	private final static String KEY_CANCEL = KEY_PREFIX + "cancel";
	private final static String KEY_NAME = KEY_PREFIX + "name";
	private final static String KEY_ADDRESS = KEY_PREFIX + "address";
	private final static String KEY_PHONE_HOME = KEY_PREFIX + "phone_home";
	private final static String KEY_PHONE_WORK = KEY_PREFIX + "phone_work";
	private final static String KEY_CO_ADDRESS_SELECT = KEY_PREFIX + "co_address_select";
	private final static String KEY_CO_STREET_ADDRESS = KEY_PREFIX + "co_street_address";
	private final static String KEY_CO_POSTAL_CODE = KEY_PREFIX + "co_postal_code";
	private final static String KEY_CO_CITY = KEY_PREFIX + "co_city";
	private final static String KEY_MESSAGES_VIA_EMAIL = KEY_PREFIX + "messages_via_email";
	private final static String KEY_PASSWORD_EMPTY = KEY_PREFIX + "password_empty";
	private final static String KEY_PASSWORDS_NOT_SAME = KEY_PREFIX + "passwords_not_same";
	private final static String KEY_PASSWORD_INVALID = KEY_PREFIX + "invalid_password";	
	private final static String KEY_PASSWORD_TOO_SHORT = KEY_PREFIX + "password_too_short";	
	private final static String KEY_PASSWORD_CHAR_ILLEGAL = KEY_PREFIX + "password_char_illegal";	
	private final static String KEY_EMAIL_INVALID = KEY_PREFIX + "email_invalid";	
	private final static String KEY_EMAIL_EMPTY = KEY_PREFIX + "email_empty";	

	private final static String DEFAULT_EMAIL = "E-mail";	
	private final static String DEFAULT_LOGIN = "Login";	
	private final static String DEFAULT_OLD_LOGIN = "Old login";	
	private final static String DEFAULT_CURRENT_PASSWORD = "Current password";	
	private final static String DEFAULT_NEW_PASSWORD = "New password";	
	private final static String DEFAULT_NEW_PASSWORD_REPEATED = "Repeat new password";	
	private final static String DEFAULT_UPDATE = "Update";	
	private final static String DEFAULT_CANCEL = "Cancel";	
	private final static String DEFAULT_NAME = "Name";	
	private final static String DEFAULT_ADDRESS = "Address";		
	private final static String DEFAULT_PHONE_HOME = "Phone (home)";		
	private final static String DEFAULT_PHONE_WORK = "Phone (work)";
	private final static String DEFAULT_CO_ADDRESS_SELECT = "Use c/o address";		
	private final static String DEFAULT_CO_STREET_ADDRESS = "Street address c/o";		
	private final static String DEFAULT_CO_POSTAL_CODE = "Postal code c/o";		
	private final static String DEFAULT_CO_CITY = "City c/o";		
	private final static String DEFAULT_MESSAGES_VIA_EMAIL = "I want to get my messages via e-email";		
	private final static String DEFAULT_PASSWORD_EMPTY = "Password cannot be empty.";		
	private final static String DEFAULT_PASSWORDS_NOT_SAME = "New passwords not the same.";		
	private final static String DEFAULT_PASSWORD_INVALID = "Invalid password.";		
	private final static String DEFAULT_PASSWORD_TOO_SHORT = "Password too short.";		
	private final static String DEFAULT_PASSWORD_CHAR_ILLEGAL = "Password contains illegal character(s).";		
	private final static String DEFAULT_EMAIL_INVALID = "Email address invalid.";		
	private final static String DEFAULT_EMAIL_EMPTY = "Email address cannot be empty.";		
	
	private User user = null;
		
	public CitizenAccountPreferences() {
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public void main(IWContext iwc) {
		if (!iwc.isLoggedOn()) {
			return;
		}
		this.setResourceBundle(getResourceBundle(iwc));
		this.user = iwc.getCurrentUser();

		try {
			int action = parseAction(iwc);
			switch (action) {
				case ACTION_VIEW_FORM:
					viewPreferencesForm(iwc);
					break;
				case ACTION_FORM_SUBMIT:
					updatePreferences(iwc);
					break;
				case ACTION_CANCEL:
					viewPreferencesForm(iwc);
					break;
			}
		} catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}    
	}

	private int parseAction (final IWContext iwc) {
		int action = ACTION_VIEW_FORM;
        
		if (iwc.isParameterSet(PARAMETER_FORM_SUBMIT)) {
			action = ACTION_FORM_SUBMIT;
		} else if (iwc.isParameterSet(PARAMETER_CANCEL)) {
			action = ACTION_CANCEL;
		}
        
		return action;
	}
	
	private void viewPreferencesForm(IWContext iwc) throws java.rmi.RemoteException {
		drawPermanentUserInfo(iwc);
		drawForm(iwc);
	}
	
	private void drawPermanentUserInfo(IWContext iwc) throws java.rmi.RemoteException {
		UserBusiness ub = (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);

		Table table = new Table();
//		table.setWidth(getWidth());
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());

		int row = 1;
		table.add(getSmallHeader(localize(KEY_NAME, DEFAULT_NAME)), 1, row);
		String userName = user.getFirstName();
		if (user.getLastName() != null) {
			userName = user.getLastName() + ", " + userName;
		}
		table.add(getSmallText(userName), 2, row);

		row++;
		table.add(getSmallHeader(localize(KEY_LOGIN, DEFAULT_LOGIN)), 1, row);
		LoginTable loginTable = LoginDBHandler.getUserLogin(((Integer) user.getPrimaryKey()).intValue());
		if (loginTable != null) {
			table.add(new HiddenInput(PARAMETER_OLD_LOGIN, loginTable.getUserLogin()), 2, row);
			table.add(getSmallText(loginTable.getUserLogin()), 2, row);
		}

		row++;
		table.add(getSmallHeader(localize(KEY_ADDRESS, DEFAULT_ADDRESS)), 1, row);
		Address mainAddress = ub.getUsersMainAddress(user);
		String addressText = "Stockholm";
		if (mainAddress != null) {
			addressText = mainAddress.getStreetAddress() + ", " + mainAddress.getPostalAddress();
		}
		table.add(getSmallText(addressText), 2, row);

		row++;
		table.addBreak(1, row);

		add(table);
	}

	private void drawForm(IWContext iwc) throws RemoteException {
		Form form = new Form();
		Table table = new Table();	
//		table.setWidth(getWidth());
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		form.add(table);
		int row = 1;
		
		String paramPhoneHome = iwc.getParameter(PARAMETER_PHONE_HOME);

		Text tEmail = getSmallText(localize(KEY_EMAIL, DEFAULT_EMAIL));
		Text tLogin = getSmallText(localize(KEY_LOGIN, DEFAULT_LOGIN));
		Text tCurrentPassword = getSmallText(localize(KEY_CURRENT_PASSWORD, DEFAULT_CURRENT_PASSWORD));
		Text tNewPassword = getSmallText(localize(KEY_NEW_PASSWORD, DEFAULT_NEW_PASSWORD));
		Text tNewPasswordRepeated = getSmallText(localize(KEY_NEW_PASSWORD_REPEATED, DEFAULT_NEW_PASSWORD_REPEATED));
		Text tPhoneHome = getSmallText(localize(KEY_PHONE_HOME, DEFAULT_PHONE_HOME));
		Text tPhoneWork = getSmallText(localize(KEY_PHONE_WORK, DEFAULT_PHONE_WORK));
		Text tCOAddressSelect = getSmallText(" " + localize(KEY_CO_ADDRESS_SELECT, DEFAULT_CO_ADDRESS_SELECT) + ":");
		Text tCOStreetAddress = getSmallText(localize(KEY_CO_STREET_ADDRESS, DEFAULT_CO_STREET_ADDRESS));
		Text tCOPostalCode = getSmallText(localize(KEY_CO_POSTAL_CODE, DEFAULT_CO_POSTAL_CODE));
		Text tCOCity = getSmallText(localize(KEY_CO_CITY, DEFAULT_CO_CITY));
		Text tMessagesViaEmail = getSmallText(" " + localize(KEY_MESSAGES_VIA_EMAIL, DEFAULT_MESSAGES_VIA_EMAIL));
//		TextInput tiLogin = (TextInput) getStyledInterface(new TextInput(PARAMETER_LOGIN));		
		TextInput tiPhoneHome = (TextInput) getStyledInterface(new TextInput(PARAMETER_PHONE_HOME));
		tiPhoneHome.setValue(paramPhoneHome);		
		TextInput tiPhoneWork = (TextInput) getStyledInterface(new TextInput(PARAMETER_PHONE_WORK));		
		TextInput tiCOStreetAddress = (TextInput) getStyledInterface(new TextInput(PARAMETER_CO_STREET_ADDRESS));		
		TextInput tiCOPostalCode = (TextInput) getStyledInterface(new TextInput(PARAMETER_CO_POSTAL_CODE));		
		TextInput tiCOCity = (TextInput) getStyledInterface(new TextInput(PARAMETER_CO_CITY));		
		PasswordInput tiCurrentPassword = (PasswordInput) getStyledInterface(new PasswordInput(PARAMETER_CURRENT_PASSWORD));	
		PasswordInput tiNewPassword = (PasswordInput) getStyledInterface(new PasswordInput(PARAMETER_NEW_PASSWORD));
		PasswordInput tiNewPasswordRepeated = (PasswordInput) getStyledInterface(new PasswordInput(PARAMETER_NEW_PASSWORD_REPEATED));
		CheckBox cbCOAddressSelect = getCheckBox("","");
		CheckBox cbMessagesViaEmail = getCheckBox("","");
		SubmitButton sbUpdate = (SubmitButton) getStyledInterface(new SubmitButton(localize(KEY_UPDATE, DEFAULT_UPDATE), PARAMETER_FORM_SUBMIT, "true"));
		SubmitButton sbCancel = (SubmitButton) getStyledInterface(new SubmitButton(localize(KEY_CANCEL, DEFAULT_CANCEL), PARAMETER_FORM_SUBMIT, "true"));

		Collection emails = user.getEmails();
		try {
			if (emails != null) {
				Iterator iter = emails.iterator();
				Email email;
				EmailHome emailHome = (EmailHome) IDOLookup.getHome(Email.class);
				String emId;
				if (iter.hasNext()) {
					email = emailHome.findByPrimaryKey(iter.next());
					emId = email.getPrimaryKey().toString();
					TextInput tiEmail = (TextInput) super.getStyledInterface(new TextInput(PARAMETER_EMAIL, email.getEmailAddress()));		
					table.add(tEmail, 1, row);
					table.add(new HiddenInput(PARAMETER_EMAIL_ID, emId), 2, row);
					table.add(tiEmail, 2, row);
				} else {
					TextInput tiEmail = (TextInput) super.getStyledInterface(new TextInput(PARAMETER_EMAIL));		
					table.add(tEmail, 1, row);
					table.add(tiEmail, 2, row);
				}
			}			
		} catch (FinderException e) {
			super.add(new ExceptionWrapper(e, this));
		}
		
//		row++;
//		LoginTable loginTable = LoginDBHandler.getUserLogin(((Integer) user.getPrimaryKey()).intValue());
//		if (loginTable != null) {
//			tiLogin.setContent(loginTable.getUserLogin());	
//			table.add(new HiddenInput(PARAMETER_OLD_LOGIN, loginTable.getUserLogin()), 2, row);
//		}
//		table.add(tLogin, 1, row);
//		table.add(tiLogin, 2, row);
		
		row++;
		table.add(tCurrentPassword, 1, row);
		table.add(tiCurrentPassword, 2, row);

		row++;
		table.add(tNewPassword, 1, row);
		table.add(tiNewPassword, 2, row);

		row++;
		table.add(tNewPasswordRepeated, 1, row);
		table.add(tiNewPasswordRepeated, 2, row);
		
		row++;
		table.addBreak(1, row);

		row++;
		table.add(tPhoneHome, 1, row);
		table.add(tiPhoneHome, 2, row);

		row++;
		table.add(tPhoneWork, 1, row);
		table.add(tiPhoneWork, 2, row);
		
		row++;
		table.addBreak(1, row);

		row++;
		table.add(cbCOAddressSelect, 2, row);
		table.add(tCOAddressSelect, 2, row);

		row++;
		table.add(tCOStreetAddress, 1, row);
		table.add(tiCOStreetAddress, 2, row);

		row++;
		table.add(tCOPostalCode, 1, row);
		table.add(tiCOPostalCode, 2, row);

		row++;
		table.add(tCOCity, 1, row);
		table.add(tiCOCity, 2, row);
		
		row++;
		table.addBreak(1, row);

		row++;
		table.add(cbMessagesViaEmail, 2, row);
		table.add(tMessagesViaEmail, 2, row);
		
		row++;
		table.addBreak(1, row);
		
		row++;
		table.add(sbUpdate, 2, row);
		table.add(getSmallText(" "), 2, row);
		table.add(sbCancel, 2, row);
		table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_RIGHT);
		
		row++;
		table.addBreak(1, row);
				
		add(form);
	}
	
	private void updatePreferences(IWContext iwc)  throws Exception {
		LoginTable loginTable = LoginDBHandler.getUserLogin(((Integer) user.getPrimaryKey()).intValue());
		String login    = loginTable.getUserLogin();
//		String loginOld = iwc.getParameter(PARAMETER_OLD_LOGIN);
		String currentPassword = iwc.getParameter(PARAMETER_CURRENT_PASSWORD);
		String newPassword1 = iwc.getParameter(PARAMETER_NEW_PASSWORD);
		String newPassword2 = iwc.getParameter(PARAMETER_NEW_PASSWORD_REPEATED);		
		String sEmail = iwc.getParameter(PARAMETER_EMAIL);
		
		String errorMessage = null;
		boolean updatePassword = false;
		boolean updateEmail = false;
		
		try {
			if (!LoginDBHandler.verifyPassword(login, currentPassword)) {
				throw new Exception(localize(KEY_PASSWORD_INVALID, DEFAULT_PASSWORD_INVALID));
			}
			if (!newPassword1.equals("") && !newPassword2.equals("")) {
				if (newPassword1.equals("")) {
					throw new Exception(localize(KEY_PASSWORD_EMPTY, DEFAULT_PASSWORD_EMPTY));
				}
				if (!newPassword1.equals(newPassword2)) {
					throw new Exception(localize(KEY_PASSWORDS_NOT_SAME, DEFAULT_PASSWORDS_NOT_SAME));
				}
				if (newPassword1.length() < MIN_PASSWORD_LENGTH) {
					throw new Exception(localize(KEY_PASSWORD_TOO_SHORT, DEFAULT_PASSWORD_TOO_SHORT));
				}
				for (int i = 0; i < newPassword1.length(); i++) {
					char c = newPassword1.charAt(i);
					boolean isPasswordCharOK = false;
					if ((c >= 'a') && (c <= 'z')) {
						isPasswordCharOK = true;
					} else if ((c >= 'A') && (c <= 'Z')) {
						isPasswordCharOK = true;
					} else if ((c >= '0') && (c <= '9')) {
						isPasswordCharOK = true;
					} else if ((c == 'Œ') || (c == 'Š') || (c == 'š')) {
						isPasswordCharOK = true;
					} else if ((c == '') || (c == '€') || (c == '…')) {
						isPasswordCharOK = true;
					}
					if (!isPasswordCharOK) {
						throw new Exception(localize(KEY_PASSWORD_CHAR_ILLEGAL, DEFAULT_PASSWORD_CHAR_ILLEGAL));
					}
				}
				updatePassword = true;
			}

			Email email = getUserEmail(iwc);
			String oldEmail = email.getEmailAddress();
			if (sEmail != oldEmail) {
				if (sEmail.equals("")) {
					throw new Exception(localize(KEY_EMAIL_EMPTY, DEFAULT_EMAIL_EMPTY));
				}
				if (sEmail.length() < 6) {
					throw new Exception(localize(KEY_EMAIL_INVALID, DEFAULT_EMAIL_INVALID));
				}
				if (sEmail.indexOf('.') == -1) {
					throw new Exception(localize(KEY_EMAIL_INVALID, DEFAULT_EMAIL_INVALID));
				}					
				if (sEmail.indexOf('@') == -1) {
					throw new Exception(localize(KEY_EMAIL_INVALID, DEFAULT_EMAIL_INVALID));
				}					
				String testEmail = sEmail.toLowerCase();
				for (int i = 0; i < testEmail.length(); i++) {
					char c = testEmail.charAt(i);
					if ((c < 'a') || (c > 'z')) {
						if ((c != '.') || (c != '@')) {
							throw new Exception(localize(KEY_EMAIL_INVALID, DEFAULT_EMAIL_INVALID));
						}
					}
				}
				updateEmail = true;
			}
		} catch (Exception e) {
			errorMessage = e.getMessage();
		}
		

				// Check e-mail address ...
				// Check c/o address ...
		if (errorMessage != null) {
			add(getErrorText(errorMessage));
		} else {
			// Ok to update preferences
			if (updatePassword) {
				LoginDBHandler.updateLogin(user.getID(), login, newPassword1);
			}
			if (updateEmail) {
				Email email = getUserEmail(iwc);
				email.setEmailAddress(sEmail);
				email.store();
			}
		}
		drawPermanentUserInfo(iwc);
		drawForm(iwc);
	}
	
	private Email getUserEmail(IWContext iwc) {
		Email email = null;
		try {
			String emailId = iwc.getParameter(PARAMETER_EMAIL_ID);
			EmailHome emailHome = (EmailHome) IDOLookup.getHome(Email.class);
			email = emailHome.findByPrimaryKey(new Integer(emailId));
		} catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}
		return email;
	}
}
