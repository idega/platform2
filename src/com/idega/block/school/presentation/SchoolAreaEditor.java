package com.idega.block.school.presentation;

import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.presentation.Table;
import com.idega.presentation.PresentationObject;
import com.idega.util.text.TextFormat;
import com.idega.block.school.data.SchoolArea;
import com.idega.block.school.business.SchoolAreaBusiness;

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

public class SchoolAreaEditor extends Block {

  IWResourceBundle iwrb;
  IWBundle iwb;
  TextFormat tFormat;

  SchoolAreaBusiness sabBean;
  public final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.school";

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  private void control(IWContext iwc) throws Exception{
    //debugParameters(iwc);
    initBeans(iwc);
    Form F = new Form();

    if(iwc.isParameterSet("sch_save_area")){
      saveArea(iwc);
      F.add(getListTable(null));
    }
    else if(iwc.isParameterSet("sch_delete_area")){
      int id = Integer.parseInt(iwc.getParameter("sch_delete_area"));
      sabBean.removeSchoolArea(id);
      F.add(getListTable(null));
    }
    else if(iwc.isParameterSet("sch_school_area_id")){
      int id = Integer.parseInt(iwc.getParameter("sch_school_area_id"));
      F.add(getInput(id));

    }
    else if(iwc.isParameterSet("sch_new_area")){
      F.add(getInput(-1));
    }
    else
      F.add(getListTable(null));

     add(F);

  }

  private void initBeans(IWContext iwc) throws java.rmi.RemoteException,javax.ejb.CreateException{
    sabBean = (SchoolAreaBusiness) IBOLookup.getServiceInstance(iwc,SchoolAreaBusiness.class);
    //sabBean = sabHome.create();
  }

  private PresentationObject getInput(int id)throws java.rmi.RemoteException{
    return getInputTable(sabBean.getSchoolArea(new Integer(id)));
  }

  private void saveArea(IWContext iwc)throws java.rmi.RemoteException{
    if(iwc.isParameterSet("sch_save_area")){
      String id = iwc.getParameter("sch_school_area_id");
      String name = iwc.getParameter("sch_area_name");
      String city = iwc.getParameter("sch_area_city");
      String info = iwc.getParameter("sch_area_info");
      int aid = -1;
      if(id!=null)
        aid = Integer.parseInt(id);
      sabBean.storeSchoolArea(aid,name,info,city);
    }
  }

  public PresentationObject getListTable(SchoolArea area) {
    Table T = new Table();
    int row = 1;

    Collection schoolAreas = new java.util.Vector(0);
    try{
      schoolAreas = sabBean.findAllSchoolAreas();
    }
    catch(java.rmi.RemoteException rex){

    }
    T.mergeCells(1,1,2,1);
    Link newLink = new Link(iwrb.getLocalizedImageButton("new","New"));
    newLink.addParameter("sch_new_area","true");
    T.add(newLink,1,row);
    row++;
    T.add(tFormat.format(iwrb.getLocalizedString("name","Name"),tFormat.HEADER),2,row);
    T.add(tFormat.format(iwrb.getLocalizedString("city","City"),tFormat.HEADER),3,row);
    row++;

    java.util.Iterator iter = schoolAreas.iterator();
    SchoolArea sarea ;
    while(iter.hasNext()){
      sarea = (SchoolArea) iter.next();
      try{
      Link L = new Link(tFormat.format("edit"));
      L.addParameter("sch_school_area_id",((Integer)sarea.getPrimaryKey()).intValue());
      T.add(L,1,row);
      T.add(tFormat.format(sarea.getSchoolAreaName()),2,row);
      T.add(tFormat.format(sarea.getSchoolAreaCity()),3,row);
      }
      catch(Exception ex){}
      row++;
    }
    return T;
  }

  public PresentationObject getInputTable(SchoolArea area){
    Table T = new Table(3,6);
    T.mergeCells(1,1,3,1);
    T.add(tFormat.format(iwrb.getLocalizedString("school_area","SchoolArea"),tFormat.TITLE),1,1);
    T.add(tFormat.format(iwrb.getLocalizedString("name","Name")),1,2);
    T.add(tFormat.format(iwrb.getLocalizedString("city","City")),1,3);
    T.add(tFormat.format(iwrb.getLocalizedString("info","Info")),1,4);

    TextInput inputName = new TextInput("sch_area_name");
    TextInput inputCity = new TextInput("sch_area_city");
    TextArea inputInfo = new TextArea("sch_area_info");
    int areaId = -1;
    if(area!=null){
      try{
      areaId = ((Integer)area.getPrimaryKey()).intValue();
      inputName.setContent(area.getSchoolAreaName());
      inputCity.setContent(area.getSchoolAreaCity());
      inputInfo.setContent(area.getSchoolAreaInfo());
      T.add(new HiddenInput("sch_school_area_id",String.valueOf(areaId)));
      }
      catch(Exception ex){}
    }

    T.add(inputName,3,2);
    T.add(inputCity,3,3);
    T.add(inputInfo,3,4);
    T.add(new SubmitButton(iwrb.getLocalizedImageButton("save","Save"),"sch_save_area","true"),3,5);
    Link cancel = new Link(iwrb.getLocalizedImageButton("cancel","Cancel"));
    T.add(cancel,3,5);
    if(areaId > 0){
      Link delete = new Link(iwrb.getLocalizedImageButton("delete","Delete"));
      delete.addParameter("sch_delete_area",areaId);
      T.add(delete,3,5);
    }

    return T;
  }

  public void main(IWContext iwc)throws Exception{
    iwb = getBundle(iwc);
    iwrb = getResourceBundle(iwc);
    tFormat = tFormat.getInstance();
    control(iwc);
  }
}