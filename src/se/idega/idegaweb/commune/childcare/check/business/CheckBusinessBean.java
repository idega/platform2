package se.idega.idegaweb.commune.childcare.check.business;

import com.idega.core.data.Address;
import is.idega.idegaweb.member.business.MemberFamilyLogic;
import com.idega.user.business.UserBusiness;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.block.process.business.CaseBusinessBean;
import com.idega.util.idegaTimestamp;
import java.util.*;

import com.idega.business.IBOSessionBean;

import se.idega.idegaweb.commune.message.data.*;
import se.idega.idegaweb.commune.childcare.check.data.*;



/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Anders Lindman
 * @version 1.0
 */

public class CheckBusinessBean extends CaseBusinessBean implements CheckBusiness{

  private Check currentCheck = null;
  private boolean rule1Verified = false;
  private boolean rule2Verified = false;
  private boolean rule3Verified = false;
  private boolean rule4Verified = false;
  private boolean rule5Verified = false;
  private boolean allRulesVerified = false;

  public CheckBusinessBean() {
  }

  private CheckHome getCheckHome()throws java.rmi.RemoteException{
    return (CheckHome) com.idega.data.IDOLookup.getHome(Check.class);
  }

  public Check getCheck(int checkId)throws Exception{
    return getCheckHome().findByPrimaryKey(new Integer(checkId));
  }

  public Check getCurrentCheck()throws Exception{
    return this.currentCheck;
  }

  public Collection findChecks()throws Exception{
    return getCheckHome().findChecks();
  }

  public boolean isRule1Verified(){
    return this.rule1Verified;
  }

  public boolean isRule2Verified(){
    return this.rule2Verified;
  }

  public boolean isRule3Verified(){
    return this.rule3Verified;
  }

  public boolean isRule4Verified(){
    return this.rule4Verified;
  }

  public boolean isRule5Verified(){
    return this.rule5Verified;
  }

  public boolean allRulesVerified(){
    return this.allRulesVerified;
  }

  public void createCheck(int childCareType,int workSituation1,int workSituation2,String motherTongueMotherChild,String motherTongueFatherChild,String motherTongueParents,int childId,int method,int amount,int checkFee,int managerId,String notes,boolean checkRule1,boolean checkRule2,boolean checkRule3,boolean checkRule4,boolean checkRule5)throws Exception{
    CheckHome home = (CheckHome)com.idega.data.IDOLookup.getHome(Check.class);
    Check check = home.create();
    check.setChildCareType(childCareType);
    check.setWorkSituation1(workSituation1);
    check.setWorkSituation2(workSituation2);
    check.setMotherTongueMotherChild(motherTongueMotherChild);
    check.setMotherTongueFatherChild(motherTongueFatherChild);
    check.setMotherTongueParents(motherTongueParents);
    check.setChildId(childId);
    check.setMethod(method);
    check.setAmount(amount);
    check.setCheckFee(checkFee);
    check.setManagerId(managerId);
    check.setNotes(notes);
    check.setRule1(checkRule1);
    check.setRule2(checkRule2);
    check.setRule3(checkRule3);
    check.setRule4(checkRule4);
    check.setRule5(checkRule5);
    check.setCaseStatus(this.getCaseStatusOpen());

    check.store();
  }

  public void sendMessageToCitizen(
      String subject,
      String body,
      int managerId)throws Exception{
    UserMessageHome home = (UserMessageHome)com.idega.data.IDOLookup.getHome(UserMessage.class);
    UserMessage msg = (UserMessage)home.create();
    msg.setSubject(subject);
    msg.setBody(body);
//    msg.setSenderId(managerId);
    msg.store();
  }

  public void verifyCheckRules(int checkId,String[] selectedRules,String notes,int managerId)throws Exception{
    this.currentCheck = getCheck(checkId);
    this.currentCheck.setManagerId(managerId);
    this.rule1Verified = false;
    this.rule2Verified = false;
    this.rule3Verified = false;
    this.rule4Verified = false;
    this.rule5Verified = false;
    if(selectedRules==null){
      this.allRulesVerified = false;
    }
    else{
      for(int i=0; i<selectedRules.length; i++){
	int rule = Integer.parseInt(selectedRules[i]);
	switch (rule) {
	  case 1:
	    this.rule1Verified = true;
	    break;
	  case 2:
	    this.rule2Verified = true;
	    break;
	  case 3:
	    this.rule3Verified = true;
	    break;
	  case 4:
	    this.rule4Verified = true;
	    break;
	  case 5:
	    this.rule5Verified = true;
	    break;
	}
      }
      // Rule 5 overrides all other rules
      this.allRulesVerified = ((selectedRules.length==4)&&!rule5Verified)||rule5Verified;
    }
    this.currentCheck.setNotes(notes);
    this.currentCheck.setRule1(this.rule1Verified);
    this.currentCheck.setRule2(this.rule2Verified);
    this.currentCheck.setRule3(this.rule3Verified);
    this.currentCheck.setRule4(this.rule4Verified);
    this.currentCheck.setRule5(this.rule5Verified);
  }

  public User getUserById(IWContext iwc,int userId) throws Exception {
    return getUserBusiness(iwc).getUser(userId);
  }

  public User getUserByPersonalId(IWContext iwc,String personalID) throws Exception {
    return getUserBusiness(iwc).getUserHome().findByPersonalID(personalID);
  }

  public Address getUserAddress(IWContext iwc,User user) {
    try {
      return getUserBusiness(iwc).getUserAddress1(((Integer)user.getPrimaryKey()).intValue());
    }
    catch (Exception e) {
      return null;
    }
  }

  public String getUserPostalCode(IWContext iwc,User user) {
    try {
      return getUserBusiness(iwc).getUserAddress1(((Integer)user.getPrimaryKey()).intValue()).getPostalCode().getPostalCode();
    }
    catch (Exception e) {
      return "";
    }
  }

  public void commit()throws Exception{
    this.currentCheck.store();
  }

  public void approveCheck()throws Exception{
    System.out.println("Status: "+this.getCaseStatusGranted().toString());
    this.currentCheck.setCaseStatus(this.getCaseStatusGranted());
  }

  public void retrialCheck()throws Exception{
    this.currentCheck.setCaseStatus(this.getCaseStatusReview());
  }

  public void saveCheck()throws Exception{
    this.currentCheck.setCaseStatus(this.getCaseStatusOpen());
  }

  private UserBusiness getUserBusiness(IWContext iwc)throws Exception{
    return (UserBusiness)com.idega.business.IBOLookup.getServiceInstance(iwc,UserBusiness.class);
  }
}