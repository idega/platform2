/*
 * $Id: Applicant.java,v 1.5 2001/06/28 10:33:16 aron Exp $
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

  public Applicant() {
    super();
  }

  public Applicant(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(firstName_,"First name",true,true,"java.lang.String");
    addAttribute(middleName_,"Middle name",true,true,"java.lang.String");
    addAttribute(lastName_,"Last name",true,true,"java.lang.String");
    addAttribute(ssn_,"Social security number",true,true,"java.lang.String");
    addAttribute(legalResidence_,"Legal residence",true,true,"java.lang.String");
    addAttribute(residence_,"Residence",true,true,"java.lang.String");
    addAttribute(residencePhone_,"Telephone",true,true,"java.lang.String");
    addAttribute(po_,"Post office",true,true,"java.lang.String");
    setMaxLength(firstName_,255);
    setMaxLength(middleName_,255);
    setMaxLength(lastName_,255);
    setMaxLength(ssn_,20);
    setMaxLength(legalResidence_,255);
    setMaxLength(residence_,255);
    setMaxLength(residencePhone_,40);
    setMaxLength(po_,255);
  }

  public String getEntityName() {
    return(name_);
  }

  public String getFirstNameColumnName() {
    return(firstName_);
  }

  public String getMiddleNameColumnName() {
    return(middleName_);
  }

  public String getLastNameColumnName() {
    return(lastName_);
  }

  public String getSSNColumnName() {
    return(ssn_);
  }

  public String getLegalResidenceColumnName() {
    return(legalResidence_);
  }

  public String getResidenceColumnName() {
    return(residence_);
  }

  public String getResidencePhoneColumnName() {
    return(residencePhone_);
  }

  public String getPOColumnName() {
    return(po_);
  }

  public void setFirstName(String name) {
    setColumn(firstName_,name);
  }

  public String getFirstName() {
    return((String)getColumnValue(firstName_));
  }

  public void setMiddleName(String name) {
    setColumn(middleName_,name);
  }

  public String getMiddleName() {
    return((String)getColumnValue(middleName_));
  }

  public void setLastName(String name) {
    setColumn(lastName_,name);
  }

  public String getLastName() {
    return((String)getColumnValue(lastName_));
  }

  public void setSSN(String ssn) {
    setColumn(ssn_,ssn);
  }

  public String getSSN() {
    return((String)getColumnValue(ssn_));
  }

  public void setLegalResidence(String legal) {
    setColumn(legalResidence_,legal);
  }

  public String getLegalResidence() {
    return((String)getColumnValue(legalResidence_));
  }

  public void setResidence(String residence) {
    setColumn(residence_,residence);
  }

  public String getResidence() {
    return((String)getColumnValue(residence_));
  }

  public void setResidencePhone(String phone) {
    setColumn(residencePhone_,phone);
  }

  public String getResidencePhone() {
    return((String)getColumnValue(residencePhone_));
  }

  public void setPO(String po) {
    setColumn(po_,po);
  }

  public String getPO() {
    return((String)getColumnValue(po_));
  }
  public String getFullName(){
    return (getFirstName()!= null?getFirstName():"")+
    " "+(getMiddleName()!= null?getMiddleName():"")+
    " "+(getLastName()!= null?getLastName():"");

  }
}