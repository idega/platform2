package se.idega.idegaweb.commune.account.citizen.presentation;

import com.idega.business.IBOLookup;
import com.idega.core.accesscontrol.business.UserHasLoginException;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.accesscontrol.data.LoginTableHome;
import com.idega.core.location.business.CommuneBusiness;
import com.idega.core.location.data.Commune;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.Group;
import com.idega.user.data.User;
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
import se.idega.idegaweb.commune.account.citizen.data.CitizenAccount;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.util.PIDChecker;

/**
 * CitizenAccountApplication is an IdegaWeb block that inputs and handles
 * applications for citizen accounts. It is based on session ejb classes in
 * {@link se.idega.idegaweb.commune.account.citizen.business} and entity ejb
 * classes in {@link se.idega.idegaweb.commune.account.citizen.business.data}.
 * <p>
 * Last modified: $Date: 2005/02/21 16:52:33 $ by $Author: laddi $
 *
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.75 $
 */
public class CitizenAccountApplication extends CommuneBlock {
	private final static int ACTION_VIEW_FORM = 0;
	private final static int ACTION_SUBMIT_SIMPLE_FORM = 1;
	private final static int ACTION_SUBMIT_UNKNOWN_CITIZEN_FORM_1 = 2;
	private final static int ACTION_SUBMIT_UNKNOWN_CITIZEN_FORM_2 = 3;
	
	final static String APPLICATION_REASON_DEFAULT = "Orsak till ansökan om medborgarkonto";
	final static String APPLICATION_REASON_KEY = "caa_application_reason";
	final static String CHILDREN_COUNT_DEFAULT = "Antal barn i familjen";
	final static String CHILDREN_COUNT_KEY = "caa_children_count";
	private final static String CHILDREN_DEFAULT = "Barn i familjen";
	private final static String CHILDREN_KEY = "caa_children";
	private final static String CITY_DEFAULT = "Postort";
	private final static String CITY_KEY = "caa_city";
	final static String CIVIL_STATUS_DEFAULT = "Civilstånd";
	final static String CIVIL_STATUS_KEY = "caa_civil_status";
	final static String COHABITANT_DEFAULT = "Sammanboende";
	final static String COHABITANT_KEY = "caa_cohabitant";
	final static String CURRENT_KOMMUN_DEFAULT = "Nuvarande kommun";
	final static String CURRENT_KOMMUN_KEY = "caa_current_kommun";
	final static String DETACHED_HOUSE_DEFAULT = "Villa";
	final static String DETACHED_HOUSE_KEY = "caa_detached_house";
	final static String EMAIL_DEFAULT = "E-post";
	final static String EMAIL_KEY = "caa_email";
	private final static String FIRST_NAME_DEFAULT = "Förnamn";
	private final static String FIRST_NAME_KEY = "caa_first_name";
	private final static String HAS_COHABITANT_DEFAULT = "Är du sammanboende?";
	private final static String HAS_COHABITANT_KEY = "caa_has_cohabitant";
	final static String HOUSING_TYPE_KEY = "caa_housing_type";
	final static String LANDLORD_DEFAULT = "Hyresvärd (endast hyreskontrakt)";
	final static String LANDLORD_KEY = "caa_landlord";
	final static String LANDLORD_NAME_DEFAULT = "Namn";
	final static String LANDLORD_NAME_KEY = "caa_landlord_name";
	final static String LANDLORD_PHONE_DEFAULT = "Telefon";
	final static String LANDLORD_PHONE_KEY = "caa_landlord_phone";
	final static String LANDLORD_ADDRESS_DEFAULT = "Adress";
	final static String LANDLORD_ADDRESS_KEY = "caa_landlord_address";
	private final static String LAST_NAME_DEFAULT = "Efternamn";
	private final static String LAST_NAME_KEY = "caa_last_name";
	final static String MOVING_IN_ADDRESS_DEFAULT = "Inflyttningsadress";
	final static String MOVING_IN_ADDRESS_KEY = "caa_moving_in_address";
	final static String MOVING_IN_DATE_DEFAULT = "Inflyttningsdatum";
	final static String MOVING_IN_DATE_KEY = "caa_moving_in_date";
	final static String MOVING_TO_NACKA_DEFAULT = "Jag flyttar till Nacka kommun";
	final static String NO_DEFAULT = "Nej";
	final static String NO_KEY = "caa_no";
	final static String PHONE_HOME_DEFAULT = "Telefon (hem)";
	final static String PHONE_HOME_KEY = "caa_phone_home";
	final static String PHONE_WORK_DEFAULT = "Telefon (arbete/mobil)";
	final static String PHONE_WORK_KEY = "caa_phone_work";
	final static String PROPERTY_TYPE_DEFAULT = "Fastighetsbeteckning (endast villa)";
	final static String PROPERTY_TYPE_KEY = "caa_property_type";
	private final static String PUT_CHILDREN_IN_NACKA_SCHOOL_DEFAULT = "Jag vill ha plats för mitt barn i en skola i Nacka kommun";
	private final static String PUT_CHILDREN_IN_NACKA_CHILDCARE_DEFAULT = "Jag vill ha plats för mitt barn i barnomsorgen i Nacka kommun";
	final static String SSN_DEFAULT = "Personnummer";
	final static String SSN_KEY = "caa_ssn";
	private final static String CAREOF_DEFAULT = "c/o";
	private final static String CAREOF_KEY = "caa_careof";
	private final static String STREET_DEFAULT = "Gatuadress";
	private final static String STREET_KEY = "caa_street";
	final static String TENANCY_AGREEMENT_DEFAULT = "Hyreskontrakt";
	final static String TENANCY_AGREEMENT_KEY = "caa_tenancy_agreement";
	private final static String TEXT_APPLICATION_SUBMITTED_DEFAULT = "Ansökan är skickad.";
	private final static String TEXT_APPLICATION_SUBMITTED_KEY = "caa_app_submitted";
	private final static String UNKNOWN_CITIZEN_DEFAULT = "Du finns inte registrerad som medborgare i kommunen. Du har ändå" + " möjlighet att registrera dig för ett användarkonto om du har" + " planerat att flytta till kommunen eller vill att ditt barn ska gå i" + " skolan i kommunen. Följ instruktionerna nedan.";
	private final static String UNKNOWN_CITIZEN_KEY = "caa_unknown_citizen";
	private final static String GOTO_FORGOT_PASSWORD_DEFAULT = "Klicka på länken \"Jag har glömt mitt användarnamn eller lösenord\"";
	private final static String GOTO_FORGOT_PASSWORD_KEY = "caa_goto_forgot_password_key";
	private final static String USER_ALLREADY_HAS_A_LOGIN_DEFAULT = "Du har redan ett konto";
	private final static String USER_ALLREADY_HAS_A_LOGIN_KEY = "caa_user_allready_has_a_login";
	final static String YES_DEFAULT = "Ja";
	final static String YES_KEY = "caa_yes";
	private final static String ONLY_ONE_PERSON_PER_SSN_KEY = "caa_only_one_person_per_ssn";
	private final static String ONLY_ONE_PERSON_PER_SSN_DEFAULT = "Flera personer kan inte ha personnummer";
	private final static String YOU_MUST_BE_18_KEY = "caa_youMustBe18";
	private final static String YOU_MUST_BE_18_DEFAULT = "Du måste vara 18 år gammal för att kunna ansöka om medborgarkonto";
	private final static String ZIP_CODE_DEFAULT = "Postnummer";
	private final static String ZIP_CODE_KEY = "caa_zip_code";
	private final static String NOT_LIVING_IN_SWEDEN_KEY = "caa_not_living_in_sweden";
	private final static String MUST_SELECT_COMMUNE_KEY = "caa_must_select_commune";
	private final static String MUST_SELECT_COMMUNE_DEFAULT = "You must select a commune";
	
