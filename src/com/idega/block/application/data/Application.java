/*
 * $Id: Application.java,v 1.1 2001/06/15 01:31:03 palli Exp $
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
  private static String name_ = "app_application";
  private static String applicationCategoryId_ = "app_allocation_id";
  private static String applicationStatusId_ = "app_appl_status";
  private static String applicantId_ = "app_applicant_id";
  private static String submitted_ = "submitted";
  private static String statusChanged_ = "status_changed";

  public Application() {
    super();
  }

  public Application(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(applicationCategoryId_,"Tegund umsóknar",true,true,"java.lang.Integer","one-to-many","com.idega.block.application.data.ApplicationCategory");
    addAttribute(applicationStatusId_,"Staða umsóknar",true,true,"java.lang.Integer","one-to-many","com.idega.block.application.data.ApplicationStatus");
    addAttribute(applicantId_,"Umsækjandi",true,true,"java.lang.Integer","one-to-many","com.idega.block.application.data.Applicant");
    addAttribute(submitted_,"Hvenær var umsókn send inn",true,true,"java.sql.Timestamp");
    addAttribute(statusChanged_,"Hvenær var stöðu umsóknar breytt",true,true,"java.sql.Timestamp");
  }

  public String getEntityName() {
    return(name_);
  }

  public void setApplicationCategoryId(int id) {
    setColumn(applicationCategoryId_,id);
  }

  public void setApplicationCategoryId(Integer id) {
    setColumn(applicationCategoryId_,id);
  }

  public Integer getApplicationCategoryId() {
    return((Integer)getColumnValue(applicationCategoryId_));
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