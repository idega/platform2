package com.idega.block.email.data;

import javax.ejb.*;

public interface MailTopic extends com.idega.data.IDOLegacyEntity,com.idega.block.email.business.EmailTopic
{
 public java.sql.Timestamp getCreated();
 public java.lang.String getDescription();
 public int getGroupId();
 public int getListId();
 public java.lang.String getName();
 public void setCreated(java.sql.Timestamp p0);
 public void setDescription(java.lang.String p0);
 public void setGroupId(int p0);
 public void setListId(int p0);
 public void setName(java.lang.String p0);
}