	private final static String SIMPLE_FORM_SUBMIT_KEY = "caa_simpleSubmit";
	private final static String SIMPLE_FORM_SUBMIT_DEFAULT = "Skicka ansökan";
	private final static String UNKNOWN_CITIZEN_FORM_1_SUBMIT_KEY = "caa_unknownCitizenSubmit1";
	private final static String UNKNOWN_CITIZEN_FORM_1_SUBMIT_DEAFULT = "Fortsätt";
	private final static String UNKNOWN_CITIZEN_FORM_2_SUBMIT_KEY = "caa_unknownCitizenSubmit2";
	private final static String UNKNOWN_CITIZEN_FORM_2_SUBMIT_DEAFULT = "Skicka ansökan";
	
	private final static String COLOR_RED = "#ff0000";
	
	private final static String ERROR_FIELD_CAN_NOT_BE_EMPTY_DEFAULT = "Fältet måste fyllas i";
	private final static String ERROR_FIELD_CAN_NOT_BE_EMPTY_KEY = "caa_field_can_not_be_empty";
	private final static String ERROR_NO_INSERT_DEFAULT = "Kunde inte lagra ansökan.";
	private final static String ERROR_NO_INSERT_KEY = "caa_unable_to_insert";
	
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
				case ACTION_SUBMIT_UNKNOWN_CITIZEN_FORM_1 :
					submitUnknownCitizenForm1(iwc);
					break;
				case ACTION_SUBMIT_UNKNOWN_CITIZEN_FORM_2 :
					submitUnknownCitizenForm2(iwc);
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
		table.add(getSubmitButton(SIMPLE_FORM_SUBMIT_KEY, SIMPLE_FORM_SUBMIT_DEFAULT), 1, table.getRows() + 1);
		final Form accountForm = new Form();
		accountForm.add(table);
		add(accountForm);
	}
	
	private void submitSimpleForm(final IWContext iwc) {
		final Collection mandatoryParametersNames = Collections.singleton(SSN_KEY);
		final Collection stringParameterNames = Arrays.asList(new String[] { EMAIL_KEY, PHONE_HOME_KEY, PHONE_WORK_KEY });
		final Collection ssnParameterNames = Collections.singleton(SSN_KEY);
		final Collection integerParameters = Collections.EMPTY_LIST;
		
		try {
			final Map parameters = parseParameters(getResourceBundle(), iwc, mandatoryParametersNames, stringParameterNames, ssnParameterNames, integerParameters);
			final String ssn = parameters.get(SSN_KEY).toString();
			if (!isOver18(ssn)) {
				throw new ParseException(localize(YOU_MUST_BE_18_KEY, YOU_MUST_BE_18_DEFAULT));
			}
			final String email = parameters.get(EMAIL_KEY).toString();
			final String phoneHome = parameters.get(PHONE_HOME_KEY).toString();
			final String phoneWork = parameters.get(PHONE_WORK_KEY).toString();
			final CitizenAccountBusiness business = (CitizenAccountBusiness) IBOLookup.getServiceInstance(iwc, CitizenAccountBusiness.class);
			final User user = business.getUser(ssn);
			final Collection logins = new ArrayList ();
			try {
				logins.addAll (getLoginTableHome ().findLoginsForUser (user));
			} catch (Exception e) {
				// no problem, no login found
			}
			if (user != null && !logins.isEmpty()) {
				throw new UserHasLoginException ();
			}
			if (user == null || !citizenLivesInNacka(iwc, user)) {
				// unknown or not-living-in-nacka user applies
				final Text text = new Text(localize(UNKNOWN_CITIZEN_KEY, UNKNOWN_CITIZEN_DEFAULT));
				text.setFontColor(COLOR_RED);
				add(text);
				viewUnknownCitizenApplicationForm1(iwc);
			} else if (null == business.insertApplication(iwc,user, ssn, email, phoneHome, phoneWork)) {
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
	
	
	private boolean citizenLivesInNacka(final IWContext iwc, final User citizen) throws RemoteException, CreateException, FinderException {
		final int primary = citizen.getPrimaryGroupID();
		final CommuneUserBusiness commune = (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);
		final Group rootCitizenGroup = commune.getRootCitizenGroup();
		return primary == ((Integer) rootCitizenGroup.getPrimaryKey()).intValue();
	}
	
	private void viewUnknownCitizenApplicationForm1(final IWContext iwc) {
		final Table table = createTable();
		addSimpleInputs(table, iwc);
		int row = table.getRows() + 1;
		
		table.add(getHeader(FIRST_NAME_KEY, FIRST_NAME_DEFAULT), 1, row);
		table.add(getSingleInput(iwc, FIRST_NAME_KEY, 40, true), 3, row++);
		
		table.add(getHeader(LAST_NAME_KEY, LAST_NAME_DEFAULT), 1, row);
		table.add(getSingleInput(iwc, LAST_NAME_KEY, 40, true), 3, row++);
		
		table.add(getHeader(CAREOF_KEY, CAREOF_DEFAULT), 1, row);
		table.add(getSingleInput(iwc, CAREOF_KEY, 40, false), 3, row++);
		
		table.add(getHeader(STREET_KEY, STREET_DEFAULT), 1, row);
		table.add(getSingleInput(iwc, STREET_KEY, 40, true), 3, row++);
		
		table.add(getHeader(ZIP_CODE_KEY, ZIP_CODE_DEFAULT), 1, row);
		table.add(getSingleInput(iwc, ZIP_CODE_KEY, 5, true), 3, row++);
		
		table.add(getHeader(CITY_KEY, CITY_DEFAULT), 1, row);
		table.add(getSingleInput(iwc, CITY_KEY, 40, true), 3, row++);
		
		table.add(getHeader(CIVIL_STATUS_KEY, CIVIL_STATUS_DEFAULT), 1, row);
		table.add(getSingleInput(iwc, CIVIL_STATUS_KEY, 20, true), 3, row++);
		
		table.setHeight(row++, 6);
		table.mergeCells(1, row, 3, row);
		table.add(getHeader(HAS_COHABITANT_KEY, HAS_COHABITANT_DEFAULT), 1, row++);
		table.mergeCells(1, row, 3, row);
		table.add(getRadioButton(HAS_COHABITANT_KEY, YES_KEY, YES_DEFAULT, true), 1, row++);
		table.mergeCells(1, row, 3, row);
		table.add(getRadioButton(HAS_COHABITANT_KEY, NO_KEY, NO_DEFAULT, false), 1, row++);
		
		table.setHeight(row++, 6);
		table.add(getHeader(CHILDREN_COUNT_KEY, CHILDREN_COUNT_DEFAULT), 1, row);
		table.add(getSingleInput(iwc, CHILDREN_COUNT_KEY, 2, true), 3, row++);
		
		table.setHeight(row++, 6);
		table.mergeCells(1, row, 3, row);
		table.add(getHeader(APPLICATION_REASON_KEY, APPLICATION_REASON_DEFAULT), 1, row++);
		table.mergeCells(1, row, 3, row);
		table.add(getRadioButton(APPLICATION_REASON_KEY, CitizenAccount.MOVING_TO_NACKA_KEY, MOVING_TO_NACKA_DEFAULT, true), 1, row++);
		table.mergeCells(1, row, 3, row);
		table.add(getRadioButton(APPLICATION_REASON_KEY, CitizenAccount.PUT_CHILDREN_IN_NACKA_SCHOOL_KEY, PUT_CHILDREN_IN_NACKA_SCHOOL_DEFAULT, false), 1, row++);
		table.mergeCells(1, row, 3, row);
		table.add(getRadioButton(APPLICATION_REASON_KEY, CitizenAccount.PUT_CHILDREN_IN_NACKA_CHILDCARE_KEY, PUT_CHILDREN_IN_NACKA_CHILDCARE_DEFAULT, false), 1, row++);
		
		table.setHeight(row++, 12);
		
		table.add(getSubmitButton(UNKNOWN_CITIZEN_FORM_1_SUBMIT_KEY, UNKNOWN_CITIZEN_FORM_1_SUBMIT_DEAFULT), 1, row);
		final Form accountForm = new Form();
		accountForm.add(table);
		add(accountForm);
	}
	
	private void submitUnknownCitizenForm1 (final IWContext iwc) {
		try {
			if (!(iwc.isParameterSet (PHONE_HOME_KEY)
						|| iwc.isParameterSet (PHONE_WORK_KEY))) {
				throw new ParseException (getResourceBundle(), PHONE_HOME_KEY);
			}
			viewUnknownCitizenApplicationForm2(iwc);
		} catch (final Exception e) {
			final Text text = new Text (e.getMessage(), true, false, false);
			text.setFontColor (COLOR_RED);
			add (text);
			add (Text.getBreak());
			viewUnknownCitizenApplicationForm1 (iwc);
		}
	}
	
	private void viewUnknownCitizenApplicationForm2(final IWContext iwc) {
		final Form form = getUnknownCitizenForm2 ();
		final Table table = createTable();
		int row = 1;
		if (getBooleanParameter(iwc, HAS_COHABITANT_KEY)) {
			row = addCohabitantInputs(iwc, table, row);
		}
		
		final int childrenCount = getIntParameter(iwc, CHILDREN_COUNT_KEY);
		if (childrenCount > 0) {
			row = addChildrenInputs(iwc, table, row, childrenCount);
		}
		
		final String applicationReason = iwc.getParameter(APPLICATION_REASON_KEY);
		if (applicationReason.equals(CitizenAccount.MOVING_TO_NACKA_KEY)) {
			row = addMovingToNackaInputs(iwc, table, row);
		} else if (applicationReason.equals(CitizenAccount.PUT_CHILDREN_IN_NACKA_SCHOOL_KEY)) {
			row = addPutChildrenInNackaSchoolInputs(iwc, table, row);
		} else if (applicationReason.equals(CitizenAccount.PUT_CHILDREN_IN_NACKA_CHILDCARE_KEY)) {
			row = addPutChildrenInNackaChildcareInputs(iwc, table, row);
		}
		
		table.setHeight(row++, 12);
		table.add(getSubmitButton(UNKNOWN_CITIZEN_FORM_2_SUBMIT_KEY, UNKNOWN_CITIZEN_FORM_2_SUBMIT_DEAFULT), 1, row);
		form.add(table);
		add(form);
	}
	
	private int addPutChildrenInNackaChildcareInputs(final IWContext iwc, final Table table, int row) {
		// applicant wants to put children in Nacka
		final Text putChildrenInNackaHeader = getLocalizedHeader(CitizenAccount.PUT_CHILDREN_IN_NACKA_CHILDCARE_KEY, PUT_CHILDREN_IN_NACKA_CHILDCARE_DEFAULT);
		table.setHeight(row++, 6);
		table.mergeCells(1, row, 3, row);
		table.add(putChildrenInNackaHeader, 1, row++);
		table.add(getHeader(CURRENT_KOMMUN_KEY, CURRENT_KOMMUN_DEFAULT), 1, row);
		DropdownMenu communes = getCommuneDropdownMenu(iwc, CURRENT_KOMMUN_KEY, null);
		table.add(communes, 3, row++);
		return row;
	}

	private int addPutChildrenInNackaSchoolInputs(final IWContext iwc, final Table table, int row) {
		// applicant wants to put children in Nacka
		final Text putChildrenInNackaHeader = getLocalizedHeader(CitizenAccount.PUT_CHILDREN_IN_NACKA_SCHOOL_KEY, PUT_CHILDREN_IN_NACKA_SCHOOL_DEFAULT);
		table.setHeight(row++, 6);
		table.mergeCells(1, row, 3, row);
		table.add(putChildrenInNackaHeader, 1, row++);
		
		table.add(getHeader(CURRENT_KOMMUN_KEY, CURRENT_KOMMUN_DEFAULT), 1, row);
		DropdownMenu communes = getCommuneDropdownMenu(iwc, CURRENT_KOMMUN_KEY, null);
		table.add(communes, 3, row++);
		return row;
	}

	private int addMovingToNackaInputs(final IWContext iwc, final Table table, int row) {
		// applicant is moving to Nacka
		final Text movingToNackaHeader = getLocalizedHeader(CitizenAccount.MOVING_TO_NACKA_KEY, MOVING_TO_NACKA_DEFAULT);
		table.setHeight(row++, 6);
		table.mergeCells(1, row, 3, row);
		table.add(movingToNackaHeader, 1, row++);
		
		table.add(getHeader(CURRENT_KOMMUN_KEY, CURRENT_KOMMUN_DEFAULT), 1, row);
		DropdownMenu communes = getCommuneDropdownMenu(iwc, CURRENT_KOMMUN_KEY, null);
		table.add(communes, 3, row++);
		
		table.add(getHeader(MOVING_IN_ADDRESS_KEY, MOVING_IN_ADDRESS_DEFAULT), 1, row);
		table.add(getSingleInput(iwc, MOVING_IN_ADDRESS_KEY, 40, true), 3, row++);
		table.add(getHeader(MOVING_IN_DATE_KEY, MOVING_IN_DATE_DEFAULT), 1, row);
		table.add(getSingleInput(iwc, MOVING_IN_DATE_KEY, 20, true), 3, row++);
		table.mergeCells(1, row, 3, row);
		table.add(getRadioButton(HOUSING_TYPE_KEY, TENANCY_AGREEMENT_KEY, TENANCY_AGREEMENT_DEFAULT, false), 1, row++);
		table.mergeCells(1, row, 3, row);
		table.add(getRadioButton(HOUSING_TYPE_KEY, DETACHED_HOUSE_KEY, DETACHED_HOUSE_DEFAULT, true), 1, row++);
		table.add(getHeader(PROPERTY_TYPE_KEY, PROPERTY_TYPE_DEFAULT), 1, row);
		table.add(getSingleInput(iwc, PROPERTY_TYPE_KEY, 20, false), 3, row++);
		final Text landlord = getLocalizedHeader(LANDLORD_KEY, LANDLORD_DEFAULT);
		table.setHeight(row++, 6);
		table.mergeCells(1, row, 3, row);
		table.add(landlord, 1, row++);
		table.add(getHeader(LANDLORD_NAME_KEY, LANDLORD_NAME_DEFAULT), 1, row);
		table.add(getSingleInput(iwc, LANDLORD_NAME_KEY, 20, false), 3, row++);
		table.add(getHeader(LANDLORD_PHONE_KEY, LANDLORD_PHONE_DEFAULT), 1, row);
		table.add(getSingleInput(iwc, LANDLORD_PHONE_KEY, 20, false), 3, row++);
		table.add(getHeader(LANDLORD_ADDRESS_KEY, LANDLORD_ADDRESS_DEFAULT), 1, row);
		table.add(getSingleInput(iwc, LANDLORD_ADDRESS_KEY, 20, false), 3, row++);
		return row;
	}

	private int addChildrenInputs(final IWContext iwc, final Table table, int row, final int childrenCount) {
		// applicant has children
		final Text childrenHeader = getLocalizedHeader(CHILDREN_KEY, CHILDREN_DEFAULT);
		table.setHeight(row++, 6);
		table.mergeCells(1, row, 3, row);
		table.add(childrenHeader, 1, row++);
		for (int i = 0; i < childrenCount; i++) {
			table.add(getHeader(FIRST_NAME_KEY, FIRST_NAME_DEFAULT), 1, row);
			table.add(getSingleInput(iwc, FIRST_NAME_KEY + CHILDREN_KEY + i, 40, true), 3, row++);
			table.add(getHeader(LAST_NAME_KEY, LAST_NAME_DEFAULT), 1, row);
			table.add(getSingleInput(iwc, LAST_NAME_KEY + CHILDREN_KEY + i, 40, true), 3, row++);
			table.add(getHeader(SSN_KEY, SSN_DEFAULT), 1, row);
			table.add(getSingleInput(iwc, SSN_KEY + CHILDREN_KEY + i, 12, true), 3, row++);
		}
		return row;
	}

	private int addCohabitantInputs(final IWContext iwc, final Table table, int row) {
		// applicant has cohabitant
		final Text cohabitantHeader = getLocalizedHeader(COHABITANT_KEY, COHABITANT_DEFAULT);
		table.mergeCells(1, row, 3, row);
		table.add(cohabitantHeader, 1, row++);
		table.add(getHeader(FIRST_NAME_KEY, FIRST_NAME_DEFAULT), 1, row);
		table.add(getSingleInput(iwc, FIRST_NAME_KEY + COHABITANT_KEY, 40, true), 3, row++);
		table.add(getHeader(LAST_NAME_KEY, LAST_NAME_DEFAULT), 1, row);
		table.add(getSingleInput(iwc, LAST_NAME_KEY + COHABITANT_KEY, 40, true), 3, row++);
		table.add(getHeader(SSN_KEY, SSN_DEFAULT), 1, row);
		table.add(getSingleInput(iwc, SSN_KEY + COHABITANT_KEY, 12, true), 3, row++);
		table.add(getHeader(CIVIL_STATUS_KEY, CIVIL_STATUS_DEFAULT), 1, row);
		table.add(getSingleInput(iwc, CIVIL_STATUS_KEY + COHABITANT_KEY, 20, true), 3, row++);
		table.add(getHeader(PHONE_WORK_KEY, PHONE_WORK_DEFAULT), 1, row);
		table.add(getSingleInput(iwc, PHONE_WORK_KEY + COHABITANT_KEY, 20, true), 3, row++);
		return row;
	}

	private Form getUnknownCitizenForm2() {
		final Form form = new Form();
		form.maintainParameter(SSN_KEY);
		form.maintainParameter(EMAIL_KEY);
		form.maintainParameter(PHONE_WORK_KEY);
		form.maintainParameter(PHONE_HOME_KEY);
		form.maintainParameter(FIRST_NAME_KEY);
		form.maintainParameter(LAST_NAME_KEY);
		form.maintainParameter(CAREOF_KEY);
		form.maintainParameter(STREET_KEY);
		form.maintainParameter(ZIP_CODE_KEY);
		form.maintainParameter(CITY_KEY);
		form.maintainParameter(HAS_COHABITANT_KEY);
		form.maintainParameter(CHILDREN_COUNT_KEY);
		form.maintainParameter(APPLICATION_REASON_KEY);
		form.maintainParameter(CIVIL_STATUS_KEY);
		form.add(new HiddenInput(UNKNOWN_CITIZEN_FORM_2_SUBMIT_KEY, UNKNOWN_CITIZEN_FORM_2_SUBMIT_DEAFULT));
		return form;
	}

	private void submitUnknownCitizenForm2(final IWContext iwc) {
		final Collection mandatoryParameterNames = new ArrayList();
		final Collection stringParameterNames = new ArrayList();
		final Collection ssnParameterNames = new ArrayList();
		final Collection integerParameters = new ArrayList();
		final boolean hasCohabitant = getBooleanParameter(iwc, HAS_COHABITANT_KEY);
		final int childrenCount = getIntParameter(iwc, CHILDREN_COUNT_KEY);
		final String applicationReason = iwc.getParameter(APPLICATION_REASON_KEY);
		setValidationStructureForUnknownCitizenForm2
				(mandatoryParameterNames, stringParameterNames, ssnParameterNames,
				 integerParameters, hasCohabitant, childrenCount, applicationReason);
		
		Integer applicationId = null;
		try {
			final Map parameters = parseParameters
					(getResourceBundle(), iwc, mandatoryParameterNames,
					 stringParameterNames, ssnParameterNames, integerParameters);
			final String ssn = parameters.get(SSN_KEY).toString();

			// applicant must be at least 18 years old
			if (!isOver18(ssn)) {
				throw new ParseException(localize(YOU_MUST_BE_18_KEY,
																					YOU_MUST_BE_18_DEFAULT));
			}
			
			// applicant should live outside nacka in order to do special application
			final CitizenAccountBusiness business = (CitizenAccountBusiness)
					IBOLookup.getServiceInstance(iwc, CitizenAccountBusiness.class);
			final User user = business.getUser(ssn);
			if (null != user && citizenLivesInNacka (iwc, user)) {
				viewSimpleApplicationForm(iwc);
				return;
			}
			
			// store application
			applicationId = insertApplication
					(iwc, hasCohabitant, childrenCount, applicationReason, parameters,
					 ssn, business);

			// store info about cohabitant
			if (null != applicationId && hasCohabitant) {
				insertCohabitant(applicationId, parameters, business);
			}
			
			// store info about children
			if (null != applicationId && childrenCount > 0) {
				insertChildren(childrenCount, applicationId, parameters, business);
			}			
			String currentCommune = parameters.get(CURRENT_KOMMUN_KEY).toString();
			int communeId = 0;
			try {
				communeId = Integer.valueOf(currentCommune).intValue();
			} catch (Exception e) {}
			if (null != applicationId && applicationReason.equals
					(CitizenAccount.MOVING_TO_NACKA_KEY)) {
				insertPutChildrenInNacka(applicationId, parameters, business,
																 communeId);
			} else if (null != applicationId
								 && (applicationReason.equals
										 (CitizenAccount.PUT_CHILDREN_IN_NACKA_SCHOOL_KEY)
										 || applicationReason.equals
										 (CitizenAccount.PUT_CHILDREN_IN_NACKA_CHILDCARE_KEY))) {
				if (communeId > 0) {
					business.insertPutChildren(applicationId, parameters.get
																		 (CURRENT_KOMMUN_KEY).toString());
				}
			}
		} catch (final ParseException e) {
			final Text text = new Text(e.getMessage(), true, false, false);
			text.setFontColor(COLOR_RED);
			add(text);
			add(Text.getBreak());
			viewUnknownCitizenApplicationForm2(iwc);
			return;
		} catch (final Exception e) {
			e.printStackTrace();
			applicationId = null;
		}
		
		if (null == applicationId) {
			final String message = localize
					(ERROR_NO_INSERT_KEY, ERROR_NO_INSERT_DEFAULT);
			final Text text = new Text(message, true, false, false);
			text.setFontColor(COLOR_RED);
			add(text);
			add(Text.getBreak());
			viewUnknownCitizenApplicationForm1(iwc);
			return;
		}
		
		if (null != getResponsePage())
			iwc.forwardToIBPage(getParentPage(), getResponsePage());
		else
			add(new Text(localize(TEXT_APPLICATION_SUBMITTED_KEY,
														TEXT_APPLICATION_SUBMITTED_DEFAULT)));
	}
	
	private Integer insertApplication(final IWContext iwc, final boolean hasCohabitant, final int childrenCount, final String applicationReason, final Map parameters, final String ssn, final CitizenAccountBusiness business) throws RemoteException {
		Integer applicationId;
		final String email = parameters.get(EMAIL_KEY).toString();
		final String phoneHome = parameters.get(PHONE_HOME_KEY).toString();
		final String phoneWork = parameters.get(PHONE_WORK_KEY).toString();
		final String name = parameters.get(FIRST_NAME_KEY) + " " + parameters.get(LAST_NAME_KEY);
		final String careOf = (String) parameters.get(CAREOF_KEY);
		final String street = parameters.get(STREET_KEY).toString();
		final String zipCode = parameters.get(ZIP_CODE_KEY).toString();
		final String city = parameters.get(CITY_KEY).toString();
		final String civilStatus = parameters.get(CIVIL_STATUS_KEY).toString();
		applicationId = business.insertApplication(iwc,name, ssn, email, phoneHome, phoneWork, careOf, street, zipCode, city, civilStatus, hasCohabitant, childrenCount, applicationReason);
		return applicationId;
	}

	private void insertPutChildrenInNacka(Integer applicationId, final Map parameters, final CitizenAccountBusiness business, int communeId) throws RemoteException {
		final String movingInAddress = parameters.get(MOVING_IN_ADDRESS_KEY).toString();
		final String movingInDate = parameters.get(MOVING_IN_DATE_KEY).toString();
		final String housingType = parameters.get(HOUSING_TYPE_KEY).toString();
		final String propertyType = parameters.get(PROPERTY_TYPE_KEY).toString();
		final String landlordName = parameters.get(LANDLORD_NAME_KEY).toString();
		final String landlordPhone = parameters.get(LANDLORD_PHONE_KEY).toString();
		final String landlordAddress = parameters.get(LANDLORD_ADDRESS_KEY).toString();
		business.insertMovingTo(applicationId, movingInAddress, movingInDate, housingType, propertyType, landlordName, landlordPhone, landlordAddress);
		if (communeId > 0) {
			business.insertPutChildren(applicationId, parameters.get(CURRENT_KOMMUN_KEY).toString());
		}
	}

	private void insertChildren(final int childrenCount, Integer applicationId, final Map parameters, final CitizenAccountBusiness business) throws RemoteException {
		for (int i = 0; i < childrenCount; i++) {
			final String childrenFirstName = parameters.get(FIRST_NAME_KEY + CHILDREN_KEY + i).toString();
			final String childrenLastName = parameters.get(LAST_NAME_KEY + CHILDREN_KEY + i).toString();
			final String childrenSsn = parameters.get(SSN_KEY + CHILDREN_KEY + i).toString();
			business.insertChildren(applicationId, childrenFirstName, childrenLastName, childrenSsn);
		}
	}

	private void insertCohabitant(Integer applicationId, final Map parameters, final CitizenAccountBusiness business) throws RemoteException {
		final String cohabitantFirstName = parameters.get(FIRST_NAME_KEY + COHABITANT_KEY).toString();
		final String cohabitantLastName = parameters.get(LAST_NAME_KEY + COHABITANT_KEY).toString();
		final String cohabitantSsn = parameters.get(SSN_KEY + COHABITANT_KEY).toString();
		final String cohabitantCivilStatus = parameters.get(CIVIL_STATUS_KEY + COHABITANT_KEY).toString();
		final String cohabitantPhoneWork = parameters.get(PHONE_WORK_KEY + COHABITANT_KEY).toString();
		business.insertCohabitant(applicationId, cohabitantFirstName, cohabitantLastName, cohabitantSsn, cohabitantCivilStatus, cohabitantPhoneWork);
	}

	private void setValidationStructureForUnknownCitizenForm2(final Collection mandatoryParameterNames, final Collection stringParameterNames, final Collection ssnParameterNames, final Collection integerParameters, final boolean hasCohabitant, final int childrenCount, final String applicationReason) {
		mandatoryParameterNames.addAll(Arrays.asList(new String[] { SSN_KEY, FIRST_NAME_KEY, LAST_NAME_KEY, CIVIL_STATUS_KEY, STREET_KEY, ZIP_CODE_KEY, CITY_KEY, CHILDREN_COUNT_KEY }));
		stringParameterNames.addAll(Arrays.asList(new String[] { EMAIL_KEY, PHONE_HOME_KEY, PHONE_WORK_KEY, FIRST_NAME_KEY, LAST_NAME_KEY, CAREOF_KEY, STREET_KEY, ZIP_CODE_KEY, CITY_KEY, CIVIL_STATUS_KEY, HAS_COHABITANT_KEY, APPLICATION_REASON_KEY }));
		ssnParameterNames.add(SSN_KEY);
		integerParameters.addAll(Arrays.asList(new String[] { CHILDREN_COUNT_KEY }));
		if (hasCohabitant) {
			stringParameterNames.addAll(Arrays.asList(new String[] { FIRST_NAME_KEY + COHABITANT_KEY, LAST_NAME_KEY + COHABITANT_KEY, CIVIL_STATUS_KEY + COHABITANT_KEY, PHONE_WORK_KEY + COHABITANT_KEY }));
			ssnParameterNames.add(SSN_KEY + COHABITANT_KEY);
			mandatoryParameterNames.addAll(Arrays.asList(new String[] { FIRST_NAME_KEY + COHABITANT_KEY, LAST_NAME_KEY + COHABITANT_KEY, SSN_KEY + COHABITANT_KEY, CIVIL_STATUS_KEY + COHABITANT_KEY, PHONE_WORK_KEY + COHABITANT_KEY }));
		}
		
		for (int i = 0; i < childrenCount; i++) {
			stringParameterNames.addAll(Arrays.asList(new String[] { FIRST_NAME_KEY + CHILDREN_KEY + i, LAST_NAME_KEY + CHILDREN_KEY + i }));
			ssnParameterNames.add(SSN_KEY + CHILDREN_KEY + i);
			mandatoryParameterNames.addAll(Arrays.asList(new String[] { FIRST_NAME_KEY + CHILDREN_KEY + i, LAST_NAME_KEY + CHILDREN_KEY + i, SSN_KEY + CHILDREN_KEY + i }));
		}
		
		if (applicationReason.equals(CitizenAccount.MOVING_TO_NACKA_KEY)) {
			stringParameterNames.addAll(Arrays.asList(new String[] { MOVING_IN_ADDRESS_KEY, MOVING_IN_DATE_KEY, HOUSING_TYPE_KEY, PROPERTY_TYPE_KEY, LANDLORD_NAME_KEY, LANDLORD_PHONE_KEY, LANDLORD_ADDRESS_KEY }));
			mandatoryParameterNames.addAll(Arrays.asList(new String[] { MOVING_IN_ADDRESS_KEY, MOVING_IN_DATE_KEY, HOUSING_TYPE_KEY }));
			mandatoryParameterNames.add(CURRENT_KOMMUN_KEY);
			stringParameterNames.add(CURRENT_KOMMUN_KEY);
		} else if (applicationReason.equals(CitizenAccount.PUT_CHILDREN_IN_NACKA_SCHOOL_KEY) ||
						 applicationReason.equals(CitizenAccount.PUT_CHILDREN_IN_NACKA_CHILDCARE_KEY)) {
			mandatoryParameterNames.add(CURRENT_KOMMUN_KEY);
			stringParameterNames.add(CURRENT_KOMMUN_KEY);
		}
	}

	private Table createTable() {
		final Table table = new Table();
		table.setCellspacing(getCellpadding());
		table.setCellpadding(0);
		table.setWidth(2, "12");
		table.setWidth(1, "180");
		return table;
	}
	
	private void addSimpleInputs(final Table table, final IWContext iwc) {
		table.add(getHeader(SSN_KEY, SSN_DEFAULT), 1, 1);
		TextInput ssnInput = getSingleInput(iwc, SSN_KEY, 12, true);
		table.add(ssnInput, 3, 1);
		
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
				Enumeration enumer = iwc.getParameterNames();
				if(enumer != null){
					while(enumer.hasMoreElements()){
						String parameterName = (String)enumer.nextElement();
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
		
		
		table.add(getHeader(EMAIL_KEY, EMAIL_DEFAULT), 1, 2);
		table.add(getSingleInput(iwc, EMAIL_KEY, 40, 100, false), 3, 2);
		table.add(getHeader(PHONE_HOME_KEY, PHONE_HOME_DEFAULT), 1, 3);
		table.add(getSingleInput(iwc, PHONE_HOME_KEY, 20, true), 3, 3);
		table.add(getHeader(PHONE_WORK_KEY, PHONE_WORK_DEFAULT), 1, 4);
		table.add(getSingleInput(iwc, PHONE_WORK_KEY, 20, false), 3, 4);
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
	    return getSingleInput( iwc, paramId, -1, maxLength,  notEmpty);
	}

	private TextInput getSingleInput(IWContext iwc, final String paramId, int length, final int maxLength, boolean notEmpty) {
		if (length == -1) {
		    length = maxLength;
		}
	    TextInput textInput = (TextInput) getStyledInterface(new TextInput(paramId));
		textInput.setLength (length);
		textInput.setMaxlength (maxLength);
		textInput.setSize (length);
		if (notEmpty) {
			final String fieldCanNotBeEmpty = localize(ERROR_FIELD_CAN_NOT_BE_EMPTY_KEY, ERROR_FIELD_CAN_NOT_BE_EMPTY_DEFAULT);
			String name = localize(paramId, paramId);
			if ((name == null || name.trim().length() == 0 || name.equals(paramId)) && paramId.lastIndexOf("caa_") > 1) {
				final int secondIndex = paramId.indexOf("caa_", 2);
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
			final int inputYear = new Integer(rawInput.substring(0, 2)).intValue();
			final int century = inputYear + 2000 > currentYear ? 19 : 20;
			rawInput = century + rawInput;
		}
		final PIDChecker pidChecker = PIDChecker.getInstance();
		boolean isTemporary = false;
		if (rawInput.indexOf("TF") != -1)
			isTemporary = true;
		if (rawInput.length() != 12 || !pidChecker.isValid(rawInput, isTemporary)) {
			return null;
		}
		final int year = new Integer(rawInput.substring(0, 4)).intValue();
		final int month = new Integer(rawInput.substring(4, 6)).intValue();
		final int day = new Integer(rawInput.substring(6, 8)).intValue();
		if (year < 1880 || year > currentYear || month < 1 || month > 12 || day < 1 || day > 31) {
			return null;
		}
		return rawInput;
	}
	
	private static boolean isOver18(final String ssn) {
		if (ssn == null || ssn.length() != 12) {
			throw new IllegalArgumentException("'" + ssn + "' isn't a SSN");
		}
		
		final int year = new Integer(ssn.substring(0, 4)).intValue();
		final int month = new Integer(ssn.substring(4, 6)).intValue();
		final int day = new Integer(ssn.substring(6, 8)).intValue();
		final Date rightNow = Calendar.getInstance().getTime();
		final Calendar birthday18 = Calendar.getInstance();
		birthday18.set(year + 18, month - 1, day);
		
		return rightNow.after(birthday18.getTime());
	}
	
	private static int parseAction(final IWContext iwc) {
		int action = ACTION_VIEW_FORM;
		
		if (iwc.isParameterSet(SIMPLE_FORM_SUBMIT_KEY)) {
			action = ACTION_SUBMIT_SIMPLE_FORM;
		}
		else if (iwc.isParameterSet(UNKNOWN_CITIZEN_FORM_1_SUBMIT_KEY)) {
			action = ACTION_SUBMIT_UNKNOWN_CITIZEN_FORM_1;
		} else if (iwc.isParameterSet(UNKNOWN_CITIZEN_FORM_2_SUBMIT_KEY)) {
			action = ACTION_SUBMIT_UNKNOWN_CITIZEN_FORM_2;
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
			final String formatErrorString = bundle.getLocalizedString("caa_format_error", "Felaktigt inmatat värde");
			return formatErrorString + ": " + displayName;
		}
		
		String getKey() {
			return key;
		}
	}
	
	private DropdownMenu getCommuneDropdownMenu(IWContext iwc, String parameter, String communeId) {
		DropdownMenu menu = (DropdownMenu) getStyledInterface(new DropdownMenu(parameter));
		menu.addMenuElement(0, "");
		menu.setAsNotEmpty(localize(MUST_SELECT_COMMUNE_KEY, MUST_SELECT_COMMUNE_DEFAULT), "0");
		try {
			Collection c = getCommuneBusiness(iwc).getCommunes();
			int defaultCommuneId = ((Integer) getCommuneBusiness(iwc).getDefaultCommune().getPrimaryKey()).intValue(); 
			if (c != null) {
				Iterator iter = c.iterator();
				while (iter.hasNext()) {
					Commune commune = (Commune) iter.next();
					int id = ((Integer) commune.getPrimaryKey()).intValue();
					if (id != defaultCommuneId) {
						menu.addMenuElement(id, commune.getCommuneName());
					}
				}
				if (communeId != null) {
					menu.setSelectedElement(communeId);
				}
				menu.addMenuElement(-1, localize(NOT_LIVING_IN_SWEDEN_KEY, "Not living in Sweden"));
			}		
		} catch (Exception e) {
			add(new ExceptionWrapper(e));
		}
		return menu;	
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
}
