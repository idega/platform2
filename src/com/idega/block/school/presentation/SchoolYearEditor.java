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
import com.idega.block.school.data.SchoolYear;
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

public class SchoolYearEditor extends Block {

  IWResourceBundle iwrb;
  IWBundle iwb;
  TextFormat tFormat;

  SchoolBusiness sabBean;
  public final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.school";

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  private void control(IWContext iwc) throws Exception{
    debugParameters(iwc);
    initBeans(iwc);
    Form F = new Form();

    if(iwc.isParameterSet("sch_save_year")){
      saveYear(iwc);
      F.add(getListTable(null));
    }
    else if(iwc.isParameterSet("sch_delete_year")){
      int id = Integer.parseInt(iwc.getParameter("sch_delete_year"));
      sabBean.removeSchoolYear(id);
      F.add(getListTable(null));
    }
    else if(iwc.isParameterSet("sch_school_year_id")){
      int id =Integer.parseInt(iwc.getParameter("sch_school_year_id"));
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
    sabBean = (SchoolBusiness) IBOLookup.getServiceInstance(iwc,SchoolBusiness.class);
    //sabBean = sabHome.create();
  }

  private PresentationObject getInput(int id)throws java.rmi.RemoteException{
    return getInputTable(sabBean.getSchoolYear(new Integer(id)));
  }

  private void saveYear(IWContext iwc)throws java.rmi.RemoteException{
    if(iwc.isParameterSet("sch_save_year")){
      String id = iwc.getParameter("sch_school_year_id");
      String name = iwc.getParameter("sch_year_name");
      String age = iwc.getParameter("sch_year_age");
      String info = iwc.getParameter("sch_year_info");
      int iAge = -1,sid = -1;
      if(id !=null)
        sid = Integer.parseInt(id);
      if(age!=null)
        iAge = Integer.parseInt(age);
      sabBean.storeSchoolYear(sid,name,info,iAge);
    }
  }

  public PresentationObject getListTable(SchoolYear area) {
    Table T = new Table();
    int row = 1;

    Collection SchoolYears = new java.util.Vector(0);
    try{
      SchoolYears = sabBean.findAllSchoolYears();
    }
    catch(java.rmi.RemoteException rex){

    }
    T.mergeCells(1,1,2,1);
    Link newLink = new Link(iwrb.getLocalizedImageButton("new","New"));
    newLink.addParameter("sch_new_area","true");
    T.add(newLink,1,row);
    row++;
    T.add(tFormat.format(iwrb.getLocalizedString("name","Name"),tFormat.HEADER),2,row);
    T.add(tFormat.format(iwrb.getLocalizedString("info","Info"),tFormat.HEADER),3,row);
    T.add(tFormat.format(iwrb.getLocalizedString("age","Age"),tFormat.HEADER),4,row);
    row++;

    java.util.Iterator iter = SchoolYears.iterator();
    SchoolYear sarea ;
    while(iter.hasNext()){
      sarea = (SchoolYear) iter.next();
      try{
      Link L = new Link(tFormat.format("edit"));
      L.addParameter("sch_school_year_id",((Integer)sarea.getPrimaryKey()).intValue());
      T.add(L,1,row);
      T.add(tFormat.format(sarea.getSchoolYearName()),2,row);
      T.add(tFormat.format(sarea.getSchoolYearInfo()),3,row);
      T.add(tFormat.format(sarea.getSchoolYearAge()),4,row);
      }
      catch(Exception ex){}
      row++;
    }
    return T;
  }

  public PresentationObject getInputTable(SchoolYear entity){
    Table T = new Table(3,6);
    T.mergeCells(1,1,3,1);
    T.add(tFormat.format(iwrb.getLocalizedString("school_area","SchoolYear"),tFormat.TITLE),1,1);
    T.add(tFormat.format(iwrb.getLocalizedString("name","Name")),1,2);
    T.add(tFormat.format(iwrb.getLocalizedString("info","Info")),1,3);
    T.add(tFormat.format(iwrb.getLocalizedString("age","Age")),1,4);

    TextInput inputName = new TextInput("sch_year_name");
    TextInput inputAge = new TextInput("sch_year_age");
    TextArea inputInfo = new TextArea("sch_year_info");
    int beanId = -1;
    if(entity!=null){
      try{
      beanId = ((Integer)entity.getPrimaryKey()).intValue();
      inputName.setContent(entity.getSchoolYearName());
      inputInfo.setContent(entity.getSchoolYearInfo());
      inputAge.setContent(String.valueOf(entity.getSchoolYearAge()));
      T.add(new HiddenInput("sch_school_year_id",String.valueOf(beanId)));
      }
      catch(Exception ex){}
    }

    T.add(inputName,3,2);
    T.add(inputInfo,3,3);
    T.add(inputAge,3,4);
    T.add(new SubmitButton(iwrb.getLocalizedImageButton("save","Save"),"sch_save_year","true"),3,5);
    Link cancel = new Link(iwrb.getLocalizedImageButton("cancel","Cancel"));
    T.add(cancel,3,5);
    if(beanId>0){
      Link delete = new Link(iwrb.getLocalizedImageButton("delete","Delete"));
      delete.addParameter("sch_delete_year",beanId);
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