/*
 * $Id: CitizenAccountApplication.java,v 1.26 2002/11/14 14:02:51 staffan Exp $
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
import com.idega.core.accesscontrol.business.UserHasLoginException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.*;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.*;
import com.idega.user.data.*;
import java.rmi.RemoteException;
import java.util.*;
import se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusiness;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

/**
 * CitizenAccountApplication is an IdegaWeb block that inputs and handles
 * applications for citizen accounts. It is based on session ejb classes in
 * {@link se.idega.idegaweb.commune.account.citizen.business} and entity ejb
 * classes in {@link se.idega.idegaweb.commune.account.citizen.business.data}.
 * <p>
 * Last modified: $Date: 2002/11/14 14:02:51 $ by $Author: staffan $
 *
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.26 $
 */
public class CitizenAccountApplication extends CommuneBlock {
	private final static int ACTION_VIEW_FORM = 0;
	private final static int ACTION_SUBMIT_SIMPLE_FORM = 1;
	private final static int ACTION_SUBMIT_UNKNOWN_CITIZEN_FORM_1 = 2;
	private final static int ACTION_SUBMIT_UNKNOWN_CITIZEN_FORM_2 = 3;
    
    private final static String APPLICATION_REASON_DEFAULT
        = "Orsak till ansökan om medborgarkonto?";
    private final static String APPLICATION_REASON_KEY
        = "caa_application_reason";
    private final static String CHILDREN_COUNT_DEFAULT
        = "Antal barn i familjen?";
    private final static String CHILDREN_COUNT_KEY = "caa_children_count";
    private final static String CHILDREN_DEFAULT = "Barn i familjen";
    private final static String CHILDREN_KEY = "caa_children";
    private final static String CITY_DEFAULT = "Postort";
    private final static String CITY_KEY = "caa_city";
    private final static String CIVIL_STATUS_DEFAULT = "Civilstånd";
    private final static String CIVIL_STATUS_KEY = "caa_civil_status";
    private final static String COHABITANT_DEFAULT = "Sammanboende";
    private final static String COHABITANT_KEY = "caa_cohabitant";
    private final static String CURRENT_KOMMUN_DEFAULT = "Nuvarande kommun";
    private final static String CURRENT_KOMMUN_KEY = "caa_current_kommun";
    private final static String DETACHED_HOUSE_DEFAULT = "Villa";
    private final static String DETACHED_HOUSE_KEY = "caa_detached_house";
    private final static String EMAIL_DEFAULT = "E-post";
    private final static String EMAIL_KEY = "caa_email";
    private final static String FIRST_NAME_DEFAULT = "Förnamn";
    private final static String FIRST_NAME_KEY = "caa_first_name";
    private final static String GENDER_DEFAULT = "Kön";
    private final static String GENDER_KEY = "caa_gender";
    private final static String HAS_COHABITANT_DEFAULT = "Är du sammanboende?";
    private final static String HAS_COHABITANT_KEY = "caa_has_cohabitant";
    private final static String HOUSING_TYPE_KEY = "caa_housing_type";
    private final static String LAST_NAME_DEFAULT = "Efternamn";
    private final static String LAST_NAME_KEY = "caa_last_name";
    private final static String MOVING_IN_ADDRESS_DEFAULT
        = "Inflyttningsadress";
    private final static String MOVING_IN_ADDRESS_KEY = "caa_moving_in_address";
    private final static String MOVING_IN_DATE_DEFAULT = "Inflyttningsdatum";
    private final static String MOVING_IN_DATE_KEY = "caa_moving_in_date";
    private final static String MOVING_TO_NACKA_DEFAULT
        = "Jag flyttar till Nacka kommun";
    private final static String MOVING_TO_NACKA_KEY = "caa_moving_to_nacka";
    private final static String NO_DEFAULT = "Nej";
    private final static String NO_KEY = "caa_no";
    private final static String PHONE_HOME_DEFAULT = "Telefon (hem)";
    private final static String PHONE_HOME_KEY = "caa_phone_home";
    private final static String PHONE_WORK_DEFAULT = "Telefon (arbete/mobil)";
    private final static String PHONE_WORK_KEY = "caa_phone_work";
    private final static String PROPERTY_TYPE_DEFAULT = "Fastighetsbeteckning (endast villa)";
    private final static String PROPERTY_TYPE_KEY = "caa_property_type";
    private final static String PUT_CHILDREN_IN_NACKA_DEFAULT
        = "Jag vill ha plats för mitt barn i en skola i Nacka kommun";
    private final static String PUT_CHILDREN_IN_NACKA_KEY
        = "caa_put_children_in_nacka";
    private final static String SSN_DEFAULT = "Personnummer";
    private final static String SSN_KEY = "caa_ssn";
    private final static String STREET_DEFAULT = "Gatuadress";
    private final static String STREET_KEY = "caa_street";
    private final static String TENANCY_AGREEMENT_DEFAULT = "Hyreskontrakt";
    private final static String TENANCY_AGREEMENT_KEY = "caa_tenancy_agreement";
	private final static String TEXT_APPLICATION_SUBMITTED_DEFAULT
        = "Ansökan är mottagen.";
	private final static String TEXT_APPLICATION_SUBMITTED_KEY
        = "caa_app_submitted";
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
	private final static String UNKNOWN_CITIZEN_FORM_2_SUBMIT_DEAFULT
        = "Skicka ansökan";

