package se.idega.idegaweb.ehealth.citizen.presentation;

import java.rmi.RemoteException;

import se.idega.idegaweb.ehealth.presentation.EHealthBlock;

import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.PasswordInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.User;
import com.idega.util.PersonalIDFormatter;



/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Anders Lindman
 * @version 1.0
 */
public class CitizenAccountPreferencesLogin extends EHealthBlock {
	
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
	
	private final int MIN_PASSWORD_LENGTH = 8;
	
	private final static String KEY_PREFIX = "citizen.";
	private final static String KEY_LOGIN = KEY_PREFIX + "login";
	//private final static String KEY_OLD_LOGIN = KEY_PREFIX + "old_login";
	private final static String KEY_CURRENT_PASSWORD = KEY_PREFIX + "current_password";
	private final static String KEY_NEW_PASSWORD = KEY_PREFIX + "new_password";
	private final static String KEY_NEW_PASSWORD_REPEATED = KEY_PREFIX + "new_password_repeated";
	private final static String KEY_UPDATE = KEY_PREFIX + "update";
	//private final static String KEY_CANCEL = KEY_PREFIX + "cancel";
	private final static String KEY_PID = KEY_PREFIX + "personalid";
	private final static String KEY_PASSWORD_EMPTY = KEY_PREFIX + "password_empty";
	private final static String KEY_PASSWORD_REPEATED_EMPTY = KEY_PREFIX + "password_repeated_empty";
	private final static String KEY_PASSWORDS_NOT_SAME = KEY_PREFIX + "passwords_not_same";
	private final static String KEY_PASSWORD_INVALID = KEY_PREFIX + "invalid_password";	
	private final static String KEY_PASSWORD_TOO_SHORT = KEY_PREFIX + "password_too_short";	
	private final static String KEY_PASSWORD_CHAR_ILLEGAL = KEY_PREFIX + "password_char_illegal";	
	private final static String KEY_PREFERENCES_SAVED = KEY_PREFIX + "preferenced_saved";	
	
	private final static String DEFAULT_LOGIN = "Login";	
	//private final static String DEFAULT_OLD_LOGIN = "Old login";	
	private final static String DEFAULT_CURRENT_PASSWORD = "Current password";	
	private final static String DEFAULT_NEW_PASSWORD = "New password";	
	private final static String DEFAULT_NEW_PASSWORD_REPEATED = "Repeat new password";	
	private final static String DEFAULT_UPDATE = "Update";	
	//private final static String DEFAULT_CANCEL = "Cancel";	
	private final static String DEFAULT_PID = "Personal id";
	private final static String DEFAULT_PASSWORD_EMPTY = "Password cannot be empty.";		
	private final static String DEFAULT_PASSWORD_REPEATED_EMPTY = "Repeated password cannot be empty.";		
	private final static String DEFAULT_PASSWORDS_NOT_SAME = "New passwords not the same.";		
	private final static String DEFAULT_PASSWORD_INVALID = "Invalid password.";		
	private final static String DEFAULT_PASSWORD_TOO_SHORT = "Password too short.";		
	private final static String DEFAULT_PASSWORD_CHAR_ILLEGAL = "Password contains illegal character(s).";		
	private final static String DEFAULT_PREFERENCES_SAVED = "Your preferences has been saved.";	
	
	public static final String CITIZEN_ACCOUNT_PREFERENCES_PROPERTIES = "citizen_account_preferences";
	public static final String USER_PROPERTY_USE_CO_ADDRESS = "cap_use_co_address";

	private User user = null;
	private boolean requirePasswordVerification = true;
	private boolean noVerificationForBankLogins = true;

  
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
	
	private void viewPreferencesForm(IWContext iwc){
		drawForm(iwc);
	}
	
	private void drawForm(IWContext iwc) {
		Form form = new Form();
		Table T = new Table();
		T.setCellpadding(2);
		T.setCellspacing(2);
		T.setBorder(0);
		form.add(T);
		
		Table table = new Table();	
//		table.setWidth(getWidth());
		table.setCellpadding(2);
		table.setCellspacing(2);
		table.setBorder(0);
		T.add(table, 1, 1);
		T.setWidth(2, 1, "20");
		
		T.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_BOTTOM);
		T.setVerticalAlignment(3, 1, Table.VERTICAL_ALIGN_BOTTOM);
		
		int row = 1;

		String personalID = PersonalIDFormatter.format(user.getPersonalID(), iwc.getIWMainApplication().getSettings().getApplicationLocale());
		
		
		table.add(new Break(2), 1, row);
		table.add(getSmallHeader(localize(KEY_PID, DEFAULT_PID)), 1, row);
		
