package com.idega.block.school.data;


public interface SchoolTypeHome extends com.idega.data.IDOHome
{
 public SchoolType create() throws javax.ejb.CreateException;
 public SchoolType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllByCategory(java.lang.String p0,boolean p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllByCategory(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllFreetimeTypes()throws javax.ejb.FinderException;
 public java.util.Collection findAllFreetimeTypes(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllSchoolTypes()throws javax.ejb.FinderException;
 public SchoolType findByTypeKey(java.lang.String p0)throws javax.ejb.FinderException;

}