package se.idega.idegaweb.commune.message.data;

import javax.ejb.*;

public interface Message extends com.idega.data.IDOEntity,com.idega.block.process.data.Case
{
 public void setBody(java.lang.String p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setSubject(java.lang.String p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getSubject()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getBody()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setSender(int userID);
 public int getSender();
}
