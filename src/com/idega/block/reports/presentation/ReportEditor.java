package com.idega.block.reports.presentation;

import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.Vector;

import com.idega.block.reports.business.ReportCondition;
import com.idega.block.reports.business.ReportEntityHandler;
import com.idega.block.reports.business.ReportMaker;
import com.idega.block.reports.business.ReportService;
import com.idega.block.reports.data.Report;
import com.idega.block.reports.data.ReportItem;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.SelectionDoubleBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.util.Edit;


public class ReportEditor extends Block implements Reports{

  private boolean isAdmin = false;
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
  private int BORDER = 0;

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
    this.sqlEditAdmin = value;
  }
  public void setManual(String manual){
    this.sManual = manual;
  }

  protected void control(IWContext iwc){
		this.iwrb = getResourceBundle(iwc);
		this.iwb = getBundle(iwc);
    try{
        if(this.isAdmin){
          if(this.iSaveCategory != -1 && iwc.getParameter(prmSaveCategory)!=null){
            this.iSaveCategory = Integer.parseInt(iwc.getParameter(prmSaveCategory));
          }

          if(iwc.getParameter(this.sAction) != null) {
			this.sActPrm = iwc.getParameter(this.sAction);
		}
		else if(iwc.getParameter(prmReportId)!=null){
            this.iReportId = Integer.parseInt(iwc.getParameter(prmReportId));
            this.sActPrm = "2";
          }
		else {
			this.sActPrm = "0";
		}
          try{
            this.iAction = Integer.parseInt(this.sActPrm);
            switch(this.iAction){
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
		else {
			add(Edit.formatText("Ekki r�ttindi"));
		}
    }
    catch(Exception S){
      S.printStackTrace();
    }
  }

  private void doMain(IWContext iwc){

    if(this.iCategory > 0){
      ReportCondition[] RC = ReportEntityHandler.getConditions(this.iCategory);
      iwc.setSessionAttribute(this.prefix+"force",RC);
      //this.BORDER = 0;
      Table T = new Table();
      T.setWidth("100%");
      T.setBorder(this.BORDER);
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
      U.setBorder(this.BORDER);
      U.setCellpadding(0);
      U.setCellspacing(0);
      M.setBorder(this.BORDER);
      M.setColor(MiddleColor);
      M.setCellpadding(0);
      M.setCellspacing(0);
      ML.setBorder(this.BORDER);
      ML.setColor(MiddleColor);
      ML.setCellpadding(0);
      ML.setCellspacing(0);
      MLL.setBorder(this.BORDER);
      MLL.setColor(MiddleColor);
      MLL.setCellpadding(0);
      MLL.setCellspacing(0);
      if(this.sManual != null) {
		MLL.add(Edit.formatText(this.sManual));
	}

      B.setBorder(this.BORDER);
      B.setCellpadding(0);
      B.setCellspacing(0);
      M.setWidth("100%");
      M.setWidth(1,"40%");
      M.add(box,1,1);
      M.add(ML,2,1);
      M.setVerticalAlignment(4, 1, Table.VERTICAL_ALIGN_TOP);
      M.add(MLL,4,1);
      T.add(U,1,1);
      T.add(M,1,2);
      T.add(B,1,3);

      Text nameText = new Text("Name");
      Text infoText = new Text("Info");
      TextInput nameInput = new TextInput(this.prefix+"name");
      TextInput infoInput = new TextInput(this.prefix+"info");
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
        PresentationObject mo = ReportObjectHandler.getInput(RC[i],this.prefix+"in"+i,"");
        ML.add(RC[i].getDisplay(),1,a);
        ML.add(mo,2,a++);
      }
      box1.setHeight(20);
      box2.setHeight(20);
      box2.selectAllOnSubmit();
      B.add(new SubmitButton(this.iwb.getImage("/shared/ok.gif")));//new Image("/reports/pics/ok.gif")));
      B.add(new HiddenInput(this.sAction, String.valueOf(ACT4)));
      form.add(new HiddenInput("reportcategory_id",String.valueOf(this.iSaveCategory)));
      add(form);
    }
	else {
		add(new Text("Nothing to show"));
	}
    Link back =  new Link(this.iwb.getImage("/shared/newlist.gif"));//new Image("/reports/pics/newlist.gif"),"/reports/index.jsp");
    add(back);
    if(this.sqlEditAdmin){
      Link admin = new Link(this.iwb.getImage("/shared/admin.gif"));//new Image("/reports/pics/admin.gif"),"/reports/reportedit.jsp");
      admin.addParameter(this.sAction,String.valueOf(ACT2));
      admin.addParameter("reportcategory_id",String.valueOf(this.iSaveCategory));
      add(admin);
    }
    add(Edit.formatText("Report Editor"));
  }
  protected void doChange(IWContext iwc) throws SQLException{

  }


  protected void doAdmin(IWContext iwc) throws SQLException{
    Report R = null;
    boolean b = false;
    if(this.iReportId >0 ){
      try {
        R = ((com.idega.block.reports.data.ReportHome)com.idega.data.IDOLookup.getHomeLegacy(Report.class)).findByPrimaryKeyLegacy(this.iReportId);
        b = true;
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
    Form form = new Form();
    Table T = new Table();

    Text nameText = Edit.formatText("Name");
    Text infoText = Edit.formatText("Info");
    Text headersText = Edit.formatText("Headers");
    Text sqlText = Edit.formatText("SQL");
    TextInput nameInput = new TextInput(this.prefix+"name",b?R.getName():"");
    TextInput infoInput = new TextInput(this.prefix+"info",b?R.getInfo():"");
    TextInput headersInput = new TextInput(this.prefix+"headers",b?R.getHeader():"");
    if(b) {
		form.add(new HiddenInput("report_id",String.valueOf(R.getID())));
	}
    headersInput.setLength(80);
    TextArea sqlInput = new TextArea(this.prefix+"sql",b?R.getSQL():"");
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

    T.add(new SubmitButton(this.iwb.getImage("/shared/ok.gif")));//new Image("/reports/pics/ok.gif")),1,9);
    T.add(new HiddenInput(this.sAction, String.valueOf(ACT1)),1,9);

    form.add(T);
    Link back =  new Link(this.iwb.getImage("/shared/newlist.gif"));//new Image("/reports/pics/newlist.gif"),"/reports/index.jsp");
    this.add(back);
    form.add(new HiddenInput("reportcategory_id",String.valueOf(this.iSaveCategory)));
    add(form);
  }

  private void doSave(IWContext iwc){
    String msg = "";
    String sName = iwc.getParameter(this.prefix+"name").trim();
    String sInfo = iwc.getParameter(this.prefix+"info").trim();
    String sHeaders = iwc.getParameter(this.prefix+"headers").trim();
    add(sHeaders);
    String sSql = iwc.getParameter(this.prefix+"sql").trim();
    String sReportId = iwc.getParameter("report_id");
    if(sName != null && sName.length() > 1 ){
      if(sSql != null && sHeaders!= null){
        String[] he = str2array(sHeaders,",:;");
        try{
          if(sReportId==null){
            Report R = ((com.idega.block.reports.data.ReportHome)com.idega.data.IDOLookup.getHomeLegacy(Report.class)).createLegacy();
            R.setCategoryId(this.iSaveCategory);
            R.setName(sName);
            R.setInfo(sInfo);
            R.setSQL(sSql);
            R.setHeaders(he);
            R.insert();
            msg = "Report was saved";
          }
          else{
            Report R = ((com.idega.block.reports.data.ReportHome)com.idega.data.IDOLookup.getHomeLegacy(Report.class)).findByPrimaryKeyLegacy(Integer.parseInt(sReportId));
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
	else {
		msg = "Needs a name";
	}
    Link back =  new Link(this.iwb.getImage("/shared/newlist.gif"));//new Image("/reports/pics/newlist.gif"),"/reports/index.jsp");
    this.add(back);
    add(Edit.formatText(msg));
  }


  protected void doUpdate(IWContext iwc) throws SQLException{
    String[] s = iwc.getParameterValues("box");
    ReportCondition[] RC = (ReportCondition[])iwc.getSessionAttribute(this.prefix+"force");
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
      temp = iwc.getParameter(this.prefix+"in"+i);
      if(!"".equalsIgnoreCase(temp) && !"0".equals(temp)){
        //add(" check "+i);
        ReportCondition rc = RC[i];
        rc.setVariable(temp);
        vRC.addElement(rc);

      }
    }
    iwc.removeSessionAttribute(this.prefix+"force");

    String name = iwc.getParameter(this.prefix+"name");
    String info = iwc.getParameter(this.prefix+"info");

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
        ReportItem RIx = ((com.idega.block.reports.data.ReportItemHome)com.idega.data.IDOLookup.getHomeLegacy(ReportItem.class)).createLegacy();
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
    Report R = ((com.idega.block.reports.data.ReportHome)com.idega.data.IDOLookup.getHomeLegacy(Report.class)).createLegacy();
    R.setCategoryId(this.iSaveCategory);
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
    Link L = new Link(this.iwb.getImage("/shared/newlist.gif"));//new Image("/reports/pics/newlist.gif"),"/reports/reportview.jsp");
    L.addParameter("report",R.getID());
    add(L);
    add(Edit.formatText("View the results"));
  }

}
