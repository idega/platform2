package se.idega.idegaweb.commune.account.citizen.data;

import javax.ejb.*;

public interface CitizenAccount extends com.idega.data.IDOEntity,com.idega.block.process.data.Case
{
 public void setEmail(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getEmail() throws java.rmi.RemoteException;
 public java.lang.String getCaseCodeDescription() throws java.rmi.RemoteException;
 public void setPhoneHome(java.lang.String p0) throws java.rmi.RemoteException;
 public void setPID(java.lang.String p0) throws java.rmi.RemoteException;
 public void setPhoneWork(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getPhoneHome() throws java.rmi.RemoteException;
 public java.lang.String getPID() throws java.rmi.RemoteException;
 public java.lang.String getPhoneWork() throws java.rmi.RemoteException;
 public java.lang.String getCaseCodeKey() throws java.rmi.RemoteException;
}
