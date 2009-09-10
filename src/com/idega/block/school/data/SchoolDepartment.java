package com.idega.block.school.data;


public interface SchoolDepartment extends com.idega.data.IDOEntity
{
 public void addSchoolUser(com.idega.block.school.data.SchoolUser p0)throws com.idega.data.IDOAddRelationshipException;
 public java.util.Collection findRelatedUsers()throws com.idega.data.IDORelationshipException;
 public java.lang.String getDepartment();
 public int getDepartmentID();
 public java.lang.String getDepartmentPhone();
 public java.lang.String getIDColumnName();
 public java.lang.String getSchoolID();
 public void initializeAttributes();
 public void removeAllSchoolUsers()throws com.idega.data.IDORemoveRelationshipException;
 public void removeSchoolUser(com.idega.block.school.data.SchoolUser p0)throws com.idega.data.IDORemoveRelationshipException;
 public void setDepartment(java.lang.String p0);
 public void setDepartmentPhone(java.lang.String p0);
 public void setSchoolID(int p0);
}
