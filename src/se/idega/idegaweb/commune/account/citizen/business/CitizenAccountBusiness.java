package se.idega.idegaweb.commune.account.citizen.business;

import javax.ejb.*;

public interface CitizenAccountBusiness extends com.idega.business.IBOService
{
 public java.util.List getListOfUnapprovedApplications() throws java.rmi.RemoteException;
 public com.idega.user.data.User getUser(java.lang.String p0) throws java.rmi.RemoteException;
 public boolean insertApplication(com.idega.user.data.User p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4) throws java.rmi.RemoteException;
}
