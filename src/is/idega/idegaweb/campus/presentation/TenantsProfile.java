package is.idega.idegaweb.campus.presentation;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.ContractHome;
import is.idega.idegaweb.campus.block.allocation.presentation.ContractResignWindow;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import is.idega.idegaweb.campus.block.request.business.RequestFinder;
import is.idega.idegaweb.campus.block.request.business.RequestHolder;
import is.idega.idegaweb.campus.block.request.data.Request;
import is.idega.idegaweb.campus.block.request.presentation.RequestView;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.ApplicantHome;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.ApartmentView;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.Floor;
import com.idega.block.finance.data.AccountInfo;
import com.idega.block.finance.data.AccountInfoHome;
import com.idega.core.contact.data.Email;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.text.SocialSecurityNumber;
import com.idega.util.text.StyleConstants;
import com.idega.util.text.TextStyler;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */


public class TenantsProfile extends CampusBlock {

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
                              CHILDNAME = "childname",
                              CHILDSSN = "childssn",
                              CHILDID = "childid",
                              CHILDCOUNT = "childcount";

  private final static String PARAMETER_MODE = "profile_mode";
  private final static String PARAMETER_SAVE = "save";
  private final static String PARAMETER_EDIT = "edit";
  protected final static String PARAMETER_USER_ID = "campus_user_id";


  private boolean isAdmin = false;
  private boolean isLoggedOn = false;
  private int userID = -1;
  private int campusID = -1;
  private boolean update = false;

  private boolean _isAdmin = false;
  private boolean _isLoggedOn = false;
  private int _userID = -1;
 
  private boolean _update = false;
  private boolean allowUserUpdate = true;


  private Contract contract;
  private ApartmentView apartmentView;
  private Applicant _applicant;
  private User user;
  
