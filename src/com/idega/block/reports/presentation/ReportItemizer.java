package com.idega.block.reports.presentation;

import java.sql.SQLException;
import java.util.List;

import com.idega.block.reports.business.ReportEntityHandler;
import com.idega.block.reports.data.ReportCategory;
import com.idega.block.reports.data.ReportEntity;
import com.idega.block.reports.data.ReportItem;
import com.idega.data.EntityFinder;
import com.idega.data.IDOLegacyEntity;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.SelectionDoubleBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.util.Edit;
import com.idega.repository.data.RefactorClassRegistry;


public class ReportItemizer extends Block implements Reports{

  private boolean isAdmin = false;
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
    this.sIndex = "0";
    this.sName = "";
    this.sInfo = "";
  }

  protected void control(IWContext iwc){
    if(this.isAdmin){
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

        if(iwc.getParameter(this.sAction) != null){
          this.sActPrm = iwc.getParameter(this.sAction);
          try{
            this.iAction = Integer.parseInt(this.sActPrm);
            switch(this.iAction){
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

  public void main(IWContext iwc){
    this.isAdmin = iwc.hasEditPermission(this);
    control(iwc);

  }

  protected PresentationObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(3,1);
    int last = 3;
    LinkTable.setWidth("100%");
    LinkTable.setCellpadding(2);
    LinkTable.setCellspacing(1);
    LinkTable.setColor(DarkColor);
    LinkTable.setWidth(last,"100%");
    Link Link1 = new Link("New");
    Link1.setFontColor(LightColor);
    Link1.addParameter(this.sAction,String.valueOf(ACT3));
    Link Link2 = new Link("View");
    Link2.setFontColor(LightColor);
    Link2.addParameter(this.sAction,String.valueOf(ACT2));
    Link Link3 = new Link("Entity");
    Link3.setFontColor(LightColor);
    Link3.addParameter(this.sAction,String.valueOf(ACT1));
    if(this.isAdmin){
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
      iwc.setSessionAttribute(this.prefix+"id",new Integer(id));
      if(id != 0){
        try {
          ReportCategory RC = ((com.idega.block.reports.data.ReportCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(ReportCategory.class)).findByPrimaryKeyLegacy(id);
          this.sName = RC.getName();
          this.sInfo = RC.getDescription();
        }
        catch (Exception ex) {
        }
      }
    }
  }

  private void doMain(IWContext iwc){
    String sIndex = iwc.getParameter("rep_cat_drp");
    Table T = new Table();
    if(sIndex==null) {
		sIndex = "0";
	}
    add(T);
  }

  private PresentationObject doView(IWContext iwc){
    List L = null;
    try{
      L = EntityFinder.findAllByColumn(((com.idega.block.reports.data.ReportItemHome)com.idega.data.IDOLookup.getHomeLegacy(ReportItem.class)).createLegacy(),com.idega.block.reports.data.ReportItemBMPBean.getColumnCategoryId(),this.iCategoryId);
    }
    catch(Exception e){L = null;}
    Table T = new Table();
		T.add(Edit.formatText("Name"),1,1);
    T.add(Edit.formatText("Entity"),2,1);
    T.add(Edit.formatText("Display order"),3,1);
    if(L != null){
      int count = L.size();
      for (int i = 0; i < count; i++) {
        int a = i+2;
        int b = 1;
        T.add(Edit.formatText(i+1),b++,a);
        ReportItem RI = (ReportItem) L.get(i);
        Link link = new Link(RI.getName());
        link.addParameter(this.sAction,ACT3);
        link.addParameter("repitemid",RI.getID());
        link.addParameter("rep.cat.drp",this.iCategoryId);
        T.add(link,b++,a);
				T.add(Edit.formatText(RI.getEntityName()),b++,a);
				T.add(Edit.formatText(RI.getDisplayOrder()),b++,a);
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
      this.iCategoryId = iCatId;
      iwc.setSessionAttribute(this.sSessPrm,new Integer(this.iCategoryId));
    }
    else if(iwc.getSessionAttribute(this.sSessPrm )!=null){
      this.iCategoryId = ((Integer)iwc.getSessionAttribute(this.sSessPrm )).intValue();
      sCatId = String.valueOf(this.iCategoryId);
    }
    Table T = new Table();
    DropdownMenu drp = ReportObjectHandler.drpCategories("rep.cat.drp",sCatId);
    drp.setToSubmit();
    Edit.setStyle(drp);
    T.add(drp);
    return T;
  }

  protected PresentationObject doChange(IWContext iwc) throws SQLException{
    String sRepItemId = iwc.getParameter("repitemid");
    Table Frame = new Table(2,1);
    Frame.setRowVerticalAlignment(1,"top");
    Table T =  new Table(2,13);
    T.setCellpadding(2);
    T.setCellspacing(1);
    T.setHorizontalZebraColored(LightColor,WhiteColor);
    T.setRowColor(1,MiddleColor);
    int a = 1;
    T.add(Edit.formatText("Property"),1,a);
    T.add(Edit.formatText("Value"),2,a++);
    T.add(Edit.formatText("Name"),1,a++);
    T.add(Edit.formatText("Field"),1,a++);
    T.add(Edit.formatText("Maintable"),1,a++);
    T.add(Edit.formatText("Joins"),1,a++);
    T.add(Edit.formatText("Join Tables"),1,a++);
    T.add(Edit.formatText("Condition Type"),1,a++);
    T.add(Edit.formatText("Condition Data"),1,a++);
    T.add(Edit.formatText("Condition Operator"),1,a++);
    T.add(Edit.formatText("Entity Class"),1,a++);
    T.add(Edit.formatText("Information"),1,a++);
//    T.add(Edit.formatText("Is Select"),1,a++);
T.add(Edit.formatText("Display order"),1,a++);
		T.add(Edit.formatText("Is Function"),1,a++);


    TextInput name,field,table,joins,jointables,
              condtype,conddata,condop,entity,info,displayorder;
    CheckBox isFunction;

    name        = new TextInput(this.prefix+"name");
    field       = new TextInput(this.prefix+"field");
    table       = new TextInput(this.prefix+"table");
    joins       = new TextInput(this.prefix+"joins");
    jointables  = new TextInput(this.prefix+"jointables");
    condtype    = new TextInput(this.prefix+"condtype");
    conddata    = new TextInput(this.prefix+"conddata");
    condop      = new TextInput(this.prefix+"condop");
    entity      = new TextInput(this.prefix+"entity");
    info        = new TextInput(this.prefix+"info");
		displayorder= new TextInput(this.prefix+"disorder");
		isFunction  = new CheckBox(this.prefix+"function");


    if(sRepItemId != null){
      int repItemId = Integer.parseInt(sRepItemId);
      if(repItemId > 0){
        try {
          ReportItem ri = ((com.idega.block.reports.data.ReportItemHome)com.idega.data.IDOLookup.getHomeLegacy(ReportItem.class)).findByPrimaryKeyLegacy(repItemId );
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
					displayorder.setContent(String.valueOf(ri.getDisplayOrder()));
					isFunction.setChecked(ri.getIsFunction());
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
		displayorder.setSize(4);

    Edit.setStyle(name);
    Edit.setStyle(field);
    Edit.setStyle(table);
    Edit.setStyle(joins);
    Edit.setStyle(jointables);
    Edit.setStyle(condtype);
    Edit.setStyle(conddata);
    Edit.setStyle(condop);
    Edit.setStyle(entity);
    Edit.setStyle(info);
		Edit.setStyle(displayorder);
		Edit.setStyle( isFunction);

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
		T.add(displayorder,col,row++);
		T.add(isFunction,col,row++);

    Frame.add(T);
    Frame.add(new SubmitButton("risave","Save"));
    Frame.add(new HiddenInput(this.sAction,String.valueOf(this.ACT4 )));
    Frame.add(new HiddenInput("rep.cat.drp",String.valueOf(this.iCategoryId)));

    return(Frame);
  }

  private PresentationObject doEntityAdd(IWContext iwc){

    String sEntId = iwc.getParameter("ent_drp");
    int iEntId = -1;
    if(sEntId !=null) {
		iEntId = Integer.parseInt(sEntId);
	}
    Table T = new Table();
    T.add(new HiddenInput(this.sAction,String.valueOf(ACT1)));
    DropdownMenu drp = getEntityDrp(getReportEntities(),"ent_drp",sEntId);
    Edit.setStyle(drp);
    drp.setToSubmit();
    T.add(drp,1,1);
    if(iEntId > 0){
      try{
        ReportEntity RE = ((com.idega.block.reports.data.ReportEntityHome)com.idega.data.IDOLookup.getHomeLegacy(ReportEntity.class)).findByPrimaryKeyLegacy(iEntId);
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
    IDOLegacyEntity ent = (IDOLegacyEntity) RefactorClassRegistry.forName(RE.getEntity()).newInstance();
    Table T = new Table();

    T.add(Edit.formatText("Display"),1,1);
    T.add(Edit.formatText("Field"),2,1);
    T.add(Edit.formatText("Relation"),3,1);
    for (int i = 0;i < ent.getColumnNames().length; i++ ){
      T.add(Edit.formatText(ent.getLongName(ent.getColumnNames()[i])),1,i+2);
      T.add(Edit.formatText(ent.getColumnNames()[i]),2,i+2);
      Class relationshipClass= ent.getRelationShipClass(ent.getColumnNames()[i]);
      if(relationshipClass!=null){
        T.add(Edit.formatText(relationshipClass.getName()),3,i+2);
      }
    }
    return T;
    }
    catch(Exception ex){return new Table();}
  }

  private PresentationObject getEntityForm(ReportEntity RE){
    try{
    IDOLegacyEntity ent = (IDOLegacyEntity) RefactorClassRegistry.forName(RE.getEntity()).newInstance();
    Table T = new Table();

    T.add(Edit.formatText("Display"),1,1);
    T.add(Edit.formatText("Field"),2,1);
    T.add(Edit.formatText("Relation"),3,1);
    T.add(new HiddenInput("re_id",String.valueOf(RE.getID())));
    SelectionDoubleBox box = new SelectionDoubleBox("box","Fields","Order");
    SelectionBox box1 = box.getLeftBox();
    box1.keepStatusOnAction();
    SelectionBox box2 = box.getRightBox();
    box1.keepStatusOnAction();
    box2.addUpAndDownMovers();
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
      ReportEntity RE = ((com.idega.block.reports.data.ReportEntityHome)com.idega.data.IDOLookup.getHomeLegacy(ReportEntity.class)).findByPrimaryKeyLegacy(re_id);
      IDOLegacyEntity ent = (IDOLegacyEntity)RefactorClassRegistry.forName(RE.getEntity()).newInstance();
      String[] s = iwc.getParameterValues("box");
      int len = s.length;
      String[] columns  = ent.getVisibleColumnNames();
      for (int i = 0; i < len; i++) {
        int nr = Integer.parseInt(s[i]);
        ReportEntityHandler.saveReportItem(this.iCategoryId
            ,ent.getLongName(columns[i])
            ,columns[nr]
            ,ent.getEntityName()
            ,RE.getJoin()
            ,RE.getJoinTables()
            ,"I"
            ,""
            ,"like"
            ,ent.getClass().getName()
            ,""
						,false);
      }
    }
    catch(Exception ex){
      ex.printStackTrace();

    }
  }

  private List getReportEntities(){
    List L = null;
    try{
      L = EntityFinder.findAll(((com.idega.block.reports.data.ReportEntityHome)com.idega.data.IDOLookup.getHomeLegacy(ReportEntity.class)).createLegacy());
    }
    catch(SQLException sql){
      sql.printStackTrace();
    }
    return L;
  }

  private DropdownMenu getEntityDrp(List entities,String name,String selected){
    DropdownMenu drp = new DropdownMenu(name);
    drp.addDisabledMenuElement("-1","Entity");
    if(entities != null) {
		for (int i = 0; i < entities.size(); i++) {
		  ReportEntity RE = (ReportEntity) entities.get(i);
		  drp.addMenuElement(RE.getID(),RE.getEntity());
		}
	}
    if(!"".equalsIgnoreCase(selected)) {
		drp.setSelectedElement(selected);
	}
    return drp;
  }

  protected void doUpdate(IWContext iwc) throws SQLException{
    String entityId = iwc.getParameter("repitemid");
    int itemId = -1;
    if(entityId!= null){
      itemId = Integer.parseInt(entityId);
    }

    int id  = this.iCategoryId;
    String name,field,table,joins,jointables,condtype,conddata,condop,entity,info;
		boolean function;

    name        = iwc.getParameter(this.prefix+"name");
    field       = iwc.getParameter(this.prefix+"field");
    table       = iwc.getParameter(this.prefix+"table");
    joins       = iwc.getParameter(this.prefix+"joins");
    jointables  = iwc.getParameter(this.prefix+"jointables");
    condtype    = iwc.getParameter(this.prefix+"condtype");
    conddata    = iwc.getParameter(this.prefix+"conddata");
    condop      = iwc.getParameter(this.prefix+"condop");
    entity      = iwc.getParameter(this.prefix+"entity");
    info        = iwc.getParameter(this.prefix+"info");
		function    = iwc.getParameter(this.prefix+"function")!=null;
    if(id != 0){
      if(itemId > 0){
        ReportEntityHandler.updateReportItem(itemId,id,name,field,table,joins, jointables,condtype,conddata,condop,entity,info,function);
      }
      else{
        ReportEntityHandler.saveReportItem(id,name,field,table,joins, jointables,condtype,conddata,condop,entity,info,function);
      }
    }
  }

}
