/*
 * $Id: Application.java,v 1.5 2001/06/27 23:37:23 aron Exp $
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
import java.sql.Timestamp;
import java.lang.IllegalStateException;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class Application extends GenericEntity {
  public static final String name_ = "app_application";
  private static final String subjectId_ = "app_subject_id";
  private static final String applicantId_ = "app_applicant_id";
  private static final String submitted_ = "submitted";
  private static final String status_ = "status";
  private static final String statusChanged_ = "status_changed";

  public static final String statusSubmitted = "S";
  public static final String statusApproved = "A";
  public static final String statusRejected = "R";

  public Application() {
    super();
  }

  public Application(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(subjectId_,"Subject",true,true,"java.lang.Integer","one-to-many","com.idega.block.application.data.ApplicationSubject");
    addAttribute(applicantId_,"Applicant",true,true,"java.lang.Integer","one-to-many","com.idega.block.application.data.Applicant");
    addAttribute(submitted_,"Submitted",true,true,"java.sql.Timestamp");
    addAttribute(status_,"Status",true,true,"java.lang.String");
    addAttribute(statusChanged_,"Status changed",true,true,"java.sql.Timestamp");
    setMaxLength(status_,1);
  }

  public String getEntityName() {
    return(name_);
  }

  public String getSubjectIdColumnName() {
    return(subjectId_);
  }

  public String getStatusColumnName() {
    return(status_);
  }

  public String getApplicantIdColumnName() {
    return(applicantId_);
  }

  public String getSubmittedColumnName() {
    return(submitted_);
  }

  public String getStatusChangedColumnName() {
    return(statusChanged_);
  }

  public void setSubjectId(int id) {
    setColumn(subjectId_,id);
  }

  public void setSubjectId(Integer id) {
    setColumn(subjectId_,id);
  }

  public int getSubjectId() {
    return(getIntColumnValue(subjectId_));
  }

  public void setApplicantId(int id) {
    setColumn(applicantId_,id);
  }

  public void setApplicantId(Integer id) {
    setColumn(applicantId_,id);
  }

  public int getApplicantId() {
    return getIntColumnValue(applicantId_);
  }

  public void setSubmitted(Timestamp submitted) {
    setColumn(submitted_,submitted);
  }

  public Timestamp getSubmitted() {
    return((Timestamp)getColumnValue(submitted_));
  }

  public void setStatus(String status) throws IllegalStateException {
    if ((status.equalsIgnoreCase(statusSubmitted)) ||
        (status.equalsIgnoreCase(statusApproved)) ||
        (status.equalsIgnoreCase(statusRejected)))
      setColumn(status_,status);
    else
      throw new IllegalStateException("Undefined state : " + status);
  }

  public void setStatusSubmitted() {
    setStatus(statusSubmitted);
  }

  public void setStatusApproved() {
    setStatus(statusApproved);
  }

  public void setStatusRejected() {
    setStatus(statusRejected);
  }

  public String getStatus() {
    return(getStringColumnValue(status_));
  }

  public void setStatusChanged(Timestamp statusChanged) {
    setColumn(statusChanged_,statusChanged);
  }

  public Timestamp getStatusChanged() {
    return((Timestamp)getColumnValue(statusChanged_));
  }
}