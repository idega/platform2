package is.idega.idegaweb.campus.presentation;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Collections;
import com.idega.presentation.Block;
import com.idega.presentation.Table;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.text.*;
import com.idega.block.login.business.LoginBusiness;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.data.*;
import is.idega.idegaweb.campus.block.allocation.business.ContractFinder;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import is.idega.idegaweb.campus.block.application.business.CampusApplicationFinder;
import is.idega.idegaweb.campus.block.phone.data.CampusPhone;
import is.idega.idegaweb.campus.block.phone.business.PhoneFinder;
import is.idega.idegaweb.campus.business.*;
import com.idega.block.application.data.Applicant;
import com.idega.util.text.TextStyler;
import com.idega.util.text.StyleConstants;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:laddi@idega.is">Þórhallur Helgason</a>
 * @version 1.1
 */

public class TenantsHabitants extends Block {

private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.campus";
protected IWResourceBundle iwrb;
protected IWBundle iwb;

private final static String PARAMETER_CAMPUS_ID = "campus_id";
private final static String PARAMETER_ORDER_ID = "order_id";
private final static String APPLICATION_VARIABLE = "cam_tenants";

private boolean _isAdmin = false;
private boolean _isLoggedOn = false;
private int _userID = -1;
private int _campusID = -1;
private int _orderID = -1;
private TextStyler styler;
private Image image;


