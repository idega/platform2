package com.idega.block.reports.presentation;

import com.idega.block.reports.data.*;
import com.idega.jmodule.object.JModuleObject;
import com.idega.jmodule.object.ModuleInfo;
import java.sql.SQLException;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.Script;
import com.idega.jmodule.object.Editor;
import com.idega.block.reports.presentation.ReportObjectHandler;
import com.idega.block.reports.business.ReportEntityHandler;
import com.idega.jmodule.object.ModuleObject;


public class ReportItemizer extends Editor{

  private final String sAction = "report_action";
  private final String prefix = "rpit_" ;
  private String sActPrm = "0";
  private int iAction = 0;
  private String sName,sInfo;
  private String sIndex;

  public ReportItemizer(){
    sIndex = "0";
    sName = "";
    sInfo = "";
    LightColor = "#D7DADF";
    MiddleColor = "#9fA9B3";
    DarkColor = "#27334B";
    WhiteColor = "#FFFFFF";
    TextFontColor = "#000000";
    HeaderFontColor = DarkColor;
    IndexFontColor = "#000000";

  }

  protected void control(ModuleInfo modinfo){

    add("check");
    try{
        doSome(modinfo);
        doMain(modinfo);
        if(modinfo.getParameter(sAction) != null){
          sActPrm = modinfo.getParameter(sAction);
          try{
            iAction = Integer.parseInt(sActPrm);
            switch(iAction){
              case ACT1:    break;
              case ACT2:    break;
              case ACT3: doChange(modinfo); break;
              case ACT4: doUpdate(modinfo);        break;
            }
          }
          catch(Exception e){
            e.printStackTrace();
          }
        }
    }
    catch(Exception S){
      S.printStackTrace();
    }

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
    Link1.addParameter(this.sAction,String.valueOf(this.ACT3));
    Link Link2 = new Link("Breyta");
    Link2.setFontColor(this.LightColor);
    Link2.addParameter(this.sAction,String.valueOf(this.ACT2));
    if(isAdmin){
      LinkTable.add(Link1,1,1);
      LinkTable.add(Link2,2,1);
    }
    return LinkTable;
  }

  private void doSome(ModuleInfo modinfo){
    int id = 0;
    String sIndex = modinfo.getParameter("rep_cat_drp");
    if(sIndex != null){
      id = Integer.parseInt(sIndex);
      modinfo.setSessionAttribute(prefix+"id",new Integer(id));
      if(id != 0){
        try {
          ReportCategory RC = new ReportCategory(id);
          sName = RC.getName();
          sInfo = RC.getInfo();
        }
        catch (Exception ex) {
        }
      }
    }
  }

