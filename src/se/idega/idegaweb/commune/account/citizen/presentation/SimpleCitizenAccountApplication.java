/*
 * Created on 2004-maj-11
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */

/**
 * @author Malin
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */

package se.idega.idegaweb.commune.account.citizen.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusiness;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import com.idega.business.IBOLookup;
import com.idega.core.accesscontrol.business.UserHasLoginException;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.accesscontrol.data.LoginTableHome;
import com.idega.core.location.business.CommuneBusiness;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import com.idega.util.Age;
import com.idega.util.text.SocialSecurityNumber;

/**
 * SimpleCitizenAccountApplication is an IdegaWeb block that inputs and handles
 * applications for citizen accounts. It is based on CitizenAccountApplication
 * but in a simplier version. To use this all the applicants needs to be in the
 * database. It is based on session ejb classes in
 * {@link se.idega.idegaweb.commune.account.citizen.business}and entity ejb
 * classes in {@link se.idega.idegaweb.commune.account.citizen.business.data}.
 * <p>
 * Last modified: $Date: 2005/03/31 19:48:50 $ by $Author: laddi $
 * 
 * @author <a href="mail:palli@idega.is">Pall Helgason </a>
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg </a>
 * @author <a href="mail:malin.anulf@agurait.com">Malin Anulf </a>
 * @version $Revision: 1.10 $
 */
public class SimpleCitizenAccountApplication extends CommuneBlock {

	private final static int ACTION_VIEW_FORM = 0;
	private final static int ACTION_SUBMIT_SIMPLE_FORM = 1;

	private final static String EMAIL_DEFAULT = "Email";
	private final static String EMAIL_KEY = "scaa_email";
	private final static String EMAIL_KEY_REPEAT = "scaa_email_repeat";
	private final static String EMAIL_REPEAT_DEFAULT = "Email again";
	private final static String PHONE_HOME_KEY = "scaa_phone_home";
	private final static String PHONE_HOME_DEFAULT = "Phone";
	private final static String PHONE_CELL_KEY = "scaa_cell_phone";
	private final static String PHONE_CELL_DEFAULT = "Cell phone";
	private final static String CITIZEN_ACCOUNT_INFO_KEY = "scaa_account_info";
	private final static String CITIZEN_ACCOUNT_INFO_DEFAULT = "If you don't have an email addrss your login info will be sent to you by snail mail.";
	private final static String MANDATORY_FIELD_EXPL_KEY = "scaa_mandatory_field_expl";
	private final static String MANDATORY_FIELD_EXPL_DEFAULT = "Fields marked with a star (*) are mandatory";
	private final static String UNKNOWN_CITIZEN_KEY = "scaa_unknown_citizen";
	private final static String UNKNOWN_CITIZEN_DEFAULT = "Something is wrong with your personal id. Please try again or contact the responsible";
	private final static String YOU_MUST_BE_18_KEY = "scaa_youMustBe18";
	private final static String YOU_MUST_BE_18_DEFAULT = "You have to be 18 to apply for a citizen account";

	private boolean _sendEmail = false;

	private final static String SSN_DEFAULT = "Personnummer";
	private 	final static String SSN_KEY = "caa_ssn";
	private final static String TEXT_APPLICATION_SUBMITTED_DEFAULT = "Application is submitted.";
	private final static String TEXT_APPLICATION_SUBMITTED_KEY = "scaa_app_submitted";

	private final static String GOTO_FORGOT_PASSWORD_DEFAULT = "Klicka på länken \"Jag har glömt mitt användarnamn eller lösenord\"";
	private final static String GOTO_FORGOT_PASSWORD_KEY = "scaa_goto_forgot_password_key";

	private final static String USER_ALLREADY_HAS_A_LOGIN_DEFAULT = "You already have an account";
	private final static String USER_ALLREADY_HAS_A_LOGIN_KEY = "scaa_user_allready_has_a_login";

	private final static String SIMPLE_FORM_SUBMIT_KEY = "scaa_simpleSubmit";
	private final static String SIMPLE_FORM_SUBMIT_DEFAULT = "Forward >>";

	private final static String COLOR_RED = "#ff0000";

	private final static String ERROR_FIELD_CAN_NOT_BE_EMPTY_DEFAULT = "You have to fill in the field";
	private final static String ERROR_FIELD_CAN_NOT_BE_EMPTY_KEY = "scaa_field_can_not_be_empty";
	private final static String ERROR_NO_INSERT_DEFAULT = "Application could not be stored";
	private final static String ERROR_NO_INSERT_KEY = "scaa_unable_to_insert";
	private final static String ERROR_EMAILS_DONT_MATCH = "scaa_emails_dont_match";
	private final static String ERROR_EMAILS_DONT_MATCH_DEFAULT = "Emails don't match";
	private final static String ERROR_NOT_VALID_PERSONAL_ID = "scaa_not_valid_personal_id";
	private final static String ERROR_NOT_VALID_PERSONAL_ID_DEFAULT = "The personal ID is not valid";