  public TenantsHabitants() {
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
      styler = new TextStyler();
        styler.setStyleValue(StyleConstants.ATTRIBUTE_FONT_FAMILY,StyleConstants.FONT_FAMILY_ARIAL);
        styler.setStyleValue(StyleConstants.ATTRIBUTE_FONT_SIZE,"8pt");

      try {
        _userID = LoginBusiness.getUser(iwc).getID();
      }
      catch (Exception e) {
        _userID = -1;
      }

      try {
        _campusID = Integer.parseInt(iwc.getParameter(PARAMETER_CAMPUS_ID));
      }
      catch (NumberFormatException e) {
        _campusID = -1;
      }

      try {
        _orderID = Integer.parseInt(iwc.getParameter(PARAMETER_ORDER_ID));
      }
      catch (NumberFormatException e) {
        _orderID = HabitantsComparator.NAME;
      }

      Table myTable = new Table(1,2);
        myTable.setWidth("100%");
      myTable.add(getLinkTable(),1,1);
      myTable.add(getTenantsTable(iwc),1,2);

      image = myTable.getTransparentCell(iwc);
        image.setHeight(6);

      add(myTable);
    }
    else {
      add(iwrb.getLocalizedString("accessdenied","Access denied"));
    }
  }

  public Table getLinkTable(){
    Table table = new Table();

    List complexes = BuildingCacher.getComplexes();
    int column = 1;

    Complex complex = null;
    Link link = null;

    if ( complexes != null ) {
      table.add(formatText("|"),column,1);
      column++;

      for ( int a = 0; a < complexes.size(); a++ ) {
        complex = (Complex) complexes.get(a);
        link = new Link(formatText(complex.getName(),"#000000",true));
          link.addParameter(this.PARAMETER_CAMPUS_ID,complex.getID());

        table.add(link,column,1);
        column++;
        table.add(formatText("|"),column,1);
        column++;
      }
    }

    return table;
  }

  public List listOfComplexHabitants(int iComplexId,IWContext iwc){
    Vector vector = new Vector();
    Building building = null;
    Contract contract = null;
    Applicant applicant = null;
    Apartment apartment = null;
    Floor floor = null;
    HabitantsCollector collector = null;
    CampusApplication campusApplication = null;

    if(!_isAdmin){
      contract = ContractFinder.findApplicant(_userID);
      applicant = ContractFinder.getApplicant(contract);
      apartment = BuildingCacher.getApartment(contract.getApartmentId().intValue());
      floor = BuildingCacher.getFloor(apartment.getFloorId());
      building = BuildingCacher.getBuilding(floor.getBuildingId());
    }

    Map collectionMap = (Map) iwc.getApplicationAttribute(APPLICATION_VARIABLE);
    Integer CMPLX_ID = new Integer(iComplexId);
    if(collectionMap!=null && collectionMap.containsKey(CMPLX_ID) ){
      //System.err.println("getting from memory");
      return (List) collectionMap.get(CMPLX_ID);
    }
    else{
      Map phoneMap = PhoneFinder.mapOfPhonesByApartmentId(PhoneFinder.listOfPhones());
      CampusPhone phone = null;
      //System.err.println("not getting from memory");
      List list = ContractFinder.listOfContractsInComplex(_campusID,new Boolean(true));

      if ( list != null ) {
        for ( int a = 0; a < list.size(); a++ ) {
          contract = (Contract) list.get(a);
          applicant = ContractFinder.getApplicant(contract);
          apartment = BuildingCacher.getApartment(contract.getApartmentId().intValue());
          floor = BuildingCacher.getFloor(apartment.getFloorId());
          campusApplication = CampusApplicationFinder.getApplicantInfo(applicant).getCampusApplication();
          Integer ID = new Integer(apartment.getID());
          if(phoneMap != null && phoneMap.containsKey(ID))
            phone = (CampusPhone)phoneMap.get(ID);
          else
            phone = null;

          collector = new HabitantsCollector();
          collector.setUserID(contract.getUserId().intValue());
          collector.setApartment(apartment.getName());
          collector.setEmail(campusApplication.getEmail());
          collector.setFirstName(applicant.getFirstName());
          collector.setFloor(floor.getName());
          collector.setLastName(applicant.getLastName());
          collector.setMiddleName(applicant.getMiddleName());
          if ( phone != null )
            collector.setPhone(phone.getPhoneNumber());

          vector.add(collector);
        }
      }
      if(collectionMap==null){
        collectionMap = new java.util.Hashtable();
      }
      collectionMap.put(CMPLX_ID,vector);
      iwc.setApplicationAttribute(APPLICATION_VARIABLE,collectionMap);
    }

    return vector;
  }

  public Table getTenantsTable(IWContext iwc){
    Table table = new Table();
      table.setCellspacing(1);
      table.setCellpadding(3);
      table.mergeCells(1,1,5,1);
      table.setWidth("100%");

    Complex complex = null;
    if ( _campusID != -1 )
      complex = BuildingCacher.getComplex(_campusID);
    else if(!_isAdmin){
      BuildingCacher bc = new BuildingCacher();
      complex = bc.getComplex(bc.getBuilding(bc.getFloor(bc.getApartment(ContractFinder.findApplicant(_userID).getApartmentId().intValue()).getFloorId()).getBuildingId()).getComplexId() );
    }
    else {
      complex = BuildingCacher.getComplex();
    }

    if ( _campusID == -1 && complex !=null ) {
      _campusID = complex.getID();
    }

    if(complex !=null)
    table.add(formatText(complex.getName(),"#FFFFFF",true),1,1);

    Link nameLink = new Link(formatText(iwrb.getLocalizedString("name","Name")));
      nameLink.addParameter(PARAMETER_ORDER_ID,HabitantsComparator.NAME);
      nameLink.addParameter(PARAMETER_CAMPUS_ID,_campusID);
    table.add(nameLink,1,2);

    Link apartmentLink = new Link(formatText(iwrb.getLocalizedString("apartment","Apartment")));
      apartmentLink.addParameter(PARAMETER_ORDER_ID,HabitantsComparator.APARTMENT);
      apartmentLink.addParameter(PARAMETER_CAMPUS_ID,_campusID);
    table.add(apartmentLink,2,2);

    Link floorLink = new Link(formatText(iwrb.getLocalizedString("floor","Floor")));
      floorLink.addParameter(PARAMETER_ORDER_ID,HabitantsComparator.FLOOR);
      floorLink.addParameter(PARAMETER_CAMPUS_ID,_campusID);
    table.add(floorLink,3,2);

    table.add(formatText(iwrb.getLocalizedString("phone","Residence phone")),4,2);
    table.add(formatText(iwrb.getLocalizedString("email","E-mail")),5,2);


    int row = 3;

    List vector = listOfComplexHabitants(_campusID,iwc);

    HabitantsComparator comparator = new HabitantsComparator(_orderID);
    Collections.sort(vector,comparator);
    Link adminLink = null;
    int column = 1;

    for ( int a = 0; a < vector.size(); a++ ) {
      column = 1;
      HabitantsCollector collected = (HabitantsCollector) vector.get(a);
      if ( _isAdmin ) {
        adminLink = new Link(formatText(collected.getName()));
          adminLink.addParameter(TenantsProfile.getUserParameter(collected.getUserID()));
          adminLink.addParameter(CampusFactory.getParameter(CampusFactory.TEN_PROFILE));
        table.add(adminLink,column++,row);
      }
      else
        table.add(formatText(collected.getName()),column++,row);
      table.add(formatText(collected.getApartment()),column++,row);
      table.add(formatText(collected.getFloor()),column++,row);
      table.add(formatText(collected.getPhone()),column++,row);
      table.add(new Link(formatText(collected.getEmail()),"mailto:"+collected.getEmail()),column,row);
      row++;
    }

    table.setHorizontalZebraColored(TenantsProfile.white,TenantsProfile.lightGray);
    table.setColor(1,1,TenantsProfile.darkBlue);
    table.setRowColor(2,TenantsProfile.darkGray);
    table.mergeCells(1,row,5,row);
    table.setColumnAlignment(2,"center");
    table.setColumnAlignment(3,"center");
    table.setColumnAlignment(4,"center");
    table.setColumnAlignment(5,"center");
    table.setRowVerticalAlignment(2,"bottom");
    table.setWidth(1,"50%");
    table.setWidth(2,"12%");
    table.setWidth(3,"12%");
    table.setWidth(4,"12%");
    table.setWidth(5,"24%");

    table.add(image,1,row);
    table.setColor(1,row,TenantsProfile.darkRed);

    return table;
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