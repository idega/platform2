package is.idega.idegaweb.campus.presentation;

import is.idega.idegaweb.campus.block.allocation.business.ContractFinder;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import is.idega.idegaweb.campus.block.phone.business.PhoneFinder;
import is.idega.idegaweb.campus.block.phone.data.CampusPhone;
import is.idega.idegaweb.campus.business.HabitantsCollector;
import is.idega.idegaweb.campus.business.HabitantsComparator;
import is.idega.idegaweb.campus.business.HabitantsFinder;
import is.idega.idegaweb.campus.data.Habitant;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.idega.block.application.data.Applicant;
import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.Floor;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DataTable;
import com.idega.util.IWTimestamp;
import com.idega.util.text.StyleConstants;
import com.idega.util.text.TextStyler;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:laddi@idega.is">Þórhallur Helgason</a>
 * @version 1.1
 */

public class TenantsHabitants extends Block implements Campus{
  private static final String NAME_KEY = "cam_habitants_view";
  private static final String DEFAULT_VALUE = "Habitant list";
  private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.campus";
  private final static String PARAMETER_CAMPUS_ID = "campus_id";
  private final static String PARAMETER_ORDER_ID = "order_id";
  private final static String APPLICATION_VARIABLE = "cam_tenants";
  private final static String APPLICATION_REFRESH_RATE = "cam_ten_refresh_rate";

  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  private boolean _isAdmin = false;
  private boolean _isLoggedOn = false;
  private boolean _isPublic = true;
  private int _userID = -1;
  private int _campusID = -1;
  private int _orderID = -1;
  private TextStyler styler;
  private Image image;
  private long refreshRate = 1000*60*60*2;


  public TenantsHabitants() {
  }

  public void main(IWContext iwc){
    //debugParameters(iwc);
    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);


      _isAdmin = iwc.hasEditPermission(this);
      _isLoggedOn = iwc.isLoggedOn();
      if (_isLoggedOn)
        _isPublic = false;


