package com.idega.block.reports.presentation;

import java.sql.SQLException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;

import javax.faces.component.UIComponent;

import com.idega.block.reports.business.ReportCondition;
import com.idega.block.reports.business.ReportEntityHandler;
import com.idega.block.reports.business.ReportMaker;
import com.idega.block.reports.data.Report;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.SelectionDoubleBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.util.Edit;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class ReportSQLEditor extends Block implements Reports{

  private final String sAction = "rep.edit.action";
  protected final static int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5,ACT6=6,ACT7=7;

  protected boolean isAdmin = false;
  private String prefix = "rep.edit.";
  private String sManual = null;
  private int iCategoryId;
  private int iReportId = -1;
  private boolean useCheckBoxes = true;

  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  public final static String prmViewCategory = "rep_viewcategory_id";
  public final static String prmSaveCategory = "rep_savecategory_id";
	public final static String prmObjInstId = "rep_icobjinstid";
  public final static String prmDelim = ";";

  public ReportSQLEditor() {

  }

  public String getLocalizedNameKey(){
    return "report_sql_editor";
  }
  public String getLocalizedNameValue(){
    return "SQL Editor";
  }
  public void setManual(String manual){
    this.sManual = manual;
  }

  protected void control(IWContext iwc){
    Table T = new Table();
    T.setCellpadding(0);
    T.setCellspacing(0);
    try{
      if(true){

        if(iCategoryId <= 0 && iwc.isParameterSet(PRM_CATEGORYID)){
          iCategoryId = Integer.parseInt(iwc.getParameter(PRM_CATEGORYID ));
        }

        if(iwc.isParameterSet(PRM_REPORTID))
          iReportId = Integer.parseInt(iwc.getParameter(PRM_REPORTID));
        String sActPrm = "0";

        if(iwc.getParameter(sAction) != null)
          sActPrm = iwc.getParameter(sAction);
        else if(iwc.isParameterSet(PRM_REPORTID)){
          sActPrm = "2";
        }
        else if(useCheckBoxes ){
          sActPrm = "7";
        }
        else
          sActPrm = "0";
        try{
          int iAction = Integer.parseInt(sActPrm);
          switch(iAction){
            case ACT1: doSaveEdit(iwc);  /*T.add(getEditTable(iwc,iReportId),1,2); */ break;
            case ACT2: T.add(getEditTable(iwc,iReportId),1,2);  break;
            case ACT3: doChange(iwc); break;
            case ACT4: doUpdate(iwc); break;
            case ACT5: doCloseNoAction(); break;
            case ACT6: doUpdateSetup(iwc);break;
            case ACT7: T.add(getSetupTable(iwc,iReportId),1,2); break;
            default : T.add(getMakeTable(iwc,iReportId),1,2); break;
          }
        }
        catch(Exception e){
          e.printStackTrace();
        }
      }
      else
        add(Edit.formatText(iwrb.getLocalizedString("access_denied","Access denied")));
    }
    catch(Exception S){
      S.printStackTrace();
    }
    Form F = new Form();
    F.add(T);
    add(F);
  }

  private PresentationObject getMakeTable(IWContext iwc,int iReportId){
    Table T = new Table(3,5);
    if(iCategoryId > 0){
      List L = ReportEntityHandler.listOfReportConditions(iCategoryId );
			if(L != null && L.size() > 0){
        iwc.setSessionAttribute(prefix+"force",L);

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
        M.setColor(Edit.MiddleColor);
        M.setCellpadding(0);
        M.setCellspacing(0);
        ML.setColor(Edit.MiddleColor);
        ML.setCellpadding(0);
        ML.setCellspacing(0);
        MLL.setColor(Edit.MiddleColor);
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
        ML.setColor(Reports.MiddleColor);
        ML.setCellpadding(0);
        ML.setCellspacing(0);
        if(sManual != null)
          T.add(Edit.formatText(sManual),1,5);

        Text nameText = Edit.formatText(iwrb.getLocalizedString("name","Name"));
        Text infoText = Edit.formatText(iwrb.getLocalizedString("info","Info"));
        TextInput nameInput = new TextInput(prefix+"name");
        TextInput infoInput = new TextInput(prefix+"info");
        Edit.setStyle(nameInput);
        Edit.setStyle(infoInput);
        nameInput.setLength(80);
        infoInput.setLength(80);
        T.add(nameText,1,1);
        T.add(nameInput,2,1);
        T.add(infoText,1,2);
        T.add(infoInput,2,2);
        T.add(ML,1,3);
        SelectionBox box1 = box.getLeftBox();
        box1.keepStatusOnAction();
        Edit.setStyle(box1);
        SelectionBox box2 = box.getRightBox();
        Edit.setStyle(box2);
        box2.addUpAndDownMovers();
        int a = 0;
        int len = L.size();
        for (int i = 0; i < len; i++) {
          ReportCondition RC = (ReportCondition) L.get(i);
          box1.addMenuElement(i,RC.getDisplay());
          InterfaceObject mo = ReportObjectHandler.getInput(RC,prefix+"in"+i,"");
          Edit.setStyle(mo);
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
        T.add(new HiddenInput(sAction, String.valueOf(ACT4)),1,4);
        T.add(new HiddenInput(PRM_REPORTID,String.valueOf(iReportId)),1,4);
        T.add(new HiddenInput(PRM_CATEGORYID,String.valueOf(iCategoryId)),1,4);
        return T;
      }
    }
    else
      T.add(Edit.formatText(iwrb.getLocalizedString("nothing","Nothing to show")));

    return T;
  }

   private PresentationObject getSetupTable(IWContext iwc,int iReportId){
    Table T = new Table(3,5);
    if(iCategoryId > 0){
      List L = ReportEntityHandler.listOfReportConditions(iCategoryId );
      if(L != null && L.size() > 0){
        iwc.setSessionAttribute(prefix+"force",L);

        T.setWidth("100%");
        T.setCellpadding(1);
        T.setCellspacing(1);
        T.mergeCells(2,1,3,1);
        T.mergeCells(2,2,3,2);
        T.mergeCells(1,3,3,3);
        //T.mergeCells(1,4,3,4);
        T.mergeCells(1,5,3,5);

        Table ML = new Table();
        ML.setColor(Edit.colorMiddle);
        ML.setCellpadding(0);
        ML.setCellspacing(1);
        if(sManual != null)
          T.add(Edit.formatText(sManual),1,7);

        Text nameText = Edit.formatText(iwrb.getLocalizedString("name","Name"));
        Text infoText = Edit.formatText(iwrb.getLocalizedString("info","Info"));
        TextInput nameInput = new TextInput(prefix+"name");
        TextInput infoInput = new TextInput(prefix+"info");
        Edit.setStyle(nameInput);
        Edit.setStyle(infoInput);
        nameInput.setLength(80);
        infoInput.setLength(80);
        T.add(nameText,1,1);
        T.add(nameInput,2,1);
        T.add(infoText,1,2);
        T.add(infoInput,2,2);

        T.add(ML,1,3);
        ML.add(Edit.formatText(iwrb.getLocalizedString("fields","Fields")),1,1);
        ML.add(Edit.formatText(iwrb.getLocalizedString("select","Select")),2,1);
        ML.add(Edit.formatText(iwrb.getLocalizedString("function","Function")),3,1);
        ML.add(Edit.formatText(iwrb.getLocalizedString("condition","Condition")),4,1);
        ML.add(Edit.formatText(iwrb.getLocalizedString("operator","Operator")),5,1);
        ML.add(Edit.formatText(iwrb.getLocalizedString("condition","Condition")),6,1);
        ML.add(Edit.formatText(iwrb.getLocalizedString("colorder","Col order")),7,1);
        ML.add(Edit.formatText(iwrb.getLocalizedString("orderby","Order by")),8,1);
        TextInput ti,ti2;
        DropdownMenu op,func ;
        InterfaceObject mo,mo2;
        CheckBox chk;
        int a = 1;
        int len = L.size();
        for (int i = 0; i < len; i++) {
          ReportCondition RC = (ReportCondition) L.get(i);
          chk = new CheckBox(prefix+"chk"+i);
          mo = ReportObjectHandler.getInput(RC,prefix+"in"+i,"");
          op = ReportObjectHandler.drpOperators(prefix+"op"+i,RC.getOperator());
          mo2 = ReportObjectHandler.getInput(RC,prefix+"inn"+i,"");
          func = ReportObjectHandler.drpFunctions(prefix+"func"+i,"");

          ti = new TextInput(prefix+"ord"+i);
          ti.setAsIntegers();
          ti.setLength(2);

          ti2 = new TextInput(prefix+"col"+i);
          ti2.setAsIntegers();
          ti2.setLength(2);

          Edit.setStyle(chk);
          Edit.setStyle(mo);
          Edit.setStyle(mo2);
          Edit.setStyle(op);
          Edit.setStyle(ti);
          Edit.setStyle(ti2);
          Edit.setStyle(func);


          ML.add(Edit.formatText(RC.getDisplay()),1,++a);
          ML.add(chk,2,a);
          ML.add(func,3,a);
          ML.add(mo,4,a);
          ML.add(op,5,a);
          ML.add(mo2,6,a);
          ML.add(ti2,7,a);
          ML.add(ti,8,a);
        }
        ML.setWidth("100%");
        T.setVerticalAlignment(1,3,"top");
        T.setWidth("100%");
        SubmitButton save = new SubmitButton("save");
        CloseButton cancel = new CloseButton("cancel");
        Edit.setStyle(save);
        Edit.setStyle(cancel);
        T.add(cancel,1,4);
        T.add(save,1,4);
        T.setAlignment(3,4,"right");
        T.add(new HiddenInput(sAction, String.valueOf(ACT6)),1,4);
        T.add(new HiddenInput(PRM_CATEGORYID,String.valueOf(iCategoryId)),1,4);
         T.add(new HiddenInput(PRM_REPORTID,String.valueOf(iReportId)),1,4);
        return T;
      }
      else
        return getEditTable(iwc,-1);
    }
    else
      T.add(Edit.formatText(iwrb.getLocalizedString("nothing","Nothing to show")));

    return T;
  }


  protected void doUpdate(IWContext iwc){
    String[] s = iwc.getParameterValues("box");
    Vector RC = (Vector)iwc.getSessionAttribute(prefix+"force");
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
      temp = iwc.getParameter(prefix+"in"+i);
      if(!"".equalsIgnoreCase(temp) && !"0".equals(temp)){
        //add(" check "+i);
        ReportCondition rc = (ReportCondition) RC.get(i);
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

    String sql = rm.makeSQL(vRC);
    if(iCategoryId > 0){
      Report saved = ReportEntityHandler.saveReport(name,info,headers,sql,iCategoryId);

			if(saved!=null){

      }
      else{
        add(Edit.formatText(iwrb.getLocalizedString("report_not_saved","Report was not saved")));
      }
    }
  }

  protected PresentationObject doUpdateSetup(IWContext iwc){
    Vector RC = (Vector)iwc.getSessionAttribute(prefix+"force");
    Vector vRC = new Vector();
    TreeMap orderMap = new TreeMap();
    TreeMap headerMap = new TreeMap();
    int rlen = RC.size();
    String chk,in,ord,col,in2,op,func;
    Vector headers = new Vector();
    boolean use = false,colorder = false;
    for (int i = 0; i < rlen; i++) {
      ReportCondition rc = (ReportCondition) RC.get(i);
      chk = iwc.getParameter(prefix+"chk"+i);
      in = iwc.getParameter(prefix+"in"+i);
      in2 = iwc.getParameter(prefix+"inn"+i);
      op = iwc.getParameter(prefix+"op"+i);
      ord = iwc.getParameter(prefix+"ord"+i);
      col = iwc.getParameter(prefix+"col"+i);
      func = iwc.getParameter(prefix+"func"+i);

      if(!"".equalsIgnoreCase(in)){
        rc.setVariableOne(in);
        use = true;
      }
      if(!"".equalsIgnoreCase(in2)){
        rc.setVariableTwo(in2);
        use = true;
      }
      if(!"".equalsIgnoreCase(op)){
        rc.setOperator(op);
        use = true;
      }
      if(!"".equalsIgnoreCase(func)){
        rc.setFunction(func);
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
        if(colorder)
          headerMap.put(new Integer(col),rc.getDisplay());
        else
          headers.add( rc.getDisplay() );
      }
      if(use){
        //System.err.println(rc.getItem().getMainTable());
        if(colorder )
          orderMap.put(new Integer(col),rc);
        else
          vRC.add(rc);
      }
      use = false;
      colorder = false;
    }
    iwc.removeSessionAttribute(prefix+"force");
    headers.addAll(0,headerMap.values());
    String[] heads = new String[headers.size()];
    for (int i = 0; i < headers.size(); i++) {
      heads[i] = (String) headers.get(i);
    }

    String name = iwc.getParameter(prefix+"name");
    String info = iwc.getParameter(prefix+"info");

    name = name != null?name: "";
    info = info != null?info: "";

    Vector vConds = new Vector(orderMap.values());
    vConds.addAll(vRC );

    ReportMaker rm = new ReportMaker();

    String sql = rm.makeSQL(vConds);
    //add(sql);

    if(iCategoryId > 0){
      Report saved = ReportEntityHandler.saveReport(name,info,heads,sql,iCategoryId);
      if(saved!=null){
        int id = saved.getID();
        iReportId = id;
        return getEditTable(iwc,id);

      }
      else{

        return Edit.formatText(iwrb.getLocalizedString("report_not_saved","Report was not saved"));
      }
    }
    return Edit.formatText(iwrb.getLocalizedString("report_not_saved","Report was not saved"));
  }

  protected void doChange(IWContext iwc){

  }

  private PresentationObject getCloseLink(){
    Link back =  new Link(iwrb.getImage("/pics/close.gif"));
    back.addParameter(sAction,ACT5);
    return back;
  }

  private void doCloseNoAction(){

  }


  protected PresentationObject getEditTable(IWContext iwc,int iReportId){
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

    Text nameText = Edit.formatText(iwrb.getLocalizedString("name","Name"));
    Text infoText = Edit.formatText(iwrb.getLocalizedString("info","Info"));
    Text colInfoText = Edit.formatText(iwrb.getLocalizedString("colinfo","Column Info"));
    Text headersText = Edit.formatText(iwrb.getLocalizedString("headers","Headers"));
    Text sqlText = Edit.formatText(iwrb.getLocalizedString("sql","SQL"));
    TextInput nameInput = new TextInput(prefix+"name");
    TextInput infoInput = new TextInput(prefix+"info");
    TextInput colInfoInput = new TextInput(prefix+"colinfo");
    TextInput headersInput = new TextInput(prefix+"headers");
    TextArea sqlInput = new TextArea(prefix+"sql");

    nameInput.setLength(80);
    infoInput.setLength(80);
    colInfoInput.setLength(80);
    headersInput.setLength(80);
    sqlInput.setWidth(80);
    sqlInput.setHeight(8);

    Edit.setStyle(nameInput);
    Edit.setStyle(infoInput);
    Edit.setStyle(colInfoInput);
    Edit.setStyle(headersInput);
    Edit.setStyle(sqlInput);

    if(b){
      T.add(new HiddenInput(PRM_REPORTID,String.valueOf(R.getID())));
      T.add(new HiddenInput(prefix+"repid",String.valueOf(R.getID())));
      T.add(new HiddenInput(prefix+"repcatid",String.valueOf(R.getCategoryId())));
      nameInput.setContent(R.getName());
      infoInput.setContent(R.getInfo());
      if(R.getColInfo()!=null)
        colInfoInput.setContent(R.getColInfo());
      headersInput.setContent(R.getHeader());
      sqlInput.setContent(R.getSQL());
    }

    T.add(nameText,1,1);
    T.add(nameInput,1,2);
    T.add(infoText,1,3);
    T.add(infoInput,1,4);
    T.add(headersText,1,5);
    T.add(headersInput,1,6);
    T.add(colInfoText,1,7);
    T.add(colInfoInput,1,8);
    T.add(sqlText,1,9);
    T.add(sqlInput,1,10);

    T.add(new SubmitButton("Ok"),1,11);
    T.add(new HiddenInput(sAction, String.valueOf(ACT1)),1,11);
    T.add(new HiddenInput(PRM_REPORTID,String.valueOf(iReportId)));
    T.add(new HiddenInput(PRM_CATEGORYID,String.valueOf(iCategoryId)));
    return T;
  }

  private void doSaveEdit(IWContext iwc){
    String msg = "";
    String sName = iwc.getParameter(prefix+"name").trim();
    String sInfo = iwc.getParameter(prefix+"info").trim();
    String sColInfo = iwc.getParameter(prefix+"colinfo").trim();
    String sHeaders = iwc.getParameter(prefix+"headers").trim();

    String sSql = iwc.getParameter(prefix+"sql").trim();
    String sReportId = iwc.getParameter(prefix+"repid");
    String sReportCatId = iwc.getParameter(prefix+"repcatid");
    int catid = sReportCatId != null?Integer.parseInt(sReportCatId):-1;
    int id = sReportId!=null? Integer.parseInt(sReportId ):-1;


    Report saved = null;
    if(sName != null && sName.length() > 1 ){
      if(sSql != null && sHeaders!= null){
        String[] he = str2array(sHeaders,",:;");
        if(iCategoryId > 0){
          int iSaveCat = iCategoryId;
          if(id < 1 && catid != iSaveCat ){
            saved = ReportEntityHandler.saveReport(sName ,sInfo ,sColInfo,he,sSql,iSaveCat);
            if(saved!=null){
              msg = iwrb.getLocalizedString("report_saved","Report was saved");
			  iReportId = ((Integer) saved.getPrimaryKey()).intValue();
			 
            }
            else
              msg = iwrb.getLocalizedString("report_not_saved","Report was not saved");
          }
          else{
            saved = ReportEntityHandler.updateReport(id,sName ,sInfo ,sColInfo,he,sSql,iSaveCat);
            if(saved!=null){
              msg = iwrb.getLocalizedString("report_updated","Report was updated");
			  iReportId = ((Integer) saved.getPrimaryKey()).intValue();
			 
            }
            else
              msg = iwrb.getLocalizedString("report_not_updated","Report was not updated");
          }

        }
        else msg = iwrb.getLocalizedString("no_savecategories","No save categories");
      }
      else msg = iwrb.getLocalizedString("no_headers","No headers entered");
    }
    else
      msg = iwrb.getLocalizedString("no_name","No name entered");

    if(saved!=null){
    	Link savedLink =getReportLink(Edit.formatText(msg));
    	add(savedLink);
    }
    else{
    	add(Edit.formatText(msg));
    }
  }
  
  private Link getReportLink(PresentationObject object){
  	 Link L = new Link(object);
  	 L.addParameter(PRM_REPORTID,iReportId);
  	 L.addParameter(PRM_CATEGORYID,iCategoryId);
  	 return L;
  }
  
  private ReportViewer getInstanceOfReportViewer(){
  	UIComponent obj = this;
	UIComponent parent ;
  	while( (parent = obj.getParent()) != null){
  		if(parent instanceof ReportViewer)
  			return (ReportViewer)parent;
  		obj = parent;
  	}
  	return null;
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
    iwb = getBundle(iwc);
    iwrb = getResourceBundle(iwc);
    isAdmin = iwc.hasEditPermission(this);
    control(iwc);
    sManual = iwrb.getLocalizedString("manual","");
  }

}
