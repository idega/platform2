/*
 * Created on 2004-maj-11
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */


/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */


package se.idega.idegaweb.commune.account.citizen.presentation;

import com.idega.business.IBOLookup;
import com.idega.core.accesscontrol.business.UserHasLoginException;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.accesscontrol.data.LoginTableHome;
import com.idega.core.builder.data.ICPage;
import com.idega.core.location.business.CommuneBusiness;
import com.idega.core.location.data.Commune;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.text.SocialSecurityNumber;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import se.idega.block.pki.business.NBSLoggedOnInfo;
import se.idega.block.pki.business.NBSLoginBusinessBean;
import se.idega.block.pki.presentation.NBSLogin;
import se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusiness;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.presentation.CommuneBlock;


/**
 * SimpleCitizenAccountApplication is an IdegaWeb block that inputs and handles
 * applications for citizen accounts. It is based on CitizenAccountApplication but in a 
 * simplier version. To use this all the applicants needs to be in the database.
 * It is based on session ejb classes in
 * {@link se.idega.idegaweb.commune.account.citizen.business} and entity ejb
 * classes in {@link se.idega.idegaweb.commune.account.citizen.business.data}.
 * <p>
 * Last modified: $Date: 2004/05/18 13:44:09 $ by $Author: malin $
 *
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @author <a href="mail:malin.anulf@agurait.com">Malin Anulf</a>
 * @version $Revision: 1.1 $
 */
public class SimpleCitizenAccountApplication extends CommuneBlock {
	private final static int ACTION_VIEW_FORM = 0;
	private final static int ACTION_SUBMIT_SIMPLE_FORM = 1;
	private final static int ACTION_SUBMIT_UNKNOWN_CITIZEN_FORM_1 = 2;
	private final static int ACTION_SUBMIT_UNKNOWN_CITIZEN_FORM_2 = 3;
	
	final static String NO_DEFAULT = "Nej";
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
	private ICPage _infoPage;
	
	final static String SSN_DEFAULT = "Personnummer";
	final static String SSN_KEY = "caa_ssn";
	private final static String TEXT_APPLICATION_SUBMITTED_DEFAULT = "Application is submitted.";
	private final static String TEXT_APPLICATION_SUBMITTED_KEY = "scaa_app_submitted";
	
	private final static String GOTO_FORGOT_PASSWORD_DEFAULT = "Klicka på länken \"Jag har glömt mitt användarnamn eller lösenord\"";
	private final static String GOTO_FORGOT_PASSWORD_KEY = "scaa_goto_forgot_password_key";
	private final static String ONLY_ONE_PERSON_PER_SSN_KEY = "caa_only_one_person_per_ssn";
	private final static String ONLY_ONE_PERSON_PER_SSN_DEFAULT = "Flera personer kan inte ha personnummer";
	
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
	
	private boolean sendUserToHomePage = true;
	
