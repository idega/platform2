package se.idega.idegaweb.commune.childcare.check.presentation;

import java.util.*;

import se.idega.idegaweb.commune.presentation.*;
import se.idega.idegaweb.commune.childcare.check.data.*;
import se.idega.idegaweb.commune.childcare.check.business.*;

import com.idega.idegaweb.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.builder.data.IBPage;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Anders Lindman
 * @version 1.0
 */

public class CheckRequestAdmin extends CommuneBlock {

  private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune.childcare.check";

  private final static int ACTION_VIEW_CHECK_LIST = 1;
  private final static int ACTION_VIEW_CHECK = 2;
  private final static int ACTION_APPROVE_CHECK = 3;

  private final static String PARAM_VIEW_CHECK_LIST = "chk_v_c_l";
  private final static String PARAM_VIEW_CHECK = "chk_view_check";
  private final static String PARAM_APPROVE_CHECK = "chk_approve_check";
  private final static String PARAM_CHECK_ID = "chk_check_id";
  private final static String PARAM_RULE = "chk_rule";
  private final static String PARAM_NOTES = "chk_notes";

  private boolean isError = false;
  private String errorMessage = null;
  private boolean paramErrorRule1 = false;
  private boolean paramErrorRule2 = false;
  private boolean paramErrorRule3 = false;
  private boolean paramErrorRule4 = false;
  private boolean paramErrorRule5 = false;

//  private IBPage formResponsePage = null;

  public CheckRequestAdmin() {
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc){
    this.setResourceBundle(getResourceBundle(iwc));

    try{
      int action = parseAction(iwc);
      switch(action){
        case ACTION_VIEW_CHECK_LIST:
          viewCheckList(iwc);
          break;
        case ACTION_VIEW_CHECK:
          viewCheck(iwc);
          break;
        case ACTION_APPROVE_CHECK:
          approveCheck(iwc);
          break;
        default:
          break;
      }
    } catch (Exception e) {
      super.add(new ExceptionWrapper(e,this));
    }
  }

  private int parseAction(IWContext iwc){
    int action = ACTION_VIEW_CHECK_LIST;

    if(iwc.isParameterSet(PARAM_VIEW_CHECK)){
      action = ACTION_VIEW_CHECK;
    }

    if(iwc.isParameterSet(PARAM_APPROVE_CHECK)){
      action = ACTION_APPROVE_CHECK;
    }

    return action;
  }

  private void viewCheckList(IWContext iwc)throws Exception{
    ColumnList checkList = new ColumnList(5);
    checkList.setHeader("Checknummer",1);
    checkList.setHeader("Datum",2);
    checkList.setHeader("Personnummer",3);
    checkList.setHeader("Handläggare",4);
    checkList.setHeader("Status",5);

    Collection checks = getCheckBusiness(iwc).findChecks();
    Iterator iter = checks.iterator();
    while(iter.hasNext()){
      Check check = (Check)iter.next();
      Link l = getLink(check.getPrimaryKey().toString());
      l.addParameter(PARAM_VIEW_CHECK,"true");
      l.addParameter(PARAM_CHECK_ID,check.getPrimaryKey().toString());
      checkList.add(l);
      checkList.add(check.getCreated().toString().substring(0,10));
      checkList.add("930412-4231");
      checkList.add("Lisa Karlsson");
      checkList.add("Ny");
    }
    add(checkList);
  }

