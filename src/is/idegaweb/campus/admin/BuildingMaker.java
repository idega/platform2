/*
 * $Id: BuildingMaker.java,v 1.2 2001/06/12 14:58:43 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.admin;

import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.login.business.*;
import com.idega.jmodule.object.*;
import com.idega.block.building.data.*;
import java.sql.SQLException;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import com.oreilly.servlet.multipart.*;
import com.oreilly.servlet.*;
import com.idega.data.GenericEntity;
import com.idega.data.genericentity.Address;
import is.idegaweb.campus.entity.*;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class BuildingMaker extends JModuleObject{

  private int iAct;
  private String sAct;
  private final String strAction = "action";
  private final String sUplDirPar = "upload_dir";
  private final String sSaveDir = "adm";
  private final String noPicPath = "/adm/buildings/nopic.gif";
  private final String PicPath = "/adm/buildings/temp";
  private boolean isAdmin;
  private final int NOACT = 0,UPLOAD = 1, SAVE = 2,  NEW = 3, QUIT = 4;
  private final int BUILDING=1,FLOOR=2,ROOMTYPE=3,ROOM=4,ROOMSUBTYPE=5;
  private Table MainFrame,Frame,InnerFrame;
  private Form theForm;
  private int BORDER= 0;
  private Boolean isMulti = new Boolean(false);
  private ImageProperties ip = null;
/*
  private void control(ModuleInfo modinfo){

    try{
      if(modinfo.getSession().getAttribute("lodging_id")!=null)
        add(modinfo.getSession().getAttribute("lodging_id"));
      if(modinfo.getSession().getAttribute("build_multi")!=null)
        isMulti = (Boolean)  modinfo.getSession().getAttribute("build_multi");
      if(modinfo.getSession().getAttribute("bm_ip")!=null)
        ip = (ImageProperties)modinfo.getSession().getAttribute("bm_ip");
      if(isMulti.booleanValue()){
        doUpload(modinfo);
      }
      else{
        doMain(modinfo,true,this.BUILDING );
      }
    }
    catch(Exception S){	S.printStackTrace();	}
  }

  private void doMain(ModuleInfo modinfo,boolean ifMulti,int choice) {
    modinfo.getSession().setAttribute("build_multi",new Boolean(ifMulti));
    this.makeView(ifMulti);
    this.addLinks(this.makeLinkTable());
    this.addBrowse(this.makeBrowse());
    if(ip !=null){
      if(ip.getId() != -1){
        try {
           this.addImage(new Image(ip.getId()));
        }
        catch (Exception ex) {   }
      }
      else
       this.addImage(new Image(ip.getWebPath()));
    }
    else
      this.addImage(new Image(noPicPath));

    this.addEdit(this.makeTextArea(""));
    this.addSave(new SubmitButton("submit","Save"));
    ModuleObject MO = null;
    switch (choice) {
      case BUILDING :  MO = this.makeBuildingFields("","","");      break;
      case FLOOR    :  MO = this.makeFloorFields("","");            break;
      case ROOMTYPE :  MO = this.makeTypeFields("");                break;
      case ROOM     :  MO = this.makeRoomFields("","","");          break;
      case ROOMSUBTYPE : MO = this.makeSubTypeFields("","","","","",false,false,false,false,false,false,false);        break;
      default : MO = this.makeBuildingFields("","","");
    }
    if(MO!=null)
      this.addFields(MO);
  }


  private void doUpload(ModuleInfo modinfo){
    String sep = System.getProperty("file.separator");
    String realPath = modinfo.getServletContext().getRealPath(PicPath);
    String webPath = PicPath;
    String realFile = "";

    try {
    MultipartParser mp = new MultipartParser(modinfo.getRequest(), 10*1024*1024); // 10MB
    Part part;
    File dir = null;
    String value = null;
    String sName="",sAddress="",sZip="",sInfo="",sFloor="",sHouse="",sType="",sSubType="";
    String sButton = "",sGet = "";
    String sRoomCount ="",sArea = "";
    boolean bKitchen=false,bBath=false,bStorage=false,bBalcony=false,bStudy=false,bLoft=false,bPlan=false;
    String submitAction="",sLodging="",sId="";
    while ((part = mp.readNextPart()) != null) {
      String name = part.getName();
      if(part.isParam()){
        ParamPart paramPart = (ParamPart) part;
        value = paramPart.getStringValue();
         if(name.equalsIgnoreCase("bm_name") )          sName     = value;
        else if(name.equalsIgnoreCase("bm_address") )  sAddress  = value;
        else if(name.equalsIgnoreCase("bm_zip"))       sZip      = value;
        else if(name.equalsIgnoreCase("bm_info"))      sInfo     = value;
        else if(name.equalsIgnoreCase("bm_choice"))    sLodging  = value;
        else if(name.equalsIgnoreCase("bm_house"))     sHouse    = value;
        else if(name.equalsIgnoreCase("bm_floor"))     sFloor    = value;
        else if(name.equalsIgnoreCase("bm_info"))      sInfo     = value;
        else if(name.equalsIgnoreCase("bm_roomtype"))  sType     = value;
        else if(name.equalsIgnoreCase("bm_subtype"))   sSubType     = value;
        else if(name.equalsIgnoreCase("bm_roomcount")) sRoomCount = value;
        else if(name.equalsIgnoreCase("bm_area"))      sArea = value;
        else if(name.equalsIgnoreCase("bm_kitch"))     bKitchen = true;
        else if(name.equalsIgnoreCase("bm_bath"))      bBath = true;
        else if(name.equalsIgnoreCase("bm_stor"))      bStorage = true;
        else if(name.equalsIgnoreCase("bm_balc"))      bBalcony = true;
        else if(name.equalsIgnoreCase("bm_study"))     bStudy = true;
        else if(name.equalsIgnoreCase("bm_loft"))      bLoft = true;
        else if(name.equalsIgnoreCase("bm_plan"))      bPlan = true;
        else if(name.equalsIgnoreCase("submit"))       submitAction = value;
        else if(name.equalsIgnoreCase("button"))       sButton = value;
        else if(name.equalsIgnoreCase("get"))          sGet = value;
        else if(name.equalsIgnoreCase("dr_id"))        sId = value;

        add(name+" : "+value+"<br>");
      }
      else if (part.isFile()) {
        // it's a file part
        FilePart filePart = (FilePart) part;
        String fileName = filePart.getFileName();
        if (fileName != null) {
          if(modinfo.getSession().getAttribute("bm_ip")!=null)
            modinfo.getSession().removeAttribute("bm_ip");
          //add("<br>");
          //add(name);
          //add("skrá er til:<br>");
          //add(fileName+"<br>");
          String s = fileName.substring(fileName.indexOf("."));
          webPath = PicPath+s;
          File file = new File(realPath+s);
          long size = filePart.writeTo(file);
          ip = new ImageProperties(fileName,filePart.getContentType(),realPath+s,webPath,size);
          modinfo.getSession().setAttribute("bm_ip",ip);
          //modinfo.getSession().removeAttribute("bm_ip");
        }
      }
    }
    add("plan:"+bPlan);
    boolean multi = true;
    modinfo.getSession().setAttribute("build_multi",new Boolean(multi));

    if(!sButton.equalsIgnoreCase("")){
      if(modinfo.getSession().getAttribute("bm_ip")!=null)
        modinfo.getSession().removeAttribute("bm_ip");
      ip = null;
      int iChoice = 0;
      if(sButton.equalsIgnoreCase("House")) iChoice = this.BUILDING;
      else if(sButton.equalsIgnoreCase("Floor")) iChoice = this.FLOOR;
      else if(sButton.equalsIgnoreCase("Type")) iChoice = this.ROOMTYPE;
      else if(sButton.equalsIgnoreCase("Room")) iChoice = this.ROOM;
      else if(sButton.equalsIgnoreCase("Subtype")) iChoice = this.ROOMSUBTYPE;
      doMain(modinfo,true,iChoice);
      //add("choice "+iChoice);
      return;
    }
    else if(submitAction.equalsIgnoreCase("Save")){
      int iImageId = -1;
      int id = -1;
      if(modinfo.getSession().getAttribute("lodging_id")!=null)
       id = ((Integer)modinfo.getSession().getAttribute("lodging_id")).intValue();
      add(""+id);
      if(ip!=null)
        iImageId = this.SaveImage(ip);
      if(modinfo.getSession().getAttribute("bm_ip")!=null)
        modinfo.getSession().removeAttribute("bm_ip");
      ip = null;
      if(sLodging.equalsIgnoreCase("building") ){
        this.storeBuilding(sName,sAddress,sZip,sInfo,iImageId,id);
        this.doMain(modinfo,true,this.BUILDING  );
      }
      else if(sLodging.equalsIgnoreCase("floor") ){
        this.storeFloor(sName,sHouse,sInfo,iImageId,id);
        this.doMain(modinfo,true,this.FLOOR);
      }
      else if(sLodging.equalsIgnoreCase("roomtype") ){
        this.storeRoomType(sName,sInfo,iImageId,id);
        this.doMain(modinfo,true,this.ROOMTYPE );
      }
      else if(sLodging.equalsIgnoreCase("room") ){
        this.storeRoom(sName,sFloor,sSubType,sInfo,iImageId,id);
        this.doMain(modinfo,true,this.ROOM);
      }
      else if(sLodging.equalsIgnoreCase("subtype") ){
        this.storeRoomSubType(sName, sInfo,id,Integer.parseInt(sType),iImageId,bPlan,
        sArea, sRoomCount,bKitchen,bBath,bStorage,bLoft,bStudy,bBalcony);
        this.doMain(modinfo,true,this.ROOMSUBTYPE);
      }
      if(modinfo.getSession().getAttribute("lodging_id")!=null)
        modinfo.getSession().removeAttribute("lodging_id");
    }
   else if(!sGet.equalsIgnoreCase("")){
      this.makeView(multi);
      this.addLinks(this.makeLinkTable());
      this.addBrowse(this.makeBrowse());

      ModuleObject fields = null;
      int id = Integer.parseInt(sId);
      int imageid = -1;
      String einfo ="";
      if(id != 0){
        try{
          if(sLodging.equalsIgnoreCase("building") ){
            Building b = new Building(id);
            Address a = new Address(b.getAddressId());
            einfo = b.getInfo();
            imageid = b.getImageId();
            fields = this.makeBuildingFields(b.getName(),a.getStreet(),String.valueOf(a.getZipcodeId()));
          }
          else if(sLodging.equalsIgnoreCase("floor") ){
            Floor f = new Floor(id);
            einfo = f.getInfo();
            imageid = f.getImageId();
            fields = this.makeFloorFields(f.getName(),String.valueOf(f.getBuildingId()));
          }
          else if(sLodging.equalsIgnoreCase("roomtype") ){
            RoomType r = new RoomType(id);
            einfo = r.getInfo();
            fields = this.makeTypeFields(r.getName());
          }
          else if(sLodging.equalsIgnoreCase("room") ){
            Room r = new Room(id);
            einfo = r.getInfo();
            imageid = r.getImageId();
            fields = this.makeRoomFields(r.getName(),String.valueOf(r.getFloorId()),String.valueOf(r.getRoomSubTypeId()));
          }
           else if(sLodging.equalsIgnoreCase("subtype") ){
            RoomSubType r = new RoomSubType(id);
            einfo = r.getInfo();
            if(bPlan)
              imageid = r.getFloorPlanId();
            else
              imageid = r.getImageId();
            fields = this.makeSubTypeFields(r.getName(),String.valueOf(r.getRoomTypeId()),String.valueOf(r.getArea()),
            String.valueOf(r.getID()),String.valueOf(r.getRoomCount()),bPlan,r.getKitchen(),r.getBathRoom(),
            r.getStorage(),r.getBalcony(),r.getStudy(),r.getLoft());
          }
        }
        catch(SQLException e){}
      }
      if(imageid != -1){
        this.addImage(new Image(imageid));
        try {
          com.idega.jmodule.image.data.ImageEntity im = new com.idega.jmodule.image.data.ImageEntity(imageid);
          ImageProperties ip = new ImageProperties(im.getImageName(),im.getContentType(),"","",0);
          ip.setId(imageid);
          modinfo.getSession().setAttribute("bm_ip",ip);
        }
        catch (SQLException ex) {     }
      }
      else
        this.addImage(new Image(this.noPicPath));
      this.addEdit(this.makeTextArea(einfo));
      this.addSave(new SubmitButton("submit","Save"));
      if(fields != null)
        this.addFields(fields);

      modinfo.getSession().setAttribute("lodging_id",new Integer(id));
    }
    else{
      this.makeView(multi);
      this.addLinks(this.makeLinkTable());
      this.addBrowse(this.makeBrowse());
      if(ip !=null){
      if(ip.getId() != -1)
        this.addImage(new Image(ip.getId()));
      else
       this.addImage(new Image(ip.getWebPath()));
      }
      this.addEdit(this.makeTextArea(sInfo));
      this.addSave(new SubmitButton("submit","Save"));
      if(sLodging.equalsIgnoreCase("building") )
        this.addFields(this.makeBuildingFields(sName,sAddress,sZip));
      else if(sLodging.equalsIgnoreCase("floor") )
        this.addFields(this.makeFloorFields(sName,sHouse));
      else if(sLodging.equalsIgnoreCase("roomtype") )
        this.addFields(this.makeTypeFields(sName));
      else if(sLodging.equalsIgnoreCase("room") )
        this.addFields(this.makeRoomFields(sName,sFloor,sType));
      else if(sLodging.equalsIgnoreCase("subtype") )
        this.addFields(this.makeSubTypeFields(sName,sType,sArea,"",sRoomCount,bPlan
        ,bKitchen,bBath,bStorage,bBalcony,bStudy,bLoft));
    }

  }
  catch (Exception s) {
    s.printStackTrace();
  }

  }

  private void doQuit(ModuleInfo modinfo) throws SQLException{  }
  private void doSave(ModuleInfo modinfo) throws SQLException{  }

  private void storeBuilding(String name, String address, String zip, String info, int imageid,int id){

    Building ebuilding = new Building();
    Address eaddress = new Address();
    try{
      if(id != -1){
        ebuilding = new Building(id);
        eaddress = new Address(ebuilding.getAddressId());
      }

      eaddress.setCountryId(1);
      eaddress.setStreet(address);
      eaddress.setZipcodeId(Integer.parseInt(zip));
      int addrId = -1;
      if(id != -1)
        eaddress.update();
      else
        eaddress.insert();

      addrId = eaddress.getID();

      ebuilding.setName(name);
      ebuilding.setAddressId(addrId);
      ebuilding.setInfo(info);
      ebuilding.setImageId(imageid);
      if(id != -1)
        ebuilding.update();
      else
        ebuilding.insert();
    }
    catch(SQLException e){}
  }
  private void storeFloor(String name, String houseid, String info, int imageid,int id){
    try{

      Floor efloor = new Floor();
      if(id != -1)
        efloor = new Floor(id);
      efloor.setName(name);
      efloor.setBuildingId(Integer.parseInt(houseid));
      efloor.setInfo(info);
      efloor.setImageId(imageid);
      if(id != -1)
        efloor.update();
      else
        efloor.insert();
    }
    catch(SQLException e){}

  }
  private void storeRoomType(String name, String info, int imageid,int id){
    try{
      RoomType etype = new RoomType();
      if(id != -1)
        etype = new RoomType(id);
      etype.setName(name);
      etype.setInfo(info);
      if(id != -1)
        etype.update();
      else
        etype.insert();
    }
    catch(SQLException e){}

  }
  private void storeRoomSubType(String name, String info,int id,int roomtypeid,
                int imageid,boolean plan,String area,String roomcount,
                boolean kitch,boolean bath,boolean stor,boolean loft,boolean stud,boolean balc){
    try{
      RoomSubType etype = new RoomSubType();
      if(id != -1)
        etype = new RoomSubType(id);
      etype.setName(name);
      etype.setInfo(info);
      if(plan)
        etype.setFloorPlanId(imageid);
      else
        etype.setImageId(imageid);
      etype.setRoomTypeId(roomtypeid);
      etype.setArea(Float.parseFloat(area));
      etype.setRoomCount(Integer.parseInt(roomcount));
      etype.setBalcony(balc);
      etype.setBathRoom(bath);
      etype.setKitchen(kitch);
      etype.setLoft(loft);
      etype.setStorage(stor);
      etype.setStudy(stud);
      if(id != -1)
        etype.update();
      else
        etype.insert();
    }
    catch(SQLException e){}

  }
  private void storeRoom(String name, String floorid, String roomtypeid,String info,int imageid,int id){
    try{
      Room eroom = new Room();
      if(id != -1)
        eroom = new Room(id);
      eroom.setName(name);
      eroom.setFloorId(Integer.parseInt( floorid));
      eroom.setRoomSubTypeId(Integer.parseInt(roomtypeid));
      eroom.setInfo(info);
      eroom.setImageId(imageid);
      if(id != -1)
        eroom.update();
      else
        eroom.insert();
    }
    catch(SQLException e){}

  }

  private int SaveImage(ImageProperties ip){
    try{
      java.sql.Connection conn = com.idega.util.database.ConnectionBroker.getConnection();
      FileInputStream fin = new FileInputStream(ip.getRealPath() );
      int id = com.idega.io.ImageSave.saveImageToDB(conn,-1,fin,ip.getContenType(),ip.getName(),true);
      ip.setId(id);
      fin.close();
      com.idega.util.database.ConnectionBroker.freeConnection(conn);
      return id;
    }
    catch(Exception e){ip.setId(-1); return -1;}
  }

  private void makeView(boolean multi){
    this.makeMainFrame();
    if(multi)
      this.makeMultiForm();
    else
      this.makeNonMultiForm();
    this.makeFrame();
    this.makeInnerFrame();

  }

  private void makeMainFrame(){
    MainFrame = new Table(2,2);
    MainFrame.setWidth(1,"70");
    MainFrame.setWidth(2,"450");
    MainFrame.setCellspacing(0);
    MainFrame.setCellpadding(0);
    MainFrame.setBorder(BORDER);
    add(MainFrame);
  }

  private void makeFrame(){
    Table Border = new Table();
    Border.setCellspacing(2);
    Border.setCellpadding(2);
    Border.setColor("#27334B");
    Frame = new Table(1,4);
    Frame.setCellspacing(0);
    Frame.setCellpadding(0);
    Frame.setWidth("100%");
    Frame.setColor("#9fA9B3");
    Frame.setBorder(BORDER);
    Border.add(Frame);
    this.addFrame(Border);

  }

  private void makeInnerFrame(){
    InnerFrame = new Table(2,1);
    InnerFrame.setCellspacing(0);
    InnerFrame.setCellpadding(0);
    InnerFrame.setVerticalAlignment("top");
    InnerFrame.setWidth("100%");
    InnerFrame.setBorder(BORDER);
    this.addInnerFrame(InnerFrame);
  }

  private void makeMultiForm(){
    theForm = new Form();
    theForm.setMultiPart();
    this.addForm(theForm);
  }

  private void makeNonMultiForm(){
    theForm = new Form();
    this.addForm(theForm);
  }

  private void addFrame(ModuleObject T){
    theForm.add(T);
  }

  private void addLinks(ModuleObject T){
    Frame.add(T,1,1);
  }
  private void addInnerFrame(ModuleObject T){
    Frame.add(T,1,2);
  }
  private void addEdit(ModuleObject T){
    Frame.add(T,1,3);
  }
  private void addForm(ModuleObject T){
    MainFrame.add(T,2,2);
  }
  private void addImage(ModuleObject T){
    InnerFrame.add(T,2,1);
  }
  private void addBrowse(ModuleObject T){
    InnerFrame.add(T,2,1);
  }
  private void addFields(ModuleObject T){
    InnerFrame.add(T,1,1);
  }
  private void addSave(ModuleObject T){
    Frame.add(T,1,4);
  }



   private Table makeLinkTable(){
      Table LinkTable = new Table();
      LinkTable.setBorder(0);
      LinkTable.setCellpadding(0);
      LinkTable.setCellspacing(0);


      SubmitButton B1 = new SubmitButton("button","House");
      SubmitButton B2 = new SubmitButton("button","Floor");
      SubmitButton B3 = new SubmitButton("button","Type");
      SubmitButton B4 = new SubmitButton("button","Room");
      SubmitButton B5 = new SubmitButton("button","Subtype");

      LinkTable.add(B1);
      LinkTable.add(B2);
      LinkTable.add(B3);
      LinkTable.add(B4);
      LinkTable.add(B5);
      return LinkTable;
    }

  private ModuleObject makeBrowse(){
    Table T = new Table();
    FileInput fi = new FileInput("file");
    SubmitButton sb = new SubmitButton("submit","OK");
    T.add(fi,1,1);
    T.add(sb,1,1);
    return T;
  }

  private ModuleObject makeTextArea(String sInit){
    TextArea TA = new TextArea("bm_info");
    TA.setContent(sInit);
    TA.setWidth(63);
    TA.setHeight(6);
    return TA;
  }
  private ModuleObject makeBuildingFields(String sName,String sAddress,String sZip){
    Table T = new Table();
    TextInput name = new TextInput("bm_name",sName);
    TextInput address = new TextInput("bm_address",sAddress);
    HiddenInput HI = new HiddenInput("bm_choice","building");
    DropdownMenu zip = drpZip("bm_zip","Póstnr",sZip,true);
    DropdownMenu houses = drpLodgings(new Building(),"dr_id","Hús","");
    name.setLength(30);
    address.setLength(30);
    T.add("Sækja:",1,1);
    T.add(houses,1,2);
    T.add(new SubmitButton("get","Get"),1,2);
    T.add("Heiti:",1,3);
    T.add(name,1,4);
    T.add("Heimilisfang:",1,5);
    T.add(address,1,6);
    T.add("Póstnúmer",1,7);
    T.add(zip,1,8);
    T.add(HI);
    return T;
  }
  private ModuleObject makeFloorFields(String sName,String sHouse){
    Table T = new Table();
    TextInput name = new TextInput("bm_name",sName);
    DropdownMenu floors = this.drpFloors("dr_id","Hæð","",true);
    HiddenInput HI = new HiddenInput("bm_choice","floor");
    name.setLength(30);
    T.add("Sækja hæð:",1,1);
    T.add(floors,1,2);
    T.add(new SubmitButton("get","Get"),1,2);
    T.add(HI);
    T.add("Heiti:",1,3);
    T.add(name,1,4);
    T.add("Hús:",1,5);
    T.add(this.drpLodgings(new Building(),"bm_house","Hús",sHouse),1,6);
    return T;
  }
  private ModuleObject makeTypeFields(String sName){
    Table T = new Table();
    String s;
    TextInput name = new TextInput("bm_name",sName);
    DropdownMenu roomtypes = drpLodgings(new RoomType(),"dr_id","Gerð","");
    HiddenInput HI = new HiddenInput("bm_choice","roomtype");
    name.setLength(30);
    T.add(HI);
    T.add("Sækja gerð:",1,1);
    T.add(roomtypes,1,2);
    T.add(new SubmitButton("get","Get"),1,2);
    T.add("Heiti:",1,3);
    T.add(name,1,4);
    return T;
  }

 private ModuleObject makeSubTypeFields(String sName,String sType,String sArea,String sSubType,
    String sRoomCount,boolean bPlan,boolean bKitch,boolean bBath,boolean bStor, boolean bBalc,
    boolean bStud, boolean bLoft){
    Table T = new Table();
    TextInput name = new TextInput("bm_name",sName);
    name.setLength(30);
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
    DropdownMenu roomtypes = drpLodgings(new RoomSubType(),"dr_id","Undirgerð",sSubType);
    HiddenInput HI = new HiddenInput("bm_choice","subtype");
    name.setLength(30);
    T.add(HI);
    T.add("Sækja undirgerð:",1,1);
    T.add(roomtypes,1,2);
    T.add(new SubmitButton("get","Get"),1,2);
    T.add("Heiti:",1,3);
    T.add(name,1,4);
    T.add("Gerð:",1,5);
    T.add(this.drpLodgings(new RoomType(),"bm_roomtype","Gerð",sType),1,5);
    T.add("Fjöldi herbergja",1,6);
    T.add(roomcount,2,6);
    T.add("Flatarmál(m2)",1,7);
    T.add(area,2,7);
    T.add("Eldhús",1,8);
    T.add(kitch,2,8);
    T.add("Bað",1,9);
    T.add(bath,2,9);
    T.add("Geymsla",1,10);
    T.add(stor,2,10);
    T.add("Lesaðstaða",1,11);
    T.add(study,2,11);
    T.add("HáaLoft",1,12);
    T.add(loft,2,12);
    T.add("Teikning",1,13);
    T.add(plan,2,13);
    return T;
  }
 private ModuleObject makeRoomFields(String sName,String sFloor,String sSubType){
    Table T = new Table();
    TextInput name = new TextInput("bm_name",sName);
    DropdownMenu rooms = drpLodgings(new Room(),"dr_id","Rými","");
    HiddenInput HI = new HiddenInput("bm_choice","room");
    T.add(HI);
    T.add("Sækja rými:",1,1);
    name.setLength(30);
    T.add(rooms,1,2);
    T.add(new SubmitButton("get","Get"),1,2);
    T.add("Heiti:",1,3);
    T.add(name,1,4);
    T.add("Hæð:",1,5);
    T.add(this.drpFloors("bm_floor","Hæð",sFloor,true),1,6);
    T.add("Gerð:",1,7);
    T.add(this.drpLodgings(new RoomSubType(),"bm_subtype","Gerð",sSubType),1,8);
    return T;
  }

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
    GenericEntity[] lods = new GenericEntity[1];
    try{
      lods =  (lodgings).findAll();
    }
    catch(SQLException e){}
    DropdownMenu drp = new DropdownMenu(name);
    drp.addDisabledMenuElement("0",display);
    for(int i = 0; i < lods.length ; i++){
      drp.addMenuElement(lods[i].getID(),lods[i].getName());
    }
    if(!selected.equalsIgnoreCase("")){
      drp.setSelectedElement(selected);
    }
    return drp;
  }

  public void main(ModuleInfo modinfo)  {
    try{
    isAdmin = com.idega.jmodule.login.business.AccessControl.isAdmin(modinfo);
    }
    catch(SQLException sql){ isAdmin = false;}
    /** @todo: fixa Admin*/
    /*control(modinfo);
  }*/
}// class BuildingMaker