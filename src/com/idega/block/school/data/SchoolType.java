package com.idega.block.school.data;

import javax.ejb.*;

public interface SchoolType extends com.idega.data.IDOLegacyEntity
{
 public void setSchoolCategory(java.lang.String p0);
 public java.lang.String getName();
 public java.lang.String getLocalizationKey();
 public java.lang.String getSchoolTypeName();
 public java.lang.String getSchoolCategory();
 public java.lang.String getSchoolTypeInfo();
 public void setSchoolTypeName(java.lang.String p0);
 public void setSchoolTypeInfo(java.lang.String p0);
 public void setLocalizationKey(java.lang.String p0);
}
