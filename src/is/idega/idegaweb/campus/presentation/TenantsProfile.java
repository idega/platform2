package is.idega.idegaweb.campus.presentation;

import java.sql.SQLException;
import java.util.StringTokenizer;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.Image;
import com.idega.presentation.text.Text;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.BackButton;
import com.idega.block.login.business.LoginBusiness;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.block.application.data.Applicant;
import com.idega.block.building.data.*;
import com.idega.block.building.business.BuildingCacher;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.business.ContractFinder;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import is.idega.idegaweb.campus.block.application.business.CampusApplicationFinder;
import com.idega.block.finance.data.*;
import com.idega.block.finance.business.AccountManager;
import com.idega.block.finance.business.FinanceFinder;
import com.idega.util.idegaTimestamp;
import com.idega.util.text.TextSoap;
import com.idega.util.text.TextStyler;
import com.idega.util.text.StyleConstants;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class TenantsProfile extends Block {

private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.campus";
protected IWResourceBundle iwrb;
protected IWBundle iwb;

private final static String NAME="name",SSN="ssn",MOBILE="mobile",EMAIL="email",FACULTY="faculty",
                            STUDYTRACK="studytrack",STUDYBEGIN="studybegin",STUDYEND="studyend",
                            SPOUSENAME="spousename",SPOUSESSN="spousessn",CHILDREN="children";

private final static String PARAMETER_MODE = "profile_mode";
private final static String PARAMETER_SAVE = "save";
private final static String PARAMETER_EDIT = "edit";

private boolean _isAdmin = false;
private boolean _isLoggedOn = false;
private int _userID = -1;
private int _campusID = -1;
private boolean _update = false;

private Contract contract;
private Applicant applicant;
private CampusApplication campusApplication;
private TextStyler styler;
private Image image;

  public TenantsProfile() {
  }

  public void main(IWContext iwc){
    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);

    try {
      _isAdmin = iwc.hasEditPermission(this);
      _isLoggedOn = com.idega.block.login.business.LoginBusiness.isLoggedOn(iwc);
    }
    catch(Exception sql) {
      _isAdmin = false;
    }

    if( _isAdmin || _isLoggedOn ) {
      try {
        _userID = LoginBusiness.getUser(iwc).getID();
      }
      catch (Exception e) {
        _userID = -1;
      }

      contract = ContractFinder.findApplicant(_userID);
      applicant = ContractFinder.getApplicant(contract);
      campusApplication = CampusApplicationFinder.getApplicantInfo(applicant).getCampusApplication();

      if ( iwc.getParameter(PARAMETER_MODE) != null ) {
        if ( iwc.getParameter(PARAMETER_MODE).equalsIgnoreCase(PARAMETER_SAVE) )
          save(iwc);
        else if ( iwc.getParameter(PARAMETER_MODE).equalsIgnoreCase(PARAMETER_EDIT) )
          _update = true;
      }

      styler = new TextStyler();
        styler.setStyleValue(StyleConstants.ATTRIBUTE_FONT_FAMILY,StyleConstants.FONT_FAMILY_ARIAL);
        styler.setStyleValue(StyleConstants.ATTRIBUTE_FONT_SIZE,"8pt");

      Table myTable = new Table(2,3);
        myTable.setWidth("100%");
        myTable.setWidth(1,"50%");
        myTable.setWidth(2,"50%");
        myTable.mergeCells(1,1,1,3);
        myTable.setColumnVerticalAlignment(1,"top");
        myTable.setColumnVerticalAlignment(2,"top");
        myTable.add(getProfile(),1,1);
        myTable.add(getApartment(),2,1);
        myTable.add(getAccount(),2,2);
        myTable.add(getRequests(),2,3);
        myTable.setCellspacing(6);

      image = myTable.getTransparentCell(iwc);
        image.setHeight(6);

      add(myTable);
    }
    else{
      add(iwrb.getLocalizedString("accessdenied","Access denied"));
    }
  }

  private PresentationObject getProfile() {
    Form myForm = new Form();

    Table table = new Table();
      table.setCellspacing(1);
      table.setCellpadding(3);
      table.mergeCells(1,1,3,1);
      table.setWidth("100%");

    table.add(formatText(iwrb.getLocalizedString("profile","Profile"),"#FFFFFF",true),1,1);
    int row = 2;

    addToTable(table,row++,iwrb.getLocalizedString("name","Name"),applicant.getFullName(),new TextInput(NAME),35);
    addToTable(table,row++,iwrb.getLocalizedString("ssn","SSN"),applicant.getSSN(),new TextInput(SSN),10);
    addToTable(table,row++,iwrb.getLocalizedString("email","email"),campusApplication.getEmail(),new TextInput(EMAIL),20);
    addToTable(table,row++,iwrb.getLocalizedString("mobile","Mobile phone"),applicant.getMobilePhone(),new TextInput(MOBILE),7);
    addToTable(table,row++,iwrb.getLocalizedString("faculty","Faculty"),campusApplication.getFaculty(),new TextInput(FACULTY),20);
    addToTable(table,row++,iwrb.getLocalizedString("studyTrack","Study track"),campusApplication.getStudyTrack(),new TextInput(STUDYTRACK),20);
    addToTable(table,row++,iwrb.getLocalizedString("studyBegin","Study began"),campusApplication.getStudyBeginMonth()+"."+campusApplication.getStudyBeginYear(),new TextInput(STUDYBEGIN),7);
    addToTable(table,row++,iwrb.getLocalizedString("studyEnd","Study ends"),campusApplication.getStudyEndMonth()+"."+campusApplication.getStudyEndYear(),new TextInput(STUDYEND),7);

    if ( _update || (campusApplication.getSpouseName() != null && campusApplication.getSpouseName().length() > 0) ) {
      addToTable(table,row++,iwrb.getLocalizedString("spouseName","Spouse"),campusApplication.getSpouseName(),new TextInput(SPOUSENAME),35);
      addToTable(table,row++,iwrb.getLocalizedString("spouseSSN","Spouse SSN"),campusApplication.getSpouseSSN(),new TextInput(SPOUSESSN),10);
    }

    if ( _update || (campusApplication.getChildren() != null && campusApplication.getChildren().length() > 0) ) {
      String children = campusApplication.getChildren();
      if ( !_update )
        children = TextSoap.findAndReplace(campusApplication.getChildren(),",",Text.getBreak().toString());
      addToTable(table,row++,iwrb.getLocalizedString("childrenProfile","Children"),children,new TextArea(CHILDREN,45,3),45);
    }

    table.setHorizontalZebraColored("#FFFFFF","#ECEEF0");
    table.setColumnColor(1,"9FA9B3");
    table.setColor(1,1,"27334B");
    table.setColumnVerticalAlignment(1,"top");
    table.setColumnVerticalAlignment(2,"top");
    table.mergeCells(1,row,2,row);

    table.add(image,1,row);
    table.setColor(1,row,"#932A2D");

    row++;
    table.mergeCells(1,row,2,row);
    table.setAlignment(1,row,"right");

    if ( _update ) {
      myForm.add(table);
      table.add(new BackButton(iwrb.getImage("back.gif")),1,row);
      table.add(new SubmitButton(iwrb.getImage("save.gif"),PARAMETER_MODE,PARAMETER_SAVE),1,row);
      return myForm;
    }
    else {
      Link editLink = new Link(iwrb.getImage("edit.gif"));
        editLink.addParameter(PARAMETER_MODE,PARAMETER_EDIT);
      table.add(editLink,1,row);
    }
    return table;
  }

  private Table getApartment() {
    Table table = new Table();
      table.setCellspacing(1);
      table.setCellpadding(3);
      table.mergeCells(1,1,2,1);
      table.setWidth("100%");

    Text apartmentStatusText = formatText(iwrb.getLocalizedString("apartment","Apartment"),"#FFFFFF",true);
      table.add(apartmentStatusText,1,1);

    Apartment apartment = BuildingCacher.getApartment(contract.getApartmentId().intValue());
    Floor floor = BuildingCacher.getFloor(apartment.getFloorId());
    Building building = BuildingCacher.getBuilding(floor.getBuildingId());
    Complex complex = BuildingCacher.getComplex(building.getComplexId());

    String[] attributes = {iwrb.getLocalizedString("apartment","Apartment"),iwrb.getLocalizedString("floor","Floor"),
                          iwrb.getLocalizedString("complex","Complex"),iwrb.getLocalizedString("building","Building")};

    String[] values = {apartment.getName(),floor.getName(),complex.getName(),building.getName()};
    int row = 2;

    for ( int a = 0; a < attributes.length; a++ ) {
      table.add(formatText(attributes[a]),1,row);
      table.add(formatText(values[a]),2,row);
      row++;
    }

    table.setHorizontalZebraColored("#FFFFFF","#ECEEF0");
    table.setColumnColor(1,"9FA9B3");
    table.setColor(1,1,"27334B");
    table.mergeCells(1,row,2,row);

    table.add(image,1,row);
    table.setColor(1,row,"#932A2D");

    return table;
  }

  private Table getAccount() {
    Table table = new Table();
      table.setCellspacing(1);
      table.setCellpadding(3);
      table.mergeCells(1,1,3,1);
      table.setWidth("100%");

    table.add(formatText(iwrb.getLocalizedString("account_status","Account status"),"#FFFFFF",true),1,1);
    table.add(formatText(iwrb.getLocalizedString("account","Account")),1,2);
    table.add(formatText(iwrb.getLocalizedString("lastentry","Last Entry")),2,2);
    table.add(formatText(iwrb.getLocalizedString("balance","Balance")),3,2);

    Account[] account = AccountManager.findAccounts(_userID);
    int row = 3;

    for ( int a = 0; a < account.length; a++ ) {
      table.add(formatText(account[a].getName()),1,row);
      table.add(formatText(new idegaTimestamp(account[a].getLastUpdated()).getISLDate(".",true)),2,row);

      float balance = account[a].getBalance();
      boolean debet = balance >= 0 ? true : false ;
      String color = "";
        if ( debet ) color = "#0000FF";
        else color = "#FF0000";

      table.add(formatText(Float.toString(balance),color),3,row);
      table.setAlignment(3,row,"right");
      row++;
    }

    table.setHorizontalZebraColored("#FFFFFF","#ECEEF0");
    table.setColor(1,1,"27334B");
    table.setRowColor(2,"9FA9B3");
    table.mergeCells(1,row,3,row);

    table.add(image,1,row);
    table.setColor(1,row,"#932A2D");

    return table;
  }

  private Table getRequests() {
    Table table = new Table();
      table.setCellspacing(1);
      table.setCellpadding(3);
      table.mergeCells(1,1,3,1);
      table.setWidth("100%");

    table.add(formatText(iwrb.getLocalizedString("requests","Requests"),"#FFFFFF",true),1,1);
    table.add(formatText(iwrb.getLocalizedString("request","Request")),1,2);
    table.add(formatText(iwrb.getLocalizedString("sent","Sent")),2,2);
    table.add(formatText(iwrb.getLocalizedString("status","Status")),3,2);

    int row = 3;

    for ( int a = 1; a < 3; a++ ) {
      table.add(formatText("Request"+a),1,row);
      table.add(formatText(new idegaTimestamp(a,3,2001).getISLDate(".",true)),2,row);
      table.add(formatText("In progress..."),3,row);
      row++;
    }

    table.setHorizontalZebraColored("#FFFFFF","#ECEEF0");
    table.setColor(1,1,"27334B");
    table.setRowColor(2,"9FA9B3");
    table.mergeCells(1,row,3,row);

    table.add(image,1,row);
    table.setColor(1,row,"#932A2D");

    return table;
  }

  private void save(IWContext iwc) {
    String name = iwc.getParameter(NAME);
    String ssn = iwc.getParameter(SSN);
    String email = iwc.getParameter(EMAIL);
    String mobile = iwc.getParameter(MOBILE);
    String faculty = iwc.getParameter(FACULTY);
    String studyTrack = iwc.getParameter(STUDYTRACK);
    String studyBegin = iwc.getParameter(STUDYBEGIN);
    String studyEnd = iwc.getParameter(STUDYEND);
    String spouseName = iwc.getParameter(SPOUSENAME);
    String spouseSSN = iwc.getParameter(SPOUSESSN);
    String children = iwc.getParameter(CHILDREN);

    if ( name != null && name.length() > 0 ) {
      StringTokenizer tokens = new StringTokenizer(name," ");
      int count = 1;
      int number = tokens.countTokens();
      while ( tokens.hasMoreTokens() ) {
        String token = tokens.nextToken();
        if ( count == 1 )
          applicant.setFirstName(token);
        if ( count > 1 && count < number )
          applicant.setMiddleName(token);
        if ( count > 2 && count < number )
          applicant.setMiddleName(" "+token);
        if ( count == number )
          applicant.setLastName(token);
        count++;
      }
    }
    if ( ssn != null ) {
      applicant.setSSN(ssn);
    }
    if ( email != null ) {
      campusApplication.setEmail(email);
    }
    if ( mobile != null ) {
      applicant.setMobilePhone(mobile);
    }
    if ( faculty != null ) {
      campusApplication.setFaculty(faculty);
    }
    if ( studyTrack != null ) {
      campusApplication.setStudyTrack(studyTrack);
    }
    if ( spouseName != null ) {
      campusApplication.setSpouseName(spouseName);
    }
    if ( spouseSSN != null ) {
      campusApplication.setSpouseSSN(spouseSSN);
    }
    if ( children != null ) {
      campusApplication.setChildren(children);
    }
    if ( studyBegin != null && studyBegin.length() > 0 ) {
      String studyBeginMo = studyBegin.substring(0,studyBegin.indexOf("."));
      String studyBeginYe = studyBegin.substring(studyBegin.indexOf(".")+1);
      campusApplication.setStudyBeginMonth(Integer.parseInt(studyBeginMo));
      campusApplication.setStudyBeginYear(Integer.parseInt(studyBeginYe));
    }
    if ( studyBegin != null && studyBegin.length() > 0 ) {
      String studyEndMo = studyEnd.substring(0,studyEnd.indexOf("."));
      String studyEndYe = studyEnd.substring(studyEnd.indexOf(".")+1);
      campusApplication.setStudyEndMonth(Integer.parseInt(studyEndMo));
      campusApplication.setStudyEndYear(Integer.parseInt(studyEndYe));
    }

    try {
      applicant.update();
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
    }

    try {
      campusApplication.update();
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
    }
  }

  private void addToTable(Table table,int row,String attribute,String value,PresentationObject iObj,int width) {
    String className = iObj.getClassName().substring(iObj.getClassName().lastIndexOf(".")+1);
    table.add(formatText(attribute),1,row);

    if ( _update ) {
        if ( className.equalsIgnoreCase("TextInput") ) {
          ((TextInput) iObj).setLength(width);
          if ( value != null )
            ((TextInput) iObj).setContent(value);
        }
        else if ( className.equalsIgnoreCase("TextArea") ) {
          ((TextArea) iObj).setWidth(width);
          if ( value != null )
            ((TextArea) iObj).setContent(value);
        }
      iObj.setAttribute("style","font-family:arial; font-size:8pt; color:#000000; text-align: justify; border: 1 solid #000000");
     table.add(iObj,2,row);
    }
    else {
      table.add(formatText(value),2,row);
    }
  }

  private Text formatText(String text){
    return formatText(text,"#000000",false);
  }

  private Text formatText(String text,String color){
    return formatText(text,color,false);
  }

  private Text formatText(String text,String color,boolean bold){
    if ( text == null ) text = "";
    Text T =new Text(text);
      styler.setStyleValue(StyleConstants.ATTRIBUTE_COLOR,color);
      if ( bold )
        styler.setStyleValue(StyleConstants.ATTRIBUTE_FONT_WEIGHT,StyleConstants.FONT_WEIGHT_BOLD);
      else
        styler.setStyleValue(StyleConstants.ATTRIBUTE_FONT_WEIGHT,StyleConstants.FONT_WEIGHT_NORMAL);

      T.setFontStyle(styler.getStyleString());

    return T;
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
}