package com.idega.block.school.data;


public interface SchoolClass extends com.idega.data.IDOEntity
{
 public void addSchoolYear(com.idega.block.school.data.SchoolYear p0)throws com.idega.data.IDOAddRelationshipException;
 public void addStudyPath(com.idega.block.school.data.SchoolStudyPath p0)throws com.idega.data.IDOAddRelationshipException;
 public void addTeacher(com.idega.user.data.User p0)throws com.idega.data.IDOAddRelationshipException;
 public java.util.Collection findRelatedSchoolYears()throws com.idega.data.IDORelationshipException;
 public java.util.Collection findRelatedStudyPaths()throws com.idega.data.IDORelationshipException;
 public java.util.Collection findRelatedUsers()throws com.idega.data.IDORelationshipException;
 public java.lang.String getGroupStringId();
 public boolean getIsSubGroup();
 public boolean getLocked();
 public java.sql.Timestamp getLockedDate();
 public java.lang.String getName();
 public boolean getReady();
 public java.sql.Timestamp getReadyDate();
 public com.idega.block.school.data.School getSchool();
 public java.lang.String getSchoolClassName();
 public int getSchoolId();
 public com.idega.block.school.data.SchoolSeason getSchoolSeason();
 public int getSchoolSeasonId();
 public com.idega.block.school.data.SchoolType getSchoolType();
 public int getSchoolTypeId();
 public java.util.Collection getSubGroupPlacements()throws com.idega.data.IDORelationshipException;
 public boolean getValid();
 public boolean hasRelationToSchoolYear(com.idega.block.school.data.SchoolYear p0);
 public boolean hasRelationToTeacher(com.idega.user.data.User p0);
 public void removeFromSchoolYear()throws com.idega.data.IDORemoveRelationshipException;
 public void removeFromUser()throws com.idega.data.IDORemoveRelationshipException;
 public void removeSchoolYear(com.idega.block.school.data.SchoolYear p0)throws com.idega.data.IDORemoveRelationshipException;
 public void removeStudyPath(com.idega.block.school.data.SchoolStudyPath p0)throws com.idega.data.IDORemoveRelationshipException;
 public void removeStudyPaths()throws com.idega.data.IDORemoveRelationshipException;
 public void removeTeacher(com.idega.user.data.User p0)throws com.idega.data.IDORemoveRelationshipException;
 public void setGroupStringId(java.lang.String p0);
 public void setIsSubGroup(boolean p0);
 public void setLocked(boolean p0);
 public void setLockedDate(java.sql.Timestamp p0);
 public void setReady(boolean p0);
 public void setReadyDate(java.sql.Timestamp p0);
 public void setSchoolClassName(java.lang.String p0);
 public void setSchoolId(int p0);
 public void setSchoolSeasonId(int p0);
 public void setSchoolTypeId(int p0);
 public void setValid(boolean p0);
}
