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

  private final static int ACTION_VIEW_CHECK_LIST = 1;
  private final static int ACTION_VIEW_CHECK = 2;
  private final static int ACTION_GRANT_CHECK = 3;
  private final static int ACTION_RETRIAL_CHECK = 4;
  private final static int ACTION_SAVE_CHECK = 5;

  private final static String PARAM_VIEW_CHECK_LIST = "chk_v_c_l";
  private final static String PARAM_VIEW_CHECK = "chk_view_check";
  private final static String PARAM_GRANT_CHECK = "chk_grant_check";
  private final static String PARAM_RETRIAL_CHECK = "chk_retrial_check";
  private final static String PARAM_SAVE_CHECK = "chk_save_check";
  private final static String PARAM_CHECK_ID = "chk_check_id";
  private final static String PARAM_RULE = "chk_rule";
  private final static String PARAM_NOTES = "chk_notes";

//  private IBPage formResponsePage = null;

  public CheckRequestAdmin() {
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
          int checkId = Integer.parseInt(iwc.getParameter(PARAM_CHECK_ID));
          Check check = getCheckBusiness(iwc).getCheck(checkId);
          viewCheck(iwc,check,false);
          break;
        case ACTION_GRANT_CHECK:
          grantCheck(iwc);
          break;
        case ACTION_RETRIAL_CHECK:
          retrialCheck(iwc);
          break;
        case ACTION_SAVE_CHECK:
          saveCheck(iwc);
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

    if(iwc.isParameterSet(PARAM_GRANT_CHECK)){
      action = ACTION_GRANT_CHECK;
    }

    if(iwc.isParameterSet(PARAM_RETRIAL_CHECK)){
      action = ACTION_RETRIAL_CHECK;
    }

    if(iwc.isParameterSet(PARAM_SAVE_CHECK)){
      action = ACTION_SAVE_CHECK;
    }

    return action;
  }

  private void viewCheckList(IWContext iwc)throws Exception{
    ColumnList checkList = new ColumnList(5);
    checkList.setWidth("600");
    checkList.setHeader(localize("check.check_number","Check number"),1);
    checkList.setHeader(localize("check.date","Date"),2);
    checkList.setHeader(localize("check.social_security_number","Social security number"),3);
    checkList.setHeader(localize("check.manager","Manager"),4);
    checkList.setHeader(localize("check.status","Status"),5);

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

  private void viewCheck(IWContext iwc, Check check,boolean isError)throws Exception{

    Table frame = new Table();
    frame.setCellpadding(10);
    frame.setCellspacing(0);
    frame.setColor("#ffffcc");
    Table checkInfoTable = new Table(2,8);
    checkInfoTable.setCellpadding(6);
    checkInfoTable.setCellspacing(0);
    frame.add(checkInfoTable);
    checkInfoTable.add(getLocalizedSmallHeader("check.case_number","Case number"),1,1);
    checkInfoTable.add(getSmallHeader(":"),1,1);
    checkInfoTable.add(getLocalizedSmallHeader("check.request_regarding","Request regarding"),1,2);
    checkInfoTable.add(getSmallHeader(":"),1,2);
    checkInfoTable.add(getLocalizedSmallHeader("check.child","Child"),1,3);
    checkInfoTable.add(getSmallHeader(":"),1,3);
    checkInfoTable.add(getLocalizedSmallHeader("check.custodians","Custodians"),1,4);
    checkInfoTable.add(getSmallHeader(":"),1,4);
    checkInfoTable.add(getLocalizedSmallHeader("check.language_mother_child","Language mother-child"),1,6);
    checkInfoTable.add(getSmallHeader(":"),1,6);
    checkInfoTable.add(getLocalizedSmallHeader("check.language_father_child","Language father-child"),1,7);
    checkInfoTable.add(getSmallHeader(":"),1,7);
    checkInfoTable.add(getLocalizedSmallHeader("check.language_parents","Language parents"),1,8);
    checkInfoTable.add(getSmallHeader(":"),1,8);
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
      add(getErrorText(localize("check.must_check_all_rules","All rules must be checked.")));
      add(new Break(2));
    }

    Form f = new Form();
    f.addParameter(PARAM_CHECK_ID,check.getPrimaryKey().toString());
    frame = new Table(2,1);
    frame.setCellpadding(14);
    frame.setCellspacing(0);
    frame.setColor(getBackgroundColor());
    frame.setVerticalAlignment(2,1,"top");
    frame.add(getLocalizedSmallText("check.requirements","Requirements"),1,1);
    frame.add(new Break(2));
    Table ruleTable = new Table(2,5);
    ruleTable.setCellpadding(4);
    ruleTable.setCellspacing(0);
    CheckBox rule1 = new CheckBox(PARAM_RULE,"1");
    rule1.setChecked(check.getRule1());
    ruleTable.add(rule1,1,1);
    String ruleText1 = localize("check.nationally_registered","Nationally registered");
    if(check.getRule1()||!isError){
      ruleTable.add(getText(ruleText1),2,1);
    }else{
      ruleTable.add(getErrorText(ruleText1),2,1);
    }
    CheckBox rule2 = new CheckBox(PARAM_RULE,"2");
    rule2.setChecked(check.getRule2());
    ruleTable.add(rule2,1,2);
    String ruleText2 = localize("check.child_one_year","Child one year of age");
    if(check.getRule2()||!isError){
      ruleTable.add(getText(ruleText2),2,2);
    }else{
      ruleTable.add(getErrorText(ruleText2),2,2);
    }
    CheckBox rule3 = new CheckBox(PARAM_RULE,"3");
    rule3.setChecked(check.getRule3());
    ruleTable.add(rule3,1,3);
    String ruleText3 = localize("check.work_situation_approved","Work situation approved");
    if(check.getRule3()||!isError){
      ruleTable.add(getText(ruleText3),2,3);
    }else{
      ruleTable.add(getErrorText(ruleText3),2,3);
    }
    CheckBox rule4 = new CheckBox(PARAM_RULE,"4");
    rule4.setChecked(check.getRule4());
    ruleTable.add(rule4,1,4);
    String ruleText4 = localize("check.dept_control","Skuldkontroll");
    if(check.getRule4()||!isError){
      ruleTable.add(getText(ruleText4),2,4);
    }else{
      ruleTable.add(getErrorText(ruleText4),2,4);
    }
    CheckBox rule5 = new CheckBox(PARAM_RULE,"5");
    rule5.setChecked(check.getRule5());
    ruleTable.add(rule5,1,5);
    String ruleText5 = localize("check.need_for_special_support","Need for special support");
    if(check.getRule5()||!isError){
      ruleTable.add(getText(ruleText5),2,5);
    }else{
      ruleTable.add(getErrorText(ruleText5),2,5);
    }
    frame.add(ruleTable,1,1);
    frame.add(new Break(2),1,1);
    Image image = getResourceBundle().getLocalizedImageButton("check.grant_check","Grant check");
    SubmitButton grantButton = new SubmitButton(image,PARAM_GRANT_CHECK);
    frame.add(grantButton,1,1);
    frame.add(new Text("&nbsp;&nbsp;&nbsp;"));
    image = getResourceBundle().getLocalizedImageButton("check.retrial","Retrial");
    SubmitButton retrialButton = new SubmitButton(image,PARAM_RETRIAL_CHECK);
    frame.add(retrialButton,1,1);
    frame.add(new Text("&nbsp;&nbsp;&nbsp;"));
    image = getResourceBundle().getLocalizedImageButton("check.save","Save");
    SubmitButton saveButton = new SubmitButton(image,PARAM_SAVE_CHECK);
    frame.add(saveButton,1,1);
    frame.add(getLocalizedSmallText("check.notes","Notes"),2,1);
    frame.add(new Break(2),2,1);
    TextArea notes = new TextArea(PARAM_NOTES);
    notes.setValue(check.getNotes());
    notes.setHeight(8);
    notes.setWidth(50);
    frame.add(notes,2,1);
    f.add(frame);
    add(f);
  }

  private CheckBusiness verifyCheckRules(IWContext iwc)throws Exception{
    int checkId = Integer.parseInt(iwc.getParameter(PARAM_CHECK_ID));
    String[] selectedRules = iwc.getParameterValues(PARAM_RULE);
    String notes = iwc.getParameter(PARAM_NOTES);
//    int managerId = iwc.getUser().getID();
    int managerId = 123;
    CheckBusiness cb = getCheckBusiness(iwc);
    cb.verifyCheckRules(checkId,selectedRules,notes,managerId);
    return cb;
  }

  private void grantCheck(IWContext iwc)throws Exception{
    CheckBusiness cb = verifyCheckRules(iwc);
    if(!cb.allRulesVerified()){
      cb.commit();
//      this.errorMessage = localize("check.must_check_all_rules","All rules must be checked.");
//      this.isError = true;
      viewCheck(iwc,cb.getCurrentCheck(),true);
      return;
    }
    cb.approveCheck();
    cb.commit();

//Create message for archive
//Create post message to citizen

    String subject = "...";
    String body = "...";
//    int managerId = iwc.getUser().getID();
    int managerId = 123;
    cb.sendMessageToCitizen(subject,body,managerId);

    add(getText("Check granted:"));
    viewCheckList(iwc);
  }

  private void retrialCheck(IWContext iwc)throws Exception{
    CheckBusiness cb = verifyCheckRules(iwc);
    cb.retrialCheck();
    cb.commit();
    viewCheckList(iwc);
//Create message to user
  }

  private void saveCheck(IWContext iwc)throws Exception{
    CheckBusiness cb = verifyCheckRules(iwc);
    cb.saveCheck();
    cb.commit();
    viewCheckList(iwc);
  }

  private CheckBusiness getCheckBusiness(IWContext iwc)throws Exception{
    return (CheckBusiness)com.idega.business.IBOLookup.getServiceInstance(iwc,CheckBusiness.class);
  }
}
