package se.idega.idegaweb.commune.care.business;


public interface AccountingSession extends com.idega.business.IBOSession
{
 public java.lang.String getOperationalField() throws java.rmi.RemoteException;
 public java.lang.String getParameterOperationalField() throws java.rmi.RemoteException;
 public void setOperationalField(java.lang.String p0) throws java.rmi.RemoteException;
}
