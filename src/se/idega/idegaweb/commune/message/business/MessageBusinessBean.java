/*
 * $Id: MessageBusinessBean.java,v 1.5 2002/07/22 15:30:29 palli Exp $
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

import se.idega.idegaweb.commune.message.data.Message;
import se.idega.idegaweb.commune.message.data.MessageHome;
import se.idega.idegaweb.commune.message.data.UserMessage;

import com.idega.data.IDOCreateException;
import com.idega.data.IDOStoreException;
import com.idega.user.data.User;

/**
 * @author Anders Lindman , <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public class MessageBusinessBean extends com.idega.block.process.business.CaseBusinessBean implements MessageBusiness {

	private String TYPE_USER_MESSAGE = "SYMEDAN";
	private String TYPE_SYSTEM_PRINT_MAIL_MESSAGE = "SYMEBRV";
	private String TYPE_SYSTEM_PRINT_ARCHIVATION_MESSAGE = "SYMEARK";

	public MessageBusinessBean() {
	}

	private MessageHome getMessageHome(String messageType) throws java.rmi.RemoteException {
		if (messageType.equals(TYPE_USER_MESSAGE)) {
			return (MessageHome) this.getIDOHome(UserMessage.class);
		}
		else {
			throw new java.lang.UnsupportedOperationException("MessageType " + messageType + " not yet implemented");
		}
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

	public Message getMessage(String messageType, int messageId) throws FinderException, RemoteException {
		return getMessageHome(messageType).findByPrimaryKey(new Integer(messageId));
	}

	public Message getUserMessage(int messageId) throws FinderException, RemoteException {
		return getMessage(getTypeUserMessage(), messageId);
	}

	public Collection findMessages(int userId) throws Exception {
		//return getMessageHome().findMessages(userId);
		return null;
	}

	public Message createUserMessage(User user, String subject, String body) throws CreateException, RemoteException {
		Message message = createMessage(getTypeUserMessage(), user, subject, body);
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
		try {
			com.idega.util.SendMail.send("idegaWeb MessageBox", email, "", "", mailServer, subject, body);
		}
		catch (javax.mail.MessagingException me) {
			System.err.println("Error sending mail to address: " + email + " Message was: " + me.getMessage());
		}
	}
}