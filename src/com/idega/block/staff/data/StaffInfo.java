package com.idega.block.staff.data;


public interface StaffInfo extends com.idega.data.IDOLegacyEntity
{
 public java.lang.String getArea();
 public java.sql.Date getBeganWork();
 public java.lang.String getEducation();
 public java.lang.String getIDColumnName();
 public int getImageID();
 public java.lang.String getSchool();
 public java.lang.String getTitle();
 public void setArea(java.lang.String p0);
 public void setBeganWork(java.sql.Date p0);
 public void setDefaultValues();
 public void setEducation(java.lang.String p0);
 public void setImageID(int p0);
 public void setSchool(java.lang.String p0);
 public void setTitle(java.lang.String p0);
}
