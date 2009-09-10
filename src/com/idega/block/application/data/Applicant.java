package com.idega.block.application.data;

import com.idega.data.TreeableEntity;


public interface Applicant extends com.idega.data.IDOEntity,TreeableEntity
{
 public java.lang.String getFirstName();
 public java.lang.String getFullName();
 public java.lang.String getLastName();
 public java.lang.String getLegalResidence();
 public java.lang.String getMiddleName();
 public java.lang.String getMobilePhone();
 public java.lang.String getName();
 public java.lang.String getPO();
 public java.lang.String getResidence();
 public java.lang.String getResidencePhone();
 public java.lang.String getSSN();
 public boolean getSendSMS();
 public java.lang.String getStatus();
 public void initializeAttributes();
 public void setFirstName(java.lang.String p0);
 public void setFullName(java.lang.String p0);
 public void setLastName(java.lang.String p0);
 public void setLegalResidence(java.lang.String p0);
 public void setMiddleName(java.lang.String p0);
 public void setMobilePhone(java.lang.String p0);
 public void setPO(java.lang.String p0);
 public void setResidence(java.lang.String p0);
 public void setResidencePhone(java.lang.String p0);
 public void setSSN(java.lang.String p0);
 public void setSendSMS(boolean p0);
 public void setStatus(java.lang.String p0);
}
