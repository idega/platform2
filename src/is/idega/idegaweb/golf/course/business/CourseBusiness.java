package is.idega.idegaweb.golf.course.business;

import is.idega.idegaweb.golf.course.data.Course;

import com.idega.user.data.Group;
import com.idega.user.data.User;


public interface CourseBusiness extends com.idega.business.IBOService
{
 public void setCourseValidation(Course p0,boolean p1,User p2) throws java.rmi.RemoteException;
 public boolean storeCourse(java.lang.Object p0,Group p1,java.lang.String p2,String p3,int p4)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean storeHoles(java.util.Collection p0,java.lang.Object p1,java.lang.Object p2,int p3,int p4,java.sql.Date p5,java.lang.String p6) throws java.rmi.RemoteException;
 public boolean storeTee(java.lang.Object p0,java.lang.Object p1,java.lang.Object p2,java.lang.Object p3,float p4,int p5,int p6,java.sql.Date p7,java.lang.String p8) throws java.rmi.RemoteException;
}
