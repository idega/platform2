package com.idega.block.school.data;


public interface SchoolClassHome extends com.idega.data.IDOHome
{
 public SchoolClass create() throws javax.ejb.CreateException;
 public SchoolClass findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;
 public SchoolClass findByNameAndSchool(java.lang.String p0,com.idega.block.school.data.School p1)throws javax.ejb.FinderException;
 public SchoolClass findByNameAndSchool(java.lang.String p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findBySchool(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findBySchool(com.idega.block.school.data.School p0)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolAndCategory(int p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolAndInYear(int p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolAndSeason(com.idega.block.school.data.School p0,com.idega.block.school.data.SchoolSeason p1)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolAndSeason(int p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolAndSeasonAndInYear(int p0,int p1,int p2,boolean p3)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolAndSeasonAndInYear(int p0,int p1,int p2)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolAndSeasonAndInYear(int p0,int p1,int p2,int p3)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolAndSeasonAndTeacher(int p0,int p1,int p2)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolAndSeasonAndTeacher(com.idega.block.school.data.School p0,com.idega.block.school.data.SchoolSeason p1,com.idega.user.data.User p2)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolAndSeasonAndYear(int p0,int p1,int p2,boolean p3)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolAndSeasonAndYear(com.idega.block.school.data.School p0,com.idega.block.school.data.SchoolSeason p1,com.idega.block.school.data.SchoolYear p2)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolAndSeasonAndYear(int p0,int p1,int p2)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolAndSeasonAndYearAndStudyPath(com.idega.block.school.data.School p0,com.idega.block.school.data.SchoolSeason p1,com.idega.block.school.data.SchoolYear p2,com.idega.block.school.data.SchoolStudyPath p3,boolean p4)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolAndSeasonAndYears(int p0,int p1,java.lang.String[] p2)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolAndSeasonAndYears(int p0,int p1,java.lang.String[] p2,boolean p3)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolAndTeacher(com.idega.block.school.data.School p0,com.idega.user.data.User p1)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolAndTeacher(int p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolAndYear(com.idega.block.school.data.School p0,com.idega.block.school.data.SchoolYear p1)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolAndYear(int p0,int p1)throws javax.ejb.FinderException;
 public SchoolClass findBySchoolClassNameSchoolSchoolYearSchoolSeason(java.lang.String p0,com.idega.block.school.data.School p1,com.idega.block.school.data.SchoolYear p2,com.idega.block.school.data.SchoolSeason p3)throws javax.ejb.FinderException;
 public java.util.Collection findBySeason(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findBySeason(com.idega.block.school.data.SchoolSeason p0)throws javax.ejb.FinderException;
 public java.util.Collection findBySeasonAndYear(int p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findBySeasonAndYear(com.idega.block.school.data.SchoolSeason p0,com.idega.block.school.data.SchoolYear p1)throws javax.ejb.FinderException;
 public java.util.Collection findByTeacher(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findByTeacher(com.idega.user.data.User p0)throws javax.ejb.FinderException;
 public SchoolClass findOneBySchool(int p0)throws javax.ejb.FinderException;
 public int getNumberOfStudentsInClass(int p0)throws com.idega.data.IDOException;

}