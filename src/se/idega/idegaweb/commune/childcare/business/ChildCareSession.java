package se.idega.idegaweb.commune.childcare.business;

import javax.ejb.*;

public interface ChildCareSession extends com.idega.business.IBOSession
{
 public void setChildCareID(int p0) throws java.rmi.RemoteException;
 public int getUserID() throws java.rmi.RemoteException;
 public int getChildCareID()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setUserID(int p0) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.business.CommuneUserBusiness getCommuneUserBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getParameterChildCareID() throws java.rmi.RemoteException;
}
