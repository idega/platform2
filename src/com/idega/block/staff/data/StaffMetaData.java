package com.idega.block.staff.data;

import javax.ejb.*;

public interface StaffMetaData extends com.idega.data.IDOLegacyEntity
{
 public java.lang.String getAttribute();
 public int getUserID();
 public java.lang.String getValue();
 public void setAttribute(java.lang.String p0);
 public void setUserID(int p0);
 public void setValue(java.lang.String p0);
}
