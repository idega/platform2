package com.idega.block.building.presentation;

import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.*;
import com.idega.block.building.data.*;
import java.sql.SQLException;
import java.io.IOException;
import java.util.StringTokenizer;
import com.oreilly.servlet.*;
import com.idega.data.GenericEntity;
import com.idega.jmodule.object.Editor;
import com.idega.jmodule.image.presentation.ImageInserter;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class BuildingEditor extends com.idega.jmodule.object.ModuleObjectContainer{

  protected boolean isAdmin = false;
  protected String TextFontColor = "#000000";
  public String sAction = "be_action";
  private String styleAttribute = "font-family:arial; font-size:8pt; color:#000000; text-align: justify; border: 1 solid #000000";
  private String styleAttribute2 = "font-family:arial; font-size:8pt; color:#000000; text-align: justify;";
  public final int COMPLEX = 1,BUILDING = 2,FLOOR = 3, APARTMENT = 4,
                    CATEGORY = 5,TYPE = 6;
  private int eId = 0;
  protected int fontSize = 1;
  protected boolean fontBold = false;
  private Table outerTable;

  protected void control(ModuleInfo modinfo){
    outerTable = new Table(1,2);
      outerTable.setCellpadding(0);
      outerTable.setCellspacing(0);
      outerTable.setWidth("100%");
      outerTable.setHeight("100%");
      outerTable.setHeight(2,"100%");
      outerTable.setColor(1,1,"#0E2456");

    int iAction = BUILDING;
    if(modinfo.getParameter(sAction)!= null){
      iAction = Integer.parseInt(modinfo.getParameter(sAction));
    }

    if(modinfo.getParameter("dr_id")!=null){
       eId = Integer.parseInt(modinfo.getParameter("dr_id"));
    }
    else if ( (String) modinfo.getSessionAttribute("dr_id") != null ) {
       eId = Integer.parseInt((String) modinfo.getSessionAttribute("dr_id"));
       modinfo.removeSessionAttribute("dr_id");
    }

    if(modinfo.getParameter("save")!=null){
      if(modinfo.getParameter("bm_choice")!=null){
        int i = Integer.parseInt(modinfo.getParameter("bm_choice"));
         switch (i) {
            case COMPLEX  : storeComplex(modinfo);   break;
            case BUILDING : storeBuilding(modinfo);  break;
            case FLOOR    : storeFloor(modinfo);     break;
            case APARTMENT: storeApartment(modinfo); break;
            case CATEGORY : storeApartmentCategory(modinfo);  break;
            case TYPE     : storeApartmentType(modinfo);   break;
        }
      }
    }
    else if(modinfo.getParameter("del")!=null){
       if(modinfo.getParameter("bm_choice")!=null && eId > 0){
        int i = Integer.parseInt(modinfo.getParameter("bm_choice"));
         switch (i) {
            case COMPLEX  : deleteComplex(eId);   break;
            case BUILDING : deleteBuilding(eId);  break;
            case FLOOR    : deleteFloor(eId);     break;
            case APARTMENT: deleteApartment(eId); break;
            case CATEGORY : deleteApartmentCategory(eId);  break;
            case TYPE     : deleteApartmentType(eId);   break;
        }
        eId = 0;
      }
    }
    outerTable.add(makeLinkTable(iAction),1,1);
    switch (iAction) {
      case COMPLEX  : doComplex(modinfo);   break;
      case BUILDING : doBuilding(modinfo);  break;
      case FLOOR    : doFloor(modinfo);     break;
      case APARTMENT: doApartment(modinfo); break;
      case CATEGORY : doCategory(modinfo);  break;
      case TYPE     : doType(modinfo);   break;
    }
    add(outerTable);
  }
  private void doMain(ModuleInfo modinfo,boolean ifMulti,int choice) {
    doBuilding(modinfo);

  }
  private void doComplex(ModuleInfo modinfo){
    try{
      Complex eComplex = eId > 0 ? new Complex(eId) : null;
      outerTable.add(makeComplexFields(eComplex),1,2);
    }
    catch(SQLException sql){}
  }
  private void doBuilding(ModuleInfo modinfo){
    try{
      Building eBuilding = eId > 0 ? new Building(eId) : null;
      outerTable.add(makeBuildingFields(eBuilding),1,2);
    }
    catch(SQLException sql){}
  }
  private void doFloor(ModuleInfo modinfo){
    try{
      Floor eFloor = eId > 0 ? new Floor(eId) : null;
      outerTable.add(makeFloorFields(eFloor),1,2);
    }
    catch(SQLException sql){}
  }
  private void doApartment(ModuleInfo modinfo){
    try{
      Apartment eApartment = eId > 0 ? new Apartment(eId) : null;
      outerTable.add(makeApartmentFields(eApartment),1,2);

    }
    catch(SQLException sql){}
    //add(getApartments());
  }
  private void doType(ModuleInfo modinfo){
    // Dirty job below
    int iPhotoId = 1, iPlanId = 1;
    boolean b1 = false,b2 = false;
    if(modinfo.getSessionAttribute("tphotoid2") != null){
      b1 = true;
      iPhotoId = Integer.parseInt((String)modinfo.getSessionAttribute("tphotoid2"));
    }
    if(modinfo.getSessionAttribute("tplanid2")!=null){
      b2 = true;
      iPlanId = Integer.parseInt((String)modinfo.getSessionAttribute("tplanid2"));
    }
    if(b1 && b2){
      modinfo.removeSessionAttribute("tphotoid2");
      modinfo.removeSessionAttribute("tplanid2");
    }
    try{
      ApartmentType eApartmentType = eId > 0 ? new ApartmentType(eId) : null;
      outerTable.add(makeTypeFields(eApartmentType,iPhotoId,iPlanId),1,2);
    }
    catch(SQLException sql){sql.printStackTrace();}
    //add(getTypes());
  }
  private void doCategory(ModuleInfo modinfo){
    try{
      ApartmentCategory eApartmentCategory = eId > 0 ? new ApartmentCategory(eId) : null;
      outerTable.add(makeCategoryFields(eApartmentCategory),1,2);
    }
    catch(SQLException sql){}
  }

  private void deleteComplex(int id){
    try{
      Complex C = new Complex(id);
      C.delete();
    }
    catch(SQLException sql){}
  }
  private void deleteBuilding(int id){
    try {
      Building B = new Building(id);
      B.delete();
    }
    catch (SQLException ex) {
    }
  }
  private void deleteFloor(int id){
    try {
      Floor F = new Floor(id);
      F.delete();
    }
    catch (SQLException ex) {

    }
  }
  private void deleteApartment(int id){
    try {
      Apartment A = new Apartment(id);
      A.delete();
    }
    catch (SQLException ex) {
    }
  }
  private void deleteApartmentCategory(int id){
    try {
      ApartmentCategory C = new ApartmentCategory(id);
      C.delete();
    }
    catch (SQLException ex) {
    }
  }
  private void deleteApartmentType(int id){
    try {
      ApartmentType T = new ApartmentType(id);
      T.delete();
    }
    catch (SQLException ex) {

    }
  }

  private void doQuit(ModuleInfo modinfo) throws SQLException{  }
  private void doSave(ModuleInfo modinfo) throws SQLException{  }

  private void storeComplex(ModuleInfo modinfo){
    String sName = modinfo.getParameter("bm_name").trim();
    String sInfo = modinfo.getParameter("bm_info").trim();
    String sId = modinfo.getParameter("dr_id");
    int id = -1;
    try {
      id = Integer.parseInt(sId);
    }
    catch (Exception ex) {
      id = -1;
    }
    Complex eComplex = new Complex();
    boolean update = false;
    try{
      if(id > 0){
        eComplex = new Complex(id);
        update = true;
      }
      eComplex.setName(sName);
      eComplex.setInfo(sInfo);
      if(update)
        eComplex.update();
      else
        eComplex.insert();
    }
    catch(SQLException e){e.printStackTrace();}
  }

  private void storeBuilding(ModuleInfo modinfo){
    String sName = modinfo.getParameter("bm_name").trim();
    String sInfo = modinfo.getParameter("bm_info").trim();
    String sAddress = modinfo.getParameter("bm_address").trim();
    String sImageId = modinfo.getParameter("photoid");
    String sComplexId = modinfo.getParameter("dr_complex");
    String sId = modinfo.getParameter("dr_id");
    String sSerie = modinfo.getParameter("bm_serie");
    int imageid = 1,iSerie = 0;
    int id = -1;
    int complexid = -1;
    try {  id = Integer.parseInt(sId);  }
    catch (NumberFormatException ex) { id = -1;  }
    try {  complexid = Integer.parseInt(sComplexId);  }
    catch (NumberFormatException ex) { complexid = -1;  }
    try {  imageid = Integer.parseInt(sImageId); }
    catch (NumberFormatException ex) { imageid = 1;  }

    Building ebuilding = new Building();
    boolean update = false;
    try{
      if(complexid > 0){
        if(id > 0 ){
          ebuilding = new Building(id);
          update = true;
        }
        ebuilding.setName(sName);
        ebuilding.setStreet(sAddress);
        ebuilding.setInfo(sInfo);
        ebuilding.setImageId(imageid);
        ebuilding.setComplexId(complexid);
        ebuilding.setSerie(sSerie);
        if(update)
          ebuilding.update();
        else
          ebuilding.insert();
      }
    }
    catch(SQLException e){}
  }
  private void storeFloor(ModuleInfo modinfo){

    String sName = modinfo.getParameter("bm_name").trim();
    String sInfo = modinfo.getParameter("bm_info").trim();
    String sImageId = modinfo.getParameter("photoid");
    String sBuildingId = modinfo.getParameter("dr_building");
    String sId = modinfo.getParameter("dr_id");
    int imageid = 1;
    int id = -1;
    int buildingid = -1;
    try {  id = Integer.parseInt(sId);  }
    catch (NumberFormatException ex) { id = -1;  }
    try {  buildingid = Integer.parseInt(sBuildingId);  }
    catch (NumberFormatException ex) { buildingid = -1;  }
    try {  imageid = Integer.parseInt(sImageId);  }
    catch (NumberFormatException ex) { imageid = 1;  }
    try{

      Floor efloor = new Floor();
      boolean update = false;
      if(id > 0){
        efloor = new Floor(id);
        update = true;
      }
      efloor.setName(sName);
      efloor.setBuildingId(buildingid);
      efloor.setInfo(sInfo);
      efloor.setImageId(imageid);
      if(update)
        efloor.update();
      else
        efloor.insert();
    }
    catch(SQLException e){e.printStackTrace();}

  }
  private void storeApartmentCategory(ModuleInfo modinfo){
    String sName = modinfo.getParameter("bm_name").trim();
    String sInfo = modinfo.getParameter("bm_info").trim();
    String sId = modinfo.getParameter("dr_id");

    int id = -1;
    try {  id = Integer.parseInt(sId);  }
    catch (NumberFormatException ex) { id = -1;  }

    try{
      boolean update = false;
      ApartmentCategory eACategory = new ApartmentCategory();
      if(id >0){
        eACategory = new ApartmentCategory(id);
        update = true;
      }
      eACategory.setName(sName);
      eACategory.setInfo(sInfo);
      if(update)
        eACategory.update();
      else
        eACategory.insert();
    }
    catch(SQLException e){}

  }
  private void storeApartmentType(ModuleInfo modinfo){

    String sName = modinfo.getParameter("bm_name").trim();
    String sInfo = modinfo.getParameter("bm_info").trim();
    String sExtraInfo = modinfo.getParameter("extra_info").trim();
    String sId = modinfo.getParameter("dr_id");
    String sRoomCount = modinfo.getParameter("bm_roomcount");
    String sCategoryId = modinfo.getParameter("bm_category");
    String sImageId = modinfo.getParameter("tphotoid");
    String sPlanId = modinfo.getParameter("tplanid");
    String sArea = modinfo.getParameter("bm_area").trim();
    String sKitch = modinfo.getParameter("bm_kitch");
    String sBath = modinfo.getParameter("bm_bath");
    String sStorage = modinfo.getParameter("bm_stor");
    String sBalcony = modinfo.getParameter("bm_balc");
    String sStudy = modinfo.getParameter("bm_study");
    String sLoft = modinfo.getParameter("bm_loft");
    String sFurniture = modinfo.getParameter("bm_furni");
    String sRent = modinfo.getParameter("bm_rent");

    int planid = 1;
    int imageid = 1;
    int id = -1;
    int categoryid = -1;
    int rent = 0;
    try { id = Integer.parseInt(sId);  }
    catch (NumberFormatException ex) { id = -1;  }
    try { categoryid = Integer.parseInt(sCategoryId);  }
    catch (NumberFormatException ex) { categoryid = -1;  }
    try { imageid = Integer.parseInt(sImageId);  }
    catch (NumberFormatException ex) { imageid = 1;  }
    try{  planid = Integer.parseInt(sPlanId); }
    catch (NumberFormatException ex) { planid = 1;  }
     try{  rent = Integer.parseInt(sRent); }
    catch (NumberFormatException ex) { rent = 1;  }

    try{
      if(categoryid > 0){
        ApartmentType etype = new ApartmentType();
        boolean update = false;
        if(id > 0){
          etype = new ApartmentType(id);
          update = true;
        }
        etype.setName(sName);
        etype.setInfo(sInfo);
        etype.setExtraInfo(sExtraInfo);
        etype.setFloorPlanId(planid);
        etype.setImageId(imageid);
        etype.setApartmentCategoryId(categoryid);
        etype.setArea(Float.parseFloat(sArea));
        etype.setRoomCount(Integer.parseInt(sRoomCount));
        etype.setRent(rent);

        etype.setBalcony(sBalcony!=null?true:false);
        etype.setBathRoom(sBath!=null?true:false);
        etype.setKitchen(sKitch !=null?true:false);
        etype.setLoft(sLoft != null?true:false);
        etype.setStorage(sStorage !=null?true:false);
        etype.setStudy(sStudy != null?true:false);
        etype.setFurniture(sFurniture!= null?true:false);

        if(update){
          etype.update();
        }
        else{
          etype.insert();
        }
      }
      else
        add("Vantar Flokk");
      }
      catch(SQLException e){e.printStackTrace();}
      catch(Exception e){e.printStackTrace();}


  }
  private void storeApartment(ModuleInfo modinfo){

    String sName = modinfo.getParameter("bm_name").trim();
    String sInfo = modinfo.getParameter("bm_info").trim();
    String sId = modinfo.getParameter("dr_id");
    String sType = modinfo.getParameter("bm_type");
    String sFloorId = modinfo.getParameter("bm_floor");
    String sRentable = modinfo.getParameter("bm_rentable");
    String sImageId = modinfo.getParameter("photoid");
    String sSerie = modinfo.getParameter("bm_serie");
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

    try{
      String slname = sName;
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
            Apartment apartment = new Apartment();
            apartment.setName(String.valueOf(i));
            apartment.setFloorId( floorid);
            apartment.setApartmentTypeId(typeid);
            apartment.setInfo(sInfo);
            apartment.setRentable(bRentable);
            apartment.setImageId(imageid);
            apartment.setSerie(sSerie);
            try{
              apartment.insert();
            }
            catch(SQLException sql){sql.printStackTrace();}
          }
        }
      }
      else if(count2 > 0  ){
        for (int i = 0; i < count2; i++) {
          Apartment apartment = new Apartment();
          apartment.setName(st2.nextToken());
          apartment.setFloorId( floorid);
          apartment.setApartmentTypeId(typeid);
          apartment.setInfo(sInfo);
          apartment.setRentable(bRentable);
          apartment.setImageId(imageid);
          apartment.setSerie(sSerie);
          try{
            apartment.insert();
          }
          catch(SQLException sql){sql.printStackTrace();}
        }
      }
      else{
        Apartment apartment = new Apartment();
        boolean update = false;
        if(id >0){
          apartment = new Apartment(id);
          update = true;
        }
        apartment.setName(sName);
        apartment.setFloorId( floorid);
        apartment.setApartmentTypeId(typeid);
        apartment.setInfo(sInfo);
        if(sRentable !=null)
          apartment.setRentable(true);
        apartment.setImageId(imageid);
        if(update)
          apartment.update();
        else{
          apartment.insert();
        }
      }
    }
    catch(SQLException sql){sql.printStackTrace();}
  }

  protected ModuleObject makeLinkTable(int i){
    Table headerTable = new Table(2,2);
      headerTable.setCellpadding(0);
      headerTable.setCellspacing(0);
      headerTable.setWidth("100%");
      headerTable.mergeCells(1,2,2,2);
      headerTable.setAlignment(1,2,"center");
      headerTable.setAlignment(2,1,"right");

    Image idegaweb = new Image("/pics/idegaweb.gif","idegaWeb");
      headerTable.add(idegaweb,1,1);
    Text tEditor = new Text("Building Editor&nbsp;&nbsp;");
      tEditor.setBold();
      tEditor.setFontColor("#FFFFFF");
      tEditor.setFontSize("3");
      headerTable.add(tEditor,2,1);

    Table LinkTable = new Table();
      LinkTable.setBorder(0);
      LinkTable.setCellpadding(3);
      LinkTable.setCellspacing(3);
      headerTable.add(LinkTable,1,2);

    Link B1 = new Link("Garður");
      B1.setFontStyle("text-decoration: none");
      B1.setFontColor("#FFFFFF");
      B1.setBold();
      B1.addParameter(sAction,COMPLEX);
    Link B2 = new Link("Hús");
      B2.setFontStyle("text-decoration: none");
      B2.setFontColor("#FFFFFF");
      B2.setBold();
      B2.addParameter(sAction,BUILDING);
    Link B3 = new Link("Hæð");
      B3.setFontStyle("text-decoration: none");
      B3.setFontColor("#FFFFFF");
      B3.setBold();
      B3.addParameter(sAction,FLOOR);
    Link B4 = new Link("Flokkur");
      B4.setFontStyle("text-decoration: none");
      B4.setFontColor("#FFFFFF");
      B4.setBold();
      B4.addParameter(sAction,CATEGORY);
    Link B5 = new Link("Gerð");
      B5.setFontStyle("text-decoration: none");
      B5.setFontColor("#FFFFFF");
      B5.setBold();
      B5.addParameter(sAction,TYPE);
    Link B6 = new Link("Íbúð");
      B6.setFontStyle("text-decoration: none");
      B6.setFontColor("#FFFFFF");
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

  private ModuleObject makeTextArea(String sInit){
    TextArea TA = new TextArea("bm_info");
    TA.setContent(sInit);
    TA.setWidth(50);
    TA.setHeight(6);
    setStyle(TA);
    return TA;
  }

  private ModuleObject makeTextArea(String name,String sInit){
    TextArea TA = new TextArea(name);
    TA.setContent(sInit);
    TA.setWidth(50);
    TA.setHeight(6);
    setStyle(TA);
    return TA;
  }

  private ModuleObject makeImageInput(int id,String name){
    ModuleObject imageObject = null;
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
    imageInsert.setAdminURL("/image/imagesaver.jsp");
    //imageInsert.setDefaultImageURL(sMemberImageURL);
    imageObject = imageInsert;
    return imageObject;
  }
  private ModuleObject makeComplexFields(Complex eComplex){
    boolean e = eComplex != null ? true : false;
    String sId = e ? String.valueOf(eComplex.getID()):"";
    String sName = e ? eComplex.getName():"";
    String sInfo = e ? eComplex.getInfo():"";

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
    Table T2 = new Table(1,1);
      T2.setCellpadding(8);
      T2.setAlignment("center");
      T2.setHeight("100%");
      T2.setWidth("100%");
      T2.setVerticalAlignment(1,1,"bottom");
      T2.setAlignment(1,1,"center");
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
    T.add(formatText("Name:"),1,2);
    T.add(Text.getBreak(),1,2);
    T.add(name,1,2);
    T.add(formatText("Info:"),1,3);
    T.add(Text.getBreak(),1,3);
    T.add( makeTextArea(sInfo),1,3);
    T2.add(new SubmitButton("save","Save"),1,1);
    T2.add(new SubmitButton("del","Delete"),1,1);
    form.add(Frame);
    return form;
  }

  private ModuleObject makeBuildingFields(Building eBuilding){
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
    T.add(formatText("Name:"),1,2);
    T.add(Text.getBreak(),1,2);
    T.add(name,1,2);
    T.add(formatText("Address:"),1,3);
    T.add(Text.getBreak(),1,3);
    T.add(address,1,3);
    T.add(formatText("Complex:"),1,4);
    T.add(Text.getBreak(),1,4);
    T.add(complex,1,4);
    T.add(formatText("Serie: "),1,5);
    T.add(serie,1,5);
    T.add(formatText("Info:"),1,6);
    T.add(Text.getBreak(),1,6);
    T.add( makeTextArea(sInfo),1,6);

    T2.add(formatText("Photo:"),1,1);
    T2.add(Text.getBreak(),1,1);
    T2.add(this.makeImageInput(iPhotoId,"photoid"),1,1);
    Frame.add(HI);
    T2.add(new SubmitButton("save","Save"),1,2);
    T2.add(new SubmitButton("del","Delete"),1,2);
    form.add(Frame);
    return form;
  }
  private ModuleObject makeFloorFields(Floor eFloor){
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

    T.add(formatText("Name:"),1,2);
    T.add(Text.getBreak(),1,2);
    T.add(name,1,2);
    T.add(formatText("Building:"),1,3);
    T.add(Text.getBreak(),1,3);
    T.add(buildings,1,3);
    T.add(formatText("Info:"),1,4);
    T.add(Text.getBreak(),1,4);
    T.add( makeTextArea(sInfo),1,4);
    T2.add(formatText("Photo:"),1,1);
    T2.add(Text.getBreak(),1,1);
    T2.add(this.makeImageInput(1,"photoid"),1,1);
    Frame.add(HI);
    Frame.add(HA);
    T2.add(new SubmitButton("save","Save"),1,2);
    T2.add(new SubmitButton("del","Delete"),1,2);
    form.add(Frame);
    return form;
  }
  private ModuleObject makeCategoryFields(ApartmentCategory eApartmentCategory){
    boolean e = eApartmentCategory != null ? true : false;
    String sName = e ? eApartmentCategory.getName() : "" ;
    String sInfo = e ? eApartmentCategory.getInfo() : "" ;
    String sId = e ? String.valueOf(eApartmentCategory.getID()) : "";
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
    Table T2 = new Table(1,1);
      T2.setCellpadding(8);
      T2.setAlignment("center");
      T2.setHeight("100%");
      T2.setWidth("100%");
      T2.setVerticalAlignment(1,1,"bottom");
      T2.setAlignment(1,1,"center");
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
    T.add(formatText("Name:"),1,2);
    T.add(Text.getBreak(),1,2);
    T.add(name,1,2);
    T.add(formatText("Info:"),1,3);
    T.add(Text.getBreak(),1,3);
    T.add( makeTextArea(sInfo),1,3);
    T2.add(new SubmitButton("save","Save"),1,1);
    T2.add(new SubmitButton("del","Delete"),1,1);
    form.add(Frame);
    return form;
  }

 private ModuleObject makeTypeFields(ApartmentType eApartmentType,int iPhoto,int iPlan){
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

    T.add(formatText("Type:"),1,1);
    T.add(Text.getBreak(),1,1);
    T.add(apartmenttypes,1,1);
    T.add(formatText("Name:"),1,2);
    T.add(Text.getBreak(),1,2);
    T.add(name,1,2);
    T.add(formatText("Category: "),1,3);
    T.add(categories,1,3);
    InnerTable.add(formatText("Room count"),1,1);
    InnerTable.add(roomcount,2,1);
    InnerTable.add(formatText("Area(m2)"),3,1);
    InnerTable.add(area,4,1);
    InnerTable.add(formatText("Kitchen"),1,2);
    InnerTable.add(kitch,2,2);
    InnerTable.add(formatText("Bathroom"),3,2);
    InnerTable.add(bath,4,2);
    InnerTable.add(formatText("Storage"),1,3);
    InnerTable.add(stor,2,3);
    InnerTable.add(formatText("Study"),3,3);
    InnerTable.add(study,4,3);
    InnerTable.add(formatText("Loft"),1,4);
    InnerTable.add(loft,2,4);
    InnerTable.add(formatText("Furniture"),3,4);
    InnerTable.add(furni,4,4);
    InnerTable.add(formatText("Balcony"),1,5);
    InnerTable.add(balc,2,5);
    InnerTable.add(formatText("Rent"),1,6);
    InnerTable.add(rent,2,6);
    T.add(InnerTable,1,4);
    T.add(formatText("Info:"),1,5);
    T.add(Text.getBreak(),1,5);
    T.add( makeTextArea(sInfo),1,5);
    T.add(formatText("ExtraInfo:"),1,6);
    T.add(Text.getBreak(),1,6);
    T.add( makeTextArea("extra_info",sExtraInfo),1,6);
    T2.add(formatText("Photo:"),1,1);
    T2.add(this.makeImageInput(iImageId,"tphotoid"),1,1);
    T2.add(formatText("Plan:"),1,2);
    T2.add(this.makeImageInput(iPlanId,"tplanid"),1,2);
    form.maintainParameter("tphotoid");
    form.maintainParameter("tplanid");
    Frame.add(HI);
    T2.add(new SubmitButton("save","Save"),1,3);
    T2.add(new SubmitButton("del","Delete"),1,3);
    form.add(Frame);
    return form;
  }
  private ModuleObject makeApartmentFields(Apartment eApartment){
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
    Link chooser = new Link(new Image("/pics/list.gif","Select appartment",13,13),chooserWindow);

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
    T.add(formatText("Name:"),1,1);
    T.add(Text.getBreak(),1,1);
    T.add(name,1,1);
    T.add(formatText("&nbsp;&nbsp;"));
    T.add(chooser,1,1);
    T.add(formatText("Floor:"),1,2);
    T.add(Text.getBreak(),1,2);
    T.add(floors,1,2);
    T.add(formatText("Type:"),1,3);
    T.add(Text.getBreak(),1,3);
    T.add(types,1,3);
    T.add(formatText("Serie: "),1,4);
    T.add(serie,1,4);
    T.add(formatText("Rentable: "),1,5);
    T.add(rentable,1,5);
    T.add(formatText("Info:"),1,6);
    T.add(Text.getBreak(),1,6);
    T.add( makeTextArea(sInfo),1,6);
    T2.add(formatText("Photo:"),1,1);
    T2.add(this.makeImageInput(1,"photoid"),1,1);
    form.add(HI);
    form.add(HA);
    if(e)
      form.add(HID);
    T2.add(new SubmitButton("save","Save"),1,2);
    T2.add(new SubmitButton("del","Delete"),1,2);
    form.add(Frame);
    return form;
  }

  private ModuleObject getApartments(){
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

  private ModuleObject getTypes(){
    try {
      ApartmentType[] AT = (ApartmentType[])(new ApartmentType()).findAllOrdered(ApartmentType.getNameColumnName());
      int len = AT.length;
      Table T = new Table();
      if(len > 0){
        T = new Table(10,len+1);
        T.setCellpadding(4);
        T.setCellspacing(2);

        int row = 1,col = 1;
        T.add(getHeaderText("Name"),col++,row);
        T.add(getHeaderText("Area(m2)"),col++,row);
        T.add(getHeaderText("Rooms"),col++,row);
        T.add(getHeaderText("Kitchen"),col++,row);
        T.add(getHeaderText("Bath"),col++,row);
        T.add(getHeaderText("Storage"),col++,row);
        T.add(getHeaderText("Study"),col++,row);
        T.add(getHeaderText("Loft"),col++,row);
        T.add(getHeaderText("Furniture"),col++,row);
        T.add(getHeaderText("Balcony"),col++,row);
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

  public void main(ModuleInfo modinfo)  {
    try{
    isAdmin = com.idega.jmodule.login.business.AccessControl.isAdmin(modinfo);
    this.getParentPage().setName("b_editor");
    this.getParentPage().setTitle("Building Editor");
    this.getParentPage().setAllMargins(0);
    }
    catch(SQLException sql){ isAdmin = false;}
    /** @todo: fixa Admin*/
    control(modinfo);
  }
  protected void setStyle(InterfaceObject O){
    O.setAttribute("style",this.styleAttribute);
  }
  protected void setStyle2(InterfaceObject O){
    O.setAttribute("style",this.styleAttribute2);
  }
}// class BuildingEditor
