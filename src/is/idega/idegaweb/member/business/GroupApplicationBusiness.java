package is.idega.idegaweb.member.business;

import javax.ejb.*;

public interface GroupApplicationBusiness extends com.idega.business.IBOService
{
 public boolean createGroupApplication(com.idega.user.data.Group p0,java.lang.String p1,java.lang.String p2,java.lang.String p3)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public is.idega.idegaweb.member.data.GroupApplication createGroupApplication(com.idega.user.data.Group p0,com.idega.user.data.User p1,java.lang.String p2,java.lang.String p3)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public boolean createGroupApplication(com.idega.user.data.Group p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,java.lang.String p6,java.lang.String p7)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
}
