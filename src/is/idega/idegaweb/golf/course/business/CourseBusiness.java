package is.idega.idegaweb.golf.course.business;


public interface CourseBusiness extends com.idega.business.IBOService
{
 public void setCourseValidation(int p0,boolean p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean storeCourse(int p0,int p1,java.lang.String p2,java.lang.String p3,int p4)throws java.rmi.RemoteException, java.rmi.RemoteException;
}
