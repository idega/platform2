/*
 * $Id: Contract.java,v 1.6 2001/07/13 00:08:35 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.entity;

import com.idega.data.GenericEntity;
import java.sql.Date;
import java.lang.IllegalStateException;
import java.sql.SQLException;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class Contract extends GenericEntity {
  private static final String name_ = "cam_contract";
  private static final String userId_ = "ic_user_id";
  private static final String apartmentId_ = "bu_apartment_id";
  private static final String validFrom_ = "valid_from";
  private static final String validTo_ = "valid_to";
  private static final String status_ = "status";
  private static final String applicantId_ = "app_applicant_id";

  public static final String statusCreated = "C";
  public static final String statusSigned = "S";
  public static final String statusRejected = "R";
  public static final String statusTerminated = "T";
  public static final String statusEnded = "E";

  public Contract() {
  }
  public Contract(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(userId_,"User id",true,true,"java.lang.Integer","one-to-many","com.idega.core.ICUser");
    addAttribute(apartmentId_,"Apartment id",true,true,"java.lang.Integer","one-to-many","com.idega.block.building.data.Apartment");
    addAttribute(applicantId_,"Applicant id",true,true,"java.lang.Integer","one-to-one","com.idega.block.application.data.Applicant");
    addAttribute(validFrom_,"Valid from",true,true,"java.sql.Date");
    addAttribute(validTo_,"Valid to",true,true,"java.sql.Date");
    addAttribute(status_,"Status",true,true,"java.lang.String");
    setMaxLength(status_,1);
  }

  public String getEntityName() {
    return(name_);
  }

  public String getUserIdColumnName() {
    return(userId_);
  }

  public String getApplicantIdColumnName(){
    return(applicantId_);
  }

  public String getApartmentIdColumnName() {
    return(apartmentId_);
  }

  public String getValidFromColumnName() {
    return(validFrom_);
  }

  public String getValidToColumnName() {
    return(validTo_);
  }

  public String getStatusColumnName() {
    return(status_);
  }

  public void setUserId(int id) {
    setColumn(userId_,id);
  }

  public void setUserId(Integer id) {
    setColumn(userId_,id);
  }

  public Integer getUserId() {
    return(getIntegerColumnValue(userId_));
  }
  public void setApplicantId(int id) {
    setColumn(applicantId_,id);
  }

  public void setApplicantId(Integer id) {
    setColumn(applicantId_,id);
  }

  public Integer getApplicantId() {
    return(getIntegerColumnValue(applicantId_));
  }

  public void setApartmentId(int id) {
    setColumn(apartmentId_,id);
  }

  public void setApartmentId(Integer id) {
    setColumn(apartmentId_,id);
  }

  public Integer getApartmentId() {
    return(getIntegerColumnValue(apartmentId_));
  }

  public void setValidFrom(Date date) {
    setColumn(validFrom_,date);
  }

  public Date getValidFrom() {
    return((Date)getColumnValue(validFrom_));
  }

  public void setValidTo(Date date) {
    setColumn(validTo_,date);
  }

  public Date getValidTo() {
    return((Date)getColumnValue(validTo_));
  }

  public void setStatus(String status) throws IllegalStateException {
    if ((status.equalsIgnoreCase(statusCreated)) ||
        (status.equalsIgnoreCase(statusEnded)) ||
        (status.equalsIgnoreCase(statusRejected)) ||
        (status.equalsIgnoreCase(statusSigned)) ||
        (status.equalsIgnoreCase(statusTerminated)))
      setColumn(status_,status);
    else
      throw new IllegalStateException("Undefined state : " + status);
  }

  public String getStatus() {
    return((String)getColumnValue(status_));
  }

  public void setStatusCreated() {
    setStatus(statusCreated);
  }

  public void setStatusEnded() {
    setStatus(statusEnded);
  }

  public void setStatusRejected() {
    setStatus(statusRejected);
  }

  public void setStatusSigned() {
    setStatus(statusSigned);
  }

  public void setStatusTerminated() {
    setStatus(statusTerminated);
  }
}