package com.idega.block.school.data;


public interface SchoolType extends com.idega.data.IDOEntity
{
 public com.idega.block.school.data.SchoolCategory getCategory();
 public java.lang.String getLocalizationKey();
 public int getMaxSchoolAge();
 public java.lang.String getName();
 public java.lang.String getSchoolCategory();
 public java.lang.String getSchoolTypeInfo();
 public java.lang.String getSchoolTypeName();
 public void initializeAttributes();
 public void setCategory(com.idega.block.school.data.SchoolCategory p0);
 public void setLocalizationKey(java.lang.String p0);
 public void setMaxSchoolAge(java.lang.String p0);
 public void setSchoolCategory(java.lang.String p0);
 public void setSchoolTypeInfo(java.lang.String p0);
 public void setSchoolTypeName(java.lang.String p0);
}
