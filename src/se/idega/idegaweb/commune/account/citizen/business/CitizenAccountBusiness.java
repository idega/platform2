package se.idega.idegaweb.commune.account.citizen.business;

import javax.ejb.*;

import se.idega.idegaweb.commune.account.business.AccountBusiness;

public interface CitizenAccountBusiness extends com.idega.business.IBOService,AccountBusiness
{
 public void acceptApplication(int p0,com.idega.user.data.User p1)throws java.rmi.RemoteException,javax.ejb.CreateException,javax.ejb.FinderException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.account.citizen.data.CitizenAccount getAccount(int p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.List getListOfUnapprovedApplications() throws java.rmi.RemoteException;
 public com.idega.user.data.User getUser(java.lang.String p0) throws java.rmi.RemoteException;
 public boolean insertApplication(com.idega.user.data.User p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4) throws java.rmi.RemoteException;
 public void rejectApplication(int p0,com.idega.user.data.User p1,java.lang.String p2)throws java.rmi.RemoteException,javax.ejb.CreateException,javax.ejb.FinderException, java.rmi.RemoteException;
}
