package com.idega.block.school.data;


public class SchoolStudyPathHomeImpl extends com.idega.data.IDOFactory implements SchoolStudyPathHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolStudyPath.class;
 }


 public SchoolStudyPath create() throws javax.ejb.CreateException{
  return (SchoolStudyPath) super.createIDO();
 }


 public SchoolStudyPath findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolStudyPath) super.findByPrimaryKeyIDO(pk);
 }


public java.util.Collection findSchoolCourses(com.idega.block.school.data.School p0,java.lang.Object p1)throws com.idega.data.IDOLookupException,com.idega.data.IDORelationshipException,javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection theReturn = ((SchoolStudyPathBMPBean)entity).ejbHomeFindSchoolCourses(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public java.util.Collection findSchoolCourses(com.idega.block.school.data.School p0)throws com.idega.data.IDOLookupException,com.idega.data.IDORelationshipException,javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection theReturn = ((SchoolStudyPathBMPBean)entity).ejbHomeFindSchoolCourses(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public java.util.Collection findSchoolCourses(com.idega.block.school.data.School p0,java.util.Collection p1)throws javax.ejb.FinderException,com.idega.data.IDOLookupException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection theReturn = ((SchoolStudyPathBMPBean)entity).ejbHomeFindSchoolCourses(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}