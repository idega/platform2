package com.idega.block.school.presentation;

import com.idega.presentation.Block;

import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.presentation.Table;
import com.idega.presentation.PresentationObject;
import com.idega.util.text.TextFormat;
import com.idega.block.school.data.School;
import com.idega.block.school.business.*;
import java.util.Collection;
import com.idega.business.IBOLookup;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class SchoolEditor extends Block {

  IWResourceBundle iwrb;
  IWBundle iwb;
  TextFormat tFormat;
  SchoolBusiness sabBean;
  public final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.school";

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  private void control(IWContext iwc) throws Exception{
    //debugParameters(iwc);
    initBeans(iwc);
    Form F = new Form();

    if(iwc.isParameterSet("sch_save_school")){
      saveArea(iwc);
      F.add(getListTable(null));
    }
    else if(iwc.isParameterSet("sch_delete_school")){
      int id = Integer.parseInt(iwc.getParameter("sch_delete_school"));
      sabBean.removeSchool(id);
      F.add(getListTable(null));
    }
    else if(iwc.isParameterSet("sch_school_id")){
      int id = Integer.parseInt(iwc.getParameter("sch_school_id"));
      F.add(getInput(iwc,id));

    }
    else if(iwc.isParameterSet("sch_new_school")){
      F.add(getInput(iwc,-1));
    }
    else
      F.add(getListTable(null));

     add(F);

  }

  private void initBeans(IWContext iwc) throws java.rmi.RemoteException,javax.ejb.CreateException{
    sabBean = (SchoolBusiness) IBOLookup.getServiceInstance(iwc,SchoolBusiness.class);
  }

  private PresentationObject getInput(IWContext iwc ,int id)throws java.rmi.RemoteException{
    return getInputTable(iwc,sabBean.getSchool(new Integer(id)));
  }

  private void saveArea(IWContext iwc)throws java.rmi.RemoteException{
    if(iwc.isParameterSet("sch_save_school")){
      String id = iwc.getParameter("sch_school_id");
      String name = iwc.getParameter("sch_name");
      String address = iwc.getParameter("sch_address");
      String info = iwc.getParameter("sch_info");
      String type = iwc.getParameter("sch_type_id");
      String area = iwc.getParameter("sch_area_id");
      String zipcode = iwc.getParameter("sch_zip_code");
      String ziparea = iwc.getParameter("sch_zip_area");
      String phone = iwc.getParameter("sch_phone");
      String keycode = iwc.getParameter("sch_keycode");
      String lon = iwc.getParameter("sch_lon");
      String lat = iwc.getParameter("sch_lat");
      int areaId = -1,typeId = -1,sid = -1;
      if(id!=null)
        sid = Integer.parseInt(id);
      if(area!=null)
        areaId = Integer.parseInt(area);
      if(type!=null)
        typeId = Integer.parseInt(type);
      sabBean.storeSchool(sid,name,info,address,zipcode,ziparea,phone,keycode,lat,lon,typeId,areaId);
    }
  }

  public PresentationObject getListTable(School ent) {
    Table T = new Table();
    int row = 1;

    Collection schools = new java.util.Vector(0);
    try{
      schools = sabBean.findAllSchools();
    }
    catch(java.rmi.RemoteException rex){

    }
    T.mergeCells(1,1,2,1);
    Link newLink = new Link(iwrb.getLocalizedImageButton("new","New"));
    newLink.addParameter("sch_new_school","true");
    int col = 2;
    T.add(newLink,1,row);
    row++;

    T.add(tFormat.format(iwrb.getLocalizedString("type","Type")),col++,row);
    T.add(tFormat.format(iwrb.getLocalizedString("area","Area")),col++,row);
    T.add(tFormat.format(iwrb.getLocalizedString("name","Name")),col++,row);
    T.add(tFormat.format(iwrb.getLocalizedString("address","Address")),col++,row);
    T.add(tFormat.format(iwrb.getLocalizedString("zipcode","Zipcode")),col++,row);
    T.add(tFormat.format(iwrb.getLocalizedString("ziparea","Ziparea")),col++,row);
    T.add(tFormat.format(iwrb.getLocalizedString("phone","Phone")),col++,row);
    T.add(tFormat.format(iwrb.getLocalizedString("keycode","Keycode")),col++,row);
    T.add(tFormat.format(iwrb.getLocalizedString("latitude","Latitude")),col++,row);
    T.add(tFormat.format(iwrb.getLocalizedString("longitude","Longitude")),col++,row);
    row++;

    java.util.Iterator iter = schools.iterator();
    School school ;
    col = 1;
    while(iter.hasNext()){
      school = (School) iter.next();
      try{
      Link L = new Link(tFormat.format("edit"));
      L.addParameter("sch_school_id",((Integer)school.getPrimaryKey()).intValue());
      T.add(L,col++,row);
      T.add(tFormat.format(school.getSchoolTypeId()),col++,row);
      T.add(tFormat.format(school.getSchoolAreaId()),col++,row);
      T.add(tFormat.format(school.getSchoolName()),col++,row);
      T.add(tFormat.format(school.getSchoolAddress()),col++,row);
      T.add(tFormat.format(school.getSchoolZipCode()),col++,row);
      T.add(tFormat.format(school.getSchoolZipArea()),col++,row);
      T.add(tFormat.format(school.getSchoolPhone()),col++,row);
      T.add(tFormat.format(school.getSchoolKeyCode()),col++,row);
      T.add(tFormat.format(school.getSchoolLatitude()),col++,row);
      T.add(tFormat.format(school.getSchoolLongitude()),col++,row);
      }
      catch(Exception ex){}
      row++;
    }
    return T;
  }

  public PresentationObject getInputTable(IWContext iwc,School ent)throws java.rmi.RemoteException{
    Table T = new Table(3,14);
    T.mergeCells(1,1,3,1);

    TextInput inputName = new TextInput("sch_name");
    TextInput inputAddress = new TextInput("sch_address");
    TextArea inputInfo = new TextArea("sch_info");

    TextInput inputZipCode = new TextInput("sch_zip_code");
    TextInput inputZipArea = new TextInput("sch_zip_area");
    TextInput inputPhone = new TextInput("sch_phone");
    TextInput inputKeyCode = new TextInput("sch_keycode");
    TextInput inputLON = new TextInput("sch_lon");
    TextInput inputLAT = new TextInput("sch_lat");
    DropdownMenu drpType = new DropdownMenu(getSchoolTypes(iwc),"sch_type_id");
    DropdownMenu drpArea = new DropdownMenu(getSchoolAreas(iwc),"sch_area_id");

    int Id = -1;
    if(ent!=null){
      try{
      Id = ((Integer)ent.getPrimaryKey()).intValue();
      inputName.setContent(ent.getSchoolName());
      inputAddress.setContent(ent.getSchoolAddress());
      inputInfo.setContent(ent.getSchoolInfo());
      inputZipCode.setContent(ent.getSchoolZipCode());
      inputZipArea.setContent(ent.getSchoolZipArea());
      inputPhone.setContent(ent.getSchoolPhone());
      inputKeyCode.setContent(ent.getSchoolKeyCode());
      inputLON.setContent(ent.getSchoolLongitude());
      inputLAT.setContent(ent.getSchoolLatitude());
      drpType.setSelectedElement(String.valueOf(ent.getSchoolTypeId()));
      drpArea.setSelectedElement(String.valueOf(ent.getSchoolAreaId()));

      T.add(new HiddenInput("sch_school_id",String.valueOf(Id)));
      }
      catch(Exception ex){}
    }

    int row = 1;
    T.add(tFormat.format(iwrb.getLocalizedString("school_area","SchoolArea"),tFormat.TITLE),1,row++);
    T.add(tFormat.format(iwrb.getLocalizedString("type","Type")),1,row++);
    T.add(tFormat.format(iwrb.getLocalizedString("area","Area")),1,row++);
    T.add(tFormat.format(iwrb.getLocalizedString("name","Name")),1,row++);
    T.add(tFormat.format(iwrb.getLocalizedString("address","Address")),1,row++);
    T.add(tFormat.format(iwrb.getLocalizedString("zipcode","Zipcode")),1,row++);
    T.add(tFormat.format(iwrb.getLocalizedString("ziparea","Ziparea")),1,row++);
    T.add(tFormat.format(iwrb.getLocalizedString("phone","Phone")),1,row++);
    T.add(tFormat.format(iwrb.getLocalizedString("info","Info")),1,row++);
    T.add(tFormat.format(iwrb.getLocalizedString("keycode","Keycode")),1,row++);
    T.add(tFormat.format(iwrb.getLocalizedString("latitude","Latitude")),1,row++);
    T.add(tFormat.format(iwrb.getLocalizedString("longitude","Longitude")),1,row++);

    row = 2;
    T.add(drpType,3,row++);
    T.add(drpArea,3,row++);
    T.add(inputName,3,row++);
    T.add(inputAddress,3,row++);
    T.add(inputZipCode,3,row++);
    T.add(inputZipArea,3,row++);
    T.add(inputPhone,3,row++);
    T.add(inputInfo,3,row++);
    T.add(inputKeyCode,3,row++);
    T.add(inputLAT,3,row++);
    T.add(inputLON,3,row++);


    T.add(new SubmitButton(iwrb.getLocalizedImageButton("save","Save"),"sch_save_school","true"),3,14);
    Link cancel = new Link(iwrb.getLocalizedImageButton("cancel","Cancel"));
    T.add(cancel,3,14);
    if(Id > 0){
      Link delete = new Link(iwrb.getLocalizedImageButton("delete","Delete"));
      delete.addParameter("sch_delete_school",Id);
      T.add(delete,3,14);
    }

    return T;
  }

  private Collection getSchoolTypes(IWContext iwc)throws java.rmi.RemoteException{
    SchoolTypeBusiness sbuiz = (SchoolTypeBusiness)IBOLookup.getServiceInstance(iwc,SchoolTypeBusiness.class);
    return sbuiz.findAllSchoolTypes();
  }

  private Collection getSchoolAreas(IWContext iwc)throws java.rmi.RemoteException{
    SchoolAreaBusiness abuiz = (SchoolAreaBusiness)IBOLookup.getServiceInstance(iwc,SchoolAreaBusiness.class);
    return abuiz.findAllSchoolAreas();
  }

  public void main(IWContext iwc)throws Exception{
    iwb = getBundle(iwc);
    iwrb = getResourceBundle(iwc);
    tFormat = tFormat.getInstance();
    control(iwc);
  }
}