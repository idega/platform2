package com.idega.block.building.presentation;

import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.*;
import com.idega.block.building.data.*;
import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.business.BuildingBusiness;
import com.idega.block.media.presentation.SimpleChooserWindow;
import java.sql.SQLException;
import java.io.IOException;
import java.util.StringTokenizer;
import com.oreilly.servlet.*;
import com.idega.data.GenericEntity;
import com.idega.presentation.Editor;
import com.idega.block.media.presentation.ImageInserter;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class BuildingEditor extends com.idega.presentation.Block{

  protected boolean isAdmin = false;
  protected String TextFontColor = "#000000";
  public String sAction = "be_action";
  private static final String prmSave = "save",prmDelete = "del";
  private String styleAttribute = "font-family:arial; font-size:8pt; color:#000000; text-align: justify; border: 1 solid #000000";
  private String styleAttribute2 = "font-family:arial; font-size:8pt; color:#000000; text-align: justify;";
  public final int COMPLEX = 1,BUILDING = 2,FLOOR = 3, APARTMENT = 4,
                    CATEGORY = 5,TYPE = 6;
  private int eId = 0;
  protected int fontSize = 1;
  protected boolean fontBold = false;
  private Table outerTable;

  private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.building";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  private boolean includeLinks = true;

  public void setToIncludeLinks(boolean include){
    includeLinks = include;
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public String getLocalizedNameKey(){
    return "building_editor";
  }

  public String getLocalizedNameValue(){
    return "Buildings";
  }

  protected void control(IWContext iwc){


    outerTable = new Table(1,2);
      outerTable.setCellpadding(0);
      outerTable.setCellspacing(0);
      outerTable.setWidth("100%");
      outerTable.setHeight("100%");
      outerTable.setHeight(2,"100%");
    //  outerTable.setColor(1,1,"#0E2456");


    int iAction = BUILDING;
    if(iwc.getParameter(sAction)!= null){
      iAction = Integer.parseInt(iwc.getParameter(sAction));
    }

    if(iwc.getParameter("dr_id")!=null){
       eId = Integer.parseInt(iwc.getParameter("dr_id"));
    }
    else if ( (String) iwc.getSessionAttribute("dr_id") != null ) {
       eId = Integer.parseInt((String) iwc.getSessionAttribute("dr_id"));
       iwc.removeSessionAttribute("dr_id");
    }

    //System.err.println("Entity id " + eId);

    if(iwc.getParameter(prmSave)!=null || iwc.getParameter(prmSave+".x")!=null){
      if(iwc.getParameter("bm_choice")!=null){
        int i = Integer.parseInt(iwc.getParameter("bm_choice"));
         switch (i) {
            case COMPLEX  : storeComplex(iwc);   break;
            case BUILDING : storeBuilding(iwc);  break;
            case FLOOR    : storeFloor(iwc);     break;
            case APARTMENT: storeApartment(iwc); break;
            case CATEGORY : storeApartmentCategory(iwc);  break;
            case TYPE     : storeApartmentType(iwc);   break;
        }
      }
    }
    else if(iwc.getParameter( prmDelete)!=null || iwc.getParameter(prmDelete+".x")!=null){
       if(iwc.getParameter("bm_choice")!=null && eId > 0){
        int i = Integer.parseInt(iwc.getParameter("bm_choice"));
         switch (i) {
            case COMPLEX  : BuildingBusiness.deleteComplex(eId);   break;
            case BUILDING : BuildingBusiness.deleteBuilding(eId);  break;
            case FLOOR    : BuildingBusiness.deleteFloor(eId);     break;
            case APARTMENT: BuildingBusiness.deleteApartment(eId); break;
            case CATEGORY : BuildingBusiness.deleteApartmentCategory(eId);  break;
            case TYPE     : BuildingBusiness.deleteApartmentType(eId);   break;
        }
        eId = 0;
        //BuildingCacher.setToReloadNextTimeReferenced();
      }
    }
    if(includeLinks)
    outerTable.add(makeLinkTable(iAction),1,1);
    switch (iAction) {
      case COMPLEX  : doComplex(iwc);   break;
      case BUILDING : doBuilding(iwc);  break;
      case FLOOR    : doFloor(iwc);     break;
      case APARTMENT: doApartment(iwc); break;
      case CATEGORY : doCategory(iwc);  break;
      case TYPE     : doType(iwc);   break;
    }
    add(outerTable);
  }
  private void doMain(IWContext iwc,boolean ifMulti,int choice) {
    doBuilding(iwc);

  }
  private void doComplex(IWContext iwc){
    Complex eComplex = eId > 0 ? BuildingCacher.getComplex(eId) : null;
    outerTable.add(makeComplexFields(eComplex),1,2);
  }
  private void doBuilding(IWContext iwc){
    Building eBuilding = eId > 0 ? BuildingCacher.getBuilding(eId) : null;
    outerTable.add(makeBuildingFields(eBuilding),1,2);
  }
  private void doFloor(IWContext iwc){
    Floor eFloor = eId > 0 ? BuildingCacher.getFloor(eId) : null;
    outerTable.add(makeFloorFields(eFloor),1,2);
  }
  private void doApartment(IWContext iwc){
    Apartment eApartment = eId > 0 ? BuildingCacher.getApartment(eId) : null;
    outerTable.add(makeApartmentFields(eApartment),1,2);
  }
  private void doType(IWContext iwc){
    // Dirty job below
    int iPhotoId = 1, iPlanId = 1;
    boolean b1 = false,b2 = false;
    if(iwc.getSessionAttribute("tphotoid2") != null){
      b1 = true;
      iPhotoId = Integer.parseInt((String)iwc.getSessionAttribute("tphotoid2"));
    }
    if(iwc.getSessionAttribute("tplanid2")!=null){
      b2 = true;
      iPlanId = Integer.parseInt((String)iwc.getSessionAttribute("tplanid2"));
    }
    if(b1 && b2){
      iwc.removeSessionAttribute("tphotoid2");
      iwc.removeSessionAttribute("tplanid2");
    }
    try{
      ApartmentType eApartmentType = eId > 0 ? new ApartmentType(eId) : null;
      outerTable.add(makeTypeFields(eApartmentType,iPhotoId,iPlanId),1,2);
    }
    catch(SQLException sql){sql.printStackTrace();}
    //add(getTypes());
  }
  private void doCategory(IWContext iwc){
    try{
      ApartmentCategory eApartmentCategory = eId > 0 ? new ApartmentCategory(eId) : null;
      outerTable.add(makeCategoryFields(eApartmentCategory),1,2);
    }
    catch(SQLException sql){}
  }

  private void doQuit(IWContext iwc) throws SQLException{  }
  private void doSave(IWContext iwc) throws SQLException{  }

  private void storeComplex(IWContext iwc){
    String sName = iwc.getParameter("bm_name").trim();
    String sInfo = iwc.getParameter("bm_info").trim();
    String sImageId = iwc.getParameter("mapid");
    String sId = iwc.getParameter("dr_id");
    int imageid = 1;
    int id = -1;
    try {  imageid = Integer.parseInt(sImageId); }
    catch (NumberFormatException ex) { imageid = 1;  }
    try { id = Integer.parseInt(sId); }
    catch (Exception ex) { id = -1; }

    BuildingBusiness.saveComplex(id,sName,sInfo,imageid);

  }

  private void storeBuilding(IWContext iwc){
    String sName = iwc.getParameter("bm_name").trim();
    String sInfo = iwc.getParameter("bm_info").trim();
    String sAddress = iwc.getParameter("bm_address").trim();
    String sImageId = iwc.getParameter("photoid");
    String sComplexId = iwc.getParameter("dr_complex");
    String sId = iwc.getParameter("dr_id");
    String sSerie = iwc.getParameter("bm_serie");
    int imageid = 1,iSerie = 0;
    int id = -1;
    int complexid = -1;
    try {  id = Integer.parseInt(sId);  }
    catch (NumberFormatException ex) { id = -1;  }
    try {  complexid = Integer.parseInt(sComplexId);  }
    catch (NumberFormatException ex) { complexid = -1;  }
    try {  imageid = Integer.parseInt(sImageId); }
    catch (NumberFormatException ex) { imageid = 1;  }

    BuildingBusiness.saveBuilding(id,sName,sAddress,sInfo,imageid,complexid,sSerie);
  }
  private void storeFloor(IWContext iwc){

    String sName = iwc.getParameter("bm_name").trim();
    String sInfo = iwc.getParameter("bm_info").trim();
    String sImageId = iwc.getParameter("photoid");
    String sBuildingId = iwc.getParameter("dr_building");
    String sId = iwc.getParameter("dr_id");
    int imageid = 1;
    int id = -1;
    int buildingid = -1;
    try {  id = Integer.parseInt(sId);  }
    catch (NumberFormatException ex) { id = -1;  }
    try {  buildingid = Integer.parseInt(sBuildingId);  }
    catch (NumberFormatException ex) { buildingid = -1;  }
    try {  imageid = Integer.parseInt(sImageId);  }
    catch (NumberFormatException ex) { imageid = 1;  }

    BuildingBusiness.saveFloor(id,sName,buildingid,sInfo,imageid);
  }
  private void storeApartmentCategory(IWContext iwc){
    String sName = iwc.getParameter("bm_name").trim();
    String sInfo = iwc.getParameter("bm_info").trim();
    String sImageId = iwc.getParameter("iconid");
    String sId = iwc.getParameter("dr_id");
    int imageid = 1;
    int id = -1;
    try {  imageid = Integer.parseInt(sImageId); }
    catch (NumberFormatException ex) { imageid = 1;  }
    try {  id = Integer.parseInt(sId);  }
    catch (NumberFormatException ex) { id = -1;  }

    BuildingBusiness.saveApartmentCategory(id,sName,sInfo,imageid);

  }
  private void storeApartmentType(IWContext iwc){

    String sName = iwc.getParameter("bm_name").trim();
    String sInfo = iwc.getParameter("bm_info").trim();
    String sExtraInfo = iwc.getParameter("extra_info").trim();
    String sId = iwc.getParameter("dr_id");
    String sRoomCount = iwc.getParameter("bm_roomcount");
    String sCategoryId = iwc.getParameter("bm_category");
    String sImageId = iwc.getParameter("tphotoid");
    String sPlanId = iwc.getParameter("tplanid");
    String sArea = iwc.getParameter("bm_area").trim();
    String sKitch = iwc.getParameter("bm_kitch");
    String sBath = iwc.getParameter("bm_bath");
    String sStorage = iwc.getParameter("bm_stor");
    String sBalcony = iwc.getParameter("bm_balc");
    String sStudy = iwc.getParameter("bm_study");
    String sLoft = iwc.getParameter("bm_loft");
    String sFurniture = iwc.getParameter("bm_furni");
    String sRent = iwc.getParameter("bm_rent");

    int planid = 1;
    int imageid = 1;
    int id = -1;
    int categoryid = -1;
    int rent = 0;
    float area = 0;
    int count = 0;
    try { id = Integer.parseInt(sId);  }
    catch (NumberFormatException ex) { id = -1;  }
    try { categoryid = Integer.parseInt(sCategoryId);  }
    catch (NumberFormatException ex) { categoryid = -1;  }
    try { imageid = Integer.parseInt(sImageId);  }
    catch (NumberFormatException ex) { imageid = 1;  }
    try{  planid = Integer.parseInt(sPlanId); }
    catch (NumberFormatException ex) { planid = 1;  }
     try{  rent = Integer.parseInt(sRent); }
    catch (NumberFormatException ex) { rent = 0;  }
     try{  area = Float.parseFloat(sArea); }
    catch (NumberFormatException ex) { area = 0;  }
     try{  count = Integer.parseInt(sRoomCount); }
    catch (NumberFormatException ex) { count = 1;  }

    BuildingBusiness.saveApartmentType(id,sName,sInfo,sExtraInfo,planid,imageid,
      categoryid,area,count,rent,sBalcony!=null?true:false,sBath!=null?true:false,
      sKitch !=null?true:false,sLoft != null?true:false,sStorage !=null?true:false,
      sFurniture!= null?true:false);

  }
  private void storeApartment(IWContext iwc){

    String sName = iwc.getParameter("bm_name").trim();
    String sInfo = iwc.getParameter("bm_info").trim();
    String sId = iwc.getParameter("dr_id");
    String sType = iwc.getParameter("bm_type");
    String sFloorId = iwc.getParameter("bm_floor");
    String sRentable = iwc.getParameter("bm_rentable");
    String sImageId = iwc.getParameter("photoid");
    String sSerie = iwc.getParameter("bm_serie");
    boolean bRentable = sRentable!=null?true:false;

    int id = -1;
    int floorid = -1;
    int imageid = 1;
    int typeid = -1;
    try { id = Integer.parseInt(sId);  }
    catch (NumberFormatException ex) { id = -1;  }
    try { floorid = Integer.parseInt(sFloorId);  }
    catch (NumberFormatException ex) { floorid = -1;  }
    try { imageid = Integer.parseInt(sImageId);  }
    catch (NumberFormatException ex) { imageid = 1;  }
    try { typeid = Integer.parseInt(sType);  }
    catch (NumberFormatException ex) { typeid = -1;  }

    //System.err.println("id is "+id);

    String slname = sName;
    /*
    StringTokenizer st2 = new StringTokenizer(slname,",");
    StringTokenizer st = new StringTokenizer(sName,":");
    int count = st.countTokens();
    int count2 = st2.countTokens();
    if( count == 2){
      String sLower = st.nextToken();
      String sUpper = st.nextToken();
      int iLower = 0,iUpper = 0;
      try{
         iLower = Integer.parseInt(sLower);
         iUpper = Integer.parseInt(sUpper);
      }
      catch(NumberFormatException nfe){}

      if(iUpper - iLower != 0){

        for (int i = iLower; i <= iUpper; i++) {
          BuildingBusiness.saveApartment(-1,String.valueOf(i),sInfo,floorid,
            typeid,bRentable,imageid,sSerie);
        }
      }
    }
    else if(count2 > 0  ){
      for (int i = 0; i < count2; i++) {
        BuildingBusiness.saveApartment(-1,st2.nextToken(),sInfo,floorid,
            typeid,bRentable,imageid,sSerie);
      }
    }
    else*/{
      BuildingBusiness.saveApartment(id,sName,sInfo,floorid,
            typeid,bRentable,imageid,sSerie);
    }
  }

  public PresentationObject getLinkTable(IWContext iwc){
    int iAct = BUILDING;
    IWResourceBundle iwrb = getResourceBundle(iwc);
    if(iwc.getParameter(sAction)!= null){
      iAct = Integer.parseInt(iwc.getParameter(sAction));
    }

    Table LinkTable = new Table();
      LinkTable.setBorder(0);
      //LinkTable.setCellpadding(3);
      //LinkTable.setCellspacing(3);

    Link B1 = new Link(iwrb.getLocalizedString("complex","Complex"));
      B1.setFontStyle("text-decoration: none");
      B1.setFontColor("#FFFFFF");
      B1.setBold();
      B1.addParameter(sAction,COMPLEX);
    Link B2 = new Link(iwrb.getLocalizedString("building","Building"));
      B2.setFontStyle("text-decoration: none");
      B2.setFontColor("#FFFFFF");
      B2.setBold();
      B2.addParameter(sAction,BUILDING);
    Link B3 = new Link(iwrb.getLocalizedString("floor","Floor"));
      B3.setFontStyle("text-decoration: none");
      B3.setFontColor("#FFFFFF");
      B3.setBold();
      B3.addParameter(sAction,FLOOR);
    Link B4 = new Link(iwrb.getLocalizedString("category","Category"));
      B4.setFontStyle("text-decoration: none");
      B4.setFontColor("#FFFFFF");
      B4.setBold();
      B4.addParameter(sAction,CATEGORY);
    Link B5 = new Link(iwrb.getLocalizedString("type","Type"));
      B5.setFontStyle("text-decoration: none");
      B5.setFontColor("#FFFFFF");
      B5.setBold();
      B5.addParameter(sAction,TYPE);
    Link B6 = new Link(iwrb.getLocalizedString("apartment","Apartment"));
      B6.setFontStyle("text-decoration: none");
      B6.setFontColor("#FFFFFF");
      B6.setBold();
      B6.addParameter(sAction,APARTMENT);

    switch (iAct) {
      case COMPLEX  : B1.setFontColor("#FF9933");   break;
      case BUILDING : B2.setFontColor("#FF9933");  break;
      case FLOOR    : B3.setFontColor("#FF9933");     break;
      case APARTMENT: B6.setFontColor("#FF9933"); break;
      case CATEGORY : B4.setFontColor("#FF9933");  break;
      case TYPE     : B5.setFontColor("#FF9933");   break;
    }

    LinkTable.add(B1,1,1);
    LinkTable.add(B2,2,1);
    LinkTable.add(B3,3,1);
    LinkTable.add(B4,4,1);
    LinkTable.add(B5,5,1);
    LinkTable.add(B6,6,1);
    return LinkTable;
  }

  protected PresentationObject makeLinkTable(int i){
    Table headerTable = new Table(1,2);
      headerTable.setCellpadding(0);
      headerTable.setCellspacing(0);
      headerTable.setWidth("100%");
      //headerTable.mergeCells(1,2,2,2);
      headerTable.setAlignment(1,2,"center");
      //headerTable.setAlignment(2,1,"right");

    /*Image idegaweb = iwb.getImage("/shared/idegaweb.gif","idegaWeb");
      headerTable.add(idegaweb,1,1);
    Text tEditor = new Text(iwrb.getLocalizedString("buildingEditor","Building Editor")+"&nbsp;&nbsp;");
      tEditor.setBold();
      tEditor.setFontColor("#FFFFFF");
      tEditor.setFontSize("3");
      headerTable.add(tEditor,2,1);
*/

    String color = includeLinks?"#000000":"#FFFFFF";
    Table LinkTable = new Table();
      LinkTable.setBorder(0);
      LinkTable.setCellpadding(3);
      LinkTable.setCellspacing(3);
      headerTable.add(LinkTable,1,2);

    Link B1 = new Link(iwrb.getLocalizedString("complex","Complex"));
      B1.setFontStyle("text-decoration: none");
      B1.setFontColor(color);
      B1.setBold();
      B1.addParameter(sAction,COMPLEX);
    Link B2 = new Link(iwrb.getLocalizedString("building","Building"));
      B2.setFontStyle("text-decoration: none");
      B2.setFontColor(color);
      B2.setBold();
      B2.addParameter(sAction,BUILDING);
    Link B3 = new Link(iwrb.getLocalizedString("floor","Floor"));
      B3.setFontStyle("text-decoration: none");
      B3.setFontColor(color);
      B3.setBold();
      B3.addParameter(sAction,FLOOR);
    Link B4 = new Link(iwrb.getLocalizedString("category","Category"));
      B4.setFontStyle("text-decoration: none");
      B4.setFontColor(color);
      B4.setBold();
      B4.addParameter(sAction,CATEGORY);
    Link B5 = new Link(iwrb.getLocalizedString("type","Type"));
      B5.setFontStyle("text-decoration: none");
      B5.setFontColor(color);
      B5.setBold();
      B5.addParameter(sAction,TYPE);
    Link B6 = new Link(iwrb.getLocalizedString("apartment","Apartment"));
      B6.setFontStyle("text-decoration: none");
      B6.setFontColor(color);
      B6.setBold();
      B6.addParameter(sAction,APARTMENT);

    switch (i) {
      case COMPLEX  : B1.setFontColor("#FF9933");   break;
      case BUILDING : B2.setFontColor("#FF9933");  break;
      case FLOOR    : B3.setFontColor("#FF9933");     break;
      case APARTMENT: B6.setFontColor("#FF9933"); break;
      case CATEGORY : B4.setFontColor("#FF9933");  break;
      case TYPE     : B5.setFontColor("#FF9933");   break;
    }

    LinkTable.add(B1,1,1);
    LinkTable.add(B2,2,1);
    LinkTable.add(B3,3,1);
    LinkTable.add(B4,4,1);
    LinkTable.add(B5,5,1);
    LinkTable.add(B6,6,1);
    return headerTable;
  }

  private PresentationObject makeTextArea(String sInit){
    TextArea TA = new TextArea("bm_info");
    TA.setContent(sInit);
    TA.setWidth(50);
    TA.setHeight(6);
    setStyle(TA);
    return TA;
  }

  private PresentationObject makeTextArea(String name,String sInit){
    TextArea TA = new TextArea(name);
    TA.setContent(sInit);
    TA.setWidth(50);
    TA.setHeight(6);
    setStyle(TA);
    return TA;
  }

  private PresentationObject makeImageInput(int id,String name){
    PresentationObject imageObject = null;
    ImageInserter imageInsert = null;
    if( id > 1){
      imageInsert = new ImageInserter(id,name);
    }
    else{
      imageInsert = new ImageInserter(name);
    }
    imageInsert.setHasUseBox(false);
    imageInsert.setMaxImageWidth(140);
    imageInsert.setHiddenInputName(name);
    imageInsert.setWindowClassToOpen(SimpleChooserWindow.class);
    //imageInsert.setDefaultImageURL(sMemberImageURL);
    imageObject = imageInsert;
    return imageObject;
  }
  private PresentationObject makeComplexFields(Complex eComplex){
    boolean e = eComplex != null ? true : false;
    String sId = e ? String.valueOf(eComplex.getID()):"";
    String sName = e ? eComplex.getName():"";
    String sInfo = e ? eComplex.getInfo():"";
    int iMapId = e ? eComplex.getImageId(): 1 ;

    Form form = new Form();
    Table Frame = new Table(2,1);
      Frame.setRowVerticalAlignment(1,"top");
      Frame.setCellpadding(0);
      Frame.setCellspacing(0);
      Frame.setColor(2,1,"#EFEFEF");
      Frame.setWidth("100%");
      Frame.setWidth(2,1,"160");
      Frame.setHeight("100%");
    Table T = new Table();
      T.setCellpadding(8);
      T.setAlignment("center");
      T.setWidth("100%");
    Table T2 = new Table(1,2);
      T2.setCellpadding(8);
      T2.setAlignment("center");
      T2.setHeight("100%");
      T2.setWidth("100%");
      T2.setVerticalAlignment(1,1,"top");
      T2.setVerticalAlignment(1,2,"bottom");
      T2.setAlignment(1,2,"center");
    Frame.add(T,1,1);
    Frame.add(T2,2,1);

    TextInput name = new TextInput("bm_name",sName);
    DropdownMenu categories = drpLodgings(new Complex(),"dr_id","Complex",sId);
    HiddenInput HI = new HiddenInput("bm_choice",String.valueOf(COMPLEX));
    HiddenInput HA = new HiddenInput(sAction,String.valueOf(COMPLEX));
    setStyle(name);
    setStyle(categories);
    categories.setToSubmit();
    name.setLength(30);
    T.add(HI);
    T.add(HA);
    T.add(categories,1,1);
    T.add(formatText(iwrb.getLocalizedString("name","Name")),1,2);
    T.add(Text.getBreak(),1,2);
    T.add(name,1,2);
    T.add(formatText(iwrb.getLocalizedString("info","Info")),1,3);
    T.add(Text.getBreak(),1,3);
    T.add( makeTextArea(sInfo),1,3);
    T2.add(formatText(iwrb.getLocalizedString("map","Map")),1,1);
    T2.add(Text.getBreak(),1,1);
    T2.add(this.makeImageInput(iMapId,"mapid"),1,1);
    T2.add(new SubmitButton(iwrb.getImage("save.gif"),"save"),1,2);
    T2.add(new SubmitButton(iwrb.getImage("delete.gif"),prmDelete),1,2);
    form.add(Frame);
    return form;
  }

  private PresentationObject makeBuildingFields(Building eBuilding){
    boolean e = eBuilding != null ? true : false ;
    String sName = e ? eBuilding.getName() : "" ;
    String sInfo = e ? eBuilding.getInfo() : "" ;
    String sAddress = e ? eBuilding.getStreet() : "" ;
    String sId = e ? String.valueOf(eBuilding.getID()): "";
    String sComplexId = e ? String.valueOf(eBuilding.getComplexId()):"";
    String sSerie = e  ? eBuilding.getSerie():"";
    int iPhotoId = e ? eBuilding.getImageId(): 1 ;

    Form form = new Form();
    Table Frame = new Table(2,1);
      Frame.setRowVerticalAlignment(1,"top");
      Frame.setCellpadding(0);
      Frame.setCellspacing(0);
      Frame.setColor(2,1,"#EFEFEF");
      Frame.setWidth("100%");
      Frame.setWidth(1,1,"100%");
      Frame.setHeight("100%");
    Table T = new Table();
      T.setCellpadding(8);
      T.setAlignment("center");
      T.setWidth("100%");
    Table T2 = new Table(1,2);
      T2.setCellpadding(8);
      T2.setAlignment("center");
      T2.setHeight("100%");
      T2.setHeight(2,"100%");
      T2.setWidth("100%");
      T2.setVerticalAlignment(1,1,"top");
      T2.setVerticalAlignment(1,2,"bottom");
      T2.setAlignment(1,2,"center");
    Frame.add(T2,2,1);
    Frame.add(T,1,1);
    TextInput name = new TextInput("bm_name",sName);
    TextInput address = new TextInput("bm_address",sAddress);
    TextInput serie = new TextInput("bm_serie",sSerie);
    HiddenInput HI = new HiddenInput("bm_choice",String.valueOf(BUILDING));
    HiddenInput HA = new HiddenInput(sAction,String.valueOf(BUILDING));
    DropdownMenu complex = drpLodgings(new Complex(),"dr_complex","Complex",sComplexId);
    DropdownMenu houses = drpLodgings(new Building(),"dr_id","Building",sId);
    houses.setToSubmit();
    setStyle(houses);
    setStyle(complex);
    setStyle(name);
    setStyle(address);
    setStyle(serie);
    name.setLength(30);
    address.setLength(30);
    serie.setLength(5);
    serie.setMaxlength(5);
    T.add(houses,1,1);
    T.add(formatText(iwrb.getLocalizedString("name","Name")),1,2);
    T.add(Text.getBreak(),1,2);
    T.add(name,1,2);
    T.add(formatText(iwrb.getLocalizedString("address","Address")),1,3);
    T.add(Text.getBreak(),1,3);
    T.add(address,1,3);
    T.add(formatText(iwrb.getLocalizedString("complex","Complex")),1,4);
    T.add(Text.getBreak(),1,4);
    T.add(complex,1,4);
    T.add(formatText(iwrb.getLocalizedString("serie","Serie")+" "),1,5);
    T.add(serie,1,5);
    T.add(formatText(iwrb.getLocalizedString("info","Info")),1,6);
    T.add(Text.getBreak(),1,6);
    T.add( makeTextArea(sInfo),1,6);

    T2.add(formatText(iwrb.getLocalizedString("photo","Photo")),1,1);
    T2.add(Text.getBreak(),1,1);
    T2.add(this.makeImageInput(iPhotoId,"photoid"),1,1);
    Frame.add(HI);
    T2.add(new SubmitButton(iwrb.getImage("save.gif"),prmSave),1,2);
    T2.add(new SubmitButton(iwrb.getImage("delete.gif"),prmDelete),1,2);
    form.add(Frame);
    return form;
  }
  private PresentationObject makeFloorFields(Floor eFloor){
    boolean e = eFloor != null ? true : false ;
    String sName = e ? eFloor.getName() : "" ;
    String sInfo = e ? eFloor.getInfo() : "" ;
    String sHouse = e ? String.valueOf(eFloor.getBuildingId()) : "" ;
    String sId = e ? String.valueOf(eFloor.getID()):"";
    Form form = new Form();
    Table Frame = new Table(2,1);
      Frame.setRowVerticalAlignment(1,"top");
      Frame.setCellpadding(0);
      Frame.setCellspacing(0);
      Frame.setColor(2,1,"#EFEFEF");
      Frame.setWidth("100%");
      Frame.setWidth(1,1,"100%");
      Frame.setHeight("100%");
    Table T = new Table();
      T.setCellpadding(8);
      T.setAlignment("center");
      T.setWidth("100%");
    Table T2 = new Table(1,2);
      T2.setCellpadding(8);
      T2.setAlignment("center");
      T2.setHeight("100%");
      T2.setHeight(2,"100%");
      T2.setWidth("100%");
      T2.setVerticalAlignment(1,1,"top");
      T2.setVerticalAlignment(1,2,"bottom");
      T2.setAlignment(1,2,"center");
    Frame.add(T2,2,1);
    Frame.add(T,1,1);
    TextInput name = new TextInput("bm_name",sName);
    DropdownMenu floors = this.drpFloors("dr_id","Floor",sId,true);
    floors.setToSubmit();
    DropdownMenu buildings = this.drpLodgings(new Building(),"dr_building","Building",sHouse);
    HiddenInput HI = new HiddenInput("bm_choice",String.valueOf(FLOOR));
    HiddenInput HA = new HiddenInput(sAction,String.valueOf(FLOOR));
    setStyle(name);
    setStyle(floors);
    setStyle(buildings);
    name.setLength(30);

    T.add(floors,1,1);

    T.add(formatText(iwrb.getLocalizedString("name","Name")),1,2);
    T.add(Text.getBreak(),1,2);
    T.add(name,1,2);
    T.add(formatText(iwrb.getLocalizedString("building","Building")),1,3);
    T.add(Text.getBreak(),1,3);
    T.add(buildings,1,3);
    T.add(formatText(iwrb.getLocalizedString("info","Info")),1,4);
    T.add(Text.getBreak(),1,4);
    T.add( makeTextArea(sInfo),1,4);
    T2.add(formatText(iwrb.getLocalizedString("photo","Photo")),1,1);
    T2.add(Text.getBreak(),1,1);
    T2.add(this.makeImageInput(1,"photoid"),1,1);
    Frame.add(HI);
    Frame.add(HA);
    T2.add(new SubmitButton(iwrb.getImage("save.gif"),prmSave),1,2);
    T2.add(new SubmitButton(iwrb.getImage("delete.gif"),prmDelete),1,2);
    form.add(Frame);
    return form;
  }
  private PresentationObject makeCategoryFields(ApartmentCategory eApartmentCategory){
    boolean e = eApartmentCategory != null ? true : false;
    String sName = e ? eApartmentCategory.getName() : "" ;
    String sInfo = e ? eApartmentCategory.getInfo() : "" ;
    String sId = e ? String.valueOf(eApartmentCategory.getID()) : "";
    int iIconId = e ? eApartmentCategory.getImageId(): 1 ;
     Form form = new Form();
    Table Frame = new Table(2,1);
      Frame.setRowVerticalAlignment(1,"top");
      Frame.setCellpadding(0);
      Frame.setCellspacing(0);
      Frame.setColor(2,1,"#EFEFEF");
      Frame.setWidth("100%");
      Frame.setWidth(2,1,"160");
      Frame.setHeight("100%");
    Table T = new Table();
      T.setCellpadding(8);
      T.setAlignment("center");
      T.setWidth("100%");
    Table T2 = new Table(1,2);
      T2.setCellpadding(8);
      T2.setAlignment("center");
      T2.setHeight("100%");
      T2.setWidth("100%");
      T2.setVerticalAlignment(1,1,"top");
      T2.setVerticalAlignment(1,2,"bottom");
      T2.setAlignment(1,2,"center");
    Frame.add(T,1,1);
    Frame.add(T2,2,1);

    String s;
    TextInput name = new TextInput("bm_name",sName);
    DropdownMenu categories = drpLodgings(new ApartmentCategory(),"dr_id","Category",sId);
    categories.setToSubmit();
    HiddenInput HI = new HiddenInput("bm_choice",String.valueOf(CATEGORY));
    HiddenInput HA = new HiddenInput(sAction,String.valueOf(CATEGORY));
    setStyle(name);
    setStyle(categories);
    name.setLength(30);
    T.add(HI);
    T.add(HA);
    T.add(categories,1,1);
    T.add(formatText(iwrb.getLocalizedString("name","Name")),1,2);
    T.add(Text.getBreak(),1,2);
    T.add(name,1,2);
    T.add(formatText(iwrb.getLocalizedString("info","Info")),1,3);
    T.add(Text.getBreak(),1,3);
    T.add( makeTextArea(sInfo),1,3);
    T2.add(formatText(iwrb.getLocalizedString("icon","Icon")),1,1);
    T2.add(Text.getBreak(),1,1);
    T2.add(this.makeImageInput(iIconId,"iconid"),1,1);
    T2.add(new SubmitButton(iwrb.getImage("save.gif"),prmSave),1,2);
    T2.add(new SubmitButton(iwrb.getImage("delete.gif"),prmDelete),1,2);
    form.add(Frame);
    return form;
  }

 private PresentationObject makeTypeFields(ApartmentType eApartmentType,int iPhoto,int iPlan){
    boolean e = eApartmentType != null ? true : false ;
    String sName = e ? eApartmentType.getName() : "" ;
    String sInfo = e ? eApartmentType.getInfo() : "" ;
    String sCategory = e ? String.valueOf(eApartmentType.getApartmentCategoryId()):"";
    String sArea = e ? String.valueOf(eApartmentType.getArea()):"";
    String sRoomCount = e ? String.valueOf(eApartmentType.getRoomCount()):"";
    String sId = e ? String.valueOf(eApartmentType.getID()):"";
    String sExtraInfo = e ? eApartmentType.getExtraInfo() :"";
    String sRent = e ? String.valueOf(eApartmentType.getRent()):"0";

    boolean bKitch = e ? eApartmentType.getKitchen() : false;
    boolean bBath = e ? eApartmentType.getBathRoom() : false;
    boolean bStor = e ? eApartmentType.getStorage() : false;
    boolean bBalc = e ? eApartmentType.getBalcony() : false;
    boolean bStud = e ? eApartmentType.getStudy() : false;
    boolean bLoft = e ? eApartmentType.getLoft() : false;
    boolean bFurniture = e ? eApartmentType.getFurniture() : false;
    int iImageId = e ? eApartmentType.getImageId() : iPhoto;
    int iPlanId = e ? eApartmentType.getFloorPlanId() : iPlan;

    Form form = new Form();

    Table Frame = new Table(2,1);
      Frame.setCellpadding(0);
      Frame.setCellspacing(0);
      Frame.setWidth("100%");
      Frame.setHeight("100%");
      Frame.setWidth(1,1,"100%");
      Frame.setColor(2,1,"#EFEFEF");
      Frame.setRowVerticalAlignment(1,"top");
    Table T = new Table();
      T.setCellpadding(8);
      T.setAlignment("center");
      T.setWidth("100%");
    Table T2 = new Table(1,3);
      T2.setCellpadding(8);
      T2.setWidth("100%");
      T2.setHeight("100%");
      T2.setHeight(3,"100%");
      T2.setAlignment("center");
      T2.setAlignment(1,3,"center");
      T2.setVerticalAlignment(1,3,"bottom");
    Frame.add(T2,2,1);
    Frame.add(T,1,1);
    Table InnerTable = new Table();
      InnerTable.setWidth("100%");
    TextInput name = new TextInput("bm_name",sName);
    DropdownMenu roomcount = drpCount("bm_roomcount","--",sRoomCount,6);
    TextInput area = new TextInput("bm_area",sArea);
    area.setLength(4);
    TextInput rent = new TextInput("bm_rent",sRent);
    rent.setLength(10);
    CheckBox kitch = new CheckBox("bm_kitch","true");
    if(bKitch) kitch.setChecked(true);
    CheckBox bath = new CheckBox("bm_bath","true");
    if(bBath) bath.setChecked(true);
    CheckBox stor = new CheckBox("bm_stor","true");
    if(bStor) stor.setChecked(true);
    CheckBox balc = new CheckBox("bm_balc","true");
    if(bBalc) balc.setChecked(true);
    CheckBox study = new CheckBox("bm_study","true");
    if(bStud) study.setChecked(true);
    CheckBox loft = new CheckBox("bm_loft","true");
    if(bLoft) loft.setChecked(true);
    CheckBox furni = new CheckBox("bm_furni","true");
    if(bFurniture) furni.setChecked(true);
    DropdownMenu apartmenttypes = drpLodgings(new ApartmentType(),"dr_id","Type",sId);
    apartmenttypes.setToSubmit();
    DropdownMenu categories = drpLodgings(new ApartmentCategory(),"bm_category","Category",sCategory);
     HiddenInput HI = new HiddenInput("bm_choice",String.valueOf(TYPE));
    HiddenInput HA = new HiddenInput(sAction,String.valueOf(TYPE));
    name.setLength(30);
    setStyle(name);
    setStyle(area);
    setStyle(rent);
    setStyle(roomcount);
    setStyle2(kitch);
    setStyle2(bath);
    setStyle2(stor);
    setStyle2(balc);
    setStyle2(study);
    setStyle2(loft);
    setStyle2(furni);
    setStyle(apartmenttypes);
    setStyle(categories);
    T.add(HI);
    T.add(HA);

    T.add(formatText(iwrb.getLocalizedString("type","Type")),1,1);
    T.add(Text.getBreak(),1,1);
    T.add(apartmenttypes,1,1);
    T.add(formatText(iwrb.getLocalizedString("name","Name")),1,2);
    T.add(Text.getBreak(),1,2);
    T.add(name,1,2);
    T.add(formatText(iwrb.getLocalizedString("category","Category")+" "),1,3);
    T.add(categories,1,3);
    InnerTable.add(formatText(iwrb.getLocalizedString("room_count","Room count")),1,1);
    InnerTable.add(roomcount,2,1);
    InnerTable.add(formatText(iwrb.getLocalizedString("area","Area(m2)")),3,1);
    InnerTable.add(area,4,1);
    InnerTable.add(formatText(iwrb.getLocalizedString("kitchen","Kitchen")),1,2);
    InnerTable.add(kitch,2,2);
    InnerTable.add(formatText(iwrb.getLocalizedString("bath","Bath")),3,2);
    InnerTable.add(bath,4,2);
    InnerTable.add(formatText(iwrb.getLocalizedString("storage","Storage")),1,3);
    InnerTable.add(stor,2,3);
    InnerTable.add(formatText(iwrb.getLocalizedString("study","Study")),3,3);
    InnerTable.add(study,4,3);
    InnerTable.add(formatText(iwrb.getLocalizedString("loft","Loft")),1,4);
    InnerTable.add(loft,2,4);
    InnerTable.add(formatText(iwrb.getLocalizedString("furniture","Furniture")),3,4);
    InnerTable.add(furni,4,4);
    InnerTable.add(formatText(iwrb.getLocalizedString("balcony","Balcony")),1,5);
    InnerTable.add(balc,2,5);
    InnerTable.add(formatText(iwrb.getLocalizedString("rent","Rent")),1,6);
    InnerTable.add(rent,2,6);
    T.add(InnerTable,1,4);
    T.add(formatText(iwrb.getLocalizedString("info","Info")),1,5);
    T.add(Text.getBreak(),1,5);
    T.add( makeTextArea(sInfo),1,5);
    T.add(formatText(iwrb.getLocalizedString("extra_info","ExtraInfo")),1,6);
    T.add(Text.getBreak(),1,6);
    T.add( makeTextArea("extra_info",sExtraInfo),1,6);
    T2.add(formatText(iwrb.getLocalizedString("photo","Photo")),1,1);
    T2.add(this.makeImageInput(iImageId,"tphotoid"),1,1);
    T2.add(formatText(iwrb.getLocalizedString("plan","Plan")),1,2);
    T2.add(this.makeImageInput(iPlanId,"tplanid"),1,2);
    form.maintainParameter("tphotoid");
    form.maintainParameter("tplanid");
    Frame.add(HI);
    T2.add(new SubmitButton("save","Save"),1,3);
    T2.add(new SubmitButton("del","Delete"),1,3);
    form.add(Frame);
    return form;
  }
  private PresentationObject makeApartmentFields(Apartment eApartment){
    boolean e = eApartment != null ? true : false ;
    String sName = e ? eApartment.getName() : "" ;
    String sInfo = e ? eApartment.getInfo() : "" ;
    String sFloor = e ? String.valueOf(eApartment.getFloorId()) : "";
    String sType = e ? String.valueOf(eApartment.getApartmentTypeId()) : "";
    String sId = e ? String.valueOf( eApartment.getID()) : "" ;
    String sPhotoId = e ? String.valueOf( eApartment.getImageId()) : "";
     String sSerie = e ? eApartment.getSerie():"";
    boolean bRentable = e ? eApartment.getRentable() : false ;
    Form form = new Form();

    Table Frame = new Table(2,1);
      Frame.setRowVerticalAlignment(1,"top");
      Frame.setCellpadding(0);
      Frame.setCellspacing(0);
      Frame.setColor(2,1,"#EFEFEF");
      Frame.setWidth("100%");
      Frame.setWidth(1,1,"100%");
      Frame.setHeight("100%");
    Table T = new Table(1,6);
      T.setCellpadding(8);
      T.setAlignment("center");
      T.setWidth("100%");
    Table T2 = new Table(1,2);
      T2.setCellpadding(8);
      T2.setAlignment("center");
      T2.setHeight("100%");
      T2.setHeight(2,"100%");
      T2.setWidth("100%");
      T2.setVerticalAlignment(1,1,"top");
      T2.setVerticalAlignment(1,2,"bottom");
      T2.setAlignment(1,2,"center");
    Frame.add(T,1,1);
    Frame.add(T2,2,1);

    TextInput name = new TextInput("bm_name",sName);
    TextInput serie = new TextInput("bm_serie",sSerie);
    DropdownMenu apartments = drpLodgings(new Apartment(),"dr_id","Apartment",sId);
    apartments.setToSubmit();
    DropdownMenu types = this.drpLodgings(new ApartmentType(),"bm_type","Type",sType);
    DropdownMenu floors = this.drpFloors("bm_floor","Floor",sFloor,true);
    CheckBox rentable = new CheckBox("bm_rentable","true");
    if(bRentable) rentable.setChecked(true);
    HiddenInput HI = new HiddenInput("bm_choice",String.valueOf(APARTMENT));
    HiddenInput HA = new HiddenInput(sAction,String.valueOf(APARTMENT));
    HiddenInput HID = new HiddenInput("dr_id",sId);

    Window chooserWindow = new Window("b_editor",ApartmentChooser.class,Page.class);
      chooserWindow.setWidth(550);
      chooserWindow.setHeight(500);
      chooserWindow.setResizable(true);
    Link chooser = new Link(iwb.getImage("/shared/list.gif",iwrb.getLocalizedString("select_apartment","Select appartment"),13,13));
		chooser.setWindowToOpen(ApartmentChooserWindow.class);

    form.add(HI);
    setStyle(name);
    setStyle(types);
    setStyle(floors);
    setStyle(serie);
    serie.setLength(5);
    serie.setMaxlength(5);
    name.setLength(30);
    //T.add(apartments,1,2);
    int a = 1;
    T.add(formatText(iwrb.getLocalizedString("name","Name")),1,1);
    T.add(Text.getBreak(),1,1);
    T.add(name,1,1);
    T.add(formatText("&nbsp;&nbsp;"));
    T.add(chooser,1,1);
    T.add(formatText( iwrb.getLocalizedString("floor","Floor")),1,2);
    T.add(Text.getBreak(),1,2);
    T.add(floors,1,2);
    T.add(formatText(iwrb.getLocalizedString("type","Type")),1,3);
    T.add(Text.getBreak(),1,3);
    T.add(types,1,3);
    T.add(formatText(iwrb.getLocalizedString("serie","Serie")+" "),1,4);
    T.add(serie,1,4);
    T.add(formatText(iwrb.getLocalizedString("rentable","Rentable")+" "),1,5);
    T.add(rentable,1,5);
    T.add(formatText(iwrb.getLocalizedString("info","Info")),1,6);
    T.add(Text.getBreak(),1,6);
    T.add( makeTextArea(sInfo),1,6);
    T2.add(formatText(iwrb.getLocalizedString("photo","Photo:")),1,1);
    T2.add(this.makeImageInput(1,"photoid"),1,1);
    form.add(HI);
    form.add(HA);
    if(e)
      form.add(HID);
    T2.add(new SubmitButton(iwrb.getImage("save.gif"),prmSave),1,2);
    T2.add(new SubmitButton(iwrb.getImage("delete.gif"),prmDelete),1,2);
    form.add(Frame);
    return form;
  }

  private PresentationObject getApartments(){
    int border = 0;
    int padding = 6;
    int spacing = 1;

  try{

    Complex[] C = (Complex[])(new Complex()).findAll();
    int clen = C.length;
    int c = 1, b = 1, f = 1, a = 1;

    Table T = new Table();
    T.setRowVerticalAlignment(1,"top");
    T.setCellpadding(padding);
    T.setCellspacing(spacing);
    T.setVerticalZebraColored("#942829","#21304a");
    T.setBorder(border);
    for (int i = 0; i < clen; i++) {
      T.add(getHeaderText( C[i].getName()),i+1,1);
      Building[] B = (Building[])(new Building()).findAllByColumnOrdered(Building.getComplexIdColumnName(),String.valueOf(C[i].getID()),Building.getNameColumnName());
      int blen = B.length;
      Table BuildingTable = new Table();
      BuildingTable.setCellpadding(padding);
      BuildingTable.setCellspacing(spacing);
      BuildingTable.setBorder(border);
      T.add(BuildingTable,i+1,2);
      b = 1;
      for (int j = 0; j < blen; j++) {
        BuildingTable.add(getHeaderText( B[j].getName()),1,b++);
        Floor[] F = (Floor[])(new Floor()).findAllByColumnOrdered(Floor.getBuildingIdColumnName(),String.valueOf(B[j].getID()),Floor.getNameColumnName());
        int flen = F.length;
        Table FloorTable = new Table();
        FloorTable.setCellpadding(padding);
        FloorTable.setCellspacing(spacing);
        FloorTable.setBorder(border);
        BuildingTable.add(FloorTable,1,b++);
        f = 1;
        for (int k = 0; k < flen; k++) {
          FloorTable.add(getHeaderText(F[k].getName()),1,f++);
          Apartment[] A = (Apartment[])(new Apartment()).findAllByColumnOrdered(Apartment.getFloorIdColumnName(),String.valueOf(F[k].getID()),Apartment.getNameColumnName());
          int alen = A.length;
          if(alen > 0){
            Table ApartmentTable = new Table();
            ApartmentTable.setCellpadding(padding);
            ApartmentTable.setBorder(border);
            ApartmentTable.setCellspacing(spacing);
            FloorTable.add(ApartmentTable,1,f++);
            for (int l = 0; l < alen; l++) {
              ApartmentTable.add(getApLink(A[l].getID(),A[l].getName()),1,l+1);
            }
          }

        }

      }

    }
      T.setRowVerticalAlignment(2,"top");
      T.setVerticalZebraColored("#942829","#21304a");
       return T;
    }
    catch(SQLException sql){
      return new Text();
    }

  }

  private PresentationObject getTypes(){
    try {
      ApartmentType[] AT = (ApartmentType[])(new ApartmentType()).findAllOrdered(ApartmentType.getNameColumnName());
      int len = AT.length;
      Table T = new Table();
      if(len > 0){
        T = new Table(10,len+1);
        T.setCellpadding(4);
        T.setCellspacing(2);

        int row = 1,col = 1;
        T.add(getHeaderText(iwrb.getLocalizedString("name","Name")),col++,row);
        T.add(getHeaderText(iwrb.getLocalizedString("area","Area(m2)")),col++,row);
        T.add(getHeaderText(iwrb.getLocalizedString("rooms","Rooms")),col++,row);
        T.add(getHeaderText(iwrb.getLocalizedString("kitchen","Kitchen")),col++,row);
        T.add(getHeaderText(iwrb.getLocalizedString("bath","Bath")),col++,row);
        T.add(getHeaderText(iwrb.getLocalizedString("storage","Storage")),col++,row);
        T.add(getHeaderText(iwrb.getLocalizedString("study","Study")),col++,row);
        T.add(getHeaderText(iwrb.getLocalizedString("loft","Loft")),col++,row);
        T.add(getHeaderText(iwrb.getLocalizedString("furniture","Furniture")),col++,row);
        T.add(getHeaderText(iwrb.getLocalizedString("balcony","Balcony")),col++,row);
        T.setColumnAlignment(3,"center");
        T.setColumnAlignment(4,"center");
        T.setColumnAlignment(5,"center");
        T.setColumnAlignment(6,"center");
        T.setColumnAlignment(7,"center");
        T.setColumnAlignment(8,"center");
        T.setColumnAlignment(9,"center");
        T.setColumnAlignment(10,"center");

        for (int i = 0; i < len; i++) {
          row = i+2;
          col = 1;
          T.add(getATLink(AT[i].getID(),AT[i].getName()),col++,row);
          T.add(getBodyText(String.valueOf(AT[i].getArea())),col++,row);
          T.add(getBodyText(AT[i].getRoomCount()),col++,row);
          T.add(getBodyText(AT[i].getKitchen()?"X":"N"),col++,row);
          T.add(getBodyText(AT[i].getBathRoom()?"X":"N"),col++,row);
          T.add(getBodyText(AT[i].getStorage()?"X":"N"),col++,row);
          T.add(getBodyText(AT[i].getStudy()?"X":"N"),col++,row);
          T.add(getBodyText(AT[i].getLoft()?"X":"N"),col++,row);
          T.add(getBodyText(AT[i].getFurniture()?"X":"N"),col++,row);
          T.add(getBodyText(AT[i].getBalcony()?"X":"N"),col++,row);
        }
        T.setBorder(0);
        T.setVerticalZebraColored("#942829","#21304a");

      }
       return T;
    }
    catch (SQLException ex) {
      return new Text();
    }
  }

  private Text getHeaderText(int i){
    return getHeaderText(String.valueOf(i));
  }

  private Text getHeaderText(String s){
    Text T = new Text(s);
    T.setBold();
    T.setFontColor("#FFFFFF");
    return T;
  }

   private Text getBodyText(int i){
    return getHeaderText(String.valueOf(i));
  }

  private Text getBodyText(String s){
    Text T = new Text(s);
    T.setFontColor("#FFFFFF");
    return T;
  }

  private Link getATLink(int id,String name){
    Link L = new Link(name);
    L.setFontColor("#FFFFFF");
    L.addParameter("dr_id",id);
    L.addParameter(sAction,TYPE);
    L.addParameter("bm_choice",TYPE);
    return L;
  }

  private Link getApLink(int id,String name){
    Link L = new Link(name);
    L.setFontColor("#FFFFFF");
    L.addParameter("dr_id",id);
    L.addParameter(sAction,APARTMENT);
    L.addParameter("bm_choice",APARTMENT);
    return L;
  }

/*
  private DropdownMenu drpZip(String name,String display,String selected,boolean withCity){
    ZipCode[] zips = new ZipCode[1];
    try{
      zips = (ZipCode[]) (new ZipCode()).findAllOrdered("code");
    }
    catch(Exception e){}
    DropdownMenu drp = new DropdownMenu(name);
    drp.addDisabledMenuElement("0",display);
    for(int i = 0; i < zips.length ; i++){
      if(withCity)
        drp.addMenuElement(zips[i].getID(),zips[i].getCode()+" "+zips[i].getCity());
      else
        drp.addMenuElement(zips[i].getID(),zips[i].getCode());
    }
    if(!selected.equalsIgnoreCase("")){
      drp.setSelectedElement(selected);
    }
    return drp;
  }
*/

  private DropdownMenu drpFloors(String name,String display,String selected,boolean withBuildingName) {
    Floor[] lods = new Floor[1];
    try{
      lods =  (Floor[])(new Floor()).findAll();
    }
    catch(SQLException e){}
    DropdownMenu drp = new DropdownMenu(name);
    drp.addDisabledMenuElement("0",display);
    for(int i = 0; i < lods.length ; i++){
      if(withBuildingName){
        try{
        drp.addMenuElement(lods[i].getID() ,lods[i].getName()+" "+(new Building(lods[i].getBuildingId()).getName()));
        }
        catch(SQLException e){}}
      else
        drp.addMenuElement(lods[i].getID() ,lods[i].getName());
    }
    if(!selected.equalsIgnoreCase("")){
      drp.setSelectedElement(selected);
    }
    return drp;
  }

  private DropdownMenu drpCount(String name,String display,String selected,int len) {
    DropdownMenu drp = new DropdownMenu(name);
    drp.addDisabledMenuElement("0",display);
    for(int i = 1; i < len+1 ; i++){
      drp.addMenuElement(String.valueOf(i));
    }
    if(!selected.equalsIgnoreCase("")){
      drp.setSelectedElement(selected);
    }
    return drp;
  }

  private DropdownMenu drpLodgings(GenericEntity lodgings,String name,String display,String selected) {
    GenericEntity[] lods = new GenericEntity[0];
    try{
      lods =  (lodgings).findAll();
    }
    catch(SQLException e){}
    DropdownMenu drp = new DropdownMenu(name);
    drp.addDisabledMenuElement("0",display);
    int len = lods.length;
    if(len > 0){
      for(int i = 0; i < len ; i++){
        drp.addMenuElement(lods[i].getID(),lods[i].getName());
      }
      if(!selected.equalsIgnoreCase("")){
        drp.setSelectedElement(selected);
      }
    }
    return drp;
  }

  public Text formatText(String s){
    Text T= new Text();
    if(s!=null){
      T= new Text(s);
      //if(this.fontBold)
      T.setBold();
      T.setFontColor(this.TextFontColor);
      T.setFontSize(this.fontSize);
      T.setFontFace(Text.FONT_FACE_VERDANA);
    }
    return T;
  }

  public Text formatText(int i){
    return formatText(String.valueOf(i));
  }

  public void main(IWContext iwc)  {
    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);

    isAdmin = iwc.hasEditPermission(this);
    this.getParentPage().setName("b_editor");
    this.getParentPage().setTitle(iwrb.getLocalizedString("buildingEditor","Building Editor"));
    this.getParentPage().setAllMargins(0);

    /** @todo: fixa Admin*/
    control(iwc);
  }
  protected void setStyle(InterfaceObject O){
    O.setAttribute("style",this.styleAttribute);
  }
  protected void setStyle2(InterfaceObject O){
    O.setAttribute("style",this.styleAttribute2);
  }
}// class BuildingEditor