  private void viewCheck(IWContext iwc)throws Exception{
    int checkId = Integer.parseInt(iwc.getParameter(PARAM_CHECK_ID));
    Check check = getCheckBusiness(iwc).getCheck(checkId);

    Table frame = new Table();
    frame.setCellpadding(10);
    frame.setCellspacing(0);
    frame.setColor("#ffffcc");
    Table checkInfoTable = new Table(2,8);
    checkInfoTable.setCellpadding(6);
    checkInfoTable.setCellspacing(0);
    frame.add(checkInfoTable);
    checkInfoTable.add(getSmallHeader("Ärende nr:"),1,1);
    checkInfoTable.add(getSmallHeader("Ansökan avser:"),1,2);
    checkInfoTable.add(getSmallHeader("Barn:"),1,3);
    checkInfoTable.add(getSmallHeader("Vårdnadshavare:"),1,4);
    checkInfoTable.add(getSmallHeader("Språk moder-barn:"),1,6);
    checkInfoTable.add(getSmallHeader("Språk fader-barn:"),1,7);
    checkInfoTable.add(getSmallHeader("Språk föräldrar:"),1,8);
    checkInfoTable.add(getSmallText(check.getPrimaryKey().toString()),2,1);
    checkInfoTable.add(getSmallText("Förskoleverksamhet"),2,2);
    checkInfoTable.add(getSmallText("980312-3213, Mickelin Henrik, Odenvägen 2C 133 38 SALTSJÖBADEN"),2,3);
    checkInfoTable.add(getSmallText("Mickelin Maria Cecilia, 08-633 54 37, Studerande, Gift"),2,4);
    checkInfoTable.add(getSmallText("Mickelin Harry, 0709-732415, Arbetar, Gift"),2,5);
    checkInfoTable.add(getSmallText("Svenska"),2,6);
    checkInfoTable.add(getSmallText("Svenska"),2,7);
    checkInfoTable.add(getSmallText("Svenska"),2,8);
    add(frame);
    add(new Break(2));

    if(isError){
      add(getErrorText(errorMessage));
      add(new Break(2));
    }

    Form f = new Form();
    f.addParameter(PARAM_CHECK_ID,check.getPrimaryKey().toString());
    f.addParameter(PARAM_APPROVE_CHECK,"true");//temp
    frame = new Table(2,1);
    frame.setCellpadding(14);
    frame.setCellspacing(0);
    frame.setColor(getBackgroundColor());
    frame.setVerticalAlignment(2,1,"top");
    frame.add(getSmallText("Grundkrav"),1,1);
    frame.add(new Break(2));
    Table ruleTable = new Table(2,5);
    ruleTable.setCellpadding(4);
    ruleTable.setCellspacing(0);
    CheckBox rule1 = new CheckBox(PARAM_RULE,"1");
    if(isError){
      rule1.setChecked(!paramErrorRule1);
    }
    ruleTable.add(rule1,1,1);
    String ruleText1 = "Folkbokförd";
    if(paramErrorRule1){
      ruleTable.add(getErrorText(ruleText1),2,1);
    }else{
      ruleTable.add(getText(ruleText1),2,1);
    }
    CheckBox rule2 = new CheckBox(PARAM_RULE,"2");
    if(isError){
      rule2.setChecked(!paramErrorRule2);
    }
    ruleTable.add(rule2,1,2);
    String ruleText2 = "Barnet fyllt ett år";
    if(paramErrorRule2){
      ruleTable.add(getErrorText(ruleText2),2,2);
    }else{
      ruleTable.add(getText(ruleText2),2,2);
    }
    CheckBox rule3 = new CheckBox(PARAM_RULE,"3");
    if(isError){
      rule3.setChecked(!paramErrorRule3);
    }
    ruleTable.add(rule3,1,3);
    String ruleText3 = "Arbetssituation godkänd";
    if(paramErrorRule3){
      ruleTable.add(getErrorText(ruleText3),2,3);
    }else{
      ruleTable.add(getText(ruleText3),2,3);
    }
    CheckBox rule4 = new CheckBox(PARAM_RULE,"4");
    if(isError){
      rule4.setChecked(!paramErrorRule4);
    }
    ruleTable.add(rule4,1,4);
    String ruleText4 = "Skuldkontroll";
    if(paramErrorRule4){
      ruleTable.add(getErrorText(ruleText4),2,4);
    }else{
      ruleTable.add(getText(ruleText4),2,4);
    }
    CheckBox rule5 = new CheckBox(PARAM_RULE,"5");
    if(isError){
      rule5.setChecked(!paramErrorRule5);
    }
    ruleTable.add(rule5,1,5);
    String ruleText5 = "Behov av särskilt stöd";
    if(paramErrorRule5){
      ruleTable.add(getErrorText(ruleText5),2,5);
    }else{
      ruleTable.add(getText(ruleText5),2,5);
    }
    frame.add(ruleTable,1,1);
    frame.add(new Break(2),1,1);
    Link approveButton = new Link("Bevilja check");
    approveButton.setAsImageButton(true);
    approveButton.setToFormSubmit(f);
    frame.add(approveButton,1,1);
    frame.add(new Text("&nbsp;&nbsp;&nbsp;"));
    Link rejectButton = new Link("Omprövning");
    rejectButton.setAsImageButton(true);
    frame.add(rejectButton,1,1);
    frame.add(getSmallText("Noteringar"),2,1);
    frame.add(new Break(2),2,1);
    TextArea notes = new TextArea(PARAM_NOTES);
    notes.setHeight(8);
    notes.setWidth(50);
    frame.add(notes,2,1);
    f.add(frame);
    add(f);
  }

  private void approveCheck(IWContext iwc)throws Exception{
  //check rules
    int checkId = Integer.parseInt(iwc.getParameter(PARAM_CHECK_ID));
    Check check = getCheckBusiness(iwc).getCheck(checkId);

    String[] rules = iwc.getParameterValues(PARAM_RULE);
    paramErrorRule1 = true;
    paramErrorRule2 = true;
    paramErrorRule3 = true;
    paramErrorRule4 = true;
    paramErrorRule5 = true;
    if(rules==null){
      isError = true;
    }else{
      isError = !(rules.length==5);
      for(int i=0; i<rules.length; i++){
        int rule = Integer.parseInt(rules[i]);
        switch (rule) {
          case 1:
            paramErrorRule1 = false;
            break;
          case 2:
            paramErrorRule2 = false;
            break;
          case 3:
            paramErrorRule3 = false;
            break;
          case 4:
            paramErrorRule4 = false;
            break;
          case 5:
            paramErrorRule5 = false;
            break;
        }
      }
    }
    if(isError){
      errorMessage = "Du måste bocka av samtliga regler";
      viewCheck(iwc);
      return;
    }
    add(getText("Check approved:"+rules.length+","+rules[0]));
  }

  private CheckBusiness getCheckBusiness(IWContext iwc)throws Exception{
    return (CheckBusiness)com.idega.business.IBOLookup.getServiceInstance(iwc,CheckBusiness.class);
  }
}
