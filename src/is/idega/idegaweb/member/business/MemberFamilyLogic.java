package is.idega.idegaweb.member.business;

import javax.ejb.*;

public interface MemberFamilyLogic extends com.idega.business.IBOService
{
 public boolean isParentOf(com.idega.user.data.User p0,com.idega.user.data.User p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean hasPersonGotChildren(com.idega.user.data.User p0) throws java.rmi.RemoteException;
 public void setAsChildFor(com.idega.user.data.User p0,com.idega.user.data.User p1)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public java.util.Collection getChildrenFor(com.idega.user.data.User p0)throws java.rmi.RemoteException,is.idega.idegaweb.member.business.NoChildrenFound, java.rmi.RemoteException;
 public java.util.Collection getCustodiansFor(com.idega.user.data.User p0)throws java.rmi.RemoteException,is.idega.idegaweb.member.business.NoCustodianFound, java.rmi.RemoteException;
 public void removeAsParentFor(com.idega.user.data.User p0,com.idega.user.data.User p1)throws java.rmi.RemoteException,javax.ejb.RemoveException, java.rmi.RemoteException;
 public void setAsParentFor(com.idega.user.data.User p0,com.idega.user.data.User p1)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public boolean isSpouseOf(com.idega.user.data.User p0,com.idega.user.data.User p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.User getSpouseFor(com.idega.user.data.User p0)throws is.idega.idegaweb.member.business.NoSpouseFound, java.rmi.RemoteException;
 public boolean isChildOf(com.idega.user.data.User p0,com.idega.user.data.User p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void removeAsSpouseFor(com.idega.user.data.User p0,com.idega.user.data.User p1)throws java.rmi.RemoteException,javax.ejb.RemoveException, java.rmi.RemoteException;
 public boolean hasPersonGotSpouse(com.idega.user.data.User p0) throws java.rmi.RemoteException;
 public void setAsSpouseFor(com.idega.user.data.User p0,com.idega.user.data.User p1)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public void removeAsChildFor(com.idega.user.data.User p0,com.idega.user.data.User p1)throws java.rmi.RemoteException,javax.ejb.RemoveException, java.rmi.RemoteException;
}
