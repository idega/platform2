/*
 * $Id: Applicant.java,v 1.1 2001/06/15 01:31:03 palli Exp $
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
  private static String name_ = "app_applicant";
  private static String firstName_ = "first_name";
  private static String middleName_ = "middle_name";
  private static String lastName_ = "lastName";
  private static String ssn_ = "ssn";
  private static String legalResidence_ = "legal_residence";
  private static String residence_ = "residence";
  private static String residencePhone_ = "residence_phone";
  private static String po_ = "po";

  public Applicant() {
    super();
  }

  public Applicant(int id) throws SQLException {
    super(id);
  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(firstName_,"Fornafn",true,true,"java.lang.String");
    addAttribute(middleName_,"Millinafn",true,true,"java.lang.String");
    addAttribute(lastName_,"Eftirnafn",true,true,"java.lang.String");
    addAttribute(ssn_,"Kennitala",true,true,"java.lang.String");
    addAttribute(legalResidence_,"Lögheimili",true,true,"java.lang.String");
    addAttribute(residence_,"Aðsetur",true,true,"java.lang.String");
    addAttribute(residencePhone_,"Sími",true,true,"java.lang.Integer");
    addAttribute(po_,"Póstnúmer",true,true,"java.lang.String");
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