/*
 * $Id: CitizenAccountBMPBean.java,v 1.1 2002/06/28 15:16:30 palli Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.account.citizen.data;

import com.idega.user.data.User;
import com.idega.block.process.data.AbstractCaseBMPBean;
import com.idega.block.process.data.Case;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class CitizenAccountBMPBean extends AbstractCaseBMPBean implements CitizenAccount, Case {
  private final static String ENTITY_NAME = "comm_cit_acc";
  private final static String CASE_CODE_KEY = "MBANSKO";
  private final static String CASE_CODE_KEY_DESC = "Request for citizen account";

  private final static String EMAIL = "email";
  private final static String PHONE_HOME = "phone_home";
  private final static String PHONE_WORK = "phone_work";
  private final static String PID = "pid";

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(EMAIL,"E-mail",true,true,java.lang.String.class,40);
    addAttribute(PHONE_HOME,"Home phone",true,true,java.lang.String.class,20);
    addAttribute(PHONE_WORK,"Work phone",true,true,java.lang.String.class,20);
    addAttribute(PID,"PID",true,true,java.lang.String.class,40);
  }

  public String getEntityName() {
    return ENTITY_NAME;
  }

  public String getCaseCodeKey(){
    return CASE_CODE_KEY;
  }

  public String getCaseCodeDescription(){
    return CASE_CODE_KEY_DESC;
  }

  public void setEmail(String email) {
    setColumn(EMAIL,email);
  }

  public void setPhoneHome(String phone) {
    setColumn(PHONE_HOME,phone);
  }

  public void setPhoneWork(String phone) {
    setColumn(PHONE_WORK,phone);
  }

  public void setPID(String pid) {
    setColumn(PID,pid);
  }

  public String getEmail() {
    return getStringColumnValue(EMAIL);
  }

  public String getPhoneHome() {
    return getStringColumnValue(PHONE_HOME);
  }

  public String getPhoneWork() {
    return getStringColumnValue(PHONE_WORK);
  }

 public String getPID() {
    return getStringColumnValue(PID);
  }
}