		if (user.getPersonalID() != null) {
			table.add(getSmallText(personalID), 2, row);
			table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_BOTTOM);
			table.setVerticalAlignment(2, row, Table.VERTICAL_ALIGN_BOTTOM);
		}
		row++;
		table.add(getSmallHeader(localize(KEY_LOGIN, DEFAULT_LOGIN)), 1, row);
		LoginTable loginTable = LoginDBHandler.getUserLogin(((Integer) user.getPrimaryKey()).intValue());
		if (loginTable != null) {
			table.add(new HiddenInput(PARAMETER_OLD_LOGIN, loginTable.getUserLogin()), 2, row);
			table.add(getSmallText(loginTable.getUserLogin()), 2, row);
			table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_BOTTOM);
			table.setVerticalAlignment(2, row, Table.VERTICAL_ALIGN_BOTTOM);
		}

		
	
	
		String valueCurrentPassword = iwc.getParameter(PARAMETER_CURRENT_PASSWORD) != null ? iwc.getParameter(PARAMETER_CURRENT_PASSWORD) : "";
		String valueNewPassword = iwc.getParameter(PARAMETER_NEW_PASSWORD) != null ? iwc.getParameter(PARAMETER_NEW_PASSWORD) : "";
		String valueNewPasswordRepeated = iwc.getParameter(PARAMETER_NEW_PASSWORD_REPEATED) != null ? iwc.getParameter(PARAMETER_NEW_PASSWORD_REPEATED) : "";
		
	
		//Text tLogin = getSmallHeader(localize(KEY_LOGIN, DEFAULT_LOGIN));
		Text tCurrentPassword = getSmallHeader(localize(KEY_CURRENT_PASSWORD, DEFAULT_CURRENT_PASSWORD));
		Text tNewPassword = getSmallHeader(localize(KEY_NEW_PASSWORD, DEFAULT_NEW_PASSWORD));
		Text tNewPasswordRepeated = getSmallHeader(localize(KEY_NEW_PASSWORD_REPEATED, DEFAULT_NEW_PASSWORD_REPEATED));
		
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
		
		SubmitButton sbUpdate = (SubmitButton) getStyledInterface(new SubmitButton(localize(KEY_UPDATE, DEFAULT_UPDATE), PARAMETER_FORM_SUBMIT, "true"));
		
		
		row++;
		table.setHeight(row, 12);

		if (requirePasswordVerification) {
			row++;
			table.add(tCurrentPassword, 1, row);
			table.add(tiCurrentPassword, 2, row);
		}

		row++;
		table.add(tNewPassword, 1, row);
		table.add(tiNewPassword, 2, row);

		row++;
		table.add(tNewPasswordRepeated, 1, row);
		table.add(tiNewPasswordRepeated, 2, row);
		
		row++;
		table.setHeight(row, 12);
	
		
		
		row++;
		table.mergeCells(1, row, 2, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		
		table.add(Text.NON_BREAKING_SPACE, 1, row);
		table.add(sbUpdate, 1, row);
		
		add(form);
	}
	
	private void updatePreferences(IWContext iwc)  throws Exception {
		LoginTable loginTable = LoginDBHandler.getUserLogin(((Integer) user.getPrimaryKey()).intValue());
		String login    = loginTable.getUserLogin();
		String currentPassword = iwc.getParameter(PARAMETER_CURRENT_PASSWORD);
		String newPassword1 = iwc.getParameter(PARAMETER_NEW_PASSWORD);
		String newPassword2 = iwc.getParameter(PARAMETER_NEW_PASSWORD_REPEATED);		
	
		String errorMessage = null;
		boolean updatePassword = false;
		
		try {
		    
		    // if authorized by bank id we allow the user change his preferences
		    if(authorizedByBankID(iwc)){
		        
		    }
		    else if (requirePasswordVerification && !LoginDBHandler.verifyPassword(login, currentPassword)) {
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
					} else if ((c == '?') || (c == '€') || (c == '…')) {
						isPasswordCharOK = true;
					}
					if (!isPasswordCharOK) {
						throw new Exception(localize(KEY_PASSWORD_CHAR_ILLEGAL, DEFAULT_PASSWORD_CHAR_ILLEGAL));
					}
				}
				updatePassword = true;
			}
		} catch (Exception e) {
			errorMessage = e.getMessage();
		}

		if (errorMessage != null) {
			add(getErrorText(" " + errorMessage));
		} else {
			// Ok to update preferences
			//UserBusiness ub = (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);

			if (updatePassword) {
				LoginDBHandler.updateLogin(((Integer)user.getPrimaryKey()).intValue(), login, newPassword1);
			}
			
			
		}
		drawForm(iwc);
		if (errorMessage == null) {
			add(new Break());
			add(getLocalizedText(KEY_PREFERENCES_SAVED, DEFAULT_PREFERENCES_SAVED));
		}
	}
	
	/**
     * @param user2
     * @return
	 * @throws RemoteException
     */
    private boolean authorizedByBankID(IWContext iwc) throws RemoteException  {
        return noVerificationForBankLogins && getUserBusiness(iwc).hasBankLogin(iwc.getCurrentUser());
    }

  
	/**
	 * @param requirePasswordVerification The requirePasswordVerification to set.
	 */
	public void setRequirePasswordVerification(boolean requirePasswordVerification) {
		this.requirePasswordVerification = requirePasswordVerification;
	}

    public boolean isNoVerificationForBankLogins() {
        return noVerificationForBankLogins;
    }
    
    public void setNoVerificationForBankLogins(
            boolean noVerificationForBankLogins) {
        this.noVerificationForBankLogins = noVerificationForBankLogins;
    }
    
   
}
