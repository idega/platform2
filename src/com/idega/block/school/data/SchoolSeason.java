package com.idega.block.school.data;


public interface SchoolSeason extends com.idega.data.IDOEntity
{
 public java.lang.String getName();
 public java.sql.Date getSchoolSeasonDueDate();
 public java.sql.Date getSchoolSeasonEnd();
 public java.lang.String getSchoolSeasonName();
 public java.sql.Date getSchoolSeasonStart();
 public void initializeAttributes();
 public void setSchoolSeasonDueDate(java.util.Date p0);
 public void setSchoolSeasonEnd(java.util.Date p0);
 public void setSchoolSeasonName(java.lang.String p0);
 public void setSchoolSeasonStart(java.util.Date p0);
}
