package com.idega.block.building.presentation;

import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.login.business.*;
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

 public class BuildingEditor extends Editor{

  private String sAction = "be_action";
  private String styleAttribute = "font-size: 8pt";
  private final int COMPLEX = 1,BUILDING = 2,FLOOR = 3, APARTMENT = 4,
                    CATEGORY = 5,TYPE = 6;

  protected void control(ModuleInfo modinfo){
    this.makeView();
    this.addLinks(this.makeLinkTable(1));
    int iAction = BUILDING;
    if(modinfo.getParameter(sAction)!= null){
      iAction = Integer.parseInt(modinfo.getParameter(sAction));
    }
    switch (iAction) {
      case COMPLEX  : doComplex(modinfo);   break;
      case BUILDING : doBuilding(modinfo);  break;
      case FLOOR    : doFloor(modinfo);     break;
      case APARTMENT: doApartment(modinfo); break;
      case CATEGORY : doCategory(modinfo);  break;
      case TYPE     : doType(modinfo);   break;
    }
  }
  private void doMain(ModuleInfo modinfo,boolean ifMulti,int choice) {
    doBuilding(modinfo);

  }
  private void doComplex(ModuleInfo modinfo){
    addMain(new Text("Complex"));
    addMain(makeComplexFields("",""));
  }
  private void doBuilding(ModuleInfo modinfo){
    addMain(new Text("Building"));
    addMain(makeBuildingFields("","",""));
  }
  private void doFloor(ModuleInfo modinfo){
    addMain(new Text("Floor"));
    addMain(makeFloorFields("",""));
  }
  private void doApartment(ModuleInfo modinfo){
    addMain(new Text("Apartment"));
    addMain(makeApartmentFields("","","",false));
  }
  private void doType(ModuleInfo modinfo){
    addMain(new Text("ApartmentType"));
    addMain(makeTypeFields("","","","","",false,false,false,false,false,false, false,false));
  }
  private void doCategory(ModuleInfo modinfo){
    addMain(new Text("ApartmentCategory"));
    addMain(makeCategoryFields(""));
  }

  private void doQuit(ModuleInfo modinfo) throws SQLException{  }
  private void doSave(ModuleInfo modinfo) throws SQLException{  }

  private void storeComplex(ModuleInfo modinfo){
    String sName = modinfo.getParameter("bm_name");
    String sInfo = modinfo.getParameter("bm_info");
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
    catch(SQLException e){}
  }

  private void storeBuilding(ModuleInfo modinfo){
    String sName = modinfo.getParameter("bm_name");
    String sInfo = modinfo.getParameter("bm_info");
    String sAddress = modinfo.getParameter("bm_address");
    String sImageId = modinfo.getParameter("image_id");
    String sComplexId = modinfo.getParameter("dr_complex");
    String sId = modinfo.getParameter("dr_id");
    int imageid = 1;
    int id = -1;
    int complexid = -1;
    try {  id = Integer.parseInt(sId);  }
    catch (NumberFormatException ex) { id = -1;  }
    try {  complexid = Integer.parseInt(sComplexId);  }
    catch (NumberFormatException ex) { complexid = -1;  }
    try {  imageid = Integer.parseInt(sImageId);  }
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
    String sImageId = modinfo.getParameter("image_id");
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

      }
      efloor.setName(sName);
      efloor.setBuildingId(buildingid);
      efloor.setInfo(sInfo);
      efloor.setImageId(imageid);
      if(id != -1)
        efloor.update();
      else
        efloor.insert();
    }
    catch(SQLException e){}

  }
  private void storeApartmentCategory(ModuleInfo modinfo){
    //String name, String info, int imageid,int id){
    String sName = modinfo.getParameter("bm_name").trim();
    String sInfo = modinfo.getParameter("bm_info").trim();
    String sId = modinfo.getParameter("dr_id");

    int id = -1;
    try {  id = Integer.parseInt(sId);  }
    catch (NumberFormatException ex) { id = -1;  }

    try{
      boolean update = false;
      ApartmentCategory eACategory = new ApartmentCategory();
      if(id != -1){
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
    /*String name, String info,int id,int roomtypeid,
                int imageid,boolean plan,String area,String roomcount,
                boolean kitch,boolean bath,boolean stor,boolean loft,boolean stud,
                boolean balc,boolean furniture){*/

    String sName = modinfo.getParameter("bm_name").trim();
    String sInfo = modinfo.getParameter("bm_info").trim();
    String sId = modinfo.getParameter("dr_id");
    String sRoomCount = modinfo.getParameter("bm_roomcount");
    String sCategoryId = modinfo.getParameter("bm_category");
    String sImageId = modinfo.getParameter("imageid");
    String sPlanId = modinfo.getParameter("planid");
    String sArea = modinfo.getParameter("bm_area").trim();
    String sKitch = modinfo.getParameter("bm_kitch").trim();
    String sBath = modinfo.getParameter("bm_bath").trim();
    String sStorage = modinfo.getParameter("bm_stor").trim();
    String sBalcony = modinfo.getParameter("bm_balc").trim();
    String sStudy = modinfo.getParameter("bm_study").trim();
    String sLoft = modinfo.getParameter("bm_loft").trim();
    String sFurniture = modinfo.getParameter("bm_furni").trim();



    int planid = 1;
    int imageid = 1;
    int id = -1;
    int categoryid = -1;
    try { id = Integer.parseInt(sId);  }
    catch (NumberFormatException ex) { id = -1;  }
    try { categoryid = Integer.parseInt(sCategoryId);  }
    catch (NumberFormatException ex) { categoryid = -1;  }
    try { imageid = Integer.parseInt(sImageId);  }
    catch (NumberFormatException ex) { imageid = 1;  }
    try{  planid = Integer.parseInt(sPlanId); }
    catch (NumberFormatException ex) { planid = 1;  }

    try{
      ApartmentType etype = new ApartmentType();
      boolean update = false;
      if(id != -1){
        etype = new ApartmentType(id);
        update = true;
      }
      etype.setName(sName);
      etype.setInfo(sInfo);
      etype.setFloorPlanId(planid);
      etype.setImageId(imageid);
      etype.setApartmentCategoryId(categoryid);
      etype.setArea(Float.parseFloat(sArea));
      etype.setRoomCount(Integer.parseInt(sRoomCount));
      if(sBalcony!=null)
        etype.setBalcony(true);
      if(sBath!=null)
        etype.setBathRoom(true);
      if(sKitch !=null)
        etype.setKitchen(true);
      if(sLoft != null)
        etype.setLoft(true);
      if(sStorage !=null)
       etype.setStorage(true);
      if(sStudy != null)
        etype.setStudy(true);
      if(sFurniture!= null)
        etype.setFurniture(true);
      if(update)
        etype.update();
      else
        etype.insert();
    }
    catch(SQLException e){}
    catch(Exception e){e.printStackTrace();}

  }
  private void storeApartment(ModuleInfo modinfo){

    String sName = modinfo.getParameter("bm_name").trim();
    String sInfo = modinfo.getParameter("bm_info").trim();
    String sId = modinfo.getParameter("dr_id");
    String sType = modinfo.getParameter("bm_type");
    String sFloorId = modinfo.getParameter("bm_floor");
    String sRentable = modinfo.getParameter("bm_rentable");
    String sImageId = modinfo.getParameter("imageid");

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
      StringTokenizer st = new StringTokenizer(sName,":");
      int count = st.countTokens();
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
            try{
              apartment.insert();
            }
            catch(SQLException sql){}
          }
        }
      }
      Apartment apartment = new Apartment();
      boolean update = false;
      if(id != -1){
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
      else
        apartment.insert();
    }
    catch(SQLException e){}

  }

  protected ModuleObject makeLinkTable(int i){
    Table LinkTable = new Table();
    LinkTable.setBorder(0);
    LinkTable.setCellpadding(0);
    LinkTable.setCellspacing(0);

    Link B1 = new Link("Garður ");
    B1.addParameter(sAction,COMPLEX);
    Link B2 = new Link("Hús  ");
    B2.addParameter(sAction,BUILDING);
    Link B3 = new Link("Hæð  ");
    B3.addParameter(sAction,FLOOR);
    Link B4 = new Link("Flokkur  ");
    B4.addParameter(sAction,CATEGORY);
    Link B5 = new Link("Gerð ");
    B5.addParameter(sAction,TYPE);
    Link B6 = new Link("Íbúð  ");
    B6.addParameter(sAction,APARTMENT);

    LinkTable.add(B1);
    LinkTable.add(B2);
    LinkTable.add(B3);
    LinkTable.add(B4);
    LinkTable.add(B5);
    LinkTable.add(B6);
    return LinkTable;
  }

  private ModuleObject makeTextArea(String sInit){
    TextArea TA = new TextArea("bm_info");
    TA.setContent(sInit);
    TA.setWidth(40);
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
    imageInsert.setMaxImageWidth(200);
    //imageInsert.setDefaultImageURL(sMemberImageURL);
    imageObject = imageInsert;
    return imageObject;
  }
  private ModuleObject makeComplexFields(String sName,String sInfo){
    Table T = new Table(1,7);
    T.setCellpadding(0);
    T.setCellspacing(0);
    T.setWidth("100%");
    String s;
    TextInput name = new TextInput("bm_name",sName);
    DropdownMenu categories = drpLodgings(new Complex(),"dr_id","Complex","");
    HiddenInput HI = new HiddenInput("bm_choice","roomtype");
    setStyle(name);
    setStyle(categories);
    categories.setToSubmit();
    name.setLength(30);
    T.add(HI);
    T.add(categories,1,2);
    T.add(formatText("Name:"),1,3);
    T.add(name,1,4);
    T.add(formatText("Info:"),1,5);
    T.add( makeTextArea(""),1,6);
    return T;
  }
  private ModuleObject makeBuildingFields(String sName,String sAddress,String sZip){
    Table T = new Table();
    TextInput name = new TextInput("bm_name",sName);
    TextInput address = new TextInput("bm_address",sAddress);
    HiddenInput HI = new HiddenInput("bm_choice","building");
    DropdownMenu complex = drpLodgings(new Complex(),"dr_complex","Complex","");
    DropdownMenu houses = drpLodgings(new Building(),"dr_id","Building","");
    houses.setToSubmit();
    setStyle(houses);
    setStyle(name);
    setStyle(address);
    name.setLength(30);
    address.setLength(30);
    T.add(houses,1,2);
    T.add(formatText("Name:"),1,3);
    T.add(name,1,4);
    T.add(formatText("Address:"),1,5);
    T.add(address,1,6);
    T.add(formatText("Info:"),1,7);
    T.add( makeTextArea(""),1,8);
    T.add(formatText("Photo:"),2,1);
    T.add(this.makeImageInput(1,"imageid"),2,2);
    T.add(HI);
    return T;
  }
  private ModuleObject makeFloorFields(String sName,String sHouse){
    Table T = new Table();
    TextInput name = new TextInput("bm_name",sName);
    DropdownMenu floors = this.drpFloors("dr_id","Floor","",true);
    floors.setToSubmit();
    DropdownMenu buildings = this.drpLodgings(new Building(),"dr_building","Building",sHouse);
    HiddenInput HI = new HiddenInput("bm_choice","floor");
    setStyle(name);
    setStyle(floors);
    setStyle(buildings);
    name.setLength(30);

    T.add(floors,1,2);
    T.add(HI);
    T.add(formatText("Name:"),1,3);
    T.add(name,1,4);
    T.add(formatText("Building:"),1,5);
    T.add(buildings,1,6);
    T.add(formatText("Info:"),1,7);
    T.add( makeTextArea(""),1,8);
    return T;
  }
  private ModuleObject makeCategoryFields(String sName){
    Table T = new Table(1,6);
    String s;
    TextInput name = new TextInput("bm_name",sName);
    DropdownMenu categories = drpLodgings(new ApartmentCategory(),"dr_id","Category","");
    categories.setToSubmit();
    HiddenInput HI = new HiddenInput("bm_choice","category");
    setStyle(name);
    setStyle(categories);
    name.setLength(30);
    T.add(HI);
    T.add(categories,1,2);
    T.add(formatText("Name:"),1,3);
    T.add(name,1,4);
    T.add(formatText("Info:"),1,5);
    T.add( makeTextArea(""),1,6);
    return T;
  }

 private ModuleObject makeTypeFields(String sName,String sType,String sArea,String sSubType,
    String sRoomCount,boolean bPlan,boolean bKitch,boolean bBath,boolean bStor, boolean bBalc,
    boolean bStud, boolean bLoft,boolean bFurniture){
    Table T2 = new Table();
    Table T = new Table();
    TextInput name = new TextInput("bm_name",sName);
    DropdownMenu roomcount = drpCount("bm_roomcount","--","",6);
    TextInput area = new TextInput("bm_area",sArea);
    area.setLength(4);
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
    CheckBox plan = new CheckBox("bm_plan","true");
    if(bPlan) plan.setChecked(true);
    CheckBox furni = new CheckBox("bm_furni","true");
    if(bFurniture) furni.setChecked(true);
    DropdownMenu apartmenttypes = drpLodgings(new ApartmentType(),"dr_id","Type",sSubType);
    apartmenttypes.setToSubmit();
    DropdownMenu categories = drpLodgings(new ApartmentCategory(),"bm_category","Category",sType);
    HiddenInput HI = new HiddenInput("bm_choice","type");
    name.setLength(30);
    setStyle(name);
    setStyle(area);
    setStyle(roomcount);
    setStyle(kitch);
    setStyle(bath);
    setStyle(stor);
    setStyle(balc);
    setStyle(study);
    setStyle(loft);
    setStyle(plan);
    setStyle(furni);
    setStyle(apartmenttypes);
    setStyle(categories);
    T2.add(HI);

    T2.add(apartmenttypes,1,2);
    T2.add(formatText("Name:"),1,3);
    T2.add(name,1,4);
    T2.add(formatText("Category:"),1,5);
    T2.add(categories,1,5);
    T.add(formatText("Room count"),1,1);
    T.add(roomcount,2,1);
    T.add(formatText("Area(m2)"),1,2);
    T.add(area,2,2);
    T.add(formatText("Kitchen"),1,3);
    T.add(kitch,2,3);
    T.add(formatText("Bathroom"),1,4);
    T.add(bath,2,4);
    T.add(formatText("Storage"),1,5);
    T.add(stor,2,5);
    T.add(formatText("Study"),1,6);
    T.add(study,2,6);
    T.add(formatText("Loft"),1,7);
    T.add(loft,2,7);
    T.add(formatText("Plan"),1,8);
    T.add(plan,2,8);
    T.add(formatText("Furniture"),1,9);
    T.add(furni,2,9);
    T2.add(T,1,6);
    T2.add(formatText("Info:"),1,7);
    T2.add( makeTextArea(""),1,8);

    return T2;
  }
 private ModuleObject makeApartmentFields(String sName,String sFloor,String sSubType,boolean bRentable){
    Table T = new Table();
    TextInput name = new TextInput("bm_name",sName);
    DropdownMenu apartments = drpLodgings(new Room(),"dr_id","Apartment","");
    apartments.setToSubmit();
    DropdownMenu types = this.drpLodgings(new ApartmentType(),"bm_type","Type",sSubType);
    DropdownMenu floors = this.drpFloors("bm_floor","Floor",sFloor,true);
    CheckBox rentable = new CheckBox("bm_rentable","true");
    if(bRentable) rentable.setChecked(true);
    HiddenInput HI = new HiddenInput("bm_choice","apartment");

    T.add(HI);
    setStyle(name);
    setStyle(types);
    setStyle(floors);
    name.setLength(30);
    T.add(apartments,1,2);

    T.add(formatText("Name:"),1,3);
    T.add(name,1,4);
    T.add(formatText("Floor:"),1,5);
    T.add(floors,1,6);
    T.add(formatText("Type:"),1,7);
    T.add(types,1,8);
    T.add(formatText("Rentable"),1,9);
    T.add(rentable,1,10);
    T.add(formatText("Info:"),1,11);
    T.add( makeTextArea(""),1,12);
    return T;
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

  public void main(ModuleInfo modinfo)  {
    try{
    isAdmin = com.idega.jmodule.login.business.AccessControl.isAdmin(modinfo);
    }
    catch(SQLException sql){ isAdmin = false;}
    /** @todo: fixa Admin*/
    control(modinfo);
  }
}// class BuildingMaker
