package com.idega.block.school.data;

import javax.ejb.*;

public interface SchoolArea extends com.idega.data.IDOEntity
{
 public java.lang.String getName();
 public java.lang.String getSchoolAreaCity();
 public void setSchoolAreaName(java.lang.String p0);
 public void initializeAttributes();
 public void setSchoolAreaInfo(java.lang.String p0);
 public void setSchoolAreaCity(java.lang.String p0);
 public java.lang.String getSchoolAreaName();
 public java.lang.String getSchoolAreaInfo();
}
