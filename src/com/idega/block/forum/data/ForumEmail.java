package com.idega.block.forum.data;

import javax.ejb.*;

public interface ForumEmail extends com.idega.data.IDOLegacyEntity
{
 public java.lang.String getEmailAddress()throws java.sql.SQLException;
 public java.lang.String getEmailAddressColumnName();
 public int getForumID()throws java.sql.SQLException;
 public java.lang.String getForumIDColumnName();
 public int getThreadID()throws java.sql.SQLException;
 public java.lang.String getThreadIDColumnName();
 public void setDefaultValues();
 public void setEmailAddress(java.lang.String p0);
 public void setForumID(int p0);
 public void setThreadID(int p0);
}
