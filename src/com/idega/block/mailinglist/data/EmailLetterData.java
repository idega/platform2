package com.idega.block.mailinglist.data;

import javax.ejb.*;

public interface EmailLetterData extends com.idega.data.IDOLegacyEntity
{
 public com.idega.core.data.ICFile getAttachments();
 public java.lang.String getBCCEmail();
 public java.lang.String getBody();
 public java.lang.String getCCEmail();
 public java.lang.String getDate();
 public java.lang.String getFromEmail();
 public boolean getHasSent();
 public java.lang.String getSubject();
 public java.lang.String getToEmail();
 public void setAttachments(com.idega.core.data.ICFile p0);
 public void setBCCEmail(java.lang.String p0);
 public void setBody(java.lang.String p0);
 public void setCCEmail(java.lang.String p0);
 public void setDate(java.sql.Timestamp p0);
 public void setFromEmail(java.lang.String p0);
 public void setHasSent(java.lang.Boolean p0);
 public void setSubject(java.lang.String p0);
 public void setToEmail(java.lang.String p0);
}
