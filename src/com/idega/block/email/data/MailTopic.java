package com.idega.block.email.data;

import javax.ejb.*;

public interface MailTopic extends com.idega.data.CategoryEntity,com.idega.block.email.business.EmailTopic
{
 public java.sql.Timestamp getCreated();
 public void setGroupId(int p0);
 public int getListId();
 public java.lang.String getName();
 public int getGroupId();
 public void setListId(int p0);
 public void setDescription(java.lang.String p0);
 public void setName(java.lang.String p0);
 public java.lang.String getDescription();
 public void setCreated(java.sql.Timestamp p0);
}
