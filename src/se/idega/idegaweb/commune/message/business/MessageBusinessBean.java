/*
 * $Id: MessageBusinessBean.java,v 1.10 2002/09/18 10:26:08 laddi Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.message.business;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import se.idega.idegaweb.commune.message.data.Message;
import se.idega.idegaweb.commune.message.data.MessageHome;
import se.idega.idegaweb.commune.message.data.PrintedLetterMessage;
import se.idega.idegaweb.commune.message.data.UserMessage;

import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.data.CaseCode;
import com.idega.core.data.Email;
import com.idega.data.IDOCreateException;
import com.idega.data.IDOStoreException;
import com.idega.idegaweb.IWPropertyList;
import com.idega.user.business.UserBusiness;
import com.idega.user.business.UserProperties;
import com.idega.user.data.User;

/**
 * @author Anders Lindman , <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public class MessageBusinessBean extends com.idega.block.process.business.CaseBusinessBean implements MessageBusiness {

  private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";
	private String TYPE_USER_MESSAGE = "SYMEDAN";
	private String TYPE_SYSTEM_PRINT_MAIL_MESSAGE = "SYMEBRV";
	private String TYPE_SYSTEM_PRINT_ARCHIVATION_MESSAGE = "SYMEARK";

	public MessageBusinessBean() {
	}

	private MessageHome getMessageHome(String messageType) throws java.rmi.RemoteException {
//		System.out.println("Getting MessageHome for messageType = " + messageType);
		if (messageType.equals(TYPE_USER_MESSAGE)) {
			return (MessageHome) this.getIDOHome(UserMessage.class);
		}
		if (messageType.equals(TYPE_SYSTEM_PRINT_MAIL_MESSAGE)) {
			return (MessageHome) this.getIDOHome(PrintedLetterMessage.class);
		}
		else {
			throw new java.lang.UnsupportedOperationException("MessageType " + messageType + " not yet implemented");
		}
	}
	
	public void deleteMessage(String messageType, int messageID) throws FinderException,RemoveException,java.rmi.RemoteException {
		getMessageHome(messageType).findByPrimaryKey(new Integer(messageID)).remove();
	}
	
	public void deleteUserMessage(int messageID) throws FinderException,RemoveException,java.rmi.RemoteException {
		getUserMessage(messageID).remove();	
	}
	
	public void markMessageAsRead(Message message) throws RemoteException {
		message.setCaseStatus(this.getCaseStatusGranted());
		message.store();
	}
	
	public boolean isMessageRead(Message message) throws RemoteException {
		if ( (message.getCaseStatus()).equals(getCaseStatusGranted()) )
			return true;
		return false;
	}

	private String getTypeUserMessage() {
		return TYPE_USER_MESSAGE;
	}
	
	private String getTypeMailMessage() {
		return TYPE_SYSTEM_PRINT_MAIL_MESSAGE;
	}
	
	private String getTypeArchivationMessage() {
		return TYPE_SYSTEM_PRINT_ARCHIVATION_MESSAGE;
	}
	
	public CaseCode getCaseCodeSystemArchivationMessage()throws RemoteException,FinderException{
		return getCaseBusiness().getCaseCode(getTypeArchivationMessage());	
	}
	
	public CaseCode getCaseCodeUserMessage()throws RemoteException,FinderException{
		return getCaseBusiness().getCaseCode(getTypeUserMessage());	
	}
	
	public CaseCode getCaseCodePrintedLetterMessage()throws RemoteException,FinderException{
		return getCaseBusiness().getCaseCode(getTypeMailMessage());	
	}

	protected CaseBusiness getCaseBusiness(){
		return this;	
	}

	public Message getMessage(String messageType, int messageId) throws FinderException, RemoteException {
		return getMessageHome(messageType).findByPrimaryKey(new Integer(messageId));
	}

	public Message getUserMessage(int messageId) throws FinderException, RemoteException {
		return getMessage(getTypeUserMessage(), messageId);
	}

	public Collection findMessages(User user) throws Exception {
		return getMessageHome(TYPE_USER_MESSAGE).findMessages(user);
	}

	public Message createUserMessage(User user, String subject, String body) throws CreateException, RemoteException {
		Message message = null;
    IWPropertyList property = ((UserBusiness)com.idega.business.IBOLookup.getServiceInstance(getIWApplicationContext(),UserBusiness.class)).getUserProperties(user).getProperties(IW_BUNDLE_IDENTIFIER);
		boolean sendMail = true;
		boolean sendToBox = true;
		
		if ( property.getProperty(MessageBusiness.SEND_TO_EMAIL) != null )
			sendMail = new Boolean(property.getProperty(MessageBusiness.SEND_TO_EMAIL)).booleanValue();
		if ( property.getProperty(MessageBusiness.SEND_TO_MESSAGE_BOX) != null )
			sendToBox = new Boolean(property.getProperty(MessageBusiness.SEND_TO_MESSAGE_BOX)).booleanValue();
		
		if ( sendToBox )
			createMessage(getTypeUserMessage(), user, subject, body);
		if ( sendMail ) {
			Email mail = ((UserBusiness)com.idega.business.IBOLookup.getServiceInstance(getIWApplicationContext(),UserBusiness.class)).getUserMail(user);	
			if ( mail != null )
				sendMessage(mail.getEmailAddress(),subject,body);
		}
		return message;
	}

	public Message createUserMessage(int userID, String subject, String body) throws CreateException, RemoteException {
		User user;
		try {
			user = getUser(userID);
		}
		catch (FinderException fex) {
			throw new IDOCreateException(fex);
		}

		return createUserMessage(user, subject, body);
	}

	public Message createPrintedLetterMessage(User user, String subject, String body) throws CreateException, RemoteException {
		Message message = createMessage(getTypeMailMessage(), user, subject, body);
		return message;
	}

	public Message createPrintedLetterMessage(int userID, String subject, String body) throws CreateException, RemoteException {
		User user;
		try {
			user = getUser(userID);
		}
		catch (FinderException fex) {
			throw new IDOCreateException(fex);
		}

		return createPrintedLetterMessage(user, subject, body);
	}

	public Message createPrintArchivationMessage(User user, String subject, String body) throws CreateException, RemoteException {
		Message message = createMessage(getTypeArchivationMessage(), user, subject, body);
		return message;
	}

	public Message createPrintArchivationMessage(int userID, String subject, String body) throws CreateException, RemoteException {
		User user;
		try {
			user = getUser(userID);
		}
		catch (FinderException fex) {
			throw new IDOCreateException(fex);
		}

		return createPrintArchivationMessage(user, subject, body);
	}

	private Message createMessage(String messageType, int userID, String subject, String body) throws CreateException, RemoteException {
		User user;
		try {
			user = getUser(userID);
		}
		catch (FinderException fex) {
			throw new IDOCreateException(fex);
		}

		return createMessage(messageType, user, subject, body);
	}

	private Message createMessage(String messageType, User user, String subject, String body) throws CreateException, RemoteException {
		MessageHome home = this.getMessageHome(messageType);
		Message message = home.create();
		message.setOwner(user);
		message.setSubject(subject);
		message.setBody(body);
		try {
			message.store();
		}
		catch (IDOStoreException idos) {
			throw new IDOCreateException(idos);
		}
		return message;
	}

	public void sendMessage(String email, String subject, String body) {
		/**
		 * @todo: Implement better
		 */
		String mailServer = "mail.idega.is";
		String fromAddress = "messagebox@idega.com";
		try {
			com.idega.util.SendMail.send(fromAddress, email.trim(), "", "", mailServer, subject, body);
		}
		catch (javax.mail.MessagingException me) {
			System.err.println("Error sending mail to address: " + email + " Message was: " + me.getMessage());
		}
	}
}