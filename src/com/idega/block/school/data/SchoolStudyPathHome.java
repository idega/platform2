package com.idega.block.school.data;


public interface SchoolStudyPathHome extends com.idega.data.IDOHome
{
 public SchoolStudyPath create() throws javax.ejb.CreateException;
 public SchoolStudyPath findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findSchoolCourses(com.idega.block.school.data.School p0,java.lang.Object p1)throws com.idega.data.IDOLookupException,com.idega.data.IDORelationshipException,javax.ejb.FinderException;
 public java.util.Collection findSchoolCourses(com.idega.block.school.data.School p0)throws com.idega.data.IDOLookupException,com.idega.data.IDORelationshipException,javax.ejb.FinderException;
 public java.util.Collection findSchoolCourses(com.idega.block.school.data.School p0,java.util.Collection p1)throws javax.ejb.FinderException,com.idega.data.IDOLookupException;

}