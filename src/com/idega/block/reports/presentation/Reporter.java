package com.idega.block.reports.presentation;

import com.idega.block.reports.data.*;
import com.idega.block.reports.business.*;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObjectContainer;
import java.sql.SQLException;
import java.util.Vector;
import java.util.Collections;
import java.util.List;
import com.idega.data.EntityFinder;
import com.idega.presentation.Editor;
import com.idega.presentation.Table;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.presentation.Script;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Image;
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


public class Reporter extends ReportPresentation{

  private final String sAction = "rep.reporter.action";
  private String sActPrm = "";
  private int iAction = 0;
  private static final String prefix = "rep.reporter.";
  private String sLastOrder = "0";
  private int iMainCategories[] = null;
  private int iViewCategories[] = null;
  private int iCategory = -1;
  private int[] iCategories = null;
  private boolean sqlEditAdmin = false;
  private String sMainCategoryAttribute = null,sViewCategoryAttribute = null;
  private int iMainCategoryAttributeId = 0, iViewCategoryAttributeId= 0;

  private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.reports";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;


  public Reporter(){
   super();
  }
  public Reporter(int iMainCategory){
    this();
    iCategory = iMainCategory;
    this.setMainCategory(iMainCategory);
  }
  public void setMainCategory(int iMainCategory){
    this.iMainCategories = new int[1];
    this.iMainCategories[0] = iMainCategory;
  }
  public void setViewCategory(int iViewCategory){
    this.iViewCategories = new int[1];
    this.iViewCategories[0] = iViewCategory;
  }
  public void setMainCategories(int[] iMainCategories){
    this.iMainCategories= iMainCategories;
  }
  public void setViewCategories(int[] iViewCategories){
    this.iViewCategories = iViewCategories;
  }
  public void setSQLEdit(boolean value){
    sqlEditAdmin = value;
  }
  public void setMainCategoryAttribute(String name,int id){
    sMainCategoryAttribute = name;
    iMainCategoryAttributeId = id;
  }
  public void setMainCategoryAttribute(String name){
    sMainCategoryAttribute = name;
  }
  public void setMainCategoryAttributeId(int id){
    iMainCategoryAttributeId = id;
  }
  public void setViewCategoryAttribute(String name,int id){
    sViewCategoryAttribute = name;
    iViewCategoryAttributeId = id;
  }
  public void setViewCategoryAttribute(String name){
    sViewCategoryAttribute = name;
  }
  public void setViewCategoryAttributeId(int id){
    iViewCategoryAttributeId = id;
  }

  private void checkCategories(IWContext iwc){
    ReportCategoryAttribute RCA = null;
    if(sMainCategoryAttribute != null){
      List L = null;
      try{
        if(iMainCategoryAttributeId!= 0){
          L = EntityFinder.findAllByColumn(new ReportCategoryAttribute(),"ATTRIBUTE_NAME",sMainCategoryAttribute,"ATTRIBUTE_ID",String.valueOf(iMainCategoryAttributeId));
        }
        else
          L = EntityFinder.findAllByColumn(new ReportCategoryAttribute(),"ATTRIBUTE_NAME",sMainCategoryAttribute);
      }
      catch(SQLException sql){sql.printStackTrace();}

      if(L != null){
        iMainCategories = new int[L.size()];
        for (int i = 0; i < L.size(); i++) {
          RCA = (ReportCategoryAttribute)L.get(i);
          iMainCategories[i] = RCA.getReportCategoryId();
        }
      }
    }
    if(sViewCategoryAttribute != null){
      List K = null;
      try{
        if(iViewCategoryAttributeId!= 0){
          K = EntityFinder.findAllByColumn(new ReportCategoryAttribute(),"ATTRIBUTE_NAME",sViewCategoryAttribute,"ATTRIBUTE_ID",String.valueOf(iViewCategoryAttributeId));
        }
        else
          K = EntityFinder.findAllByColumn(new ReportCategoryAttribute(),"ATTRIBUTE_NAME",sViewCategoryAttribute);
      }
      catch(SQLException sql){sql.printStackTrace();}

      if(K!= null){
        iViewCategories = new int[K.size()];
        for (int i = 0; i < K.size(); i++) {
          RCA = (ReportCategoryAttribute)K.get(i);
          iViewCategories[i] = RCA.getReportCategoryId();
        }
      }
    }
  }

  protected void control(IWContext iwc){
    iwrb = getResourceBundle(iwc);
		iwb = getBundle(iwc);
    Form form = new Form();
    checkCategories(iwc);
    if(iwc.getParameter(sAction) != null){
      sActPrm = iwc.getParameter(sAction);
      try{
        iAction = Integer.parseInt(sActPrm);
        switch(iAction){
          case ACT1:  break;
          case ACT2:  break;
          case ACT3: doChange(iwc); break;
          case ACT4: doDelete(iwc); break;
        }
      }
      catch(Exception e){
        e.printStackTrace();
      }
    }
    form.add(doMain(iwc));
    add(form);
  }


