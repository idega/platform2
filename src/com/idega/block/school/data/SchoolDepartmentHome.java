package com.idega.block.school.data;


public interface SchoolDepartmentHome extends com.idega.data.IDOHome
{
 public SchoolDepartment create() throws javax.ejb.CreateException;
 public SchoolDepartment findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllDepartmentsBySchool(com.idega.block.school.data.School p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllDepartmentsBySchool(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllDepartmentsByUser(com.idega.block.school.data.SchoolUser p0)throws javax.ejb.FinderException;

}