package se.idega.idegaweb.commune.message.business;

import java.rmi.RemoteException;
import javax.ejb.*;

import se.idega.idegaweb.commune.message.data.PrintedLetterMessage;
import se.idega.idegaweb.commune.message.data.SystemArchivationMessage;

import se.idega.idegaweb.commune.message.data.Message;
import com.idega.core.data.ICFile;
import com.idega.user.data.User;

import java.util.Collection;

public interface MessageBusiness extends com.idega.business.IBOService
{
  //public static final String SEND_TO_MESSAGE_BOX = "msg_send_box";
  //public static final String SEND_TO_EMAIL = "msg_send_email";

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

	/**
	 * @return Collection of PrintedLetterMessage that have already been printed
	 */
	public Collection getPrintedLetterMessages()throws FinderException,RemoteException;
	public Collection getPrintedLetterMessagesByType(String type)throws FinderException,RemoteException;
	/**
	 * @return Collection of PrintedLetterMessage that have already been printed
	 */	
	public Collection getUnPrintedLetterMessages()throws FinderException,RemoteException;
	public Collection getUnPrintedLetterMessagesByType(String type)throws FinderException,RemoteException;
	/**
	 * Mark the status of the message so that it is printed.
	 * @param performer The User that makes the change
	 * @param message the message to be marked
	 */
	public void flagPrintedLetterAsPrinted(User performer,PrintedLetterMessage message)throws RemoteException;


	public boolean getIfUserPreferesMessageByEmail(User user)throws RemoteException;
	public boolean getIfUserPreferesMessageInMessageBox(User user)throws RemoteException;
	public void setIfUserPreferesMessageByEmail(User user,boolean preference)throws RemoteException;
	public void setIfUserPreferesMessageInMessageBox(User user,boolean preference)throws RemoteException;

	public SystemArchivationMessage createPrintArchivationMessage(User forUser, User creator, String subject, String body,ICFile attatchement) throws CreateException, RemoteException;
	public SystemArchivationMessage createPrintArchivationMessage(int forUserID, int creatorUserID, String subject, String body,int attatchementFileID) throws CreateException, RemoteException;
	public PrintedLetterMessage createPrintedPasswordLetterMessage(User forUser, String subject, String body) throws CreateException, RemoteException;

}
