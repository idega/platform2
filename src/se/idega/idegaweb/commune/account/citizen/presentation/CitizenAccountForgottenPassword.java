package se.idega.idegaweb.commune.account.citizen.presentation;

import is.idega.idegaweb.golf.login.presentation.Login;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.builder.data.IBPage;
import com.idega.business.IBOLookup;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.LoginInfo;
import com.idega.core.accesscontrol.data.LoginRecord;
import com.idega.core.accesscontrol.data.LoginRecordHome;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.accesscontrol.data.LoginTableHome;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import com.idega.util.Encrypter;

import se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusiness;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.util.PIDChecker;


/**
 * Title:        idegaWeb
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      idega software
 * @author <a href="mailto:thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 */

public class CitizenAccountForgottenPassword extends CommuneBlock {
  
  private final static String SSN_KEY = "caf_ssn";
  private final static String SSN_DEFAULT = "Personnummer";
  
  private final static String PASSWORD_CREATED_KEY = "cafp_password_was_created";
  private final static String PASSWORD_CREATED_DEFAULT = 
    "A new password was generated.";
  
  private final static String LETTER_SENT_KEY = "cafp_letter_was_sent";
  private final static String LETTER_SENT_DEFAULT = 
    "A letter with the new password is sent to you.";      
  
  private final static String EMAIL_SENT_KEY = "cafp_email_was_sent";
  private final static String EMAIL_SENT_DEFAULT = 
    "An email with the new password is sent to you.";
  
  private final static String ACCOUNT_APPLICATION_KEY = "cafp_go_to_citizen_account";
  private final static String ACCOUNT_APPLICATION_DEFAULT = 
    "You have not applied for a citizen account yet.";
  private final static String ACCOUNT_APPLICATION_LINK_KEY = "cafp_link_to_citizen_account_application";
  private final static String ACCOUNT_APPLICATION_LINK_DEFAULT = "Apply for a citizen account!";  
  
  private final static String FORMAT_ERROR_KEY = "caa_format_error";
  private final static String FORMAT_ERROR_DEFAULT = "Felaktigt inmatat värde";
  
  private final static String FORM_SUBMIT_KEY = "caf_form_submit_key";
  private final static String FORM_SUBMIT_DEFAULT = "Skicka ansökan";
	
	private final static String ERROR_FIELD_CAN_NOT_BE_EMPTY_DEFAULT = "Fältet måste fyllas i";
  private final static String ERROR_FIELD_CAN_NOT_BE_EMPTY_KEY = "caf_field_can_not_be_empty";

  private final static String ACTION_VIEW_FORM = "action_view_form";
  private final static String ACTION_FORM_SUBMIT = "action_form_submit";  
  
  // display
  private final static String COLOR_RED = "#ff0000";

	private IBPage citizenAccountApplicationPage;


  public void setCitizenAccountApplicationPage(IBPage citizenAccountApplicationPage) {
    this.citizenAccountApplicationPage = citizenAccountApplicationPage;
  }


	public void main(final IWContext iwc) {
		setResourceBundle(getResourceBundle(iwc));

		String action = parseAction(iwc);
		if (ACTION_VIEW_FORM.equals(action))
			viewForm(iwc);
		else if (ACTION_FORM_SUBMIT.equals(action))
			submitForm(iwc);
	}

	/**
	 * Method submitForm.
	 * @param iwc
	 */
	private void submitForm(IWContext iwc) {
    String ssn = getSsn(iwc, SSN_KEY);
    if (ssn == null)  {
      // set error text and show input field again
      IWResourceBundle bundle = getResourceBundle();
      String displayName = bundle.getLocalizedString(SSN_KEY, SSN_DEFAULT);
      String formatErrorString = bundle.getLocalizedString( FORMAT_ERROR_KEY , FORMAT_ERROR_DEFAULT);
      viewError(formatErrorString + ": " + displayName, iwc);
      return;
    }
    try  {
      final CitizenAccountBusiness business = (CitizenAccountBusiness) IBOLookup.getServiceInstance(iwc, CitizenAccountBusiness.class);
      final User user = business.getUser(ssn);
      if (user == null)
        // user unknown show link to "Citizen account application"
        viewCitizienAccountApplication();
      else {
         handleKnownUser(user, iwc);
      }
    }
    catch (Exception ex)  {
      viewError(ex.getMessage(),iwc);
    }
	}

