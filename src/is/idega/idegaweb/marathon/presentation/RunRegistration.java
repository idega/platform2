/*
 * Created on Jun 30, 2004
 */
package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import java.text.DecimalFormat;
import java.util.Collection;

import com.idega.block.trade.business.CurrencyBusiness;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SelectOption;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.util.SelectorUtility;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author birna
 */
public class RunRegistration extends Page{
  
  private static final String PARAMETER_ACTION = "prm_action";
  
  private static final int ACTION_STEP_ONE = 1;
  private static final int ACTION_STEP_TWO = 2;
  private static final int ACTION_STEP_THREE = 3;
  private static final int ACTION_SAVE = 4;
  
  private Text redStar;
  private Text infoRedStarText;
  //texts step one
  private Text primaryDDLable;
  private Text secondaryDDLable;
  private Text nameText;
  private Text nationalityText;
  private Text ssnText;
  private Text genderText;
  private Text femaleText;
  private Text maleText;
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
  private TextInput ssnField;
  private RadioButton femaleField;
  private RadioButton maleField;
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
  
  private Form f;
  
  public RunRegistration() {
    super();
  }
  
  private void initializeTexts(IWContext iwc) {
    IWResourceBundle iwrb = getResourceBundle(iwc);
    redStar = new Text("*");
    redStar.setFontColor("#ff0000");
    infoRedStarText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_INFO_RED_STAR,"These fields must be filled out"));
    //step one texts begin
    primaryDDLable = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_PRIMARY_DD,"Run"));
    secondaryDDLable = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_SECONDARY_DD,"Distance"));
    nameText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_NAME,"Name "));
    nationalityText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_NATIONALITY,"Nationality "));
    ssnText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_SSN,"SSN "));
    genderText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_GENDER,"Gender "));
    femaleText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_FEMALE,"Female"));
    maleText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_MALE, "Male"));
    addressText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_ADDRESS,"Address"));
    postalText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_POSTAL,"Postal Code"));
    cityText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_CITY,"City"));
    countryText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_COUNTRY,"Country"));
    telText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_TEL,"Telephone"));
    mobileText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_MOBILE,"Mobile Phone"));
    emailText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_EMAIL,"Email "));
    tShirtText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_TSHIRT,"T-Shirt"));
    //step one texts end
    
    //step two texts begin
    chipText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_CHIP_TIME,"Championchip timing: "));
    chipLink = new Link(iwrb.getLocalizedString(IWMarathonConstants.RR_CHIP_LINK,"www.championchip.com"),"http://www.championchip.com");
    ownChipText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_OWN_CHIP,"Own Chip - number: "));
    buyChipText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_BUY_CHIP,"Buy Chip - "));
    rentChipText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_RENT_CHIP,"Rent Chip - "));
    groupCompetitionText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_GROUP_COMP,"Group competition"));
    groupNameText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_GROUP_NAME,"Group Name"));
    bestTimeText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_BEST_TIME,"Your best time running this distance"));
    goalTimeText = new Text(iwrb.getLocalizedString(IWMarathonConstants.RR_GOAL_TIME,"Your goal in running this distance now"));
    //step two texts end
  }
  
  private void initializeFields(IWContext iwc) {
    //TODO: remove javascript popups - put red text containing error messages... 
    IWResourceBundle iwrb = getResourceBundle(iwc);
    
    //step one fields begin
    runDisDropdownField = new RunDistanceDropdownDouble();
    runDisDropdownField.setPrimaryLabel(primaryDDLable);
    runDisDropdownField.setSecondaryLabel(secondaryDDLable);
    if(iwc.isParameterSet(IWMarathonConstants.GROUP_TYPE_RUN)) {
      runDisDropdownField.setSelectedValues(iwc.getParameter(IWMarathonConstants.GROUP_TYPE_RUN),"");
    }
    
    nameField = new TextInput(IWMarathonConstants.PARAMETER_NAME);
    nameField.setAsAlphabeticText(iwrb.getLocalizedString("run_reg.name_err_msg","Your name may only contain alphabetic characters"));
    nameField.setAsNotEmpty(iwrb.getLocalizedString("run_reg.name_not_empty","Name field cannot be empty"));
    nameField.setInFocusOnPageLoad(true);
    
    Collection countries = getRunBiz(iwc).getCountries();
    
    nationalityField = new DropdownMenu(IWMarathonConstants.PARAMETER_NATIONALITY);
    countryField = new DropdownMenu(IWMarathonConstants.PARAMETER_COUNTRY);
    SelectorUtility util = new SelectorUtility();
    if (countries != null && !countries.isEmpty()) {
      nationalityField = (DropdownMenu) util.getSelectorFromIDOEntities(
          nationalityField, countries, "getName");
      countryField = (DropdownMenu) util.getSelectorFromIDOEntities(
          countryField, countries, "getName");
    }
    if(iwc.getCurrentLocale().getLanguage().equals("is_IS")) {
      nationalityField.setSelectedElement("104");
      countryField.setSelectedElement("104");
    }
    ssnISField = new TextInput(IWMarathonConstants.PARAMETER_SSN_IS);
    ssnISField.setAsIcelandicSSNumber(iwrb.getLocalizedString("run_reg.ssn_is_err_msg","Your ssn is not a valid Icelandic ssn"));
    ssnISField.setAsNotEmpty(iwrb.getLocalizedString("run_reg.ssnIS_not_empty","ssnIS may not be empty"));
    
    ssnField = new TextInput(IWMarathonConstants.PARAMETER_SSN);
    //TODO: set the ssnField as either dateInput or set a error check on the TextInput... 
    ssnField.setAsNotEmpty("ssn may not be empty");
    
    femaleField = new RadioButton(IWMarathonConstants.PARAMETER_GENDER,IWMarathonConstants.PARAMETER_FEMALE);
    maleField = new RadioButton(IWMarathonConstants.PARAMETER_GENDER,IWMarathonConstants.PARAMETER_MALE);
    
    addressField = new TextInput(IWMarathonConstants.PARAMETER_ADDRESS);
    
    postalField = new TextInput(IWMarathonConstants.PARAMETER_POSTAL);
    postalField.setMaxlength(7);
    
    cityField = new TextInput(IWMarathonConstants.PARAMETER_CITY);
    
    telField = new TextInput(IWMarathonConstants.PARAMETER_TEL);
    telField.setAsIntegers(iwrb.getLocalizedString("run_reg.tel_err_msg","Phonenumber must be integers"));
    
    mobileField = new TextInput(IWMarathonConstants.PARAMETER_MOBILE);
    mobileField.setAsIntegers(iwrb.getLocalizedString("run_reg.mob_err_msg","Mobilephonenumber must be integers"));
    
    emailField = new TextInput(IWMarathonConstants.PARAMETER_EMAIL);
    emailField.setAsEmail(iwrb.getLocalizedString("run_reg.email_err_msg","Not a valid email address"));
    
    tShirtField = new DropdownMenu(IWMarathonConstants.PARAMETER_TSHIRT);
    small = new SelectOption(iwrb.getLocalizedString("run_reg.small","Small"),IWMarathonConstants.PARAMETER_TSHIRT_S);
    medium = new SelectOption(iwrb.getLocalizedString("run_reg.medium","Medium"),IWMarathonConstants.PARAMETER_TSHIRT_M);
    large = new SelectOption(iwrb.getLocalizedString("run_reg.large","Large"),IWMarathonConstants.PARAMETER_TSHIRT_L);
    xlarge = new SelectOption(iwrb.getLocalizedString("run_reg.xlarge","Larger"),IWMarathonConstants.PARAMETER_TSHIRT_XL);
    xxlarge = new SelectOption(iwrb.getLocalizedString("run_reg.xxlarge","Largest"),IWMarathonConstants.PARAMETER_TSHIRT_XXL);
    
    tShirtField.addOption(small);
    tShirtField.addOption(medium);
    tShirtField.addOption(large);
    tShirtField.addOption(xlarge);
    tShirtField.addOption(xxlarge);
    
    stepOneButton = new SubmitButton(iwrb.getLocalizedString("run_reg.submit_step_one","Next step"),PARAMETER_ACTION,String.valueOf(ACTION_STEP_TWO));
    
    //step one fields end
    
    //step two fields begin
    ownChipField = new RadioButton(IWMarathonConstants.PARAMETER_CHIP,IWMarathonConstants.PARAMETER_OWN_CHIP);
    
    buyChipField = new RadioButton(IWMarathonConstants.PARAMETER_CHIP,IWMarathonConstants.PARAMETER_BUY_CHIP);
    
    rentChipField = new RadioButton(IWMarathonConstants.PARAMETER_CHIP,IWMarathonConstants.PARAMETER_RENT_CHIP);
    
    chipNumberField = new TextInput(IWMarathonConstants.PARAMETER_CHIP_NUMBER);
    
    groupCompetitionField = new CheckBox(IWMarathonConstants.PARAMETER_GROUP_COMP);
    
    groupNameField = new TextInput(IWMarathonConstants.PARAMETER_GROUP_NAME);
    
    bestTimeField = new TextInput(IWMarathonConstants.PARAMETER_BEST_TIME);
    
    goalTimeField = new TextInput(IWMarathonConstants.PARAMETER_GOAL_TIME);
    
    stepTwoButton = new SubmitButton(iwrb.getLocalizedString("run_reg.submit_step_two","Next step"));
    //step two fields end
    
    backButton = new BackButton(iwrb.getLocalizedString("run_reg.back","Back to previous step"));
  }
  
  private void stepOne(IWContext iwc) {
    Table t = new Table();
    t.setCellpadding(0);
    t.setCellspacing(0);
    t.setWidth(600);
    f.add(t);
    
    Table ddTable = new Table();
    ddTable.setCellpadding(0);
    ddTable.setCellspacing(0);
    ddTable.add(runDisDropdownField,1,1);
    
    Table infoTable = new Table();
    infoTable.setCellpadding(0);
    infoTable.setCellspacing(0);
    infoTable.add(redStar,1,1);
    infoTable.add(Text.NON_BREAKING_SPACE,1,1);
    infoTable.add(infoRedStarText,1,1);
    
    Table nameTable = new Table();
    nameTable.setCellpadding(0);
    nameTable.setCellspacing(0);
    nameTable.add(nameText,1,1);
    nameTable.add(redStar,1,1);
    nameTable.add(nameField,1,2);
    
    Table nationTable = new Table();
    nationTable.setCellpadding(0);
    nationTable.setCellspacing(0);
    nationTable.add(nationalityText,1,1);
    nationTable.add(redStar,1,1);
    nationTable.add(nationalityField,1,2);
    
    Table ssnTable = new Table();
    ssnTable.setCellpadding(0);
    ssnTable.setCellspacing(0);
    ssnTable.add(ssnText,1,1);
    ssnTable.add(redStar,1,1);
    if(iwc.getCurrentLocale().getLanguage().equals("is_IS")) {
      ssnTable.add(ssnISField,1,2);
    }
    else {
      ssnTable.add(ssnField,1,2);
    }
    
    Table genderTable = new Table();
    genderTable.setCellpadding(0);
    genderTable.setCellspacing(0);
    genderTable.mergeCells(1,1,2,1);
    genderTable.add(genderText,1,1);
    genderTable.add(redStar,1,1);
    genderTable.add(femaleField,1,2);
    genderTable.add(Text.NON_BREAKING_SPACE,1,2);
    genderTable.add(femaleText,1,2);
    genderTable.add(maleField,2,2);
    genderTable.add(Text.NON_BREAKING_SPACE,2,2);
    genderTable.add(maleText,2,2);
    
    Table addressTable = new Table();
    addressTable.setCellpadding(0);
    addressTable.setCellspacing(0);
    addressTable.add(addressText,1,1);
    addressTable.add(addressField,1,2);
    
    Table postalTable = new Table();
    postalTable.setCellpadding(0);
    postalTable.setCellspacing(0);
    postalTable.add(postalText,1,1);
    postalTable.add(postalField,1,2);
    
    Table cityTable = new Table();
    cityTable.setCellpadding(0);
    cityTable.setCellspacing(0);
    cityTable.add(cityText,1,1);
    cityTable.add(cityField,1,2);
    
    Table countryTable = new Table();
    countryTable.setCellpadding(0);
    countryTable.setCellspacing(0);
    countryTable.add(countryText,1,1);
    countryTable.add(countryField,1,2);
    
    Table telTable = new Table();
    telTable.setCellpadding(0);
    telTable.setCellspacing(0);
    telTable.add(telText,1,1);
    telTable.add(telField,1,2);
    
    Table mobileTable = new Table();
    mobileTable.setCellpadding(0);
    mobileTable.setCellspacing(0);
    mobileTable.add(mobileText,1,1);
    mobileTable.add(mobileField,1,2);
    
    Table emailTable = new Table();
    emailTable.setCellpadding(0);
    emailTable.setCellspacing(0);
    emailTable.add(emailText,1,1);
    emailTable.add(emailField,1,2);
    
    Table tShirtTable = new Table();
    tShirtTable.setCellpadding(0);
    tShirtTable.setCellspacing(0);
    tShirtTable.add(tShirtText,1,1);
    tShirtTable.add(tShirtField,1,2);
    
    Table buttonTable = new Table();
    buttonTable.setCellpadding(0);
    buttonTable.setCellspacing(0);
    buttonTable.setWidth(Table.HUNDRED_PERCENT);
    buttonTable.setAlignment(1,1,Table.HORIZONTAL_ALIGN_RIGHT);
    buttonTable.add(stepOneButton,1,1);
    
    t.mergeCells(1,1,2,1);
    t.mergeCells(1,2,2,2);
    t.add(ddTable,1,1);
    t.add(infoTable,1,2);
    t.add(nameTable,1,3);
    t.add(nationTable,2,3);
    t.add(ssnTable,1,4);
    t.add(genderTable,2,4);
    t.add(addressTable,1,5);
    t.add(postalTable,2,5);
    t.add(cityTable,1,6);
    t.add(countryTable,2,6);
    t.add(telTable,1,7);
    t.add(mobileTable,2,7);
    t.add(emailTable,1,8);
    t.add(tShirtTable,2,8);
    t.mergeCells(1,9,2,9);
    t.add(buttonTable,1,9);
    
    f.addParameter(PARAMETER_ACTION,ACTION_STEP_TWO);
    f.keepStatusOnAction();
  }
  
  private void stepTwo(IWContext iwc) {
    Table t = new Table();
    t.setCellpadding(0);
    t.setCellspacing(0);
    t.setWidth(400);
    
    Table infoTable = new Table();
    infoTable.setCellpadding(0);
    infoTable.setCellspacing(0);
    
    Table chipTable = new Table();
    chipTable.setCellpadding(0);
    chipTable.setCellspacing(0);
    chipTable.add(chipText,1,1);
    chipTable.add(chipLink,1,1);
    chipTable.add(ownChipField,1,2);
    chipTable.add(Text.NON_BREAKING_SPACE,1,2);
    chipTable.add(ownChipText,1,2);
    chipTable.add(buyChipField,1,3);
    chipTable.add(Text.NON_BREAKING_SPACE,1,3);
    chipTable.add(buyChipText,1,3);
    chipTable.add(rentChipField,1,4);
    chipTable.add(Text.NON_BREAKING_SPACE,1,4);
    chipTable.add(rentChipText,1,4);
    if(iwc.getCurrentLocale().getLanguage().equals("is_IS")) {
      chipTable.add(String.valueOf(new DecimalFormat("##.##").format(getBuyPrice())),1,3);
      chipTable.add(String.valueOf(new DecimalFormat("##.##").format(getRentPrice())),1,4);
    }
    else {
      float eurBuy = CurrencyBusiness.convertCurrency("ISK","EUR",getBuyPrice());
      chipTable.add(new DecimalFormat("##.##").format(eurBuy),1,3);
      float eurRent = CurrencyBusiness.convertCurrency("ISK","EUR",getRentPrice());
      chipTable.add(new DecimalFormat("##.##").format(eurRent),1,4);
    }
    
    Table groupCompTable = new Table();
    groupCompTable.setCellpadding(0);
    groupCompTable.setCellspacing(0);
    groupCompTable.add(groupCompetitionField,1,1);
    groupCompTable.add(Text.NON_BREAKING_SPACE,1,1);
    groupCompTable.add(groupCompetitionText,1,1);
    
    Table groupNameTable = new Table();
    groupNameTable.setCellpadding(0);
    groupNameTable.setCellspacing(0);
    groupNameTable.add(groupNameText,1,1);
    groupNameTable.add(groupNameField,1,2);
    
    Table bestTimeTable = new Table();
    bestTimeTable.setCellpadding(0);
    bestTimeTable.setCellspacing(0);
    bestTimeTable.add(bestTimeText,1,1);
    bestTimeTable.add(bestTimeField,1,2);
    
    Table goalTimeTable = new Table();
    goalTimeTable.setCellpadding(0);
    goalTimeTable.setCellspacing(0);
    goalTimeTable.add(goalTimeText,1,1);
    goalTimeTable.add(goalTimeField,1,2);
    
    Table buttonTable = new Table();
    buttonTable.setCellpadding(0);
    buttonTable.setCellspacing(0);
    buttonTable.setWidth(Table.HUNDRED_PERCENT);
    buttonTable.setAlignment(2,1,Table.HORIZONTAL_ALIGN_RIGHT);
    buttonTable.add(backButton,1,1);
    buttonTable.add(stepTwoButton,2,1);
    
    t.mergeCells(1,1,2,1);
    t.add(infoTable,1,1);
    t.mergeCells(1,2,2,2);
    t.add(chipTable,1,2);
    t.setVerticalAlignment(1,3,Table.VERTICAL_ALIGN_TOP);
    t.add(groupCompTable,1,3);
    t.add(groupNameTable,2,3);
    t.add(bestTimeTable,1,4);
    t.add(goalTimeTable,2,4);
    t.mergeCells(1,5,2,5);
    t.add(buttonTable,1,5);
    
    f.addParameter(PARAMETER_ACTION,ACTION_SAVE);
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
    
    if(ssnIS!=null && !ssnIS.equals("")) {
      userID = runBiz.saveUser(name,nationality,ssnIS,gender,address,postal,city,country,tel,mobile,email);
    }else if(ssn!=null && !ssn.equals("")) {
      userID = runBiz.saveUser(name,nationality,ssn,gender,address,postal,city,country,tel,mobile,email);
    }
    
    if(userID>0) {
      runBiz.saveRun(userID,run,distance,year,tshirt,chipNumber,groupName,bestTime,goalTime);
    }
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
  private RunBusiness getRunBiz(IWContext iwc){
    RunBusiness business = null;
    try{
      business = (RunBusiness) IBOLookup.getServiceInstance(iwc,RunBusiness.class);
    }catch(IBOLookupException e) {
      business = null;
    }
    return business;  
  }
}
