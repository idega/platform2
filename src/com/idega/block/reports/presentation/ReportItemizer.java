package com.idega.block.reports.presentation;

import com.idega.block.reports.data.*;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObjectContainer;
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
import com.idega.data.EntityFinder;
import java.util.List;


public class ReportItemizer extends ReportPresentation{

  private final String sAction = "report_action";
  private final String prefix = "rpit_" ;
  private final String sSessPrm = "rep_category";
  private  int iCategoryId = -1;
  private String sActPrm = "0";
  private int iAction = 0;
  private String sName,sInfo;
  private String sIndex;
  private int iCatId = 0;

  public ReportItemizer(){
    super();
    sIndex = "0";
    sName = "";
    sInfo = "";
  }

  protected void control(IWContext iwc){
    if(isAdmin){
      try{
        Form F = new Form();
        Table T = new Table();
        T.add(this.makeLinkTable(0),1,1);
        T.add(getCategoryTable(iwc),1,2);
        if(iwc.getParameter("risave")!=null){
          doUpdate(iwc);
        }
        else if(iwc.getParameter("ea_apply")!= null || iwc.getParameter("ea_ok")!= null){
          doUpdateEntityForm(iwc);
        }

        if(iwc.getParameter(sAction) != null){
          sActPrm = iwc.getParameter(sAction);
          try{
            iAction = Integer.parseInt(sActPrm);
            switch(iAction){
              case ACT1: T.add(doEntityAdd(iwc),1,3);    break;
              case ACT2: T.add(doView(iwc),1,3);         break;
              case ACT3: T.add(doChange(iwc),1,3);       break;
            }
          }
          catch(Exception e){
            e.printStackTrace();
          }
        }
        else{
          T.add(doView(iwc),1,3);
        }
        F.add(T);
        add(F);
      }
      catch(Exception S){
        S.printStackTrace();
      }
    }
    else{
      add("access denied");
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
    Link Link1 = new Link("New");
    Link1.setFontColor(this.LightColor);
    Link1.addParameter(this.sAction,String.valueOf(this.ACT3));
    Link Link2 = new Link("View");
    Link2.setFontColor(this.LightColor);
    Link2.addParameter(this.sAction,String.valueOf(this.ACT2));
    Link Link3 = new Link("Entity");
    Link3.setFontColor(this.LightColor);
    Link3.addParameter(this.sAction,String.valueOf(this.ACT1));
    if(isAdmin){
      LinkTable.add(Link1,1,1);
      LinkTable.add(Link2,2,1);
      LinkTable.add(Link3,2,1);
    }
    return LinkTable;
  }

  private void doSome(IWContext iwc){
    int id = 0;
    String sIndex = iwc.getParameter("rep.cat.drp");
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
    if(sIndex==null)
      sIndex = "0";
    add(T);
  }

  private PresentationObject doView(IWContext iwc){
    List L = null;
    try{
      L = EntityFinder.findAllByColumn(new ReportItem(),ReportItem.getColumnNameCategory(),iCategoryId);
    }
    catch(Exception e){L = null;}
    Table T = new Table();
    if(L != null){
      int count = L.size();
      for (int i = 0; i < count; i++) {
        int a = i+2;
        int b = 1;
        T.add(formatText(i+1),b++,a);
        ReportItem RI = (ReportItem) L.get(i);
        Link link = new Link(RI.getName());
        link.addParameter(sAction,ACT3);
        link.addParameter("repitemid",RI.getID());
        link.addParameter("rep.cat.drp",iCategoryId);
        T.add(link,b++,a);
      }
      T.setWidth("100%");
      T.setHorizontalZebraColored(LightColor,WhiteColor);
      T.setRowColor(1,MiddleColor);
    }
    return T;
  }

  private PresentationObject getCategoryTable(IWContext iwc){
    String sCatId = iwc.getParameter("rep.cat.drp");
    if(sCatId != null){
      int iCatId = Integer.parseInt(sCatId);
      iCategoryId = iCatId;
      iwc.setSessionAttribute(sSessPrm,new Integer(iCategoryId));
    }
    else if(iwc.getSessionAttribute(sSessPrm )!=null){
      iCategoryId = ((Integer)iwc.getSessionAttribute(sSessPrm )).intValue();
      sCatId = String.valueOf(iCategoryId);
    }
    Table T = new Table();
    DropdownMenu drp = ReportObjectHandler.drpCategories("rep.cat.drp",sCatId);
    drp.setToSubmit();
    setStyle(drp);
    T.add(drp);
    return T;
  }

  protected PresentationObject doChange(IWContext iwc) throws SQLException{
    String sRepItemId = iwc.getParameter("repitemid");
    Table Frame = new Table(2,1);
    Frame.setRowVerticalAlignment(1,"top");
    Table T =  new Table(2,12);
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
    T.add(formatText("Is Select"),1,a++);

    String s = "";
    TextInput name,field,table,joins,jointables,
              condtype,conddata,condop,entity,info;
    HiddenInput idInput;
    CheckBox delCheck,isSelect;

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

    if(sRepItemId != null){
      int repItemId = Integer.parseInt(sRepItemId);
      if(repItemId > 0){
        try {
          ReportItem ri = new ReportItem(repItemId );
          name.setContent(ri.getName());
          field.setContent(ri.getField());
          table.setContent(ri.getMainTable());
          joins.setContent(ri.getJoin());
          jointables.setContent(ri.getJoinTables());
          condtype.setContent(ri.getConditionType());
          conddata.setContent(ri.getConditionData());
          condop.setContent(ri.getConditionOperator());
          entity.setContent(ri.getEntity());
          info.setContent(ri.getInfo());
          T.add(new HiddenInput("repitemid",String.valueOf(ri.getID())));
        }
        catch (SQLException ex) {

        }
      }
    }

    int tlen = 50;
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

    Frame.add(T);
    Frame.add(new SubmitButton("risave","Save"));
    Frame.add(new HiddenInput(this.sAction,String.valueOf(this.ACT4 )));
    Frame.add(new HiddenInput("rep.cat.drp",String.valueOf(iCategoryId)));

    return(Frame);
  }

  private PresentationObject doEntityAdd(IWContext iwc){

    String sEntId = iwc.getParameter("ent_drp");
    int iEntId = -1;
    if(sEntId !=null)
      iEntId = Integer.parseInt(sEntId);
    Table T = new Table();
    T.add(new HiddenInput(sAction,String.valueOf(ACT1)));
    DropdownMenu drp = getEntityDrp(getReportEntities(),"ent_drp",sEntId);
    setStyle(drp);
    drp.setToSubmit();
    T.add(drp,1,1);
    if(iEntId > 0){
      try{
        ReportEntity RE = new ReportEntity(iEntId);
        T.add(getEntityForm(RE),1,2);
      }
      catch(SQLException sql){sql.printStackTrace();}
      T.add(new SubmitButton("ea_cancel","Cancel"),1,3);
      T.add(new SubmitButton("ea_apply","Apply"),1,3);
      T.add(new SubmitButton("ea_ok","Ok"),1,3);
    }
    return T;
  }

  private PresentationObject getEntityTable(ReportEntity RE){
    try{
    GenericEntity ent = (GenericEntity)Class.forName(RE.getEntity()).newInstance();
    Table T = new Table();

    T.add(formatText("Display"),1,1);
    T.add(formatText("Field"),2,1);
    T.add(formatText("Relation"),3,1);
    for (int i = 0;i < ent.getColumnNames().length; i++ ){
      T.add(formatText(ent.getLongName(ent.getColumnNames()[i])),1,i+2);
      T.add(formatText(ent.getColumnNames()[i]),2,i+2);
      Class relationshipClass= ent.getRelationShipClass(ent.getColumnNames()[i]);
      if(relationshipClass!=null){
        T.add(formatText(relationshipClass.getName()),3,i+2);
      }
    }
    return T;
    }
    catch(Exception ex){return new Table();}
  }

  private PresentationObject getEntityForm(ReportEntity RE){
    try{
    GenericEntity ent = (GenericEntity)Class.forName(RE.getEntity()).newInstance();
    Table T = new Table();

    T.add(formatText("Display"),1,1);
    T.add(formatText("Field"),2,1);
    T.add(formatText("Relation"),3,1);
    T.add(new HiddenInput("re_id",String.valueOf(RE.getID())));
    SelectionDoubleBox box = new SelectionDoubleBox("box","Fields","Order");
    SelectionBox box1 = box.getLeftBox();
    box1.keepStatusOnAction();
    SelectionBox box2 = box.getRightBox();
    box1.keepStatusOnAction();
    box2.addUpAndDownMovers();
    int a = 1;
    for (int i = 0; i < ent.getVisibleColumnNames().length; i++) {
      box1.addMenuElement(i,ent.getLongName(ent.getVisibleColumnNames()[i]));
    }
    box1.setHeight(20);
    box2.setHeight(20);
    box2.selectAllOnSubmit();
    T.mergeCells(1,2,3,2);
    T.add(box,1,2);
    return T;
    }
    catch(Exception ex){
      ex.printStackTrace();
      return new Table();
    }
  }

  protected void doUpdateEntityForm(IWContext iwc) throws SQLException{
    System.err.println("doUpdateEntityForm");
    try{
      int re_id = Integer.parseInt(iwc.getParameter("re_id"));
      ReportEntity RE = new ReportEntity(re_id);
      GenericEntity ent = (GenericEntity)Class.forName(RE.getEntity()).newInstance();
      String[] s = iwc.getParameterValues("box");
      int len = s.length;
      String[] columns  = ent.getVisibleColumnNames();
      for (int i = 0; i < len; i++) {
        int nr = Integer.parseInt(s[i]);
        boolean b = ReportEntityHandler.saveReportItem(iCategoryId
            ,ent.getLongName(columns[i])
            ,columns[nr]
            ,ent.getEntityName()
            ,RE.getJoin()
            ,RE.getJoinTables()
            ,"I"
            ,""
            ,"like"
            ,ent.getClass().getName()
            ,"");
      }
    }
    catch(Exception ex){
      ex.printStackTrace();

    }
  }

  private List getReportEntities(){
    List L = null;
    try{
      L = EntityFinder.findAll(new ReportEntity());
    }
    catch(SQLException sql){
      sql.printStackTrace();
    }
    return L;
  }

  private DropdownMenu getEntityDrp(List entities,String name,String selected){
    DropdownMenu drp = new DropdownMenu(name);
    drp.addDisabledMenuElement("-1","Entity");
    if(entities != null)
    for (int i = 0; i < entities.size(); i++) {
      ReportEntity RE = (ReportEntity) entities.get(i);
      drp.addMenuElement(RE.getID(),RE.getEntity());
    }
    if(!"".equalsIgnoreCase(selected))
      drp.setSelectedElement(selected);
    return drp;
  }

  protected void doUpdate(IWContext iwc) throws SQLException{
    String entityId = iwc.getParameter("repitemid");
    int itemId = -1;
    if(entityId!= null){
      itemId = Integer.parseInt(entityId);
    }

    int id  = iCategoryId;
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
      boolean b = false;
      if(itemId > 0){
        b = ReportEntityHandler.updateReportItem(itemId,id,name,field,table,joins, jointables,condtype,conddata,condop,entity,info);
      }
      else{
        b = ReportEntityHandler.saveReportItem(id,name,field,table,joins, jointables,condtype,conddata,condop,entity,info);
      }
    }
  }

}