    private final static String ERROR_NO_INSERT_DEFAULT
        = "Kunde inte lagra ansökan."; 
    private final static String ERROR_NO_INSERT_KEY = "caa_unable_to_insert";
    
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
		} catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}    
	}
    
	private void viewSimpleApplicationForm (final IWContext iwc) {
		final Table table = createTable (this);
        addSimpleInputs (this, table, iwc);
        addSubmitButton (this, table, 9, iwc, SIMPLE_FORM_SUBMIT_KEY,
                         SIMPLE_FORM_SUBMIT_DEFAULT);
		final Form accountForm = new Form();
		accountForm.add(table);
		add(accountForm);
	}
    
	private void submitSimpleForm (final IWContext iwc) {
        final Collection mandatoryParametersNames = Arrays.asList (new String []
            { SSN_KEY, EMAIL_KEY, PHONE_WORK_KEY });
        final Collection stringParameterNames = Arrays.asList (new String [] {
            EMAIL_KEY, PHONE_HOME_KEY, PHONE_WORK_KEY });
        final Collection ssnParameterNames = Collections.singleton (SSN_KEY);
        final Collection integerParameters = Collections.EMPTY_LIST;
        
        try {
            final Map parameters = parseParameters
                    (getResourceBundle (), iwc, mandatoryParametersNames,
                     stringParameterNames, ssnParameterNames,
                     integerParameters);
            final String ssn = parameters.get (SSN_KEY).toString ();
            final String email = parameters.get (EMAIL_KEY).toString ();
            final String phoneHome
                    = parameters.get (PHONE_HOME_KEY).toString ();
            final String phoneWork
                    = parameters.get (PHONE_WORK_KEY).toString ();
            final CitizenAccountBusiness business
                    = (CitizenAccountBusiness) IBOLookup.getServiceInstance
                    (iwc, CitizenAccountBusiness.class);
            final User user = business.getUser (ssn);
            if (user == null) {
                // unknown user applies
                final Text text = new Text (localize (UNKNOWN_CITIZEN_KEY,
                                                      UNKNOWN_CITIZEN_DEFAULT));
                text.setFontColor ("#ff0000");
                add (text);
                viewUnknownCitizenApplicationForm1 (iwc);
            } else if (!business.insertApplication(user, ssn, email, phoneHome,
                                                   phoneWork)) {
                // known user applied, but couldn't be submitted
                throw new Exception (localize(ERROR_NO_INSERT_KEY,
                                              ERROR_NO_INSERT_DEFAULT));
            } else {
                // known user applied and was submitted
                if (getResponsePage() != null) {
                    iwc.forwardToIBPage(getParentPage(), getResponsePage());
                } else {
                    add(new Text(localize(TEXT_APPLICATION_SUBMITTED_KEY,
                                          "Ansökan är skickad")));
                }
            }            
        } catch (UserHasLoginException uhle) {
            final Text text = new Text
                    (localize("user_already_has_a_login",
                              "Du har redan ett konto"), true, false, false);
            text.setFontColor ("#ff0000");
            add (text);
            add (Text.getBreak ());
            viewSimpleApplicationForm(iwc);
        } catch (final Exception e) {
            final Text text = new Text(e.getMessage (), true, false, false);
            text.setFontColor ("#ff0000");
            add (text);
            add (Text.getBreak ());
            viewSimpleApplicationForm(iwc);
        }
    }
    
	private void viewUnknownCitizenApplicationForm1 (final IWContext iwc) {
		final Table table = createTable (this);
        addSimpleInputs (this, table, iwc);
        int row = 5;
        addHeader (this, table, 1, row,
                   FIRST_NAME_KEY, FIRST_NAME_DEFAULT);
        addHeader (this, table, 2, row++,
                   LAST_NAME_KEY, LAST_NAME_DEFAULT);
        addSingleInput (this, table, 1, row, iwc, FIRST_NAME_KEY, 40);
        addSingleInput (this, table, 2, row++, iwc, LAST_NAME_KEY, 40);
        addHeader (this, table, 1, row++, STREET_KEY, STREET_DEFAULT);
        addSingleInput (this, table, 1, row++, iwc, STREET_KEY, 40);
        addHeader (this, table, 1, row, ZIP_CODE_KEY, ZIP_CODE_DEFAULT);
        addHeader (this, table, 2, row++, CITY_KEY, CITY_DEFAULT);
        addSingleInput (this, table, 1, row, iwc, ZIP_CODE_KEY, 40);
        addSingleInput (this, table, 2, row++, iwc, CITY_KEY, 40);
        addGenderDropdownInput (this, table, row, iwc);
        addHeader (this, table, 2, row++, CIVIL_STATUS_KEY,
                   CIVIL_STATUS_DEFAULT);
        addSingleInput (this, table, 2, row++, iwc, CIVIL_STATUS_KEY, 20);
        table.mergeCells (1, row, 2, row);
        addHeader (this, table, 1, row++, HAS_COHABITANT_KEY,
                   HAS_COHABITANT_DEFAULT);
        final RadioGroup hasCohabitant = new RadioGroup (HAS_COHABITANT_KEY);
        addRadioInput (this, iwc, hasCohabitant, YES_KEY, YES_DEFAULT);
        addRadioInput (this, iwc, hasCohabitant, NO_KEY, NO_DEFAULT);
        hasCohabitant.setSelected (YES_KEY);
        table.add (hasCohabitant, 1, row++);
        table.mergeCells (1, row, 2, row);
        addHeader (this, table, 1, row++, CHILDREN_COUNT_KEY,
                   CHILDREN_COUNT_DEFAULT);
        addSingleInput (this, table, 1, row++, iwc, CHILDREN_COUNT_KEY,
                        2);
        table.mergeCells (1, row, 2, row);
        addHeader (this, table, 1, row++, APPLICATION_REASON_KEY,
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
        row += 1;
        addSubmitButton (this, table, row, iwc,
                         UNKNOWN_CITIZEN_FORM_1_SUBMIT_KEY,
                         UNKNOWN_CITIZEN_FORM_1_SUBMIT_DEAFULT );
		final Form accountForm = new Form();
		accountForm.add(table);
		add(accountForm);
    }
    
    private void submitUnknownCitizenForm1 (final IWContext iwc) {
        final Collection mandatoryParameterNames = Arrays.asList (new String []
            { SSN_KEY, EMAIL_KEY, PHONE_WORK_KEY, FIRST_NAME_KEY, LAST_NAME_KEY,
              STREET_KEY, ZIP_CODE_KEY, CITY_KEY, CIVIL_STATUS_KEY,
              HAS_COHABITANT_KEY, APPLICATION_REASON_KEY, GENDER_KEY,
              CHILDREN_COUNT_KEY });
        final Collection stringParameterNames  = Arrays.asList (new String [] {
            EMAIL_KEY, PHONE_HOME_KEY, PHONE_WORK_KEY, FIRST_NAME_KEY,
            LAST_NAME_KEY, STREET_KEY, ZIP_CODE_KEY, CITY_KEY, CIVIL_STATUS_KEY,
            HAS_COHABITANT_KEY, APPLICATION_REASON_KEY });
        final Collection ssnParameterNames = Collections.singleton (SSN_KEY);
        final Collection integerParameters = Arrays.asList (new String [] {
            GENDER_KEY, CHILDREN_COUNT_KEY });
        
        try {
            final Map parameters = parseParameters
                    (getResourceBundle (), iwc, mandatoryParameterNames,
                     stringParameterNames, ssnParameterNames,
                     integerParameters);
            viewUnknownCitizenApplicationForm2 (iwc);
            
        } catch (final Exception e) {
            final Text text = new Text(e.getMessage (), true, false, false);
            text.setFontColor ("#ff0000");
            add (text);
            add (Text.getBreak ());
			viewUnknownCitizenApplicationForm1 (iwc);
        }
    }
            
	private void viewUnknownCitizenApplicationForm2 (final IWContext iwc) {
		final Form form = new Form();
        copyParameterToHidden (iwc, form, SSN_KEY);
        copyParameterToHidden (iwc, form, EMAIL_KEY);
        copyParameterToHidden (iwc, form, PHONE_WORK_KEY);
        copyParameterToHidden (iwc, form, PHONE_HOME_KEY);
        copyParameterToHidden (iwc, form, FIRST_NAME_KEY);
        copyParameterToHidden (iwc, form, LAST_NAME_KEY);
        copyParameterToHidden (iwc, form, STREET_KEY);
        copyParameterToHidden (iwc, form, ZIP_CODE_KEY);
        copyParameterToHidden (iwc, form, CITY_KEY);
        copyParameterToHidden (iwc, form, GENDER_KEY);
        copyParameterToHidden (iwc, form, HAS_COHABITANT_KEY);
        copyParameterToHidden (iwc, form, CHILDREN_COUNT_KEY);
        copyParameterToHidden (iwc, form, APPLICATION_REASON_KEY);
        copyParameterToHidden (iwc, form, CIVIL_STATUS_KEY);        

        final Table table = createTable (this);
        int row = 1;
        if (getBooleanParameter (iwc, HAS_COHABITANT_KEY)) {
            // applicant has cohabitant
            final Text cohabitantHeader
                    = getLocalizedHeader (COHABITANT_KEY, COHABITANT_DEFAULT);
            table.mergeCells (1, row, 2, row);
            table.add (cohabitantHeader, 1, row++);
            addHeader (this, table, 1, row, FIRST_NAME_KEY, FIRST_NAME_DEFAULT);
            addHeader (this, table, 2, row++, LAST_NAME_KEY, LAST_NAME_DEFAULT);
            addSingleInput (this, table, 1, row, iwc,
                            FIRST_NAME_KEY + COHABITANT_KEY, 40);
            addSingleInput (this, table, 2, row++, iwc,
                            LAST_NAME_KEY + COHABITANT_KEY, 40);
            addHeader (this, table, 1, row, SSN_KEY, SSN_DEFAULT);
            addHeader (this, table, 2, row++, CIVIL_STATUS_KEY,
                       CIVIL_STATUS_DEFAULT);
            addSingleInput (this, table, 1, row, iwc, SSN_KEY + COHABITANT_KEY,
                            12);
            addSingleInput (this, table, 2, row++, iwc,
                            CIVIL_STATUS_KEY + COHABITANT_KEY, 40);
            addHeader (this, table, 1, row++, PHONE_WORK_KEY,
                       PHONE_WORK_DEFAULT);
            addSingleInput (this, table, 1, row++, iwc,
                            PHONE_WORK_KEY + COHABITANT_KEY, 40);
        }
        
        final int childrenCount = getIntParameter (iwc, CHILDREN_COUNT_KEY);
        if (childrenCount > 0) {
            // applicant has children
            final Text childrenHeader
                    = getLocalizedHeader (CHILDREN_KEY, CHILDREN_DEFAULT);
            table.mergeCells (1, row, 2, row);
            table.add (childrenHeader, 1, row++);
            for (int i = 0; i < childrenCount; i++) {
                addHeader (this, table, 1, row, FIRST_NAME_KEY,
                           FIRST_NAME_DEFAULT);
                addHeader (this, table, 2, row++, LAST_NAME_KEY,
                           LAST_NAME_DEFAULT);
                addSingleInput (this, table, 1, row, iwc,
                                FIRST_NAME_KEY + CHILDREN_KEY + i, 40);
                addSingleInput (this, table, 2, row++, iwc,
                                LAST_NAME_KEY + CHILDREN_KEY + i, 40);
                addHeader (this, table, 1, row++, SSN_KEY, SSN_DEFAULT);
                addSingleInput (this, table, 1, row++, iwc,
                                SSN_KEY + CHILDREN_KEY + i, 12);
            }
        }
        
        final String applicationReason
                = iwc.getParameter (APPLICATION_REASON_KEY);
        if (applicationReason.equals (MOVING_TO_NACKA_KEY)) {
            // applicant is moving to Nacka
            final Text movingToNackaHeader
                    = getLocalizedHeader (MOVING_TO_NACKA_KEY,
                                          MOVING_TO_NACKA_DEFAULT);
            table.mergeCells (1, row, 2, row);
            table.add (movingToNackaHeader, 1, row++);
            addHeader (this, table, 1, row, MOVING_IN_ADDRESS_KEY,
                       MOVING_IN_ADDRESS_DEFAULT);
            addHeader (this, table, 2, row++, MOVING_IN_DATE_KEY,
                       MOVING_IN_DATE_DEFAULT);
            addSingleInput (this, table, 1, row, iwc, MOVING_IN_ADDRESS_KEY,
                            40);
            addSingleInput (this, table, 2, row++, iwc, MOVING_IN_DATE_KEY, 20);
            final RadioGroup housingType
                    = new RadioGroup (HOUSING_TYPE_KEY);
            addRadioInput (this, iwc, housingType, TENANCY_AGREEMENT_KEY,
                           TENANCY_AGREEMENT_DEFAULT);
            addRadioInput (this, iwc, housingType, DETACHED_HOUSE_KEY,
                           DETACHED_HOUSE_DEFAULT);
            housingType.setSelected (DETACHED_HOUSE_KEY);
            table.mergeCells (1, row, 1, row + 1);
            table.add (housingType, 1, row);
            addHeader (this, table, 2, row++, PROPERTY_TYPE_KEY,
                       PROPERTY_TYPE_DEFAULT);
            addSingleInput (this, table, 2, row++, iwc, PROPERTY_TYPE_KEY, 20);
        } else if (applicationReason.equals (PUT_CHILDREN_IN_NACKA_KEY)) {
            // applicant wants to put children in Nacka
            final Text putChildrenInNackaHeader
                    = getLocalizedHeader (PUT_CHILDREN_IN_NACKA_KEY,
                                          PUT_CHILDREN_IN_NACKA_DEFAULT);
            table.mergeCells (1, row, 2, row);
            table.add (putChildrenInNackaHeader, 1, row++);
            addHeader (this, table, 1, row++, CURRENT_KOMMUN_KEY,
                       CURRENT_KOMMUN_DEFAULT);
            addSingleInput (this, table, 1, row++, iwc, CURRENT_KOMMUN_KEY, 30);
        }
        
        addSubmitButton (this, table, row + 1, iwc,
                         UNKNOWN_CITIZEN_FORM_2_SUBMIT_KEY,
                         UNKNOWN_CITIZEN_FORM_2_SUBMIT_DEAFULT );
		form.add (table);
		add (form);
    }
    
    private void submitUnknownCitizenForm2 (final IWContext iwc) {
        final Collection mandatoryParameterNames = new ArrayList ();
        mandatoryParameterNames.addAll (Arrays.asList (new String []
            { SSN_KEY, EMAIL_KEY, PHONE_WORK_KEY, FIRST_NAME_KEY, LAST_NAME_KEY,
              STREET_KEY, ZIP_CODE_KEY, CITY_KEY, CIVIL_STATUS_KEY,
              HAS_COHABITANT_KEY, APPLICATION_REASON_KEY, GENDER_KEY,
              CHILDREN_COUNT_KEY }));
        final Collection stringParameterNames = new ArrayList ();
        stringParameterNames.addAll (Arrays.asList (new String [] {
            EMAIL_KEY, PHONE_HOME_KEY, PHONE_WORK_KEY, FIRST_NAME_KEY,
            LAST_NAME_KEY, STREET_KEY, ZIP_CODE_KEY, CITY_KEY, CIVIL_STATUS_KEY,
            HAS_COHABITANT_KEY, APPLICATION_REASON_KEY }));
        final Collection ssnParameterNames = new ArrayList ();
        ssnParameterNames.add (SSN_KEY);
        final Collection integerParameters = new ArrayList ();
        integerParameters.addAll (Arrays.asList (new String [] {
            GENDER_KEY, CHILDREN_COUNT_KEY }));

        try {
            final Map parameters = parseParameters
                    (getResourceBundle (), iwc, mandatoryParameterNames,
                     stringParameterNames, ssnParameterNames,
                     integerParameters);
        } catch (final Exception e) {
            final Text text = new Text(e.getMessage (), true, false, false);
            text.setFontColor ("#ff0000");
            add (text);
            add (Text.getBreak ());
			viewUnknownCitizenApplicationForm1 (iwc);
            return;
        }
        final boolean hasCohabitant = getBooleanParameter (iwc,
                                                           HAS_COHABITANT_KEY);
        if (hasCohabitant) {
            mandatoryParameterNames.addAll (Arrays.asList (new String [] {
                FIRST_NAME_KEY + COHABITANT_KEY, LAST_NAME_KEY + COHABITANT_KEY,
                SSN_KEY + COHABITANT_KEY, CIVIL_STATUS_KEY + COHABITANT_KEY,
                PHONE_WORK_KEY + COHABITANT_KEY }));
            stringParameterNames.addAll (Arrays.asList (new String [] {
                FIRST_NAME_KEY + COHABITANT_KEY, LAST_NAME_KEY + COHABITANT_KEY,
                CIVIL_STATUS_KEY + COHABITANT_KEY,
                PHONE_WORK_KEY + COHABITANT_KEY }));
            ssnParameterNames.add (SSN_KEY + COHABITANT_KEY);
        }

        final int childrenCount = getIntParameter (iwc, CHILDREN_COUNT_KEY);
        for (int i = 0; i < childrenCount; i++) {
            mandatoryParameterNames.addAll (Arrays.asList (new String [] {
                FIRST_NAME_KEY + CHILDREN_KEY + i,
                LAST_NAME_KEY + CHILDREN_KEY + i,
                SSN_KEY + CHILDREN_KEY + i }));
            stringParameterNames.addAll (Arrays.asList (new String [] {
                FIRST_NAME_KEY + CHILDREN_KEY + i,
                LAST_NAME_KEY + CHILDREN_KEY + i }));
            ssnParameterNames.add (SSN_KEY + CHILDREN_KEY + i);            
        }
        
        final String applicationReason
                = iwc.getParameter (APPLICATION_REASON_KEY);
        if (applicationReason.equals (MOVING_TO_NACKA_KEY)) {
            mandatoryParameterNames.addAll (Arrays.asList (new String [] {
                MOVING_IN_ADDRESS_KEY, MOVING_IN_DATE_KEY, HOUSING_TYPE_KEY }));
            stringParameterNames.addAll (Arrays.asList (new String [] {
                MOVING_IN_ADDRESS_KEY, MOVING_IN_DATE_KEY, HOUSING_TYPE_KEY,
                PROPERTY_TYPE_KEY }));
        } else if (applicationReason.equals (PUT_CHILDREN_IN_NACKA_KEY)) {
            mandatoryParameterNames.add (CURRENT_KOMMUN_KEY);
            stringParameterNames.add (CURRENT_KOMMUN_KEY);
        }

        boolean isInserted = false;
        try {
            final Map parameters = parseParameters
                    (getResourceBundle (), iwc, mandatoryParameterNames,
                     stringParameterNames, ssnParameterNames,
                     integerParameters);
            final String ssn = parameters.get (SSN_KEY).toString ();
            final String email = parameters.get (EMAIL_KEY).toString ();
            final String phoneHome
                    = parameters.get (PHONE_HOME_KEY).toString ();
            final String phoneWork
                    = parameters.get (PHONE_WORK_KEY).toString ();
            final String name = parameters.get (FIRST_NAME_KEY) + " "
                    + parameters.get (LAST_NAME_KEY);
            final int genderId = getIntParameter (iwc, GENDER_KEY);
            final String street = parameters.get (STREET_KEY).toString ();
            final String zipCode = parameters.get (ZIP_CODE_KEY).toString ();
            final String city = parameters.get (CITY_KEY).toString ();
            final String civilStatus
                    = parameters.get (CIVIL_STATUS_KEY).toString ();

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
                    (name, ssn, email, phoneHome, phoneWork, birth.getTime (),
                     street, zipCode, city, genderId, civilStatus,
                     hasCohabitant, childrenCount, applicationReason);
        } catch (final ParseException e) {
            final Text text = new Text(e.getMessage (), true, false, false);
            text.setFontColor ("#ff0000");
            add (text);
            add (Text.getBreak ());
            viewUnknownCitizenApplicationForm2 (iwc);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            isInserted = false;
        }
        
        if (!isInserted) {
            final String message = localize(ERROR_NO_INSERT_KEY,
                                            ERROR_NO_INSERT_DEFAULT);
            final Text text = new Text (message, true, false, false);
            text.setFontColor ("#ff0000");
            add (text);
            add (Text.getBreak ());
            viewUnknownCitizenApplicationForm1 (iwc);
            return;
        }
        
        if (getResponsePage() != null)
            iwc.forwardToIBPage(getParentPage(), getResponsePage());
        else
            add(new Text(localize(TEXT_APPLICATION_SUBMITTED_KEY,
                                  TEXT_APPLICATION_SUBMITTED_DEFAULT)));
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
                                         final IWContext iwc) {
        addHeader (communeBlock, table, 1, 1, SSN_KEY, SSN_DEFAULT);
        addSingleInput (communeBlock, table, 1, 2, iwc, SSN_KEY, 12);
        addHeader (communeBlock, table, 2, 1,
                   EMAIL_KEY, EMAIL_DEFAULT);
        addSingleInput (communeBlock, table, 2, 2, iwc, EMAIL_KEY, 40);
        addHeader (communeBlock, table, 1, 3, PHONE_HOME_KEY,
                   PHONE_HOME_DEFAULT);
        addSingleInput (communeBlock, table, 1, 4, iwc, PHONE_HOME_KEY, 20);
        addHeader (communeBlock, table, 2, 3,
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
        group.addRadioButton (paramId, communeBlock.getSmallText (name));
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
         final int col, final int row,
         final String paramId, final String defaultText) {
        final String header = communeBlock.localize (paramId, defaultText);
        final Text text = communeBlock.getSmallText (header);
        table.add (text, col, row);
    }
    
    private static void addSubmitButton
        (final CommuneBlock communeBlock, final Table table, final int row,
         final IWContext iwc, final String submitId, final String defaultText) {
        final String text = communeBlock.localize (submitId, defaultText);
		final SubmitButton submitButton = new SubmitButton
                (communeBlock.getBundle(iwc).getImageButton (text), submitId);
		submitButton.setStyleClass (communeBlock.getLinkFontStyle());
		table.setAlignment (1, row, Table.HORIZONTAL_ALIGN_LEFT);
		table.add (submitButton, 1, row);
    }
    
    private static boolean getBooleanParameter (final IWContext iwc,
                                                final String key) {
        final String value = iwc.getParameter (key);
        return value != null && value.equals (YES_KEY);

    }

    private static int getIntParameter (final IWContext iwc, final String key) {
        final String valueAsString = iwc.getParameter (key);
        final Integer valueAsInteger = new Integer (valueAsString);
        return valueAsInteger.intValue ();
    }
    
    private static String getSsn (final IWContext iwc, final String key) {
        final String rawInput = iwc.getParameter (key);
        if (rawInput == null) {
            return null;
        }
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
    
    private static void copyParameterToHidden
        (final IWContext iwc, final Form form, final String key) {
        if (iwc.isParameterSet (key)) {
            final String value = iwc.getParameter (key);
            final HiddenInput hiddenInput = new HiddenInput (key, value);
            form.add (hiddenInput);
        }
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
    
    private static Map parseParameters
        (final IWResourceBundle bundle, final IWContext iwc,
         final Collection mandatoryParameterNames,
         final Collection stringParameterNames,
         final Collection ssnParameterNames,
         final Collection integerParameterNames) throws ParseException {
        final Map result = new HashMap ();
        
        for (Iterator i = stringParameterNames.iterator (); i.hasNext ();) {
            final String key = i.next ().toString ();
            final String value = iwc.getParameter (key);
            final int length = value == null ? 0 : value.trim ().length ();
            if (length == 0) {
                if (mandatoryParameterNames.contains (key)) {
                    throw new ParseException (bundle, key);
                } else {
                    result.put (key, "");
                }
            } else {
                result.put (key, value.trim ());
            }
        }
        
        for (Iterator i = ssnParameterNames.iterator (); i.hasNext ();) {
            final String key = i.next ().toString ();
            final String value = getSsn (iwc, key);
            if (value == null) {
                if (mandatoryParameterNames.contains (key)) {
                    throw new ParseException (bundle, key);
                } else {
                    result.put (key, "");
                }
            } else {
                result.put (key, value);
            }
        }
        
        for (Iterator i = integerParameterNames.iterator (); i.hasNext ();) {
            final String key = i.next ().toString ();
            try {
                final String valueAsString = iwc.getParameter (key);
                if ((valueAsString == null
                     || valueAsString.trim ().length () == 0)
                    && !mandatoryParameterNames.contains (key)) {
                    result.put (key, new Integer (0));
                } else {
                    final Integer value = new Integer (valueAsString);
                    result.put (key, value);
                }
            } catch (Exception e) {
                throw new ParseException (bundle, key);
            }
        }
        
        return result;
    }
    
    static private class ParseException extends Exception {
        private final String key;
        
        ParseException (final IWResourceBundle bundle, final String key) {
            super (createMessage (bundle, key));
            this.key = key;
        }
        
        static String createMessage (final IWResourceBundle bundle,
                                     final String key) {
            String displayName = bundle.getLocalizedString (key);
            if ((displayName == null || displayName.trim ().length () == 0)
            && key.lastIndexOf ("caa_") > 0) {
                final int secondIndex = key.indexOf ("caa_", 2);
                final String shortKey = key.substring (0, secondIndex);
                displayName = bundle.getLocalizedString (shortKey);
            }
            final String formatErrorString = bundle.getLocalizedString
                    ("caa_format_error", "Felaktigt inmatat värde");
            return formatErrorString + ": " + displayName;
        }
        
        String getKey () {
            return key;
        }
    }
}

