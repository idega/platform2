/*
 * $Id: Applicant.java,v 1.9 2001/08/01 11:17:37 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.application.data;

import com.idega.data.GenericEntity;
import java.sql.SQLException;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class Applicant extends GenericEntity {
  private static final String name_ = "app_applicant";
  private static final String firstName_ = "first_name";
  private static final String middleName_ = "middle_name";
  private static final String lastName_ = "last_name";
  private static final String ssn_ = "ssn";
  private static final String legalResidence_ = "legal_residence";
  private static final String residence_ = "residence";
  private static final String residencePhone_ = "residence_phone";
  private static final String po_ = "po";
  private static final String mobilePhone_ = "mobile_phone";
  private static final String sendSMS_ = "send_sms";

  public Applicant() {
    super();
  }

  public Applicant(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(firstName_,"First name",true,true,"java.lang.String",255);
    addAttribute(middleName_,"Middle name",true,true,"java.lang.String",255);
    addAttribute(lastName_,"Last name",true,true,"java.lang.String",255);
    addAttribute(ssn_,"Social security number",true,true,"java.lang.String",20);
    addAttribute(legalResidence_,"Legal residence",true,true,"java.lang.String",255);
    addAttribute(residence_,"Residence",true,true,"java.lang.String",255);
    addAttribute(residencePhone_,"Telephone",true,true,"java.lang.String",40);
    addAttribute(po_,"Post office",true,true,"java.lang.String",255);
    addAttribute(mobilePhone_,"Mobile phone",true,true,"java.lang.String",40);
    addAttribute(sendSMS_,"Send SMS",true,true,"java.lang.String",1);
  }

  public String getEntityName() {    return(name_);  }
  public static String getEntityTableName() {    return(name_);  }
  public static String getFirstNameColumnName() {    return(firstName_);  }
  public static String getMiddleNameColumnName() {    return(middleName_);  }
  public static String getLastNameColumnName() {    return(lastName_);  }
  public static String getSSNColumnName() {    return(ssn_);  }
  public static String getLegalResidenceColumnName() {    return(legalResidence_);  }
  public static String getResidenceColumnName() {    return(residence_);  }
  public static String getResidencePhoneColumnName() {    return(residencePhone_);  }
  public static String getPOColumnName() {    return(po_);  }
  public static String getMobilePhoneColumnName() {    return(mobilePhone_);  }
  public static String getSendSMSColumnName() {    return(sendSMS_);  }

  public void setFirstName(String name) {
    setColumn(firstName_,name);
  }

  public String getFirstName() {
    return(getStringColumnValue(firstName_));
  }

  public void setMiddleName(String name) {
    setColumn(middleName_,name);
  }

  public String getMiddleName() {
    return(getStringColumnValue(middleName_));
  }

  public void setLastName(String name) {
    setColumn(lastName_,name);
  }

  public String getLastName() {
    return(getStringColumnValue(lastName_));
  }

  public void setSSN(String ssn) {
    setColumn(ssn_,ssn);
  }

  public String getSSN() {
    return(getStringColumnValue(ssn_));
  }

  public void setLegalResidence(String legal) {
    setColumn(legalResidence_,legal);
  }

  public String getLegalResidence() {
    return(getStringColumnValue(legalResidence_));
  }

  public void setResidence(String residence) {
    setColumn(residence_,residence);
  }

  public String getResidence() {
    return(getStringColumnValue(residence_));
  }

  public void setResidencePhone(String phone) {
    setColumn(residencePhone_,phone);
  }

  public String getResidencePhone() {
    return(getStringColumnValue(residencePhone_));
  }

  public void setPO(String po) {
    setColumn(po_,po);
  }

  public String getPO() {
    return(getStringColumnValue(po_));
  }

  public void setMobilePhone(String mobilePhone) {
    setColumn(mobilePhone_,mobilePhone);
  }

  public String getMobilePhone() {
    return(getStringColumnValue(mobilePhone_));
  }

  public boolean getSendSMS() {
    String send = getStringColumnValue(sendSMS_);
    if ((send == null) || (send.equalsIgnoreCase("n")))
      return(false);
    else if (send.equalsIgnoreCase("y"))
      return(true);
    else
      return(false);
  }

  public void setSendSMS(boolean send) {
    if (send)
      setColumn(sendSMS_,"Y");
    else
      setColumn(sendSMS_,"N");
  }

  public String getFullName(){
    return (getFirstName()!= null?getFirstName():"")+
    " "+(getMiddleName()!= null?getMiddleName():"")+
    " "+(getLastName()!= null?getLastName():"");

  }
}