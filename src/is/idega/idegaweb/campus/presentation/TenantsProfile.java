package is.idega.idegaweb.campus.presentation;

import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.List;
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
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.HiddenInput;
import com.idega.block.login.business.LoginBusiness;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.block.application.data.Applicant;
import com.idega.block.building.data.*;
import com.idega.block.building.business.BuildingCacher;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.business.ContractFinder;
import is.idega.idegaweb.campus.block.allocation.presentation.ContractReSignWindow;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import is.idega.idegaweb.campus.block.application.business.CampusApplicationFinder;
import is.idega.idegaweb.campus.block.request.business.RequestFinder;
import is.idega.idegaweb.campus.block.request.business.RequestHolder;
import is.idega.idegaweb.campus.block.request.data.Request;
import is.idega.idegaweb.campus.block.request.presentation.RequestView;
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
  protected IWResourceBundle _iwrb;
  protected IWBundle _iwb;

  private final static String NAME = "name",
                              SSN = "ssn",
                              MOBILE = "mobile",
                              EMAIL = "email",
                              FACULTY = "faculty",
                              STUDYTRACK = "studytrack",
                              STUDYBEGIN = "studybegin",
                              STUDYEND = "studyend",
                              SPOUSENAME = "spousename",
                              SPOUSESSN = "spousessn",
                              CHILDREN = "children";

  private final static String PARAMETER_MODE = "profile_mode";
  private final static String PARAMETER_SAVE = "save";
  private final static String PARAMETER_EDIT = "edit";
  private final static String PARAMETER_USER_ID = "campus_user_id";

  private boolean _isAdmin = false;
  private boolean _isLoggedOn = false;
  private int _userID = -1;
  private int _campusID = -1;
  private boolean _update = false;

  private Contract _contract;
  private Applicant _applicant;
  private CampusApplication _campusApplication;
  private TextStyler _styler;
  private Image _image;

  public static final String darkBlue = CampusColors.DARKBLUE;
  public static final String darkGray = CampusColors.DARKGREY;
  public static final String lightGray = CampusColors.LIGHTGREY;
  public static final String white = CampusColors.WHITE;
  public static final String darkRed = CampusColors.DARKRED;

  /**
   *
   */
  public TenantsProfile() {
  }

  /**
   *
   */
  public void main(IWContext iwc) {
    _iwrb = getResourceBundle(iwc);
    _iwb = getBundle(iwc);

    try {
      _isAdmin = iwc.hasEditPermission(this);
      _isLoggedOn = com.idega.block.login.business.LoginBusiness.isLoggedOn(iwc);
    }
    catch(Exception sql) {
      _isAdmin = false;
    }

    if( _isAdmin || _isLoggedOn ) {
      if ( iwc.getParameter(PARAMETER_USER_ID) != null ) {
        try {
          _userID = Integer.parseInt(iwc.getParameter(PARAMETER_USER_ID));
        }
        catch (NumberFormatException e) {
          try {
            _userID = LoginBusiness.getUser(iwc).getID();
          }
          catch (Exception ex) {
            _userID = -1;
          }
        }
      }
      else {
        try {
          _userID = LoginBusiness.getUser(iwc).getID();
        }
        catch (Exception e) {
          _userID = -1;
        }
      }

      try {
        _contract = ContractFinder.findApplicant(_userID);
      }
      catch(Exception e) {
        _contract = null;
      }

      try {
        _applicant = ContractFinder.getApplicant(_contract);
      }
      catch(Exception e) {
        _applicant = null;
      }

      try {
        _campusApplication = CampusApplicationFinder.getApplicantInfo(_applicant).getCampusApplication();
      }
      catch(Exception e) {
        _campusApplication = null;
      }

      if ( iwc.getParameter(PARAMETER_MODE) != null ) {
        if ( iwc.getParameter(PARAMETER_MODE).equalsIgnoreCase(PARAMETER_SAVE) )
          save(iwc);
        else if ( iwc.getParameter(PARAMETER_MODE).equalsIgnoreCase(PARAMETER_EDIT) )
          _update = true;
      }

      _styler = new TextStyler();
      _styler.setStyleValue(StyleConstants.ATTRIBUTE_FONT_FAMILY,StyleConstants.FONT_FAMILY_ARIAL);
      _styler.setStyleValue(StyleConstants.ATTRIBUTE_FONT_SIZE,"8pt");

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

      _image = myTable.getTransparentCell(iwc);
      _image.setHeight(6);

      add(myTable);
    }
    else{
      add(_iwrb.getLocalizedString("accessdenied","Access denied"));
    }
  }

  private PresentationObject getProfile() {
    Form myForm = new Form();

    Table table = new Table();
      table.setCellspacing(1);
      table.setCellpadding(3);
      table.mergeCells(1,1,3,1);
      table.setWidth("100%");

    table.add(formatText(_iwrb.getLocalizedString("profile","Profile"),"#FFFFFF",true),1,1);
    int row = 2;

    addToTable(table,row++,_iwrb.getLocalizedString("name","Name"),_applicant.getFullName(),new TextInput(NAME),35);
    addToTable(table,row++,_iwrb.getLocalizedString("ssn","SSN"),_applicant.getSSN(),new TextInput(SSN),10);
    addToTable(table,row++,_iwrb.getLocalizedString("email","email"),_campusApplication.getEmail(),new TextInput(EMAIL),20);
    addToTable(table,row++,_iwrb.getLocalizedString("mobile","Mobile phone"),_applicant.getMobilePhone(),new TextInput(MOBILE),7);
    addToTable(table,row++,_iwrb.getLocalizedString("faculty","Faculty"),_campusApplication.getFaculty(),new TextInput(FACULTY),20);
    addToTable(table,row++,_iwrb.getLocalizedString("studyTrack","Study track"),_campusApplication.getStudyTrack(),new TextInput(STUDYTRACK),20);
    addToTable(table,row++,_iwrb.getLocalizedString("studyBegin","Study began"),_campusApplication.getStudyBeginMonth()+"."+_campusApplication.getStudyBeginYear(),new TextInput(STUDYBEGIN),7);
    addToTable(table,row++,_iwrb.getLocalizedString("studyEnd","Study ends"),_campusApplication.getStudyEndMonth()+"."+_campusApplication.getStudyEndYear(),new TextInput(STUDYEND),7);

    if ( _update || (_campusApplication.getSpouseName() != null && _campusApplication.getSpouseName().length() > 0) ) {
      addToTable(table,row++,_iwrb.getLocalizedString("spouseName","Spouse"),_campusApplication.getSpouseName(),new TextInput(SPOUSENAME),35);
      addToTable(table,row++,_iwrb.getLocalizedString("spouseSSN","Spouse SSN"),_campusApplication.getSpouseSSN(),new TextInput(SPOUSESSN),10);
    }

    if ( _update || (_campusApplication.getChildren() != null && _campusApplication.getChildren().length() > 0) ) {
      String children = _campusApplication.getChildren();
      if ( !_update )
        children = TextSoap.findAndReplace(_campusApplication.getChildren(),",",Text.getBreak().toString());
      addToTable(table,row++,_iwrb.getLocalizedString("childrenProfile","Children"),children,new TextArea(CHILDREN,45,3),45);
    }

    table.setHorizontalZebraColored(white,lightGray);
    table.setColumnColor(1,darkGray);
    table.setColor(1,1,darkBlue);
    table.setColumnVerticalAlignment(1,"top");
    table.setColumnVerticalAlignment(2,"top");
    table.mergeCells(1,row,2,row);

    table.add(_image,1,row);
    table.setColor(1,row,darkRed);

    row++;
    table.mergeCells(1,row,2,row);
    table.setAlignment(1,row,"right");

    if ( _update ) {
      myForm.add(table);
      myForm.add(new HiddenInput(this.PARAMETER_USER_ID,Integer.toString(_userID)));
      table.add(new BackButton(_iwrb.getImage("back.gif")),1,row);
      table.add(new SubmitButton(_iwrb.getImage("save.gif"),PARAMETER_MODE,PARAMETER_SAVE),1,row);
      return myForm;
    }
    else {
      Link editLink = new Link(_iwrb.getImage("edit.gif"));
        editLink.addParameter(PARAMETER_MODE,PARAMETER_EDIT);
        editLink.addParameter(PARAMETER_USER_ID,_userID);
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

    Text apartmentStatusText = formatText(_iwrb.getLocalizedString("apartment","Apartment"),"#FFFFFF",true);
      table.add(apartmentStatusText,1,1);

    Apartment apartment = BuildingCacher.getApartment(_contract.getApartmentId().intValue());
    Floor floor = BuildingCacher.getFloor(apartment.getFloorId());
    Building building = BuildingCacher.getBuilding(floor.getBuildingId());
    Complex complex = BuildingCacher.getComplex(building.getComplexId());

    String[] attributes = {_iwrb.getLocalizedString("apartment","Apartment"),_iwrb.getLocalizedString("floor","Floor"),
                          _iwrb.getLocalizedString("complex","Complex"),_iwrb.getLocalizedString("building","Building")};

    String[] values = {apartment.getName(),floor.getName(),complex.getName(),building.getName()};
    int row = 2;

    for ( int a = 0; a < attributes.length; a++ ) {
      table.add(formatText(attributes[a]),1,row);
      table.add(formatText(values[a]),2,row);
      row++;
    }

    table.setHorizontalZebraColored(white,lightGray);
    table.setColumnColor(1,darkGray);
    table.setColor(1,1,darkBlue);
    table.mergeCells(1,row,2,row);

    table.add(_image,1,row);
    table.setColor(1,row,darkRed);
    row++;

    Link resignLink = new Link(_iwrb.getImage("resign.gif"));
      resignLink.addParameter("contract_id",_contract.getID());
      resignLink.setWindowToOpen(ContractReSignWindow.class);
    table.mergeCells(1,row,3,row);
    table.setAlignment(1,row,"right");
    table.add(resignLink,1,row);

    return table;
  }

  private Table getAccount() {
    Table table = new Table();
      table.setCellspacing(1);
      table.setCellpadding(3);
      table.mergeCells(1,1,3,1);
      table.setWidth("100%");

    table.add(formatText(_iwrb.getLocalizedString("account_status","Account status"),"#FFFFFF",true),1,1);
    table.add(formatText(_iwrb.getLocalizedString("account","Account")),1,2);
    table.add(formatText(_iwrb.getLocalizedString("lastentry","Last Entry")),2,2);
    table.add(formatText(_iwrb.getLocalizedString("balance","Balance")),3,2);

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

    table.setHorizontalZebraColored(white,lightGray);
    table.setColor(1,1,darkBlue);
    table.setRowColor(2,darkGray);
    table.mergeCells(1,row,3,row);

    table.add(_image,1,row);
    table.setColor(1,row,darkRed);

    return table;
  }

  private Table getRequests() {
    Table table = new Table();
      table.setCellspacing(1);
      table.setCellpadding(3);
      table.mergeCells(1,1,3,1);
      table.setWidth("100%");

    table.add(formatText(_iwrb.getLocalizedString("REQUEST_HEADER","Beiðni"),"#FFFFFF",true),1,1);
    table.add(formatText(_iwrb.getLocalizedString("REQUEST_TYPE","Tegund")),1,2);
    table.add(formatText(_iwrb.getLocalizedString("REQUEST_SENT","Send")),2,2);
    table.add(formatText(_iwrb.getLocalizedString("REQUEST_STATUS","Staða")),3,2);

    int row = 3;

    List requests = RequestFinder.getRequests(_userID);
    Request request = null;
    RequestHolder holder = null;

    if ( requests != null ) {
      for ( int a = 0; a < requests.size(); a++ ) {
        holder = (RequestHolder) requests.get(a);
        request = holder.getRequest();
        String type = request.getRequestType();
        String status = request.getStatus();
        table.add(formatText(_iwrb.getLocalizedString("REQUEST_TYPE_" + type,"Almenn viðgerð")),1,row);
        table.add(formatText(new idegaTimestamp(request.getDateSent()).getISLDate(".",true)),2,row);
        table.add(formatText(_iwrb.getLocalizedString("REQUEST_STATUS_" + status,"Innsend")),3,row);
        row++;
      }
    }

    table.setHorizontalZebraColored(white,lightGray);
    table.setColor(1,1,darkBlue);
    table.setRowColor(2,darkGray);
    table.mergeCells(1,row,3,row);

    table.add(_image,1,row);
    table.setColor(1,row,darkRed);
    row++;

    Link requestLink = new Link(_iwrb.getImage("request.gif"));
    requestLink.setWindowToOpen(RequestView.class);
    Apartment apartment = null;
    Floor floor = null;
    Building building = null;
    if (_contract != null) {
      apartment = BuildingCacher.getApartment(_contract.getApartmentId().intValue());
      floor = BuildingCacher.getFloor(apartment.getFloorId());
      building = BuildingCacher.getBuilding(floor.getBuildingId());
    }

    if (building != null)
      requestLink.addParameter(RequestView.REQUEST_STREET,building.getName());
    if (apartment != null)
      requestLink.addParameter(RequestView.REQUEST_APRT,apartment.getName());
    if (_applicant != null) {
      requestLink.addParameter(RequestView.REQUEST_NAME,_applicant.getFullName());
      requestLink.addParameter(RequestView.REQUEST_TEL,_applicant.getResidencePhone());
    }
    if (_campusApplication != null)
      requestLink.addParameter(RequestView.REQUEST_EMAIL,_campusApplication.getEmail());

//    Link requestLink2 = new Link(iwrb.getImage("request.gif"));
//    requestLink2.setWindowToOpen(RequestView.class);
//    requestLink2.addParameter(TabAction.sAction,0);
//    requestLink2.addParameter(CampusFactory.getParameter(60));


    table.mergeCells(1,row,3,row);
    table.setAlignment(1,row,"right");
    table.add(requestLink,1,row);
//    table.add(requestLink2,1,row);

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
          _applicant.setFirstName(token);
        if ( count > 1 && count < number )
          _applicant.setMiddleName(token);
        if ( count > 2 && count < number )
          _applicant.setMiddleName(" "+token);
        if ( count == number )
          _applicant.setLastName(token);
        count++;
      }
    }
    if ( ssn != null ) {
      _applicant.setSSN(ssn);
    }
    if ( email != null ) {
      _campusApplication.setEmail(email);
    }
    if ( mobile != null ) {
      _applicant.setMobilePhone(mobile);
    }
    if ( faculty != null ) {
      _campusApplication.setFaculty(faculty);
    }
    if ( studyTrack != null ) {
      _campusApplication.setStudyTrack(studyTrack);
    }
    if ( spouseName != null ) {
      _campusApplication.setSpouseName(spouseName);
    }
    if ( spouseSSN != null ) {
      _campusApplication.setSpouseSSN(spouseSSN);
    }
    if ( children != null ) {
      _campusApplication.setChildren(children);
    }
    if ( studyBegin != null && studyBegin.length() > 0 ) {
      String studyBeginMo = studyBegin.substring(0,studyBegin.indexOf("."));
      String studyBeginYe = studyBegin.substring(studyBegin.indexOf(".")+1);
      _campusApplication.setStudyBeginMonth(Integer.parseInt(studyBeginMo));
      _campusApplication.setStudyBeginYear(Integer.parseInt(studyBeginYe));
    }
    if ( studyBegin != null && studyBegin.length() > 0 ) {
      String studyEndMo = studyEnd.substring(0,studyEnd.indexOf("."));
      String studyEndYe = studyEnd.substring(studyEnd.indexOf(".")+1);
      _campusApplication.setStudyEndMonth(Integer.parseInt(studyEndMo));
      _campusApplication.setStudyEndYear(Integer.parseInt(studyEndYe));
    }

    try {
      _applicant.update();
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
    }

    try {
      _campusApplication.update();
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
      _styler.setStyleValue(StyleConstants.ATTRIBUTE_COLOR,color);
      if ( bold )
        _styler.setStyleValue(StyleConstants.ATTRIBUTE_FONT_WEIGHT,StyleConstants.FONT_WEIGHT_BOLD);
      else
        _styler.setStyleValue(StyleConstants.ATTRIBUTE_FONT_WEIGHT,StyleConstants.FONT_WEIGHT_NORMAL);

      T.setFontStyle(_styler.getStyleString());

    return T;
  }

  public static Parameter getUserParameter(int userID) {
    return new Parameter(PARAMETER_USER_ID,Integer.toString(userID));
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
}