/*
 * $Id: Applicant.java,v 1.2 2001/06/21 16:20:46 palli Exp $
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
  public static final String name_ = "app_applicant";
  public static final String firstName_ = "first_name";
  public static final String middleName_ = "middle_name";
  public static final String lastName_ = "lastName";
  public static final String ssn_ = "ssn";
  public static final String legalResidence_ = "legal_residence";
  public static final String residence_ = "residence";
  public static final String residencePhone_ = "residence_phone";
  public static final String po_ = "po";

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

  public void setResidencePhone(int phone) {
    setColumn(residencePhone_,phone);
  }

  public void setResidencePhone(Integer phone) {
    setColumn(residencePhone_,phone);
  }

  public Integer getResidencePhone() {
    return((Integer)getColumnValue(residencePhone_));
  }

  public void setPO(String po) {
    setColumn(po_,po);
  }

  public String getPO() {
    return((String)getColumnValue(po_));
  }
}