  private Applicant spouse;
  private Vector children;
  private CampusApplication campusApplication;
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
  public void main(IWContext iwc) throws java.rmi.RemoteException{
    try {
      isAdmin = iwc.hasEditPermission(this);
      isLoggedOn = iwc.isLoggedOn();
    }

    catch(Exception sql) {
      isAdmin = false;
    }


    if( isAdmin || isLoggedOn ) {
      if ( iwc.getParameter(PARAMETER_USER_ID) != null ) {
        try {
          userID = Integer.parseInt(iwc.getParameter(PARAMETER_USER_ID));
        }
        catch (NumberFormatException e) {
          try {
            userID =  iwc.getCurrentUserId();
          }
          catch (Exception ex) {
            userID = -1;
          }
        }
      }
      else {
        try {
          userID = iwc.getCurrentUserId();
        }
        catch (Exception e) {
          userID = -1;
        }
      }
     
      try {
      	java.util.Collection userContracts =(( ContractHome)IDOLookup.getHome(Contract.class)).findByUserAndRented(new Integer(userID),Boolean.TRUE);
        //_contract = ContractFinder.findValidContractByUser(_userID);
      	if(userContracts!=null && !userContracts.isEmpty()){
      		contract = (Contract)userContracts.iterator().next();
      		apartmentView =getBuildingService(iwc).getApartmentViewHome().findByPrimaryKey(contract.getApartmentId());
      	}
      }
      catch(Exception e) {
        contract = null;
      }
    if(contract !=null){
      try {
      	user = contract.getUser();
        _applicant = contract.getApplicant();
        // Spouse and childs
        java.util.Iterator iter = _applicant.getChildrenIterator();
        if(iter !=null){
          String status;
          while(iter.hasNext()){
            Applicant cant = (Applicant) iter.next();
            status = cant.getStatus();
            if("P".equals(status)){
              spouse = cant;
            }
            else if("C".equals(status)){
              if(children==null)
                children = new Vector();
              children.add(cant);
            }

          }
        }
      }
      catch(Exception e) {
        e.printStackTrace();
        _applicant = null;
      }

      try {
      	campusApplication = getApplicationService(iwc).getApplicantInfo(_applicant).getCampusApplication();
      }
      catch(Exception e) {
        campusApplication = null;
      }

      if ( iwc.getParameter(PARAMETER_MODE) != null ) {
        if ( iwc.getParameter(PARAMETER_MODE).equalsIgnoreCase(PARAMETER_SAVE) )
          save(iwc);
        else if ( iwc.getParameter(PARAMETER_MODE).equalsIgnoreCase(PARAMETER_EDIT) )
          update = true;
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
      myTable.add(getProfile(iwc),1,1);
      myTable.add(getApartment(iwc),2,1);
      myTable.add(getAccount(iwc),2,2);
      myTable.add(getRequests(iwc),2,3);
      myTable.setCellspacing(6);

      _image = Table.getTransparentCell(iwc);
      _image.setHeight(6);

      add(myTable);
      }
      else add(localize("noselecteduser","No user selected"));
    }
    else{
      add(getNoAccessObject(iwc));
    }
  }


  private PresentationObject getProfile(IWContext iwc) {

    Form myForm = new Form();

    Table table = new Table();
      table.setCellspacing(1);
      table.setCellpadding(3);
      table.mergeCells(1,1,3,1);
      table.setWidth("100%");
    table.add(formatText(localize("profile","Profile"),"#FFFFFF",true),1,1);
    int row = 2;

    //addToTable(table,row++,localize("name","Name"),_applicant.getFullName(),new TextInput(NAME),35);
    //addToTable(table,row++,localize("ssn","SSN"),_applicant.getSSN(),new TextInput(SSN),10);
    //addToTable(table,row++,localize("email","email"),campusApplication.getEmail(),new TextInput(EMAIL),20);
    addToTable(table,row++,localize("name","Name"),user.getName(),new TextInput(NAME),35);
    TextInput ssnInput = new TextInput(SSN);
    ssnInput.setAsIcelandicSSNumber(localize("provide_icelandic_ss","Please provide a valid Icelandic personal ID"));
    addToTable(table,row++,localize("ssn","SSN"),user.getPersonalID(),new TextInput(SSN),10);
    Collection emails = user.getEmails();
    String email = "";
    if(emails!=null &&!emails.isEmpty()){
    		email = ((Email) emails.iterator().next()).getEmailAddress();
    }
    else{
    		email = campusApplication.getEmail();
    }
    addToTable(table,row++,localize("email","email"),email,new TextInput(EMAIL),20);
    addToTable(table,row++,localize("mobile","Mobile phone"),_applicant.getMobilePhone(),new TextInput(MOBILE),7);
    addToTable(table,row++,localize("faculty","Faculty"),campusApplication.getFaculty(),new TextInput(FACULTY),20);
    addToTable(table,row++,localize("studyTrack","Study track"),campusApplication.getStudyTrack(),new TextInput(STUDYTRACK),20);
    addToTable(table,row++,localize("studyBegin","Study began"),campusApplication.getStudyBeginMonth()+"."+campusApplication.getStudyBeginYear(),new TextInput(STUDYBEGIN),7);
    addToTable(table,row++,localize("studyEnd","Study ends"),campusApplication.getStudyEndMonth()+"."+campusApplication.getStudyEndYear(),new TextInput(STUDYEND),7);

    if ( update  ) {
      String name = "";
      String ssn = "";
      if(spouse!=null){
        name = spouse.getName();
        ssn = spouse.getSSN();
      }
      addToTable(table,row++,localize("spouseName","Spouse"),name,new TextInput(SPOUSENAME),35);
      addToTable(table,row++,localize("spouseSSN","Spouse SSN"),ssn,new TextInput(SPOUSESSN),10);
    }

    if ( update  ) {

      int size = 0;
      if( children != null && children.size() > 0){
       size = children.size();
       for (int i = 0; i < size; i++) {
          Applicant child = (Applicant) children.get(i);
          addToTable(table,row++,localize("childName","Child name"),child.getName(),new TextInput(CHILDNAME+i),35);
          addToTable(table,row++,localize("childSSN","Child ssn"),child.getSSN(),new TextInput(CHILDSSN+i),10);
          table.add(new HiddenInput(CHILDID+i,child.getPrimaryKey().toString()));
        }
      }
      addToTable(table,row++,localize("childName","Child name"),"",new TextInput(CHILDNAME+size),35);
      addToTable(table,row++,localize("childSSN","Child ssn"),"",new TextInput(CHILDSSN+size),10);
      size++;
      table.add(new HiddenInput(CHILDCOUNT,String.valueOf(size)));
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

    if ( update ) {
      myForm.add(table);
      myForm.add(new HiddenInput(PARAMETER_USER_ID,Integer.toString(userID)));
      table.add(new BackButton(getResourceBundle().getImage("back.gif")),1,row);
      table.add(new SubmitButton(getResourceBundle().getImage("save.gif"),PARAMETER_MODE,PARAMETER_SAVE),1,row);
      return myForm;
    }
    else {
      Link editLink = new Link(getResourceBundle().getImage("edit.gif"));
        editLink.addParameter(PARAMETER_MODE,PARAMETER_EDIT);
        editLink.addParameter(PARAMETER_USER_ID,userID);
      table.add(editLink,1,row);
    }
    return table;
  }

  private Table getApartment(IWContext iwc) {
    Table table = new Table();
      table.setCellspacing(1);
      table.setCellpadding(3);
      table.mergeCells(1,1,2,1);
      table.setWidth("100%");

    Text apartmentStatusText = formatText(localize("apartment","Apartment"),"#FFFFFF",true);
      table.add(apartmentStatusText,1,1);
    int row =2;
		String[] attributes = {localize("apartment","Apartment"),localize("floor","Floor"),
		                      localize("complex","Complex"),localize("building","Building")};

		String[] values = {apartmentView.getApartmentName(),apartmentView.getFloorName(),apartmentView.getComplexName(),apartmentView.getBuildingName()};
		row = 2;

		for ( int a = 0; a < attributes.length; a++ ) {
		  table.add(formatText(attributes[a]),1,row);
		  table.add(formatText(values[a]),2,row);
		  row++;
		}
	
    
    if (contract != null) {
    	Date moving = contract.getMovingDate();
    	if (moving != null) {
    		table.add(localize("resignation","Resigned"),1,row);
    		table.add(new IWTimestamp(moving.getTime()).getLocaleDate(iwc.getCurrentLocale()),2,row++);
    	}
    }

    table.setHorizontalZebraColored(white,lightGray);
    table.setColumnColor(1,darkGray);
    table.setColor(1,1,darkBlue);
    table.mergeCells(1,row,2,row);

    table.add(_image,1,row);
    table.setColor(1,row,darkRed);
    row++;


    Link resignLink = new Link(getResourceBundle().getImage("resign.gif"));
      resignLink.addParameter("contract_id",contract.getPrimaryKey().toString());
      resignLink.setWindowToOpen(ContractResignWindow.class);

      resignLink.addParameter("contract_id",contract.getPrimaryKey().toString());
      resignLink.setWindowToOpen(ContractResignWindow.class);


    table.mergeCells(1,row,3,row);
    table.setAlignment(1,row,"right");
    table.add(resignLink,1,row);

    return table;
  }

  private Table getAccount(IWContext iwc)throws java.rmi.RemoteException {
    Table table = new Table();
      table.setCellspacing(1);
      table.setCellpadding(3);
      table.mergeCells(1,1,3,1);
      table.setWidth("100%");

    table.add(formatText(localize("account_status","Account status"),"#FFFFFF",true),1,1);
    table.add(formatText(localize("account","Account")),1,2);
    table.add(formatText(localize("lastentry","Last Entry")),2,2);
    table.add(formatText(localize("balance","Balance")),3,2);

    //Account[] account = AccountManager.findAccounts(_userID);

    //List accounts = FinanceFinder.getInstance().listOfAccountInfoByUserId(userID);
    int row = 3;

    //List accounts = FinanceFinder.getInstance().listOfAccountInfoByUserId(_userID);
    
	Collection accounts=null;
	try {
		accounts = ((AccountInfoHome)IDOLookup.getHome(AccountInfo.class)).findByOwner(new Integer(userID));
	} catch (IDOLookupException e) {
		e.printStackTrace();
	} catch (FinderException e) {
		e.printStackTrace();
	}

    if(accounts!=null && !accounts.isEmpty()){
      java.util.Iterator iter = accounts.iterator();
      while(iter.hasNext()){
        AccountInfo account = (AccountInfo) iter.next();
        table.add(formatText(account.getName()),1,row);
        Date lastUpdate = getCampusService(iwc).getFinanceService().getAccountLastUpdate(account.getAccountId());
        table.add(formatText(new IWTimestamp().getLocaleDate(iwc.getCurrentLocale())),2,row);

        double balance = getCampusService(iwc).getFinanceService().getAccountBalancePublished(account.getAccountId());
        boolean debet = balance >= 0 ? true : false ;
        String color = "";
          if ( debet ) color = "#0000FF";
          else color = "#FF0000";
        
        table.add(getAmountText((balance)),3,row);
        table.setAlignment(3,row,"right");
        row++;
      }
    }

    table.setHorizontalZebraColored(white,lightGray);
    table.setColor(1,1,darkBlue);
    table.setRowColor(2,darkGray);
    table.mergeCells(1,row,3,row);

    table.add(_image,1,row);
    table.setColor(1,row,darkRed);

    return table;
  }

  private Table getRequests(IWContext iwc) {
    Table table = new Table();
      table.setCellspacing(1);
      table.setCellpadding(3);
      table.mergeCells(1,1,3,1);
      table.setWidth("100%");

    table.add(formatText(localize("REQUEST_HEADER","Bei?ni"),"#FFFFFF",true),1,1);
    table.add(formatText(localize("REQUEST_TYPE","Tegund")),1,2);
    table.add(formatText(localize("REQUEST_SENT","Send")),2,2);
    table.add(formatText(localize("REQUEST_STATUS","Sta?a")),3,2);

    int row = 3;

    List requests = RequestFinder.getRequests(userID);
    Request request = null;
    RequestHolder holder = null;

    if ( requests != null ) {
      for ( int a = 0; a < requests.size(); a++ ) {
        holder = (RequestHolder) requests.get(a);
        request = holder.getRequest();
        String type = null;
        try {
          type = request.getRequestType();
        }
        catch(Exception e) {

        }
        String status = null;
        try {
          status = request.getStatus();
        }
        catch(Exception e) {

        }
        table.add(formatText(localize("REQUEST_TYPE_" + type,"Almenn vi?ger?")),1,row);
        try {
          table.add(formatText(new IWTimestamp(request.getDateSent()).getLocaleDate(iwc.getCurrentLocale())),2,row);
        }
        catch(Exception e) {
          table.add("",2,row);
        }
        table.add(formatText(localize("REQUEST_STATUS_" + status,"Innsend")),3,row);
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

    Link requestLink = new Link(getResourceBundle().getImage("request.gif"));
    requestLink.setWindowToOpen(RequestView.class);
    Apartment apartment = null;
    Floor floor = null;
    Building building = null;
    if (contract != null) {
     
    }

    if (apartmentView != null){
      requestLink.addParameter(RequestView.REQUEST_STREET,apartmentView.getBuildingName());
      requestLink.addParameter(RequestView.REQUEST_APRT,apartmentView.getApartmentName());
    }
    if (_applicant != null) {
      requestLink.addParameter(RequestView.REQUEST_NAME,_applicant.getFullName());
      requestLink.addParameter(RequestView.REQUEST_TEL,_applicant.getResidencePhone());
    }
    if (campusApplication != null)
      requestLink.addParameter(RequestView.REQUEST_EMAIL,campusApplication.getEmail());

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



  private void save(IWContext iwc) throws RemoteException {
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
    String childcount = iwc.getParameter(CHILDCOUNT);
    
    ApplicantHome aHome = getApplicationService(iwc).getApplicantHome();
    boolean storeUser = false;
	// TODO move to business layer
    if ( name != null && name.length() > 0 ) {
      _applicant.setFullName(name);
      if(!user.getName().equals(name)){
      	user.setFullName(name);
      	storeUser = true;
      }
    }

    if ( ssn != null ) {
      _applicant.setSSN(ssn);
      if(!ssn.equals(user.getPersonalID()) && SocialSecurityNumber.isValidSocialSecurityNumber(ssn,getCampusSettings(iwc).getSystemLocale())){
      	user.setPersonalID(ssn);
      	storeUser = true;
      }
    }
    if ( email != null ) {
    	   
      campusApplication.setEmail(email);
      getUserService(iwc).storeUserEmail(user,email,true);
      
      
    }
    if ( mobile != null ) {
      _applicant.setMobilePhone(mobile);
    }
    if ( faculty != null ) {
      campusApplication.setFaculty(faculty);
    }
    if ( studyTrack != null ) {
      campusApplication.setStudyTrack(studyTrack);
    }
    ////// spouse //////////////
    try{
      if ( spouseName != null ) {
        if(spouseName.length()>0){
          boolean update = true;
          if(spouse == null){
            spouse = aHome.create();
            spouse.setStatus("P");
            update = false;
          }

          if(!spouseName.equals(spouse.getName())){
            spouse.setFullName(spouseName);
          }
          spouse.setSSN(spouseSSN);

          if(update)
            spouse.store();
          else{
            spouse.store();
            _applicant.addChild(spouse);
          }
        }
      }
     }catch(Exception ex){
      ex.printStackTrace();
    }

    try{
      if(childcount!=null){
        int count = Integer.parseInt(childcount);
        if(count > 0){
          Hashtable chi = new Hashtable();
          if(children!=null){
            for (int i = 0; i < children.size(); i++) {
              Applicant child = (Applicant) children.get(i);
              chi.put((Integer)child.getPrimaryKey(),child);
            }
          }
          for (int i = 0; i < count; i++) {
            String childName = iwc.getParameter(CHILDNAME+i);
            String childSSN = iwc.getParameter(CHILDSSN+i);
            int childId = iwc.isParameterSet(CHILDID+i)?Integer.parseInt(iwc.getParameter(CHILDID+i)):-1;
            if(childName.length() > 0){

              Applicant child = (Applicant) chi.get(new Integer(childId));
              boolean update = true;
              if(child == null){
                child = aHome.create();
                child.setStatus("C");
                update = false;
              }
              if(!childName.equals(child.getName())){
                child.setFullName(childName);
              }
              child.setSSN(childSSN);
              if(update)
                child.store();
              else{
                child.store();
                _applicant.addChild(child);
              }
            }
          }
        }
      }
    }catch(Exception ex){
      ex.printStackTrace();
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
    	  
      _applicant.store();
      if(storeUser)
      	user.store();
      
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }

    try {
      campusApplication.store();
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  private void addToTable(Table table,int row,String attribute,String value,PresentationObject iObj,int width) {
    String className = iObj.getClassName().substring(iObj.getClassName().lastIndexOf(".")+1);
    table.add(formatText(attribute),1,row);
    if ( update ) {
        if ( className.equalsIgnoreCase("TextInput") ) {
          ((TextInput) iObj).setLength(width);
          if ( value != null )
            ((TextInput) iObj).setContent(value);
        }
        else if ( className.equalsIgnoreCase("TextArea") ) {
          ((TextArea) iObj).setColumns(width);
          if ( value != null )
            ((TextArea) iObj).setContent(value);
        }
      iObj.setMarkupAttribute("style","font-family:arial; font-size:8pt; color:#000000; text-align: justify; border: 1 solid #000000");
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


}
