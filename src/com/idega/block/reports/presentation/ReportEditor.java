package com.idega.block.reports.presentation;

import com.idega.block.reports.data.*;
import com.idega.block.reports.business.*;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.data.EntityFinder;
import java.sql.SQLException;
import java.util.Vector;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Collections;
import com.idega.presentation.Editor;
import com.idega.presentation.Table;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.presentation.Script;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Image;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;


public class ReportEditor extends ReportPresentation{

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

  public final static String prmSaveCategory = "rep.category_id";
  public final static String prmReportId = "rep.report_id";

	private IWBundle iwb;
	private IWResourceBundle iwrb;

  public ReportEditor(){
    super();
    this.iCategory = 0;
  }

  public ReportEditor(int iCategory){
    this.iCategory = iCategory;
    this.iSaveCategory = iCategory;
  }

  public void setSQLEdit(boolean value){
    sqlEditAdmin = value;
  }
  public void setManual(String manual){
    this.sManual = manual;
  }

  protected void control(IWContext iwc){
		iwrb = getResourceBundle(iwc);
		iwb = getBundle(iwc);
    try{
        if(isAdmin){
          if(iSaveCategory != -1 && iwc.getParameter(prmSaveCategory)!=null){
            iSaveCategory = Integer.parseInt(iwc.getParameter(prmSaveCategory));
          }

          if(iwc.getParameter(sAction) != null)
            sActPrm = iwc.getParameter(sAction);
          else if(iwc.getParameter(prmReportId)!=null){
            iReportId = Integer.parseInt(iwc.getParameter(prmReportId));
            sActPrm = "2";
          }
          else
            sActPrm = "0";
          try{
            iAction = Integer.parseInt(sActPrm);
            switch(iAction){
              case ACT1: doSave(iwc);   break;
              case ACT2: doAdmin(iwc);  break;
              case ACT3: doChange(iwc); break;
              case ACT4: doUpdate(iwc); break;
              default : doMain(iwc);    break;
            }
          }
          catch(Exception e){
            e.printStackTrace();
          }
        }
        else
          add(formatText("Ekki réttindi"));
    }
    catch(Exception S){
      S.printStackTrace();
    }
  }

  private void doMain(IWContext iwc){

    if(iCategory > 0){
      ReportCondition[] RC = ReportEntityHandler.getConditions(iCategory);
      iwc.setSessionAttribute(prefix+"force",RC);
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
        PresentationObject mo = ReportObjectHandler.getInput(RC[i],prefix+"in"+i,"");
        ML.add(RC[i].getDisplay(),1,a);
        ML.add(mo,2,a++);
      }
      box1.setHeight(20);
      box2.setHeight(20);
      box2.selectAllOnSubmit();
      B.add(new SubmitButton(iwb.getImage("/shared/ok.gif")));//new Image("/reports/pics/ok.gif")));
      B.add(new HiddenInput(sAction, String.valueOf(ACT4)));
      form.add(new HiddenInput("reportcategory_id",String.valueOf(iSaveCategory)));
      add(form);
    }
    else
      add(new Text("Nothing to show"));
    Link back =  new Link(iwb.getImage("/shared/newlist.gif"));//new Image("/reports/pics/newlist.gif"),"/reports/index.jsp");
    add(back);
    if(sqlEditAdmin){
      Link admin = new Link(iwb.getImage("/shared/admin.gif"));//new Image("/reports/pics/admin.gif"),"/reports/reportedit.jsp");
      admin.addParameter(sAction,String.valueOf(ACT2));
      admin.addParameter("reportcategory_id",String.valueOf(iSaveCategory));
      add(admin);
    }
    add(formatText("Report Editor"));
  }
  protected void doChange(IWContext iwc) throws SQLException{

  }


  protected void doAdmin(IWContext iwc) throws SQLException{
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

    T.add(new SubmitButton(iwb.getImage("/shared/ok.gif")));//new Image("/reports/pics/ok.gif")),1,9);
    T.add(new HiddenInput(sAction, String.valueOf(ACT1)),1,9);

    form.add(T);
    Link back =  new Link(iwb.getImage("/shared/newlist.gif"));//new Image("/reports/pics/newlist.gif"),"/reports/index.jsp");
    this.add(back);
    form.add(new HiddenInput("reportcategory_id",String.valueOf(iSaveCategory)));
    add(form);
  }

  private void doSave(IWContext iwc){
    String msg = "";
    String sName = iwc.getParameter(prefix+"name").trim();
    String sInfo = iwc.getParameter(prefix+"info").trim();
    String sHeaders = iwc.getParameter(prefix+"headers").trim();
    add(sHeaders);
    String sSql = iwc.getParameter(prefix+"sql").trim();
    String sReportId = iwc.getParameter("report_id");
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
    Link back =  new Link(iwb.getImage("/shared/newlist.gif"));//new Image("/reports/pics/newlist.gif"),"/reports/index.jsp");
    this.add(back);
    add(formatText(msg));
  }


  protected void doUpdate(IWContext iwc) throws SQLException{
    String[] s = iwc.getParameterValues("box");
    ReportCondition[] RC = (ReportCondition[])iwc.getSessionAttribute(prefix+"force");
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
      temp = iwc.getParameter(prefix+"in"+i);
      if(!"".equalsIgnoreCase(temp) && !"0".equals(temp)){
        //add(" check "+i);
        ReportCondition rc = RC[i];
        rc.setVariable(temp);
        vRC.addElement(rc);

      }
    }
    iwc.removeSessionAttribute(prefix+"force");

    String name = iwc.getParameter(prefix+"name");
    String info = iwc.getParameter(prefix+"info");

    name = name != null?name: "";
    info = info != null?info: "";

    ReportMaker rm = new ReportMaker();

    /////////////// Golf union Case ////////////////////////////////////

    if(iwc.getSessionAttribute("golf_union_id")!= null){
      String sUnionId = ((String) iwc.getSessionAttribute("golf_union_id"));
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
    ReportService.setSessionReport(iwc,R);
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
    Link L = new Link(iwb.getImage("/shared/newlist.gif"));//new Image("/reports/pics/newlist.gif"),"/reports/reportview.jsp");
    L.addParameter("report",R.getID());
    add(L);
    add(formatText("View the results"));
  }

}