	/**
	 * Method viewCitizienAccountApplication.
	 */
	private void viewCitizienAccountApplication() {        
    IWResourceBundle bundle = getResourceBundle(); 
    Text text = new Text(bundle.getLocalizedString(ACCOUNT_APPLICATION_KEY,ACCOUNT_APPLICATION_DEFAULT),true, false, false);
    Text linkText = new Text(bundle.getLocalizedString(ACCOUNT_APPLICATION_LINK_KEY,ACCOUNT_APPLICATION_LINK_DEFAULT),true, false, false);
    text.setFontColor(COLOR_RED);
    add(text);
    add(Text.getBreak());
    Link link = new Link(linkText);
    if (citizenAccountApplicationPage != null)
      link.setPage(citizenAccountApplicationPage);
    add(link);
  }



  private void handleKnownUser(User user, IWContext iwc) throws RemoteException, CreateException {
    int userID = user.getID();
    LoginTable loginTable = LoginDBHandler.getUserLogin(userID);
    if (loginTable == null) {
      viewCitizienAccountApplication();
      return;
    }
    
    IWResourceBundle bundle = getResourceBundle();    
    // get login info because of access to password
    int loginID = loginTable.getID();
    // LoginInfo loginInfo = LoginDBHandler.getLoginInfo(loginTable.getID());
    LoginRecord loginRecord;
    boolean lastLoginRecordWasFound;
    try {
      loginRecord =
        ((LoginRecordHome) com.idega.data.IDOLookup.getHomeLegacy(LoginRecord.class)).findByLoginID(loginID);
      lastLoginRecordWasFound = true;  
    }
    catch (FinderException ex) {
      // last login record was not found  
      lastLoginRecordWasFound = false;
    }
    String message;
    if (lastLoginRecordWasFound)  {
      handleKnownUserLoggedIn(loginTable, user, iwc);
      message = bundle.getLocalizedString(EMAIL_SENT_KEY, EMAIL_SENT_DEFAULT);
    }
    else {
      handleKnownUserNeverLoggedIn(loginTable, user, iwc);
      message = bundle.getLocalizedString(LETTER_SENT_KEY, LETTER_SENT_DEFAULT);
    }
    String password = bundle.getLocalizedString(PASSWORD_CREATED_KEY, PASSWORD_CREATED_DEFAULT);
    Text textPassword = new Text(password, true, false, false);
    Text textMessage = new Text(message ,true, false, false);
    textPassword.setFontColor(COLOR_RED);
    textMessage.setFontColor(COLOR_RED);
    add(textPassword);
    add(Text.getBreak());
    add(textMessage);
      
  }

	/**
	 * Method handleKnownUserLoggedIn.
	 * 
	 */
	private void handleKnownUserLoggedIn(LoginTable loginTable, User user, IWContext iwc) throws RemoteException, CreateException  {
    String newPassword = createNewPassword();
    CitizenAccountBusiness business = getBusiness(iwc);
    business.changePasswordAndSendLetterOrEmail(loginTable, user,newPassword, false, true);
	}

	/**
	 * Method handleKnownUserNeverLoggedIn.
   * Creates new password, stores it and send a letter to the user
   * @param loginTable
	 * @param user
   * @param iwc
   * @throws RemoteException
   * @throws CreateException
	 */
	private void handleKnownUserNeverLoggedIn(LoginTable loginTable, User user, IWContext iwc) throws RemoteException, CreateException {
    // send a letter with the new password
    // create new password
    String newPassword = createNewPassword();
    CitizenAccountBusiness business = getBusiness(iwc);
    business.changePasswordAndSendLetterOrEmail(loginTable, user,newPassword, true, false);
	}


	/**
	 * Method viewForm.
	 * @param iwc
	 */
	private void viewForm(final IWContext iwc) {
	  final Table table = createTable();
    addSimpleInputs(table, iwc);
    table.setHeight(table.getRows() + 1, 12);
    table.add(getSubmitButton(FORM_SUBMIT_KEY, FORM_SUBMIT_DEFAULT), 1, table.getRows() + 1);
    final Form accountForm = new Form();
    accountForm.add(table);
    add(accountForm);
  }

