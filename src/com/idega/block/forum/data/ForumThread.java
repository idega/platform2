package com.idega.block.forum.data;

import javax.ejb.*;

public interface ForumThread extends com.idega.data.IDOLegacyEntity
{
 public int getForumID();
 public int getNumberOfResponses();
 public int getParentThreadID();
 public java.lang.String getThreadBody();
 public java.sql.Timestamp getThreadDate();
 public java.lang.String getThreadSubject();
 public int getUserID();
 public java.lang.String getUserName();
 public boolean isValid();
 public void setForumID(java.lang.Integer p0);
 public void setNumberOfResponses(java.lang.Integer p0);
 public void setParentThreadID(java.lang.Integer p0);
 public void setThreadBody(java.lang.String p0);
 public void setThreadDate(java.sql.Timestamp p0);
 public void setThreadSubject(java.lang.String p0);
 public void setUserID(java.lang.Integer p0);
 public void setUserName(java.lang.String p0);
 public void setValid(boolean p0);
}
