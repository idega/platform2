/*
 * $Id: ApplicantBMPBean.java,v 1.4 2004/06/05 06:16:42 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.application.data;

import java.sql.SQLException;
import java.util.Collection;
import java.util.StringTokenizer;

import javax.ejb.FinderException;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ApplicantBMPBean extends com.idega.data.TreeableEntityBMPBean implements com.idega.block.application.data.Applicant {
  private static final String ENTITY_NAME = "app_applicant";
  private static final String FIRST_NAME = "first_name";
  private static final String MIDDLE_NAME = "middle_name";
  private static final String LAST_NAME = "last_name";
  private static final String FULL_NAME = "full_name";
  private static final String SSN = "ssn";
  private static final String LEGAL_RESIDENCE = "legal_residence";
  private static final String RESIDENCE = "residence";
  private static final String PHONE = "residence_phone";
  private static final String PO = "po";
  private static final String MOBILE_PHONE = "mobile_phone";
  private static final String SEND_SMS = "send_sms";
  private static final String STATUS = "status";

  /**
   *
   */
  public ApplicantBMPBean() {
    super();
  }

  /**
   *
   */
  public ApplicantBMPBean(int id) throws SQLException {
    super(id);
  }

  /**
   *
   */
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getFirstNameColumnName(),"First name",true,true,"java.lang.String",255);
    addAttribute(getMiddleNameColumnName(),"Middle name",true,true,"java.lang.String",255);
    addAttribute(getLastNameColumnName(),"Last name",true,true,"java.lang.String",255);
    addAttribute(getFullNameColumnName(),"Full name",true,true,"java.lang.String",255);
    addAttribute(getSSNColumnName(),"Social security number",true,true,"java.lang.String",20);
    addAttribute(getLegalResidenceColumnName(),"Legal residence",true,true,"java.lang.String",255);
    addAttribute(getResidenceColumnName(),"Residence",true,true,"java.lang.String",255);
    addAttribute(getResidencePhoneColumnName(),"Telephone",true,true,"java.lang.String",40);
    addAttribute(getPOColumnName(),"Post office",true,true,"java.lang.String",255);
    addAttribute(getMobilePhoneColumnName(),"Mobile phone",true,true,"java.lang.String",40);
    addAttribute(getSendSMSColumnName(),"Send SMS",true,true,"java.lang.String",1);
    addAttribute(getStatusColumnName(),"Status",true,true,"java.lang.String",1);
  }

  /**
   *
   */
  public String getEntityName() {
    return(ENTITY_NAME);
  }

  /**
   *
   */
  public static String getEntityTableName() {
    return(ENTITY_NAME);
  }

  /**
   *
   */
  public static String getFirstNameColumnName() {
    return(FIRST_NAME);
  }

  /**
   *
   */
  public static String getMiddleNameColumnName() {
    return(MIDDLE_NAME);
  }

  /**
   *
   */
  public static String getLastNameColumnName() {
    return(LAST_NAME);
  }

  /**
   *
   */
  public static String getFullNameColumnName() {
    return(FULL_NAME);
  }

  /**
   *
   */
  public static String getSSNColumnName() {
    return(SSN);
  }

  /**
   *
   */
  public static String getLegalResidenceColumnName() {
    return(LEGAL_RESIDENCE);
  }

  /**
   *
   */
  public static String getResidenceColumnName() {
    return(RESIDENCE);
  }

  /**
   *
   */
  public static String getResidencePhoneColumnName() {
    return(PHONE);
  }

  /**
   *
   */
  public static String getPOColumnName() {
    return(PO);
  }

  /**
   *
   */
  public static String getMobilePhoneColumnName() {
    return(MOBILE_PHONE);
  }

  /**
   *
   */
  public static String getSendSMSColumnName() {
    return(SEND_SMS);
  }

   /**
   *
   */
  public static String getStatusColumnName() {
    return(STATUS);
  }

  /**
   *
   */
  public static String getFullnameOrderValue() {
    StringBuffer ret = new StringBuffer(getFirstNameColumnName());
    ret.append(",");
    ret.append(getMiddleNameColumnName());
    ret.append(",");
    ret.append(getLastNameColumnName());
    return(ret.toString());
  }

  public void setFirstName(String name) {
    setColumn(getFirstNameColumnName(),name);
  }

  public String getFirstName() {
    return(getStringColumnValue(getFirstNameColumnName()));
  }

  public void setMiddleName(String name) {
    setColumn(getMiddleNameColumnName(),name);
  }

  public String getMiddleName() {
    return(getStringColumnValue(getMiddleNameColumnName()));
  }

  public void setLastName(String name) {
    setColumn(getLastNameColumnName(),name);
  }

  public String getLastName() {
    return(getStringColumnValue(getLastNameColumnName()));
  }

  private void setFullName(){
    setColumn(getFullNameColumnName(),getFullName());
  }

  public void setSSN(String ssn) {
    setColumn(getSSNColumnName(),ssn);
  }

  public String getSSN() {
    return(getStringColumnValue(getSSNColumnName()));
  }

  public void setLegalResidence(String legal) {
    setColumn(getLegalResidenceColumnName(),legal);
  }

  public String getLegalResidence() {
    return(getStringColumnValue(getLegalResidenceColumnName()));
  }

  public void setResidence(String residence) {
    setColumn(getResidenceColumnName(),residence);
  }

  public String getResidence() {
    return(getStringColumnValue(getResidenceColumnName()));
  }

  public void setResidencePhone(String phone) {
    setColumn(getResidencePhoneColumnName(),phone);
  }

  public String getResidencePhone() {
    return(getStringColumnValue(getResidencePhoneColumnName()));
  }

  public void setPO(String po) {
    setColumn(getPOColumnName(),po);
  }

  public String getPO() {
    return(getStringColumnValue(getPOColumnName()));
  }

  public void setMobilePhone(String mobilePhone) {
    setColumn(getMobilePhoneColumnName(),mobilePhone);
  }

  public String getMobilePhone() {
    return(getStringColumnValue(getMobilePhoneColumnName()));
  }

  public boolean getSendSMS() {
    String send = getStringColumnValue(getSendSMSColumnName());
    if ((send == null) || (send.equalsIgnoreCase("n")))
      return(false);
    else if (send.equalsIgnoreCase("y"))
      return(true);
    else
      return(false);
  }

  public void setSendSMS(boolean send) {
    if (send)
      setColumn(getSendSMSColumnName(),"Y");
    else
      setColumn(getSendSMSColumnName(),"N");
  }

  public void setStatus(String status){
    setColumn(getStatusColumnName(),status);
  }

  public String getStatus(){
    return getStringColumnValue(getStatusColumnName());
  }

  public String getName(){
    return getFullName();
  }

  public String getFullName() {
    return (getFirstName()!= null?getFirstName():"")+
    " "+(getMiddleName()!= null?getMiddleName():"")+
    " "+(getLastName()!= null?getLastName():"");

  }

  public void setFullName(String fullName){
    StringTokenizer tok = new StringTokenizer(fullName);
    String temp = "";
    if(tok.hasMoreTokens()){
      temp = tok.nextToken();
      setFirstName(temp);
      temp = "";
      if(tok.hasMoreTokens()){
        temp = tok.nextToken();
        setMiddleName(temp);
      }
      if(tok.hasMoreTokens()){
        temp = tok.nextToken();
        setLastName(temp);
      }
      else{
        setMiddleName("");
        setLastName(temp);
      }
    }
  }

  public void insert() throws SQLException{
    setFullName();
    super.insert();
  }

  public void update() throws SQLException{
    setFullName();
    super.update();
  }

  public java.util.Collection ejbFindBySSN(String SSN) throws javax.ejb.FinderException {
    StringBuffer sql = new StringBuffer("select * from ");
    sql.append(getTableName());
    sql.append(" where ");
    sql.append(this.getSSNColumnName());
    sql.append(" = ");
    sql.append(SSN);

    return super.idoFindIDsBySQL(sql.toString());
  }
  
  public Collection ejbFindByApplicationStatusOrderedBy(String status,String order) throws FinderException{
		String applicantTable = ENTITY_NAME;
		String applicationTable = ApplicationBMPBean.ENTITY_NAME;
		 StringBuffer sql = new StringBuffer("select distinct ");
		 sql.append(applicantTable);
		 sql.append(".* from ");
		 sql.append(applicantTable);
		 sql.append(",");
		 sql.append(applicationTable);
		 sql.append(" where ");
		 sql.append(applicantTable);
		 sql.append(".");
		 sql.append(getIDColumnName());
		 sql.append(" = ");
		 sql.append(applicationTable);
		 sql.append(".");
		 sql.append(ApplicationBMPBean.getApplicantIdColumnName());
		 sql.append(" and ");
		 if(status != null){
		   sql.append(applicationTable);
		   sql.append(".");
		   sql.append(ApplicationBMPBean.getStatusColumnName());
		   sql.append(" = '");
		   sql.append(status);
		   sql.append("' ");
		 }
		 if(order != null && order.length() > 0){
		   sql.append(" order by ");
		   sql.append( order);
		 }
		 return super.idoFindPKsBySQL(sql.toString());
  }
  
  public Collection ejbFindBySQL(String sql)throws FinderException{
  	return super.idoFindPKsBySQL(sql);
  }	
}