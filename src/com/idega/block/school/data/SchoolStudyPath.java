package com.idega.block.school.data;


public interface SchoolStudyPath extends com.idega.data.IDOEntity
{
 public void addSchool(com.idega.block.school.data.School p0)throws com.idega.data.IDOAddRelationshipException;
 public java.lang.String getCode();
 public java.lang.String getDescription();
 public com.idega.block.school.data.SchoolType getSchoolCategory();
 public java.lang.Object getSchoolCategoryPK();
 public com.idega.block.school.data.SchoolType getSchoolType();
 public int getSchoolTypeId();
 public java.util.Collection getSchools()throws com.idega.data.IDORelationshipException;
 public boolean isValid();
 public void removeAllSchools()throws com.idega.data.IDORemoveRelationshipException;
 public void removeSchool(com.idega.block.school.data.School p0)throws com.idega.data.IDORemoveRelationshipException;
 public void setCode(java.lang.String p0);
 public void setDescription(java.lang.String p0);
 public void setSchoolCategory(java.lang.Object p0);
 public void setSchoolTypeId(java.lang.Object p0);
}
