package com.idega.block.projectmanager.data;

import javax.ejb.*;

public interface ProjectGroup extends com.idega.data.IDOLegacyEntity
{
 public java.lang.String getIDColumnName();
 public java.lang.String getName();
 public boolean isVisible();
 public void setDefaultValues();
 public void setName(java.lang.String p0);
 public void setVisible(boolean p0);
}
