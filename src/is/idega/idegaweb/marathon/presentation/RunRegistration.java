/*
 * Created on Jun 30, 2004
 */
package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SelectOption;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.util.LocaleUtil;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * 
 * @author birna
 */
public class RunRegistration extends Block {

	private static final String PARAMETER_ACTION = "prm_action";

	private static final int ACTION_STEP_ONE = 1;

	private static final int ACTION_STEP_TWO = 2;

	private static final int ACTION_STEP_THREE = 3;

	private static final int ACTION_SAVE = 4;

	private static final String STYLENAME_HEADING = "headingText";

	private static final String STYLENAME_GREEN_TEXT = "greenText";

	private static final String STYLENAME_BLUE_TEXT = "blueText";

	private static final String STYLENAME_INTERFACE = "interface";

	private static final String STYLENAME_CHECKBOX = "checkbox";

	private Text redStar;

	private Text infoRedStarText;

	//texts step one
	private Text distanceText;

	private Text primaryDDLable;

	private Text secondaryDDLable;

	private Text nameText;

	private Text nationalityText;

	private Text ssnText;

	private Text genderText;

	private Text addressText;

	private Text postalText;

	private Text cityText;

	private Text countryText;

	private Text telText;

	private Text mobileText;

	private Text emailText;

	private Text tShirtText;

	//texts step two
	private Text chipText;

	private Text chipNumberText;

	private Link chipLink;

	private Text ownChipText;

	private Text buyChipText;

	private float buyPrice = 2700;

	private Text rentChipText;

	private float rentPrice = 300;

	private Text groupCompetitionText;

	private Text groupNameText;

	private Text bestTimeText;

	private Text goalTimeText;

	//fields step one
	private RunDistanceDropdownDouble runDisDropdownField;

	private TextInput nameField;

	private DropdownMenu nationalityField;

	private TextInput ssnISField;

	private DateInput ssnField;

	private DropdownMenu genderField;

	private TextInput addressField;

	private TextInput postalField;

	private TextInput cityField;

	private DropdownMenu countryField;

	private TextInput telField;

	private TextInput mobileField;

	private TextInput emailField;

	private DropdownMenu tShirtField;

	private SelectOption small;

	private SelectOption medium;

	private SelectOption large;

	private SelectOption xlarge;

	private SelectOption xxlarge;

	private SubmitButton stepOneButton;

	private SubmitButton stepTwoButton;

	//fields step two
	private RadioButton ownChipField;

	private RadioButton buyChipField;

	private RadioButton rentChipField;

	private TextInput chipNumberField;

	private CheckBox groupCompetitionField;

	private TextInput groupNameField;

	private TextInput bestTimeField;

	private TextInput goalTimeField;

	private BackButton backButton;

	private Link backGreen;

	private Link backBlue;

	private Form f;

	private IWResourceBundle iwrb;

	private boolean showPayment = false;

	public RunRegistration() {
		super();
	}

	public Map getStyleNames() {
		Map map = new HashMap();
		map.put(STYLENAME_HEADING, "");
		map.put(STYLENAME_GREEN_TEXT, "");
		map.put(STYLENAME_GREEN_TEXT + ":hover", "");
		map.put(STYLENAME_BLUE_TEXT, "");
		map.put(STYLENAME_BLUE_TEXT + ":hover", "");
		map.put(STYLENAME_INTERFACE, "");
		map.put(STYLENAME_CHECKBOX, "");

		return map;
	}