  protected PresentationObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(3,1);
    int last = 3;
    LinkTable.setWidth("100%");
    LinkTable.setCellpadding(2);
    LinkTable.setCellspacing(1);
    LinkTable.setColor(this.DarkColor);
    LinkTable.setWidth(last,"100%");
    Link Link1 = new Link("Yfirlit");
    Link1.setFontColor(this.LightColor);
    Link1.addParameter(this.sAction,String.valueOf(this.ACT1));
    Link Link2 = new Link("Breyta");
    Link2.setFontColor(this.LightColor);
    Link2.addParameter(this.sAction,String.valueOf(this.ACT2));
    if(isAdmin){
      LinkTable.add(Link1,1,1);
      LinkTable.add(Link2,2,1);
    }
    return LinkTable;
  }

  private PresentationObject doMain(IWContext iwc){
    Table T = new Table();
    T.setWidth("100%");
    int a = 1;
    if(iMainCategories != null){
      for (int i = 0; i < iMainCategories.length; i++) {
        if(i==0)
          iCategory = iMainCategories[i];
        T.add(getCategoryReports(iMainCategories[i],true),1,a++);
      }
    }
    if(iViewCategories != null){
      for (int i = 0; i < iViewCategories.length; i++) {
        T.add(getCategoryReports(iViewCategories[i],false),1,a++);
      }
    }
    TextFontColor = DarkColor;
    fontBold = true;
    return T;
  }

  private PresentationObject getCategoryReports(int iCategoryId,boolean bEdit){
    ReportCategory RC = null;
    try {
      RC = new ReportCategory(iCategoryId);
    }
    catch (Exception ex) {

    }
    Table T = new Table();

      T.setCellpadding(2);
      T.setCellspacing(1);
      T.setBorder(0);
      Link lEdit =  new Link(iwb.getImage("/shared/new.gif"));//new Image("/reports/pics/new.gif"));
      lEdit.setWindowToOpen(ReportEditorWindow.class);
      lEdit.addParameter(ReportEditorWindow.prmSaveCategory,iCategory);

      if(RC !=null){
        T.add(formatText(RC.getName()),2,1);
        T.add(formatText(RC.getInfo()),3,1);
      }
      TextFontColor = "#FFFFFF";
      int a = 2;

      T.setColumnAlignment(1,"right");
      T.setWidth(3,"100%");
      T.add(lEdit,1,2);
      T.add(formatText(iwrb.getLocalizedString("name","Name")),2,2);
      T.add(formatText(iwrb.getLocalizedString("info","Info")),3,2);
      String prm = prefix+"chk";
      if(bEdit){
        T.setWidth(4,"40");
        T.setColumnAlignment(4,"center");
        T.add(formatText(iwrb.getLocalizedString("delete","Delete")),4,2);

      }
      TextFontColor = "#000000";
      List L = ReportEntityHandler.listOfReports(iCategoryId);
      if(L!=null){

        int len = L.size();

        for (int i = 0; i < len; i++) {
          Report R = (Report) L.get(i);
          int col = 1;
          int row = i+3;
          if(sqlEditAdmin){
            T.add(getAdminLink(R.getID(),iCategoryId),col,row);
          }
          T.add(getLink(R.getID()),col,row);

          col++;
          T.add(formatText(R.getName()),col++,row);
          T.add(formatText(R.getInfo()),col++,row);
          if(bEdit)
            T.add(getCheckBox(prm+i,R.getID()),col++,row);
        }
        T.add(new HiddenInput(this.sAction,String.valueOf(this.ACT4)));
        if(bEdit){
          SubmitButton deleteButtton = new SubmitButton(iwb.getImage("/shared/delete.gif"));//new Image("/reports/pics/delete.gif"));
          T.add(new HiddenInput(this.sAction,String.valueOf(this.ACT4)));
          T.add(deleteButtton,4,len+3);
          HiddenInput countHidden = new HiddenInput(prefix+"count",String.valueOf(len));
          T.add(countHidden);
        }
        T.setRowColor(len+3,WhiteColor);
      }

      T.setHorizontalZebraColored(this.LightColor,this.MiddleColor);
      T.setRowColor(1,WhiteColor);
      T.setRowColor(2,DarkColor);

      T.setColumnColor(1,WhiteColor);
      T.setWidth("100%");
      T.setWidth(1,"40");
      T.setWidth(2,"150");
      //T.setWidth(3,"50%");
      T.setColumnAlignment(1,"center");

    return T;
  }

  private CheckBox getCheckBox(String name,int id){
    CheckBox chk = new CheckBox(name,String.valueOf(id));
    setStyle(chk);
    return chk;
  }

  private Link getLink(int id){
    Link L = new Link(iwb.getImage("/shared/view.gif"));// Image("/reports/pics/view.gif"));
    L.setWindowToOpen(ReportViewWindow.class);
    L.addParameter(ReportViewWindow.prmReportId,id);
    return L;
  }
  private Link getAdminLink(int id,int catid){
    Link L = new Link(iwb.getImage("/shared/edit.gif"));//new Image("/reports/pics/edit.gif"));
    L.setWindowToOpen(ReportEditorWindow.class);
    L.addParameter(ReportEditorWindow.prmReportId,id);
    L.addParameter(ReportEditorWindow.prmSaveCategory,catid);
    return L;
  }

  protected void doChange(IWContext iwc) throws SQLException{

  }
  protected void doUpdate(IWContext iwc) throws SQLException{

  }

  protected void doDelete(IWContext iwc) throws SQLException{
    String prm = prefix+"chk";
    int count = Integer.parseInt(iwc.getParameter(prefix+"count"));
    for (int i = 0; i < count; i++) {
      if(iwc.getParameter(prm+i)!=null){
        String sId = iwc.getParameter(prm+i);
        try{
          int id = Integer.parseInt(sId);
          Report R = new Report(id);
          R.delete();
        }
        catch(NumberFormatException ex){}
        catch(SQLException ex){}
      }
    }
  }


}
