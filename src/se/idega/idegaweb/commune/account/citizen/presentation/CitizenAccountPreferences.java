package se.idega.idegaweb.commune.account.citizen.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.account.citizen.business.CitizenAccountSession;
import se.idega.idegaweb.commune.message.business.MessageSession;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.business.IBOLookup;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.builder.data.ICPage;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.AddressHome;
import com.idega.core.location.data.AddressType;
import com.idega.core.location.data.PostalCode;
import com.idega.core.location.data.PostalCodeHome;
import com.idega.data.IDOLookup;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.PasswordInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;

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
	//private final static String PARAMETER_LOGIN = "cap_lgn";
	private final static String PARAMETER_OLD_LOGIN = "cap_old_lgn";
	private final static String PARAMETER_CURRENT_PASSWORD = "cap_c_pw";
	private final static String PARAMETER_NEW_PASSWORD = "cap_n_pw";
	private final static String PARAMETER_NEW_PASSWORD_REPEATED = "cap_n_pw_r";
	private final static String PARAMETER_EMAIL = "cap_email";
	//private final static String PARAMETER_EMAIL_ID = "cap_email_id";
	private final static String PARAMETER_PHONE_HOME = "cap_phn_h";
	private final static String PARAMETER_PHONE_WORK = "cap_phn_w";
	private final static String PARAMETER_CO_ADDRESS_SELECT = "cap_co";
	private final static String PARAMETER_CO_STREET_ADDRESS = "cap_co_sa";
	private final static String PARAMETER_CO_POSTAL_CODE = "cap_co_pc";
	private final static String PARAMETER_CO_CITY = "cap_co_ct";
	private final static String PARAMETER_MESSAGES_VIA_EMAIL = "cap_m_v_e";

	private final int MIN_PASSWORD_LENGTH = 8;
	
	private final static String KEY_PREFIX = "citizen.";
	private final static String KEY_EMAIL = KEY_PREFIX + "email";
	private final static String KEY_LOGIN = KEY_PREFIX + "login";
	//private final static String KEY_OLD_LOGIN = KEY_PREFIX + "old_login";
	private final static String KEY_CURRENT_PASSWORD = KEY_PREFIX + "current_password";
	private final static String KEY_NEW_PASSWORD = KEY_PREFIX + "new_password";
	private final static String KEY_NEW_PASSWORD_REPEATED = KEY_PREFIX + "new_password_repeated";
	private final static String KEY_UPDATE = KEY_PREFIX + "update";
	//private final static String KEY_CANCEL = KEY_PREFIX + "cancel";
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
	private final static String KEY_PASSWORD_REPEATED_EMPTY = KEY_PREFIX + "password_repeated_empty";
	private final static String KEY_PASSWORDS_NOT_SAME = KEY_PREFIX + "passwords_not_same";
	private final static String KEY_PASSWORD_INVALID = KEY_PREFIX + "invalid_password";	
	private final static String KEY_PASSWORD_TOO_SHORT = KEY_PREFIX + "password_too_short";	
	private final static String KEY_PASSWORD_CHAR_ILLEGAL = KEY_PREFIX + "password_char_illegal";	
	private final static String KEY_EMAIL_INVALID = KEY_PREFIX + "email_invalid";	
	private final static String KEY_EMAIL_EMPTY = KEY_PREFIX + "email_empty";	
	private final static String KEY_CO_STREET_ADDRESS_MISSING = KEY_PREFIX + "co_street_address_missing";	
	private final static String KEY_CO_POSTAL_CODE_MISSING = KEY_PREFIX + "co_postal_code_missing";	
	private final static String KEY_CO_CITY_MISSING = KEY_PREFIX + "co_city_missing";	
	private final static String KEY_PREFERENCES_SAVED = KEY_PREFIX + "preferenced_saved";	

	private final static String DEFAULT_EMAIL = "E-mail";	
	private final static String DEFAULT_LOGIN = "Login";	
	//private final static String DEFAULT_OLD_LOGIN = "Old login";	
	private final static String DEFAULT_CURRENT_PASSWORD = "Current password";	
	private final static String DEFAULT_NEW_PASSWORD = "New password";	
	private final static String DEFAULT_NEW_PASSWORD_REPEATED = "Repeat new password";	
	private final static String DEFAULT_UPDATE = "Update";	
	//private final static String DEFAULT_CANCEL = "Cancel";	
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
	private final static String DEFAULT_PASSWORD_REPEATED_EMPTY = "Repeated password cannot be empty.";		
	private final static String DEFAULT_PASSWORDS_NOT_SAME = "New passwords not the same.";		
	private final static String DEFAULT_PASSWORD_INVALID = "Invalid password.";		
	private final static String DEFAULT_PASSWORD_TOO_SHORT = "Password too short.";		
	private final static String DEFAULT_PASSWORD_CHAR_ILLEGAL = "Password contains illegal character(s).";		
	private final static String DEFAULT_EMAIL_INVALID = "Email address invalid.";		
	private final static String DEFAULT_EMAIL_EMPTY = "Email address cannot be empty.";		
	private final static String DEFAULT_CO_STREET_ADDRESS_MISSING = "Street address c/o must be entered.";	
	private final static String DEFAULT_CO_POSTAL_CODE_MISSING = "Postal code c/o must be entered.";	
	private final static String DEFAULT_CO_CITY_MISSING = "City c/o must be entered.";	
	private final static String DEFAULT_PREFERENCES_SAVED = "Your preferences has been saved.";	

	public static final String CITIZEN_ACCOUNT_PREFERENCES_PROPERTIES = "citizen_account_preferences";
	public static final String USER_PROPERTY_USE_CO_ADDRESS = "cap_use_co_address";

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
		drawForm(iwc);
	}
	
	private void drawForm(IWContext iwc) throws RemoteException {
		Form form = new Form();
		Table table = new Table();	
//		table.setWidth(getWidth());
		table.setCellpadding(2);
		table.setCellspacing(getCellspacing());
		form.add(table);
		int row = 1;

		UserBusiness ub = (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);

		table.add(getSmallHeader(localize(KEY_NAME, DEFAULT_NAME)), 1, row);
		String userName = user.getName();
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
		String addressText = "";
		if (mainAddress != null) {
			addressText = mainAddress.getStreetAddress() + ", " + mainAddress.getPostalAddress();
		}
		table.add(getSmallText(addressText), 2, row);

		String valueEmail = iwc.getParameter(PARAMETER_EMAIL);
		if (valueEmail == null) {
			Email userMail = ub.getUserMail(user);
			if (userMail != null) {
				valueEmail = userMail.getEmailAddress();
			} else {
				valueEmail = "";
			}
		}
		String valueCurrentPassword = iwc.getParameter(PARAMETER_CURRENT_PASSWORD) != null ? iwc.getParameter(PARAMETER_CURRENT_PASSWORD) : "";
		String valueNewPassword = iwc.getParameter(PARAMETER_NEW_PASSWORD) != null ? iwc.getParameter(PARAMETER_NEW_PASSWORD) : "";
		String valueNewPasswordRepeated = iwc.getParameter(PARAMETER_NEW_PASSWORD_REPEATED) != null ? iwc.getParameter(PARAMETER_NEW_PASSWORD_REPEATED) : "";
		String valuePhoneHome = iwc.getParameter(PARAMETER_PHONE_HOME);
		if (valuePhoneHome == null) {
			Phone p = ub.getUserPhone(((Integer)user.getPrimaryKey()).intValue(), 1);
			if (p != null) {
				valuePhoneHome = p.getNumber();
			} else {
				valuePhoneHome = "";
			}
		}
		String valuePhoneWork = iwc.getParameter(PARAMETER_PHONE_WORK);
		if (valuePhoneWork == null) {
			Phone p = ub.getUserPhone(((Integer)user.getPrimaryKey()).intValue(), 2);
			if (p != null) {
				valuePhoneWork = p.getNumber();
			} else {
				valuePhoneWork = "";
			}
		}
		String valueCOAddressSelect = iwc.getParameter(PARAMETER_CO_ADDRESS_SELECT);
		Address coAddress = getCOAddress(iwc);
		String valueCOStreetAddress = iwc.getParameter(PARAMETER_CO_STREET_ADDRESS);
		if (valueCOStreetAddress == null) {
			valueCOStreetAddress = coAddress.getStreetAddress();
			if (valueCOStreetAddress == null) {
				valueCOStreetAddress = "";
			}
		}
		String valueCOPostalCode = iwc.getParameter(PARAMETER_CO_POSTAL_CODE);
		if (valueCOPostalCode == null) {
			PostalCode pc = null;
			pc = coAddress.getPostalCode();
			if (pc != null) {
				valueCOPostalCode = pc.getPostalCode();
				if (valueCOPostalCode == null) {
					valueCOPostalCode = "";
				}
			} else {
				valueCOPostalCode = "";
			}
		}
		String valueCOCity = iwc.getParameter(PARAMETER_CO_CITY);
		if (valueCOCity == null) {
			valueCOCity = coAddress.getCity();
			if (valueCOCity == null) {
				valueCOCity = "";
			}
		}
		String valueMessagesViaEmail = iwc.getParameter(PARAMETER_MESSAGES_VIA_EMAIL);
		
		Text tEmail = getSmallHeader(localize(KEY_EMAIL, DEFAULT_EMAIL));
		//Text tLogin = getSmallHeader(localize(KEY_LOGIN, DEFAULT_LOGIN));
		Text tCurrentPassword = getSmallHeader(localize(KEY_CURRENT_PASSWORD, DEFAULT_CURRENT_PASSWORD));
		Text tNewPassword = getSmallHeader(localize(KEY_NEW_PASSWORD, DEFAULT_NEW_PASSWORD));
		Text tNewPasswordRepeated = getSmallHeader(localize(KEY_NEW_PASSWORD_REPEATED, DEFAULT_NEW_PASSWORD_REPEATED));
		Text tPhoneHome = getSmallHeader(localize(KEY_PHONE_HOME, DEFAULT_PHONE_HOME));
		Text tPhoneWork = getSmallHeader(localize(KEY_PHONE_WORK, DEFAULT_PHONE_WORK));
		Text tCOAddressSelect = getSmallText(" " + localize(KEY_CO_ADDRESS_SELECT, DEFAULT_CO_ADDRESS_SELECT) + ":");
		Text tCOStreetAddress = getSmallHeader(localize(KEY_CO_STREET_ADDRESS, DEFAULT_CO_STREET_ADDRESS));
		Text tCOPostalCode = getSmallHeader(localize(KEY_CO_POSTAL_CODE, DEFAULT_CO_POSTAL_CODE));
		Text tCOCity = getSmallHeader(localize(KEY_CO_CITY, DEFAULT_CO_CITY));
		Text tMessagesViaEmail = getSmallText(" " + localize(KEY_MESSAGES_VIA_EMAIL, DEFAULT_MESSAGES_VIA_EMAIL));
//		TextInput tiLogin = (TextInput) getStyledInterface(new TextInput(PARAMETER_LOGIN));		
		TextInput tiEmail = (TextInput) getStyledInterface(new TextInput(PARAMETER_EMAIL));
		if(valueEmail!=null){
			tiEmail.setValue(valueEmail);
		}		
		TextInput tiPhoneHome = (TextInput) getStyledInterface(new TextInput(PARAMETER_PHONE_HOME));
		if(valuePhoneHome!=null){
			tiPhoneHome.setValue(valuePhoneHome);		
		}
		TextInput tiPhoneWork = (TextInput) getStyledInterface(new TextInput(PARAMETER_PHONE_WORK));		
		if(valuePhoneWork!=null){
			tiPhoneWork.setValue(valuePhoneWork);
		}
		TextInput tiCOStreetAddress = (TextInput) getStyledInterface(new TextInput(PARAMETER_CO_STREET_ADDRESS));		
		if(valueCOStreetAddress!=null){
			tiCOStreetAddress.setValue(valueCOStreetAddress);
		}
		TextInput tiCOPostalCode = (TextInput) getStyledInterface(new TextInput(PARAMETER_CO_POSTAL_CODE));
		if(valueCOPostalCode!=null){
			tiCOPostalCode.setValue(valueCOPostalCode);
		}
		TextInput tiCOCity = (TextInput) getStyledInterface(new TextInput(PARAMETER_CO_CITY));
		if(valueCOCity!=null){
			tiCOCity.setValue(valueCOCity);
		}
		PasswordInput tiCurrentPassword = (PasswordInput) getStyledInterface(new PasswordInput(PARAMETER_CURRENT_PASSWORD));	
		if(valueCurrentPassword!=null){
			tiCurrentPassword.setValue(valueCurrentPassword);
		}
		PasswordInput tiNewPassword = (PasswordInput) getStyledInterface(new PasswordInput(PARAMETER_NEW_PASSWORD));
		if(valueNewPassword!=null){
			tiNewPassword.setValue(valueNewPassword);
		}
		PasswordInput tiNewPasswordRepeated = (PasswordInput) getStyledInterface(new PasswordInput(PARAMETER_NEW_PASSWORD_REPEATED));
		if(valueNewPasswordRepeated!=null){
			tiNewPasswordRepeated.setValue(valueNewPasswordRepeated);
		}
		CheckBox cbCOAddressSelect = getCheckBox(PARAMETER_CO_ADDRESS_SELECT, "true");
		if (valueCOAddressSelect != null) {
			if (valueCOAddressSelect.length() > 0)
				cbCOAddressSelect.setChecked(true);
		} else {
			if (iwc.getParameter(PARAMETER_FORM_SUBMIT) == null) {
				try {
					CitizenAccountSession cas = getCitizenAccountSession(iwc);
					cbCOAddressSelect.setChecked(cas.getIfUserUsesCOAddress());
				} catch (Exception e) {}
			} else {
				cbCOAddressSelect.setChecked(false);
			}
		}
		CheckBox cbMessagesViaEmail = getCheckBox(PARAMETER_MESSAGES_VIA_EMAIL, "true");
		if (valueMessagesViaEmail != null) {
			cbMessagesViaEmail.setChecked(true);
		} else {
			if (iwc.getParameter(PARAMETER_FORM_SUBMIT) == null) {
				try {
					MessageSession messageSession = getMessageSession(iwc);
					cbMessagesViaEmail.setChecked(messageSession.getIfUserPreferesMessageByEmail());
				} catch (Exception e) {}
			} else {
				cbMessagesViaEmail.setChecked(false);
			}
		}
		SubmitButton sbUpdate = (SubmitButton) getStyledInterface(new SubmitButton(localize(KEY_UPDATE, DEFAULT_UPDATE), PARAMETER_FORM_SUBMIT, "true"));
		
		row++;
		table.setHeight(row, 12);

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
		table.setHeight(row, 12);

		row++;
		table.add(tEmail, 1, row);
		table.add(tiEmail, 2, row);
		
		row++;
		table.add(tPhoneHome, 1, row);
		table.add(tiPhoneHome, 2, row);

		row++;
		table.add(tPhoneWork, 1, row);
		table.add(tiPhoneWork, 2, row);
		
		row++;
		table.setHeight(row, 12);

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
		table.setHeight(row, 12);

		row++;
		table.mergeCells(1, row, 2, row);
		table.add(cbCOAddressSelect, 1, row);
		table.add(tCOAddressSelect, 1, row);

		row++;
		table.mergeCells(1, row, 2, row);
		table.add(cbMessagesViaEmail, 1, row);
		table.add(tMessagesViaEmail, 1, row);
		
		row++;
		table.setHeight(row, 12);
		
		ICPage homepage = null;
		try {
			ub.getHomePageForUser(user);
		}
		catch (FinderException fe) {
			//No page found
			homepage = null;
		}
		
		row++;
		table.add(sbUpdate, 1, row);
		if (homepage != null) {
			table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
			GenericButton home = getButton(new GenericButton("home", localize("my_page", "Back to My Page")));
			home.setPageToOpen(homepage);
			table.add(home, 1, row);
		}
		
		add(form);
	}
	
	private void updatePreferences(IWContext iwc)  throws Exception {
		LoginTable loginTable = LoginDBHandler.getUserLogin(((Integer) user.getPrimaryKey()).intValue());
		String login    = loginTable.getUserLogin();
		String currentPassword = iwc.getParameter(PARAMETER_CURRENT_PASSWORD);
		String newPassword1 = iwc.getParameter(PARAMETER_NEW_PASSWORD);
		String newPassword2 = iwc.getParameter(PARAMETER_NEW_PASSWORD_REPEATED);		
		String sEmail = iwc.getParameter(PARAMETER_EMAIL);
		String phoneHome = iwc.getParameter(PARAMETER_PHONE_HOME);
		String phoneWork = iwc.getParameter(PARAMETER_PHONE_WORK);
		String coStreetAddress = iwc.getParameter(PARAMETER_CO_STREET_ADDRESS);
		String coPostalCode = iwc.getParameter(PARAMETER_CO_POSTAL_CODE);
		String coCity = iwc.getParameter(PARAMETER_CO_CITY);		
		boolean useCOAddress = iwc.getParameter(PARAMETER_CO_ADDRESS_SELECT) != null;
		boolean messagesViaEmail = iwc.getParameter(PARAMETER_MESSAGES_VIA_EMAIL) != null;
		
		String errorMessage = null;
		boolean updatePassword = false;
		boolean updateEmail = false;
		boolean updateCOAddress = false;
		
		try {
			if (!LoginDBHandler.verifyPassword(login, currentPassword)) {
				throw new Exception(localize(KEY_PASSWORD_INVALID, DEFAULT_PASSWORD_INVALID));
			}
			
			// Validate new password
			if (!newPassword1.equals("") || !newPassword2.equals("")) {
				if (newPassword1.equals("")) {
					throw new Exception(localize(KEY_PASSWORD_EMPTY, DEFAULT_PASSWORD_EMPTY));
				}
				if (newPassword2.equals("")) {
					throw new Exception(localize(KEY_PASSWORD_REPEATED_EMPTY, DEFAULT_PASSWORD_REPEATED_EMPTY));
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

			updateEmail = validateEmail(sEmail);

			// Validate c/o-address
			if (useCOAddress) {
				if (coStreetAddress.equals("")) {
					throw new Exception(localize(KEY_CO_STREET_ADDRESS_MISSING, DEFAULT_CO_STREET_ADDRESS_MISSING));
				}
				if (coPostalCode.equals("")) {
					throw new Exception(localize(KEY_CO_POSTAL_CODE_MISSING, DEFAULT_CO_POSTAL_CODE_MISSING));
				}
				if (coCity.equals("")) {
					throw new Exception(localize(KEY_CO_CITY_MISSING, DEFAULT_CO_CITY_MISSING));
				}
				updateCOAddress = true;
			}
		} catch (Exception e) {
			errorMessage = e.getMessage();
		}

		if (errorMessage != null) {
			add(getErrorText(" " + errorMessage));
		} else {
			// Ok to update preferences
			UserBusiness ub = (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);

			if (updatePassword) {
				LoginDBHandler.updateLogin(((Integer)user.getPrimaryKey()).intValue(), login, newPassword1);
			}
			if (updateEmail) {
				ub.updateUserMail(((Integer)user.getPrimaryKey()).intValue(), sEmail);
			}
			ub.updateUserPhone(((Integer)user.getPrimaryKey()).intValue(), 1, phoneHome);
			ub.updateUserPhone(((Integer)user.getPrimaryKey()).intValue(), 2, phoneWork);
			if (updateCOAddress) {
				Address coAddress = getCOAddress(iwc);
				coAddress.setStreetName(coStreetAddress);
				PostalCode pc = coAddress.getPostalCode();
				if (pc == null) {
					PostalCodeHome ph = (PostalCodeHome) IDOLookup.getHome(PostalCode.class);
					pc = ph.create();
				}
				pc.setPostalCode(coPostalCode);
				pc.store();
				coAddress.setPostalCode(pc);
				coAddress.setCity(coCity);
				coAddress.store();
			}
			MessageSession messageSession = getMessageSession(iwc);
			messageSession.setIfUserPreferesMessageByEmail(messagesViaEmail);
			CitizenAccountSession cas = getCitizenAccountSession(iwc);
			cas.setIfUserUsesCOAddress(useCOAddress);
		}
		drawForm(iwc);
		if (errorMessage == null) {
			add(new Break());
			add(getLocalizedText(KEY_PREFERENCES_SAVED, DEFAULT_PREFERENCES_SAVED));
		}
	}
	
	private Address getCOAddress(IWContext iwc) {
		Address coAddress = null;
		try {
			UserBusiness ub = (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
			AddressHome ah = ub.getAddressHome();
			AddressType coType = ah.getAddressType2();
			Collection addresses = user.getAddresses();
			Iterator iter = addresses.iterator();
			while (iter.hasNext()) {
				Address address = (Address) iter.next();
				int typeID = address.getAddressTypeID();
				if (typeID == ((Integer)coType.getPrimaryKey()).intValue()) {
					return address;
				}				
			}
			// No c/o address found, create one
			coAddress = ah.create();
			coAddress.setAddressType(coType);
			coAddress.store();
			user.addAddress(coAddress);
		} catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}
		return coAddress;
	}
	
	private MessageSession getMessageSession(IWContext iwc) throws Exception {
		return (MessageSession) com.idega.business.IBOLookup.getSessionInstance(iwc, MessageSession.class);
	}
	
	private CitizenAccountSession getCitizenAccountSession(IWContext iwc) throws Exception {
		return (CitizenAccountSession) com.idega.business.IBOLookup.getSessionInstance(iwc, CitizenAccountSession.class);
	}


	/**
	 * @param sEmail an email address, valid or not, to check
	 * @return boolean true if there is no problem with all characters and is
	 * not empty. False if it is an empty string.
	 * @throws Exception if the sEmail is a non empty string and contains
	 * erroneous characters.
	 */
	private boolean validateEmail(String sEmail)throws Exception{
		boolean validEmail=false;
		if (null == sEmail || 0 == sEmail.trim ().length ()) {
			validEmail = false;
		} else {
			// Validate e-mail address
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
					if ((c != '.') && (c != '@') && (c != '-') && (c != '_') && ((c < '0') || (c > '9'))) {
						throw new Exception(localize(KEY_EMAIL_INVALID, DEFAULT_EMAIL_INVALID));
					}
				}
			}
			validEmail = true;
		}
		return validEmail;
	}

}
