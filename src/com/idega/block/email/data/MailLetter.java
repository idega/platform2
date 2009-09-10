package com.idega.block.email.data;


public interface MailLetter extends com.idega.data.IDOLegacyEntity,com.idega.block.email.business.EmailLetter
{
 public java.lang.String getBody();
 public java.sql.Timestamp getCreated();
 public java.lang.String getFromAddress();
 public java.lang.String getFromName();
 public java.lang.String getSubject();
 public int getType();
 public void setBody(java.lang.String p0);
 public void setCreated(java.sql.Timestamp p0);
 public void setFromAddress(java.lang.String p0);
 public void setFromName(java.lang.String p0);
 public void setSubject(java.lang.String p0);
 public void setType(int p0);
 public Integer getIdentifier();
}
