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
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.business.*;
import com.idega.util.IWTimeStamp;

import java.util.Collection;
import com.idega.business.IBOLookup;
import java.text.DateFormat;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class SchoolSeasonEditor extends Block {

  IWResourceBundle iwrb;
  IWBundle iwb;
  TextFormat tFormat;
  DateFormat dFormat;

  SchoolSeasonBusiness sabBean;
  public final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.school";

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  private void control(IWContext iwc) throws Exception{
    //debugParameters(iwc);
    initBeans(iwc);
    Form F = new Form();

    if(iwc.isParameterSet("sch_save_season")){
      saveArea(iwc);
      F.add(getListTable(null));
    }
    else if(iwc.isParameterSet("sch_delete_season")){
      int id = Integer.parseInt(iwc.getParameter("sch_delete_season"));
      sabBean.removeSchoolSeason(id);
      F.add(getListTable(null));
    }
    else if(iwc.isParameterSet("sch_school_season_id")){
      int id = Integer.parseInt(iwc.getParameter("sch_school_season_id"));
      F.add(getInput(id));

    }
    else if(iwc.isParameterSet("sch_new_season")){
      F.add(getInput(-1));
    }
    else
      F.add(getListTable(null));

     add(F);

  }

  private void initBeans(IWContext iwc) throws java.rmi.RemoteException,javax.ejb.CreateException{
    sabBean = (SchoolSeasonBusiness) IBOLookup.getServiceInstance(iwc,SchoolSeasonBusiness.class);
    //sabBean = sabHome.create();
  }

  private PresentationObject getInput(int id)throws java.rmi.RemoteException{
    return getInputTable(sabBean.getSchoolSeason(new Integer(id)));
  }

  private void saveArea(IWContext iwc)throws java.rmi.RemoteException{
    if(iwc.isParameterSet("sch_save_season")){
      String id = iwc.getParameter("sch_school_season_id");
      String name = iwc.getParameter("sch_season_name");
      String start = iwc.getParameter("sch_season_start");
      String end = iwc.getParameter("sch_season_end");
      String duedate = iwc.getParameter("sch_season_due_date");
      int aid = -1;
      java.util.Date startDate,endDate,dueDate;
      if(id!=null)
        aid = Integer.parseInt(id);
      if(start!=null);
        startDate = new IWTimeStamp(start).getSQLDate();
      if(end!=null);
        endDate = new IWTimeStamp(end).getSQLDate();
      if(duedate!=null);
        dueDate = new IWTimeStamp(duedate).getSQLDate();
      sabBean.storeSchoolSeason(aid,name,startDate,endDate,dueDate);
    }
  }

  public PresentationObject getListTable(SchoolSeason area) {
    Table T = new Table();
    int row = 1;

    Collection SchoolSeasons = new java.util.Vector(0);
    try{
      SchoolSeasons = sabBean.findAllSchoolSeasons();
    }
    catch(java.rmi.RemoteException rex){

    }
    T.mergeCells(1,1,2,1);
    Link newLink = new Link(iwrb.getLocalizedImageButton("new","New"));
    newLink.addParameter("sch_new_season","true");
    T.add(newLink,1,row);
    row++;
    T.add(tFormat.format(iwrb.getLocalizedString("name","Name"),tFormat.HEADER),2,row);
    T.add(tFormat.format(iwrb.getLocalizedString("start","Start"),tFormat.HEADER),3,row);
    T.add(tFormat.format(iwrb.getLocalizedString("end","End"),tFormat.HEADER),4,row);
    T.add(tFormat.format(iwrb.getLocalizedString("due_date","Duedate"),tFormat.HEADER),5,row);
    row++;

    java.util.Iterator iter = SchoolSeasons.iterator();
    SchoolSeason sarea ;
    while(iter.hasNext()){
      sarea = (SchoolSeason) iter.next();
      try{
      Link L = new Link(tFormat.format("edit"));
      L.addParameter("sch_school_season_id",((Integer)sarea.getPrimaryKey()).intValue());
      T.add(L,1,row);
      T.add(tFormat.format(sarea.getSchoolSeasonName()),2,row);
      T.add(tFormat.format(dFormat.format(sarea.getSchoolSeasonStart())),3,row);
      T.add(tFormat.format(dFormat.format(sarea.getSchoolSeasonEnd())),4,row);
      T.add(tFormat.format(dFormat.format(sarea.getSchoolSeasonDueDate())),5,row);
      }
      catch(Exception ex){}
      row++;
    }
    return T;
  }

  public PresentationObject getInputTable(SchoolSeason bean){
    Table T = new Table(3,6);
    T.mergeCells(1,1,3,1);
    T.add(tFormat.format(iwrb.getLocalizedString("school_season","SchoolSeason"),tFormat.HEADER),1,1);
    T.add(tFormat.format(iwrb.getLocalizedString("name","Name"),tFormat.HEADER),1,2);
    T.add(tFormat.format(iwrb.getLocalizedString("start","Start")),1,3);
    T.add(tFormat.format(iwrb.getLocalizedString("end","End"),tFormat.HEADER),1,4);
    T.add(tFormat.format(iwrb.getLocalizedString("due_date","Duedate"),tFormat.HEADER),1,5);

    TextInput inputName = new TextInput("sch_season_name");
    DateInput inputStart = new DateInput("sch_season_start");
    DateInput inputEnd = new DateInput("sch_season_end");
    DateInput inputDueDate = new DateInput("sch_season_due_date");
    int beanId = -1;
    if(bean!=null){
      try{
      beanId = ((Integer)bean.getPrimaryKey()).intValue();
      inputName.setContent(bean.getSchoolSeasonName());
      inputStart.setDate(bean.getSchoolSeasonStart());
      inputEnd.setDate(bean.getSchoolSeasonEnd());
      inputDueDate.setDate(bean.getSchoolSeasonDueDate());
      T.add(new HiddenInput("sch_school_season_id",String.valueOf(beanId)));
      }
      catch(Exception ex){}
    }

    T.add(inputName,3,2);
    T.add(inputStart,3,3);
    T.add(inputEnd,3,4);
    T.add(inputDueDate,3,5);
    T.add(new SubmitButton(iwrb.getLocalizedImageButton("save","Save"),"sch_save_season","true"),3,6);
    Link cancel = new Link(iwrb.getLocalizedImageButton("cancel","Cancel"));
    T.add(cancel,3,6);
    if(beanId > 0){
      Link delete = new Link(iwrb.getLocalizedImageButton("delete","Delete"));
      delete.addParameter("sch_delete_season",beanId);
      T.add(delete,3,6);
    }

    return T;
  }

  public void main(IWContext iwc)throws Exception{
    iwb = getBundle(iwc);
    iwrb = getResourceBundle(iwc);
    tFormat = tFormat.getInstance();
    dFormat = DateFormat.getDateInstance(dFormat.SHORT,iwc.getCurrentLocale());
    control(iwc);
  }
}