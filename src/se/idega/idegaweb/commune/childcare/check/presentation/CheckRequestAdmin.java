package se.idega.idegaweb.commune.childcare.check.presentation;

import com.idega.user.data.User;
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
      User child = getCheckBusiness(iwc).getUserById(iwc,check.getChildId());
      User manager = getCheckBusiness(iwc).getUserById(iwc,check.getManagerId());

      String childSSN = "-";
      if ( child != null )
	childSSN = child.getPersonalID();
      String managerName = "-";
      if ( manager != null )
	managerName = manager.getName();

      Link l = getLink(check.getPrimaryKey().toString());
      l.addParameter(PARAM_VIEW_CHECK,"true");
      l.addParameter(PARAM_CHECK_ID,check.getPrimaryKey().toString());
      checkList.add(l);
      checkList.add(check.getCreated().toString().substring(0,10));
      checkList.add(childSSN);
      checkList.add(managerName);
      checkList.add(check.getStatus());
    }
    add(checkList);
  }

  private void viewCheck(IWContext iwc, Check check,boolean isError)throws Exception{

    add(getCheckInfoTable(iwc,check));
    add(new Break(2));

    if(isError){
      add(getErrorText(localize("check.must_check_all_rules","All rules must be checked.")));
      add(new Break(2));
    }

    add(getCheckForm(iwc,check,isError));
  }

  private Table getCheckInfoTable(IWContext iwc, Check check) throws Exception {
    Table frame = new Table();
    frame.setCellpadding(10);
    frame.setCellspacing(0);
    frame.setColor("#ffffcc");

    Table checkInfoTable = new Table(2,8);
    checkInfoTable.setCellpadding(6);
    checkInfoTable.setCellspacing(0);
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
    frame.add(checkInfoTable);

    return frame;
  }

  private Form getCheckForm(IWContext iwc, Check check, boolean isError) throws Exception {
    Form f = new Form();
    f.addParameter(PARAM_CHECK_ID,check.getPrimaryKey().toString());

    Table frame = new Table(2,1);
    frame.setCellpadding(14);
    frame.setCellspacing(0);
    frame.setColor(getBackgroundColor());
    frame.setVerticalAlignment(2,1,"top");
    frame.add(getLocalizedSmallText("check.requirements","Requirements"),1,1);
    frame.add(new Break(2));

    Table ruleTable = new Table(2,5);
    ruleTable.setCellpadding(4);
    ruleTable.setCellspacing(0);

    ruleTable.add(getCheckBox("1",check.getRule1()),1,1);
    ruleTable.add(getRuleText(localize("check.nationally_registered","Nationally registered"),check.getRule1(),isError),2,1);

    ruleTable.add(getCheckBox("2",check.getRule2()),1,2);
    ruleTable.add(getRuleText(localize("check.child_one_year","Child one year of age"),check.getRule2(),isError),2,2);

    ruleTable.add(getCheckBox("3",check.getRule3()),1,3);
    ruleTable.add(getRuleText(localize("check.work_situation_approved","Work situation approved"),check.getRule3(),isError),2,3);

    ruleTable.add(getCheckBox("4",check.getRule4()),1,4);
    ruleTable.add(getRuleText(localize("check.dept_control","Skuldkontroll"),check.getRule4(),isError),2,4);

    ruleTable.add(getCheckBox("5",check.getRule5()),1,5);
    ruleTable.add(getRuleText(localize("check.need_for_special_support","Need for special support"),check.getRule5(),isError),2,5);

    frame.add(ruleTable,1,1);
    frame.add(new Break(2),1,1);
    frame.add(getSubmitButtonTable(),1,1);

    frame.add(getLocalizedSmallText("check.notes","Notes"),2,1);
    frame.add(new Break(2),2,1);
    TextArea notes = new TextArea(PARAM_NOTES);
    notes.setValue(check.getNotes());
    notes.setHeight(8);
    notes.setWidth(50);
    frame.add(notes,2,1);
    f.add(frame);

    return f;
  }

  private CheckBox getCheckBox(String ruleNumber,boolean checked) {
    CheckBox rule = new CheckBox(PARAM_RULE,ruleNumber);
    rule.setChecked(checked);
    return rule;
  }

  private Text getRuleText(String ruleText,boolean ruleChecked,boolean isError) {
    if ( ruleChecked || !isError ) {
      return getText(ruleText);
    }
    else{
      return getErrorText(ruleText);
    }
  }

  private Table getSubmitButtonTable() {
    Table submitTable = new Table(5,1);
    submitTable.setWidth(2,"3");
    submitTable.setWidth(4,"3");
    submitTable.setCellpaddingAndCellspacing(0);

    Image image = getResourceBundle().getLocalizedImageButton("check.grant_check","Grant check");
    SubmitButton grantButton = new SubmitButton(image,PARAM_GRANT_CHECK);
    submitTable.add(grantButton,1,1);

    image = getResourceBundle().getLocalizedImageButton("check.retrial","Retrial");
    SubmitButton retrialButton = new SubmitButton(image,PARAM_RETRIAL_CHECK);
    submitTable.add(retrialButton,3,1);

    image = getResourceBundle().getLocalizedImageButton("check.save","Save");
    SubmitButton saveButton = new SubmitButton(image,PARAM_SAVE_CHECK);
    submitTable.add(saveButton,5,1);

    return submitTable;
  }

  private CheckBusiness verifyCheckRules(IWContext iwc)throws Exception{
    int checkId = Integer.parseInt(iwc.getParameter(PARAM_CHECK_ID));
    String[] selectedRules = iwc.getParameterValues(PARAM_RULE);
    String notes = iwc.getParameter(PARAM_NOTES);
//    int managerId = iwc.getUser().getID();
    int managerId = iwc.getUserId();
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
    int managerId = iwc.getUserId();
    //cb.sendMessageToCitizen(subject,body,managerId);

    add(getText("Check granted:"));
    viewCheckList(iwc);
  }

  private void retrialCheck(IWContext iwc)throws Exception{
    CheckBusiness cb = verifyCheckRules(iwc);
    cb.retrialCheck();
    cb.commit();

    //Create message to user
    String subject = "...";
    String body = "...";
    int managerId = iwc.getUserId();
    //cb.sendMessageToCitizen(subject,body,managerId);

    viewCheckList(iwc);
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