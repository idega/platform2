/*
 * $Id: SchoolApplication.java,v 1.24 2005/10/14 05:05:36 laddi Exp $
 * Created on Aug 3, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.presentation;

import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.care.business.CareConstants;
import se.idega.idegaweb.commune.care.business.Relative;
import se.idega.idegaweb.commune.school.business.SchoolAreaCollectionHandler;
import se.idega.idegaweb.commune.school.data.SchoolChoice;
import com.idega.block.school.business.SchoolYearComparator;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolArea;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolYear;
import com.idega.core.builder.data.ICPage;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.localisation.data.ICLanguage;
import com.idega.core.localisation.data.ICLanguageHome;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOCreateException;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.remotescripting.RemoteScriptHandler;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.HorizontalRule;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.user.business.NoEmailFoundException;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.data.User;
import com.idega.util.Age;
import com.idega.util.PersonalIDFormatter;

/**
 * Last modified: $Date: 2005/10/14 05:05:36 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.24 $
 */
public class SchoolApplication extends SchoolBlock {

	protected static final int ACTION_PHASE_1 = 1;
	protected static final int ACTION_PHASE_2 = 2;
	protected static final int ACTION_PHASE_3 = 3;
	protected static final int ACTION_PHASE_4 = 4;
	protected static final int ACTION_PHASE_5 = 5;
	protected static final int ACTION_OVERVIEW = 6;
	protected static final int ACTION_VIEW = 7;
	protected static final int ACTION_SAVE = 0;
	
	protected static final String PARAMETER_ACTION = "prm_action";

	private static final String PARAMETER_SCHOOLS = "prm_school";
	private static final String PARAMETER_AREA = "prm_area";
	public static final String PARAMETER_SEASON = "prm_season";

	private static final String PARAMETER_HOME_SCHOOL = "prm_home_school";
	private static final String PARAMETER_MESSAGE = "prm_message";
	private static final String PARAMETER_LANGUAGE = "prm_language";

	private static final String PARAMETER_USER = "prm_user_id";
	private static final String PARAMETER_RELATIVE = "prm_relative_user_id";
	private static final String PARAMETER_PERSONAL_ID = "prm_personal_id";
	private static final String PARAMETER_HOME_PHONE = "prm_home";
	private static final String PARAMETER_WORK_PHONE = "prm_work";
	private static final String PARAMETER_MOBILE_PHONE = "prm_mobile";
	private static final String PARAMETER_EMAIL = "prm_email";
	private static final String PARAMETER_RELATION = "prm_relation";
	
	protected static final String PARAMETER_GROWTH_DEVIATION = "prm_growth_deviation";
	protected static final String PARAMETER_GROWTH_DEVIATION_DETAILS = "prm_growth_deviation_details";
	protected static final String PARAMETER_ALLERGIES = "prm_allergies";
	protected static final String PARAMETER_ALLERGIES_DETAILS = "prm_allergies_details";
	protected static final String PARAMETER_LAST_CARE_PROVIDER = "prm_last_care_provider";
	protected static final String PARAMETER_CAN_CONTACT_LAST_PROVIDER = "prm_can_contact_last_provider";
	protected static final String PARAMETER_OTHER_INFORMATION = "prm_other_information";
	protected static final String PARAMETER_CAN_DISPLAY_IMAGES = "prm_can_display_images";

	private boolean iHomeSchoolChosen = false;
	private boolean iSchoolChange = false;
	private boolean iUseHomeSchool = true;
	private boolean iShowOverview = false;
	
	private ICPage iAfterSchoolCarePage;
	protected ICPage iHomePage;
	private int iMaxAge = -1;

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.school.presentation.SchoolBlock#init(com.idega.presentation.IWContext)
	 */
	public void present(IWContext iwc) throws Exception {
		SchoolSeason season = null;
		try {
			if (iSchoolChange) {
				season = getSchoolBusiness().getCurrentSchoolSeason(getSchoolBusiness().getCategoryElementarySchool());
			}
			else {
				season = getCareBusiness().getCurrentSeason();
			}
		}
		catch (FinderException fe) {
			log(fe);
			add(getErrorText(localize("no_season_found", "No season found...")));
			return;
		}
		boolean hasPlacing = getBusiness().hasSchoolPlacing(getSession().getUser(), season);
		
		switch (parseAction(iwc)) {
			case ACTION_VIEW:
				viewApplication(iwc, season, hasPlacing);
				break;
			
			case ACTION_PHASE_1:
				showPhaseOne(iwc, hasPlacing);
				break;
			
			case ACTION_PHASE_2:
				showPhaseTwo(iwc);
				break;
				
			case ACTION_PHASE_3:
				showPhaseThree(iwc, ACTION_PHASE_4, iHomeSchoolChosen ? ACTION_PHASE_1 : ACTION_PHASE_2, ACTION_PHASE_3);
				break;
				
			case ACTION_PHASE_4:
				showPhaseFour(iwc, ACTION_PHASE_5, ACTION_PHASE_3, ACTION_PHASE_4);
				break;
				
			case ACTION_PHASE_5:
				showPhaseFive(iwc);
				break;
				
			case ACTION_OVERVIEW:
				showOverview(iwc);
				break;
				
			case ACTION_SAVE:
				save(iwc);
				break;
		}
	}

	protected void showPhaseOne(IWContext iwc, boolean hasPlacing) throws RemoteException {
		if (hasPlacing && !iSchoolChange) {
			add(getErrorText(localize("has_granted_choice", "Child already has a granted choice")));
			return;
		}

		if (getBusiness().hasHomeSchool(getSession().getUser())) {
			saveCustodianInfo(iwc, false);
			
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
			
			SchoolChoice choice = null;
			try {
				choice = getBusiness().getChoice(getSession().getUser(), season, 1);
			}
			catch (FinderException fe) {
				fe.printStackTrace();
			}

			Table table = new Table();
			table.setCellpadding(0);
			table.setCellspacing(0);
			table.setWidth(Table.HUNDRED_PERCENT);
			form.add(table);
			int row = 1;
			
			table.add(getPersonInfoTable(iwc, getSession().getUser()), 1, row++);
			table.setHeight(row++, 6);
			
			table.add(getText(localize("application.home_school_information", "Home school information")), 1, row++);
			table.setHeight(row++, 18);
			
			School school = getBusiness().getHomeSchoolForUser(getSession().getUser());
			List years = new ArrayList(getBusiness().getSchoolYearsInSchool(school));
			Collections.sort(years, new SchoolYearComparator());

			Table applicationTable = new Table(2, 3);
			applicationTable.add(getSmallHeader(localize("application.home_school", "Home school") + ":"), 1, 1);
			applicationTable.add(getSmallText(school.getSchoolName()), 2, 1);
			
			SelectorUtility util = new SelectorUtility();
			DropdownMenu yearDropdown = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(SchoolAreaCollectionHandler.PARAMETER_SCHOOL_YEAR), years, "getSchoolYearName"));
			if (choice != null) {
				yearDropdown.setSelectedElement(choice.getSchoolYearID());
			}
			else {
				try {
					SchoolYear year = getBusiness().getSchoolYearForUser(getSession().getUser());
					yearDropdown.setSelectedElement(year.getPrimaryKey().toString());
				}
				catch (FinderException fe) {
					//No school found for age...
				}
			}
			yearDropdown.keepStatusOnAction(true);
			applicationTable.add(getSmallHeader(localize("application.year", "Year") + ":"), 1, 2);
			applicationTable.add(yearDropdown, 2, 2);
			
