package com.idega.block.school.data;


public interface SchoolYearHome extends com.idega.data.IDOHome
{
 public SchoolYear create() throws javax.ejb.CreateException;
 public SchoolYear findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllByAge(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllByAge(com.idega.block.school.data.SchoolType p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllByIDs(java.lang.String[] p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllSchoolYearBySchoolCategory(com.idega.block.school.data.SchoolCategory p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllSchoolYearBySchoolType(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllSchoolYears()throws javax.ejb.FinderException;
 public java.util.Collection findAllSchoolYearsBySchoolCategory(com.idega.block.school.data.SchoolCategory p0,boolean p1)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolCategory(java.lang.String p0)throws javax.ejb.FinderException;
 public SchoolYear findByYearName(java.lang.String p0)throws javax.ejb.FinderException;
 public SchoolYear findByYearName(com.idega.block.school.data.SchoolType p0,java.lang.String p1)throws javax.ejb.FinderException;
 public SchoolYear findPreviousSchoolYearFromAge(com.idega.block.school.data.SchoolYear p0)throws javax.ejb.FinderException;

}