  private void doMain(ModuleInfo modinfo){
    String sIndex = modinfo.getParameter("rep_cat_drp");
    Table T = new Table();
    Form myForm = new Form();
    if(sIndex==null)
      sIndex = "0";
    DropdownMenu drp = ReportObjectHandler.drpCategories("rep_cat_drp",sIndex);
    drp.setToSubmit();
    Text Name = new Text(sName);
    Text Info = new Text(sInfo);

    Table T2 = new Table();

    T2.add("Flokkur",1,1);
    T2.add(drp,1,2);
    T2.add("Name:",2,1);
    T2.add(Name,2,2);
    T2.add("Info:",3,1);
    T2.add(Info,3,2);

    myForm.add(T2);
    T.add(myForm);
    Link L = new Link("sjá");
    L.addParameter(this.sAction,String.valueOf(this.ACT3));
    add(L);
    this.makeView();
    this.addHeader(this.makeLinkTable(0));
    this.addMain(T);
  }
  protected void doChange(ModuleInfo modinfo) throws SQLException{
    Form myForm = new Form();
    int id  = ((Integer)modinfo.getSessionAttribute(prefix+"id")).intValue();
    add(""+id);
    /*
    ReportItem[] RI;
    try{
    RI = (ReportItem[])new ReportItem().findAll();
    }
    catch(Exception e){RI = new ReportItem[0];}
    int count = RI.length;
    */

    Table T =  new Table(2,11);
    T.setCellpadding(2);
    T.setCellspacing(1);
    T.setHorizontalZebraColored(LightColor,WhiteColor);
    T.setRowColor(1,MiddleColor);
    int a = 1;
    T.add(formatText("Property"),1,a);
    T.add(formatText("Value"),2,a++);
    T.add(formatText("Name"),1,a++);
    T.add(formatText("Field"),1,a++);
    T.add(formatText("Maintable"),1,a++);
    T.add(formatText("Joins"),1,a++);
    T.add(formatText("Join Tables"),1,a++);
    T.add(formatText("Condition Type"),1,a++);
    T.add(formatText("Condition Data"),1,a++);
    T.add(formatText("Condition Operator"),1,a++);
    T.add(formatText("Entity Class"),1,a++);
    T.add(formatText("Information"),1,a++);


    String s = "";
    TextInput name,field,table,joins,jointables,
              condtype,conddata,condop,entity,info;
    HiddenInput idInput;
    CheckBox delCheck;

    name        = new TextInput(prefix+"name");
    field       = new TextInput(prefix+"field");
    table       = new TextInput(prefix+"table");
    joins       = new TextInput(prefix+"joins");
    jointables  = new TextInput(prefix+"jointables");
    condtype    = new TextInput(prefix+"condtype");
    conddata    = new TextInput(prefix+"conddata");
    condop      = new TextInput(prefix+"condop");
    entity      = new TextInput(prefix+"entity");
    info        = new TextInput(prefix+"info");

    int tlen = 80;
    name.setSize(tlen);
    field.setSize(tlen);
    table.setSize(tlen);
    joins.setSize(tlen);
    jointables.setSize(tlen);
    condtype.setSize(tlen);
    conddata.setSize(tlen);
    condop.setSize(tlen);
    entity.setSize(tlen);
    info.setSize(tlen);

    setStyle(name);
    setStyle(field);
    setStyle(table);
    setStyle(joins);
    setStyle(jointables);
    setStyle(condtype);
    setStyle(conddata);
    setStyle(condop);
    setStyle(entity);
    setStyle(info);

    int col = 2;
    int row = 2;
    T.add(name,col,row++);
    T.add(field,col,row++);
    T.add(table,col,row++);
    T.add(joins,col,row++);
    T.add(jointables,col,row++);
    T.add(condtype,col,row++);
    T.add(conddata,col,row++);
    T.add(condop,col,row++);
    T.add(entity,col,row++);
    T.add(info,col,row++);

    myForm.add(T);
    myForm.add(new SubmitButton("Vista",this.sAction,String.valueOf(this.ACT4 )));
    this.makeView();
    this.addHeader(this.makeLinkTable(0));
    this.addMain(myForm);
  }

 protected void doUpdate(ModuleInfo modinfo) throws SQLException{

    int id  = ((Integer)modinfo.getSessionAttribute(prefix+"id")).intValue();
    String name,field,table,joins,jointables,condtype,conddata,condop,entity,info;

    name        = modinfo.getParameter(prefix+"name");
    field       = modinfo.getParameter(prefix+"field");
    table       = modinfo.getParameter(prefix+"table");
    joins       = modinfo.getParameter(prefix+"joins");
    jointables  = modinfo.getParameter(prefix+"jointables");
    condtype    = modinfo.getParameter(prefix+"condtype");
    conddata    = modinfo.getParameter(prefix+"conddata");
    condop      = modinfo.getParameter(prefix+"condop");
    entity      = modinfo.getParameter(prefix+"entity");
    info        = modinfo.getParameter(prefix+"info");

    if(id!=0){
     boolean b = ReportEntityHandler.saveReportItem(id,name,field,table,joins, jointables,condtype,conddata,condop,entity,info);
    }
  }
}