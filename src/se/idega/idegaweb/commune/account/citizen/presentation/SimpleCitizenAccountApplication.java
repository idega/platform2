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
 * Last modified: $Date: 2004/05/24 23:04:07 $ by $Author: laddi $
 * 
 * @author <a href="mail:palli@idega.is">Pall Helgason </a>
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg </a>
 * @author <a href="mail:malin.anulf@agurait.com">Malin Anulf </a>
 * @version $Revision: 1.4 $
 */
public class SimpleCitizenAccountApplication extends CommuneBlock {

	private final static int ACTION_VIEW_FORM = 0;
	private final static int ACTION_SUBMIT_SIMPLE_FORM = 1;

	final static String NO_DEFAULT = "No";
	final static String NO_KEY = "caa_no";

	final static String EMAIL_DEFAULT = "Email";
	final static String EMAIL_KEY = "scaa_email";
	final static String PHONE_HOME_KEY = "scaa_phone_home";
	final static String PHONE_HOME_DEFAULT = "Phone";
	final static String PHONE_CELL_KEY = "scaa_cell_phone";
	final static String PHONE_CELL_DEFAULT = "Cell phone";
	final static String CITIZEN_ACCOUNT_INFO_KEY = "scaa_account_info";
	final static String CITIZEN_ACCOUNT_INFO_DEFAULT = "If you don't have an email addrss your login info will be sent to you by snail mail.";
	final static String MANDATORY_FIELD_EXPL_KEY = "scaa_mandatory_field_expl";
	final static String MANDATORY_FIELD_EXPL_DEFAULT = "Fields marked with a star (*) are mandatory";
	final static String PERSONAL_ID_CELL_CONNECTION_KEY = "scaa_personal_id_cell_connection";
	final static String PERSONAL_ID_CELL_CONNECTION_DEFAULT = "You must enter the personal id that is registered on your mobile phone";
	private final static String UNKNOWN_CITIZEN_KEY = "scaa_unknown_citizen";
	private final static String UNKNOWN_CITIZEN_DEFAULT = "Something is wrong with your personal id. Please try again or contact the responsible";
	private final static String YOU_MUST_BE_18_KEY = "scaa_youMustBe18";
	private final static String YOU_MUST_BE_18_DEFAULT = "You have to be 18 to apply for a citizen account";

	private boolean _sendEmail = false;

	final static String SSN_DEFAULT = "Personnummer";
	final static String SSN_KEY = "caa_ssn";
	private final static String TEXT_APPLICATION_SUBMITTED_DEFAULT = "Application is submitted.";
	private final static String TEXT_APPLICATION_SUBMITTED_KEY = "scaa_app_submitted";

	private final static String GOTO_FORGOT_PASSWORD_DEFAULT = "Klicka på länken \"Jag har glömt mitt användarnamn eller lösenord\"";
	private final static String GOTO_FORGOT_PASSWORD_KEY = "scaa_goto_forgot_password_key";

	private final static String USER_ALLREADY_HAS_A_LOGIN_DEFAULT = "You already have an account";
	private final static String USER_ALLREADY_HAS_A_LOGIN_KEY = "scaa_user_allready_has_a_login";
	final static String YES_DEFAULT = "Yes";
	final static String YES_KEY = "caa_yes";

	private final static String SIMPLE_FORM_SUBMIT_KEY = "scaa_simpleSubmit";
	private final static String SIMPLE_FORM_SUBMIT_DEFAULT = "Forward >>";

	private final static String COLOR_RED = "#ff0000";

	private final static String ERROR_FIELD_CAN_NOT_BE_EMPTY_DEFAULT = "You have to fill in the field";
	private final static String ERROR_FIELD_CAN_NOT_BE_EMPTY_KEY = "scaa_field_can_not_be_empty";
	private final static String ERROR_NO_INSERT_DEFAULT = "Application could not be stored";
	private final static String ERROR_NO_INSERT_KEY = "scaa_unable_to_insert";

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

		table.add(getSubmitButton(SIMPLE_FORM_SUBMIT_KEY, SIMPLE_FORM_SUBMIT_DEFAULT), 3, table.getRows() + 1);
		final Form accountForm = new Form();
		accountForm.add(table);
		add(accountForm);
	}

	private Table getSimpleApplicationForm(final IWContext iwc) {
		final Table table = createTable();
		addSimpleInputs(table, iwc);
		table.setHeight(table.getRows() + 1, 12);

		table.add(getSubmitButton(SIMPLE_FORM_SUBMIT_KEY, SIMPLE_FORM_SUBMIT_DEFAULT), 3, table.getRows() + 1);
		final Form accountForm = new Form();
		accountForm.add(table);
		add(accountForm);

		return table;
	}

	private void submitSimpleForm(final IWContext iwc) {
		final Table table = createTable();
		int row = 1;
		try {
			final String ssn = iwc.getParameter(SSN_KEY);
			if (!isOver18(ssn)) { throw new Exception(localize(YOU_MUST_BE_18_KEY, YOU_MUST_BE_18_DEFAULT)); }
			final String email = iwc.getParameter(EMAIL_KEY).toString();
			final String phoneHome = iwc.getParameter(PHONE_HOME_KEY).toString();
			final String phoneWork = iwc.getParameter(PHONE_CELL_KEY).toString();
			final CitizenAccountBusiness business = (CitizenAccountBusiness) IBOLookup.getServiceInstance(iwc, CitizenAccountBusiness.class);
			final User user = business.getUserIcelandic(ssn);
			final Collection logins = new ArrayList();
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
				table.add(text, 1, row++);
				table.add(new Break(2), 1, row++);
				table.add(getSimpleApplicationForm(iwc), 1, row++);

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
		table.add(ssnInput, 1, row++);

		table.add(getHeader(PHONE_HOME_KEY, PHONE_HOME_DEFAULT), 1, row);
		table.add(getErrorText("*"), 1, row++);
		table.add(getSingleInput(iwc, PHONE_HOME_KEY, 25, true), 1, row++);

		table.add(getHeader(PHONE_CELL_KEY, PHONE_CELL_DEFAULT), 1, row++);
		table.add(getSingleInput(iwc, PHONE_CELL_KEY, 25, false), 1, row++);

		table.add(getHeader(EMAIL_KEY, EMAIL_DEFAULT), 1, row++);
		table.add(getSingleInput(iwc, EMAIL_KEY, 25, false), 1, row++);

		table.setHeight(row++, 18);
		table.mergeCells(1, row, 3, row);
		table.add(getErrorText(localize(MANDATORY_FIELD_EXPL_KEY, MANDATORY_FIELD_EXPL_DEFAULT)), 1, row++);
		table.mergeCells(1, row, 3, row);
		table.add(getSmallText(localize(CITIZEN_ACCOUNT_INFO_KEY, CITIZEN_ACCOUNT_INFO_DEFAULT)), 1, row++);
	}

	private TextInput getSingleInput(IWContext iwc, final String paramId, final int maxLength, boolean notEmpty) {
		TextInput textInput = (TextInput) getStyledInterface(new TextInput(paramId));
		textInput.setLength(maxLength);
		textInput.setMaxlength(maxLength);
		textInput.setSize(maxLength);
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