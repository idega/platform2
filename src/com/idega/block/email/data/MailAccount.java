package com.idega.block.email.data;


public interface MailAccount extends com.idega.data.IDOLegacyEntity,com.idega.block.email.business.EmailAccount
{
 public java.sql.Timestamp getCreated();
 public java.lang.String getHost();
 public java.lang.String getName();
 public java.lang.String getPassword();
 public int getPort();
 public int getProtocol();
 public java.lang.String getProtocolName();
 public java.lang.String getUser();
 public void setCreated(java.sql.Timestamp p0);
 public void setHost(java.lang.String p0);
 public void setName(java.lang.String p0);
 public void setPassword(java.lang.String p0);
 public void setPort(int p0);
 public void setProtocol(int p0);
 public void setUser(java.lang.String p0);
}