	/**
	 * Method getSubmitButton.
	 * @param submitId
	 * @param defaultText
	 * @return SubmitButton
	 */
	private SubmitButton getSubmitButton(final String submitId, final String defaultText) {
		return (SubmitButton) getButton(new SubmitButton(submitId, localize(submitId, defaultText)));
  }


	/**
	 * Method addSimpleInputs.
	 * @param table
   * @param iwc
	 */
	private void addSimpleInputs(Table table, IWContext iwc) {
    Text header = getSmallHeader(localize(SSN_KEY, SSN_DEFAULT));
    table.add(header, 1, 1);
    table.add(getSingleInput(iwc, SSN_KEY, 12, true), 3, 1);
	}



  private TextInput getSingleInput (IWContext iwc, final String paramId,
                                      final int maxLength, boolean notEmpty) {
    TextInput textInput = (TextInput) getStyledInterface
                (new TextInput(paramId));
    textInput.setMaxlength(maxLength);
    if (notEmpty) {
            final String fieldCanNotBeEmpty = localize
                    (ERROR_FIELD_CAN_NOT_BE_EMPTY_KEY,
                     ERROR_FIELD_CAN_NOT_BE_EMPTY_DEFAULT);
            final String name = localize(paramId, paramId);
      textInput.setAsNotEmpty(fieldCanNotBeEmpty + ": " + name);
        }
    String param = iwc.getParameter(paramId);
    if (param != null) {
      textInput.setContent(param);
    }
    return textInput;
  }





	/**
	 * Method createTable.
	 * 
	 */
	private Table createTable() {
    final Table table = new Table();
    table.setCellspacing(getCellpadding());
    table.setCellpadding(0);
    table.setWidth(2, "12");
    table.setWidth(1, "180");
    return table;
	}

			
			
	private String parseAction(final IWContext iwc) {
		String action = ACTION_VIEW_FORM;

		if (iwc.isParameterSet(FORM_SUBMIT_KEY)) 
			action = ACTION_FORM_SUBMIT;			
			
		return action;
	}
			
    


  private void viewError(String errorMessage, IWContext iwc) {
    final Text text = new Text(errorMessage, true, false, false);
    text.setFontColor(COLOR_RED);
    add(text);
    add(Text.getBreak());
    viewForm(iwc);
  } 
			

  private String getSsn(final IWContext iwc, final String key) {
    final String rawInput = iwc.getParameter(key);
    if (rawInput == null) {
      return null;
    }
    final StringBuffer digitOnlyInput = new StringBuffer();
    for (int i = 0; i < rawInput.length(); i++) {
      if (Character.isDigit(rawInput.charAt(i))) {
        digitOnlyInput.append(rawInput.charAt(i));
      }
    }
    final Calendar rightNow = Calendar.getInstance();
    final int currentYear = rightNow.get(Calendar.YEAR);
    if (digitOnlyInput.length() == 10) {
      final int inputYear = new Integer(digitOnlyInput.substring(0, 2)).intValue();
      final int century = inputYear + 2000 > currentYear ? 19 : 20;
      digitOnlyInput.insert(0, century);
    }
        final PIDChecker pidChecker = PIDChecker.getInstance ();
    if (digitOnlyInput.length() != 12
            || !pidChecker.isValid (digitOnlyInput.toString ())) {
      return null;
    }
    final int year = new Integer(digitOnlyInput.substring(0, 4)).intValue();
    final int month = new Integer(digitOnlyInput.substring(4, 6)).intValue();
    final int day = new Integer(digitOnlyInput.substring(6, 8)).intValue();
    if (year < 1880 || year > currentYear || month < 1 || month > 12 || day < 1 || day > 31) {
      return null;
    }
    return digitOnlyInput.toString();
  }
  
  
  private CitizenAccountBusiness getBusiness(IWContext iwc) throws RemoteException  {
    return (CitizenAccountBusiness) IBOLookup.getServiceInstance(iwc, CitizenAccountBusiness.class);
  }
  
  
  private String createNewPassword() {
    return com.idega.util.StringHandler.getRandomString(8);
  }
   
}






