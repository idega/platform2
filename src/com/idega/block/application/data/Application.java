/*
 * $Id: Application.java,v 1.2 2001/06/21 16:20:46 palli Exp $
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

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class Application extends GenericEntity {
  public static final String name_ = "app_application";
  public static final String applicationSubjectId_ = "app_subject_id";
  public static final String applicationStatusId_ = "app_appl_status";
  public static final String applicantId_ = "app_applicant_id";
  public static final String submitted_ = "submitted";
  public static final String statusChanged_ = "status_changed";

  public Application() {
    super();
  }

  public Application(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(applicationSubjectId_,"Application subject",true,true,"java.lang.Integer","one-to-many","com.idega.block.application.data.ApplicationCategory");
    addAttribute(applicationStatusId_,"Application status",true,true,"java.lang.Integer","one-to-many","com.idega.block.application.data.ApplicationStatus");
    addAttribute(applicantId_,"Applicant",true,true,"java.lang.Integer","one-to-many","com.idega.block.application.data.Applicant");
    addAttribute(submitted_,"Submitted",true,true,"java.sql.Timestamp");
    addAttribute(statusChanged_,"Status changed",true,true,"java.sql.Timestamp");
  }

  public String getEntityName() {
    return(name_);
  }

  public void setApplicationSubjectId(int id) {
    setColumn(applicationSubjectId_,id);
  }

  public void setApplicationSubjectId(Integer id) {
    setColumn(applicationSubjectId_,id);
  }

  public Integer getApplicationCategoryId() {
    return((Integer)getColumnValue(applicationSubjectId_));
  }

  public void setApplicationStatusId(int id) {
    setColumn(applicationStatusId_,id);
  }

  public void setApplicationStatusId(Integer id) {
    setColumn(applicationStatusId_,id);
  }

  public Integer getApplicationStatusId() {
    return((Integer)getColumnValue(applicationStatusId_));
  }

  public void setApplicantId(int id) {
    setColumn(applicantId_,id);
  }

  public void setApplicantId(Integer id) {
    setColumn(applicantId_,id);
  }

  public Integer getApplicantId() {
    return((Integer)getColumnValue(applicantId_));
  }

  public void setSubmitted(Timestamp submitted) {
    setColumn(submitted_,submitted);
  }

  public Timestamp getSubmitted() {
    return((Timestamp)getColumnValue(submitted_));
  }

  public void setStatusChanged(Timestamp statusChanged) {
    setColumn(statusChanged_,statusChanged);
  }

  public Timestamp getStatusChanged() {
    return((Timestamp)getColumnValue(statusChanged_));
  }
}