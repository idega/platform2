package com.idega.block.school.data;


public interface SchoolYear extends com.idega.data.IDOEntity
{
 public java.lang.String getLocalizedKey();
 public java.lang.String getName();
 public com.idega.block.school.data.SchoolYear getPreviousSchoolYearFromAge()throws javax.ejb.FinderException;
 public com.idega.block.school.data.SchoolCategory getSchoolCategory();
 public java.lang.Object getSchoolCategoryPK();
 public com.idega.block.school.data.SchoolType getSchoolType();
 public int getSchoolTypeId();
 public int getSchoolYearAge();
 public java.lang.String getSchoolYearInfo();
 public java.lang.String getSchoolYearName();
 public java.util.Collection getSchoolYears(com.idega.block.school.data.SchoolType p0)throws javax.ejb.FinderException;
 public boolean isSelectable();
 public void setIsSelectable(boolean p0);
 public void setLocalizedKey(java.lang.String p0);
 public void setSchoolCategory(java.lang.Object p0);
 public void setSchoolTypeId(int p0);
 public void setSchoolYearAge(int p0);
 public void setSchoolYearInfo(java.lang.String p0);
 public void setSchoolYearName(java.lang.String p0);
}