	public void main(final IWContext iwc) {
		setResourceBundle(getResourceBundle(iwc));
		
		try {
			int action = parseAction(iwc);
			switch (action) {
			case ACTION_VIEW_FORM :
				viewSimpleApplicationForm(iwc);
				break;
			case ACTION_SUBMIT_SIMPLE_FORM :
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
		
		final Collection mandatoryParametersNames = Collections.singleton(SSN_KEY);
		final Collection stringParameterNames = Arrays.asList(new String[] { EMAIL_KEY, PHONE_HOME_KEY, PHONE_CELL_KEY });
		final Collection ssnParameterNames = Collections.singleton(SSN_KEY);
		final Collection integerParameters = Collections.EMPTY_LIST;
		
		final Table table = createTable();
		int row = 1;
		try {
			final Map parameters = parseParameters(getResourceBundle(), iwc, mandatoryParametersNames, stringParameterNames, ssnParameterNames, integerParameters);
			final String ssn = parameters.get(SSN_KEY).toString();
			if (!isOver18(ssn)) { 
				throw new ParseException(localize(YOU_MUST_BE_18_KEY, YOU_MUST_BE_18_DEFAULT));
			}
			final String email = parameters.get(EMAIL_KEY).toString();
			final String phoneHome = parameters.get(PHONE_HOME_KEY).toString();
			final String phoneWork = parameters.get(PHONE_CELL_KEY).toString();
			final CitizenAccountBusiness business = (CitizenAccountBusiness) IBOLookup.getServiceInstance(iwc, CitizenAccountBusiness.class);
			final User user = business.getUserIcelandic(ssn);
			final Collection logins = new ArrayList ();
			try {
				logins.addAll (getLoginTableHome ().findLoginsForUser (user));
			} catch (Exception e) {
				// no problem, no login found
			}
			if (user != null && !logins.isEmpty()) {
				throw new UserHasLoginException ();
			}
			if (user == null || !citizenLivesInCommune(iwc, user)) {
				// unknown or user not in system applies
				final Text text = new Text(localize(UNKNOWN_CITIZEN_KEY, UNKNOWN_CITIZEN_DEFAULT));
				text.setFontColor(COLOR_RED);
				table.add(text, 1, row++);
				table.add(new Break(2), 1, row++);
				table.add(getSimpleApplicationForm(iwc), 1, row++);
				
			} else if (null == business.insertApplication(iwc,user, ssn, email, phoneHome, phoneWork, _sendEmail)) {
				// known user applied, but couldn't be submitted
				throw new Exception(localize(ERROR_NO_INSERT_KEY, ERROR_NO_INSERT_DEFAULT));
			} else {
				// known user applied and was submitted
				
				NBSLoginBusinessBean loginBusiness = NBSLoginBusinessBean.createNBSLoginBusiness();
				NBSLoggedOnInfo info = loginBusiness.getBankIDLoggedOnInfo(iwc);
			
								
				if(info != null && sendUserToHomePage){
					Group primaryGroup = user.getPrimaryGroup();
					if (user.getHomePageID() != -1)
						iwc.forwardToIBPage(this.getParentPage(), user.getHomePage());
					if (primaryGroup != null && primaryGroup.getHomePageID() != -1)
						iwc.forwardToIBPage(this.getParentPage(), primaryGroup.getHomePage());
				} else if (getResponsePage() != null) {
					iwc.forwardToIBPage(getParentPage(), getResponsePage());
				} else {
					add(new Text(localize(TEXT_APPLICATION_SUBMITTED_KEY, TEXT_APPLICATION_SUBMITTED_DEFAULT)));
				}
			}
		}	catch (UserHasLoginException uhle) {
			final Text text = new Text(localize(USER_ALLREADY_HAS_A_LOGIN_KEY, USER_ALLREADY_HAS_A_LOGIN_DEFAULT) + ". " + localize(GOTO_FORGOT_PASSWORD_KEY, GOTO_FORGOT_PASSWORD_DEFAULT) + '.', true, false, false);
			text.setFontColor(COLOR_RED);
			add(text);
			add(Text.getBreak());
			viewSimpleApplicationForm(iwc);
		} catch (final Exception e) {
			final Text text = new Text(e.getMessage(), true, false, false);
			text.setFontColor(COLOR_RED);
			add(text);
			add(Text.getBreak());
			viewSimpleApplicationForm(iwc);
		}
	}
	
	
	/**
	 * Set if the form should send the user to his home page after login.
	 **/
	public void setToSendUserToHomePage(boolean doSendToHomePage) {
		sendUserToHomePage = doSendToHomePage;
	}
	
	
	private boolean citizenLivesInCommune(final IWContext iwc, final User citizen) throws RemoteException, CreateException, FinderException {
		final int primary = citizen.getPrimaryGroupID();
		final CommuneUserBusiness commune = (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);
		final Group rootCitizenGroup = commune.getRootCitizenGroup();
		return primary == ((Integer) rootCitizenGroup.getPrimaryKey()).intValue();
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
		
		
		NBSLoginBusinessBean NBSLBusiness = NBSLoginBusinessBean.createNBSLoginBusiness();
		String unlockAction = "nbs_unlock_action";
		if(iwc.getParameter(unlockAction)!= null){
			NBSLBusiness.logOutBankID(iwc);
		}
		
		NBSLoggedOnInfo info = NBSLBusiness.getBankIDLoggedOnInfo(iwc);
		if(info !=null){
			String persID = info.getNBSPersonalID();
			if(persID!= null){
				ssnInput.setValue(persID);
				ssnInput.setDisabled(true);
				
				String unlockString = "Unlock input";
				IWBundle iwb = this.getBundle(iwc);
				if(iwb != null){
					IWResourceBundle iwrb = iwb.getResourceBundle(iwc);
					if(iwrb != null){
						unlockString = iwrb.getLocalizedString("unlock", "Unlock input");
					}
				}
				
				Link unlockLink = new Link(unlockString);
				Enumeration enum = iwc.getParameterNames();
				if(enum != null){
					while(enum.hasMoreElements()){
						String parameterName = (String)enum.nextElement();
						unlockLink.maintainParameter(parameterName,iwc);
					}
				}
				unlockLink.addParameter(NBSLogin.PRM_PERSONAL_ID,persID);
				unlockLink.addParameter(unlockAction,"true");
				
				table.add(Text.getNonBrakingSpace(), 3, 1);	
				table.add(unlockLink, 3, 1);	
				
				//HiddenInput ssnHInput = new HiddenInput(SSN_KEY,persID);
				//table.add(ssnHInput, 3, 1);	
			}	
		} else {
			String ssn = iwc.getParameter(NBSLogin.PRM_PERSONAL_ID);
			if(ssn!=null){
				ssnInput.setValue(ssn);
			}
		}
		
		table.mergeCells(1, row, 3, row);
		table.add(getSmallText(localize(PERSONAL_ID_CELL_CONNECTION_KEY, PERSONAL_ID_CELL_CONNECTION_DEFAULT)), 1, row++);
		table.setHeight(row++, 30);
		table.add(getHeader(PHONE_HOME_KEY, PHONE_HOME_DEFAULT), 1, row);
		table.add(getErrorText("*"), 1, row++);
		table.add(getSingleInput(iwc, PHONE_HOME_KEY, 25, true), 1, row++);
		
		table.add(getHeader(PHONE_CELL_KEY, PHONE_CELL_DEFAULT), 1, row++);
		table.add(getSingleInput(iwc, PHONE_CELL_KEY, 25, false), 1, row++);
		
		table.add(getHeader(EMAIL_KEY, EMAIL_DEFAULT), 1, row++);
		table.add(getSingleInput(iwc, EMAIL_KEY, 25, false), 1, row++);		
						
		table.setHeight(row++, 30);
		table.mergeCells(1, row, 3, row);
		table.add(getErrorText(localize(MANDATORY_FIELD_EXPL_KEY, MANDATORY_FIELD_EXPL_DEFAULT)), 1, row++);
		table.mergeCells(1, row, 3, row);
		table.add(getSmallText(localize(CITIZEN_ACCOUNT_INFO_KEY, CITIZEN_ACCOUNT_INFO_DEFAULT)), 1, row++);
		
		
	}
	
	private Table getRadioButton(final String name, final String value, String defaultValue, boolean selected) {
		Table table = new Table(3, 1);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(2, "3");
		
		RadioButton button = getRadioButton(name, value);
		if (selected)
			button.setSelected();
		table.add(button, 1, 1);
		table.add(getSmallText(localize(value, defaultValue)), 3, 1);
		
		return table;
	}
	
	private TextInput getSingleInput(IWContext iwc, final String paramId, final int maxLength, boolean notEmpty) {
		TextInput textInput = (TextInput) getStyledInterface(new TextInput(paramId));
		textInput.setLength (maxLength);
		textInput.setMaxlength (maxLength);
		textInput.setSize (maxLength);
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
	
	private static boolean getBooleanParameter(final IWContext iwc, final String key) {
		final String value = iwc.getParameter(key);
		return value != null && value.equals(YES_KEY);
	}
	
	private static int getIntParameter(final IWContext iwc, final String key) {
		final String value = iwc.getParameter(key);
		return (value == null || value.trim().length() == 0) ? 0 : Integer.parseInt(value.trim());
	}
	
	private static String getSsn(final IWContext iwc, final String key) {
		String rawInput = iwc.getParameter(key);
		if (rawInput == null) {
			return null;
		}
		final StringBuffer digitOnlyInput = new StringBuffer();
		for (int i = 0; i < rawInput.length(); i++) {
			char number = rawInput.charAt(i);
			if (Character.isDigit(number))
				digitOnlyInput.append(number);
			else {
				if (Character.isLetter(number) && number == 'T')
					digitOnlyInput.append(number);
				if (Character.isLetter(number) && number == 'F')
					digitOnlyInput.append(number);
			}
		}
		rawInput = digitOnlyInput.toString();
		final Calendar rightNow = Calendar.getInstance();
		final int currentYear = rightNow.get(Calendar.YEAR);
		
		if (rawInput.length() == 10) {
			String bornYear = "";
			final int inputYear = new Integer(rawInput.substring(4, 6)).intValue();
			final int century = inputYear + 2000 > currentYear ? 19 : 20;
			bornYear = bornYear + century + inputYear;
		}
		
		try {
		final SocialSecurityNumber ssnumber = (SocialSecurityNumber) IBOLookup.getServiceInstance(iwc, SocialSecurityNumber.class);
		
		boolean isTemporary = false;
		if (rawInput.indexOf("TF") != -1)
			isTemporary = true;
		if (rawInput.length() != 12 || !ssnumber.isValidIcelandicSocialSecurityNumber(rawInput)) {
			return null;
		}
		
		final int year = new Integer(rawInput.substring(4, 6)).intValue();
		final int month = new Integer(rawInput.substring(2, 4)).intValue();
		final int day = new Integer(rawInput.substring(0, 2)).intValue();
		if (year < 1880 || year > currentYear || month < 1 || month > 12 || day < 1 || day > 31) {
			return null;
		}
		}
		catch (Exception e){
			
		}
		return rawInput;
	}
	
	private static boolean isOver18(final String ssn) {
		if (ssn == null || ssn.length() != 10) {
			throw new IllegalArgumentException("'" + ssn + "' isn't a SSN");
		}
		
		final Calendar birthday18 = Calendar.getInstance();
		final int currentYear = birthday18.get(Calendar.YEAR);
		String bornYear = "";
		
		if (ssn.length() == 10) {
			
			final int inputYear = new Integer(ssn.substring(4, 6)).intValue();
			final int century = inputYear + 2000 > currentYear ? 19 : 20;
			bornYear = bornYear + century + inputYear;
		}
		
		final int month = new Integer(ssn.substring(2, 4)).intValue();
		final int day = new Integer(ssn.substring(0, 2)).intValue();
		final Date rightToday = Calendar.getInstance().getTime();
		
		birthday18.set(new Integer(bornYear).intValue() + 18, month - 1, day);
		
		return rightToday.after(birthday18.getTime());
	}
	
	private static int parseAction(final IWContext iwc) {
		int action = ACTION_VIEW_FORM;
		
		if (iwc.isParameterSet(SIMPLE_FORM_SUBMIT_KEY)) {
			action = ACTION_SUBMIT_SIMPLE_FORM;
		}
				
		return action;
	}
	
	private static Map parseParameters(final IWResourceBundle bundle, final IWContext iwc, final Collection mandatoryParameterNames, final Collection stringParameterNames, final Collection ssnParameterNames, final Collection integerParameterNames) throws ParseException {
		final Map result = new HashMap();
		
		for (Iterator i = stringParameterNames.iterator(); i.hasNext();) {
			final String key = i.next().toString();
			final String value = iwc.getParameter(key);
			final int length = value == null ? 0 : value.trim().length();
			if (length == 0) {
				if (mandatoryParameterNames.contains(key)) {
					throw new ParseException(bundle, key);
				} else {
					result.put(key, "");
				}
			} else {
				result.put(key, value.trim());
			}
		}
		
		for (Iterator i = ssnParameterNames.iterator(); i.hasNext();) {
			final String key = i.next().toString();
			final String value = getSsn(iwc, key);
			if (value == null && mandatoryParameterNames.contains(key)) {
				throw new ParseException(bundle, key);
			} else if (value != null && result.containsValue (value)) {
				throw new ParseException
				(bundle.getLocalizedString (ONLY_ONE_PERSON_PER_SSN_KEY,
						ONLY_ONE_PERSON_PER_SSN_DEFAULT) + " "
						+ value);
			} else {
				result.put(key, value == null ? "" : value);
			}
		}
		
		for (Iterator i = integerParameterNames.iterator(); i.hasNext();) {
			final String key = i.next().toString();
			try {
				final String valueAsString = iwc.getParameter(key);
				if ((valueAsString == null || valueAsString.trim().length() == 0) && !mandatoryParameterNames.contains(key)) {
					result.put(key, new Integer(0));
				} else {
					final Integer value = new Integer(valueAsString.trim());
					result.put(key, value);
				}
			} catch (Exception e) {
				throw new ParseException(bundle, key);
			}
		}
		
		return result;
	}
	
	static private class ParseException extends Exception {
		private final String key;
		
		ParseException(final IWResourceBundle bundle, final String key) {
			super(createMessage(bundle, key));
			this.key = key;
		}
		
		ParseException(final String message) {
			super(message);
			key = "";
		}
		
		static String createMessage(final IWResourceBundle bundle, final String key) {
			String displayName = bundle.getLocalizedString(key);
			if ((displayName == null || displayName.trim().length() == 0) && key.lastIndexOf("caa_") > 1) {
				final int secondIndex = key.indexOf("caa_", 2);
				final String shortKey = key.substring(0, secondIndex);
				displayName = bundle.getLocalizedString(shortKey);
			}
			final String formatErrorString = bundle.getLocalizedString("scaa_format_error", "Inserted value is incorrect");
			return formatErrorString + ": " + displayName;
		}
		
		String getKey() {
			return key;
		}
	}
	
	
	
	protected CommuneBusiness getCommuneBusiness(IWApplicationContext iwac) throws RemoteException {
		return (CommuneBusiness) IBOLookup.getServiceInstance(iwac, CommuneBusiness.class);
	}
	
	private static LoginTableHome getLoginTableHome () {
		try {
			return (LoginTableHome) IDOLookup.getHome (LoginTable.class);
		} catch (Exception e) {
			e.printStackTrace ();
			return null;
		}
	}
	
	/**
	 * @param sendEmail. If set, email will be sent when application is sent.
	 */
	public void setSendEmail(boolean sendEmail) {
		_sendEmail = sendEmail;
	}

	/**
	 * Returns the _sendEmail.
	 * @return boolean
	 */
	public boolean getSendEmail() {
		return _sendEmail;
	}
	
	/**
	 * @param infoPage The infoPage to set.
	 */
	public void setInfoPage(ICPage infoPage) {
		this._infoPage = infoPage;
	}
	
	/**
	 * @return Returns the infoPage.
	 */
	private ICPage getInfoPage() {
		return this._infoPage;
	}
	
	
}
