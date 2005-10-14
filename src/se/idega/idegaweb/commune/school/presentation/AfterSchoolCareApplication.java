/*
 * $Id: AfterSchoolCareApplication.java,v 1.24 2005/10/14 07:44:52 laddi Exp $
 * Created on Aug 7, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.care.business.CareConstants;
import se.idega.idegaweb.commune.care.data.AfterSchoolChoice;
import se.idega.idegaweb.commune.childcare.data.AfterSchoolCareDays;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolSeason;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;


/**
 * Last modified: $Date: 2005/10/14 07:44:52 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.24 $
 */
public class AfterSchoolCareApplication extends SchoolApplication {
	
	protected static final int ACTION_PHASE_6 = 6;
	protected static final int ACTION_PHASE_7 = 7;
	protected static final int ACTION_VIEW = 9;

	private static final String PARAMETER_PROVIDER = "prm_provider";
	private static final String PARAMETER_TIME = "prm_time";
	private static final String PARAMETER_PICKED_UP = "prm_picked_up";
	private static final String PARAMETER_CREDITCARD_PAYMENT = "prm_creditcard_payment";

	private static final String PARAMETER_PAYER_NAME = "prm_payer_name";
	private static final String PARAMETER_PAYER_PERSONAL_ID = "prm_payer_personal_id";
	private static final String PARAMETER_CARD_TYPE = "prm_card_type";
	private static final String PARAMETER_CARD_NUMBER = "prm_card_number";
	private static final String PARAMETER_VALID_MONTH = "prm_valid_month";
	private static final String PARAMETER_VALID_YEAR = "prm_valid_year";

	private static final String PARAMETER_AFTER_SCHOOL_INFORMATION = "prm_after_school_information";
	
	private boolean iHideDetailsPhases = false;

	public void present(IWContext iwc) throws Exception {
		SchoolSeason season = null;
		try {
			season = getCareBusiness().getCurrentSeason();
		}
		catch (FinderException fe) {
			log(fe);
			add(getErrorText(localize("no_season_found", "No season found...")));
			return;
		}
		boolean hasOpenApplication = getAfterSchoolBusiness().hasOpenApplication(getSession().getUser(), season, 1);

		switch (parseAction(iwc)) {
			case ACTION_VIEW:
				viewApplication(iwc, season);
				break;
			
			case ACTION_PHASE_1:
				showPhaseOne(iwc, hasOpenApplication);
				break;
			
			case ACTION_PHASE_2:
				showPhaseThree(iwc);
				break;
				
			case ACTION_PHASE_3:
				showPhaseFour(iwc, ACTION_PHASE_4, ACTION_PHASE_2, ACTION_PHASE_3);
				break;
				
			case ACTION_PHASE_4:
				showPhaseFour2(iwc);
				break;
				
			case ACTION_PHASE_5:
				showPhaseFive(iwc, season);
				break;
				
			case ACTION_PHASE_6:
				showPhaseSix(iwc, season);
				break;
				
			case ACTION_PHASE_7:
				showPhaseSeven(iwc, season);
				break;
				
			case ACTION_SAVE:
				save(iwc);
				break;
		}
	}
	
	protected void showPhaseOne(IWContext iwc, boolean hasOpenApplication) throws RemoteException {
		if (!hasOpenApplication) {
			add(getErrorText(localize("has_granted_after_school_care_choice", "Child already has a granted after school care choice")));
			return;
		}
		Form form = createForm();
		
		SchoolSeason season = null;
		try {
			season = getCareBusiness().getCurrentSeason();
		}
		catch (FinderException fe) {
			log(fe);
			add(getErrorText(localize("no_season_found", "No season found...")));
			return;
		}
		form.addParameter(PARAMETER_SEASON, season.getPrimaryKey().toString());
			
		if (getBusiness().hasSchoolPlacing(getSession().getUser(), season)) {
			SchoolClassMember placement = getBusiness().getSchoolPlacing(getSession().getUser(), season);
			SchoolClass group = placement.getSchoolClass();
			School school = group.getSchool();
			School provider = school.getAfterSchoolCareProvider();
			if (provider == null) {
				add(getErrorText(localize("no_after_school_care_provider_found", "No after school care provider was found for the school the student is placed in.")));
				return;
			}
			saveCustodianInfo(iwc, false);

			Table table = new Table();
			table.setCellpadding(0);
			table.setCellspacing(0);
			table.setWidth(Table.HUNDRED_PERCENT);
			form.add(table);
			int row = 1;
			
			table.add(getPersonInfoTable(iwc, getSession().getUser()), 1, row++);
			table.setHeight(row++, 6);
			
			table.add(getText(localize("after_school_application.application_information", "Application for after school care information")), 1, row++);
			table.setHeight(row++, 12);
			
			Table applicationTable = new Table(2, 3);
			applicationTable.setCellpadding(3);
			applicationTable.setCellspacing(0);
			table.add(applicationTable, 1, row++);
			
			applicationTable.add(getSmallHeader(localize("application.school", "School") + ":"), 1, 1);
			applicationTable.add(getSmallText(school.getSchoolName()), 2, 1);
			
			applicationTable.add(getSmallHeader(localize("application.group", "Group") + ":"), 1, 2);
			applicationTable.add(getSmallText(group.getName()), 2, 2);
			
			applicationTable.add(new HiddenInput(PARAMETER_PROVIDER, provider.getPrimaryKey().toString()), 1, 3);
			applicationTable.add(getSmallHeader(localize("application.after_school_care_provider", "Provider") + ":"), 1, 3);
			applicationTable.add(getSmallText(provider.getSchoolName()), 2, 3);
			
			table.setHeight(row++, 18);
			
			SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("next", "Next"), PARAMETER_ACTION, iHideDetailsPhases ? String.valueOf(ACTION_PHASE_4) : String.valueOf(ACTION_PHASE_2)));
			
