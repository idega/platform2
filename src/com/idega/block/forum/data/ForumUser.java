package com.idega.block.forum.data;

import javax.ejb.*;

public interface ForumUser extends com.idega.data.IDOLegacyEntity
{
 public int getUserID();
 public java.lang.String getUserName();
 public void setName(java.lang.String p0);
 public void setUserID(java.lang.Integer p0);
}
