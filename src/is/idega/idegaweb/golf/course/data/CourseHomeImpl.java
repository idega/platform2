package is.idega.idegaweb.golf.course.data;


public class CourseHomeImpl extends com.idega.data.IDOFactory implements CourseHome
{
 protected Class getEntityInterfaceClass(){
  return Course.class;
 }


 public Course create() throws javax.ejb.CreateException{
  return (Course) super.createIDO();
 }


public java.util.Collection findAllCourses()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CourseBMPBean)entity).ejbFindAllCourses();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public Course findCourseByClubAndName(com.idega.user.data.Group p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((CourseBMPBean)entity).ejbFindCourseByClubAndName(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public Course findCourseByName(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((CourseBMPBean)entity).ejbFindCourseByName(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findCoursesByClub(com.idega.user.data.Group p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CourseBMPBean)entity).ejbFindCoursesByClub(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Course findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Course) super.findByPrimaryKeyIDO(pk);
 }



}