package is.idega.idegaweb.golf.course.business;


public class CourseBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements CourseBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return CourseBusiness.class;
 }


 public CourseBusiness create() throws javax.ejb.CreateException{
  return (CourseBusiness) super.createIBO();
 }



}