			applicationTable.add(getSmallHeader(localize("application.native_language", "Language") + ":"), 1, 3);
			applicationTable.add(getNativeLanguagesDropdown(), 2, 3);
			
			table.add(applicationTable, 1, row++);
			table.setHeight(row++, 12);
	
			table.add(getText(localize("application.choose_home_school_information", "Choose home school information")), 1, row++);
			table.setHeight(row++, 12);
			
			table.add(getSmallHeader(localize("application.choose_home_school", "Choose home school") + ":"), 1, row++);
			table.setHeight(row++, 3);

			RadioButton yes = getRadioButton(PARAMETER_HOME_SCHOOL, Boolean.TRUE.toString());
			yes.keepStatusOnAction(true);
			RadioButton no = getRadioButton(PARAMETER_HOME_SCHOOL, Boolean.FALSE.toString());
			no.keepStatusOnAction(true);
			
			table.add(yes, 1, row);
			table.add(Text.getNonBrakingSpace(), 1, row);
			table.add(getSmallText(localize("yes", "Yes")), 1, row++);
			table.setHeight(row++, 3);
			table.add(no, 1, row);
			table.add(Text.getNonBrakingSpace(), 1, row);
			table.add(getSmallText(localize("no", "No")), 1, row++);
			table.setHeight(row++, 18);
			
			SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("next", "Next"), PARAMETER_ACTION, String.valueOf(ACTION_PHASE_2)));
			
			table.add(next, 1, row);
			table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
			table.add(getHelpButton("help_school_application_phase_1"), 1, row);
			table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
			table.setCellpaddingRight(1, row, 12);
	
			add(form);
		}
		else {
			showPhaseTwo(iwc);
		}
	}
	
	protected void showPhaseTwo(IWContext iwc) throws RemoteException {
		saveCustodianInfo(iwc, false);

		Form form = createForm();
		SchoolSeason season = null;
		if (!iwc.isParameterSet(PARAMETER_SEASON)) {
			try {
				if (iSchoolChange) {
					season = getSchoolBusiness().getCurrentSchoolSeason(getSchoolBusiness().getCategoryElementarySchool());
				}
				else {
					season = getCareBusiness().getCurrentSeason();
				}
			}
			catch (FinderException fe) {
				log(fe);
				add(getErrorText(localize("no_season_found", "No season found...")));
				return;
			}
			form.addParameter(PARAMETER_SEASON, season.getPrimaryKey().toString());
		}
		else {
			season = getSchoolBusiness().getSchoolSeason(new Integer(iwc.getParameter(PARAMETER_SEASON)));
		}
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;
		
		table.add(getPersonInfoTable(iwc, getSession().getUser()), 1, row++);
		table.setHeight(row++, 6);
		
		table.add(getHeader(localize("application.other_school_information", "Other school information")), 1, row++);
		table.setHeight(row++, 6);
		
		Table applicationTable = new Table(3, 4);
		applicationTable.setCellpadding(3);
		applicationTable.setCellspacing(0);
		table.add(applicationTable, 1, row++);
		table.setHeight(row++, 12);
		
		Collection areas = getSchoolBusiness().findAllSchoolAreas();
		
		List years = new ArrayList(getBusiness().getSchoolYears());
		Collections.sort(years, new SchoolYearComparator());
		SelectorUtility util = new SelectorUtility();
		DropdownMenu yearDropdown = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(SchoolAreaCollectionHandler.PARAMETER_SCHOOL_YEAR), years, "getSchoolYearName"));
		yearDropdown.addMenuElementFirst("-1", localize("application.select_year", "Select year"));
		if (iwc.isParameterSet(SchoolAreaCollectionHandler.PARAMETER_SCHOOL_YEAR)) {
			yearDropdown.setSelectedElement(iwc.getParameter(SchoolAreaCollectionHandler.PARAMETER_SCHOOL_YEAR));
		}
		else {
			try {
				SchoolYear year = getBusiness().getSchoolYearForUser(getSession().getUser());
				yearDropdown.setSelectedElement(year.getPrimaryKey().toString());
			}
			catch (FinderException fe) {
				//No school found for age...
			}
		}
		
		applicationTable.add(getSmallHeader(localize("application.school_year", "School year")), 1, 1);
		applicationTable.add(yearDropdown, 2, 1);
		
		for (int a = 1; a <= (iSchoolChange ? 1 : 3); a++) {
			SchoolChoice choice = null;
			try {
				choice = getBusiness().getChoice(getSession().getUser(), season, a);
			}
			catch (FinderException fe) {
				fe.printStackTrace();
			}
			
			DropdownMenu areaDropdown = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_AREA + "_" + a), areas, "getSchoolAreaName"));
			areaDropdown.addMenuElementFirst("", localize("appilcation.select_area", "Select area"));
			DropdownMenu schoolDropdown = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_SCHOOLS + "_" + a));
			if (iwc.isParameterSet(SchoolAreaCollectionHandler.PARAMETER_SCHOOL_YEAR) && iwc.isParameterSet(PARAMETER_AREA + "_" + a) && iwc.isParameterSet(PARAMETER_SCHOOLS + "_" + a)) {
				try {
					Collection schools = getSchoolBusiness().getSchoolHome().findAllByAreaAndTypesAndYear(Integer.parseInt(iwc.getParameter(PARAMETER_AREA + "_" + a)), getSchoolBusiness().getSchoolTypesForCategory(getSchoolBusiness().getCategoryElementarySchool(), false), Integer.parseInt(iwc.getParameter(SchoolAreaCollectionHandler.PARAMETER_SCHOOL_YEAR)));
			    Iterator iter = schools.iterator();
			    while (iter.hasNext()) {
			    		School school = (School) iter.next();
			    		schoolDropdown.addMenuElement(school.getPrimaryKey().toString(), school.getSchoolName());
			    }
					schoolDropdown.addMenuElementFirst("-1", localize("select_school","Select school"));
				}
				catch (FinderException fe) {
					fe.printStackTrace();
				}
				areaDropdown.setSelectedElement(iwc.getParameter(PARAMETER_AREA + "_" + a));
				schoolDropdown.setSelectedElement(iwc.getParameter(PARAMETER_SCHOOLS + "_" + a));
			}
			else if (choice != null) {
				School school = choice.getChosenSchool();
				SchoolArea area = school.getSchoolArea();
				if (area != null) {
					try {
						Collection schools = getSchoolBusiness().getSchoolHome().findAllByAreaAndTypesAndYear(((Integer) area.getPrimaryKey()).intValue(), getSchoolBusiness().getSchoolTypesForCategory(getSchoolBusiness().getCategoryElementarySchool(), false), choice.getSchoolYearID());
				    Iterator iter = schools.iterator();
				    while (iter.hasNext()) {
				    		School element = (School) iter.next();
				    		schoolDropdown.addMenuElement(element.getPrimaryKey().toString(), element.getSchoolName());
				    }
						schoolDropdown.addMenuElementFirst("-1", localize("select_school","Select school"));
					}
					catch (FinderException fe) {
						fe.printStackTrace();
					}
					areaDropdown.setSelectedElement(area.getPrimaryKey().toString());
					schoolDropdown.setSelectedElement(school.getPrimaryKey().toString());
					yearDropdown.setSelectedElement(choice.getSchoolYearID());
				}
			}

			applicationTable.add(getSmallHeader(localize("application.school_" + a, "School choice nr. " + a)), 1, a + 1);
			applicationTable.add(areaDropdown, 2, a + 1);
			applicationTable.add(schoolDropdown, 3, a + 1);

			try {
				RemoteScriptHandler rsh = new RemoteScriptHandler(areaDropdown, schoolDropdown);
				rsh.setRemoteScriptCollectionClass(SchoolAreaCollectionHandler.class);
				add(rsh);
			}
			catch (IllegalAccessException iae) {
				iae.printStackTrace();
			}
			catch (InstantiationException ie) {
				ie.printStackTrace();
			}
		}
		
		TextArea message = (TextArea) getStyledInterface(new TextArea(PARAMETER_MESSAGE));
		message.setWidth(Table.HUNDRED_PERCENT);
		message.setRows(6);
		
		table.add(getText(localize("application.other_school_message", "Message")), 1, row++);
		table.add(message, 1, row++);
		table.setHeight(row++, 18);
		
		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(localize("previous", "Previous"), PARAMETER_ACTION, String.valueOf(ACTION_PHASE_1)));
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("next", "Next"), PARAMETER_ACTION, String.valueOf(ACTION_PHASE_3)));
		
		table.add(previous, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(next, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(getHelpButton("help_school_application_phase_2"), 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setCellpaddingRight(1, row, 12);

		add(form);
	}

	protected void showPhaseThree(IWContext iwc, int nextPhase, int previousPhase, int currentPhase) throws RemoteException {
		saveCustodianInfo(iwc, true);
		
		Form form = createForm();
		form.addParameter(PARAMETER_ACTION, String.valueOf(currentPhase));
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;
		
		table.add(getPersonInfoTable(iwc, getSession().getUser()), 1, row++);
		table.setHeight(row++, 6);
		
		table.add(getHeader(localize("application.custodian_information", "Custodian information")), 1, row++);
		table.setHeight(row++, 18);
		
		Table applicationTable = new Table();
		applicationTable.setCellpadding(0);
		applicationTable.setCellspacing(0);
		applicationTable.setWidth(Table.HUNDRED_PERCENT);
		table.add(applicationTable, 1, row++);
		int aRow = 1;

		Collection custodians = getUserBusiness(iwc).getParentsForChild(getSession().getUser());
		Iterator iter = custodians.iterator();
		while (iter.hasNext()) {
			User custodian = (User) iter.next();
			aRow = addParentToTable(iwc, applicationTable, custodian, aRow, false, 0, false);
			
			if (iter.hasNext()) {
				applicationTable.setHeight(aRow++, 6);
				applicationTable.mergeCells(1, aRow, applicationTable.getColumns(), aRow);
				applicationTable.add(new HorizontalRule(), 1, aRow++);
				applicationTable.setHeight(aRow++, 6);
			}
		}

		applicationTable.setHeight(aRow++, 6);
		applicationTable.mergeCells(1, aRow, applicationTable.getColumns(), aRow);
		applicationTable.add(new HorizontalRule(), 1, aRow++);
		applicationTable.setHeight(aRow++, 6);
		
		User custodian = getCareBusiness().getExtraCustodian(getSession().getUser());
		if (iwc.isParameterSet(PARAMETER_PERSONAL_ID)) {
			saveCustodianInfo(iwc, false);
			
			String personalID = iwc.getParameter(PARAMETER_PERSONAL_ID);
			try {
				custodian = getUserBusiness().getUser(personalID);
			}
			catch (FinderException fe) {
				getParentPage().setAlertOnLoad(localize("no_user_found_with_personal_id", "No user found with personal ID") + ": " + personalID);
			}
		}
		
		addParentToTable(iwc, applicationTable, custodian, aRow, false, 0, true);
		table.setHeight(row++, 18);
		
		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(localize("previous", "Previous")));
		previous.setValueOnClick(PARAMETER_ACTION, String.valueOf(previousPhase));
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("next", "Next")));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(nextPhase));
		
		table.add(previous, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(next, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(getHelpButton("help_school_application_phase_3"), 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setCellpaddingRight(1, row, 12);

		add(form);
	}
	
	private int addParentToTable(IWContext iwc, Table table, User custodian, int row, boolean isExtraCustodian, int number, boolean editable) throws RemoteException {
		Address address = null;
		Phone phone = null;
		Phone work = null;
		Phone mobile = null;
		Email email = null;

		if (custodian != null) {
			address = getUserBusiness(iwc).getUsersMainAddress(custodian);

			try {
				phone = getUserBusiness(iwc).getUsersHomePhone(custodian);
			}
			catch (NoPhoneFoundException npfe) {
				phone = null;
			}
			
			try {
				work = getUserBusiness(iwc).getUsersWorkPhone(custodian);
			}
			catch (NoPhoneFoundException npfe) {
				work = null;
			}
			
			try {
				mobile = getUserBusiness(iwc).getUsersMobilePhone(custodian);
			}
			catch (NoPhoneFoundException npfe) {
				mobile = null;
			}
			
			try {
				email = getUserBusiness(iwc).getUsersMainEmail(custodian);
			}
			catch (NoEmailFoundException nefe) {
				email = null;
			}
		}

		table.setCellpaddingRight(1, row, 5);
		table.setCellpaddingLeft(2, row, 5);
		table.add(getText(localize("name", "Name")), 1, row);
		table.add(getText(localize("personal_id", "Personal ID")), 2, row++);
		if (custodian != null) {
			table.add(new HiddenInput(!isExtraCustodian ? PARAMETER_USER : PARAMETER_RELATIVE, custodian.getPrimaryKey().toString()), 1, row);
		}
		if (!editable) {
			table.setCellpaddingRight(1, row, 5);
			table.setCellpaddingLeft(2, row, 5);
			table.add(getTextInput("name", custodian.getName(), true), 1, row);
			table.add(getTextInput("personalID", PersonalIDFormatter.format(custodian.getPersonalID(), iwc.getCurrentLocale()), true), 2, row++);
		}
		else {
			TextInput personalID = (TextInput) getStyledInterface(new TextInput(PARAMETER_PERSONAL_ID + (isExtraCustodian ? "_" + number : "")));
			personalID.setLength(10);
			personalID.setAsPersonalID(iwc.getCurrentLocale(), localize("not_valid_personal_id", "Not a valid personal ID"));
			if (custodian != null) {
				personalID.setContent(custodian.getPersonalID());
			}
			
			SubmitButton search = (SubmitButton) getButton(new SubmitButton(localize("search", "Search")));
			
			table.setCellpaddingRight(1, row, 5);
			table.setCellpaddingLeft(2, row, 5);
			table.add(getTextInput("name", custodian != null ? custodian.getName() : null, true), 1, row);
			table.add(personalID, 2, row);
			table.add(Text.getNonBrakingSpace(), 2, row);
			table.add(search, 2, row++);
		}
		table.setHeight(row++, 5);
		
		table.setCellpaddingRight(1, row, 5);
		table.setCellpaddingLeft(2, row, 5);
		table.add(getText(localize("address", "Address")), 1, row);
		table.add(getText(localize("zip_code", "Zip code")), 2, row++);
		TextInput addr = getTextInput("address", null, true);
		TextInput zip = getTextInput("zipCode", null, true);
		if (address != null) {
			addr.setContent(address.getStreetAddress());
			PostalCode code = address.getPostalCode();
			if (code != null) {
				zip.setContent(code.getPostalAddress());
			}
		}
		table.setCellpaddingRight(1, row, 5);
		table.setCellpaddingLeft(2, row, 5);
		table.add(addr, 1, row);
		table.add(zip, 2, row++);
		table.setHeight(row++, 5);

		table.setCellpaddingRight(1, row, 5);
		table.setCellpaddingLeft(2, row, 5);
		table.add(getText(localize("home_phone", "Home phone")), 1, row);
		table.add(getText(localize("work_phone", "Work phone")), 2, row++);
		TextInput homePhone = getTextInput(PARAMETER_HOME_PHONE, null, false);
		TextInput workPhone = getTextInput(PARAMETER_WORK_PHONE, null, false);
		if (phone != null) {
			homePhone.setContent(phone.getNumber());
		}
		if (custodian != null && isExtraCustodian) {
			homePhone.setAsNotEmpty(localize("must_enter_home_phone", "You must enter a home phone for relative."));
		}
		if (work != null) {
			workPhone.setContent(work.getNumber());
		}
		table.setCellpaddingRight(1, row, 5);
		table.setCellpaddingLeft(2, row, 5);
		table.add(homePhone, 1, row);
		table.add(workPhone, 2, row++);
		table.setHeight(row++, 5);

		table.setCellpaddingRight(1, row, 5);
		table.setCellpaddingLeft(2, row, 5);
		table.add(getText(localize("mobile_phone", "Mobile phone")), 1, row);
		table.add(getText(localize("email", "E-mail")), 2, row++);
		TextInput mobilePhone = getTextInput(PARAMETER_MOBILE_PHONE, null, false);
		TextInput mail = getTextInput(PARAMETER_EMAIL, null, false);
		if (mobile != null) {
			mobilePhone.setContent(mobile.getNumber());
		}
		if (email != null) {
			mail.setContent(email.getEmailAddress());
		}
		table.setCellpaddingRight(1, row, 5);
		table.setCellpaddingLeft(2, row, 5);
		table.add(mobilePhone, 1, row);
		table.add(mail, 2, row++);
		table.setHeight(row++, 5);
		
		table.add(getText(localize("relation", "Relation")), 1, row++);
		DropdownMenu relationMenu = getRelationDropdown(custodian);
		if (custodian != null) {
			String relation = getCareBusiness().getUserRelation(getSession().getUser(), custodian);
			if (relation != null) {
				relationMenu.setSelectedElement(relation);
			}
			relationMenu.setAsNotEmpty(localize("must_select_relation", "You must select a relation to the child."), "");
		}
		table.add(relationMenu, 1, row++);
		
		return row;
	}

	private int addRelativeToTable(Table table, Relative relative, int row) {
		table.setCellpaddingRight(1, row, 5);
		table.add(getText(localize("name", "Name")), 1, row++);
		table.mergeCells(1, row, 2, row);
		table.add(getTextInput(PARAMETER_RELATIVE, relative != null ? relative.getName() : null, false), 1, row++);
		table.setHeight(row++, 5);
		
		table.setCellpaddingRight(1, row, 5);
		table.setCellpaddingLeft(2, row, 5);
		table.add(getText(localize("home_phone", "Home phone")), 1, row);
		table.add(getText(localize("work_phone", "Work phone")), 2, row++);
		TextInput homePhone = getTextInput(PARAMETER_HOME_PHONE, null, false);
		TextInput workPhone = getTextInput(PARAMETER_WORK_PHONE, null, false);
		if (relative != null && relative.getHomePhone() != null) {
			homePhone.setContent(relative.getHomePhone());
		}
		if (relative != null && relative.getWorkPhone() != null) {
			workPhone.setContent(relative.getWorkPhone());
		}
		table.setCellpaddingRight(1, row, 5);
		table.setCellpaddingLeft(2, row, 5);
		table.add(homePhone, 1, row);
		table.add(workPhone, 2, row++);
		table.setHeight(row++, 5);

		table.setCellpaddingRight(1, row, 5);
		table.setCellpaddingLeft(2, row, 5);
		table.add(getText(localize("mobile_phone", "Mobile phone")), 1, row);
		table.add(getText(localize("email", "E-mail")), 2, row++);
		TextInput mobilePhone = getTextInput(PARAMETER_MOBILE_PHONE, null, false);
		TextInput mail = getTextInput(PARAMETER_EMAIL, null, false);
		if (relative != null && relative.getMobilePhone() != null) {
			mobilePhone.setContent(relative.getMobilePhone());
		}
		if (relative != null && relative.getEmail() != null) {
			mail.setContent(relative.getEmail());
		}
		table.setCellpaddingRight(1, row, 5);
		table.setCellpaddingLeft(2, row, 5);
		table.add(mobilePhone, 1, row);
		table.add(mail, 2, row++);
		table.setHeight(row++, 5);
		
		table.add(getText(localize("relation", "Relation")), 1, row++);
		DropdownMenu relationMenu = getRelationDropdown(null);
		if (relative != null && relative.getRelation() != null) {
			relationMenu.setSelectedElement(relative.getRelation());
		}
		table.add(relationMenu, 1, row++);
		
		return row;
	}

	protected void showPhaseFour(IWContext iwc, int nextPhase, int previousPhase, int currentPhase) throws RemoteException {
		saveCustodianInfo(iwc, false);
		saveChildInfo(iwc);

		Form form = createForm();
		form.addParameter(PARAMETER_ACTION, String.valueOf(currentPhase));
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;
		
		table.add(getPersonInfoTable(iwc, getSession().getUser()), 1, row++);
		table.setHeight(row++, 6);
		
		table.add(getText(localize("application.relative_information", "Relative information")), 1, row++);
		table.setHeight(row++, 18);
		
		Table applicationTable = new Table();
		applicationTable.setCellpadding(0);
		applicationTable.setCellspacing(0);
		applicationTable.setWidth(Table.HUNDRED_PERCENT);
		table.add(applicationTable, 1, row++);
		int aRow = 1;

		List relatives = getCareBusiness().getRelatives(getSession().getUser());
		for (int a = 1; a <= 2; a++) {
			Relative relative = null;
			if (relatives.size() >= a) {
				relative = (Relative) relatives.get(a - 1);
			}

			aRow = addRelativeToTable(applicationTable, relative, aRow);
			
			if (a == 1) {
				applicationTable.setHeight(aRow++, 6);
				applicationTable.mergeCells(1, aRow, applicationTable.getColumns(), aRow);
				applicationTable.add(new HorizontalRule(), 1, aRow++);
				applicationTable.setHeight(aRow++, 6);
			}
		}

		table.setHeight(row++, 18);
		
		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(localize("previous", "Previous")));
		previous.setValueOnClick(PARAMETER_ACTION, String.valueOf(previousPhase));
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("next", "Next")));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(nextPhase));
		
		table.add(previous, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(next, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(getHelpButton("help_school_application_phase_4"), 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setCellpaddingRight(1, row, 12);

		add(form);
	}

	protected void showPhaseFive(IWContext iwc) throws RemoteException {
		saveCustodianInfo(iwc, true);

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
		
		table.add(getHeader(localize("application.child_information", "Child information")), 1, row++);
		table.setHeight(row++, 6);
		
		Table applicationTable = new Table(4, 9);
		applicationTable.setCellpadding(3);
		applicationTable.setCellspacing(0);
		table.add(applicationTable, 1, row++);
		int aRow = 1;
		
		applicationTable.add(getSmallHeader(localize("yes", "Yes")), 2, aRow);
		applicationTable.add(getSmallHeader(localize("no", "No")), 3, aRow);
		applicationTable.add(getSmallHeader(localize("no_answer", "Won't answer")), 4, aRow++);
		
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

		applicationTable.add(getSmallHeader(localize("child.can_diplay_images", "Can display images")), 1, aRow);
		yes = getRadioButton(PARAMETER_CAN_DISPLAY_IMAGES, Boolean.TRUE.toString());
		no = getRadioButton(PARAMETER_CAN_DISPLAY_IMAGES, Boolean.FALSE.toString());
		boolean canDisplayImages = getBusiness().canDisplaySchoolImages(getSession().getUser());
		if (canDisplayImages) {
			yes.setSelected(true);
		}
		else {
			no.setSelected(true);
		}
		applicationTable.add(yes, 2, aRow);
		applicationTable.add(no, 3, aRow++);
		
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
		previous.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_4));
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("next", "Next")));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_OVERVIEW));
		
		table.add(previous, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(next, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(getHelpButton("help_school_application_phase_5"), 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setCellpaddingRight(1, row, 12);
		
		add(form);
	}
	
	protected void showOverview(IWContext iwc) throws RemoteException {
		saveChildInfo(iwc);

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
		
		String school1 = iwc.getParameter(PARAMETER_SCHOOLS + "_1");
		String school2 = iwc.getParameter(PARAMETER_SCHOOLS + "_2");
		String school3 = iwc.getParameter(PARAMETER_SCHOOLS + "_3");
		
		String yearPK = iwc.getParameter(SchoolAreaCollectionHandler.PARAMETER_SCHOOL_YEAR);
		String message = iwc.getParameter(PARAMETER_MESSAGE);

		if (iHomeSchoolChosen) {
			School school = getBusiness().getHomeSchoolForUser(getSession().getUser());
			if (school != null) {
				verifyTable.add(getSmallHeader(localize("application.home_school", "Home school")), 1, iRow);
				verifyTable.add(getText(school.getSchoolName()), 2, iRow++);
			}
		}
		else {
			if (school1 != null) {
				School school = getSchoolBusiness().getSchool(school1);
				if (school != null) {
					verifyTable.add(getSmallHeader(localize("application.first_school", "First school")), 1, iRow);
					verifyTable.add(getSmallText(school.getSchoolName()), 2, iRow++);
				}
			}
			
			if (school2 != null) {
				School school = getSchoolBusiness().getSchool(school2);
				if (school != null) {
					verifyTable.add(getSmallHeader(localize("application.second_school", "Second school")), 1, iRow);
					verifyTable.add(getSmallText(school.getSchoolName()), 2, iRow++);
				}
			}
			
			if (school3 != null) {
				School school = getSchoolBusiness().getSchool(school3);
				if (school != null) {
					verifyTable.add(getSmallHeader(localize("application.third_school", "Third school")), 1, iRow);
					verifyTable.add(getSmallText(school.getSchoolName()), 2, iRow++);
				}
			}
		}
		
		verifyTable.setHeight(iRow++, 6);
		
		if (yearPK != null) {
			SchoolYear year = getSchoolBusiness().getSchoolYear(yearPK);
			if (year != null) {
				verifyTable.add(getSmallHeader(localize("application.year", "Year")), 1, iRow);
				verifyTable.add(getSmallText(year.getSchoolYearName()), 2, iRow++);
				verifyTable.setHeight(iRow++, 6);
				verifyTable.mergeCells(1, iRow, 2, iRow);
				verifyTable.setBottomCellBorder(1, iRow++, 1, "#D7D7D7", "solid");
				verifyTable.setHeight(iRow++, 6);
			}
		}
		
		if (message != null) {
			verifyTable.add(getSmallHeader(localize("application.message", "Message")), 1, iRow++);
			verifyTable.mergeCells(1, iRow, 2, iRow);
			verifyTable.add(getSmallText(message), 1, iRow++);
			verifyTable.setHeight(iRow++, 6);
			verifyTable.mergeCells(1, iRow, 2, iRow);
			verifyTable.setBottomCellBorder(1, iRow++, 1, "#D7D7D7", "solid");
			verifyTable.setHeight(iRow++, 6);
		}
		
		row = addChildInformation(verifyTable, getSession().getUser(), row);
		
		boolean canDisplaySchoolImages = getBusiness().canDisplaySchoolImages(getSession().getUser());
		verifyTable.mergeCells(1, iRow, table.getColumns(), iRow);
		verifyTable.add(getBooleanTable(getSmallHeader(localize("child.can_diplay_images", "Can display images")), canDisplaySchoolImages), 1, iRow);
		verifyTable.setWidth(1, "50%");
		verifyTable.setWidth(2, "50%");
		
		table.setHeight(row++, 18);
		
		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(localize("previous", "Previous")));
		previous.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_5));
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("send", "Send")));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_SAVE));
		
		table.add(previous, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(next, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(getHelpButton("help_school_application_overview"), 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setCellpaddingRight(1, row, 12);
		
		next.setSubmitConfirm(localize("confirm_application_submit", "Are you sure you want to send the application?"));
		form.setToDisableOnSubmit(next, true);

		add(form);
	}
	
	private void viewApplication(IWContext iwc, SchoolSeason season, boolean hasPlacing) throws RemoteException {
		Form form = new Form();
		form.addParameter(PARAMETER_ACTION, "");

		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;
		
		table.add(getPersonInfoTable(iwc, getSession().getUser()), 1, row++);
		table.setHeight(row++, 6);
		
		table.add(getHeader(localize("application.view_application", "View application")), 1, row++);
		table.setHeight(row++, 6);
		
		Table viewTable = new Table();
		viewTable.setCellpadding(getCellpadding());
		viewTable.setCellspacing(getCellspacing());
		viewTable.setColumns(2);
		table.add(viewTable, 1, row++);
		int iRow = 1;
		
		SchoolYear year = null;
		String message = null;
		Collection choices = getBusiness().getAllChoices(getSession().getUser(), season);
		
		int count = 1;
		Iterator iter = choices.iterator();
		while (iter.hasNext()) {
			SchoolChoice choice = (SchoolChoice) iter.next();
			School school = choice.getChosenSchool();
			
			if (count == 1) {
				viewTable.add(getSmallHeader(localize("application.first_school", "First school")), 1, iRow);
			}
			else if (count == 2) {
				viewTable.add(getSmallHeader(localize("application.second_school", "Second school")), 1, iRow);
			}
			else if (count == 3) {
				viewTable.add(getSmallHeader(localize("application.third_school", "Third school")), 1, iRow);
			}
			viewTable.add(getSmallText(school.getSchoolName()), 2, iRow++);

			if (year == null) {
				year = choice.getSchoolYear();
			}
			if (message == null) {
				message = choice.getMessage();
			}
		}
		
		viewTable.setHeight(iRow++, 6);
		
		if (year != null) {
			viewTable.add(getSmallHeader(localize("application.year", "Year")), 1, iRow);
			viewTable.add(getSmallText(year.getSchoolYearName()), 2, iRow++);
			viewTable.setHeight(iRow++, 6);
			viewTable.mergeCells(1, iRow, 2, iRow);
			viewTable.setBottomCellBorder(1, iRow++, 1, "#D7D7D7", "solid");
			viewTable.setHeight(iRow++, 6);
		}
		
		if (message != null) {
			viewTable.add(getSmallHeader(localize("application.message", "Message")), 1, iRow++);
			viewTable.mergeCells(1, iRow, 2, iRow);
			viewTable.add(getSmallText(message), 1, iRow++);
			viewTable.setHeight(iRow++, 6);
			viewTable.mergeCells(1, iRow, 2, iRow);
			viewTable.setBottomCellBorder(1, iRow++, 1, "#D7D7D7", "solid");
			viewTable.setHeight(iRow++, 6);
		}
		
		iRow = addChildInformation(viewTable, getSession().getUser(), iRow);
		
		boolean canDisplaySchoolImages = getBusiness().canDisplaySchoolImages(getSession().getUser());
		viewTable.mergeCells(1, iRow, table.getColumns(), iRow);
		viewTable.add(getBooleanTable(getSmallHeader(localize("child.can_diplay_images", "Can display images")), canDisplaySchoolImages), 1, iRow);
		viewTable.setWidth(1, "50%");
		viewTable.setWidth(2, "50%");

		table.setHeight(row++, 18);
		
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("edit", "Edit")));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_1));
		
		if (!hasPlacing) {
			table.add(next, 1, row);
			table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		}
		table.add(getHelpButton("help_school_application_view"), 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setCellpaddingRight(1, row, 12);
		
		add(form);
	}
	
	private int addChildInformation(Table table, User child, int iRow) throws RemoteException {
		Boolean hasGrowthDeviation = getCareBusiness().hasGrowthDeviation(child);
		String growthDeviation = getCareBusiness().getGrowthDeviationDetails(child);
		Boolean hasAllergies = getCareBusiness().hasAllergies(child);
		String allergies = getCareBusiness().getAllergiesDetails(child);
		String lastCareProvider = getCareBusiness().getLastCareProvider(child);
		boolean canContactLastProvider = getCareBusiness().canContactLastCareProvider(child);
		String otherInformation = getCareBusiness().getOtherInformation(child);
		
		if (hasGrowthDeviation != null) {
			table.mergeCells(1, iRow, table.getColumns(), iRow);
			table.add(getBooleanTable(getSmallHeader(localize("child.has_growth_deviation", "Has growth deviation")), hasGrowthDeviation.booleanValue()), 1, iRow++);

			if (growthDeviation != null) {
				table.setHeight(iRow++, 6);
				table.mergeCells(1, iRow, table.getColumns(), iRow);
				table.add(getTextAreaTable(getSmallHeader(localize("child.growth_deviation_details", "Growth deviation details")), growthDeviation), 1, iRow++);
			}
			
			table.setHeight(iRow++, 6);
			table.mergeCells(1, iRow, table.getColumns(), iRow);
			table.setBottomCellBorder(1, iRow++, 1, "#D7D7D7", "solid");
			table.setHeight(iRow++, 6);
		}
		
		if (hasAllergies != null) {
			table.mergeCells(1, iRow, table.getColumns(), iRow);
			table.add(getBooleanTable(getSmallHeader(localize("child.has_allergies", "Has allergies")), hasAllergies.booleanValue()), 1, iRow++);
	
			if (allergies != null) {
				table.setHeight(iRow++, 6);
				table.mergeCells(1, iRow, table.getColumns(), iRow);
				table.add(getTextAreaTable(getSmallHeader(localize("child.allergies_details", "Allergies details")), allergies), 1, iRow++);
			}
			
			table.setHeight(iRow++, 6);
			table.mergeCells(1, iRow, table.getColumns(), iRow);
			table.setBottomCellBorder(1, iRow++, 1, "#D7D7D7", "solid");
			table.setHeight(iRow++, 6);
		}
		
		if (lastCareProvider != null) {
			table.mergeCells(1, iRow, table.getColumns(), iRow);
			table.add(getTextInputTable(getSmallHeader(localize("child.last_care_provider", "Last care provider")), lastCareProvider), 1, iRow++);

			table.setHeight(iRow++, 6);
			table.mergeCells(1, iRow, table.getColumns(), iRow);
			table.setBottomCellBorder(1, iRow++, 1, "#D7D7D7", "solid");
			table.setHeight(iRow++, 6);
		}
		
		table.mergeCells(1, iRow, table.getColumns(), iRow);
		table.add(getBooleanTable(getSmallHeader(localize("child.can_contact_last_care_provider", "Can contact last care provider")), canContactLastProvider), 1, iRow++);
		
		table.setHeight(iRow++, 6);
		table.mergeCells(1, iRow, table.getColumns(), iRow);
		table.setBottomCellBorder(1, iRow++, 1, "#D7D7D7", "solid");
		table.setHeight(iRow++, 6);

		if (otherInformation != null) {
			table.mergeCells(1, iRow, table.getColumns(), iRow);
			table.add(getTextAreaTable(getSmallHeader(localize("child.other_information", "Other information")), otherInformation), 1, iRow++);

			table.setHeight(iRow++, 6);
			table.mergeCells(1, iRow, table.getColumns(), iRow);
			table.setBottomCellBorder(1, iRow++, 1, "#D7D7D7", "solid");
			table.setHeight(iRow++, 6);
		}

		return iRow;
	}
	
	private Table getBooleanTable(Text text, boolean checked) {
		Table table = new Table(3, 1);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(2, 6);
		table.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_TOP);
		
		CheckBox box = getCheckBox("void", "");
		box.setChecked(checked);
		box.setDisabled(true);
		table.add(box, 1, 1);
		table.add(text, 3, 1);
		
		return table;
	}

	private Table getTextInputTable(Text text, String value) {
		Table table = new Table(1, 2);
		table.setCellpadding(0);
		table.setCellspacing(0);
		
		table.add(text, 1, 1);

		TextInput input = (TextInput) getStyledInterface(new TextInput("void"));
		input.setContent(value);
		input.setDisabled(true);
		table.add(input, 1, 2);
		
		return table;
	}

	private Table getTextAreaTable(Text text, String value) {
		Table table = new Table(1, 2);
		table.setCellpadding(0);
		table.setCellspacing(0);
		
		table.add(text, 1, 1);

		TextArea input = (TextArea) getStyledInterface(new TextArea("void"));
		input.setContent(value);
		input.setDisabled(true);
		input.setWidth("100%");
		input.setRows(4);
		table.add(input, 1, 2);
		
		return table;
	}

	private void save(IWContext iwc) throws RemoteException {
		boolean saved = false;
		
		Object seasonPK = iwc.getParameter(PARAMETER_SEASON);
		Object yearPK = iwc.getParameter(SchoolAreaCollectionHandler.PARAMETER_SCHOOL_YEAR);
		String language = iwc.getParameter(PARAMETER_LANGUAGE);
		String message = iwc.getParameter(PARAMETER_MESSAGE);
		if (iHomeSchoolChosen) {
			Object schoolPK = getBusiness().getHomeSchoolForUser(getSession().getUser()).getPrimaryKey();
			try {
				getBusiness().saveHomeSchoolChoice(iwc.getCurrentUser(), getSession().getUser(), schoolPK, seasonPK, yearPK, language, message);
				saved = true;
			}
			catch (IDOCreateException ice) {
				ice.printStackTrace();
			}
		}
		else {
			Collection schools = new ArrayList();
			for (int a = 1; a <= 3; a++) {
				String schoolPK = iwc.getParameter(PARAMETER_SCHOOLS + "_" + a);
				if (schoolPK != null && schoolPK.length() > 0) {
					schools.add(schoolPK);
				}
			}
			try {
				getBusiness().saveChoices(iwc.getCurrentUser(), getSession().getUser(), schools, seasonPK, yearPK, language, message);
				saved = true;
			}
			catch (IDOCreateException ice) {
				ice.printStackTrace();
			}
		}
		
		if (saved) {
			Table table = new Table();
			table.setCellpadding(0);
			table.setCellspacing(0);
			table.setWidth(Table.HUNDRED_PERCENT);
			int row = 1;
			
			table.add(getHeader(localize("application.save_completed", "Application sent")), 1, row++);
			table.setHeight(row++, 6);
			
			if (iHomeSchoolChosen) {
				Object[] arguments = { getSession().getUser().getFirstName(), getBusiness().getHomeSchoolForUser(getSession().getUser()).getSchoolName() };
				table.add(getText(MessageFormat.format(localize("application.home_school_confirmation", "{1} has received your application for a school placement and has placed {0}Êin the school."), arguments)), 1, row++);
				
				boolean showAfterSchoolCare = true;
				Age age = new Age(getSession().getUser().getDateOfBirth());
				if (iMaxAge > 0 && iMaxAge < age.getYears()) {
					showAfterSchoolCare = false;
				}
				
				table.setHeight(row++, 12);
				table.add(getHeader(localize("application.after_school_care", "After school care")), 1, row++);
				if (showAfterSchoolCare) {
					table.setHeight(row++, 6);
					table.add(getText(localize("application.after_school_care_information", "Information about after school care")), 1, row++);
				}
				table.setHeight(row++, 18);
				
				GenericButton home = getButton(new GenericButton(localize("my_page", "My page")));
				if (iHomePage != null) {
					home.setPageToOpen(iHomePage);
				}

				table.add(home, 1, row);
				table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
				table.setCellpaddingRight(1, row, 12);

				if (showAfterSchoolCare) {
					GenericButton afterSchoolCare = getButton(new GenericButton(localize("apply_for_after_school_care", "Apply for after school care")));
					if (iAfterSchoolCarePage != null) {
						afterSchoolCare.setPageToOpen(iAfterSchoolCarePage);
					}
					table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
					table.add(afterSchoolCare, 1, row);
				}
			}
			else {
				table.add(getText(localize("application.other_school_confirmation", "Your application for a school placement has been sent and will be processed.")), 1, row++);
				table.setHeight(row++, 18);
				
				GenericButton home = getButton(new GenericButton(localize("my_page", "My page")));
				if (iHomePage != null) {
					home.setPageToOpen(iHomePage);
				}

				table.add(home, 1, row);
				table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
				table.setCellpaddingRight(1, row, 12);
			}
			
			add(table);
		}
		else {
			add(getErrorText(localize("application.save_failed", "Save failed, contact the community office")));
		}
	}

	protected void saveChildInfo(IWContext iwc) throws RemoteException {
		Boolean growthDeviation = iwc.isParameterSet(PARAMETER_GROWTH_DEVIATION) ? new Boolean(iwc.getParameter(PARAMETER_GROWTH_DEVIATION)) : null;
		Boolean allergies = iwc.isParameterSet(PARAMETER_ALLERGIES) ? new Boolean(iwc.getParameter(PARAMETER_ALLERGIES)) : null;
		
		boolean canContactLastProvider = iwc.isParameterSet(PARAMETER_CAN_CONTACT_LAST_PROVIDER);
		boolean canDisplayImages = iwc.isParameterSet(PARAMETER_CAN_DISPLAY_IMAGES);
		
		String growthDeviationDetails = iwc.getParameter(PARAMETER_GROWTH_DEVIATION_DETAILS);
		String allergiesDetails = iwc.getParameter(PARAMETER_ALLERGIES_DETAILS);
		String lastCareProvider = iwc.getParameter(PARAMETER_LAST_CARE_PROVIDER);
		String otherInformation = iwc.getParameter(PARAMETER_OTHER_INFORMATION);
		
		getCareBusiness().storeChildInformation(getSession().getUser(), growthDeviation, growthDeviationDetails, allergies, allergiesDetails, lastCareProvider, canContactLastProvider, otherInformation);
		getBusiness().storeChildSchoolInformation(getSession().getUser(), canDisplayImages);
	}
	
	protected void saveCustodianInfo(IWContext iwc, boolean storeRelatives) throws RemoteException {
		String[] userPKs = storeRelatives ? iwc.getParameterValues(PARAMETER_RELATIVE) : iwc.getParameterValues(PARAMETER_USER);
		String[] homePhones = iwc.getParameterValues(PARAMETER_HOME_PHONE);
		String[] workPhones = iwc.getParameterValues(PARAMETER_WORK_PHONE);
		String[] mobilePhones = iwc.getParameterValues(PARAMETER_MOBILE_PHONE);
		String[] emails = iwc.getParameterValues(PARAMETER_EMAIL);
		String[] relations = iwc.getParameterValues(PARAMETER_RELATION);
		
		if (userPKs != null) {
			for (int a = 0; a < userPKs.length; a++) {
				String userPK = userPKs[a];
				String relation = iwc.getParameter(PARAMETER_RELATION + "_" + userPK);
				
				if (storeRelatives) {
					if (userPK.length() > 0) {
						getCareBusiness().storeRelative(getSession().getUser(), userPK, relations[a], a + 1, homePhones[a], workPhones[a], mobilePhones[a], emails[a]);
					}
				}
				else {
					User custodian = getUserBusiness().getUser(new Integer(userPK));
					if (getUserBusiness().getMemberFamilyLogic().isCustodianOf(custodian, getSession().getUser())) {
						getCareBusiness().updateUserInfo(custodian, homePhones[a], workPhones[a], mobilePhones[a], emails[a]);
						if (relation != null && relation.length() > 0) {
							getCareBusiness().storeUserRelation(getSession().getUser(), custodian, relation);
						}
					}
					else {
						getCareBusiness().storeExtraCustodian(getSession().getUser(), custodian, relation, homePhones[a], workPhones[a], mobilePhones[a], emails[a]);
					}
				}
			}
		}
	}

	private DropdownMenu getNativeLanguagesDropdown() {
		DropdownMenu drop = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_LANGUAGE));
		drop.addMenuElement("-1", localize("application.choose_language", "Choose languge"));
		try {
			ICLanguageHome languageHome = (ICLanguageHome) IDOLookup.getHome(ICLanguage.class);
			Collection langs = languageHome.findAll();
			if (langs != null) {
				for (Iterator iter = langs.iterator(); iter.hasNext();) {
					ICLanguage aLang = (ICLanguage) iter.next();
					drop.addMenuElement(aLang.getName(), aLang.getName());
				}
			}
		}
		catch (RemoteException re) {
			re.printStackTrace();
		}
		catch (FinderException fe) {
			fe.printStackTrace();
		}
		drop.keepStatusOnAction(true);
		
		return drop;
	}
	
	private int parseAction(IWContext iwc) {
		int action = iUseHomeSchool ? ACTION_PHASE_1 : ACTION_PHASE_2;
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		else {
			if (iSchoolChange) {
				action = ACTION_PHASE_2;
			}
			if (iShowOverview) {
				action = ACTION_VIEW;
			}
		}
		
		iHomeSchoolChosen = false;
		if (iwc.isParameterSet(PARAMETER_HOME_SCHOOL)) {
			iHomeSchoolChosen = new Boolean(iwc.getParameter(PARAMETER_HOME_SCHOOL)).booleanValue();
		}
		if (action == ACTION_PHASE_2 && iHomeSchoolChosen) {
			action = ACTION_PHASE_3;
		}
		
		return action;
	}
	
	protected Form createForm() {
		Form form = new Form();
		form.maintainParameter(PARAMETER_SEASON);
		form.maintainParameter(PARAMETER_SCHOOLS + "_1");
		form.maintainParameter(PARAMETER_SCHOOLS + "_2");
		form.maintainParameter(PARAMETER_SCHOOLS + "_3");
		form.maintainParameter(PARAMETER_AREA + "_1");
		form.maintainParameter(PARAMETER_AREA + "_2");
		form.maintainParameter(PARAMETER_AREA + "_3");
		form.maintainParameter(SchoolAreaCollectionHandler.PARAMETER_SCHOOL_YEAR);
		form.maintainParameter(PARAMETER_MESSAGE);
		form.maintainParameter(PARAMETER_HOME_SCHOOL);
		form.maintainParameter(PARAMETER_LANGUAGE);
		
		return form;
	}
	
	private DropdownMenu getRelationDropdown(User relative) {
		DropdownMenu relations = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_RELATION + (relative != null ? "_" + relative.getPrimaryKey().toString() : "")));
		relations.addMenuElement("", localize("select_relation", "Select relation"));
		relations.addMenuElement(CareConstants.RELATION_MOTHER, localize("relation.mother", "Mother"));
		relations.addMenuElement(CareConstants.RELATION_FATHER, localize("relation.father", "Father"));
		relations.addMenuElement(CareConstants.RELATION_STEPMOTHER, localize("relation.stepmother", "Stepmother"));
		relations.addMenuElement(CareConstants.RELATION_STEPFATHER, localize("relation.stepfather", "Stepfather"));
		relations.addMenuElement(CareConstants.RELATION_GRANDMOTHER, localize("relation.grandmother", "Grandmother"));
		relations.addMenuElement(CareConstants.RELATION_GRANDFATHER, localize("relation.grandfather", "Grandfather"));
		relations.addMenuElement(CareConstants.RELATION_SIBLING, localize("relation.sibling", "Sibling"));
		relations.addMenuElement(CareConstants.RELATION_OTHER, localize("relation.other", "Other"));
		
		return relations;
	}
	
	protected ICPage getHomePage() {
		return iHomePage;
	}
	
	public void setHomePage(ICPage homePage) {
		iHomePage = homePage;
	}
	
	public void setAfterSchoolCarePage(ICPage afterSchoolCarePage) {
		iAfterSchoolCarePage = afterSchoolCarePage;
	}
	
	public void setAsSchoolChange(boolean schoolChange) {
		iSchoolChange = schoolChange;
	}
	
	public void setUseHomeSchool(boolean useHomeSchool) {
		iUseHomeSchool = useHomeSchool;
	}
	
	public void setMaxAfterSchoolCareAge(int age) {
		iMaxAge = age;
	}
	
	public void setShowOverview(boolean showOverview) {
		iShowOverview = showOverview;
	}
}