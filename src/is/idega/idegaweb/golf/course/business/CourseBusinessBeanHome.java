package is.idega.idegaweb.golf.course.business;


public interface CourseBusinessBeanHome extends com.idega.data.IDOHome
{
 public CourseBusinessBean create() throws javax.ejb.CreateException;
 public CourseBusinessBean findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}