    if(iwc.isParameterSet(TenantsProfile.PARAMETER_USER_ID)){
      add(new TenantsProfile());
    }
    else
      add(getHabitantsTable(iwc));
//      add(iwrb.getLocalizedString("accessdenied","Access denied"));

  }

  private Table getHabitantsTable(IWContext iwc){
    styler = new TextStyler();
    styler.setStyleValue(StyleConstants.ATTRIBUTE_FONT_FAMILY,StyleConstants.FONT_FAMILY_ARIAL);
    styler.setStyleValue(StyleConstants.ATTRIBUTE_FONT_SIZE,"8pt");

    if(iwc.isParameterSet(PARAMETER_ORDER_ID))
        _orderID = Integer.parseInt(iwc.getParameter(PARAMETER_ORDER_ID));
    else
        _orderID = HabitantsComparator.NAME;

    if(iwc.isParameterSet(PARAMETER_CAMPUS_ID))
      _campusID = Integer.parseInt(iwc.getParameter(PARAMETER_CAMPUS_ID));

    if( _isAdmin || _isLoggedOn ) {
      _userID = iwc.getUserId();
    }
    else {
      _isPublic = true;
    }

      Table myTable = new Table(1,2);
        myTable.setWidth("100%");
      myTable.add(getLinkTable(),1,1);
      myTable.add(getTenantsTable(iwc),1,2);

      image = myTable.getTransparentCell(iwc);
        image.setHeight(6);
      return myTable;
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
          link.addParameter(PARAMETER_CAMPUS_ID,complex.getID());
//          link.addParameter(CampusMenu.getParameter(TEN_HABITANTS));
//          link.addParameter(TabAction.sAction,22);

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

    if(!_isAdmin && !_isPublic){
      contract = ContractFinder.findByUser(_userID);
      if(contract !=null){
      applicant = ContractFinder.getApplicant(contract);
      apartment = BuildingCacher.getApartment(contract.getApartmentId().intValue());
      floor = BuildingCacher.getFloor(apartment.getFloorId());
      building = BuildingCacher.getBuilding(floor.getBuildingId());
      }
    }

    Map collectionMap = (Map) iwc.getApplicationAttribute(APPLICATION_VARIABLE);
    Long refreshed = (Long) iwc.getApplicationAttribute(APPLICATION_REFRESH_RATE);
    boolean refresh = false;
    if(refreshed !=null){
      long now = IWTimestamp.RightNow().getTimestamp().getTime();
      long then = refreshed.longValue();
      long diff = now-then;
      refresh = (Math.abs(diff) >= refreshRate);
      //System.err.println("diff: "+diff+"  refresrate: "+refreshRate);
    }

    Integer CMPLX_ID = new Integer(iComplexId);
    if(collectionMap!=null && collectionMap.containsKey(CMPLX_ID) && !refresh ){
      //System.err.println("getting from memory");
      return (List) collectionMap.get(CMPLX_ID);
    }
    else{
      Map phoneMap = PhoneFinder.mapOfPhonesByApartmentId(PhoneFinder.listOfPhones());
      CampusPhone phone = null;
      //System.err.println("not getting from memory");
      //List list = ContractFinder.listOfContractsInComplex(_campusID,new Boolean(true));
      List list = HabitantsFinder.findHabitants(_campusID);
      Habitant hab;
      if ( list != null ) {
        for ( int a = 0; a < list.size(); a++ ) {
          hab = (Habitant) list.get(a);
          collector = new HabitantsCollector();
          collector.setUserID(hab.getUserId());
          collector.setApartment(hab.getApartment());
          /** @todo  fixa email */
          collector.setEmail("");
          collector.setName(hab.getFullName());
          collector.setFloor(hab.getFloor());
          collector.setAddress(hab.getAddress());
          collector.setPhone(hab.getPhoneNumber());

          vector.add(collector);
        }
      }
      if(collectionMap==null){
        collectionMap = new java.util.Hashtable();
      }
      collectionMap.put(CMPLX_ID,vector);
      iwc.setApplicationAttribute(APPLICATION_VARIABLE,collectionMap);
      iwc.setApplicationAttribute(APPLICATION_REFRESH_RATE,new Long( IWTimestamp.RightNow().getTimestamp().getTime()));
    }

    return vector;
  }

  public PresentationObject getTenantsTable(IWContext iwc){
    DataTable table = new DataTable();
    table.setTitlesHorizontal(true);
    table.getContentTable().setCellpadding(3);
    table.getContentTable().setCellspacing(1);
    /*
      table.setCellspacing(1);
      table.setCellpadding(3);
      table.mergeCells(1,1,5,1);
    */
      table.setWidth("100%");

    Complex complex = null;
    if ( _campusID != -1 )
      complex = BuildingCacher.getComplex(_campusID);
    else if(!_isAdmin && !_isPublic){
      BuildingCacher bc = new BuildingCacher();
      Contract C = ContractFinder.findByUser(_userID);
      if(C!=null )
        complex = bc.getComplex(bc.getBuilding(bc.getFloor(bc.getApartment(C.getApartmentId().intValue()).getFloorId()).getBuildingId()).getComplexId() );
    }
    else {
      complex = BuildingCacher.getComplex();
    }

    if ( _campusID == -1 && complex !=null ) {
      _campusID = complex.getID();
    }

    if(complex !=null)
    table.addTitle(complex.getName());

    Link nameLink = new Link(formatText(iwrb.getLocalizedString("name","Name")));
      nameLink.addParameter(PARAMETER_ORDER_ID,HabitantsComparator.NAME);
      nameLink.addParameter(PARAMETER_CAMPUS_ID,_campusID);
    table.add(nameLink,1,1);

    Link apartmentLink = new Link(formatText(iwrb.getLocalizedString("address","Address")));
      apartmentLink.addParameter(PARAMETER_ORDER_ID,HabitantsComparator.ADDRESS);
      apartmentLink.addParameter(PARAMETER_CAMPUS_ID,_campusID);
    table.add(apartmentLink,2,1);

    Link addressLink = new Link(formatText(iwrb.getLocalizedString("apartment","Apartment")));
      addressLink.addParameter(PARAMETER_ORDER_ID,HabitantsComparator.APARTMENT);
      addressLink.addParameter(PARAMETER_CAMPUS_ID,_campusID);
    table.add(addressLink,3,1);

    Link floorLink = new Link(formatText(iwrb.getLocalizedString("floor","Floor")));
      floorLink.addParameter(PARAMETER_ORDER_ID,HabitantsComparator.FLOOR);
      floorLink.addParameter(PARAMETER_CAMPUS_ID,_campusID);
    table.add(floorLink,4,1);

    table.add(formatText(iwrb.getLocalizedString("phone","Residence phone")),5,1);
    //table.add(formatText(iwrb.getLocalizedString("email","E-mail")),6,1);


    int row = 2;

    List vector = listOfComplexHabitants(_campusID,iwc);

    HabitantsComparator comparator = new HabitantsComparator(_orderID);
    //System.err.println("starting sort "+com.idega.util.IWTimestamp.getTimestampRightNow().toString());
    Collections.sort(vector,comparator);
    //System.err.println("ending sort "+com.idega.util.IWTimestamp.getTimestampRightNow().toString());
    Link adminLink = null;
    int column = 1;

    for ( int a = 0; a < vector.size(); a++ ) {
      column = 1;
      HabitantsCollector collected = (HabitantsCollector) vector.get(a);
      if ( _isAdmin ) {
        adminLink = new Link(formatText(collected.getName()));
          adminLink.addParameter(TenantsProfile.getUserParameter(collected.getUserID()));
          //adminLink.addParameter(CampusFactory.getParameter(CampusFactory.TEN_PROFILE));
        table.add(adminLink,column++,row);
      }
      else
        table.add(formatText(collected.getName()),column++,row);
      table.add(formatText(collected.getAddress()),column++,row);
      table.add(formatText(collected.getApartment()),column++,row);
      table.add(formatText(collected.getFloor()),column++,row);
      table.add(formatText(collected.getPhone()),column++,row);
      //table.add(new Link(formatText(collected.getEmail()),"mailto:"+collected.getEmail()),column,row);
      row++;
    }

    /*
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
*/
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

  /**
   *
   */
  public String getLocalizedNameKey() {
    return(NAME_KEY);
  }

  /**
   *
   */
  public String getLocalizedNameValue() {
    return(DEFAULT_VALUE);
  }

  public void setRefreshRate(long refreshRate){
    this.refreshRate = refreshRate;
  }

}
