package com.idega.block.school.data;


public interface SchoolCategoryHome extends com.idega.data.IDOHome
{
 public SchoolCategory create() throws javax.ejb.CreateException;
 public SchoolCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllCategories()throws javax.ejb.FinderException;
 public SchoolCategory findByLocalizedKey(java.lang.String p0)throws javax.ejb.FinderException;
 public SchoolCategory findChildcareCategory()throws javax.ejb.FinderException;
 public SchoolCategory findCollegeCategory()throws javax.ejb.FinderException;
 public SchoolCategory findElementarySchoolCategory()throws javax.ejb.FinderException;
 public SchoolCategory findHighSchoolCategory()throws javax.ejb.FinderException;
 public SchoolCategory findMusicSchoolCategory()throws javax.ejb.FinderException;
 public SchoolCategory findUniversityCategory()throws javax.ejb.FinderException;

}