package com.idega.block.contract.data;

import com.idega.data.IDORemoveRelationshipException;


public interface Contract extends com.idega.data.IDOLegacyEntity
{
 public void addFileToContract(com.idega.core.file.data.ICFile p0)throws com.idega.data.IDOAddRelationshipException;
 public java.lang.Integer getCategoryId();
 public java.sql.Date getCreationDate();
 public java.lang.Integer getSignedBy(java.lang.Integer p0);
 public java.sql.Date getSignedDate();
 public java.lang.Boolean getSignedFlag();
 public java.lang.String getStatus();
 public java.sql.Date getStatusDate();
 public java.lang.String getText();
 public java.util.Set getUnsetFields();
 public java.lang.Integer getUserId();
 public java.sql.Date getValidFrom();
 public java.sql.Date getValidTo();
 public java.lang.String getXmlSignedData();
 public void initializeAttributes();
 public boolean isSigned();
 public void removeFileFromContract(com.idega.core.file.data.ICFile p0)throws com.idega.data.IDORemoveRelationshipException;
 public void setCategoryId(java.lang.Integer p0);
 public void setCategoryId(int p0);
 public void setCreationDate(java.sql.Date p0);
 public void setSignedBy(java.lang.Integer p0);
 public void setSignedDate(java.sql.Date p0);
 public void setSignedFlag(java.lang.Boolean p0);
 public void setStatus(java.lang.String p0)throws java.lang.IllegalStateException;
 public void setStatusCreated();
 public void setStatusDate(java.sql.Date p0);
 public void setStatusEnded();
 public void setStatusPrinted();
 public void setStatusRejected();
 public void setStatusResigned();
 public void setStatusSigned();
 public void setStatusTerminated();
 public void setText(java.lang.String p0);
 public void setUnsetFields(java.util.Map p0);
 public void setUserId(java.lang.Integer p0);
 public void setUserId(int p0);
 public void setValidFrom(java.sql.Date p0);
 public void setValidTo(java.sql.Date p0);
 public void setXmlSignedData(java.lang.String p0);
 public void removeAllFiles() throws IDORemoveRelationshipException;
}
