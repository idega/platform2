package se.idega.idegaweb.commune.message.data;

import javax.ejb.*;

import com.idega.core.data.ICFile;

public interface SystemArchivationMessage extends com.idega.data.IDOEntity,se.idega.idegaweb.commune.message.data.Message,com.idega.block.process.data.Case
{
 public void setMessageData(com.idega.core.data.ICFile p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getDateString() throws java.rmi.RemoteException;
 public void setMessageType(java.lang.String p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getMessageDataFileID()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getSenderName() throws java.rmi.RemoteException;
 public void initializeAttributes() throws java.rmi.RemoteException;
 public com.idega.core.data.ICFile getMessageData()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getSubject()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getMessageType()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setBody(java.lang.String p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setMessageData(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getCaseCodeDescription() throws java.rmi.RemoteException;
 public void setSubject(java.lang.String p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getCaseCodeKey() throws java.rmi.RemoteException;
 public java.lang.String getBody()throws java.rmi.RemoteException, java.rmi.RemoteException;


  public void setAttatchedFile(ICFile file)throws java.rmi.RemoteException;

  public void setAttatchedFile(int fileID)throws java.rmi.RemoteException;
  
  public ICFile getAttatchedFile()throws java.rmi.RemoteException;
  
   public int getAttatchedFileID()throws java.rmi.RemoteException;

}