	public void main(final IWContext iwc) {
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
			}
		}
		catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}
	}

	private void viewSimpleApplicationForm(final IWContext iwc) {
		final Table table = createTable();
		addSimpleInputs(table, iwc);
		table.setHeight(table.getRows() + 1, 12);

		SubmitButton button = getSubmitButton(SIMPLE_FORM_SUBMIT_KEY + "_button", SIMPLE_FORM_SUBMIT_DEFAULT);
		table.add(button, 3, table.getRows() + 1);
		table.add(Text.getNonBrakingSpace(), 3, table.getRows());
		table.add(getHelpButton("registration_help_key"), 3, table.getRows());
		final Form accountForm = new Form();
		accountForm.add(table);
		accountForm.setToDisableOnSubmit(button, true);
		accountForm.addParameter(SIMPLE_FORM_SUBMIT_KEY, Boolean.TRUE.toString());
		add(accountForm);
	}

	private void submitSimpleForm(final IWContext iwc) {
		try {
			String ssn = iwc.getParameter(SSN_KEY);
			if (!isOver18(ssn)) { throw new Exception(localize(YOU_MUST_BE_18_KEY, YOU_MUST_BE_18_DEFAULT)); }
			String email = iwc.getParameter(EMAIL_KEY).toString();
			String emailRepeat = iwc.getParameter(EMAIL_KEY_REPEAT).toString();
			String phoneHome = iwc.getParameter(PHONE_HOME_KEY).toString();
			String phoneWork = iwc.getParameter(PHONE_CELL_KEY).toString();
			CitizenAccountBusiness business = (CitizenAccountBusiness) IBOLookup.getServiceInstance(iwc, CitizenAccountBusiness.class);
			User user = business.getUserIcelandic(ssn);
			Collection logins = new ArrayList();
			try {
				logins.addAll(getLoginTableHome().findLoginsForUser(user));
			}
			catch (Exception e) {
				// no problem, no login found
			}
			if (user != null && !logins.isEmpty()) { throw new UserHasLoginException(); }
			if (user == null) {
				// unknown or user not in system applies
				final Text text = new Text(localize(UNKNOWN_CITIZEN_KEY, UNKNOWN_CITIZEN_DEFAULT));
				text.setFontColor(COLOR_RED);
				add(text);
				add(new Break(2));
				viewSimpleApplicationForm(iwc);
			}
			if (email != null && email.length() > 0) {
				if (emailRepeat == null || !email.equals(emailRepeat)) {
					throw new Exception(localize(ERROR_EMAILS_DONT_MATCH, ERROR_EMAILS_DONT_MATCH_DEFAULT));
				}
			}
			else if (null == business.insertApplication(iwc, user, ssn, email, phoneHome, phoneWork, _sendEmail)) {
				// known user applied, but couldn't be submitted
				throw new Exception(localize(ERROR_NO_INSERT_KEY, ERROR_NO_INSERT_DEFAULT));
			}
			else {
				// known user applied and was submitted
				if (getResponsePage() != null) {
					iwc.forwardToIBPage(getParentPage(), getResponsePage());
				}
				else {
					add(new Text(localize(TEXT_APPLICATION_SUBMITTED_KEY, TEXT_APPLICATION_SUBMITTED_DEFAULT)));
				}
			}
		}
		catch (UserHasLoginException uhle) {
			Text text = new Text(localize(USER_ALLREADY_HAS_A_LOGIN_KEY, USER_ALLREADY_HAS_A_LOGIN_DEFAULT) + ". " + localize(GOTO_FORGOT_PASSWORD_KEY, GOTO_FORGOT_PASSWORD_DEFAULT) + '.', true, false, false);
			text.setFontColor(COLOR_RED);
			add(text);
			add(new Break(2));
			viewSimpleApplicationForm(iwc);
		}
		catch (Exception e) {
			Text text = new Text(e.getMessage(), true, false, false);
			text.setFontColor(COLOR_RED);
			add(text);
			add(new Break(2));
			viewSimpleApplicationForm(iwc);
		}
	}

	private Table createTable() {
		final Table table = new Table();
		table.setCellspacing(getCellpadding());
		table.setCellpadding(0);
		table.setWidth(2, "12");
		return table;
	}

	private void addSimpleInputs(final Table table, final IWContext iwc) {
		int row = 1;
		table.add(getHeader(SSN_KEY, SSN_DEFAULT), 1, row);
		table.add(getErrorText("*"), 1, row++);
		TextInput ssnInput = getSingleInput(iwc, SSN_KEY, 25, true);
		ssnInput.setAsPersonalID(iwc.getApplicationSettings().getDefaultLocale(), localize(ERROR_NOT_VALID_PERSONAL_ID, ERROR_NOT_VALID_PERSONAL_ID_DEFAULT));
		table.add(ssnInput, 1, row++);

		table.add(getHeader(EMAIL_KEY, EMAIL_DEFAULT), 1, row++);
		table.add(getSingleInput(iwc, EMAIL_KEY, 25, 255, false), 1, row++);

		table.add(getHeader(EMAIL_KEY_REPEAT, EMAIL_REPEAT_DEFAULT), 1, row++);
		table.add(getSingleInput(iwc, EMAIL_KEY_REPEAT, 25, 255, false), 1, row++);

		table.add(getHeader(PHONE_CELL_KEY, PHONE_CELL_DEFAULT), 1, row++);
		table.add(getSingleInput(iwc, PHONE_CELL_KEY, 25, false), 1, row++);

		table.add(getHeader(PHONE_HOME_KEY, PHONE_HOME_DEFAULT), 1, row);
		table.add(getErrorText("*"), 1, row++);
		table.add(getSingleInput(iwc, PHONE_HOME_KEY, 25, true), 1, row++);

		table.setHeight(row++, 18);
		table.mergeCells(1, row, 3, row);
		table.add(getErrorText(localize(MANDATORY_FIELD_EXPL_KEY, MANDATORY_FIELD_EXPL_DEFAULT)), 1, row++);
		table.mergeCells(1, row, 3, row);
		table.add(getSmallText(localize(CITIZEN_ACCOUNT_INFO_KEY, CITIZEN_ACCOUNT_INFO_DEFAULT)), 1, row++);
	}

	private TextInput getSingleInput(IWContext iwc, final String paramId, final int maxLength, boolean notEmpty) {
		return getSingleInput(iwc,paramId,maxLength,maxLength,notEmpty);
	}
	
	private TextInput getSingleInput(IWContext iwc, final String paramId, final int length,final int maxLength, boolean notEmpty) {
		TextInput textInput = (TextInput) getStyledInterface(new TextInput(paramId));
		textInput.setLength(maxLength);
		textInput.setMaxlength(maxLength);
		textInput.setSize(length);
		if (notEmpty) {
			final String fieldCanNotBeEmpty = localize(ERROR_FIELD_CAN_NOT_BE_EMPTY_KEY, ERROR_FIELD_CAN_NOT_BE_EMPTY_DEFAULT);
			String name = localize(paramId, paramId);
			if ((name == null || name.trim().length() == 0 || name.equals(paramId)) && paramId.lastIndexOf("scaa_") > 1) {
				final int secondIndex = paramId.indexOf("scaa_", 2);
				final String shortKey = paramId.substring(0, secondIndex);
				name = localize(shortKey, shortKey);
			}
			textInput.setAsNotEmpty(fieldCanNotBeEmpty + ": " + name);
		}
		String param = iwc.getParameter(paramId);
		if (param != null) {
			textInput.setContent(param);
		}
		return textInput;
	}

	private Text getHeader(final String paramId, final String defaultText) {
		return getSmallHeader(localize(paramId, defaultText));
	}

	private SubmitButton getSubmitButton(final String submitId, final String defaultText) {
		return (SubmitButton) getButton(new SubmitButton(submitId, localize(submitId, defaultText)));
	}

	private boolean isOver18(final String ssn) {
		if (ssn == null || !SocialSecurityNumber.isValidIcelandicSocialSecurityNumber(ssn)) { throw new IllegalArgumentException("'" + ssn + "' isn't a SSN"); }
		Age age = new Age(SocialSecurityNumber.getDateFromSocialSecurityNumber(ssn));

		return age.getYears() >= 18;
	}

	private int parseAction(final IWContext iwc) {
		int action = ACTION_VIEW_FORM;

		if (iwc.isParameterSet(SIMPLE_FORM_SUBMIT_KEY)) {
			action = ACTION_SUBMIT_SIMPLE_FORM;
		}

		return action;
	}

	protected CommuneBusiness getCommuneBusiness(IWApplicationContext iwac) throws RemoteException {
		return (CommuneBusiness) IBOLookup.getServiceInstance(iwac, CommuneBusiness.class);
	}

	private LoginTableHome getLoginTableHome() {
		try {
			return (LoginTableHome) IDOLookup.getHome(LoginTable.class);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param sendEmail.
	 *          If set, email will be sent when application is sent.
	 */
	public void setSendEmail(boolean sendEmail) {
		_sendEmail = sendEmail;
	}

	/**
	 * Returns the _sendEmail.
	 * 
	 * @return boolean
	 */
	public boolean getSendEmail() {
		return _sendEmail;
	}
}