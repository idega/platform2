package se.idega.idegaweb.commune.childcare.business;

import javax.ejb.*;

public interface ChildCareSession extends com.idega.business.IBOSession
{
 public int getApplicationID();
 public int getChildCareID() throws java.rmi.RemoteException;
 public int getChildID();
 public se.idega.idegaweb.commune.business.CommuneUserBusiness getCommuneUserBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getParameterApplicationID();
 public java.lang.String getParameterChildCareID();
 public java.lang.String getParameterUserID();
 public int getUserID() throws java.rmi.RemoteException;
 public void setApplicationID(int p0);
 public void setChildCareID(int p0);
 public void setChildID(int p0);
 public void setUserID(int p0);
}
