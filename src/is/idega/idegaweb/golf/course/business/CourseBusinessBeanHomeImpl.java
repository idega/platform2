package is.idega.idegaweb.golf.course.business;


public class CourseBusinessBeanHomeImpl extends com.idega.data.IDOFactory implements CourseBusinessBeanHome
{
 protected Class getEntityInterfaceClass(){
  return CourseBusinessBean.class;
 }


 public CourseBusinessBean create() throws javax.ejb.CreateException{
  return (CourseBusinessBean) super.createIDO();
 }


 public CourseBusinessBean findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CourseBusinessBean) super.findByPrimaryKeyIDO(pk);
 }



}