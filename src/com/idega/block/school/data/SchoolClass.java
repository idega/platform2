package com.idega.block.school.data;


public interface SchoolClass extends com.idega.data.IDOEntity
{
 public boolean getLocked();
 public java.lang.String getName();
 public boolean getReady();
 public com.idega.block.school.data.School getSchool();
 public java.lang.String getSchoolClassName();
 public int getSchoolId();
 public int getSchoolSeasonId();
 public com.idega.block.school.data.SchoolType getSchoolType();
 public int getSchoolTypeId();
 public com.idega.block.school.data.SchoolYear getSchoolYear();
 public int getSchoolYearId();
 public int getTeacherId();
 public boolean getValid();
 public void initializeAttributes();
 public void setLocked(boolean p0);
 public void setReady(boolean p0);
 public void setSchoolClassName(java.lang.String p0);
 public void setSchoolId(int p0);
 public void setSchoolSeasonId(int p0);
 public void setSchoolTypeId(int p0);
 public void setSchoolYearId(int p0);
 public void setTeacherId(int p0);
 public void setValid(boolean p0);
}
