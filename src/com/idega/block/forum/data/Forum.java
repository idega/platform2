package com.idega.block.forum.data;

import javax.ejb.*;

public interface Forum extends com.idega.data.IDOLegacyEntity
{
 public java.lang.String getForumDescription();
 public java.lang.String getForumName();
 public int getGroupID();
 public java.sql.Timestamp getNewThreadDate();
 public int getNumberOfThreads();
 public boolean isValid();
 public void setForumDescription(java.lang.String p0);
 public void setForumName(java.lang.String p0);
 public void setGroupID(java.lang.Integer p0);
 public void setNewThreadDate(java.sql.Timestamp p0);
 public void setNumberOfThreads(java.lang.Integer p0);
 public void setValid(boolean p0);
}
