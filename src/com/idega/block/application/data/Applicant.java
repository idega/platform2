package com.idega.block.application.data;


public interface Applicant extends com.idega.data.IDOLegacyEntity, com.idega.data.TreeableEntity
{
 public java.lang.String getLegalResidence();
 public java.lang.String getStatus();
 public boolean getSendSMS();
 public void setMobilePhone(java.lang.String p0);
 public void setFirstName(java.lang.String p0);
 public void setStatus(java.lang.String p0);
 public java.lang.String getMiddleName();
 public java.lang.String getLastName();
 public java.lang.String getResidencePhone();
 public void setResidence(java.lang.String p0);
 public java.lang.String getMobilePhone();
 public java.lang.String getFullName();
 public void setLegalResidence(java.lang.String p0);
 public void setLastName(java.lang.String p0);
 public void setPO(java.lang.String p0);
 public java.lang.String getSSN();
 public void setSendSMS(boolean p0);
 public void setMiddleName(java.lang.String p0);
 public void setFullName(java.lang.String p0);
 public java.lang.String getName();
 public java.lang.String getFirstName();
 public void setSSN(java.lang.String p0);
 public void setResidencePhone(java.lang.String p0);
 public java.lang.String getPO();
 public java.lang.String getResidence();
}
