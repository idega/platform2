package se.idega.idegaweb.commune.account.business;


public interface AccountBusiness extends com.idega.business.IBOService {

public void acceptApplication(int p0,com.idega.user.data.User p1)throws java.rmi.RemoteException,javax.ejb.CreateException; 
public void acceptApplication(int p0,com.idega.user.data.User p1,boolean p2)throws java.rmi.RemoteException,javax.ejb.CreateException;
public void acceptApplication(int p0,com.idega.user.data.User p1,boolean p2,boolean p3)throws java.rmi.RemoteException,javax.ejb.CreateException;
 public void acceptApplication(int p0,com.idega.user.data.User p1,boolean p2,boolean p3, boolean p4 )throws java.rmi.RemoteException,javax.ejb.CreateException;
 public void acceptApplication(int p0,com.idega.user.data.User p1,boolean p2,java.lang.String p3)throws java.rmi.RemoteException,javax.ejb.CreateException;
 
 public java.lang.String getAcceptMessageSubject() throws java.rmi.RemoteException;
 public java.util.Collection getAllAcceptedApplications()throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Iterator getAllAcceptedApplicationsIterator()throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection getAllPendingApplications()throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Iterator getAllPendingApplicationsIterator()throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection getAllRejectedApplications()throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Iterator getAllRejectedApplicationsIterator()throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getBundleIdentifier() throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.business.MessageBusiness getMessageBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getRejectMessageSubject() throws java.rmi.RemoteException;
 public void rejectApplication(int p0,com.idega.user.data.User p1,java.lang.String p2)throws java.rmi.RemoteException,javax.ejb.CreateException,javax.ejb.FinderException, java.rmi.RemoteException;
 public void rejectApplication(int p0,com.idega.user.data.User p1)throws java.rmi.RemoteException,javax.ejb.CreateException,javax.ejb.FinderException, java.rmi.RemoteException;
}
