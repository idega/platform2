package se.idega.idegaweb.commune.message.business;

import javax.ejb.*;

public interface MessageBusiness extends com.idega.business.IBOService
{
  public static final String SEND_TO_MESSAGE_BOX = "msg_send_box";
  public static final String SEND_TO_EMAIL = "msg_send_email";

 public void deleteUserMessage(int p0)throws javax.ejb.FinderException,javax.ejb.RemoveException,java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.Message createPrintArchivationMessage(int p0,java.lang.String p1,java.lang.String p2)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.Message createUserMessage(int p0,java.lang.String p1,java.lang.String p2)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.process.data.CaseCode getCaseCodeSystemArchivationMessage()throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findMessages(com.idega.user.data.User p0)throws java.lang.Exception, java.rmi.RemoteException;
 public boolean isMessageRead(se.idega.idegaweb.commune.message.data.Message p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.Message createUserMessage(com.idega.user.data.User p0,java.lang.String p1,java.lang.String p2)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void deleteMessage(java.lang.String p0,int p1)throws javax.ejb.FinderException,javax.ejb.RemoveException,java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.Message createPrintedLetterMessage(int p0,java.lang.String p1,java.lang.String p2)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.Message getUserMessage(int p0)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.Message getMessage(java.lang.String p0,int p1)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void sendMessage(java.lang.String p0,java.lang.String p1,java.lang.String p2) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.Message createPrintArchivationMessage(com.idega.user.data.User p0,java.lang.String p1,java.lang.String p2)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.process.data.CaseCode getCaseCodeUserMessage()throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public void markMessageAsRead(se.idega.idegaweb.commune.message.data.Message p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.Message createPrintedLetterMessage(com.idega.user.data.User p0,java.lang.String p1,java.lang.String p2)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.process.data.CaseCode getCaseCodePrintedLetterMessage()throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
}
