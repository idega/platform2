package com.idega.block.reports.presentation;

import com.idega.block.reports.data.*;
import com.idega.block.reports.business.*;
import com.idega.jmodule.object.JModuleObject;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.data.EntityFinder;
import java.sql.SQLException;
import java.util.Vector;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Collections;
import com.idega.jmodule.object.Editor;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.Script;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.Image;


public class ReportEditor extends Editor{

  private final String sAction = "report_action";
  private String sActPrm = "0";
  private int iAction = 0;
  private String prefix = "reed_";
  private String sLastOrder = "0";
  private String sManual = null;
  private int iCategory;
  private int iSaveCategory = -1;
  private int iReportId = -1;
  private boolean sqlEditAdmin = false;
  private String sMainCategoryAttribute = null,sViewCategoryAttribute = null;
  private int iMainCategoryAttributeId = 0, iViewCategoryAttributeId= 0;


  public ReportEditor(){
    this.iCategory = 0;
  }

  public ReportEditor(int iCategory){
    this.iCategory = iCategory;
    this.iSaveCategory = iCategory;
  }

  public void setSQLEdit(boolean value){
    sqlEditAdmin = value;
  }

  public void setSaveCategory(int iSaveCategory){
    this.iSaveCategory = iSaveCategory;
  }
  public void setManual(String manual){
    this.sManual = manual;
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

  private void checkCategories(ModuleInfo modinfo){
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
        RCA = (ReportCategoryAttribute)L.get(0);
        iSaveCategory = RCA.getReportCategoryId();
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
        RCA = (ReportCategoryAttribute)K.get(0);
        iCategory = RCA.getReportCategoryId();
      }
    }
  }

  protected void control(ModuleInfo modinfo){
    try{
        this.makeView();
        if(isAdmin){
          if(iSaveCategory != -1 && modinfo.getParameter("category_id")!=null){
            iSaveCategory = Integer.parseInt(modinfo.getParameter("category_id"));
          }
          checkCategories(modinfo);

          if(modinfo.getParameter(sAction) != null)
            sActPrm = modinfo.getParameter(sAction);
          else if(modinfo.getParameter("report")!=null){
            iReportId = Integer.parseInt(modinfo.getParameter("report"));
            sActPrm = "2";
          }
          else
            sActPrm = "0";
          try{
            iAction = Integer.parseInt(sActPrm);
            switch(iAction){
              case ACT1: doSave(modinfo);   break;
              case ACT2: doAdmin(modinfo);  break;
              case ACT3: doChange(modinfo); break;
              case ACT4: doUpdate(modinfo); break;
              default : doMain(modinfo);    break;
            }
          }
          catch(Exception e){
            e.printStackTrace();
          }
        }
        else
          addMain(formatText("Ekki réttindi"));
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

    if(iCategory > 0){
      ReportCondition[] RC = ReportEntityHandler.getConditions(iCategory);
      modinfo.setSessionAttribute(prefix+"force",RC);
      //this.BORDER = 0;
      Table T = new Table();
      T.setWidth("100%");
      T.setBorder(BORDER);
      T.setCellpadding(0);
      T.setCellspacing(0);
      Form form = new Form();
      form.add(T);
      SelectionDoubleBox box = new SelectionDoubleBox("box","Fields","Order");

      Table U = new Table();
      Table M = new Table();
      Table ML = new Table();
      Table MLL = new Table();
      Table B = new Table();
      U.setBorder(BORDER);
      U.setCellpadding(0);
      U.setCellspacing(0);
      M.setBorder(BORDER);
      M.setColor(this.MiddleColor);
      M.setCellpadding(0);
      M.setCellspacing(0);
      ML.setBorder(BORDER);
      ML.setColor(this.MiddleColor);
      ML.setCellpadding(0);
      ML.setCellspacing(0);
      MLL.setBorder(BORDER);
      MLL.setColor(this.MiddleColor);
      MLL.setCellpadding(0);
      MLL.setCellspacing(0);
      MLL.setVerticalAlignment("top");
      if(this.sManual != null)
        MLL.add(this.formatText(sManual));

      B.setBorder(BORDER);
      B.setCellpadding(0);
      B.setCellspacing(0);
      M.setWidth("100%");
      M.setWidth(1,"40%");
      M.add(box,1,1);
      M.add(ML,2,1);
      M.add(MLL,4,1);
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
      B.add(new SubmitButton(new Image("/reports/pics/ok.gif")));
      B.add(new HiddenInput(sAction, String.valueOf(ACT4)));
      form.add(new HiddenInput("reportcategory_id",String.valueOf(iSaveCategory)));
      addMain(form);
    }
    else
      addMain(new Text("Nothing to show"));
    Link back =  new Link(new Image("/reports/pics/newlist.gif"),"/reports/index.jsp");
    this.addToHeader(back);
    if(sqlEditAdmin){
      Link admin = new Link(new Image("/reports/pics/admin.gif"),"/reports/reportedit.jsp");
      admin.addParameter(sAction,String.valueOf(ACT2));
      admin.addParameter("reportcategory_id",String.valueOf(iSaveCategory));
      this.addToHeader(admin);
    }
    this.addLinks(formatText("Report Editor"));
  }
  protected void doChange(ModuleInfo modinfo) throws SQLException{

  }


  protected void doAdmin(ModuleInfo modinfo) throws SQLException{
    Report R = null;
    boolean b = false;
    if(iReportId >0 ){
      try {
        R = new Report(iReportId);
        b = true;
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
    Form form = new Form();
    Table T = new Table();

    Text nameText = formatText("Name");
    Text infoText = formatText("Info");
    Text headersText = formatText("Headers");
    Text sqlText = formatText("SQL");
    TextInput nameInput = new TextInput(prefix+"name",b?R.getName():"");
    TextInput infoInput = new TextInput(prefix+"info",b?R.getInfo():"");
    TextInput headersInput = new TextInput(prefix+"headers",b?R.getHeader():"");
    if(b)
      form.add(new HiddenInput("report_id",String.valueOf(R.getID())));
    headersInput.setLength(80);
    TextArea sqlInput = new TextArea(prefix+"sql",b?R.getSQL():"");
    sqlInput.setWidth(80);
    sqlInput.setHeight(8);

    T.add(nameText,1,1);
    T.add(nameInput,1,2);
    T.add(infoText,1,3);
    T.add(infoInput,1,4);
    T.add(headersText,1,5);
    T.add(headersInput,1,6);
    T.add(sqlText,1,7);
    T.add(sqlInput,1,8);

    T.add(new SubmitButton(new Image("/reports/pics/ok.gif")),1,9);
    T.add(new HiddenInput(sAction, String.valueOf(ACT1)),1,9);

    form.add(T);
    Link back =  new Link(new Image("/reports/pics/newlist.gif"),"/reports/index.jsp");
    this.addToHeader(back);
    form.add(new HiddenInput("reportcategory_id",String.valueOf(iSaveCategory)));
    addMain(form);
  }

  private void doSave(ModuleInfo modinfo){
    String msg = "";
    String sName = modinfo.getParameter(prefix+"name").trim();
    String sInfo = modinfo.getParameter(prefix+"info").trim();
    String sHeaders = modinfo.getParameter(prefix+"headers").trim();
    add(sHeaders);
    String sSql = modinfo.getParameter(prefix+"sql").trim();
    String sReportId = modinfo.getParameter("report_id");
    if(sName != null && sName.length() > 1 ){
      if(sSql != null && sHeaders!= null){
        String[] he = str2array(sHeaders,",:;");
        try{
          if(sReportId==null){
            Report R = new Report();
            R.setCategory(iSaveCategory);
            R.setName(sName);
            R.setInfo(sInfo);
            R.setSQL(sSql);
            R.setHeaders(he);
            R.insert();
            msg = "Report was saved";
          }
          else{
            Report R = new Report(Integer.parseInt(sReportId));
            //R.setCategory(iSaveCategory);
            R.setName(sName);
            R.setInfo(sInfo);
            R.setSQL(sSql);
            R.setHeaders(he);
            R.update();
            msg = "Report was updated";
          }
        }
        catch(SQLException ex){
          msg = "Report could not be saved";
          System.err.println(msg);
          ex.printStackTrace();
        }
        catch(NumberFormatException ex){
          msg = "Wrong Report id";
        }
      }
    }
    else
      msg = "Needs a name";
    Link back =  new Link(new Image("/reports/pics/newlist.gif"),"/reports/index.jsp");
    this.addToHeader(back);
    addMain(formatText(msg));
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
        rc.setVariable(temp);
        vRC.addElement(rc);

      }
    }
    modinfo.removeSessionAttribute(prefix+"force");

    String name = modinfo.getParameter(prefix+"name");
    String info = modinfo.getParameter(prefix+"info");

    name = name != null?name: "";
    info = info != null?info: "";

    ReportMaker rm = new ReportMaker();

    /////////////// Golf union Case ////////////////////////////////////

    if(modinfo.getSessionAttribute("golf_union_id")!= null){
      String sUnionId = ((String) modinfo.getSessionAttribute("golf_union_id"));
      int id = 1;
      try{ id = Integer.parseInt(sUnionId);}
      catch(NumberFormatException e){}
      if(id > 1){
        ReportItem RIx = new ReportItem();
        RIx.setMainTable("union_member_info");
        RIx.setJoinTables("member");
        RIx.setJoin("union_member_info.member_id = member.member_id,union_member_info.union_id = "+sUnionId);
        ReportCondition RCx = new ReportCondition(RIx);
        RCx.setIsSelect();
        vRC.addElement(RCx);
      }

    }
    //////////////////////////////////////////////////////////////////

    String sql = rm.makeSQL(vRC);
    Report R = new Report();
    R.setCategory(iSaveCategory);
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

  private String[] str2array(String s,String delim){
    StringTokenizer st = new StringTokenizer(s,delim);
    String[] array = new String[st.countTokens()];
    int i = 0;
    while(st.hasMoreTokens()){
      array[i++] = st.nextToken();
    }
    return array;
  }
   public void makeAnswer(Report R){
    Link L = new Link(new Image("/reports/pics/newlist.gif"),"/reports/reportview.jsp");
    L.addParameter("report",R.getID());
    addMain(L);
    addMain(formatText("View the results"));
  }
}