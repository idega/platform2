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


public class ReportEditor extends Editor{

  private final String sAction = "report_action";
  private String sActPrm = "0";
  private int iAction = 0;
  private String prefix = "reed_";
  private String sLastOrder = "0";
  private int iCategory;


  public ReportEditor(){

  }

  protected void control(ModuleInfo modinfo){
    try{
        iCategory = ReportService.getSessionCategory(modinfo);

        if(modinfo.getParameter(sAction) != null)
          sActPrm = modinfo.getParameter(sAction);
        else
          sActPrm = "0";
        try{
          iAction = Integer.parseInt(sActPrm);
          switch(iAction){
            case ACT1:    break;
            case ACT2: break;
            case ACT3: doChange(modinfo); break;
            case ACT4: doUpdate(modinfo); break;
            default : doMain(modinfo);           break;
          }
        }
        catch(Exception e){
          e.printStackTrace();
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

    int iCategory = 12;
    ReportCondition[] RC = ReportEntityHandler.getConditions(iCategory);
    modinfo.setSessionAttribute(prefix+"force",RC);

    Table T = new Table();
    Form form = new Form();
    form.add(T);
    SelectionDoubleBox box = new SelectionDoubleBox("box","Fields","Order");

    Table U = new Table();
    Table M = new Table();
    Table ML = new Table();
    Table B = new Table();
    M.add(box,1,1);
    M.add(ML,2,1);
    T.add(U,1,1);
    T.add(M,1,2);
    T.add(B,1,3);

    Text nameText = new Text("Name");
    Text infoText = new Text("Info");
    TextInput nameInput = new TextInput(prefix+"name");
    TextInput infoInput = new TextInput(prefix+"info");
    U.add(nameText,1,1);
    U.add(nameInput,1,2);
    U.add(infoText,2,1);
    U.add(infoInput,2,2);

    SelectionBox box1 = box.getLeftBox();
    box1.keepStatusOnAction();
    SelectionBox box2 = box.getRightBox();
    box1.keepStatusOnAction();
    box2.addUpAndDownMovers();
    int a = 1;
    for (int i = 0; i < RC.length; i++) {
      box1.addMenuElement(i,RC[i].getDisplay());
      ModuleObject mo = ReportObjectHandler.getInput(RC[i],prefix+"in"+i,"");
      ML.add(RC[i].getDisplay(),1,a);
      ML.add(mo,2,a++);
    }
    box1.setHeight(20);
    box2.setHeight(20);
    box2.selectAllOnSubmit();
    B.add(new SubmitButton("OK",sAction, String.valueOf(ACT4)));
    this.makeView();
    addMain(form);
  }
  protected void doChange(ModuleInfo modinfo) throws SQLException{

  }

  protected void doUpdate(ModuleInfo modinfo) throws SQLException{
    String[] s = modinfo.getParameterValues("box");

    ReportCondition[] RC = (ReportCondition[])modinfo.getSessionAttribute(prefix+"force");
    Vector vRC = new Vector();
    int len = s.length;
    String[] headers = new String[len];
    for (int i = 0; i < len; i++) {
      ReportCondition rc = RC[Integer.parseInt(s[i])];
      headers[i] = rc.getDisplay();
      rc.setIsSelect();
      vRC.addElement(rc);
    }
    String temp;
    for (int i = 0; i < RC.length; i++) {
      temp = modinfo.getParameter(prefix+"in"+i);
      if(!"".equalsIgnoreCase(temp) && !"0".equals(temp)){
        //add(" check "+i);
        ReportCondition rc = RC[i];
        rc.setVariable("'"+temp+"'");
        vRC.addElement(rc);
      }
    }
    modinfo.removeSessionAttribute(prefix+"force");

    String name = modinfo.getParameter(prefix+"name");
    String info = modinfo.getParameter(prefix+"info");

    name = name != null?name: "";
    info = info != null?info: "";

    ReportMaker rm = new ReportMaker();
    String sql = rm.makeSQL(vRC);
    Report R = new Report();
    R.setCategory(12);
    R.setName(name);
    R.setInfo(info);
    R.setSQL(sql);
    R.setHeaders(headers);
    try{
      R.insert();
    }
    catch(SQLException ex){

    }

    ReportService.setSessionReport(modinfo,R);
    makeAnswer(R);


  }
  public void makeAnswer(Report R){
    Link L = new Link("Skoða","/reports/index.jsp");
    add(L);
    add("<br>");
    add(R.getSQL());
  }
}