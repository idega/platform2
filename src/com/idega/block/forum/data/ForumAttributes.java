package com.idega.block.forum.data;

import javax.ejb.*;

public interface ForumAttributes extends com.idega.data.IDOLegacyEntity
{
 public int getAttributeID();
 public java.lang.String getAttributeName();
 public int getForumID();
 public void setAttributeID(java.lang.Integer p0);
 public void setAttributeName(java.lang.String p0);
 public void setForumID(java.lang.Integer p0);
}
