package se.idega.idegaweb.commune.account.provider.data;

import javax.ejb.*;

public interface ProviderApplication extends com.idega.data.IDOEntity,com.idega.block.process.data.Case
{
 public void setAdditionalInfo(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getName() throws java.rmi.RemoteException;
 public void setEmailAddress(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getCaseCodeDescription() throws java.rmi.RemoteException;
 public void setNumberOfPlaces(int p0) throws java.rmi.RemoteException;
 public void setManagerName(java.lang.String p0) throws java.rmi.RemoteException;
 public void setAddress(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getAddress() throws java.rmi.RemoteException;
 public void setName(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getAdditionalInfo() throws java.rmi.RemoteException;
 public int getNumberOfPlaces() throws java.rmi.RemoteException;
 public java.lang.String getManagerName() throws java.rmi.RemoteException;
 public java.lang.String getEmailAddress() throws java.rmi.RemoteException;
 public java.lang.String getCaseCodeKey() throws java.rmi.RemoteException;
}
