package se.idega.idegaweb.commune.account.citizen.presentation;

import java.rmi.RemoteException;
import java.util.Calendar;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusiness;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.util.PIDChecker;

import com.idega.business.IBOLookup;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.LoginRecord;
import com.idega.core.accesscontrol.data.LoginRecordHome;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.builder.data.ICPage;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import com.idega.util.LocaleUtil;
import com.idega.util.text.SocialSecurityNumber;


/**
 *  * 
 * 
 * Title:         idegaWeb
 * Description:   This class handles the case, when a user has forgotten his password.  
 *                The presentation provides a single input field for the personal id. 
 *                After submitting the input is checked. 
 *                If the inputfield is empty a warning dialog pops up.
 *                If the input represents an impossible social security number (SSN) 
 *                an error message is returned (with the inputfield again). If the input is a
 *                possible valid ssn the value is checked if this ssn can be found in the table 
 *                of already known citizen in the database.
 *                If the ssn is unknown a link to the citizen application form is returned.
 *                If the ssn is known it is checked if the citizen has already activated his account.
 *                If the citizen has not an activated account yet again a link to to the citizen 
 *                application form is returned.
 *                There are two different cases if the citizen has already an activated account:
 *                If the user has never logged in you can not trust his registered email address. 
 *                Therefore a new password is generated and send by regular post 
 *                to the person.
 *                If the the user has logged at some time a new password is generated and send by email.
 *                  
 * Copyright:     Copyright (c) 2002
 * Company:       idega software
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
    "A letter containing the new password is sent to you.";      
  
  private final static String EMAIL_SENT_KEY = "cafp_email_was_sent";
  private final static String EMAIL_SENT_DEFAULT = 
    "An email containing the new password is sent to you.";
  
  private final static String ACCOUNT_APPLICATION_KEY = "cafp_go_to_citizen_account";
  private final static String ACCOUNT_APPLICATION_DEFAULT = 
    "You have not applied for a citizen account yet.";
  private final static String ACCOUNT_APPLICATION_LINK_KEY = "cafp_link_to_citizen_account_application";
  private final static String ACCOUNT_APPLICATION_LINK_DEFAULT = "Apply for a citizen account!";  
  
  private final static String FORMAT_ERROR_KEY = "caa_format_error";
  private final static String FORMAT_ERROR_DEFAULT = "Felaktigt inmatat värde";
  
  private final static String FORM_SUBMIT_KEY = "cafp_form_submit_key";
  private final static String FORM_SUBMIT_DEFAULT = "Forgot my password";
	
	private final static String ERROR_FIELD_CAN_NOT_BE_EMPTY_DEFAULT = "Fältet måste fyllas i";
  private final static String ERROR_FIELD_CAN_NOT_BE_EMPTY_KEY = "caf_field_can_not_be_empty";

  private final static String ACTION_VIEW_FORM = "action_view_form";
  private final static String ACTION_FORM_SUBMIT = "action_form_submit";  
  
  /** The color of all messages shown. */
  private final static String COLOR_RED = "#ff0000";

	/** Contains the page where the user can apply for a new account. */
  private ICPage citizenAccountApplicationPage;
  
  /** Used when an e-mail should always be sent and never a letter (unless user has no e-mail) */
  private boolean alwaysSendEmail = false;

  /** Sets the page where a user can apply for a new account.
   * @param citizenAccountApplicationPage the page where the user can apply for a new account.
   */
  public void setCitizenAccountApplicationPage(ICPage citizenAccountApplicationPage) {
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
	 * Handles the input. Checks if the input is a possible valid ssn and if the 
   * user is known or unknown.
	 * @param iwc 
	 */
	private void submitForm(IWContext iwc) {
    String ssn = getSsn(SSN_KEY, iwc);
    // in does not represent a possible ssn number
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
      final User user = business.getUser(ssn, iwc.getApplicationSettings().getDefaultLocale());
      if (user == null)
        // user unknown show link to "Citizen account application"
        viewCitizienAccountApplication();
      else {
        // user is known
         handleKnownUser(user, iwc);
      }
    }
    catch (Exception ex)  {
      viewError(ex.getMessage(),iwc);
    }
	}

	/**
	 * Builds a presentation containing the link to the citizen application page.
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

  /**
   * Handles the case if the user is a known user with or without an activated account.
   * @param user the known user.
   * @param iwc
   * @throws RemoteException
   * @throws CreateException
   */
  private void handleKnownUser(User user, IWContext iwc) throws RemoteException, CreateException {
    int userID = ((Integer)user.getPrimaryKey()).intValue();
    LoginTable loginTable = LoginDBHandler.getUserLogin(userID);
    if (loginTable == null) {
      viewCitizienAccountApplication();
      return;
    }
    
    IWResourceBundle bundle = getResourceBundle();    
    // check if user has ever logged in
    int loginID = ((Integer)loginTable.getPrimaryKey()).intValue();
    boolean lastLoginRecordWasFound;
    try {
      ((LoginRecordHome) com.idega.data.IDOLookup.getHomeLegacy(LoginRecord.class)).findByLoginID(loginID);
        // last login was found
      lastLoginRecordWasFound = true;  
    }
    catch (FinderException ex) {
      // last login record was not found  
      lastLoginRecordWasFound = false;
    }
    if (alwaysSendEmail) {
    		lastLoginRecordWasFound = true;
    }
    String message;
    // different messages are returned depending on the result 
    // if the user has ever logged in
    if (lastLoginRecordWasFound)  {
      // email is sent
      handleKnownUserLoggedIn(loginTable, user, iwc);
      message = bundle.getLocalizedString(EMAIL_SENT_KEY, EMAIL_SENT_DEFAULT);
    }
    else {
      // letter is sent
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
	 * Handles known user, that has logged in at some time. 
   * Creates new password, stores it and send it by an email to the user.
   * @param loginTable  login of the user
   * @param user        user
   * @throws RemoteException
   * @throws CreateException
	 */
	private void handleKnownUserLoggedIn(LoginTable loginTable, User user, IWContext iwc) throws RemoteException, CreateException  {
    String newPassword = createNewPassword();
    CitizenAccountBusiness business = getBusiness(iwc);
    business.changePasswordAndSendLetterOrEmail(loginTable, user,newPassword, false, true);
	}

	/**
	 * Handles known user, that has never logged in.
   * Creates new password, stores it and send it by a letter to the user.
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
	 * Builds a presentation containing the form with input field and submit button.
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
	 * Gets submit button.
	 * @param submitId
	 * @param defaultText
	 * @return SubmitButton
	 */
	private SubmitButton getSubmitButton(final String submitId, final String defaultText) {
		return (SubmitButton) getButton(new SubmitButton(submitId, localize(submitId, defaultText)));
  }


	/**
	 * Puts an input field for the social security number into the specified table.
	 * @param table
   * @param iwc
	 */
	private void addSimpleInputs(Table table, IWContext iwc) {
    Text header = getSmallHeader(localize(SSN_KEY, SSN_DEFAULT));
    table.add(header, 1, 1);
    table.add(getSingleInput(SSN_KEY, 12, true, iwc), 3, 1);
	}


  /**
   * Gets input field for the social security number.
   * @param iwc
   * @param paramID 
   * @param maxLength
   * @param notEmpty
   * @return the input field
   */
  private TextInput getSingleInput (final String paramId,
                                    final int maxLength, boolean notEmpty, IWContext iwc) {
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
	 * Creates table.
	 * @return the table
	 */
	private Table createTable() {
    final Table table = new Table();
    table.setCellspacing(getCellpadding());
    table.setCellpadding(0);
    table.setWidth(2, "12");
    return table;
	}

			
  /** 
   * Parses the parameter string.
   * @param iwc
   * @return	either string for action "view form" 
   * or string for action "form was submitted".		
   */
	private String parseAction(final IWContext iwc) {
		String action = ACTION_VIEW_FORM;

		if (iwc.isParameterSet(FORM_SUBMIT_KEY)) 
			action = ACTION_FORM_SUBMIT;			
			
		return action;
	}
			
  /** Builds a presentation containing an error message and the inputfield
   * @param errorMessage the message to be shown.
   * @param iwc
   */
  private void viewError(String errorMessage, IWContext iwc) {
    final Text text = new Text(errorMessage, true, false, false);
    text.setFontColor(COLOR_RED);
    add(text);
    add(Text.getBreak());
    viewForm(iwc);
  } 


  /**
   * Gets social security number. Checks if the parameter 
   * that corresponds to the specified key represents a possible 
   * social security number. Returns the number without any non
   * digit characters. Returns null if the parameter does not represent 
   * a possible social security number. This method does not check if the 
   * social security number is a real existing one. 
   * This method does not use any database access.
   * @param iwc
   * @param key the key of the social security number parameter.
   * @return a digit string representing a (possible) social security number else null.
   */
  private String getSsn( final String key, final IWContext iwc) {
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
    
    if (iwc.getApplicationSettings().getDefaultLocale().equals(LocaleUtil.getSwedishLocale())) {
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
    else if (iwc.getApplicationSettings().getDefaultLocale().equals(LocaleUtil.getIcelandicLocale())) {
    		if (SocialSecurityNumber.isValidIcelandicSocialSecurityNumber(digitOnlyInput.toString())) {
    			return digitOnlyInput.toString();
    		}
    	}
    return null;
  }
  

  /**
   * Looks up service bean citizen account business
   * @param iwc
   * @return a service bean CitizenAccountBusiness.
   */  
  private CitizenAccountBusiness getBusiness(IWContext iwc) throws RemoteException  {
    return (CitizenAccountBusiness) IBOLookup.getServiceInstance(iwc, CitizenAccountBusiness.class);
  }
  
  /**
   * Creates a new unencrypted password.
   * @return an unencrypted password.
   */
  private String createNewPassword() {
    return LoginDBHandler.getGeneratedPasswordForUser();
  }
   
	/**
	 * @param alwaysSendEmail The alwaysSendEmail to set.
	 */
	public void setAlwaysSendEmail(boolean alwaysSendEmail) {
		this.alwaysSendEmail = alwaysSendEmail;
	}
}