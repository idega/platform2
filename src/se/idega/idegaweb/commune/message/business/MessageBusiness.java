package se.idega.idegaweb.commune.message.business;

import javax.ejb.*;

public interface MessageBusiness extends com.idega.business.IBOService
{
 public se.idega.idegaweb.commune.message.data.Message createPrintArchivationMessage(com.idega.user.data.User p0,java.lang.String p1,java.lang.String p2)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.SystemArchivationMessage createPrintArchivationMessage(com.idega.user.data.User p0,com.idega.user.data.User p1,java.lang.String p2,java.lang.String p3,com.idega.core.data.ICFile p4)throws javax.ejb.CreateException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.SystemArchivationMessage createPrintArchivationMessage(int p0,int p1,java.lang.String p2,java.lang.String p3,int p4)throws javax.ejb.CreateException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.Message createPrintArchivationMessage(int p0,java.lang.String p1,java.lang.String p2)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.Message createPrintedLetterMessage(com.idega.user.data.User p0,java.lang.String p1,java.lang.String p2)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.PrintedLetterMessage createPrintedLetterMessage(com.idega.user.data.User p0,java.lang.String p1,java.lang.String p2,java.lang.String p3)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.Message createPrintedLetterMessage(int p0,java.lang.String p1,java.lang.String p2)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.PrintedLetterMessage createPrintedPasswordLetterMessage(com.idega.user.data.User p0,java.lang.String p1,java.lang.String p2)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.Message createUserMessage(com.idega.user.data.User p0,java.lang.String p1,java.lang.String p2) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.Message createUserMessage(int p0,java.lang.String p1,java.lang.String p2)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void deleteMessage(java.lang.String p0,int p1)throws javax.ejb.FinderException,javax.ejb.RemoveException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void deleteUserMessage(int p0)throws javax.ejb.FinderException,javax.ejb.RemoveException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findMessages(com.idega.user.data.User p0)throws java.lang.Exception, java.rmi.RemoteException;
 public void flagPrintedLetterAsPrinted(com.idega.user.data.User p0,se.idega.idegaweb.commune.message.data.PrintedLetterMessage p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.process.data.CaseCode getCaseCodePrintedLetterMessage()throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public com.idega.block.process.data.CaseCode getCaseCodeSystemArchivationMessage()throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public com.idega.block.process.data.CaseCode getCaseCodeUserMessage()throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public boolean getIfUserPreferesMessageByEmail(com.idega.user.data.User p0) throws java.rmi.RemoteException;
 public boolean getIfUserPreferesMessageInMessageBox(com.idega.user.data.User p0) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.Message getMessage(java.lang.String p0,int p1)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection getPrintedLetterMessages()throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getPrintedLetterMessagesByType(java.lang.String p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getPrintedLetterMessagesByType(java.lang.String p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getUnPrintedLetterMessages()throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getUnPrintedLetterMessagesByType(java.lang.String p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getUnPrintedLetterMessagesByType(java.lang.String p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.Message getUserMessage(int p0)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean isMessageRead(se.idega.idegaweb.commune.message.data.Message p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void markMessageAsRead(se.idega.idegaweb.commune.message.data.Message p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void sendMessage(java.lang.String p0,java.lang.String p1,java.lang.String p2) throws java.rmi.RemoteException;
 public void setIfUserPreferesMessageByEmail(com.idega.user.data.User p0,boolean p1) throws java.rmi.RemoteException;
 public void setIfUserPreferesMessageInMessageBox(com.idega.user.data.User p0,boolean p1) throws java.rmi.RemoteException;
}
