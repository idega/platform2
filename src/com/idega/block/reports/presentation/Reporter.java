package com.idega.block.reports.presentation;

import com.idega.block.reports.data.*;
import com.idega.block.reports.business.*;
import com.idega.jmodule.object.JModuleObject;
import com.idega.jmodule.object.ModuleInfo;
import java.sql.SQLException;
import java.util.Vector;
import java.util.Collections;
import com.idega.jmodule.object.Editor;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.Script;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.Image;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */


public class Reporter extends Editor{

  private final String sAction = "reporter.action";
  private String sActPrm = "";
  private int iAction = 0;
  private String prefix = "reporter.";
  private String sLastOrder = "0";
  private int iCategory = -1;


  public Reporter(){

  }
  public Reporter(int category){
    this.iCategory = category;
  }

  private void checkCategory(ModuleInfo modinfo){
    if(ReportService.isSessionCategory(modinfo))
      iCategory = ReportService.getSessionCategory(modinfo);
    else
      ReportService.setSessionCategory(modinfo,iCategory);
  }

  protected void control(ModuleInfo modinfo){
    checkCategory(modinfo);
    if(modinfo.getParameter(sAction) != null){
      sActPrm = modinfo.getParameter(sAction);
      try{
        iAction = Integer.parseInt(sActPrm);
        switch(iAction){
          case ACT1:  break;
          case ACT2:  break;
          case ACT3: doChange(modinfo); break;
          case ACT4: doDelete(modinfo); break;
        }
      }
      catch(Exception e){
        e.printStackTrace();
      }
    }
    doMain(modinfo);
  }

  protected ModuleObject makeLinkTable(int menuNr){
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

  private void doMain(ModuleInfo modinfo){
    this.makeView();
    if(iCategory != -1){
      Report[] R = ReportEntityHandler.findReports(iCategory);
      //modinfo.setSessionAttribute(prefix+"force",R);
      int len = R.length;
      Table T = new Table(4,len+2);
      T.setCellpadding(2);
      T.setCellspacing(1);
      T.setBorder(0);
      T.setHorizontalZebraColored(this.LightColor,this.MiddleColor);

      T.setRowColor(1,DarkColor);
      T.setRowColor(len+2,WhiteColor);
      T.setColumnColor(1,WhiteColor);
      T.setWidth("100%");
      T.setWidth(1,"20");
      T.setWidth(3,"50%");
      T.setWidth(4,"40");
      T.setColumnAlignment(1,"center");
      T.setColumnAlignment(4,"center");

      this.setTextFontColor("#FFFFFF");
      int a = 2;

      T.add(formatText("Name"),a++,1);
      T.add(formatText("Info"),a++,1);
      T.add(formatText("Delete"),a++,1);
      String prm = prefix+"chk";
      HiddenInput countHidden = new HiddenInput(prefix+"count",String.valueOf(len));
      T.add(countHidden);
      this.setTextFontColor("#000000");
      for (int i = 0; i < len; i++) {
        int col = 1;
        int row = i+2;
        T.add(getLink(R[i].getID()),col++,row);
        T.add(formatText(R[i].getName()),col++,row);
        T.add(formatText(R[i].getInfo()),col++,row);
        T.add(getCheckBox(prm+i,R[i].getID()),col++,row);

      }
      SubmitButton deleteButtton = new SubmitButton(new Image("/reports/pics/delete.gif"));
      T.add(new HiddenInput(this.sAction,String.valueOf(this.ACT4)));
      T.add(deleteButtton,4,len+2);
      Form form = new Form();
      Link back =  new Link(new Image("/reports/pics/new.gif"),"/reports/reportedit.jsp");
      form.add(T);
      addToHeader(back);
      this.setTextFontColor(DarkColor);
      this.setTextFontBold(true);
      addToHeader(formatText(" Reports"));
      addMain(form);
    }
    else
      addMain(new Text("No Category available"));
  }

  private CheckBox getCheckBox(String name,int id){
    CheckBox chk = new CheckBox(name,String.valueOf(id));
    return chk;
  }

  private Link getLink(int id){
    Link L = new Link(new Image("/reports/pics/view.gif"),"/reports/reportview.jsp");
    L.addParameter("report",id);
    return L;
  }
  protected void doChange(ModuleInfo modinfo) throws SQLException{

  }
  protected void doUpdate(ModuleInfo modinfo) throws SQLException{

  }

  protected void doDelete(ModuleInfo modinfo) throws SQLException{
    String prm = prefix+"chk";
    int count = Integer.parseInt(modinfo.getParameter(prefix+"count"));
    for (int i = 0; i < count; i++) {
      if(modinfo.getParameter(prm+i)!=null){
        String sId = modinfo.getParameter(prm+i);
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
