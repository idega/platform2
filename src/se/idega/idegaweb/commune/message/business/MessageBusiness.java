package se.idega.idegaweb.commune.message.business;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.*;

import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.data.Case;
import com.idega.core.component.data.ICObject;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

import se.idega.idegaweb.commune.message.data.Message;
import se.idega.idegaweb.commune.message.data.MessageHandlerInfo;
import se.idega.idegaweb.commune.message.data.PrintedLetterMessage;

public interface MessageBusiness extends CaseBusiness
{
 public MessageHandlerInfo createMessageHandlerInfo(MessagePdfHandler handler,ICObject ico) throws CreateException, RemoteException;
 public PrintedLetterMessage createPasswordMessage(com.idega.user.data.User user, String username,String password) throws CreateException, java.rmi.RemoteException ;
 public se.idega.idegaweb.commune.message.data.SystemArchivationMessage createPrintArchivationMessage(com.idega.user.data.User p0,com.idega.user.data.User p1,java.lang.String p2,java.lang.String p3,com.idega.core.file.data.ICFile p4)throws javax.ejb.CreateException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.Message createPrintArchivationMessage(com.idega.user.data.User p0,java.lang.String p1,java.lang.String p2)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.SystemArchivationMessage createPrintArchivationMessage(int p0,int p1,java.lang.String p2,java.lang.String p3,int p4)throws javax.ejb.CreateException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.Message createPrintArchivationMessage(int p0,java.lang.String p1,java.lang.String p2)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.Message createPrintedLetterMessage(com.idega.user.data.User p0,java.lang.String p1,java.lang.String p2)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.PrintedLetterMessage createPrintedLetterMessage(com.idega.user.data.User p0,java.lang.String p1,java.lang.String p2,java.lang.String p3)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.Message createPrintedLetterMessage(int p0,java.lang.String p1,java.lang.String p2)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.PrintedLetterMessage createPrintedPasswordLetterMessage(com.idega.user.data.User p0,java.lang.String p1,java.lang.String p2)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.Message createUserMessage(Case parentCase, User user, String subject, String body, boolean sendLetter);
 public se.idega.idegaweb.commune.message.data.Message createUserMessage(Case parentCase, User user, String subject, String body, boolean sendLetter, boolean alwaysSendLetter);
 public se.idega.idegaweb.commune.message.data.Message createUserMessage(Case parentCase, User user, String subject, String body, String letterBody, boolean sendLetter, boolean alwaysSendLetter);
 public se.idega.idegaweb.commune.message.data.Message createUserMessage(Case parentCase, User receiver, User sender, com.idega.user.data.Group handler, String subject, String body, boolean sendLetter);
 public se.idega.idegaweb.commune.message.data.Message createUserMessage(Case parentCase, User receiver, User sender, Group handler, String subject, String body, boolean sendLetter,String contentCode) throws RemoteException;
 public se.idega.idegaweb.commune.message.data.Message createUserMessage(Case parentCase, User receiver, User sender, Group handler, String subject, String body, boolean sendLetter,String contentCode, boolean alwaysSendLetter) throws RemoteException;
 public se.idega.idegaweb.commune.message.data.Message createUserMessage(Case parentCase, User receiver, User sender, Group handler, String subject, String body, String letterBody, boolean sendLetter,String contentCode, boolean alwaysSendLetter) throws RemoteException;
 public se.idega.idegaweb.commune.message.data.Message createUserMessage(Case parentCase, User receiver, User sender, Group handler, String subject, String body, String letterBody, boolean sendLetter,String contentCode, boolean alwaysSendLetter, boolean sendEMail) throws RemoteException;
 public se.idega.idegaweb.commune.message.data.Message createUserMessage(Case parentCase, User receiver, User sender, String subject, String body, boolean sendLetter);
 public se.idega.idegaweb.commune.message.data.Message createUserMessage(com.idega.user.data.User p0,java.lang.String p1,java.lang.String p2) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.Message createUserMessage(int p0,java.lang.String p1,java.lang.String p2)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.Message createUserMessage(User user, String subject, com.idega.user.data.Group handler, String body, boolean sendLetter); 
 public se.idega.idegaweb.commune.message.data.Message createUserMessage(User user, String subject, Group handler, String body, boolean sendLetter,String contentCode) throws RemoteException;
 public se.idega.idegaweb.commune.message.data.Message createUserMessage(User user, String subject, String body, boolean sendLetter);
 public se.idega.idegaweb.commune.message.data.Message createUserMessage(User receiver, String subject, String body, User sender, boolean sendLetter);
 public void deleteMessage(java.lang.String p0,int p1)throws javax.ejb.FinderException,javax.ejb.RemoveException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void deleteUserMessage(int p0)throws javax.ejb.FinderException,javax.ejb.RemoveException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findMessages(com.idega.user.data.Group p0)throws java.lang.Exception, java.rmi.RemoteException;
 public java.util.Collection findMessages(com.idega.user.data.Group p0, int p1, int p2)throws java.lang.Exception, java.rmi.RemoteException;
 public java.util.Collection findMessages(com.idega.user.data.User p0)throws java.lang.Exception, java.rmi.RemoteException;
 public java.util.Collection findMessages(com.idega.user.data.User p0, Collection p1, int p2, int p3)throws java.lang.Exception, java.rmi.RemoteException;
 public java.util.Collection findMessages(com.idega.user.data.User p0, int p1, int p2)throws java.lang.Exception, java.rmi.RemoteException;
 public void flagMessageAsInactive(User performer,Message message)throws RemoteException;
 public void flagMessageAsPrinted(com.idega.user.data.User performer,se.idega.idegaweb.commune.message.data.Message message)throws  java.rmi.RemoteException;
 public void flagMessageAsUnPrinted(com.idega.user.data.User performer,se.idega.idegaweb.commune.message.data.Message message)throws  java.rmi.RemoteException;
 public void flagMessagesAsInactive(User performer, String[] msgKeys)throws RemoteException,FinderException;
 public void flagMessagesWithStatus(User performer, String[] msgKeys,String status)throws RemoteException,FinderException;
 public void flagMessageWithStatus(User performer,Message message,String status)throws RemoteException;  
 public void flagPrintedLetterAsPrinted(com.idega.user.data.User p0,se.idega.idegaweb.commune.message.data.PrintedLetterMessage p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.process.data.CaseCode getCaseCodePrintedLetterMessage()throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public com.idega.block.process.data.CaseCode getCaseCodeSystemArchivationMessage()throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public com.idega.block.process.data.CaseCode getCaseCodeUserMessage()throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public boolean getIfCanSendEmail();
 public boolean getIfUserPreferesMessageByEmail(com.idega.user.data.User p0) throws java.rmi.RemoteException;
 public boolean getIfUserPreferesMessageInMessageBox(com.idega.user.data.User p0) throws java.rmi.RemoteException;
 public Collection getLettersByBulkFile(int file, String type , String status)throws FinderException;
 public se.idega.idegaweb.commune.message.data.Message getMessage(java.lang.String p0,int p1)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public int getNumberOfMessages(User user) throws Exception;
 public int getNumberOfNewMessages(User user) throws com.idega.data.IDOException;
 public int getNumberOfMessages(User user,Collection groups) throws Exception;
 public java.util.Collection getPrintedLetterMessages()throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getPrintedLetterMessagesByType(java.lang.String p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getPrintedLetterMessagesByType(java.lang.String p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public Collection getSingleLettersByTypeAndStatus(String type,String status,IWTimestamp from,IWTimestamp to)throws FinderException;
 public java.util.Collection getSinglePrintedLetterMessagesByType(java.lang.String p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getSingleUnPrintedLetterMessagesByType(java.lang.String p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getUnPrintedLetterMessages()throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getUnPrintedLetterMessagesByType(java.lang.String p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getUnPrintedLetterMessagesByType(java.lang.String p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.Message getUserMessage(int p0)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean isMessageRead(se.idega.idegaweb.commune.message.data.Message p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void markMessageAsRead(se.idega.idegaweb.commune.message.data.Message p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void sendMessage(java.lang.String p0,java.lang.String p1,java.lang.String p2) throws java.rmi.RemoteException;
 public void sendMessage(java.lang.String p0,java.lang.String p1,java.lang.String p2, java.io.File p3) throws java.rmi.RemoteException;
 public void sendMessageToCommuneAdministrators(Case theCase, String subject, String body) throws RemoteException;
 public void sendMessageToCommuneAdministrators(String subject, String body) throws java.rmi.RemoteException;
 public void setIfUserPreferesMessageByEmail(com.idega.user.data.User p0,boolean p1) throws java.rmi.RemoteException;
 public void setIfUserPreferesMessageInMessageBox(com.idega.user.data.User p0,boolean p1) throws java.rmi.RemoteException;
}
