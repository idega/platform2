package com.idega.block.school.data;


public interface SchoolTypeHome extends com.idega.data.IDOHome
{
 public SchoolType create() throws javax.ejb.CreateException;
 public SchoolType createLegacy();
 public SchoolType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public SchoolType findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public SchoolType findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;
 public java.util.Collection findAllSchoolTypes()throws javax.ejb.FinderException;
 public java.util.Collection findAllByCategory(java.lang.String p0)throws javax.ejb.FinderException;

}