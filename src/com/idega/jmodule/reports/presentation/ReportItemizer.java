package com.idega.jmodule.reports.presentation;

import com.idega.jmodule.reports.data.*;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import java.sql.SQLException;
import com.idega.presentation.Table;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.presentation.Script;
import com.idega.presentation.Editor;
import com.idega.block.reports.presentation.ReportObjectHandler;
import com.idega.block.reports.business.ReportEntityHandler;
import com.idega.presentation.PresentationObject;
import com.idega.data.GenericEntity;


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

  protected void control(IWContext iwc){

    try{
        doSome(iwc);
        doMain(iwc);
        if(iwc.getParameter(sAction) != null){
          sActPrm = iwc.getParameter(sAction);
          try{
            iAction = Integer.parseInt(sActPrm);
            switch(iAction){
              case ACT1:    break;
              case ACT2:    break;
              case ACT3: doChange(iwc); break;
              case ACT4: doUpdate(iwc);        break;
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

  private void doSome(IWContext iwc){
    int id = 0;
    String sIndex = iwc.getParameter("rep_cat_drp");
    if(sIndex != null){
      id = Integer.parseInt(sIndex);
      iwc.setSessionAttribute(prefix+"id",new Integer(id));
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

  private void doMain(IWContext iwc){
    String sIndex = iwc.getParameter("rep_cat_drp");
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

    this.makeView();
    this.addHeader(this.makeLinkTable(0));
    this.addMain(T);
  }
  protected void doChange(IWContext iwc) throws SQLException{
    Form myForm = new Form();

    /*
    ReportItem[] RI;
    try{
    RI = (ReportItem[])new ReportItem().findAll();
    }
    catch(Exception e){RI = new ReportItem[0];}
    int count = RI.length;
    */

    DropdownMenu drp = ReportObjectHandler.drpCategories(prefix+"id","");

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
    myForm.add(drp);
    this.makeView();
    this.addHeader(this.makeLinkTable(0));
    this.addMain(myForm);
  }

 protected void doUpdate(IWContext iwc) throws SQLException{

    int id  = Integer.parseInt(iwc.getParameter(prefix+"id"));
    String name,field,table,joins,jointables,condtype,conddata,condop,entity,info;

    name        = iwc.getParameter(prefix+"name");
    field       = iwc.getParameter(prefix+"field");
    table       = iwc.getParameter(prefix+"table");
    joins       = iwc.getParameter(prefix+"joins");
    jointables  = iwc.getParameter(prefix+"jointables");
    condtype    = iwc.getParameter(prefix+"condtype");
    conddata    = iwc.getParameter(prefix+"conddata");
    condop      = iwc.getParameter(prefix+"condop");
    entity      = iwc.getParameter(prefix+"entity");
    info        = iwc.getParameter(prefix+"info");
    if(id != 0){
    boolean b = ReportEntityHandler.saveReportItem(id,name,field,table,joins, jointables,condtype,conddata,condop,entity,info);
    add(new Boolean(b).toString() );
    }
    add(name+field+table+joins+jointables+condtype+conddata+condop+entity+info);

  }
}