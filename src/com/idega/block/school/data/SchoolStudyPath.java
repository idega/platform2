package com.idega.block.school.data;


public interface SchoolStudyPath extends com.idega.data.IDOEntity
{
 public void addSchool(com.idega.block.school.data.School p0)throws com.idega.data.IDOAddRelationshipException;
 public void addSchoolClassMember(com.idega.block.school.data.SchoolClassMember p0)throws com.idega.data.IDOAddRelationshipException;
 public java.lang.String getCode();
 public java.lang.String getDescription();
 public java.util.Collection getSchoolClassMembers()throws com.idega.data.IDORelationshipException;
 public com.idega.block.school.data.SchoolType getSchoolType();
 public int getSchoolTypeId();
 public java.util.Collection getSchools()throws com.idega.data.IDORelationshipException;
 public void initializeAttributes();
 public void removeAllSchoolClassMembers()throws com.idega.data.IDORemoveRelationshipException;
 public void removeAllSchools()throws com.idega.data.IDORemoveRelationshipException;
 public void removeSchool(com.idega.block.school.data.School p0)throws com.idega.data.IDORemoveRelationshipException;
 public void removeSchoolClassMember(com.idega.block.school.data.SchoolClassMember p0)throws com.idega.data.IDORemoveRelationshipException;
 public void setCode(java.lang.String p0);
 public void setDescription(java.lang.String p0);
 public void setSchoolTypeId(java.lang.Object p0);
}
