package com.idega.block.school.data;


public class SchoolCourseHomeImpl extends com.idega.data.IDOFactory implements SchoolCourseHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolCourse.class;
 }


 public SchoolCourse create() throws javax.ejb.CreateException{
  return (SchoolCourse) super.createIDO();
 }


 public SchoolCourse findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolCourse) super.findByPrimaryKeyIDO(pk);
 }


public java.util.Collection findSchoolCourses(com.idega.block.school.data.School p0,java.lang.Object p1)throws com.idega.data.IDOLookupException,com.idega.data.IDORelationshipException,javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection theReturn = ((SchoolCourseBMPBean)entity).ejbHomeFindSchoolCourses(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public java.util.Collection findSchoolCourses(com.idega.block.school.data.School p0)throws com.idega.data.IDOLookupException,com.idega.data.IDORelationshipException,javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection theReturn = ((SchoolCourseBMPBean)entity).ejbHomeFindSchoolCourses(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public java.util.Collection findSchoolCourses(com.idega.block.school.data.School p0,java.util.Collection p1)throws javax.ejb.FinderException,com.idega.data.IDOLookupException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection theReturn = ((SchoolCourseBMPBean)entity).ejbHomeFindSchoolCourses(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}