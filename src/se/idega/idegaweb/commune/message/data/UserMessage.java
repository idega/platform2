package se.idega.idegaweb.commune.message.data;

import javax.ejb.*;

public interface UserMessage extends com.idega.data.IDOEntity,se.idega.idegaweb.commune.message.data.Message,com.idega.block.process.data.Case
{
 public void setBody(java.lang.String p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getCaseCodeDescription() throws java.rmi.RemoteException;
 public void setSubject(java.lang.String p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getSubject()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getBody()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getCaseCodeKey() throws java.rmi.RemoteException;
}
