package com.idega.block.school.data;

import javax.ejb.FinderException;


public interface SchoolYear extends com.idega.data.IDOEntity
{
 public java.lang.String getName();
 public int getSchoolTypeId();
 public SchoolType getSchoolType();
 public int getSchoolYearAge();
 public java.lang.String getSchoolYearInfo();
 public java.lang.String getSchoolYearName();
 public java.util.Collection getSchoolYears(com.idega.block.school.data.SchoolType p0)throws javax.ejb.FinderException;
 public void initializeAttributes();
 public void setSchoolTypeId(int p0);
 public void setSchoolYearAge(int p0);
 public void setSchoolYearInfo(java.lang.String p0);
 public void setSchoolYearName(java.lang.String p0);
 public SchoolYear getPreviousSchoolYearFromAge()throws FinderException;
}
