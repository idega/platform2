package se.idega.idegaweb.commune.message.data;

import javax.ejb.*;

public interface SystemArchivationMessage extends com.idega.data.IDOEntity,se.idega.idegaweb.commune.message.data.PrintMessage,com.idega.block.process.data.Case
{
 public com.idega.core.data.ICFile getAttachedFile()throws java.rmi.RemoteException;
 public int getAttachedFileID()throws java.rmi.RemoteException;
 public java.lang.String getBody()throws java.rmi.RemoteException;
 public java.lang.String getCaseCodeDescription();
 public java.lang.String getCaseCodeKey();
 public java.lang.String getDateString();
 public com.idega.core.data.ICFile getMessageData()throws java.rmi.RemoteException;
 public int getMessageDataFileID()throws java.rmi.RemoteException;
 public java.lang.String getMessageType()throws java.rmi.RemoteException;
 public java.lang.String getPrintType();
 public java.lang.String getSenderName();
 public java.lang.String getSubject()throws java.rmi.RemoteException;
 public void initializeAttributes();
 public void setAttachedFile(com.idega.core.data.ICFile p0)throws java.rmi.RemoteException;
 public void setAttachedFile(int p0)throws java.rmi.RemoteException;
 public void setBody(java.lang.String p0)throws java.rmi.RemoteException;
 public void setMessageData(com.idega.core.data.ICFile p0)throws java.rmi.RemoteException;
 public void setMessageData(int p0)throws java.rmi.RemoteException;
 public void setMessageType(java.lang.String p0);
 public void setSubject(java.lang.String p0)throws java.rmi.RemoteException;
}
