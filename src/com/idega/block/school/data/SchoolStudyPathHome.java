package com.idega.block.school.data;


public interface SchoolStudyPathHome extends com.idega.data.IDOHome
{
 public SchoolStudyPath create() throws javax.ejb.CreateException;
 public SchoolStudyPath findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllByIDs(java.lang.String[] p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllStudyPaths()throws javax.ejb.FinderException;
 public java.util.Collection findAllStudyPathsByCodeLength(int p0)throws javax.ejb.FinderException;
 public SchoolStudyPath findByCode(java.lang.String p0)throws javax.ejb.FinderException;
 public SchoolStudyPath findByCodeAndSchoolType(java.lang.String p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findBySchool(com.idega.block.school.data.School p0)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolAndSchoolCategory(com.idega.block.school.data.School p0,com.idega.block.school.data.SchoolCategory p1)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolCategory(com.idega.block.school.data.SchoolCategory p0)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolType(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolTypes(java.lang.String[] p0)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolTypes(java.util.Collection p0)throws javax.ejb.FinderException;
 public java.util.Collection findStudyPaths(com.idega.block.school.data.School p0,java.util.Collection p1)throws javax.ejb.FinderException;
 public java.util.Collection findStudyPaths(com.idega.block.school.data.School p0)throws com.idega.data.IDORelationshipException,javax.ejb.FinderException;
 public java.util.Collection findStudyPaths(com.idega.block.school.data.School p0,java.lang.Object p1)throws javax.ejb.FinderException;

}