package com.idega.block.school.data;

import javax.ejb.*;

public interface SchoolClass extends com.idega.data.IDOEntity
{
 public boolean getLocked();
 public java.lang.String getName();
 public boolean getReady();
 public java.lang.String getSchoolClassName(java.lang.String p0);
 public int getSchoolId();
 public int getSchoolSeasonId();
 public int getSchoolYearId();
 public int getTeacherId();
 public boolean getValid();
 public void initializeAttributes();
 public void setLocked(boolean p0);
 public void setReady(boolean p0);
 public void setSchoolClassName(java.lang.String p0);
 public void setSchoolId(int p0);
 public void setSchoolSeasonId(int p0);
 public void setSchoolYearId(int p0);
 public void setTeacherId(int p0);
 public void setValid(boolean p0);
}
