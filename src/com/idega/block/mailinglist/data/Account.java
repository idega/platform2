package com.idega.block.mailinglist.data;

import javax.ejb.*;

public interface Account extends com.idega.data.IDOLegacyEntity
{
 public java.lang.String getCreationDate();
 public java.lang.String getEmail();
 public java.lang.String getPOP3Host();
 public java.lang.String getPOP3LoginName();
 public java.lang.String getPOP3Password();
 public int getPOP3Port();
 public java.lang.String getReplyEmail();
 public java.lang.String getSMTPHost();
 public java.lang.String getSMTPLoginName();
 public java.lang.String getSMTPPassword();
 public int getSMTPPort();
 public void setCreationDate(java.sql.Timestamp p0);
 public void setEmail(java.lang.String p0);
 public void setPOP3Host(java.lang.String p0);
 public void setPOP3LoginName(java.lang.String p0);
 public void setPOP3Password(java.lang.String p0);
 public void setPOP3Port(int p0);
 public void setReplyEmail(java.lang.String p0);
 public void setSMTPHost(java.lang.String p0);
 public void setSMTPLoginName(java.lang.String p0);
 public void setSMTPPassword(java.lang.String p0);
 public void setSMTPPort(int p0);
}