	private void initializeTexts(IWContext iwc) {
		iwrb = getResourceBundle(iwc);
		redStar = new Text("*");
		redStar.setFontColor("#ff0000");
		infoRedStarText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_INFO_RED_STAR, "These fields must be filled out"));
		//step one texts begin
		distanceText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_PRIMARY_DD, "Run") + "/" + iwrb.getLocalizedString(IWMarathonConstants.RR_SECONDARY_DD, "Distance"));
		primaryDDLable = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_PRIMARY_DD, "Run"));
		secondaryDDLable = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_SECONDARY_DD, "Distance"));
		nameText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_NAME, "Name "));
		nationalityText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_NATIONALITY, "Nationality "));
		ssnText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_SSN, "SSN "));
		genderText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_GENDER, "Gender "));
		addressText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_ADDRESS, "Address"));
		postalText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_POSTAL, "Postal Code"));
		cityText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_CITY, "City"));
		countryText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_COUNTRY, "Country"));
		telText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_TEL, "Telephone"));
		mobileText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_MOBILE, "Mobile Phone"));
		emailText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_EMAIL, "Email "));
		tShirtText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_TSHIRT, "T-Shirt"));
		//step one texts end

		//step two texts begin
		chipText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_CHIP_TIME, "Championchip timing: "));
		chipNumberText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_CHIP_TIME + "_number", "Own chip number"));
		chipLink = new Link(iwrb.getLocalizedString(IWMarathonConstants.RR_CHIP_LINK, "www.championchip.com"), "http://www.championchip.com");
		ownChipText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_OWN_CHIP, "Own Chip"));
		buyChipText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_BUY_CHIP, "Buy Chip"));
		rentChipText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_RENT_CHIP, "Rent Chip"));
		groupCompetitionText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_GROUP_COMP, "Group competition"));
		groupNameText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_GROUP_NAME, "Group Name"));
		bestTimeText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_BEST_TIME, "Your best time running this distance"));
		goalTimeText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_GOAL_TIME, "Your goal in running this distance now"));
		//step two texts end
	}

	private void initializeFields(IWContext iwc) {
		//TODO: remove javascript popups - put red text containing error
		// messages...
		iwrb = getResourceBundle(iwc);

		//step one fields begin
		runDisDropdownField = (RunDistanceDropdownDouble) getStyleObject(new RunDistanceDropdownDouble(), STYLENAME_INTERFACE);
		runDisDropdownField.setPrimaryLabel(primaryDDLable);
		runDisDropdownField.setSecondaryLabel(secondaryDDLable);
		if (iwc.isParameterSet(IWMarathonConstants.GROUP_TYPE_RUN)) {
			runDisDropdownField.setSelectedValues(iwc.getParameter(IWMarathonConstants.GROUP_TYPE_RUN), "");
		}

		nameField = (TextInput) getStyleObject(new TextInput(IWMarathonConstants.PARAMETER_NAME), STYLENAME_INTERFACE);
		nameField.setAsAlphabeticText(iwrb.getLocalizedString("run_reg.name_err_msg", "Your name may only contain alphabetic characters"));
		nameField.setAsNotEmpty(iwrb.getLocalizedString("run_reg.name_not_empty", "Name field cannot be empty"));
		nameField.setInFocusOnPageLoad(true);
		nameField.setWidth(Table.HUNDRED_PERCENT);

		Collection countries = getRunBiz(iwc).getCountries();

		nationalityField = (DropdownMenu) getStyleObject(new DropdownMenu(IWMarathonConstants.PARAMETER_NATIONALITY), STYLENAME_INTERFACE);
		countryField = (DropdownMenu) getStyleObject(new DropdownMenu(IWMarathonConstants.PARAMETER_COUNTRY), STYLENAME_INTERFACE);
		SelectorUtility util = new SelectorUtility();
		if (countries != null && !countries.isEmpty()) {
			nationalityField = (DropdownMenu) util.getSelectorFromIDOEntities(nationalityField, countries, "getName");
			countryField = (DropdownMenu) util.getSelectorFromIDOEntities(countryField, countries, "getName");
		}
		if (iwc.getCurrentLocale().equals(LocaleUtil.getIcelandicLocale())) {
			nationalityField.setSelectedElement("104");
			countryField.setSelectedElement("104");
		}
		nationalityField.setWidth(Table.HUNDRED_PERCENT);
		countryField.setWidth(Table.HUNDRED_PERCENT);

		ssnISField = (TextInput) getStyleObject(new TextInput(IWMarathonConstants.PARAMETER_SSN_IS), STYLENAME_INTERFACE);
		ssnISField.setAsIcelandicSSNumber(iwrb.getLocalizedString("run_reg.ssn_is_err_msg", "Your ssn is not a valid Icelandic ssn"));
		ssnISField.setAsNotEmpty(iwrb.getLocalizedString("run_reg.ssnIS_not_empty", "ssnIS may not be empty"));
		ssnISField.setLength(10);

		ssnField = (DateInput) getStyleObject(new DateInput(IWMarathonConstants.PARAMETER_SSN), STYLENAME_INTERFACE);
		//TODO: set the ssnField as either dateInput or set a error check on
		// the TextInput...
		ssnField.setAsNotEmpty("Date of birth can not be empty");

		genderField = (DropdownMenu) getStyleObject(new DropdownMenu(IWMarathonConstants.PARAMETER_GENDER), STYLENAME_INTERFACE);
		genderField.addMenuElement(IWMarathonConstants.PARAMETER_MALE, iwrb.getLocalizedString(IWMarathonConstants.RR_MALE, "Male"));
		genderField.addMenuElement(IWMarathonConstants.PARAMETER_FEMALE, iwrb.getLocalizedString(IWMarathonConstants.RR_FEMALE, "Female"));

		addressField = (TextInput) getStyleObject(new TextInput(IWMarathonConstants.PARAMETER_ADDRESS), STYLENAME_INTERFACE);
		addressField.setWidth(Table.HUNDRED_PERCENT);

		postalField = (TextInput) getStyleObject(new TextInput(IWMarathonConstants.PARAMETER_POSTAL), STYLENAME_INTERFACE);
		postalField.setMaxlength(7);
		nameField.setLength(7);

		cityField = (TextInput) getStyleObject(new TextInput(IWMarathonConstants.PARAMETER_CITY), STYLENAME_INTERFACE);
		cityField.setWidth(Table.HUNDRED_PERCENT);

		telField = (TextInput) getStyleObject(new TextInput(IWMarathonConstants.PARAMETER_TEL), STYLENAME_INTERFACE);
		telField.setAsIntegers(iwrb.getLocalizedString("run_reg.tel_err_msg", "Phonenumber must be integers"));
		telField.setWidth(Table.HUNDRED_PERCENT);

		mobileField = (TextInput) getStyleObject(new TextInput(IWMarathonConstants.PARAMETER_MOBILE), STYLENAME_INTERFACE);
		mobileField.setAsIntegers(iwrb.getLocalizedString("run_reg.mob_err_msg", "Mobilephonenumber must be integers"));
		mobileField.setWidth(Table.HUNDRED_PERCENT);

		emailField = (TextInput) getStyleObject(new TextInput(IWMarathonConstants.PARAMETER_EMAIL), STYLENAME_INTERFACE);
		emailField.setAsEmail(iwrb.getLocalizedString("run_reg.email_err_msg", "Not a valid email address"));
		emailField.setWidth(Table.HUNDRED_PERCENT);

		tShirtField = (DropdownMenu) getStyleObject(new DropdownMenu(IWMarathonConstants.PARAMETER_TSHIRT), STYLENAME_INTERFACE);
		small = new SelectOption(iwrb.getLocalizedString("run_reg.small", "Small"), IWMarathonConstants.PARAMETER_TSHIRT_S);
		medium = new SelectOption(iwrb.getLocalizedString("run_reg.medium", "Medium"), IWMarathonConstants.PARAMETER_TSHIRT_M);
		large = new SelectOption(iwrb.getLocalizedString("run_reg.large", "Large"), IWMarathonConstants.PARAMETER_TSHIRT_L);
		xlarge = new SelectOption(iwrb.getLocalizedString("run_reg.xlarge", "Larger"), IWMarathonConstants.PARAMETER_TSHIRT_XL);
		xxlarge = new SelectOption(iwrb.getLocalizedString("run_reg.xxlarge", "Largest"), IWMarathonConstants.PARAMETER_TSHIRT_XXL);

		tShirtField.addOption(small);
		tShirtField.addOption(medium);
		tShirtField.addOption(large);
		tShirtField.addOption(xlarge);
		tShirtField.addOption(xxlarge);

		//step one fields end

		//step two fields begin
		ownChipField = (RadioButton) getStyleObject(new RadioButton(IWMarathonConstants.PARAMETER_CHIP, IWMarathonConstants.PARAMETER_OWN_CHIP), STYLENAME_CHECKBOX);

		buyChipField = (RadioButton) getStyleObject(new RadioButton(IWMarathonConstants.PARAMETER_CHIP, IWMarathonConstants.PARAMETER_BUY_CHIP), STYLENAME_CHECKBOX);

		rentChipField = (RadioButton) getStyleObject(new RadioButton(IWMarathonConstants.PARAMETER_CHIP, IWMarathonConstants.PARAMETER_RENT_CHIP), STYLENAME_CHECKBOX);

		chipNumberField = (TextInput) getStyleObject(new TextInput(IWMarathonConstants.PARAMETER_CHIP_NUMBER), STYLENAME_INTERFACE);

		groupCompetitionField = (CheckBox) getStyleObject(new CheckBox(IWMarathonConstants.PARAMETER_GROUP_COMP), STYLENAME_CHECKBOX);

		groupNameField = (TextInput) getStyleObject(new TextInput(IWMarathonConstants.PARAMETER_GROUP_NAME), STYLENAME_INTERFACE);
		groupNameField.setWidth("50%");

		bestTimeField = (TextInput) getStyleObject(new TextInput(IWMarathonConstants.PARAMETER_BEST_TIME), STYLENAME_INTERFACE);
		bestTimeField.setWidth("50%");

		goalTimeField = (TextInput) getStyleObject(new TextInput(IWMarathonConstants.PARAMETER_GOAL_TIME), STYLENAME_INTERFACE);
		goalTimeField.setWidth("50%");

		//step two fields end

		backGreen = getStyleLink(new Link(iwrb.getLocalizedString("run_reg.back", "Back")), STYLENAME_BLUE_TEXT);
		backGreen.setAsBackLink();
		backBlue = getStyleLink(new Link("&gt;&gt;"), STYLENAME_GREEN_TEXT);
		backBlue.setAsBackLink();
	}

	private void stepOne(IWContext iwc) {
		Table t = new Table();
		t.setColumns(2);
		t.setCellpadding(0);
		t.setCellspacing(0);
		t.setWidth(Table.HUNDRED_PERCENT);
		f.add(t);
		int row = 1;
		int column = 1;
		int formRow = -1;

		t.add(iwrb.getLocalizedString("step", "Step") + " 1 " + iwrb.getLocalizedString("of", "of") + " 2", column, row++);
		t.setHeight(row++, 12);

		t.mergeCells(column, row, t.getColumns(), row);
		t.add(redStar, column, row);
		t.add(Text.getNonBrakingSpace(), column, row);
		t.add(infoRedStarText, column, row++);

		t.setHeight(row++, 8);

		t.add(distanceText, column, row);
		t.add(redStar, column, row++);
		t.setHeight(row++, 3);
		t.mergeCells(column, row, t.getColumns(), row);
		t.add(runDisDropdownField, column, row++);

		t.setHeight(row++, 12);

		formRow = row;
		t.add(nameText, column, row);
		t.add(redStar, column, row++);
		t.setHeight(row++, 3);
		t.add(nameField, column, row++);

		t.setHeight(row++, 8);

		t.add(ssnText, column, row);
		t.add(redStar, column, row++);
		t.setHeight(row++, 3);
		if (iwc.getCurrentLocale().equals(LocaleUtil.getIcelandicLocale())) {
			t.add(ssnISField, column, row++);
		}
		else {
			t.add(ssnField, column, row++);
		}

		t.setHeight(row++, 8);

		t.add(addressText, column, row++);
		t.setHeight(row++, 3);
		t.add(addressField, column, row++);

		t.setHeight(row++, 8);

		t.add(cityText, column, row++);
		t.setHeight(row++, 3);
		t.add(cityField, column, row++);

		t.setHeight(row++, 8);

		t.add(postalText, column, row++);
		t.setHeight(row++, 3);
		t.add(postalField, column, row++);

		t.setHeight(row++, 8);

		t.add(countryText, column, row++);
		t.setHeight(row++, 3);
		t.add(countryField, column, row++);

		row = formRow;
		column = 2;

		t.add(genderText, column, row);
		t.add(redStar, column, row++);
		t.setHeight(row++, 3);
		t.add(genderField, column, row++);

		t.setHeight(row++, 8);

		t.add(nationalityText, column, row);
		t.add(redStar, column, row++);
		t.setHeight(row++, 3);
		t.add(nationalityField, column, row++);

		t.setHeight(row++, 8);

		t.add(emailText, column, row++);
		t.setHeight(row++, 3);
		t.add(emailField, column, row++);

		t.setHeight(row++, 8);

		t.add(telText, column, row++);
		t.setHeight(row++, 3);
		t.add(telField, column, row++);

		t.setHeight(row++, 8);

		t.add(mobileText, column, row++);
		t.setHeight(row++, 3);
		t.add(mobileField, column, row++);

		t.setHeight(row++, 8);

		t.add(tShirtText, column, row++);
		t.setHeight(row++, 3);
		t.add(tShirtField, column, row++);

		t.setHeight(row++, 18);

		Link stepTwoGreen = getStyleLink(new Link(iwrb.getLocalizedString("run_reg.submit_step_one", "Next step")), STYLENAME_BLUE_TEXT);
		stepTwoGreen.setToFormSubmit(f);
		Link stepTwoBlue = getStyleLink(new Link("&gt;&gt;"), STYLENAME_GREEN_TEXT);
		stepTwoGreen.setToFormSubmit(f);

		t.add(stepTwoGreen, 1, row);
		t.add(Text.getNonBrakingSpace(), 1, row);
		t.add(stepTwoBlue, 1, row);

		t.setWidth(1, "50%");
		t.setWidth(2, "50%");
		t.setColumnPaddingRight(1, 8);
		t.setColumnPaddingLeft(2, 8);

		f.addParameter(PARAMETER_ACTION, ACTION_STEP_TWO);
		f.keepStatusOnAction();
	}

	private void stepTwo(IWContext iwc) {
		Table t = new Table();
		t.setCellpadding(0);
		t.setCellspacing(0);
		int column = 1;
		int row = 1;
		int formRow = -1;

		t.add(iwrb.getLocalizedString("step", "Step") + " 2 " + iwrb.getLocalizedString("of", "of") + " 2", column, row++);
		t.setHeight(row++, 12);

		formRow = row;
		t.add(chipText, column, row++);
		t.setHeight(row++, 3);
		t.add(ownChipField, column, row);
		t.add(ownChipText, column, row);
		t.add(chipNumberField, column, row++);
		t.setHeight(row++, 3);
		t.add(buyChipField, column, row);
		t.add(buyChipText, column, row++);
		t.setHeight(row++, 3);
		t.add(rentChipField, column, row);
		t.add(rentChipText, column, row++);

		t.setHeight(row++, 8);

		t.add(groupCompetitionField, column, row);
		t.add(Text.getNonBrakingSpace(), column, row);
		t.add(groupCompetitionText, column, row++);

		t.setHeight(row++, 8);

		t.add(groupNameText, column, row++);
		t.setHeight(row++, 3);
		t.add(groupNameField, column, row++);

		t.setHeight(row++, 8);

		t.add(bestTimeText, column, row++);
		t.setHeight(row++, 3);
		t.add(bestTimeField, column, row++);

		t.setHeight(row++, 8);

		t.add(goalTimeText, column, row++);
		t.setHeight(row++, 3);
		t.add(goalTimeField, column, row++);

		t.setHeight(row++, 18);

		Table buttonTable = new Table(3, 1);
		buttonTable.setCellpadding(0);
		buttonTable.setCellspacing(0);
		buttonTable.setWidth(2, 1, 12);
		Link finishGreen = getStyleLink(new Link(iwrb.getLocalizedString("run_reg.submit_step_two", "Next step")), STYLENAME_BLUE_TEXT);
		finishGreen.setToFormSubmit(f);
		Link finishBlue = getStyleLink(new Link("&gt;&gt;"), STYLENAME_GREEN_TEXT);
		finishBlue.setToFormSubmit(f);

		buttonTable.add(backGreen, 1, 1);
		buttonTable.add(Text.getNonBrakingSpace(), 1, 1);
		buttonTable.add(backBlue, 1, 1);
		buttonTable.add(finishGreen, 3, 1);
		buttonTable.add(Text.getNonBrakingSpace(), 3, 1);
		buttonTable.add(finishBlue, 3, 1);

		t.add(buttonTable, 1, row);

		f.addParameter(PARAMETER_ACTION, ACTION_SAVE);
		f.keepStatusOnAction();
		f.maintainAllParameters();
		f.add(t);
	}

	private void commitRegistration(IWContext iwc) {
		RunBusiness runBiz = getRunBiz(iwc);

		//user info
		String name = iwc.getParameter(IWMarathonConstants.PARAMETER_NAME);
		String nationality = iwc.getParameter(IWMarathonConstants.PARAMETER_NATIONALITY);
		String ssnIS = iwc.getParameter(IWMarathonConstants.PARAMETER_SSN_IS);
		String ssn = iwc.getParameter(IWMarathonConstants.PARAMETER_SSN);
		String gender = iwc.getParameter(IWMarathonConstants.PARAMETER_GENDER);
		String address = iwc.getParameter(IWMarathonConstants.PARAMETER_ADDRESS);
		String postal = iwc.getParameter(IWMarathonConstants.PARAMETER_POSTAL);
		String city = iwc.getParameter(IWMarathonConstants.PARAMETER_CITY);
		String country = iwc.getParameter(IWMarathonConstants.PARAMETER_COUNTRY);
		String tel = iwc.getParameter(IWMarathonConstants.PARAMETER_TEL);
		String mobile = iwc.getParameter(IWMarathonConstants.PARAMETER_MOBILE);
		String email = iwc.getParameter(IWMarathonConstants.PARAMETER_EMAIL);

		//run info
		String run = iwc.getParameter(IWMarathonConstants.GROUP_TYPE_RUN);
		String distance = iwc.getParameter(IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE);
		String year = "2004";//iwc.getParameter(IWMarathonConstants.GROUP_TYPE_RUN_YEAR);

		String tshirt = iwc.getParameter(IWMarathonConstants.PARAMETER_TSHIRT);
		String chip = iwc.getParameter(IWMarathonConstants.PARAMETER_CHIP);
		String chipNumber = iwc.getParameter(IWMarathonConstants.PARAMETER_CHIP_NUMBER);
		String groupComp = iwc.getParameter(IWMarathonConstants.PARAMETER_GROUP_COMP);
		String groupName = iwc.getParameter(IWMarathonConstants.PARAMETER_GROUP_NAME);
		String bestTime = iwc.getParameter(IWMarathonConstants.PARAMETER_BEST_TIME);
		String goalTime = iwc.getParameter(IWMarathonConstants.PARAMETER_GOAL_TIME);

		int userID = -1;

		if (ssnIS != null && !ssnIS.equals("")) {
			userID = runBiz.saveUser(name, ssnIS, gender, address, postal, city, country, tel, mobile, email);
		}
		else if (ssn != null && !ssn.equals("")) {
			userID = runBiz.saveUser(name, ssn, gender, address, postal, city, country, tel, mobile, email);
		}

		if (userID > 0) {
			runBiz.saveRun(userID, run, distance, year, nationality, tshirt, chipNumber, groupName, bestTime, goalTime);
		}

		Table t = new Table();
		t.setCellpadding(0);
		t.setCellspacing(0);
		t.setWidth(Table.HUNDRED_PERCENT);
		int row = 1;

		t.setHeight(row++, 12);
		t.add(iwrb.getLocalizedString("registration_received", "Your registration has been received."), 1, row++);
		t.setHeight(row++, 8);

		Table buttonTable = new Table(3, 1);
		buttonTable.setCellpadding(0);
		buttonTable.setCellspacing(0);
		buttonTable.setWidth(2, 1, 12);

		Link backBlue = getStyleLink(new Link(iwrb.getLocalizedString("run_reg.back", "Back")), STYLENAME_BLUE_TEXT);
		Link backGreen = getStyleLink(new Link("&gt;&gt;"), STYLENAME_GREEN_TEXT);

		Link payBlue = getStyleLink(new Link(iwrb.getLocalizedString("run_reg.pay", "Pay fee")), STYLENAME_BLUE_TEXT);
		Link payGreen = getStyleLink(new Link("&gt;&gt;"), STYLENAME_GREEN_TEXT);

		buttonTable.add(backBlue, 1, 1);
		buttonTable.add(Text.getNonBrakingSpace(), 1, 1);
		buttonTable.add(backGreen, 1, 1);
		if (showPayment) {
			buttonTable.add(payBlue, 3, 1);
			buttonTable.add(Text.getNonBrakingSpace(), 3, 1);
			buttonTable.add(payGreen, 3, 1);
		}
		t.add(buttonTable, 1, row);
		add(t);
	}

	public void main(IWContext iwc) {
		f = new Form();
		initializeTexts(iwc);
		initializeFields(iwc);

		switch (parseAction(iwc)) {
			case ACTION_STEP_ONE:
				stepOne(iwc);
				break;
			case ACTION_STEP_TWO:
				stepTwo(iwc);
				break;
			case ACTION_SAVE:
				commitRegistration(iwc);
				break;
		}
		add(f);
	}

	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		else {
			return ACTION_STEP_ONE;
		}
	}

	public void setRentPrice(float price) {
		rentPrice = price;
	}

	public void setBuyPrice(float price) {
		buyPrice = price;
	}

	public float getRentPrice() {
		return rentPrice;
	}

	public float getBuyPrice() {
		return buyPrice;
	}

	public String getBundleIdentifier() {
		return IWMarathonConstants.IW_BUNDLE_IDENTIFIER;
	}

	private RunBusiness getRunBiz(IWContext iwc) {
		RunBusiness business = null;
		try {
			business = (RunBusiness) IBOLookup.getServiceInstance(iwc, RunBusiness.class);
		}
		catch (IBOLookupException e) {
			business = null;
		}
		return business;
	}

	/**
	 * @param showPayment
	 *            The showPayment to set.
	 */
	public void setShowPayment(boolean showPayment) {
		this.showPayment = showPayment;
	}
}