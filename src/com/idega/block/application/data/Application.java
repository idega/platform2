package com.idega.block.application.data;


public interface Application extends com.idega.data.IDOLegacyEntity
{
 public int getApplicantId();
 public java.lang.String getStatus();
 public java.sql.Timestamp getStatusChanged();
 public int getSubjectId();
 public java.sql.Timestamp getSubmitted();
 public void setApplicantId(int p0);
 public void setApplicantId(java.lang.Integer p0);
 public void setStatus(java.lang.String p0)throws java.lang.IllegalStateException;
 public void setStatusApproved();
 public void setStatusChanged(java.sql.Timestamp p0);
 public void setStatusRejected();
 public void setStatusSigned();
 public void setStatusSubmitted();
 public void setSubjectId(java.lang.Integer p0);
 public void setSubjectId(int p0);
 public void setSubmitted(java.sql.Timestamp p0);
}
