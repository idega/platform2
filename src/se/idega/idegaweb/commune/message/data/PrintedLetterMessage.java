package se.idega.idegaweb.commune.message.data;

import javax.ejb.*;

public interface PrintedLetterMessage extends com.idega.data.IDOEntity,se.idega.idegaweb.commune.message.data.Message,com.idega.block.process.data.Case
{
 public void setMessageData(com.idega.core.data.ICFile p0)throws java.rmi.RemoteException;
 public java.lang.String getDateString();
 public void setMessageType(java.lang.String p0)throws java.rmi.RemoteException;
 public int getMessageDataFileID()throws java.rmi.RemoteException;
 public java.lang.String getSenderName();
 public void initializeAttributes();
 public com.idega.core.data.ICFile getMessageData()throws java.rmi.RemoteException;
 public java.lang.String getSubject()throws java.rmi.RemoteException;
 public java.lang.String getLetterType();
 public void setAsPasswordLetter();
 public java.lang.String getMessageType()throws java.rmi.RemoteException;
 public void setLetterType(java.lang.String p0);
 public void setBody(java.lang.String p0)throws java.rmi.RemoteException;
 public void setMessageData(int p0)throws java.rmi.RemoteException;
 public java.lang.String getCaseCodeDescription();
 public void setSubject(java.lang.String p0)throws java.rmi.RemoteException;
 public java.lang.String getCaseCodeKey();
 public java.lang.String getBody()throws java.rmi.RemoteException;
}
