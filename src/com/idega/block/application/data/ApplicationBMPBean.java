/*
 * $Id: ApplicationBMPBean.java,v 1.5 2004/06/05 06:16:42 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.application.data;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.util.IWTimestamp;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ApplicationBMPBean extends com.idega.data.GenericEntity implements com.idega.block.application.data.Application {
  public static final String ENTITY_NAME = "app_application";
  private static final String SUBJECT_ID = "app_subject_id";
  private static final String APPLICANT_ID = "app_applicant_id";
  private static final String SUBMITTED = "submitted";
  private static final String STATUS = "status";
  private static final String STATUS_CHANGED = "status_changed";


  public static final String STATUS_SUBMITTED = Status.SUBMITTED.toString();
  public static final String STATUS_APPROVED = Status.APPROVED.toString();
  public static final String STATUS_REJECTED = Status.REJECTED.toString();
  public static final String STATUS_SIGNED = Status.SIGNED.toString();
  public static final String STATUS_GARBAGE = Status.GARBAGE.toString();

  public ApplicationBMPBean() {
    super();
  }

  public ApplicationBMPBean(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getSubjectIdColumnName(),"Subject",true,true,java.lang.Integer.class,"one-to-many",com.idega.block.application.data.ApplicationSubject.class);
    addAttribute(getApplicantIdColumnName(),"Applicant",true,true,java.lang.Integer.class,"one-to-many",com.idega.block.application.data.Applicant.class);
    addAttribute(getSubmittedColumnName(),"Submitted",true,true,java.sql.Timestamp.class);
    addAttribute(getStatusColumnName(),"Status",true,true,java.lang.String.class,1);
    addAttribute(getStatusChangedColumnName(),"Status changed",true,true,java.sql.Timestamp.class);
  }

/*  public static String getEntityTableName(){return name_ ;}
  public static String getColumnNameSubjectId(){return subjectId_;}
  public static String getColumnNameApplicantId(){return applicantId_;}
  public static String getColumnNameSubmitted(){return submitted_;}
  public static String getColumnNameStatus(){return status_;}
  public static String getColumnNameStatusChanged(){return statusChanged_;}*/

  public String getEntityName() {
    return(ENTITY_NAME);
  }

  public static String getEntityTableName() {
    return(ENTITY_NAME);
  }

  public static String getSubjectIdColumnName() {
    return(SUBJECT_ID);
  }

  public static String getStatusColumnName() {
    return(STATUS);
  }

  public static String getApplicantIdColumnName() {
    return(APPLICANT_ID);
  }

  public static String getSubmittedColumnName() {
    return(SUBMITTED);
  }

  public static String getStatusChangedColumnName() {
    return(STATUS_CHANGED);
  }

  public void setSubjectId(int id) {
    setColumn(getSubjectIdColumnName(),id);
  }

  public void setSubjectId(Integer id) {
    setColumn(getSubjectIdColumnName(),id);
  }

  public int getSubjectId() {
    return(getIntColumnValue(getSubjectIdColumnName()));
  }

  public void setApplicantId(int id) {
    setColumn(getApplicantIdColumnName(),id);
  }

  public void setApplicantId(Integer id) {
    setColumn(getApplicantIdColumnName(),id);
  }

  public int getApplicantId() {
    return getIntColumnValue(getApplicantIdColumnName());
  }
  
  public Applicant getApplicant(){
  	return (Applicant)getColumnValue(getApplicantIdColumnName());
  }

  public void setSubmitted(Timestamp submitted) {
    setColumn(getSubmittedColumnName(),submitted);
  }

  public Timestamp getSubmitted() {
    return((Timestamp)getColumnValue(getSubmittedColumnName()));
  }

  public void setStatus(String status) throws IllegalStateException {
    if ((status.equalsIgnoreCase(STATUS_SUBMITTED)) ||
        (status.equalsIgnoreCase(STATUS_APPROVED)) ||
        (status.equalsIgnoreCase(STATUS_SIGNED)) ||
        (status.equalsIgnoreCase(STATUS_REJECTED))||
        (status.equalsIgnoreCase(STATUS_GARBAGE))){
      setColumn(getStatusColumnName(),status);
      setStatusChanged(IWTimestamp.getTimestampRightNow());
    }
    else
      throw new IllegalStateException("Undefined state : " + status);
  }

  public void setStatusSubmitted() {
    setStatus(STATUS_SUBMITTED);
  }

  public void setStatusApproved() {
    setStatus(STATUS_APPROVED);
  }

  public void setStatusRejected() {
    setStatus(STATUS_REJECTED);
  }

  public void setStatusSigned() {
    setStatus(STATUS_SIGNED);
  }

  public String getStatus() {
    return(getStringColumnValue(getStatusColumnName()));
  }

  public void setStatusChanged(Timestamp statusChanged) {
    setColumn(getStatusChangedColumnName(),statusChanged);
  }

  public Timestamp getStatusChanged() {
    return((Timestamp)getColumnValue(getStatusChangedColumnName()));
  }
  
  public Collection ejbFindByApplicantID(Integer ID)throws FinderException{
  	return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getApplicantIdColumnName(),ID.intValue()));
  }
  
  public Collection ejbFindByApplicantAndStatus(Integer ID,String status)throws FinderException{
	  return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getApplicantIdColumnName(),ID.intValue()).appendAndEqualsQuoted(getStatusColumnName(),status).appendOrderByDescending(getStatusChangedColumnName()));
	}
  
  public Collection ejbFindBySubjectAndStatus(Integer subjectID,String status)throws FinderException{
  	return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getSubjectIdColumnName(),subjectID.intValue()).appendAndEqualsQuoted(getStatusColumnName(),status));
  }
  
  public Collection ejbFindByStatus(String status)throws FinderException{
	 return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEqualsQuoted(getStatusColumnName(),status).appendOrderByDescending(getStatusChangedColumnName()));
   }
  
  public Collection ejbFindBySubject(Integer subjectID)throws FinderException{
	 return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getSubjectIdColumnName(),subjectID.intValue()));
   }
   
   public Collection ejbFindBySQL(String sql)throws FinderException{
	  return super.idoFindPKsBySQL(sql);
	}	
   
}
