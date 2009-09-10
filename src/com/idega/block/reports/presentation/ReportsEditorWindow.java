package com.idega.block.reports.presentation;

import java.sql.SQLException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;

import com.idega.block.reports.business.ReportCondition;
import com.idega.block.reports.business.ReportEntityHandler;
import com.idega.block.reports.business.ReportMaker;
import com.idega.block.reports.data.Report;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.SelectionDoubleBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class ReportsEditorWindow extends IWAdminWindow {

  private final String sAction = "rep.edit.action";
  protected final static int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5,ACT6=6,ACT7=7;

  protected boolean isAdmin = false;
  private String prefix = "rep.edit.";
  private String sManual = null;
  private int iCategory;
  private int iSaveCategory = -1;
  private int iReportId = -1;
  private Integer[] iSaveCategoryIds = null, iViewCategoryIds = null;
  private String sSaveCategories = null,sViewCategories = null;
  private boolean useCheckBoxes = true;

	public static final  String prmCategoryId = "rep_categoryid";
	public  final static String prmDelete = "rep_deleteid";

  private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.reports";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  public final static String prmViewCategory = "rep_viewcategory_id";
  public final static String prmSaveCategory = "rep_savecategory_id";
  public final static String prmReportId = "rep_report_id";
	public final static String prmObjInstId = "rep_icobjinstid";
  public final static String prmDelim = ";";

  public ReportsEditorWindow() {
    setWidth(500);
    setHeight(460);
    setResizable(true);
  }
  public void setManual(String manual){
    this.sManual = manual;
  }

  protected void control(IWContext iwc){
    Table T = new Table();
    T.setCellpadding(0);
    T.setCellspacing(0);
    try{
        if(this.isAdmin){
          this.sSaveCategories = iwc.getParameter(prmSaveCategory);
          this.sViewCategories = iwc.getParameter(prmViewCategory);
          if(this.sSaveCategories!=null){
            this.iSaveCategoryIds = str2IntegerArray(this.sSaveCategories,prmDelim);
          }
          if(this.sViewCategories !=null){
            this.iViewCategoryIds = str2IntegerArray(this.sViewCategories,prmDelim);
          }
          else if(this.iSaveCategoryIds != null){
            this.iViewCategoryIds = this.iSaveCategoryIds;
          }

          String sActPrm = "0";

          if(iwc.getParameter(this.sAction) != null) {
			sActPrm = iwc.getParameter(this.sAction);
		}
		else if(iwc.getParameter(prmReportId)!=null){
            this.iReportId = Integer.parseInt(iwc.getParameter(prmReportId));
            sActPrm = "2";
          }
          else if(this.useCheckBoxes ){
            sActPrm = "7";
          }
		else {
			sActPrm = "0";
		}
          try{
            int iAction = Integer.parseInt(sActPrm);
            switch(iAction){
              case ACT1: doSaveEdit(iwc);   break;
              case ACT2: T.add(getEditTable(iwc,this.iReportId),1,2);  break;
              case ACT3: doChange(iwc); break;
              case ACT4: doUpdate(iwc); break;
              case ACT5: doCloseNoAction(); break;
              case ACT6: doUpdateSetup(iwc);break;
              case ACT7: T.add(getSetupTable(iwc),1,2); break;
              default : T.add(getMakeTable(iwc),1,2); break;
            }
          }
          catch(Exception e){
            e.printStackTrace();
          }
        }
		else {
			add(formatText(this.iwrb.getLocalizedString("access_denied","Access denied")));
		}
    }
    catch(Exception S){
      S.printStackTrace();
    }
    Form F = new Form();
    F.add(T);
    add(F);
  }

  private PresentationObject getMakeTable(IWContext iwc){
    Table T = new Table(3,5);
    if(this.iViewCategoryIds != null){
      Vector V = new Vector();
      for (int i = 0; i < this.iViewCategoryIds.length; i++) {
        List L = ReportEntityHandler.listOfReportConditions(this.iViewCategoryIds[i].intValue() );
        if(L != null) {
			V.addAll(L);
		}
      }
      if(V != null && V.size() > 0){
        iwc.setSessionAttribute(this.prefix+"force",V);

        T.setWidth("100%");
        T.setCellpadding(1);
        T.setCellspacing(1);
        T.mergeCells(2,1,3,1);
        T.mergeCells(2,2,3,2);
        T.mergeCells(1,3,3,3);
        //T.mergeCells(1,4,3,4);
        T.mergeCells(1,5,3,5);
        SelectionDoubleBox box = new SelectionDoubleBox("box","Fields","Order");
/*
        Table U = new Table();
        Table M = new Table();
        Table ML = new Table();
        Table MLL = new Table();
        Table B = new Table();
        U.setCellpadding(0);
        U.setCellspacing(0);
        M.setColor(ReportPresentation.MiddleColor);
        M.setCellpadding(0);
        M.setCellspacing(0);
        ML.setColor(ReportPresentation.MiddleColor);
        ML.setCellpadding(0);
        ML.setCellspacing(0);
        MLL.setColor(ReportPresentation.MiddleColor);
        MLL.setCellpadding(0);
        MLL.setCellspacing(0);
        MLL.setVerticalAlignment("top");
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
        */

        Table ML = new Table();
        ML.setColor(ReportPresentation.MiddleColor);
        ML.setCellpadding(0);
        ML.setCellspacing(0);
        if(this.sManual != null) {
			T.add(this.formatText(this.sManual),1,5);
		}

        Text nameText = ReportPresentation.formatText(this.iwrb.getLocalizedString("name","Name"));
        Text infoText = ReportPresentation.formatText(this.iwrb.getLocalizedString("info","Info"));
        TextInput nameInput = new TextInput(this.prefix+"name");
        TextInput infoInput = new TextInput(this.prefix+"info");
        ReportPresentation.setStyle(nameInput);
        ReportPresentation.setStyle(infoInput);
        nameInput.setLength(80);
        infoInput.setLength(80);
        T.add(nameText,1,1);
        T.add(nameInput,2,1);
        T.add(infoText,1,2);
        T.add(infoInput,2,2);
        T.add(ML,1,3);
        SelectionBox box1 = box.getLeftBox();
        box1.keepStatusOnAction();
        ReportPresentation.setStyle(box1);
        SelectionBox box2 = box.getRightBox();
        ReportPresentation.setStyle(box2);
        box2.addUpAndDownMovers();
        int a = 0;
        int len = V.size();
        for (int i = 0; i < len; i++) {
          ReportCondition RC = (ReportCondition) V.get(i);
          box1.addMenuElement(i,RC.getDisplay());
          InterfaceObject mo = ReportObjectHandler.getInput(RC,this.prefix+"in"+i,"");
          ReportPresentation.setStyle(mo);
          ML.add(RC.getDisplay(),3,++a);
          ML.add(mo,4,a);
        }
        ML.setWidth("100%");
        ML.mergeCells(1,1,1,a);
        ML.add(box,1,1);
        box1.setHeight(20);
        box2.setHeight(20);
        box2.selectAllOnSubmit();
        T.setVerticalAlignment(1,3,"top");
        T.setWidth("100%");
        T.add(new CloseButton("cancel"),3,4);
        T.add(new SubmitButton("save"),3,4);
        T.setAlignment(3,4,"right");
        T.add(new HiddenInput(this.sAction, String.valueOf(ACT4)),1,4);
        T.add(new HiddenInput(prmSaveCategory,this.sSaveCategories),1,4);
        return T;
      }
    }
	else {
		T.add(ReportPresentation.formatText(this.iwrb.getLocalizedString("nothing","Nothing to show")));
	}

    return T;
  }

   private PresentationObject getSetupTable(IWContext iwc){
    Table T = new Table(3,5);
    if(this.iViewCategoryIds != null){
      Vector V = new Vector();
      for (int i = 0; i < this.iViewCategoryIds.length; i++) {
        List L = ReportEntityHandler.listOfReportConditions(this.iViewCategoryIds[i].intValue() );
        if(L != null) {
			V.addAll(L);
		}
      }
      if(V != null && V.size() > 0){
        iwc.setSessionAttribute(this.prefix+"force",V);

        T.setWidth("100%");
        T.setCellpadding(1);
        T.setCellspacing(1);
        T.mergeCells(2,1,3,1);
        T.mergeCells(2,2,3,2);
        T.mergeCells(1,3,3,3);
        //T.mergeCells(1,4,3,4);
        T.mergeCells(1,5,3,5);


        Table ML = new Table();
        ML.setColor(ReportPresentation.MiddleColor);
        ML.setCellpadding(0);
        ML.setCellspacing(1);
        if(this.sManual != null) {
			T.add(this.formatText(this.sManual),1,5);
		}

        Text nameText = ReportPresentation.formatText(this.iwrb.getLocalizedString("name","Name"));
        Text infoText = ReportPresentation.formatText(this.iwrb.getLocalizedString("info","Info"));
        TextInput nameInput = new TextInput(this.prefix+"name");
        TextInput infoInput = new TextInput(this.prefix+"info");
        ReportPresentation.setStyle(nameInput);
        ReportPresentation.setStyle(infoInput);
        nameInput.setLength(80);
        infoInput.setLength(80);
        T.add(nameText,1,1);
        T.add(nameInput,2,1);
        T.add(infoText,1,2);
        T.add(infoInput,2,2);

        T.add(ML,1,3);
        ML.add(ReportPresentation.formatText(this.iwrb.getLocalizedString("fields","Fields")),1,1);
        ML.add(ReportPresentation.formatText(this.iwrb.getLocalizedString("select","Select")),2,1);
        ML.add(ReportPresentation.formatText(this.iwrb.getLocalizedString("condition","Condition")),3,1);
         ML.add(ReportPresentation.formatText(this.iwrb.getLocalizedString("colorder","Col order")),4,1);
        ML.add(ReportPresentation.formatText(this.iwrb.getLocalizedString("orderby","Order by")),5,1);
        TextInput ti,ti2;
        InterfaceObject mo;
        CheckBox chk;
        int a = 1;
        int len = V.size();
        for (int i = 0; i < len; i++) {
          ReportCondition RC = (ReportCondition) V.get(i);
          chk = new CheckBox(this.prefix+"chk"+i);
          mo = ReportObjectHandler.getInput(RC,this.prefix+"in"+i,"");

          ti = new TextInput(this.prefix+"ord"+i);
          ti.setAsIntegers();
          ti.setLength(2);

          ti2 = new TextInput(this.prefix+"col"+i);
          ti2.setAsIntegers();
          ti2.setLength(2);

          ReportPresentation.setStyle(chk);
          ReportPresentation.setStyle(mo);
          ReportPresentation.setStyle(ti);
          ReportPresentation.setStyle(ti2);

          ML.add(ReportPresentation.formatText(RC.getDisplay()),1,++a);
          ML.add(chk,2,a);
          ML.add(mo,3,a);
          ML.add(ti2,4,a);
          ML.add(ti,5,a);
        }
        ML.setWidth("100%");
        T.setVerticalAlignment(1,3,"top");
        T.setWidth("100%");
        SubmitButton save = new SubmitButton("save");
        CloseButton cancel = new CloseButton("cancel");
        ReportPresentation.setStyle(save);
        ReportPresentation.setStyle(cancel);
        T.add(cancel,3,4);
        T.add(save,3,4);
        T.setAlignment(3,4,"right");
        T.add(new HiddenInput(this.sAction, String.valueOf(ACT6)),1,4);
        T.add(new HiddenInput(prmSaveCategory,this.sSaveCategories),1,4);
        return T;
      }
    }
	else {
		T.add(ReportPresentation.formatText(this.iwrb.getLocalizedString("nothing","Nothing to show")));
	}

    return T;
  }


  protected void doUpdate(IWContext iwc) throws SQLException{
    String[] s = iwc.getParameterValues("box");
    Vector RC = (Vector)iwc.getSessionAttribute(this.prefix+"force");
    Vector vRC = new Vector();
    int slen = s.length;
    String[] headers = new String[slen];
    for (int i = 0; i < slen; i++) {
      ReportCondition rc = (ReportCondition) RC.get(Integer.parseInt(s[i]));
      headers[i] = rc.getDisplay();
      rc.setIsSelect();
      vRC.addElement(rc);
    }
    String temp;
    int rlen = RC.size();
    for (int i = 0; i < rlen; i++) {
      temp = iwc.getParameter(this.prefix+"in"+i);
      if(!"".equalsIgnoreCase(temp) && !"0".equals(temp)){
        //add(" check "+i);
        ReportCondition rc = (ReportCondition) RC.get(i);
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

    String sql = rm.makeSQL(vRC);
    if(this.iSaveCategoryIds != null){
      int count = 0;
      for (int i = 0; i < this.iSaveCategoryIds.length; i++) {
        Report saved = ReportEntityHandler.saveReport(name,info,headers,sql,this.iSaveCategoryIds[i].intValue() );
        if(saved!=null) {
			count++;
		}
      }
      if(count > 0 && count == this.iSaveCategoryIds.length){
        setParentToReload();
        close();
      }
      else{
        add(ReportPresentation.formatText(this.iwrb.getLocalizedString("report_not_saved","Report was not saved")));
      }
    }
  }

  protected void doUpdateSetup(IWContext iwc) throws SQLException{
    Vector RC = (Vector)iwc.getSessionAttribute(this.prefix+"force");
    Vector vRC = new Vector();
    TreeMap orderMap = new TreeMap();
    TreeMap headerMap = new TreeMap();
    int rlen = RC.size();
    String chk,in,ord,col;
    Vector headers = new Vector();
    boolean use = false,colorder = false;
    for (int i = 0; i < rlen; i++) {
      ReportCondition rc = (ReportCondition) RC.get(i);
      chk = iwc.getParameter(this.prefix+"chk"+i);
      in = iwc.getParameter(this.prefix+"in"+i);
      ord = iwc.getParameter(this.prefix+"ord"+i);
      col = iwc.getParameter(this.prefix+"col"+i);

      if(!"".equalsIgnoreCase(in) && !"0".equals(in)){
        rc.setVariable(in);
        use = true;
      }
      if(ord != null && ord.length() > 0){
        rc.setOrder(new Integer(ord));
        use = true;
      }
      if(col!= null && col.length() > 0){
        rc.setColumnOrder(new Integer(col));
        use = true;
        colorder = true;
      }
      if(chk!= null){
        rc.setIsSelect();
        use = true;
        if(colorder) {
			headerMap.put(new Integer(col),rc.getDisplay());
		}
		else {
			headers.add( rc.getDisplay() );
		}
      }
      if(use){
        //System.err.println(rc.getItem().getMainTable());
        if(colorder ) {
			orderMap.put(new Integer(col),rc);
		}
		else {
			vRC.add(rc);
		}
      }
      use = false;
      colorder = false;
    }
    iwc.removeSessionAttribute(this.prefix+"force");
    headers.addAll(0,headerMap.values());
    String[] heads = new String[headers.size()];
    for (int i = 0; i < headers.size(); i++) {
      heads[i] = (String) headers.get(i);
    }

    String name = iwc.getParameter(this.prefix+"name");
    String info = iwc.getParameter(this.prefix+"info");

    name = name != null?name: "";
    info = info != null?info: "";

    Vector vConds = new Vector(orderMap.values());
    vConds.addAll(vRC );

    ReportMaker rm = new ReportMaker();

    String sql = rm.makeSQL(vConds);
    //add(sql);

    if(this.iSaveCategoryIds != null){
      int count = 0;
      for (int i = 0; i < this.iSaveCategoryIds.length; i++) {
        Report saved = ReportEntityHandler.saveReport(name,info,heads,sql,this.iSaveCategoryIds[i].intValue());
        if(saved!=null) {
			count++;
		}
      }
      if(count > 0 && count == this.iSaveCategoryIds.length){
        setParentToReload();
        close();
      }
      else{
        add(ReportPresentation.formatText(this.iwrb.getLocalizedString("report_not_saved","Report was not saved")));
      }
    }

  }


  protected void doChange(IWContext iwc) throws SQLException{

  }

  private PresentationObject getCloseLink(){
    Link back =  new Link(this.iwrb.getImage("/pics/close.gif"));
    back.addParameter(this.sAction,ACT5);
    return back;
  }

  private void doCloseNoAction(){
    close();
  }


  protected PresentationObject getEditTable(IWContext iwc,int iReportId) throws SQLException{
    Report R = null;
    boolean b = false;
    if(iReportId >0 ){
      try {
        R = ((com.idega.block.reports.data.ReportHome)com.idega.data.IDOLookup.getHomeLegacy(Report.class)).findByPrimaryKeyLegacy(iReportId);
        b = true;
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
    }

    Table T = new Table();

    Text nameText = ReportPresentation.formatText(this.iwrb.getLocalizedString("name","Name"));
    Text infoText = ReportPresentation.formatText(this.iwrb.getLocalizedString("info","Info"));
    Text headersText = ReportPresentation.formatText(this.iwrb.getLocalizedString("headers","Headers"));
    Text sqlText = ReportPresentation.formatText(this.iwrb.getLocalizedString("sql","SQL"));
    TextInput nameInput = new TextInput(this.prefix+"name");
    TextInput infoInput = new TextInput(this.prefix+"info");
    TextInput headersInput = new TextInput(this.prefix+"headers");
    TextArea sqlInput = new TextArea(this.prefix+"sql");

    nameInput.setLength(80);
    infoInput.setLength(80);
    headersInput.setLength(80);
    sqlInput.setWidth(80);
    sqlInput.setHeight(8);

    ReportPresentation.setStyle(nameInput);
    ReportPresentation.setStyle(infoInput);
    ReportPresentation.setStyle(headersInput);
    ReportPresentation.setStyle(sqlInput);

    if(b){
      T.add(new HiddenInput(this.prefix+"repid",String.valueOf(R.getID())));
      T.add(new HiddenInput(this.prefix+"repcatid",String.valueOf(R.getCategoryId())));
      nameInput.setContent(R.getName());
      infoInput.setContent(R.getInfo());
      headersInput.setContent(R.getHeader());
      sqlInput.setContent(R.getSQL());
    }

    T.add(nameText,1,1);
    T.add(nameInput,1,2);
    T.add(infoText,1,3);
    T.add(infoInput,1,4);
    T.add(headersText,1,5);
    T.add(headersInput,1,6);
    T.add(sqlText,1,7);
    T.add(sqlInput,1,8);

    T.add(new SubmitButton(this.iwrb.getImage("/pics/ok.gif")),1,9);
    T.add(new HiddenInput(this.sAction, String.valueOf(ACT1)),1,9);
    T.add(new HiddenInput(prmSaveCategory,this.sSaveCategories));
    return T;
  }

  private void doSaveEdit(IWContext iwc){
    String msg = "";
    String sName = iwc.getParameter(this.prefix+"name").trim();
    String sInfo = iwc.getParameter(this.prefix+"info").trim();
    String sHeaders = iwc.getParameter(this.prefix+"headers").trim();

    String sSql = iwc.getParameter(this.prefix+"sql").trim();
    String sReportId = iwc.getParameter(this.prefix+"repid");
    String sReportCatId = iwc.getParameter(this.prefix+"repcatid");
    int catid = sReportCatId != null?Integer.parseInt(sReportCatId):-1;
    int id = sReportId!=null? Integer.parseInt(sReportId ):-1;

    Report saved = null;
    if(sName != null && sName.length() > 1 ){
      if(sSql != null && sHeaders!= null){
        String[] he = str2array(sHeaders,",:;");
        if(this.iSaveCategoryIds != null){
          for (int i = 0; i < this.iSaveCategoryIds.length; i++) {
            int iSaveCat = this.iSaveCategoryIds[i].intValue();
            if(id < 1 && catid != iSaveCat ){
              saved = ReportEntityHandler.saveReport(sName ,sInfo ,he,sSql,iSaveCat);
              if(saved!=null) {
				msg = this.iwrb.getLocalizedString("report_saved","Report was saved");
			}
			else {
				msg = this.iwrb.getLocalizedString("report_not_saved","Report was not saved");
			}
            }
            else{
              saved = ReportEntityHandler.updateReport(id,sName ,sInfo ,he,sSql,iSaveCat);
              if(saved!=null) {
				msg = this.iwrb.getLocalizedString("report_updated","Report was updated");
			}
			else {
				msg = this.iwrb.getLocalizedString("report_not_updated","Report was not updated");
			}
            }
          }
        }
		else {
			msg = this.iwrb.getLocalizedString("no_savecategories","No save categories");
		}
      }
	else {
		msg = this.iwrb.getLocalizedString("no_headers","No headers entered");
	}
    }
	else {
		msg = this.iwrb.getLocalizedString("no_name","No name entered");
	}

    if(saved!=null){
      setParentToReload();
      close();
    }
    else{
      add(ReportPresentation.formatText(msg));
    }
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

  private Integer[] str2IntegerArray(String s,String delim){
    StringTokenizer st = new StringTokenizer(s,delim);
    Integer[] array = new Integer[st.countTokens()];
    int i = 0;
    while(st.hasMoreTokens()){
      try{
        array[i++] = new Integer(st.nextToken());
      }
      catch(NumberFormatException nfe){}
    }
    return array;
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc) throws Exception{
    super.main(iwc);
    this.iwb = getBundle(iwc);
    this.iwrb = getResourceBundle(iwc);
    String title = this.iwrb.getLocalizedString("report_editor","Report Editor");
    setTitle(title);
    addTitle(title);

    this.isAdmin = iwc.hasEditPermission(this);

    control(iwc);
    this.sManual = this.iwrb.getLocalizedString("manual","");
  }

}