			table.add(next, 1, row);
			table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
			table.add(getHelpButton("help_school_application_phase_1"), 1, row);
			table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
			table.setCellpaddingRight(1, row, 12);
	
			add(form);
		}
		else {
			add(getErrorText(localize("student_has_no_school_placment", "Student has no school placement")));
		}
	}
	
	protected void showPhaseThree(IWContext iwc) throws RemoteException {
		saveChildInfo(iwc);

		super.showPhaseThree(iwc, ACTION_PHASE_3, ACTION_PHASE_1, ACTION_PHASE_2);
	}

	protected void showPhaseFour2(IWContext iwc) throws RemoteException {
		saveCustodianInfo(iwc, true);

		Form form = createForm();
		form.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_4));
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;
		
		table.add(getPersonInfoTable(iwc, getSession().getUser()), 1, row++);
		table.setHeight(row++, 6);
		
		table.add(getHeader(localize("application.child_information", "Child information")), 1, row++);
		table.setHeight(row++, 6);
		
		Table applicationTable = new Table();
		applicationTable.setCellpadding(3);
		applicationTable.setCellspacing(0);
		applicationTable.setWidth(Table.HUNDRED_PERCENT);
		table.add(applicationTable, 1, row++);
		int aRow = 1;
		
		applicationTable.add(getSmallHeader(localize("yes", "Yes")), 2, aRow);
		applicationTable.add(getSmallHeader(localize("no", "No")), 3, aRow);
		applicationTable.add(getSmallHeader(localize("no_answer", "Won't answer")), 4, aRow++);
		
		if (!iHideDetailsPhases) {
			applicationTable.add(getSmallHeader(localize("child.has_growth_deviation", "Has growth deviation")), 1, aRow);
			RadioButton yes = getRadioButton(PARAMETER_GROWTH_DEVIATION, Boolean.TRUE.toString());
			RadioButton no = getRadioButton(PARAMETER_GROWTH_DEVIATION, Boolean.FALSE.toString());
			RadioButton noAnswer = getRadioButton(PARAMETER_GROWTH_DEVIATION, "");
			Boolean hasGrowthDeviation = getCareBusiness().hasGrowthDeviation(getSession().getUser());
			if (hasGrowthDeviation != null) {
				if (hasGrowthDeviation.booleanValue()) {
					yes.setSelected(true);
				}
				else {
					no.setSelected(true);
				}
			}
			else {
				no.setSelected(true);
			}
			applicationTable.add(yes, 2, aRow);
			applicationTable.add(no, 3, aRow);
			applicationTable.add(noAnswer, 4, aRow++);
	
			applicationTable.add(getSmallHeader(localize("child.growth_deviation_details", "Growth deviation details")), 1, aRow);
			applicationTable.add(new Break(), 1, aRow);
			applicationTable.add(getTextArea(PARAMETER_GROWTH_DEVIATION_DETAILS, getCareBusiness().getGrowthDeviationDetails(getSession().getUser())), 1, aRow++);
	
			applicationTable.add(getSmallHeader(localize("child.has_allergies", "Has allergies")), 1, aRow);
			yes = getRadioButton(PARAMETER_ALLERGIES, Boolean.TRUE.toString());
			no = getRadioButton(PARAMETER_ALLERGIES, Boolean.FALSE.toString());
			noAnswer = getRadioButton(PARAMETER_ALLERGIES, "");
			Boolean hasAllergies = getCareBusiness().hasAllergies(getSession().getUser());
			if (hasAllergies != null) {
				if (hasAllergies.booleanValue()) {
					yes.setSelected(true);
				}
				else {
					no.setSelected(true);
				}
			}
			else {
				no.setSelected(true);
			}
			applicationTable.add(yes, 2, aRow);
			applicationTable.add(no, 3, aRow);
			applicationTable.add(noAnswer, 4, aRow++);
	
			applicationTable.add(getSmallHeader(localize("child.allergies_details", "Allergies details")), 1, aRow);
			applicationTable.add(new Break(), 1, aRow);
			applicationTable.add(getTextArea(PARAMETER_ALLERGIES_DETAILS, getCareBusiness().getAllergiesDetails(getSession().getUser())), 1, aRow++);
	
			applicationTable.add(getSmallHeader(localize("child.last_care_provider", "Last care provider")), 1, aRow);
			applicationTable.add(new Break(), 1, aRow);
			applicationTable.add(getTextArea(PARAMETER_LAST_CARE_PROVIDER, getCareBusiness().getLastCareProvider(getSession().getUser())), 1, aRow++);
	
			applicationTable.add(getSmallHeader(localize("child.can_contact_last_care_provider", "Can contact last care provider")), 1, aRow);
			yes = getRadioButton(PARAMETER_CAN_CONTACT_LAST_PROVIDER, Boolean.TRUE.toString());
			no = getRadioButton(PARAMETER_CAN_CONTACT_LAST_PROVIDER, Boolean.FALSE.toString());
			boolean canContactLastProvider = getCareBusiness().canContactLastCareProvider(getSession().getUser());
			if (canContactLastProvider) {
				yes.setSelected(true);
			}
			else {
				no.setSelected(true);
			}
			applicationTable.add(yes, 2, aRow);
			applicationTable.add(no, 3, aRow++);
	
			applicationTable.add(getSmallHeader(localize("child.other_information", "Other information")), 1, aRow);
			applicationTable.add(new Break(), 1, aRow);
			applicationTable.add(getTextArea(PARAMETER_OTHER_INFORMATION, getCareBusiness().getOtherInformation(getSession().getUser())), 1, aRow++);
		}
		
		applicationTable.add(getSmallHeader(localize("child.can_diplay_after_school_images_images", "Can display images")), 1, aRow);
		RadioButton yes = getRadioButton(PARAMETER_CAN_DISPLAY_IMAGES, Boolean.TRUE.toString());
		RadioButton no = getRadioButton(PARAMETER_CAN_DISPLAY_IMAGES, Boolean.FALSE.toString());
		boolean canDisplayImages = getBusiness().canDisplayAfterSchoolCareImages(getSession().getUser());
		if (canDisplayImages) {
			yes.setSelected(true);
		}
		else {
			no.setSelected(true);
		}
		applicationTable.add(yes, 2, aRow);
		applicationTable.add(no, 3, aRow++);

		applicationTable.add(getSmallHeader(localize("child.other_after_school_information", "Other after school care information")), 1, aRow);
		applicationTable.add(new Break(), 1, aRow);
		applicationTable.add(getTextArea(PARAMETER_AFTER_SCHOOL_INFORMATION, getBusiness().getAfterSchoolCareOtherInformation(getSession().getUser())), 1, aRow);

		for (int a = 1; a <= applicationTable.getRows(); a++) {
			if (a > 1) {
				applicationTable.setLeftCellBorder(2, a, 1, "#D7D7D7", "solid");
				applicationTable.setLeftCellBorder(3, a, 1, "#D7D7D7", "solid");
				applicationTable.setLeftCellBorder(4, a, 1, "#D7D7D7", "solid");
			}
			
			applicationTable.setCellpaddingRight(1, a, 6);
			applicationTable.setAlignment(2, a, Table.HORIZONTAL_ALIGN_CENTER);
			applicationTable.setAlignment(3, a, Table.HORIZONTAL_ALIGN_CENTER);
			applicationTable.setAlignment(4, a, Table.HORIZONTAL_ALIGN_CENTER);
		}

		table.setHeight(row++, 18);
		
		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(localize("previous", "Previous")));
		previous.setValueOnClick(PARAMETER_ACTION, iHideDetailsPhases ? String.valueOf(ACTION_PHASE_1) : String.valueOf(ACTION_PHASE_3));
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("next", "Next")));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_5));
		
		table.add(previous, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(next, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(getHelpButton("help_school_application_phase_5"), 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setCellpaddingRight(1, row, 12);

		add(form);
	}
	
	protected void showPhaseFive(IWContext iwc, SchoolSeason season) throws RemoteException {
		saveChildInfo(iwc);

		Form form = createForm();
		form.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_5));
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;
		
		table.add(getPersonInfoTable(iwc, getSession().getUser()), 1, row++);
		table.setHeight(row++, 6);
		
		table.add(getText(localize("application.care_time_information", "Care time information")), 1, row++);
		table.setHeight(row++, 6);
		
		Table applicationTable = new Table(4, 5);
		applicationTable.setCellpadding(3);
		applicationTable.setCellspacing(0);
		applicationTable.setWidth(Table.HUNDRED_PERCENT);
		table.add(applicationTable, 1, row++);
		int aRow = 1;
		
		AfterSchoolChoice choice = null;
		try {
			choice = getAfterSchoolBusiness().findChoiceByChild(getSession().getUser(), season, 1);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
		}
		
		String[] days = { localize("monday", "Monday"), localize("tuesday", "Tuesday"), localize("wednesday", "Wednesday"), localize("thursday", "Thursday"), localize("friday", "Friday") };
		for (int a = 0; a <= 4; a++) {
			AfterSchoolCareDays day = null;
			if (choice != null) {
				try {
					day = getAfterSchoolBusiness().getDay(choice, (a+1));
				}
				catch (FinderException fe) {
					//Nothing found...
				}
			}
			
			applicationTable.add(getSmallHeader(days[a]), 1, aRow);
			applicationTable.add(getTimeDropdown(PARAMETER_TIME + "_" + (a+1), iwc.getCurrentLocale(), day), 2, aRow);
			
			RadioButton pickedUp = getRadioButton(PARAMETER_PICKED_UP + "_" + (a+1), Boolean.TRUE.toString());
			pickedUp.setSelected(true);
			if (day != null) {
				pickedUp.setSelected(day.isPickedUp());
			}
			pickedUp.keepStatusOnAction(true);
			RadioButton walksSelf = getRadioButton(PARAMETER_PICKED_UP + "_" + (a+1), Boolean.FALSE.toString());
			if (day != null) {
				walksSelf.setSelected(!day.isPickedUp());
			}
			walksSelf.keepStatusOnAction(true);
			
			applicationTable.add(pickedUp, 3, aRow);
			applicationTable.add(Text.getNonBrakingSpace(), 3, aRow);
			applicationTable.add(getSmallText(localize("application.picked_up", "Is picked up")), 3, aRow);
			
			applicationTable.add(walksSelf, 4, aRow);
			applicationTable.add(Text.getNonBrakingSpace(), 4, aRow);
			applicationTable.add(getSmallText(localize("application.walks_self", "Walks home")), 4, aRow++);
		}

		table.setHeight(row++, 12);
		table.add(getErrorText(localize("application.opening_hours", "The after school care providers are open until 17:15.")), 1, row++);
		table.setHeight(row++, 18);

		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(localize("previous", "Previous")));
		previous.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_4));
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("next", "Next")));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_6));
		
		table.add(previous, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(next, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(getHelpButton("help_after_school_care_application_phase_5"), 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setCellpaddingRight(1, row, 12);

		add(form);
	}
	
	private void showPhaseSix(IWContext iwc, SchoolSeason season) throws RemoteException {
		boolean timesSet = false;
		for (int i = 0; i < 5; i++) {
			if (iwc.isParameterSet(PARAMETER_TIME + "_" + (i+1))) {
				timesSet = true;
				break;
			}
		}
		if (!timesSet) {
			getParentPage().setAlertOnLoad(localize("must_select_departure_times", "You have to select times for departure"));
			showPhaseFive(iwc);
			return;
		}
		
		Form form = createForm();
		form.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_6));
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;
		
		table.add(getPersonInfoTable(iwc, getSession().getUser()), 1, row++);
		table.setHeight(row++, 6);
		
		table.add(getText(localize("application.payment_information", "Payment information")), 1, row++);
		table.setHeight(row++, 6);
		
		table.add(getHeader(localize("application.payment_details", "Payment details")), 1, row++);
		table.setHeight(row++, 12);
		
		AfterSchoolChoice choice = null;
		try {
			choice = getAfterSchoolBusiness().findChoiceByChild(getSession().getUser(), season, 1);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
		}
		
		RadioButton giro = getRadioButton(PARAMETER_CREDITCARD_PAYMENT, Boolean.FALSE.toString());
		giro.setSelected(true);
		if (choice != null) {
			giro.setSelected(choice.getPayerName() == null);
		}
		giro.keepStatusOnAction(true);
		RadioButton creditcard = getRadioButton(PARAMETER_CREDITCARD_PAYMENT, Boolean.TRUE.toString());
		if (choice != null) {
			creditcard.setSelected(choice.getPayerName() != null);
		}
		creditcard.keepStatusOnAction(true);
		
		table.add(giro, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getSmallText(localize("application.payment_giro", "Payment with giro")), 1, row++);
		table.setHeight(row++, 3);
		
		table.add(creditcard, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getSmallText(localize("application.payment_creditcard", "Payment with creditcard")), 1, row++);

		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("next", "Next")));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_7));
		next.setDisabled(true);
		
		CheckBox agree = getCheckBox("agree", Boolean.TRUE.toString());
		agree.setToEnableWhenChecked(next);
		agree.setToDisableWhenUnchecked(next);
		
		table.setHeight(row++, 12);

		table.add(getText(localize("application.agreement", "Agreement information")), 1, row++);
		table.setHeight(row++, 6);
		table.add(agree, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getHeader(localize("application.agree_terms", "Yes, I agree")), 1, row++);
		
		table.setHeight(row++, 18);

		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(localize("previous", "Previous")));
		previous.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_5));
		table.add(previous, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(next, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(getHelpButton("help_after_school_care_application_phase_6"), 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setCellpaddingRight(1, row, 12);

		add(form);
	}
	
	private void showPhaseSeven(IWContext iwc, SchoolSeason season) throws RemoteException {
		Form form = createForm();
		form.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_7));
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;
		
		table.add(getPersonInfoTable(iwc, getSession().getUser()), 1, row++);
		table.setHeight(row++, 6);
		
		table.add(getText(localize("application.creditcard_information", "Creditcard information")), 1, row++);
		table.setHeight(row++, 12);
		
		Table applicationTable = new Table(2, 5);
		applicationTable.setCellpadding(3);
		applicationTable.setCellspacing(0);
		table.add(applicationTable, 1, row++);
		table.setHeight(row++, 12);
		
		AfterSchoolChoice choice = null;
		try {
			choice = getAfterSchoolBusiness().findChoiceByChild(getSession().getUser(), season, 1);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
		}
		
		TextInput payerName = getTextInput(PARAMETER_PAYER_NAME, null);
		if (choice != null && choice.getPayerName() != null) {
			payerName.setContent(choice.getPayerName());
		}
		payerName.keepStatusOnAction(true);
		payerName.setLength(24);
		TextInput payerPersonalID = getTextInput(PARAMETER_PAYER_PERSONAL_ID, null);
		if (choice != null && choice.getPayerPersonalID() != null) {
			payerPersonalID.setContent(choice.getPayerPersonalID());
		}
		payerPersonalID.setAsPersonalID(iwc.getCurrentLocale(), localize("application.personal_id_invalid", "Personal ID invalid."));
		payerPersonalID.keepStatusOnAction(true);
		payerPersonalID.setLength(24);
		
		DropdownMenu cardType = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_CARD_TYPE));
		cardType.addMenuElement(CareConstants.CARD_TYPE_EUROCARD, localize("application.eurocard", "Eurocard"));
		cardType.addMenuElement(CareConstants.CARD_TYPE_VISA, localize("application.visa", "Visa"));
		if (choice != null && choice.getCardType() != null) {
			cardType.setSelectedElement(choice.getCardType());
		}
		cardType.keepStatusOnAction(true);
		
		TextInput cardNumber = getTextInput(PARAMETER_CARD_NUMBER, null);
		if (choice != null && choice.getCardNumber() != null) {
			cardNumber.setContent(choice.getCardNumber());
		}
		cardNumber.setLength(16);
		cardNumber.setMaxlength(16);
		cardNumber.keepStatusOnAction(true);
		
		DropdownMenu validMonth = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_VALID_MONTH));
		if (choice != null && choice.getCardValidMonth() != -1) {
			validMonth.setSelectedElement(choice.getCardValidMonth());
		}
		validMonth.keepStatusOnAction(true);
		for (int a = 1; a <= 12; a++) {
			validMonth.addMenuElement(a, TextSoap.addZero(a));
		}
		
		IWTimestamp stamp = new IWTimestamp();
		DropdownMenu validYear = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_VALID_YEAR));
		if (choice != null && choice.getCardValidYear() != -1) {
			validYear.setSelectedElement(choice.getCardValidYear());
		}
		validYear.keepStatusOnAction(true);
		int year = stamp.getYear();
		for (int a = year; a <= year + 10; a++) {
			validYear.addMenuElement(stamp.getYear(), String.valueOf(stamp.getYear()));
			stamp.addYears(1);
		}
		
		applicationTable.add(getSmallHeader(localize("application.payer_name", "Name") + ":"), 1, 1);
		applicationTable.add(payerName, 2, 1);
		applicationTable.add(getSmallHeader(localize("application.payer_personal_id", "Personal ID") + ":"), 1, 2);
		applicationTable.add(payerPersonalID, 2, 2);
		applicationTable.add(getSmallHeader(localize("application.card_type", "Card type") + ":"), 1, 3);
		applicationTable.add(cardType, 2, 3);
		applicationTable.add(getSmallHeader(localize("application.card_number", "Card number") + ":"), 1, 4);
		applicationTable.add(cardNumber, 2, 4);
		applicationTable.add(getSmallHeader(localize("application.card_valid_time", "Card valid through") + ":"), 1, 5);
		applicationTable.add(validMonth, 2, 5);
		applicationTable.add(Text.getNonBrakingSpace(), 2, 5);
		applicationTable.add(validYear, 2, 5);

		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("next", "Next")));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_OVERVIEW));

		table.setHeight(row++, 18);

		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(localize("previous", "Previous")));
		previous.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_6));
		
		table.add(previous, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(next, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(getHelpButton("help_after_school_care_application_phase_7"), 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setCellpaddingRight(1, row, 12);

		add(form);
	}
	
	protected void showOverview(IWContext iwc) throws RemoteException {
		Form form = createForm();
		form.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_OVERVIEW));
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;
		
		table.add(getPersonInfoTable(iwc, getSession().getUser()), 1, row++);
		table.setHeight(row++, 6);
		
		table.add(getHeader(localize("application.overview", "Overview")), 1, row++);
		table.setHeight(row++, 18);
		
		Table verifyTable = new Table();
		verifyTable.setCellpadding(getCellpadding());
		verifyTable.setCellspacing(getCellspacing());
		verifyTable.setColumns(2);
		table.add(verifyTable, 1, row++);
		int iRow = 1;
		
		Integer providerPK = new Integer(iwc.getParameter(PARAMETER_PROVIDER));
		School provider = getSchoolBusiness().getSchool(providerPK);
		
		verifyTable.add(getSmallHeader(localize("application.after_school_care_provider", "Provider")), 1, iRow);
		verifyTable.add(getSmallText(provider.getSchoolName()), 2, iRow++);

		verifyTable.setHeight(iRow++, 6);
		verifyTable.mergeCells(1, iRow, 2, iRow);
		verifyTable.setBottomCellBorder(1, iRow++, 1, "#D7D7D7", "solid");
		verifyTable.setHeight(iRow++, 6);

		String[] days = { localize("monday", "Monday"), localize("tuesday", "Tuesday"), localize("wednesday", "Wednesday"), localize("thursday", "Thursday"), localize("friday", "Friday") };
		for (int a = 0; a < days.length; a++) {
			boolean pickedUp = new Boolean(iwc.getParameter(PARAMETER_PICKED_UP + "_" + (a+1))).booleanValue();
			String time = iwc.isParameterSet(PARAMETER_TIME + "_" + (a+1)) ? iwc.getParameter(PARAMETER_TIME + "_" + (a+1)) : null;
			
			if (time.length() > 0) {
				verifyTable.add(getSmallHeader(days[a]), 1, iRow);
				verifyTable.add(getSmallText(time + " - " + (pickedUp ? localize("application.picked_up", "Is picked up") : localize("application.walks_self", "Walks home"))), 2, iRow++);
			}
		}
		
		verifyTable.setHeight(iRow++, 6);
		verifyTable.mergeCells(1, iRow, 2, iRow);
		verifyTable.setBottomCellBorder(1, iRow++, 1, "#D7D7D7", "solid");
		verifyTable.setHeight(iRow++, 6);

		boolean creditcardPayment = false;
		if (iwc.isParameterSet(PARAMETER_CREDITCARD_PAYMENT)) {
			creditcardPayment = new Boolean(iwc.getParameter(PARAMETER_CREDITCARD_PAYMENT)).booleanValue();
		}
		String payerName = iwc.getParameter(PARAMETER_PAYER_NAME);
		String payerPersonalID = iwc.getParameter(PARAMETER_PAYER_PERSONAL_ID);
		String cardType = iwc.getParameter(PARAMETER_CARD_TYPE);
		String cardNumber = iwc.getParameter(PARAMETER_CARD_NUMBER);
		int validMonth = iwc.isParameterSet(PARAMETER_VALID_MONTH) ? Integer.parseInt(iwc.getParameter(PARAMETER_VALID_MONTH)) : -1;
		int validYear = iwc.isParameterSet(PARAMETER_VALID_MONTH) ? Integer.parseInt(iwc.getParameter(PARAMETER_VALID_YEAR)) : -1;

		if (creditcardPayment) {
			verifyTable.mergeCells(1, iRow, 2, iRow);
			verifyTable.add(getBooleanTable(getSmallHeader(localize("application.payment_creditcard", "Payment with creditcard")), true), 1, iRow++);
			verifyTable.setHeight(iRow++, 6);
			
			verifyTable.add(getSmallHeader(localize("application.payer_name", "Name")), 1, iRow);
			verifyTable.add(getSmallText(payerName), 2, iRow++);
			
			verifyTable.add(getSmallHeader(localize("application.payer_personal_id", "Personal ID")), 1, iRow);
			verifyTable.add(getSmallText(payerPersonalID), 2, iRow++);
			
			verifyTable.add(getSmallHeader(localize("application.card_type", "Card type")), 1, iRow);
			verifyTable.add(getSmallText(cardType), 2, iRow++);
			
			verifyTable.add(getSmallHeader(localize("application.card_number", "Card number")), 1, iRow);
			verifyTable.add(getSmallText(cardNumber), 2, iRow++);
			
			verifyTable.add(getSmallHeader(localize("application.card_valid_time", "Card valid through")), 1, iRow);
			verifyTable.add(getSmallText(validMonth + "/" + validYear), 2, iRow++);
			
		}
		else {
			verifyTable.mergeCells(1, iRow, 2, iRow);
			verifyTable.add(getBooleanTable(getSmallHeader(localize("application.payment_giro", "Payment with giro")), true), 1, iRow++);
		}
		
		verifyTable.setHeight(iRow++, 6);
		verifyTable.mergeCells(1, iRow, 2, iRow);
		verifyTable.setBottomCellBorder(1, iRow++, 1, "#D7D7D7", "solid");
		verifyTable.setHeight(iRow++, 6);		
		
		if (!iHideDetailsPhases) {
			row = addChildInformation(verifyTable, getSession().getUser(), row);
		}
		
		boolean canDisplayImages = getBusiness().canDisplayAfterSchoolCareImages(getSession().getUser());
		String otherAfterSchoolInformation = getBusiness().getAfterSchoolCareOtherInformation(getSession().getUser());
		verifyTable.mergeCells(1, iRow, table.getColumns(), iRow);
		verifyTable.add(getBooleanTable(getSmallHeader(localize("child.can_diplay_after_school_images_images", "Can display images")), canDisplayImages), 1, iRow++);
		
		if (otherAfterSchoolInformation != null) {
			verifyTable.setHeight(iRow++, 6);
			verifyTable.mergeCells(1, iRow, 2, iRow);
			verifyTable.setBottomCellBorder(1, iRow++, 1, "#D7D7D7", "solid");
			verifyTable.setHeight(iRow++, 6);		
			
			verifyTable.mergeCells(1, iRow, table.getColumns(), iRow);
			verifyTable.add(getTextAreaTable(getSmallHeader(localize("child.other_after_school_information", "Other after school information")), otherAfterSchoolInformation), 1, iRow++);
		}
		
		verifyTable.setWidth(1, "50%");
		verifyTable.setWidth(2, "50%");

		table.setHeight(row++, 18);

		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(localize("previous", "Previous")));
		previous.setValueOnClick(PARAMETER_ACTION, String.valueOf(creditcardPayment ? ACTION_PHASE_7 : ACTION_PHASE_6));
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("next", "Next")));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_SAVE));
		
		table.add(previous, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(next, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(getHelpButton("help_after_school_care_application_overview"), 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setCellpaddingRight(1, row, 12);
		
		next.setSubmitConfirm(localize("confirm_after_school_application_submit", "Are you sure you want to send the application?"));
		form.setToDisableOnSubmit(next, true);

		add(form);
	}
	
	private void viewApplication(IWContext iwc, SchoolSeason season) throws RemoteException {
		Form form = createForm();
		form.addParameter(PARAMETER_ACTION, String.valueOf(""));
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;
		
		table.add(getPersonInfoTable(iwc, getSession().getUser()), 1, row++);
		table.setHeight(row++, 6);
		
		table.add(getHeader(localize("application.overview", "Overview")), 1, row++);
		table.setHeight(row++, 18);
		
		Table verifyTable = new Table();
		verifyTable.setCellpadding(getCellpadding());
		verifyTable.setCellspacing(getCellspacing());
		verifyTable.setColumns(2);
		table.add(verifyTable, 1, row++);
		int iRow = 1;
		
		AfterSchoolChoice choice = null;
		try {
			choice = getAfterSchoolBusiness().findChoiceByChild(getSession().getUser(), season, 1);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
		}
		
		School provider = choice.getProvider();
		
		verifyTable.add(getSmallHeader(localize("application.after_school_care_provider", "Provider")), 1, iRow);
		verifyTable.add(getSmallText(provider.getSchoolName()), 2, iRow++);

		verifyTable.setHeight(iRow++, 6);
		verifyTable.mergeCells(1, iRow, 2, iRow);
		verifyTable.setBottomCellBorder(1, iRow++, 1, "#D7D7D7", "solid");
		verifyTable.setHeight(iRow++, 6);

		String[] localizedDays = { localize("monday", "Monday"), localize("tuesday", "Tuesday"), localize("wednesday", "Wednesday"), localize("thursday", "Thursday"), localize("friday", "Friday") };
		Collection days = getAfterSchoolBusiness().getDays(choice);
		if (!days.isEmpty()) {
			Iterator iter = days.iterator();
			while (iter.hasNext()) {
				AfterSchoolCareDays day = (AfterSchoolCareDays) iter.next();
				IWTimestamp stamp = new IWTimestamp(day.getTimeOfDeparture());
				boolean pickedUp = day.isPickedUp();
				
				verifyTable.add(getSmallHeader(localizedDays[day.getDayOfWeek() - 1]), 1, iRow);
				verifyTable.add(getSmallText(stamp.getDateString("HH:mm", iwc.getCurrentLocale()) + " - " + (pickedUp ? localize("application.picked_up", "Is picked up") : localize("application.walks_self", "Walks home"))), 2, iRow++);
			}
			
			verifyTable.setHeight(iRow++, 6);
			verifyTable.mergeCells(1, iRow, 2, iRow);
			verifyTable.setBottomCellBorder(1, iRow++, 1, "#D7D7D7", "solid");
			verifyTable.setHeight(iRow++, 6);
		}

		boolean creditcardPayment = choice.getPayerName() == null;
		String payerName = choice.getPayerName();
		String payerPersonalID = choice.getPayerPersonalID();
		String cardType = choice.getCardType();
		String cardNumber = choice.getCardNumber();
		int validMonth = choice.getCardValidMonth();
		int validYear = choice.getCardValidYear();

		if (creditcardPayment) {
			verifyTable.mergeCells(1, iRow, 2, iRow);
			verifyTable.add(getBooleanTable(getSmallHeader(localize("application.payment_creditcard", "Payment with creditcard")), true), 1, iRow++);
			verifyTable.setHeight(iRow++, 6);
			
			verifyTable.add(getSmallHeader(localize("application.payer_name", "Name")), 1, iRow);
			verifyTable.add(getSmallText(payerName), 2, iRow++);
			
			verifyTable.add(getSmallHeader(localize("application.payer_personal_id", "Personal ID")), 1, iRow);
			verifyTable.add(getSmallText(payerPersonalID), 2, iRow++);
			
			verifyTable.add(getSmallHeader(localize("application.card_type", "Card type")), 1, iRow);
			verifyTable.add(getSmallText(cardType), 2, iRow++);
			
			verifyTable.add(getSmallHeader(localize("application.card_number", "Card number")), 1, iRow);
			verifyTable.add(getSmallText(cardNumber), 2, iRow++);
			
			verifyTable.add(getSmallHeader(localize("application.card_valid_time", "Card valid through")), 1, iRow);
			verifyTable.add(getSmallText(validMonth + "/" + validYear), 2, iRow++);
			
		}
		else {
			verifyTable.mergeCells(1, iRow, 2, iRow);
			verifyTable.add(getBooleanTable(getSmallHeader(localize("application.payment_giro", "Payment with giro")), true), 1, iRow++);
		}
		
		verifyTable.setHeight(iRow++, 6);
		verifyTable.mergeCells(1, iRow, 2, iRow);
		verifyTable.setBottomCellBorder(1, iRow++, 1, "#D7D7D7", "solid");
		verifyTable.setHeight(iRow++, 6);		
		
		row = addChildInformation(verifyTable, getSession().getUser(), row);
		
		boolean canDisplayImages = getBusiness().canDisplayAfterSchoolCareImages(getSession().getUser());
		String otherAfterSchoolInformation = getBusiness().getAfterSchoolCareOtherInformation(getSession().getUser());
		verifyTable.mergeCells(1, iRow, table.getColumns(), iRow);
		verifyTable.add(getBooleanTable(getSmallHeader(localize("child.can_diplay_after_school_images_images", "Can display images")), canDisplayImages), 1, iRow++);
		
		if (otherAfterSchoolInformation != null) {
			verifyTable.setHeight(iRow++, 6);
			verifyTable.mergeCells(1, iRow, 2, iRow);
			verifyTable.setBottomCellBorder(1, iRow++, 1, "#D7D7D7", "solid");
			verifyTable.setHeight(iRow++, 6);		
			
			verifyTable.mergeCells(1, iRow, table.getColumns(), iRow);
			verifyTable.add(getTextAreaTable(getSmallHeader(localize("child.other_after_school_information", "Other after school information")), otherAfterSchoolInformation), 1, iRow++);
		}
		
		verifyTable.setWidth(1, "50%");
		verifyTable.setWidth(2, "50%");

		table.setHeight(row++, 18);

		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("edit", "Edit")));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_1));
		
		if (choice.getStatus().equals(getBusiness().getCaseStatusPreliminary().getStatus())) {
			table.add(next, 1, row);
			table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		}
		table.add(getHelpButton("help_after_school_application_view"), 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setCellpaddingRight(1, row, 12);
		
		add(form);
	}
	
	private void save(IWContext iwc) throws RemoteException {
	
		Integer seasonPK = new Integer(iwc.getParameter(PARAMETER_SEASON));
		Integer providerPK = new Integer(iwc.getParameter(PARAMETER_PROVIDER));
		SchoolSeason season = getSchoolBusiness().getSchoolSeason(seasonPK);
		School provider = getSchoolBusiness().getSchool(providerPK);
		
		int[] days = { 1, 2, 3, 4, 5 };
		String[] time = new String[days.length];
		boolean[] pickedUp = new boolean[days.length];
		for (int a = 0; a < days.length; a++) {
			pickedUp[a] = new Boolean(iwc.getParameter(PARAMETER_PICKED_UP + "_" + (a+1))).booleanValue();
			time[a] = iwc.isParameterSet(PARAMETER_TIME + "_" + (a+1)) ? iwc.getParameter(PARAMETER_TIME + "_" + (a+1)) : null;
		}
		
		String payerName = iwc.getParameter(PARAMETER_PAYER_NAME);
		String payerPersonalID = iwc.getParameter(PARAMETER_PAYER_PERSONAL_ID);
		String cardType = iwc.getParameter(PARAMETER_CARD_TYPE);
		String cardNumber = iwc.getParameter(PARAMETER_CARD_NUMBER);
		int validMonth = iwc.isParameterSet(PARAMETER_VALID_MONTH) ? Integer.parseInt(iwc.getParameter(PARAMETER_VALID_MONTH)) : -1;
		int validYear = iwc.isParameterSet(PARAMETER_VALID_MONTH) ? Integer.parseInt(iwc.getParameter(PARAMETER_VALID_YEAR)) : -1;

		boolean saved = getAfterSchoolBusiness().storeAfterSchoolCare(new IWTimestamp(), iwc.getCurrentUser(), getSession().getUser(), provider, null, season, days, time, pickedUp, payerName, payerPersonalID, cardType, cardNumber, validMonth, validYear);
		
		if (saved) {
			Table table = new Table();
			table.setCellpadding(0);
			table.setCellspacing(0);
			table.setWidth(Table.HUNDRED_PERCENT);
			int row = 1;
			
			table.add(getHeader(localize("application.after_school_save_completed", "Application sent")), 1, row++);
			table.setHeight(row++, 6);
			
			table.add(getText(localize("application.after_school_care_send_confirmation", "Your application for a after school care has been processed.")), 1, row++);
			table.setHeight(row++, 18);
			
			GenericButton home = getButton(new GenericButton(localize("my_page", "My page")));
			if (iHomePage != null) {
				home.setPageToOpen(iHomePage);
			}

			table.add(home, 1, row);
			table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
			table.setCellpaddingRight(1, row, 12);
			
			add(table);
		}
		else {
			add(getErrorText(localize("application.save_failed", "Save failed, contact the community office")));
		}
	}

	protected void saveChildInfo(IWContext iwc) throws RemoteException {
		boolean canDisplayImages = iwc.isParameterSet(PARAMETER_CAN_DISPLAY_IMAGES);
		String otherAfterSchoolInformation = iwc.getParameter(PARAMETER_AFTER_SCHOOL_INFORMATION);

		if (!iHideDetailsPhases) {
			Boolean growthDeviation = iwc.isParameterSet(PARAMETER_GROWTH_DEVIATION) ? new Boolean(iwc.getParameter(PARAMETER_GROWTH_DEVIATION)) : null;
			Boolean allergies = iwc.isParameterSet(PARAMETER_ALLERGIES) ? new Boolean(iwc.getParameter(PARAMETER_ALLERGIES)) : null;
			
			boolean canContactLastProvider = iwc.isParameterSet(PARAMETER_CAN_CONTACT_LAST_PROVIDER);
			
			String growthDeviationDetails = iwc.getParameter(PARAMETER_GROWTH_DEVIATION_DETAILS);
			String allergiesDetails = iwc.getParameter(PARAMETER_ALLERGIES_DETAILS);
			String lastCareProvider = iwc.getParameter(PARAMETER_LAST_CARE_PROVIDER);
			String otherInformation = iwc.getParameter(PARAMETER_OTHER_INFORMATION);
			
			getCareBusiness().storeChildInformation(getSession().getUser(), growthDeviation, growthDeviationDetails, allergies, allergiesDetails, lastCareProvider, canContactLastProvider, otherInformation);
		}
		getBusiness().storeChildAfterSchoolCareInformation(getSession().getUser(), canDisplayImages, otherAfterSchoolInformation);
	}

	private int parseAction(IWContext iwc) {
		int action = iShowOverview ? ACTION_VIEW : ACTION_PHASE_1;
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		
		if (action == ACTION_PHASE_2 && iHideDetailsPhases) {
			action = ACTION_PHASE_4;
		}
		
		boolean creditcardPayment = false;
		if (iwc.isParameterSet(PARAMETER_CREDITCARD_PAYMENT)) {
			creditcardPayment = new Boolean(iwc.getParameter(PARAMETER_CREDITCARD_PAYMENT)).booleanValue();
		}
		if (action == ACTION_PHASE_7 && !creditcardPayment) {
			action = ACTION_OVERVIEW;
		}

		return action;
	}
	
	protected Form createForm() {
		Form form = new Form();
		form.maintainParameter(PARAMETER_SEASON);
		form.maintainParameter(PARAMETER_PROVIDER);
		for (int a = 1; a <= 5; a++) {
			form.maintainParameter(PARAMETER_TIME + "_" + a);
			form.maintainParameter(PARAMETER_PICKED_UP + "_" + a);
		}
		form.maintainParameter(PARAMETER_CREDITCARD_PAYMENT);
		form.maintainParameter(PARAMETER_CARD_TYPE);
		form.maintainParameter(PARAMETER_CARD_NUMBER);
		form.maintainParameter(PARAMETER_PAYER_NAME);
		form.maintainParameter(PARAMETER_PAYER_PERSONAL_ID);
		form.maintainParameter(PARAMETER_VALID_MONTH);
		form.maintainParameter(PARAMETER_VALID_YEAR);
		
		return form;
	}
	
	private DropdownMenu getTimeDropdown(String parameterName, Locale locale, AfterSchoolCareDays day) {
		DropdownMenu menu = (DropdownMenu) getStyledInterface(new DropdownMenu(parameterName));
		menu.addMenuElementFirst("", localize("not_staying", "Not staying"));
		menu.keepStatusOnAction(true);
		
		IWTimestamp stamp = new IWTimestamp();
		stamp.setAsTime();
		stamp.setTime(14, 0, 0);
		
		IWTimestamp end = new IWTimestamp();
		end.setAsTime();
		end.setTime(17, 15, 0);
		
		while (stamp.getTime().before(end.getTime())) {
			menu.addMenuElement(stamp.getDateString("HH:mm:ss"), stamp.getDateString("HH:mm", locale));
			stamp.addMinutes(30);
		}
		
		if (day != null) {
			IWTimestamp time = new IWTimestamp(day.getTimeOfDeparture());
			menu.setSelectedElement(time.getDateString("HH:mm:ss", locale));
		}
		
		return menu;
	}
	
	public void setHideDetailsPhases(boolean hideDetailsPhases) {
		iHideDetailsPhases = hideDetailsPhases;
	}	
}