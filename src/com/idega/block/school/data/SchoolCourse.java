package com.idega.block.school.data;


public interface SchoolCourse extends com.idega.data.IDOEntity
{
 public void addSchoolClassMember(com.idega.block.school.data.SchoolClassMember p0)throws com.idega.data.IDOAddRelationshipException;
 public java.lang.String getCourseName();
 public java.lang.String getDescription();
 public java.lang.String getName();
 public java.util.Collection getSchoolClassMembers()throws com.idega.data.IDORelationshipException;
 public void initializeAttributes();
 public void removeAllSchoolClassMembers()throws com.idega.data.IDORemoveRelationshipException;
 public void removeSchoolClassMember(com.idega.block.school.data.SchoolClassMember p0)throws com.idega.data.IDORemoveRelationshipException;
 public void setCourseName(java.lang.String p0);
 public void setDescription(java.lang.String p0);
 public void setName(java.lang.String p0);
 public void setSchoolPk(Object schoolPk);
 public void setSchoolTypePk(Object schoolTypePk);
 public void remove(); 
}
